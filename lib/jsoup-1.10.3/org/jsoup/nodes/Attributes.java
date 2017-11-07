package org.jsoup.nodes;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.jsoup.SerializationException;
import org.jsoup.helper.Validate;












public class Attributes
  implements Iterable<Attribute>, Cloneable
{
  protected static final String dataPrefix = "data-";
  private LinkedHashMap<String, Attribute> attributes = null;
  



  public Attributes() {}
  


  public String get(String key)
  {
    Validate.notEmpty(key);
    
    if (attributes == null) {
      return "";
    }
    Attribute attr = (Attribute)attributes.get(key);
    return attr != null ? attr.getValue() : "";
  }
  




  public String getIgnoreCase(String key)
  {
    Validate.notEmpty(key);
    if (attributes == null) {
      return "";
    }
    Attribute attr = (Attribute)attributes.get(key);
    if (attr != null) {
      return attr.getValue();
    }
    for (String attrKey : attributes.keySet()) {
      if (attrKey.equalsIgnoreCase(key))
        return ((Attribute)attributes.get(attrKey)).getValue();
    }
    return "";
  }
  




  public void put(String key, String value)
  {
    Attribute attr = new Attribute(key, value);
    put(attr);
  }
  




  public void put(String key, boolean value)
  {
    if (value) {
      put(new BooleanAttribute(key));
    } else {
      remove(key);
    }
  }
  


  public void put(Attribute attribute)
  {
    Validate.notNull(attribute);
    if (attributes == null)
      attributes = new LinkedHashMap(2);
    attributes.put(attribute.getKey(), attribute);
  }
  



  public void remove(String key)
  {
    Validate.notEmpty(key);
    if (attributes == null)
      return;
    attributes.remove(key);
  }
  



  public void removeIgnoreCase(String key)
  {
    Validate.notEmpty(key);
    if (attributes == null)
      return;
    for (Iterator<String> it = attributes.keySet().iterator(); it.hasNext();) {
      String attrKey = (String)it.next();
      if (attrKey.equalsIgnoreCase(key)) {
        it.remove();
      }
    }
  }
  



  public boolean hasKey(String key)
  {
    return (attributes != null) && (attributes.containsKey(key));
  }
  




  public boolean hasKeyIgnoreCase(String key)
  {
    if (attributes == null)
      return false;
    for (String attrKey : attributes.keySet()) {
      if (attrKey.equalsIgnoreCase(key))
        return true;
    }
    return false;
  }
  



  public int size()
  {
    if (attributes == null)
      return 0;
    return attributes.size();
  }
  



  public void addAll(Attributes incoming)
  {
    if (incoming.size() == 0)
      return;
    if (attributes == null)
      attributes = new LinkedHashMap(incoming.size());
    attributes.putAll(attributes);
  }
  
  public Iterator<Attribute> iterator() {
    if ((attributes == null) || (attributes.isEmpty())) {
      return Collections.emptyList().iterator();
    }
    
    return attributes.values().iterator();
  }
  




  public List<Attribute> asList()
  {
    if (attributes == null) {
      return Collections.emptyList();
    }
    List<Attribute> list = new ArrayList(attributes.size());
    for (Map.Entry<String, Attribute> entry : attributes.entrySet()) {
      list.add(entry.getValue());
    }
    return Collections.unmodifiableList(list);
  }
  




  public Map<String, String> dataset()
  {
    return new Dataset(null);
  }
  




  public String html()
  {
    StringBuilder accum = new StringBuilder();
    try {
      html(accum, new Document("").outputSettings());
    } catch (IOException e) {
      throw new SerializationException(e);
    }
    return accum.toString();
  }
  
  void html(Appendable accum, Document.OutputSettings out) throws IOException {
    if (attributes == null) {
      return;
    }
    for (Map.Entry<String, Attribute> entry : attributes.entrySet()) {
      Attribute attribute = (Attribute)entry.getValue();
      accum.append(" ");
      attribute.html(accum, out);
    }
  }
  
  public String toString()
  {
    return html();
  }
  





  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (!(o instanceof Attributes)) { return false;
    }
    Attributes that = (Attributes)o;
    
    return attributes != null ? attributes.equals(attributes) : attributes == null;
  }
  




  public int hashCode()
  {
    return attributes != null ? attributes.hashCode() : 0;
  }
  
  public Attributes clone()
  {
    if (attributes == null) {
      return new Attributes();
    }
    try
    {
      clone = (Attributes)super.clone();
    } catch (CloneNotSupportedException e) { Attributes clone;
      throw new RuntimeException(e); }
    Attributes clone;
    attributes = new LinkedHashMap(attributes.size());
    for (Attribute attribute : this)
      attributes.put(attribute.getKey(), attribute.clone());
    return clone;
  }
  
  private class Dataset extends AbstractMap<String, String>
  {
    private Dataset() {
      if (attributes == null) {
        attributes = new LinkedHashMap(2);
      }
    }
    
    public Set<Map.Entry<String, String>> entrySet() {
      return new EntrySet(null);
    }
    
    public String put(String key, String value)
    {
      String dataKey = Attributes.dataKey(key);
      String oldValue = hasKey(dataKey) ? ((Attribute)attributes.get(dataKey)).getValue() : null;
      Attribute attr = new Attribute(dataKey, value);
      attributes.put(dataKey, attr);
      return oldValue;
    }
    
    private class EntrySet extends AbstractSet<Map.Entry<String, String>> {
      private EntrySet() {}
      
      public Iterator<Map.Entry<String, String>> iterator() {
        return new Attributes.Dataset.DatasetIterator(Attributes.Dataset.this, null);
      }
      
      public int size()
      {
        int count = 0;
        Iterator iter = new Attributes.Dataset.DatasetIterator(Attributes.Dataset.this, null);
        while (iter.hasNext())
          count++;
        return count;
      } }
    
    private class DatasetIterator implements Iterator<Map.Entry<String, String>> { private DatasetIterator() {}
      
      private Iterator<Attribute> attrIter = attributes.values().iterator();
      
      public boolean hasNext() {
        while (attrIter.hasNext()) {
          attr = ((Attribute)attrIter.next());
          if (attr.isDataAttribute()) return true;
        }
        return false;
      }
      
      private Attribute attr;
      public Map.Entry<String, String> next() { return new Attribute(attr.getKey().substring("data-".length()), attr.getValue()); }
      
      public void remove()
      {
        attributes.remove(attr.getKey());
      }
    }
  }
  
  private static String dataKey(String key) {
    return "data-" + key;
  }
}
