package org.apache.xerces.dom;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class PSVIDocumentImpl
  extends DocumentImpl
{
  static final long serialVersionUID = -8822220250676434522L;
  
  public PSVIDocumentImpl() {}
  
  public PSVIDocumentImpl(DocumentType paramDocumentType)
  {
    super(paramDocumentType);
  }
  
  public Node cloneNode(boolean paramBoolean)
  {
    PSVIDocumentImpl localPSVIDocumentImpl = new PSVIDocumentImpl();
    callUserDataHandlers(this, localPSVIDocumentImpl, (short)1);
    cloneNode(localPSVIDocumentImpl, paramBoolean);
    mutationEvents = mutationEvents;
    return localPSVIDocumentImpl;
  }
  
  public DOMImplementation getImplementation()
  {
    return PSVIDOMImplementationImpl.getDOMImplementation();
  }
  
  public Element createElementNS(String paramString1, String paramString2)
    throws DOMException
  {
    return new PSVIElementNSImpl(this, paramString1, paramString2);
  }
  
  public Element createElementNS(String paramString1, String paramString2, String paramString3)
    throws DOMException
  {
    return new PSVIElementNSImpl(this, paramString1, paramString2, paramString3);
  }
  
  public Attr createAttributeNS(String paramString1, String paramString2)
    throws DOMException
  {
    return new PSVIAttrNSImpl(this, paramString1, paramString2);
  }
  
  public Attr createAttributeNS(String paramString1, String paramString2, String paramString3)
    throws DOMException
  {
    return new PSVIAttrNSImpl(this, paramString1, paramString2, paramString3);
  }
  
  public DOMConfiguration getDomConfig()
  {
    super.getDomConfig();
    return fConfiguration;
  }
  
  private void writeObject(ObjectOutputStream paramObjectOutputStream)
    throws IOException
  {
    throw new NotSerializableException(getClass().getName());
  }
  
  private void readObject(ObjectInputStream paramObjectInputStream)
    throws IOException, ClassNotFoundException
  {
    throw new NotSerializableException(getClass().getName());
  }
}
