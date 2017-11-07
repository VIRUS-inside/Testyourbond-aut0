package com.gargoylesoftware.htmlunit;

import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJobManager;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;























public class DialogWindow
  extends WebWindowImpl
{
  private Object arguments_;
  
  protected DialogWindow(WebClient webClient, Object arguments)
  {
    super(webClient);
    arguments_ = arguments;
    performRegistration();
  }
  



  protected boolean isJavaScriptInitializationNeeded()
  {
    return getScriptableObject() == null;
  }
  



  public WebWindow getParentWindow()
  {
    return this;
  }
  



  public WebWindow getTopWindow()
  {
    return this;
  }
  



  public void setScriptableObject(ScriptableObject scriptObject)
  {
    if (scriptObject != null) {
      scriptObject.put("dialogArguments", scriptObject, arguments_);
    }
    super.setScriptableObject(scriptObject);
  }
  


  public void close()
  {
    getJobManager().shutdown();
    destroyChildren();
    getWebClient().deregisterWebWindow(this);
  }
  




  public String toString()
  {
    return "DialogWindow[name=\"" + getName() + "\"]";
  }
}
