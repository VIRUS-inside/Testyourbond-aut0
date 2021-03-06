package org.apache.xerces.parsers;

import java.util.HashMap;
import org.apache.xerces.impl.XML11DTDScannerImpl;
import org.apache.xerces.impl.dtd.XML11DTDProcessor;
import org.apache.xerces.impl.dtd.XMLDTDProcessor;
import org.apache.xerces.impl.xs.XMLSchemaValidator;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.xinclude.XIncludeHandler;
import org.apache.xerces.xinclude.XIncludeNamespaceSupport;
import org.apache.xerces.xni.XMLDTDHandler;
import org.apache.xerces.xni.XMLDocumentHandler;
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLDTDScanner;
import org.apache.xerces.xni.parser.XMLDocumentSource;
import org.apache.xerces.xpointer.XPointerHandler;

public class XPointerParserConfiguration
  extends XML11Configuration
{
  private XPointerHandler fXPointerHandler;
  private XIncludeHandler fXIncludeHandler = new XIncludeHandler();
  protected static final String ALLOW_UE_AND_NOTATION_EVENTS = "http://xml.org/sax/features/allow-dtd-events-after-endDTD";
  protected static final String XINCLUDE_FIXUP_BASE_URIS = "http://apache.org/xml/features/xinclude/fixup-base-uris";
  protected static final String XINCLUDE_FIXUP_LANGUAGE = "http://apache.org/xml/features/xinclude/fixup-language";
  protected static final String XPOINTER_HANDLER = "http://apache.org/xml/properties/internal/xpointer-handler";
  protected static final String XINCLUDE_HANDLER = "http://apache.org/xml/properties/internal/xinclude-handler";
  protected static final String NAMESPACE_CONTEXT = "http://apache.org/xml/properties/internal/namespace-context";
  
  public XPointerParserConfiguration()
  {
    this(null, null, null);
  }
  
  public XPointerParserConfiguration(SymbolTable paramSymbolTable)
  {
    this(paramSymbolTable, null, null);
  }
  
  public XPointerParserConfiguration(SymbolTable paramSymbolTable, XMLGrammarPool paramXMLGrammarPool)
  {
    this(paramSymbolTable, paramXMLGrammarPool, null);
  }
  
  public XPointerParserConfiguration(SymbolTable paramSymbolTable, XMLGrammarPool paramXMLGrammarPool, XMLComponentManager paramXMLComponentManager)
  {
    super(paramSymbolTable, paramXMLGrammarPool, paramXMLComponentManager);
    addCommonComponent(fXIncludeHandler);
    fXPointerHandler = new XPointerHandler();
    addCommonComponent(fXPointerHandler);
    String[] arrayOfString1 = { "http://xml.org/sax/features/allow-dtd-events-after-endDTD", "http://apache.org/xml/features/xinclude/fixup-base-uris", "http://apache.org/xml/features/xinclude/fixup-language" };
    addRecognizedFeatures(arrayOfString1);
    String[] arrayOfString2 = { "http://apache.org/xml/properties/internal/xinclude-handler", "http://apache.org/xml/properties/internal/xpointer-handler", "http://apache.org/xml/properties/internal/namespace-context" };
    addRecognizedProperties(arrayOfString2);
    setFeature("http://xml.org/sax/features/allow-dtd-events-after-endDTD", true);
    setFeature("http://apache.org/xml/features/xinclude/fixup-base-uris", true);
    setFeature("http://apache.org/xml/features/xinclude/fixup-language", true);
    setProperty("http://apache.org/xml/properties/internal/xinclude-handler", fXIncludeHandler);
    setProperty("http://apache.org/xml/properties/internal/xpointer-handler", fXPointerHandler);
    setProperty("http://apache.org/xml/properties/internal/namespace-context", new XIncludeNamespaceSupport());
  }
  
  protected void configurePipeline()
  {
    super.configurePipeline();
    fDTDScanner.setDTDHandler(fDTDProcessor);
    fDTDProcessor.setDTDSource(fDTDScanner);
    fDTDProcessor.setDTDHandler(fXIncludeHandler);
    fXIncludeHandler.setDTDSource(fDTDProcessor);
    fXIncludeHandler.setDTDHandler(fXPointerHandler);
    fXPointerHandler.setDTDSource(fXIncludeHandler);
    fXPointerHandler.setDTDHandler(fDTDHandler);
    if (fDTDHandler != null) {
      fDTDHandler.setDTDSource(fXPointerHandler);
    }
    XMLDocumentSource localXMLDocumentSource = null;
    if (fFeatures.get("http://apache.org/xml/features/validation/schema") == Boolean.TRUE)
    {
      localXMLDocumentSource = fSchemaValidator.getDocumentSource();
    }
    else
    {
      localXMLDocumentSource = fLastComponent;
      fLastComponent = fXPointerHandler;
    }
    XMLDocumentHandler localXMLDocumentHandler = localXMLDocumentSource.getDocumentHandler();
    localXMLDocumentSource.setDocumentHandler(fXIncludeHandler);
    fXIncludeHandler.setDocumentSource(localXMLDocumentSource);
    if (localXMLDocumentHandler != null)
    {
      fXIncludeHandler.setDocumentHandler(localXMLDocumentHandler);
      localXMLDocumentHandler.setDocumentSource(fXIncludeHandler);
    }
    fXIncludeHandler.setDocumentHandler(fXPointerHandler);
    fXPointerHandler.setDocumentSource(fXIncludeHandler);
  }
  
  protected void configureXML11Pipeline()
  {
    super.configureXML11Pipeline();
    fXML11DTDScanner.setDTDHandler(fXML11DTDProcessor);
    fXML11DTDProcessor.setDTDSource(fXML11DTDScanner);
    fDTDProcessor.setDTDHandler(fXIncludeHandler);
    fXIncludeHandler.setDTDSource(fXML11DTDProcessor);
    fXIncludeHandler.setDTDHandler(fXPointerHandler);
    fXPointerHandler.setDTDSource(fXIncludeHandler);
    fXPointerHandler.setDTDHandler(fDTDHandler);
    if (fDTDHandler != null) {
      fDTDHandler.setDTDSource(fXPointerHandler);
    }
    XMLDocumentSource localXMLDocumentSource = null;
    if (fFeatures.get("http://apache.org/xml/features/validation/schema") == Boolean.TRUE)
    {
      localXMLDocumentSource = fSchemaValidator.getDocumentSource();
    }
    else
    {
      localXMLDocumentSource = fLastComponent;
      fLastComponent = fXPointerHandler;
    }
    XMLDocumentHandler localXMLDocumentHandler = localXMLDocumentSource.getDocumentHandler();
    localXMLDocumentSource.setDocumentHandler(fXIncludeHandler);
    fXIncludeHandler.setDocumentSource(localXMLDocumentSource);
    if (localXMLDocumentHandler != null)
    {
      fXIncludeHandler.setDocumentHandler(localXMLDocumentHandler);
      localXMLDocumentHandler.setDocumentSource(fXIncludeHandler);
    }
    fXIncludeHandler.setDocumentHandler(fXPointerHandler);
    fXPointerHandler.setDocumentSource(fXIncludeHandler);
  }
  
  public void setProperty(String paramString, Object paramObject)
    throws XMLConfigurationException
  {
    super.setProperty(paramString, paramObject);
  }
}
