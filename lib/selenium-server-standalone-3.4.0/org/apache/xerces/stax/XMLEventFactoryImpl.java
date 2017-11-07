package org.apache.xerces.stax;

import java.util.Iterator;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLEventFactory;
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
import org.apache.xerces.stax.events.AttributeImpl;
import org.apache.xerces.stax.events.CharactersImpl;
import org.apache.xerces.stax.events.CommentImpl;
import org.apache.xerces.stax.events.DTDImpl;
import org.apache.xerces.stax.events.EndDocumentImpl;
import org.apache.xerces.stax.events.EndElementImpl;
import org.apache.xerces.stax.events.EntityReferenceImpl;
import org.apache.xerces.stax.events.NamespaceImpl;
import org.apache.xerces.stax.events.ProcessingInstructionImpl;
import org.apache.xerces.stax.events.StartDocumentImpl;
import org.apache.xerces.stax.events.StartElementImpl;

public final class XMLEventFactoryImpl
  extends XMLEventFactory
{
  private Location fLocation;
  
  public XMLEventFactoryImpl() {}
  
  public void setLocation(Location paramLocation)
  {
    fLocation = paramLocation;
  }
  
  public Attribute createAttribute(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    return createAttribute(new QName(paramString2, paramString3, paramString1), paramString4);
  }
  
  public Attribute createAttribute(String paramString1, String paramString2)
  {
    return createAttribute(new QName(paramString1), paramString2);
  }
  
  public Attribute createAttribute(QName paramQName, String paramString)
  {
    return new AttributeImpl(paramQName, paramString, "CDATA", true, fLocation);
  }
  
  public Namespace createNamespace(String paramString)
  {
    return createNamespace("", paramString);
  }
  
  public Namespace createNamespace(String paramString1, String paramString2)
  {
    return new NamespaceImpl(paramString1, paramString2, fLocation);
  }
  
  public StartElement createStartElement(QName paramQName, Iterator paramIterator1, Iterator paramIterator2)
  {
    return createStartElement(paramQName, paramIterator1, paramIterator2, null);
  }
  
  public StartElement createStartElement(String paramString1, String paramString2, String paramString3)
  {
    return createStartElement(new QName(paramString2, paramString3, paramString1), null, null);
  }
  
  public StartElement createStartElement(String paramString1, String paramString2, String paramString3, Iterator paramIterator1, Iterator paramIterator2)
  {
    return createStartElement(new QName(paramString2, paramString3, paramString1), paramIterator1, paramIterator2);
  }
  
  public StartElement createStartElement(String paramString1, String paramString2, String paramString3, Iterator paramIterator1, Iterator paramIterator2, NamespaceContext paramNamespaceContext)
  {
    return createStartElement(new QName(paramString2, paramString3, paramString1), paramIterator1, paramIterator2, paramNamespaceContext);
  }
  
  private StartElement createStartElement(QName paramQName, Iterator paramIterator1, Iterator paramIterator2, NamespaceContext paramNamespaceContext)
  {
    return new StartElementImpl(paramQName, paramIterator1, paramIterator2, paramNamespaceContext, fLocation);
  }
  
  public EndElement createEndElement(QName paramQName, Iterator paramIterator)
  {
    return new EndElementImpl(paramQName, paramIterator, fLocation);
  }
  
  public EndElement createEndElement(String paramString1, String paramString2, String paramString3)
  {
    return createEndElement(new QName(paramString2, paramString3, paramString1), null);
  }
  
  public EndElement createEndElement(String paramString1, String paramString2, String paramString3, Iterator paramIterator)
  {
    return createEndElement(new QName(paramString2, paramString3, paramString1), paramIterator);
  }
  
  public Characters createCharacters(String paramString)
  {
    return new CharactersImpl(paramString, 4, fLocation);
  }
  
  public Characters createCData(String paramString)
  {
    return new CharactersImpl(paramString, 12, fLocation);
  }
  
  public Characters createSpace(String paramString)
  {
    return createCharacters(paramString);
  }
  
  public Characters createIgnorableSpace(String paramString)
  {
    return new CharactersImpl(paramString, 6, fLocation);
  }
  
  public StartDocument createStartDocument()
  {
    return createStartDocument(null, null);
  }
  
  public StartDocument createStartDocument(String paramString1, String paramString2, boolean paramBoolean)
  {
    return new StartDocumentImpl(paramString1, paramString1 != null, paramBoolean, true, paramString2, fLocation);
  }
  
  public StartDocument createStartDocument(String paramString1, String paramString2)
  {
    return new StartDocumentImpl(paramString1, paramString1 != null, false, false, paramString2, fLocation);
  }
  
  public StartDocument createStartDocument(String paramString)
  {
    return createStartDocument(paramString, null);
  }
  
  public EndDocument createEndDocument()
  {
    return new EndDocumentImpl(fLocation);
  }
  
  public EntityReference createEntityReference(String paramString, EntityDeclaration paramEntityDeclaration)
  {
    return new EntityReferenceImpl(paramString, paramEntityDeclaration, fLocation);
  }
  
  public Comment createComment(String paramString)
  {
    return new CommentImpl(paramString, fLocation);
  }
  
  public ProcessingInstruction createProcessingInstruction(String paramString1, String paramString2)
  {
    return new ProcessingInstructionImpl(paramString1, paramString2, fLocation);
  }
  
  public DTD createDTD(String paramString)
  {
    return new DTDImpl(paramString, fLocation);
  }
}
