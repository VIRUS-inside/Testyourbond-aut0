package org.openqa.selenium.remote;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
















public class SimplePropertyDescriptor
{
  private String name;
  private Method readMethod;
  private Method writeMethod;
  
  public SimplePropertyDescriptor() {}
  
  public SimplePropertyDescriptor(String name, Method readMethod, Method writeMethod)
  {
    this.name = name;
    this.readMethod = readMethod;
    this.writeMethod = writeMethod;
  }
  
  public String getName() {
    return name;
  }
  
  public Method getReadMethod() {
    return readMethod;
  }
  
  public Method getWriteMethod() {
    return writeMethod;
  }
  
  public static SimplePropertyDescriptor[] getPropertyDescriptors(Class<? extends Object> clazz) {
    HashMap<String, SimplePropertyDescriptor> properties = new HashMap();
    for (Method m : clazz.getMethods()) {
      String methodName = m.getName();
      if ((methodName.length() > 2) && (methodName.startsWith("is"))) {
        String propertyName = uncapitalize(methodName.substring(2));
        if (properties.containsKey(propertyName)) {
          getreadMethod = m;
        } else
          properties.put(propertyName, new SimplePropertyDescriptor(propertyName, m, null));
      }
      if (methodName.length() > 3)
      {

        String propertyName = uncapitalize(methodName.substring(3));
        if ((methodName.startsWith("get")) || (methodName.startsWith("has"))) {
          if (properties.containsKey(propertyName)) {
            getreadMethod = m;
          } else
            properties.put(propertyName, new SimplePropertyDescriptor(propertyName, m, null));
        }
        if (methodName.startsWith("set"))
          if (properties.containsKey(propertyName)) {
            getwriteMethod = m;
          } else
            properties.put(propertyName, new SimplePropertyDescriptor(propertyName, null, m));
      }
    }
    SimplePropertyDescriptor[] pdsArray = new SimplePropertyDescriptor[properties.size()];
    return (SimplePropertyDescriptor[])properties.values().toArray(pdsArray);
  }
  
  private static String uncapitalize(String s) {
    return s.substring(0, 1).toLowerCase() + s.substring(1);
  }
}
