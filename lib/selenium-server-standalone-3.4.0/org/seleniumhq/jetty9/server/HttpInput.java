package org.seleniumhq.jetty9.server;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import org.seleniumhq.jetty9.http.BadMessageException;
import org.seleniumhq.jetty9.http.HttpFields;
import org.seleniumhq.jetty9.io.EofException;
import org.seleniumhq.jetty9.io.RuntimeIOException;
import org.seleniumhq.jetty9.util.BufferUtil;
import org.seleniumhq.jetty9.util.Callback;
import org.seleniumhq.jetty9.util.component.Destroyable;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;
import org.seleniumhq.jetty9.util.thread.Invocable.InvocationType;


















































public class HttpInput
  extends ServletInputStream
  implements Runnable
{
  public static abstract interface Interceptor
  {
    public abstract HttpInput.Content readFrom(HttpInput.Content paramContent);
  }
  
  public static class ChainedInterceptor
    implements HttpInput.Interceptor, Destroyable
  {
    private final HttpInput.Interceptor _prev;
    private final HttpInput.Interceptor _next;
    
    public ChainedInterceptor(HttpInput.Interceptor prev, HttpInput.Interceptor next)
    {
      _prev = prev;
      _next = next;
    }
    
    public HttpInput.Interceptor getPrev()
    {
      return _prev;
    }
    
    public HttpInput.Interceptor getNext()
    {
      return _next;
    }
    

    public HttpInput.Content readFrom(HttpInput.Content content)
    {
      return getNext().readFrom(getPrev().readFrom(content));
    }
    

    public void destroy()
    {
      if ((_prev instanceof Destroyable))
        ((Destroyable)_prev).destroy();
      if ((_next instanceof Destroyable))
        ((Destroyable)_next).destroy();
    }
  }
  
  private static final Logger LOG = Log.getLogger(HttpInput.class);
  static final Content EOF_CONTENT = new EofContent("EOF");
  static final Content EARLY_EOF_CONTENT = new EofContent("EARLY_EOF");
  
  private final byte[] _oneByteBuffer = new byte[1];
  private Content _content;
  private Content _intercepted;
  private final Deque<Content> _inputQ = new ArrayDeque();
  private final HttpChannelState _channelState;
  private ReadListener _listener;
  private State _state = STREAM;
  private long _firstByteTimeStamp = -1L;
  private long _contentArrived;
  private long _contentConsumed;
  private long _blockUntil;
  private Interceptor _interceptor;
  
  public HttpInput(HttpChannelState state)
  {
    _channelState = state;
  }
  
  protected HttpChannelState getHttpChannelState()
  {
    return _channelState;
  }
  
  public void recycle()
  {
    synchronized (_inputQ)
    {
      if (_content != null)
        _content.failed(null);
      _content = null;
      Content item = (Content)_inputQ.poll();
      while (item != null)
      {
        item.failed(null);
        item = (Content)_inputQ.poll();
      }
      _listener = null;
      _state = STREAM;
      _contentArrived = 0L;
      _contentConsumed = 0L;
      _firstByteTimeStamp = -1L;
      _blockUntil = 0L;
      if ((_interceptor instanceof Destroyable))
        ((Destroyable)_interceptor).destroy();
      _interceptor = null;
    }
  }
  



  public Interceptor getInterceptor()
  {
    return _interceptor;
  }
  




  public void setInterceptor(Interceptor interceptor)
  {
    _interceptor = interceptor;
  }
  





  public void addInterceptor(Interceptor interceptor)
  {
    if (_interceptor == null) {
      _interceptor = interceptor;
    } else {
      _interceptor = new ChainedInterceptor(_interceptor, interceptor);
    }
  }
  
  public int available()
  {
    int available = 0;
    boolean woken = false;
    synchronized (_inputQ)
    {
      if (_content == null)
        _content = ((Content)_inputQ.poll());
      if (_content == null)
      {
        try
        {
          produceContent();
        }
        catch (IOException e)
        {
          woken = failed(e);
        }
        if (_content == null) {
          _content = ((Content)_inputQ.poll());
        }
      }
      if (_content != null) {
        available = _content.remaining();
      }
    }
    if (woken)
      wake();
    return available;
  }
  
  protected void wake()
  {
    HttpChannel channel = _channelState.getHttpChannel();
    Executor executor = channel.getConnector().getServer().getThreadPool();
    executor.execute(channel);
  }
  
  private long getBlockingTimeout()
  {
    return getHttpChannelState().getHttpChannel().getHttpConfiguration().getBlockingTimeout();
  }
  
  public int read()
    throws IOException
  {
    int read = read(_oneByteBuffer, 0, 1);
    if (read == 0)
      throw new IllegalStateException("unready read=0");
    return read < 0 ? -1 : _oneByteBuffer[0] & 0xFF;
  }
  
  public int read(byte[] b, int off, int len)
    throws IOException
  {
    boolean wake = false;
    int l;
    synchronized (_inputQ)
    {

      if (!isAsync())
      {
        if (_blockUntil == 0L)
        {
          long blockingTimeout = getBlockingTimeout();
          if (blockingTimeout > 0L) {
            _blockUntil = (System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(blockingTimeout));
          }
        }
      }
      
      long minRequestDataRate = _channelState.getHttpChannel().getHttpConfiguration().getMinRequestDataRate();
      if ((minRequestDataRate > 0L) && (_firstByteTimeStamp != -1L))
      {
        long period = System.nanoTime() - _firstByteTimeStamp;
        if (period > 0L)
        {
          long minimum_data = minRequestDataRate * TimeUnit.NANOSECONDS.toMillis(period) / TimeUnit.SECONDS.toMillis(1L);
          if (_contentArrived < minimum_data) {
            throw new BadMessageException(408, String.format("Request data rate < %d B/s", new Object[] { Long.valueOf(minRequestDataRate) }));
          }
        }
      }
      
      for (;;)
      {
        Content item = nextContent();
        if (item != null)
        {
          int l = get(item, b, off, len);
          if (LOG.isDebugEnabled()) {
            LOG.debug("{} read {} from {}", new Object[] { this, Integer.valueOf(l), item });
          }
          
          if (!item.isEmpty()) break;
          pollReadableContent(); break;
        }
        


        if (!_state.blockForContent(this))
        {

          int l = _state.noContent();
          

          if (l >= 0) break;
          wake = _channelState.onReadEof(); break;
        }
      }
    }
    
    int l;
    if (wake)
      wake();
    return l;
  }
  







  protected void produceContent()
    throws IOException
  {}
  






  protected Content nextContent()
    throws IOException
  {
    Content content = pollNonEmptyContent();
    if ((content == null) && (!isFinished()))
    {
      produceContent();
      content = pollNonEmptyContent();
    }
    return content;
  }
  



  protected Content pollNonEmptyContent()
  {
    Content content;
    

    for (;;)
    {
      content = pollReadableContent();
      

      if (!(content instanceof SentinelContent))
        break;
      if ((content instanceof EofContent))
      {
        if (content == EARLY_EOF_CONTENT) {
          _state = EARLY_EOF;
        } else if (_listener == null) {
          _state = EOF;
        } else {
          _state = AEOF;
        }
      }
      

      content.succeeded();
      if (_content == content) {
        _content = null;
      } else if (_intercepted == content) {
        _intercepted = null;
      }
    }
    
    return content;
  }
  








  protected Content pollReadableContent()
  {
    if (_intercepted != null)
    {

      if (_intercepted.hasContent()) {
        return _intercepted;
      }
      
      _intercepted.succeeded();
      _intercepted = null;
    }
    


    if (_content == null) {
      _content = ((Content)_inputQ.poll());
    }
    
    while (_content != null)
    {

      if (_interceptor != null)
      {


        _intercepted = _interceptor.readFrom(_content);
        

        if ((_intercepted != null) && (_intercepted != _content))
        {

          if (_intercepted.hasContent())
            return _intercepted;
          _intercepted.succeeded();
        }
        

        _intercepted = null;
      }
      





      if ((_content.hasContent()) || ((_content instanceof SentinelContent))) {
        return _content;
      }
      

      _content.succeeded();
      _content = ((Content)_inputQ.poll());
    }
    
    return null;
  }
  








  protected Content nextReadable()
    throws IOException
  {
    Content content = pollReadableContent();
    if ((content == null) && (!isFinished()))
    {
      produceContent();
      content = pollReadableContent();
    }
    return content;
  }
  














  protected int get(Content content, byte[] buffer, int offset, int length)
  {
    int l = content.get(buffer, offset, length);
    _contentConsumed += l;
    return l;
  }
  








  protected void skip(Content content, int length)
  {
    int l = content.skip(length);
    
    _contentConsumed += l;
    if ((l > 0) && (content.isEmpty())) {
      pollNonEmptyContent();
    }
  }
  





  protected void blockForContent()
    throws IOException
  {
    try
    {
      long timeout = 0L;
      if (_blockUntil != 0L)
      {
        timeout = TimeUnit.NANOSECONDS.toMillis(_blockUntil - System.nanoTime());
        if (timeout <= 0L) {
          throw new TimeoutException();
        }
      }
      if (LOG.isDebugEnabled())
        LOG.debug("{} blocking for content timeout={}", new Object[] { this, Long.valueOf(timeout) });
      if (timeout > 0L) {
        _inputQ.wait(timeout);
      } else {
        _inputQ.wait();
      }
      


      if ((_blockUntil != 0L) && (TimeUnit.NANOSECONDS.toMillis(_blockUntil - System.nanoTime()) <= 0L)) {
        throw new TimeoutException(String.format("Blocking timeout %d ms", new Object[] { Long.valueOf(getBlockingTimeout()) }));
      }
    }
    catch (Throwable e) {
      throw ((IOException)new InterruptedIOException().initCause(e));
    }
  }
  










  public boolean prependContent(Content item)
  {
    boolean woken = false;
    synchronized (_inputQ)
    {
      if (_content != null)
        _inputQ.push(_content);
      _content = item;
      _contentConsumed -= item.remaining();
      if (LOG.isDebugEnabled()) {
        LOG.debug("{} prependContent {}", new Object[] { this, item });
      }
      if (_listener == null) {
        _inputQ.notify();
      } else
        woken = _channelState.onReadPossible();
    }
    return woken;
  }
  







  public boolean addContent(Content content)
  {
    boolean woken = false;
    synchronized (_inputQ)
    {
      if (_firstByteTimeStamp == -1L) {
        _firstByteTimeStamp = System.nanoTime();
      }
      _contentArrived += content.remaining();
      
      if ((_content == null) && (_inputQ.isEmpty())) {
        _content = content;
      } else {
        _inputQ.offer(content);
      }
      if (LOG.isDebugEnabled()) {
        LOG.debug("{} addContent {}", new Object[] { this, content });
      }
      if (pollReadableContent() != null)
      {
        if (_listener == null) {
          _inputQ.notify();
        } else
          woken = _channelState.onReadPossible();
      }
    }
    return woken;
  }
  
  public boolean hasContent()
  {
    synchronized (_inputQ)
    {
      return (_content != null) || (_inputQ.size() > 0);
    }
  }
  
  public void unblock()
  {
    synchronized (_inputQ)
    {
      _inputQ.notify();
    }
  }
  
  public long getContentConsumed()
  {
    synchronized (_inputQ)
    {
      return _contentConsumed;
    }
  }
  







  public boolean earlyEOF()
  {
    return addContent(EARLY_EOF_CONTENT);
  }
  





  public boolean eof()
  {
    return addContent(EOF_CONTENT);
  }
  
  public boolean consumeAll()
  {
    synchronized (_inputQ)
    {
      try
      {
        while (!isFinished())
        {
          Content item = nextContent();
          if (item == null) {
            break;
          }
          skip(item, item.remaining());
        }
        return (isFinished()) && (!isError());
      }
      catch (IOException e)
      {
        LOG.debug(e);
        return false;
      }
    }
  }
  
  public boolean isError()
  {
    synchronized (_inputQ)
    {
      return _state instanceof ErrorState;
    }
  }
  
  public boolean isAsync()
  {
    synchronized (_inputQ)
    {
      return _state == ASYNC;
    }
  }
  

  public boolean isFinished()
  {
    synchronized (_inputQ)
    {
      return _state instanceof EOFState;
    }
  }
  

  public boolean isReady()
  {
    try
    {
      synchronized (_inputQ)
      {
        if (_listener == null)
          return true;
        if ((_state instanceof EOFState))
          return true;
        if (nextReadable() != null) {
          return true;
        }
        _channelState.onReadUnready();
      }
      return false;
    }
    catch (IOException e)
    {
      LOG.ignore(e); }
    return true;
  }
  


  public void setReadListener(ReadListener readListener)
  {
    readListener = (ReadListener)Objects.requireNonNull(readListener);
    boolean woken = false;
    try
    {
      synchronized (_inputQ)
      {
        if (_listener != null) {
          throw new IllegalStateException("ReadListener already set");
        }
        _listener = readListener;
        
        Content content = nextReadable();
        if (content != null)
        {
          _state = ASYNC;
          woken = _channelState.onReadReady();
        }
        else if (_state == EOF)
        {
          _state = AEOF;
          woken = _channelState.onReadReady();
        }
        else
        {
          _state = ASYNC;
          _channelState.onReadUnready();
        }
      }
    }
    catch (IOException e)
    {
      throw new RuntimeIOException(e);
    }
    
    if (woken) {
      wake();
    }
  }
  
  public boolean failed(Throwable x) {
    boolean woken = false;
    synchronized (_inputQ)
    {
      if ((_state instanceof ErrorState)) {
        LOG.warn(x);
      } else {
        _state = new ErrorState(x);
      }
      if (_listener == null) {
        _inputQ.notify();
      } else {
        woken = _channelState.onReadPossible();
      }
    }
    return woken;
  }
  







  public void run()
  {
    boolean aeof = false;
    
    synchronized (_inputQ)
    {
      ReadListener listener = _listener;
      
      if (_state == EOF) {
        return;
      }
      if (_state == AEOF)
      {
        _state = EOF;
        aeof = true;
      }
      
      Throwable error = _state.getError();
      
      if ((!aeof) && (error == null))
      {
        Content content = pollReadableContent();
        

        if ((content instanceof EofContent))
        {
          content.succeeded();
          if (_content == content)
            _content = null;
          if (content == EARLY_EOF_CONTENT)
          {
            _state = EARLY_EOF;
            error = _state.getError();
          }
          else
          {
            _state = EOF;
            aeof = true;
          }
        }
        else if (content == null) {
          return;
        }
      }
    }
    try
    {
      if (error != null)
      {
        _channelState.getHttpChannel().getResponse().getHttpFields().add(HttpConnection.CONNECTION_CLOSE);
        listener.onError(error);
      }
      else if (aeof)
      {
        listener.onAllDataRead();
      }
      else
      {
        listener.onDataAvailable();
        synchronized (_inputQ)
        {
          if (_state == AEOF)
          {
            _state = EOF;
            aeof = !_channelState.isAsyncComplete();
          }
        }
        if (aeof)
          listener.onAllDataRead();
      }
    } catch (Throwable e) {
      Throwable error;
      ReadListener listener;
      LOG.warn(e.toString(), new Object[0]);
      LOG.debug(e);
      try
      {
        if ((aeof) || (error == null))
        {
          _channelState.getHttpChannel().getResponse().getHttpFields().add(HttpConnection.CONNECTION_CLOSE);
          listener.onError(e);
        }
      }
      catch (Throwable e2)
      {
        LOG.warn(e2.toString(), new Object[0]);
        LOG.debug(e2);
        throw new RuntimeIOException(e2);
      }
    }
  }
  


  public String toString()
  {
    Content content;
    

    synchronized (_inputQ)
    {
      State state = _state;
      long consumed = _contentConsumed;
      int q = _inputQ.size();
      content = (Content)_inputQ.peekFirst(); }
    Content content;
    int q; long consumed; State state; return String.format("%s@%x[c=%d,q=%d,[0]=%s,s=%s]", new Object[] {
      getClass().getSimpleName(), 
      Integer.valueOf(hashCode()), 
      Long.valueOf(consumed), 
      Integer.valueOf(q), content, state });
  }
  



  public static class SentinelContent
    extends HttpInput.Content
  {
    private final String _name;
    



    public SentinelContent(String name)
    {
      super();
      _name = name;
    }
    

    public String toString()
    {
      return _name;
    }
  }
  
  public static class EofContent extends HttpInput.SentinelContent
  {
    EofContent(String name)
    {
      super();
    }
  }
  
  public static class Content implements Callback
  {
    protected final ByteBuffer _content;
    
    public Content(ByteBuffer content)
    {
      _content = content;
    }
    
    public ByteBuffer getByteBuffer()
    {
      return _content;
    }
    

    public Invocable.InvocationType getInvocationType()
    {
      return Invocable.InvocationType.NON_BLOCKING;
    }
    
    public int get(byte[] buffer, int offset, int length)
    {
      length = Math.min(_content.remaining(), length);
      _content.get(buffer, offset, length);
      return length;
    }
    
    public int skip(int length)
    {
      length = Math.min(_content.remaining(), length);
      _content.position(_content.position() + length);
      return length;
    }
    
    public boolean hasContent()
    {
      return _content.hasRemaining();
    }
    
    public int remaining()
    {
      return _content.remaining();
    }
    
    public boolean isEmpty()
    {
      return !_content.hasRemaining();
    }
    

    public String toString()
    {
      return String.format("Content@%x{%s}", new Object[] { Integer.valueOf(hashCode()), BufferUtil.toDetailString(_content) });
    }
  }
  
  protected static abstract class State {
    protected State() {}
    
    public boolean blockForContent(HttpInput in) throws IOException {
      return false;
    }
    
    public int noContent() throws IOException
    {
      return -1;
    }
    
    public Throwable getError()
    {
      return null;
    }
  }
  
  protected static class EOFState extends HttpInput.State
  {
    protected EOFState() {}
  }
  
  protected class ErrorState extends HttpInput.EOFState
  {
    final Throwable _error;
    
    ErrorState(Throwable error) {
      _error = error;
    }
    
    public Throwable getError()
    {
      return _error;
    }
    
    public int noContent()
      throws IOException
    {
      if ((_error instanceof IOException))
        throw ((IOException)_error);
      throw new IOException(_error);
    }
    

    public String toString()
    {
      return "ERROR:" + _error;
    }
  }
  
  protected static final State STREAM = new State()
  {
    public boolean blockForContent(HttpInput input)
      throws IOException
    {
      input.blockForContent();
      return true;
    }
    

    public String toString()
    {
      return "STREAM";
    }
  };
  
  protected static final State ASYNC = new State()
  {
    public int noContent()
      throws IOException
    {
      return 0;
    }
    

    public String toString()
    {
      return "ASYNC";
    }
  };
  
  protected static final State EARLY_EOF = new EOFState()
  {
    public int noContent()
      throws IOException
    {
      throw getError();
    }
    

    public String toString()
    {
      return "EARLY_EOF";
    }
    
    public IOException getError()
    {
      return new EofException("Early EOF");
    }
  };
  
  protected static final State EOF = new EOFState()
  {

    public String toString()
    {
      return "EOF";
    }
  };
  
  protected static final State AEOF = new EOFState()
  {

    public String toString()
    {
      return "AEOF";
    }
  };
}
