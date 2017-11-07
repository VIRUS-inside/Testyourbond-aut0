package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClasses;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
























@JsxClasses({@com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlBackgroundSound.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(isJSObject=false, domClass=com.gargoylesoftware.htmlunit.html.HtmlBackgroundSound.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})})
public class HTMLBGSoundElement
  extends HTMLElement
{
  public HTMLBGSoundElement() {}
  
  public String getClassName()
  {
    if ((getWindow().getWebWindow() != null) && 
      (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_BGSOUND_AS_UNKNOWN))) {
      return "HTMLUnknownElement";
    }
    
    return super.getClassName();
  }
  



  protected boolean isEndTagForbidden()
  {
    return true;
  }
}
