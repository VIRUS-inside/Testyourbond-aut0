package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;
import com.gargoylesoftware.htmlunit.util.UrlUtils;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;





























public class HtmlAnchor
  extends HtmlElement
{
  private static final Log LOG = LogFactory.getLog(HtmlAnchor.class);
  




  public static final String TAG_NAME = "a";
  




  HtmlAnchor(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
  }
  



  public <P extends Page> P click(Event event, boolean ignoreVisibility)
    throws IOException
  {
    boolean ctrl = event.getCtrlKey();
    WebWindow oldWebWindow = null;
    if (ctrl) {
      oldWebWindow = 
        ((HTMLElement)event.getSrcElement()).getDomNodeOrDie().getPage().getWebClient().getCurrentWindow();
    }
    
    P page = super.click(event, ignoreVisibility);
    
    if (ctrl) {
      page.getEnclosingWindow().getWebClient().setCurrentWindow(oldWebWindow);
      page = oldWebWindow.getEnclosedPage();
    }
    
    return page;
  }
  









  protected void doClickStateUpdate(boolean shiftKey, boolean ctrlKey, String hrefSuffix)
    throws IOException
  {
    String href = (getHrefAttribute() + hrefSuffix).trim();
    if (LOG.isDebugEnabled()) {
      String w = getPage().getEnclosingWindow().getName();
      LOG.debug("do click action in window '" + w + "', using href '" + href + "'");
    }
    if (ATTRIBUTE_NOT_DEFINED == getHrefAttribute()) {
      return;
    }
    HtmlPage page = (HtmlPage)getPage();
    if (StringUtils.startsWithIgnoreCase(href, "javascript:")) {
      StringBuilder builder = new StringBuilder(href.length());
      builder.append("javascript:");
      for (int i = "javascript:".length(); i < href.length(); i++) {
        char ch = href.charAt(i);
        if ((ch == '%') && (i + 2 < href.length())) {
          char ch1 = Character.toUpperCase(href.charAt(i + 1));
          char ch2 = Character.toUpperCase(href.charAt(i + 2));
          if (((Character.isDigit(ch1)) || ((ch1 >= 'A') && (ch1 <= 'F'))) && (
            (Character.isDigit(ch2)) || ((ch2 >= 'A') && (ch2 <= 'F')))) {
            builder.append((char)Integer.parseInt(href.substring(i + 1, i + 3), 16));
            i += 2;
            continue;
          }
        }
        builder.append(ch);
      }
      
      if (hasFeature(BrowserVersionFeatures.ANCHOR_IGNORE_TARGET_FOR_JS_HREF)) {
        page.executeJavaScriptIfPossible(builder.toString(), "javascript url", getStartLineNumber());
      }
      else {
        WebWindow win = page.getWebClient().openTargetWindow(page.getEnclosingWindow(), 
          page.getResolvedTarget(getTargetAttribute()), "_self");
        Page enclosedPage = win.getEnclosedPage();
        if ((enclosedPage != null) && (enclosedPage.isHtmlPage())) {
          page = (HtmlPage)enclosedPage;
          page.executeJavaScriptIfPossible(builder.toString(), "javascript url", getStartLineNumber());
        }
      }
      return;
    }
    
    URL url = getTargetUrl(href, page);
    
    BrowserVersion browser = page.getWebClient().getBrowserVersion();
    if ((ATTRIBUTE_NOT_DEFINED != getPingAttribute()) && (browser.hasFeature(BrowserVersionFeatures.ANCHOR_IGNORE_TARGET_FOR_JS_HREF))) {
      URL pingUrl = getTargetUrl(getPingAttribute(), page);
      WebRequest pingRequest = new WebRequest(pingUrl, HttpMethod.POST);
      pingRequest.setAdditionalHeader("Ping-From", page.getUrl().toExternalForm());
      pingRequest.setAdditionalHeader("Ping-To", url.toExternalForm());
      pingRequest.setRequestBody("PING");
      page.getWebClient().loadWebResponse(pingRequest);
    }
    
    WebRequest webRequest = new WebRequest(url, browser.getHtmlAcceptHeader());
    webRequest.setCharset(page.getCharset());
    webRequest.setAdditionalHeader("Referer", page.getUrl().toExternalForm());
    if (LOG.isDebugEnabled())
      LOG.debug(
        "Getting page for " + url.toExternalForm() + 
        ", derived from href '" + href + 
        "', using the originating URL " + 
        page.getUrl());
    String target;
    String target;
    if ((shiftKey) || (ctrlKey)) {
      target = "_blank";
    }
    else {
      target = page.getResolvedTarget(getTargetAttribute());
    }
    page.getWebClient().download(page.getEnclosingWindow(), target, webRequest, true, false, "Link click");
  }
  






  public static URL getTargetUrl(String href, HtmlPage page)
    throws MalformedURLException
  {
    URL url = page.getFullyQualifiedUrl(href);
    
    if (StringUtils.isEmpty(href)) {
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
    return url;
  }
  


  protected boolean doClickStateUpdate(boolean shiftKey, boolean ctrlKey)
    throws IOException
  {
    doClickStateUpdate(shiftKey, ctrlKey, "");
    return false;
  }
  






  public final String getCharsetAttribute()
  {
    return getAttribute("charset");
  }
  






  public final String getTypeAttribute()
  {
    return getAttribute("type");
  }
  






  public final String getNameAttribute()
  {
    return getAttribute("name");
  }
  






  public final String getHrefAttribute()
  {
    return getAttribute("href").trim();
  }
  






  public final String getHrefLangAttribute()
  {
    return getAttribute("hreflang");
  }
  






  public final String getRelAttribute()
  {
    return getAttribute("rel");
  }
  






  public final String getRevAttribute()
  {
    return getAttribute("rev");
  }
  






  public final String getAccessKeyAttribute()
  {
    return getAttribute("accesskey");
  }
  






  public final String getShapeAttribute()
  {
    return getAttribute("shape");
  }
  






  public final String getCoordsAttribute()
  {
    return getAttribute("coords");
  }
  






  public final String getTabIndexAttribute()
  {
    return getAttribute("tabindex");
  }
  






  public final String getOnFocusAttribute()
  {
    return getAttribute("onfocus");
  }
  






  public final String getOnBlurAttribute()
  {
    return getAttribute("onblur");
  }
  






  public final String getTargetAttribute()
  {
    return getAttribute("target");
  }
  









  public final Page openLinkInNewWindow()
    throws MalformedURLException
  {
    URL target = ((HtmlPage)getPage()).getFullyQualifiedUrl(getHrefAttribute());
    String windowName = "HtmlAnchor.openLinkInNewWindow() target";
    WebWindow newWindow = getPage().getWebClient().openWindow(target, "HtmlAnchor.openLinkInNewWindow() target");
    return newWindow.getEnclosedPage();
  }
  
  protected boolean isEmptyXmlTagExpanded()
  {
    return true;
  }
  



  public HtmlElement.DisplayStyle getDefaultStyleDisplay()
  {
    return HtmlElement.DisplayStyle.INLINE;
  }
  



  public boolean handles(Event event)
  {
    if (("blur".equals(event.getType())) || ("focus".equals(event.getType()))) {
      return true;
    }
    return super.handles(event);
  }
  




  public final String getPingAttribute()
  {
    return getAttribute("ping");
  }
}
