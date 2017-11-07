package org.apache.commons.lang3.builder;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;









































































































public class HashCodeBuilder
  implements Builder<Integer>
{
  private static final int DEFAULT_INITIAL_VALUE = 17;
  private static final int DEFAULT_MULTIPLIER_VALUE = 37;
  private static final ThreadLocal<Set<IDKey>> REGISTRY = new ThreadLocal();
  











  private final int iConstant;
  











  static Set<IDKey> getRegistry()
  {
    return (Set)REGISTRY.get();
  }
  










  static boolean isRegistered(Object value)
  {
    Set<IDKey> registry = getRegistry();
    return (registry != null) && (registry.contains(new IDKey(value)));
  }
  
















  private static void reflectionAppend(Object object, Class<?> clazz, HashCodeBuilder builder, boolean useTransients, String[] excludeFields)
  {
    if (isRegistered(object)) {
      return;
    }
    try {
      register(object);
      Field[] fields = clazz.getDeclaredFields();
      AccessibleObject.setAccessible(fields, true);
      for (Field field : fields) {
        if ((!ArrayUtils.contains(excludeFields, field.getName())) && 
          (!field.getName().contains("$")) && ((useTransients) || 
          (!Modifier.isTransient(field.getModifiers()))) && 
          (!Modifier.isStatic(field.getModifiers())) && 
          (!field.isAnnotationPresent(HashCodeExclude.class))) {
          try {
            Object fieldValue = field.get(object);
            builder.append(fieldValue);
          }
          catch (IllegalAccessException e)
          {
            throw new InternalError("Unexpected IllegalAccessException");
          }
        }
      }
    } finally {
      unregister(object);
    }
  }
  







































  public static int reflectionHashCode(int initialNonZeroOddNumber, int multiplierNonZeroOddNumber, Object object)
  {
    return reflectionHashCode(initialNonZeroOddNumber, multiplierNonZeroOddNumber, object, false, null, new String[0]);
  }
  










































  public static int reflectionHashCode(int initialNonZeroOddNumber, int multiplierNonZeroOddNumber, Object object, boolean testTransients)
  {
    return reflectionHashCode(initialNonZeroOddNumber, multiplierNonZeroOddNumber, object, testTransients, null, new String[0]);
  }
  



















































  public static <T> int reflectionHashCode(int initialNonZeroOddNumber, int multiplierNonZeroOddNumber, T object, boolean testTransients, Class<? super T> reflectUpToClass, String... excludeFields)
  {
    if (object == null) {
      throw new IllegalArgumentException("The object to build a hash code for must not be null");
    }
    HashCodeBuilder builder = new HashCodeBuilder(initialNonZeroOddNumber, multiplierNonZeroOddNumber);
    Class<?> clazz = object.getClass();
    reflectionAppend(object, clazz, builder, testTransients, excludeFields);
    while ((clazz.getSuperclass() != null) && (clazz != reflectUpToClass)) {
      clazz = clazz.getSuperclass();
      reflectionAppend(object, clazz, builder, testTransients, excludeFields);
    }
    return builder.toHashCode();
  }
  


































  public static int reflectionHashCode(Object object, boolean testTransients)
  {
    return reflectionHashCode(17, 37, object, testTransients, null, new String[0]);
  }
  



































  public static int reflectionHashCode(Object object, Collection<String> excludeFields)
  {
    return reflectionHashCode(object, ReflectionToStringBuilder.toNoNullStringArray(excludeFields));
  }
  




































  public static int reflectionHashCode(Object object, String... excludeFields)
  {
    return reflectionHashCode(17, 37, object, false, null, excludeFields);
  }
  








  private static void register(Object value)
  {
    Set<IDKey> registry = getRegistry();
    if (registry == null) {
      registry = new HashSet();
      REGISTRY.set(registry);
    }
    registry.add(new IDKey(value));
  }
  











  private static void unregister(Object value)
  {
    Set<IDKey> registry = getRegistry();
    if (registry != null) {
      registry.remove(new IDKey(value));
      if (registry.isEmpty()) {
        REGISTRY.remove();
      }
    }
  }
  








  private int iTotal = 0;
  




  public HashCodeBuilder()
  {
    iConstant = 37;
    iTotal = 17;
  }
  
















  public HashCodeBuilder(int initialOddNumber, int multiplierOddNumber)
  {
    Validate.isTrue(initialOddNumber % 2 != 0, "HashCodeBuilder requires an odd initial value", new Object[0]);
    Validate.isTrue(multiplierOddNumber % 2 != 0, "HashCodeBuilder requires an odd multiplier", new Object[0]);
    iConstant = multiplierOddNumber;
    iTotal = initialOddNumber;
  }
  




















  public HashCodeBuilder append(boolean value)
  {
    iTotal = (iTotal * iConstant + (value ? 0 : 1));
    return this;
  }
  








  public HashCodeBuilder append(boolean[] array)
  {
    if (array == null) {
      iTotal *= iConstant;
    } else {
      for (boolean element : array) {
        append(element);
      }
    }
    return this;
  }
  










  public HashCodeBuilder append(byte value)
  {
    iTotal = (iTotal * iConstant + value);
    return this;
  }
  










  public HashCodeBuilder append(byte[] array)
  {
    if (array == null) {
      iTotal *= iConstant;
    } else {
      for (byte element : array) {
        append(element);
      }
    }
    return this;
  }
  








  public HashCodeBuilder append(char value)
  {
    iTotal = (iTotal * iConstant + value);
    return this;
  }
  








  public HashCodeBuilder append(char[] array)
  {
    if (array == null) {
      iTotal *= iConstant;
    } else {
      for (char element : array) {
        append(element);
      }
    }
    return this;
  }
  








  public HashCodeBuilder append(double value)
  {
    return append(Double.doubleToLongBits(value));
  }
  








  public HashCodeBuilder append(double[] array)
  {
    if (array == null) {
      iTotal *= iConstant;
    } else {
      for (double element : array) {
        append(element);
      }
    }
    return this;
  }
  








  public HashCodeBuilder append(float value)
  {
    iTotal = (iTotal * iConstant + Float.floatToIntBits(value));
    return this;
  }
  








  public HashCodeBuilder append(float[] array)
  {
    if (array == null) {
      iTotal *= iConstant;
    } else {
      for (float element : array) {
        append(element);
      }
    }
    return this;
  }
  








  public HashCodeBuilder append(int value)
  {
    iTotal = (iTotal * iConstant + value);
    return this;
  }
  








  public HashCodeBuilder append(int[] array)
  {
    if (array == null) {
      iTotal *= iConstant;
    } else {
      for (int element : array) {
        append(element);
      }
    }
    return this;
  }
  












  public HashCodeBuilder append(long value)
  {
    iTotal = (iTotal * iConstant + (int)(value ^ value >> 32));
    return this;
  }
  








  public HashCodeBuilder append(long[] array)
  {
    if (array == null) {
      iTotal *= iConstant;
    } else {
      for (long element : array) {
        append(element);
      }
    }
    return this;
  }
  








  public HashCodeBuilder append(Object object)
  {
    if (object == null) {
      iTotal *= iConstant;

    }
    else if (object.getClass().isArray())
    {

      appendArray(object);
    } else {
      iTotal = (iTotal * iConstant + object.hashCode());
    }
    
    return this;
  }
  









  private void appendArray(Object object)
  {
    if ((object instanceof long[])) {
      append((long[])object);
    } else if ((object instanceof int[])) {
      append((int[])object);
    } else if ((object instanceof short[])) {
      append((short[])object);
    } else if ((object instanceof char[])) {
      append((char[])object);
    } else if ((object instanceof byte[])) {
      append((byte[])object);
    } else if ((object instanceof double[])) {
      append((double[])object);
    } else if ((object instanceof float[])) {
      append((float[])object);
    } else if ((object instanceof boolean[])) {
      append((boolean[])object);
    }
    else {
      append((Object[])object);
    }
  }
  








  public HashCodeBuilder append(Object[] array)
  {
    if (array == null) {
      iTotal *= iConstant;
    } else {
      for (Object element : array) {
        append(element);
      }
    }
    return this;
  }
  








  public HashCodeBuilder append(short value)
  {
    iTotal = (iTotal * iConstant + value);
    return this;
  }
  








  public HashCodeBuilder append(short[] array)
  {
    if (array == null) {
      iTotal *= iConstant;
    } else {
      for (short element : array) {
        append(element);
      }
    }
    return this;
  }
  









  public HashCodeBuilder appendSuper(int superHashCode)
  {
    iTotal = (iTotal * iConstant + superHashCode);
    return this;
  }
  






  public int toHashCode()
  {
    return iTotal;
  }
  







  public Integer build()
  {
    return Integer.valueOf(toHashCode());
  }
  









  public int hashCode()
  {
    return toHashCode();
  }
}
