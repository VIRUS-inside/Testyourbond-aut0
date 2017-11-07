package com.gargoylesoftware.htmlunit.javascript.host.dom;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClasses;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.xml.XmlUtil;
import org.apache.xml.utils.PrefixResolver;

































@JsxClasses({@com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(isJSObject=false, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})})
public class XPathNSResolver
  extends SimpleScriptable
  implements PrefixResolver
{
  private Object element_;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public XPathNSResolver() {}
  
  public void setElement(Node element)
  {
    element_ = element;
  }
  




  @JsxFunction
  public String lookupNamespaceURI(String prefix)
  {
    return XmlUtil.lookupNamespaceURI((DomElement)((SimpleScriptable)element_).getDomNodeOrDie(), prefix);
  }
  



  public String getBaseIdentifier()
  {
    return XmlUtil.lookupNamespaceURI((DomElement)((SimpleScriptable)element_).getDomNodeOrDie(), "");
  }
  



  public String getNamespaceForPrefix(String prefix)
  {
    return lookupNamespaceURI(prefix);
  }
  



  public String getNamespaceForPrefix(String prefix, org.w3c.dom.Node context)
  {
    throw new UnsupportedOperationException();
  }
  



  public boolean handlesNullPrefixes()
  {
    return false;
  }
}
