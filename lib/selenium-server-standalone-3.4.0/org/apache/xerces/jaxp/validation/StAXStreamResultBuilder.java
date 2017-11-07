package org.apache.xerces.jaxp.validation;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.Comment;
import javax.xml.stream.events.DTD;
import javax.xml.stream.events.EndDocument;
import javax.xml.stream.events.EntityReference;
import javax.xml.stream.events.ProcessingInstruction;
import javax.xml.stream.events.StartDocument;
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

final class StAXStreamResultBuilder
  implements StAXDocumentHandler
{
  private XMLStreamWriter fStreamWriter;
  private final JAXPNamespaceContextWrapper fNamespaceContext;
  private boolean fIgnoreChars;
  private boolean fInCDATA;
  private final QName fAttrName = new QName();
  
  public StAXStreamResultBuilder(JAXPNamespaceContextWrapper paramJAXPNamespaceContextWrapper)
  {
    fNamespaceContext = paramJAXPNamespaceContextWrapper;
  }
  
  public void setStAXResult(StAXResult paramStAXResult)
  {
    fIgnoreChars = false;
    fInCDATA = false;
    fAttrName.clear();
    fStreamWriter = (paramStAXResult != null ? paramStAXResult.getXMLStreamWriter() : null);
  }
  
  public void startDocument(XMLStreamReader paramXMLStreamReader)
    throws XMLStreamException
  {
    String str1 = paramXMLStreamReader.getVersion();
    String str2 = paramXMLStreamReader.getCharacterEncodingScheme();
    fStreamWriter.writeStartDocument(str2 != null ? str2 : "UTF-8", str1 != null ? str1 : "1.0");
  }
  
  public void endDocument(XMLStreamReader paramXMLStreamReader)
    throws XMLStreamException
  {
    fStreamWriter.writeEndDocument();
    fStreamWriter.flush();
  }
  
  public void comment(XMLStreamReader paramXMLStreamReader)
    throws XMLStreamException
  {
    fStreamWriter.writeComment(paramXMLStreamReader.getText());
  }
  
  public void processingInstruction(XMLStreamReader paramXMLStreamReader)
    throws XMLStreamException
  {
    String str = paramXMLStreamReader.getPIData();
    if ((str != null) && (str.length() > 0)) {
      fStreamWriter.writeProcessingInstruction(paramXMLStreamReader.getPITarget(), str);
    } else {
      fStreamWriter.writeProcessingInstruction(paramXMLStreamReader.getPITarget());
    }
  }
  
  public void entityReference(XMLStreamReader paramXMLStreamReader)
    throws XMLStreamException
  {
    fStreamWriter.writeEntityRef(paramXMLStreamReader.getLocalName());
  }
  
  public void startDocument(StartDocument paramStartDocument)
    throws XMLStreamException
  {
    String str1 = paramStartDocument.getVersion();
    String str2 = paramStartDocument.getCharacterEncodingScheme();
    fStreamWriter.writeStartDocument(str2 != null ? str2 : "UTF-8", str1 != null ? str1 : "1.0");
  }
  
  public void endDocument(EndDocument paramEndDocument)
    throws XMLStreamException
  {
    fStreamWriter.writeEndDocument();
    fStreamWriter.flush();
  }
  
  public void doctypeDecl(DTD paramDTD)
    throws XMLStreamException
  {
    fStreamWriter.writeDTD(paramDTD.getDocumentTypeDeclaration());
  }
  
  public void characters(Characters paramCharacters)
    throws XMLStreamException
  {
    fStreamWriter.writeCharacters(paramCharacters.getData());
  }
  
  public void cdata(Characters paramCharacters)
    throws XMLStreamException
  {
    fStreamWriter.writeCData(paramCharacters.getData());
  }
  
  public void comment(Comment paramComment)
    throws XMLStreamException
  {
    fStreamWriter.writeComment(paramComment.getText());
  }
  
  public void processingInstruction(ProcessingInstruction paramProcessingInstruction)
    throws XMLStreamException
  {
    String str = paramProcessingInstruction.getData();
    if ((str != null) && (str.length() > 0)) {
      fStreamWriter.writeProcessingInstruction(paramProcessingInstruction.getTarget(), str);
    } else {
      fStreamWriter.writeProcessingInstruction(paramProcessingInstruction.getTarget());
    }
  }
  
  public void entityReference(EntityReference paramEntityReference)
    throws XMLStreamException
  {
    fStreamWriter.writeEntityRef(paramEntityReference.getName());
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
      if (prefix.length() > 0) {
        fStreamWriter.writeStartElement(prefix, localpart, uri != null ? uri : "");
      } else if (uri != null) {
        fStreamWriter.writeStartElement(uri, localpart);
      } else {
        fStreamWriter.writeStartElement(localpart);
      }
      int i = fNamespaceContext.getDeclaredPrefixCount();
      javax.xml.namespace.NamespaceContext localNamespaceContext = fNamespaceContext.getNamespaceContext();
      for (int j = 0; j < i; j++)
      {
        String str1 = fNamespaceContext.getDeclaredPrefixAt(j);
        String str2 = localNamespaceContext.getNamespaceURI(str1);
        if (str1.length() == 0) {
          fStreamWriter.writeDefaultNamespace(str2 != null ? str2 : "");
        } else {
          fStreamWriter.writeNamespace(str1, str2 != null ? str2 : "");
        }
      }
      i = paramXMLAttributes.getLength();
      for (int k = 0; k < i; k++)
      {
        paramXMLAttributes.getName(k, fAttrName);
        if (fAttrName.prefix.length() > 0) {
          fStreamWriter.writeAttribute(fAttrName.prefix, fAttrName.uri != null ? fAttrName.uri : "", fAttrName.localpart, paramXMLAttributes.getValue(k));
        } else if (fAttrName.uri != null) {
          fStreamWriter.writeAttribute(fAttrName.uri, fAttrName.localpart, paramXMLAttributes.getValue(k));
        } else {
          fStreamWriter.writeAttribute(fAttrName.localpart, paramXMLAttributes.getValue(k));
        }
      }
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
          fStreamWriter.writeCharacters(ch, offset, length);
        } else {
          fStreamWriter.writeCData(paramXMLString.toString());
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
      fStreamWriter.writeEndElement();
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
}
