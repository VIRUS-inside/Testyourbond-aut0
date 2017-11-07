package org.seleniumhq.jetty9.servlets;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.seleniumhq.jetty9.http.HttpField;
import org.seleniumhq.jetty9.http.HttpFields;
import org.seleniumhq.jetty9.http.HttpHeader;
import org.seleniumhq.jetty9.http.HttpMethod;
import org.seleniumhq.jetty9.http.HttpURI;
import org.seleniumhq.jetty9.http.HttpVersion;
import org.seleniumhq.jetty9.server.PushBuilder;
import org.seleniumhq.jetty9.server.Request;
import org.seleniumhq.jetty9.util.StringUtil;
import org.seleniumhq.jetty9.util.annotation.ManagedAttribute;
import org.seleniumhq.jetty9.util.annotation.ManagedObject;
import org.seleniumhq.jetty9.util.annotation.ManagedOperation;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;



































@ManagedObject("Push cache based on the HTTP 'Referer' header")
public class PushCacheFilter
  implements Filter
{
  private static final Logger LOG = Log.getLogger(PushCacheFilter.class);
  
  private final Set<Integer> _ports = new HashSet();
  private final Set<String> _hosts = new HashSet();
  private final ConcurrentMap<String, PrimaryResource> _cache = new ConcurrentHashMap();
  private long _associatePeriod = 4000L;
  private int _maxAssociations = 16;
  private long _renew = System.nanoTime();
  
  public PushCacheFilter() {}
  
  public void init(FilterConfig config) throws ServletException {
    String associatePeriod = config.getInitParameter("associatePeriod");
    if (associatePeriod != null) {
      _associatePeriod = Long.parseLong(associatePeriod);
    }
    String maxAssociations = config.getInitParameter("maxAssociations");
    if (maxAssociations != null) {
      _maxAssociations = Integer.parseInt(maxAssociations);
    }
    String hosts = config.getInitParameter("hosts");
    if (hosts != null) {
      Collections.addAll(_hosts, StringUtil.csvSplit(hosts));
    }
    String ports = config.getInitParameter("ports");
    if (ports != null) {
      for (String p : StringUtil.csvSplit(ports)) {
        _ports.add(Integer.valueOf(Integer.parseInt(p)));
      }
    }
    config.getServletContext().setAttribute(config.getFilterName(), this);
    
    if (LOG.isDebugEnabled()) {
      LOG.debug("period={} max={} hosts={} ports={}", new Object[] { Long.valueOf(_associatePeriod), Integer.valueOf(_maxAssociations), _hosts, _ports });
    }
  }
  
  public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException
  {
    HttpServletRequest request = (HttpServletRequest)req;
    Request jettyRequest = Request.getBaseRequest(request);
    
    if ((HttpVersion.fromString(request.getProtocol()).getVersion() < 20) || 
      (!HttpMethod.GET.is(request.getMethod())) || 
      (!jettyRequest.isPushSupported()))
    {
      chain.doFilter(req, resp);
      return;
    }
    
    long now = System.nanoTime();
    

    HttpFields fields = jettyRequest.getHttpFields();
    boolean conditional = false;
    String referrer = null;
    
    for (int i = 0; i < fields.size(); i++)
    {
      HttpField field = fields.getField(i);
      HttpHeader header = field.getHeader();
      if (header != null)
      {

        switch (1.$SwitchMap$org$eclipse$jetty$http$HttpHeader[header.ordinal()])
        {
        case 1: 
        case 2: 
        case 3: 
        case 4: 
          conditional = true;
          break;
        
        case 5: 
          referrer = field.getValue();
        }
        
      }
    }
    


    if (LOG.isDebugEnabled()) {
      LOG.debug("{} {} referrer={} conditional={}", new Object[] { request.getMethod(), request.getRequestURI(), referrer, Boolean.valueOf(conditional) });
    }
    String path = request.getRequestURI();
    String query = request.getQueryString();
    if (query != null)
      path = path + "?" + query;
    String referrerPath; if (referrer != null)
    {
      HttpURI referrerURI = new HttpURI(referrer);
      String host = referrerURI.getHost();
      int port = referrerURI.getPort();
      if (port <= 0) {
        port = request.isSecure() ? 443 : 80;
      }
      boolean referredFromHere = _hosts.size() > 0 ? _hosts.contains(host) : host.equals(request.getServerName());
      referredFromHere &= (port == request.getServerPort() ? true : _ports.size() > 0 ? _ports.contains(Integer.valueOf(port)) : false);
      
      if (referredFromHere)
      {
        if (HttpMethod.GET.is(request.getMethod()))
        {
          referrerPath = referrerURI.getPath();
          if (referrerPath == null)
            referrerPath = "/";
          if (referrerPath.startsWith(request.getContextPath() + "/"))
          {
            if (!referrerPath.equals(path))
            {
              PrimaryResource primaryResource = (PrimaryResource)_cache.get(referrerPath);
              if (primaryResource != null)
              {
                long primaryTimestamp = _timestamp.get();
                if (primaryTimestamp != 0L)
                {
                  if (now - primaryTimestamp < TimeUnit.MILLISECONDS.toNanos(_associatePeriod))
                  {
                    Set<String> associated = _associated;
                    
                    if (associated.size() <= _maxAssociations)
                    {
                      if (associated.add(path))
                      {
                        if (LOG.isDebugEnabled()) {
                          LOG.debug("Associated {} to {}", new Object[] { path, referrerPath });
                        }
                        
                      }
                    }
                    else if (LOG.isDebugEnabled()) {
                      LOG.debug("Not associated {} to {}, exceeded max associations of {}", new Object[] { path, referrerPath, Integer.valueOf(_maxAssociations) });
                    }
                    

                  }
                  else if (LOG.isDebugEnabled()) {
                    LOG.debug("Not associated {} to {}, outside associate period of {}ms", new Object[] { path, referrerPath, Long.valueOf(_associatePeriod) });
                  }
                  
                }
                
              }
            }
            else if (LOG.isDebugEnabled()) {
              LOG.debug("Not associated {} to {}, referring to self", new Object[] { path, referrerPath });
            }
            

          }
          else if (LOG.isDebugEnabled()) {
            LOG.debug("Not associated {} to {}, different context", new Object[] { path, referrerPath });
          }
          
        }
        
      }
      else if (LOG.isDebugEnabled()) {
        LOG.debug("External referrer {}", new Object[] { referrer });
      }
    }
    
    PrimaryResource primaryResource = (PrimaryResource)_cache.get(path);
    if (primaryResource == null)
    {
      PrimaryResource r = new PrimaryResource(null);
      primaryResource = (PrimaryResource)_cache.putIfAbsent(path, r);
      primaryResource = primaryResource == null ? r : primaryResource;
      _timestamp.compareAndSet(0L, now);
      if (LOG.isDebugEnabled()) {
        LOG.debug("Cached primary resource {}", new Object[] { path });
      }
    }
    else {
      long last = _timestamp.get();
      if ((last < _renew) && (_timestamp.compareAndSet(last, now)))
      {
        _associated.clear();
        if (LOG.isDebugEnabled()) {
          LOG.debug("Clear associated resources for {}", new Object[] { path });
        }
      }
    }
    
    if ((!conditional) && (!_associated.isEmpty()))
    {
      PushBuilder pushBuilder = jettyRequest.getPushBuilder();
      

      Queue<PrimaryResource> queue = new ArrayDeque();
      queue.offer(primaryResource);
      while (!queue.isEmpty())
      {
        PrimaryResource parent = (PrimaryResource)queue.poll();
        for (String childPath : _associated)
        {
          PrimaryResource child = (PrimaryResource)_cache.get(childPath);
          if (child != null) {
            queue.offer(child);
          }
          if (LOG.isDebugEnabled())
            LOG.debug("Pushing {} for {}", new Object[] { childPath, path });
          pushBuilder.path(childPath).push();
        }
      }
    }
    
    chain.doFilter(request, resp);
  }
  

  public void destroy()
  {
    clearPushCache();
  }
  
  @ManagedAttribute("The push cache contents")
  public Map<String, String> getPushCache()
  {
    Map<String, String> result = new HashMap();
    for (Map.Entry<String, PrimaryResource> entry : _cache.entrySet())
    {
      PrimaryResource resource = (PrimaryResource)entry.getValue();
      String value = String.format("size=%d: %s", new Object[] { Integer.valueOf(_associated.size()), new TreeSet(_associated) });
      result.put(entry.getKey(), value);
    }
    return result;
  }
  
  @ManagedOperation(value="Renews the push cache contents", impact="ACTION")
  public void renewPushCache()
  {
    _renew = System.nanoTime();
  }
  
  @ManagedOperation(value="Clears the push cache contents", impact="ACTION")
  public void clearPushCache()
  {
    _cache.clear();
  }
  
  private static class PrimaryResource
  {
    private final Set<String> _associated = Collections.newSetFromMap(new ConcurrentHashMap());
    private final AtomicLong _timestamp = new AtomicLong();
    
    private PrimaryResource() {}
  }
}
