package org.yaml.snakeyaml.introspector;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;












public abstract class GenericProperty
  extends Property
{
  private Type genType;
  private boolean actualClassesChecked;
  private Class<?>[] actualClasses;
  
  public GenericProperty(String name, Class<?> aClass, Type aType)
  {
    super(name, aClass);
    genType = aType;
    actualClassesChecked = (aType == null);
  }
  


  public Class<?>[] getActualTypeArguments()
  {
    if (!actualClassesChecked) {
      if ((genType instanceof ParameterizedType)) {
        ParameterizedType parameterizedType = (ParameterizedType)genType;
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        if (actualTypeArguments.length > 0) {
          actualClasses = new Class[actualTypeArguments.length];
          for (int i = 0; i < actualTypeArguments.length; i++) {
            if ((actualTypeArguments[i] instanceof Class)) {
              actualClasses[i] = ((Class)actualTypeArguments[i]);
            } else if ((actualTypeArguments[i] instanceof ParameterizedType)) {
              actualClasses[i] = ((Class)((ParameterizedType)actualTypeArguments[i]).getRawType());
            }
            else if ((actualTypeArguments[i] instanceof GenericArrayType)) {
              Type componentType = ((GenericArrayType)actualTypeArguments[i]).getGenericComponentType();
              
              if ((componentType instanceof Class)) {
                actualClasses[i] = Array.newInstance((Class)componentType, 0).getClass();
              }
              else {
                actualClasses = null;
                break;
              }
            } else {
              actualClasses = null;
              break;
            }
          }
        }
      } else if ((genType instanceof GenericArrayType)) {
        Type componentType = ((GenericArrayType)genType).getGenericComponentType();
        if ((componentType instanceof Class)) {
          actualClasses = new Class[] { (Class)componentType };
        }
      } else if ((genType instanceof Class))
      {
        Class<?> classType = (Class)genType;
        if (classType.isArray()) {
          actualClasses = new Class[1];
          actualClasses[0] = getType().getComponentType();
        }
      }
      actualClassesChecked = true;
    }
    return actualClasses;
  }
}
