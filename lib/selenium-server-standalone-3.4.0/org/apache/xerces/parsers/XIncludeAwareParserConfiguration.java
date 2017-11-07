package org.apache.xerces.parsers;

import java.util.HashMap;
import org.apache.xerces.impl.XML11DTDScannerImpl;
import org.apache.xerces.impl.dtd.XML11DTDProcessor;
import org.apache.xerces.impl.dtd.XMLDTDProcessor;
import org.apache.xerces.impl.xs.XMLSchemaValidator;
import org.apache.xerces.util.NamespaceSupport;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.xinclude.XIncludeHandler;
import org.apache.xerces.xinclude.XIncludeNamespaceSupport;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.XMLDTDHandler;
import org.apache.xerces.xni.XMLDocumentHandler;
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLDTDScanner;
import org.apache.xerces.xni.parser.XMLDocumentSource;

public class XIncludeAwareParserConfiguration
  extends XML11Configuration
{
  protected static final String ALLOW_UE_AND_NOTATION_EVENTS = "http://xml.org/sax/features/allow-dtd-events-after-endDTD";
  protected static final String XINCLUDE_FIXUP_BASE_URIS = "http://apache.org/xml/features/xinclude/fixup-base-uris";
  protected static final String XINCLUDE_FIXUP_LANGUAGE = "http://apache.org/xml/features/xinclude/fixup-language";
  protected static final String XINCLUDE_FEATURE = "http://apache.org/xml/features/xinclude";
  protected static final String XINCLUDE_HANDLER = "http://apache.org/xml/properties/internal/xinclude-handler";
  protected static final String NAMESPACE_CONTEXT = "http://apache.org/xml/properties/internal/namespace-context";
  protected XIncludeHandler fXIncludeHandler;
  protected NamespaceSupport fNonXIncludeNSContext;
  protected XIncludeNamespaceSupport fXIncludeNSContext;
  protected NamespaceContext fCurrentNSContext;
  protected boolean fXIncludeEnabled = false;
  
  public XIncludeAwareParserConfiguration()
  {
    this(null, null, null);
  }
  
  public XIncludeAwareParserConfiguration(SymbolTable paramSymbolTable)
  {
    this(paramSymbolTable, null, null);
  }
  
  public XIncludeAwareParserConfiguration(SymbolTable paramSymbolTable, XMLGrammarPool paramXMLGrammarPool)
  {
    this(paramSymbolTable, paramXMLGrammarPool, null);
  }
  
  public XIncludeAwareParserConfiguration(SymbolTable paramSymbolTable, XMLGrammarPool paramXMLGrammarPool, XMLComponentManager paramXMLComponentManager)
  {
    super(paramSymbolTable, paramXMLGrammarPool, paramXMLComponentManager);
    String[] arrayOfString1 = { "http://xml.org/sax/features/allow-dtd-events-after-endDTD", "http://apache.org/xml/features/xinclude/fixup-base-uris", "http://apache.org/xml/features/xinclude/fixup-language" };
    addRecognizedFeatures(arrayOfString1);
    String[] arrayOfString2 = { "http://apache.org/xml/properties/internal/xinclude-handler", "http://apache.org/xml/properties/internal/namespace-context" };
    addRecognizedProperties(arrayOfString2);
    setFeature("http://xml.org/sax/features/allow-dtd-events-after-endDTD", true);
    setFeature("http://apache.org/xml/features/xinclude/fixup-base-uris", true);
    setFeature("http://apache.org/xml/features/xinclude/fixup-language", true);
    fNonXIncludeNSContext = new NamespaceSupport();
    fCurrentNSContext = fNonXIncludeNSContext;
    setProperty("http://apache.org/xml/properties/internal/namespace-context", fNonXIncludeNSContext);
  }
  
  protected void configurePipeline()
  {
    super.configurePipeline();
    if (fXIncludeEnabled)
    {
      if (fXIncludeHandler == null)
      {
        fXIncludeHandler = new XIncludeHandler();
        setProperty("http://apache.org/xml/properties/internal/xinclude-handler", fXIncludeHandler);
        addCommonComponent(fXIncludeHandler);
        fXIncludeHandler.reset(this);
      }
      if (fCurrentNSContext != fXIncludeNSContext)
      {
        if (fXIncludeNSContext == null) {
          fXIncludeNSContext = new XIncludeNamespaceSupport();
        }
        fCurrentNSContext = fXIncludeNSContext;
        setProperty("http://apache.org/xml/properties/internal/namespace-context", fXIncludeNSContext);
      }
      fDTDScanner.setDTDHandler(fDTDProcessor);
      fDTDProcessor.setDTDSource(fDTDScanner);
      fDTDProcessor.setDTDHandler(fXIncludeHandler);
      fXIncludeHandler.setDTDSource(fDTDProcessor);
      fXIncludeHandler.setDTDHandler(fDTDHandler);
      if (fDTDHandler != null) {
        fDTDHandler.setDTDSource(fXIncludeHandler);
      }
      XMLDocumentSource localXMLDocumentSource = null;
      if (fFeatures.get("http://apache.org/xml/features/validation/schema") == Boolean.TRUE)
      {
        localXMLDocumentSource = fSchemaValidator.getDocumentSource();
      }
      else
      {
        localXMLDocumentSource = fLastComponent;
        fLastComponent = fXIncludeHandler;
      }
      XMLDocumentHandler localXMLDocumentHandler = localXMLDocumentSource.getDocumentHandler();
      localXMLDocumentSource.setDocumentHandler(fXIncludeHandler);
      fXIncludeHandler.setDocumentSource(localXMLDocumentSource);
      if (localXMLDocumentHandler != null)
      {
        fXIncludeHandler.setDocumentHandler(localXMLDocumentHandler);
        localXMLDocumentHandler.setDocumentSource(fXIncludeHandler);
      }
    }
    else if (fCurrentNSContext != fNonXIncludeNSContext)
    {
      fCurrentNSContext = fNonXIncludeNSContext;
      setProperty("http://apache.org/xml/properties/internal/namespace-context", fNonXIncludeNSContext);
    }
  }
  
  protected void configureXML11Pipeline()
  {
    super.configureXML11Pipeline();
    if (fXIncludeEnabled)
    {
      if (fXIncludeHandler == null)
      {
        fXIncludeHandler = new XIncludeHandler();
        setProperty("http://apache.org/xml/properties/internal/xinclude-handler", fXIncludeHandler);
        addCommonComponent(fXIncludeHandler);
        fXIncludeHandler.reset(this);
      }
      if (fCurrentNSContext != fXIncludeNSContext)
      {
        if (fXIncludeNSContext == null) {
          fXIncludeNSContext = new XIncludeNamespaceSupport();
        }
        fCurrentNSContext = fXIncludeNSContext;
        setProperty("http://apache.org/xml/properties/internal/namespace-context", fXIncludeNSContext);
      }
      fXML11DTDScanner.setDTDHandler(fXML11DTDProcessor);
      fXML11DTDProcessor.setDTDSource(fXML11DTDScanner);
      fXML11DTDProcessor.setDTDHandler(fXIncludeHandler);
      fXIncludeHandler.setDTDSource(fXML11DTDProcessor);
      fXIncludeHandler.setDTDHandler(fDTDHandler);
      if (fDTDHandler != null) {
        fDTDHandler.setDTDSource(fXIncludeHandler);
      }
      XMLDocumentSource localXMLDocumentSource = null;
      if (fFeatures.get("http://apache.org/xml/features/validation/schema") == Boolean.TRUE)
      {
        localXMLDocumentSource = fSchemaValidator.getDocumentSource();
      }
      else
      {
        localXMLDocumentSource = fLastComponent;
        fLastComponent = fXIncludeHandler;
      }
      XMLDocumentHandler localXMLDocumentHandler = localXMLDocumentSource.getDocumentHandler();
      localXMLDocumentSource.setDocumentHandler(fXIncludeHandler);
      fXIncludeHandler.setDocumentSource(localXMLDocumentSource);
      if (localXMLDocumentHandler != null)
      {
        fXIncludeHandler.setDocumentHandler(localXMLDocumentHandler);
        localXMLDocumentHandler.setDocumentSource(fXIncludeHandler);
      }
    }
    else if (fCurrentNSContext != fNonXIncludeNSContext)
    {
      fCurrentNSContext = fNonXIncludeNSContext;
      setProperty("http://apache.org/xml/properties/internal/namespace-context", fNonXIncludeNSContext);
    }
  }
  
  public boolean getFeature(String paramString)
    throws XMLConfigurationException
  {
    if (paramString.equals("http://apache.org/xml/features/internal/parser-settings")) {
      return fConfigUpdated;
    }
    if (paramString.equals("http://apache.org/xml/features/xinclude")) {
      return fXIncludeEnabled;
    }
    return super.getFeature0(paramString);
  }
  
  public void setFeature(String paramString, boolean paramBoolean)
    throws XMLConfigurationException
  {
    if (paramString.equals("http://apache.org/xml/features/xinclude"))
    {
      fXIncludeEnabled = paramBoolean;
      fConfigUpdated = true;
      return;
    }
    super.setFeature(paramString, paramBoolean);
  }
}
