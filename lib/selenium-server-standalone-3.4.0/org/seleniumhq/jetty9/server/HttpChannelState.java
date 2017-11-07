package org.seleniumhq.jetty9.server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import javax.servlet.AsyncListener;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.UnavailableException;
import org.seleniumhq.jetty9.http.BadMessageException;
import org.seleniumhq.jetty9.server.handler.ContextHandler;
import org.seleniumhq.jetty9.server.handler.ContextHandler.Context;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;
import org.seleniumhq.jetty9.util.thread.Locker;
import org.seleniumhq.jetty9.util.thread.Locker.Lock;
import org.seleniumhq.jetty9.util.thread.Scheduler;




























public class HttpChannelState
{
  private static final Logger LOG = Log.getLogger(HttpChannelState.class);
  
  private static final long DEFAULT_TIMEOUT = Long.getLong("org.seleniumhq.jetty9.server.HttpChannelState.DEFAULT_TIMEOUT", 30000L).longValue();
  



  public static enum State
  {
    IDLE, 
    DISPATCHED, 
    THROWN, 
    ASYNC_WAIT, 
    ASYNC_WOKEN, 
    ASYNC_IO, 
    ASYNC_ERROR, 
    COMPLETING, 
    COMPLETED, 
    UPGRADED;
    

    private State() {}
  }
  
  public static enum Action
  {
    DISPATCH, 
    ASYNC_DISPATCH, 
    ERROR_DISPATCH, 
    ASYNC_ERROR, 
    WRITE_CALLBACK, 
    READ_CALLBACK, 
    COMPLETE, 
    TERMINATED, 
    WAIT;
    

    private Action() {}
  }
  
  public static enum Async
  {
    NOT_ASYNC, 
    STARTED, 
    DISPATCH, 
    COMPLETE, 
    EXPIRING, 
    EXPIRED, 
    ERRORING, 
    ERRORED;
    
    private Async() {} }
  private final boolean DEBUG = LOG.isDebugEnabled();
  private final Locker _locker = new Locker();
  
  private final HttpChannel _channel;
  private List<AsyncListener> _asyncListeners;
  private State _state;
  private Async _async;
  private boolean _initial;
  private boolean _asyncReadPossible;
  private boolean _asyncReadUnready;
  private boolean _asyncWrite;
  private long _timeoutMs = DEFAULT_TIMEOUT;
  private AsyncContextEvent _event;
  
  protected HttpChannelState(HttpChannel channel)
  {
    _channel = channel;
    _state = State.IDLE;
    _async = Async.NOT_ASYNC;
    _initial = true;
  }
  
  public State getState()
  {
    Locker.Lock lock = _locker.lock();Throwable localThrowable3 = null;
    try {
      return _state;
    }
    catch (Throwable localThrowable4)
    {
      localThrowable3 = localThrowable4;throw localThrowable4;
    }
    finally {
      if (lock != null) if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else lock.close();
    }
  }
  
  public void addListener(AsyncListener listener) {
    Locker.Lock lock = _locker.lock();Throwable localThrowable3 = null;
    try {
      if (_asyncListeners == null)
        _asyncListeners = new ArrayList();
      _asyncListeners.add(listener);
    }
    catch (Throwable localThrowable1)
    {
      localThrowable3 = localThrowable1;throw localThrowable1;

    }
    finally
    {
      if (lock != null) if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else lock.close();
    }
  }
  
  public void setTimeout(long ms) {
    Locker.Lock lock = _locker.lock();Throwable localThrowable3 = null;
    try {
      _timeoutMs = ms;
    }
    catch (Throwable localThrowable1)
    {
      localThrowable3 = localThrowable1;throw localThrowable1;
    }
    finally {
      if (lock != null) if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else lock.close();
    }
  }
  
  public long getTimeout() {
    Locker.Lock lock = _locker.lock();Throwable localThrowable3 = null;
    try {
      return _timeoutMs;
    }
    catch (Throwable localThrowable1)
    {
      localThrowable3 = localThrowable1;throw localThrowable1;
    }
    finally {
      if (lock != null) if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else lock.close();
    }
  }
  
  public AsyncContextEvent getAsyncContextEvent() {
    Locker.Lock lock = _locker.lock();Throwable localThrowable3 = null;
    try {
      return _event;
    }
    catch (Throwable localThrowable4)
    {
      localThrowable3 = localThrowable4;throw localThrowable4;
    }
    finally {
      if (lock != null) if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else lock.close();
    }
  }
  
  public String toString()
  {
    Locker.Lock lock = _locker.lock();Throwable localThrowable3 = null;
    try {
      return toStringLocked();
    }
    catch (Throwable localThrowable4)
    {
      localThrowable3 = localThrowable4;throw localThrowable4;
    }
    finally {
      if (lock != null) if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else lock.close();
    }
  }
  
  public String toStringLocked() {
    return String.format("%s@%x{s=%s a=%s i=%b r=%s w=%b}", new Object[] { getClass().getSimpleName(), Integer.valueOf(hashCode()), _state, _async, Boolean.valueOf(_initial), _asyncReadUnready ? "!PU" : _asyncReadPossible ? "P!U" : _asyncReadUnready ? "PU" : "!P!U", 
    
      Boolean.valueOf(_asyncWrite) });
  }
  

  private String getStatusStringLocked()
  {
    return String.format("s=%s i=%b a=%s", new Object[] { _state, Boolean.valueOf(_initial), _async });
  }
  
  public String getStatusString()
  {
    Locker.Lock lock = _locker.lock();Throwable localThrowable3 = null;
    try {
      return getStatusStringLocked();
    }
    catch (Throwable localThrowable4)
    {
      localThrowable3 = localThrowable4;throw localThrowable4;
    }
    finally {
      if (lock != null) { if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else { lock.close();
        }
      }
    }
  }
  
  protected Action handling()
  {
    Locker.Lock lock = _locker.lock();Throwable localThrowable12 = null;
    try {
      if (DEBUG)
        LOG.debug("handling {}", new Object[] { toStringLocked() });
      Action localAction;
      switch (5.$SwitchMap$org$eclipse$jetty$server$HttpChannelState$State[_state.ordinal()])
      {
      case 1: 
        _initial = true;
        _state = State.DISPATCHED;
        return Action.DISPATCH;
      
      case 2: 
      case 3: 
        return Action.TERMINATED;
      
      case 4: 
        if (_asyncReadPossible)
        {
          _state = State.ASYNC_IO;
          _asyncReadUnready = false;
          return Action.READ_CALLBACK;
        }
        
        if (_asyncWrite)
        {
          _state = State.ASYNC_IO;
          _asyncWrite = false;
          return Action.WRITE_CALLBACK;
        }
        
        switch (5.$SwitchMap$org$eclipse$jetty$server$HttpChannelState$Async[_async.ordinal()])
        {
        case 1: 
          _state = State.COMPLETING;
          return Action.COMPLETE;
        case 2: 
          _state = State.DISPATCHED;
          _async = Async.NOT_ASYNC;
          return Action.ASYNC_DISPATCH;
        case 3: 
        case 4: 
          _state = State.DISPATCHED;
          _async = Async.NOT_ASYNC;
          return Action.ERROR_DISPATCH;
        case 5: 
        case 6: 
        case 7: 
          return Action.WAIT;
        case 8: 
          break;
        default: 
          throw new IllegalStateException(getStatusStringLocked());
        }
        
        return Action.WAIT;
      
      case 5: 
        return Action.ASYNC_ERROR;
      }
      
      



      throw new IllegalStateException(getStatusStringLocked());
    }
    catch (Throwable localThrowable13)
    {
      localThrowable12 = localThrowable13;throw localThrowable13;
































    }
    finally
    {
































      if (lock != null) if (localThrowable12 != null) try { lock.close(); } catch (Throwable localThrowable11) { localThrowable12.addSuppressed(localThrowable11); } else { lock.close();
        }
    }
  }
  
  public void startAsync(final AsyncContextEvent event)
  {
    Locker.Lock lock = _locker.lock();Throwable localThrowable3 = null;
    try {
      if (DEBUG)
        LOG.debug("startAsync {}", new Object[] { toStringLocked() });
      if ((_state != State.DISPATCHED) || (_async != Async.NOT_ASYNC)) {
        throw new IllegalStateException(getStatusStringLocked());
      }
      _async = Async.STARTED;
      _event = event;
      List<AsyncListener> lastAsyncListeners = _asyncListeners;
      _asyncListeners = null;
    }
    catch (Throwable localThrowable1)
    {
      localThrowable3 = localThrowable1;throw localThrowable1;




    }
    finally
    {



      if (lock != null) if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else lock.close(); }
    final List<AsyncListener> lastAsyncListeners;
    if (lastAsyncListeners != null)
    {
      Runnable callback = new Runnable()
      {

        public void run()
        {
          for (AsyncListener listener : lastAsyncListeners)
          {
            try
            {
              listener.onStartAsync(event);

            }
            catch (Throwable e)
            {
              HttpChannelState.LOG.warn(e);
            }
          }
        }
        
        public String toString()
        {
          return "startAsync";
        }
        
      };
      runInContext(event, callback);
    }
  }
  

  public void asyncError(Throwable failure)
  {
    AsyncContextEvent event = null;
    Locker.Lock lock = _locker.lock();Throwable localThrowable3 = null;
    try {
      switch (5.$SwitchMap$org$eclipse$jetty$server$HttpChannelState$State[_state.ordinal()])
      {
      case 1: 
      case 2: 
      case 3: 
      case 4: 
      case 5: 
      case 6: 
      case 8: 
      case 9: 
        break;
      


      case 7: 
        _event.addThrowable(failure);
        _state = State.ASYNC_ERROR;
        event = _event;
        break;
      

      default: 
        throw new IllegalStateException(getStatusStringLocked());
      }
    }
    catch (Throwable localThrowable1)
    {
      localThrowable3 = localThrowable1;throw localThrowable1;












    }
    finally
    {











      if (lock != null) if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else lock.close();
    }
    if (event != null)
    {
      cancelTimeout(event);
      runInContext(event, _channel);
    }
  }
  








  protected Action unhandle()
  {
    boolean read_interested = false;
    
    Locker.Lock lock = _locker.lock();Throwable localThrowable5 = null;
    try {
      if (DEBUG)
        LOG.debug("unhandle {}", new Object[] { toStringLocked() });
      Action localAction1;
      switch (5.$SwitchMap$org$eclipse$jetty$server$HttpChannelState$State[_state.ordinal()])
      {
      case 2: 
      case 3: 
        return Action.TERMINATED;
      
      case 10: 
        _state = State.DISPATCHED;
        return Action.ERROR_DISPATCH;
      case 5: 
      case 6: 
      case 8: 
        break;
      case 4: case 7: 
      case 9: 
      default: 
        throw new IllegalStateException(getStatusStringLocked());
      }
      
      _initial = false;
      Action action; Action action; Action action; Action action; Action action; Action action; switch (5.$SwitchMap$org$eclipse$jetty$server$HttpChannelState$Async[_async.ordinal()])
      {
      case 1: 
        _state = State.COMPLETING;
        _async = Async.NOT_ASYNC;
        action = Action.COMPLETE;
        break;
      
      case 2: 
        _state = State.DISPATCHED;
        _async = Async.NOT_ASYNC;
        action = Action.ASYNC_DISPATCH;
        break;
      case 5: 
        Action action;
        if ((_asyncReadUnready) && (_asyncReadPossible))
        {
          _state = State.ASYNC_IO;
          _asyncReadUnready = false;
          action = Action.READ_CALLBACK;
        } else { Action action;
          if (_asyncWrite)
          {
            _asyncWrite = false;
            _state = State.ASYNC_IO;
            action = Action.WRITE_CALLBACK;
          }
          else
          {
            _state = State.ASYNC_WAIT;
            Action action = Action.WAIT;
            if (_asyncReadUnready)
              read_interested = true;
            Scheduler scheduler = _channel.getScheduler();
            if ((scheduler != null) && (_timeoutMs > 0L))
              _event.setTimeoutTask(scheduler.schedule(_event, _timeoutMs, TimeUnit.MILLISECONDS));
          } }
        break;
      

      case 6: 
        _state = State.ASYNC_WAIT;
        action = Action.WAIT;
        break;
      


      case 3: 
        _state = State.DISPATCHED;
        _async = Async.NOT_ASYNC;
        action = Action.ERROR_DISPATCH;
        break;
      
      case 4: 
        _state = State.DISPATCHED;
        _async = Async.NOT_ASYNC;
        action = Action.ERROR_DISPATCH;
        break;
      
      case 8: 
        _state = State.COMPLETING;
        action = Action.COMPLETE;
        break;
      case 7: 
      default: 
        _state = State.COMPLETING;
        action = Action.COMPLETE;
      }
    }
    catch (Throwable localThrowable7)
    {
      Action action;
      localThrowable5 = localThrowable7;throw localThrowable7;













































    }
    finally
    {













































      if (lock != null) if (localThrowable5 != null) try { lock.close(); } catch (Throwable localThrowable4) { localThrowable5.addSuppressed(localThrowable4); } else lock.close(); }
    Action action;
    if (read_interested) {
      _channel.asyncReadFillInterested();
    }
    return action;
  }
  
  public void dispatch(ServletContext context, String path)
  {
    boolean dispatch = false;
    
    Locker.Lock lock = _locker.lock();Throwable localThrowable3 = null;
    try {
      if (DEBUG) {
        LOG.debug("dispatch {} -> {}", new Object[] { toStringLocked(), path });
      }
      boolean started = false;
      AsyncContextEvent event = _event;
      switch (5.$SwitchMap$org$eclipse$jetty$server$HttpChannelState$Async[_async.ordinal()])
      {
      case 5: 
        started = true;
        break;
      case 4: 
      case 6: 
      case 7: 
        break;
      default: 
        throw new IllegalStateException(getStatusStringLocked());
      }
      _async = Async.DISPATCH;
      
      if (context != null)
        _event.setDispatchContext(context);
      if (path != null) {
        _event.setDispatchPath(path);
      }
      if (started)
      {
        switch (5.$SwitchMap$org$eclipse$jetty$server$HttpChannelState$State[_state.ordinal()])
        {
        case 4: 
        case 6: 
        case 8: 
          break;
        case 7: 
          _state = State.ASYNC_WOKEN;
          dispatch = true;
          break;
        case 5: default: 
          LOG.warn("async dispatched when complete {}", new Object[] { this });
        }
      }
    }
    catch (Throwable localThrowable1)
    {
      localThrowable3 = localThrowable1;throw localThrowable1;




















    }
    finally
    {



















      if (lock != null) if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else lock.close(); }
    AsyncContextEvent event;
    cancelTimeout(event);
    if (dispatch) {
      scheduleDispatch();
    }
  }
  

  protected void onTimeout()
  {
    Locker.Lock lock = _locker.lock();Throwable localThrowable8 = null;
    try {
      if (DEBUG) {
        LOG.debug("onTimeout {}", new Object[] { toStringLocked() });
      }
      if (_async != Async.STARTED)
        return;
      _async = Async.EXPIRING;
      AsyncContextEvent event = _event;
      listeners = _asyncListeners;
    }
    catch (Throwable localThrowable3)
    {
      List<AsyncListener> listeners;
      localThrowable8 = localThrowable3;throw localThrowable3;




    }
    finally
    {



      if (lock != null) if (localThrowable8 != null) try { lock.close(); } catch (Throwable localThrowable4) { localThrowable8.addSuppressed(localThrowable4); } else lock.close(); }
    final List<AsyncListener> listeners;
    final AsyncContextEvent event; final AtomicReference<Throwable> error = new AtomicReference();
    if (listeners != null)
    {
      Object task = new Runnable()
      {

        public void run()
        {
          for (AsyncListener listener : listeners)
          {
            try
            {
              listener.onTimeout(event);
            }
            catch (Throwable x)
            {
              HttpChannelState.LOG.warn(x + " while invoking onTimeout listener " + listener, new Object[0]);
              HttpChannelState.LOG.debug(x);
              if (error.get() == null) {
                error.set(x);
              } else {
                ((Throwable)error.get()).addSuppressed(x);
              }
            }
          }
        }
        
        public String toString() {
          return "onTimeout";
        }
        
      };
      runInContext(event, (Runnable)task);
    }
    
    Throwable th = (Throwable)error.get();
    boolean dispatch = false;
    Locker.Lock lock = _locker.lock();localThrowable4 = null;
    try {
      switch (_async)
      {
      case EXPIRING: 
        _async = (th == null ? Async.EXPIRED : Async.ERRORING);
        break;
      
      case COMPLETE: 
      case DISPATCH: 
        if (th != null)
        {
          LOG.ignore(th);
          th = null;
        }
        
        break;
      default: 
        throw new IllegalStateException();
      }
      
      if (_state == State.ASYNC_WAIT)
      {
        _state = State.ASYNC_WOKEN;
        dispatch = true;
      }
    }
    catch (Throwable localThrowable6)
    {
      localThrowable4 = localThrowable6;throw localThrowable6;











    }
    finally
    {











      if (lock != null) if (localThrowable4 != null) try { lock.close(); } catch (Throwable localThrowable7) { localThrowable4.addSuppressed(localThrowable7); } else lock.close();
    }
    if (th != null)
    {
      if (LOG.isDebugEnabled())
        LOG.debug("Error after async timeout {}", new Object[] { this, th });
      onError(th);
    }
    
    if (dispatch)
    {
      if (LOG.isDebugEnabled())
        LOG.debug("Dispatch after async timeout {}", new Object[] { this });
      scheduleDispatch();
    }
  }
  


  public void complete()
  {
    boolean handle = false;
    
    Locker.Lock lock = _locker.lock();Throwable localThrowable4 = null;
    try {
      if (DEBUG) {
        LOG.debug("complete {}", new Object[] { toStringLocked() });
      }
      boolean started = false;
      AsyncContextEvent event = _event;
      
      switch (5.$SwitchMap$org$eclipse$jetty$server$HttpChannelState$Async[_async.ordinal()])
      {
      case 5: 
        started = true;
        break;
      case 4: 
      case 6: 
      case 7: 
        break;
      case 1: 
        return;
      case 2: case 3: default: 
        throw new IllegalStateException(getStatusStringLocked());
      }
      _async = Async.COMPLETE;
      
      if ((started) && (_state == State.ASYNC_WAIT))
      {
        handle = true;
        _state = State.ASYNC_WOKEN;
      }
    }
    catch (Throwable localThrowable2)
    {
      localThrowable4 = localThrowable2;throw localThrowable2;













    }
    finally
    {












      if (lock != null) if (localThrowable4 != null) try { lock.close(); } catch (Throwable localThrowable3) { localThrowable4.addSuppressed(localThrowable3); } else lock.close(); }
    AsyncContextEvent event;
    cancelTimeout(event);
    if (handle) {
      runInContext(event, _channel);
    }
  }
  
  public void errorComplete() {
    Locker.Lock lock = _locker.lock();Throwable localThrowable3 = null;
    try {
      if (DEBUG) {
        LOG.debug("error complete {}", new Object[] { toStringLocked() });
      }
      _async = Async.COMPLETE;
      _event.setDispatchContext(null);
      _event.setDispatchPath(null);
    }
    catch (Throwable localThrowable1)
    {
      localThrowable3 = localThrowable1;throw localThrowable1;


    }
    finally
    {


      if (lock != null) if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else lock.close();
    }
    cancelTimeout();
  }
  


  protected void onError(Throwable failure)
  {
    Request baseRequest = _channel.getRequest();
    
    int code = 500;
    String reason = null;
    if ((failure instanceof BadMessageException))
    {
      BadMessageException bme = (BadMessageException)failure;
      code = bme.getCode();
      reason = bme.getReason();
    }
    else if ((failure instanceof UnavailableException))
    {
      if (((UnavailableException)failure).isPermanent()) {
        code = 404;
      } else {
        code = 503;
      }
    }
    Locker.Lock lock = _locker.lock();Throwable localThrowable8 = null;
    try {
      if (DEBUG) {
        LOG.debug("onError {} {}", new Object[] { toStringLocked(), failure });
      }
      
      if (_event != null)
      {
        _event.addThrowable(failure);
        _event.getSuppliedRequest().setAttribute("javax.servlet.error.status_code", Integer.valueOf(code));
        _event.getSuppliedRequest().setAttribute("javax.servlet.error.exception", failure);
        _event.getSuppliedRequest().setAttribute("javax.servlet.error.exception_type", failure == null ? null : failure.getClass());
        
        _event.getSuppliedRequest().setAttribute("javax.servlet.error.message", reason != null ? reason : null);
      }
      else
      {
        Throwable error = (Throwable)baseRequest.getAttribute("javax.servlet.error.exception");
        if (error != null)
          throw new IllegalStateException("Error already set", error);
        baseRequest.setAttribute("javax.servlet.error.status_code", Integer.valueOf(code));
        baseRequest.setAttribute("javax.servlet.error.exception", failure);
        baseRequest.setAttribute("javax.servlet.error.exception_type", failure == null ? null : failure.getClass());
        baseRequest.setAttribute("javax.servlet.error.message", reason != null ? reason : null);
      }
      

      if (_async == Async.NOT_ASYNC)
      {

        if (_state == State.DISPATCHED)
        {
          _state = State.THROWN;
          return;
        }
        throw new IllegalStateException(getStatusStringLocked());
      }
      

      _async = Async.ERRORING;
      List<AsyncListener> listeners = _asyncListeners;
      event = _event;
    }
    catch (Throwable localThrowable3)
    {
      AsyncContextEvent event;
      localThrowable8 = localThrowable3;throw localThrowable3;



















    }
    finally
    {



















      if (lock != null) if (localThrowable8 != null) try { lock.close(); } catch (Throwable localThrowable4) { localThrowable8.addSuppressed(localThrowable4); } else lock.close(); }
    final AsyncContextEvent event;
    final List<AsyncListener> listeners; if (listeners != null)
    {
      Runnable task = new Runnable()
      {

        public void run()
        {
          for (AsyncListener listener : listeners)
          {
            try
            {
              listener.onError(event);
            }
            catch (Throwable x)
            {
              HttpChannelState.LOG.warn(x + " while invoking onError listener " + listener, new Object[0]);
              HttpChannelState.LOG.debug(x);
            }
          }
        }
        

        public String toString()
        {
          return "onError";
        }
      };
      runInContext(event, task);
    }
    
    boolean dispatch = false;
    Locker.Lock lock = _locker.lock();localThrowable3 = null;
    try {
      switch (_async)
      {



      case ERRORING: 
        _async = Async.ERRORED;
        break;
      

      case COMPLETE: 
      case DISPATCH: 
        break;
      


      default: 
        throw new IllegalStateException(toString());
      }
      
      
      if (_state == State.ASYNC_WAIT)
      {
        _state = State.ASYNC_WOKEN;
        dispatch = true;
      }
    }
    catch (Throwable localThrowable10)
    {
      localThrowable3 = localThrowable10;throw localThrowable10;












    }
    finally
    {












      if (lock != null) if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable7) { localThrowable3.addSuppressed(localThrowable7); } else lock.close();
    }
    if (dispatch)
    {
      if (LOG.isDebugEnabled())
        LOG.debug("Dispatch after error {}", new Object[] { this });
      scheduleDispatch();
    }
  }
  



  protected void onComplete()
  {
    Locker.Lock lock = _locker.lock();Throwable localThrowable3 = null;
    try {
      if (DEBUG) {
        LOG.debug("onComplete {}", new Object[] { toStringLocked() });
      }
      switch (_state)
      {
      case COMPLETING: 
        List<AsyncListener> aListeners = _asyncListeners;
        AsyncContextEvent event = _event;
        _state = State.COMPLETED;
        _async = Async.NOT_ASYNC;
        break;
      
      default: 
        throw new IllegalStateException(getStatusStringLocked());
      }
    }
    catch (Throwable localThrowable1)
    {
      AsyncContextEvent event;
      List<AsyncListener> aListeners;
      localThrowable3 = localThrowable1;throw localThrowable1;







    }
    finally
    {






      if (lock != null) if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else lock.close(); }
    final AsyncContextEvent event;
    final List<AsyncListener> aListeners; if (event != null)
    {
      if (aListeners != null)
      {
        Runnable callback = new Runnable()
        {

          public void run()
          {
            for (AsyncListener listener : aListeners)
            {
              try
              {
                listener.onComplete(event);
              }
              catch (Throwable e)
              {
                HttpChannelState.LOG.warn(e + " while invoking onComplete listener " + listener, new Object[0]);
                HttpChannelState.LOG.debug(e);
              }
            }
          }
          
          public String toString()
          {
            return "onComplete";
          }
          
        };
        runInContext(event, callback);
      }
      event.completed();
    }
  }
  
  protected void recycle()
  {
    cancelTimeout();
    Locker.Lock lock = _locker.lock();Throwable localThrowable4 = null;
    try {
      if (DEBUG) {
        LOG.debug("recycle {}", new Object[] { toStringLocked() });
      }
      switch (5.$SwitchMap$org$eclipse$jetty$server$HttpChannelState$State[_state.ordinal()])
      {
      case 6: 
      case 8: 
        throw new IllegalStateException(getStatusStringLocked());
      case 9: 
        return;
      }
      
      
      _asyncListeners = null;
      _state = State.IDLE;
      _async = Async.NOT_ASYNC;
      _initial = true;
      _asyncReadPossible = (this._asyncReadUnready = 0);
      _asyncWrite = false;
      _timeoutMs = DEFAULT_TIMEOUT;
      _event = null;
    }
    catch (Throwable localThrowable2)
    {
      localThrowable4 = localThrowable2;throw localThrowable2;










    }
    finally
    {









      if (lock != null) if (localThrowable4 != null) try { lock.close(); } catch (Throwable localThrowable3) { localThrowable4.addSuppressed(localThrowable3); } else lock.close();
    }
  }
  
  public void upgrade() {
    cancelTimeout();
    Locker.Lock lock = _locker.lock();Throwable localThrowable3 = null;
    try {
      if (DEBUG) {
        LOG.debug("upgrade {}", new Object[] { toStringLocked() });
      }
      switch (_state)
      {
      case IDLE: 
      case COMPLETED: 
        break;
      default: 
        throw new IllegalStateException(getStatusStringLocked());
      }
      _asyncListeners = null;
      _state = State.UPGRADED;
      _async = Async.NOT_ASYNC;
      _initial = true;
      _asyncReadPossible = (this._asyncReadUnready = 0);
      _asyncWrite = false;
      _timeoutMs = DEFAULT_TIMEOUT;
      _event = null;
    }
    catch (Throwable localThrowable1)
    {
      localThrowable3 = localThrowable1;throw localThrowable1;









    }
    finally
    {








      if (lock != null) if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else lock.close();
    }
  }
  
  protected void scheduleDispatch() {
    _channel.execute(_channel);
  }
  

  protected void cancelTimeout()
  {
    Locker.Lock lock = _locker.lock();Throwable localThrowable3 = null;
    try {
      event = _event;
    }
    catch (Throwable localThrowable1)
    {
      AsyncContextEvent event;
      localThrowable3 = localThrowable1;throw localThrowable1;
    }
    finally {
      if (lock != null) if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else lock.close(); }
    AsyncContextEvent event; cancelTimeout(event);
  }
  
  protected void cancelTimeout(AsyncContextEvent event)
  {
    if (event != null) {
      event.cancelTimeoutTask();
    }
  }
  
  public boolean isIdle() {
    Locker.Lock lock = _locker.lock();Throwable localThrowable3 = null;
    try {
      return _state == State.IDLE;
    }
    catch (Throwable localThrowable4)
    {
      localThrowable3 = localThrowable4;throw localThrowable4;
    }
    finally {
      if (lock != null) if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else lock.close();
    }
  }
  
  public boolean isExpired() {
    Locker.Lock lock = _locker.lock();Throwable localThrowable3 = null;
    try {
      return _async == Async.EXPIRED;
    }
    catch (Throwable localThrowable4)
    {
      localThrowable3 = localThrowable4;throw localThrowable4;
    }
    finally {
      if (lock != null) if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else lock.close();
    }
  }
  
  public boolean isInitial() {
    Locker.Lock lock = _locker.lock();Throwable localThrowable3 = null;
    try {
      return _initial;
    }
    catch (Throwable localThrowable4)
    {
      localThrowable3 = localThrowable4;throw localThrowable4;
    }
    finally {
      if (lock != null) if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else lock.close();
    }
  }
  
  public boolean isSuspended() {
    Locker.Lock lock = _locker.lock();Throwable localThrowable3 = null;
    try {
      return (_state == State.ASYNC_WAIT) || ((_state == State.DISPATCHED) && (_async == Async.STARTED));
    }
    catch (Throwable localThrowable4)
    {
      localThrowable3 = localThrowable4;throw localThrowable4;
    }
    finally {
      if (lock != null) if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else lock.close();
    }
  }
  
  boolean isCompleting() {
    Locker.Lock lock = _locker.lock();Throwable localThrowable3 = null;
    try {
      return _state == State.COMPLETING;
    }
    catch (Throwable localThrowable4)
    {
      localThrowable3 = localThrowable4;throw localThrowable4;
    }
    finally {
      if (lock != null) if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else lock.close();
    }
  }
  
  boolean isCompleted() {
    Locker.Lock lock = _locker.lock();Throwable localThrowable3 = null;
    try {
      return _state == State.COMPLETED;
    }
    catch (Throwable localThrowable4)
    {
      localThrowable3 = localThrowable4;throw localThrowable4;
    }
    finally {
      if (lock != null) if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else lock.close();
    }
  }
  
  public boolean isAsyncStarted() {
    Locker.Lock lock = _locker.lock();Throwable localThrowable4 = null;
    try { boolean bool;
      if (_state == State.DISPATCHED)
        return _async != Async.NOT_ASYNC;
      return (_async == Async.STARTED) || (_async == Async.EXPIRING);
    }
    catch (Throwable localThrowable5)
    {
      localThrowable4 = localThrowable5;throw localThrowable5;

    }
    finally
    {
      if (lock != null) if (localThrowable4 != null) try { lock.close(); } catch (Throwable localThrowable3) { localThrowable4.addSuppressed(localThrowable3); } else lock.close();
    }
  }
  
  public boolean isAsyncComplete() {
    Locker.Lock lock = _locker.lock();Throwable localThrowable3 = null;
    try {
      return _async == Async.COMPLETE;
    }
    catch (Throwable localThrowable4)
    {
      localThrowable3 = localThrowable4;throw localThrowable4;
    }
    finally {
      if (lock != null) if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else lock.close();
    }
  }
  
  public boolean isAsync() {
    Locker.Lock lock = _locker.lock();Throwable localThrowable3 = null;
    try {
      return (!_initial) || (_async != Async.NOT_ASYNC);
    }
    catch (Throwable localThrowable4)
    {
      localThrowable3 = localThrowable4;throw localThrowable4;
    }
    finally {
      if (lock != null) if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else lock.close();
    }
  }
  
  public Request getBaseRequest() {
    return _channel.getRequest();
  }
  
  public HttpChannel getHttpChannel()
  {
    return _channel;
  }
  

  public ContextHandler getContextHandler()
  {
    Locker.Lock lock = _locker.lock();Throwable localThrowable3 = null;
    try {
      event = _event;
    }
    catch (Throwable localThrowable1)
    {
      AsyncContextEvent event;
      localThrowable3 = localThrowable1;throw localThrowable1;
    }
    finally {
      if (lock != null) if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else lock.close(); }
    AsyncContextEvent event; return getContextHandler(event);
  }
  
  ContextHandler getContextHandler(AsyncContextEvent event)
  {
    if (event != null)
    {
      ContextHandler.Context context = (ContextHandler.Context)event.getServletContext();
      if (context != null)
        return context.getContextHandler();
    }
    return null;
  }
  

  public ServletResponse getServletResponse()
  {
    Locker.Lock lock = _locker.lock();Throwable localThrowable3 = null;
    try {
      event = _event;
    }
    catch (Throwable localThrowable1)
    {
      AsyncContextEvent event;
      localThrowable3 = localThrowable1;throw localThrowable1;
    }
    finally {
      if (lock != null) if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else lock.close(); }
    AsyncContextEvent event; return getServletResponse(event);
  }
  
  public ServletResponse getServletResponse(AsyncContextEvent event)
  {
    if ((event != null) && (event.getSuppliedResponse() != null))
      return event.getSuppliedResponse();
    return _channel.getResponse();
  }
  
  void runInContext(AsyncContextEvent event, Runnable runnable)
  {
    ContextHandler contextHandler = getContextHandler(event);
    if (contextHandler == null) {
      runnable.run();
    } else {
      contextHandler.handle(_channel.getRequest(), runnable);
    }
  }
  
  public Object getAttribute(String name) {
    return _channel.getRequest().getAttribute(name);
  }
  
  public void removeAttribute(String name)
  {
    _channel.getRequest().removeAttribute(name);
  }
  
  public void setAttribute(String name, Object attribute)
  {
    _channel.getRequest().setAttribute(name, attribute);
  }
  








  public void onReadUnready()
  {
    boolean interested = false;
    Locker.Lock lock = _locker.lock();Throwable localThrowable3 = null;
    try {
      if (DEBUG) {
        LOG.debug("onReadUnready {}", new Object[] { toStringLocked() });
      }
      
      if (!_asyncReadUnready)
      {
        _asyncReadUnready = true;
        _asyncReadPossible = false;
        if (_state == State.ASYNC_WAIT) {
          interested = true;
        }
      }
    }
    catch (Throwable localThrowable1)
    {
      localThrowable3 = localThrowable1;throw localThrowable1;





    }
    finally
    {




      if (lock != null) if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else lock.close();
    }
    if (interested) {
      _channel.asyncReadFillInterested();
    }
  }
  






  public boolean onReadPossible()
  {
    boolean woken = false;
    Locker.Lock lock = _locker.lock();Throwable localThrowable3 = null;
    try {
      if (DEBUG) {
        LOG.debug("onReadPossible {}", new Object[] { toStringLocked() });
      }
      _asyncReadPossible = true;
      if ((_state == State.ASYNC_WAIT) && (_asyncReadUnready))
      {
        woken = true;
        _state = State.ASYNC_WOKEN;
      }
    }
    catch (Throwable localThrowable1)
    {
      localThrowable3 = localThrowable1;throw localThrowable1;




    }
    finally
    {



      if (lock != null) if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else lock.close(); }
    return woken;
  }
  







  public boolean onReadReady()
  {
    boolean woken = false;
    Locker.Lock lock = _locker.lock();Throwable localThrowable3 = null;
    try {
      if (DEBUG) {
        LOG.debug("onReadReady {}", new Object[] { toStringLocked() });
      }
      _asyncReadUnready = true;
      _asyncReadPossible = true;
      if (_state == State.ASYNC_WAIT)
      {
        woken = true;
        _state = State.ASYNC_WOKEN;
      }
    }
    catch (Throwable localThrowable1)
    {
      localThrowable3 = localThrowable1;throw localThrowable1;




    }
    finally
    {




      if (lock != null) if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else lock.close(); }
    return woken;
  }
  





  public boolean onReadEof()
  {
    boolean woken = false;
    Locker.Lock lock = _locker.lock();Throwable localThrowable3 = null;
    try {
      if (DEBUG) {
        LOG.debug("onReadEof {}", new Object[] { toStringLocked() });
      }
      if (_state == State.ASYNC_WAIT)
      {
        _state = State.ASYNC_WOKEN;
        _asyncReadUnready = true;
        _asyncReadPossible = true;
        woken = true;
      }
    }
    catch (Throwable localThrowable1)
    {
      localThrowable3 = localThrowable1;throw localThrowable1;




    }
    finally
    {




      if (lock != null) if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else lock.close(); }
    return woken;
  }
  

  public boolean isReadPossible()
  {
    Locker.Lock lock = _locker.lock();Throwable localThrowable3 = null;
    try {
      return _asyncReadPossible;
    }
    catch (Throwable localThrowable4)
    {
      localThrowable3 = localThrowable4;throw localThrowable4;
    }
    finally {
      if (lock != null) if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else lock.close();
    }
  }
  
  public boolean onWritePossible() {
    boolean handle = false;
    
    Locker.Lock lock = _locker.lock();Throwable localThrowable3 = null;
    try {
      if (DEBUG) {
        LOG.debug("onWritePossible {}", new Object[] { toStringLocked() });
      }
      _asyncWrite = true;
      if (_state == State.ASYNC_WAIT)
      {
        _state = State.ASYNC_WOKEN;
        handle = true;
      }
    }
    catch (Throwable localThrowable1)
    {
      localThrowable3 = localThrowable1;throw localThrowable1;




    }
    finally
    {



      if (lock != null) if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else lock.close();
    }
    return handle;
  }
}
