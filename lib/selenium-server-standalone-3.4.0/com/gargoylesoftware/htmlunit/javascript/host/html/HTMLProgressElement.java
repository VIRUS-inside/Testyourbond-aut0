package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlProgress;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.host.dom.AbstractList;


































@JsxClass(domClass=HtmlProgress.class)
public class HTMLProgressElement
  extends HTMLElement
{
  private AbstractList labels_;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public HTMLProgressElement() {}
  
  @JsxGetter
  public double getValue()
  {
    return getAttributeAsDouble("value", 0.0D);
  }
  



  @JsxGetter
  public double getMax()
  {
    return getAttributeAsDouble("max", 1.0D);
  }
  
  private double getAttributeAsDouble(String attributeName, double defaultValue) {
    try {
      return Double.parseDouble(getDomNodeOrDie().getAttribute(attributeName));
    }
    catch (NumberFormatException e) {}
    return defaultValue;
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
