package org.apache.xerces.impl.dtd;

import org.apache.xerces.impl.dv.DatatypeValidator;

public class XMLSimpleType
{
  public static final short TYPE_CDATA = 0;
  public static final short TYPE_ENTITY = 1;
  public static final short TYPE_ENUMERATION = 2;
  public static final short TYPE_ID = 3;
  public static final short TYPE_IDREF = 4;
  public static final short TYPE_NMTOKEN = 5;
  public static final short TYPE_NOTATION = 6;
  public static final short TYPE_NAMED = 7;
  public static final short DEFAULT_TYPE_DEFAULT = 3;
  public static final short DEFAULT_TYPE_FIXED = 1;
  public static final short DEFAULT_TYPE_IMPLIED = 0;
  public static final short DEFAULT_TYPE_REQUIRED = 2;
  public short type;
  public String name;
  public String[] enumeration;
  public boolean list;
  public short defaultType;
  public String defaultValue;
  public String nonNormalizedDefaultValue;
  public DatatypeValidator datatypeValidator;
  
  public XMLSimpleType() {}
  
  public void setValues(short paramShort1, String paramString1, String[] paramArrayOfString, boolean paramBoolean, short paramShort2, String paramString2, String paramString3, DatatypeValidator paramDatatypeValidator)
  {
    type = paramShort1;
    name = paramString1;
    if ((paramArrayOfString != null) && (paramArrayOfString.length > 0))
    {
      enumeration = new String[paramArrayOfString.length];
      System.arraycopy(paramArrayOfString, 0, enumeration, 0, enumeration.length);
    }
    else
    {
      enumeration = null;
    }
    list = paramBoolean;
    defaultType = paramShort2;
    defaultValue = paramString2;
    nonNormalizedDefaultValue = paramString3;
    datatypeValidator = paramDatatypeValidator;
  }
  
  public void setValues(XMLSimpleType paramXMLSimpleType)
  {
    type = type;
    name = name;
    if ((enumeration != null) && (enumeration.length > 0))
    {
      enumeration = new String[enumeration.length];
      System.arraycopy(enumeration, 0, enumeration, 0, enumeration.length);
    }
    else
    {
      enumeration = null;
    }
    list = list;
    defaultType = defaultType;
    defaultValue = defaultValue;
    nonNormalizedDefaultValue = nonNormalizedDefaultValue;
    datatypeValidator = datatypeValidator;
  }
  
  public void clear()
  {
    type = -1;
    name = null;
    enumeration = null;
    list = false;
    defaultType = -1;
    defaultValue = null;
    nonNormalizedDefaultValue = null;
    datatypeValidator = null;
  }
}
