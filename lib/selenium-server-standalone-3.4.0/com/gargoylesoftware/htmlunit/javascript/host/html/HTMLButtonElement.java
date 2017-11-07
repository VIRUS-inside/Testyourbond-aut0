package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.dom.AbstractList;
import java.util.Locale;






































@JsxClass(domClass=HtmlButton.class)
public class HTMLButtonElement
  extends FormField
{
  private AbstractList labels_;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public HTMLButtonElement() {}
  
  @JsxSetter
  public void setType(String newType)
  {
    getDomNodeOrDie().setAttribute("type", newType);
  }
  



  @JsxGetter
  public String getType()
  {
    String type = ((HtmlButton)getDomNodeOrDie()).getTypeAttribute();
    if (type != null) {
      type = type.toLowerCase(Locale.ROOT);
    }
    if ("reset".equals(type)) {
      return "reset";
    }
    if ("submit".equals(type)) {
      return "submit";
    }
    if ("button".equals(type)) {
      return "button";
    }
    return "submit";
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
  public AbstractList getLabels()
  {
    if (labels_ == null) {
      labels_ = new LabelsHelper(getDomNodeOrDie());
    }
    return labels_;
  }
}
