package com.gargoylesoftware.htmlunit.javascript.background;

import com.gargoylesoftware.htmlunit.WebWindow;
import java.io.Serializable;

public abstract interface JavaScriptExecutor
  extends Runnable, Serializable
{
  public abstract void addWindow(WebWindow paramWebWindow);
  
  public abstract void shutdown();
  
  public abstract int pumpEventLoop(long paramLong);
}
