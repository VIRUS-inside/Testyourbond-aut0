package org.apache.http.impl.client;

import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.auth.AuthSchemeRegistry;
import org.apache.http.client.AuthenticationHandler;
import org.apache.http.client.AuthenticationStrategy;
import org.apache.http.client.BackoffManager;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ConnectionBackoffStrategy;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.RedirectHandler;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.RequestDirector;
import org.apache.http.client.UserTokenHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.params.HttpClientParamConfig;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ClientConnectionManagerFactory;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.cookie.CookieSpecRegistry;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.auth.BasicSchemeFactory;
import org.apache.http.impl.auth.DigestSchemeFactory;
import org.apache.http.impl.auth.KerberosSchemeFactory;
import org.apache.http.impl.auth.NTLMSchemeFactory;
import org.apache.http.impl.auth.SPNegoSchemeFactory;
import org.apache.http.impl.conn.BasicClientConnectionManager;
import org.apache.http.impl.conn.DefaultHttpRoutePlanner;
import org.apache.http.impl.conn.SchemeRegistryFactory;
import org.apache.http.impl.cookie.BestMatchSpecFactory;
import org.apache.http.impl.cookie.BrowserCompatSpecFactory;
import org.apache.http.impl.cookie.IgnoreSpecFactory;
import org.apache.http.impl.cookie.NetscapeDraftSpecFactory;
import org.apache.http.impl.cookie.RFC2109SpecFactory;
import org.apache.http.impl.cookie.RFC2965SpecFactory;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.protocol.DefaultedHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.protocol.ImmutableHttpProcessor;
import org.apache.http.util.Args;






































































































































@Deprecated
@Contract(threading=ThreadingBehavior.SAFE_CONDITIONAL)
public abstract class AbstractHttpClient
  extends CloseableHttpClient
{
  private final Log log = LogFactory.getLog(getClass());
  
  private HttpParams defaultParams;
  
  private HttpRequestExecutor requestExec;
  
  private ClientConnectionManager connManager;
  
  private ConnectionReuseStrategy reuseStrategy;
  
  private ConnectionKeepAliveStrategy keepAliveStrategy;
  
  private CookieSpecRegistry supportedCookieSpecs;
  
  private AuthSchemeRegistry supportedAuthSchemes;
  
  private BasicHttpProcessor mutableProcessor;
  
  private ImmutableHttpProcessor protocolProcessor;
  private HttpRequestRetryHandler retryHandler;
  private RedirectStrategy redirectStrategy;
  private AuthenticationStrategy targetAuthStrategy;
  private AuthenticationStrategy proxyAuthStrategy;
  private CookieStore cookieStore;
  private CredentialsProvider credsProvider;
  private HttpRoutePlanner routePlanner;
  private UserTokenHandler userTokenHandler;
  private ConnectionBackoffStrategy connectionBackoffStrategy;
  private BackoffManager backoffManager;
  
  protected AbstractHttpClient(ClientConnectionManager conman, HttpParams params)
  {
    defaultParams = params;
    connManager = conman;
  }
  

  protected abstract HttpParams createHttpParams();
  

  protected abstract BasicHttpProcessor createHttpProcessor();
  
  protected HttpContext createHttpContext()
  {
    HttpContext context = new BasicHttpContext();
    context.setAttribute("http.scheme-registry", getConnectionManager().getSchemeRegistry());
    

    context.setAttribute("http.authscheme-registry", getAuthSchemes());
    

    context.setAttribute("http.cookiespec-registry", getCookieSpecs());
    

    context.setAttribute("http.cookie-store", getCookieStore());
    

    context.setAttribute("http.auth.credentials-provider", getCredentialsProvider());
    

    return context;
  }
  
  protected ClientConnectionManager createClientConnectionManager()
  {
    SchemeRegistry registry = SchemeRegistryFactory.createDefault();
    
    ClientConnectionManager connManager = null;
    HttpParams params = getParams();
    
    ClientConnectionManagerFactory factory = null;
    
    String className = (String)params.getParameter("http.connection-manager.factory-class-name");
    
    ClassLoader contextLoader = Thread.currentThread().getContextClassLoader();
    if (className != null) {
      try { Class<?> clazz;
        Class<?> clazz;
        if (contextLoader != null) {
          clazz = Class.forName(className, true, contextLoader);
        } else {
          clazz = Class.forName(className);
        }
        factory = (ClientConnectionManagerFactory)clazz.newInstance();
      } catch (ClassNotFoundException ex) {
        throw new IllegalStateException("Invalid class name: " + className);
      } catch (IllegalAccessException ex) {
        throw new IllegalAccessError(ex.getMessage());
      } catch (InstantiationException ex) {
        throw new InstantiationError(ex.getMessage());
      }
    }
    if (factory != null) {
      connManager = factory.newInstance(params, registry);
    } else {
      connManager = new BasicClientConnectionManager(registry);
    }
    
    return connManager;
  }
  
  protected AuthSchemeRegistry createAuthSchemeRegistry()
  {
    AuthSchemeRegistry registry = new AuthSchemeRegistry();
    registry.register("Basic", new BasicSchemeFactory());
    

    registry.register("Digest", new DigestSchemeFactory());
    

    registry.register("NTLM", new NTLMSchemeFactory());
    

    registry.register("Negotiate", new SPNegoSchemeFactory());
    

    registry.register("Kerberos", new KerberosSchemeFactory());
    

    return registry;
  }
  
  protected CookieSpecRegistry createCookieSpecRegistry()
  {
    CookieSpecRegistry registry = new CookieSpecRegistry();
    registry.register("default", new BestMatchSpecFactory());
    

    registry.register("best-match", new BestMatchSpecFactory());
    

    registry.register("compatibility", new BrowserCompatSpecFactory());
    

    registry.register("netscape", new NetscapeDraftSpecFactory());
    

    registry.register("rfc2109", new RFC2109SpecFactory());
    

    registry.register("rfc2965", new RFC2965SpecFactory());
    

    registry.register("ignoreCookies", new IgnoreSpecFactory());
    

    return registry;
  }
  
  protected HttpRequestExecutor createRequestExecutor() {
    return new HttpRequestExecutor();
  }
  
  protected ConnectionReuseStrategy createConnectionReuseStrategy() {
    return new DefaultConnectionReuseStrategy();
  }
  
  protected ConnectionKeepAliveStrategy createConnectionKeepAliveStrategy() {
    return new DefaultConnectionKeepAliveStrategy();
  }
  
  protected HttpRequestRetryHandler createHttpRequestRetryHandler() {
    return new DefaultHttpRequestRetryHandler();
  }
  


  @Deprecated
  protected RedirectHandler createRedirectHandler()
  {
    return new DefaultRedirectHandler();
  }
  
  protected AuthenticationStrategy createTargetAuthenticationStrategy() {
    return new TargetAuthenticationStrategy();
  }
  


  @Deprecated
  protected AuthenticationHandler createTargetAuthenticationHandler()
  {
    return new DefaultTargetAuthenticationHandler();
  }
  
  protected AuthenticationStrategy createProxyAuthenticationStrategy() {
    return new ProxyAuthenticationStrategy();
  }
  


  @Deprecated
  protected AuthenticationHandler createProxyAuthenticationHandler()
  {
    return new DefaultProxyAuthenticationHandler();
  }
  
  protected CookieStore createCookieStore() {
    return new BasicCookieStore();
  }
  
  protected CredentialsProvider createCredentialsProvider() {
    return new BasicCredentialsProvider();
  }
  
  protected HttpRoutePlanner createHttpRoutePlanner() {
    return new DefaultHttpRoutePlanner(getConnectionManager().getSchemeRegistry());
  }
  
  protected UserTokenHandler createUserTokenHandler() {
    return new DefaultUserTokenHandler();
  }
  

  public final synchronized HttpParams getParams()
  {
    if (defaultParams == null) {
      defaultParams = createHttpParams();
    }
    return defaultParams;
  }
  





  public synchronized void setParams(HttpParams params)
  {
    defaultParams = params;
  }
  

  public final synchronized ClientConnectionManager getConnectionManager()
  {
    if (connManager == null) {
      connManager = createClientConnectionManager();
    }
    return connManager;
  }
  
  public final synchronized HttpRequestExecutor getRequestExecutor()
  {
    if (requestExec == null) {
      requestExec = createRequestExecutor();
    }
    return requestExec;
  }
  
  public final synchronized AuthSchemeRegistry getAuthSchemes()
  {
    if (supportedAuthSchemes == null) {
      supportedAuthSchemes = createAuthSchemeRegistry();
    }
    return supportedAuthSchemes;
  }
  
  public synchronized void setAuthSchemes(AuthSchemeRegistry registry) {
    supportedAuthSchemes = registry;
  }
  
  public final synchronized ConnectionBackoffStrategy getConnectionBackoffStrategy() {
    return connectionBackoffStrategy;
  }
  
  public synchronized void setConnectionBackoffStrategy(ConnectionBackoffStrategy strategy) {
    connectionBackoffStrategy = strategy;
  }
  
  public final synchronized CookieSpecRegistry getCookieSpecs() {
    if (supportedCookieSpecs == null) {
      supportedCookieSpecs = createCookieSpecRegistry();
    }
    return supportedCookieSpecs;
  }
  
  public final synchronized BackoffManager getBackoffManager() {
    return backoffManager;
  }
  
  public synchronized void setBackoffManager(BackoffManager manager) {
    backoffManager = manager;
  }
  
  public synchronized void setCookieSpecs(CookieSpecRegistry registry) {
    supportedCookieSpecs = registry;
  }
  
  public final synchronized ConnectionReuseStrategy getConnectionReuseStrategy() {
    if (reuseStrategy == null) {
      reuseStrategy = createConnectionReuseStrategy();
    }
    return reuseStrategy;
  }
  
  public synchronized void setReuseStrategy(ConnectionReuseStrategy strategy)
  {
    reuseStrategy = strategy;
  }
  
  public final synchronized ConnectionKeepAliveStrategy getConnectionKeepAliveStrategy()
  {
    if (keepAliveStrategy == null) {
      keepAliveStrategy = createConnectionKeepAliveStrategy();
    }
    return keepAliveStrategy;
  }
  
  public synchronized void setKeepAliveStrategy(ConnectionKeepAliveStrategy strategy)
  {
    keepAliveStrategy = strategy;
  }
  
  public final synchronized HttpRequestRetryHandler getHttpRequestRetryHandler()
  {
    if (retryHandler == null) {
      retryHandler = createHttpRequestRetryHandler();
    }
    return retryHandler;
  }
  
  public synchronized void setHttpRequestRetryHandler(HttpRequestRetryHandler handler) {
    retryHandler = handler;
  }
  


  @Deprecated
  public final synchronized RedirectHandler getRedirectHandler()
  {
    return createRedirectHandler();
  }
  


  @Deprecated
  public synchronized void setRedirectHandler(RedirectHandler handler)
  {
    redirectStrategy = new DefaultRedirectStrategyAdaptor(handler);
  }
  


  public final synchronized RedirectStrategy getRedirectStrategy()
  {
    if (redirectStrategy == null) {
      redirectStrategy = new DefaultRedirectStrategy();
    }
    return redirectStrategy;
  }
  


  public synchronized void setRedirectStrategy(RedirectStrategy strategy)
  {
    redirectStrategy = strategy;
  }
  


  @Deprecated
  public final synchronized AuthenticationHandler getTargetAuthenticationHandler()
  {
    return createTargetAuthenticationHandler();
  }
  


  @Deprecated
  public synchronized void setTargetAuthenticationHandler(AuthenticationHandler handler)
  {
    targetAuthStrategy = new AuthenticationStrategyAdaptor(handler);
  }
  


  public final synchronized AuthenticationStrategy getTargetAuthenticationStrategy()
  {
    if (targetAuthStrategy == null) {
      targetAuthStrategy = createTargetAuthenticationStrategy();
    }
    return targetAuthStrategy;
  }
  


  public synchronized void setTargetAuthenticationStrategy(AuthenticationStrategy strategy)
  {
    targetAuthStrategy = strategy;
  }
  


  @Deprecated
  public final synchronized AuthenticationHandler getProxyAuthenticationHandler()
  {
    return createProxyAuthenticationHandler();
  }
  


  @Deprecated
  public synchronized void setProxyAuthenticationHandler(AuthenticationHandler handler)
  {
    proxyAuthStrategy = new AuthenticationStrategyAdaptor(handler);
  }
  


  public final synchronized AuthenticationStrategy getProxyAuthenticationStrategy()
  {
    if (proxyAuthStrategy == null) {
      proxyAuthStrategy = createProxyAuthenticationStrategy();
    }
    return proxyAuthStrategy;
  }
  


  public synchronized void setProxyAuthenticationStrategy(AuthenticationStrategy strategy)
  {
    proxyAuthStrategy = strategy;
  }
  
  public final synchronized CookieStore getCookieStore() {
    if (cookieStore == null) {
      cookieStore = createCookieStore();
    }
    return cookieStore;
  }
  
  public synchronized void setCookieStore(CookieStore cookieStore) {
    this.cookieStore = cookieStore;
  }
  
  public final synchronized CredentialsProvider getCredentialsProvider() {
    if (credsProvider == null) {
      credsProvider = createCredentialsProvider();
    }
    return credsProvider;
  }
  
  public synchronized void setCredentialsProvider(CredentialsProvider credsProvider) {
    this.credsProvider = credsProvider;
  }
  
  public final synchronized HttpRoutePlanner getRoutePlanner() {
    if (routePlanner == null) {
      routePlanner = createHttpRoutePlanner();
    }
    return routePlanner;
  }
  
  public synchronized void setRoutePlanner(HttpRoutePlanner routePlanner) {
    this.routePlanner = routePlanner;
  }
  
  public final synchronized UserTokenHandler getUserTokenHandler() {
    if (userTokenHandler == null) {
      userTokenHandler = createUserTokenHandler();
    }
    return userTokenHandler;
  }
  
  public synchronized void setUserTokenHandler(UserTokenHandler handler) {
    userTokenHandler = handler;
  }
  
  protected final synchronized BasicHttpProcessor getHttpProcessor() {
    if (mutableProcessor == null) {
      mutableProcessor = createHttpProcessor();
    }
    return mutableProcessor;
  }
  
  private synchronized HttpProcessor getProtocolProcessor() {
    if (protocolProcessor == null)
    {
      BasicHttpProcessor proc = getHttpProcessor();
      
      int reqc = proc.getRequestInterceptorCount();
      HttpRequestInterceptor[] reqinterceptors = new HttpRequestInterceptor[reqc];
      for (int i = 0; i < reqc; i++) {
        reqinterceptors[i] = proc.getRequestInterceptor(i);
      }
      int resc = proc.getResponseInterceptorCount();
      HttpResponseInterceptor[] resinterceptors = new HttpResponseInterceptor[resc];
      for (int i = 0; i < resc; i++) {
        resinterceptors[i] = proc.getResponseInterceptor(i);
      }
      protocolProcessor = new ImmutableHttpProcessor(reqinterceptors, resinterceptors);
    }
    return protocolProcessor;
  }
  
  public synchronized int getResponseInterceptorCount() {
    return getHttpProcessor().getResponseInterceptorCount();
  }
  
  public synchronized HttpResponseInterceptor getResponseInterceptor(int index) {
    return getHttpProcessor().getResponseInterceptor(index);
  }
  
  public synchronized HttpRequestInterceptor getRequestInterceptor(int index) {
    return getHttpProcessor().getRequestInterceptor(index);
  }
  
  public synchronized int getRequestInterceptorCount() {
    return getHttpProcessor().getRequestInterceptorCount();
  }
  
  public synchronized void addResponseInterceptor(HttpResponseInterceptor itcp) {
    getHttpProcessor().addInterceptor(itcp);
    protocolProcessor = null;
  }
  
  public synchronized void addResponseInterceptor(HttpResponseInterceptor itcp, int index) {
    getHttpProcessor().addInterceptor(itcp, index);
    protocolProcessor = null;
  }
  
  public synchronized void clearResponseInterceptors() {
    getHttpProcessor().clearResponseInterceptors();
    protocolProcessor = null;
  }
  
  public synchronized void removeResponseInterceptorByClass(Class<? extends HttpResponseInterceptor> clazz) {
    getHttpProcessor().removeResponseInterceptorByClass(clazz);
    protocolProcessor = null;
  }
  
  public synchronized void addRequestInterceptor(HttpRequestInterceptor itcp) {
    getHttpProcessor().addInterceptor(itcp);
    protocolProcessor = null;
  }
  
  public synchronized void addRequestInterceptor(HttpRequestInterceptor itcp, int index) {
    getHttpProcessor().addInterceptor(itcp, index);
    protocolProcessor = null;
  }
  
  public synchronized void clearRequestInterceptors() {
    getHttpProcessor().clearRequestInterceptors();
    protocolProcessor = null;
  }
  
  public synchronized void removeRequestInterceptorByClass(Class<? extends HttpRequestInterceptor> clazz) {
    getHttpProcessor().removeRequestInterceptorByClass(clazz);
    protocolProcessor = null;
  }
  


  protected final CloseableHttpResponse doExecute(HttpHost target, HttpRequest request, HttpContext context)
    throws IOException, ClientProtocolException
  {
    Args.notNull(request, "HTTP request");
    


    HttpContext execContext = null;
    RequestDirector director = null;
    HttpRoutePlanner routePlanner = null;
    ConnectionBackoffStrategy connectionBackoffStrategy = null;
    BackoffManager backoffManager = null;
    


    synchronized (this)
    {
      HttpContext defaultContext = createHttpContext();
      if (context == null) {
        execContext = defaultContext;
      } else {
        execContext = new DefaultedHttpContext(context, defaultContext);
      }
      HttpParams params = determineParams(request);
      RequestConfig config = HttpClientParamConfig.getRequestConfig(params);
      execContext.setAttribute("http.request-config", config);
      

      director = createClientRequestDirector(getRequestExecutor(), getConnectionManager(), getConnectionReuseStrategy(), getConnectionKeepAliveStrategy(), getRoutePlanner(), getProtocolProcessor(), getHttpRequestRetryHandler(), getRedirectStrategy(), getTargetAuthenticationStrategy(), getProxyAuthenticationStrategy(), getUserTokenHandler(), params);
      











      routePlanner = getRoutePlanner();
      connectionBackoffStrategy = getConnectionBackoffStrategy();
      backoffManager = getBackoffManager();
    }
    try
    {
      if ((connectionBackoffStrategy != null) && (backoffManager != null)) {
        HttpHost targetForRoute = target != null ? target : (HttpHost)determineParams(request).getParameter("http.default-host");
        

        HttpRoute route = routePlanner.determineRoute(targetForRoute, request, execContext);
        CloseableHttpResponse out;
        try
        {
          out = CloseableHttpResponseProxy.newProxy(director.execute(target, request, execContext));
        }
        catch (RuntimeException re) {
          if (connectionBackoffStrategy.shouldBackoff(re)) {
            backoffManager.backOff(route);
          }
          throw re;
        } catch (Exception e) {
          if (connectionBackoffStrategy.shouldBackoff(e)) {
            backoffManager.backOff(route);
          }
          if ((e instanceof HttpException)) {
            throw ((HttpException)e);
          }
          if ((e instanceof IOException)) {
            throw ((IOException)e);
          }
          throw new UndeclaredThrowableException(e);
        }
        if (connectionBackoffStrategy.shouldBackoff(out)) {
          backoffManager.backOff(route);
        } else {
          backoffManager.probe(route);
        }
        return out;
      }
      return CloseableHttpResponseProxy.newProxy(director.execute(target, request, execContext));
    }
    catch (HttpException httpException)
    {
      throw new ClientProtocolException(httpException);
    }
  }
  














  @Deprecated
  protected RequestDirector createClientRequestDirector(HttpRequestExecutor requestExec, ClientConnectionManager conman, ConnectionReuseStrategy reustrat, ConnectionKeepAliveStrategy kastrat, HttpRoutePlanner rouplan, HttpProcessor httpProcessor, HttpRequestRetryHandler retryHandler, RedirectHandler redirectHandler, AuthenticationHandler targetAuthHandler, AuthenticationHandler proxyAuthHandler, UserTokenHandler userTokenHandler, HttpParams params)
  {
    return new DefaultRequestDirector(requestExec, conman, reustrat, kastrat, rouplan, httpProcessor, retryHandler, redirectHandler, targetAuthHandler, proxyAuthHandler, userTokenHandler, params);
  }
  


























  @Deprecated
  protected RequestDirector createClientRequestDirector(HttpRequestExecutor requestExec, ClientConnectionManager conman, ConnectionReuseStrategy reustrat, ConnectionKeepAliveStrategy kastrat, HttpRoutePlanner rouplan, HttpProcessor httpProcessor, HttpRequestRetryHandler retryHandler, RedirectStrategy redirectStrategy, AuthenticationHandler targetAuthHandler, AuthenticationHandler proxyAuthHandler, UserTokenHandler userTokenHandler, HttpParams params)
  {
    return new DefaultRequestDirector(log, requestExec, conman, reustrat, kastrat, rouplan, httpProcessor, retryHandler, redirectStrategy, targetAuthHandler, proxyAuthHandler, userTokenHandler, params);
  }
  




























  protected RequestDirector createClientRequestDirector(HttpRequestExecutor requestExec, ClientConnectionManager conman, ConnectionReuseStrategy reustrat, ConnectionKeepAliveStrategy kastrat, HttpRoutePlanner rouplan, HttpProcessor httpProcessor, HttpRequestRetryHandler retryHandler, RedirectStrategy redirectStrategy, AuthenticationStrategy targetAuthStrategy, AuthenticationStrategy proxyAuthStrategy, UserTokenHandler userTokenHandler, HttpParams params)
  {
    return new DefaultRequestDirector(log, requestExec, conman, reustrat, kastrat, rouplan, httpProcessor, retryHandler, redirectStrategy, targetAuthStrategy, proxyAuthStrategy, userTokenHandler, params);
  }
  




























  protected HttpParams determineParams(HttpRequest req)
  {
    return new ClientParamsStack(null, getParams(), req.getParams(), null);
  }
  


  public void close()
  {
    getConnectionManager().shutdown();
  }
}
