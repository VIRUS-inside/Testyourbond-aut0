package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.MapMaker;
import com.google.common.math.IntMath;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


































































































@Beta
@GwtIncompatible
public abstract class Striped<L>
{
  private static final int LARGE_LAZY_CUTOFF = 1024;
  
  private Striped() {}
  
  public abstract L get(Object paramObject);
  
  public abstract L getAt(int paramInt);
  
  abstract int indexFor(Object paramObject);
  
  public abstract int size();
  
  public Iterable<L> bulkGet(Iterable<?> keys)
  {
    Object[] array = Iterables.toArray(keys, Object.class);
    if (array.length == 0) {
      return ImmutableList.of();
    }
    int[] stripes = new int[array.length];
    for (int i = 0; i < array.length; i++) {
      stripes[i] = indexFor(array[i]);
    }
    Arrays.sort(stripes);
    
    int previousStripe = stripes[0];
    array[0] = getAt(previousStripe);
    for (int i = 1; i < array.length; i++) {
      int currentStripe = stripes[i];
      if (currentStripe == previousStripe) {
        array[i] = array[(i - 1)];
      } else {
        array[i] = getAt(currentStripe);
        previousStripe = currentStripe;
      }
    }
    

















    List<L> asList = Arrays.asList(array);
    return Collections.unmodifiableList(asList);
  }
  








  public static Striped<Lock> lock(int stripes)
  {
    new CompactStriped(stripes, new Supplier()
    {


      public Lock get() {
        return new Striped.PaddedLock(); } }, null);
  }
  








  public static Striped<Lock> lazyWeakLock(int stripes)
  {
    lazy(stripes, new Supplier()
    {

      public Lock get()
      {
        return new ReentrantLock(false);
      }
    });
  }
  
  private static <L> Striped<L> lazy(int stripes, Supplier<L> supplier) {
    return stripes < 1024 ? new SmallLazyStriped(stripes, supplier) : new LargeLazyStriped(stripes, supplier);
  }
  









  public static Striped<Semaphore> semaphore(int stripes, int permits)
  {
    new CompactStriped(stripes, new Supplier()
    {


      public Semaphore get() {
        return new Striped.PaddedSemaphore(val$permits); } }, null);
  }
  









  public static Striped<Semaphore> lazyWeakSemaphore(int stripes, int permits)
  {
    lazy(stripes, new Supplier()
    {

      public Semaphore get()
      {
        return new Semaphore(val$permits, false);
      }
    });
  }
  






  public static Striped<ReadWriteLock> readWriteLock(int stripes)
  {
    return new CompactStriped(stripes, READ_WRITE_LOCK_SUPPLIER, null);
  }
  






  public static Striped<ReadWriteLock> lazyWeakReadWriteLock(int stripes)
  {
    return lazy(stripes, READ_WRITE_LOCK_SUPPLIER);
  }
  

  private static final Supplier<ReadWriteLock> READ_WRITE_LOCK_SUPPLIER = new Supplier()
  {

    public ReadWriteLock get() {
      return new ReentrantReadWriteLock(); }
  };
  private static final int ALL_SET = -1;
  
  private static abstract class PowerOfTwoStriped<L> extends Striped<L> {
    final int mask;
    
    PowerOfTwoStriped(int stripes) {
      super();
      Preconditions.checkArgument(stripes > 0, "Stripes must be positive");
      mask = (stripes > 1073741824 ? -1 : Striped.ceilToPowerOfTwo(stripes) - 1);
    }
    
    final int indexFor(Object key)
    {
      int hash = Striped.smear(key.hashCode());
      return hash & mask;
    }
    
    public final L get(Object key)
    {
      return getAt(indexFor(key));
    }
  }
  

  private static class CompactStriped<L>
    extends Striped.PowerOfTwoStriped<L>
  {
    private final Object[] array;
    

    private CompactStriped(int stripes, Supplier<L> supplier)
    {
      super();
      Preconditions.checkArgument(stripes <= 1073741824, "Stripes must be <= 2^30)");
      
      array = new Object[mask + 1];
      for (int i = 0; i < array.length; i++) {
        array[i] = supplier.get();
      }
    }
    

    public L getAt(int index)
    {
      return array[index];
    }
    
    public int size()
    {
      return array.length;
    }
  }
  

  @VisibleForTesting
  static class SmallLazyStriped<L>
    extends Striped.PowerOfTwoStriped<L>
  {
    final AtomicReferenceArray<ArrayReference<? extends L>> locks;
    
    final Supplier<L> supplier;
    
    final int size;
    final ReferenceQueue<L> queue = new ReferenceQueue();
    
    SmallLazyStriped(int stripes, Supplier<L> supplier) {
      super();
      size = (mask == -1 ? Integer.MAX_VALUE : mask + 1);
      locks = new AtomicReferenceArray(size);
      this.supplier = supplier;
    }
    
    public L getAt(int index)
    {
      if (size != Integer.MAX_VALUE) {
        Preconditions.checkElementIndex(index, size());
      }
      ArrayReference<? extends L> existingRef = (ArrayReference)locks.get(index);
      L existing = existingRef == null ? null : existingRef.get();
      if (existing != null) {
        return existing;
      }
      L created = supplier.get();
      ArrayReference<L> newRef = new ArrayReference(created, index, queue);
      while (!locks.compareAndSet(index, existingRef, newRef))
      {
        existingRef = (ArrayReference)locks.get(index);
        existing = existingRef == null ? null : existingRef.get();
        if (existing != null) {
          return existing;
        }
      }
      drainQueue();
      return created;
    }
    

    private void drainQueue()
    {
      Reference<? extends L> ref;
      
      while ((ref = queue.poll()) != null)
      {
        ArrayReference<? extends L> arrayRef = (ArrayReference)ref;
        

        locks.compareAndSet(index, arrayRef, null);
      }
    }
    
    public int size()
    {
      return size;
    }
    
    private static final class ArrayReference<L> extends WeakReference<L> {
      final int index;
      
      ArrayReference(L referent, int index, ReferenceQueue<L> queue) {
        super(queue);
        this.index = index;
      }
    }
  }
  

  @VisibleForTesting
  static class LargeLazyStriped<L>
    extends Striped.PowerOfTwoStriped<L>
  {
    final ConcurrentMap<Integer, L> locks;
    
    final Supplier<L> supplier;
    final int size;
    
    LargeLazyStriped(int stripes, Supplier<L> supplier)
    {
      super();
      size = (mask == -1 ? Integer.MAX_VALUE : mask + 1);
      this.supplier = supplier;
      locks = new MapMaker().weakValues().makeMap();
    }
    
    public L getAt(int index)
    {
      if (size != Integer.MAX_VALUE) {
        Preconditions.checkElementIndex(index, size());
      }
      L existing = locks.get(Integer.valueOf(index));
      if (existing != null) {
        return existing;
      }
      L created = supplier.get();
      existing = locks.putIfAbsent(Integer.valueOf(index), created);
      return MoreObjects.firstNonNull(existing, created);
    }
    
    public int size()
    {
      return size;
    }
  }
  




  private static int ceilToPowerOfTwo(int x)
  {
    return 1 << IntMath.log2(x, RoundingMode.CEILING);
  }
  








  private static int smear(int hashCode)
  {
    hashCode ^= hashCode >>> 20 ^ hashCode >>> 12;
    return hashCode ^ hashCode >>> 7 ^ hashCode >>> 4;
  }
  

  private static class PaddedLock
    extends ReentrantLock
  {
    long unused1;
    
    long unused2;
    long unused3;
    
    PaddedLock()
    {
      super();
    }
  }
  
  private static class PaddedSemaphore extends Semaphore
  {
    long unused1;
    long unused2;
    long unused3;
    
    PaddedSemaphore(int permits) {
      super(false);
    }
  }
}
