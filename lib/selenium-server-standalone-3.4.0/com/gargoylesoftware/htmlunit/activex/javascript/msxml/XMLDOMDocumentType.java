package com.gargoylesoftware.htmlunit.activex.javascript.msxml;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.DomDocumentType;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import org.w3c.dom.NamedNodeMap;




























@JsxClass(domClass=DomDocumentType.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
public class XMLDOMDocumentType
  extends XMLDOMNode
{
  private XMLDOMNamedNodeMap attributes_;
  
  public XMLDOMDocumentType() {}
  
  public Object getAttributes()
  {
    if (attributes_ == null) {
      attributes_ = new XMLDOMNamedNodeMap(getDomNodeOrDie());
    }
    return attributes_;
  }
  



  public String getBaseName()
  {
    return getName();
  }
  



  @JsxGetter
  public Object getEntities()
  {
    DomDocumentType domDocumentType = getDomNodeOrDie();
    NamedNodeMap entities = domDocumentType.getEntities();
    if (entities != null) {
      return entities;
    }
    
    return "";
  }
  



  @JsxGetter
  public String getName()
  {
    DomDocumentType domDocumentType = getDomNodeOrDie();
    return domDocumentType.getName();
  }
  



  public String getNodeName()
  {
    return getName();
  }
  




  public void setNodeValue(String newValue)
  {
    if ((newValue == null) || ("null".equals(newValue))) {
      throw Context.reportRuntimeError("Type mismatch.");
    }
    throw Context.reportRuntimeError("This operation cannot be performed with a node of type DTD.");
  }
  



  @JsxGetter
  public Object getNotations()
  {
    DomDocumentType domDocumentType = getDomNodeOrDie();
    NamedNodeMap notations = domDocumentType.getNotations();
    if (notations != null) {
      return notations;
    }
    
    return "";
  }
  



  public Object getParentNode()
  {
    DomDocumentType domDocumentType = getDomNodeOrDie();
    return domDocumentType.getPage().getScriptableObject();
  }
  




  public Object getText()
  {
    return "";
  }
  




  public void setText(Object value)
  {
    if ((value == null) || ("null".equals(value))) {
      throw Context.reportRuntimeError("Type mismatch.");
    }
    throw Context.reportRuntimeError("This operation cannot be performed with a node of type DTD.");
  }
  



  public Object getXml()
  {
    return "<!DOCTYPE " + getName() + " [  ]>";
  }
  



  public DomDocumentType getDomNodeOrDie()
  {
    return (DomDocumentType)super.getDomNodeOrDie();
  }
}
