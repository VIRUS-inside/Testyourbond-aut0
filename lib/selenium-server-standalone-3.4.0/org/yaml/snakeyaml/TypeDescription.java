package org.yaml.snakeyaml;

import java.util.HashMap;
import java.util.Map;
import org.yaml.snakeyaml.nodes.Tag;



















public final class TypeDescription
{
  private final Class<? extends Object> type;
  private Tag tag;
  private Map<String, Class<? extends Object>> listProperties;
  private Map<String, Class<? extends Object>> keyProperties;
  private Map<String, Class<? extends Object>> valueProperties;
  
  public TypeDescription(Class<? extends Object> clazz, Tag tag)
  {
    type = clazz;
    this.tag = tag;
    listProperties = new HashMap();
    keyProperties = new HashMap();
    valueProperties = new HashMap();
  }
  
  public TypeDescription(Class<? extends Object> clazz, String tag) {
    this(clazz, new Tag(tag));
  }
  
  public TypeDescription(Class<? extends Object> clazz) {
    this(clazz, (Tag)null);
  }
  





  public Tag getTag()
  {
    return tag;
  }
  





  public void setTag(Tag tag)
  {
    this.tag = tag;
  }
  
  public void setTag(String tag) {
    setTag(new Tag(tag));
  }
  




  public Class<? extends Object> getType()
  {
    return type;
  }
  







  public void putListPropertyType(String property, Class<? extends Object> type)
  {
    listProperties.put(property, type);
  }
  






  public Class<? extends Object> getListPropertyType(String property)
  {
    return (Class)listProperties.get(property);
  }
  










  public void putMapPropertyType(String property, Class<? extends Object> key, Class<? extends Object> value)
  {
    keyProperties.put(property, key);
    valueProperties.put(property, value);
  }
  






  public Class<? extends Object> getMapKeyType(String property)
  {
    return (Class)keyProperties.get(property);
  }
  






  public Class<? extends Object> getMapValueType(String property)
  {
    return (Class)valueProperties.get(property);
  }
  
  public String toString()
  {
    return "TypeDescription for " + getType() + " (tag='" + getTag() + "')";
  }
}
