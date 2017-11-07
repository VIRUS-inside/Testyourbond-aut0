package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import net.sourceforge.htmlunit.corejs.javascript.Context;



















public class Netscape
  extends SimpleScriptable
{
  Netscape(Window window)
  {
    setParentScope(window);
    

    put("security", this, Context.getCurrentContext().newObject(window));
  }
  
  public String getClassName()
  {
    return "Object";
  }
}
