package org.openqa.grid.internal;

import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;
import com.google.common.net.MediaType;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.util.EntityUtils;
import org.openqa.grid.common.SeleniumProtocol;
import org.openqa.grid.common.exception.ClientGoneException;
import org.openqa.grid.common.exception.GridException;
import org.openqa.grid.internal.listeners.CommandListener;
import org.openqa.grid.internal.utils.configuration.GridHubConfiguration;
import org.openqa.grid.web.Hub;
import org.openqa.grid.web.servlet.handler.LegacySeleniumRequest;
import org.openqa.grid.web.servlet.handler.RequestType;
import org.openqa.grid.web.servlet.handler.SeleniumBasedRequest;
import org.openqa.grid.web.servlet.handler.SeleniumBasedResponse;
import org.openqa.grid.web.servlet.handler.WebDriverRequest;
import org.openqa.selenium.remote.internal.HttpClientFactory;
























public class TestSession
{
  private static final Logger log = Logger.getLogger(TestSession.class.getName());
  
  static final int MAX_IDLE_TIME_BEFORE_CONSIDERED_ORPHANED = 5000;
  private final String internalKey;
  private final TestSlot slot;
  private volatile ExternalSessionKey externalKey = null;
  private volatile long sessionCreatedAt;
  private volatile long lastActivity;
  private final Map<String, Object> requestedCapabilities;
  private Map<String, Object> objects = Collections.synchronizedMap(new HashMap());
  private volatile boolean ignoreTimeout = false;
  private final TimeSource timeSource;
  private volatile boolean forwardingRequest;
  private final int MAX_NETWORK_LATENCY = 1000;
  
  public String getInternalKey() {
    return internalKey;
  }
  



  public TestSession(TestSlot slot, Map<String, Object> requestedCapabilities, TimeSource timeSource)
  {
    internalKey = UUID.randomUUID().toString();
    this.slot = slot;
    this.requestedCapabilities = requestedCapabilities;
    this.timeSource = timeSource;
    lastActivity = this.timeSource.currentTimeInMillis();
  }
  



  public Map<String, Object> getRequestedCapabilities()
  {
    return requestedCapabilities;
  }
  





  public ExternalSessionKey getExternalKey()
  {
    return externalKey;
  }
  



  public void setExternalKey(ExternalSessionKey externalKey)
  {
    this.externalKey = externalKey;
    sessionCreatedAt = lastActivity;
  }
  






  public long getInactivityTime()
  {
    if (ignoreTimeout) {
      return 0L;
    }
    return timeSource.currentTimeInMillis() - lastActivity;
  }
  
  public boolean isOrphaned() {
    long elapsedSinceCreation = timeSource.currentTimeInMillis() - sessionCreatedAt;
    


    return (slot.getProtocol().equals(SeleniumProtocol.Selenium)) && (elapsedSinceCreation > 5000L) && (sessionCreatedAt == lastActivity);
  }
  




  public TestSlot getSlot()
  {
    return slot;
  }
  
  public int hashCode()
  {
    int prime = 31;
    int result = 1;
    result = 31 * result + (internalKey == null ? 0 : internalKey.hashCode());
    return result;
  }
  
  public boolean equals(Object obj)
  {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    TestSession other = (TestSession)obj;
    return internalKey.equals(internalKey);
  }
  
  public String toString()
  {
    return internalKey + " (int. key, remote not contacted yet.)";
  }
  

  private HttpClient getClient()
  {
    Registry reg = slot.getProxy().getRegistry();
    long browserTimeout = TimeUnit.SECONDS.toMillis(getConfigurationbrowserTimeout.intValue());
    if (browserTimeout > 0L) {
      long selenium_server_cleanup_cycle = browserTimeout / 10L;
      browserTimeout += selenium_server_cleanup_cycle + 1000L;
      browserTimeout *= 2L;
    }
    return slot.getProxy().getHttpClientFactory().getGridHttpClient((int)browserTimeout, (int)browserTimeout);
  }
  



  public String forward(SeleniumBasedRequest request, HttpServletResponse response, boolean newSessionRequest)
    throws IOException
  {
    String res = null;
    
    String currentThreadName = Thread.currentThread().getName();
    setThreadDisplayName();
    forwardingRequest = true;
    try
    {
      if ((slot.getProxy() instanceof CommandListener)) {
        ((CommandListener)slot.getProxy()).beforeCommand(this, request, response);
      }
      
      lastActivity = timeSource.currentTimeInMillis();
      
      HttpRequest proxyRequest = prepareProxyRequest(request);
      
      HttpResponse proxyResponse = sendRequestToNode(proxyRequest);
      lastActivity = timeSource.currentTimeInMillis();
      HttpEntity responseBody = proxyResponse.getEntity();
      int statusCode;
      try { statusCode = proxyResponse.getStatusLine().getStatusCode();
        response.setStatus(statusCode);
        processResponseHeaders(request, response, slot.getRemoteURL(), proxyResponse);
        
        byte[] consumedNewWebDriverSessionBody = null;
        if ((statusCode != 500) && (statusCode != 404) && (statusCode != 400) && (statusCode != 401))
        {


          consumedNewWebDriverSessionBody = updateHubIfNewWebDriverSession(request, proxyResponse);
        }
        if ((newSessionRequest) && ((statusCode == 500) || (statusCode == 400) || (statusCode == 401)))
        {


          removeIncompleteNewSessionRequest();
        }
        if (statusCode == 404) {
          removeSessionBrowserTimeout();
        }
        
        byte[] contentBeingForwarded = null;
        if (responseBody != null) {
          InputStream in;
          if (consumedNewWebDriverSessionBody == null) {
            InputStream in = responseBody.getContent();
            if ((request.getRequestType() == RequestType.START_SESSION) && ((request instanceof LegacySeleniumRequest))) {
              res = getResponseUtf8Content(in);
              
              updateHubNewSeleniumSession(res);
              
              in = new ByteArrayInputStream(res.getBytes("UTF-8"));
            }
          } else {
            in = new ByteArrayInputStream(consumedNewWebDriverSessionBody);
          }
          
          byte[] bytes = drainInputStream(in);
          contentBeingForwarded = bytes;
        }
        
        if ((slot.getProxy() instanceof CommandListener)) {
          SeleniumBasedResponse wrappedResponse = new SeleniumBasedResponse(response);
          wrappedResponse.setForwardedContent(contentBeingForwarded);
          ((CommandListener)slot.getProxy()).afterCommand(this, request, wrappedResponse);
          contentBeingForwarded = wrappedResponse.getForwardedContentAsByteArray();
        }
        
        if (contentBeingForwarded != null) {
          writeRawBody(response, contentBeingForwarded);
        }
        response.flushBuffer();
      } finally {
        EntityUtils.consume(responseBody);
      }
      response.flushBuffer();
      
      return res;
    } finally {
      forwardingRequest = false;
      Thread.currentThread().setName(currentThreadName);
    }
  }
  
  private void setThreadDisplayName() {
    DateFormat dfmt = DateFormat.getTimeInstance();
    
    String name = "Forwarding " + this + " to " + slot.getRemoteURL() + " at " + dfmt.format(Calendar.getInstance().getTime());
    Thread.currentThread().setName(name);
  }
  
  private void removeIncompleteNewSessionRequest() {
    RemoteProxy proxy = slot.getProxy();
    proxy.getRegistry().terminate(this, SessionTerminationReason.CREATIONFAILED);
  }
  
  private void removeSessionBrowserTimeout() {
    RemoteProxy proxy = slot.getProxy();
    proxy.getRegistry().terminate(this, SessionTerminationReason.BROWSER_TIMEOUT);
  }
  
  private void updateHubNewSeleniumSession(String content) {
    ExternalSessionKey key = ExternalSessionKey.fromResponseBody(content);
    setExternalKey(key);
  }
  
  private byte[] updateHubIfNewWebDriverSession(SeleniumBasedRequest request, HttpResponse proxyResponse) throws IOException
  {
    byte[] consumedData = null;
    if ((request.getRequestType() == RequestType.START_SESSION) && ((request instanceof WebDriverRequest)))
    {
      Header h = proxyResponse.getFirstHeader("Location");
      if (h == null) {
        if ((isSuccessJsonResponse(proxyResponse)) && (proxyResponse.getEntity() != null)) {
          InputStream stream = proxyResponse.getEntity().getContent();
          consumedData = ByteStreams.toByteArray(stream);
          stream.close();
          
          String contentString = new String(consumedData, Charsets.UTF_8);
          ExternalSessionKey key = ExternalSessionKey.fromJsonResponseBody(contentString);
          if (key == null) {
            throw new GridException("webdriver new session JSON response body did not contain a session ID");
          }
          
          setExternalKey(key);
          return consumedData;
        }
        throw new GridException("new session request for webdriver should contain a location header or an 'application/json;charset=UTF-8' response body with the session ID.");
      }
      

      ExternalSessionKey key = ExternalSessionKey.fromWebDriverRequest(h.getValue());
      setExternalKey(key);
    }
    return consumedData;
  }
  
  private static boolean isSuccessJsonResponse(HttpResponse response) {
    if (response.getStatusLine().getStatusCode() == 200) {
      for (Header header : response.getHeaders("Content-Type"))
      {
        try {
          type = MediaType.parse(header.getValue());
        } catch (IllegalArgumentException ignored) { MediaType type;
          continue;
        }
        MediaType type;
        if (MediaType.JSON_UTF_8.is(type)) {
          return true;
        }
      }
    }
    return false;
  }
  
  private HttpResponse sendRequestToNode(HttpRequest proxyRequest) throws ClientProtocolException, IOException
  {
    HttpClient client = getClient();
    URL remoteURL = slot.getRemoteURL();
    HttpHost host = new HttpHost(remoteURL.getHost(), remoteURL.getPort(), remoteURL.getProtocol());
    
    return client.execute(host, proxyRequest);
  }
  
  private HttpRequest prepareProxyRequest(HttpServletRequest request)
    throws IOException
  {
    URL remoteURL = slot.getRemoteURL();
    
    String pathSpec = request.getServletPath() + request.getContextPath();
    String path = request.getRequestURI();
    if (!path.startsWith(pathSpec)) {
      throw new IllegalStateException("Expected path " + path + " to start with pathSpec " + pathSpec);
    }
    
    String end = path.substring(pathSpec.length());
    String ok = remoteURL + end;
    if (request.getQueryString() != null) {
      ok = ok + "?" + request.getQueryString();
    }
    String uri = new URL(remoteURL, ok).toExternalForm();
    
    InputStream body = null;
    if ((request.getContentLength() > 0) || (request.getHeader("Transfer-Encoding") != null)) {
      body = request.getInputStream();
    }
    
    HttpRequest proxyRequest;
    HttpRequest proxyRequest;
    if (body != null)
    {
      BasicHttpEntityEnclosingRequest r = new BasicHttpEntityEnclosingRequest(request.getMethod(), uri);
      r.setEntity(new InputStreamEntity(body, request.getContentLength()));
      proxyRequest = r;
    } else {
      proxyRequest = new BasicHttpRequest(request.getMethod(), uri);
    }
    
    for (Enumeration<?> e = request.getHeaderNames(); e.hasMoreElements();) {
      String headerName = (String)e.nextElement();
      
      if (!"Content-Length".equalsIgnoreCase(headerName))
      {


        proxyRequest.setHeader(headerName, request.getHeader(headerName)); }
    }
    return proxyRequest;
  }
  
  private void writeRawBody(HttpServletResponse response, byte[] rawBody) throws IOException {
    try { OutputStream out = response.getOutputStream();Throwable localThrowable3 = null;
      






      try
      {
        if (!response.containsHeader("Content-Length")) {
          response.setIntHeader("Content-Length", rawBody.length);
        }
        
        out.write(rawBody);
      }
      catch (Throwable localThrowable1)
      {
        localThrowable3 = localThrowable1;throw localThrowable1;






      }
      finally
      {





        if (out != null) if (localThrowable3 != null) try { out.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else out.close();
      } } catch (IOException e) { throw new ClientGoneException(e);
    }
  }
  
  private byte[] drainInputStream(InputStream in) throws IOException {
    try {
      return ByteStreams.toByteArray(in);
    } finally {
      in.close();
    }
  }
  
  private String getResponseUtf8Content(InputStream in)
  {
    StringBuilder sb = new StringBuilder();
    try
    {
      BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
      String line; while ((line = reader.readLine()) != null)
      {
        sb.append(line);
      }
      in.close();
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e); }
    String line;
    String res = sb.toString();
    return res;
  }
  
  private void processResponseHeaders(HttpServletRequest request, HttpServletResponse response, URL remoteURL, HttpResponse proxyResponse)
    throws MalformedURLException
  {
    String pathSpec = request.getServletPath() + request.getContextPath();
    for (Header header : proxyResponse.getAllHeaders()) {
      String name = header.getName();
      String value = header.getValue();
      








      if ((!name.equalsIgnoreCase("Transfer-Encoding")) || (!value.equalsIgnoreCase("chunked")))
      {




        if (name.equalsIgnoreCase("Location")) {
          URL returnedLocation = new URL(remoteURL, value);
          String driverPath = remoteURL.getPath();
          String wrongPath = returnedLocation.getPath();
          String correctPath = wrongPath.replace(driverPath, "");
          Hub hub = slot.getProxy().getRegistry().getHub();
          response.setHeader(name, hub.getUrl(pathSpec + correctPath).toString());
        } else {
          response.setHeader(name, value);
        }
      }
    }
  }
  




  public Object get(String key)
  {
    return objects.get(key);
  }
  





  public void put(String key, Object value)
  {
    objects.put(key, value);
  }
  






  public boolean sendDeleteSessionRequest()
  {
    URL remoteURL = slot.getRemoteURL();
    HttpRequest request;
    HttpRequest request;
    switch (1.$SwitchMap$org$openqa$grid$common$SeleniumProtocol[slot.getProtocol().ordinal()])
    {


    case 1: 
      request = new BasicHttpRequest("POST", remoteURL.toExternalForm() + "/?cmd=testComplete&sessionId=" + getExternalKey().getKey());
      break;
    case 2: 
      String uri = remoteURL.toString() + "/session/" + externalKey;
      request = new BasicHttpRequest("DELETE", uri);
      break;
    default: 
      throw new GridException("Error, protocol not implemented."); }
    
    HttpRequest request;
    HttpHost host = new HttpHost(remoteURL.getHost(), remoteURL.getPort());
    HttpEntity responseBody = null;
    try
    {
      HttpClient client = getClient();
      HttpResponse response = client.execute(host, request);
      responseBody = response.getEntity();
      int code = response.getStatusLine().getStatusCode();
      boolean ok; boolean ok; return (code >= 200) && (code <= 299);
    } catch (Throwable e) {
      ok = false;
      
      log.severe("Unable to send DELETE request for the current session " + e.getMessage());
    } finally {
      try {
        EntityUtils.consume(responseBody);
      } catch (IOException e) {
        log.warning("Consuming the response body when DELETE to the node " + e.getMessage());
      }
    }
  }
  







  public void setIgnoreTimeout(boolean ignore)
  {
    if (!ignore) {
      lastActivity = timeSource.currentTimeInMillis();
    }
    ignoreTimeout = ignore;
  }
  
  public boolean isForwardingRequest()
  {
    return forwardingRequest;
  }
}
