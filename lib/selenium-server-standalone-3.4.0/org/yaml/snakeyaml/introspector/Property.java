package org.yaml.snakeyaml.introspector;










public abstract class Property
  implements Comparable<Property>
{
  private final String name;
  








  private final Class<?> type;
  








  public Property(String name, Class<?> type)
  {
    this.name = name;
    this.type = type;
  }
  
  public Class<?> getType() {
    return type;
  }
  
  public abstract Class<?>[] getActualTypeArguments();
  
  public String getName() {
    return name;
  }
  
  public String toString()
  {
    return getName() + " of " + getType();
  }
  
  public int compareTo(Property o) {
    return name.compareTo(name);
  }
  
  public boolean isWritable() {
    return true;
  }
  
  public boolean isReadable() {
    return true;
  }
  
  public abstract void set(Object paramObject1, Object paramObject2) throws Exception;
  
  public abstract Object get(Object paramObject);
  
  public int hashCode()
  {
    return name.hashCode() + type.hashCode();
  }
  
  public boolean equals(Object other)
  {
    if ((other instanceof Property)) {
      Property p = (Property)other;
      return (name.equals(p.getName())) && (type.equals(p.getType()));
    }
    return false;
  }
}
