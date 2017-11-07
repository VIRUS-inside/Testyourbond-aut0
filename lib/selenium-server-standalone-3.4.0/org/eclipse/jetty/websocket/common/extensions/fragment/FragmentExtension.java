package org.eclipse.jetty.websocket.common.extensions.fragment;

import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.Queue;
import org.eclipse.jetty.util.IteratingCallback;
import org.eclipse.jetty.util.IteratingCallback.Action;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.websocket.api.BatchMode;
import org.eclipse.jetty.websocket.api.WriteCallback;
import org.eclipse.jetty.websocket.api.extensions.ExtensionConfig;
import org.eclipse.jetty.websocket.api.extensions.Frame;
import org.eclipse.jetty.websocket.api.extensions.Frame.Type;
import org.eclipse.jetty.websocket.common.OpCode;
import org.eclipse.jetty.websocket.common.extensions.AbstractExtension;
import org.eclipse.jetty.websocket.common.frames.DataFrame;





















public class FragmentExtension
  extends AbstractExtension
{
  private static final Logger LOG = Log.getLogger(FragmentExtension.class);
  
  private final Queue<FrameEntry> entries = new ArrayDeque();
  private final IteratingCallback flusher = new Flusher(null);
  private int maxLength;
  
  public FragmentExtension() {}
  
  public String getName() {
    return "fragment";
  }
  

  public void incomingFrame(Frame frame)
  {
    nextIncomingFrame(frame);
  }
  

  public void outgoingFrame(Frame frame, WriteCallback callback, BatchMode batchMode)
  {
    ByteBuffer payload = frame.getPayload();
    int length = payload != null ? payload.remaining() : 0;
    if ((OpCode.isControlFrame(frame.getOpCode())) || (maxLength <= 0) || (length <= maxLength))
    {
      nextOutgoingFrame(frame, callback, batchMode);
      return;
    }
    
    FrameEntry entry = new FrameEntry(frame, callback, batchMode, null);
    if (LOG.isDebugEnabled())
      LOG.debug("Queuing {}", new Object[] { entry });
    offerEntry(entry);
    flusher.iterate();
  }
  

  public void setConfig(ExtensionConfig config)
  {
    super.setConfig(config);
    maxLength = config.getParameter("maxLength", -1);
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
    private FragmentExtension.FrameEntry current;
    private boolean finished = true;
    
    private Flusher() {}
    
    protected IteratingCallback.Action process() throws Exception {
      if (finished)
      {
        current = FragmentExtension.this.pollEntry();
        FragmentExtension.LOG.debug("Processing {}", new Object[] { current });
        if (current == null)
          return IteratingCallback.Action.IDLE;
        fragment(current, true);
      }
      else
      {
        fragment(current, false);
      }
      return IteratingCallback.Action.SCHEDULED;
    }
    
    private void fragment(FragmentExtension.FrameEntry entry, boolean first)
    {
      Frame frame = FragmentExtension.FrameEntry.access$400(entry);
      ByteBuffer payload = frame.getPayload();
      int remaining = payload.remaining();
      int length = Math.min(remaining, maxLength);
      finished = (length == remaining);
      
      boolean continuation = (frame.getType().isContinuation()) || (!first);
      DataFrame fragment = new DataFrame(frame, continuation);
      boolean fin = (frame.isFin()) && (finished);
      fragment.setFin(fin);
      
      int limit = payload.limit();
      int newLimit = payload.position() + length;
      payload.limit(newLimit);
      ByteBuffer payloadFragment = payload.slice();
      payload.limit(limit);
      fragment.setPayload(payloadFragment);
      if (FragmentExtension.LOG.isDebugEnabled())
        FragmentExtension.LOG.debug("Fragmented {}->{}", new Object[] { frame, fragment });
      payload.position(newLimit);
      
      nextOutgoingFrame(fragment, this, FragmentExtension.FrameEntry.access$600(entry));
    }
    





    protected void onCompleteSuccess() {}
    




    protected void onCompleteFailure(Throwable x) {}
    




    public void writeSuccess()
    {
      notifyCallbackSuccess(FragmentExtension.FrameEntry.access$800(current));
      succeeded();
    }
    






    public void writeFailed(Throwable x)
    {
      notifyCallbackFailure(FragmentExtension.FrameEntry.access$800(current), x);
      succeeded();
    }
    
    private void notifyCallbackSuccess(WriteCallback callback)
    {
      try
      {
        if (callback != null) {
          callback.writeSuccess();
        }
      }
      catch (Throwable x) {
        if (FragmentExtension.LOG.isDebugEnabled()) {
          FragmentExtension.LOG.debug("Exception while notifying success of callback " + callback, x);
        }
      }
    }
    
    private void notifyCallbackFailure(WriteCallback callback, Throwable failure)
    {
      try {
        if (callback != null) {
          callback.writeFailed(failure);
        }
      }
      catch (Throwable x) {
        if (FragmentExtension.LOG.isDebugEnabled()) {
          FragmentExtension.LOG.debug("Exception while notifying failure of callback " + callback, x);
        }
      }
    }
  }
}
