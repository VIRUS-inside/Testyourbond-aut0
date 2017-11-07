package org.seleniumhq.jetty9.server.session;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import javax.servlet.http.HttpServletRequest;
import org.seleniumhq.jetty9.server.Handler;
import org.seleniumhq.jetty9.server.Server;
import org.seleniumhq.jetty9.server.SessionIdManager;
import org.seleniumhq.jetty9.server.handler.ContextHandler;
import org.seleniumhq.jetty9.util.StringUtil;
import org.seleniumhq.jetty9.util.component.ContainerLifeCycle;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;

































public class DefaultSessionIdManager
  extends ContainerLifeCycle
  implements SessionIdManager
{
  private static final Logger LOG = Log.getLogger("org.seleniumhq.jetty9.server.session");
  
  private static final String __NEW_SESSION_ID = "org.seleniumhq.jetty9.server.newSessionId";
  
  protected static final AtomicLong COUNTER = new AtomicLong();
  
  protected Random _random;
  protected boolean _weakRandom;
  protected String _workerName;
  protected String _workerAttr;
  protected long _reseed = 100000L;
  

  protected Server _server;
  
  protected HouseKeeper _houseKeeper;
  
  protected boolean _ownHouseKeeper;
  

  public DefaultSessionIdManager(Server server)
  {
    _server = server;
  }
  





  public DefaultSessionIdManager(Server server, Random random)
  {
    this(server);
    _random = random;
  }
  




  public void setServer(Server server)
  {
    _server = server;
  }
  





  public Server getServer()
  {
    return _server;
  }
  






  public void setSessionHouseKeeper(HouseKeeper houseKeeper)
  {
    updateBean(_houseKeeper, houseKeeper);
    _houseKeeper = houseKeeper;
    _houseKeeper.setSessionIdManager(this);
  }
  




  public HouseKeeper getSessionHouseKeeper()
  {
    return _houseKeeper;
  }
  









  public String getWorkerName()
  {
    return _workerName;
  }
  












  public void setWorkerName(String workerName)
  {
    if (isRunning())
      throw new IllegalStateException(getState());
    if (workerName == null) {
      _workerName = "";
    }
    else {
      if (workerName.contains("."))
        throw new IllegalArgumentException("Name cannot contain '.'");
      _workerName = workerName;
    }
  }
  




  public Random getRandom()
  {
    return _random;
  }
  




  public synchronized void setRandom(Random random)
  {
    _random = random;
    _weakRandom = false;
  }
  




  public long getReseed()
  {
    return _reseed;
  }
  




  public void setReseed(long reseed)
  {
    _reseed = reseed;
  }
  







  public String newSessionId(HttpServletRequest request, long created)
  {
    if (request == null) {
      return newSessionId(created);
    }
    
    String requested_id = request.getRequestedSessionId();
    if (requested_id != null)
    {
      String cluster_id = getId(requested_id);
      if (isIdInUse(cluster_id)) {
        return cluster_id;
      }
    }
    

    String new_id = (String)request.getAttribute("org.seleniumhq.jetty9.server.newSessionId");
    if ((new_id != null) && (isIdInUse(new_id))) {
      return new_id;
    }
    
    String id = newSessionId(request.hashCode());
    
    request.setAttribute("org.seleniumhq.jetty9.server.newSessionId", id);
    return id;
  }
  








  public String newSessionId(long seedTerm)
  {
    String id = null;
    
    synchronized (_random)
    {
      while ((id == null) || (id.length() == 0))
      {


        long r0 = _weakRandom ? hashCode() ^ Runtime.getRuntime().freeMemory() ^ _random.nextInt() ^ seedTerm << 32 : _random.nextLong();
        if (r0 < 0L) {
          r0 = -r0;
        }
        
        if ((_reseed > 0L) && (r0 % _reseed == 1L))
        {
          if (LOG.isDebugEnabled())
            LOG.debug("Reseeding {}", new Object[] { this });
          if ((_random instanceof SecureRandom))
          {
            SecureRandom secure = (SecureRandom)_random;
            secure.setSeed(secure.generateSeed(8));
          }
          else
          {
            _random.setSeed(_random.nextLong() ^ System.currentTimeMillis() ^ seedTerm ^ Runtime.getRuntime().freeMemory());
          }
        }
        


        long r1 = _weakRandom ? hashCode() ^ Runtime.getRuntime().freeMemory() ^ _random.nextInt() ^ seedTerm << 32 : _random.nextLong();
        if (r1 < 0L) {
          r1 = -r1;
        }
        id = Long.toString(r0, 36) + Long.toString(r1, 36);
        


        if (!StringUtil.isBlank(_workerName)) {
          id = _workerName + id;
        }
        id = id + Long.toString(COUNTER.getAndIncrement());
      }
    }
    
    return id;
  }
  







  public boolean isIdInUse(String id)
  {
    if (id == null) {
      return false;
    }
    boolean inUse = false;
    if (LOG.isDebugEnabled()) {
      LOG.debug("Checking {} is in use by at least one context", new Object[] { id });
    }
    try
    {
      for (SessionHandler manager : getSessionHandlers())
      {
        if (manager.isIdInUse(id))
        {
          if (LOG.isDebugEnabled())
            LOG.debug("Context {} reports id in use", new Object[] { manager });
          inUse = true;
          break;
        }
      }
      
      if (LOG.isDebugEnabled())
        LOG.debug("Checked {}, in use:", new Object[] { id, Boolean.valueOf(inUse) });
      return inUse;
    }
    catch (Exception e)
    {
      LOG.warn("Problem checking if id {} is in use", new Object[] { id, e }); }
    return false;
  }
  







  protected void doStart()
    throws Exception
  {
    if (_server == null) {
      throw new IllegalStateException("No Server for SessionIdManager");
    }
    initRandom();
    
    if (_workerName == null)
    {
      String inst = System.getenv("JETTY_WORKER_INSTANCE");
      _workerName = ("node" + (inst == null ? "0" : inst));
    }
    
    LOG.info("DefaultSessionIdManager workerName={}", new Object[] { _workerName });
    _workerAttr = ((_workerName != null) && (_workerName.startsWith("$")) ? _workerName.substring(1) : null);
    
    if (_houseKeeper == null)
    {
      LOG.info("No SessionScavenger set, using defaults", new Object[0]);
      _ownHouseKeeper = true;
      _houseKeeper = new HouseKeeper();
      _houseKeeper.setSessionIdManager(this);
      addBean(_houseKeeper, true);
    }
    
    _houseKeeper.start();
  }
  




  protected void doStop()
    throws Exception
  {
    _houseKeeper.stop();
    if (_ownHouseKeeper)
    {
      _houseKeeper = null;
    }
    _random = null;
  }
  






  public void initRandom()
  {
    if (_random == null)
    {
      try
      {
        _random = new SecureRandom();
      }
      catch (Exception e)
      {
        LOG.warn("Could not generate SecureRandom for session-id randomness", e);
        _random = new Random();
        _weakRandom = true;
      }
      
    } else {
      _random.setSeed(_random.nextLong() ^ System.currentTimeMillis() ^ hashCode() ^ Runtime.getRuntime().freeMemory());
    }
  }
  








  public String getExtendedId(String clusterId, HttpServletRequest request)
  {
    if (!StringUtil.isBlank(_workerName))
    {
      if (_workerAttr == null) {
        return clusterId + '.' + _workerName;
      }
      String worker = (String)request.getAttribute(_workerAttr);
      if (worker != null) {
        return clusterId + '.' + worker;
      }
    }
    return clusterId;
  }
  








  public String getId(String extendedId)
  {
    int dot = extendedId.lastIndexOf('.');
    return dot > 0 ? extendedId.substring(0, dot) : extendedId;
  }
  









  public void expireAll(String id)
  {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Expiring {}", new Object[] { id });
    }
    for (SessionHandler manager : getSessionHandlers())
    {
      manager.invalidate(id);
    }
  }
  



  public void invalidateAll(String id)
  {
    for (SessionHandler manager : getSessionHandlers())
    {
      manager.invalidate(id);
    }
  }
  









  public String renewSessionId(String oldClusterId, String oldNodeId, HttpServletRequest request)
  {
    String newClusterId = newSessionId(request.hashCode());
    



    for (SessionHandler manager : getSessionHandlers())
    {
      manager.renewSessionId(oldClusterId, oldNodeId, newClusterId, getExtendedId(newClusterId, request));
    }
    
    return newClusterId;
  }
  







  public Set<SessionHandler> getSessionHandlers()
  {
    Set<SessionHandler> handlers = new HashSet();
    
    Handler[] contexts = _server.getChildHandlersByClass(ContextHandler.class);
    for (int i = 0; (contexts != null) && (i < contexts.length); i++)
    {
      SessionHandler sessionHandler = (SessionHandler)((ContextHandler)contexts[i]).getChildHandlerByClass(SessionHandler.class);
      if (sessionHandler != null)
      {
        handlers.add(sessionHandler);
      }
    }
    return handlers;
  }
  




  public String toString()
  {
    return String.format("%s[worker=%s]", new Object[] { super.toString(), _workerName });
  }
}
