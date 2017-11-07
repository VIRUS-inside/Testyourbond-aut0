package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import net.sourceforge.htmlunit.corejs.javascript.Context;






















@JsxClass(isJSObject=false)
public class HTMLListElement
  extends HTMLElement
{
  public HTMLListElement() {}
  
  @JsxGetter
  public boolean getCompact()
  {
    return getDomNodeOrDie().hasAttribute("compact");
  }
  



  @JsxSetter
  public void setCompact(Object compact)
  {
    if (Context.toBoolean(compact)) {
      getDomNodeOrDie().setAttribute("compact", "");
    }
    else {
      getDomNodeOrDie().removeAttribute("compact");
    }
  }
  



  protected String getType()
  {
    boolean acceptArbitraryValues = getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_TYPE_ACCEPTS_ARBITRARY_VALUES);
    
    String type = getDomNodeOrDie().getAttribute("type");
    if ((acceptArbitraryValues) || 
      ("1".equals(type)) || 
      ("a".equals(type)) || 
      ("A".equals(type)) || 
      ("i".equals(type)) || 
      ("I".equals(type))) {
      return type;
    }
    return "";
  }
  



  protected void setType(String type)
  {
    boolean acceptArbitraryValues = getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_TYPE_ACCEPTS_ARBITRARY_VALUES);
    if ((acceptArbitraryValues) || 
      ("1".equals(type)) || 
      ("a".equals(type)) || 
      ("A".equals(type)) || 
      ("i".equals(type)) || 
      ("I".equals(type))) {
      getDomNodeOrDie().setAttribute("type", type);
      return;
    }
    
    throw Context.reportRuntimeError("Cannot set the type property to invalid value: '" + type + "'");
  }
}
