package org.seleniumhq.jetty9.server.session;

import java.util.Collections;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.seleniumhq.jetty9.util.StringUtil;
import org.seleniumhq.jetty9.util.component.ContainerLifeCycle;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;
import org.seleniumhq.jetty9.util.thread.Locker.Lock;










































public abstract class AbstractSessionCache
  extends ContainerLifeCycle
  implements SessionCache
{
  static final Logger LOG = Log.getLogger("org.seleniumhq.jetty9.server.session");
  




  protected SessionDataStore _sessionDataStore;
  



  protected final SessionHandler _handler;
  



  protected SessionContext _context;
  



  protected int _evictionPolicy = -1;
  



  protected boolean _saveOnCreate = false;
  







  protected boolean _saveOnInactiveEviction;
  







  protected boolean _removeUnloadableSessions;
  







  public abstract Session newSession(SessionData paramSessionData);
  







  public abstract Session newSession(HttpServletRequest paramHttpServletRequest, SessionData paramSessionData);
  







  public abstract Session doGet(String paramString);
  







  public abstract Session doPutIfAbsent(String paramString, Session paramSession);
  







  public abstract boolean doReplace(String paramString, Session paramSession1, Session paramSession2);
  







  public abstract Session doDelete(String paramString);
  







  protected class PlaceHolderSession
    extends Session
  {
    public PlaceHolderSession(SessionData data)
    {
      super(data);
    }
  }
  





  public AbstractSessionCache(SessionHandler handler)
  {
    _handler = handler;
  }
  






  public SessionHandler getSessionHandler()
  {
    return _handler;
  }
  




  public void initialize(SessionContext context)
  {
    if (isStarted())
      throw new IllegalStateException("Context set after session store started");
    _context = context;
  }
  



  protected void doStart()
    throws Exception
  {
    if (_sessionDataStore == null) {
      throw new IllegalStateException("No session data store configured");
    }
    if (_handler == null) {
      throw new IllegalStateException("No session manager");
    }
    if (_context == null) {
      throw new IllegalStateException("No ContextId");
    }
    _sessionDataStore.initialize(_context);
    super.doStart();
  }
  



  protected void doStop()
    throws Exception
  {
    _sessionDataStore.stop();
    super.doStop();
  }
  



  public SessionDataStore getSessionDataStore()
  {
    return _sessionDataStore;
  }
  



  public void setSessionDataStore(SessionDataStore sessionStore)
  {
    updateBean(_sessionDataStore, sessionStore);
    _sessionDataStore = sessionStore;
  }
  








  public int getEvictionPolicy()
  {
    return _evictionPolicy;
  }
  








  public void setEvictionPolicy(int evictionTimeout)
  {
    _evictionPolicy = evictionTimeout;
  }
  


  public boolean isSaveOnCreate()
  {
    return _saveOnCreate;
  }
  


  public void setSaveOnCreate(boolean saveOnCreate)
  {
    _saveOnCreate = saveOnCreate;
  }
  





  public boolean isRemoveUnloadableSessions()
  {
    return _removeUnloadableSessions;
  }
  








  public void setRemoveUnloadableSessions(boolean removeUnloadableSessions)
  {
    _removeUnloadableSessions = removeUnloadableSessions;
  }
  










  public Session get(String id)
    throws Exception
  {
    Session session = null;
    Exception ex = null;
    
    for (;;)
    {
      session = doGet(id);
      
      if (_sessionDataStore != null)
      {

        if (session == null)
        {
          if (LOG.isDebugEnabled()) {
            LOG.debug("Session {} not found locally, attempting to load", new Object[] { id });
          }
          
          PlaceHolderSession phs = new PlaceHolderSession(new SessionData(id, null, null, 0L, 0L, 0L, 0L));
          phsLock = phs.lock();
          Session s = doPutIfAbsent(id, phs);
          if (s == null)
          {
            try
            {

              session = loadSession(id);
              if (session == null)
              {

                doDelete(id);
                phsLock.close();
                break;
              }
              
              Locker.Lock lock = session.lock();localThrowable12 = null;
              try
              {
                boolean success = doReplace(id, phs, session);
                if (!success)
                {

                  doDelete(id);
                  session = null;
                  LOG.warn("Replacement of placeholder for session {} failed", new Object[] { id });
                  phsLock.close();
                  









                  if (lock != null) if (localThrowable12 != null) try { lock.close(); } catch (Throwable localThrowable) { localThrowable12.addSuppressed(localThrowable); } else lock.close();
                  break;
                }
                session.setResident(true);
                session.updateInactivityTimer();
                phsLock.close();
              }
              catch (Throwable localThrowable2)
              {
                localThrowable12 = localThrowable2;throw localThrowable2;









              }
              finally
              {








                if (lock != null) { if (localThrowable12 != null) try { lock.close(); } catch (Throwable localThrowable3) { localThrowable12.addSuppressed(localThrowable3); } else { lock.close();
                  }
                }
              }
              









              phsLock.close();
            }
            catch (Exception e)
            {
              ex = e;
              doDelete(id);
              phsLock.close();
              session = null;
            }
          }
          




          Locker.Lock lock = s.lock();Throwable localThrowable12 = null;
          try
          {
            if ((!s.isResident()) || ((s instanceof PlaceHolderSession)))
            {
              session = null;
              



              if (lock == null) continue; if (localThrowable12 != null) { try { lock.close(); } catch (Throwable localThrowable4) {} localThrowable12.addSuppressed(localThrowable4); continue; } lock.close(); continue;
            }
            session = s;
          }
          catch (Throwable localThrowable6)
          {
            localThrowable12 = localThrowable6;throw localThrowable6;



          }
          finally
          {



            if (lock != null) { if (localThrowable12 != null) try { lock.close(); } catch (Throwable localThrowable7) { localThrowable12.addSuppressed(localThrowable7); } else { lock.close();
              }
            }
          }
        }
        

        Locker.Lock lock = session.lock();Locker.Lock phsLock = null;
        try
        {
          if ((!session.isResident()) || ((session instanceof PlaceHolderSession)))
          {
            session = null;
            




            if (lock == null) continue; if (phsLock != null) { try { lock.close(); } catch (Throwable localThrowable8) {} phsLock.addSuppressed(localThrowable8); continue; } lock.close();
          }
        }
        catch (Throwable localThrowable10)
        {
          phsLock = localThrowable10;throw localThrowable10;




        }
        finally
        {



          if (lock != null) if (phsLock != null) try { lock.close(); } catch (Throwable localThrowable11) { phsLock.addSuppressed(localThrowable11); } else lock.close();
        }
      }
    }
    if (ex != null)
      throw ex;
    return session;
  }
  







  private Session loadSession(String id)
    throws Exception
  {
    SessionData data = null;
    Session session = null;
    
    if (_sessionDataStore == null) {
      return null;
    }
    try
    {
      data = _sessionDataStore.load(id);
      
      if (data == null) {
        return null;
      }
      data.setLastNode(_context.getWorkerName());
      return newSession(data);

    }
    catch (UnreadableSessionDataException e)
    {

      if (isRemoveUnloadableSessions())
        _sessionDataStore.delete(id);
      throw e;
    }
  }
  















  public void put(String id, Session session)
    throws Exception
  {
    if ((id == null) || (session == null)) {
      throw new IllegalArgumentException("Put key=" + id + " session=" + (session == null ? "null" : session.getId()));
    }
    Locker.Lock lock = session.lock();Throwable localThrowable5 = null;
    try {
      if (session.getSessionHandler() == null) {
        throw new IllegalStateException("Session " + id + " is not managed");
      }
      if (!session.isValid()) {
        return;
      }
      if (_sessionDataStore == null)
      {
        if (LOG.isDebugEnabled()) LOG.debug("No SessionDataStore, putting into SessionCache only id={}", new Object[] { id });
        session.setResident(true);
        if (doPutIfAbsent(id, session) == null)
          session.updateInactivityTimer();
        return;
      }
      

      if (session.getRequests() <= 0L)
      {

        if (!_sessionDataStore.isPassivating())
        {

          _sessionDataStore.store(id, session.getSessionData());
          
          if (getEvictionPolicy() == 0)
          {
            if (LOG.isDebugEnabled()) LOG.debug("Eviction on request exit id={}", new Object[] { id });
            doDelete(session.getId());
            session.setResident(false);
          }
          else
          {
            session.setResident(true);
            if (doPutIfAbsent(id, session) == null)
              session.updateInactivityTimer();
            if (LOG.isDebugEnabled()) { LOG.debug("Non passivating SessionDataStore, session in SessionCache only id={}", new Object[] { id });
            }
          }
        }
        else
        {
          session.willPassivate();
          if (LOG.isDebugEnabled()) LOG.debug("Session passivating id={}", new Object[] { id });
          _sessionDataStore.store(id, session.getSessionData());
          
          if (getEvictionPolicy() == 0)
          {

            doDelete(id);
            session.setResident(false);
            if (LOG.isDebugEnabled()) { LOG.debug("Evicted on request exit id={}", new Object[] { id });
            }
          }
          else
          {
            session.didActivate();
            session.setResident(true);
            if (doPutIfAbsent(id, session) == null)
              session.updateInactivityTimer();
            if (LOG.isDebugEnabled()) LOG.debug("Session reactivated id={}", new Object[] { id });
          }
        }
      }
      else
      {
        if (LOG.isDebugEnabled()) LOG.debug("Req count={} for id={}", new Object[] { Long.valueOf(session.getRequests()), id });
        session.setResident(true);
        if (doPutIfAbsent(id, session) == null) {
          session.updateInactivityTimer();
        }
      }
    }
    catch (Throwable localThrowable3)
    {
      localThrowable5 = localThrowable3;throw localThrowable3;


































    }
    finally
    {


































      if (lock != null) { if (localThrowable5 != null) try { lock.close(); } catch (Throwable localThrowable4) { localThrowable5.addSuppressed(localThrowable4); } else { lock.close();
        }
      }
    }
  }
  








  public boolean exists(String id)
    throws Exception
  {
    Session s = doGet(id);
    if (s != null)
    {
      Locker.Lock lock = s.lock();Throwable localThrowable3 = null;
      try
      {
        return s.isValid();
      }
      catch (Throwable localThrowable4)
      {
        localThrowable3 = localThrowable4;throw localThrowable4;
      }
      finally
      {
        if (lock != null) if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else { lock.close();
          }
      }
    }
    return _sessionDataStore.exists(id);
  }
  







  public boolean contains(String id)
    throws Exception
  {
    return doGet(id) != null;
  }
  








  public Session delete(String id)
    throws Exception
  {
    Session session = get(id);
    


    if (_sessionDataStore != null)
    {

      boolean dsdel = _sessionDataStore.delete(id);
      if (LOG.isDebugEnabled()) { LOG.debug("Session {} deleted in db {}", new Object[] { id, Boolean.valueOf(dsdel) });
      }
    }
    
    if (session != null)
    {
      session.stopInactivityTimer();
      session.setResident(false);
    }
    
    return doDelete(id);
  }
  









  public Set<String> checkExpiration(Set<String> candidates)
  {
    if (!isStarted()) {
      return Collections.emptySet();
    }
    if (LOG.isDebugEnabled())
      LOG.debug("SessionDataStore checking expiration on {}", new Object[] { candidates });
    return _sessionDataStore.getExpired(candidates);
  }
  










  public void checkInactiveSession(Session session)
  {
    if (session == null) {
      return;
    }
    if (LOG.isDebugEnabled()) LOG.debug("Checking for idle {}", new Object[] { session.getId() });
    Locker.Lock s = session.lock();Throwable localThrowable3 = null;
    try {
      if ((getEvictionPolicy() > 0) && (session.isIdleLongerThan(getEvictionPolicy())) && (session.isValid()) && (session.isResident()) && (session.getRequests() <= 0L))
      {

        try
        {

          if (LOG.isDebugEnabled()) {
            LOG.debug("Evicting idle session {}", new Object[] { session.getId() });
          }
          
          if ((isSaveOnInactiveEviction()) && (_sessionDataStore != null))
          {
            if (_sessionDataStore.isPassivating()) {
              session.willPassivate();
            }
            _sessionDataStore.store(session.getId(), session.getSessionData());
          }
          
          doDelete(session.getId());
          session.setResident(false);
        }
        catch (Exception e)
        {
          LOG.warn("Passivation of idle session {} failed", new Object[] { session.getId(), e });
          session.updateInactivityTimer();
        }
      }
    }
    catch (Throwable localThrowable1)
    {
      localThrowable3 = localThrowable1;throw localThrowable1;













    }
    finally
    {












      if (s != null) { if (localThrowable3 != null) try { s.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else { s.close();
        }
      }
    }
  }
  





  public Session renewSessionId(String oldId, String newId)
    throws Exception
  {
    if (StringUtil.isBlank(oldId))
      throw new IllegalArgumentException("Old session id is null");
    if (StringUtil.isBlank(newId)) {
      throw new IllegalArgumentException("New session id is null");
    }
    Session session = get(oldId);
    if (session == null) {
      return null;
    }
    Locker.Lock lock = session.lock();Throwable localThrowable3 = null;
    try {
      session.checkValidForWrite();
      session.getSessionData().setId(newId);
      session.getSessionData().setLastSaved(0L);
      session.getSessionData().setDirty(true);
      doPutIfAbsent(newId, session);
      doDelete(oldId);
      if (_sessionDataStore != null)
      {
        _sessionDataStore.delete(oldId);
        _sessionDataStore.store(newId, session.getSessionData());
      }
      LOG.info("Session id {} swapped for new id {}", new Object[] { oldId, newId });
      return session;
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
  


  public void setSaveOnInactiveEviction(boolean saveOnEvict)
  {
    _saveOnInactiveEviction = saveOnEvict;
  }
  








  public boolean isSaveOnInactiveEviction()
  {
    return _saveOnInactiveEviction;
  }
  





  public Session newSession(HttpServletRequest request, String id, long time, long maxInactiveMs)
  {
    if (LOG.isDebugEnabled()) LOG.debug("Creating new session id=" + id, new Object[0]);
    Session session = newSession(request, _sessionDataStore.newSessionData(id, time, time, time, maxInactiveMs));
    session.getSessionData().setLastNode(_context.getWorkerName());
    try
    {
      if ((isSaveOnCreate()) && (_sessionDataStore != null)) {
        _sessionDataStore.store(id, session.getSessionData());
      }
    }
    catch (Exception e) {
      LOG.warn("Save of new session {} failed", new Object[] { id, e });
    }
    return session;
  }
  


  public String toString()
  {
    return String.format("%s@%x[evict=%d,removeUnloadable=%b,saveOnCreate=%b,saveOnInactiveEvict=%b]", new Object[] {
      getClass().getName(), Integer.valueOf(hashCode()), Integer.valueOf(_evictionPolicy), Boolean.valueOf(_removeUnloadableSessions), Boolean.valueOf(_saveOnCreate), Boolean.valueOf(_saveOnInactiveEviction) });
  }
}
