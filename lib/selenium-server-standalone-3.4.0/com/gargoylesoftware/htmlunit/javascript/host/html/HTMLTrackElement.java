package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.html.HtmlTrack;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;





































@JsxClass(domClass=HtmlTrack.class)
public class HTMLTrackElement
  extends HTMLElement
{
  @JsxConstant
  public static final int NONE = 0;
  @JsxConstant
  public static final int LOADING = 1;
  @JsxConstant
  public static final int LOADED = 2;
  @JsxConstant
  public static final int ERROR = 3;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public HTMLTrackElement() {}
  
  protected boolean isEndTagForbidden()
  {
    if (getBrowserVersion().hasFeature(BrowserVersionFeatures.HTMLTRACK_END_TAG_FORBIDDEN)) {
      return true;
    }
    return super.isEndTagForbidden();
  }
}
