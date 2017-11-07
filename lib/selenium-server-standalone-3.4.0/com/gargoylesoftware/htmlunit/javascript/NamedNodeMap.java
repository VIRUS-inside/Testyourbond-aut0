package com.gargoylesoftware.htmlunit.javascript;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Node;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;



































@JsxClass
public class NamedNodeMap
  extends SimpleScriptable
{
  private final org.w3c.dom.NamedNodeMap attributes_;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public NamedNodeMap()
  {
    attributes_ = null;
  }
  




  public NamedNodeMap(DomElement element)
  {
    setParentScope(element.getScriptableObject());
    setPrototype(getPrototype(getClass()));
    
    attributes_ = element.getAttributes();
    setDomNode(element, false);
  }
  





  public final Object get(int index, Scriptable start)
  {
    NamedNodeMap startMap = (NamedNodeMap)start;
    Object response = startMap.item(index);
    if (response != null) {
      return response;
    }
    return NOT_FOUND;
  }
  



  public Object get(String name, Scriptable start)
  {
    Object response = super.get(name, start);
    if (response != NOT_FOUND) {
      return response;
    }
    
    response = getNamedItem(name);
    if (response != null) {
      return response;
    }
    
    return NOT_FOUND;
  }
  








  public Object getNamedItemWithoutSytheticClassAttr(String name)
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
    Object attr = getNamedItemWithoutSytheticClassAttr(name);
    if (attr != null) {
      return attr;
    }
    return null;
  }
  



  @JsxFunction
  public void setNamedItem(Node node)
  {
    attributes_.setNamedItem(node.getDomNodeOrDie());
  }
  



  @JsxFunction
  public void removeNamedItem(String name)
  {
    attributes_.removeNamedItem(name);
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
  



  @JsxGetter
  public int getLength()
  {
    return attributes_.getLength();
  }
  



  public boolean has(int index, Scriptable start)
  {
    return (index >= 0) && (index < getLength());
  }
}
