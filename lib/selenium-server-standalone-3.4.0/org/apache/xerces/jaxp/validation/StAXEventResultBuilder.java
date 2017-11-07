package org.apache.xerces.jaxp.validation;

import java.util.Iterator;
import java.util.NoSuchElementException;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.Comment;
import javax.xml.stream.events.DTD;
import javax.xml.stream.events.EndDocument;
import javax.xml.stream.events.EntityReference;
import javax.xml.stream.events.ProcessingInstruction;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.stax.StAXResult;
import org.apache.xerces.util.JAXPNamespaceContextWrapper;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLDocumentSource;

final class StAXEventResultBuilder
  implements StAXDocumentHandler
{
  private XMLEventWriter fEventWriter;
  private final XMLEventFactory fEventFactory;
  private final StAXValidatorHelper fStAXValidatorHelper;
  private final JAXPNamespaceContextWrapper fNamespaceContext;
  private boolean fIgnoreChars;
  private boolean fInCDATA;
  private final QName fAttrName = new QName();
  private static final Iterator EMPTY_COLLECTION_ITERATOR = new Iterator()
  {
    public boolean hasNext()
    {
      return false;
    }
    
    public Object next()
    {
      throw new NoSuchElementException();
    }
    
    public void remove()
    {
      throw new UnsupportedOperationException();
    }
  };
  
  public StAXEventResultBuilder(StAXValidatorHelper paramStAXValidatorHelper, JAXPNamespaceContextWrapper paramJAXPNamespaceContextWrapper)
  {
    fStAXValidatorHelper = paramStAXValidatorHelper;
    fNamespaceContext = paramJAXPNamespaceContextWrapper;
    fEventFactory = XMLEventFactory.newInstance();
  }
  
  public void setStAXResult(StAXResult paramStAXResult)
  {
    fIgnoreChars = false;
    fInCDATA = false;
    fEventWriter = (paramStAXResult != null ? paramStAXResult.getXMLEventWriter() : null);
  }
  
  public void startDocument(XMLStreamReader paramXMLStreamReader)
    throws XMLStreamException
  {
    String str1 = paramXMLStreamReader.getVersion();
    String str2 = paramXMLStreamReader.getCharacterEncodingScheme();
    boolean bool = paramXMLStreamReader.standaloneSet();
    fEventWriter.add(fEventFactory.createStartDocument(str2 != null ? str2 : "UTF-8", str1 != null ? str1 : "1.0", bool));
  }
  
  public void endDocument(XMLStreamReader paramXMLStreamReader)
    throws XMLStreamException
  {
    fEventWriter.add(fEventFactory.createEndDocument());
    fEventWriter.flush();
  }
  
  public void comment(XMLStreamReader paramXMLStreamReader)
    throws XMLStreamException
  {
    fEventWriter.add(fEventFactory.createComment(paramXMLStreamReader.getText()));
  }
  
  public void processingInstruction(XMLStreamReader paramXMLStreamReader)
    throws XMLStreamException
  {
    String str = paramXMLStreamReader.getPIData();
    fEventWriter.add(fEventFactory.createProcessingInstruction(paramXMLStreamReader.getPITarget(), str != null ? str : ""));
  }
  
  public void entityReference(XMLStreamReader paramXMLStreamReader)
    throws XMLStreamException
  {
    String str = paramXMLStreamReader.getLocalName();
    fEventWriter.add(fEventFactory.createEntityReference(str, fStAXValidatorHelper.getEntityDeclaration(str)));
  }
  
  public void startDocument(StartDocument paramStartDocument)
    throws XMLStreamException
  {
    fEventWriter.add(paramStartDocument);
  }
  
  public void endDocument(EndDocument paramEndDocument)
    throws XMLStreamException
  {
    fEventWriter.add(paramEndDocument);
    fEventWriter.flush();
  }
  
  public void doctypeDecl(DTD paramDTD)
    throws XMLStreamException
  {
    fEventWriter.add(paramDTD);
  }
  
  public void characters(Characters paramCharacters)
    throws XMLStreamException
  {
    fEventWriter.add(paramCharacters);
  }
  
  public void cdata(Characters paramCharacters)
    throws XMLStreamException
  {
    fEventWriter.add(paramCharacters);
  }
  
  public void comment(Comment paramComment)
    throws XMLStreamException
  {
    fEventWriter.add(paramComment);
  }
  
  public void processingInstruction(ProcessingInstruction paramProcessingInstruction)
    throws XMLStreamException
  {
    fEventWriter.add(paramProcessingInstruction);
  }
  
  public void entityReference(EntityReference paramEntityReference)
    throws XMLStreamException
  {
    fEventWriter.add(paramEntityReference);
  }
  
  public void setIgnoringCharacters(boolean paramBoolean)
  {
    fIgnoreChars = paramBoolean;
  }
  
  public void startDocument(XMLLocator paramXMLLocator, String paramString, org.apache.xerces.xni.NamespaceContext paramNamespaceContext, Augmentations paramAugmentations)
    throws XNIException
  {}
  
  public void xmlDecl(String paramString1, String paramString2, String paramString3, Augmentations paramAugmentations)
    throws XNIException
  {}
  
  public void doctypeDecl(String paramString1, String paramString2, String paramString3, Augmentations paramAugmentations)
    throws XNIException
  {}
  
  public void comment(XMLString paramXMLString, Augmentations paramAugmentations)
    throws XNIException
  {}
  
  public void processingInstruction(String paramString, XMLString paramXMLString, Augmentations paramAugmentations)
    throws XNIException
  {}
  
  public void startElement(QName paramQName, XMLAttributes paramXMLAttributes, Augmentations paramAugmentations)
    throws XNIException
  {
    try
    {
      int i = paramXMLAttributes.getLength();
      if (i == 0)
      {
        XMLEvent localXMLEvent = fStAXValidatorHelper.getCurrentEvent();
        if (localXMLEvent != null)
        {
          fEventWriter.add(localXMLEvent);
          return;
        }
      }
      fEventWriter.add(fEventFactory.createStartElement(prefix, uri != null ? uri : "", localpart, getAttributeIterator(paramXMLAttributes, i), getNamespaceIterator(), fNamespaceContext.getNamespaceContext()));
    }
    catch (XMLStreamException localXMLStreamException)
    {
      throw new XNIException(localXMLStreamException);
    }
  }
  
  public void emptyElement(QName paramQName, XMLAttributes paramXMLAttributes, Augmentations paramAugmentations)
    throws XNIException
  {
    startElement(paramQName, paramXMLAttributes, paramAugmentations);
    endElement(paramQName, paramAugmentations);
  }
  
  public void startGeneralEntity(String paramString1, XMLResourceIdentifier paramXMLResourceIdentifier, String paramString2, Augmentations paramAugmentations)
    throws XNIException
  {}
  
  public void textDecl(String paramString1, String paramString2, Augmentations paramAugmentations)
    throws XNIException
  {}
  
  public void endGeneralEntity(String paramString, Augmentations paramAugmentations)
    throws XNIException
  {}
  
  public void characters(XMLString paramXMLString, Augmentations paramAugmentations)
    throws XNIException
  {
    if (!fIgnoreChars) {
      try
      {
        if (!fInCDATA) {
          fEventWriter.add(fEventFactory.createCharacters(paramXMLString.toString()));
        } else {
          fEventWriter.add(fEventFactory.createCData(paramXMLString.toString()));
        }
      }
      catch (XMLStreamException localXMLStreamException)
      {
        throw new XNIException(localXMLStreamException);
      }
    }
  }
  
  public void ignorableWhitespace(XMLString paramXMLString, Augmentations paramAugmentations)
    throws XNIException
  {
    characters(paramXMLString, paramAugmentations);
  }
  
  public void endElement(QName paramQName, Augmentations paramAugmentations)
    throws XNIException
  {
    try
    {
      XMLEvent localXMLEvent = fStAXValidatorHelper.getCurrentEvent();
      if (localXMLEvent != null) {
        fEventWriter.add(localXMLEvent);
      } else {
        fEventWriter.add(fEventFactory.createEndElement(prefix, uri, localpart, getNamespaceIterator()));
      }
    }
    catch (XMLStreamException localXMLStreamException)
    {
      throw new XNIException(localXMLStreamException);
    }
  }
  
  public void startCDATA(Augmentations paramAugmentations)
    throws XNIException
  {
    fInCDATA = true;
  }
  
  public void endCDATA(Augmentations paramAugmentations)
    throws XNIException
  {
    fInCDATA = false;
  }
  
  public void endDocument(Augmentations paramAugmentations)
    throws XNIException
  {}
  
  public void setDocumentSource(XMLDocumentSource paramXMLDocumentSource) {}
  
  public XMLDocumentSource getDocumentSource()
  {
    return null;
  }
  
  private Iterator getAttributeIterator(XMLAttributes paramXMLAttributes, int paramInt)
  {
    return paramInt > 0 ? new AttributeIterator(paramXMLAttributes, paramInt) : EMPTY_COLLECTION_ITERATOR;
  }
  
  private Iterator getNamespaceIterator()
  {
    int i = fNamespaceContext.getDeclaredPrefixCount();
    return i > 0 ? new NamespaceIterator(i) : EMPTY_COLLECTION_ITERATOR;
  }
  
  final class NamespaceIterator
    implements Iterator
  {
    javax.xml.namespace.NamespaceContext fNC = fNamespaceContext.getNamespaceContext();
    int fIndex = 0;
    int fEnd;
    
    NamespaceIterator(int paramInt)
    {
      fEnd = paramInt;
    }
    
    public boolean hasNext()
    {
      if (fIndex < fEnd) {
        return true;
      }
      fNC = null;
      return false;
    }
    
    public Object next()
    {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      String str1 = fNamespaceContext.getDeclaredPrefixAt(fIndex++);
      String str2 = fNC.getNamespaceURI(str1);
      if (str1.length() == 0) {
        return fEventFactory.createNamespace(str2 != null ? str2 : "");
      }
      return fEventFactory.createNamespace(str1, str2 != null ? str2 : "");
    }
    
    public void remove()
    {
      throw new UnsupportedOperationException();
    }
  }
  
  final class AttributeIterator
    implements Iterator
  {
    XMLAttributes fAttributes;
    int fIndex;
    int fEnd;
    
    AttributeIterator(XMLAttributes paramXMLAttributes, int paramInt)
    {
      fAttributes = paramXMLAttributes;
      fIndex = 0;
      fEnd = paramInt;
    }
    
    public boolean hasNext()
    {
      if (fIndex < fEnd) {
        return true;
      }
      fAttributes = null;
      return false;
    }
    
    public Object next()
    {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      fAttributes.getName(fIndex, fAttrName);
      return fEventFactory.createAttribute(fAttrName.prefix, fAttrName.uri != null ? fAttrName.uri : "", fAttrName.localpart, fAttributes.getValue(fIndex++));
    }
    
    public void remove()
    {
      throw new UnsupportedOperationException();
    }
  }
}
