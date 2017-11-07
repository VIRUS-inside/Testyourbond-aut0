package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Comparator;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.annotation.Nullable;


























































@GwtCompatible(serializable=true, emulated=true)
public class TreeMultimap<K, V>
  extends AbstractSortedKeySortedSetMultimap<K, V>
{
  private transient Comparator<? super K> keyComparator;
  private transient Comparator<? super V> valueComparator;
  @GwtIncompatible
  private static final long serialVersionUID = 0L;
  
  public static <K extends Comparable, V extends Comparable> TreeMultimap<K, V> create()
  {
    return new TreeMultimap(Ordering.natural(), Ordering.natural());
  }
  








  public static <K, V> TreeMultimap<K, V> create(Comparator<? super K> keyComparator, Comparator<? super V> valueComparator)
  {
    return new TreeMultimap((Comparator)Preconditions.checkNotNull(keyComparator), (Comparator)Preconditions.checkNotNull(valueComparator));
  }
  






  public static <K extends Comparable, V extends Comparable> TreeMultimap<K, V> create(Multimap<? extends K, ? extends V> multimap)
  {
    return new TreeMultimap(Ordering.natural(), Ordering.natural(), multimap);
  }
  
  TreeMultimap(Comparator<? super K> keyComparator, Comparator<? super V> valueComparator) {
    super(new TreeMap(keyComparator));
    this.keyComparator = keyComparator;
    this.valueComparator = valueComparator;
  }
  


  private TreeMultimap(Comparator<? super K> keyComparator, Comparator<? super V> valueComparator, Multimap<? extends K, ? extends V> multimap)
  {
    this(keyComparator, valueComparator);
    putAll(multimap);
  }
  








  SortedSet<V> createCollection()
  {
    return new TreeSet(valueComparator);
  }
  
  Collection<V> createCollection(@Nullable K key)
  {
    if (key == null) {
      keyComparator().compare(key, key);
    }
    return super.createCollection(key);
  }
  





  @Deprecated
  public Comparator<? super K> keyComparator()
  {
    return keyComparator;
  }
  
  public Comparator<? super V> valueComparator()
  {
    return valueComparator;
  }
  



  @GwtIncompatible
  public NavigableSet<V> get(@Nullable K key)
  {
    return (NavigableSet)super.get(key);
  }
  









  public NavigableSet<K> keySet()
  {
    return (NavigableSet)super.keySet();
  }
  









  public NavigableMap<K, Collection<V>> asMap()
  {
    return (NavigableMap)super.asMap();
  }
  



  @GwtIncompatible
  private void writeObject(ObjectOutputStream stream)
    throws IOException
  {
    stream.defaultWriteObject();
    stream.writeObject(keyComparator());
    stream.writeObject(valueComparator());
    Serialization.writeMultimap(this, stream);
  }
  
  @GwtIncompatible
  private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException
  {
    stream.defaultReadObject();
    keyComparator = ((Comparator)Preconditions.checkNotNull((Comparator)stream.readObject()));
    valueComparator = ((Comparator)Preconditions.checkNotNull((Comparator)stream.readObject()));
    setMap(new TreeMap(keyComparator));
    Serialization.populateMultimap(this, stream);
  }
}
