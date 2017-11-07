package com.google.common.base;

import com.google.common.annotations.GwtIncompatible;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;
























@GwtIncompatible
public final class Defaults
{
  private static final Map<Class<?>, Object> DEFAULTS;
  
  static
  {
    Map<Class<?>, Object> map = new HashMap();
    put(map, Boolean.TYPE, Boolean.valueOf(false));
    put(map, Character.TYPE, Character.valueOf('\000'));
    put(map, Byte.TYPE, Byte.valueOf((byte)0));
    put(map, Short.TYPE, Short.valueOf((short)0));
    put(map, Integer.TYPE, Integer.valueOf(0));
    put(map, Long.TYPE, Long.valueOf(0L));
    put(map, Float.TYPE, Float.valueOf(0.0F));
    put(map, Double.TYPE, Double.valueOf(0.0D));
    DEFAULTS = Collections.unmodifiableMap(map);
  }
  
  private static <T> void put(Map<Class<?>, Object> map, Class<T> type, T value) {
    map.put(type, value);
  }
  






  @Nullable
  public static <T> T defaultValue(Class<T> type)
  {
    T t = DEFAULTS.get(Preconditions.checkNotNull(type));
    return t;
  }
  
  private Defaults() {}
}
