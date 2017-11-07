package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.SgmlPage;
import org.w3c.dom.Attr;
import org.w3c.dom.TypeInfo;
































public class DomAttr
  extends DomNamespaceNode
  implements Attr
{
  private String value_;
  private boolean specified_;
  
  public DomAttr(SgmlPage page, String namespaceURI, String qualifiedName, String value, boolean specified)
  {
    super(namespaceURI, qualifiedName, page);
    
    if ((value != null) && (value.isEmpty())) {
      value_ = DomElement.ATTRIBUTE_VALUE_EMPTY;
    }
    else {
      value_ = value;
    }
    
    specified_ = specified;
  }
  



  public short getNodeType()
  {
    return 2;
  }
  



  public String getNodeName()
  {
    return getName();
  }
  



  public String getNodeValue()
  {
    return getValue();
  }
  



  public String getName()
  {
    return getQualifiedName();
  }
  



  public String getValue()
  {
    return value_;
  }
  



  public void setNodeValue(String value)
  {
    setValue(value);
  }
  



  public void setValue(String value)
  {
    value_ = value;
    specified_ = true;
  }
  



  public DomElement getOwnerElement()
  {
    return (DomElement)getParentNode();
  }
  



  public boolean getSpecified()
  {
    return specified_;
  }
  




  public TypeInfo getSchemaTypeInfo()
  {
    throw new UnsupportedOperationException("DomAttr.getSchemaTypeInfo is not yet implemented.");
  }
  



  public boolean isId()
  {
    return "id".equals(getNodeName());
  }
  



  public String toString()
  {
    return getClass().getSimpleName() + "[name=" + getNodeName() + " value=" + getNodeValue() + "]";
  }
  



  public String getCanonicalXPath()
  {
    return getParentNode().getCanonicalXPath() + "/@" + getName();
  }
  



  public String getTextContent()
  {
    return getNodeValue();
  }
  



  public void setTextContent(String textContent)
  {
    boolean mappedElement = HtmlPage.isMappedElement(getOwnerDocument(), getName());
    if (mappedElement) {
      ((HtmlPage)getPage()).removeMappedElement((HtmlElement)getOwnerElement());
    }
    setValue(textContent);
    if (mappedElement) {
      ((HtmlPage)getPage()).addMappedElement(getOwnerElement());
    }
  }
}
