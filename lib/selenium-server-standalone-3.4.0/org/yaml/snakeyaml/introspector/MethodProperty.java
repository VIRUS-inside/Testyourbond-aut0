package org.yaml.snakeyaml.introspector;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import org.yaml.snakeyaml.error.YAMLException;























public class MethodProperty
  extends GenericProperty
{
  private final PropertyDescriptor property;
  private final boolean readable;
  private final boolean writable;
  
  public MethodProperty(PropertyDescriptor property)
  {
    super(property.getName(), property.getPropertyType(), property.getReadMethod() == null ? null : property.getReadMethod().getGenericReturnType());
    

    this.property = property;
    readable = (property.getReadMethod() != null);
    writable = (property.getWriteMethod() != null);
  }
  
  public void set(Object object, Object value) throws Exception
  {
    property.getWriteMethod().invoke(object, new Object[] { value });
  }
  
  public Object get(Object object)
  {
    try {
      property.getReadMethod().setAccessible(true);
      return property.getReadMethod().invoke(object, new Object[0]);
    } catch (Exception e) {
      throw new YAMLException("Unable to find getter for property '" + property.getName() + "' on object " + object + ":" + e);
    }
  }
  

  public boolean isWritable()
  {
    return writable;
  }
  
  public boolean isReadable()
  {
    return readable;
  }
}
