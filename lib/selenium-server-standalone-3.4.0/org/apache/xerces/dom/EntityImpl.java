package org.apache.xerces.dom;

import org.w3c.dom.Entity;
import org.w3c.dom.Node;

public class EntityImpl
  extends ParentNode
  implements Entity
{
  static final long serialVersionUID = -3575760943444303423L;
  protected String name;
  protected String publicId;
  protected String systemId;
  protected String encoding;
  protected String inputEncoding;
  protected String version;
  protected String notationName;
  protected String baseURI;
  
  public EntityImpl(CoreDocumentImpl paramCoreDocumentImpl, String paramString)
  {
    super(paramCoreDocumentImpl);
    name = paramString;
    isReadOnly(true);
  }
  
  public short getNodeType()
  {
    return 6;
  }
  
  public String getNodeName()
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    return name;
  }
  
  public Node cloneNode(boolean paramBoolean)
  {
    EntityImpl localEntityImpl = (EntityImpl)super.cloneNode(paramBoolean);
    localEntityImpl.setReadOnly(true, paramBoolean);
    return localEntityImpl;
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
  
  public String getXmlVersion()
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    return version;
  }
  
  public String getXmlEncoding()
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    return encoding;
  }
  
  public String getNotationName()
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    return notationName;
  }
  
  public void setPublicId(String paramString)
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    publicId = paramString;
  }
  
  public void setXmlEncoding(String paramString)
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    encoding = paramString;
  }
  
  public String getInputEncoding()
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    return inputEncoding;
  }
  
  public void setInputEncoding(String paramString)
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    inputEncoding = paramString;
  }
  
  public void setXmlVersion(String paramString)
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    version = paramString;
  }
  
  public void setSystemId(String paramString)
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    systemId = paramString;
  }
  
  public void setNotationName(String paramString)
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    notationName = paramString;
  }
  
  public String getBaseURI()
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    return baseURI != null ? baseURI : ((CoreDocumentImpl)getOwnerDocument()).getBaseURI();
  }
  
  public void setBaseURI(String paramString)
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    baseURI = paramString;
  }
}
