package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptableProxy;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;























public class WindowProxy
  extends SimpleScriptableProxy<Window>
{
  private final WebWindow webWindow_;
  
  public WindowProxy(WebWindow webWindow)
  {
    webWindow_ = webWindow;
  }
  



  public Window getDelegee()
  {
    return (Window)webWindow_.getScriptableObject();
  }
  
  public void setParentScope(Scriptable parent) {}
}
