package org.apache.commons.lang3.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.ClassUtils.Interfaces;
import org.apache.commons.lang3.Validate;


































































public class MethodUtils
{
  public MethodUtils() {}
  
  public static Object invokeMethod(Object object, String methodName)
    throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
  {
    return invokeMethod(object, methodName, ArrayUtils.EMPTY_OBJECT_ARRAY, null);
  }
  

















  public static Object invokeMethod(Object object, boolean forceAccess, String methodName)
    throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
  {
    return invokeMethod(object, forceAccess, methodName, ArrayUtils.EMPTY_OBJECT_ARRAY, null);
  }
  






















  public static Object invokeMethod(Object object, String methodName, Object... args)
    throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
  {
    args = ArrayUtils.nullToEmpty(args);
    Class<?>[] parameterTypes = ClassUtils.toClass(args);
    return invokeMethod(object, methodName, args, parameterTypes);
  }
  























  public static Object invokeMethod(Object object, boolean forceAccess, String methodName, Object... args)
    throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
  {
    args = ArrayUtils.nullToEmpty(args);
    Class<?>[] parameterTypes = ClassUtils.toClass(args);
    return invokeMethod(object, forceAccess, methodName, args, parameterTypes);
  }
  




















  public static Object invokeMethod(Object object, boolean forceAccess, String methodName, Object[] args, Class<?>[] parameterTypes)
    throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
  {
    parameterTypes = ArrayUtils.nullToEmpty(parameterTypes);
    args = ArrayUtils.nullToEmpty(args);
    

    Method method = null;
    boolean isOriginallyAccessible = false;
    Object result = null;
    try {
      String messagePrefix;
      if (forceAccess) {
        String messagePrefix = "No such method: ";
        method = getMatchingMethod(object.getClass(), methodName, parameterTypes);
        
        if (method != null) {
          isOriginallyAccessible = method.isAccessible();
          if (!isOriginallyAccessible) {
            method.setAccessible(true);
          }
        }
      } else {
        messagePrefix = "No such accessible method: ";
        method = getMatchingAccessibleMethod(object.getClass(), methodName, parameterTypes);
      }
      

      if (method == null)
      {

        throw new NoSuchMethodException(messagePrefix + methodName + "() on object: " + object.getClass().getName());
      }
      args = toVarArgs(method, args);
      
      result = method.invoke(object, args);
    }
    finally {
      if ((method != null) && (forceAccess) && (method.isAccessible() != isOriginallyAccessible)) {
        method.setAccessible(isOriginallyAccessible);
      }
    }
    String messagePrefix;
    return result;
  }
  




















  public static Object invokeMethod(Object object, String methodName, Object[] args, Class<?>[] parameterTypes)
    throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
  {
    return invokeMethod(object, false, methodName, args, parameterTypes);
  }
  


















  public static Object invokeExactMethod(Object object, String methodName)
    throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
  {
    return invokeExactMethod(object, methodName, ArrayUtils.EMPTY_OBJECT_ARRAY, null);
  }
  

















  public static Object invokeExactMethod(Object object, String methodName, Object... args)
    throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
  {
    args = ArrayUtils.nullToEmpty(args);
    Class<?>[] parameterTypes = ClassUtils.toClass(args);
    return invokeExactMethod(object, methodName, args, parameterTypes);
  }
  




















  public static Object invokeExactMethod(Object object, String methodName, Object[] args, Class<?>[] parameterTypes)
    throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
  {
    args = ArrayUtils.nullToEmpty(args);
    parameterTypes = ArrayUtils.nullToEmpty(parameterTypes);
    Method method = getAccessibleMethod(object.getClass(), methodName, parameterTypes);
    
    if (method == null)
    {

      throw new NoSuchMethodException("No such accessible method: " + methodName + "() on object: " + object.getClass().getName());
    }
    return method.invoke(object, args);
  }
  




















  public static Object invokeExactStaticMethod(Class<?> cls, String methodName, Object[] args, Class<?>[] parameterTypes)
    throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
  {
    args = ArrayUtils.nullToEmpty(args);
    parameterTypes = ArrayUtils.nullToEmpty(parameterTypes);
    Method method = getAccessibleMethod(cls, methodName, parameterTypes);
    if (method == null)
    {
      throw new NoSuchMethodException("No such accessible method: " + methodName + "() on class: " + cls.getName());
    }
    return method.invoke(null, args);
  }
  
























  public static Object invokeStaticMethod(Class<?> cls, String methodName, Object... args)
    throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
  {
    args = ArrayUtils.nullToEmpty(args);
    Class<?>[] parameterTypes = ClassUtils.toClass(args);
    return invokeStaticMethod(cls, methodName, args, parameterTypes);
  }
  























  public static Object invokeStaticMethod(Class<?> cls, String methodName, Object[] args, Class<?>[] parameterTypes)
    throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
  {
    args = ArrayUtils.nullToEmpty(args);
    parameterTypes = ArrayUtils.nullToEmpty(parameterTypes);
    Method method = getMatchingAccessibleMethod(cls, methodName, parameterTypes);
    
    if (method == null)
    {
      throw new NoSuchMethodException("No such accessible method: " + methodName + "() on class: " + cls.getName());
    }
    args = toVarArgs(method, args);
    return method.invoke(null, args);
  }
  
  private static Object[] toVarArgs(Method method, Object[] args) {
    if (method.isVarArgs()) {
      Class<?>[] methodParameterTypes = method.getParameterTypes();
      args = getVarArgs(args, methodParameterTypes);
    }
    return args;
  }
  









  static Object[] getVarArgs(Object[] args, Class<?>[] methodParameterTypes)
  {
    if ((args.length == methodParameterTypes.length) && 
      (args[(args.length - 1)].getClass().equals(methodParameterTypes[(methodParameterTypes.length - 1)])))
    {
      return args;
    }
    

    Object[] newArgs = new Object[methodParameterTypes.length];
    

    System.arraycopy(args, 0, newArgs, 0, methodParameterTypes.length - 1);
    

    Class<?> varArgComponentType = methodParameterTypes[(methodParameterTypes.length - 1)].getComponentType();
    int varArgLength = args.length - methodParameterTypes.length + 1;
    
    Object varArgsArray = Array.newInstance(ClassUtils.primitiveToWrapper(varArgComponentType), varArgLength);
    
    System.arraycopy(args, methodParameterTypes.length - 1, varArgsArray, 0, varArgLength);
    
    if (varArgComponentType.isPrimitive())
    {
      varArgsArray = ArrayUtils.toPrimitive(varArgsArray);
    }
    

    newArgs[(methodParameterTypes.length - 1)] = varArgsArray;
    

    return newArgs;
  }
  


















  public static Object invokeExactStaticMethod(Class<?> cls, String methodName, Object... args)
    throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
  {
    args = ArrayUtils.nullToEmpty(args);
    Class<?>[] parameterTypes = ClassUtils.toClass(args);
    return invokeExactStaticMethod(cls, methodName, args, parameterTypes);
  }
  











  public static Method getAccessibleMethod(Class<?> cls, String methodName, Class<?>... parameterTypes)
  {
    try
    {
      return getAccessibleMethod(cls.getMethod(methodName, parameterTypes));
    }
    catch (NoSuchMethodException e) {}
    return null;
  }
  








  public static Method getAccessibleMethod(Method method)
  {
    if (!MemberUtils.isAccessible(method)) {
      return null;
    }
    
    Class<?> cls = method.getDeclaringClass();
    if (Modifier.isPublic(cls.getModifiers())) {
      return method;
    }
    String methodName = method.getName();
    Class<?>[] parameterTypes = method.getParameterTypes();
    

    method = getAccessibleMethodFromInterfaceNest(cls, methodName, parameterTypes);
    


    if (method == null) {
      method = getAccessibleMethodFromSuperclass(cls, methodName, parameterTypes);
    }
    
    return method;
  }
  










  private static Method getAccessibleMethodFromSuperclass(Class<?> cls, String methodName, Class<?>... parameterTypes)
  {
    Class<?> parentClass = cls.getSuperclass();
    while (parentClass != null) {
      if (Modifier.isPublic(parentClass.getModifiers())) {
        try {
          return parentClass.getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
          return null;
        }
      }
      parentClass = parentClass.getSuperclass();
    }
    return null;
  }
  
  private static Method getAccessibleMethodFromInterfaceNest(Class<?> cls, String methodName, Class<?>... parameterTypes)
  {
    for (; 
        














        cls != null; cls = cls.getSuperclass())
    {

      Class<?>[] interfaces = cls.getInterfaces();
      for (int i = 0; i < interfaces.length; i++)
      {
        if (Modifier.isPublic(interfaces[i].getModifiers()))
        {
          try
          {

            return interfaces[i].getDeclaredMethod(methodName, parameterTypes);



          }
          catch (NoSuchMethodException localNoSuchMethodException)
          {


            Method method = getAccessibleMethodFromInterfaceNest(interfaces[i], methodName, parameterTypes);
            
            if (method != null)
              return method;
          } }
      }
    }
    return null;
  }
  





















  public static Method getMatchingAccessibleMethod(Class<?> cls, String methodName, Class<?>... parameterTypes)
  {
    try
    {
      Method method = cls.getMethod(methodName, parameterTypes);
      MemberUtils.setAccessibleWorkaround(method);
      return method;
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      Method bestMatch = null;
      Method[] methods = cls.getMethods();
      for (Method method : methods)
      {
        if ((method.getName().equals(methodName)) && 
          (MemberUtils.isMatchingMethod(method, parameterTypes)))
        {
          Method accessibleMethod = getAccessibleMethod(method);
          if ((accessibleMethod != null) && ((bestMatch == null) || (MemberUtils.compareMethodFit(accessibleMethod, bestMatch, parameterTypes) < 0)))
          {


            bestMatch = accessibleMethod;
          }
        }
      }
      if (bestMatch != null) {
        MemberUtils.setAccessibleWorkaround(bestMatch);
      }
      return bestMatch;
    }
  }
  









  public static Method getMatchingMethod(Class<?> cls, String methodName, Class<?>... parameterTypes)
  {
    Validate.notNull(cls, "Null class not allowed.", new Object[0]);
    Validate.notEmpty(methodName, "Null or blank methodName not allowed.", new Object[0]);
    

    Method[] methodArray = cls.getDeclaredMethods();
    List<Class<?>> superclassList = ClassUtils.getAllSuperclasses(cls);
    for (Iterator localIterator = superclassList.iterator(); localIterator.hasNext();) { klass = (Class)localIterator.next();
      methodArray = (Method[])ArrayUtils.addAll(methodArray, klass.getDeclaredMethods());
    }
    Class<?> klass;
    Method inexactMatch = null;
    for (Method method : methodArray) {
      if ((methodName.equals(method.getName())) && 
        (ArrayUtils.isEquals(parameterTypes, method.getParameterTypes())))
        return method;
      if ((methodName.equals(method.getName())) && 
        (ClassUtils.isAssignable(parameterTypes, method.getParameterTypes(), true))) {
        if (inexactMatch == null) {
          inexactMatch = method;
        }
        else if (distance(parameterTypes, method.getParameterTypes()) < distance(parameterTypes, inexactMatch.getParameterTypes())) {
          inexactMatch = method;
        }
      }
    }
    
    return inexactMatch;
  }
  






  private static int distance(Class<?>[] classArray, Class<?>[] toClassArray)
  {
    int answer = 0;
    
    if (!ClassUtils.isAssignable(classArray, toClassArray, true)) {
      return -1;
    }
    for (int offset = 0; offset < classArray.length; offset++)
    {
      if (!classArray[offset].equals(toClassArray[offset]))
      {
        if ((ClassUtils.isAssignable(classArray[offset], toClassArray[offset], true)) && 
          (!ClassUtils.isAssignable(classArray[offset], toClassArray[offset], false))) {
          answer++;
        } else {
          answer += 2;
        }
      }
    }
    return answer;
  }
  







  public static Set<Method> getOverrideHierarchy(Method method, ClassUtils.Interfaces interfacesBehavior)
  {
    Validate.notNull(method);
    Set<Method> result = new LinkedHashSet();
    result.add(method);
    
    Class<?>[] parameterTypes = method.getParameterTypes();
    
    Class<?> declaringClass = method.getDeclaringClass();
    
    Iterator<Class<?>> hierarchy = ClassUtils.hierarchy(declaringClass, interfacesBehavior).iterator();
    
    hierarchy.next();
    while (hierarchy.hasNext()) {
      Class<?> c = (Class)hierarchy.next();
      Method m = getMatchingAccessibleMethod(c, method.getName(), parameterTypes);
      if (m != null)
      {

        if (Arrays.equals(m.getParameterTypes(), parameterTypes))
        {
          result.add(m);
        }
        else
        {
          Map<TypeVariable<?>, Type> typeArguments = TypeUtils.getTypeArguments(declaringClass, m.getDeclaringClass());
          for (int i = 0;; i++) { if (i >= parameterTypes.length) break label189;
            Type childType = TypeUtils.unrollVariables(typeArguments, method.getGenericParameterTypes()[i]);
            Type parentType = TypeUtils.unrollVariables(typeArguments, m.getGenericParameterTypes()[i]);
            if (!TypeUtils.equals(childType, parentType))
              break;
          }
          label189:
          result.add(m);
        } } }
    return result;
  }
  










  public static Method[] getMethodsWithAnnotation(Class<?> cls, Class<? extends Annotation> annotationCls)
  {
    List<Method> annotatedMethodsList = getMethodsListWithAnnotation(cls, annotationCls);
    return (Method[])annotatedMethodsList.toArray(new Method[annotatedMethodsList.size()]);
  }
  










  public static List<Method> getMethodsListWithAnnotation(Class<?> cls, Class<? extends Annotation> annotationCls)
  {
    Validate.isTrue(cls != null, "The class must not be null", new Object[0]);
    Validate.isTrue(annotationCls != null, "The annotation class must not be null", new Object[0]);
    Method[] allMethods = cls.getMethods();
    List<Method> annotatedMethods = new ArrayList();
    for (Method method : allMethods) {
      if (method.getAnnotation(annotationCls) != null) {
        annotatedMethods.add(method);
      }
    }
    return annotatedMethods;
  }
}
