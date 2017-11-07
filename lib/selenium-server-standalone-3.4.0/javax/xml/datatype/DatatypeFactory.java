package javax.xml.datatype;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.GregorianCalendar;

public abstract class DatatypeFactory
{
  public static final String DATATYPEFACTORY_PROPERTY = "javax.xml.datatype.DatatypeFactory";
  public static final String DATATYPEFACTORY_IMPLEMENTATION_CLASS = new String("org.apache.xerces.jaxp.datatype.DatatypeFactoryImpl");
  
  protected DatatypeFactory() {}
  
  public static DatatypeFactory newInstance()
    throws DatatypeConfigurationException
  {
    try
    {
      return (DatatypeFactory)FactoryFinder.find("javax.xml.datatype.DatatypeFactory", DATATYPEFACTORY_IMPLEMENTATION_CLASS);
    }
    catch (FactoryFinder.ConfigurationError localConfigurationError)
    {
      throw new DatatypeConfigurationException(localConfigurationError.getMessage(), localConfigurationError.getException());
    }
  }
  
  public static DatatypeFactory newInstance(String paramString, ClassLoader paramClassLoader)
    throws DatatypeConfigurationException
  {
    if (paramString == null) {
      throw new DatatypeConfigurationException("factoryClassName cannot be null.");
    }
    if (paramClassLoader == null) {
      paramClassLoader = SecuritySupport.getContextClassLoader();
    }
    try
    {
      return (DatatypeFactory)FactoryFinder.newInstance(paramString, paramClassLoader);
    }
    catch (FactoryFinder.ConfigurationError localConfigurationError)
    {
      throw new DatatypeConfigurationException(localConfigurationError.getMessage(), localConfigurationError.getException());
    }
  }
  
  public abstract Duration newDuration(String paramString);
  
  public abstract Duration newDuration(long paramLong);
  
  public abstract Duration newDuration(boolean paramBoolean, BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, BigInteger paramBigInteger4, BigInteger paramBigInteger5, BigDecimal paramBigDecimal);
  
  public Duration newDuration(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
  {
    BigInteger localBigInteger1 = paramInt1 != Integer.MIN_VALUE ? BigInteger.valueOf(paramInt1) : null;
    BigInteger localBigInteger2 = paramInt2 != Integer.MIN_VALUE ? BigInteger.valueOf(paramInt2) : null;
    BigInteger localBigInteger3 = paramInt3 != Integer.MIN_VALUE ? BigInteger.valueOf(paramInt3) : null;
    BigInteger localBigInteger4 = paramInt4 != Integer.MIN_VALUE ? BigInteger.valueOf(paramInt4) : null;
    BigInteger localBigInteger5 = paramInt5 != Integer.MIN_VALUE ? BigInteger.valueOf(paramInt5) : null;
    BigDecimal localBigDecimal = paramInt6 != Integer.MIN_VALUE ? BigDecimal.valueOf(paramInt6) : null;
    return newDuration(paramBoolean, localBigInteger1, localBigInteger2, localBigInteger3, localBigInteger4, localBigInteger5, localBigDecimal);
  }
  
  public Duration newDurationDayTime(String paramString)
  {
    if (paramString == null) {
      throw new NullPointerException("The lexical representation cannot be null.");
    }
    int i = paramString.indexOf('T');
    int j = i >= 0 ? i : paramString.length();
    for (int k = 0; k < j; k++)
    {
      int m = paramString.charAt(k);
      if ((m == 89) || (m == 77)) {
        throw new IllegalArgumentException("Invalid dayTimeDuration value: " + paramString);
      }
    }
    return newDuration(paramString);
  }
  
  public Duration newDurationDayTime(long paramLong)
  {
    long l1 = paramLong;
    if (l1 == 0L) {
      return newDuration(true, Integer.MIN_VALUE, Integer.MIN_VALUE, 0, 0, 0, 0);
    }
    int i = 0;
    boolean bool;
    if (l1 < 0L)
    {
      bool = false;
      if (l1 == Long.MIN_VALUE)
      {
        l1 += 1L;
        i = 1;
      }
      l1 *= -1L;
    }
    else
    {
      bool = true;
    }
    long l2 = l1;
    int j = (int)(l2 % 60000L);
    if (i != 0) {
      j++;
    }
    if (j % 1000 == 0)
    {
      int k = j / 1000;
      l2 /= 60000L;
      int m = (int)(l2 % 60L);
      l2 /= 60L;
      int n = (int)(l2 % 24L);
      long l3 = l2 / 24L;
      if (l3 <= 2147483647L) {
        return newDuration(bool, Integer.MIN_VALUE, Integer.MIN_VALUE, (int)l3, n, m, k);
      }
      return newDuration(bool, null, null, BigInteger.valueOf(l3), BigInteger.valueOf(n), BigInteger.valueOf(m), BigDecimal.valueOf(j, 3));
    }
    BigDecimal localBigDecimal = BigDecimal.valueOf(j, 3);
    l2 /= 60000L;
    BigInteger localBigInteger1 = BigInteger.valueOf(l2 % 60L);
    l2 /= 60L;
    BigInteger localBigInteger2 = BigInteger.valueOf(l2 % 24L);
    l2 /= 24L;
    BigInteger localBigInteger3 = BigInteger.valueOf(l2);
    return newDuration(bool, null, null, localBigInteger3, localBigInteger2, localBigInteger1, localBigDecimal);
  }
  
  public Duration newDurationDayTime(boolean paramBoolean, BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, BigInteger paramBigInteger4)
  {
    return newDuration(paramBoolean, null, null, paramBigInteger1, paramBigInteger2, paramBigInteger3, paramBigInteger4 != null ? new BigDecimal(paramBigInteger4) : null);
  }
  
  public Duration newDurationDayTime(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    return newDuration(paramBoolean, Integer.MIN_VALUE, Integer.MIN_VALUE, paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  public Duration newDurationYearMonth(String paramString)
  {
    if (paramString == null) {
      throw new NullPointerException("The lexical representation cannot be null.");
    }
    int i = paramString.length();
    for (int j = 0; j < i; j++)
    {
      int k = paramString.charAt(j);
      if ((k == 68) || (k == 84)) {
        throw new IllegalArgumentException("Invalid yearMonthDuration value: " + paramString);
      }
    }
    return newDuration(paramString);
  }
  
  public Duration newDurationYearMonth(long paramLong)
  {
    return newDuration(paramLong);
  }
  
  public Duration newDurationYearMonth(boolean paramBoolean, BigInteger paramBigInteger1, BigInteger paramBigInteger2)
  {
    return newDuration(paramBoolean, paramBigInteger1, paramBigInteger2, null, null, null, null);
  }
  
  public Duration newDurationYearMonth(boolean paramBoolean, int paramInt1, int paramInt2)
  {
    return newDuration(paramBoolean, paramInt1, paramInt2, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
  }
  
  public abstract XMLGregorianCalendar newXMLGregorianCalendar();
  
  public abstract XMLGregorianCalendar newXMLGregorianCalendar(String paramString);
  
  public abstract XMLGregorianCalendar newXMLGregorianCalendar(GregorianCalendar paramGregorianCalendar);
  
  public abstract XMLGregorianCalendar newXMLGregorianCalendar(BigInteger paramBigInteger, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, BigDecimal paramBigDecimal, int paramInt6);
  
  public XMLGregorianCalendar newXMLGregorianCalendar(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8)
  {
    BigInteger localBigInteger = paramInt1 != Integer.MIN_VALUE ? BigInteger.valueOf(paramInt1) : null;
    BigDecimal localBigDecimal = null;
    if (paramInt7 != Integer.MIN_VALUE)
    {
      if ((paramInt7 < 0) || (paramInt7 > 1000)) {
        throw new IllegalArgumentException("javax.xml.datatype.DatatypeFactory#newXMLGregorianCalendar(int year, int month, int day, int hour, int minute, int second, int millisecond, int timezone)with invalid millisecond: " + paramInt7);
      }
      localBigDecimal = BigDecimal.valueOf(paramInt7, 3);
    }
    return newXMLGregorianCalendar(localBigInteger, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, localBigDecimal, paramInt8);
  }
  
  public XMLGregorianCalendar newXMLGregorianCalendarDate(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    return newXMLGregorianCalendar(paramInt1, paramInt2, paramInt3, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, paramInt4);
  }
  
  public XMLGregorianCalendar newXMLGregorianCalendarTime(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    return newXMLGregorianCalendar(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, paramInt1, paramInt2, paramInt3, Integer.MIN_VALUE, paramInt4);
  }
  
  public XMLGregorianCalendar newXMLGregorianCalendarTime(int paramInt1, int paramInt2, int paramInt3, BigDecimal paramBigDecimal, int paramInt4)
  {
    return newXMLGregorianCalendar(null, Integer.MIN_VALUE, Integer.MIN_VALUE, paramInt1, paramInt2, paramInt3, paramBigDecimal, paramInt4);
  }
  
  public XMLGregorianCalendar newXMLGregorianCalendarTime(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    BigDecimal localBigDecimal = null;
    if (paramInt4 != Integer.MIN_VALUE)
    {
      if ((paramInt4 < 0) || (paramInt4 > 1000)) {
        throw new IllegalArgumentException("javax.xml.datatype.DatatypeFactory#newXMLGregorianCalendarTime(int hours, int minutes, int seconds, int milliseconds, int timezone)with invalid milliseconds: " + paramInt4);
      }
      localBigDecimal = BigDecimal.valueOf(paramInt4, 3);
    }
    return newXMLGregorianCalendarTime(paramInt1, paramInt2, paramInt3, localBigDecimal, paramInt5);
  }
}
