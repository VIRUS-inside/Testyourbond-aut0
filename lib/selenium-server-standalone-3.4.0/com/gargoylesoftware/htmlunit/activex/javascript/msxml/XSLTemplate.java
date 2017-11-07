package com.gargoylesoftware.htmlunit.activex.javascript.msxml;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;


























@JsxClass(browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
public class XSLTemplate
  extends MSXMLScriptable
{
  private XMLDOMNode stylesheet_;
  
  public XSLTemplate() {}
  
  @JsxSetter
  public void setStylesheet(XMLDOMNode node)
  {
    stylesheet_ = node;
  }
  



  @JsxGetter
  public XMLDOMNode getStylesheet()
  {
    return stylesheet_;
  }
  



  @JsxFunction
  public XSLProcessor createProcessor()
  {
    XSLProcessor processor = new XSLProcessor();
    processor.setPrototype(getPrototype(processor.getClass()));
    processor.setParentScope(this);
    processor.importStylesheet(stylesheet_);
    return processor;
  }
}
