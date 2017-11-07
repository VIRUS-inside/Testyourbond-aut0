package com.gargoylesoftware.htmlunit.activex.javascript.msxml;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import org.w3c.dom.NamedNodeMap;
































@JsxClass(browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
public class XMLDOMNamedNodeMap
  extends MSXMLScriptable
{
  private final NamedNodeMap attributes_;
  private int currentIndex_ = 0;
  


  public XMLDOMNamedNodeMap()
  {
    attributes_ = null;
  }
  




  public XMLDOMNamedNodeMap(DomNode node)
  {
    setParentScope(node.getScriptableObject());
    setPrototype(getPrototype(getClass()));
    
    attributes_ = node.getAttributes();
    setDomNode(node, false);
  }
  





  public final Object get(int index, Scriptable start)
  {
    XMLDOMNamedNodeMap startMap = (XMLDOMNamedNodeMap)start;
    Object response = startMap.item(index);
    if (response != null) {
      return response;
    }
    return NOT_FOUND;
  }
  



  @JsxGetter
  public int getLength()
  {
    return attributes_.getLength();
  }
  








  public Object getNamedItemWithoutSyntheticClassAttr(String name)
  {
    if (attributes_ != null) {
      DomNode attr = (DomNode)attributes_.getNamedItem(name);
      if (attr != null) {
        return attr.getScriptableObject();
      }
    }
    return null;
  }
  




  @JsxFunction
  public Object getNamedItem(String name)
  {
    if ((name == null) || ("null".equals(name))) {
      throw Context.reportRuntimeError("Type mismatch.");
    }
    
    Object attr = getNamedItemWithoutSyntheticClassAttr(name);
    if (attr != null) {
      return attr;
    }
    return null;
  }
  




  @JsxFunction
  public Object item(int index)
  {
    DomNode attr = (DomNode)attributes_.item(index);
    if (attr != null) {
      return attr.getScriptableObject();
    }
    return null;
  }
  



  @JsxFunction
  public Object nextNode()
  {
    return item(currentIndex_++);
  }
  




  @JsxFunction
  public Object removeNamedItem(String name)
  {
    if ((name == null) || ("null".equals(name))) {
      throw Context.reportRuntimeError("Type mismatch.");
    }
    
    DomNode attr = (DomNode)attributes_.removeNamedItem(name);
    if (attr != null) {
      return attr.getScriptableObject();
    }
    return null;
  }
  


  @JsxFunction
  public void reset()
  {
    currentIndex_ = 0;
  }
  




  @JsxFunction
  public Object setNamedItem(XMLDOMNode node)
  {
    if (node == null) {
      throw Context.reportRuntimeError("Type mismatch.");
    }
    
    attributes_.setNamedItem(node.getDomNodeOrDie());
    return node;
  }
}
