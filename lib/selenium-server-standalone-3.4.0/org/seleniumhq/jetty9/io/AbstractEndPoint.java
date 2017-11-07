package org.seleniumhq.jetty9.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import org.seleniumhq.jetty9.util.BufferUtil;
import org.seleniumhq.jetty9.util.Callback;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;
import org.seleniumhq.jetty9.util.thread.Scheduler;


















public abstract class AbstractEndPoint
  extends IdleTimeout
  implements EndPoint
{
  private static final Logger LOG = Log.getLogger(AbstractEndPoint.class);
  
  private final AtomicReference<State> _state = new AtomicReference(State.OPEN);
  private final long _created = System.currentTimeMillis();
  
  private volatile Connection _connection;
  private final FillInterest _fillInterest = new FillInterest()
  {
    protected void needsFillInterest()
      throws IOException
    {
      AbstractEndPoint.this.needsFillInterest();
    }
  };
  
  private final WriteFlusher _writeFlusher = new WriteFlusher(this)
  {

    protected void onIncompleteFlush()
    {
      AbstractEndPoint.this.onIncompleteFlush();
    }
  };
  
  protected AbstractEndPoint(Scheduler scheduler)
  {
    super(scheduler);
  }
  
  protected final void shutdownInput()
  {
    for (;;)
    {
      State s = (State)_state.get();
      switch (3.$SwitchMap$org$eclipse$jetty$io$AbstractEndPoint$State[s.ordinal()])
      {
      case 1: 
        if (_state.compareAndSet(s, State.ISHUTTING))
        {
          try
          {
            doShutdownInput();
            


            if (!_state.compareAndSet(State.ISHUTTING, State.ISHUT))
            {


              if (_state.get() == State.CLOSED) {
                doOnClose(null);
              } else {
                throw new IllegalStateException();
              }
            }
          }
          finally
          {
            if (!_state.compareAndSet(State.ISHUTTING, State.ISHUT))
            {


              if (_state.get() == State.CLOSED) {
                doOnClose(null);
              } else
                throw new IllegalStateException();
            }
          }
          return;
        }
        break;
      case 2: case 3: 
        return;
      
      case 4: 
        if (_state.compareAndSet(s, State.CLOSED))
        {

          return; }
        break;
      case 5: 
        if (_state.compareAndSet(s, State.CLOSED))
        {

          doOnClose(null);
          return;
        }
        break;
      case 6:  return;
      }
      
    }
  }
  
  public final void shutdownOutput()
  {
    for (;;)
    {
      State s = (State)_state.get();
      switch (3.$SwitchMap$org$eclipse$jetty$io$AbstractEndPoint$State[s.ordinal()])
      {
      case 1: 
        if (_state.compareAndSet(s, State.OSHUTTING))
        {
          try
          {
            doShutdownOutput();
            


            if (!_state.compareAndSet(State.OSHUTTING, State.OSHUT))
            {


              if (_state.get() == State.CLOSED) {
                doOnClose(null);
              } else {
                throw new IllegalStateException();
              }
            }
          }
          finally
          {
            if (!_state.compareAndSet(State.OSHUTTING, State.OSHUT))
            {


              if (_state.get() == State.CLOSED) {
                doOnClose(null);
              } else
                throw new IllegalStateException();
            }
          }
          return;
        }
        break;
      case 2:  if (_state.compareAndSet(s, State.CLOSED))
        {

          return; }
        break;
      case 3: 
        if (_state.compareAndSet(s, State.CLOSED))
        {

          doOnClose(null);
          return;
        }
        break;
      case 4: case 5: 
        return;
      
      case 6: 
        return;
      }
      
    }
  }
  
  public final void close()
  {
    close(null);
  }
  
  protected final void close(Throwable failure)
  {
    for (;;)
    {
      State s = (State)_state.get();
      switch (3.$SwitchMap$org$eclipse$jetty$io$AbstractEndPoint$State[s.ordinal()])
      {
      case 1: 
      case 3: 
      case 5: 
        if (_state.compareAndSet(s, State.CLOSED))
        {
          doOnClose(failure);
          return;
        }
        break;
      case 2: case 4: 
        if (_state.compareAndSet(s, State.CLOSED))
        {

          return; }
        break;
      case 6: 
        return;
      }
      
    }
  }
  

  protected void doShutdownInput() {}
  

  protected void doShutdownOutput() {}
  

  private void doOnClose(Throwable failure)
  {
    try
    {
      doClose();
      


      if (failure == null) {
        onClose();
      } else {
        onClose(failure);
      }
    }
    finally
    {
      if (failure == null) {
        onClose();
      } else {
        onClose(failure);
      }
    }
  }
  

  protected void doClose() {}
  
  protected void onClose(Throwable failure)
  {
    super.onClose();
    _writeFlusher.onFail(failure);
    _fillInterest.onFail(failure);
  }
  

  public boolean isOutputShutdown()
  {
    switch (3.$SwitchMap$org$eclipse$jetty$io$AbstractEndPoint$State[((State)_state.get()).ordinal()])
    {
    case 4: 
    case 5: 
    case 6: 
      return true;
    }
    return false;
  }
  

  public boolean isInputShutdown()
  {
    switch (3.$SwitchMap$org$eclipse$jetty$io$AbstractEndPoint$State[((State)_state.get()).ordinal()])
    {
    case 2: 
    case 3: 
    case 6: 
      return true;
    }
    return false;
  }
  


  public boolean isOpen()
  {
    switch (3.$SwitchMap$org$eclipse$jetty$io$AbstractEndPoint$State[((State)_state.get()).ordinal()])
    {
    case 6: 
      return false;
    }
    return true;
  }
  
  public void checkFlush()
    throws IOException
  {
    State s = (State)_state.get();
    switch (3.$SwitchMap$org$eclipse$jetty$io$AbstractEndPoint$State[s.ordinal()])
    {
    case 4: 
    case 5: 
    case 6: 
      throw new IOException(s.toString());
    }
    
  }
  
  public void checkFill()
    throws IOException
  {
    State s = (State)_state.get();
    switch (3.$SwitchMap$org$eclipse$jetty$io$AbstractEndPoint$State[s.ordinal()])
    {
    case 2: 
    case 3: 
    case 6: 
      throw new IOException(s.toString());
    }
    
  }
  


  public long getCreatedTimeStamp()
  {
    return _created;
  }
  

  public Connection getConnection()
  {
    return _connection;
  }
  

  public void setConnection(Connection connection)
  {
    _connection = connection;
  }
  

  public boolean isOptimizedForDirectBuffers()
  {
    return false;
  }
  
  protected void reset()
  {
    _state.set(State.OPEN);
    _writeFlusher.onClose();
    _fillInterest.onClose();
  }
  

  public void onOpen()
  {
    if (LOG.isDebugEnabled())
      LOG.debug("onOpen {}", new Object[] { this });
    if (_state.get() != State.OPEN) {
      throw new IllegalStateException();
    }
  }
  
  public void onClose()
  {
    super.onClose();
    _writeFlusher.onClose();
    _fillInterest.onClose();
  }
  

  public void fillInterested(Callback callback)
  {
    notIdle();
    _fillInterest.register(callback);
  }
  

  public boolean tryFillInterested(Callback callback)
  {
    notIdle();
    return _fillInterest.tryRegister(callback);
  }
  

  public boolean isFillInterested()
  {
    return _fillInterest.isInterested();
  }
  
  public void write(Callback callback, ByteBuffer... buffers)
    throws IllegalStateException
  {
    _writeFlusher.write(callback, buffers);
  }
  
  protected abstract void onIncompleteFlush();
  
  protected abstract void needsFillInterest() throws IOException;
  
  public FillInterest getFillInterest()
  {
    return _fillInterest;
  }
  
  protected WriteFlusher getWriteFlusher()
  {
    return _writeFlusher;
  }
  

  protected void onIdleExpired(TimeoutException timeout)
  {
    Connection connection = _connection;
    if ((connection != null) && (!connection.onIdleExpired())) {
      return;
    }
    boolean output_shutdown = isOutputShutdown();
    boolean input_shutdown = isInputShutdown();
    boolean fillFailed = _fillInterest.onFail(timeout);
    boolean writeFailed = _writeFlusher.onFail(timeout);
    







    if ((isOpen()) && ((output_shutdown) || (input_shutdown)) && (!fillFailed) && (!writeFailed)) {
      close();
    } else {
      LOG.debug("Ignored idle endpoint {}", new Object[] { this });
    }
  }
  
  public void upgrade(Connection newConnection)
  {
    Connection old_connection = getConnection();
    
    if (LOG.isDebugEnabled()) {
      LOG.debug("{} upgrading from {} to {}", new Object[] { this, old_connection, newConnection });
    }
    
    ByteBuffer prefilled = (old_connection instanceof Connection.UpgradeFrom) ? ((Connection.UpgradeFrom)old_connection).onUpgradeFrom() : null;
    old_connection.onClose();
    old_connection.getEndPoint().setConnection(newConnection);
    
    if ((newConnection instanceof Connection.UpgradeTo)) {
      ((Connection.UpgradeTo)newConnection).onUpgradeTo(prefilled);
    } else if (BufferUtil.hasContent(prefilled)) {
      throw new IllegalStateException();
    }
    newConnection.onOpen();
  }
  

  public String toString()
  {
    return String.format("%s->%s", new Object[] { toEndPointString(), toConnectionString() });
  }
  
  public String toEndPointString()
  {
    Class<?> c = getClass();
    String name = c.getSimpleName();
    while ((name.length() == 0) && (c.getSuperclass() != null))
    {
      c = c.getSuperclass();
      name = c.getSimpleName();
    }
    
    return String.format("%s@%h{%s<->%s,%s,fill=%s,flush=%s,to=%d/%d}", new Object[] { name, this, 
    

      getRemoteAddress(), 
      getLocalAddress(), _state
      .get(), _fillInterest
      .toStateString(), _writeFlusher
      .toStateString(), 
      Long.valueOf(getIdleFor()), 
      Long.valueOf(getIdleTimeout()) });
  }
  
  public String toConnectionString()
  {
    Connection connection = getConnection();
    if (connection == null)
      return "<null>";
    if ((connection instanceof AbstractConnection))
      return ((AbstractConnection)connection).toConnectionString();
    return String.format("%s@%x", new Object[] { connection.getClass().getSimpleName(), Integer.valueOf(connection.hashCode()) });
  }
  

  private static enum State
  {
    OPEN,  ISHUTTING,  ISHUT,  OSHUTTING,  OSHUT,  CLOSED;
    
    private State() {}
  }
}
