package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlMeter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.host.dom.AbstractList;
































@JsxClass(domClass=HtmlMeter.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
public class HTMLMeterElement
  extends HTMLElement
{
  private AbstractList labels_;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public HTMLMeterElement() {}
  
  @JsxGetter
  public double getValue()
  {
    return getAttributeAsDouble("value", 0.0D);
  }
  



  @JsxGetter
  public double getMin()
  {
    return getAttributeAsDouble("min", 0.0D);
  }
  



  @JsxGetter
  public double getMax()
  {
    return getAttributeAsDouble("max", 1.0D);
  }
  



  @JsxGetter
  public double getLow()
  {
    double val = getAttributeAsDouble("low", Double.MAX_VALUE);
    if (val == Double.MAX_VALUE) {
      return getMin();
    }
    return val;
  }
  



  @JsxGetter
  public double getHigh()
  {
    double val = getAttributeAsDouble("high", Double.MIN_VALUE);
    if (val == Double.MIN_VALUE) {
      return getMax();
    }
    return val;
  }
  



  @JsxGetter
  public double getOptimum()
  {
    double val = getAttributeAsDouble("optimum", Double.MAX_VALUE);
    if (val == Double.MAX_VALUE) {
      return getValue();
    }
    return val;
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
