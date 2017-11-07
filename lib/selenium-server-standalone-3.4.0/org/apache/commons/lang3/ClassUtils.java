package org.apache.commons.lang3;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.commons.lang3.mutable.MutableObject;






























public class ClassUtils
{
  public static final char PACKAGE_SEPARATOR_CHAR = '.';
  
  public static enum Interfaces
  {
    INCLUDE,  EXCLUDE;
    



    private Interfaces() {}
  }
  



  public static final String PACKAGE_SEPARATOR = String.valueOf('.');
  



  public static final char INNER_CLASS_SEPARATOR_CHAR = '$';
  



  public static final String INNER_CLASS_SEPARATOR = String.valueOf('$');
  



  private static final Map<String, Class<?>> namePrimitiveMap = new HashMap();
  
  static { namePrimitiveMap.put("boolean", Boolean.TYPE);
    namePrimitiveMap.put("byte", Byte.TYPE);
    namePrimitiveMap.put("char", Character.TYPE);
    namePrimitiveMap.put("short", Short.TYPE);
    namePrimitiveMap.put("int", Integer.TYPE);
    namePrimitiveMap.put("long", Long.TYPE);
    namePrimitiveMap.put("double", Double.TYPE);
    namePrimitiveMap.put("float", Float.TYPE);
    namePrimitiveMap.put("void", Void.TYPE);
    




    primitiveWrapperMap = new HashMap();
    
    primitiveWrapperMap.put(Boolean.TYPE, Boolean.class);
    primitiveWrapperMap.put(Byte.TYPE, Byte.class);
    primitiveWrapperMap.put(Character.TYPE, Character.class);
    primitiveWrapperMap.put(Short.TYPE, Short.class);
    primitiveWrapperMap.put(Integer.TYPE, Integer.class);
    primitiveWrapperMap.put(Long.TYPE, Long.class);
    primitiveWrapperMap.put(Double.TYPE, Double.class);
    primitiveWrapperMap.put(Float.TYPE, Float.class);
    primitiveWrapperMap.put(Void.TYPE, Void.TYPE);
    




    wrapperPrimitiveMap = new HashMap();
    
    for (Map.Entry<Class<?>, Class<?>> entry : primitiveWrapperMap.entrySet()) {
      primitiveClass = (Class)entry.getKey();
      Class<?> wrapperClass = (Class)entry.getValue();
      if (!primitiveClass.equals(wrapperClass)) {
        wrapperPrimitiveMap.put(wrapperClass, primitiveClass);
      }
    }
    







    Class<?> primitiveClass;
    






    Object m = new HashMap();
    ((Map)m).put("int", "I");
    ((Map)m).put("boolean", "Z");
    ((Map)m).put("float", "F");
    ((Map)m).put("long", "J");
    ((Map)m).put("short", "S");
    ((Map)m).put("byte", "B");
    ((Map)m).put("double", "D");
    ((Map)m).put("char", "C");
    Map<String, String> r = new HashMap();
    for (Map.Entry<String, String> e : ((Map)m).entrySet()) {
      r.put(e.getValue(), e.getKey());
    }
    abbreviationMap = Collections.unmodifiableMap((Map)m);
    reverseAbbreviationMap = Collections.unmodifiableMap(r);
  }
  










  private static final Map<Class<?>, Class<?>> primitiveWrapperMap;
  








  public static String getShortClassName(Object object, String valueIfNull)
  {
    if (object == null) {
      return valueIfNull;
    }
    return getShortClassName(object.getClass());
  }
  









  public static String getShortClassName(Class<?> cls)
  {
    if (cls == null) {
      return "";
    }
    return getShortClassName(cls.getName());
  }
  











  public static String getShortClassName(String className)
  {
    if (StringUtils.isEmpty(className)) {
      return "";
    }
    
    StringBuilder arrayPrefix = new StringBuilder();
    

    if (className.startsWith("[")) {
      while (className.charAt(0) == '[') {
        className = className.substring(1);
        arrayPrefix.append("[]");
      }
      
      if ((className.charAt(0) == 'L') && (className.charAt(className.length() - 1) == ';')) {
        className = className.substring(1, className.length() - 1);
      }
      
      if (reverseAbbreviationMap.containsKey(className)) {
        className = (String)reverseAbbreviationMap.get(className);
      }
    }
    
    int lastDotIdx = className.lastIndexOf('.');
    int innerIdx = className.indexOf('$', lastDotIdx == -1 ? 0 : lastDotIdx + 1);
    
    String out = className.substring(lastDotIdx + 1);
    if (innerIdx != -1) {
      out = out.replace('$', '.');
    }
    return out + arrayPrefix;
  }
  







  public static String getSimpleName(Class<?> cls)
  {
    if (cls == null) {
      return "";
    }
    return cls.getSimpleName();
  }
  








  public static String getSimpleName(Object object, String valueIfNull)
  {
    if (object == null) {
      return valueIfNull;
    }
    return getSimpleName(object.getClass());
  }
  








  public static String getPackageName(Object object, String valueIfNull)
  {
    if (object == null) {
      return valueIfNull;
    }
    return getPackageName(object.getClass());
  }
  





  public static String getPackageName(Class<?> cls)
  {
    if (cls == null) {
      return "";
    }
    return getPackageName(cls.getName());
  }
  








  public static String getPackageName(String className)
  {
    if (StringUtils.isEmpty(className)) {
      return "";
    }
    

    while (className.charAt(0) == '[') {
      className = className.substring(1);
    }
    
    if ((className.charAt(0) == 'L') && (className.charAt(className.length() - 1) == ';')) {
      className = className.substring(1);
    }
    
    int i = className.lastIndexOf('.');
    if (i == -1) {
      return "";
    }
    return className.substring(0, i);
  }
  











  public static String getAbbreviatedName(Class<?> cls, int len)
  {
    if (cls == null) {
      return "";
    }
    return getAbbreviatedName(cls.getName(), len);
  }
  












  private static final Map<Class<?>, Class<?>> wrapperPrimitiveMap;
  










  public static String getAbbreviatedName(String className, int len)
  {
    if (len <= 0) {
      throw new IllegalArgumentException("len must be > 0");
    }
    if (className == null) {
      return "";
    }
    
    int availableSpace = len;
    int packageLevels = StringUtils.countMatches(className, '.');
    String[] output = new String[packageLevels + 1];
    int endIndex = className.length() - 1;
    for (int level = packageLevels; level >= 0; level--) {
      int startIndex = className.lastIndexOf('.', endIndex);
      String part = className.substring(startIndex + 1, endIndex + 1);
      availableSpace -= part.length();
      if (level > 0)
      {
        availableSpace--;
      }
      if (level == packageLevels)
      {
        output[level] = part;
      }
      else if (availableSpace > 0) {
        output[level] = part;
      }
      else {
        output[level] = part.substring(0, 1);
      }
      
      endIndex = startIndex - 1;
    }
    
    return StringUtils.join(output, '.');
  }
  








  public static List<Class<?>> getAllSuperclasses(Class<?> cls)
  {
    if (cls == null) {
      return null;
    }
    List<Class<?>> classes = new ArrayList();
    Class<?> superclass = cls.getSuperclass();
    while (superclass != null) {
      classes.add(superclass);
      superclass = superclass.getSuperclass();
    }
    return classes;
  }
  












  public static List<Class<?>> getAllInterfaces(Class<?> cls)
  {
    if (cls == null) {
      return null;
    }
    
    LinkedHashSet<Class<?>> interfacesFound = new LinkedHashSet();
    getAllInterfaces(cls, interfacesFound);
    
    return new ArrayList(interfacesFound);
  }
  





  private static void getAllInterfaces(Class<?> cls, HashSet<Class<?>> interfacesFound)
  {
    while (cls != null) {
      Class<?>[] interfaces = cls.getInterfaces();
      
      for (Class<?> i : interfaces) {
        if (interfacesFound.add(i)) {
          getAllInterfaces(i, interfacesFound);
        }
      }
      
      cls = cls.getSuperclass();
    }
  }
  













  public static List<Class<?>> convertClassNamesToClasses(List<String> classNames)
  {
    if (classNames == null) {
      return null;
    }
    List<Class<?>> classes = new ArrayList(classNames.size());
    for (String className : classNames) {
      try {
        classes.add(Class.forName(className));
      } catch (Exception ex) {
        classes.add(null);
      }
    }
    return classes;
  }
  











  public static List<String> convertClassesToClassNames(List<Class<?>> classes)
  {
    if (classes == null) {
      return null;
    }
    List<String> classNames = new ArrayList(classes.size());
    for (Class<?> cls : classes) {
      if (cls == null) {
        classNames.add(null);
      } else {
        classNames.add(cls.getName());
      }
    }
    return classNames;
  }
  












  private static final Map<String, String> abbreviationMap;
  











  private static final Map<String, String> reverseAbbreviationMap;
  










  public static boolean isAssignable(Class<?>[] classArray, Class<?>... toClassArray)
  {
    return isAssignable(classArray, toClassArray, SystemUtils.isJavaVersionAtLeast(JavaVersion.JAVA_1_5));
  }
  































  public static boolean isAssignable(Class<?>[] classArray, Class<?>[] toClassArray, boolean autoboxing)
  {
    if (!ArrayUtils.isSameLength(classArray, toClassArray)) {
      return false;
    }
    if (classArray == null) {
      classArray = ArrayUtils.EMPTY_CLASS_ARRAY;
    }
    if (toClassArray == null) {
      toClassArray = ArrayUtils.EMPTY_CLASS_ARRAY;
    }
    for (int i = 0; i < classArray.length; i++) {
      if (!isAssignable(classArray[i], toClassArray[i], autoboxing)) {
        return false;
      }
    }
    return true;
  }
  









  public static boolean isPrimitiveOrWrapper(Class<?> type)
  {
    if (type == null) {
      return false;
    }
    return (type.isPrimitive()) || (isPrimitiveWrapper(type));
  }
  









  public static boolean isPrimitiveWrapper(Class<?> type)
  {
    return wrapperPrimitiveMap.containsKey(type);
  }
  






























  public static boolean isAssignable(Class<?> cls, Class<?> toClass)
  {
    return isAssignable(cls, toClass, SystemUtils.isJavaVersionAtLeast(JavaVersion.JAVA_1_5));
  }
  


























  public static boolean isAssignable(Class<?> cls, Class<?> toClass, boolean autoboxing)
  {
    if (toClass == null) {
      return false;
    }
    
    if (cls == null) {
      return !toClass.isPrimitive();
    }
    
    if (autoboxing) {
      if ((cls.isPrimitive()) && (!toClass.isPrimitive())) {
        cls = primitiveToWrapper(cls);
        if (cls == null) {
          return false;
        }
      }
      if ((toClass.isPrimitive()) && (!cls.isPrimitive())) {
        cls = wrapperToPrimitive(cls);
        if (cls == null) {
          return false;
        }
      }
    }
    if (cls.equals(toClass)) {
      return true;
    }
    if (cls.isPrimitive()) {
      if (!toClass.isPrimitive()) {
        return false;
      }
      if (Integer.TYPE.equals(cls)) {
        return (Long.TYPE.equals(toClass)) || 
          (Float.TYPE.equals(toClass)) || 
          (Double.TYPE.equals(toClass));
      }
      if (Long.TYPE.equals(cls)) {
        return (Float.TYPE.equals(toClass)) || 
          (Double.TYPE.equals(toClass));
      }
      if (Boolean.TYPE.equals(cls)) {
        return false;
      }
      if (Double.TYPE.equals(cls)) {
        return false;
      }
      if (Float.TYPE.equals(cls)) {
        return Double.TYPE.equals(toClass);
      }
      if (Character.TYPE.equals(cls)) {
        return (Integer.TYPE.equals(toClass)) || 
          (Long.TYPE.equals(toClass)) || 
          (Float.TYPE.equals(toClass)) || 
          (Double.TYPE.equals(toClass));
      }
      if (Short.TYPE.equals(cls)) {
        return (Integer.TYPE.equals(toClass)) || 
          (Long.TYPE.equals(toClass)) || 
          (Float.TYPE.equals(toClass)) || 
          (Double.TYPE.equals(toClass));
      }
      if (Byte.TYPE.equals(cls)) {
        return (Short.TYPE.equals(toClass)) || 
          (Integer.TYPE.equals(toClass)) || 
          (Long.TYPE.equals(toClass)) || 
          (Float.TYPE.equals(toClass)) || 
          (Double.TYPE.equals(toClass));
      }
      
      return false;
    }
    return toClass.isAssignableFrom(cls);
  }
  











  public static Class<?> primitiveToWrapper(Class<?> cls)
  {
    Class<?> convertedClass = cls;
    if ((cls != null) && (cls.isPrimitive())) {
      convertedClass = (Class)primitiveWrapperMap.get(cls);
    }
    return convertedClass;
  }
  









  public static Class<?>[] primitivesToWrappers(Class<?>... classes)
  {
    if (classes == null) {
      return null;
    }
    
    if (classes.length == 0) {
      return classes;
    }
    
    Class<?>[] convertedClasses = new Class[classes.length];
    for (int i = 0; i < classes.length; i++) {
      convertedClasses[i] = primitiveToWrapper(classes[i]);
    }
    return convertedClasses;
  }
  















  public static Class<?> wrapperToPrimitive(Class<?> cls)
  {
    return (Class)wrapperPrimitiveMap.get(cls);
  }
  













  public static Class<?>[] wrappersToPrimitives(Class<?>... classes)
  {
    if (classes == null) {
      return null;
    }
    
    if (classes.length == 0) {
      return classes;
    }
    
    Class<?>[] convertedClasses = new Class[classes.length];
    for (int i = 0; i < classes.length; i++) {
      convertedClasses[i] = wrapperToPrimitive(classes[i]);
    }
    return convertedClasses;
  }
  








  public static boolean isInnerClass(Class<?> cls)
  {
    return (cls != null) && (cls.getEnclosingClass() != null);
  }
  






  public static Class<?> getClass(ClassLoader classLoader, String className, boolean initialize)
    throws ClassNotFoundException
  {
    try
    {
      Class<?> clazz;
      





      if (namePrimitiveMap.containsKey(className)) {
        clazz = (Class)namePrimitiveMap.get(className);
      }
      return Class.forName(toCanonicalName(className), initialize, classLoader);

    }
    catch (ClassNotFoundException ex)
    {
      int lastDotIndex = className.lastIndexOf('.');
      
      if (lastDotIndex != -1) {
        try {
          return getClass(classLoader, className.substring(0, lastDotIndex) + '$' + className
            .substring(lastDotIndex + 1), initialize);
        }
        catch (ClassNotFoundException localClassNotFoundException1) {}
      }
      


      throw ex;
    }
  }
  










  public static Class<?> getClass(ClassLoader classLoader, String className)
    throws ClassNotFoundException
  {
    return getClass(classLoader, className, true);
  }
  









  public static Class<?> getClass(String className)
    throws ClassNotFoundException
  {
    return getClass(className, true);
  }
  









  public static Class<?> getClass(String className, boolean initialize)
    throws ClassNotFoundException
  {
    ClassLoader contextCL = Thread.currentThread().getContextClassLoader();
    ClassLoader loader = contextCL == null ? ClassUtils.class.getClassLoader() : contextCL;
    return getClass(loader, className, initialize);
  }
  
























  public static Method getPublicMethod(Class<?> cls, String methodName, Class<?>... parameterTypes)
    throws SecurityException, NoSuchMethodException
  {
    Method declaredMethod = cls.getMethod(methodName, parameterTypes);
    if (Modifier.isPublic(declaredMethod.getDeclaringClass().getModifiers())) {
      return declaredMethod;
    }
    
    List<Class<?>> candidateClasses = new ArrayList();
    candidateClasses.addAll(getAllInterfaces(cls));
    candidateClasses.addAll(getAllSuperclasses(cls));
    
    for (Class<?> candidateClass : candidateClasses) {
      if (Modifier.isPublic(candidateClass.getModifiers()))
      {
        Method candidateMethod;
        try
        {
          candidateMethod = candidateClass.getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException ex) {}
        continue;
        Method candidateMethod;
        if (Modifier.isPublic(candidateMethod.getDeclaringClass().getModifiers())) {
          return candidateMethod;
        }
      }
    }
    
    throw new NoSuchMethodException("Can't find a public method for " + methodName + " " + ArrayUtils.toString(parameterTypes));
  }
  






  private static String toCanonicalName(String className)
  {
    className = StringUtils.deleteWhitespace(className);
    if (className == null)
      throw new NullPointerException("className must not be null.");
    if (className.endsWith("[]")) {
      StringBuilder classNameBuffer = new StringBuilder();
      while (className.endsWith("[]")) {
        className = className.substring(0, className.length() - 2);
        classNameBuffer.append("[");
      }
      String abbreviation = (String)abbreviationMap.get(className);
      if (abbreviation != null) {
        classNameBuffer.append(abbreviation);
      } else {
        classNameBuffer.append("L").append(className).append(";");
      }
      className = classNameBuffer.toString();
    }
    return className;
  }
  









  public static Class<?>[] toClass(Object... array)
  {
    if (array == null)
      return null;
    if (array.length == 0) {
      return ArrayUtils.EMPTY_CLASS_ARRAY;
    }
    Class<?>[] classes = new Class[array.length];
    for (int i = 0; i < array.length; i++) {
      classes[i] = (array[i] == null ? null : array[i].getClass());
    }
    return classes;
  }
  









  public static String getShortCanonicalName(Object object, String valueIfNull)
  {
    if (object == null) {
      return valueIfNull;
    }
    return getShortCanonicalName(object.getClass().getName());
  }
  






  public static String getShortCanonicalName(Class<?> cls)
  {
    if (cls == null) {
      return "";
    }
    return getShortCanonicalName(cls.getName());
  }
  








  public static String getShortCanonicalName(String canonicalName)
  {
    return getShortClassName(getCanonicalName(canonicalName));
  }
  









  public static String getPackageCanonicalName(Object object, String valueIfNull)
  {
    if (object == null) {
      return valueIfNull;
    }
    return getPackageCanonicalName(object.getClass().getName());
  }
  






  public static String getPackageCanonicalName(Class<?> cls)
  {
    if (cls == null) {
      return "";
    }
    return getPackageCanonicalName(cls.getName());
  }
  









  public static String getPackageCanonicalName(String canonicalName)
  {
    return getPackageName(getCanonicalName(canonicalName));
  }
  















  private static String getCanonicalName(String className)
  {
    className = StringUtils.deleteWhitespace(className);
    if (className == null) {
      return null;
    }
    int dim = 0;
    while (className.startsWith("[")) {
      dim++;
      className = className.substring(1);
    }
    if (dim < 1) {
      return className;
    }
    if (className.startsWith("L")) {
      className = className.substring(1, className
      
        .endsWith(";") ? className
        .length() - 1 : className
        .length());
    }
    else if (className.length() > 0) {
      className = (String)reverseAbbreviationMap.get(className.substring(0, 1));
    }
    
    StringBuilder canonicalClassNameBuffer = new StringBuilder(className);
    for (int i = 0; i < dim; i++) {
      canonicalClassNameBuffer.append("[]");
    }
    return canonicalClassNameBuffer.toString();
  }
  







  public static Iterable<Class<?>> hierarchy(Class<?> type)
  {
    return hierarchy(type, Interfaces.EXCLUDE);
  }
  







  public static Iterable<Class<?>> hierarchy(Class<?> type, Interfaces interfacesBehavior)
  {
    Iterable<Class<?>> classes = new Iterable()
    {
      public Iterator<Class<?>> iterator()
      {
        final MutableObject<Class<?>> next = new MutableObject(val$type);
        new Iterator()
        {
          public boolean hasNext()
          {
            return next.getValue() != null;
          }
          
          public Class<?> next()
          {
            Class<?> result = (Class)next.getValue();
            next.setValue(result.getSuperclass());
            return result;
          }
          
          public void remove()
          {
            throw new UnsupportedOperationException();
          }
        };
      }
    };
    

    if (interfacesBehavior != Interfaces.INCLUDE) {
      return classes;
    }
    new Iterable()
    {
      public Iterator<Class<?>> iterator()
      {
        final Set<Class<?>> seenInterfaces = new HashSet();
        final Iterator<Class<?>> wrapped = val$classes.iterator();
        
        new Iterator() {
          Iterator<Class<?>> interfaces = Collections.emptySet().iterator();
          
          public boolean hasNext()
          {
            return (interfaces.hasNext()) || (wrapped.hasNext());
          }
          
          public Class<?> next()
          {
            if (interfaces.hasNext()) {
              Class<?> nextInterface = (Class)interfaces.next();
              seenInterfaces.add(nextInterface);
              return nextInterface;
            }
            Class<?> nextSuperclass = (Class)wrapped.next();
            Set<Class<?>> currentInterfaces = new LinkedHashSet();
            walkInterfaces(currentInterfaces, nextSuperclass);
            interfaces = currentInterfaces.iterator();
            return nextSuperclass;
          }
          
          private void walkInterfaces(Set<Class<?>> addTo, Class<?> c) {
            for (Class<?> iface : c.getInterfaces()) {
              if (!seenInterfaces.contains(iface)) {
                addTo.add(iface);
              }
              walkInterfaces(addTo, iface);
            }
          }
          
          public void remove()
          {
            throw new UnsupportedOperationException();
          }
        };
      }
    };
  }
  
  public ClassUtils() {}
}
