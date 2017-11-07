package com.gargoylesoftware.htmlunit.javascript.host.dom;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import java.net.URL;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;




































@JsxClass(domClass=DomAttr.class)
public class Attr
  extends Node
{
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public Attr() {}
  
  public void detachFromParent()
  {
    DomAttr domNode = getDomNodeOrDie();
    DomElement parent = (DomElement)domNode.getParentNode();
    if (parent != null) {
      domNode.setValue(parent.getAttribute(getName()));
    }
    domNode.remove();
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(value=com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF, maxVersion=23)})
  public boolean getIsId()
  {
    return getDomNodeOrDie().isId();
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public boolean getExpando()
  {
    Object owner = getOwnerElement();
    if (owner == null) {
      return false;
    }
    return !ScriptableObject.hasProperty((Scriptable)owner, getName());
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
  



  @JsxGetter
  public Object getOwnerElement()
  {
    DomElement parent = getDomNodeOrDie().getOwnerElement();
    if (parent != null) {
      return parent.getScriptableObject();
    }
    return null;
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
  



  @JsxGetter
  public String getValue()
  {
    return getDomNodeOrDie().getValue();
  }
  



  @JsxSetter
  public void setValue(String value)
  {
    getDomNodeOrDie().setValue(value);
  }
  



  public Node getFirstChild()
  {
    return getLastChild();
  }
  



  public Node getLastChild()
  {
    if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_ATTR_FIRST_LAST_CHILD_RETURNS_NULL)) {
      return null;
    }
    
    DomText text = new DomText(getDomNodeOrDie().getPage(), getNodeValue());
    return (Node)text.getScriptableObject();
  }
  



  public DomAttr getDomNodeOrDie()
  {
    return (DomAttr)super.getDomNodeOrDie();
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
  public String getBaseURI()
  {
    return getDomNodeOrDie().getPage().getUrl().toExternalForm();
  }
}
