package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.History;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.javascript.host.event.HashChangeEvent;
import com.gargoylesoftware.htmlunit.util.UrlUtils;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;











































@JsxClass
public class Location
  extends SimpleScriptable
{
  private static final Log LOG = LogFactory.getLog(Location.class);
  



  private static final String UNKNOWN = "null";
  



  private Window window_;
  


  private String hash_;
  



  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public Location() {}
  



  public void initialize(Window window)
  {
    window_ = window;
    if ((window_ != null) && (window_.getWebWindow().getEnclosedPage() != null)) {
      setHash(window_.getWebWindow().getEnclosedPage().getUrl().getRef());
    }
  }
  



  public Object getDefaultValue(Class<?> hint)
  {
    if ((getPrototype() != null) && ((hint == null) || (String.class.equals(hint)))) {
      return getHref();
    }
    return super.getDefaultValue(hint);
  }
  




  @JsxFunction
  public void assign(String url)
    throws IOException
  {
    setHref(url);
  }
  





  @JsxFunction
  public void reload(boolean force)
    throws IOException
  {
    String url = getHref();
    if ("null".equals(url)) {
      LOG.error("Unable to reload location: current URL is unknown.");
    }
    else {
      setHref(url);
    }
  }
  




  @JsxFunction
  public void replace(String url)
    throws IOException
  {
    window_.getWebWindow().getHistory().removeCurrent();
    setHref(url);
  }
  




  @JsxFunction
  public String toString()
  {
    if (window_ != null) {
      return getHref();
    }
    return "";
  }
  




  @JsxGetter
  public String getHref()
  {
    Page page = window_.getWebWindow().getEnclosedPage();
    if (page == null) {
      return "null";
    }
    try {
      URL url = page.getUrl();
      boolean encodeHash = getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_LOCATION_HREF_HASH_IS_ENCODED);
      String hash = getHash(encodeHash);
      if (hash != null) {
        url = UrlUtils.getUrlWithNewRef(url, hash);
      }
      String s = url.toExternalForm();
      if ((s.startsWith("file:/")) && (!s.startsWith("file:///"))) {}
      

      return "file:///" + s.substring("file:/".length());

    }
    catch (MalformedURLException e)
    {
      LOG.error(e.getMessage(), e); }
    return page.getUrl().toExternalForm();
  }
  





  @JsxSetter
  public void setHref(String newLocation)
    throws IOException
  {
    setHref(newLocation, false, null);
  }
  










  public void setHref(String newLocation, boolean justHistoryAPIPushState, Object state)
    throws IOException
  {
    HtmlPage page = (HtmlPage)getWindow(getStartingScope()).getWebWindow().getEnclosedPage();
    if (newLocation.startsWith("javascript:")) {
      String script = newLocation.substring(11);
      page.executeJavaScriptIfPossible(script, "new location value", 1);
      return;
    }
    try {
      URL url = page.getFullyQualifiedUrl(newLocation);
      
      if (StringUtils.isEmpty(newLocation)) {
        boolean dropFilename = page.getWebClient().getBrowserVersion()
          .hasFeature(BrowserVersionFeatures.ANCHOR_EMPTY_HREF_NO_FILENAME);
        if (dropFilename) {
          String path = url.getPath();
          path = path.substring(0, path.lastIndexOf('/') + 1);
          url = UrlUtils.getUrlWithNewPath(url, path);
          url = UrlUtils.getUrlWithNewRef(url, null);
        }
        else {
          url = UrlUtils.getUrlWithNewRef(url, null);
        }
      }
      
      WebRequest request = new WebRequest(url);
      request.setAdditionalHeader("Referer", page.getUrl().toExternalForm());
      
      WebWindow webWindow = window_.getWebWindow();
      webWindow.getWebClient().download(webWindow, "", request, true, false, "JS set location");
      if (justHistoryAPIPushState) {
        webWindow.getWebClient().loadDownloadedResponses();
      }
    }
    catch (MalformedURLException e) {
      LOG.error("setHref('" + newLocation + "') got MalformedURLException", e);
      throw e;
    }
  }
  




  @JsxGetter
  public String getSearch()
  {
    String search = getUrl().getQuery();
    if (search == null) {
      return "";
    }
    return "?" + search;
  }
  




  @JsxSetter
  public void setSearch(String search)
    throws Exception
  {
    setUrl(UrlUtils.getUrlWithNewQuery(getUrl(), search));
  }
  




  @JsxGetter
  public String getHash()
  {
    boolean decodeHash = getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_LOCATION_HASH_IS_DECODED);
    String hash = hash_;
    
    if ((hash_ != null) && ((decodeHash) || (hash_.equals(getUrl().getRef())))) {
      hash = decodeHash(hash);
    }
    
    if (StringUtils.isEmpty(hash)) {
      if ((getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_LOCATION_HASH_RETURNS_HASH_FOR_EMPTY_DEFINED)) && 
        (getHref().endsWith("#"))) {
        return "#";
      }
    } else {
      if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_LOCATION_HASH_HASH_IS_ENCODED)) {
        return "#" + UrlUtils.encodeHash(hash);
      }
      
      return "#" + hash;
    }
    
    return "";
  }
  
  private String getHash(boolean encoded) {
    if ((hash_ == null) || (hash_.isEmpty())) {
      return null;
    }
    if (encoded) {
      return UrlUtils.encodeAnchor(hash_);
    }
    return hash_;
  }
  







  @JsxSetter
  public void setHash(String hash)
  {
    setHash(getHref(), hash);
  }
  







  public void setHash(String oldURL, String hash)
  {
    if ((hash != null) && (!hash.isEmpty()) && (hash.charAt(0) == '#')) {
      hash = hash.substring(1);
    }
    boolean hasChanged = (hash != null) && (!hash.equals(hash_));
    hash_ = hash;
    String newURL = getHref();
    
    if (hasChanged) { Event event;
      Event event;
      if (getBrowserVersion().hasFeature(BrowserVersionFeatures.EVENT_TYPE_HASHCHANGEEVENT)) {
        event = new HashChangeEvent(getWindow(), "hashchange", oldURL, newURL);
      }
      else {
        event = new Event(getWindow(), "hashchange");
        event.initEvent("hashchange", false, false);
      }
      getWindow().executeEventLocally(event);
    }
  }
  
  private static String decodeHash(String hash) {
    if (hash.indexOf('%') == -1) {
      return hash;
    }
    return UrlUtils.decode(hash);
  }
  




  @JsxGetter
  public String getHostname()
  {
    return getUrl().getHost();
  }
  




  @JsxSetter
  public void setHostname(String hostname)
    throws Exception
  {
    setUrl(UrlUtils.getUrlWithNewHost(getUrl(), hostname));
  }
  




  @JsxGetter
  public String getHost()
  {
    URL url = getUrl();
    int port = url.getPort();
    String host = url.getHost();
    
    if (port == -1) {
      return host;
    }
    return host + ":" + port;
  }
  






  @JsxSetter
  public void setHost(String host)
    throws Exception
  {
    int index = host.indexOf(':');
    int port; String hostname; int port; if (index != -1) {
      String hostname = host.substring(0, index);
      port = Integer.parseInt(host.substring(index + 1));
    }
    else {
      hostname = host;
      port = -1;
    }
    URL url = UrlUtils.getUrlWithNewHostAndPort(getUrl(), hostname, port);
    setUrl(url);
  }
  




  @JsxGetter
  public String getPathname()
  {
    if (WebClient.URL_ABOUT_BLANK == getUrl()) {
      if (getBrowserVersion().hasFeature(BrowserVersionFeatures.URL_ABOUT_BLANK_HAS_EMPTY_PATH)) {
        return "";
      }
      if (getBrowserVersion().hasFeature(BrowserVersionFeatures.URL_ABOUT_BLANK_HAS_BLANK_PATH)) {
        return "blank";
      }
      return "/blank";
    }
    return getUrl().getPath();
  }
  




  @JsxSetter
  public void setPathname(String pathname)
    throws Exception
  {
    setUrl(UrlUtils.getUrlWithNewPath(getUrl(), pathname));
  }
  




  @JsxGetter
  public String getPort()
  {
    int port = getUrl().getPort();
    if (port == -1) {
      return "";
    }
    return Integer.toString(port);
  }
  




  @JsxSetter
  public void setPort(String port)
    throws Exception
  {
    setUrl(UrlUtils.getUrlWithNewPort(getUrl(), Integer.parseInt(port)));
  }
  




  @JsxGetter
  public String getProtocol()
  {
    return getUrl().getProtocol() + ":";
  }
  




  @JsxSetter
  public void setProtocol(String protocol)
    throws Exception
  {
    setUrl(UrlUtils.getUrlWithNewProtocol(getUrl(), protocol));
  }
  



  private URL getUrl()
  {
    return window_.getWebWindow().getEnclosedPage().getUrl();
  }
  




  private void setUrl(URL url)
    throws IOException
  {
    window_.getWebWindow().getWebClient().getPage(window_.getWebWindow(), new WebRequest(url));
  }
  



  @JsxGetter
  public String getOrigin()
  {
    return getUrl().getProtocol() + "://" + getHost();
  }
}
