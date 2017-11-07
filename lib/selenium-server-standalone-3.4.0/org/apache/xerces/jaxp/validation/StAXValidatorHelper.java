package org.apache.xerces.jaxp.validation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.xml.stream.Location;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.Comment;
import javax.xml.stream.events.DTD;
import javax.xml.stream.events.EndDocument;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.EntityDeclaration;
import javax.xml.stream.events.EntityReference;
import javax.xml.stream.events.Namespace;
import javax.xml.stream.events.ProcessingInstruction;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.stax.StAXResult;
import javax.xml.transform.stax.StAXSource;
import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.impl.validation.EntityState;
import org.apache.xerces.impl.validation.ValidationManager;
import org.apache.xerces.impl.xs.XMLSchemaValidator;
import org.apache.xerces.util.JAXPNamespaceContextWrapper;
import org.apache.xerces.util.StAXLocationWrapper;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLAttributesImpl;
import org.apache.xerces.util.XMLStringBuffer;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLParseException;
import org.xml.sax.SAXException;

final class StAXValidatorHelper
  implements ValidatorHelper, EntityState
{
  private static final String STRING_INTERNING = "javax.xml.stream.isInterning";
  private static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
  private static final String SCHEMA_VALIDATOR = "http://apache.org/xml/properties/internal/validator/schema";
  private static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
  private static final String VALIDATION_MANAGER = "http://apache.org/xml/properties/internal/validation-manager";
  private final XMLErrorReporter fErrorReporter;
  private final XMLSchemaValidator fSchemaValidator;
  private final SymbolTable fSymbolTable;
  private final ValidationManager fValidationManager;
  private final XMLSchemaValidatorComponentManager fComponentManager;
  private final JAXPNamespaceContextWrapper fNamespaceContext;
  private final StAXLocationWrapper fStAXLocationWrapper = new StAXLocationWrapper();
  private final XMLStreamReaderLocation fXMLStreamReaderLocation = new XMLStreamReaderLocation();
  private HashMap fEntities = null;
  private boolean fStringsInternalized = false;
  private StreamHelper fStreamHelper;
  private EventHelper fEventHelper;
  private StAXDocumentHandler fStAXValidatorHandler;
  private StAXStreamResultBuilder fStAXStreamResultBuilder;
  private StAXEventResultBuilder fStAXEventResultBuilder;
  private int fDepth = 0;
  private XMLEvent fCurrentEvent = null;
  final org.apache.xerces.xni.QName fElementQName = new org.apache.xerces.xni.QName();
  final org.apache.xerces.xni.QName fAttributeQName = new org.apache.xerces.xni.QName();
  final XMLAttributesImpl fAttributes = new XMLAttributesImpl();
  final ArrayList fDeclaredPrefixes = new ArrayList();
  final XMLString fTempString = new XMLString();
  final XMLStringBuffer fStringBuffer = new XMLStringBuffer();
  
  public StAXValidatorHelper(XMLSchemaValidatorComponentManager paramXMLSchemaValidatorComponentManager)
  {
    fComponentManager = paramXMLSchemaValidatorComponentManager;
    fErrorReporter = ((XMLErrorReporter)fComponentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter"));
    fSchemaValidator = ((XMLSchemaValidator)fComponentManager.getProperty("http://apache.org/xml/properties/internal/validator/schema"));
    fSymbolTable = ((SymbolTable)fComponentManager.getProperty("http://apache.org/xml/properties/internal/symbol-table"));
    fValidationManager = ((ValidationManager)fComponentManager.getProperty("http://apache.org/xml/properties/internal/validation-manager"));
    fNamespaceContext = new JAXPNamespaceContextWrapper(fSymbolTable);
    fNamespaceContext.setDeclaredPrefixes(fDeclaredPrefixes);
  }
  
  public void validate(Source paramSource, Result paramResult)
    throws SAXException, IOException
  {
    if (((paramResult instanceof StAXResult)) || (paramResult == null))
    {
      StAXSource localStAXSource = (StAXSource)paramSource;
      StAXResult localStAXResult = (StAXResult)paramResult;
      try
      {
        XMLStreamReader localXMLStreamReader = localStAXSource.getXMLStreamReader();
        if (localXMLStreamReader != null)
        {
          if (fStreamHelper == null) {
            fStreamHelper = new StreamHelper();
          }
          fStreamHelper.validate(localXMLStreamReader, localStAXResult);
        }
        else
        {
          if (fEventHelper == null) {
            fEventHelper = new EventHelper();
          }
          fEventHelper.validate(localStAXSource.getXMLEventReader(), localStAXResult);
        }
      }
      catch (XMLStreamException localXMLStreamException)
      {
        throw new SAXException(localXMLStreamException);
      }
      catch (XMLParseException localXMLParseException)
      {
        throw Util.toSAXParseException(localXMLParseException);
      }
      catch (XNIException localXNIException)
      {
        throw Util.toSAXException(localXNIException);
      }
      finally
      {
        fCurrentEvent = null;
        fStAXLocationWrapper.setLocation(null);
        fXMLStreamReaderLocation.setXMLStreamReader(null);
        if (fStAXValidatorHandler != null) {
          fStAXValidatorHandler.setStAXResult(null);
        }
      }
      return;
    }
    throw new IllegalArgumentException(JAXPValidationMessageFormatter.formatMessage(fComponentManager.getLocale(), "SourceResultMismatch", new Object[] { paramSource.getClass().getName(), paramResult.getClass().getName() }));
  }
  
  public boolean isEntityDeclared(String paramString)
  {
    if (fEntities != null) {
      return fEntities.containsKey(paramString);
    }
    return false;
  }
  
  public boolean isEntityUnparsed(String paramString)
  {
    if (fEntities != null)
    {
      EntityDeclaration localEntityDeclaration = (EntityDeclaration)fEntities.get(paramString);
      if (localEntityDeclaration != null) {
        return localEntityDeclaration.getNotationName() != null;
      }
    }
    return false;
  }
  
  final EntityDeclaration getEntityDeclaration(String paramString)
  {
    return fEntities != null ? (EntityDeclaration)fEntities.get(paramString) : null;
  }
  
  final XMLEvent getCurrentEvent()
  {
    return fCurrentEvent;
  }
  
  final void fillQName(org.apache.xerces.xni.QName paramQName, String paramString1, String paramString2, String paramString3)
  {
    if (!fStringsInternalized)
    {
      paramString1 = (paramString1 != null) && (paramString1.length() > 0) ? fSymbolTable.addSymbol(paramString1) : null;
      paramString2 = paramString2 != null ? fSymbolTable.addSymbol(paramString2) : XMLSymbols.EMPTY_STRING;
      paramString3 = (paramString3 != null) && (paramString3.length() > 0) ? fSymbolTable.addSymbol(paramString3) : XMLSymbols.EMPTY_STRING;
    }
    else
    {
      if ((paramString1 != null) && (paramString1.length() == 0)) {
        paramString1 = null;
      }
      if (paramString2 == null) {
        paramString2 = XMLSymbols.EMPTY_STRING;
      }
      if (paramString3 == null) {
        paramString3 = XMLSymbols.EMPTY_STRING;
      }
    }
    String str = paramString2;
    if (paramString3 != XMLSymbols.EMPTY_STRING)
    {
      fStringBuffer.clear();
      fStringBuffer.append(paramString3);
      fStringBuffer.append(':');
      fStringBuffer.append(paramString2);
      str = fSymbolTable.addSymbol(fStringBuffer.ch, fStringBuffer.offset, fStringBuffer.length);
    }
    paramQName.setValues(paramString3, paramString2, str, paramString1);
  }
  
  final void setup(Location paramLocation, StAXResult paramStAXResult, boolean paramBoolean)
  {
    fDepth = 0;
    fComponentManager.reset();
    setupStAXResultHandler(paramStAXResult);
    fValidationManager.setEntityState(this);
    if ((fEntities != null) && (!fEntities.isEmpty())) {
      fEntities.clear();
    }
    fStAXLocationWrapper.setLocation(paramLocation);
    fErrorReporter.setDocumentLocator(fStAXLocationWrapper);
    fStringsInternalized = paramBoolean;
  }
  
  final void processEntityDeclarations(List paramList)
  {
    int i = paramList != null ? paramList.size() : 0;
    if (i > 0)
    {
      if (fEntities == null) {
        fEntities = new HashMap();
      }
      for (int j = 0; j < i; j++)
      {
        EntityDeclaration localEntityDeclaration = (EntityDeclaration)paramList.get(j);
        fEntities.put(localEntityDeclaration.getName(), localEntityDeclaration);
      }
    }
  }
  
  private void setupStAXResultHandler(StAXResult paramStAXResult)
  {
    if (paramStAXResult == null)
    {
      fStAXValidatorHandler = null;
      fSchemaValidator.setDocumentHandler(null);
      return;
    }
    XMLStreamWriter localXMLStreamWriter = paramStAXResult.getXMLStreamWriter();
    if (localXMLStreamWriter != null)
    {
      if (fStAXStreamResultBuilder == null) {
        fStAXStreamResultBuilder = new StAXStreamResultBuilder(fNamespaceContext);
      }
      fStAXValidatorHandler = fStAXStreamResultBuilder;
      fStAXStreamResultBuilder.setStAXResult(paramStAXResult);
    }
    else
    {
      if (fStAXEventResultBuilder == null) {
        fStAXEventResultBuilder = new StAXEventResultBuilder(this, fNamespaceContext);
      }
      fStAXValidatorHandler = fStAXEventResultBuilder;
      fStAXEventResultBuilder.setStAXResult(paramStAXResult);
    }
    fSchemaValidator.setDocumentHandler(fStAXValidatorHandler);
  }
  
  static final class XMLStreamReaderLocation
    implements Location
  {
    private XMLStreamReader reader;
    
    public XMLStreamReaderLocation() {}
    
    public int getCharacterOffset()
    {
      Location localLocation = getLocation();
      if (localLocation != null) {
        return localLocation.getCharacterOffset();
      }
      return -1;
    }
    
    public int getColumnNumber()
    {
      Location localLocation = getLocation();
      if (localLocation != null) {
        return localLocation.getColumnNumber();
      }
      return -1;
    }
    
    public int getLineNumber()
    {
      Location localLocation = getLocation();
      if (localLocation != null) {
        return localLocation.getLineNumber();
      }
      return -1;
    }
    
    public String getPublicId()
    {
      Location localLocation = getLocation();
      if (localLocation != null) {
        return localLocation.getPublicId();
      }
      return null;
    }
    
    public String getSystemId()
    {
      Location localLocation = getLocation();
      if (localLocation != null) {
        return localLocation.getSystemId();
      }
      return null;
    }
    
    public void setXMLStreamReader(XMLStreamReader paramXMLStreamReader)
    {
      reader = paramXMLStreamReader;
    }
    
    private Location getLocation()
    {
      return reader != null ? reader.getLocation() : null;
    }
  }
  
  final class EventHelper
  {
    private static final int CHUNK_SIZE = 1024;
    private static final int CHUNK_MASK = 1023;
    private final char[] fCharBuffer = new char['Ѐ'];
    
    EventHelper() {}
    
    final void validate(XMLEventReader paramXMLEventReader, StAXResult paramStAXResult)
      throws SAXException, XMLStreamException
    {
      fCurrentEvent = paramXMLEventReader.peek();
      if (fCurrentEvent != null)
      {
        int i = fCurrentEvent.getEventType();
        if ((i != 7) && (i != 1)) {
          throw new SAXException(JAXPValidationMessageFormatter.formatMessage(fComponentManager.getLocale(), "StAXIllegalInitialState", null));
        }
        setup(null, paramStAXResult, false);
        fSchemaValidator.startDocument(fStAXLocationWrapper, null, fNamespaceContext, null);
        while (paramXMLEventReader.hasNext())
        {
          fCurrentEvent = paramXMLEventReader.nextEvent();
          i = fCurrentEvent.getEventType();
          Object localObject;
          switch (i)
          {
          case 1: 
            StAXValidatorHelper.access$504(StAXValidatorHelper.this);
            StartElement localStartElement = fCurrentEvent.asStartElement();
            fillQName(fElementQName, localStartElement.getName());
            fillXMLAttributes(localStartElement);
            fillDeclaredPrefixes(localStartElement);
            fNamespaceContext.setNamespaceContext(localStartElement.getNamespaceContext());
            fStAXLocationWrapper.setLocation(localStartElement.getLocation());
            fSchemaValidator.startElement(fElementQName, fAttributes, null);
            break;
          case 2: 
            EndElement localEndElement = fCurrentEvent.asEndElement();
            fillQName(fElementQName, localEndElement.getName());
            fillDeclaredPrefixes(localEndElement);
            fStAXLocationWrapper.setLocation(localEndElement.getLocation());
            fSchemaValidator.endElement(fElementQName, null);
            if (StAXValidatorHelper.access$506(StAXValidatorHelper.this) > 0) {
              break;
            }
            break;
          case 4: 
          case 6: 
            if (fStAXValidatorHandler != null)
            {
              localObject = fCurrentEvent.asCharacters();
              fStAXValidatorHandler.setIgnoringCharacters(true);
              sendCharactersToValidator(((Characters)localObject).getData());
              fStAXValidatorHandler.setIgnoringCharacters(false);
              fStAXValidatorHandler.characters((Characters)localObject);
            }
            else
            {
              sendCharactersToValidator(fCurrentEvent.asCharacters().getData());
            }
            break;
          case 12: 
            if (fStAXValidatorHandler != null)
            {
              localObject = fCurrentEvent.asCharacters();
              fStAXValidatorHandler.setIgnoringCharacters(true);
              fSchemaValidator.startCDATA(null);
              sendCharactersToValidator(fCurrentEvent.asCharacters().getData());
              fSchemaValidator.endCDATA(null);
              fStAXValidatorHandler.setIgnoringCharacters(false);
              fStAXValidatorHandler.cdata((Characters)localObject);
            }
            else
            {
              fSchemaValidator.startCDATA(null);
              sendCharactersToValidator(fCurrentEvent.asCharacters().getData());
              fSchemaValidator.endCDATA(null);
            }
            break;
          case 7: 
            StAXValidatorHelper.access$504(StAXValidatorHelper.this);
            if (fStAXValidatorHandler != null) {
              fStAXValidatorHandler.startDocument((StartDocument)fCurrentEvent);
            }
            break;
          case 8: 
            if (fStAXValidatorHandler != null) {
              fStAXValidatorHandler.endDocument((EndDocument)fCurrentEvent);
            }
            break;
          case 3: 
            if (fStAXValidatorHandler != null) {
              fStAXValidatorHandler.processingInstruction((ProcessingInstruction)fCurrentEvent);
            }
            break;
          case 5: 
            if (fStAXValidatorHandler != null) {
              fStAXValidatorHandler.comment((Comment)fCurrentEvent);
            }
            break;
          case 9: 
            if (fStAXValidatorHandler != null) {
              fStAXValidatorHandler.entityReference((EntityReference)fCurrentEvent);
            }
            break;
          case 11: 
            localObject = (DTD)fCurrentEvent;
            processEntityDeclarations(((DTD)localObject).getEntities());
            if (fStAXValidatorHandler != null) {
              fStAXValidatorHandler.doctypeDecl((DTD)localObject);
            }
            break;
          }
        }
        fSchemaValidator.endDocument(null);
      }
    }
    
    private void fillQName(org.apache.xerces.xni.QName paramQName, javax.xml.namespace.QName paramQName1)
    {
      fillQName(paramQName, paramQName1.getNamespaceURI(), paramQName1.getLocalPart(), paramQName1.getPrefix());
    }
    
    private void fillXMLAttributes(StartElement paramStartElement)
    {
      fAttributes.removeAllAttributes();
      Iterator localIterator = paramStartElement.getAttributes();
      while (localIterator.hasNext())
      {
        Attribute localAttribute = (Attribute)localIterator.next();
        fillQName(fAttributeQName, localAttribute.getName());
        String str = localAttribute.getDTDType();
        int i = fAttributes.getLength();
        fAttributes.addAttributeNS(fAttributeQName, str != null ? str : XMLSymbols.fCDATASymbol, localAttribute.getValue());
        fAttributes.setSpecified(i, localAttribute.isSpecified());
      }
    }
    
    private void fillDeclaredPrefixes(StartElement paramStartElement)
    {
      fillDeclaredPrefixes(paramStartElement.getNamespaces());
    }
    
    private void fillDeclaredPrefixes(EndElement paramEndElement)
    {
      fillDeclaredPrefixes(paramEndElement.getNamespaces());
    }
    
    private void fillDeclaredPrefixes(Iterator paramIterator)
    {
      fDeclaredPrefixes.clear();
      while (paramIterator.hasNext())
      {
        Namespace localNamespace = (Namespace)paramIterator.next();
        String str = localNamespace.getPrefix();
        fDeclaredPrefixes.add(str != null ? str : "");
      }
    }
    
    private void sendCharactersToValidator(String paramString)
    {
      if (paramString != null)
      {
        int i = paramString.length();
        int j = i & 0x3FF;
        if (j > 0)
        {
          paramString.getChars(0, j, fCharBuffer, 0);
          fTempString.setValues(fCharBuffer, 0, j);
          fSchemaValidator.characters(fTempString, null);
        }
        int k = j;
        while (k < i)
        {
          k += 1024;
          paramString.getChars(k, k, fCharBuffer, 0);
          fTempString.setValues(fCharBuffer, 0, 1024);
          fSchemaValidator.characters(fTempString, null);
        }
      }
    }
  }
  
  final class StreamHelper
  {
    StreamHelper() {}
    
    final void validate(XMLStreamReader paramXMLStreamReader, StAXResult paramStAXResult)
      throws SAXException, XMLStreamException
    {
      if (paramXMLStreamReader.hasNext())
      {
        int i = paramXMLStreamReader.getEventType();
        if ((i != 7) && (i != 1)) {
          throw new SAXException(JAXPValidationMessageFormatter.formatMessage(fComponentManager.getLocale(), "StAXIllegalInitialState", null));
        }
        fXMLStreamReaderLocation.setXMLStreamReader(paramXMLStreamReader);
        setup(fXMLStreamReaderLocation, paramStAXResult, Boolean.TRUE.equals(paramXMLStreamReader.getProperty("javax.xml.stream.isInterning")));
        fSchemaValidator.startDocument(fStAXLocationWrapper, null, fNamespaceContext, null);
        do
        {
          switch (i)
          {
          case 1: 
            StAXValidatorHelper.access$504(StAXValidatorHelper.this);
            fillQName(fElementQName, paramXMLStreamReader.getNamespaceURI(), paramXMLStreamReader.getLocalName(), paramXMLStreamReader.getPrefix());
            fillXMLAttributes(paramXMLStreamReader);
            fillDeclaredPrefixes(paramXMLStreamReader);
            fNamespaceContext.setNamespaceContext(paramXMLStreamReader.getNamespaceContext());
            fSchemaValidator.startElement(fElementQName, fAttributes, null);
            break;
          case 2: 
            fillQName(fElementQName, paramXMLStreamReader.getNamespaceURI(), paramXMLStreamReader.getLocalName(), paramXMLStreamReader.getPrefix());
            fillDeclaredPrefixes(paramXMLStreamReader);
            fNamespaceContext.setNamespaceContext(paramXMLStreamReader.getNamespaceContext());
            fSchemaValidator.endElement(fElementQName, null);
            StAXValidatorHelper.access$506(StAXValidatorHelper.this);
            break;
          case 4: 
          case 6: 
            fTempString.setValues(paramXMLStreamReader.getTextCharacters(), paramXMLStreamReader.getTextStart(), paramXMLStreamReader.getTextLength());
            fSchemaValidator.characters(fTempString, null);
            break;
          case 12: 
            fSchemaValidator.startCDATA(null);
            fTempString.setValues(paramXMLStreamReader.getTextCharacters(), paramXMLStreamReader.getTextStart(), paramXMLStreamReader.getTextLength());
            fSchemaValidator.characters(fTempString, null);
            fSchemaValidator.endCDATA(null);
            break;
          case 7: 
            StAXValidatorHelper.access$504(StAXValidatorHelper.this);
            if (fStAXValidatorHandler != null) {
              fStAXValidatorHandler.startDocument(paramXMLStreamReader);
            }
            break;
          case 3: 
            if (fStAXValidatorHandler != null) {
              fStAXValidatorHandler.processingInstruction(paramXMLStreamReader);
            }
            break;
          case 5: 
            if (fStAXValidatorHandler != null) {
              fStAXValidatorHandler.comment(paramXMLStreamReader);
            }
            break;
          case 9: 
            if (fStAXValidatorHandler != null) {
              fStAXValidatorHandler.entityReference(paramXMLStreamReader);
            }
            break;
          case 11: 
            processEntityDeclarations((List)paramXMLStreamReader.getProperty("javax.xml.stream.entities"));
          }
          i = paramXMLStreamReader.next();
        } while ((paramXMLStreamReader.hasNext()) && (fDepth > 0));
        fSchemaValidator.endDocument(null);
        if ((i == 8) && (fStAXValidatorHandler != null)) {
          fStAXValidatorHandler.endDocument(paramXMLStreamReader);
        }
      }
    }
    
    private void fillXMLAttributes(XMLStreamReader paramXMLStreamReader)
    {
      fAttributes.removeAllAttributes();
      int i = paramXMLStreamReader.getAttributeCount();
      for (int j = 0; j < i; j++)
      {
        fillQName(fAttributeQName, paramXMLStreamReader.getAttributeNamespace(j), paramXMLStreamReader.getAttributeLocalName(j), paramXMLStreamReader.getAttributePrefix(j));
        String str = paramXMLStreamReader.getAttributeType(j);
        fAttributes.addAttributeNS(fAttributeQName, str != null ? str : XMLSymbols.fCDATASymbol, paramXMLStreamReader.getAttributeValue(j));
        fAttributes.setSpecified(j, paramXMLStreamReader.isAttributeSpecified(j));
      }
    }
    
    private void fillDeclaredPrefixes(XMLStreamReader paramXMLStreamReader)
    {
      fDeclaredPrefixes.clear();
      int i = paramXMLStreamReader.getNamespaceCount();
      for (int j = 0; j < i; j++)
      {
        String str = paramXMLStreamReader.getNamespacePrefix(j);
        fDeclaredPrefixes.add(str != null ? str : "");
      }
    }
  }
}
