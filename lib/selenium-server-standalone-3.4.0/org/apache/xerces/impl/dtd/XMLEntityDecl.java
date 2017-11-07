package org.apache.xerces.impl.dtd;

public class XMLEntityDecl
{
  public String name;
  public String publicId;
  public String systemId;
  public String baseSystemId;
  public String notation;
  public boolean isPE;
  public boolean inExternal;
  public String value;
  
  public XMLEntityDecl() {}
  
  public void setValues(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, boolean paramBoolean1, boolean paramBoolean2)
  {
    setValues(paramString1, paramString2, paramString3, paramString4, paramString5, null, paramBoolean1, paramBoolean2);
  }
  
  public void setValues(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, boolean paramBoolean1, boolean paramBoolean2)
  {
    name = paramString1;
    publicId = paramString2;
    systemId = paramString3;
    baseSystemId = paramString4;
    notation = paramString5;
    value = paramString6;
    isPE = paramBoolean1;
    inExternal = paramBoolean2;
  }
  
  public void clear()
  {
    name = null;
    publicId = null;
    systemId = null;
    baseSystemId = null;
    notation = null;
    value = null;
    isPE = false;
    inExternal = false;
  }
}
