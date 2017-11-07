package com.gargoylesoftware.htmlunit.activex.javascript.msxml;

import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Node;
import net.sourceforge.htmlunit.corejs.javascript.Context;



































@JsxClass(domClass=DomAttr.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
public class XMLDOMAttribute
  extends XMLDOMNode
{
  private XMLDOMText textNode_;
  
  public XMLDOMAttribute() {}
  
  public XMLDOMNodeList getChildNodes()
  {
    initTextNode();
    
    return super.getChildNodes();
  }
  




  public XMLDOMNode getFirstChild()
  {
    return getLastChild();
  }
  




  public XMLDOMNode getLastChild()
  {
    initTextNode();
    
    return textNode_;
  }
  
  private void initTextNode() {
    if (textNode_ == null) {
      String value = getValue();
      if (!org.apache.commons.lang3.StringUtils.isEmpty(value)) {
        DomText text = new DomText(getDomNodeOrDie().getPage(), value);
        getDomNodeOrDie().appendChild(text);
        textNode_ = ((XMLDOMText)text.getScriptableObject());
      }
    }
  }
  



  @JsxGetter
  public String getName()
  {
    return getDomNodeOrDie().getName();
  }
  




  public String getNodeValue()
  {
    return getValue();
  }
  




  public void setNodeValue(String value)
  {
    setValue(value);
  }
  




  public Node getParentNode()
  {
    return null;
  }
  




  @JsxGetter
  public boolean getSpecified()
  {
    return getDomNodeOrDie().getSpecified();
  }
  




  public Object getText()
  {
    return getValue();
  }
  




  public void setText(Object value)
  {
    setValue(value == null ? null : Context.toString(value));
  }
  



  @JsxGetter
  public String getValue()
  {
    return getDomNodeOrDie().getValue();
  }
  



  @JsxSetter
  public void setValue(String value)
  {
    getDomNodeOrDie().setValue(value);
    
    resetTextNode();
  }
  
  private void resetTextNode() {
    if (textNode_ != null) {
      getDomNodeOrDie().removeChild(textNode_.getDomNodeOrNull());
      textNode_ = null;
    }
  }
  



  public String getXml()
  {
    StringBuilder sb = new StringBuilder(getName());
    sb.append('=').append('"');
    sb.append(com.gargoylesoftware.htmlunit.util.StringUtils.escapeXmlAttributeValue(getValue()));
    sb.append('"');
    return sb.toString();
  }
  


  public void detachFromParent()
  {
    DomAttr domNode = getDomNodeOrDie();
    DomElement parent = (DomElement)domNode.getParentNode();
    if (parent != null) {
      domNode.setValue(parent.getAttribute(getName()));
    }
    domNode.remove();
  }
  



  public DomAttr getDomNodeOrDie()
  {
    return (DomAttr)super.getDomNodeOrDie();
  }
}
