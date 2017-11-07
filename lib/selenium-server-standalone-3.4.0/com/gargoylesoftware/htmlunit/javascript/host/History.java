package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import org.apache.commons.lang3.StringUtils;







































@JsxClass
public class History
  extends SimpleScriptable
{
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public History() {}
  
  @JsxGetter
  public int getLength()
  {
    WebWindow w = getWindow().getWebWindow();
    return w.getHistory().getLength();
  }
  



  @JsxGetter
  public Object getState()
  {
    WebWindow w = getWindow().getWebWindow();
    return w.getHistory().getCurrentState();
  }
  

  @JsxFunction
  public void back()
  {
    try
    {
      getWindow().getWebWindow().getHistory().back();
    }
    catch (IOException e) {
      Context.throwAsScriptRuntimeEx(e);
    }
  }
  

  @JsxFunction
  public void forward()
  {
    try
    {
      getWindow().getWebWindow().getHistory().forward();
    }
    catch (IOException e) {
      Context.throwAsScriptRuntimeEx(e);
    }
  }
  


  @JsxFunction
  public void go(int relativeIndex)
  {
    try
    {
      getWindow().getWebWindow().getHistory().go(relativeIndex);
    }
    catch (IOException e) {
      Context.throwAsScriptRuntimeEx(e);
    }
  }
  





  @JsxFunction
  public void replaceState(Object object, String title, String url)
  {
    WebWindow w = getWindow().getWebWindow();
    try {
      URL newStateUrl = null;
      HtmlPage page = (HtmlPage)w.getEnclosedPage();
      if (StringUtils.isNotBlank(url)) {
        newStateUrl = page.getFullyQualifiedUrl(url);
      }
      w.getHistory().replaceState(object, newStateUrl);
    }
    catch (MalformedURLException e) {
      Context.throwAsScriptRuntimeEx(e);
    }
  }
  





  @JsxFunction
  public void pushState(Object object, String title, String url)
  {
    WebWindow w = getWindow().getWebWindow();
    try {
      URL newStateUrl = null;
      HtmlPage page = (HtmlPage)w.getEnclosedPage();
      if (StringUtils.isNotBlank(url)) {
        newStateUrl = page.getFullyQualifiedUrl(url);
      }
      w.getHistory().pushState(object, newStateUrl);
    }
    catch (IOException e) {
      Context.throwAsScriptRuntimeEx(e);
    }
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
  public String getScrollRestoration()
  {
    return "auto";
  }
}
