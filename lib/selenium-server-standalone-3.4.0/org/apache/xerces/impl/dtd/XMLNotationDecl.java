package org.apache.xerces.impl.dtd;

public class XMLNotationDecl
{
  public String name;
  public String publicId;
  public String systemId;
  public String baseSystemId;
  
  public XMLNotationDecl() {}
  
  public void setValues(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    name = paramString1;
    publicId = paramString2;
    systemId = paramString3;
    baseSystemId = paramString4;
  }
  
  public void clear()
  {
    name = null;
    publicId = null;
    systemId = null;
    baseSystemId = null;
  }
}
