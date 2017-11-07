package com.gargoylesoftware.htmlunit.javascript;

import java.lang.reflect.Member;
import net.sourceforge.htmlunit.corejs.javascript.FunctionObject;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;






















class HiddenFunctionObject
  extends FunctionObject
{
  HiddenFunctionObject(String name, Member methodOrConstructor, Scriptable scope)
  {
    super(name, methodOrConstructor, scope);
  }
  
  public boolean avoidObjectDetection()
  {
    return true;
  }
}
