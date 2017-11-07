package com.gargoylesoftware.htmlunit.javascript.host.geo;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;


























@JsxClass(browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
public class Coordinates
  extends SimpleScriptable
{
  private double latitude_;
  private double longitude_;
  private double accuracy_;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public Coordinates() {}
  
  Coordinates(double latitude, double longitude, double accuracy)
  {
    latitude_ = latitude;
    longitude_ = longitude;
    accuracy_ = accuracy;
  }
  



  @JsxGetter
  public double getLatitude()
  {
    return latitude_;
  }
  



  @JsxGetter
  public double getLongitude()
  {
    return longitude_;
  }
  



  @JsxGetter
  public double getAccuracy()
  {
    return accuracy_;
  }
}
