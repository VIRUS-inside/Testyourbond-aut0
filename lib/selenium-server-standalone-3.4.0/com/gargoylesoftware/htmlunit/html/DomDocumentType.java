package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.SgmlPage;
import org.w3c.dom.DocumentType;
import org.w3c.dom.NamedNodeMap;

























public class DomDocumentType
  extends DomNode
  implements DocumentType
{
  private final String name_;
  private final String publicId_;
  private final String systemId_;
  
  public DomDocumentType(SgmlPage page, String name, String publicId, String systemId)
  {
    super(page);
    name_ = name;
    publicId_ = publicId;
    systemId_ = systemId;
  }
  



  public String getNodeName()
  {
    return name_;
  }
  



  public short getNodeType()
  {
    return 10;
  }
  



  public NamedNodeMap getEntities()
  {
    return null;
  }
  



  public String getInternalSubset()
  {
    return "";
  }
  



  public String getName()
  {
    return name_;
  }
  



  public NamedNodeMap getNotations()
  {
    return null;
  }
  



  public String getPublicId()
  {
    return publicId_;
  }
  



  public String getSystemId()
  {
    return systemId_;
  }
}
