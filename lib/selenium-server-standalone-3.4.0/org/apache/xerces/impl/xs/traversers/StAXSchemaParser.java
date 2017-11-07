package org.apache.xerces.impl.xs.traversers;

import java.util.ArrayList;
import java.util.Iterator;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.Namespace;
import javax.xml.stream.events.ProcessingInstruction;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import org.apache.xerces.impl.xs.opti.SchemaDOMParser;
import org.apache.xerces.util.JAXPNamespaceContextWrapper;
import org.apache.xerces.util.StAXLocationWrapper;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLAttributesImpl;
import org.apache.xerces.util.XMLStringBuffer;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.w3c.dom.Document;

final class StAXSchemaParser
{
  private static final int CHUNK_SIZE = 1024;
  private static final int CHUNK_MASK = 1023;
  private final char[] fCharBuffer = new char['Ѐ'];
  private SymbolTable fSymbolTable;
  private SchemaDOMParser fSchemaDOMParser;
  private final StAXLocationWrapper fLocationWrapper = new StAXLocationWrapper();
  private final JAXPNamespaceContextWrapper fNamespaceContext = new JAXPNamespaceContextWrapper(fSymbolTable);
  private final org.apache.xerces.xni.QName fElementQName = new org.apache.xerces.xni.QName();
  private final org.apache.xerces.xni.QName fAttributeQName = new org.apache.xerces.xni.QName();
  private final XMLAttributesImpl fAttributes = new XMLAttributesImpl();
  private final XMLString fTempString = new XMLString();
  private final ArrayList fDeclaredPrefixes = new ArrayList();
  private final XMLStringBuffer fStringBuffer = new XMLStringBuffer();
  private int fDepth;
  
  public StAXSchemaParser()
  {
    fNamespaceContext.setDeclaredPrefixes(fDeclaredPrefixes);
  }
  
  public void reset(SchemaDOMParser paramSchemaDOMParser, SymbolTable paramSymbolTable)
  {
    fSchemaDOMParser = paramSchemaDOMParser;
    fSymbolTable = paramSymbolTable;
    fNamespaceContext.setSymbolTable(fSymbolTable);
    fNamespaceContext.reset();
  }
  
  public Document getDocument()
  {
    return fSchemaDOMParser.getDocument();
  }
  
  public void parse(XMLEventReader paramXMLEventReader)
    throws XMLStreamException, XNIException
  {
    XMLEvent localXMLEvent = paramXMLEventReader.peek();
    if (localXMLEvent != null)
    {
      int i = localXMLEvent.getEventType();
      if ((i != 7) && (i != 1)) {
        throw new XMLStreamException();
      }
      fLocationWrapper.setLocation(localXMLEvent.getLocation());
      fSchemaDOMParser.startDocument(fLocationWrapper, null, fNamespaceContext, null);
      while (paramXMLEventReader.hasNext())
      {
        localXMLEvent = paramXMLEventReader.nextEvent();
        i = localXMLEvent.getEventType();
        switch (i)
        {
        case 1: 
          fDepth += 1;
          StartElement localStartElement = localXMLEvent.asStartElement();
          fillQName(fElementQName, localStartElement.getName());
          fLocationWrapper.setLocation(localStartElement.getLocation());
          fNamespaceContext.setNamespaceContext(localStartElement.getNamespaceContext());
          fillXMLAttributes(localStartElement);
          fillDeclaredPrefixes(localStartElement);
          addNamespaceDeclarations();
          fNamespaceContext.pushContext();
          fSchemaDOMParser.startElement(fElementQName, fAttributes, null);
          break;
        case 2: 
          EndElement localEndElement = localXMLEvent.asEndElement();
          fillQName(fElementQName, localEndElement.getName());
          fillDeclaredPrefixes(localEndElement);
          fLocationWrapper.setLocation(localEndElement.getLocation());
          fSchemaDOMParser.endElement(fElementQName, null);
          fNamespaceContext.popContext();
          fDepth -= 1;
          if (fDepth > 0) {
            break;
          }
          break;
        case 4: 
          sendCharactersToSchemaParser(localXMLEvent.asCharacters().getData(), false);
          break;
        case 6: 
          sendCharactersToSchemaParser(localXMLEvent.asCharacters().getData(), true);
          break;
        case 12: 
          fSchemaDOMParser.startCDATA(null);
          sendCharactersToSchemaParser(localXMLEvent.asCharacters().getData(), false);
          fSchemaDOMParser.endCDATA(null);
          break;
        case 3: 
          ProcessingInstruction localProcessingInstruction = (ProcessingInstruction)localXMLEvent;
          fillProcessingInstruction(localProcessingInstruction.getData());
          fSchemaDOMParser.processingInstruction(localProcessingInstruction.getTarget(), fTempString, null);
          break;
        case 11: 
          break;
        case 9: 
          break;
        case 5: 
          break;
        case 7: 
          fDepth += 1;
        }
      }
      fLocationWrapper.setLocation(null);
      fNamespaceContext.setNamespaceContext(null);
      fSchemaDOMParser.endDocument(null);
    }
  }
  
  public void parse(XMLStreamReader paramXMLStreamReader)
    throws XMLStreamException, XNIException
  {
    if (paramXMLStreamReader.hasNext())
    {
      int i = paramXMLStreamReader.getEventType();
      if ((i != 7) && (i != 1)) {
        throw new XMLStreamException();
      }
      fLocationWrapper.setLocation(paramXMLStreamReader.getLocation());
      fSchemaDOMParser.startDocument(fLocationWrapper, null, fNamespaceContext, null);
      int j = 1;
      while (paramXMLStreamReader.hasNext())
      {
        if (j == 0) {
          i = paramXMLStreamReader.next();
        } else {
          j = 0;
        }
        switch (i)
        {
        case 1: 
          fDepth += 1;
          fLocationWrapper.setLocation(paramXMLStreamReader.getLocation());
          fNamespaceContext.setNamespaceContext(paramXMLStreamReader.getNamespaceContext());
          fillQName(fElementQName, paramXMLStreamReader.getNamespaceURI(), paramXMLStreamReader.getLocalName(), paramXMLStreamReader.getPrefix());
          fillXMLAttributes(paramXMLStreamReader);
          fillDeclaredPrefixes(paramXMLStreamReader);
          addNamespaceDeclarations();
          fNamespaceContext.pushContext();
          fSchemaDOMParser.startElement(fElementQName, fAttributes, null);
          break;
        case 2: 
          fLocationWrapper.setLocation(paramXMLStreamReader.getLocation());
          fNamespaceContext.setNamespaceContext(paramXMLStreamReader.getNamespaceContext());
          fillQName(fElementQName, paramXMLStreamReader.getNamespaceURI(), paramXMLStreamReader.getLocalName(), paramXMLStreamReader.getPrefix());
          fillDeclaredPrefixes(paramXMLStreamReader);
          fSchemaDOMParser.endElement(fElementQName, null);
          fNamespaceContext.popContext();
          fDepth -= 1;
          if (fDepth > 0) {
            break;
          }
          break;
        case 4: 
          fTempString.setValues(paramXMLStreamReader.getTextCharacters(), paramXMLStreamReader.getTextStart(), paramXMLStreamReader.getTextLength());
          fSchemaDOMParser.characters(fTempString, null);
          break;
        case 6: 
          fTempString.setValues(paramXMLStreamReader.getTextCharacters(), paramXMLStreamReader.getTextStart(), paramXMLStreamReader.getTextLength());
          fSchemaDOMParser.ignorableWhitespace(fTempString, null);
          break;
        case 12: 
          fSchemaDOMParser.startCDATA(null);
          fTempString.setValues(paramXMLStreamReader.getTextCharacters(), paramXMLStreamReader.getTextStart(), paramXMLStreamReader.getTextLength());
          fSchemaDOMParser.characters(fTempString, null);
          fSchemaDOMParser.endCDATA(null);
          break;
        case 3: 
          fillProcessingInstruction(paramXMLStreamReader.getPIData());
          fSchemaDOMParser.processingInstruction(paramXMLStreamReader.getPITarget(), fTempString, null);
          break;
        case 11: 
          break;
        case 9: 
          break;
        case 5: 
          break;
        case 7: 
          fDepth += 1;
        }
      }
      fLocationWrapper.setLocation(null);
      fNamespaceContext.setNamespaceContext(null);
      fSchemaDOMParser.endDocument(null);
    }
  }
  
  private void sendCharactersToSchemaParser(String paramString, boolean paramBoolean)
  {
    if (paramString != null)
    {
      int i = paramString.length();
      int j = i & 0x3FF;
      if (j > 0)
      {
        paramString.getChars(0, j, fCharBuffer, 0);
        fTempString.setValues(fCharBuffer, 0, j);
        if (paramBoolean) {
          fSchemaDOMParser.ignorableWhitespace(fTempString, null);
        } else {
          fSchemaDOMParser.characters(fTempString, null);
        }
      }
      int k = j;
      while (k < i)
      {
        k += 1024;
        paramString.getChars(k, k, fCharBuffer, 0);
        fTempString.setValues(fCharBuffer, 0, 1024);
        if (paramBoolean) {
          fSchemaDOMParser.ignorableWhitespace(fTempString, null);
        } else {
          fSchemaDOMParser.characters(fTempString, null);
        }
      }
    }
  }
  
  private void fillProcessingInstruction(String paramString)
  {
    int i = paramString.length();
    char[] arrayOfChar = fCharBuffer;
    if (arrayOfChar.length < i) {
      arrayOfChar = paramString.toCharArray();
    } else {
      paramString.getChars(0, i, arrayOfChar, 0);
    }
    fTempString.setValues(arrayOfChar, 0, i);
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
  
  private void addNamespaceDeclarations()
  {
    String str1 = null;
    Object localObject = null;
    String str2 = null;
    String str3 = null;
    String str4 = null;
    Iterator localIterator = fDeclaredPrefixes.iterator();
    while (localIterator.hasNext())
    {
      str3 = (String)localIterator.next();
      str4 = fNamespaceContext.getURI(str3);
      if (str3.length() > 0)
      {
        str1 = XMLSymbols.PREFIX_XMLNS;
        localObject = str3;
        fStringBuffer.clear();
        fStringBuffer.append(str1);
        fStringBuffer.append(':');
        fStringBuffer.append((String)localObject);
        str2 = fSymbolTable.addSymbol(fStringBuffer.ch, fStringBuffer.offset, fStringBuffer.length);
      }
      else
      {
        str1 = XMLSymbols.EMPTY_STRING;
        localObject = XMLSymbols.PREFIX_XMLNS;
        str2 = XMLSymbols.PREFIX_XMLNS;
      }
      fAttributeQName.setValues(str1, (String)localObject, str2, NamespaceContext.XMLNS_URI);
      fAttributes.addAttribute(fAttributeQName, XMLSymbols.fCDATASymbol, str4 != null ? str4 : XMLSymbols.EMPTY_STRING);
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
  
  private void fillQName(org.apache.xerces.xni.QName paramQName, javax.xml.namespace.QName paramQName1)
  {
    fillQName(paramQName, paramQName1.getNamespaceURI(), paramQName1.getLocalPart(), paramQName1.getPrefix());
  }
  
  final void fillQName(org.apache.xerces.xni.QName paramQName, String paramString1, String paramString2, String paramString3)
  {
    paramString1 = (paramString1 != null) && (paramString1.length() > 0) ? fSymbolTable.addSymbol(paramString1) : null;
    paramString2 = paramString2 != null ? fSymbolTable.addSymbol(paramString2) : XMLSymbols.EMPTY_STRING;
    paramString3 = (paramString3 != null) && (paramString3.length() > 0) ? fSymbolTable.addSymbol(paramString3) : XMLSymbols.EMPTY_STRING;
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
}
