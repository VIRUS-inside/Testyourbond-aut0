package com.gargoylesoftware.htmlunit.activex.javascript.msxml;

import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import org.apache.commons.lang3.StringUtils;


































@JsxClass(domClass=DomElement.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
public class XMLDOMElement
  extends XMLDOMNode
{
  private XMLDOMNamedNodeMap attributes_;
  private Map<String, XMLDOMNodeList> elementsByTagName_;
  
  public XMLDOMElement() {}
  
  public Object getAttributes()
  {
    if (attributes_ == null) {
      attributes_ = createAttributesObject();
    }
    return attributes_;
  }
  



  protected XMLDOMNamedNodeMap createAttributesObject()
  {
    return new XMLDOMNamedNodeMap(getDomNodeOrDie());
  }
  




  public void setNodeValue(String newValue)
  {
    if ((newValue == null) || ("null".equals(newValue))) {
      throw Context.reportRuntimeError("Type mismatch.");
    }
    throw Context.reportRuntimeError("This operation cannot be performed with a node of type ELEMENT.");
  }
  



  @JsxGetter
  public String getTagName()
  {
    return getNodeName();
  }
  





  public String getText()
  {
    StringBuilder builder = new StringBuilder();
    toText(getDomNodeOrDie(), builder);
    if ((builder.length() > 0) && (builder.charAt(builder.length() - 1) == '\n')) {
      return builder.substring(0, builder.length() - 1);
    }
    return builder.toString();
  }
  




  public void setText(Object value)
  {
    if ((value == null) || ("null".equals(value))) {
      throw Context.reportRuntimeError("Type mismatch.");
    }
    
    super.setText(value);
  }
  
  private void toText(DomNode node, StringBuilder builder) {
    switch (node.getNodeType()) {
    case 10: 
    case 12: 
      return;
    case 3: 
    case 4: 
    case 7: 
    case 8: 
      builder.append(node.getNodeValue());
    }
    
    
    boolean lastWasElement = false;
    for (DomNode child : node.getChildren()) {
      switch (child.getNodeType()) {
      case 1: 
        lastWasElement = true;
        toText(child, builder);
        break;
      
      case 3: 
      case 4: 
        if (StringUtils.isBlank(child.getNodeValue())) {
          if (lastWasElement) {
            builder.append(' ');
          }
          lastWasElement = false;
        }
        else {
          lastWasElement = false;
          builder.append(child.getNodeValue()); }
        break;
      case 2: default: 
        lastWasElement = false;
      }
      
    }
  }
  




  @JsxFunction
  public Object getAttribute(String name)
  {
    if ((name == null) || ("null".equals(name))) {
      throw Context.reportRuntimeError("Type mismatch.");
    }
    if (StringUtils.isEmpty(name)) {
      throw Context.reportRuntimeError("The empty string '' is not a valid name.");
    }
    
    String value = getDomNodeOrDie().getAttribute(name);
    if (value == DomElement.ATTRIBUTE_NOT_DEFINED) {
      return null;
    }
    return value;
  }
  





  @JsxFunction
  public Object getAttributeNode(String name)
  {
    if ((name == null) || ("null".equals(name))) {
      throw Context.reportRuntimeError("Type mismatch.");
    }
    if (StringUtils.isEmpty(name)) {
      throw Context.reportRuntimeError("The empty string '' is not a valid name.");
    }
    
    Map<String, DomAttr> attributes = getDomNodeOrDie().getAttributesMap();
    for (DomAttr attr : attributes.values()) {
      if (attr.getName().equals(name)) {
        return attr.getScriptableObject();
      }
    }
    return null;
  }
  



  @JsxFunction
  public void removeAttribute(String name)
  {
    if ((name == null) || ("null".equals(name))) {
      throw Context.reportRuntimeError("Type mismatch.");
    }
    if (StringUtils.isEmpty(name)) {
      throw Context.reportRuntimeError("The empty string '' is not a valid name.");
    }
    
    getDomNodeOrDie().removeAttribute(name);
    delete(name);
  }
  




  @JsxFunction
  public XMLDOMAttribute removeAttributeNode(XMLDOMAttribute att)
  {
    if (att == null) {
      throw Context.reportRuntimeError("Type mismatch.");
    }
    
    String name = att.getName();
    
    XMLDOMNamedNodeMap nodes = (XMLDOMNamedNodeMap)getAttributes();
    XMLDOMAttribute removedAtt = (XMLDOMAttribute)nodes.getNamedItemWithoutSyntheticClassAttr(name);
    if (removedAtt != null) {
      removedAtt.detachFromParent();
    }
    removeAttribute(name);
    
    return removedAtt;
  }
  





  @JsxFunction
  public void setAttribute(String name, String value)
  {
    if ((name == null) || ("null".equals(name))) {
      throw Context.reportRuntimeError("Type mismatch.");
    }
    if (StringUtils.isEmpty(name)) {
      throw Context.reportRuntimeError("The empty string '' is not a valid name.");
    }
    if ((value == null) || ("null".equals(value))) {
      throw Context.reportRuntimeError("Type mismatch.");
    }
    
    getDomNodeOrDie().setAttribute(name, value);
  }
  




  @JsxFunction
  public XMLDOMAttribute setAttributeNode(XMLDOMAttribute newAtt)
  {
    if (newAtt == null) {
      throw Context.reportRuntimeError("Type mismatch.");
    }
    
    String name = newAtt.getBaseName();
    
    XMLDOMNamedNodeMap nodes = (XMLDOMNamedNodeMap)getAttributes();
    XMLDOMAttribute replacedAtt = (XMLDOMAttribute)nodes.getNamedItemWithoutSyntheticClassAttr(name);
    if (replacedAtt != null) {
      replacedAtt.detachFromParent();
    }
    
    DomAttr newDomAttr = newAtt.getDomNodeOrDie();
    getDomNodeOrDie().setAttributeNode(newDomAttr);
    return replacedAtt;
  }
  





  @JsxFunction
  public XMLDOMNodeList getElementsByTagName(String tagName)
  {
    if ((tagName == null) || ("null".equals(tagName))) {
      throw Context.reportRuntimeError("Type mismatch.");
    }
    
    final String tagNameTrimmed = tagName.trim();
    
    if (elementsByTagName_ == null) {
      elementsByTagName_ = new HashMap();
    }
    
    XMLDOMNodeList collection = (XMLDOMNodeList)elementsByTagName_.get(tagNameTrimmed);
    if (collection != null) {
      return collection;
    }
    
    DomNode node = getDomNodeOrDie();
    String description = "XMLDOMElement.getElementsByTagName('" + tagNameTrimmed + "')";
    if ("*".equals(tagNameTrimmed)) {
      collection = new XMLDOMNodeList(node, false, description)
      {
        protected boolean isMatching(DomNode domNode) {
          return true;
        }
        
      };
    } else if ("".equals(tagNameTrimmed)) {
      collection = new XMLDOMNodeList(node, false, description)
      {
        protected List<DomNode> computeElements() {
          List<DomNode> response = new ArrayList();
          DomNode domNode = getDomNodeOrNull();
          if (domNode == null) {
            return response;
          }
          for (DomNode candidate : getCandidates()) {
            if ((candidate instanceof DomText)) {
              DomText domText = (DomText)candidate;
              if (!StringUtils.isBlank(domText.getWholeText())) {
                response.add(candidate);
              }
            }
            else {
              response.add(candidate);
            }
          }
          return response;
        }
        
      };
    } else {
      collection = new XMLDOMNodeList(node, false, description)
      {
        protected boolean isMatching(DomNode domNode) {
          return tagNameTrimmed.equals(domNode.getNodeName());
        }
      };
    }
    
    elementsByTagName_.put(tagName, collection);
    
    return collection;
  }
  


  @JsxFunction
  public void normalize()
  {
    DomElement domElement = getDomNodeOrDie();
    
    domElement.normalize();
    
    normalize(domElement);
  }
  
  private void normalize(DomElement domElement) {
    for (DomNode domNode : domElement.getChildren()) {
      if ((domNode instanceof DomElement)) {
        domNode.normalize();
        normalize((DomElement)domNode);
      }
    }
  }
  



  public DomElement getDomNodeOrDie()
  {
    return (DomElement)super.getDomNodeOrDie();
  }
}
