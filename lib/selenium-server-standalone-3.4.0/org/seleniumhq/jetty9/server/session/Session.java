package org.seleniumhq.jetty9.server.session;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSessionActivationListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.http.HttpSessionContext;
import javax.servlet.http.HttpSessionEvent;
import org.seleniumhq.jetty9.io.IdleTimeout;
import org.seleniumhq.jetty9.server.SessionIdManager;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;
import org.seleniumhq.jetty9.util.thread.Locker;
import org.seleniumhq.jetty9.util.thread.Locker.Lock;







































public class Session
  implements SessionHandler.SessionIf
{
  private static final Logger LOG = Log.getLogger("org.seleniumhq.jetty9.server.session");
  
  public static final String SESSION_CREATED_SECURE = "org.seleniumhq.jetty9.security.sessionCreatedSecure";
  
  protected SessionData _sessionData;
  
  protected SessionHandler _handler;
  protected String _extendedId;
  protected long _requests;
  protected boolean _idChanged;
  protected boolean _newSession;
  
  public static enum State
  {
    VALID,  INVALID,  INVALIDATING;
    




    private State() {}
  }
  



  protected State _state = State.VALID;
  protected Locker _lock = new Locker();
  protected boolean _resident = false;
  protected SessionInactivityTimeout _sessionInactivityTimer = null;
  















  public class SessionInactivityTimeout
    extends IdleTimeout
  {
    public SessionInactivityTimeout()
    {
      super();
    }
    





    protected void onIdleExpired(TimeoutException timeout)
    {
      if (Session.LOG.isDebugEnabled()) Session.LOG.debug("Timer expired for session {}", new Object[] { getId() });
      getSessionHandler().sessionInactivityTimerExpired(Session.this);
    }
    








    public boolean isOpen()
    {
      Locker.Lock lock = _lock.lockIfNotHeld();Throwable localThrowable3 = null;
      try {
        return (isValid()) && (isResident());
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
    

    public void setIdleTimeout(long idleTimeout)
    {
      if (Session.LOG.isDebugEnabled()) Session.LOG.debug("setIdleTimeout called: " + idleTimeout, new Object[0]);
      super.setIdleTimeout(idleTimeout);
    }
  }
  










  public Session(SessionHandler handler, HttpServletRequest request, SessionData data)
  {
    _handler = handler;
    _sessionData = data;
    _newSession = true;
    _sessionData.setDirty(true);
    _requests = 1L;
  }
  









  public Session(SessionHandler handler, SessionData data)
  {
    _handler = handler;
    _sessionData = data;
  }
  








  public long getRequests()
  {
    Locker.Lock lock = _lock.lockIfNotHeld();Throwable localThrowable3 = null;
    try {
      return _requests;
    }
    catch (Throwable localThrowable1)
    {
      localThrowable3 = localThrowable1;throw localThrowable1;
    }
    finally {
      if (lock != null) { if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else { lock.close();
        }
      }
    }
  }
  


  public void setExtendedId(String extendedId)
  {
    _extendedId = extendedId;
  }
  

  protected void cookieSet()
  {
    Locker.Lock lock = _lock.lockIfNotHeld();Throwable localThrowable3 = null;
    try {
      _sessionData.setCookieSet(_sessionData.getAccessed());
    }
    catch (Throwable localThrowable1)
    {
      localThrowable3 = localThrowable1;throw localThrowable1;
    }
    finally {
      if (lock != null) if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else lock.close();
    }
  }
  
  protected boolean access(long time) {
    Locker.Lock lock = _lock.lockIfNotHeld();Throwable localThrowable5 = null;
    try {
      if (!isValid())
        return false;
      _newSession = false;
      long lastAccessed = _sessionData.getAccessed();
      _sessionData.setAccessed(time);
      _sessionData.setLastAccessed(lastAccessed);
      _sessionData.calcAndSetExpiry(time);
      boolean bool2; if (isExpiredAt(time))
      {
        invalidate();
        return false;
      }
      _requests += 1L;
      return true;
    }
    catch (Throwable localThrowable6)
    {
      localThrowable5 = localThrowable6;throw localThrowable6;






    }
    finally
    {






      if (lock != null) if (localThrowable5 != null) try { lock.close(); } catch (Throwable localThrowable4) { localThrowable5.addSuppressed(localThrowable4); } else lock.close();
    }
  }
  
  protected void complete()
  {
    Locker.Lock lock = _lock.lockIfNotHeld();Throwable localThrowable3 = null;
    try {
      _requests -= 1L;
    }
    catch (Throwable localThrowable1)
    {
      localThrowable3 = localThrowable1;throw localThrowable1;
    }
    finally {
      if (lock != null) { if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else { lock.close();
        }
      }
    }
  }
  





  protected boolean isExpiredAt(long time)
  {
    Locker.Lock lock = _lock.lockIfNotHeld();Throwable localThrowable3 = null;
    try {
      return _sessionData.isExpiredAt(time);
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
  




  protected boolean isIdleLongerThan(int sec)
  {
    long now = System.currentTimeMillis();
    Locker.Lock lock = _lock.lockIfNotHeld();Throwable localThrowable3 = null;
    try {
      return _sessionData.getAccessed() + sec * 1000 <= now;
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
  








  protected void callSessionAttributeListeners(String name, Object newValue, Object oldValue)
  {
    if ((newValue == null) || (!newValue.equals(oldValue)))
    {
      if (oldValue != null)
        unbindValue(name, oldValue);
      if (newValue != null) {
        bindValue(name, newValue);
      }
      if (_handler == null) {
        throw new IllegalStateException("No session manager for session " + _sessionData.getId());
      }
      _handler.doSessionAttributeListeners(this, name, oldValue, newValue);
    }
  }
  








  public void unbindValue(String name, Object value)
  {
    if ((value != null) && ((value instanceof HttpSessionBindingListener))) {
      ((HttpSessionBindingListener)value).valueUnbound(new HttpSessionBindingEvent(this, name));
    }
  }
  






  public void bindValue(String name, Object value)
  {
    if ((value != null) && ((value instanceof HttpSessionBindingListener))) {
      ((HttpSessionBindingListener)value).valueBound(new HttpSessionBindingEvent(this, name));
    }
  }
  





  public void didActivate()
  {
    HttpSessionEvent event = new HttpSessionEvent(this);
    for (Iterator<String> iter = _sessionData.getKeys().iterator(); iter.hasNext();)
    {
      Object value = _sessionData.getAttribute((String)iter.next());
      if ((value instanceof HttpSessionActivationListener))
      {
        HttpSessionActivationListener listener = (HttpSessionActivationListener)value;
        listener.sessionDidActivate(event);
      }
    }
  }
  






  public void willPassivate()
  {
    HttpSessionEvent event = new HttpSessionEvent(this);
    for (Iterator<String> iter = _sessionData.getKeys().iterator(); iter.hasNext();)
    {
      Object value = _sessionData.getAttribute((String)iter.next());
      if ((value instanceof HttpSessionActivationListener))
      {
        HttpSessionActivationListener listener = (HttpSessionActivationListener)value;
        listener.sessionWillPassivate(event);
      }
    }
  }
  

  public boolean isValid()
  {
    Locker.Lock lock = _lock.lockIfNotHeld();Throwable localThrowable3 = null;
    try {
      return _state == State.VALID;
    }
    catch (Throwable localThrowable4)
    {
      localThrowable3 = localThrowable4;throw localThrowable4;
    }
    finally {
      if (lock != null) if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else { lock.close();
        }
    }
  }
  
  public long getCookieSetTime()
  {
    Locker.Lock lock = _lock.lockIfNotHeld();Throwable localThrowable3 = null;
    try {
      return _sessionData.getCookieSet();
    }
    catch (Throwable localThrowable1)
    {
      localThrowable3 = localThrowable1;throw localThrowable1;
    }
    finally {
      if (lock != null) if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else { lock.close();
        }
    }
  }
  
  public long getCreationTime()
    throws IllegalStateException
  {
    Locker.Lock lock = _lock.lockIfNotHeld();Throwable localThrowable3 = null;
    try {
      checkValidForRead();
      return _sessionData.getCreated();
    }
    catch (Throwable localThrowable1)
    {
      localThrowable3 = localThrowable1;throw localThrowable1;
    }
    finally
    {
      if (lock != null) { if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else { lock.close();
        }
      }
    }
  }
  



  public String getId()
  {
    Locker.Lock lock = _lock.lockIfNotHeld();Throwable localThrowable3 = null;
    try {
      return _sessionData.getId();
    }
    catch (Throwable localThrowable4)
    {
      localThrowable3 = localThrowable4;throw localThrowable4;
    }
    finally {
      if (lock != null) if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else lock.close();
    }
  }
  
  public String getExtendedId()
  {
    return _extendedId;
  }
  
  public String getContextPath()
  {
    return _sessionData.getContextPath();
  }
  

  public String getVHost()
  {
    return _sessionData.getVhost();
  }
  





  public long getLastAccessedTime()
  {
    Locker.Lock lock = _lock.lockIfNotHeld();Throwable localThrowable3 = null;
    try {
      return _sessionData.getLastAccessed();
    }
    catch (Throwable localThrowable1)
    {
      localThrowable3 = localThrowable1;throw localThrowable1;
    }
    finally {
      if (lock != null) { if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else { lock.close();
        }
      }
    }
  }
  

  public ServletContext getServletContext()
  {
    if (_handler == null)
      throw new IllegalStateException("No session manager for session " + _sessionData.getId());
    return _handler._context;
  }
  




  public void setMaxInactiveInterval(int secs)
  {
    Locker.Lock lock = _lock.lockIfNotHeld();Throwable localThrowable3 = null;
    try {
      _sessionData.setMaxInactiveMs(secs * 1000L);
      _sessionData.calcAndSetExpiry();
      _sessionData.setDirty(true);
      updateInactivityTimer();
      if (LOG.isDebugEnabled())
      {
        if (secs <= 0) {
          LOG.debug("Session {} is now immortal (maxInactiveInterval={})", new Object[] { _sessionData.getId(), Integer.valueOf(secs) });
        } else {
          LOG.debug("Session {} maxInactiveInterval={}", new Object[] { _sessionData.getId(), Integer.valueOf(secs) });
        }
      }
    }
    catch (Throwable localThrowable1)
    {
      localThrowable3 = localThrowable1;throw localThrowable1;





    }
    finally
    {




      if (lock != null) { if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else { lock.close();
        }
      }
    }
  }
  


  public void updateInactivityTimer()
  {
    Locker.Lock lock = _lock.lockIfNotHeld();Throwable localThrowable3 = null;
    try {
      if (LOG.isDebugEnabled()) { LOG.debug("updateInactivityTimer", new Object[0]);
      }
      long maxInactive = _sessionData.getMaxInactiveMs();
      int evictionPolicy = getSessionHandler().getSessionCache().getEvictionPolicy();
      
      if (maxInactive <= 0L)
      {

        if (evictionPolicy < 1)
        {

          setInactivityTimer(-1L);
          if (LOG.isDebugEnabled()) { LOG.debug("Session is immortal && no inactivity eviction: timer cancelled", new Object[0]);
          }
        }
        else
        {
          setInactivityTimer(TimeUnit.SECONDS.toMillis(evictionPolicy));
          if (LOG.isDebugEnabled()) { LOG.debug("Session is immortal; evict after {} sec inactivity", evictionPolicy);
          }
          
        }
        
      }
      else if (evictionPolicy < 1)
      {

        setInactivityTimer(_sessionData.getMaxInactiveMs());
        if (LOG.isDebugEnabled()) { LOG.debug("No inactive session eviction", new Object[0]);
        }
      }
      else
      {
        setInactivityTimer(Math.min(maxInactive, TimeUnit.SECONDS.toMillis(evictionPolicy)));
        if (LOG.isDebugEnabled()) LOG.debug("Inactivity timer set to lesser of maxInactive={} and inactivityEvict={}", new Object[] { Long.valueOf(maxInactive), Integer.valueOf(evictionPolicy) });
      }
    }
    catch (Throwable localThrowable1)
    {
      localThrowable3 = localThrowable1;throw localThrowable1;


















    }
    finally
    {

















      if (lock != null) { if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else { lock.close();
        }
      }
    }
  }
  


  private void setInactivityTimer(long ms)
  {
    if (_sessionInactivityTimer == null)
      _sessionInactivityTimer = new SessionInactivityTimeout();
    _sessionInactivityTimer.setIdleTimeout(ms);
  }
  




  public void stopInactivityTimer()
  {
    Locker.Lock lock = _lock.lockIfNotHeld();Throwable localThrowable3 = null;
    try {
      if (_sessionInactivityTimer != null)
      {
        _sessionInactivityTimer.setIdleTimeout(-1L);
        _sessionInactivityTimer = null;
        if (LOG.isDebugEnabled()) LOG.debug("Session timer stopped", new Object[0]);
      }
    }
    catch (Throwable localThrowable1)
    {
      localThrowable3 = localThrowable1;throw localThrowable1;


    }
    finally
    {


      if (lock != null) { if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else { lock.close();
        }
      }
    }
  }
  

  public int getMaxInactiveInterval()
  {
    Locker.Lock lock = _lock.lockIfNotHeld();Throwable localThrowable3 = null;
    try {
      return (int)(_sessionData.getMaxInactiveMs() / 1000L);
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
  

  @Deprecated
  public HttpSessionContext getSessionContext()
  {
    checkValidForRead();
    return SessionHandler.__nullSessionContext;
  }
  

  public SessionHandler getSessionHandler()
  {
    return _handler;
  }
  






  protected void checkValidForWrite()
    throws IllegalStateException
  {
    checkLocked();
    
    if (_state == State.INVALID) {
      throw new IllegalStateException("Not valid for write: id=" + _sessionData.getId() + " created=" + _sessionData.getCreated() + " accessed=" + _sessionData.getAccessed() + " lastaccessed=" + _sessionData.getLastAccessed() + " maxInactiveMs=" + _sessionData.getMaxInactiveMs() + " expiry=" + _sessionData.getExpiry());
    }
    if (_state == State.INVALIDATING) {
      return;
    }
    if (!isResident()) {
      throw new IllegalStateException("Not valid for write: id=" + _sessionData.getId() + " not resident");
    }
  }
  





  protected void checkValidForRead()
    throws IllegalStateException
  {
    checkLocked();
    
    if (_state == State.INVALID) {
      throw new IllegalStateException("Invalid for read: id=" + _sessionData.getId() + " created=" + _sessionData.getCreated() + " accessed=" + _sessionData.getAccessed() + " lastaccessed=" + _sessionData.getLastAccessed() + " maxInactiveMs=" + _sessionData.getMaxInactiveMs() + " expiry=" + _sessionData.getExpiry());
    }
    if (_state == State.INVALIDATING) {
      return;
    }
    if (!isResident()) {
      throw new IllegalStateException("Invalid for read: id=" + _sessionData.getId() + " not resident");
    }
  }
  

  protected void checkLocked()
    throws IllegalStateException
  {
    if (!_lock.isLocked()) {
      throw new IllegalStateException("Session not locked");
    }
  }
  



  public Object getAttribute(String name)
  {
    Locker.Lock lock = _lock.lockIfNotHeld();Throwable localThrowable3 = null;
    try {
      checkValidForRead();
      return _sessionData.getAttribute(name);
    }
    catch (Throwable localThrowable4)
    {
      localThrowable3 = localThrowable4;throw localThrowable4;
    }
    finally
    {
      if (lock != null) { if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else { lock.close();
        }
      }
    }
  }
  

  @Deprecated
  public Object getValue(String name)
  {
    Locker.Lock lock = _lock.lockIfNotHeld();Throwable localThrowable3 = null;
    try {
      return _sessionData.getAttribute(name);
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
  

  public Enumeration<String> getAttributeNames()
  {
    Locker.Lock lock = _lock.lockIfNotHeld();Throwable localThrowable3 = null;
    try {
      checkValidForRead();
      final Iterator<String> itor = _sessionData.getKeys().iterator();
      new Enumeration()
      {

        public boolean hasMoreElements()
        {

          return itor.hasNext();
        }
        

        public String nextElement()
        {
          return (String)itor.next();
        }
      };
    }
    catch (Throwable localThrowable1)
    {
      localThrowable3 = localThrowable1;throw localThrowable1;








    }
    finally
    {








      if (lock != null) { if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else { lock.close();
        }
      }
    }
  }
  

  public int getAttributes()
  {
    return _sessionData.getKeys().size();
  }
  




  public Set<String> getNames()
  {
    return Collections.unmodifiableSet(_sessionData.getKeys());
  }
  






  @Deprecated
  public String[] getValueNames()
    throws IllegalStateException
  {
    Locker.Lock lock = _lock.lockIfNotHeld();Throwable localThrowable4 = null;
    try {
      checkValidForRead();
      Iterator<String> itor = _sessionData.getKeys().iterator();
      if (!itor.hasNext())
        return new String[0];
      Object names = new ArrayList();
      while (itor.hasNext())
        ((ArrayList)names).add(itor.next());
      return (String[])((ArrayList)names).toArray(new String[((ArrayList)names).size()]);
    }
    catch (Throwable localThrowable2)
    {
      localThrowable4 = localThrowable2;throw localThrowable2;



    }
    finally
    {



      if (lock != null) { if (localThrowable4 != null) try { lock.close(); } catch (Throwable localThrowable3) { localThrowable4.addSuppressed(localThrowable3); } else { lock.close();
        }
      }
    }
  }
  


  public void setAttribute(String name, Object value)
  {
    Object old = null;
    Locker.Lock lock = _lock.lockIfNotHeld();Throwable localThrowable3 = null;
    try
    {
      checkValidForWrite();
      old = _sessionData.setAttribute(name, value);
    }
    catch (Throwable localThrowable1)
    {
      localThrowable3 = localThrowable1;throw localThrowable1;

    }
    finally
    {
      if (lock != null) if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else lock.close(); }
    if ((value == null) && (old == null))
      return;
    callSessionAttributeListeners(name, value, old);
  }
  







  @Deprecated
  public void putValue(String name, Object value)
  {
    setAttribute(name, value);
  }
  







  public void removeAttribute(String name)
  {
    setAttribute(name, null);
  }
  







  @Deprecated
  public void removeValue(String name)
  {
    setAttribute(name, null);
  }
  





  public void renewId(HttpServletRequest request)
  {
    if (_handler == null) {
      throw new IllegalStateException("No session manager for session " + _sessionData.getId());
    }
    String id = null;
    String extendedId = null;
    Locker.Lock lock = _lock.lockIfNotHeld();Throwable localThrowable6 = null;
    try {
      checkValidForWrite();
      id = _sessionData.getId();
      extendedId = getExtendedId();
    }
    catch (Throwable localThrowable1)
    {
      localThrowable6 = localThrowable1;throw localThrowable1;

    }
    finally
    {
      if (lock != null) if (localThrowable6 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable6.addSuppressed(localThrowable2); } else lock.close();
    }
    String newId = _handler._sessionIdManager.renewSessionId(id, extendedId, request);
    Locker.Lock lock = _lock.lockIfNotHeld();localThrowable1 = null;
    try {
      checkValidForWrite();
      _sessionData.setId(newId);
      setExtendedId(_handler._sessionIdManager.getExtendedId(newId, request));
    }
    catch (Throwable localThrowable8)
    {
      localThrowable1 = localThrowable8;throw localThrowable8;

    }
    finally
    {
      if (lock != null) if (localThrowable1 != null) try { lock.close(); } catch (Throwable localThrowable5) { localThrowable1.addSuppressed(localThrowable5); } else lock.close(); }
    setIdChanged(true);
  }
  










  public void invalidate()
  {
    if (_handler == null) {
      throw new IllegalStateException("No session manager for session " + _sessionData.getId());
    }
    boolean result = false;
    
    Locker.Lock lock = _lock.lockIfNotHeld();Throwable localThrowable3 = null;
    try {
      switch (2.$SwitchMap$org$eclipse$jetty$server$session$Session$State[_state.ordinal()])
      {

      case 1: 
        throw new IllegalStateException();
      


      case 2: 
        result = true;
        _state = State.INVALIDATING;
        break;
      

      default: 
        LOG.info("Session {} already being invalidated", new Object[] { _sessionData.getId() });
      }
      
    }
    catch (Throwable localThrowable1)
    {
      localThrowable3 = localThrowable1;throw localThrowable1;








    }
    finally
    {








      if (lock != null) if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else { lock.close();
        }
    }
    try
    {
      if (result)
      {

        _handler.getSessionIdManager().invalidateAll(_sessionData.getId());
      }
    }
    catch (Exception e)
    {
      LOG.warn(e);
    }
  }
  




  public Locker.Lock lock()
  {
    return _lock.lock();
  }
  





  protected void doInvalidate()
    throws IllegalStateException
  {
    Locker.Lock lock = _lock.lockIfNotHeld();Throwable localThrowable4 = null;
    try
    {
      try {
        if (LOG.isDebugEnabled())
          LOG.debug("invalidate {}", new Object[] { _sessionData.getId() });
        if ((_state == State.VALID) || (_state == State.INVALIDATING))
        {
          Set<String> keys = null;
          do
          {
            keys = _sessionData.getKeys();
            for (String key : keys)
            {
              Object old = _sessionData.setAttribute(key, null);
              if (old == null)
              {










                _state = State.INVALID;return;
              }
              callSessionAttributeListeners(key, null, old);
            }
            
          }
          while (!keys.isEmpty());
        }
        
      }
      finally
      {
        _state = State.INVALID;
      }
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
  
  public boolean isNew()
    throws IllegalStateException
  {
    Locker.Lock lock = _lock.lockIfNotHeld();Throwable localThrowable3 = null;
    try {
      checkValidForRead();
      return _newSession;
    }
    catch (Throwable localThrowable4)
    {
      localThrowable3 = localThrowable4;throw localThrowable4;
    }
    finally
    {
      if (lock != null) { if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else { lock.close();
        }
      }
    }
  }
  
  public void setIdChanged(boolean changed)
  {
    Locker.Lock lock = _lock.lockIfNotHeld();Throwable localThrowable3 = null;
    try {
      _idChanged = changed;
    }
    catch (Throwable localThrowable1)
    {
      localThrowable3 = localThrowable1;throw localThrowable1;
    }
    finally {
      if (lock != null) if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else { lock.close();
        }
    }
  }
  
  public boolean isIdChanged()
  {
    Locker.Lock lock = _lock.lockIfNotHeld();Throwable localThrowable3 = null;
    try {
      return _idChanged;
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
  

  public Session getSession()
  {
    return this;
  }
  

  protected SessionData getSessionData()
  {
    return _sessionData;
  }
  

  public void setResident(boolean resident)
  {
    _resident = resident;
  }
  

  public boolean isResident()
  {
    return _resident;
  }
}
