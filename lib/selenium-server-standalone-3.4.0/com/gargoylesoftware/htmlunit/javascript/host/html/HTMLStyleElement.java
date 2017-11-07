package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.Cache;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlStyle;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import java.io.StringReader;
import java.net.URL;
import org.w3c.css.sac.InputSource;






























@JsxClass(domClass=HtmlStyle.class)
public class HTMLStyleElement
  extends HTMLElement
{
  private com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleSheet sheet_;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public HTMLStyleElement() {}
  
  @JsxGetter
  public com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleSheet getSheet()
  {
    if (sheet_ != null) {
      return sheet_;
    }
    
    HtmlStyle style = (HtmlStyle)getDomNodeOrDie();
    String css = style.getTextContent();
    
    Cache cache = getWindow().getWebWindow().getWebClient().getCache();
    org.w3c.dom.css.CSSStyleSheet cached = cache.getCachedStyleSheet(css);
    String uri = getDomNodeOrDie().getPage().getWebResponse().getWebRequest()
      .getUrl().toExternalForm();
    if (cached != null) {
      sheet_ = new com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleSheet(this, cached, uri);
    }
    else {
      InputSource source = new InputSource(new StringReader(css));
      sheet_ = new com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleSheet(this, source, uri);
      cache.cache(css, sheet_.getWrappedSheet());
    }
    
    return sheet_;
  }
  



  @JsxGetter
  public String getType()
  {
    HtmlStyle style = (HtmlStyle)getDomNodeOrDie();
    return style.getTypeAttribute();
  }
  



  @JsxSetter
  public void setType(String type)
  {
    HtmlStyle style = (HtmlStyle)getDomNodeOrDie();
    style.setTypeAttribute(type);
  }
  



  @JsxGetter
  public String getMedia()
  {
    HtmlStyle style = (HtmlStyle)getDomNodeOrDie();
    return style.getAttribute("media");
  }
  



  @JsxSetter
  public void setMedia(String media)
  {
    HtmlStyle style = (HtmlStyle)getDomNodeOrDie();
    style.setAttribute("media", media);
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public boolean getScoped()
  {
    HtmlStyle style = (HtmlStyle)getDomNodeOrDie();
    return style.hasAttribute("scoped");
  }
  



  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public void setScoped(boolean scoped)
  {
    HtmlStyle style = (HtmlStyle)getDomNodeOrDie();
    style.setAttribute("scoped", Boolean.toString(scoped));
  }
  



  @JsxGetter
  public boolean isDisabled()
  {
    return !getSheet().isEnabled();
  }
  




  @JsxSetter
  public void setDisabled(boolean disabled)
  {
    com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleSheet sheet = getSheet();
    boolean modified = disabled == sheet.isEnabled();
    sheet.setEnabled(!disabled);
    if (modified) {
      getWindow().clearComputedStyles();
    }
  }
}
