package com.gargoylesoftware.htmlunit.activex.javascript.msxml;

import com.gargoylesoftware.htmlunit.html.DomProcessingInstruction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import net.sourceforge.htmlunit.corejs.javascript.Context;






























@JsxClass(domClass=DomProcessingInstruction.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
public final class XMLDOMProcessingInstruction
  extends XMLDOMNode
{
  private static final String XML_DECLARATION_TARGET = "xml";
  private boolean attributesComputed_;
  private XMLDOMNamedNodeMap attributes_;
  
  public XMLDOMProcessingInstruction() {}
  
  public Object getAttributes()
  {
    if (!attributesComputed_) {
      DomProcessingInstruction domProcessingInstruction = getDomNodeOrDie();
      if ("xml".equalsIgnoreCase(domProcessingInstruction.getTarget())) {
        attributes_ = new XMLDOMNamedNodeMap(getDomNodeOrDie());
      }
      attributesComputed_ = true;
    }
    return attributes_;
  }
  



  public String getBaseName()
  {
    return getTarget();
  }
  



  @JsxGetter
  public String getData()
  {
    DomProcessingInstruction domProcessingInstruction = getDomNodeOrDie();
    return domProcessingInstruction.getData();
  }
  



  @JsxSetter
  public void setData(String data)
  {
    if ((data == null) || ("null".equals(data))) {
      throw Context.reportRuntimeError("Type mismatch.");
    }
    
    DomProcessingInstruction domProcessingInstruction = getDomNodeOrDie();
    if ("xml".equalsIgnoreCase(domProcessingInstruction.getTarget())) {
      throw Context.reportRuntimeError("This operation cannot be performed with a node of type XMLDECL.");
    }
    domProcessingInstruction.setData(data);
  }
  




  public void setNodeValue(String newValue)
  {
    DomProcessingInstruction domProcessingInstruction = getDomNodeOrDie();
    if ("xml".equalsIgnoreCase(domProcessingInstruction.getTarget())) {
      throw Context.reportRuntimeError("This operation cannot be performed with a node of type XMLDECL.");
    }
    
    super.setNodeValue(newValue);
  }
  



  @JsxGetter
  public String getTarget()
  {
    DomProcessingInstruction domProcessingInstruction = getDomNodeOrDie();
    return domProcessingInstruction.getTarget();
  }
  




  public void setText(Object newText)
  {
    setData(newText == null ? null : Context.toString(newText));
  }
  



  public DomProcessingInstruction getDomNodeOrDie()
  {
    return (DomProcessingInstruction)super.getDomNodeOrDie();
  }
}
