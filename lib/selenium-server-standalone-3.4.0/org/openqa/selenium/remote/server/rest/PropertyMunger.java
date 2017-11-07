package org.openqa.selenium.remote.server.rest;

import java.lang.reflect.Method;
import org.openqa.selenium.remote.SimplePropertyDescriptor;
















class PropertyMunger
{
  PropertyMunger() {}
  
  public static Object get(String name, Object on)
    throws Exception
  {
    SimplePropertyDescriptor[] properties = SimplePropertyDescriptor.getPropertyDescriptors(on.getClass());
    for (SimplePropertyDescriptor property : properties) {
      if (property.getName().equals(name)) {
        Object result = property.getReadMethod().invoke(on, new Object[0]);
        return String.valueOf(result);
      }
    }
    
    return null;
  }
  
  public static void set(String name, Object on, Object value) throws Exception
  {
    SimplePropertyDescriptor[] properties = SimplePropertyDescriptor.getPropertyDescriptors(on.getClass());
    for (SimplePropertyDescriptor property : properties) {
      if (property.getName().equals(name)) {
        Method writeMethod = property.getWriteMethod();
        if (writeMethod == null) {
          return;
        }
        
        Class<?>[] types = writeMethod.getParameterTypes();
        if (types.length != 1) {
          return;
        }
        
        if (String.class.equals(types[0])) {
          writeMethod.invoke(on, new Object[] { value });
        }
      }
    }
  }
}
