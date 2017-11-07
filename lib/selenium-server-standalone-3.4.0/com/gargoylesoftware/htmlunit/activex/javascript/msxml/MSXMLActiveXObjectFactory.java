package com.gargoylesoftware.htmlunit.activex.javascript.msxml;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.WebWindow;
import java.util.Locale;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;























public class MSXMLActiveXObjectFactory
{
  private static final Log LOG = LogFactory.getLog(MSXMLActiveXObjectFactory.class);
  
  private MSXMLJavaScriptEnvironment environment_;
  

  public MSXMLActiveXObjectFactory() {}
  

  public void init(BrowserVersion browserVersion)
    throws Exception
  {
    environment_ = new MSXMLJavaScriptEnvironment(browserVersion);
  }
  





  public boolean supports(String activeXName)
  {
    return (isXMLDOMDocument(activeXName)) || 
      (isXMLHTTPRequest(activeXName)) || 
      (isXSLTemplate(activeXName));
  }
  




  static boolean isXMLDOMDocument(String name)
  {
    if (name == null) {
      return false;
    }
    name = name.toLowerCase(Locale.ROOT);
    return ("microsoft.xmldom".equals(name)) || 
      (name.startsWith("msxml2.domdocument")) || 
      (name.startsWith("msxml2.freethreadeddomdocument"));
  }
  




  static boolean isXMLHTTPRequest(String name)
  {
    if (name == null) {
      return false;
    }
    name = name.toLowerCase(Locale.ROOT);
    return ("microsoft.xmlhttp".equals(name)) || 
      (name.startsWith("msxml2.xmlhttp"));
  }
  




  static boolean isXSLTemplate(String name)
  {
    if (name == null) {
      return false;
    }
    name = name.toLowerCase(Locale.ROOT);
    return name.startsWith("msxml2.xsltemplate");
  }
  






  public Scriptable create(String activeXName, WebWindow enclosingWindow)
  {
    if (isXMLDOMDocument(activeXName)) {
      return createXMLDOMDocument(enclosingWindow);
    }
    
    if (isXMLHTTPRequest(activeXName)) {
      return createXMLHTTPRequest();
    }
    
    if (isXSLTemplate(activeXName)) {
      return createXSLTemplate();
    }
    return null;
  }
  
  private XMLDOMDocument createXMLDOMDocument(WebWindow enclosingWindow) {
    XMLDOMDocument document = new XMLDOMDocument(enclosingWindow);
    initObject(document);
    try
    {
      document.setParentScope(enclosingWindow.getScriptableObject());
    }
    catch (Exception e) {
      LOG.error("Exception while initializing JavaScript for the page", e);
      throw new ScriptException(null, e);
    }
    return document;
  }
  
  private Scriptable createXMLHTTPRequest() {
    XMLHTTPRequest request = new XMLHTTPRequest();
    initObject(request);
    return request;
  }
  
  private Scriptable createXSLTemplate() {
    XSLTemplate template = new XSLTemplate();
    initObject(template);
    return template;
  }
  
  private void initObject(MSXMLScriptable scriptable) {
    try {
      scriptable.setPrototype(environment_.getPrototype(scriptable.getClass()));
      scriptable.setEnvironment(environment_);
    }
    catch (Exception e) {
      LOG.error("Exception while initializing JavaScript for the page", e);
      throw new ScriptException(null, e);
    }
  }
}
