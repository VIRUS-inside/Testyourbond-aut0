package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DOMTokenList;
import com.gargoylesoftware.htmlunit.util.UrlUtils;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import org.apache.commons.lang3.StringUtils;







































@JsxClass(domClass=HtmlAnchor.class)
public class HTMLAnchorElement
  extends HTMLElement
{
  private static final List<String> REFERRER_POLICIES = Arrays.asList(new String[] { "no-referrer", "origin", "unsafe-url" });
  




  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public HTMLAnchorElement() {}
  



  @JsxSetter
  public void setHref(String href)
  {
    getDomNodeOrDie().setAttribute("href", href);
  }
  



  @JsxGetter
  public String getHref()
  {
    HtmlAnchor anchor = (HtmlAnchor)getDomNodeOrDie();
    String hrefAttr = anchor.getHrefAttribute();
    
    if (hrefAttr == DomElement.ATTRIBUTE_NOT_DEFINED) {
      return "";
    }
    try
    {
      return getUrl().toString();
    }
    catch (MalformedURLException e) {}
    return hrefAttr;
  }
  




  public void focus()
  {
    HtmlAnchor anchor = (HtmlAnchor)getDomNodeOrDie();
    String hrefAttr = anchor.getHrefAttribute();
    
    if (hrefAttr != DomElement.ATTRIBUTE_NOT_DEFINED) {
      anchor.focus();
    }
  }
  



  @JsxSetter
  public void setName(String name)
  {
    getDomNodeOrDie().setAttribute("name", name);
  }
  



  @JsxGetter
  public String getName()
  {
    return getDomNodeOrDie().getAttribute("name");
  }
  



  @JsxSetter
  public void setTarget(String target)
  {
    getDomNodeOrDie().setAttribute("target", target);
  }
  



  @JsxGetter
  public String getTarget()
  {
    return getDomNodeOrDie().getAttribute("target");
  }
  



  private URL getUrl()
    throws MalformedURLException
  {
    HtmlAnchor anchor = (HtmlAnchor)getDomNodeOrDie();
    return ((HtmlPage)anchor.getPage()).getFullyQualifiedUrl(anchor.getHrefAttribute());
  }
  



  private void setUrl(URL url)
  {
    getDomNodeOrDie().setAttribute("href", url.toString());
  }
  



  @JsxSetter
  public void setRel(String rel)
  {
    getDomNodeOrDie().setAttribute("rel", rel);
  }
  



  @JsxGetter
  public String getRel()
  {
    return ((HtmlAnchor)getDomNodeOrDie()).getRelAttribute();
  }
  



  @JsxGetter
  public String getRev()
  {
    return ((HtmlAnchor)getDomNodeOrDie()).getRevAttribute();
  }
  



  @JsxSetter
  public void setRev(String rel)
  {
    getDomNodeOrDie().setAttribute("rev", rel);
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
  public String getReferrerPolicy()
  {
    String attrib = ((HtmlAnchor)getDomNodeOrDie()).getAttribute("referrerPolicy");
    if (StringUtils.isEmpty(attrib)) {
      return "";
    }
    attrib = attrib.toLowerCase(Locale.ROOT);
    if (REFERRER_POLICIES.contains(attrib)) {
      return attrib;
    }
    return "";
  }
  



  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
  public void setReferrerPolicy(String referrerPolicy)
  {
    getDomNodeOrDie().setAttribute("referrerPolicy", referrerPolicy);
  }
  




  @JsxGetter
  public String getSearch()
  {
    try
    {
      String query = getUrl().getQuery();
      if (query == null) {
        return "";
      }
      return "?" + query;
    }
    catch (MalformedURLException e) {}
    return "";
  }
  


  @JsxSetter
  public void setSearch(String search)
    throws Exception
  {
    String query;
    

    String query;
    
    if ((search == null) || ("?".equals(search)) || ("".equals(search))) {
      query = null;
    } else { String query;
      if (search.charAt(0) == '?') {
        query = search.substring(1);
      }
      else {
        query = search;
      }
    }
    setUrl(UrlUtils.getUrlWithNewQuery(getUrl(), query));
  }
  



  @JsxGetter
  public String getHash()
  {
    try
    {
      String hash = getUrl().getRef();
      if (hash == null) {
        return "";
      }
      return "#" + hash;
    }
    catch (MalformedURLException e) {}
    return "";
  }
  





  @JsxSetter
  public void setHash(String hash)
    throws Exception
  {
    setUrl(UrlUtils.getUrlWithNewRef(getUrl(), hash));
  }
  



  @JsxGetter
  public String getHost()
  {
    try
    {
      URL url = getUrl();
      int port = url.getPort();
      String host = url.getHost();
      
      if (port == -1) {
        return host;
      }
      return host + ":" + port;
    }
    catch (MalformedURLException e) {}
    return "";
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
  public String getHostname()
  {
    try
    {
      return getUrl().getHost();
    }
    catch (MalformedURLException e) {}
    return "";
  }
  





  @JsxSetter
  public void setHostname(String hostname)
    throws Exception
  {
    setUrl(UrlUtils.getUrlWithNewHost(getUrl(), hostname));
  }
  



  @JsxGetter
  public String getPathname()
  {
    try
    {
      URL url = getUrl();
      if ((!url.getProtocol().startsWith("http")) && 
        (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_ANCHOR_PATHNAME_NONE_FOR_NONE_HTTP_URL))) {
        return "";
      }
      if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_ANCHOR_PATHNAME_DETECT_WIN_DRIVES_URL)) {
        HtmlAnchor anchor = (HtmlAnchor)getDomNodeOrDie();
        String href = anchor.getHrefAttribute();
        if ((href.length() > 1) && (Character.isLetter(href.charAt(0))) && (':' == href.charAt(1))) {
          if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_ANCHOR_PROTOCOL_COLON_UPPER_CASE_DRIVE_LETTERS)) {
            href = StringUtils.capitalize(href);
          }
          if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_ANCHOR_PATHNAME_PREFIX_WIN_DRIVES_URL)) {}
          return "/" + href;
        }
      }
      

      return url.getPath();
    }
    catch (MalformedURLException e) {
      HtmlAnchor anchor = (HtmlAnchor)getDomNodeOrDie();
      if ((anchor.getHrefAttribute().startsWith("http")) && 
        (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_ANCHOR_PATHNAME_NONE_FOR_BROKEN_URL)))
        return "";
    }
    return "/";
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
    try
    {
      int port = getUrl().getPort();
      if (port == -1) {
        return "";
      }
      return Integer.toString(port);
    }
    catch (MalformedURLException e) {}
    return "";
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
    try
    {
      if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_ANCHOR_PATHNAME_DETECT_WIN_DRIVES_URL)) {
        HtmlAnchor anchor = (HtmlAnchor)getDomNodeOrDie();
        String href = anchor.getHrefAttribute().toLowerCase(Locale.ROOT);
        if ((href.length() > 1) && (Character.isLetter(href.charAt(0))) && (':' == href.charAt(1))) {
          return "file:";
        }
      }
      
      return getUrl().getProtocol() + ":";
    }
    catch (MalformedURLException e) {
      HtmlAnchor anchor = (HtmlAnchor)getDomNodeOrDie();
      if ((anchor.getHrefAttribute().startsWith("http")) && 
        (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_ANCHOR_PROTOCOL_COLON_FOR_BROKEN_URL))) {
        return ":";
      }
      return StringUtils.substringBefore(anchor.getHrefAttribute(), "/");
    }
  }
  




  @JsxSetter
  public void setProtocol(String protocol)
    throws Exception
  {
    String bareProtocol = StringUtils.substringBefore(protocol, ":");
    setUrl(UrlUtils.getUrlWithNewProtocol(getUrl(), bareProtocol));
  }
  






  public Object getDefaultValue(Class<?> hint)
  {
    HtmlElement element = getDomNodeOrNull();
    if (element == null) {
      return super.getDefaultValue(null);
    }
    return getDefaultValue(element);
  }
  
  static String getDefaultValue(HtmlElement element) {
    String href = element.getAttribute("href");
    
    if (DomElement.ATTRIBUTE_NOT_DEFINED == href) {
      return "";
    }
    
    href = href.trim();
    
    SgmlPage page = element.getPage();
    if ((page == null) || (!page.isHtmlPage())) {
      return href;
    }
    try
    {
      return HtmlAnchor.getTargetUrl(href, (HtmlPage)page).toExternalForm();
    }
    catch (MalformedURLException e) {}
    return href;
  }
  




  @JsxGetter
  public String getText()
  {
    DomNode htmlElement = getDomNodeOrDie();
    return htmlElement.asText();
  }
  



  @JsxSetter
  public void setText(String text)
  {
    DomNode htmlElement = getDomNodeOrDie();
    htmlElement.setTextContent(text);
  }
  



  @JsxGetter
  public String getCharset()
  {
    return getDomNodeOrDie().getAttribute("charset");
  }
  



  @JsxSetter
  public void setCharset(String charset)
  {
    getDomNodeOrDie().setAttribute("charset", charset);
  }
  



  @JsxGetter
  public String getCoords()
  {
    return getDomNodeOrDie().getAttribute("coords");
  }
  



  @JsxSetter
  public void setCoords(String coords)
  {
    getDomNodeOrDie().setAttribute("coords", coords);
  }
  



  @JsxGetter
  public String getHreflang()
  {
    return getDomNodeOrDie().getAttribute("hreflang");
  }
  



  @JsxSetter
  public void setHreflang(String hreflang)
  {
    getDomNodeOrDie().setAttribute("hreflang", hreflang);
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public String getOrigin()
  {
    if (!getDomNodeOrDie().hasAttribute("href")) {
      return "";
    }
    try
    {
      return getUrl().getProtocol() + "://" + getHost();
    }
    catch (Exception e) {}
    return "";
  }
  





  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public void setOrigin(String origin) {}
  





  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public String getUsername()
  {
    throw Context.throwAsScriptRuntimeEx(new UnsupportedOperationException());
  }
  



  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public void setUsername(String username)
  {
    throw Context.throwAsScriptRuntimeEx(new UnsupportedOperationException());
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public String getPassword()
  {
    throw Context.throwAsScriptRuntimeEx(new UnsupportedOperationException());
  }
  



  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public void setPassword(String password)
  {
    throw Context.throwAsScriptRuntimeEx(new UnsupportedOperationException());
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public String getDownload()
  {
    throw Context.throwAsScriptRuntimeEx(new UnsupportedOperationException());
  }
  



  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public void setDownload(String download)
  {
    throw Context.throwAsScriptRuntimeEx(new UnsupportedOperationException());
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public String getPing()
  {
    return ((HtmlAnchor)getDomNodeOrDie()).getPingAttribute();
  }
  



  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public void setPing(String ping)
  {
    getDomNodeOrDie().setAttribute("ping", ping);
  }
  



  @JsxGetter
  public String getShape()
  {
    throw Context.throwAsScriptRuntimeEx(new UnsupportedOperationException());
  }
  



  @JsxSetter
  public void setShape(String shape)
  {
    throw Context.throwAsScriptRuntimeEx(new UnsupportedOperationException());
  }
  



  @JsxGetter
  public String getType()
  {
    return getDomNodeOrDie().getAttribute("type");
  }
  



  @JsxSetter
  public void setType(String type)
  {
    getDomNodeOrDie().setAttribute("type", type);
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public DOMTokenList getRelList()
  {
    throw Context.throwAsScriptRuntimeEx(new UnsupportedOperationException());
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public String getProtocolLong()
  {
    throw Context.throwAsScriptRuntimeEx(new UnsupportedOperationException());
  }
  



  @JsxGetter(propertyName="Methods", value={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public String getMethods()
  {
    throw Context.throwAsScriptRuntimeEx(new UnsupportedOperationException());
  }
  



  @JsxSetter(propertyName="Methods", value={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void setMethods(String methods)
  {
    throw Context.throwAsScriptRuntimeEx(new UnsupportedOperationException());
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public String getMimeType()
  {
    throw Context.throwAsScriptRuntimeEx(new UnsupportedOperationException());
  }
  



  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void setMimeType(String mimeType)
  {
    throw Context.throwAsScriptRuntimeEx(new UnsupportedOperationException());
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public String getNameProp()
  {
    throw Context.throwAsScriptRuntimeEx(new UnsupportedOperationException());
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public String getUrn()
  {
    throw Context.throwAsScriptRuntimeEx(new UnsupportedOperationException());
  }
  



  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void setUrn(String urn)
  {
    throw Context.throwAsScriptRuntimeEx(new UnsupportedOperationException());
  }
}
