package org.apache.commons.lang3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;




































public class EnumUtils
{
  private static final String NULL_ELEMENTS_NOT_PERMITTED = "null elements not permitted";
  private static final String CANNOT_STORE_S_S_VALUES_IN_S_BITS = "Cannot store %s %s values in %s bits";
  private static final String S_DOES_NOT_SEEM_TO_BE_AN_ENUM_TYPE = "%s does not seem to be an Enum type";
  private static final String ENUM_CLASS_MUST_BE_DEFINED = "EnumClass must be defined.";
  
  public EnumUtils() {}
  
  public static <E extends Enum<E>> Map<String, E> getEnumMap(Class<E> enumClass)
  {
    Map<String, E> map = new LinkedHashMap();
    for (E e : (Enum[])enumClass.getEnumConstants()) {
      map.put(e.name(), e);
    }
    return map;
  }
  








  public static <E extends Enum<E>> List<E> getEnumList(Class<E> enumClass)
  {
    return new ArrayList(Arrays.asList(enumClass.getEnumConstants()));
  }
  










  public static <E extends Enum<E>> boolean isValidEnum(Class<E> enumClass, String enumName)
  {
    if (enumName == null) {
      return false;
    }
    try {
      Enum.valueOf(enumClass, enumName);
      return true;
    } catch (IllegalArgumentException ex) {}
    return false;
  }
  











  public static <E extends Enum<E>> E getEnum(Class<E> enumClass, String enumName)
  {
    if (enumName == null) {
      return null;
    }
    try {
      return Enum.valueOf(enumClass, enumName);
    } catch (IllegalArgumentException ex) {}
    return null;
  }
  


















  public static <E extends Enum<E>> long generateBitVector(Class<E> enumClass, Iterable<? extends E> values)
  {
    checkBitVectorable(enumClass);
    Validate.notNull(values);
    long total = 0L;
    for (E constant : values) {
      Validate.isTrue(constant != null, "null elements not permitted", new Object[0]);
      total |= 1L << constant.ordinal();
    }
    return total;
  }
  















  public static <E extends Enum<E>> long[] generateBitVectors(Class<E> enumClass, Iterable<? extends E> values)
  {
    asEnum(enumClass);
    Validate.notNull(values);
    EnumSet<E> condensed = EnumSet.noneOf(enumClass);
    for (Iterator localIterator = values.iterator(); localIterator.hasNext();) { constant = (Enum)localIterator.next();
      Validate.isTrue(constant != null, "null elements not permitted", new Object[0]);
      condensed.add(constant); }
    E constant;
    long[] result = new long[(((Enum[])enumClass.getEnumConstants()).length - 1) / 64 + 1];
    for (E value : condensed) {
      result[(value.ordinal() / 64)] |= 1L << value.ordinal() % 64;
    }
    ArrayUtils.reverse(result);
    return result;
  }
  
















  public static <E extends Enum<E>> long generateBitVector(Class<E> enumClass, E... values)
  {
    Validate.noNullElements(values);
    return generateBitVector(enumClass, Arrays.asList(values));
  }
  















  public static <E extends Enum<E>> long[] generateBitVectors(Class<E> enumClass, E... values)
  {
    asEnum(enumClass);
    Validate.noNullElements(values);
    EnumSet<E> condensed = EnumSet.noneOf(enumClass);
    Collections.addAll(condensed, values);
    long[] result = new long[(((Enum[])enumClass.getEnumConstants()).length - 1) / 64 + 1];
    for (E value : condensed) {
      result[(value.ordinal() / 64)] |= 1L << value.ordinal() % 64;
    }
    ArrayUtils.reverse(result);
    return result;
  }
  












  public static <E extends Enum<E>> EnumSet<E> processBitVector(Class<E> enumClass, long value)
  {
    checkBitVectorable(enumClass).getEnumConstants();
    return processBitVectors(enumClass, new long[] { value });
  }
  












  public static <E extends Enum<E>> EnumSet<E> processBitVectors(Class<E> enumClass, long... values)
  {
    EnumSet<E> results = EnumSet.noneOf(asEnum(enumClass));
    long[] lvalues = ArrayUtils.clone((long[])Validate.notNull(values));
    ArrayUtils.reverse(lvalues);
    for (E constant : (Enum[])enumClass.getEnumConstants()) {
      int block = constant.ordinal() / 64;
      if ((block < lvalues.length) && ((lvalues[block] & 1L << constant.ordinal() % 64) != 0L)) {
        results.add(constant);
      }
    }
    return results;
  }
  








  private static <E extends Enum<E>> Class<E> checkBitVectorable(Class<E> enumClass)
  {
    E[] constants = (Enum[])asEnum(enumClass).getEnumConstants();
    Validate.isTrue(constants.length <= 64, "Cannot store %s %s values in %s bits", new Object[] {
      Integer.valueOf(constants.length), enumClass.getSimpleName(), Integer.valueOf(64) });
    
    return enumClass;
  }
  








  private static <E extends Enum<E>> Class<E> asEnum(Class<E> enumClass)
  {
    Validate.notNull(enumClass, "EnumClass must be defined.", new Object[0]);
    Validate.isTrue(enumClass.isEnum(), "%s does not seem to be an Enum type", new Object[] { enumClass });
    return enumClass;
  }
}
