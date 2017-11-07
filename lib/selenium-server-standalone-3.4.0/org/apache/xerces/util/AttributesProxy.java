package org.apache.xerces.util;

import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.XMLAttributes;
import org.xml.sax.AttributeList;
import org.xml.sax.ext.Attributes2;

public final class AttributesProxy
  implements AttributeList, Attributes2
{
  private XMLAttributes fAttributes;
  
  public AttributesProxy(XMLAttributes paramXMLAttributes)
  {
    fAttributes = paramXMLAttributes;
  }
  
  public void setAttributes(XMLAttributes paramXMLAttributes)
  {
    fAttributes = paramXMLAttributes;
  }
  
  public XMLAttributes getAttributes()
  {
    return fAttributes;
  }
  
  public int getLength()
  {
    return fAttributes.getLength();
  }
  
  public String getQName(int paramInt)
  {
    return fAttributes.getQName(paramInt);
  }
  
  public String getURI(int paramInt)
  {
    String str = fAttributes.getURI(paramInt);
    return str != null ? str : XMLSymbols.EMPTY_STRING;
  }
  
  public String getLocalName(int paramInt)
  {
    return fAttributes.getLocalName(paramInt);
  }
  
  public String getType(int paramInt)
  {
    return fAttributes.getType(paramInt);
  }
  
  public String getType(String paramString)
  {
    return fAttributes.getType(paramString);
  }
  
  public String getType(String paramString1, String paramString2)
  {
    return paramString1.equals(XMLSymbols.EMPTY_STRING) ? fAttributes.getType(null, paramString2) : fAttributes.getType(paramString1, paramString2);
  }
  
  public String getValue(int paramInt)
  {
    return fAttributes.getValue(paramInt);
  }
  
  public String getValue(String paramString)
  {
    return fAttributes.getValue(paramString);
  }
  
  public String getValue(String paramString1, String paramString2)
  {
    return paramString1.equals(XMLSymbols.EMPTY_STRING) ? fAttributes.getValue(null, paramString2) : fAttributes.getValue(paramString1, paramString2);
  }
  
  public int getIndex(String paramString)
  {
    return fAttributes.getIndex(paramString);
  }
  
  public int getIndex(String paramString1, String paramString2)
  {
    return paramString1.equals(XMLSymbols.EMPTY_STRING) ? fAttributes.getIndex(null, paramString2) : fAttributes.getIndex(paramString1, paramString2);
  }
  
  public boolean isDeclared(int paramInt)
  {
    if ((paramInt < 0) || (paramInt >= fAttributes.getLength())) {
      throw new ArrayIndexOutOfBoundsException(paramInt);
    }
    return Boolean.TRUE.equals(fAttributes.getAugmentations(paramInt).getItem("ATTRIBUTE_DECLARED"));
  }
  
  public boolean isDeclared(String paramString)
  {
    int i = getIndex(paramString);
    if (i == -1) {
      throw new IllegalArgumentException(paramString);
    }
    return Boolean.TRUE.equals(fAttributes.getAugmentations(i).getItem("ATTRIBUTE_DECLARED"));
  }
  
  public boolean isDeclared(String paramString1, String paramString2)
  {
    int i = getIndex(paramString1, paramString2);
    if (i == -1) {
      throw new IllegalArgumentException(paramString2);
    }
    return Boolean.TRUE.equals(fAttributes.getAugmentations(i).getItem("ATTRIBUTE_DECLARED"));
  }
  
  public boolean isSpecified(int paramInt)
  {
    if ((paramInt < 0) || (paramInt >= fAttributes.getLength())) {
      throw new ArrayIndexOutOfBoundsException(paramInt);
    }
    return fAttributes.isSpecified(paramInt);
  }
  
  public boolean isSpecified(String paramString)
  {
    int i = getIndex(paramString);
    if (i == -1) {
      throw new IllegalArgumentException(paramString);
    }
    return fAttributes.isSpecified(i);
  }
  
  public boolean isSpecified(String paramString1, String paramString2)
  {
    int i = getIndex(paramString1, paramString2);
    if (i == -1) {
      throw new IllegalArgumentException(paramString2);
    }
    return fAttributes.isSpecified(i);
  }
  
  public String getName(int paramInt)
  {
    return fAttributes.getQName(paramInt);
  }
}
