package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;























































@Beta
@GwtCompatible
public abstract class MultimapBuilder<K0, V0>
{
  private static final int DEFAULT_EXPECTED_KEYS = 8;
  
  private MultimapBuilder() {}
  
  public static MultimapBuilderWithKeys<Object> hashKeys()
  {
    return hashKeys(8);
  }
  





  public static MultimapBuilderWithKeys<Object> hashKeys(int expectedKeys)
  {
    CollectPreconditions.checkNonnegative(expectedKeys, "expectedKeys");
    new MultimapBuilderWithKeys()
    {
      <K, V> Map<K, Collection<V>> createMap() {
        return Maps.newHashMapWithExpectedSize(val$expectedKeys);
      }
    };
  }
  







  public static MultimapBuilderWithKeys<Object> linkedHashKeys()
  {
    return linkedHashKeys(8);
  }
  








  public static MultimapBuilderWithKeys<Object> linkedHashKeys(int expectedKeys)
  {
    CollectPreconditions.checkNonnegative(expectedKeys, "expectedKeys");
    new MultimapBuilderWithKeys()
    {
      <K, V> Map<K, Collection<V>> createMap() {
        return Maps.newLinkedHashMapWithExpectedSize(val$expectedKeys);
      }
    };
  }
  










  public static MultimapBuilderWithKeys<Comparable> treeKeys()
  {
    return treeKeys(Ordering.natural());
  }
  












  public static <K0> MultimapBuilderWithKeys<K0> treeKeys(Comparator<K0> comparator)
  {
    Preconditions.checkNotNull(comparator);
    new MultimapBuilderWithKeys()
    {
      <K extends K0, V> Map<K, Collection<V>> createMap() {
        return new TreeMap(val$comparator);
      }
    };
  }
  



  public static <K0 extends Enum<K0>> MultimapBuilderWithKeys<K0> enumKeys(Class<K0> keyClass)
  {
    Preconditions.checkNotNull(keyClass);
    new MultimapBuilderWithKeys()
    {



      <K extends K0, V> Map<K, Collection<V>> createMap() {
        return new EnumMap(val$keyClass); }
    };
  }
  
  public abstract <K extends K0, V extends V0> Multimap<K, V> build();
  
  private static final class ArrayListSupplier<V> implements Supplier<List<V>>, Serializable {
    private final int expectedValuesPerKey;
    
    ArrayListSupplier(int expectedValuesPerKey) { this.expectedValuesPerKey = CollectPreconditions.checkNonnegative(expectedValuesPerKey, "expectedValuesPerKey"); }
    

    public List<V> get()
    {
      return new ArrayList(expectedValuesPerKey);
    }
  }
  
  private static enum LinkedListSupplier implements Supplier<List<Object>> {
    INSTANCE;
    
    private LinkedListSupplier() {}
    
    public static <V> Supplier<List<V>> instance() {
      Supplier<List<V>> result = INSTANCE;
      return result;
    }
    
    public List<Object> get()
    {
      return new LinkedList();
    }
  }
  
  private static final class HashSetSupplier<V> implements Supplier<Set<V>>, Serializable {
    private final int expectedValuesPerKey;
    
    HashSetSupplier(int expectedValuesPerKey) {
      this.expectedValuesPerKey = CollectPreconditions.checkNonnegative(expectedValuesPerKey, "expectedValuesPerKey");
    }
    
    public Set<V> get()
    {
      return Sets.newHashSetWithExpectedSize(expectedValuesPerKey);
    }
  }
  
  private static final class LinkedHashSetSupplier<V> implements Supplier<Set<V>>, Serializable {
    private final int expectedValuesPerKey;
    
    LinkedHashSetSupplier(int expectedValuesPerKey) {
      this.expectedValuesPerKey = CollectPreconditions.checkNonnegative(expectedValuesPerKey, "expectedValuesPerKey");
    }
    
    public Set<V> get()
    {
      return Sets.newLinkedHashSetWithExpectedSize(expectedValuesPerKey);
    }
  }
  
  private static final class TreeSetSupplier<V> implements Supplier<SortedSet<V>>, Serializable {
    private final Comparator<? super V> comparator;
    
    TreeSetSupplier(Comparator<? super V> comparator) {
      this.comparator = ((Comparator)Preconditions.checkNotNull(comparator));
    }
    
    public SortedSet<V> get()
    {
      return new TreeSet(comparator);
    }
  }
  
  private static final class EnumSetSupplier<V extends Enum<V>> implements Supplier<Set<V>>, Serializable
  {
    private final Class<V> clazz;
    
    EnumSetSupplier(Class<V> clazz) {
      this.clazz = ((Class)Preconditions.checkNotNull(clazz));
    }
    
    public Set<V> get()
    {
      return EnumSet.noneOf(clazz);
    }
  }
  


  public static abstract class MultimapBuilderWithKeys<K0>
  {
    private static final int DEFAULT_EXPECTED_VALUES_PER_KEY = 2;
    


    MultimapBuilderWithKeys() {}
    


    abstract <K extends K0, V> Map<K, Collection<V>> createMap();
    


    public MultimapBuilder.ListMultimapBuilder<K0, Object> arrayListValues()
    {
      return arrayListValues(2);
    }
    





    public MultimapBuilder.ListMultimapBuilder<K0, Object> arrayListValues(final int expectedValuesPerKey)
    {
      CollectPreconditions.checkNonnegative(expectedValuesPerKey, "expectedValuesPerKey");
      new MultimapBuilder.ListMultimapBuilder()
      {
        public <K extends K0, V> ListMultimap<K, V> build() {
          return Multimaps.newListMultimap(
            createMap(), new MultimapBuilder.ArrayListSupplier(expectedValuesPerKey));
        }
      };
    }
    



    public MultimapBuilder.ListMultimapBuilder<K0, Object> linkedListValues()
    {
      new MultimapBuilder.ListMultimapBuilder()
      {
        public <K extends K0, V> ListMultimap<K, V> build() {
          return Multimaps.newListMultimap(
            createMap(), MultimapBuilder.LinkedListSupplier.instance());
        }
      };
    }
    


    public MultimapBuilder.SetMultimapBuilder<K0, Object> hashSetValues()
    {
      return hashSetValues(2);
    }
    





    public MultimapBuilder.SetMultimapBuilder<K0, Object> hashSetValues(final int expectedValuesPerKey)
    {
      CollectPreconditions.checkNonnegative(expectedValuesPerKey, "expectedValuesPerKey");
      new MultimapBuilder.SetMultimapBuilder()
      {
        public <K extends K0, V> SetMultimap<K, V> build() {
          return Multimaps.newSetMultimap(
            createMap(), new MultimapBuilder.HashSetSupplier(expectedValuesPerKey));
        }
      };
    }
    



    public MultimapBuilder.SetMultimapBuilder<K0, Object> linkedHashSetValues()
    {
      return linkedHashSetValues(2);
    }
    





    public MultimapBuilder.SetMultimapBuilder<K0, Object> linkedHashSetValues(final int expectedValuesPerKey)
    {
      CollectPreconditions.checkNonnegative(expectedValuesPerKey, "expectedValuesPerKey");
      new MultimapBuilder.SetMultimapBuilder()
      {
        public <K extends K0, V> SetMultimap<K, V> build() {
          return Multimaps.newSetMultimap(
            createMap(), new MultimapBuilder.LinkedHashSetSupplier(expectedValuesPerKey));
        }
      };
    }
    




    public MultimapBuilder.SortedSetMultimapBuilder<K0, Comparable> treeSetValues()
    {
      return treeSetValues(Ordering.natural());
    }
    





    public <V0> MultimapBuilder.SortedSetMultimapBuilder<K0, V0> treeSetValues(final Comparator<V0> comparator)
    {
      Preconditions.checkNotNull(comparator, "comparator");
      new MultimapBuilder.SortedSetMultimapBuilder()
      {
        public <K extends K0, V extends V0> SortedSetMultimap<K, V> build() {
          return Multimaps.newSortedSetMultimap(
            createMap(), new MultimapBuilder.TreeSetSupplier(comparator));
        }
      };
    }
    



    public <V0 extends Enum<V0>> MultimapBuilder.SetMultimapBuilder<K0, V0> enumSetValues(final Class<V0> valueClass)
    {
      Preconditions.checkNotNull(valueClass, "valueClass");
      new MultimapBuilder.SetMultimapBuilder()
      {

        public <K extends K0, V extends V0> SetMultimap<K, V> build()
        {

          Supplier<Set<V>> factory = new MultimapBuilder.EnumSetSupplier(valueClass);
          return Multimaps.newSetMultimap(createMap(), factory);
        }
      };
    }
  }
  









  public <K extends K0, V extends V0> Multimap<K, V> build(Multimap<? extends K, ? extends V> multimap)
  {
    Multimap<K, V> result = build();
    result.putAll(multimap);
    return result;
  }
  
  public static abstract class ListMultimapBuilder<K0, V0> extends MultimapBuilder<K0, V0>
  {
    ListMultimapBuilder()
    {
      super();
    }
    

    public abstract <K extends K0, V extends V0> ListMultimap<K, V> build();
    
    public <K extends K0, V extends V0> ListMultimap<K, V> build(Multimap<? extends K, ? extends V> multimap)
    {
      return (ListMultimap)super.build(multimap);
    }
  }
  
  public static abstract class SetMultimapBuilder<K0, V0> extends MultimapBuilder<K0, V0>
  {
    SetMultimapBuilder()
    {
      super();
    }
    

    public abstract <K extends K0, V extends V0> SetMultimap<K, V> build();
    
    public <K extends K0, V extends V0> SetMultimap<K, V> build(Multimap<? extends K, ? extends V> multimap)
    {
      return (SetMultimap)super.build(multimap);
    }
  }
  

  public static abstract class SortedSetMultimapBuilder<K0, V0>
    extends MultimapBuilder.SetMultimapBuilder<K0, V0>
  {
    SortedSetMultimapBuilder() {}
    

    public abstract <K extends K0, V extends V0> SortedSetMultimap<K, V> build();
    

    public <K extends K0, V extends V0> SortedSetMultimap<K, V> build(Multimap<? extends K, ? extends V> multimap)
    {
      return (SortedSetMultimap)super.build(multimap);
    }
  }
}
