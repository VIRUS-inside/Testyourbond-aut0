package org.seleniumhq.jetty9.servlets;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import org.seleniumhq.jetty9.http.HttpFields;
import org.seleniumhq.jetty9.http.HttpHeader;
import org.seleniumhq.jetty9.http.HttpURI;
import org.seleniumhq.jetty9.server.PushBuilder;
import org.seleniumhq.jetty9.server.Request;
import org.seleniumhq.jetty9.server.Response;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;


















public class PushSessionCacheFilter
  implements Filter
{
  private static final String TARGET_ATTR = "PushCacheFilter.target";
  private static final String TIMESTAMP_ATTR = "PushCacheFilter.timestamp";
  private static final Logger LOG = Log.getLogger(PushSessionCacheFilter.class);
  private final ConcurrentMap<String, Target> _cache = new ConcurrentHashMap();
  private long _associateDelay = 5000L;
  
  public PushSessionCacheFilter() {}
  
  public void init(FilterConfig config) throws ServletException {
    if (config.getInitParameter("associateDelay") != null) {
      _associateDelay = Long.valueOf(config.getInitParameter("associateDelay")).longValue();
    }
    

    config.getServletContext().addListener(new ServletRequestListener()
    {

      public void requestDestroyed(ServletRequestEvent sre)
      {

        Request request = Request.getBaseRequest(sre.getServletRequest());
        PushSessionCacheFilter.Target target = (PushSessionCacheFilter.Target)request.getAttribute("PushCacheFilter.target");
        if (target == null) {
          return;
        }
        
        Response response = request.getResponse();
        _etag = response.getHttpFields().get(HttpHeader.ETAG);
        _lastModified = response.getHttpFields().get(HttpHeader.LAST_MODIFIED);
        

        if (request.isPush())
        {
          if (PushSessionCacheFilter.LOG.isDebugEnabled())
            PushSessionCacheFilter.LOG.debug("Pushed {} for {}", new Object[] { Integer.valueOf(request.getResponse().getStatus()), request.getRequestURI() });
          return;
        }
        if (PushSessionCacheFilter.LOG.isDebugEnabled())
        {
          PushSessionCacheFilter.LOG.debug("Served {} for {}", new Object[] { Integer.valueOf(request.getResponse().getStatus()), request.getRequestURI() });
        }
        

        String referer = request.getHttpFields().get(HttpHeader.REFERER);
        
        if (referer != null)
        {

          HttpURI referer_uri = new HttpURI(referer);
          if (request.getServerName().equals(referer_uri.getHost()))
          {
            PushSessionCacheFilter.Target referer_target = (PushSessionCacheFilter.Target)_cache.get(referer_uri.getPath());
            if (referer_target != null)
            {
              HttpSession session = request.getSession();
              ConcurrentHashMap<String, Long> timestamps = (ConcurrentHashMap)session.getAttribute("PushCacheFilter.timestamp");
              Long last = (Long)timestamps.get(_path);
              if ((last != null) && (System.currentTimeMillis() - last.longValue() < _associateDelay))
              {
                if (_associated.putIfAbsent(_path, target) == null)
                {
                  if (PushSessionCacheFilter.LOG.isDebugEnabled()) {
                    PushSessionCacheFilter.LOG.debug("ASSOCIATE {}->{}", new Object[] { _path, _path });
                  }
                }
              }
            }
          }
        }
      }
      


      public void requestInitialized(ServletRequestEvent sre) {}
    });
  }
  

  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
    throws IOException, ServletException
  {
    Request baseRequest = Request.getBaseRequest(request);
    String uri = baseRequest.getRequestURI();
    
    if (LOG.isDebugEnabled()) {
      LOG.debug("{} {} push={}", new Object[] { baseRequest.getMethod(), uri, Boolean.valueOf(baseRequest.isPush()) });
    }
    HttpSession session = baseRequest.getSession(true);
    

    Target target = (Target)_cache.get(uri);
    if (target == null)
    {
      Target t = new Target(uri, null);
      target = (Target)_cache.putIfAbsent(uri, t);
      target = target == null ? t : target;
    }
    request.setAttribute("PushCacheFilter.target", target);
    

    ConcurrentHashMap<String, Long> timestamps = (ConcurrentHashMap)session.getAttribute("PushCacheFilter.timestamp");
    if (timestamps == null)
    {
      timestamps = new ConcurrentHashMap();
      session.setAttribute("PushCacheFilter.timestamp", timestamps);
    }
    timestamps.put(uri, Long.valueOf(System.currentTimeMillis()));
    

    if ((baseRequest.isPushSupported()) && (!baseRequest.isPush()) && (!_associated.isEmpty()))
    {

      Queue<Target> queue = new ArrayDeque();
      queue.offer(target);
      PushBuilder builder; while (!queue.isEmpty())
      {
        Target parent = (Target)queue.poll();
        builder = baseRequest.getPushBuilder();
        builder.addHeader("X-Pusher", PushSessionCacheFilter.class.toString());
        for (Target child : _associated.values())
        {
          queue.offer(child);
          
          String path = _path;
          if (LOG.isDebugEnabled()) {
            LOG.debug("PUSH {} <- {}", new Object[] { path, uri });
          }
          builder.path(path).etag(_etag).lastModified(_lastModified).push();
        }
      }
    }
    
    chain.doFilter(request, response);
  }
  

  public void destroy()
  {
    _cache.clear();
  }
  
  private static class Target
  {
    private final String _path;
    private final ConcurrentMap<String, Target> _associated = new ConcurrentHashMap();
    private volatile String _etag;
    private volatile String _lastModified;
    
    private Target(String path)
    {
      _path = path;
    }
    

    public String toString()
    {
      return String.format("Target{p=%s,e=%s,m=%s,a=%d}", new Object[] { _path, _etag, _lastModified, Integer.valueOf(_associated.size()) });
    }
  }
}
