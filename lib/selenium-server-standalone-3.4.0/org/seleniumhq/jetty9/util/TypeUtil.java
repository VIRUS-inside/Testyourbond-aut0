package org.seleniumhq.jetty9.util;

import java.io.IOException;
import java.io.PrintStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.seleniumhq.jetty9.util.annotation.Name;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;
import org.seleniumhq.jetty9.util.resource.Resource;






























public class TypeUtil
{
  private static final Logger LOG = Log.getLogger(TypeUtil.class);
  public static final Class<?>[] NO_ARGS = new Class[0];
  
  public static final int CR = 13;
  
  public static final int LF = 10;
  private static final HashMap<String, Class<?>> name2Class = new HashMap();
  private static final HashMap<Class<?>, String> class2Name;
  
  static { name2Class.put("boolean", Boolean.TYPE);
    name2Class.put("byte", Byte.TYPE);
    name2Class.put("char", Character.TYPE);
    name2Class.put("double", Double.TYPE);
    name2Class.put("float", Float.TYPE);
    name2Class.put("int", Integer.TYPE);
    name2Class.put("long", Long.TYPE);
    name2Class.put("short", Short.TYPE);
    name2Class.put("void", Void.TYPE);
    
    name2Class.put("java.lang.Boolean.TYPE", Boolean.TYPE);
    name2Class.put("java.lang.Byte.TYPE", Byte.TYPE);
    name2Class.put("java.lang.Character.TYPE", Character.TYPE);
    name2Class.put("java.lang.Double.TYPE", Double.TYPE);
    name2Class.put("java.lang.Float.TYPE", Float.TYPE);
    name2Class.put("java.lang.Integer.TYPE", Integer.TYPE);
    name2Class.put("java.lang.Long.TYPE", Long.TYPE);
    name2Class.put("java.lang.Short.TYPE", Short.TYPE);
    name2Class.put("java.lang.Void.TYPE", Void.TYPE);
    
    name2Class.put("java.lang.Boolean", Boolean.class);
    name2Class.put("java.lang.Byte", Byte.class);
    name2Class.put("java.lang.Character", Character.class);
    name2Class.put("java.lang.Double", Double.class);
    name2Class.put("java.lang.Float", Float.class);
    name2Class.put("java.lang.Integer", Integer.class);
    name2Class.put("java.lang.Long", Long.class);
    name2Class.put("java.lang.Short", Short.class);
    
    name2Class.put("Boolean", Boolean.class);
    name2Class.put("Byte", Byte.class);
    name2Class.put("Character", Character.class);
    name2Class.put("Double", Double.class);
    name2Class.put("Float", Float.class);
    name2Class.put("Integer", Integer.class);
    name2Class.put("Long", Long.class);
    name2Class.put("Short", Short.class);
    
    name2Class.put(null, Void.TYPE);
    name2Class.put("string", String.class);
    name2Class.put("String", String.class);
    name2Class.put("java.lang.String", String.class);
    


    class2Name = new HashMap();
    

    class2Name.put(Boolean.TYPE, "boolean");
    class2Name.put(Byte.TYPE, "byte");
    class2Name.put(Character.TYPE, "char");
    class2Name.put(Double.TYPE, "double");
    class2Name.put(Float.TYPE, "float");
    class2Name.put(Integer.TYPE, "int");
    class2Name.put(Long.TYPE, "long");
    class2Name.put(Short.TYPE, "short");
    class2Name.put(Void.TYPE, "void");
    
    class2Name.put(Boolean.class, "java.lang.Boolean");
    class2Name.put(Byte.class, "java.lang.Byte");
    class2Name.put(Character.class, "java.lang.Character");
    class2Name.put(Double.class, "java.lang.Double");
    class2Name.put(Float.class, "java.lang.Float");
    class2Name.put(Integer.class, "java.lang.Integer");
    class2Name.put(Long.class, "java.lang.Long");
    class2Name.put(Short.class, "java.lang.Short");
    
    class2Name.put(null, "void");
    class2Name.put(String.class, "java.lang.String");
    


    class2Value = new HashMap();
    

    try
    {
      Class<?>[] s = { String.class };
      
      class2Value.put(Boolean.TYPE, Boolean.class
        .getMethod("valueOf", s));
      class2Value.put(Byte.TYPE, Byte.class
        .getMethod("valueOf", s));
      class2Value.put(Double.TYPE, Double.class
        .getMethod("valueOf", s));
      class2Value.put(Float.TYPE, Float.class
        .getMethod("valueOf", s));
      class2Value.put(Integer.TYPE, Integer.class
        .getMethod("valueOf", s));
      class2Value.put(Long.TYPE, Long.class
        .getMethod("valueOf", s));
      class2Value.put(Short.TYPE, Short.class
        .getMethod("valueOf", s));
      
      class2Value.put(Boolean.class, Boolean.class
        .getMethod("valueOf", s));
      class2Value.put(Byte.class, Byte.class
        .getMethod("valueOf", s));
      class2Value.put(Double.class, Double.class
        .getMethod("valueOf", s));
      class2Value.put(Float.class, Float.class
        .getMethod("valueOf", s));
      class2Value.put(Integer.class, Integer.class
        .getMethod("valueOf", s));
      class2Value.put(Long.class, Long.class
        .getMethod("valueOf", s));
      class2Value.put(Short.class, Short.class
        .getMethod("valueOf", s));
    }
    catch (Exception e)
    {
      throw new Error(e);
    }
  }
  








  public static <T> List<T> asList(T[] a)
  {
    if (a == null)
      return Collections.emptyList();
    return Arrays.asList(a);
  }
  





  public static Class<?> fromName(String name)
  {
    return (Class)name2Class.get(name);
  }
  





  public static String toName(Class<?> type)
  {
    return (String)class2Name.get(type);
  }
  






  public static Object valueOf(Class<?> type, String value)
  {
    try
    {
      if (type.equals(String.class)) {
        return value;
      }
      Method m = (Method)class2Value.get(type);
      if (m != null) {
        return m.invoke(null, new Object[] { value });
      }
      if ((type.equals(Character.TYPE)) || 
        (type.equals(Character.class))) {
        return Character.valueOf(value.charAt(0));
      }
      Constructor<?> c = type.getConstructor(new Class[] { String.class });
      return c.newInstance(new Object[] { value });
    }
    catch (NoSuchMethodException|IllegalAccessException|InstantiationException x)
    {
      LOG.ignore(x);
    }
    catch (InvocationTargetException x)
    {
      if ((x.getTargetException() instanceof Error))
        throw ((Error)x.getTargetException());
      LOG.ignore(x);
    }
    return null;
  }
  






  public static Object valueOf(String type, String value)
  {
    return valueOf(fromName(type), value);
  }
  





  private static final HashMap<Class<?>, Method> class2Value;
  



  public static int parseInt(String s, int offset, int length, int base)
    throws NumberFormatException
  {
    int value = 0;
    
    if (length < 0) {
      length = s.length() - offset;
    }
    for (int i = 0; i < length; i++)
    {
      char c = s.charAt(offset + i);
      
      int digit = convertHexDigit(c);
      if ((digit < 0) || (digit >= base))
        throw new NumberFormatException(s.substring(offset, offset + length));
      value = value * base + digit;
    }
    return value;
  }
  










  public static int parseInt(byte[] b, int offset, int length, int base)
    throws NumberFormatException
  {
    int value = 0;
    
    if (length < 0) {
      length = b.length - offset;
    }
    for (int i = 0; i < length; i++)
    {
      char c = (char)(0xFF & b[(offset + i)]);
      
      int digit = c - '0';
      if ((digit < 0) || (digit >= base) || (digit >= 10))
      {
        digit = '\n' + c - 65;
        if ((digit < 10) || (digit >= base))
          digit = '\n' + c - 97;
      }
      if ((digit < 0) || (digit >= base))
        throw new NumberFormatException(new String(b, offset, length));
      value = value * base + digit;
    }
    return value;
  }
  

  public static byte[] parseBytes(String s, int base)
  {
    byte[] bytes = new byte[s.length() / 2];
    for (int i = 0; i < s.length(); i += 2)
      bytes[(i / 2)] = ((byte)parseInt(s, i, 2, base));
    return bytes;
  }
  

  public static String toString(byte[] bytes, int base)
  {
    StringBuilder buf = new StringBuilder();
    for (byte b : bytes)
    {
      int bi = 0xFF & b;
      int c = 48 + bi / base % base;
      if (c > 57)
        c = 97 + (c - 48 - 10);
      buf.append((char)c);
      c = 48 + bi % base;
      if (c > 57)
        c = 97 + (c - 48 - 10);
      buf.append((char)c);
    }
    return buf.toString();
  }
  





  public static byte convertHexDigit(byte c)
  {
    byte b = (byte)((c & 0x1F) + (c >> 6) * 25 - 16);
    if ((b < 0) || (b > 15))
      throw new NumberFormatException("!hex " + c);
    return b;
  }
  





  public static int convertHexDigit(char c)
  {
    int d = (c & 0x1F) + (c >> '\006') * 25 - 16;
    if ((d < 0) || (d > 15))
      throw new NumberFormatException("!hex " + c);
    return d;
  }
  





  public static int convertHexDigit(int c)
  {
    int d = (c & 0x1F) + (c >> 6) * 25 - 16;
    if ((d < 0) || (d > 15))
      throw new NumberFormatException("!hex " + c);
    return d;
  }
  

  public static void toHex(byte b, Appendable buf)
  {
    try
    {
      int d = 0xF & (0xF0 & b) >> 4;
      buf.append((char)((d > 9 ? 55 : 48) + d));
      d = 0xF & b;
      buf.append((char)((d > 9 ? 55 : 48) + d));
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
  }
  
  public static void toHex(int value, Appendable buf)
    throws IOException
  {
    int d = 0xF & (0xF0000000 & value) >> 28;
    buf.append((char)((d > 9 ? 55 : 48) + d));
    d = 0xF & (0xF000000 & value) >> 24;
    buf.append((char)((d > 9 ? 55 : 48) + d));
    d = 0xF & (0xF00000 & value) >> 20;
    buf.append((char)((d > 9 ? 55 : 48) + d));
    d = 0xF & (0xF0000 & value) >> 16;
    buf.append((char)((d > 9 ? 55 : 48) + d));
    d = 0xF & (0xF000 & value) >> 12;
    buf.append((char)((d > 9 ? 55 : 48) + d));
    d = 0xF & (0xF00 & value) >> 8;
    buf.append((char)((d > 9 ? 55 : 48) + d));
    d = 0xF & (0xF0 & value) >> 4;
    buf.append((char)((d > 9 ? 55 : 48) + d));
    d = 0xF & value;
    buf.append((char)((d > 9 ? 55 : 48) + d));
    
    Integer.toString(0, 36);
  }
  

  public static void toHex(long value, Appendable buf)
    throws IOException
  {
    toHex((int)(value >> 32), buf);
    toHex((int)value, buf);
  }
  

  public static String toHexString(byte b)
  {
    return toHexString(new byte[] { b }, 0, 1);
  }
  

  public static String toHexString(byte[] b)
  {
    return toHexString(b, 0, b.length);
  }
  

  public static String toHexString(byte[] b, int offset, int length)
  {
    StringBuilder buf = new StringBuilder();
    for (int i = offset; i < offset + length; i++)
    {
      int bi = 0xFF & b[i];
      int c = 48 + bi / 16 % 16;
      if (c > 57)
        c = 65 + (c - 48 - 10);
      buf.append((char)c);
      c = 48 + bi % 16;
      if (c > 57)
        c = 97 + (c - 48 - 10);
      buf.append((char)c);
    }
    return buf.toString();
  }
  

  public static byte[] fromHexString(String s)
  {
    if (s.length() % 2 != 0)
      throw new IllegalArgumentException(s);
    byte[] array = new byte[s.length() / 2];
    for (int i = 0; i < array.length; i++)
    {
      int b = Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16);
      array[i] = ((byte)(0xFF & b));
    }
    return array;
  }
  

  public static void dump(Class<?> c)
  {
    System.err.println("Dump: " + c);
    dump(c.getClassLoader());
  }
  
  public static void dump(ClassLoader cl)
  {
    System.err.println("Dump Loaders:");
    while (cl != null)
    {
      System.err.println("  loader " + cl);
      cl = cl.getParent();
    }
  }
  

  public static Object call(Class<?> oClass, String methodName, Object obj, Object[] arg)
    throws InvocationTargetException, NoSuchMethodException
  {
    Objects.requireNonNull(oClass, "Class cannot be null");
    Objects.requireNonNull(methodName, "Method name cannot be null");
    if (StringUtil.isBlank(methodName))
    {
      throw new IllegalArgumentException("Method name cannot be blank");
    }
    

    Method[] arrayOfMethod1 = oClass.getMethods();int i = arrayOfMethod1.length; for (Method localMethod1 = 0; localMethod1 < i; localMethod1++) { method = arrayOfMethod1[localMethod1];
      
      if (method.getName().equals(methodName))
      {
        if (method.getParameterCount() == arg.length)
        {
          if (Modifier.isStatic(method.getModifiers()) == (obj == null))
          {
            if ((obj != null) || (method.getDeclaringClass() == oClass))
            {
              try
              {

                return method.invoke(obj, arg);
              }
              catch (IllegalAccessException|IllegalArgumentException e)
              {
                LOG.ignore(e);
              } } }
        }
      }
    }
    Object[] args_with_opts = null;
    
    Method[] arrayOfMethod2 = oClass.getMethods();localMethod1 = arrayOfMethod2.length; for (Method method = 0; method < localMethod1; method++) { Method method = arrayOfMethod2[method];
      
      if (method.getName().equals(methodName))
      {
        if (method.getParameterCount() == arg.length + 1)
        {
          if (method.getParameterTypes()[arg.length].isArray())
          {
            if (Modifier.isStatic(method.getModifiers()) == (obj == null))
            {
              if ((obj != null) || (method.getDeclaringClass() == oClass))
              {

                if (args_with_opts == null) {
                  args_with_opts = ArrayUtil.addToArray(arg, new Object[0], Object.class);
                }
                try {
                  return method.invoke(obj, args_with_opts);
                }
                catch (IllegalAccessException|IllegalArgumentException e)
                {
                  LOG.ignore(e);
                }
              } } } }
      }
    }
    throw new NoSuchMethodException(methodName);
  }
  
  public static Object construct(Class<?> klass, Object[] arguments) throws InvocationTargetException, NoSuchMethodException
  {
    Objects.requireNonNull(klass, "Class cannot be null");
    
    for (Constructor<?> constructor : klass.getConstructors())
    {
      if (arguments == null ? 
      

        constructor.getParameterCount() == 0 : 
        

        constructor.getParameterCount() == arguments.length)
      {
        try
        {

          return constructor.newInstance(arguments);
        }
        catch (InstantiationException|IllegalAccessException|IllegalArgumentException e)
        {
          LOG.ignore(e);
        } }
    }
    throw new NoSuchMethodException("<init>");
  }
  
  public static Object construct(Class<?> klass, Object[] arguments, Map<String, Object> namedArgMap) throws InvocationTargetException, NoSuchMethodException
  {
    Objects.requireNonNull(klass, "Class cannot be null");
    Objects.requireNonNull(namedArgMap, "Named Argument Map cannot be null");
    
    for (Constructor<?> constructor : klass.getConstructors())
    {
      if (arguments == null ? 
      

        constructor.getParameterCount() == 0 : 
        

        constructor.getParameterCount() == arguments.length)
      {
        try
        {

          Annotation[][] parameterAnnotations = constructor.getParameterAnnotations();
          
          if ((arguments == null) || (arguments.length == 0))
          {
            if (LOG.isDebugEnabled())
              LOG.debug("Constructor has no arguments", new Object[0]);
            return constructor.newInstance(arguments);
          }
          if ((parameterAnnotations == null) || (parameterAnnotations.length == 0))
          {
            if (LOG.isDebugEnabled())
              LOG.debug("Constructor has no parameter annotations", new Object[0]);
            return constructor.newInstance(arguments);
          }
          

          Object[] swizzled = new Object[arguments.length];
          
          int count = 0;
          for (Annotation[] annotations : parameterAnnotations)
          {
            for (Annotation annotation : annotations)
            {
              if ((annotation instanceof Name))
              {
                Name param = (Name)annotation;
                
                if (namedArgMap.containsKey(param.value()))
                {
                  if (LOG.isDebugEnabled())
                    LOG.debug("placing named {} in position {}", new Object[] { param.value(), Integer.valueOf(count) });
                  swizzled[count] = namedArgMap.get(param.value());
                }
                else
                {
                  if (LOG.isDebugEnabled())
                    LOG.debug("placing {} in position {}", new Object[] { arguments[count], Integer.valueOf(count) });
                  swizzled[count] = arguments[count];
                }
                count++;


              }
              else if (LOG.isDebugEnabled()) {
                LOG.debug("passing on annotation {}", new Object[] { annotation });
              }
            }
          }
          
          return constructor.newInstance(swizzled);

        }
        catch (InstantiationException|IllegalAccessException|IllegalArgumentException e)
        {

          LOG.ignore(e);
        } }
    }
    throw new NoSuchMethodException("<init>");
  }
  





  public static boolean isTrue(Object o)
  {
    if (o == null)
      return false;
    if ((o instanceof Boolean))
      return ((Boolean)o).booleanValue();
    return Boolean.parseBoolean(o.toString());
  }
  





  public static boolean isFalse(Object o)
  {
    if (o == null)
      return false;
    if ((o instanceof Boolean))
      return !((Boolean)o).booleanValue();
    return "false".equalsIgnoreCase(o.toString());
  }
  

  public static Resource getLoadedFrom(Class<?> clazz)
  {
    ProtectionDomain domain = clazz.getProtectionDomain();
    if (domain != null)
    {
      CodeSource source = domain.getCodeSource();
      if (source != null)
      {
        URL location = source.getLocation();
        
        if (location != null) {
          return Resource.newResource(location);
        }
      }
    }
    String rname = clazz.getName().replace('.', '/') + ".class";
    ClassLoader loader = clazz.getClassLoader();
    URL url = (loader == null ? ClassLoader.getSystemClassLoader() : loader).getResource(rname);
    if (url != null)
    {
      try
      {
        return Resource.newResource(URIUtil.getJarSource(url.toString()));
      }
      catch (Exception e)
      {
        LOG.debug(e);
      }
    }
    
    return null;
  }
  
  public TypeUtil() {}
}
