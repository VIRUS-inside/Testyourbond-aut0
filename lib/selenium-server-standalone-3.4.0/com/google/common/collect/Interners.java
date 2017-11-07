package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Equivalence;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import java.util.concurrent.ConcurrentMap;


























@Beta
@GwtIncompatible
public final class Interners
{
  private Interners() {}
  
  public static class InternerBuilder
  {
    private final MapMaker mapMaker = new MapMaker();
    private boolean strong = true;
    



    private InternerBuilder() {}
    


    public InternerBuilder strong()
    {
      strong = true;
      return this;
    }
    




    @GwtIncompatible("java.lang.ref.WeakReference")
    public InternerBuilder weak()
    {
      strong = false;
      return this;
    }
    




    public InternerBuilder concurrencyLevel(int concurrencyLevel)
    {
      mapMaker.concurrencyLevel(concurrencyLevel);
      return this;
    }
    
    public <E> Interner<E> build() {
      return strong ? new Interners.StrongInterner(mapMaker, null) : new Interners.WeakInterner(mapMaker, null);
    }
  }
  
  public static InternerBuilder newBuilder()
  {
    return new InternerBuilder(null);
  }
  




  public static <E> Interner<E> newStrongInterner()
  {
    return newBuilder().strong().build();
  }
  





  @GwtIncompatible("java.lang.ref.WeakReference")
  public static <E> Interner<E> newWeakInterner()
  {
    return newBuilder().weak().build();
  }
  
  @VisibleForTesting
  static final class StrongInterner<E> implements Interner<E> {
    @VisibleForTesting
    final ConcurrentMap<E, E> map;
    
    private StrongInterner(MapMaker mapMaker) {
      map = mapMaker.makeMap();
    }
    
    public E intern(E sample)
    {
      E canonical = map.putIfAbsent(Preconditions.checkNotNull(sample), sample);
      return canonical == null ? sample : canonical;
    }
  }
  
  @VisibleForTesting
  static final class WeakInterner<E> implements Interner<E>
  {
    @VisibleForTesting
    final MapMakerInternalMap<E, Dummy, ?, ?> map;
    
    private WeakInterner(MapMaker mapMaker) {
      map = mapMaker.weakKeys().keyEquivalence(Equivalence.equals()).makeCustomMap();
    }
    
    public E intern(E sample)
    {
      for (;;)
      {
        MapMakerInternalMap.InternalEntry<E, Dummy, ?> entry = map.getEntry(sample);
        if (entry != null) {
          E canonical = entry.getKey();
          if (canonical != null) {
            return canonical;
          }
        }
        

        Dummy sneaky = (Dummy)map.putIfAbsent(sample, Dummy.VALUE);
        if (sneaky == null) {
          return sample;
        }
      }
    }
    






    private static enum Dummy
    {
      VALUE;
      

      private Dummy() {}
    }
  }
  

  public static <E> Function<E, E> asFunction(Interner<E> interner)
  {
    return new InternerFunction((Interner)Preconditions.checkNotNull(interner));
  }
  
  private static class InternerFunction<E> implements Function<E, E>
  {
    private final Interner<E> interner;
    
    public InternerFunction(Interner<E> interner) {
      this.interner = interner;
    }
    
    public E apply(E input)
    {
      return interner.intern(input);
    }
    
    public int hashCode()
    {
      return interner.hashCode();
    }
    
    public boolean equals(Object other)
    {
      if ((other instanceof InternerFunction)) {
        InternerFunction<?> that = (InternerFunction)other;
        return interner.equals(interner);
      }
      
      return false;
    }
  }
}
