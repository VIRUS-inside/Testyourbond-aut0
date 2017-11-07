package com.gargoylesoftware.htmlunit.javascript.host.dom;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.host.xml.XMLDocument;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;






































@JsxClass
public class DOMParser
  extends SimpleScriptable
{
  @JsxConstructor
  public DOMParser() {}
  
  @JsxFunction
  public XMLDocument parseFromString(String str, Object type)
  {
    if ((type == null) || (Undefined.instance == type)) {
      throw Context.reportRuntimeError("Missing 'type' parameter");
    }
    if ((!"text/html".equals(type)) && (!"text/xml".equals(type)) && (!"application/xml".equals(type)) && 
      (!"application/xhtml+xml".equals(type)) && (!"image/svg+xml".equals(type))) {
      throw Context.reportRuntimeError("Invalid 'type' parameter: " + type);
    }
    
    XMLDocument document = new XMLDocument();
    document.setParentScope(getParentScope());
    document.setPrototype(getPrototype(XMLDocument.class));
    document.loadXML(str);
    return document;
  }
}
