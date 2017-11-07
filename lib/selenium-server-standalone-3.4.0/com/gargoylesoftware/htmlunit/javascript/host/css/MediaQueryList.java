package com.gargoylesoftware.htmlunit.javascript.host.css;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventTarget;
import com.steadystate.css.dom.MediaListImpl;
import org.w3c.css.sac.ErrorHandler;
import org.w3c.css.sac.SACMediaList;






























@JsxClass
public class MediaQueryList
  extends EventTarget
{
  private String media_;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public MediaQueryList() {}
  
  public MediaQueryList(String mediaQueryString)
  {
    media_ = mediaQueryString;
  }
  



  @JsxGetter
  public String getMedia()
  {
    return media_;
  }
  



  @JsxGetter
  public boolean getMatches()
  {
    ErrorHandler errorHandler = getWindow().getWebWindow().getWebClient().getCssErrorHandler();
    SACMediaList mediaList = CSSStyleSheet.parseMedia(errorHandler, media_);
    return CSSStyleSheet.isActive(this, new MediaListImpl(mediaList));
  }
  
  @JsxFunction
  public void addListener(Object listener) {}
  
  @JsxFunction
  public void removeListener(Object listener) {}
}
