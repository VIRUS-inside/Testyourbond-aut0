package org.apache.xerces.impl.dtd;

public class XMLContentSpec
{
  public static final short CONTENTSPECNODE_LEAF = 0;
  public static final short CONTENTSPECNODE_ZERO_OR_ONE = 1;
  public static final short CONTENTSPECNODE_ZERO_OR_MORE = 2;
  public static final short CONTENTSPECNODE_ONE_OR_MORE = 3;
  public static final short CONTENTSPECNODE_CHOICE = 4;
  public static final short CONTENTSPECNODE_SEQ = 5;
  public static final short CONTENTSPECNODE_ANY = 6;
  public static final short CONTENTSPECNODE_ANY_OTHER = 7;
  public static final short CONTENTSPECNODE_ANY_LOCAL = 8;
  public static final short CONTENTSPECNODE_ANY_LAX = 22;
  public static final short CONTENTSPECNODE_ANY_OTHER_LAX = 23;
  public static final short CONTENTSPECNODE_ANY_LOCAL_LAX = 24;
  public static final short CONTENTSPECNODE_ANY_SKIP = 38;
  public static final short CONTENTSPECNODE_ANY_OTHER_SKIP = 39;
  public static final short CONTENTSPECNODE_ANY_LOCAL_SKIP = 40;
  public short type;
  public Object value;
  public Object otherValue;
  
  public XMLContentSpec()
  {
    clear();
  }
  
  public XMLContentSpec(short paramShort, Object paramObject1, Object paramObject2)
  {
    setValues(paramShort, paramObject1, paramObject2);
  }
  
  public XMLContentSpec(XMLContentSpec paramXMLContentSpec)
  {
    setValues(paramXMLContentSpec);
  }
  
  public XMLContentSpec(Provider paramProvider, int paramInt)
  {
    setValues(paramProvider, paramInt);
  }
  
  public void clear()
  {
    type = -1;
    value = null;
    otherValue = null;
  }
  
  public void setValues(short paramShort, Object paramObject1, Object paramObject2)
  {
    type = paramShort;
    value = paramObject1;
    otherValue = paramObject2;
  }
  
  public void setValues(XMLContentSpec paramXMLContentSpec)
  {
    type = type;
    value = value;
    otherValue = otherValue;
  }
  
  public void setValues(Provider paramProvider, int paramInt)
  {
    if (!paramProvider.getContentSpec(paramInt, this)) {
      clear();
    }
  }
  
  public int hashCode()
  {
    return type << 16 | value.hashCode() << 8 | otherValue.hashCode();
  }
  
  public boolean equals(Object paramObject)
  {
    if ((paramObject != null) && ((paramObject instanceof XMLContentSpec)))
    {
      XMLContentSpec localXMLContentSpec = (XMLContentSpec)paramObject;
      return (type == type) && (value == value) && (otherValue == otherValue);
    }
    return false;
  }
  
  public static abstract interface Provider
  {
    public abstract boolean getContentSpec(int paramInt, XMLContentSpec paramXMLContentSpec);
  }
}
