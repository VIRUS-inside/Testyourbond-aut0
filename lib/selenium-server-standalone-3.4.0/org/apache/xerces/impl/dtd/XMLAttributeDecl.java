package org.apache.xerces.impl.dtd;

import org.apache.xerces.xni.QName;

public class XMLAttributeDecl
{
  public final QName name = new QName();
  public final XMLSimpleType simpleType = new XMLSimpleType();
  public boolean optional;
  
  public XMLAttributeDecl() {}
  
  public void setValues(QName paramQName, XMLSimpleType paramXMLSimpleType, boolean paramBoolean)
  {
    name.setValues(paramQName);
    simpleType.setValues(paramXMLSimpleType);
    optional = paramBoolean;
  }
  
  public void clear()
  {
    name.clear();
    simpleType.clear();
    optional = false;
  }
}
