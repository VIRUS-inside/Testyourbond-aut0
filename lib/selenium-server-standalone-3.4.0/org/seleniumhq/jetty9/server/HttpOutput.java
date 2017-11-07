package org.seleniumhq.jetty9.server;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritePendingException;
import java.util.concurrent.atomic.AtomicReference;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import org.seleniumhq.jetty9.http.HttpContent;
import org.seleniumhq.jetty9.io.ByteBufferPool;
import org.seleniumhq.jetty9.io.EofException;
import org.seleniumhq.jetty9.util.BufferUtil;
import org.seleniumhq.jetty9.util.Callback;
import org.seleniumhq.jetty9.util.Callback.Nested;
import org.seleniumhq.jetty9.util.IteratingCallback;
import org.seleniumhq.jetty9.util.IteratingCallback.Action;
import org.seleniumhq.jetty9.util.IteratingNestedCallback;
import org.seleniumhq.jetty9.util.SharedBlockingCallback;
import org.seleniumhq.jetty9.util.SharedBlockingCallback.Blocker;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;










































































public class HttpOutput
  extends ServletOutputStream
  implements Runnable
{
  public static abstract interface Interceptor
  {
    public abstract void write(ByteBuffer paramByteBuffer, boolean paramBoolean, Callback paramCallback);
    
    public abstract Interceptor getNextInterceptor();
    
    public abstract boolean isOptimizedForDirectBuffers();
    
    public void resetBuffer()
      throws IllegalStateException
    {
      Interceptor next = getNextInterceptor();
      if (next != null)
        next.resetBuffer();
    }
  }
  
  private static Logger LOG = Log.getLogger(HttpOutput.class);
  

  private final HttpChannel _channel;
  

  private final SharedBlockingCallback _writeBlocker;
  

  private Interceptor _interceptor;
  

  private long _written;
  

  private ByteBuffer _aggregate;
  
  private int _bufferSize;
  
  private int _commitSize;
  
  private WriteListener _writeListener;
  
  private volatile Throwable _onError;
  

  private static enum OutputState
  {
    OPEN,  ASYNC,  READY,  PENDING,  UNREADY,  ERROR,  CLOSED;
    
    private OutputState() {} }
  private final AtomicReference<OutputState> _state = new AtomicReference(OutputState.OPEN);
  
  public HttpOutput(HttpChannel channel)
  {
    _channel = channel;
    _interceptor = channel;
    _writeBlocker = new WriteBlocker(channel, null);
    HttpConfiguration config = channel.getHttpConfiguration();
    _bufferSize = config.getOutputBufferSize();
    _commitSize = config.getOutputAggregationSize();
    if (_commitSize > _bufferSize)
    {
      LOG.warn("OutputAggregationSize {} exceeds bufferSize {}", new Object[] { Integer.valueOf(_commitSize), Integer.valueOf(_bufferSize) });
      _commitSize = _bufferSize;
    }
  }
  
  public HttpChannel getHttpChannel()
  {
    return _channel;
  }
  
  public Interceptor getInterceptor()
  {
    return _interceptor;
  }
  
  public void setInterceptor(Interceptor interceptor)
  {
    _interceptor = interceptor;
  }
  
  public boolean isWritten()
  {
    return _written > 0L;
  }
  
  public long getWritten()
  {
    return _written;
  }
  
  public void reopen()
  {
    _state.set(OutputState.OPEN);
  }
  
  private boolean isLastContentToWrite(int len)
  {
    _written += len;
    return _channel.getResponse().isAllContentWritten(_written);
  }
  
  public boolean isAllContentWritten()
  {
    return _channel.getResponse().isAllContentWritten(_written);
  }
  
  protected SharedBlockingCallback.Blocker acquireWriteBlockingCallback() throws IOException
  {
    return _writeBlocker.acquire();
  }
  
  private void write(ByteBuffer content, boolean complete) throws IOException {
    try {
      SharedBlockingCallback.Blocker blocker = _writeBlocker.acquire();Throwable localThrowable3 = null;
      try {
        write(content, complete, blocker);
        blocker.block();
      }
      catch (Throwable localThrowable1)
      {
        localThrowable3 = localThrowable1;throw localThrowable1;
      }
      finally
      {
        if (blocker != null) if (localThrowable3 != null) try { blocker.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else blocker.close();
      }
    } catch (Exception failure) {
      if (LOG.isDebugEnabled())
        LOG.debug(failure);
      abort(failure);
      if ((failure instanceof IOException))
        throw failure;
      throw new IOException(failure);
    }
  }
  
  protected void write(ByteBuffer content, boolean complete, Callback callback)
  {
    _interceptor.write(content, complete, callback);
  }
  
  private void abort(Throwable failure)
  {
    closed();
    _channel.abort(failure);
  }
  

  public void close()
  {
    for (;;)
    {
      OutputState state = (OutputState)_state.get();
      switch (2.$SwitchMap$org$eclipse$jetty$server$HttpOutput$OutputState[state.ordinal()])
      {

      case 1: 
        return;
      






      case 2: 
        if (_state.compareAndSet(state, OutputState.READY)) {}
        break; case 3: case 4:  if ((goto 0) && 
        










          (_state.compareAndSet(state, OutputState.CLOSED)))
        {
          IOException ex = new IOException("Closed while Pending/Unready");
          LOG.warn(ex.toString(), new Object[0]);
          LOG.debug(ex);
          _channel.abort(ex);
          return;
        }
        break;
      default: 
        if (_state.compareAndSet(state, OutputState.CLOSED))
        {


          try
          {

            write(BufferUtil.hasContent(_aggregate) ? _aggregate : BufferUtil.EMPTY_BUFFER, !_channel.getResponse().isIncluding());
          }
          catch (IOException x)
          {
            LOG.ignore(x);
          }
          finally
          {
            releaseBuffer();
          }
          
          return;
        }
        
        break;
      }
      
    }
  }
  

  void closed()
  {
    for (;;)
    {
      OutputState state = (OutputState)_state.get();
      switch (2.$SwitchMap$org$eclipse$jetty$server$HttpOutput$OutputState[state.ordinal()])
      {

      case 1: 
        return;
      

      case 3: 
        if (_state.compareAndSet(state, OutputState.ERROR)) {
          _writeListener.onError(_onError == null ? new EofException("Async closed") : _onError);
        }
        
        break;
      default: 
        if (_state.compareAndSet(state, OutputState.CLOSED))
        {

          try
          {
            _channel.getResponse().closeOutput();
          }
          catch (Throwable x)
          {
            if (LOG.isDebugEnabled())
              LOG.debug(x);
            abort(x);
          }
          finally
          {
            releaseBuffer();
          }
          
          return;
        }
        break; }
      
    }
  }
  
  private void releaseBuffer() {
    if (_aggregate != null)
    {
      _channel.getConnector().getByteBufferPool().release(_aggregate);
      _aggregate = null;
    }
  }
  
  public boolean isClosed()
  {
    return _state.get() == OutputState.CLOSED;
  }
  
  public boolean isAsync()
  {
    switch (2.$SwitchMap$org$eclipse$jetty$server$HttpOutput$OutputState[((OutputState)_state.get()).ordinal()])
    {
    case 2: 
    case 3: 
    case 4: 
    case 5: 
      return true;
    }
    return false;
  }
  

  public void flush()
    throws IOException
  {
    do
    {
      switch (2.$SwitchMap$org$eclipse$jetty$server$HttpOutput$OutputState[((OutputState)_state.get()).ordinal()])
      {
      case 6: 
        write(BufferUtil.hasContent(_aggregate) ? _aggregate : BufferUtil.EMPTY_BUFFER, false);
        return;
      
      case 2: 
        throw new IllegalStateException("isReady() not called");
      }
      
    } while (!_state.compareAndSet(OutputState.READY, OutputState.PENDING));
    
    new AsyncFlush().iterate();
    return;
    

    return;
    

    throw new WritePendingException();
    

    throw new EofException(_onError);
    

    return;
    

    throw new IllegalStateException();
  }
  



  public void write(byte[] b, int off, int len)
    throws IOException
  {
    do
    {
      switch (2.$SwitchMap$org$eclipse$jetty$server$HttpOutput$OutputState[((OutputState)_state.get()).ordinal()])
      {
      case 6: 
        break;
      

      case 2: 
        throw new IllegalStateException("isReady() not called");
      }
      
    } while (!_state.compareAndSet(OutputState.READY, OutputState.PENDING));
    


    boolean last = isLastContentToWrite(len);
    if ((!last) && (len <= _commitSize))
    {
      if (_aggregate == null) {
        _aggregate = _channel.getByteBufferPool().acquire(getBufferSize(), _interceptor.isOptimizedForDirectBuffers());
      }
      
      int filled = BufferUtil.fill(_aggregate, b, off, len);
      

      if ((filled == len) && (!BufferUtil.isFull(_aggregate)))
      {
        if (!_state.compareAndSet(OutputState.PENDING, OutputState.ASYNC))
          throw new IllegalStateException();
        return;
      }
      

      off += filled;
      len -= filled;
    }
    

    new AsyncWrite(b, off, len, last).iterate();
    return;
    


    throw new WritePendingException();
    

    throw new EofException(_onError);
    

    throw new EofException("Closed");
    

    throw new IllegalStateException();
    






    int capacity = getBufferSize();
    boolean last = isLastContentToWrite(len);
    if ((!last) && (len <= _commitSize))
    {
      if (_aggregate == null) {
        _aggregate = _channel.getByteBufferPool().acquire(capacity, _interceptor.isOptimizedForDirectBuffers());
      }
      
      int filled = BufferUtil.fill(_aggregate, b, off, len);
      

      if ((filled == len) && (!BufferUtil.isFull(_aggregate))) {
        return;
      }
      
      off += filled;
      len -= filled;
    }
    

    if (BufferUtil.hasContent(_aggregate))
    {
      write(_aggregate, (last) && (len == 0));
      

      if ((len > 0) && (!last) && (len <= _commitSize) && (len <= BufferUtil.space(_aggregate)))
      {
        BufferUtil.append(_aggregate, b, off, len);
        return;
      }
    }
    

    if (len > 0)
    {


      ByteBuffer view = ByteBuffer.wrap(b, off, len);
      while (len > getBufferSize())
      {
        int p = view.position();
        int l = p + getBufferSize();
        view.limit(p + getBufferSize());
        write(view, false);
        len -= getBufferSize();
        view.limit(l + Math.min(len, getBufferSize()));
        view.position(l);
      }
      write(view, last);
    }
    else if (last)
    {
      write(BufferUtil.EMPTY_BUFFER, true);
    }
    
    if (last) {
      closed();
    }
  }
  
  public void write(ByteBuffer buffer) throws IOException
  {
    do
    {
      switch (2.$SwitchMap$org$eclipse$jetty$server$HttpOutput$OutputState[((OutputState)_state.get()).ordinal()])
      {
      case 6: 
        break;
      

      case 2: 
        throw new IllegalStateException("isReady() not called");
      }
      
    } while (!_state.compareAndSet(OutputState.READY, OutputState.PENDING));
    


    boolean last = isLastContentToWrite(buffer.remaining());
    new AsyncWrite(buffer, last).iterate();
    return;
    


    throw new WritePendingException();
    

    throw new EofException(_onError);
    

    throw new EofException("Closed");
    

    throw new IllegalStateException();
    




    int len = BufferUtil.length(buffer);
    boolean last = isLastContentToWrite(len);
    

    if (BufferUtil.hasContent(_aggregate)) {
      write(_aggregate, (last) && (len == 0));
    }
    
    if (len > 0) {
      write(buffer, last);
    } else if (last) {
      write(BufferUtil.EMPTY_BUFFER, true);
    }
    if (last) {
      closed();
    }
  }
  
  public void write(int b) throws IOException
  {
    _written += 1L;
    boolean complete = _channel.getResponse().isAllContentWritten(_written);
    

    do
    {
      switch (2.$SwitchMap$org$eclipse$jetty$server$HttpOutput$OutputState[((OutputState)_state.get()).ordinal()])
      {
      case 6: 
        if (_aggregate == null)
          _aggregate = _channel.getByteBufferPool().acquire(getBufferSize(), _interceptor.isOptimizedForDirectBuffers());
        BufferUtil.append(_aggregate, (byte)b);
        

        if ((!complete) && (!BufferUtil.isFull(_aggregate)))
          break;
        write(_aggregate, complete);
        if (!complete) break;
        closed(); break;
      


      case 2: 
        throw new IllegalStateException("isReady() not called");
      }
      
    } while (!_state.compareAndSet(OutputState.READY, OutputState.PENDING));
    

    if (_aggregate == null)
      _aggregate = _channel.getByteBufferPool().acquire(getBufferSize(), _interceptor.isOptimizedForDirectBuffers());
    BufferUtil.append(_aggregate, (byte)b);
    

    if ((!complete) && (!BufferUtil.isFull(_aggregate)))
    {
      if (!_state.compareAndSet(OutputState.PENDING, OutputState.ASYNC))
        throw new IllegalStateException();
      return;
    }
    

    new AsyncFlush().iterate();
    return;
    


    throw new WritePendingException();
    

    throw new EofException(_onError);
    

    throw new EofException("Closed");
    

    throw new IllegalStateException();
  }
  



  public void print(String s)
    throws IOException
  {
    if (isClosed()) {
      throw new IOException("Closed");
    }
    write(s.getBytes(_channel.getResponse().getCharacterEncoding()));
  }
  





  public void sendContent(ByteBuffer content)
    throws IOException
  {
    if (LOG.isDebugEnabled()) {
      LOG.debug("sendContent({})", new Object[] { BufferUtil.toDetailString(content) });
    }
    _written += content.remaining();
    write(content, true);
    closed();
  }
  



  public void sendContent(InputStream in)
    throws IOException
  {
    try
    {
      SharedBlockingCallback.Blocker blocker = _writeBlocker.acquire();Throwable localThrowable4 = null;
      try {
        new InputStreamWritingCB(in, blocker).iterate();
        blocker.block();
      }
      catch (Throwable localThrowable2)
      {
        localThrowable4 = localThrowable2;throw localThrowable2;
      }
      finally
      {
        if (blocker != null) if (localThrowable4 != null) try { blocker.close(); } catch (Throwable localThrowable3) { localThrowable4.addSuppressed(localThrowable3); } else blocker.close();
      }
    } catch (Throwable failure) {
      if (LOG.isDebugEnabled())
        LOG.debug(failure);
      abort(failure);
      throw failure;
    }
  }
  



  public void sendContent(ReadableByteChannel in)
    throws IOException
  {
    try
    {
      SharedBlockingCallback.Blocker blocker = _writeBlocker.acquire();Throwable localThrowable4 = null;
      try {
        new ReadableByteChannelWritingCB(in, blocker).iterate();
        blocker.block();
      }
      catch (Throwable localThrowable2)
      {
        localThrowable4 = localThrowable2;throw localThrowable2;
      }
      finally
      {
        if (blocker != null) if (localThrowable4 != null) try { blocker.close(); } catch (Throwable localThrowable3) { localThrowable4.addSuppressed(localThrowable3); } else blocker.close();
      }
    } catch (Throwable failure) {
      if (LOG.isDebugEnabled())
        LOG.debug(failure);
      abort(failure);
      throw failure;
    }
  }
  



  public void sendContent(HttpContent content)
    throws IOException
  {
    try
    {
      SharedBlockingCallback.Blocker blocker = _writeBlocker.acquire();Throwable localThrowable4 = null;
      try {
        sendContent(content, blocker);
        blocker.block();
      }
      catch (Throwable localThrowable2)
      {
        localThrowable4 = localThrowable2;throw localThrowable2;
      }
      finally
      {
        if (blocker != null) if (localThrowable4 != null) try { blocker.close(); } catch (Throwable localThrowable3) { localThrowable4.addSuppressed(localThrowable3); } else blocker.close();
      }
    } catch (Throwable failure) {
      if (LOG.isDebugEnabled())
        LOG.debug(failure);
      abort(failure);
      throw failure;
    }
  }
  






  public void sendContent(ByteBuffer content, Callback callback)
  {
    if (LOG.isDebugEnabled()) {
      LOG.debug("sendContent(buffer={},{})", new Object[] { BufferUtil.toDetailString(content), callback });
    }
    _written += content.remaining();
    write(content, true, new Callback.Nested(callback)
    {

      public void succeeded()
      {
        closed();
        super.succeeded();
      }
      

      public void failed(Throwable x)
      {
        HttpOutput.this.abort(x);
        super.failed(x);
      }
    });
  }
  







  public void sendContent(InputStream in, Callback callback)
  {
    if (LOG.isDebugEnabled()) {
      LOG.debug("sendContent(stream={},{})", new Object[] { in, callback });
    }
    new InputStreamWritingCB(in, callback).iterate();
  }
  







  public void sendContent(ReadableByteChannel in, Callback callback)
  {
    if (LOG.isDebugEnabled()) {
      LOG.debug("sendContent(channel={},{})", new Object[] { in, callback });
    }
    new ReadableByteChannelWritingCB(in, callback).iterate();
  }
  






  public void sendContent(HttpContent httpContent, Callback callback)
  {
    if (LOG.isDebugEnabled()) {
      LOG.debug("sendContent(http={},{})", new Object[] { httpContent, callback });
    }
    if (BufferUtil.hasContent(_aggregate))
    {
      callback.failed(new IOException("cannot sendContent() after write()"));
      return;
    }
    if (_channel.isCommitted())
    {
      callback.failed(new IOException("cannot sendContent(), output already committed"));
    }
    else
    {
      for (;;)
      {
        switch (2.$SwitchMap$org$eclipse$jetty$server$HttpOutput$OutputState[((OutputState)_state.get()).ordinal()])
        {
        case 6: 
          if (_state.compareAndSet(OutputState.OPEN, OutputState.PENDING)) {
            break label202;
          }
        }
      }
      callback.failed(new EofException(_onError));
      return;
      

      callback.failed(new EofException("Closed"));
      return;
      

      throw new IllegalStateException();
      

      label202:
      
      ByteBuffer buffer = _channel.useDirectBuffers() ? httpContent.getDirectBuffer() : null;
      if (buffer == null) {
        buffer = httpContent.getIndirectBuffer();
      }
      if (buffer != null)
      {
        sendContent(buffer, callback);
        return;
      }
      
      try
      {
        ReadableByteChannel rbc = httpContent.getReadableByteChannel();
        if (rbc != null)
        {

          sendContent(rbc, callback);
          return;
        }
        
        InputStream in = httpContent.getInputStream();
        if (in != null)
        {
          sendContent(in, callback);
          return;
        }
        
        throw new IllegalArgumentException("unknown content for " + httpContent);
      }
      catch (Throwable th)
      {
        abort(th);
        callback.failed(th);
      }
    }
  }
  
  public int getBufferSize() {
    return _bufferSize;
  }
  
  public void setBufferSize(int size)
  {
    _bufferSize = size;
    _commitSize = size;
  }
  
  public void recycle()
  {
    _interceptor = _channel;
    HttpConfiguration config = _channel.getHttpConfiguration();
    _bufferSize = config.getOutputBufferSize();
    _commitSize = config.getOutputAggregationSize();
    if (_commitSize > _bufferSize)
      _commitSize = _bufferSize;
    releaseBuffer();
    _written = 0L;
    reopen();
  }
  
  public void resetBuffer()
  {
    _interceptor.resetBuffer();
    if (BufferUtil.hasContent(_aggregate))
      BufferUtil.clear(_aggregate);
    _written = 0L;
    reopen();
  }
  

  public void setWriteListener(WriteListener writeListener)
  {
    if (!_channel.getState().isAsync()) {
      throw new IllegalStateException("!ASYNC");
    }
    if (_state.compareAndSet(OutputState.OPEN, OutputState.READY))
    {
      _writeListener = writeListener;
      if (_channel.getState().onWritePossible()) {
        _channel.execute(_channel);
      }
    } else {
      throw new IllegalStateException();
    }
  }
  
  public boolean isReady()
  {
    do {
      do {
        switch (2.$SwitchMap$org$eclipse$jetty$server$HttpOutput$OutputState[((OutputState)_state.get()).ordinal()])
        {
        case 6: 
          return true;
        }
        
      } while (!_state.compareAndSet(OutputState.ASYNC, OutputState.READY));
      
      return true;
      

      return true;

    }
    while (!_state.compareAndSet(OutputState.PENDING, OutputState.UNREADY));
    
    return false;
    

    return false;
    

    return true;
    

    return true;
    

    throw new IllegalStateException();
  }
  



  public void run()
  {
    for (;;)
    {
      OutputState state = (OutputState)_state.get();
      
      if (_onError != null)
      {
        switch (2.$SwitchMap$org$eclipse$jetty$server$HttpOutput$OutputState[state.ordinal()])
        {

        case 1: 
        case 7: 
          _onError = null;
          return;
        }
        
        
        if (_state.compareAndSet(state, OutputState.ERROR))
        {
          Throwable th = _onError;
          _onError = null;
          if (LOG.isDebugEnabled())
            LOG.debug("onError", th);
          _writeListener.onError(th);
          close();




        }
        





      }
      else
      {




        try
        {




          _writeListener.onWritePossible();

        }
        catch (Throwable e)
        {
          _onError = e;
        }
      }
    }
  }
  
  private void close(Closeable resource)
  {
    try {
      resource.close();
    }
    catch (Throwable x)
    {
      LOG.ignore(x);
    }
  }
  

  public String toString()
  {
    return String.format("%s@%x{%s}", new Object[] { getClass().getSimpleName(), Integer.valueOf(hashCode()), _state.get() });
  }
  
  private abstract class AsyncICB extends IteratingCallback
  {
    final boolean _last;
    
    AsyncICB(boolean last)
    {
      _last = last;
    }
    

    protected void onCompleteSuccess()
    {
      do
      {
        HttpOutput.OutputState last = (HttpOutput.OutputState)_state.get();
        switch (HttpOutput.2.$SwitchMap$org$eclipse$jetty$server$HttpOutput$OutputState[last.ordinal()])
        {
        case 4: 
          if (_state.compareAndSet(HttpOutput.OutputState.PENDING, HttpOutput.OutputState.ASYNC)) return;
          break; } } while ((goto 0) || 
      


        (!_state.compareAndSet(HttpOutput.OutputState.UNREADY, HttpOutput.OutputState.READY)));
      
      if (_last)
        closed();
      if (_channel.getState().onWritePossible()) {
        _channel.execute(_channel); return;
        


        return;
        

        throw new IllegalStateException();
      }
    }
    



    public void onCompleteFailure(Throwable e)
    {
      _onError = (e == null ? new IOException() : e);
      if (_channel.getState().onWritePossible()) {
        _channel.execute(_channel);
      }
    }
  }
  
  private class AsyncFlush extends HttpOutput.AsyncICB
  {
    protected volatile boolean _flushed;
    
    public AsyncFlush() {
      super(false);
    }
    

    protected IteratingCallback.Action process()
    {
      if (BufferUtil.hasContent(_aggregate))
      {
        _flushed = true;
        write(_aggregate, false, this);
        return IteratingCallback.Action.SCHEDULED;
      }
      
      if (!_flushed)
      {
        _flushed = true;
        write(BufferUtil.EMPTY_BUFFER, false, this);
        return IteratingCallback.Action.SCHEDULED;
      }
      
      return IteratingCallback.Action.SUCCEEDED;
    }
  }
  
  private class AsyncWrite extends HttpOutput.AsyncICB
  {
    private final ByteBuffer _buffer;
    private final ByteBuffer _slice;
    private final int _len;
    protected volatile boolean _completed;
    
    public AsyncWrite(byte[] b, int off, int len, boolean last)
    {
      super(last);
      _buffer = ByteBuffer.wrap(b, off, len);
      _len = len;
      
      _slice = (_len < getBufferSize() ? null : _buffer.duplicate());
    }
    
    public AsyncWrite(ByteBuffer buffer, boolean last)
    {
      super(last);
      _buffer = buffer;
      _len = buffer.remaining();
      
      if ((_buffer.isDirect()) || (_len < getBufferSize())) {
        _slice = null;
      }
      else {
        _slice = _buffer.duplicate();
      }
    }
    


    protected IteratingCallback.Action process()
    {
      if (BufferUtil.hasContent(_aggregate))
      {
        _completed = (_len == 0);
        write(_aggregate, (_last) && (_completed), this);
        return IteratingCallback.Action.SCHEDULED;
      }
      

      if ((!_last) && (_len < BufferUtil.space(_aggregate)) && (_len < _commitSize))
      {
        int position = BufferUtil.flipToFill(_aggregate);
        BufferUtil.put(_buffer, _aggregate);
        BufferUtil.flipToFlush(_aggregate, position);
        return IteratingCallback.Action.SUCCEEDED;
      }
      

      if (_buffer.hasRemaining())
      {

        if (_slice == null)
        {
          _completed = true;
          write(_buffer, _last, this);
          return IteratingCallback.Action.SCHEDULED;
        }
        

        int p = _buffer.position();
        int l = Math.min(getBufferSize(), _buffer.remaining());
        int pl = p + l;
        _slice.limit(pl);
        _buffer.position(pl);
        _slice.position(p);
        _completed = (!_buffer.hasRemaining());
        write(_slice, (_last) && (_completed), this);
        return IteratingCallback.Action.SCHEDULED;
      }
      


      if ((_last) && (!_completed))
      {
        _completed = true;
        write(BufferUtil.EMPTY_BUFFER, true, this);
        return IteratingCallback.Action.SCHEDULED;
      }
      
      if ((HttpOutput.LOG.isDebugEnabled()) && (_completed))
        HttpOutput.LOG.debug("EOF of {}", new Object[] { this });
      return IteratingCallback.Action.SUCCEEDED;
    }
  }
  


  private class InputStreamWritingCB
    extends IteratingNestedCallback
  {
    private final InputStream _in;
    

    private final ByteBuffer _buffer;
    

    private boolean _eof;
    

    public InputStreamWritingCB(InputStream in, Callback callback)
    {
      super();
      _in = in;
      _buffer = _channel.getByteBufferPool().acquire(getBufferSize(), false);
    }
    


    protected IteratingCallback.Action process()
      throws Exception
    {
      if (_eof)
      {
        if (HttpOutput.LOG.isDebugEnabled()) {
          HttpOutput.LOG.debug("EOF of {}", new Object[] { this });
        }
        _in.close();
        closed();
        _channel.getByteBufferPool().release(_buffer);
        return IteratingCallback.Action.SUCCEEDED;
      }
      

      int len = 0;
      while ((len < _buffer.capacity()) && (!_eof))
      {
        int r = _in.read(_buffer.array(), _buffer.arrayOffset() + len, _buffer.capacity() - len);
        if (r < 0) {
          _eof = true;
        } else {
          len += r;
        }
      }
      
      _buffer.position(0);
      _buffer.limit(len);
      _written = (_written + len);
      write(_buffer, _eof, this);
      return IteratingCallback.Action.SCHEDULED;
    }
    

    public void onCompleteFailure(Throwable x)
    {
      HttpOutput.this.abort(x);
      _channel.getByteBufferPool().release(_buffer);
      HttpOutput.this.close(_in);
      super.onCompleteFailure(x);
    }
  }
  


  private class ReadableByteChannelWritingCB
    extends IteratingNestedCallback
  {
    private final ReadableByteChannel _in;
    

    private final ByteBuffer _buffer;
    

    private boolean _eof;
    


    public ReadableByteChannelWritingCB(ReadableByteChannel in, Callback callback)
    {
      super();
      _in = in;
      _buffer = _channel.getByteBufferPool().acquire(getBufferSize(), _channel.useDirectBuffers());
    }
    


    protected IteratingCallback.Action process()
      throws Exception
    {
      if (_eof)
      {
        if (HttpOutput.LOG.isDebugEnabled())
          HttpOutput.LOG.debug("EOF of {}", new Object[] { this });
        _in.close();
        closed();
        _channel.getByteBufferPool().release(_buffer);
        return IteratingCallback.Action.SUCCEEDED;
      }
      

      BufferUtil.clearToFill(_buffer);
      while ((_buffer.hasRemaining()) && (!_eof)) {
        _eof = (_in.read(_buffer) < 0);
      }
      
      BufferUtil.flipToFlush(_buffer, 0);
      _written = (_written + _buffer.remaining());
      write(_buffer, _eof, this);
      
      return IteratingCallback.Action.SCHEDULED;
    }
    

    public void onCompleteFailure(Throwable x)
    {
      HttpOutput.this.abort(x);
      _channel.getByteBufferPool().release(_buffer);
      HttpOutput.this.close(_in);
      super.onCompleteFailure(x);
    }
  }
  
  private static class WriteBlocker extends SharedBlockingCallback
  {
    private final HttpChannel _channel;
    
    private WriteBlocker(HttpChannel channel)
    {
      _channel = channel;
    }
    

    protected long getIdleTimeout()
    {
      long blockingTimeout = _channel.getHttpConfiguration().getBlockingTimeout();
      if (blockingTimeout == 0L)
        return _channel.getIdleTimeout();
      return blockingTimeout;
    }
  }
}
