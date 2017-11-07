package org.apache.xerces.jaxp.validation;

import javax.xml.transform.dom.DOMResult;
import org.apache.xerces.xni.XMLDocumentHandler;
import org.apache.xerces.xni.XNIException;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

abstract interface DOMDocumentHandler
  extends XMLDocumentHandler
{
  public abstract void setDOMResult(DOMResult paramDOMResult);
  
  public abstract void doctypeDecl(DocumentType paramDocumentType)
    throws XNIException;
  
  public abstract void characters(Text paramText)
    throws XNIException;
  
  public abstract void cdata(CDATASection paramCDATASection)
    throws XNIException;
  
  public abstract void comment(Comment paramComment)
    throws XNIException;
  
  public abstract void processingInstruction(ProcessingInstruction paramProcessingInstruction)
    throws XNIException;
  
  public abstract void setIgnoringCharacters(boolean paramBoolean);
}
