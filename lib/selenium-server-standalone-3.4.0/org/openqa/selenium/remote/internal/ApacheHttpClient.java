package org.openqa.selenium.remote.internal;

import com.google.common.base.Preconditions;
import java.io.IOException;
import java.net.BindException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NoHttpResponseException;
import org.apache.http.StatusLine;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.http.HttpClient.Factory;
import org.openqa.selenium.remote.http.HttpMethod;
import org.openqa.selenium.remote.http.HttpRequest;


















public class ApacheHttpClient
  implements org.openqa.selenium.remote.http.HttpClient
{
  private static final int MAX_REDIRECTS = 10;
  private final URL url;
  private final HttpHost targetHost;
  private final org.apache.http.client.HttpClient client;
  
  public ApacheHttpClient(org.apache.http.client.HttpClient client, URL url)
  {
    this.client = ((org.apache.http.client.HttpClient)Preconditions.checkNotNull(client, "null HttpClient"));
    this.url = ((URL)Preconditions.checkNotNull(url, "null URL"));
    


    String host = url.getHost().replace(".localdomain", "");
    targetHost = new HttpHost(host, url.getPort(), url.getProtocol());
  }
  
  public org.openqa.selenium.remote.http.HttpResponse execute(HttpRequest request, boolean followRedirects) throws IOException
  {
    HttpContext context = createContext();
    
    String requestUrl = url.toExternalForm().replaceAll("/$", "") + request.getUri();
    HttpUriRequest httpMethod = createHttpUriRequest(request.getMethod(), requestUrl);
    for (Iterator localIterator1 = request.getHeaderNames().iterator(); localIterator1.hasNext();) { name = (String)localIterator1.next();
      
      if (!"Content-Length".equalsIgnoreCase(name)) {
        for (String value : request.getHeaders(name)) {
          httpMethod.addHeader(name, value);
        }
      }
    }
    String name;
    if ((httpMethod instanceof HttpPost)) {
      ((HttpPost)httpMethod).setEntity(new ByteArrayEntity(request.getContent()));
    }
    
    org.apache.http.HttpResponse response = fallBackExecute(context, httpMethod);
    if (followRedirects) {
      response = followRedirects(client, context, response, 0);
    }
    return createResponse(response, context);
  }
  
  private org.openqa.selenium.remote.http.HttpResponse createResponse(org.apache.http.HttpResponse response, HttpContext context) throws IOException
  {
    org.openqa.selenium.remote.http.HttpResponse internalResponse = new org.openqa.selenium.remote.http.HttpResponse();
    
    internalResponse.setStatus(response.getStatusLine().getStatusCode());
    for (Header header : response.getAllHeaders()) {
      internalResponse.addHeader(header.getName(), header.getValue());
    }
    
    HttpEntity entity = response.getEntity();
    if (entity != null) {
      try {
        internalResponse.setContent(EntityUtils.toByteArray(entity));
      } finally {
        EntityUtils.consume(entity);
      }
    }
    
    Object host = context.getAttribute("http.target_host");
    if ((host instanceof HttpHost)) {
      internalResponse.setTargetHost(((HttpHost)host).toURI());
    }
    
    return internalResponse;
  }
  
  protected HttpContext createContext() {
    return new BasicHttpContext();
  }
  
  private static HttpUriRequest createHttpUriRequest(HttpMethod method, String url) {
    switch (1.$SwitchMap$org$openqa$selenium$remote$http$HttpMethod[method.ordinal()]) {
    case 1: 
      return new HttpDelete(url);
    case 2: 
      return new HttpGet(url);
    case 3: 
      return new HttpPost(url);
    }
    throw new AssertionError("Unsupported method: " + method);
  }
  
  private org.apache.http.HttpResponse fallBackExecute(HttpContext context, HttpUriRequest httpMethod) throws IOException
  {
    try {
      return client.execute(targetHost, httpMethod, context);
    }
    catch (BindException e)
    {
      try {
        Thread.sleep(2000L);
      } catch (InterruptedException ie) {
        throw new RuntimeException(ie);
      }
    }
    catch (NoHttpResponseException e)
    {
      try {
        Thread.sleep(2000L);
      } catch (InterruptedException ie) {
        throw new RuntimeException(ie);
      }
    }
    return client.execute(targetHost, httpMethod, context);
  }
  


  private org.apache.http.HttpResponse followRedirects(org.apache.http.client.HttpClient client, HttpContext context, org.apache.http.HttpResponse response, int redirectCount)
  {
    if (!isRedirect(response)) {
      return response;
    }
    
    try
    {
      HttpEntity httpEntity = response.getEntity();
      if (httpEntity != null) {
        EntityUtils.consume(httpEntity);
      }
    } catch (IOException e) {
      throw new WebDriverException(e);
    }
    
    if (redirectCount > 10) {
      throw new WebDriverException("Maximum number of redirects exceeded. Aborting");
    }
    
    String location = response.getFirstHeader("location").getValue();
    try
    {
      URI uri = buildUri(context, location);
      
      HttpGet get = new HttpGet(uri);
      get.setHeader("Accept", "application/json; charset=utf-8");
      org.apache.http.HttpResponse newResponse = client.execute(targetHost, get, context);
      return followRedirects(client, context, newResponse, redirectCount + 1);
    } catch (URISyntaxException e) {
      throw new WebDriverException(e);
    } catch (ClientProtocolException e) {
      throw new WebDriverException(e);
    } catch (IOException e) {
      throw new WebDriverException(e);
    }
  }
  
  private URI buildUri(HttpContext context, String location) throws URISyntaxException
  {
    URI uri = new URI(location);
    if (!uri.isAbsolute()) {
      HttpHost host = (HttpHost)context.getAttribute("http.target_host");
      uri = new URI(host.toURI() + location);
    }
    return uri;
  }
  
  private boolean isRedirect(org.apache.http.HttpResponse response) {
    int code = response.getStatusLine().getStatusCode();
    
    return ((code == 301) || (code == 302) || (code == 303) || (code == 307)) && 
      (response.containsHeader("location"));
  }
  
  public static class Factory implements HttpClient.Factory
  {
    private static HttpClientFactory defaultClientFactory;
    private final HttpClientFactory clientFactory;
    
    public Factory()
    {
      this(getDefaultHttpClientFactory());
    }
    
    public Factory(HttpClientFactory clientFactory) {
      this.clientFactory = ((HttpClientFactory)Preconditions.checkNotNull(clientFactory, "null HttpClientFactory"));
    }
    
    public org.openqa.selenium.remote.http.HttpClient createClient(URL url)
    {
      Preconditions.checkNotNull(url, "null URL");
      org.apache.http.client.HttpClient client;
      org.apache.http.client.HttpClient client; if (url.getUserInfo() != null)
      {
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(url.getUserInfo());
        client = clientFactory.createHttpClient(credentials);
      } else {
        client = clientFactory.getHttpClient();
      }
      return new ApacheHttpClient(client, url);
    }
    
    private static synchronized HttpClientFactory getDefaultHttpClientFactory() {
      if (defaultClientFactory == null) {
        defaultClientFactory = new HttpClientFactory();
      }
      return defaultClientFactory;
    }
  }
  
  public void close() throws IOException
  {
    client.getConnectionManager().closeIdleConnections(0L, TimeUnit.SECONDS);
  }
}
