package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;






























@JsxClass(browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
public class Enumerator
  extends SimpleScriptable
{
  private int index_;
  private HTMLCollection collection_;
  
  public Enumerator() {}
  
  @JsxConstructor
  public void jsConstructor(Object o)
  {
    if (Undefined.instance == o) {
      collection_ = HTMLCollection.emptyCollection(getWindow().getDomNodeOrDie());
    } else {
      if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_ENUMERATOR_CONSTRUCTOR_THROWS)) {
        throw Context.reportRuntimeError("TypeError: object is not enumerable");
      }
      if ((o instanceof HTMLCollection)) {
        collection_ = ((HTMLCollection)o);
      }
      else if ((o instanceof HTMLFormElement)) {
        collection_ = ((HTMLFormElement)o).getElements();
      }
      else {
        throw Context.reportRuntimeError("TypeError: object is not enumerable (" + String.valueOf(o) + ")");
      }
    }
  }
  


  @JsxFunction
  public boolean atEnd()
  {
    return index_ >= collection_.getLength();
  }
  



  @JsxFunction
  public Object item()
  {
    if (!atEnd()) {
      SimpleScriptable scriptable = (SimpleScriptable)collection_.get(index_, collection_);
      scriptable = scriptable.clone();
      scriptable.setCaseSensitive(false);
      return scriptable;
    }
    return Undefined.instance;
  }
  


  @JsxFunction
  public void moveFirst()
  {
    index_ = 0;
  }
  


  @JsxFunction
  public void moveNext()
  {
    index_ += 1;
  }
}
