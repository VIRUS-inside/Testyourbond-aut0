package com.gargoylesoftware.htmlunit;

import com.gargoylesoftware.htmlunit.httpclient.HtmlUnitCookieSpecProvider;
import com.gargoylesoftware.htmlunit.httpclient.HtmlUnitCookieStore;
import com.gargoylesoftware.htmlunit.httpclient.HtmlUnitRedirectStrategie;
import com.gargoylesoftware.htmlunit.httpclient.HtmlUnitSSLConnectionSocketFactory;
import com.gargoylesoftware.htmlunit.httpclient.SocksConnectionSocketFactory;
import com.gargoylesoftware.htmlunit.util.KeyDataPair;
import com.gargoylesoftware.htmlunit.util.UrlUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSocketFactory;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.ConnectionClosedException;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.protocol.RequestAcceptEncoding;
import org.apache.http.client.protocol.RequestAddCookies;
import org.apache.http.client.protocol.RequestAuthCache;
import org.apache.http.client.protocol.RequestClientConnControl;
import org.apache.http.client.protocol.RequestDefaultHeaders;
import org.apache.http.client.protocol.RequestExpectContinue;
import org.apache.http.client.protocol.ResponseProcessCookies;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.config.SocketConfig.Builder;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.util.PublicSuffixMatcher;
import org.apache.http.conn.util.PublicSuffixMatcherLoader;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpProcessorBuilder;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.TextUtils;

































public class HttpWebConnection
  implements WebConnection
{
  private static final Log LOG = LogFactory.getLog(HttpWebConnection.class);
  

  private static final String HACKED_COOKIE_POLICY = "mine";
  

  private final Map<Thread, HttpClientBuilder> httpClientBuilder_ = new WeakHashMap();
  
  private final WebClient webClient_;
  
  private String virtualHost_;
  
  private final CookieSpecProvider htmlUnitCookieSpecProvider_;
  private final WebClientOptions usedOptions_;
  private PoolingHttpClientConnectionManager connectionManager_;
  private final AuthCache sharedAuthCache_ = new SynchronizedAuthCache(null);
  

  private final Map<Thread, HttpClientContext> httpClientContextByThread_ = new WeakHashMap();
  



  public HttpWebConnection(WebClient webClient)
  {
    webClient_ = webClient;
    htmlUnitCookieSpecProvider_ = new HtmlUnitCookieSpecProvider(webClient.getBrowserVersion());
    usedOptions_ = new WebClientOptions();
  }
  


  public WebResponse getResponse(WebRequest request)
    throws IOException
  {
    URL url = request.getUrl();
    HttpClientBuilder builder = reconfigureHttpClientIfNeeded(getHttpClientBuilder());
    HttpContext httpContext = getHttpContext();
    
    if (connectionManager_ == null) {
      connectionManager_ = createConnectionManager(builder);
    }
    builder.setConnectionManager(connectionManager_);
    
    HttpUriRequest httpMethod = null;
    try {
      try {
        httpMethod = makeHttpMethod(request, builder);
      }
      catch (URISyntaxException e) {
        throw new IOException("Unable to create URI from URL: " + url.toExternalForm() + 
          " (reason: " + e.getMessage() + ")", e);
      }
      HttpHost hostConfiguration = getHostConfiguration(request);
      long startTime = System.currentTimeMillis();
      
      HttpResponse httpResponse = null;
      try {
        httpResponse = builder.build().execute(hostConfiguration, httpMethod, httpContext);
      }
      catch (SSLPeerUnverifiedException s)
      {
        if (webClient_.getOptions().isUseInsecureSSL()) {
          HtmlUnitSSLConnectionSocketFactory.setUseSSL3Only(httpContext, true);
          httpResponse = builder.build().execute(hostConfiguration, httpMethod, httpContext);
        }
        else {
          throw s;
        }
        

      }
      catch (Error e)
      {

        httpClientBuilder_.remove(Thread.currentThread());
        throw e;
      }
      
      DownloadedContent downloadedBody = downloadResponseBody(httpResponse);
      long endTime = System.currentTimeMillis();
      return makeWebResponse(httpResponse, request, downloadedBody, endTime - startTime);
    }
    finally {
      if (httpMethod != null) {
        onResponseGenerated(httpMethod);
      }
    }
  }
  





  protected void onResponseGenerated(HttpUriRequest httpMethod) {}
  





  private static HttpHost getHostConfiguration(WebRequest webRequest)
  {
    URL url = webRequest.getUrl();
    return new HttpHost(url.getHost(), url.getPort(), url.getProtocol());
  }
  


  private synchronized HttpContext getHttpContext()
  {
    HttpClientContext httpClientContext = (HttpClientContext)httpClientContextByThread_.get(Thread.currentThread());
    if (httpClientContext == null) {
      httpClientContext = new HttpClientContext();
      

      httpClientContext.setAttribute("http.auth.auth-cache", sharedAuthCache_);
      
      httpClientContextByThread_.put(Thread.currentThread(), httpClientContext);
    }
    return httpClientContext;
  }
  
  private void setProxy(HttpRequestBase httpRequest, WebRequest webRequest) {
    InetAddress localAddress = webClient_.getOptions().getLocalAddress();
    RequestConfig.Builder requestBuilder = createRequestConfigBuilder(getTimeout(), localAddress);
    
    if (webRequest.getProxyHost() != null) {
      HttpHost proxy = new HttpHost(webRequest.getProxyHost(), webRequest.getProxyPort());
      if (webRequest.isSocksProxy()) {
        SocksConnectionSocketFactory.setSocksProxy(getHttpContext(), proxy);
      }
      else {
        requestBuilder.setProxy(proxy);
        httpRequest.setConfig(requestBuilder.build());
      }
    }
    else {
      requestBuilder.setProxy(null);
      httpRequest.setConfig(requestBuilder.build());
    }
  }
  








  private HttpUriRequest makeHttpMethod(WebRequest webRequest, HttpClientBuilder httpClientBuilder)
    throws URISyntaxException
  {
    Charset charset = webRequest.getCharset();
    HttpContext httpContext = getHttpContext();
    



    URL url = UrlUtils.encodeUrl(webRequest.getUrl(), false, charset);
    
    URI uri = UrlUtils.toURI(url, escapeQuery(url.getQuery()));
    if (getVirtualHost() != null) {
      uri = URI.create(getVirtualHost());
    }
    HttpRequestBase httpMethod = buildHttpMethod(webRequest.getHttpMethod(), uri);
    setProxy(httpMethod, webRequest);
    if (!(httpMethod instanceof HttpEntityEnclosingRequest))
    {
      if (!webRequest.getRequestParameters().isEmpty()) {
        List<com.gargoylesoftware.htmlunit.util.NameValuePair> pairs = webRequest.getRequestParameters();
        org.apache.http.NameValuePair[] httpClientPairs = com.gargoylesoftware.htmlunit.util.NameValuePair.toHttpClient(pairs);
        String query = URLEncodedUtils.format(Arrays.asList(httpClientPairs), charset);
        uri = UrlUtils.toURI(url, query);
        httpMethod.setURI(uri);
      }
    }
    else {
      HttpEntityEnclosingRequest method = (HttpEntityEnclosingRequest)httpMethod;
      String query;
      if ((webRequest.getEncodingType() == FormEncodingType.URL_ENCODED) && ((method instanceof HttpPost))) {
        HttpPost postMethod = (HttpPost)method;
        if (webRequest.getRequestBody() == null) {
          List<com.gargoylesoftware.htmlunit.util.NameValuePair> pairs = webRequest.getRequestParameters();
          org.apache.http.NameValuePair[] httpClientPairs = com.gargoylesoftware.htmlunit.util.NameValuePair.toHttpClient(pairs);
          query = URLEncodedUtils.format(Arrays.asList(httpClientPairs), charset);
          StringEntity urlEncodedEntity = new StringEntity(query, charset);
          urlEncodedEntity.setContentType("application/x-www-form-urlencoded");
          postMethod.setEntity(urlEncodedEntity);
        }
        else {
          String body = StringUtils.defaultString(webRequest.getRequestBody());
          StringEntity urlEncodedEntity = new StringEntity(body, charset);
          urlEncodedEntity.setContentType("application/x-www-form-urlencoded");
          postMethod.setEntity(urlEncodedEntity);
        }
      }
      else if (FormEncodingType.MULTIPART == webRequest.getEncodingType()) {
        Charset c = getCharset(charset, webRequest.getRequestParameters());
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setLaxMode();
        builder.setCharset(c);
        
        for (com.gargoylesoftware.htmlunit.util.NameValuePair pair : webRequest.getRequestParameters()) {
          if ((pair instanceof KeyDataPair)) {
            buildFilePart((KeyDataPair)pair, builder);
          }
          else {
            builder.addTextBody(pair.getName(), pair.getValue(), 
              ContentType.create("text/plain", charset));
          }
        }
        method.setEntity(builder.build());
      }
      else {
        String body = webRequest.getRequestBody();
        if (body != null) {
          method.setEntity(new StringEntity(body, charset));
        }
      }
    }
    
    configureHttpProcessorBuilder(httpClientBuilder, webRequest);
    


    CredentialsProvider credentialsProvider = webClient_.getCredentialsProvider();
    

    Credentials requestUrlCredentials = webRequest.getUrlCredentials();
    if ((requestUrlCredentials != null) && 
      (webClient_.getBrowserVersion().hasFeature(BrowserVersionFeatures.URL_AUTH_CREDENTIALS))) {
      URL requestUrl = webRequest.getUrl();
      AuthScope authScope = new AuthScope(requestUrl.getHost(), requestUrl.getPort());
      
      credentialsProvider.setCredentials(authScope, requestUrlCredentials);
    }
    

    Credentials requestCredentials = webRequest.getCredentials();
    if (requestCredentials != null) {
      URL requestUrl = webRequest.getUrl();
      AuthScope authScope = new AuthScope(requestUrl.getHost(), requestUrl.getPort());
      
      credentialsProvider.setCredentials(authScope, requestCredentials);
    }
    httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
    httpContext.removeAttribute("http.auth.credentials-provider");
    httpContext.removeAttribute("http.auth.target-scope");
    
    return httpMethod;
  }
  
  private static String escapeQuery(String query) {
    if (query == null) {
      return null;
    }
    return query.replace("%%", "%25%25");
  }
  
  private static Charset getCharset(Charset charset, List<com.gargoylesoftware.htmlunit.util.NameValuePair> pairs) {
    for (com.gargoylesoftware.htmlunit.util.NameValuePair pair : pairs) {
      if ((pair instanceof KeyDataPair)) {
        KeyDataPair pairWithFile = (KeyDataPair)pair;
        if ((pairWithFile.getData() == null) && (pairWithFile.getFile() != null)) {
          String fileName = pairWithFile.getFile().getName();
          for (int i = 0; i < fileName.length(); i++) {
            if (fileName.codePointAt(i) > 127) {
              return charset;
            }
          }
        }
      }
    }
    return null;
  }
  
  void buildFilePart(KeyDataPair pairWithFile, MultipartEntityBuilder builder) {
    String mimeType = pairWithFile.getMimeType();
    if (mimeType == null) {
      mimeType = "application/octet-stream";
    }
    
    ContentType contentType = ContentType.create(mimeType);
    File file = pairWithFile.getFile();
    
    if (pairWithFile.getData() != null) { String filename;
      String filename;
      if (file == null) {
        filename = pairWithFile.getValue();
      } else { String filename;
        if (pairWithFile.getFileName() != null) {
          filename = pairWithFile.getFileName();
        } else { String filename;
          if (webClient_.getBrowserVersion().hasFeature(BrowserVersionFeatures.HEADER_CONTENT_DISPOSITION_ABSOLUTE_PATH)) {
            filename = file.getAbsolutePath();
          }
          else
            filename = file.getName();
        }
      }
      builder.addBinaryBody(pairWithFile.getName(), new ByteArrayInputStream(pairWithFile.getData()), 
        contentType, filename);
      return;
    }
    
    if (file == null) {
      builder.addPart(pairWithFile.getName(), 
      
        new InputStreamBody(new ByteArrayInputStream(new byte[0]), contentType, pairWithFile.getValue())
        {
          public long getContentLength() {
            return 0L;
          }
        }); return;
    }
    
    String filename;
    String filename;
    if (pairWithFile.getFile() == null) {
      filename = pairWithFile.getValue();
    } else { String filename;
      if (pairWithFile.getFileName() != null) {
        filename = pairWithFile.getFileName();
      } else { String filename;
        if (webClient_.getBrowserVersion().hasFeature(BrowserVersionFeatures.HEADER_CONTENT_DISPOSITION_ABSOLUTE_PATH)) {
          filename = pairWithFile.getFile().getAbsolutePath();
        }
        else
          filename = pairWithFile.getFile().getName();
      } }
    builder.addBinaryBody(pairWithFile.getName(), pairWithFile.getFile(), contentType, filename);
  }
  
  private static HttpRequestBase buildHttpMethod(HttpMethod submitMethod, URI uri) { HttpRequestBase method;
    HttpRequestBase method;
    HttpRequestBase method;
    HttpRequestBase method;
    HttpRequestBase method;
    HttpRequestBase method;
    HttpRequestBase method;
    HttpRequestBase method;
    switch (submitMethod) {
    case GET: 
      method = new HttpGet(uri);
      break;
    
    case OPTIONS: 
      method = new HttpPost(uri);
      break;
    
    case PATCH: 
      method = new HttpPut(uri);
      break;
    
    case POST: 
      method = new HttpDelete(uri);
      break;
    
    case DELETE: 
      method = new HttpOptions(uri);
      break;
    
    case HEAD: 
      method = new HttpHead(uri);
      break;
    
    case PUT: 
      method = new HttpTrace(uri);
      break;
    
    case TRACE: 
      method = new HttpPatch(uri);
      break;
    
    default: 
      throw new IllegalStateException("Submit method not yet supported: " + submitMethod); }
    HttpRequestBase method;
    return method;
  }
  




  protected HttpClientBuilder getHttpClientBuilder()
  {
    HttpClientBuilder builder = (HttpClientBuilder)httpClientBuilder_.get(Thread.currentThread());
    if (builder == null) {
      builder = createHttpClient();
      


      RegistryBuilder<CookieSpecProvider> registeryBuilder = 
        RegistryBuilder.create()
        .register("mine", htmlUnitCookieSpecProvider_);
      builder.setDefaultCookieSpecRegistry(registeryBuilder.build());
      
      builder.setDefaultCookieStore(new HtmlUnitCookieStore(webClient_.getCookieManager()));
      builder.setUserAgent(webClient_.getBrowserVersion().getUserAgent());
      httpClientBuilder_.put(Thread.currentThread(), builder);
    }
    
    return builder;
  }
  





  protected int getTimeout()
  {
    return webClient_.getOptions().getTimeout();
  }
  







  protected HttpClientBuilder createHttpClient()
  {
    HttpClientBuilder builder = HttpClientBuilder.create();
    builder.setRedirectStrategy(new HtmlUnitRedirectStrategie());
    configureTimeout(builder, getTimeout());
    configureHttpsScheme(builder);
    builder.setMaxConnPerRoute(6);
    return builder;
  }
  
  private void configureTimeout(HttpClientBuilder builder, int timeout) {
    InetAddress localAddress = webClient_.getOptions().getLocalAddress();
    RequestConfig.Builder requestBuilder = createRequestConfigBuilder(timeout, localAddress);
    builder.setDefaultRequestConfig(requestBuilder.build());
    
    builder.setDefaultSocketConfig(createSocketConfigBuilder(timeout).build());
    
    getHttpContext().removeAttribute("http.request-config");
    usedOptions_.setTimeout(timeout);
  }
  
  private static RequestConfig.Builder createRequestConfigBuilder(int timeout, InetAddress localAddress) {
    RequestConfig.Builder requestBuilder = RequestConfig.custom()
      .setCookieSpec("mine")
      .setRedirectsEnabled(false)
      .setLocalAddress(localAddress)
      

      .setConnectTimeout(timeout)
      .setConnectionRequestTimeout(timeout)
      .setSocketTimeout(timeout);
    return requestBuilder;
  }
  
  private static SocketConfig.Builder createSocketConfigBuilder(int timeout) {
    SocketConfig.Builder socketBuilder = SocketConfig.custom()
    
      .setSoTimeout(timeout);
    return socketBuilder;
  }
  



  private HttpClientBuilder reconfigureHttpClientIfNeeded(HttpClientBuilder httpClientBuilder)
  {
    WebClientOptions options = webClient_.getOptions();
    

    if ((options.isUseInsecureSSL() != usedOptions_.isUseInsecureSSL()) || 
      (options.getSSLClientCertificateStore() != usedOptions_.getSSLClientCertificateStore()) || 
      (options.getSSLTrustStore() != usedOptions_.getSSLTrustStore()) || 
      (options.getSSLClientCipherSuites() != usedOptions_.getSSLClientCipherSuites()) || 
      (options.getSSLClientProtocols() != usedOptions_.getSSLClientProtocols()) || 
      (options.getProxyConfig() != usedOptions_.getProxyConfig())) {
      configureHttpsScheme(httpClientBuilder);
      if (connectionManager_ != null) {
        connectionManager_.shutdown();
        connectionManager_ = null;
      }
    }
    
    int timeout = getTimeout();
    if (timeout != usedOptions_.getTimeout()) {
      configureTimeout(httpClientBuilder, timeout);
    }
    return httpClientBuilder;
  }
  
  private void configureHttpsScheme(HttpClientBuilder builder) {
    WebClientOptions options = webClient_.getOptions();
    
    SSLConnectionSocketFactory socketFactory = 
      HtmlUnitSSLConnectionSocketFactory.buildSSLSocketFactory(options);
    
    builder.setSSLSocketFactory(socketFactory);
    
    usedOptions_.setUseInsecureSSL(options.isUseInsecureSSL());
    usedOptions_.setSSLClientCertificateStore(options.getSSLClientCertificateStore());
    usedOptions_.setSSLTrustStore(options.getSSLTrustStore());
    usedOptions_.setSSLClientCipherSuites(options.getSSLClientCipherSuites());
    usedOptions_.setSSLClientProtocols(options.getSSLClientProtocols());
    usedOptions_.setProxyConfig(options.getProxyConfig());
  }
  
  private void configureHttpProcessorBuilder(HttpClientBuilder builder, WebRequest webRequest) {
    HttpProcessorBuilder b = HttpProcessorBuilder.create();
    for (HttpRequestInterceptor i : getHttpRequestInterceptors(webRequest)) {
      b.add(i);
    }
    


    b.addAll(new HttpRequestInterceptor[] { new RequestDefaultHeaders(null), 
      new RequestContent(), 
      new RequestTargetHost(), 
      new RequestExpectContinue() });
    b.add(new RequestAcceptEncoding());
    b.add(new RequestAuthCache());
    b.add(new ResponseProcessCookies());
    builder.setHttpProcessor(b.build());
  }
  



  public void setVirtualHost(String virtualHost)
  {
    virtualHost_ = virtualHost;
  }
  



  public String getVirtualHost()
  {
    return virtualHost_;
  }
  




  private WebResponse makeWebResponse(HttpResponse httpResponse, WebRequest request, DownloadedContent responseBody, long loadTime)
  {
    String statusMessage = httpResponse.getStatusLine().getReasonPhrase();
    if (statusMessage == null) {
      statusMessage = "Unknown status message";
    }
    int statusCode = httpResponse.getStatusLine().getStatusCode();
    List<com.gargoylesoftware.htmlunit.util.NameValuePair> headers = new ArrayList();
    for (Header header : httpResponse.getAllHeaders()) {
      headers.add(new com.gargoylesoftware.htmlunit.util.NameValuePair(header.getName(), header.getValue()));
    }
    WebResponseData responseData = new WebResponseData(responseBody, statusCode, statusMessage, headers);
    return newWebResponseInstance(responseData, loadTime, request);
  }
  




  protected DownloadedContent downloadResponseBody(HttpResponse httpResponse)
    throws IOException
  {
    HttpEntity httpEntity = httpResponse.getEntity();
    if (httpEntity == null) {
      return new DownloadedContent.InMemory(null);
    }
    
    return downloadContent(httpEntity.getContent(), webClient_.getOptions().getMaxInMemory());
  }
  





  public static DownloadedContent downloadContent(InputStream is, int maxInMemory)
    throws IOException
  {
    if (is == null) {
      return new DownloadedContent.InMemory(null);
    }
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    
    byte[] buffer = new byte['Ѐ'];
    try { label148:
      int nbRead;
      while ((nbRead = is.read(buffer)) != -1) { int nbRead;
        bos.write(buffer, 0, nbRead);
        if (bos.size() > maxInMemory)
        {
          File file = File.createTempFile("htmlunit", ".tmp");
          file.deleteOnExit();
          Object localObject1 = null;Object localObject4 = null; Object localObject3; try { fos = new FileOutputStream(file);
          } finally {
            FileOutputStream fos;
            localObject3 = localThrowable; break label148; if (localObject3 != localThrowable) localObject3.addSuppressed(localThrowable); }
          return new DownloadedContent.OnFile(file, true);
        }
      }
    }
    catch (ConnectionClosedException e) {
      LOG.warn("Connection was closed while reading from stream.", e);
      return new DownloadedContent.InMemory(bos.toByteArray());
    }
    catch (EOFException e) {
      Object localObject6;
      LOG.warn("EOFException while reading from stream.", e);
      return new DownloadedContent.InMemory(bos.toByteArray());
    }
    finally {
      IOUtils.closeQuietly(is); } int nbRead; IOUtils.closeQuietly(is);
    

    return new DownloadedContent.InMemory(bos.toByteArray());
  }
  










  protected WebResponse newWebResponseInstance(WebResponseData responseData, long loadTime, WebRequest request)
  {
    return new WebResponse(responseData, request, loadTime);
  }
  
  private List<HttpRequestInterceptor> getHttpRequestInterceptors(WebRequest webRequest) {
    List<HttpRequestInterceptor> list = new ArrayList();
    Map<String, String> requestHeaders = webRequest.getAdditionalHeaders();
    URL url = webRequest.getUrl();
    StringBuilder host = new StringBuilder(url.getHost());
    
    int port = url.getPort();
    if ((port > 0) && (port != url.getDefaultPort())) {
      host.append(':');
      host.append(Integer.toString(port));
    }
    
    String userAgent = webClient_.getBrowserVersion().getUserAgent();
    String[] headerNames = webClient_.getBrowserVersion().getHeaderNamesOrdered();
    if (headerNames != null) {
      for (String header : headerNames) {
        if ("Host".equals(header)) {
          list.add(new HostHeaderHttpRequestInterceptor(host.toString()));
        }
        else if ("User-Agent".equals(header)) {
          list.add(new UserAgentHeaderHttpRequestInterceptor(userAgent));
        }
        else if (("Accept".equals(header)) && (requestHeaders.get(header) != null)) {
          list.add(new AcceptHeaderHttpRequestInterceptor((String)requestHeaders.get(header)));
        }
        else if (("Accept-Language".equals(header)) && (requestHeaders.get(header) != null)) {
          list.add(new AcceptLanguageHeaderHttpRequestInterceptor((String)requestHeaders.get(header)));
        }
        else if (("Accept-Encoding".equals(header)) && (requestHeaders.get(header) != null)) {
          list.add(new AcceptEncodingHeaderHttpRequestInterceptor((String)requestHeaders.get(header)));
        }
        else if (("Referer".equals(header)) && (requestHeaders.get(header) != null)) {
          list.add(new RefererHeaderHttpRequestInterceptor((String)requestHeaders.get(header)));
        }
        else if ("Connection".equals(header)) {
          list.add(new RequestClientConnControl());
        }
        else if ("Cookie".equals(header)) {
          list.add(new RequestAddCookies());
        }
        else if (("DNT".equals(header)) && (webClient_.getOptions().isDoNotTrackEnabled())) {
          list.add(new DntHeaderHttpRequestInterceptor("1"));
        }
      }
    }
    else {
      list.add(new UserAgentHeaderHttpRequestInterceptor(userAgent));
      list.add(new RequestAddCookies());
      list.add(new RequestClientConnControl());
    }
    


    if (webClient_.getOptions().isDoNotTrackEnabled()) {
      list.add(new DntHeaderHttpRequestInterceptor("1"));
    }
    
    synchronized (requestHeaders) {
      list.add(new MultiHttpRequestInterceptor(new HashMap(requestHeaders)));
    }
    return list;
  }
  
  private static final class HostHeaderHttpRequestInterceptor implements HttpRequestInterceptor
  {
    private String value_;
    
    HostHeaderHttpRequestInterceptor(String value) {
      value_ = value;
    }
    
    public void process(HttpRequest request, HttpContext context) throws HttpException, IOException
    {
      request.setHeader("Host", value_);
    }
  }
  
  private static final class UserAgentHeaderHttpRequestInterceptor implements HttpRequestInterceptor {
    private String value_;
    
    UserAgentHeaderHttpRequestInterceptor(String value) {
      value_ = value;
    }
    
    public void process(HttpRequest request, HttpContext context) throws HttpException, IOException
    {
      request.setHeader("User-Agent", value_);
    }
  }
  
  private static final class AcceptHeaderHttpRequestInterceptor implements HttpRequestInterceptor {
    private String value_;
    
    AcceptHeaderHttpRequestInterceptor(String value) {
      value_ = value;
    }
    
    public void process(HttpRequest request, HttpContext context) throws HttpException, IOException
    {
      request.setHeader("Accept", value_);
    }
  }
  
  private static final class AcceptLanguageHeaderHttpRequestInterceptor implements HttpRequestInterceptor {
    private String value_;
    
    AcceptLanguageHeaderHttpRequestInterceptor(String value) {
      value_ = value;
    }
    
    public void process(HttpRequest request, HttpContext context) throws HttpException, IOException
    {
      request.setHeader("Accept-Language", value_);
    }
  }
  
  private static final class AcceptEncodingHeaderHttpRequestInterceptor implements HttpRequestInterceptor {
    private String value_;
    
    AcceptEncodingHeaderHttpRequestInterceptor(String value) {
      value_ = value;
    }
    
    public void process(HttpRequest request, HttpContext context) throws HttpException, IOException
    {
      request.setHeader("Accept-Encoding", value_);
    }
  }
  
  private static final class RefererHeaderHttpRequestInterceptor implements HttpRequestInterceptor {
    private String value_;
    
    RefererHeaderHttpRequestInterceptor(String value) {
      value_ = value;
    }
    
    public void process(HttpRequest request, HttpContext context) throws HttpException, IOException
    {
      request.setHeader("Referer", value_);
    }
  }
  
  private static final class DntHeaderHttpRequestInterceptor implements HttpRequestInterceptor {
    private String value_;
    
    DntHeaderHttpRequestInterceptor(String value) {
      value_ = value;
    }
    
    public void process(HttpRequest request, HttpContext context) throws HttpException, IOException
    {
      request.setHeader("DNT", value_);
    }
  }
  
  private static class MultiHttpRequestInterceptor implements HttpRequestInterceptor {
    private final Map<String, String> map_;
    
    MultiHttpRequestInterceptor(Map<String, String> map) {
      map_ = map;
    }
    
    public void process(HttpRequest request, HttpContext context)
      throws HttpException, IOException
    {
      for (String key : map_.keySet()) {
        request.setHeader(key, (String)map_.get(key));
      }
    }
  }
  
  private static final class SynchronizedAuthCache
    extends BasicAuthCache
  {
    private SynchronizedAuthCache() {}
    
    public synchronized void put(HttpHost host, AuthScheme authScheme)
    {
      super.put(host, authScheme);
    }
    
    public synchronized AuthScheme get(HttpHost host)
    {
      return super.get(host);
    }
    
    public synchronized void remove(HttpHost host)
    {
      super.remove(host);
    }
    
    public synchronized void clear()
    {
      super.clear();
    }
    
    public synchronized String toString()
    {
      return super.toString();
    }
  }
  



  public void close()
  {
    Thread current = Thread.currentThread();
    if (httpClientBuilder_.get(current) != null) {
      httpClientBuilder_.remove(current);
    }
    if (connectionManager_ != null) {
      connectionManager_.shutdown();
      connectionManager_ = null;
    }
  }
  


  private static PoolingHttpClientConnectionManager createConnectionManager(HttpClientBuilder builder)
  {
    try
    {
      PublicSuffixMatcher publicSuffixMatcher = (PublicSuffixMatcher)getField(builder, "publicSuffixMatcher");
      if (publicSuffixMatcher == null) {
        publicSuffixMatcher = PublicSuffixMatcherLoader.getDefault();
      }
      
      HttpRequestExecutor requestExec = (HttpRequestExecutor)getField(builder, "requestExec");
      if (requestExec == null) {
        requestExec = new HttpRequestExecutor();
      }
      
      LayeredConnectionSocketFactory sslSocketFactory = (LayeredConnectionSocketFactory)getField(builder, "sslSocketFactory");
      SocketConfig defaultSocketConfig = (SocketConfig)getField(builder, "defaultSocketConfig");
      ConnectionConfig defaultConnectionConfig = (ConnectionConfig)getField(builder, "defaultConnectionConfig");
      boolean systemProperties = ((Boolean)getField(builder, "systemProperties")).booleanValue();
      int maxConnTotal = ((Integer)getField(builder, "maxConnTotal")).intValue();
      int maxConnPerRoute = ((Integer)getField(builder, "maxConnPerRoute")).intValue();
      HostnameVerifier hostnameVerifier = (HostnameVerifier)getField(builder, "hostnameVerifier");
      SSLContext sslcontext = (SSLContext)getField(builder, "sslContext");
      DnsResolver dnsResolver = (DnsResolver)getField(builder, "dnsResolver");
      long connTimeToLive = ((Long)getField(builder, "connTimeToLive")).longValue();
      TimeUnit connTimeToLiveTimeUnit = (TimeUnit)getField(builder, "connTimeToLiveTimeUnit");
      
      if (sslSocketFactory == null) {
        String[] supportedProtocols = systemProperties ? 
          split(System.getProperty("https.protocols")) : null;
        String[] supportedCipherSuites = systemProperties ? 
          split(System.getProperty("https.cipherSuites")) : null;
        if (hostnameVerifier == null) {
          hostnameVerifier = new DefaultHostnameVerifier(publicSuffixMatcher);
        }
        if (sslcontext != null) {
          sslSocketFactory = new SSLConnectionSocketFactory(
            sslcontext, supportedProtocols, supportedCipherSuites, hostnameVerifier);

        }
        else if (systemProperties) {
          sslSocketFactory = new SSLConnectionSocketFactory(
            (SSLSocketFactory)SSLSocketFactory.getDefault(), 
            supportedProtocols, supportedCipherSuites, hostnameVerifier);
        }
        else {
          sslSocketFactory = new SSLConnectionSocketFactory(
            SSLContexts.createDefault(), 
            hostnameVerifier);
        }
      }
      

      PoolingHttpClientConnectionManager poolingmgr = new PoolingHttpClientConnectionManager(
        RegistryBuilder.create()
        .register("http", new SocksConnectionSocketFactory())
        .register("https", sslSocketFactory)
        .build(), 
        null, 
        null, 
        dnsResolver, 
        connTimeToLive, 
        connTimeToLiveTimeUnit != null ? connTimeToLiveTimeUnit : TimeUnit.MILLISECONDS);
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
      return poolingmgr;
    }
    catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
  
  private static String[] split(String s) {
    if (TextUtils.isBlank(s)) {
      return null;
    }
    return s.split(" *, *");
  }
  
  private static <T> T getField(Object target, String fieldName) throws IllegalAccessException
  {
    return FieldUtils.readDeclaredField(target, fieldName, true);
  }
}
