package org.apache.xerces.impl.dtd;

import org.apache.xerces.impl.dtd.models.ContentModelValidator;
import org.apache.xerces.xni.QName;

public class XMLElementDecl
{
  public static final short TYPE_ANY = 0;
  public static final short TYPE_EMPTY = 1;
  public static final short TYPE_MIXED = 2;
  public static final short TYPE_CHILDREN = 3;
  public static final short TYPE_SIMPLE = 4;
  public final QName name = new QName();
  public int scope = -1;
  public short type = -1;
  public ContentModelValidator contentModelValidator;
  public final XMLSimpleType simpleType = new XMLSimpleType();
  
  public XMLElementDecl() {}
  
  public void setValues(QName paramQName, int paramInt, short paramShort, ContentModelValidator paramContentModelValidator, XMLSimpleType paramXMLSimpleType)
  {
    name.setValues(paramQName);
    scope = paramInt;
    type = paramShort;
    contentModelValidator = paramContentModelValidator;
    simpleType.setValues(paramXMLSimpleType);
  }
  
  public void clear()
  {
    name.clear();
    type = -1;
    scope = -1;
    contentModelValidator = null;
    simpleType.clear();
  }
}
