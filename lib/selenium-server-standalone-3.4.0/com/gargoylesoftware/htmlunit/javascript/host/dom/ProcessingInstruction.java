package com.gargoylesoftware.htmlunit.javascript.host.dom;

import com.gargoylesoftware.htmlunit.html.DomProcessingInstruction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;































@JsxClass(domClass=DomProcessingInstruction.class)
public class ProcessingInstruction
  extends CharacterData
{
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public ProcessingInstruction() {}
  
  @JsxGetter
  public String getTarget()
  {
    return ((DomProcessingInstruction)getDomNodeOrDie()).getTarget();
  }
  




  @JsxGetter
  public String getData()
  {
    return ((DomProcessingInstruction)getDomNodeOrDie()).getData();
  }
  




  @JsxSetter
  public void setData(String data)
  {
    ((DomProcessingInstruction)getDomNodeOrDie()).setData(data);
  }
}
