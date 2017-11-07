package com.google.common.collect;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Equivalence;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.j2objc.annotations.Weak;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.locks.ReentrantLock;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;









































































































@GwtIncompatible
class MapMakerInternalMap<K, V, E extends InternalEntry<K, V, E>, S extends Segment<K, V, E, S>>
  extends AbstractMap<K, V>
  implements ConcurrentMap<K, V>, Serializable
{
  static final int MAXIMUM_CAPACITY = 1073741824;
  static final int MAX_SEGMENTS = 65536;
  static final int CONTAINS_VALUE_RETRIES = 3;
  static final int DRAIN_THRESHOLD = 63;
  static final int DRAIN_MAX = 16;
  static final long CLEANUP_EXECUTOR_DELAY_SECS = 60L;
  final transient int segmentMask;
  final transient int segmentShift;
  final transient Segment<K, V, E, S>[] segments;
  final int concurrencyLevel;
  final Equivalence<Object> keyEquivalence;
  final transient InternalEntryHelper<K, V, E, S> entryHelper;
  
  private MapMakerInternalMap(MapMaker builder, InternalEntryHelper<K, V, E, S> entryHelper)
  {
    concurrencyLevel = Math.min(builder.getConcurrencyLevel(), 65536);
    
    keyEquivalence = builder.getKeyEquivalence();
    this.entryHelper = entryHelper;
    
    int initialCapacity = Math.min(builder.getInitialCapacity(), 1073741824);
    


    int segmentShift = 0;
    int segmentCount = 1;
    while (segmentCount < concurrencyLevel) {
      segmentShift++;
      segmentCount <<= 1;
    }
    this.segmentShift = (32 - segmentShift);
    segmentMask = (segmentCount - 1);
    
    segments = newSegmentArray(segmentCount);
    
    int segmentCapacity = initialCapacity / segmentCount;
    if (segmentCapacity * segmentCount < initialCapacity) {
      segmentCapacity++;
    }
    
    int segmentSize = 1;
    while (segmentSize < segmentCapacity) {
      segmentSize <<= 1;
    }
    
    for (int i = 0; i < segments.length; i++) {
      segments[i] = createSegment(segmentSize, -1);
    }
  }
  
  static <K, V> MapMakerInternalMap<K, V, ? extends InternalEntry<K, V, ?>, ?> create(MapMaker builder)
  {
    if ((builder.getKeyStrength() == Strength.STRONG) && 
      (builder.getValueStrength() == Strength.STRONG)) {
      return new MapMakerInternalMap(builder, 
      
        MapMakerInternalMap.StrongKeyStrongValueEntry.Helper.instance());
    }
    if ((builder.getKeyStrength() == Strength.STRONG) && 
      (builder.getValueStrength() == Strength.WEAK)) {
      return new MapMakerInternalMap(builder, 
      
        MapMakerInternalMap.StrongKeyWeakValueEntry.Helper.instance());
    }
    if ((builder.getKeyStrength() == Strength.WEAK) && 
      (builder.getValueStrength() == Strength.STRONG)) {
      return new MapMakerInternalMap(builder, 
      
        MapMakerInternalMap.WeakKeyStrongValueEntry.Helper.instance());
    }
    if ((builder.getKeyStrength() == Strength.WEAK) && (builder.getValueStrength() == Strength.WEAK)) {
      return new MapMakerInternalMap(builder, 
      
        MapMakerInternalMap.WeakKeyWeakValueEntry.Helper.instance());
    }
    throw new AssertionError();
  }
  
  static abstract enum Strength {
    STRONG, 
    





    WEAK;
    




    private Strength() {}
    




    abstract Equivalence<Object> defaultEquivalence();
  }
  




  static abstract interface InternalEntryHelper<K, V, E extends MapMakerInternalMap.InternalEntry<K, V, E>, S extends MapMakerInternalMap.Segment<K, V, E, S>>
  {
    public abstract MapMakerInternalMap.Strength keyStrength();
    




    public abstract MapMakerInternalMap.Strength valueStrength();
    




    public abstract S newSegment(MapMakerInternalMap<K, V, E, S> paramMapMakerInternalMap, int paramInt1, int paramInt2);
    




    public abstract E newEntry(S paramS, K paramK, int paramInt, @Nullable E paramE);
    




    public abstract E copy(S paramS, E paramE1, @Nullable E paramE2);
    



    public abstract void setValue(S paramS, E paramE, V paramV);
  }
  



  static abstract interface InternalEntry<K, V, E extends InternalEntry<K, V, E>>
  {
    public abstract E getNext();
    



    public abstract int getHash();
    



    public abstract K getKey();
    



    public abstract V getValue();
  }
  



  static abstract class AbstractStrongKeyEntry<K, V, E extends MapMakerInternalMap.InternalEntry<K, V, E>>
    implements MapMakerInternalMap.InternalEntry<K, V, E>
  {
    final K key;
    


    final int hash;
    


    final E next;
    



    AbstractStrongKeyEntry(K key, int hash, @Nullable E next)
    {
      this.key = key;
      this.hash = hash;
      this.next = next;
    }
    
    public K getKey()
    {
      return key;
    }
    
    public int getHash()
    {
      return hash;
    }
    
    public E getNext()
    {
      return next;
    }
  }
  








  static abstract interface StrongValueEntry<K, V, E extends MapMakerInternalMap.InternalEntry<K, V, E>>
    extends MapMakerInternalMap.InternalEntry<K, V, E>
  {}
  







  static <K, V, E extends InternalEntry<K, V, E>> WeakValueReference<K, V, E> unsetWeakValueReference() { return UNSET_WEAK_VALUE_REFERENCE; }
  
  static abstract interface WeakValueEntry<K, V, E extends MapMakerInternalMap.InternalEntry<K, V, E>> extends MapMakerInternalMap.InternalEntry<K, V, E> { public abstract MapMakerInternalMap.WeakValueReference<K, V, E> getValueReference();
    
    public abstract void clearValue(); }
  
  static final class StrongKeyStrongValueEntry<K, V> extends MapMakerInternalMap.AbstractStrongKeyEntry<K, V, StrongKeyStrongValueEntry<K, V>> implements MapMakerInternalMap.StrongValueEntry<K, V, StrongKeyStrongValueEntry<K, V>> { @Nullable
    private volatile V value = null;
    
    StrongKeyStrongValueEntry(K key, int hash, @Nullable StrongKeyStrongValueEntry<K, V> next) {
      super(hash, next);
    }
    
    @Nullable
    public V getValue()
    {
      return value;
    }
    
    void setValue(V value) {
      this.value = value;
    }
    
    StrongKeyStrongValueEntry<K, V> copy(StrongKeyStrongValueEntry<K, V> newNext) {
      StrongKeyStrongValueEntry<K, V> newEntry = new StrongKeyStrongValueEntry(key, hash, newNext);
      
      value = value;
      return newEntry;
    }
    

    static final class Helper<K, V>
      implements MapMakerInternalMap.InternalEntryHelper<K, V, MapMakerInternalMap.StrongKeyStrongValueEntry<K, V>, MapMakerInternalMap.StrongKeyStrongValueSegment<K, V>>
    {
      private static final Helper<?, ?> INSTANCE = new Helper();
      
      Helper() {}
      
      static <K, V> Helper<K, V> instance() { return INSTANCE; }
      

      public MapMakerInternalMap.Strength keyStrength()
      {
        return MapMakerInternalMap.Strength.STRONG;
      }
      
      public MapMakerInternalMap.Strength valueStrength()
      {
        return MapMakerInternalMap.Strength.STRONG;
      }
      





      public MapMakerInternalMap.StrongKeyStrongValueSegment<K, V> newSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.StrongKeyStrongValueEntry<K, V>, MapMakerInternalMap.StrongKeyStrongValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize)
      {
        return new MapMakerInternalMap.StrongKeyStrongValueSegment(map, initialCapacity, maxSegmentSize);
      }
      



      public MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> copy(MapMakerInternalMap.StrongKeyStrongValueSegment<K, V> segment, MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> entry, @Nullable MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> newNext)
      {
        return entry.copy(newNext);
      }
      



      public void setValue(MapMakerInternalMap.StrongKeyStrongValueSegment<K, V> segment, MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> entry, V value)
      {
        entry.setValue(value);
      }
      




      public MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> newEntry(MapMakerInternalMap.StrongKeyStrongValueSegment<K, V> segment, K key, int hash, @Nullable MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> next)
      {
        return new MapMakerInternalMap.StrongKeyStrongValueEntry(key, hash, next);
      }
    }
  }
  

  static final class StrongKeyWeakValueEntry<K, V>
    extends MapMakerInternalMap.AbstractStrongKeyEntry<K, V, StrongKeyWeakValueEntry<K, V>>
    implements MapMakerInternalMap.WeakValueEntry<K, V, StrongKeyWeakValueEntry<K, V>>
  {
    private volatile MapMakerInternalMap.WeakValueReference<K, V, StrongKeyWeakValueEntry<K, V>> valueReference = MapMakerInternalMap.unsetWeakValueReference();
    
    StrongKeyWeakValueEntry(K key, int hash, @Nullable StrongKeyWeakValueEntry<K, V> next) {
      super(hash, next);
    }
    
    public V getValue()
    {
      return valueReference.get();
    }
    
    public void clearValue()
    {
      valueReference.clear();
    }
    
    void setValue(V value, ReferenceQueue<V> queueForValues) {
      MapMakerInternalMap.WeakValueReference<K, V, StrongKeyWeakValueEntry<K, V>> previous = valueReference;
      valueReference = new MapMakerInternalMap.WeakValueReferenceImpl(queueForValues, value, this);
      

      previous.clear();
    }
    
    StrongKeyWeakValueEntry<K, V> copy(ReferenceQueue<V> queueForValues, StrongKeyWeakValueEntry<K, V> newNext)
    {
      StrongKeyWeakValueEntry<K, V> newEntry = new StrongKeyWeakValueEntry(key, hash, newNext);
      
      valueReference = valueReference.copyFor(queueForValues, newEntry);
      return newEntry;
    }
    
    public MapMakerInternalMap.WeakValueReference<K, V, StrongKeyWeakValueEntry<K, V>> getValueReference()
    {
      return valueReference;
    }
    

    static final class Helper<K, V>
      implements MapMakerInternalMap.InternalEntryHelper<K, V, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V>, MapMakerInternalMap.StrongKeyWeakValueSegment<K, V>>
    {
      private static final Helper<?, ?> INSTANCE = new Helper();
      
      Helper() {}
      
      static <K, V> Helper<K, V> instance() { return INSTANCE; }
      

      public MapMakerInternalMap.Strength keyStrength()
      {
        return MapMakerInternalMap.Strength.STRONG;
      }
      
      public MapMakerInternalMap.Strength valueStrength()
      {
        return MapMakerInternalMap.Strength.WEAK;
      }
      




      public MapMakerInternalMap.StrongKeyWeakValueSegment<K, V> newSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V>, MapMakerInternalMap.StrongKeyWeakValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize)
      {
        return new MapMakerInternalMap.StrongKeyWeakValueSegment(map, initialCapacity, maxSegmentSize);
      }
      



      public MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> copy(MapMakerInternalMap.StrongKeyWeakValueSegment<K, V> segment, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> entry, @Nullable MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> newNext)
      {
        if (MapMakerInternalMap.Segment.isCollected(entry)) {
          return null;
        }
        return entry.copy(queueForValues, newNext);
      }
      

      public void setValue(MapMakerInternalMap.StrongKeyWeakValueSegment<K, V> segment, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> entry, V value)
      {
        entry.setValue(value, queueForValues);
      }
      




      public MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> newEntry(MapMakerInternalMap.StrongKeyWeakValueSegment<K, V> segment, K key, int hash, @Nullable MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> next)
      {
        return new MapMakerInternalMap.StrongKeyWeakValueEntry(key, hash, next);
      }
    }
  }
  
  static abstract class AbstractWeakKeyEntry<K, V, E extends MapMakerInternalMap.InternalEntry<K, V, E>> extends WeakReference<K> implements MapMakerInternalMap.InternalEntry<K, V, E>
  {
    final int hash;
    final E next;
    
    AbstractWeakKeyEntry(ReferenceQueue<K> queue, K key, int hash, @Nullable E next)
    {
      super(queue);
      this.hash = hash;
      this.next = next;
    }
    
    public K getKey()
    {
      return get();
    }
    
    public int getHash()
    {
      return hash;
    }
    
    public E getNext()
    {
      return next;
    }
  }
  
  static final class WeakKeyStrongValueEntry<K, V>
    extends MapMakerInternalMap.AbstractWeakKeyEntry<K, V, WeakKeyStrongValueEntry<K, V>> implements MapMakerInternalMap.StrongValueEntry<K, V, WeakKeyStrongValueEntry<K, V>>
  {
    @Nullable
    private volatile V value = null;
    
    WeakKeyStrongValueEntry(ReferenceQueue<K> queue, K key, int hash, @Nullable WeakKeyStrongValueEntry<K, V> next)
    {
      super(key, hash, next);
    }
    
    @Nullable
    public V getValue()
    {
      return value;
    }
    
    void setValue(V value) {
      this.value = value;
    }
    

    WeakKeyStrongValueEntry<K, V> copy(ReferenceQueue<K> queueForKeys, WeakKeyStrongValueEntry<K, V> newNext)
    {
      WeakKeyStrongValueEntry<K, V> newEntry = new WeakKeyStrongValueEntry(queueForKeys, getKey(), hash, newNext);
      newEntry.setValue(value);
      return newEntry;
    }
    

    static final class Helper<K, V>
      implements MapMakerInternalMap.InternalEntryHelper<K, V, MapMakerInternalMap.WeakKeyStrongValueEntry<K, V>, MapMakerInternalMap.WeakKeyStrongValueSegment<K, V>>
    {
      private static final Helper<?, ?> INSTANCE = new Helper();
      
      Helper() {}
      
      static <K, V> Helper<K, V> instance() { return INSTANCE; }
      

      public MapMakerInternalMap.Strength keyStrength()
      {
        return MapMakerInternalMap.Strength.WEAK;
      }
      
      public MapMakerInternalMap.Strength valueStrength()
      {
        return MapMakerInternalMap.Strength.STRONG;
      }
      




      public MapMakerInternalMap.WeakKeyStrongValueSegment<K, V> newSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.WeakKeyStrongValueEntry<K, V>, MapMakerInternalMap.WeakKeyStrongValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize)
      {
        return new MapMakerInternalMap.WeakKeyStrongValueSegment(map, initialCapacity, maxSegmentSize);
      }
      



      public MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> copy(MapMakerInternalMap.WeakKeyStrongValueSegment<K, V> segment, MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> entry, @Nullable MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> newNext)
      {
        if (entry.getKey() == null)
        {
          return null;
        }
        return entry.copy(queueForKeys, newNext);
      }
      

      public void setValue(MapMakerInternalMap.WeakKeyStrongValueSegment<K, V> segment, MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> entry, V value)
      {
        entry.setValue(value);
      }
      




      public MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> newEntry(MapMakerInternalMap.WeakKeyStrongValueSegment<K, V> segment, K key, int hash, @Nullable MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> next)
      {
        return new MapMakerInternalMap.WeakKeyStrongValueEntry(queueForKeys, key, hash, next);
      }
    }
  }
  

  static final class WeakKeyWeakValueEntry<K, V>
    extends MapMakerInternalMap.AbstractWeakKeyEntry<K, V, WeakKeyWeakValueEntry<K, V>>
    implements MapMakerInternalMap.WeakValueEntry<K, V, WeakKeyWeakValueEntry<K, V>>
  {
    private volatile MapMakerInternalMap.WeakValueReference<K, V, WeakKeyWeakValueEntry<K, V>> valueReference = MapMakerInternalMap.unsetWeakValueReference();
    
    WeakKeyWeakValueEntry(ReferenceQueue<K> queue, K key, int hash, @Nullable WeakKeyWeakValueEntry<K, V> next)
    {
      super(key, hash, next);
    }
    
    public V getValue()
    {
      return valueReference.get();
    }
    



    WeakKeyWeakValueEntry<K, V> copy(ReferenceQueue<K> queueForKeys, ReferenceQueue<V> queueForValues, WeakKeyWeakValueEntry<K, V> newNext)
    {
      WeakKeyWeakValueEntry<K, V> newEntry = new WeakKeyWeakValueEntry(queueForKeys, getKey(), hash, newNext);
      valueReference = valueReference.copyFor(queueForValues, newEntry);
      return newEntry;
    }
    
    public void clearValue()
    {
      valueReference.clear();
    }
    
    void setValue(V value, ReferenceQueue<V> queueForValues) {
      MapMakerInternalMap.WeakValueReference<K, V, WeakKeyWeakValueEntry<K, V>> previous = valueReference;
      valueReference = new MapMakerInternalMap.WeakValueReferenceImpl(queueForValues, value, this);
      

      previous.clear();
    }
    
    public MapMakerInternalMap.WeakValueReference<K, V, WeakKeyWeakValueEntry<K, V>> getValueReference()
    {
      return valueReference;
    }
    

    static final class Helper<K, V>
      implements MapMakerInternalMap.InternalEntryHelper<K, V, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V>, MapMakerInternalMap.WeakKeyWeakValueSegment<K, V>>
    {
      private static final Helper<?, ?> INSTANCE = new Helper();
      
      Helper() {}
      
      static <K, V> Helper<K, V> instance() { return INSTANCE; }
      

      public MapMakerInternalMap.Strength keyStrength()
      {
        return MapMakerInternalMap.Strength.WEAK;
      }
      
      public MapMakerInternalMap.Strength valueStrength()
      {
        return MapMakerInternalMap.Strength.WEAK;
      }
      



      public MapMakerInternalMap.WeakKeyWeakValueSegment<K, V> newSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V>, MapMakerInternalMap.WeakKeyWeakValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize)
      {
        return new MapMakerInternalMap.WeakKeyWeakValueSegment(map, initialCapacity, maxSegmentSize);
      }
      



      public MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> copy(MapMakerInternalMap.WeakKeyWeakValueSegment<K, V> segment, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> entry, @Nullable MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> newNext)
      {
        if (entry.getKey() == null)
        {
          return null;
        }
        if (MapMakerInternalMap.Segment.isCollected(entry)) {
          return null;
        }
        return entry.copy(queueForKeys, queueForValues, newNext);
      }
      

      public void setValue(MapMakerInternalMap.WeakKeyWeakValueSegment<K, V> segment, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> entry, V value)
      {
        entry.setValue(value, queueForValues);
      }
      




      public MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> newEntry(MapMakerInternalMap.WeakKeyWeakValueSegment<K, V> segment, K key, int hash, @Nullable MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> next)
      {
        return new MapMakerInternalMap.WeakKeyWeakValueEntry(queueForKeys, key, hash, next);
      }
    }
  }
  



  static abstract interface WeakValueReference<K, V, E extends MapMakerInternalMap.InternalEntry<K, V, E>>
  {
    @Nullable
    public abstract V get();
    



    public abstract E getEntry();
    


    public abstract void clear();
    


    public abstract WeakValueReference<K, V, E> copyFor(ReferenceQueue<V> paramReferenceQueue, E paramE);
  }
  


  static final class DummyInternalEntry
    implements MapMakerInternalMap.InternalEntry<Object, Object, DummyInternalEntry>
  {
    private DummyInternalEntry()
    {
      throw new AssertionError();
    }
    
    public DummyInternalEntry getNext()
    {
      throw new AssertionError();
    }
    
    public int getHash()
    {
      throw new AssertionError();
    }
    
    public Object getKey()
    {
      throw new AssertionError();
    }
    
    public Object getValue()
    {
      throw new AssertionError();
    }
  }
  




  static final WeakValueReference<Object, Object, DummyInternalEntry> UNSET_WEAK_VALUE_REFERENCE = new WeakValueReference()
  {
    public MapMakerInternalMap.DummyInternalEntry getEntry()
    {
      return null;
    }
    

    public void clear() {}
    
    public Object get()
    {
      return null;
    }
    



    public MapMakerInternalMap.WeakValueReference<Object, Object, MapMakerInternalMap.DummyInternalEntry> copyFor(ReferenceQueue<Object> queue, MapMakerInternalMap.DummyInternalEntry entry) { return this; }
  };
  transient Set<K> keySet;
  transient Collection<V> values;
  transient Set<Map.Entry<K, V>> entrySet;
  private static final long serialVersionUID = 5L;
  
  static final class WeakValueReferenceImpl<K, V, E extends MapMakerInternalMap.InternalEntry<K, V, E>> extends WeakReference<V> implements MapMakerInternalMap.WeakValueReference<K, V, E> { @Weak
    final E entry;
    
    WeakValueReferenceImpl(ReferenceQueue<V> queue, V referent, E entry) { super(queue);
      this.entry = entry;
    }
    
    public E getEntry()
    {
      return entry;
    }
    
    public MapMakerInternalMap.WeakValueReference<K, V, E> copyFor(ReferenceQueue<V> queue, E entry)
    {
      return new WeakValueReferenceImpl(queue, get(), entry);
    }
  }
  










  static int rehash(int h)
  {
    h += (h << 15 ^ 0xCD7D);
    h ^= h >>> 10;
    h += (h << 3);
    h ^= h >>> 6;
    h += (h << 2) + (h << 14);
    return h ^ h >>> 16;
  }
  



  @VisibleForTesting
  E copyEntry(E original, E newNext)
  {
    int hash = original.getHash();
    return segmentFor(hash).copyEntry(original, newNext);
  }
  
  int hash(Object key) {
    int h = keyEquivalence.hash(key);
    return rehash(h);
  }
  
  void reclaimValue(WeakValueReference<K, V, E> valueReference) {
    E entry = valueReference.getEntry();
    int hash = entry.getHash();
    segmentFor(hash).reclaimValue(entry.getKey(), hash, valueReference);
  }
  
  void reclaimKey(E entry) {
    int hash = entry.getHash();
    segmentFor(hash).reclaimKey(entry, hash);
  }
  



  @VisibleForTesting
  boolean isLiveForTesting(InternalEntry<K, V, ?> entry)
  {
    return segmentFor(entry.getHash()).getLiveValueForTesting(entry) != null;
  }
  






  Segment<K, V, E, S> segmentFor(int hash)
  {
    return segments[(hash >>> segmentShift & segmentMask)];
  }
  
  Segment<K, V, E, S> createSegment(int initialCapacity, int maxSegmentSize) {
    return entryHelper.newSegment(this, initialCapacity, maxSegmentSize);
  }
  



  V getLiveValue(E entry)
  {
    if (entry.getKey() == null) {
      return null;
    }
    V value = entry.getValue();
    if (value == null) {
      return null;
    }
    return value;
  }
  
  final Segment<K, V, E, S>[] newSegmentArray(int ssize)
  {
    return new Segment[ssize];
  }
  









  static abstract class Segment<K, V, E extends MapMakerInternalMap.InternalEntry<K, V, E>, S extends Segment<K, V, E, S>>
    extends ReentrantLock
  {
    @Weak
    final MapMakerInternalMap<K, V, E, S> map;
    








    volatile int count;
    








    int modCount;
    








    int threshold;
    








    volatile AtomicReferenceArray<E> table;
    








    final int maxSegmentSize;
    








    final AtomicInteger readCount = new AtomicInteger();
    
    Segment(MapMakerInternalMap<K, V, E, S> map, int initialCapacity, int maxSegmentSize) {
      this.map = map;
      this.maxSegmentSize = maxSegmentSize;
      initTable(newEntryArray(initialCapacity));
    }
    


    abstract S self();
    


    @GuardedBy("this")
    void maybeDrainReferenceQueues() {}
    


    void maybeClearReferenceQueues() {}
    


    void setValue(E entry, V value)
    {
      map.entryHelper.setValue(self(), entry, value);
    }
    
    E copyEntry(E original, E newNext)
    {
      return map.entryHelper.copy(self(), original, newNext);
    }
    
    AtomicReferenceArray<E> newEntryArray(int size) {
      return new AtomicReferenceArray(size);
    }
    
    void initTable(AtomicReferenceArray<E> newTable) {
      threshold = (newTable.length() * 3 / 4);
      if (threshold == maxSegmentSize)
      {
        threshold += 1;
      }
      table = newTable;
    }
    





    abstract E castForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> paramInternalEntry);
    




    ReferenceQueue<K> getKeyReferenceQueueForTesting()
    {
      throw new AssertionError();
    }
    
    ReferenceQueue<V> getValueReferenceQueueForTesting()
    {
      throw new AssertionError();
    }
    
    MapMakerInternalMap.WeakValueReference<K, V, E> getWeakValueReferenceForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry)
    {
      throw new AssertionError();
    }
    




    MapMakerInternalMap.WeakValueReference<K, V, E> newWeakValueReferenceForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry, V value)
    {
      throw new AssertionError();
    }
    





    void setWeakValueReferenceForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry, MapMakerInternalMap.WeakValueReference<K, V, ? extends MapMakerInternalMap.InternalEntry<K, V, ?>> valueReference)
    {
      throw new AssertionError();
    }
    


    void setTableEntryForTesting(int i, MapMakerInternalMap.InternalEntry<K, V, ?> entry)
    {
      table.set(i, castForTesting(entry));
    }
    
    E copyForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry, @Nullable MapMakerInternalMap.InternalEntry<K, V, ?> newNext)
    {
      return map.entryHelper.copy(self(), castForTesting(entry), castForTesting(newNext));
    }
    
    void setValueForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry, V value)
    {
      map.entryHelper.setValue(self(), castForTesting(entry), value);
    }
    
    E newEntryForTesting(K key, int hash, @Nullable MapMakerInternalMap.InternalEntry<K, V, ?> next)
    {
      return map.entryHelper.newEntry(self(), key, hash, castForTesting(next));
    }
    
    @CanIgnoreReturnValue
    boolean removeTableEntryForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry)
    {
      return removeEntryForTesting(castForTesting(entry));
    }
    
    E removeFromChainForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> first, MapMakerInternalMap.InternalEntry<K, V, ?> entry)
    {
      return removeFromChain(castForTesting(first), castForTesting(entry));
    }
    


    @Nullable
    V getLiveValueForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry)
    {
      return getLiveValue(castForTesting(entry));
    }
    




    void tryDrainReferenceQueues()
    {
      if (tryLock()) {
        try {
          maybeDrainReferenceQueues();
          
          unlock(); } finally { unlock();
        }
      }
    }
    
    @GuardedBy("this")
    void drainKeyReferenceQueue(ReferenceQueue<K> keyReferenceQueue)
    {
      int i = 0;
      Reference<? extends K> ref; for (; (ref = keyReferenceQueue.poll()) != null; 
          


          i == 16)
      {
        E entry = (MapMakerInternalMap.InternalEntry)ref;
        map.reclaimKey(entry);
        i++;
      }
    }
    


    @GuardedBy("this")
    void drainValueReferenceQueue(ReferenceQueue<V> valueReferenceQueue)
    {
      int i = 0;
      Reference<? extends V> ref; for (; (ref = valueReferenceQueue.poll()) != null; 
          


          i == 16)
      {
        MapMakerInternalMap.WeakValueReference<K, V, E> valueReference = (MapMakerInternalMap.WeakValueReference)ref;
        map.reclaimValue(valueReference);
        i++;
      }
    }
    

    <T> void clearReferenceQueue(ReferenceQueue<T> referenceQueue)
    {
      while (referenceQueue.poll() != null) {}
    }
    

    E getFirst(int hash)
    {
      AtomicReferenceArray<E> table = this.table;
      return (MapMakerInternalMap.InternalEntry)table.get(hash & table.length() - 1);
    }
    

    E getEntry(Object key, int hash)
    {
      if (count != 0) {
        for (E e = getFirst(hash); e != null; e = e.getNext()) {
          if (e.getHash() == hash)
          {


            K entryKey = e.getKey();
            if (entryKey == null) {
              tryDrainReferenceQueues();


            }
            else if (map.keyEquivalence.equivalent(key, entryKey)) {
              return e;
            }
          }
        }
      }
      return null;
    }
    
    E getLiveEntry(Object key, int hash) {
      return getEntry(key, hash);
    }
    
    V get(Object key, int hash) {
      try {
        E e = getLiveEntry(key, hash);
        if (e == null) {
          return null;
        }
        
        Object value = e.getValue();
        if (value == null) {
          tryDrainReferenceQueues();
        }
        return value;
      } finally {
        postReadCleanup();
      }
    }
    
    boolean containsKey(Object key, int hash) {
      try { E e;
        if (count != 0) {
          e = getLiveEntry(key, hash);
          return (e != null) && (e.getValue() != null);
        }
        
        return 0;
      } finally {
        postReadCleanup();
      }
    }
    

    @VisibleForTesting
    boolean containsValue(Object value)
    {
      try
      {
        AtomicReferenceArray<E> table;
        if (count != 0) {
          table = this.table;
          int length = table.length();
          for (int i = 0; i < length; i++) {
            for (E e = (MapMakerInternalMap.InternalEntry)table.get(i); e != null; e = e.getNext()) {
              V entryValue = getLiveValue(e);
              if (entryValue != null)
              {

                if (map.valueEquivalence().equivalent(value, entryValue)) {
                  return true;
                }
              }
            }
          }
        }
        return 0;
      } finally {
        postReadCleanup();
      }
    }
    
    V put(K key, int hash, V value, boolean onlyIfAbsent) {
      lock();
      try {
        preWriteCleanup();
        
        int newCount = count + 1;
        if (newCount > threshold) {
          expand();
          newCount = count + 1;
        }
        
        AtomicReferenceArray<E> table = this.table;
        int index = hash & table.length() - 1;
        E first = (MapMakerInternalMap.InternalEntry)table.get(index);
        
        K entryKey;
        for (E e = first; e != null; e = e.getNext()) {
          entryKey = e.getKey();
          if ((e.getHash() == hash) && (entryKey != null))
          {
            if (map.keyEquivalence.equivalent(key, entryKey))
            {

              V entryValue = e.getValue();
              V ?;
              if (entryValue == null) {
                modCount += 1;
                setValue(e, value);
                newCount = count;
                count = newCount;
                return null; }
              if (onlyIfAbsent)
              {


                return entryValue;
              }
              
              modCount += 1;
              setValue(e, value);
              return entryValue;
            }
          }
        }
        

        modCount += 1;
        E newEntry = map.entryHelper.newEntry(self(), key, hash, first);
        setValue(newEntry, value);
        table.set(index, newEntry);
        count = newCount;
        return null;
      } finally {
        unlock();
      }
    }
    


    @GuardedBy("this")
    void expand()
    {
      AtomicReferenceArray<E> oldTable = table;
      int oldCapacity = oldTable.length();
      if (oldCapacity >= 1073741824) {
        return;
      }
      










      int newCount = count;
      AtomicReferenceArray<E> newTable = newEntryArray(oldCapacity << 1);
      threshold = (newTable.length() * 3 / 4);
      int newMask = newTable.length() - 1;
      for (int oldIndex = 0; oldIndex < oldCapacity; oldIndex++)
      {

        E head = (MapMakerInternalMap.InternalEntry)oldTable.get(oldIndex);
        
        if (head != null) {
          E next = head.getNext();
          int headIndex = head.getHash() & newMask;
          

          if (next == null) {
            newTable.set(headIndex, head);

          }
          else
          {
            E tail = head;
            int tailIndex = headIndex;
            for (E e = next; e != null; e = e.getNext()) {
              int newIndex = e.getHash() & newMask;
              if (newIndex != tailIndex)
              {
                tailIndex = newIndex;
                tail = e;
              }
            }
            newTable.set(tailIndex, tail);
            

            for (E e = head; e != tail; e = e.getNext()) {
              int newIndex = e.getHash() & newMask;
              E newNext = (MapMakerInternalMap.InternalEntry)newTable.get(newIndex);
              E newFirst = copyEntry(e, newNext);
              if (newFirst != null) {
                newTable.set(newIndex, newFirst);
              } else {
                newCount--;
              }
            }
          }
        }
      }
      table = newTable;
      count = newCount;
    }
    
    boolean replace(K key, int hash, V oldValue, V newValue) {
      lock();
      try {
        preWriteCleanup();
        
        AtomicReferenceArray<E> table = this.table;
        int index = hash & table.length() - 1;
        E first = (MapMakerInternalMap.InternalEntry)table.get(index);
        
        for (E e = first; e != null; e = e.getNext()) {
          K entryKey = e.getKey();
          if ((e.getHash() == hash) && (entryKey != null))
          {
            if (map.keyEquivalence.equivalent(key, entryKey))
            {

              V entryValue = e.getValue();
              int newCount; if (entryValue == null) {
                if (isCollected(e)) {
                  newCount = count - 1;
                  modCount += 1;
                  E newFirst = removeFromChain(first, e);
                  newCount = count - 1;
                  table.set(index, newFirst);
                  count = newCount;
                }
                return 0;
              }
              
              if (map.valueEquivalence().equivalent(oldValue, entryValue)) {
                modCount += 1;
                setValue(e, newValue);
                return 1;
              }
              

              return 0;
            }
          }
        }
        
        return 0;
      } finally {
        unlock();
      }
    }
    
    V replace(K key, int hash, V newValue) {
      lock();
      try {
        preWriteCleanup();
        
        AtomicReferenceArray<E> table = this.table;
        int index = hash & table.length() - 1;
        E first = (MapMakerInternalMap.InternalEntry)table.get(index);
        
        for (E e = first; e != null; e = e.getNext()) {
          K entryKey = e.getKey();
          if ((e.getHash() == hash) && (entryKey != null))
          {
            if (map.keyEquivalence.equivalent(key, entryKey))
            {

              V entryValue = e.getValue();
              int newCount; if (entryValue == null) {
                if (isCollected(e)) {
                  newCount = count - 1;
                  modCount += 1;
                  E newFirst = removeFromChain(first, e);
                  newCount = count - 1;
                  table.set(index, newFirst);
                  count = newCount;
                }
                return null;
              }
              
              modCount += 1;
              setValue(e, newValue);
              return entryValue;
            }
          }
        }
        return null;
      } finally {
        unlock();
      }
    }
    
    @CanIgnoreReturnValue
    V remove(Object key, int hash) {
      lock();
      try {
        preWriteCleanup();
        
        int newCount = count - 1;
        AtomicReferenceArray<E> table = this.table;
        int index = hash & table.length() - 1;
        E first = (MapMakerInternalMap.InternalEntry)table.get(index);
        
        for (E e = first; e != null; e = e.getNext()) {
          K entryKey = e.getKey();
          if ((e.getHash() == hash) && (entryKey != null))
          {
            if (map.keyEquivalence.equivalent(key, entryKey)) {
              V entryValue = e.getValue();
              
              if (entryValue == null)
              {
                if (!isCollected(e))
                {

                  return null;
                }
              }
              modCount += 1;
              Object newFirst = removeFromChain(first, e);
              newCount = count - 1;
              table.set(index, newFirst);
              count = newCount;
              return entryValue;
            }
          }
        }
        return null;
      } finally {
        unlock();
      }
    }
    
    boolean remove(Object key, int hash, Object value) {
      lock();
      try {
        preWriteCleanup();
        
        int newCount = count - 1;
        AtomicReferenceArray<E> table = this.table;
        int index = hash & table.length() - 1;
        E first = (MapMakerInternalMap.InternalEntry)table.get(index);
        
        for (E e = first; e != null; e = e.getNext()) {
          K entryKey = e.getKey();
          if ((e.getHash() == hash) && (entryKey != null))
          {
            if (map.keyEquivalence.equivalent(key, entryKey)) {
              V entryValue = e.getValue();
              
              boolean explicitRemoval = false;
              if (map.valueEquivalence().equivalent(value, entryValue)) {
                explicitRemoval = true;
              } else if (!isCollected(e))
              {

                return false;
              }
              
              modCount += 1;
              Object newFirst = removeFromChain(first, e);
              newCount = count - 1;
              table.set(index, newFirst);
              count = newCount;
              return explicitRemoval;
            }
          }
        }
        return 0;
      } finally {
        unlock();
      }
    }
    
    void clear() {
      if (count != 0) {
        lock();
        try {
          AtomicReferenceArray<E> table = this.table;
          for (int i = 0; i < table.length(); i++) {
            table.set(i, null);
          }
          maybeClearReferenceQueues();
          readCount.set(0);
          
          modCount += 1;
          count = 0;
          
          unlock(); } finally { unlock();
        }
      }
    }
    











    @GuardedBy("this")
    E removeFromChain(E first, E entry)
    {
      int newCount = count;
      E newFirst = entry.getNext();
      for (E e = first; e != entry; e = e.getNext()) {
        E next = copyEntry(e, newFirst);
        if (next != null) {
          newFirst = next;
        } else {
          newCount--;
        }
      }
      count = newCount;
      return newFirst;
    }
    
    @CanIgnoreReturnValue
    boolean reclaimKey(E entry, int hash)
    {
      lock();
      try {
        int newCount = count - 1;
        AtomicReferenceArray<E> table = this.table;
        int index = hash & table.length() - 1;
        E first = (MapMakerInternalMap.InternalEntry)table.get(index);
        
        for (E e = first; e != null; e = e.getNext()) {
          if (e == entry) {
            modCount += 1;
            E newFirst = removeFromChain(first, e);
            newCount = count - 1;
            table.set(index, newFirst);
            count = newCount;
            return true;
          }
        }
        
        return 0;
      } finally {
        unlock();
      }
    }
    
    @CanIgnoreReturnValue
    boolean reclaimValue(K key, int hash, MapMakerInternalMap.WeakValueReference<K, V, E> valueReference)
    {
      lock();
      try {
        int newCount = count - 1;
        AtomicReferenceArray<E> table = this.table;
        int index = hash & table.length() - 1;
        E first = (MapMakerInternalMap.InternalEntry)table.get(index);
        
        for (E e = first; e != null; e = e.getNext()) {
          K entryKey = e.getKey();
          if ((e.getHash() == hash) && (entryKey != null))
          {
            if (map.keyEquivalence.equivalent(key, entryKey)) {
              MapMakerInternalMap.WeakValueReference<K, V, E> v = ((MapMakerInternalMap.WeakValueEntry)e).getValueReference();
              E newFirst; if (v == valueReference) {
                modCount += 1;
                newFirst = removeFromChain(first, e);
                newCount = count - 1;
                table.set(index, newFirst);
                count = newCount;
                return true;
              }
              return 0;
            }
          }
        }
        return 0;
      } finally {
        unlock();
      }
    }
    



    @CanIgnoreReturnValue
    boolean clearValueForTesting(K key, int hash, MapMakerInternalMap.WeakValueReference<K, V, ? extends MapMakerInternalMap.InternalEntry<K, V, ?>> valueReference)
    {
      lock();
      try {
        AtomicReferenceArray<E> table = this.table;
        int index = hash & table.length() - 1;
        E first = (MapMakerInternalMap.InternalEntry)table.get(index);
        
        for (E e = first; e != null; e = e.getNext()) {
          K entryKey = e.getKey();
          if ((e.getHash() == hash) && (entryKey != null))
          {
            if (map.keyEquivalence.equivalent(key, entryKey)) {
              MapMakerInternalMap.WeakValueReference<K, V, E> v = ((MapMakerInternalMap.WeakValueEntry)e).getValueReference();
              E newFirst; if (v == valueReference) {
                newFirst = removeFromChain(first, e);
                table.set(index, newFirst);
                return true;
              }
              return 0;
            }
          }
        }
        return 0;
      } finally {
        unlock();
      }
    }
    
    @GuardedBy("this")
    boolean removeEntryForTesting(E entry) {
      int hash = entry.getHash();
      int newCount = count - 1;
      AtomicReferenceArray<E> table = this.table;
      int index = hash & table.length() - 1;
      E first = (MapMakerInternalMap.InternalEntry)table.get(index);
      
      for (E e = first; e != null; e = e.getNext()) {
        if (e == entry) {
          modCount += 1;
          E newFirst = removeFromChain(first, e);
          newCount = count - 1;
          table.set(index, newFirst);
          count = newCount;
          return true;
        }
      }
      
      return false;
    }
    



    static <K, V, E extends MapMakerInternalMap.InternalEntry<K, V, E>> boolean isCollected(E entry)
    {
      return entry.getValue() == null;
    }
    



    @Nullable
    V getLiveValue(E entry)
    {
      if (entry.getKey() == null) {
        tryDrainReferenceQueues();
        return null;
      }
      V value = entry.getValue();
      if (value == null) {
        tryDrainReferenceQueues();
        return null;
      }
      
      return value;
    }
    




    void postReadCleanup()
    {
      if ((readCount.incrementAndGet() & 0x3F) == 0) {
        runCleanup();
      }
    }
    



    @GuardedBy("this")
    void preWriteCleanup()
    {
      runLockedCleanup();
    }
    
    void runCleanup() {
      runLockedCleanup();
    }
    
    void runLockedCleanup() {
      if (tryLock()) {
        try {
          maybeDrainReferenceQueues();
          readCount.set(0);
          
          unlock(); } finally { unlock();
        }
      }
    }
  }
  




  static final class StrongKeyStrongValueSegment<K, V>
    extends MapMakerInternalMap.Segment<K, V, MapMakerInternalMap.StrongKeyStrongValueEntry<K, V>, StrongKeyStrongValueSegment<K, V>>
  {
    StrongKeyStrongValueSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.StrongKeyStrongValueEntry<K, V>, StrongKeyStrongValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize)
    {
      super(initialCapacity, maxSegmentSize);
    }
    
    StrongKeyStrongValueSegment<K, V> self()
    {
      return this;
    }
    

    public MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> castForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry)
    {
      return (MapMakerInternalMap.StrongKeyStrongValueEntry)entry;
    }
  }
  
  static final class StrongKeyWeakValueSegment<K, V>
    extends MapMakerInternalMap.Segment<K, V, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V>, StrongKeyWeakValueSegment<K, V>>
  {
    private final ReferenceQueue<V> queueForValues = new ReferenceQueue();
    



    StrongKeyWeakValueSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V>, StrongKeyWeakValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize)
    {
      super(initialCapacity, maxSegmentSize);
    }
    
    StrongKeyWeakValueSegment<K, V> self()
    {
      return this;
    }
    
    ReferenceQueue<V> getValueReferenceQueueForTesting()
    {
      return queueForValues;
    }
    

    public MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> castForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry)
    {
      return (MapMakerInternalMap.StrongKeyWeakValueEntry)entry;
    }
    

    public MapMakerInternalMap.WeakValueReference<K, V, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V>> getWeakValueReferenceForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> e)
    {
      return castForTesting(e).getValueReference();
    }
    

    public MapMakerInternalMap.WeakValueReference<K, V, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V>> newWeakValueReferenceForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> e, V value)
    {
      return new MapMakerInternalMap.WeakValueReferenceImpl(queueForValues, value, 
        castForTesting(e));
    }
    


    public void setWeakValueReferenceForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> e, MapMakerInternalMap.WeakValueReference<K, V, ? extends MapMakerInternalMap.InternalEntry<K, V, ?>> valueReference)
    {
      MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> entry = castForTesting(e);
      
      MapMakerInternalMap.WeakValueReference<K, V, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V>> newValueReference = valueReference;
      
      MapMakerInternalMap.WeakValueReference<K, V, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V>> previous = MapMakerInternalMap.StrongKeyWeakValueEntry.access$500(entry);
      MapMakerInternalMap.StrongKeyWeakValueEntry.access$502(entry, newValueReference);
      previous.clear();
    }
    
    void maybeDrainReferenceQueues()
    {
      drainValueReferenceQueue(queueForValues);
    }
    
    void maybeClearReferenceQueues()
    {
      clearReferenceQueue(queueForValues);
    }
  }
  
  static final class WeakKeyStrongValueSegment<K, V>
    extends MapMakerInternalMap.Segment<K, V, MapMakerInternalMap.WeakKeyStrongValueEntry<K, V>, WeakKeyStrongValueSegment<K, V>>
  {
    private final ReferenceQueue<K> queueForKeys = new ReferenceQueue();
    



    WeakKeyStrongValueSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.WeakKeyStrongValueEntry<K, V>, WeakKeyStrongValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize)
    {
      super(initialCapacity, maxSegmentSize);
    }
    
    WeakKeyStrongValueSegment<K, V> self()
    {
      return this;
    }
    
    ReferenceQueue<K> getKeyReferenceQueueForTesting()
    {
      return queueForKeys;
    }
    

    public MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> castForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry)
    {
      return (MapMakerInternalMap.WeakKeyStrongValueEntry)entry;
    }
    
    void maybeDrainReferenceQueues()
    {
      drainKeyReferenceQueue(queueForKeys);
    }
    
    void maybeClearReferenceQueues()
    {
      clearReferenceQueue(queueForKeys);
    }
  }
  
  static final class WeakKeyWeakValueSegment<K, V>
    extends MapMakerInternalMap.Segment<K, V, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V>, WeakKeyWeakValueSegment<K, V>>
  {
    private final ReferenceQueue<K> queueForKeys = new ReferenceQueue();
    private final ReferenceQueue<V> queueForValues = new ReferenceQueue();
    


    WeakKeyWeakValueSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V>, WeakKeyWeakValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize)
    {
      super(initialCapacity, maxSegmentSize);
    }
    
    WeakKeyWeakValueSegment<K, V> self()
    {
      return this;
    }
    
    ReferenceQueue<K> getKeyReferenceQueueForTesting()
    {
      return queueForKeys;
    }
    
    ReferenceQueue<V> getValueReferenceQueueForTesting()
    {
      return queueForValues;
    }
    

    public MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> castForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry)
    {
      return (MapMakerInternalMap.WeakKeyWeakValueEntry)entry;
    }
    

    public MapMakerInternalMap.WeakValueReference<K, V, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V>> getWeakValueReferenceForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> e)
    {
      return castForTesting(e).getValueReference();
    }
    

    public MapMakerInternalMap.WeakValueReference<K, V, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V>> newWeakValueReferenceForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> e, V value)
    {
      return new MapMakerInternalMap.WeakValueReferenceImpl(queueForValues, value, 
        castForTesting(e));
    }
    


    public void setWeakValueReferenceForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> e, MapMakerInternalMap.WeakValueReference<K, V, ? extends MapMakerInternalMap.InternalEntry<K, V, ?>> valueReference)
    {
      MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> entry = castForTesting(e);
      
      MapMakerInternalMap.WeakValueReference<K, V, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V>> newValueReference = valueReference;
      
      MapMakerInternalMap.WeakValueReference<K, V, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V>> previous = MapMakerInternalMap.WeakKeyWeakValueEntry.access$600(entry);
      MapMakerInternalMap.WeakKeyWeakValueEntry.access$602(entry, newValueReference);
      previous.clear();
    }
    
    void maybeDrainReferenceQueues()
    {
      drainKeyReferenceQueue(queueForKeys);
      drainValueReferenceQueue(queueForValues);
    }
    
    void maybeClearReferenceQueues()
    {
      clearReferenceQueue(queueForKeys);
    }
  }
  
  static final class CleanupMapTask implements Runnable {
    final WeakReference<MapMakerInternalMap<?, ?, ?, ?>> mapReference;
    
    public CleanupMapTask(MapMakerInternalMap<?, ?, ?, ?> map) {
      mapReference = new WeakReference(map);
    }
    
    public void run()
    {
      MapMakerInternalMap<?, ?, ?, ?> map = (MapMakerInternalMap)mapReference.get();
      if (map == null) {
        throw new CancellationException();
      }
      
      for (MapMakerInternalMap.Segment<?, ?, ?, ?> segment : segments) {
        segment.runCleanup();
      }
    }
  }
  
  @VisibleForTesting
  Strength keyStrength() {
    return entryHelper.keyStrength();
  }
  
  @VisibleForTesting
  Strength valueStrength() {
    return entryHelper.valueStrength();
  }
  
  @VisibleForTesting
  Equivalence<Object> valueEquivalence() {
    return entryHelper.valueStrength().defaultEquivalence();
  }
  









  public boolean isEmpty()
  {
    long sum = 0L;
    Segment<K, V, E, S>[] segments = this.segments;
    for (int i = 0; i < segments.length; i++) {
      if (count != 0) {
        return false;
      }
      sum += modCount;
    }
    
    if (sum != 0L) {
      for (int i = 0; i < segments.length; i++) {
        if (count != 0) {
          return false;
        }
        sum -= modCount;
      }
      if (sum != 0L) {
        return false;
      }
    }
    return true;
  }
  
  public int size()
  {
    Segment<K, V, E, S>[] segments = this.segments;
    long sum = 0L;
    for (int i = 0; i < segments.length; i++) {
      sum += count;
    }
    return Ints.saturatedCast(sum);
  }
  
  public V get(@Nullable Object key)
  {
    if (key == null) {
      return null;
    }
    int hash = hash(key);
    return segmentFor(hash).get(key, hash);
  }
  



  E getEntry(@Nullable Object key)
  {
    if (key == null) {
      return null;
    }
    int hash = hash(key);
    return segmentFor(hash).getEntry(key, hash);
  }
  
  public boolean containsKey(@Nullable Object key)
  {
    if (key == null) {
      return false;
    }
    int hash = hash(key);
    return segmentFor(hash).containsKey(key, hash);
  }
  
  public boolean containsValue(@Nullable Object value)
  {
    if (value == null) {
      return false;
    }
    





    Segment<K, V, E, S>[] segments = this.segments;
    long last = -1L;
    for (int i = 0; i < 3; i++) {
      long sum = 0L;
      for (Segment<K, V, E, S> segment : segments)
      {
        int unused = count;
        
        AtomicReferenceArray<E> table = table;
        for (int j = 0; j < table.length(); j++) {
          for (E e = (InternalEntry)table.get(j); e != null; e = e.getNext()) {
            V v = segment.getLiveValue(e);
            if ((v != null) && (valueEquivalence().equivalent(value, v))) {
              return true;
            }
          }
        }
        sum += modCount;
      }
      if (sum == last) {
        break;
      }
      last = sum;
    }
    return false;
  }
  
  @CanIgnoreReturnValue
  public V put(K key, V value)
  {
    Preconditions.checkNotNull(key);
    Preconditions.checkNotNull(value);
    int hash = hash(key);
    return segmentFor(hash).put(key, hash, value, false);
  }
  
  @CanIgnoreReturnValue
  public V putIfAbsent(K key, V value)
  {
    Preconditions.checkNotNull(key);
    Preconditions.checkNotNull(value);
    int hash = hash(key);
    return segmentFor(hash).put(key, hash, value, true);
  }
  
  public void putAll(Map<? extends K, ? extends V> m)
  {
    for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
      put(e.getKey(), e.getValue());
    }
  }
  
  @CanIgnoreReturnValue
  public V remove(@Nullable Object key)
  {
    if (key == null) {
      return null;
    }
    int hash = hash(key);
    return segmentFor(hash).remove(key, hash);
  }
  
  @CanIgnoreReturnValue
  public boolean remove(@Nullable Object key, @Nullable Object value)
  {
    if ((key == null) || (value == null)) {
      return false;
    }
    int hash = hash(key);
    return segmentFor(hash).remove(key, hash, value);
  }
  
  @CanIgnoreReturnValue
  public boolean replace(K key, @Nullable V oldValue, V newValue)
  {
    Preconditions.checkNotNull(key);
    Preconditions.checkNotNull(newValue);
    if (oldValue == null) {
      return false;
    }
    int hash = hash(key);
    return segmentFor(hash).replace(key, hash, oldValue, newValue);
  }
  
  @CanIgnoreReturnValue
  public V replace(K key, V value)
  {
    Preconditions.checkNotNull(key);
    Preconditions.checkNotNull(value);
    int hash = hash(key);
    return segmentFor(hash).replace(key, hash, value);
  }
  
  public void clear()
  {
    for (Segment<K, V, E, S> segment : segments) {
      segment.clear();
    }
  }
  


  public Set<K> keySet()
  {
    Set<K> ks = keySet;
    return this.keySet = new KeySet();
  }
  


  public Collection<V> values()
  {
    Collection<V> vs = values;
    return this.values = new Values();
  }
  


  public Set<Map.Entry<K, V>> entrySet()
  {
    Set<Map.Entry<K, V>> es = entrySet;
    return this.entrySet = new EntrySet();
  }
  
  abstract class HashIterator<T>
    implements Iterator<T>
  {
    int nextSegmentIndex;
    int nextTableIndex;
    MapMakerInternalMap.Segment<K, V, E, S> currentSegment;
    AtomicReferenceArray<E> currentTable;
    E nextEntry;
    MapMakerInternalMap<K, V, E, S>.WriteThroughEntry nextExternal;
    MapMakerInternalMap<K, V, E, S>.WriteThroughEntry lastReturned;
    
    HashIterator()
    {
      nextSegmentIndex = (segments.length - 1);
      nextTableIndex = -1;
      advance();
    }
    
    public abstract T next();
    
    final void advance()
    {
      nextExternal = null;
      
      if (nextInChain()) {
        return;
      }
      
      if (nextInTable()) {
        return;
      }
      
      while (nextSegmentIndex >= 0) {
        currentSegment = segments[(nextSegmentIndex--)];
        if (currentSegment.count != 0) {
          currentTable = currentSegment.table;
          nextTableIndex = (currentTable.length() - 1);
          if (nextInTable()) {}
        }
      }
    }
    




    boolean nextInChain()
    {
      if (nextEntry != null) {
        for (nextEntry = nextEntry.getNext(); nextEntry != null; nextEntry = nextEntry.getNext()) {
          if (advanceTo(nextEntry)) {
            return true;
          }
        }
      }
      return false;
    }
    


    boolean nextInTable()
    {
      while (nextTableIndex >= 0) {
        if (((this.nextEntry = (MapMakerInternalMap.InternalEntry)currentTable.get(nextTableIndex--)) != null) && (
          (advanceTo(nextEntry)) || (nextInChain()))) {
          return true;
        }
      }
      
      return false;
    }
    


    boolean advanceTo(E entry)
    {
      try
      {
        K key = entry.getKey();
        V value = getLiveValue(entry);
        boolean bool; if (value != null) {
          nextExternal = new MapMakerInternalMap.WriteThroughEntry(MapMakerInternalMap.this, key, value);
          return true;
        }
        
        return false;
      }
      finally {
        currentSegment.postReadCleanup();
      }
    }
    
    public boolean hasNext()
    {
      return nextExternal != null;
    }
    
    MapMakerInternalMap<K, V, E, S>.WriteThroughEntry nextEntry() {
      if (nextExternal == null) {
        throw new NoSuchElementException();
      }
      lastReturned = nextExternal;
      advance();
      return lastReturned;
    }
    
    public void remove()
    {
      CollectPreconditions.checkRemove(lastReturned != null);
      remove(lastReturned.getKey());
      lastReturned = null;
    }
  }
  
  final class KeyIterator extends MapMakerInternalMap<K, V, E, S>.HashIterator<K> { KeyIterator() { super(); }
    


    public K next() { return nextEntry().getKey(); }
  }
  
  final class ValueIterator extends MapMakerInternalMap<K, V, E, S>.HashIterator<V> {
    ValueIterator() { super(); }
    
    public V next()
    {
      return nextEntry().getValue();
    }
  }
  

  final class WriteThroughEntry
    extends AbstractMapEntry<K, V>
  {
    final K key;
    V value;
    
    WriteThroughEntry(V key)
    {
      this.key = key;
      this.value = value;
    }
    
    public K getKey()
    {
      return key;
    }
    
    public V getValue()
    {
      return value;
    }
    

    public boolean equals(@Nullable Object object)
    {
      if ((object instanceof Map.Entry)) {
        Map.Entry<?, ?> that = (Map.Entry)object;
        return (key.equals(that.getKey())) && (value.equals(that.getValue()));
      }
      return false;
    }
    

    public int hashCode()
    {
      return key.hashCode() ^ value.hashCode();
    }
    
    public V setValue(V newValue)
    {
      V oldValue = put(key, newValue);
      value = newValue;
      return oldValue;
    }
  }
  
  final class EntryIterator extends MapMakerInternalMap<K, V, E, S>.HashIterator<Map.Entry<K, V>> { EntryIterator() { super(); }
    
    public Map.Entry<K, V> next()
    {
      return nextEntry();
    }
  }
  
  final class KeySet extends MapMakerInternalMap.SafeToArraySet<K> {
    KeySet() { super(); }
    
    public Iterator<K> iterator()
    {
      return new MapMakerInternalMap.KeyIterator(MapMakerInternalMap.this);
    }
    
    public int size()
    {
      return MapMakerInternalMap.this.size();
    }
    
    public boolean isEmpty()
    {
      return MapMakerInternalMap.this.isEmpty();
    }
    
    public boolean contains(Object o)
    {
      return containsKey(o);
    }
    
    public boolean remove(Object o)
    {
      return remove(o) != null;
    }
    
    public void clear()
    {
      MapMakerInternalMap.this.clear();
    }
  }
  
  final class Values extends AbstractCollection<V>
  {
    Values() {}
    
    public Iterator<V> iterator() {
      return new MapMakerInternalMap.ValueIterator(MapMakerInternalMap.this);
    }
    
    public int size()
    {
      return MapMakerInternalMap.this.size();
    }
    
    public boolean isEmpty()
    {
      return MapMakerInternalMap.this.isEmpty();
    }
    
    public boolean contains(Object o)
    {
      return containsValue(o);
    }
    
    public void clear()
    {
      MapMakerInternalMap.this.clear();
    }
    



    public Object[] toArray()
    {
      return MapMakerInternalMap.toArrayList(this).toArray();
    }
    
    public <E> E[] toArray(E[] a)
    {
      return MapMakerInternalMap.toArrayList(this).toArray(a);
    }
  }
  
  final class EntrySet extends MapMakerInternalMap.SafeToArraySet<Map.Entry<K, V>> {
    EntrySet() { super(); }
    
    public Iterator<Map.Entry<K, V>> iterator()
    {
      return new MapMakerInternalMap.EntryIterator(MapMakerInternalMap.this);
    }
    
    public boolean contains(Object o)
    {
      if (!(o instanceof Map.Entry)) {
        return false;
      }
      Map.Entry<?, ?> e = (Map.Entry)o;
      Object key = e.getKey();
      if (key == null) {
        return false;
      }
      V v = get(key);
      
      return (v != null) && (valueEquivalence().equivalent(e.getValue(), v));
    }
    
    public boolean remove(Object o)
    {
      if (!(o instanceof Map.Entry)) {
        return false;
      }
      Map.Entry<?, ?> e = (Map.Entry)o;
      Object key = e.getKey();
      return (key != null) && (remove(key, e.getValue()));
    }
    
    public int size()
    {
      return MapMakerInternalMap.this.size();
    }
    
    public boolean isEmpty()
    {
      return MapMakerInternalMap.this.isEmpty();
    }
    
    public void clear()
    {
      MapMakerInternalMap.this.clear();
    }
  }
  
  private static abstract class SafeToArraySet<E> extends AbstractSet<E>
  {
    private SafeToArraySet() {}
    
    public Object[] toArray()
    {
      return MapMakerInternalMap.toArrayList(this).toArray();
    }
    
    public <E> E[] toArray(E[] a)
    {
      return MapMakerInternalMap.toArrayList(this).toArray(a);
    }
  }
  
  private static <E> ArrayList<E> toArrayList(Collection<E> c)
  {
    ArrayList<E> result = new ArrayList(c.size());
    Iterators.addAll(result, c.iterator());
    return result;
  }
  



  Object writeReplace()
  {
    return new SerializationProxy(entryHelper
      .keyStrength(), entryHelper
      .valueStrength(), keyEquivalence, entryHelper
      
      .valueStrength().defaultEquivalence(), concurrencyLevel, this);
  }
  


  static abstract class AbstractSerializationProxy<K, V>
    extends ForwardingConcurrentMap<K, V>
    implements Serializable
  {
    private static final long serialVersionUID = 3L;
    

    final MapMakerInternalMap.Strength keyStrength;
    

    final MapMakerInternalMap.Strength valueStrength;
    
    final Equivalence<Object> keyEquivalence;
    
    final Equivalence<Object> valueEquivalence;
    
    final int concurrencyLevel;
    
    transient ConcurrentMap<K, V> delegate;
    

    AbstractSerializationProxy(MapMakerInternalMap.Strength keyStrength, MapMakerInternalMap.Strength valueStrength, Equivalence<Object> keyEquivalence, Equivalence<Object> valueEquivalence, int concurrencyLevel, ConcurrentMap<K, V> delegate)
    {
      this.keyStrength = keyStrength;
      this.valueStrength = valueStrength;
      this.keyEquivalence = keyEquivalence;
      this.valueEquivalence = valueEquivalence;
      this.concurrencyLevel = concurrencyLevel;
      this.delegate = delegate;
    }
    
    protected ConcurrentMap<K, V> delegate()
    {
      return delegate;
    }
    
    void writeMapTo(ObjectOutputStream out) throws IOException {
      out.writeInt(delegate.size());
      for (Map.Entry<K, V> entry : delegate.entrySet()) {
        out.writeObject(entry.getKey());
        out.writeObject(entry.getValue());
      }
      out.writeObject(null);
    }
    
    MapMaker readMapMaker(ObjectInputStream in) throws IOException
    {
      int size = in.readInt();
      return new MapMaker()
        .initialCapacity(size)
        .setKeyStrength(keyStrength)
        .setValueStrength(valueStrength)
        .keyEquivalence(keyEquivalence)
        .concurrencyLevel(concurrencyLevel);
    }
    
    void readEntries(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
      for (;;) {
        K key = in.readObject();
        if (key == null) {
          break;
        }
        V value = in.readObject();
        delegate.put(key, value);
      }
    }
  }
  




  private static final class SerializationProxy<K, V>
    extends MapMakerInternalMap.AbstractSerializationProxy<K, V>
  {
    private static final long serialVersionUID = 3L;
    



    SerializationProxy(MapMakerInternalMap.Strength keyStrength, MapMakerInternalMap.Strength valueStrength, Equivalence<Object> keyEquivalence, Equivalence<Object> valueEquivalence, int concurrencyLevel, ConcurrentMap<K, V> delegate)
    {
      super(valueStrength, keyEquivalence, valueEquivalence, concurrencyLevel, delegate);
    }
    




    private void writeObject(ObjectOutputStream out)
      throws IOException
    {
      out.defaultWriteObject();
      writeMapTo(out);
    }
    
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
      in.defaultReadObject();
      MapMaker mapMaker = readMapMaker(in);
      delegate = mapMaker.makeMap();
      readEntries(in);
    }
    
    private Object readResolve() {
      return delegate;
    }
  }
}
