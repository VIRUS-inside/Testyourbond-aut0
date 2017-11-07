package com.gargoylesoftware.htmlunit.activex.javascript.msxml;

import com.gargoylesoftware.htmlunit.html.DomDocumentFragment;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import net.sourceforge.htmlunit.corejs.javascript.Context;




























@JsxClass(domClass=DomDocumentFragment.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
public class XMLDOMDocumentFragment
  extends XMLDOMNode
{
  public XMLDOMDocumentFragment() {}
  
  public void setNodeValue(String newValue)
  {
    if ((newValue == null) || ("null".equals(newValue))) {
      throw Context.reportRuntimeError("Type mismatch.");
    }
    throw Context.reportRuntimeError("This operation cannot be performed with a node of type DOCFRAG.");
  }
  




  public void setText(Object value)
  {
    if ((value == null) || ("null".equals(value))) {
      throw Context.reportRuntimeError("Type mismatch.");
    }
    throw Context.reportRuntimeError("This operation cannot be performed with a node of type DOCFRAG.");
  }
  



  public Object getXml()
  {
    Object xml = super.getXml();
    if ((xml instanceof String)) {
      String xmlString = (String)xml;
      if (xmlString.indexOf('\n') >= 0) {
        xmlString = xmlString.replaceAll("([^\r])\n", "$1\r\n");
        xmlString = xmlString.replaceAll(">\r\n\\s*", ">");
        xml = xmlString;
      }
    }
    return xml;
  }
}
