package org.apache.xerces.jaxp.validation;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.Comment;
import javax.xml.stream.events.DTD;
import javax.xml.stream.events.EndDocument;
import javax.xml.stream.events.EntityReference;
import javax.xml.stream.events.ProcessingInstruction;
import javax.xml.stream.events.StartDocument;
import javax.xml.transform.stax.StAXResult;
import org.apache.xerces.xni.XMLDocumentHandler;

abstract interface StAXDocumentHandler
  extends XMLDocumentHandler
{
  public abstract void setStAXResult(StAXResult paramStAXResult);
  
  public abstract void startDocument(XMLStreamReader paramXMLStreamReader)
    throws XMLStreamException;
  
  public abstract void endDocument(XMLStreamReader paramXMLStreamReader)
    throws XMLStreamException;
  
  public abstract void comment(XMLStreamReader paramXMLStreamReader)
    throws XMLStreamException;
  
  public abstract void processingInstruction(XMLStreamReader paramXMLStreamReader)
    throws XMLStreamException;
  
  public abstract void entityReference(XMLStreamReader paramXMLStreamReader)
    throws XMLStreamException;
  
  public abstract void startDocument(StartDocument paramStartDocument)
    throws XMLStreamException;
  
  public abstract void endDocument(EndDocument paramEndDocument)
    throws XMLStreamException;
  
  public abstract void doctypeDecl(DTD paramDTD)
    throws XMLStreamException;
  
  public abstract void characters(Characters paramCharacters)
    throws XMLStreamException;
  
  public abstract void cdata(Characters paramCharacters)
    throws XMLStreamException;
  
  public abstract void comment(Comment paramComment)
    throws XMLStreamException;
  
  public abstract void processingInstruction(ProcessingInstruction paramProcessingInstruction)
    throws XMLStreamException;
  
  public abstract void entityReference(EntityReference paramEntityReference)
    throws XMLStreamException;
  
  public abstract void setIgnoringCharacters(boolean paramBoolean);
}
