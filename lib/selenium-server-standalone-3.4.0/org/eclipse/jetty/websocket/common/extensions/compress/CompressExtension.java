package org.eclipse.jetty.websocket.common.extensions.compress;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import java.util.zip.ZipException;
import org.eclipse.jetty.io.ByteBufferPool;
import org.eclipse.jetty.util.BufferUtil;
import org.eclipse.jetty.util.IteratingCallback;
import org.eclipse.jetty.util.IteratingCallback.Action;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.websocket.api.BatchMode;
import org.eclipse.jetty.websocket.api.WebSocketPolicy;
import org.eclipse.jetty.websocket.api.WriteCallback;
import org.eclipse.jetty.websocket.api.extensions.Frame;
import org.eclipse.jetty.websocket.api.extensions.Frame.Type;
import org.eclipse.jetty.websocket.common.OpCode;
import org.eclipse.jetty.websocket.common.extensions.AbstractExtension;
import org.eclipse.jetty.websocket.common.frames.DataFrame;















public abstract class CompressExtension
  extends AbstractExtension
{
  protected static final byte[] TAIL_BYTES = { 0, 0, -1, -1 };
  protected static final ByteBuffer TAIL_BYTES_BUF = ByteBuffer.wrap(TAIL_BYTES);
  private static final Logger LOG = Log.getLogger(CompressExtension.class);
  

  protected static final int TAIL_DROP_NEVER = 0;
  

  protected static final int TAIL_DROP_ALWAYS = 1;
  

  protected static final int TAIL_DROP_FIN_ONLY = 2;
  

  protected static final int RSV_USE_ALWAYS = 0;
  

  protected static final int RSV_USE_ONLY_FIRST = 1;
  

  protected static final int INFLATE_BUFFER_SIZE = 8192;
  

  protected static final int INPUT_MAX_BUFFER_SIZE = 8192;
  

  private static final int DECOMPRESS_BUF_SIZE = 8192;
  

  private static final boolean NOWRAP = true;
  
  private final Queue<FrameEntry> entries = new ArrayDeque();
  private final IteratingCallback flusher = new Flusher(null);
  private Deflater deflaterImpl;
  private Inflater inflaterImpl;
  protected AtomicInteger decompressCount = new AtomicInteger(0);
  private int tailDrop = 0;
  private int rsvUse = 0;
  
  protected CompressExtension()
  {
    tailDrop = getTailDropMode();
    rsvUse = getRsvUseMode();
  }
  
  public Deflater getDeflater()
  {
    if (deflaterImpl == null)
    {
      deflaterImpl = new Deflater(-1, true);
    }
    return deflaterImpl;
  }
  
  public Inflater getInflater()
  {
    if (inflaterImpl == null)
    {
      inflaterImpl = new Inflater(true);
    }
    return inflaterImpl;
  }
  




  public boolean isRsv1User()
  {
    return true;
  }
  




  abstract int getTailDropMode();
  



  abstract int getRsvUseMode();
  



  protected void forwardIncoming(Frame frame, ByteAccumulator accumulator)
  {
    DataFrame newFrame = new DataFrame(frame);
    
    newFrame.setRsv1(false);
    
    ByteBuffer buffer = getBufferPool().acquire(accumulator.getLength(), false);
    try
    {
      BufferUtil.flipToFill(buffer);
      accumulator.transferTo(buffer);
      newFrame.setPayload(buffer);
      nextIncomingFrame(newFrame);
    }
    finally
    {
      getBufferPool().release(buffer);
    }
  }
  
  protected ByteAccumulator newByteAccumulator()
  {
    int maxSize = Math.max(getPolicy().getMaxTextMessageSize(), getPolicy().getMaxBinaryMessageBufferSize());
    return new ByteAccumulator(maxSize);
  }
  
  protected void decompress(ByteAccumulator accumulator, ByteBuffer buf) throws DataFormatException
  {
    if ((buf == null) || (!buf.hasRemaining()))
    {
      return;
    }
    byte[] output = new byte[' '];
    
    Inflater inflater = getInflater();
    
    while ((buf.hasRemaining()) && (inflater.needsInput()))
    {
      if (!supplyInput(inflater, buf))
      {
        LOG.debug("Needed input, but no buffer could supply input", new Object[0]); return;
      }
      
      int read;
      
      while ((read = inflater.inflate(output)) >= 0)
      {
        if (read == 0)
        {
          LOG.debug("Decompress: read 0 {}", new Object[] { toDetail(inflater) });
          break;
        }
        


        if (LOG.isDebugEnabled())
        {
          LOG.debug("Decompressed {} bytes: {}", new Object[] { Integer.valueOf(read), toDetail(inflater) });
        }
        
        accumulator.copyChunk(output, 0, read);
      }
    }
    

    if (LOG.isDebugEnabled())
    {
      LOG.debug("Decompress: exiting {}", new Object[] { toDetail(inflater) });
    }
  }
  





  public void outgoingFrame(Frame frame, WriteCallback callback, BatchMode batchMode)
  {
    if (flusher.isFailed())
    {
      notifyCallbackFailure(callback, new ZipException());
      return;
    }
    
    FrameEntry entry = new FrameEntry(frame, callback, batchMode, null);
    if (LOG.isDebugEnabled())
      LOG.debug("Queuing {}", new Object[] { entry });
    offerEntry(entry);
    flusher.iterate();
  }
  
  private void offerEntry(FrameEntry entry)
  {
    synchronized (this)
    {
      entries.offer(entry);
    }
  }
  
  private FrameEntry pollEntry()
  {
    synchronized (this)
    {
      return (FrameEntry)entries.poll();
    }
  }
  
  protected void notifyCallbackSuccess(WriteCallback callback)
  {
    try
    {
      if (callback != null) {
        callback.writeSuccess();
      }
    }
    catch (Throwable x) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Exception while notifying success of callback " + callback, x);
      }
    }
  }
  
  protected void notifyCallbackFailure(WriteCallback callback, Throwable failure)
  {
    try {
      if (callback != null) {
        callback.writeFailed(failure);
      }
    }
    catch (Throwable x) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Exception while notifying failure of callback " + callback, x);
      }
    }
  }
  
  private static boolean supplyInput(Inflater inflater, ByteBuffer buf) {
    if (buf.remaining() <= 0)
    {
      if (LOG.isDebugEnabled())
      {
        LOG.debug("No data left left to supply to Inflater", new Object[0]);
      }
      return false;
    }
    
    int len;
    
    byte[] input;
    int inputOffset;
    if (buf.hasArray())
    {

      int len = buf.remaining();
      byte[] input = buf.array();
      int inputOffset = buf.position() + buf.arrayOffset();
      buf.position(buf.position() + len);

    }
    else
    {
      len = Math.min(8192, buf.remaining());
      input = new byte[len];
      inputOffset = 0;
      buf.get(input, 0, len);
    }
    
    inflater.setInput(input, inputOffset, len);
    if (LOG.isDebugEnabled())
    {
      LOG.debug("Supplied {} input bytes: {}", new Object[] { Integer.valueOf(input.length), toDetail(inflater) });
    }
    return true;
  }
  
  private static boolean supplyInput(Deflater deflater, ByteBuffer buf)
  {
    if (buf.remaining() <= 0)
    {
      if (LOG.isDebugEnabled())
      {
        LOG.debug("No data left left to supply to Deflater", new Object[0]);
      }
      return false;
    }
    
    int len;
    
    byte[] input;
    int inputOffset;
    if (buf.hasArray())
    {

      int len = buf.remaining();
      byte[] input = buf.array();
      int inputOffset = buf.position() + buf.arrayOffset();
      buf.position(buf.position() + len);

    }
    else
    {
      len = Math.min(8192, buf.remaining());
      input = new byte[len];
      inputOffset = 0;
      buf.get(input, 0, len);
    }
    
    deflater.setInput(input, inputOffset, len);
    if (LOG.isDebugEnabled())
    {
      LOG.debug("Supplied {} input bytes: {}", new Object[] { Integer.valueOf(input.length), toDetail(deflater) });
    }
    return true;
  }
  
  private static String toDetail(Inflater inflater)
  {
    return String.format("Inflater[finished=%b,read=%d,written=%d,remaining=%d,in=%d,out=%d]", new Object[] { Boolean.valueOf(inflater.finished()), Long.valueOf(inflater.getBytesRead()), 
      Long.valueOf(inflater.getBytesWritten()), Integer.valueOf(inflater.getRemaining()), Integer.valueOf(inflater.getTotalIn()), Integer.valueOf(inflater.getTotalOut()) });
  }
  
  private static String toDetail(Deflater deflater)
  {
    return String.format("Deflater[finished=%b,read=%d,written=%d,in=%d,out=%d]", new Object[] { Boolean.valueOf(deflater.finished()), Long.valueOf(deflater.getBytesRead()), Long.valueOf(deflater.getBytesWritten()), 
      Integer.valueOf(deflater.getTotalIn()), Integer.valueOf(deflater.getTotalOut()) });
  }
  
  public static boolean endsWithTail(ByteBuffer buf)
  {
    if ((buf == null) || (buf.remaining() < TAIL_BYTES.length))
    {
      return false;
    }
    int limit = buf.limit();
    for (int i = TAIL_BYTES.length; i > 0; i--)
    {
      if (buf.get(limit - i) != TAIL_BYTES[(TAIL_BYTES.length - i)])
      {
        return false;
      }
    }
    return true;
  }
  
  protected void doStop()
    throws Exception
  {
    if (deflaterImpl != null)
      deflaterImpl.end();
    if (inflaterImpl != null)
      inflaterImpl.end();
    super.doStop();
  }
  

  public String toString()
  {
    return getClass().getSimpleName();
  }
  
  private static class FrameEntry
  {
    private final Frame frame;
    private final WriteCallback callback;
    private final BatchMode batchMode;
    
    private FrameEntry(Frame frame, WriteCallback callback, BatchMode batchMode)
    {
      this.frame = frame;
      this.callback = callback;
      this.batchMode = batchMode;
    }
    

    public String toString()
    {
      return frame.toString();
    }
  }
  
  private class Flusher extends IteratingCallback implements WriteCallback
  {
    private CompressExtension.FrameEntry current;
    private boolean finished = true;
    
    private Flusher() {}
    
    public void failed(Throwable x) {
      CompressExtension.LOG.warn(x);
      super.failed(x);
    }
    
    protected IteratingCallback.Action process()
      throws Exception
    {
      if (finished)
      {
        current = CompressExtension.this.pollEntry();
        CompressExtension.LOG.debug("Processing {}", new Object[] { current });
        if (current == null)
          return IteratingCallback.Action.IDLE;
        deflate(current);
      }
      else
      {
        compress(current, false);
      }
      return IteratingCallback.Action.SCHEDULED;
    }
    
    private void deflate(CompressExtension.FrameEntry entry)
    {
      Frame frame = CompressExtension.FrameEntry.access$400(entry);
      BatchMode batchMode = CompressExtension.FrameEntry.access$500(entry);
      if (OpCode.isControlFrame(frame.getOpCode()))
      {

        nextOutgoingFrame(frame, this, batchMode);
        return;
      }
      
      compress(entry, true);
    }
    


    private void compress(CompressExtension.FrameEntry entry, boolean first)
    {
      Frame frame = CompressExtension.FrameEntry.access$400(entry);
      ByteBuffer data = frame.getPayload();
      int remaining = data.remaining();
      int outputLength = Math.max(256, data.remaining());
      if (CompressExtension.LOG.isDebugEnabled()) {
        CompressExtension.LOG.debug("Compressing {}: {} bytes in {} bytes chunk", new Object[] { entry, Integer.valueOf(remaining), Integer.valueOf(outputLength) });
      }
      boolean needsCompress = true;
      
      Deflater deflater = getDeflater();
      
      if ((deflater.needsInput()) && (!CompressExtension.supplyInput(deflater, data)))
      {

        needsCompress = false;
      }
      
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      
      byte[] output = new byte[outputLength];
      
      boolean fin = frame.isFin();
      

      while (needsCompress)
      {
        int compressed = deflater.deflate(output, 0, outputLength, 2);
        

        if (CompressExtension.LOG.isDebugEnabled())
          CompressExtension.LOG.debug("Wrote {} bytes to output buffer", compressed);
        out.write(output, 0, compressed);
        
        if (compressed < outputLength)
        {
          needsCompress = false;
        }
      }
      
      ByteBuffer payload = ByteBuffer.wrap(out.toByteArray());
      
      if (payload.remaining() > 0)
      {

        if (CompressExtension.LOG.isDebugEnabled()) {
          CompressExtension.LOG.debug("compressed bytes[] = {}", new Object[] { BufferUtil.toDetailString(payload) });
        }
        if (tailDrop == 1)
        {
          if (CompressExtension.endsWithTail(payload))
          {
            payload.limit(payload.limit() - CompressExtension.TAIL_BYTES.length);
          }
          if (CompressExtension.LOG.isDebugEnabled()) {
            CompressExtension.LOG.debug("payload (TAIL_DROP_ALWAYS) = {}", new Object[] { BufferUtil.toDetailString(payload) });
          }
        } else if (tailDrop == 2)
        {
          if ((frame.isFin()) && (CompressExtension.endsWithTail(payload)))
          {
            payload.limit(payload.limit() - CompressExtension.TAIL_BYTES.length);
          }
          if (CompressExtension.LOG.isDebugEnabled()) {
            CompressExtension.LOG.debug("payload (TAIL_DROP_FIN_ONLY) = {}", new Object[] { BufferUtil.toDetailString(payload) });
          }
        }
      } else if (fin)
      {


        payload = ByteBuffer.wrap(new byte[] { 0 });
      }
      
      if (CompressExtension.LOG.isDebugEnabled())
      {
        CompressExtension.LOG.debug("Compressed {}: input:{} -> payload:{}", new Object[] { entry, Integer.valueOf(outputLength), Integer.valueOf(payload.remaining()) });
      }
      
      boolean continuation = (frame.getType().isContinuation()) || (!first);
      DataFrame chunk = new DataFrame(frame, continuation);
      if (rsvUse == 1)
      {
        chunk.setRsv1(!continuation);

      }
      else
      {
        chunk.setRsv1(true);
      }
      chunk.setPayload(payload);
      chunk.setFin(fin);
      
      nextOutgoingFrame(chunk, this, CompressExtension.FrameEntry.access$500(entry));
    }
    


    protected void onCompleteSuccess() {}
    


    protected void onCompleteFailure(Throwable x)
    {
      CompressExtension.FrameEntry entry;
      

      while ((entry = CompressExtension.this.pollEntry()) != null) {
        notifyCallbackFailure(CompressExtension.FrameEntry.access$1100(entry), x);
      }
    }
    
    public void writeSuccess()
    {
      if (finished)
        notifyCallbackSuccess(CompressExtension.FrameEntry.access$1100(current));
      succeeded();
    }
    

    public void writeFailed(Throwable x)
    {
      notifyCallbackFailure(CompressExtension.FrameEntry.access$1100(current), x);
      

      failed(x);
    }
  }
}
