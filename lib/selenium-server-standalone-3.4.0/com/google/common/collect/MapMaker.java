package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Ascii;
import com.google.common.base.Equivalence;
import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;












































































@GwtCompatible(emulated=true)
public final class MapMaker
{
  private static final int DEFAULT_INITIAL_CAPACITY = 16;
  private static final int DEFAULT_CONCURRENCY_LEVEL = 4;
  static final int UNSET_INT = -1;
  boolean useCustomMap;
  int initialCapacity = -1;
  int concurrencyLevel = -1;
  


  MapMakerInternalMap.Strength keyStrength;
  


  MapMakerInternalMap.Strength valueStrength;
  

  Equivalence<Object> keyEquivalence;
  


  public MapMaker() {}
  


  @CanIgnoreReturnValue
  @GwtIncompatible
  MapMaker keyEquivalence(Equivalence<Object> equivalence)
  {
    Preconditions.checkState(keyEquivalence == null, "key equivalence was already set to %s", keyEquivalence);
    keyEquivalence = ((Equivalence)Preconditions.checkNotNull(equivalence));
    useCustomMap = true;
    return this;
  }
  
  Equivalence<Object> getKeyEquivalence() {
    return (Equivalence)MoreObjects.firstNonNull(keyEquivalence, getKeyStrength().defaultEquivalence());
  }
  









  @CanIgnoreReturnValue
  public MapMaker initialCapacity(int initialCapacity)
  {
    Preconditions.checkState(this.initialCapacity == -1, "initial capacity was already set to %s", this.initialCapacity);
    


    Preconditions.checkArgument(initialCapacity >= 0);
    this.initialCapacity = initialCapacity;
    return this;
  }
  
  int getInitialCapacity() {
    return initialCapacity == -1 ? 16 : initialCapacity;
  }
  


















  @CanIgnoreReturnValue
  public MapMaker concurrencyLevel(int concurrencyLevel)
  {
    Preconditions.checkState(this.concurrencyLevel == -1, "concurrency level was already set to %s", this.concurrencyLevel);
    


    Preconditions.checkArgument(concurrencyLevel > 0);
    this.concurrencyLevel = concurrencyLevel;
    return this;
  }
  
  int getConcurrencyLevel() {
    return concurrencyLevel == -1 ? 4 : concurrencyLevel;
  }
  










  @CanIgnoreReturnValue
  @GwtIncompatible
  public MapMaker weakKeys()
  {
    return setKeyStrength(MapMakerInternalMap.Strength.WEAK);
  }
  
  MapMaker setKeyStrength(MapMakerInternalMap.Strength strength) {
    Preconditions.checkState(keyStrength == null, "Key strength was already set to %s", keyStrength);
    keyStrength = ((MapMakerInternalMap.Strength)Preconditions.checkNotNull(strength));
    if (strength != MapMakerInternalMap.Strength.STRONG)
    {
      useCustomMap = true;
    }
    return this;
  }
  
  MapMakerInternalMap.Strength getKeyStrength() {
    return (MapMakerInternalMap.Strength)MoreObjects.firstNonNull(keyStrength, MapMakerInternalMap.Strength.STRONG);
  }
  















  @CanIgnoreReturnValue
  @GwtIncompatible
  public MapMaker weakValues()
  {
    return setValueStrength(MapMakerInternalMap.Strength.WEAK);
  }
  
  MapMaker setValueStrength(MapMakerInternalMap.Strength strength) {
    Preconditions.checkState(valueStrength == null, "Value strength was already set to %s", valueStrength);
    valueStrength = ((MapMakerInternalMap.Strength)Preconditions.checkNotNull(strength));
    if (strength != MapMakerInternalMap.Strength.STRONG)
    {
      useCustomMap = true;
    }
    return this;
  }
  
  MapMakerInternalMap.Strength getValueStrength() {
    return (MapMakerInternalMap.Strength)MoreObjects.firstNonNull(valueStrength, MapMakerInternalMap.Strength.STRONG);
  }
  










  public <K, V> ConcurrentMap<K, V> makeMap()
  {
    if (!useCustomMap) {
      return new ConcurrentHashMap(getInitialCapacity(), 0.75F, getConcurrencyLevel());
    }
    return MapMakerInternalMap.create(this);
  }
  



  @GwtIncompatible
  <K, V> MapMakerInternalMap<K, V, ?, ?> makeCustomMap()
  {
    return MapMakerInternalMap.create(this);
  }
  




  public String toString()
  {
    MoreObjects.ToStringHelper s = MoreObjects.toStringHelper(this);
    if (initialCapacity != -1) {
      s.add("initialCapacity", initialCapacity);
    }
    if (concurrencyLevel != -1) {
      s.add("concurrencyLevel", concurrencyLevel);
    }
    if (keyStrength != null) {
      s.add("keyStrength", Ascii.toLowerCase(keyStrength.toString()));
    }
    if (valueStrength != null) {
      s.add("valueStrength", Ascii.toLowerCase(valueStrength.toString()));
    }
    if (keyEquivalence != null) {
      s.addValue("keyEquivalence");
    }
    return s.toString();
  }
}
