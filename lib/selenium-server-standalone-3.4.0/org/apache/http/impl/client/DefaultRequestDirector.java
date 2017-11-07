package org.apache.http.impl.client;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NoHttpResponseException;
import org.apache.http.ProtocolException;
import org.apache.http.ProtocolVersion;
import org.apache.http.RequestLine;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthProtocolState;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthenticationHandler;
import org.apache.http.client.AuthenticationStrategy;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.NonRepeatableRequestException;
import org.apache.http.client.RedirectException;
import org.apache.http.client.RedirectHandler;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.RequestDirector;
import org.apache.http.client.UserTokenHandler;
import org.apache.http.client.methods.AbortableHttpRequest;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.conn.BasicManagedEntity;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ClientConnectionRequest;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.ManagedClientConnection;
import org.apache.http.conn.routing.BasicRouteDirector;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.HttpRouteDirector;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.conn.ConnectionShutdownException;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.util.Args;
import org.apache.http.util.EntityUtils;
























































































































@Deprecated
public class DefaultRequestDirector
  implements RequestDirector
{
  private final Log log;
  protected final ClientConnectionManager connManager;
  protected final HttpRoutePlanner routePlanner;
  protected final ConnectionReuseStrategy reuseStrategy;
  protected final ConnectionKeepAliveStrategy keepAliveStrategy;
  protected final HttpRequestExecutor requestExec;
  protected final HttpProcessor httpProcessor;
  protected final HttpRequestRetryHandler retryHandler;
  @Deprecated
  protected final RedirectHandler redirectHandler;
  protected final RedirectStrategy redirectStrategy;
  @Deprecated
  protected final AuthenticationHandler targetAuthHandler;
  protected final AuthenticationStrategy targetAuthStrategy;
  @Deprecated
  protected final AuthenticationHandler proxyAuthHandler;
  protected final AuthenticationStrategy proxyAuthStrategy;
  protected final UserTokenHandler userTokenHandler;
  protected final HttpParams params;
  protected ManagedClientConnection managedConn;
  protected final AuthState targetAuthState;
  protected final AuthState proxyAuthState;
  private final HttpAuthenticator authenticator;
  private int execCount;
  private int redirectCount;
  private final int maxRedirects;
  private HttpHost virtualHost;
  
  @Deprecated
  public DefaultRequestDirector(HttpRequestExecutor requestExec, ClientConnectionManager conman, ConnectionReuseStrategy reustrat, ConnectionKeepAliveStrategy kastrat, HttpRoutePlanner rouplan, HttpProcessor httpProcessor, HttpRequestRetryHandler retryHandler, RedirectHandler redirectHandler, AuthenticationHandler targetAuthHandler, AuthenticationHandler proxyAuthHandler, UserTokenHandler userTokenHandler, HttpParams params)
  {
    this(LogFactory.getLog(DefaultRequestDirector.class), requestExec, conman, reustrat, kastrat, rouplan, httpProcessor, retryHandler, new DefaultRedirectStrategyAdaptor(redirectHandler), new AuthenticationStrategyAdaptor(targetAuthHandler), new AuthenticationStrategyAdaptor(proxyAuthHandler), userTokenHandler, params);
  }
  



















  @Deprecated
  public DefaultRequestDirector(Log log, HttpRequestExecutor requestExec, ClientConnectionManager conman, ConnectionReuseStrategy reustrat, ConnectionKeepAliveStrategy kastrat, HttpRoutePlanner rouplan, HttpProcessor httpProcessor, HttpRequestRetryHandler retryHandler, RedirectStrategy redirectStrategy, AuthenticationHandler targetAuthHandler, AuthenticationHandler proxyAuthHandler, UserTokenHandler userTokenHandler, HttpParams params)
  {
    this(LogFactory.getLog(DefaultRequestDirector.class), requestExec, conman, reustrat, kastrat, rouplan, httpProcessor, retryHandler, redirectStrategy, new AuthenticationStrategyAdaptor(targetAuthHandler), new AuthenticationStrategyAdaptor(proxyAuthHandler), userTokenHandler, params);
  }
  






















  public DefaultRequestDirector(Log log, HttpRequestExecutor requestExec, ClientConnectionManager conman, ConnectionReuseStrategy reustrat, ConnectionKeepAliveStrategy kastrat, HttpRoutePlanner rouplan, HttpProcessor httpProcessor, HttpRequestRetryHandler retryHandler, RedirectStrategy redirectStrategy, AuthenticationStrategy targetAuthStrategy, AuthenticationStrategy proxyAuthStrategy, UserTokenHandler userTokenHandler, HttpParams params)
  {
    Args.notNull(log, "Log");
    Args.notNull(requestExec, "Request executor");
    Args.notNull(conman, "Client connection manager");
    Args.notNull(reustrat, "Connection reuse strategy");
    Args.notNull(kastrat, "Connection keep alive strategy");
    Args.notNull(rouplan, "Route planner");
    Args.notNull(httpProcessor, "HTTP protocol processor");
    Args.notNull(retryHandler, "HTTP request retry handler");
    Args.notNull(redirectStrategy, "Redirect strategy");
    Args.notNull(targetAuthStrategy, "Target authentication strategy");
    Args.notNull(proxyAuthStrategy, "Proxy authentication strategy");
    Args.notNull(userTokenHandler, "User token handler");
    Args.notNull(params, "HTTP parameters");
    this.log = log;
    authenticator = new HttpAuthenticator(log);
    this.requestExec = requestExec;
    connManager = conman;
    reuseStrategy = reustrat;
    keepAliveStrategy = kastrat;
    routePlanner = rouplan;
    this.httpProcessor = httpProcessor;
    this.retryHandler = retryHandler;
    this.redirectStrategy = redirectStrategy;
    this.targetAuthStrategy = targetAuthStrategy;
    this.proxyAuthStrategy = proxyAuthStrategy;
    this.userTokenHandler = userTokenHandler;
    this.params = params;
    
    if ((redirectStrategy instanceof DefaultRedirectStrategyAdaptor)) {
      redirectHandler = ((DefaultRedirectStrategyAdaptor)redirectStrategy).getHandler();
    } else {
      redirectHandler = null;
    }
    if ((targetAuthStrategy instanceof AuthenticationStrategyAdaptor)) {
      targetAuthHandler = ((AuthenticationStrategyAdaptor)targetAuthStrategy).getHandler();
    } else {
      targetAuthHandler = null;
    }
    if ((proxyAuthStrategy instanceof AuthenticationStrategyAdaptor)) {
      proxyAuthHandler = ((AuthenticationStrategyAdaptor)proxyAuthStrategy).getHandler();
    } else {
      proxyAuthHandler = null;
    }
    
    managedConn = null;
    
    execCount = 0;
    redirectCount = 0;
    targetAuthState = new AuthState();
    proxyAuthState = new AuthState();
    maxRedirects = this.params.getIntParameter("http.protocol.max-redirects", 100);
  }
  
  private RequestWrapper wrapRequest(HttpRequest request)
    throws ProtocolException
  {
    if ((request instanceof HttpEntityEnclosingRequest)) {
      return new EntityEnclosingRequestWrapper((HttpEntityEnclosingRequest)request);
    }
    
    return new RequestWrapper(request);
  }
  



  protected void rewriteRequestURI(RequestWrapper request, HttpRoute route)
    throws ProtocolException
  {
    try
    {
      URI uri = request.getURI();
      if ((route.getProxyHost() != null) && (!route.isTunnelled()))
      {
        if (!uri.isAbsolute()) {
          HttpHost target = route.getTargetHost();
          uri = URIUtils.rewriteURI(uri, target, true);
        } else {
          uri = URIUtils.rewriteURI(uri);
        }
        
      }
      else if (uri.isAbsolute()) {
        uri = URIUtils.rewriteURI(uri, null, true);
      } else {
        uri = URIUtils.rewriteURI(uri);
      }
      
      request.setURI(uri);
    }
    catch (URISyntaxException ex) {
      throw new ProtocolException("Invalid URI: " + request.getRequestLine().getUri(), ex);
    }
  }
  





  public HttpResponse execute(HttpHost targetHost, HttpRequest request, HttpContext context)
    throws HttpException, IOException
  {
    context.setAttribute("http.auth.target-scope", targetAuthState);
    context.setAttribute("http.auth.proxy-scope", proxyAuthState);
    
    HttpHost target = targetHost;
    
    HttpRequest orig = request;
    RequestWrapper origWrapper = wrapRequest(orig);
    origWrapper.setParams(params);
    HttpRoute origRoute = determineRoute(target, origWrapper, context);
    
    virtualHost = ((HttpHost)origWrapper.getParams().getParameter("http.virtual-host"));
    

    if ((virtualHost != null) && (virtualHost.getPort() == -1)) {
      HttpHost host = target != null ? target : origRoute.getTargetHost();
      int port = host.getPort();
      if (port != -1) {
        virtualHost = new HttpHost(virtualHost.getHostName(), port, virtualHost.getSchemeName());
      }
    }
    
    RoutedRequest roureq = new RoutedRequest(origWrapper, origRoute);
    
    boolean reuse = false;
    boolean done = false;
    try {
      HttpResponse response = null;
      while (!done)
      {




        RequestWrapper wrapper = roureq.getRequest();
        HttpRoute route = roureq.getRoute();
        response = null;
        

        Object userToken = context.getAttribute("http.user-token");
        

        if (managedConn == null) {
          ClientConnectionRequest connRequest = connManager.requestConnection(route, userToken);
          
          if ((orig instanceof AbortableHttpRequest)) {
            ((AbortableHttpRequest)orig).setConnectionRequest(connRequest);
          }
          
          long timeout = HttpClientParams.getConnectionManagerTimeout(params);
          try {
            managedConn = connRequest.getConnection(timeout, TimeUnit.MILLISECONDS);
          } catch (InterruptedException interrupted) {
            Thread.currentThread().interrupt();
            throw new InterruptedIOException();
          }
          
          if (HttpConnectionParams.isStaleCheckingEnabled(params))
          {
            if (managedConn.isOpen()) {
              log.debug("Stale connection check");
              if (managedConn.isStale()) {
                log.debug("Stale connection detected");
                managedConn.close();
              }
            }
          }
        }
        
        if ((orig instanceof AbortableHttpRequest)) {
          ((AbortableHttpRequest)orig).setReleaseTrigger(managedConn);
        }
        try
        {
          tryConnect(roureq, context);
        } catch (TunnelRefusedException ex) {
          if (log.isDebugEnabled()) {
            log.debug(ex.getMessage());
          }
          response = ex.getResponse();
          break;
        }
        
        String userinfo = wrapper.getURI().getUserInfo();
        if (userinfo != null) {
          targetAuthState.update(new BasicScheme(), new UsernamePasswordCredentials(userinfo));
        }
        


        if (virtualHost != null) {
          target = virtualHost;
        } else {
          URI requestURI = wrapper.getURI();
          if (requestURI.isAbsolute()) {
            target = URIUtils.extractHost(requestURI);
          }
        }
        if (target == null) {
          target = route.getTargetHost();
        }
        

        wrapper.resetHeaders();
        
        rewriteRequestURI(wrapper, route);
        

        context.setAttribute("http.target_host", target);
        context.setAttribute("http.route", route);
        context.setAttribute("http.connection", managedConn);
        

        requestExec.preProcess(wrapper, httpProcessor, context);
        
        response = tryExecute(roureq, context);
        if (response != null)
        {




          response.setParams(params);
          requestExec.postProcess(response, httpProcessor, context);
          


          reuse = reuseStrategy.keepAlive(response, context);
          if (reuse)
          {
            long duration = keepAliveStrategy.getKeepAliveDuration(response, context);
            if (log.isDebugEnabled()) { String s;
              String s;
              if (duration > 0L) {
                s = "for " + duration + " " + TimeUnit.MILLISECONDS;
              } else {
                s = "indefinitely";
              }
              log.debug("Connection can be kept alive " + s);
            }
            managedConn.setIdleDuration(duration, TimeUnit.MILLISECONDS);
          }
          
          RoutedRequest followup = handleResponse(roureq, response, context);
          if (followup == null) {
            done = true;
          } else {
            if (reuse)
            {
              HttpEntity entity = response.getEntity();
              EntityUtils.consume(entity);
              

              managedConn.markReusable();
            } else {
              managedConn.close();
              if ((proxyAuthState.getState().compareTo(AuthProtocolState.CHALLENGED) > 0) && (proxyAuthState.getAuthScheme() != null) && (proxyAuthState.getAuthScheme().isConnectionBased()))
              {

                log.debug("Resetting proxy auth state");
                proxyAuthState.reset();
              }
              if ((targetAuthState.getState().compareTo(AuthProtocolState.CHALLENGED) > 0) && (targetAuthState.getAuthScheme() != null) && (targetAuthState.getAuthScheme().isConnectionBased()))
              {

                log.debug("Resetting target auth state");
                targetAuthState.reset();
              }
            }
            
            if (!followup.getRoute().equals(roureq.getRoute())) {
              releaseConnection();
            }
            roureq = followup;
          }
          
          if (managedConn != null) {
            if (userToken == null) {
              userToken = userTokenHandler.getUserToken(context);
              context.setAttribute("http.user-token", userToken);
            }
            if (userToken != null) {
              managedConn.setState(userToken);
            }
          }
        }
      }
      


      if ((response == null) || (response.getEntity() == null) || (!response.getEntity().isStreaming()))
      {

        if (reuse) {
          managedConn.markReusable();
        }
        releaseConnection();
      }
      else {
        HttpEntity entity = response.getEntity();
        entity = new BasicManagedEntity(entity, managedConn, reuse);
        response.setEntity(entity);
      }
      
      return response;
    }
    catch (ConnectionShutdownException ex) {
      InterruptedIOException ioex = new InterruptedIOException("Connection has been shut down");
      
      ioex.initCause(ex);
      throw ioex;
    } catch (HttpException ex) {
      abortConnection();
      throw ex;
    } catch (IOException ex) {
      abortConnection();
      throw ex;
    } catch (RuntimeException ex) {
      abortConnection();
      throw ex;
    }
  }
  



  private void tryConnect(RoutedRequest req, HttpContext context)
    throws HttpException, IOException
  {
    HttpRoute route = req.getRoute();
    HttpRequest wrapper = req.getRequest();
    
    int connectCount = 0;
    for (;;) {
      context.setAttribute("http.request", wrapper);
      
      connectCount++;
      try {
        if (!managedConn.isOpen()) {
          managedConn.open(route, context, params);
        } else {
          managedConn.setSocketTimeout(HttpConnectionParams.getSoTimeout(params));
        }
        establishRoute(route, context);
      }
      catch (IOException ex) {
        try {
          managedConn.close();
        }
        catch (IOException ignore) {}
        if (retryHandler.retryRequest(ex, connectCount, context)) {
          if (log.isInfoEnabled()) {
            log.info("I/O exception (" + ex.getClass().getName() + ") caught when connecting to " + route + ": " + ex.getMessage());
            



            if (log.isDebugEnabled()) {
              log.debug(ex.getMessage(), ex);
            }
            log.info("Retrying connect to " + route);
          }
        } else {
          throw ex;
        }
      }
    }
  }
  


  private HttpResponse tryExecute(RoutedRequest req, HttpContext context)
    throws HttpException, IOException
  {
    RequestWrapper wrapper = req.getRequest();
    HttpRoute route = req.getRoute();
    HttpResponse response = null;
    
    Exception retryReason = null;
    for (;;)
    {
      execCount += 1;
      
      wrapper.incrementExecCount();
      if (!wrapper.isRepeatable()) {
        log.debug("Cannot retry non-repeatable request");
        if (retryReason != null) {
          throw new NonRepeatableRequestException("Cannot retry request with a non-repeatable request entity.  The cause lists the reason the original request failed.", retryReason);
        }
        

        throw new NonRepeatableRequestException("Cannot retry request with a non-repeatable request entity.");
      }
      

      try
      {
        if (!managedConn.isOpen())
        {

          if (!route.isTunnelled()) {
            log.debug("Reopening the direct connection.");
            managedConn.open(route, context, params);
          }
          else {
            log.debug("Proxied connection. Need to start over.");
            break;
          }
        }
        
        if (log.isDebugEnabled()) {
          log.debug("Attempt " + execCount + " to execute request");
        }
        response = requestExec.execute(wrapper, managedConn, context);
      }
      catch (IOException ex)
      {
        log.debug("Closing the connection.");
        try {
          managedConn.close();
        }
        catch (IOException ignore) {}
        if (retryHandler.retryRequest(ex, wrapper.getExecCount(), context)) {
          if (log.isInfoEnabled()) {
            log.info("I/O exception (" + ex.getClass().getName() + ") caught when processing request to " + route + ": " + ex.getMessage());
          }
          



          if (log.isDebugEnabled()) {
            log.debug(ex.getMessage(), ex);
          }
          if (log.isInfoEnabled()) {
            log.info("Retrying request to " + route);
          }
          retryReason = ex;
        } else {
          if ((ex instanceof NoHttpResponseException)) {
            NoHttpResponseException updatedex = new NoHttpResponseException(route.getTargetHost().toHostString() + " failed to respond");
            
            updatedex.setStackTrace(ex.getStackTrace());
            throw updatedex;
          }
          throw ex;
        }
      }
    }
    
    return response;
  }
  






  protected void releaseConnection()
  {
    try
    {
      managedConn.releaseConnection();
    } catch (IOException ignored) {
      log.debug("IOException releasing connection", ignored);
    }
    managedConn = null;
  }
  


















  protected HttpRoute determineRoute(HttpHost targetHost, HttpRequest request, HttpContext context)
    throws HttpException
  {
    return routePlanner.determineRoute(targetHost != null ? targetHost : (HttpHost)request.getParams().getParameter("http.default-host"), request, context);
  }
  













  protected void establishRoute(HttpRoute route, HttpContext context)
    throws HttpException, IOException
  {
    HttpRouteDirector rowdy = new BasicRouteDirector();
    int step;
    do {
      HttpRoute fact = managedConn.getRoute();
      step = rowdy.nextStep(route, fact);
      
      switch (step)
      {
      case 1: 
      case 2: 
        managedConn.open(route, context, params);
        break;
      
      case 3: 
        boolean secure = createTunnelToTarget(route, context);
        log.debug("Tunnel to target created.");
        managedConn.tunnelTarget(secure, params);
        break;
      




      case 4: 
        int hop = fact.getHopCount() - 1;
        boolean secure = createTunnelToProxy(route, hop, context);
        log.debug("Tunnel to proxy created.");
        managedConn.tunnelProxy(route.getHopTarget(hop), secure, params);
        
        break;
      

      case 5: 
        managedConn.layerProtocol(context, params);
        break;
      
      case -1: 
        throw new HttpException("Unable to establish route: planned = " + route + "; current = " + fact);
      
      case 0: 
        break;
      
      default: 
        throw new IllegalStateException("Unknown step indicator " + step + " from RouteDirector.");
      
      }
      
    } while (step > 0);
  }
  






















  protected boolean createTunnelToTarget(HttpRoute route, HttpContext context)
    throws HttpException, IOException
  {
    HttpHost proxy = route.getProxyHost();
    HttpHost target = route.getTargetHost();
    HttpResponse response = null;
    for (;;)
    {
      if (!managedConn.isOpen()) {
        managedConn.open(route, context, params);
      }
      
      HttpRequest connect = createConnectRequest(route, context);
      connect.setParams(params);
      

      context.setAttribute("http.target_host", target);
      context.setAttribute("http.route", route);
      context.setAttribute("http.proxy_host", proxy);
      context.setAttribute("http.connection", managedConn);
      context.setAttribute("http.request", connect);
      
      requestExec.preProcess(connect, httpProcessor, context);
      
      response = requestExec.execute(connect, managedConn, context);
      
      response.setParams(params);
      requestExec.postProcess(response, httpProcessor, context);
      
      int status = response.getStatusLine().getStatusCode();
      if (status < 200) {
        throw new HttpException("Unexpected response to CONNECT request: " + response.getStatusLine());
      }
      

      if (HttpClientParams.isAuthenticating(params)) {
        if (!authenticator.isAuthenticationRequested(proxy, response, proxyAuthStrategy, proxyAuthState, context))
          break;
        if (!authenticator.authenticate(proxy, response, proxyAuthStrategy, proxyAuthState, context)) {
          break;
        }
        if (reuseStrategy.keepAlive(response, context)) {
          log.debug("Connection kept alive");
          
          HttpEntity entity = response.getEntity();
          EntityUtils.consume(entity);
        } else {
          managedConn.close();
        }
      }
    }
    






    int status = response.getStatusLine().getStatusCode();
    
    if (status > 299)
    {

      HttpEntity entity = response.getEntity();
      if (entity != null) {
        response.setEntity(new BufferedHttpEntity(entity));
      }
      
      managedConn.close();
      throw new TunnelRefusedException("CONNECT refused by proxy: " + response.getStatusLine(), response);
    }
    

    managedConn.markReusable();
    




    return false;
  }
  






























  protected boolean createTunnelToProxy(HttpRoute route, int hop, HttpContext context)
    throws HttpException, IOException
  {
    throw new HttpException("Proxy chains are not supported.");
  }
  















  protected HttpRequest createConnectRequest(HttpRoute route, HttpContext context)
  {
    HttpHost target = route.getTargetHost();
    
    String host = target.getHostName();
    int port = target.getPort();
    if (port < 0) {
      Scheme scheme = connManager.getSchemeRegistry().getScheme(target.getSchemeName());
      
      port = scheme.getDefaultPort();
    }
    
    StringBuilder buffer = new StringBuilder(host.length() + 6);
    buffer.append(host);
    buffer.append(':');
    buffer.append(Integer.toString(port));
    
    String authority = buffer.toString();
    ProtocolVersion ver = HttpProtocolParams.getVersion(params);
    HttpRequest req = new BasicHttpRequest("CONNECT", authority, ver);
    

    return req;
  }
  
















  protected RoutedRequest handleResponse(RoutedRequest roureq, HttpResponse response, HttpContext context)
    throws HttpException, IOException
  {
    HttpRoute route = roureq.getRoute();
    RequestWrapper request = roureq.getRequest();
    
    HttpParams params = request.getParams();
    
    if (HttpClientParams.isAuthenticating(params)) {
      HttpHost target = (HttpHost)context.getAttribute("http.target_host");
      if (target == null) {
        target = route.getTargetHost();
      }
      if (target.getPort() < 0) {
        Scheme scheme = connManager.getSchemeRegistry().getScheme(target);
        target = new HttpHost(target.getHostName(), scheme.getDefaultPort(), target.getSchemeName());
      }
      
      boolean targetAuthRequested = authenticator.isAuthenticationRequested(target, response, targetAuthStrategy, targetAuthState, context);
      

      HttpHost proxy = route.getProxyHost();
      
      if (proxy == null) {
        proxy = route.getTargetHost();
      }
      boolean proxyAuthRequested = authenticator.isAuthenticationRequested(proxy, response, proxyAuthStrategy, proxyAuthState, context);
      

      if ((targetAuthRequested) && 
        (authenticator.authenticate(target, response, targetAuthStrategy, targetAuthState, context)))
      {

        return roureq;
      }
      
      if ((proxyAuthRequested) && 
        (authenticator.authenticate(proxy, response, proxyAuthStrategy, proxyAuthState, context)))
      {

        return roureq;
      }
    }
    

    if ((HttpClientParams.isRedirecting(params)) && (redirectStrategy.isRedirected(request, response, context)))
    {

      if (redirectCount >= maxRedirects) {
        throw new RedirectException("Maximum redirects (" + maxRedirects + ") exceeded");
      }
      
      redirectCount += 1;
      

      virtualHost = null;
      
      HttpUriRequest redirect = redirectStrategy.getRedirect(request, response, context);
      HttpRequest orig = request.getOriginal();
      redirect.setHeaders(orig.getAllHeaders());
      
      URI uri = redirect.getURI();
      HttpHost newTarget = URIUtils.extractHost(uri);
      if (newTarget == null) {
        throw new ProtocolException("Redirect URI does not specify a valid host name: " + uri);
      }
      

      if (!route.getTargetHost().equals(newTarget)) {
        log.debug("Resetting target auth state");
        targetAuthState.reset();
        AuthScheme authScheme = proxyAuthState.getAuthScheme();
        if ((authScheme != null) && (authScheme.isConnectionBased())) {
          log.debug("Resetting proxy auth state");
          proxyAuthState.reset();
        }
      }
      
      RequestWrapper wrapper = wrapRequest(redirect);
      wrapper.setParams(params);
      
      HttpRoute newRoute = determineRoute(newTarget, wrapper, context);
      RoutedRequest newRequest = new RoutedRequest(wrapper, newRoute);
      
      if (log.isDebugEnabled()) {
        log.debug("Redirecting to '" + uri + "' via " + newRoute);
      }
      
      return newRequest;
    }
    
    return null;
  }
  





  private void abortConnection()
  {
    ManagedClientConnection mcc = managedConn;
    if (mcc != null)
    {

      managedConn = null;
      try {
        mcc.abortConnection();
      } catch (IOException ex) {
        if (log.isDebugEnabled()) {
          log.debug(ex.getMessage(), ex);
        }
      }
      try
      {
        mcc.releaseConnection();
      } catch (IOException ignored) {
        log.debug("Error releasing connection", ignored);
      }
    }
  }
}
