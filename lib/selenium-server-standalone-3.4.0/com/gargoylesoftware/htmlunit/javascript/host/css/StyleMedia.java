package com.gargoylesoftware.htmlunit.javascript.host.css;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClasses;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.steadystate.css.dom.MediaListImpl;
import org.w3c.css.sac.ErrorHandler;
import org.w3c.css.sac.SACMediaList;






























@JsxClasses({@com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(isJSObject=false, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})})
public class StyleMedia
  extends SimpleScriptable
{
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public StyleMedia() {}
  
  @JsxGetter
  public String getType()
  {
    return "screen";
  }
  




  @JsxFunction
  public boolean matchMedium(String media)
  {
    ErrorHandler errorHandler = getWindow().getWebWindow().getWebClient().getCssErrorHandler();
    SACMediaList mediaList = CSSStyleSheet.parseMedia(errorHandler, media);
    return CSSStyleSheet.isActive(this, new MediaListImpl(mediaList));
  }
}
