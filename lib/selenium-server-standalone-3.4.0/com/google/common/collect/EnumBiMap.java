package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;































@GwtCompatible(emulated=true)
public final class EnumBiMap<K extends Enum<K>, V extends Enum<V>>
  extends AbstractBiMap<K, V>
{
  private transient Class<K> keyType;
  private transient Class<V> valueType;
  @GwtIncompatible
  private static final long serialVersionUID = 0L;
  
  public static <K extends Enum<K>, V extends Enum<V>> EnumBiMap<K, V> create(Class<K> keyType, Class<V> valueType)
  {
    return new EnumBiMap(keyType, valueType);
  }
  









  public static <K extends Enum<K>, V extends Enum<V>> EnumBiMap<K, V> create(Map<K, V> map)
  {
    EnumBiMap<K, V> bimap = create(inferKeyType(map), inferValueType(map));
    bimap.putAll(map);
    return bimap;
  }
  
  private EnumBiMap(Class<K> keyType, Class<V> valueType) {
    super(
      WellBehavedMap.wrap(new EnumMap(keyType)), 
      WellBehavedMap.wrap(new EnumMap(valueType)));
    this.keyType = keyType;
    this.valueType = valueType;
  }
  
  static <K extends Enum<K>> Class<K> inferKeyType(Map<K, ?> map) {
    if ((map instanceof EnumBiMap)) {
      return ((EnumBiMap)map).keyType();
    }
    if ((map instanceof EnumHashBiMap)) {
      return ((EnumHashBiMap)map).keyType();
    }
    Preconditions.checkArgument(!map.isEmpty());
    return ((Enum)map.keySet().iterator().next()).getDeclaringClass();
  }
  
  private static <V extends Enum<V>> Class<V> inferValueType(Map<?, V> map) {
    if ((map instanceof EnumBiMap)) {
      return valueType;
    }
    Preconditions.checkArgument(!map.isEmpty());
    return ((Enum)map.values().iterator().next()).getDeclaringClass();
  }
  
  public Class<K> keyType()
  {
    return keyType;
  }
  
  public Class<V> valueType()
  {
    return valueType;
  }
  
  K checkKey(K key)
  {
    return (Enum)Preconditions.checkNotNull(key);
  }
  
  V checkValue(V value)
  {
    return (Enum)Preconditions.checkNotNull(value);
  }
  


  @GwtIncompatible
  private void writeObject(ObjectOutputStream stream)
    throws IOException
  {
    stream.defaultWriteObject();
    stream.writeObject(keyType);
    stream.writeObject(valueType);
    Serialization.writeMap(this, stream);
  }
  
  @GwtIncompatible
  private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException
  {
    stream.defaultReadObject();
    keyType = ((Class)stream.readObject());
    valueType = ((Class)stream.readObject());
    setDelegates(
      WellBehavedMap.wrap(new EnumMap(keyType)), 
      WellBehavedMap.wrap(new EnumMap(valueType)));
    Serialization.populateMap(this, stream);
  }
}
