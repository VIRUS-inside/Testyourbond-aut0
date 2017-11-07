package com.gargoylesoftware.htmlunit.util;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.gae.GAEUtils;
import com.gargoylesoftware.htmlunit.protocol.AnyHandler;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLStreamHandler;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;















abstract class URLCreator
{
  URLCreator() {}
  
  abstract URL toUrlUnsafeClassic(String paramString)
    throws MalformedURLException;
  
  abstract String getProtocol(URL paramURL);
  
  protected URL toNormalUrl(String url)
    throws MalformedURLException
  {
    URL response = new URL(url);
    if ((response.getProtocol().startsWith("http")) && (StringUtils.isEmpty(response.getHost()))) {
      throw new MalformedURLException("Missing host name in url: " + url);
    }
    return response;
  }
  



  static URLCreator getCreator()
  {
    if (!GAEUtils.isGaeMode()) {
      return new URLCreatorStandard();
    }
    return new URLCreatorGAE();
  }
  


  static class URLCreatorStandard
    extends URLCreator
  {
    private static final URLStreamHandler JS_HANDLER = new com.gargoylesoftware.htmlunit.protocol.javascript.Handler();
    
    private static final URLStreamHandler ABOUT_HANDLER = new com.gargoylesoftware.htmlunit.protocol.about.Handler();
    private static final URLStreamHandler DATA_HANDLER = new com.gargoylesoftware.htmlunit.protocol.data.Handler();
    
    URLCreatorStandard() {}
    
    URL toUrlUnsafeClassic(String url) throws MalformedURLException { String protocol = StringUtils.substringBefore(url, ":")
        .toLowerCase(Locale.ROOT);
      
      if ((protocol.isEmpty()) || (UrlUtils.isNormalUrlProtocol(protocol))) {
        return toNormalUrl(url);
      }
      if ("javascript:".equals(protocol + ":")) {
        return new URL(null, url, JS_HANDLER);
      }
      if ("about".equals(protocol)) {
        if ((WebClient.URL_ABOUT_BLANK != null) && 
          (StringUtils.equalsIgnoreCase(WebClient.URL_ABOUT_BLANK.toExternalForm(), url))) {
          return WebClient.URL_ABOUT_BLANK;
        }
        return new URL(null, url, ABOUT_HANDLER);
      }
      if ("data".equals(protocol)) {
        return new URL(null, url, DATA_HANDLER);
      }
      
      return new URL(null, url, AnyHandler.INSTANCE);
    }
    

    String getProtocol(URL url)
    {
      return url.getProtocol();
    }
  }
  
  static class URLCreatorGAE extends URLCreator
  {
    private static final String PREFIX = "http://gaeHack_";
    
    URLCreatorGAE() {}
    
    URL toUrlUnsafeClassic(String url) throws MalformedURLException
    {
      if ((WebClient.URL_ABOUT_BLANK != null) && 
        (StringUtils.equalsIgnoreCase(WebClient.URL_ABOUT_BLANK.toExternalForm(), url))) {
        return WebClient.URL_ABOUT_BLANK;
      }
      if (StringUtils.startsWithIgnoreCase(url, "javascript:")) {
        return new URL("http://gaeHack_" + url.replaceFirst(":", "/"));
      }
      if (StringUtils.startsWithIgnoreCase(url, "about:")) {
        return new URL("http://gaeHack_" + url.replaceFirst(":", "/"));
      }
      if (StringUtils.startsWithIgnoreCase(url, "data:")) {
        return new URL("http://gaeHack_" + url.replaceFirst(":", "/"));
      }
      
      return toNormalUrl(url);
    }
    

    String getProtocol(URL url)
    {
      String stringUrl = url.toString();
      if (stringUrl.startsWith("http://gaeHack_")) {
        int begin = "http://gaeHack_".length();
        int end = stringUrl.indexOf("/", begin);
        return stringUrl.substring(begin, end);
      }
      
      return url.getProtocol();
    }
  }
}
