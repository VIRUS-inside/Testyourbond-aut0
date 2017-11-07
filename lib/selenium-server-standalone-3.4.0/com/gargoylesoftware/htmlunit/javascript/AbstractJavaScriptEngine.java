package com.gargoylesoftware.htmlunit.javascript;

import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.configuration.JavaScriptConfiguration;

public abstract interface AbstractJavaScriptEngine
{
  public abstract JavaScriptConfiguration getJavaScriptConfiguration();
  
  public abstract void addPostponedAction(PostponedAction paramPostponedAction);
  
  public abstract void processPostponedActions();
  
  public abstract Object execute(HtmlPage paramHtmlPage, String paramString1, String paramString2, int paramInt);
  
  public abstract void registerWindowAndMaybeStartEventLoop(WebWindow paramWebWindow);
  
  public abstract void initialize(WebWindow paramWebWindow);
  
  public abstract void setJavaScriptTimeout(long paramLong);
  
  public abstract long getJavaScriptTimeout();
  
  public abstract void shutdown();
  
  public abstract boolean isScriptRunning();
}
