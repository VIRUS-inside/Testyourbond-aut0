package com.gargoylesoftware.htmlunit.javascript.host.intl;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.javascript.HtmlUnitScriptable;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.RecursiveFunctionObject;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.AbstractJavaScriptConfiguration;
import com.gargoylesoftware.htmlunit.javascript.configuration.ClassConfiguration;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.FunctionObject;






















public class Intl
  extends SimpleScriptable
{
  public Intl() {}
  
  public void defineProperties(BrowserVersion browserVersion)
  {
    setClassName("Object");
    define(Collator.class, browserVersion);
    define(DateTimeFormat.class, browserVersion);
    define(NumberFormat.class, browserVersion);
    if (browserVersion.hasFeature(BrowserVersionFeatures.JS_INTL_V8_BREAK_ITERATOR)) {
      define(V8BreakIterator.class, browserVersion);
    }
  }
  
  private void define(Class<? extends SimpleScriptable> c, BrowserVersion browserVersion) {
    try {
      ClassConfiguration config = AbstractJavaScriptConfiguration.getClassConfiguration(c, browserVersion);
      HtmlUnitScriptable prototype = JavaScriptEngine.configureClass(config, this, browserVersion);
      FunctionObject functionObject = 
        new RecursiveFunctionObject(c.getSimpleName(), config.getJsConstructor(), this);
      if (c == V8BreakIterator.class) {
        prototype.setClassName("v8BreakIterator");
      }
      functionObject.addAsConstructor(this, prototype);
    }
    catch (Exception e) {
      throw Context.throwAsScriptRuntimeEx(e);
    }
  }
}
