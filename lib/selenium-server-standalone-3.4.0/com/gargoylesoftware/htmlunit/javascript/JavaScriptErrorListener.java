package com.gargoylesoftware.htmlunit.javascript;

import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.net.MalformedURLException;
import java.net.URL;

public abstract interface JavaScriptErrorListener
{
  public abstract void scriptException(HtmlPage paramHtmlPage, ScriptException paramScriptException);
  
  public abstract void timeoutError(HtmlPage paramHtmlPage, long paramLong1, long paramLong2);
  
  public abstract void malformedScriptURL(HtmlPage paramHtmlPage, String paramString, MalformedURLException paramMalformedURLException);
  
  public abstract void loadScriptError(HtmlPage paramHtmlPage, URL paramURL, Exception paramException);
}
