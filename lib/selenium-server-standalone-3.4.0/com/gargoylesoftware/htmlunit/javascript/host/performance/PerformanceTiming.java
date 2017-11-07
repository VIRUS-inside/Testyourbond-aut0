package com.gargoylesoftware.htmlunit.javascript.host.performance;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;






























@JsxClass
public class PerformanceTiming
  extends SimpleScriptable
{
  private final long domainLookupStart_;
  private final long domainLookupEnd_;
  private final long connectStart_;
  private final long connectEnd_;
  private final long responseStart_;
  private final long responseEnd_;
  private final long domContentLoadedEventStart_;
  private final long domContentLoadedEventEnd_;
  private final long domLoading_;
  private final long domInteractive_;
  private final long domComplete_;
  private final long loadEventStart_;
  private final long loadEventEnd_;
  private final long navigationStart_;
  private final long fetchStart_;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public PerformanceTiming()
  {
    long now = System.currentTimeMillis();
    

    domainLookupStart_ = now;
    domainLookupEnd_ = (domainLookupStart_ + 1L);
    
    connectStart_ = domainLookupEnd_;
    connectEnd_ = (connectStart_ + 1L);
    
    responseStart_ = connectEnd_;
    responseEnd_ = (responseStart_ + 1L);
    
    loadEventStart_ = responseEnd_;
    loadEventEnd_ = (loadEventStart_ + 1L);
    domLoading_ = responseEnd_;
    domInteractive_ = responseEnd_;
    domContentLoadedEventStart_ = responseEnd_;
    domContentLoadedEventEnd_ = (domContentLoadedEventStart_ + 1L);
    domComplete_ = domContentLoadedEventEnd_;
    
    navigationStart_ = now;
    fetchStart_ = now;
  }
  


  @JsxGetter
  public long getDomainLookupStart()
  {
    return domainLookupStart_;
  }
  


  @JsxGetter
  public long getDomainLookupEnd()
  {
    return domainLookupEnd_;
  }
  


  @JsxGetter
  public long getConnectStart()
  {
    return connectStart_;
  }
  


  @JsxGetter
  public long getConnectEnd()
  {
    return connectEnd_;
  }
  


  @JsxGetter
  public long getResponseStart()
  {
    return responseStart_;
  }
  


  @JsxGetter
  public long getResponseEnd()
  {
    return responseEnd_;
  }
  


  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
  public long getSecureConnectionStart()
  {
    return 0L;
  }
  


  @JsxGetter
  public long getUnloadEventStart()
  {
    return 0L;
  }
  


  @JsxGetter
  public long getUnloadEventEnd()
  {
    return 0L;
  }
  


  @JsxGetter
  public long getRedirectStart()
  {
    return 0L;
  }
  


  @JsxGetter
  public long getRedirectEnd()
  {
    return 0L;
  }
  


  @JsxGetter
  public long getDomContentLoadedEventStart()
  {
    return domContentLoadedEventStart_;
  }
  


  @JsxGetter
  public long getDomLoading()
  {
    return domLoading_;
  }
  


  @JsxGetter
  public long getDomInteractive()
  {
    return domInteractive_;
  }
  


  @JsxGetter
  public long getDomContentLoadedEventEnd()
  {
    return domContentLoadedEventEnd_;
  }
  


  @JsxGetter
  public long getDomComplete()
  {
    return domComplete_;
  }
  


  @JsxGetter
  public long getLoadEventStart()
  {
    return loadEventStart_;
  }
  


  @JsxGetter
  public long getLoadEventEnd()
  {
    return loadEventEnd_;
  }
  


  @JsxGetter
  public long getNavigationStart()
  {
    return navigationStart_;
  }
  


  @JsxGetter
  public long getFetchStart()
  {
    return fetchStart_;
  }
}
