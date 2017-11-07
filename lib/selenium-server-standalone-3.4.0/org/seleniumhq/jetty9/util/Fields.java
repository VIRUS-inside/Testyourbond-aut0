package org.seleniumhq.jetty9.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;



























public class Fields
  implements Iterable<Field>
{
  private final boolean caseSensitive;
  private final Map<String, Field> fields;
  
  public Fields()
  {
    this(false);
  }
  





  public Fields(boolean caseSensitive)
  {
    this.caseSensitive = caseSensitive;
    fields = new LinkedHashMap();
  }
  







  public Fields(Fields original, boolean immutable)
  {
    caseSensitive = caseSensitive;
    Map<String, Field> copy = new LinkedHashMap();
    copy.putAll(fields);
    fields = (immutable ? Collections.unmodifiableMap(copy) : copy);
  }
  

  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if ((obj == null) || (getClass() != obj.getClass()))
      return false;
    Fields that = (Fields)obj;
    if (getSize() != that.getSize())
      return false;
    if (caseSensitive != caseSensitive)
      return false;
    for (Map.Entry<String, Field> entry : fields.entrySet())
    {
      String name = (String)entry.getKey();
      Field value = (Field)entry.getValue();
      if (!value.equals(that.get(name), caseSensitive))
        return false;
    }
    return true;
  }
  

  public int hashCode()
  {
    return fields.hashCode();
  }
  



  public Set<String> getNames()
  {
    Set<String> result = new LinkedHashSet();
    for (Field field : fields.values())
      result.add(field.getName());
    return result;
  }
  
  private String normalizeName(String name)
  {
    return caseSensitive ? name : name.toLowerCase(Locale.ENGLISH);
  }
  




  public Field get(String name)
  {
    return (Field)fields.get(normalizeName(name));
  }
  







  public void put(String name, String value)
  {
    Field field = new Field(name, value);
    fields.put(normalizeName(name), field);
  }
  





  public void put(Field field)
  {
    if (field != null) {
      fields.put(normalizeName(field.getName()), field);
    }
  }
  






  public void add(String name, String value)
  {
    String key = normalizeName(name);
    Field field = (Field)fields.get(key);
    if (field == null)
    {

      field = new Field(name, value);
      fields.put(key, field);
    }
    else
    {
      field = new Field(field.getName(), field.getValues(), new String[] { value }, null);
      fields.put(key, field);
    }
  }
  






  public Field remove(String name)
  {
    return (Field)fields.remove(normalizeName(name));
  }
  




  public void clear()
  {
    fields.clear();
  }
  



  public boolean isEmpty()
  {
    return fields.isEmpty();
  }
  



  public int getSize()
  {
    return fields.size();
  }
  




  public Iterator<Field> iterator()
  {
    return fields.values().iterator();
  }
  

  public String toString()
  {
    return fields.toString();
  }
  


  public static class Field
  {
    private final String name;
    
    private final List<String> values;
    

    public Field(String name, String value)
    {
      this(name, Collections.singletonList(value), new String[0]);
    }
    
    private Field(String name, List<String> values, String... moreValues)
    {
      this.name = name;
      List<String> list = new ArrayList(values.size() + moreValues.length);
      list.addAll(values);
      list.addAll(Arrays.asList(moreValues));
      this.values = Collections.unmodifiableList(list);
    }
    
    public boolean equals(Field that, boolean caseSensitive)
    {
      if (this == that)
        return true;
      if (that == null)
        return false;
      if (caseSensitive)
        return equals(that);
      return (name.equalsIgnoreCase(name)) && (values.equals(values));
    }
    

    public boolean equals(Object obj)
    {
      if (this == obj)
        return true;
      if ((obj == null) || (getClass() != obj.getClass()))
        return false;
      Field that = (Field)obj;
      return (name.equals(name)) && (values.equals(values));
    }
    

    public int hashCode()
    {
      int result = name.hashCode();
      result = 31 * result + values.hashCode();
      return result;
    }
    



    public String getName()
    {
      return name;
    }
    



    public String getValue()
    {
      return (String)values.get(0);
    }
    








    public Integer getValueAsInt()
    {
      String value = getValue();
      return value == null ? null : Integer.valueOf(value);
    }
    



    public List<String> getValues()
    {
      return values;
    }
    



    public boolean hasMultipleValues()
    {
      return values.size() > 1;
    }
    

    public String toString()
    {
      return String.format("%s=%s", new Object[] { name, values });
    }
  }
}
