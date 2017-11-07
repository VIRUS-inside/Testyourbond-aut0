package org.seleniumhq.jetty9.server.session;

import org.seleniumhq.jetty9.server.handler.ContextHandler;
import org.seleniumhq.jetty9.server.handler.ContextHandler.Context;






























public class SessionContext
{
  public static final String NULL_VHOST = "0.0.0.0";
  private ContextHandler.Context _context;
  private SessionHandler _sessionHandler;
  private String _workerName;
  private String _canonicalContextPath;
  private String _vhost;
  
  public SessionContext(String workerName, ContextHandler.Context context)
  {
    if (context != null)
      _sessionHandler = ((SessionHandler)context.getContextHandler().getChildHandlerByClass(SessionHandler.class));
    _workerName = workerName;
    _context = context;
    _canonicalContextPath = canonicalizeContextPath(_context);
    _vhost = canonicalizeVHost(_context);
  }
  

  public String getWorkerName()
  {
    return _workerName;
  }
  
  public SessionHandler getSessionHandler()
  {
    return _sessionHandler;
  }
  

  public ContextHandler.Context getContext()
  {
    return _context;
  }
  
  public String getCanonicalContextPath()
  {
    return _canonicalContextPath;
  }
  
  public String getVhost()
  {
    return _vhost;
  }
  
  public String toString()
  {
    return _workerName + "_" + _canonicalContextPath + "_" + _vhost;
  }
  






  public void run(Runnable r)
  {
    if (_context != null) {
      _context.getContextHandler().handle(r);
    } else {
      r.run();
    }
  }
  
  private String canonicalizeContextPath(ContextHandler.Context context) {
    if (context == null)
      return "";
    return canonicalize(context.getContextPath());
  }
  








  private String canonicalizeVHost(ContextHandler.Context context)
  {
    String vhost = "0.0.0.0";
    
    if (context == null) {
      return vhost;
    }
    String[] vhosts = context.getContextHandler().getVirtualHosts();
    if ((vhosts == null) || (vhosts.length == 0) || (vhosts[0] == null)) {
      return vhost;
    }
    return vhosts[0];
  }
  






  private String canonicalize(String path)
  {
    if (path == null) {
      return "";
    }
    return path.replace('/', '_').replace('.', '_').replace('\\', '_');
  }
}
