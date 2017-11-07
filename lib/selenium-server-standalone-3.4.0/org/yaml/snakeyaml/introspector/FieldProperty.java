package org.yaml.snakeyaml.introspector;

import java.lang.reflect.Field;
import org.yaml.snakeyaml.error.YAMLException;





















public class FieldProperty
  extends GenericProperty
{
  private final Field field;
  
  public FieldProperty(Field field)
  {
    super(field.getName(), field.getType(), field.getGenericType());
    this.field = field;
    field.setAccessible(true);
  }
  
  public void set(Object object, Object value) throws Exception
  {
    field.set(object, value);
  }
  
  public Object get(Object object)
  {
    try {
      return field.get(object);
    } catch (Exception e) {
      throw new YAMLException("Unable to access field " + field.getName() + " on object " + object + " : " + e);
    }
  }
}
