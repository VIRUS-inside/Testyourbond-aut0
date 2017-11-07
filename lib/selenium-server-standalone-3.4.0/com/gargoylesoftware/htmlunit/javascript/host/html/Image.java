package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.host.Window;



















@JsxClass
public class Image
  extends HTMLImageElement
{
  public Image() {}
  
  @JsxConstructor
  public void jsConstructor()
  {
    super.jsConstructor();
  }
  



  public Object getDefaultValue(Class<?> hint)
  {
    if ((String.class.equals(hint)) || (hint == null)) {
      return "[object " + getClassName() + "]";
    }
    return super.getDefaultValue(hint);
  }
  



  public String getClassName()
  {
    if ((getWindow().getWebWindow() != null) && (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_IMAGE_HTML_IMAGE_ELEMENT))) {
      return "HTMLImageElement";
    }
    return super.getClassName();
  }
}
