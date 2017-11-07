package org.yaml.snakeyaml.introspector;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.yaml.snakeyaml.error.YAMLException;
















public class PropertyUtils
{
  private final Map<Class<?>, Map<String, Property>> propertiesCache = new HashMap();
  private final Map<Class<?>, Set<Property>> readableProperties = new HashMap();
  private BeanAccess beanAccess = BeanAccess.DEFAULT;
  private boolean allowReadOnlyProperties = false;
  private boolean skipMissingProperties = false;
  
  public PropertyUtils() {}
  
  protected Map<String, Property> getPropertiesMap(Class<?> type, BeanAccess bAccess) throws IntrospectionException { if (propertiesCache.containsKey(type)) {
      return (Map)propertiesCache.get(type);
    }
    
    Map<String, Property> properties = new LinkedHashMap();
    boolean inaccessableFieldsExist = false;
    switch (1.$SwitchMap$org$yaml$snakeyaml$introspector$BeanAccess[bAccess.ordinal()]) {
    case 1: 
      for (Class<?> c = type; c != null; c = c.getSuperclass()) {
        for (Field field : c.getDeclaredFields()) {
          int modifiers = field.getModifiers();
          if ((!Modifier.isStatic(modifiers)) && (!Modifier.isTransient(modifiers)) && (!properties.containsKey(field.getName())))
          {
            properties.put(field.getName(), new FieldProperty(field));
          }
        }
      }
      break;
    
    default: 
      for (PropertyDescriptor property : Introspector.getBeanInfo(type).getPropertyDescriptors())
      {
        Method readMethod = property.getReadMethod();
        if ((readMethod == null) || (!readMethod.getName().equals("getClass"))) {
          properties.put(property.getName(), new MethodProperty(property));
        }
      }
      

      for (Class<?> c = type; c != null; c = c.getSuperclass()) {
        for (Field field : c.getDeclaredFields()) {
          int modifiers = field.getModifiers();
          if ((!Modifier.isStatic(modifiers)) && (!Modifier.isTransient(modifiers))) {
            if (Modifier.isPublic(modifiers)) {
              properties.put(field.getName(), new FieldProperty(field));
            } else {
              inaccessableFieldsExist = true;
            }
          }
        }
      }
    }
    
    if ((properties.isEmpty()) && (inaccessableFieldsExist)) {
      throw new YAMLException("No JavaBean properties found in " + type.getName());
    }
    propertiesCache.put(type, properties);
    return properties;
  }
  
  public Set<Property> getProperties(Class<? extends Object> type) throws IntrospectionException {
    return getProperties(type, beanAccess);
  }
  
  public Set<Property> getProperties(Class<? extends Object> type, BeanAccess bAccess) throws IntrospectionException
  {
    if (readableProperties.containsKey(type)) {
      return (Set)readableProperties.get(type);
    }
    Set<Property> properties = createPropertySet(type, bAccess);
    readableProperties.put(type, properties);
    return properties;
  }
  
  protected Set<Property> createPropertySet(Class<? extends Object> type, BeanAccess bAccess) throws IntrospectionException
  {
    Set<Property> properties = new TreeSet();
    Collection<Property> props = getPropertiesMap(type, bAccess).values();
    for (Property property : props) {
      if ((property.isReadable()) && ((allowReadOnlyProperties) || (property.isWritable()))) {
        properties.add(property);
      }
    }
    return properties;
  }
  
  public Property getProperty(Class<? extends Object> type, String name) throws IntrospectionException
  {
    return getProperty(type, name, beanAccess);
  }
  
  public Property getProperty(Class<? extends Object> type, String name, BeanAccess bAccess) throws IntrospectionException
  {
    Map<String, Property> properties = getPropertiesMap(type, bAccess);
    Property property = (Property)properties.get(name);
    if ((property == null) && (skipMissingProperties)) {
      property = new MissingProperty(name);
    }
    if ((property == null) || (!property.isWritable())) {
      throw new YAMLException("Unable to find property '" + name + "' on class: " + type.getName());
    }
    
    return property;
  }
  
  public void setBeanAccess(BeanAccess beanAccess) {
    if (this.beanAccess != beanAccess) {
      this.beanAccess = beanAccess;
      propertiesCache.clear();
      readableProperties.clear();
    }
  }
  
  public void setAllowReadOnlyProperties(boolean allowReadOnlyProperties) {
    if (this.allowReadOnlyProperties != allowReadOnlyProperties) {
      this.allowReadOnlyProperties = allowReadOnlyProperties;
      readableProperties.clear();
    }
  }
  






  public void setSkipMissingProperties(boolean skipMissingProperties)
  {
    if (this.skipMissingProperties != skipMissingProperties) {
      this.skipMissingProperties = skipMissingProperties;
      readableProperties.clear();
    }
  }
}
