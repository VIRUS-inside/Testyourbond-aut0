package org.apache.xerces.dom;

import org.apache.xerces.util.URI;
import org.apache.xerces.util.URI.MalformedURIException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Notation;

public class NotationImpl
  extends NodeImpl
  implements Notation
{
  static final long serialVersionUID = -764632195890658402L;
  protected String name;
  protected String publicId;
  protected String systemId;
  protected String baseURI;
  
  public NotationImpl(CoreDocumentImpl paramCoreDocumentImpl, String paramString)
  {
    super(paramCoreDocumentImpl);
    name = paramString;
  }
  
  public short getNodeType()
  {
    return 12;
  }
  
  public String getNodeName()
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    return name;
  }
  
  public String getPublicId()
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    return publicId;
  }
  
  public String getSystemId()
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    return systemId;
  }
  
  public void setPublicId(String paramString)
  {
    if (isReadOnly()) {
      throw new DOMException((short)7, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null));
    }
    if (needsSyncData()) {
      synchronizeData();
    }
    publicId = paramString;
  }
  
  public void setSystemId(String paramString)
  {
    if (isReadOnly()) {
      throw new DOMException((short)7, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null));
    }
    if (needsSyncData()) {
      synchronizeData();
    }
    systemId = paramString;
  }
  
  public String getBaseURI()
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    if ((baseURI != null) && (baseURI.length() != 0)) {
      try
      {
        return new URI(baseURI).toString();
      }
      catch (URI.MalformedURIException localMalformedURIException)
      {
        return null;
      }
    }
    return baseURI;
  }
  
  public void setBaseURI(String paramString)
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    baseURI = paramString;
  }
}
