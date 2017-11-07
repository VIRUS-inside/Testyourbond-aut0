package com.gargoylesoftware.htmlunit.activex.javascript.msxml;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.javascript.configuration.AbstractJavaScriptConfiguration;
import java.util.Map;
import java.util.WeakHashMap;

























public final class MSXMLConfiguration
  extends AbstractJavaScriptConfiguration
{
  private static final Class<MSXMLScriptable>[] CLASSES_ = {
    XMLDOMAttribute.class, XMLDOMCDATASection.class, XMLDOMCharacterData.class, XMLDOMComment.class, 
    XMLDOMDocument.class, XMLDOMDocumentFragment.class, XMLDOMDocumentType.class, XMLDOMElement.class, 
    XMLDOMImplementation.class, XMLDOMNamedNodeMap.class, XMLDOMNode.class, XMLDOMNodeList.class, 
    XMLDOMParseError.class, XMLDOMProcessingInstruction.class, XMLDOMSelection.class, XMLDOMText.class, 
    XMLHTTPRequest.class, XSLProcessor.class, XSLTemplate.class };
  


  private static final Map<BrowserVersion, MSXMLConfiguration> CONFIGURATION_MAP_ = new WeakHashMap();
  



  private MSXMLConfiguration(BrowserVersion browser)
  {
    super(browser);
  }
  





  public static synchronized MSXMLConfiguration getInstance(BrowserVersion browserVersion)
  {
    if (browserVersion == null) {
      throw new IllegalStateException("BrowserVersion must be defined");
    }
    MSXMLConfiguration configuration = (MSXMLConfiguration)CONFIGURATION_MAP_.get(browserVersion);
    
    if (configuration == null) {
      configuration = new MSXMLConfiguration(browserVersion);
      CONFIGURATION_MAP_.put(browserVersion, configuration);
    }
    return configuration;
  }
  
  protected Class<MSXMLScriptable>[] getClasses()
  {
    return CLASSES_;
  }
}
