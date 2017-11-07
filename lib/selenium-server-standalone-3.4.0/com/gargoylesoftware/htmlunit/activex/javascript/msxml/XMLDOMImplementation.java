package com.gargoylesoftware.htmlunit.activex.javascript.msxml;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import net.sourceforge.htmlunit.corejs.javascript.Context;




























@JsxClass(browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
public class XMLDOMImplementation
  extends MSXMLScriptable
{
  public XMLDOMImplementation() {}
  
  @JsxFunction
  public boolean hasFeature(String feature, String version)
  {
    if ((feature == null) || ("null".equals(feature)) || (version == null) || ("null".equals(version))) {
      throw Context.reportRuntimeError("Type mismatch.");
    }
    
    if (("XML".equals(feature)) && ("1.0".equals(version))) {
      return true;
    }
    if (("DOM".equals(feature)) && ("1.0".equals(version))) {
      return true;
    }
    if (("MS-DOM".equals(feature)) && (("1.0".equals(version)) || ("2.0".equals(version)))) {
      return true;
    }
    return false;
  }
}
