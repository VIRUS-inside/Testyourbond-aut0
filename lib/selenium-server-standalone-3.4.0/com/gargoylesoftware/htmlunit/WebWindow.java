package com.gargoylesoftware.htmlunit;

import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJobManager;
import java.io.Serializable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

public abstract interface WebWindow
  extends Serializable
{
  public abstract String getName();
  
  public abstract void setName(String paramString);
  
  public abstract Page getEnclosedPage();
  
  public abstract void setEnclosedPage(Page paramPage);
  
  public abstract WebWindow getParentWindow();
  
  public abstract WebWindow getTopWindow();
  
  public abstract WebClient getWebClient();
  
  public abstract History getHistory();
  
  public abstract void setScriptableObject(ScriptableObject paramScriptableObject);
  
  public abstract ScriptableObject getScriptableObject();
  
  public abstract JavaScriptJobManager getJobManager();
  
  public abstract boolean isClosed();
  
  public abstract int getInnerWidth();
  
  public abstract void setInnerWidth(int paramInt);
  
  public abstract int getOuterWidth();
  
  public abstract void setOuterWidth(int paramInt);
  
  public abstract int getInnerHeight();
  
  public abstract void setInnerHeight(int paramInt);
  
  public abstract int getOuterHeight();
  
  public abstract void setOuterHeight(int paramInt);
}
