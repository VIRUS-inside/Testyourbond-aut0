package com.sun.jna;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;





















public class DefaultTypeMapper
  implements TypeMapper
{
  public DefaultTypeMapper() {}
  
  private static class Entry
  {
    public Class type;
    public Object converter;
    
    public Entry(Class type, Object converter)
    {
      this.type = type;
      this.converter = converter;
    } }
  
  private List toNativeConverters = new ArrayList();
  private List fromNativeConverters = new ArrayList();
  
  private Class getAltClass(Class cls) { if (cls == Boolean.class) {
      return Boolean.TYPE;
    }
    if (cls == Boolean.TYPE) {
      return Boolean.class;
    }
    if (cls == Byte.class) {
      return Byte.TYPE;
    }
    if (cls == Byte.TYPE) {
      return Byte.class;
    }
    if (cls == Character.class) {
      return Character.TYPE;
    }
    if (cls == Character.TYPE) {
      return Character.class;
    }
    if (cls == Short.class) {
      return Short.TYPE;
    }
    if (cls == Short.TYPE) {
      return Short.class;
    }
    if (cls == Integer.class) {
      return Integer.TYPE;
    }
    if (cls == Integer.TYPE) {
      return Integer.class;
    }
    if (cls == Long.class) {
      return Long.TYPE;
    }
    if (cls == Long.TYPE) {
      return Long.class;
    }
    if (cls == Float.class) {
      return Float.TYPE;
    }
    if (cls == Float.TYPE) {
      return Float.class;
    }
    if (cls == Double.class) {
      return Double.TYPE;
    }
    if (cls == Double.TYPE) {
      return Double.class;
    }
    return null;
  }
  


  public void addToNativeConverter(Class cls, ToNativeConverter converter)
  {
    toNativeConverters.add(new Entry(cls, converter));
    Class alt = getAltClass(cls);
    if (alt != null) {
      toNativeConverters.add(new Entry(alt, converter));
    }
  }
  

  public void addFromNativeConverter(Class cls, FromNativeConverter converter)
  {
    fromNativeConverters.add(new Entry(cls, converter));
    Class alt = getAltClass(cls);
    if (alt != null) {
      fromNativeConverters.add(new Entry(alt, converter));
    }
  }
  

  protected void addTypeConverter(Class cls, TypeConverter converter)
  {
    addFromNativeConverter(cls, converter);
    addToNativeConverter(cls, converter);
  }
  
  private Object lookupConverter(Class javaClass, List converters) {
    for (Iterator i = converters.iterator(); i.hasNext();) {
      Entry entry = (Entry)i.next();
      if (type.isAssignableFrom(javaClass)) {
        return converter;
      }
    }
    return null;
  }
  

  public FromNativeConverter getFromNativeConverter(Class javaType)
  {
    return (FromNativeConverter)lookupConverter(javaType, fromNativeConverters);
  }
  

  public ToNativeConverter getToNativeConverter(Class javaType)
  {
    return (ToNativeConverter)lookupConverter(javaType, toNativeConverters);
  }
}
