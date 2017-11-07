package com.sun.jna;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.WeakHashMap;












public class NativeMappedConverter
  implements TypeConverter
{
  private static final Map converters = new WeakHashMap();
  private final Class type;
  private final Class nativeType;
  private final NativeMapped instance;
  
  public static NativeMappedConverter getInstance(Class cls) {
    synchronized (converters) {
      Reference r = (Reference)converters.get(cls);
      NativeMappedConverter nmc = r != null ? (NativeMappedConverter)r.get() : null;
      if (nmc == null) {
        nmc = new NativeMappedConverter(cls);
        converters.put(cls, new SoftReference(nmc));
      }
      return nmc;
    }
  }
  
  public NativeMappedConverter(Class type) {
    if (!NativeMapped.class.isAssignableFrom(type)) {
      throw new IllegalArgumentException("Type must derive from " + NativeMapped.class);
    }
    this.type = type;
    instance = defaultValue();
    nativeType = instance.nativeType();
  }
  
  public NativeMapped defaultValue() {
    try {
      return (NativeMapped)type.newInstance();
    }
    catch (InstantiationException e) {
      String msg = "Can't create an instance of " + type + ", requires a no-arg constructor: " + e;
      
      throw new IllegalArgumentException(msg);
    }
    catch (IllegalAccessException e) {
      String msg = "Not allowed to create an instance of " + type + ", requires a public, no-arg constructor: " + e;
      
      throw new IllegalArgumentException(msg);
    }
  }
  
  public Object fromNative(Object nativeValue, FromNativeContext context) { return instance.fromNative(nativeValue, context); }
  
  public Class nativeType()
  {
    return nativeType;
  }
  
  public Object toNative(Object value, ToNativeContext context) {
    if (value == null) {
      if (Pointer.class.isAssignableFrom(nativeType)) {
        return null;
      }
      value = defaultValue();
    }
    return ((NativeMapped)value).toNative();
  }
}
