package org.eclipse.jetty.util;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;




















































public class LazyList
  implements Cloneable, Serializable
{
  private static final String[] __EMTPY_STRING_ARRAY = new String[0];
  





  private LazyList() {}
  




  public static Object add(Object list, Object item)
  {
    if (list == null)
    {
      if (((item instanceof List)) || (item == null))
      {
        List<Object> l = new ArrayList();
        l.add(item);
        return l;
      }
      
      return item;
    }
    
    if ((list instanceof List))
    {
      ((List)list).add(item);
      return list;
    }
    
    List<Object> l = new ArrayList();
    l.add(list);
    l.add(item);
    return l;
  }
  








  public static Object add(Object list, int index, Object item)
  {
    if (list == null)
    {
      if ((index > 0) || ((item instanceof List)) || (item == null))
      {
        List<Object> l = new ArrayList();
        l.add(index, item);
        return l;
      }
      return item;
    }
    
    if ((list instanceof List))
    {
      ((List)list).add(index, item);
      return list;
    }
    
    List<Object> l = new ArrayList();
    l.add(list);
    l.add(index, item);
    return l;
  }
  






  public static Object addCollection(Object list, Collection<?> collection)
  {
    Iterator<?> i = collection.iterator();
    while (i.hasNext())
      list = add(list, i.next());
    return list;
  }
  






  public static Object addArray(Object list, Object[] array)
  {
    for (int i = 0; (array != null) && (i < array.length); i++)
      list = add(list, array[i]);
    return list;
  }
  






  public static Object ensureSize(Object list, int initialSize)
  {
    if (list == null)
      return new ArrayList(initialSize);
    if ((list instanceof ArrayList))
    {
      ArrayList<?> ol = (ArrayList)list;
      if (ol.size() > initialSize)
        return ol;
      ArrayList<Object> nl = new ArrayList(initialSize);
      nl.addAll(ol);
      return nl;
    }
    List<Object> l = new ArrayList(initialSize);
    l.add(list);
    return l;
  }
  

  public static Object remove(Object list, Object o)
  {
    if (list == null) {
      return null;
    }
    if ((list instanceof List))
    {
      List<?> l = (List)list;
      l.remove(o);
      if (l.size() == 0)
        return null;
      return list;
    }
    
    if (list.equals(o))
      return null;
    return list;
  }
  

  public static Object remove(Object list, int i)
  {
    if (list == null) {
      return null;
    }
    if ((list instanceof List))
    {
      List<?> l = (List)list;
      l.remove(i);
      if (l.size() == 0)
        return null;
      return list;
    }
    
    if (i == 0)
      return null;
    return list;
  }
  










  public static <E> List<E> getList(Object list)
  {
    return getList(list, false);
  }
  












  public static <E> List<E> getList(Object list, boolean nullForEmpty)
  {
    if (list == null)
    {
      if (nullForEmpty)
        return null;
      return Collections.emptyList();
    }
    if ((list instanceof List)) {
      return (List)list;
    }
    return Collections.singletonList(list);
  }
  







  public static boolean hasEntry(Object list)
  {
    if (list == null)
      return false;
    if ((list instanceof List))
      return !((List)list).isEmpty();
    return true;
  }
  







  public static boolean isEmpty(Object list)
  {
    if (list == null)
      return true;
    if ((list instanceof List))
      return ((List)list).isEmpty();
    return false;
  }
  


  public static String[] toStringArray(Object list)
  {
    if (list == null) {
      return __EMTPY_STRING_ARRAY;
    }
    if ((list instanceof List))
    {
      List<?> l = (List)list;
      String[] a = new String[l.size()];
      for (int i = l.size(); i-- > 0;)
      {
        Object o = l.get(i);
        if (o != null)
          a[i] = o.toString();
      }
      return a;
    }
    
    return new String[] { list.toString() };
  }
  






  public static Object toArray(Object list, Class<?> clazz)
  {
    if (list == null) {
      return Array.newInstance(clazz, 0);
    }
    if ((list instanceof List))
    {
      List<?> l = (List)list;
      if (clazz.isPrimitive())
      {
        Object a = Array.newInstance(clazz, l.size());
        for (int i = 0; i < l.size(); i++)
          Array.set(a, i, l.get(i));
        return a;
      }
      return l.toArray((Object[])Array.newInstance(clazz, l.size()));
    }
    

    Object a = Array.newInstance(clazz, 1);
    Array.set(a, 0, list);
    return a;
  }
  





  public static int size(Object list)
  {
    if (list == null)
      return 0;
    if ((list instanceof List))
      return ((List)list).size();
    return 1;
  }
  








  public static <E> E get(Object list, int i)
  {
    if (list == null) {
      throw new IndexOutOfBoundsException();
    }
    if ((list instanceof List)) {
      return ((List)list).get(i);
    }
    if (i == 0) {
      return list;
    }
    throw new IndexOutOfBoundsException();
  }
  

  public static boolean contains(Object list, Object item)
  {
    if (list == null) {
      return false;
    }
    if ((list instanceof List)) {
      return ((List)list).contains(item);
    }
    return list.equals(item);
  }
  


  public static Object clone(Object list)
  {
    if (list == null)
      return null;
    if ((list instanceof List))
      return new ArrayList((List)list);
    return list;
  }
  

  public static String toString(Object list)
  {
    if (list == null)
      return "[]";
    if ((list instanceof List))
      return list.toString();
    return "[" + list + "]";
  }
  


  public static <E> Iterator<E> iterator(Object list)
  {
    if (list == null)
    {
      List<E> empty = Collections.emptyList();
      return empty.iterator();
    }
    if ((list instanceof List))
    {
      return ((List)list).iterator();
    }
    List<E> l = getList(list);
    return l.iterator();
  }
  


  public static <E> ListIterator<E> listIterator(Object list)
  {
    if (list == null)
    {
      List<E> empty = Collections.emptyList();
      return empty.listIterator();
    }
    if ((list instanceof List)) {
      return ((List)list).listIterator();
    }
    List<E> l = getList(list);
    return l.listIterator();
  }
}
