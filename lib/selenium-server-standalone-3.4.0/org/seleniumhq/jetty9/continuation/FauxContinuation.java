package org.seleniumhq.jetty9.continuation;

import java.util.ArrayList;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletResponseWrapper;































class FauxContinuation
  implements ContinuationFilter.FilteredContinuation
{
  private static final ContinuationThrowable __exception = new ContinuationThrowable();
  
  private static final int __HANDLING = 1;
  
  private static final int __SUSPENDING = 2;
  
  private static final int __RESUMING = 3;
  private static final int __COMPLETING = 4;
  private static final int __SUSPENDED = 5;
  private static final int __UNSUSPENDING = 6;
  private static final int __COMPLETE = 7;
  private final ServletRequest _request;
  private ServletResponse _response;
  private int _state = 1;
  private boolean _initial = true;
  private boolean _resumed = false;
  private boolean _timeout = false;
  private boolean _responseWrapped = false;
  private long _timeoutMs = 30000L;
  
  private ArrayList<ContinuationListener> _listeners;
  
  FauxContinuation(ServletRequest request)
  {
    _request = request;
  }
  

  public void onComplete()
  {
    if (_listeners != null) {
      for (ContinuationListener l : _listeners) {
        l.onComplete(this);
      }
    }
  }
  
  public void onTimeout() {
    if (_listeners != null) {
      for (ContinuationListener l : _listeners) {
        l.onTimeout(this);
      }
    }
  }
  



  public boolean isResponseWrapped()
  {
    return _responseWrapped;
  }
  


  public boolean isInitial()
  {
    synchronized (this)
    {
      return _initial;
    }
  }
  


  public boolean isResumed()
  {
    synchronized (this)
    {
      return _resumed;
    }
  }
  


  public boolean isSuspended()
  {
    synchronized (this)
    {
      switch (_state)
      {
      case 1: 
        return false;
      case 2: 
      case 3: 
      case 4: 
      case 5: 
        return true;
      }
      
      return false;
    }
  }
  



  public boolean isExpired()
  {
    synchronized (this)
    {
      return _timeout;
    }
  }
  


  public void setTimeout(long timeoutMs)
  {
    _timeoutMs = timeoutMs;
  }
  


  public void suspend(ServletResponse response)
  {
    _response = response;
    _responseWrapped = (response instanceof ServletResponseWrapper);
    suspend();
  }
  


  public void suspend()
  {
    synchronized (this)
    {
      switch (_state)
      {
      case 1: 
        _timeout = false;
        _resumed = false;
        _state = 2;
        return;
      
      case 2: 
      case 3: 
        return;
      
      case 4: 
      case 5: 
      case 6: 
        throw new IllegalStateException(getStatusString());
      }
      
      throw new IllegalStateException("" + _state);
    }
  }
  








  public void resume()
  {
    synchronized (this)
    {
      switch (_state)
      {
      case 1: 
        _resumed = true;
        return;
      
      case 2: 
        _resumed = true;
        _state = 3;
        return;
      
      case 3: 
      case 4: 
        
      
      case 5: 
        fauxResume();
        _resumed = true;
        _state = 6;
        break;
      
      case 6: 
        _resumed = true;
        return;
      
      default: 
        throw new IllegalStateException(getStatusString());
      }
      
    }
  }
  




  public void complete()
  {
    synchronized (this)
    {
      switch (_state)
      {
      case 1: 
        throw new IllegalStateException(getStatusString());
      
      case 2: 
        _state = 4;
        break;
      
      case 3: 
        break;
      
      case 4: 
        
      
      case 5: 
        _state = 4;
        fauxResume();
        break;
      
      case 6: 
        
      
      default: 
        throw new IllegalStateException(getStatusString());
      }
      
    }
  }
  




  public boolean enter(ServletResponse response)
  {
    _response = response;
    return true;
  }
  





  public ServletResponse getServletResponse()
  {
    return _response;
  }
  


  void handling()
  {
    synchronized (this)
    {
      _responseWrapped = false;
      switch (_state)
      {
      case 1: 
        throw new IllegalStateException(getStatusString());
      
      case 2: 
      case 3: 
        throw new IllegalStateException(getStatusString());
      
      case 4: 
        return;
      
      case 5: 
        fauxResume();
        _state = 1;
        return;
      
      case 6: 
        _state = 1;
        return;
      }
      
      throw new IllegalStateException("" + _state);
    }
  }
  







  public boolean exit()
  {
    synchronized (this)
    {
      switch (_state)
      {
      case 1: 
        _state = 7;
        onComplete();
        return true;
      
      case 2: 
        _initial = false;
        _state = 5;
        fauxSuspend();
        if ((_state == 5) || (_state == 4))
        {
          onComplete();
          return true;
        }
        
        _initial = false;
        _state = 1;
        return false;
      
      case 3: 
        _initial = false;
        _state = 1;
        return false;
      
      case 4: 
        _initial = false;
        _state = 7;
        onComplete();
        return true;
      }
      
      

      throw new IllegalStateException(getStatusString());
    }
  }
  




  protected void expire()
  {
    synchronized (this)
    {
      _timeout = true;
    }
    
    onTimeout();
    
    synchronized (this)
    {
      switch (_state)
      {
      case 1: 
        
      
      case 2: 
        _timeout = true;
        _state = 3;
        fauxResume();
        return;
      
      case 3: 
        
      
      case 4: 
        
      
      case 5: 
        _timeout = true;
        _state = 6;
        break;
      
      case 6: 
        _timeout = true;
        return;
      
      default: 
        throw new IllegalStateException(getStatusString());
      }
    }
  }
  
  private void fauxSuspend()
  {
    long expire_at = System.currentTimeMillis() + _timeoutMs;
    long wait = _timeoutMs;
    while ((_timeoutMs > 0L) && (wait > 0L))
    {
      try
      {
        wait(wait);
      }
      catch (InterruptedException e)
      {
        break;
      }
      wait = expire_at - System.currentTimeMillis();
    }
    
    if ((_timeoutMs > 0L) && (wait <= 0L)) {
      expire();
    }
  }
  
  private void fauxResume() {
    _timeoutMs = 0L;
    notifyAll();
  }
  

  public String toString()
  {
    return getStatusString();
  }
  
  String getStatusString()
  {
    synchronized (this)
    {
      return (_state == 4 ? "COMPLETING" : _state == 6 ? "UNSUSPENDING" : _state == 3 ? "RESUMING" : _state == 5 ? "SUSPENDED" : _state == 2 ? "SUSPENDING" : _state == 1 ? "HANDLING" : new StringBuilder().append("???").append(_state).toString()) + (_initial ? ",initial" : "") + (_resumed ? ",resumed" : "") + (_timeout ? ",timeout" : "");
    }
  }
  












  public void addContinuationListener(ContinuationListener listener)
  {
    if (_listeners == null)
      _listeners = new ArrayList();
    _listeners.add(listener);
  }
  






  public Object getAttribute(String name)
  {
    return _request.getAttribute(name);
  }
  





  public void removeAttribute(String name)
  {
    _request.removeAttribute(name);
  }
  





  public void setAttribute(String name, Object attribute)
  {
    _request.setAttribute(name, attribute);
  }
  





  public void undispatch()
  {
    if (isSuspended())
    {
      if (ContinuationFilter.__debug)
        throw new ContinuationThrowable();
      throw __exception;
    }
    throw new IllegalStateException("!suspended");
  }
}
