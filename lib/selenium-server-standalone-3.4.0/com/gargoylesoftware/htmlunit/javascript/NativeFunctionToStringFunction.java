package com.gargoylesoftware.htmlunit.javascript;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import net.sourceforge.htmlunit.corejs.javascript.BaseFunction;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;




























class NativeFunctionToStringFunction
  extends FunctionWrapper
{
  static void installFix(Scriptable window, BrowserVersion browserVersion)
  {
    if (browserVersion.hasFeature(BrowserVersionFeatures.JS_NATIVE_FUNCTION_TOSTRING_NEW_LINE)) {
      ScriptableObject fnPrototype = 
        (ScriptableObject)ScriptableObject.getClassPrototype(window, "Function");
      Function originalToString = (Function)ScriptableObject.getProperty(fnPrototype, "toString");
      Function newToString = new NativeFunctionToStringFunction(originalToString);
      ScriptableObject.putProperty(fnPrototype, "toString", newToString);
    }
    else if (browserVersion.hasFeature(BrowserVersionFeatures.JS_NATIVE_FUNCTION_TOSTRING_COMPACT)) {
      ScriptableObject fnPrototype = 
        (ScriptableObject)ScriptableObject.getClassPrototype(window, "Function");
      Function originalToString = (Function)ScriptableObject.getProperty(fnPrototype, "toString");
      Function newToString = new NativeFunctionToStringFunctionChrome(originalToString);
      ScriptableObject.putProperty(fnPrototype, "toString", newToString);
    }
  }
  
  NativeFunctionToStringFunction(Function wrapped) {
    super(wrapped);
  }
  



  public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args)
  {
    String s = (String)super.call(cx, scope, thisObj, args);
    
    if (((thisObj instanceof BaseFunction)) && (s.indexOf("[native code]") > -1)) {
      String functionName = ((BaseFunction)thisObj).getFunctionName();
      return "\nfunction " + functionName + "() {\n    [native code]\n}\n";
    }
    return s;
  }
  
  static class NativeFunctionToStringFunctionChrome extends FunctionWrapper
  {
    NativeFunctionToStringFunctionChrome(Function wrapped) {
      super();
    }
    



    public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args)
    {
      String s = (String)super.call(cx, scope, thisObj, args);
      
      if (((thisObj instanceof BaseFunction)) && (s.indexOf("[native code]") > -1)) {
        String functionName = ((BaseFunction)thisObj).getFunctionName();
        return "function " + functionName + "() { [native code] }";
      }
      return s;
    }
  }
}
