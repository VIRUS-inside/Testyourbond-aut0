package com.gargoylesoftware.htmlunit.javascript.background;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.gae.GAEUtils;
import java.net.URL;
import net.sourceforge.htmlunit.corejs.javascript.ContextAction;
import net.sourceforge.htmlunit.corejs.javascript.ContextFactory;
import net.sourceforge.htmlunit.corejs.javascript.Function;























public class BackgroundJavaScriptFactory
{
  private static BackgroundJavaScriptFactory Factory_ = new BackgroundJavaScriptFactory();
  




  public static BackgroundJavaScriptFactory theFactory()
  {
    return Factory_;
  }
  




  public static void setFactory(BackgroundJavaScriptFactory factory)
  {
    Factory_ = factory;
  }
  










  public JavaScriptJob createJavaScriptJob(int initialDelay, Integer period, String label, WebWindow window, String script)
  {
    return new JavaScriptStringJob(initialDelay, period, label, window, script);
  }
  











  public JavaScriptFunctionJob createJavaScriptJob(int initialDelay, Integer period, String label, WebWindow window, Function function)
  {
    return new JavaScriptFunctionJob(initialDelay, period, label, window, function);
  }
  







  public JavaScriptJob createJavascriptXMLHttpRequestJob(ContextFactory contextFactory, ContextAction action)
  {
    return new JavascriptXMLHttpRequestJob(contextFactory, action);
  }
  







  public JavaScriptJob createJavaScriptJob(int initialDelay, Integer period, final Runnable runnable)
  {
    new BasicJavaScriptJob(initialDelay, period)
    {
      public void run() {
        runnable.run();
      }
    };
  }
  








  public JavaScriptJob createDownloadBehaviorJob(URL url, Function callback, WebClient client)
  {
    return new DownloadBehaviorJob(url, callback, client);
  }
  




  public JavaScriptExecutor createJavaScriptExecutor(WebClient webClient)
  {
    if (GAEUtils.isGaeMode()) {
      return new GAEJavaScriptExecutor(webClient);
    }
    return new DefaultJavaScriptExecutor(webClient);
  }
  




  public JavaScriptJobManager createJavaScriptJobManager(WebWindow webWindow)
  {
    return new JavaScriptJobManagerImpl(webWindow);
  }
  
  protected BackgroundJavaScriptFactory() {}
}
