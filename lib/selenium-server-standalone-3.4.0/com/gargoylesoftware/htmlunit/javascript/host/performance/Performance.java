package com.gargoylesoftware.htmlunit.javascript.host.performance;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventTarget;































@JsxClass
public class Performance
  extends EventTarget
{
  private PerformanceTiming timing_;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public Performance() {}
  
  @JsxGetter
  public PerformanceNavigation getNavigation()
  {
    PerformanceNavigation navigation = new PerformanceNavigation();
    navigation.setParentScope(getParentScope());
    navigation.setPrototype(getPrototype(navigation.getClass()));
    return navigation;
  }
  



  @JsxGetter
  public PerformanceTiming getTiming()
  {
    if (timing_ == null) {
      PerformanceTiming timing = new PerformanceTiming();
      timing.setParentScope(getParentScope());
      timing.setPrototype(getPrototype(timing.getClass()));
      timing_ = timing;
    }
    
    return timing_;
  }
  


  @JsxFunction
  public double now()
  {
    return System.nanoTime() / 1000000.0D;
  }
}
