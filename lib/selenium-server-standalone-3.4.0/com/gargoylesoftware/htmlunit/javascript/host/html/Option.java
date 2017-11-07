package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.host.Window;






















@JsxClass
public class Option
  extends HTMLOptionElement
{
  public Option() {}
  
  @JsxConstructor
  public void jsConstructor(String newText, String newValue, boolean defaultSelected, boolean selected)
  {
    super.jsConstructor(newText, newValue, defaultSelected, selected);
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
    if ((getWindow().getWebWindow() != null) && (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_OPTION_HTML_OPTION_ELEMENT))) {
      return "HTMLOptionElement";
    }
    return super.getClassName();
  }
}
