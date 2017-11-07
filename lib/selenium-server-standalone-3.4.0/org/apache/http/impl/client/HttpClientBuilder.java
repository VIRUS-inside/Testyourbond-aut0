package org.apache.http.impl.client;

import java.io.Closeable;
import java.io.IOException;
import java.net.ProxySelector;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.client.AuthenticationStrategy;
import org.apache.http.client.BackoffManager;
import org.apache.http.client.ConnectionBackoffStrategy;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.client.UserTokenHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.InputStreamFactory;
import org.apache.http.client.protocol.RequestAcceptEncoding;
import org.apache.http.client.protocol.RequestAddCookies;
import org.apache.http.client.protocol.RequestAuthCache;
import org.apache.http.client.protocol.RequestClientConnControl;
import org.apache.http.client.protocol.RequestDefaultHeaders;
import org.apache.http.client.protocol.RequestExpectContinue;
import org.apache.http.client.protocol.ResponseContentEncoding;
import org.apache.http.client.protocol.ResponseProcessCookies;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Lookup;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.SchemePortResolver;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.conn.util.PublicSuffixMatcher;
import org.apache.http.conn.util.PublicSuffixMatcherLoader;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.impl.NoConnectionReuseStrategy;
import org.apache.http.impl.auth.BasicSchemeFactory;
import org.apache.http.impl.auth.DigestSchemeFactory;
import org.apache.http.impl.auth.KerberosSchemeFactory;
import org.apache.http.impl.auth.NTLMSchemeFactory;
import org.apache.http.impl.auth.SPNegoSchemeFactory;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.DefaultRoutePlanner;
import org.apache.http.impl.conn.DefaultSchemePortResolver;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultRoutePlanner;
import org.apache.http.impl.execchain.BackoffStrategyExec;
import org.apache.http.impl.execchain.ClientExecChain;
import org.apache.http.impl.execchain.MainClientExec;
import org.apache.http.impl.execchain.ProtocolExec;
import org.apache.http.impl.execchain.RedirectExec;
import org.apache.http.impl.execchain.RetryExec;
import org.apache.http.impl.execchain.ServiceUnavailableRetryExec;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpProcessorBuilder;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.protocol.ImmutableHttpProcessor;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestUserAgent;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.TextUtils;
import org.apache.http.util.VersionInfo;








































































public class HttpClientBuilder
{
  private HttpRequestExecutor requestExec;
  private HostnameVerifier hostnameVerifier;
  private LayeredConnectionSocketFactory sslSocketFactory;
  private SSLContext sslContext;
  private HttpClientConnectionManager connManager;
  private boolean connManagerShared;
  private SchemePortResolver schemePortResolver;
  private ConnectionReuseStrategy reuseStrategy;
  private ConnectionKeepAliveStrategy keepAliveStrategy;
  private AuthenticationStrategy targetAuthStrategy;
  private AuthenticationStrategy proxyAuthStrategy;
  private UserTokenHandler userTokenHandler;
  private HttpProcessor httpprocessor;
  private DnsResolver dnsResolver;
  private LinkedList<HttpRequestInterceptor> requestFirst;
  private LinkedList<HttpRequestInterceptor> requestLast;
  private LinkedList<HttpResponseInterceptor> responseFirst;
  private LinkedList<HttpResponseInterceptor> responseLast;
  private HttpRequestRetryHandler retryHandler;
  private HttpRoutePlanner routePlanner;
  private RedirectStrategy redirectStrategy;
  private ConnectionBackoffStrategy connectionBackoffStrategy;
  private BackoffManager backoffManager;
  private ServiceUnavailableRetryStrategy serviceUnavailStrategy;
  private Lookup<AuthSchemeProvider> authSchemeRegistry;
  private Lookup<CookieSpecProvider> cookieSpecRegistry;
  private Map<String, InputStreamFactory> contentDecoderMap;
  private CookieStore cookieStore;
  private CredentialsProvider credentialsProvider;
  private String userAgent;
  private HttpHost proxy;
  private Collection<? extends Header> defaultHeaders;
  private SocketConfig defaultSocketConfig;
  private ConnectionConfig defaultConnectionConfig;
  private RequestConfig defaultRequestConfig;
  private boolean evictExpiredConnections;
  private boolean evictIdleConnections;
  private long maxIdleTime;
  private TimeUnit maxIdleTimeUnit;
  private boolean systemProperties;
  private boolean redirectHandlingDisabled;
  private boolean automaticRetriesDisabled;
  private boolean contentCompressionDisabled;
  private boolean cookieManagementDisabled;
  private boolean authCachingDisabled;
  private boolean connectionStateDisabled;
  private int maxConnTotal = 0;
  private int maxConnPerRoute = 0;
  
  private long connTimeToLive = -1L;
  private TimeUnit connTimeToLiveTimeUnit = TimeUnit.MILLISECONDS;
  
  private List<Closeable> closeables;
  private PublicSuffixMatcher publicSuffixMatcher;
  
  public static HttpClientBuilder create()
  {
    return new HttpClientBuilder();
  }
  


  protected HttpClientBuilder() {}
  


  public final HttpClientBuilder setRequestExecutor(HttpRequestExecutor requestExec)
  {
    this.requestExec = requestExec;
    return this;
  }
  









  @Deprecated
  public final HttpClientBuilder setHostnameVerifier(X509HostnameVerifier hostnameVerifier)
  {
    this.hostnameVerifier = hostnameVerifier;
    return this;
  }
  









  public final HttpClientBuilder setSSLHostnameVerifier(HostnameVerifier hostnameVerifier)
  {
    this.hostnameVerifier = hostnameVerifier;
    return this;
  }
  








  public final HttpClientBuilder setPublicSuffixMatcher(PublicSuffixMatcher publicSuffixMatcher)
  {
    this.publicSuffixMatcher = publicSuffixMatcher;
    return this;
  }
  









  @Deprecated
  public final HttpClientBuilder setSslcontext(SSLContext sslcontext)
  {
    return setSSLContext(sslcontext);
  }
  







  public final HttpClientBuilder setSSLContext(SSLContext sslContext)
  {
    this.sslContext = sslContext;
    return this;
  }
  







  public final HttpClientBuilder setSSLSocketFactory(LayeredConnectionSocketFactory sslSocketFactory)
  {
    this.sslSocketFactory = sslSocketFactory;
    return this;
  }
  






  public final HttpClientBuilder setMaxConnTotal(int maxConnTotal)
  {
    this.maxConnTotal = maxConnTotal;
    return this;
  }
  






  public final HttpClientBuilder setMaxConnPerRoute(int maxConnPerRoute)
  {
    this.maxConnPerRoute = maxConnPerRoute;
    return this;
  }
  






  public final HttpClientBuilder setDefaultSocketConfig(SocketConfig config)
  {
    defaultSocketConfig = config;
    return this;
  }
  






  public final HttpClientBuilder setDefaultConnectionConfig(ConnectionConfig config)
  {
    defaultConnectionConfig = config;
    return this;
  }
  








  public final HttpClientBuilder setConnectionTimeToLive(long connTimeToLive, TimeUnit connTimeToLiveTimeUnit)
  {
    this.connTimeToLive = connTimeToLive;
    this.connTimeToLiveTimeUnit = connTimeToLiveTimeUnit;
    return this;
  }
  



  public final HttpClientBuilder setConnectionManager(HttpClientConnectionManager connManager)
  {
    this.connManager = connManager;
    return this;
  }
  














  public final HttpClientBuilder setConnectionManagerShared(boolean shared)
  {
    connManagerShared = shared;
    return this;
  }
  



  public final HttpClientBuilder setConnectionReuseStrategy(ConnectionReuseStrategy reuseStrategy)
  {
    this.reuseStrategy = reuseStrategy;
    return this;
  }
  



  public final HttpClientBuilder setKeepAliveStrategy(ConnectionKeepAliveStrategy keepAliveStrategy)
  {
    this.keepAliveStrategy = keepAliveStrategy;
    return this;
  }
  




  public final HttpClientBuilder setTargetAuthenticationStrategy(AuthenticationStrategy targetAuthStrategy)
  {
    this.targetAuthStrategy = targetAuthStrategy;
    return this;
  }
  




  public final HttpClientBuilder setProxyAuthenticationStrategy(AuthenticationStrategy proxyAuthStrategy)
  {
    this.proxyAuthStrategy = proxyAuthStrategy;
    return this;
  }
  






  public final HttpClientBuilder setUserTokenHandler(UserTokenHandler userTokenHandler)
  {
    this.userTokenHandler = userTokenHandler;
    return this;
  }
  


  public final HttpClientBuilder disableConnectionState()
  {
    connectionStateDisabled = true;
    return this;
  }
  



  public final HttpClientBuilder setSchemePortResolver(SchemePortResolver schemePortResolver)
  {
    this.schemePortResolver = schemePortResolver;
    return this;
  }
  






  public final HttpClientBuilder setUserAgent(String userAgent)
  {
    this.userAgent = userAgent;
    return this;
  }
  






  public final HttpClientBuilder setDefaultHeaders(Collection<? extends Header> defaultHeaders)
  {
    this.defaultHeaders = defaultHeaders;
    return this;
  }
  






  public final HttpClientBuilder addInterceptorFirst(HttpResponseInterceptor itcp)
  {
    if (itcp == null) {
      return this;
    }
    if (responseFirst == null) {
      responseFirst = new LinkedList();
    }
    responseFirst.addFirst(itcp);
    return this;
  }
  






  public final HttpClientBuilder addInterceptorLast(HttpResponseInterceptor itcp)
  {
    if (itcp == null) {
      return this;
    }
    if (responseLast == null) {
      responseLast = new LinkedList();
    }
    responseLast.addLast(itcp);
    return this;
  }
  





  public final HttpClientBuilder addInterceptorFirst(HttpRequestInterceptor itcp)
  {
    if (itcp == null) {
      return this;
    }
    if (requestFirst == null) {
      requestFirst = new LinkedList();
    }
    requestFirst.addFirst(itcp);
    return this;
  }
  





  public final HttpClientBuilder addInterceptorLast(HttpRequestInterceptor itcp)
  {
    if (itcp == null) {
      return this;
    }
    if (requestLast == null) {
      requestLast = new LinkedList();
    }
    requestLast.addLast(itcp);
    return this;
  }
  





  public final HttpClientBuilder disableCookieManagement()
  {
    cookieManagementDisabled = true;
    return this;
  }
  





  public final HttpClientBuilder disableContentCompression()
  {
    contentCompressionDisabled = true;
    return this;
  }
  





  public final HttpClientBuilder disableAuthCaching()
  {
    authCachingDisabled = true;
    return this;
  }
  


  public final HttpClientBuilder setHttpProcessor(HttpProcessor httpprocessor)
  {
    this.httpprocessor = httpprocessor;
    return this;
  }
  




  public final HttpClientBuilder setDnsResolver(DnsResolver dnsResolver)
  {
    this.dnsResolver = dnsResolver;
    return this;
  }
  





  public final HttpClientBuilder setRetryHandler(HttpRequestRetryHandler retryHandler)
  {
    this.retryHandler = retryHandler;
    return this;
  }
  


  public final HttpClientBuilder disableAutomaticRetries()
  {
    automaticRetriesDisabled = true;
    return this;
  }
  





  public final HttpClientBuilder setProxy(HttpHost proxy)
  {
    this.proxy = proxy;
    return this;
  }
  


  public final HttpClientBuilder setRoutePlanner(HttpRoutePlanner routePlanner)
  {
    this.routePlanner = routePlanner;
    return this;
  }
  






  public final HttpClientBuilder setRedirectStrategy(RedirectStrategy redirectStrategy)
  {
    this.redirectStrategy = redirectStrategy;
    return this;
  }
  


  public final HttpClientBuilder disableRedirectHandling()
  {
    redirectHandlingDisabled = true;
    return this;
  }
  



  public final HttpClientBuilder setConnectionBackoffStrategy(ConnectionBackoffStrategy connectionBackoffStrategy)
  {
    this.connectionBackoffStrategy = connectionBackoffStrategy;
    return this;
  }
  


  public final HttpClientBuilder setBackoffManager(BackoffManager backoffManager)
  {
    this.backoffManager = backoffManager;
    return this;
  }
  



  public final HttpClientBuilder setServiceUnavailableRetryStrategy(ServiceUnavailableRetryStrategy serviceUnavailStrategy)
  {
    this.serviceUnavailStrategy = serviceUnavailStrategy;
    return this;
  }
  



  public final HttpClientBuilder setDefaultCookieStore(CookieStore cookieStore)
  {
    this.cookieStore = cookieStore;
    return this;
  }
  





  public final HttpClientBuilder setDefaultCredentialsProvider(CredentialsProvider credentialsProvider)
  {
    this.credentialsProvider = credentialsProvider;
    return this;
  }
  





  public final HttpClientBuilder setDefaultAuthSchemeRegistry(Lookup<AuthSchemeProvider> authSchemeRegistry)
  {
    this.authSchemeRegistry = authSchemeRegistry;
    return this;
  }
  








  public final HttpClientBuilder setDefaultCookieSpecRegistry(Lookup<CookieSpecProvider> cookieSpecRegistry)
  {
    this.cookieSpecRegistry = cookieSpecRegistry;
    return this;
  }
  





  public final HttpClientBuilder setContentDecoderRegistry(Map<String, InputStreamFactory> contentDecoderMap)
  {
    this.contentDecoderMap = contentDecoderMap;
    return this;
  }
  




  public final HttpClientBuilder setDefaultRequestConfig(RequestConfig config)
  {
    defaultRequestConfig = config;
    return this;
  }
  



  public final HttpClientBuilder useSystemProperties()
  {
    systemProperties = true;
    return this;
  }
  

















  public final HttpClientBuilder evictExpiredConnections()
  {
    evictExpiredConnections = true;
    return this;
  }
  
























  @Deprecated
  public final HttpClientBuilder evictIdleConnections(Long maxIdleTime, TimeUnit maxIdleTimeUnit)
  {
    return evictIdleConnections(maxIdleTime.longValue(), maxIdleTimeUnit);
  }
  






















  public final HttpClientBuilder evictIdleConnections(long maxIdleTime, TimeUnit maxIdleTimeUnit)
  {
    evictIdleConnections = true;
    this.maxIdleTime = maxIdleTime;
    this.maxIdleTimeUnit = maxIdleTimeUnit;
    return this;
  }
  



















  protected ClientExecChain createMainExec(HttpRequestExecutor requestExec, HttpClientConnectionManager connManager, ConnectionReuseStrategy reuseStrategy, ConnectionKeepAliveStrategy keepAliveStrategy, HttpProcessor proxyHttpProcessor, AuthenticationStrategy targetAuthStrategy, AuthenticationStrategy proxyAuthStrategy, UserTokenHandler userTokenHandler)
  {
    return new MainClientExec(requestExec, connManager, reuseStrategy, keepAliveStrategy, proxyHttpProcessor, targetAuthStrategy, proxyAuthStrategy, userTokenHandler);
  }
  










  protected ClientExecChain decorateMainExec(ClientExecChain mainExec)
  {
    return mainExec;
  }
  


  protected ClientExecChain decorateProtocolExec(ClientExecChain protocolExec)
  {
    return protocolExec;
  }
  


  protected void addCloseable(Closeable closeable)
  {
    if (closeable == null) {
      return;
    }
    if (closeables == null) {
      closeables = new ArrayList();
    }
    closeables.add(closeable);
  }
  
  private static String[] split(String s) {
    if (TextUtils.isBlank(s)) {
      return null;
    }
    return s.split(" *, *");
  }
  

  public CloseableHttpClient build()
  {
    PublicSuffixMatcher publicSuffixMatcherCopy = publicSuffixMatcher;
    if (publicSuffixMatcherCopy == null) {
      publicSuffixMatcherCopy = PublicSuffixMatcherLoader.getDefault();
    }
    
    HttpRequestExecutor requestExecCopy = requestExec;
    if (requestExecCopy == null) {
      requestExecCopy = new HttpRequestExecutor();
    }
    HttpClientConnectionManager connManagerCopy = connManager;
    if (connManagerCopy == null) {
      LayeredConnectionSocketFactory sslSocketFactoryCopy = sslSocketFactory;
      if (sslSocketFactoryCopy == null) {
        String[] supportedProtocols = systemProperties ? split(System.getProperty("https.protocols")) : null;
        
        String[] supportedCipherSuites = systemProperties ? split(System.getProperty("https.cipherSuites")) : null;
        
        HostnameVerifier hostnameVerifierCopy = hostnameVerifier;
        if (hostnameVerifierCopy == null) {
          hostnameVerifierCopy = new DefaultHostnameVerifier(publicSuffixMatcherCopy);
        }
        if (sslContext != null) {
          sslSocketFactoryCopy = new SSLConnectionSocketFactory(sslContext, supportedProtocols, supportedCipherSuites, hostnameVerifierCopy);

        }
        else if (systemProperties) {
          sslSocketFactoryCopy = new SSLConnectionSocketFactory((SSLSocketFactory)SSLSocketFactory.getDefault(), supportedProtocols, supportedCipherSuites, hostnameVerifierCopy);
        }
        else
        {
          sslSocketFactoryCopy = new SSLConnectionSocketFactory(SSLContexts.createDefault(), hostnameVerifierCopy);
        }
      }
      



      PoolingHttpClientConnectionManager poolingmgr = new PoolingHttpClientConnectionManager(RegistryBuilder.create().register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", sslSocketFactoryCopy).build(), null, null, dnsResolver, connTimeToLive, connTimeToLiveTimeUnit != null ? connTimeToLiveTimeUnit : TimeUnit.MILLISECONDS);
      








      if (defaultSocketConfig != null) {
        poolingmgr.setDefaultSocketConfig(defaultSocketConfig);
      }
      if (defaultConnectionConfig != null) {
        poolingmgr.setDefaultConnectionConfig(defaultConnectionConfig);
      }
      if (systemProperties) {
        String s = System.getProperty("http.keepAlive", "true");
        if ("true".equalsIgnoreCase(s)) {
          s = System.getProperty("http.maxConnections", "5");
          int max = Integer.parseInt(s);
          poolingmgr.setDefaultMaxPerRoute(max);
          poolingmgr.setMaxTotal(2 * max);
        }
      }
      if (maxConnTotal > 0) {
        poolingmgr.setMaxTotal(maxConnTotal);
      }
      if (maxConnPerRoute > 0) {
        poolingmgr.setDefaultMaxPerRoute(maxConnPerRoute);
      }
      connManagerCopy = poolingmgr;
    }
    ConnectionReuseStrategy reuseStrategyCopy = reuseStrategy;
    if (reuseStrategyCopy == null) {
      if (systemProperties) {
        String s = System.getProperty("http.keepAlive", "true");
        if ("true".equalsIgnoreCase(s)) {
          reuseStrategyCopy = DefaultClientConnectionReuseStrategy.INSTANCE;
        } else {
          reuseStrategyCopy = NoConnectionReuseStrategy.INSTANCE;
        }
      } else {
        reuseStrategyCopy = DefaultClientConnectionReuseStrategy.INSTANCE;
      }
    }
    ConnectionKeepAliveStrategy keepAliveStrategyCopy = keepAliveStrategy;
    if (keepAliveStrategyCopy == null) {
      keepAliveStrategyCopy = DefaultConnectionKeepAliveStrategy.INSTANCE;
    }
    AuthenticationStrategy targetAuthStrategyCopy = targetAuthStrategy;
    if (targetAuthStrategyCopy == null) {
      targetAuthStrategyCopy = TargetAuthenticationStrategy.INSTANCE;
    }
    AuthenticationStrategy proxyAuthStrategyCopy = proxyAuthStrategy;
    if (proxyAuthStrategyCopy == null) {
      proxyAuthStrategyCopy = ProxyAuthenticationStrategy.INSTANCE;
    }
    UserTokenHandler userTokenHandlerCopy = userTokenHandler;
    if (userTokenHandlerCopy == null) {
      if (!connectionStateDisabled) {
        userTokenHandlerCopy = DefaultUserTokenHandler.INSTANCE;
      } else {
        userTokenHandlerCopy = NoopUserTokenHandler.INSTANCE;
      }
    }
    
    String userAgentCopy = userAgent;
    if (userAgentCopy == null) {
      if (systemProperties) {
        userAgentCopy = System.getProperty("http.agent");
      }
      if (userAgentCopy == null) {
        userAgentCopy = VersionInfo.getUserAgent("Apache-HttpClient", "org.apache.http.client", getClass());
      }
    }
    

    ClientExecChain execChain = createMainExec(requestExecCopy, connManagerCopy, reuseStrategyCopy, keepAliveStrategyCopy, new ImmutableHttpProcessor(new HttpRequestInterceptor[] { new RequestTargetHost(), new RequestUserAgent(userAgentCopy) }), targetAuthStrategyCopy, proxyAuthStrategyCopy, userTokenHandlerCopy);
    








    execChain = decorateMainExec(execChain);
    
    HttpProcessor httpprocessorCopy = httpprocessor;
    if (httpprocessorCopy == null)
    {
      HttpProcessorBuilder b = HttpProcessorBuilder.create();
      if (requestFirst != null) {
        for (HttpRequestInterceptor i : requestFirst) {
          b.addFirst(i);
        }
      }
      if (responseFirst != null) {
        for (HttpResponseInterceptor i : responseFirst) {
          b.addFirst(i);
        }
      }
      b.addAll(new HttpRequestInterceptor[] { new RequestDefaultHeaders(defaultHeaders), new RequestContent(), new RequestTargetHost(), new RequestClientConnControl(), new RequestUserAgent(userAgentCopy), new RequestExpectContinue() });
      





      if (!cookieManagementDisabled) {
        b.add(new RequestAddCookies());
      }
      if (!contentCompressionDisabled) {
        if (contentDecoderMap != null) {
          List<String> encodings = new ArrayList(contentDecoderMap.keySet());
          Collections.sort(encodings);
          b.add(new RequestAcceptEncoding(encodings));
        } else {
          b.add(new RequestAcceptEncoding());
        }
      }
      if (!authCachingDisabled) {
        b.add(new RequestAuthCache());
      }
      if (!cookieManagementDisabled) {
        b.add(new ResponseProcessCookies());
      }
      if (!contentCompressionDisabled) {
        if (contentDecoderMap != null) {
          RegistryBuilder<InputStreamFactory> b2 = RegistryBuilder.create();
          for (Map.Entry<String, InputStreamFactory> entry : contentDecoderMap.entrySet()) {
            b2.register((String)entry.getKey(), entry.getValue());
          }
          b.add(new ResponseContentEncoding(b2.build()));
        } else {
          b.add(new ResponseContentEncoding());
        }
      }
      if (requestLast != null) {
        for (HttpRequestInterceptor i : requestLast) {
          b.addLast(i);
        }
      }
      if (responseLast != null) {
        for (HttpResponseInterceptor i : responseLast) {
          b.addLast(i);
        }
      }
      httpprocessorCopy = b.build();
    }
    execChain = new ProtocolExec(execChain, httpprocessorCopy);
    
    execChain = decorateProtocolExec(execChain);
    

    if (!automaticRetriesDisabled) {
      HttpRequestRetryHandler retryHandlerCopy = retryHandler;
      if (retryHandlerCopy == null) {
        retryHandlerCopy = DefaultHttpRequestRetryHandler.INSTANCE;
      }
      execChain = new RetryExec(execChain, retryHandlerCopy);
    }
    
    HttpRoutePlanner routePlannerCopy = routePlanner;
    if (routePlannerCopy == null) {
      SchemePortResolver schemePortResolverCopy = schemePortResolver;
      if (schemePortResolverCopy == null) {
        schemePortResolverCopy = DefaultSchemePortResolver.INSTANCE;
      }
      if (proxy != null) {
        routePlannerCopy = new DefaultProxyRoutePlanner(proxy, schemePortResolverCopy);
      } else if (systemProperties) {
        routePlannerCopy = new SystemDefaultRoutePlanner(schemePortResolverCopy, ProxySelector.getDefault());
      }
      else {
        routePlannerCopy = new DefaultRoutePlanner(schemePortResolverCopy);
      }
    }
    

    ServiceUnavailableRetryStrategy serviceUnavailStrategyCopy = serviceUnavailStrategy;
    if (serviceUnavailStrategyCopy != null) {
      execChain = new ServiceUnavailableRetryExec(execChain, serviceUnavailStrategyCopy);
    }
    

    if (!redirectHandlingDisabled) {
      RedirectStrategy redirectStrategyCopy = redirectStrategy;
      if (redirectStrategyCopy == null) {
        redirectStrategyCopy = DefaultRedirectStrategy.INSTANCE;
      }
      execChain = new RedirectExec(execChain, routePlannerCopy, redirectStrategyCopy);
    }
    

    if ((backoffManager != null) && (connectionBackoffStrategy != null)) {
      execChain = new BackoffStrategyExec(execChain, connectionBackoffStrategy, backoffManager);
    }
    
    Lookup<AuthSchemeProvider> authSchemeRegistryCopy = authSchemeRegistry;
    if (authSchemeRegistryCopy == null) {
      authSchemeRegistryCopy = RegistryBuilder.create().register("Basic", new BasicSchemeFactory()).register("Digest", new DigestSchemeFactory()).register("NTLM", new NTLMSchemeFactory()).register("Negotiate", new SPNegoSchemeFactory()).register("Kerberos", new KerberosSchemeFactory()).build();
    }
    





    Lookup<CookieSpecProvider> cookieSpecRegistryCopy = cookieSpecRegistry;
    if (cookieSpecRegistryCopy == null) {
      cookieSpecRegistryCopy = CookieSpecRegistries.createDefault(publicSuffixMatcherCopy);
    }
    
    CookieStore defaultCookieStore = cookieStore;
    if (defaultCookieStore == null) {
      defaultCookieStore = new BasicCookieStore();
    }
    
    CredentialsProvider defaultCredentialsProvider = credentialsProvider;
    if (defaultCredentialsProvider == null) {
      if (systemProperties) {
        defaultCredentialsProvider = new SystemDefaultCredentialsProvider();
      } else {
        defaultCredentialsProvider = new BasicCredentialsProvider();
      }
    }
    
    List<Closeable> closeablesCopy = closeables != null ? new ArrayList(closeables) : null;
    if (!connManagerShared) {
      if (closeablesCopy == null) {
        closeablesCopy = new ArrayList(1);
      }
      final HttpClientConnectionManager cm = connManagerCopy;
      
      if ((evictExpiredConnections) || (evictIdleConnections)) {
        final IdleConnectionEvictor connectionEvictor = new IdleConnectionEvictor(cm, maxIdleTime > 0L ? maxIdleTime : 10L, maxIdleTimeUnit != null ? maxIdleTimeUnit : TimeUnit.SECONDS);
        
        closeablesCopy.add(new Closeable()
        {
          public void close() throws IOException
          {
            connectionEvictor.shutdown();
          }
          
        });
        connectionEvictor.start();
      }
      closeablesCopy.add(new Closeable()
      {
        public void close() throws IOException
        {
          cm.shutdown();
        }
      });
    }
    

    return new InternalHttpClient(execChain, connManagerCopy, routePlannerCopy, cookieSpecRegistryCopy, authSchemeRegistryCopy, defaultCookieStore, defaultCredentialsProvider, defaultRequestConfig != null ? defaultRequestConfig : RequestConfig.DEFAULT, closeablesCopy);
  }
}
