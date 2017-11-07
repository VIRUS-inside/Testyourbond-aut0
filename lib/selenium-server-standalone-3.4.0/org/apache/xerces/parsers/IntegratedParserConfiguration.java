package org.apache.xerces.parsers;

import java.util.HashMap;
import org.apache.xerces.impl.XMLDocumentScannerImpl;
import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.impl.XMLNSDocumentScannerImpl;
import org.apache.xerces.impl.dtd.XMLDTDValidator;
import org.apache.xerces.impl.dtd.XMLNSDTDValidator;
import org.apache.xerces.impl.xs.XMLSchemaValidator;
import org.apache.xerces.impl.xs.XSMessageFormatter;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.xni.XMLDocumentHandler;
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLDocumentScanner;
import org.apache.xerces.xni.parser.XMLDocumentSource;

public class IntegratedParserConfiguration
  extends StandardParserConfiguration
{
  protected XMLNSDocumentScannerImpl fNamespaceScanner;
  protected XMLDocumentScannerImpl fNonNSScanner = new XMLDocumentScannerImpl();
  protected XMLDTDValidator fNonNSDTDValidator = new XMLDTDValidator();
  
  public IntegratedParserConfiguration()
  {
    this(null, null, null);
  }
  
  public IntegratedParserConfiguration(SymbolTable paramSymbolTable)
  {
    this(paramSymbolTable, null, null);
  }
  
  public IntegratedParserConfiguration(SymbolTable paramSymbolTable, XMLGrammarPool paramXMLGrammarPool)
  {
    this(paramSymbolTable, paramXMLGrammarPool, null);
  }
  
  public IntegratedParserConfiguration(SymbolTable paramSymbolTable, XMLGrammarPool paramXMLGrammarPool, XMLComponentManager paramXMLComponentManager)
  {
    super(paramSymbolTable, paramXMLGrammarPool, paramXMLComponentManager);
    addComponent(fNonNSScanner);
    addComponent(fNonNSDTDValidator);
  }
  
  protected void configurePipeline()
  {
    setProperty("http://apache.org/xml/properties/internal/datatype-validator-factory", fDatatypeValidatorFactory);
    configureDTDPipeline();
    if (fFeatures.get("http://xml.org/sax/features/namespaces") == Boolean.TRUE)
    {
      fProperties.put("http://apache.org/xml/properties/internal/namespace-binder", fNamespaceBinder);
      fScanner = fNamespaceScanner;
      fProperties.put("http://apache.org/xml/properties/internal/document-scanner", fNamespaceScanner);
      if (fDTDValidator != null)
      {
        fProperties.put("http://apache.org/xml/properties/internal/validator/dtd", fDTDValidator);
        fNamespaceScanner.setDTDValidator(fDTDValidator);
        fNamespaceScanner.setDocumentHandler(fDTDValidator);
        fDTDValidator.setDocumentSource(fNamespaceScanner);
        fDTDValidator.setDocumentHandler(fDocumentHandler);
        if (fDocumentHandler != null) {
          fDocumentHandler.setDocumentSource(fDTDValidator);
        }
        fLastComponent = fDTDValidator;
      }
      else
      {
        fNamespaceScanner.setDocumentHandler(fDocumentHandler);
        fNamespaceScanner.setDTDValidator(null);
        if (fDocumentHandler != null) {
          fDocumentHandler.setDocumentSource(fNamespaceScanner);
        }
        fLastComponent = fNamespaceScanner;
      }
    }
    else
    {
      fScanner = fNonNSScanner;
      fProperties.put("http://apache.org/xml/properties/internal/document-scanner", fNonNSScanner);
      if (fNonNSDTDValidator != null)
      {
        fProperties.put("http://apache.org/xml/properties/internal/validator/dtd", fNonNSDTDValidator);
        fNonNSScanner.setDocumentHandler(fNonNSDTDValidator);
        fNonNSDTDValidator.setDocumentSource(fNonNSScanner);
        fNonNSDTDValidator.setDocumentHandler(fDocumentHandler);
        if (fDocumentHandler != null) {
          fDocumentHandler.setDocumentSource(fNonNSDTDValidator);
        }
        fLastComponent = fNonNSDTDValidator;
      }
      else
      {
        fScanner.setDocumentHandler(fDocumentHandler);
        if (fDocumentHandler != null) {
          fDocumentHandler.setDocumentSource(fScanner);
        }
        fLastComponent = fScanner;
      }
    }
    if (fFeatures.get("http://apache.org/xml/features/validation/schema") == Boolean.TRUE)
    {
      if (fSchemaValidator == null)
      {
        fSchemaValidator = new XMLSchemaValidator();
        fProperties.put("http://apache.org/xml/properties/internal/validator/schema", fSchemaValidator);
        addComponent(fSchemaValidator);
        if (fErrorReporter.getMessageFormatter("http://www.w3.org/TR/xml-schema-1") == null)
        {
          XSMessageFormatter localXSMessageFormatter = new XSMessageFormatter();
          fErrorReporter.putMessageFormatter("http://www.w3.org/TR/xml-schema-1", localXSMessageFormatter);
        }
      }
      fLastComponent.setDocumentHandler(fSchemaValidator);
      fSchemaValidator.setDocumentSource(fLastComponent);
      fSchemaValidator.setDocumentHandler(fDocumentHandler);
      if (fDocumentHandler != null) {
        fDocumentHandler.setDocumentSource(fSchemaValidator);
      }
      fLastComponent = fSchemaValidator;
    }
  }
  
  protected XMLDocumentScanner createDocumentScanner()
  {
    fNamespaceScanner = new XMLNSDocumentScannerImpl();
    return fNamespaceScanner;
  }
  
  protected XMLDTDValidator createDTDValidator()
  {
    return new XMLNSDTDValidator();
  }
}
