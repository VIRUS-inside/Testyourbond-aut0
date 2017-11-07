package com.gargoylesoftware.htmlunit;

import com.gargoylesoftware.htmlunit.util.Cookie;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


































public class UrlFetchWebConnection
  implements WebConnection
{
  private static final Log LOG = LogFactory.getLog(UrlFetchWebConnection.class);
  
  private static final String[] GAE_URL_HACKS = { "http://gaeHack_javascript/", "http://gaeHack_data/", 
    "http://gaeHack_about/" };
  

  private final WebClient webClient_;
  


  public UrlFetchWebConnection(WebClient webClient)
  {
    webClient_ = webClient;
  }
  


  public WebResponse getResponse(WebRequest webRequest)
    throws IOException
  {
    long startTime = System.currentTimeMillis();
    URL url = webRequest.getUrl();
    if (LOG.isTraceEnabled()) {
      LOG.trace("about to fetch URL " + url);
    }
    

    WebResponse response = produceWebResponseForGAEProcolHack(url);
    if (response != null) {
      return response;
    }
    
    try
    {
      HttpURLConnection connection = (HttpURLConnection)url.openConnection();
      
      connection.setConnectTimeout(webClient_.getOptions().getTimeout());
      
      connection.addRequestProperty("User-Agent", webClient_.getBrowserVersion().getUserAgent());
      connection.setInstanceFollowRedirects(false);
      

      for (Map.Entry<String, String> header : webRequest.getAdditionalHeaders().entrySet()) {
        connection.addRequestProperty((String)header.getKey(), (String)header.getValue());
      }
      addCookies(connection);
      
      HttpMethod httpMethod = webRequest.getHttpMethod();
      connection.setRequestMethod(httpMethod.name());
      if ((HttpMethod.POST == httpMethod) || (HttpMethod.PUT == httpMethod) || (HttpMethod.PATCH == httpMethod)) {
        connection.setDoOutput(true);
        Charset charset = webRequest.getCharset();
        connection.addRequestProperty("Content-Type", FormEncodingType.URL_ENCODED.getName());
        
        Object localObject1 = null;Object localObject4 = null; Object localObject3; label373: try { outputStream = connection.getOutputStream();
        }
        finally
        {
          List<com.gargoylesoftware.htmlunit.util.NameValuePair> pairs;
          
          org.apache.http.NameValuePair[] httpClientPairs;
          String query;
          localObject3 = localThrowable; break label373; if (localObject3 != localThrowable) localObject3.addSuppressed(localThrowable);
        }
      }
      int responseCode = connection.getResponseCode();
      if (LOG.isTraceEnabled()) {
        LOG.trace("fetched URL " + url);
      }
      
      Object headers = new ArrayList();
      for (Object headerEntry : connection.getHeaderFields().entrySet()) {
        headerKey = (String)((Map.Entry)headerEntry).getKey();
        if (headerKey != null) {
          StringBuilder sb = new StringBuilder();
          for (String headerValue : (List)((Map.Entry)headerEntry).getValue()) {
            if (sb.length() != 0) {
              sb.append(", ");
            }
            sb.append(headerValue);
          }
          ((List)headers).add(new com.gargoylesoftware.htmlunit.util.NameValuePair(headerKey, sb.toString()));
        }
      }
      

      outputStream = null;headerKey = null; label678: try { is = responseCode < 400 ? 
          connection.getInputStream() : connection.getErrorStream(); } finally { InputStream is;
        byte[] byteArray;
        outputStream = headerKey; break label678; if (outputStream != headerKey) outputStream.addSuppressed(headerKey); }
      byte[] byteArray;
      long duration = System.currentTimeMillis() - startTime;
      WebResponseData responseData = new WebResponseData(byteArray, responseCode, 
        connection.getResponseMessage(), (List)headers);
      saveCookies(url.getHost(), (List)headers);
      return new WebResponse(responseData, webRequest, duration);
    }
    catch (IOException e) {
      LOG.error("Exception while tyring to fetch " + url, e);
      throw new RuntimeException(e);
    }
  }
  
  private void addCookies(HttpURLConnection connection) {
    StringBuilder cookieHeader = new StringBuilder();
    Set<Cookie> cookies = webClient_.getCookieManager().getCookies();
    if (cookies.isEmpty()) {
      return;
    }
    
    int cookieNb = 1;
    for (Cookie cookie : webClient_.getCookieManager().getCookies()) {
      cookieHeader.append(cookie.getName()).append('=').append(cookie.getValue());
      if (cookieNb < cookies.size()) {
        cookieHeader.append("; ");
      }
      cookieNb++;
    }
    connection.setRequestProperty("Cookie", cookieHeader.toString());
  }
  
  private void saveCookies(String domain, List<com.gargoylesoftware.htmlunit.util.NameValuePair> headers) {
    for (com.gargoylesoftware.htmlunit.util.NameValuePair nvp : headers) {
      if ("Set-Cookie".equalsIgnoreCase(nvp.getName())) {
        Set<Cookie> cookies = parseCookies(domain, nvp.getValue());
        for (Cookie cookie : cookies) {
          webClient_.getCookieManager().addCookie(cookie);
        }
      }
    }
  }
  
  private static WebResponse produceWebResponseForGAEProcolHack(URL url) {
    String externalForm = url.toExternalForm();
    for (String pattern : GAE_URL_HACKS) {
      int index = externalForm.indexOf(pattern);
      if (index == 0) {
        String contentString = externalForm.substring(pattern.length());
        if ((contentString.startsWith("'")) && (contentString.endsWith("'"))) {
          contentString = contentString.substring(1, contentString.length() - 1);
        }
        if (LOG.isDebugEnabled()) {
          LOG.debug("special handling of URL, returning (" + contentString + ") for URL " + url);
        }
        return new StringWebResponse(contentString, url);
      }
    }
    return null;
  }
  









  static Set<Cookie> parseCookies(String domain, String cookieHeaderString)
  {
    Set<Cookie> cookies = new HashSet();
    String[] cookieStrings = cookieHeaderString.split(",");
    for (int i = 0; i < cookieStrings.length; i++) {
      String[] nameAndValue = cookieStrings[i].split(";")[0].split("=");
      if (nameAndValue.length > 1) {
        cookies.add(new Cookie(domain, nameAndValue[0], nameAndValue[1]));
      }
    }
    return cookies;
  }
  
  public void close()
    throws Exception
  {}
}
