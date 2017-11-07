package org.apache.xerces.jaxp.datatype;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeConstants.Field;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.xerces.util.DatatypeMessageFormatter;

class DurationImpl
  extends Duration
  implements Serializable
{
  private static final long serialVersionUID = -2650025807136350131L;
  private static final DatatypeConstants.Field[] FIELDS = { DatatypeConstants.YEARS, DatatypeConstants.MONTHS, DatatypeConstants.DAYS, DatatypeConstants.HOURS, DatatypeConstants.MINUTES, DatatypeConstants.SECONDS };
  private static final BigDecimal ZERO = BigDecimal.valueOf(0L);
  private final int signum;
  private final BigInteger years;
  private final BigInteger months;
  private final BigInteger days;
  private final BigInteger hours;
  private final BigInteger minutes;
  private final BigDecimal seconds;
  private static final XMLGregorianCalendar[] TEST_POINTS = { XMLGregorianCalendarImpl.parse("1696-09-01T00:00:00Z"), XMLGregorianCalendarImpl.parse("1697-02-01T00:00:00Z"), XMLGregorianCalendarImpl.parse("1903-03-01T00:00:00Z"), XMLGregorianCalendarImpl.parse("1903-07-01T00:00:00Z") };
  private static final BigDecimal[] FACTORS = { BigDecimal.valueOf(12L), null, BigDecimal.valueOf(24L), BigDecimal.valueOf(60L), BigDecimal.valueOf(60L) };
  
  public int getSign()
  {
    return signum;
  }
  
  private int calcSignum(boolean paramBoolean)
  {
    if (((years == null) || (years.signum() == 0)) && ((months == null) || (months.signum() == 0)) && ((days == null) || (days.signum() == 0)) && ((hours == null) || (hours.signum() == 0)) && ((minutes == null) || (minutes.signum() == 0)) && ((seconds == null) || (seconds.signum() == 0))) {
      return 0;
    }
    if (paramBoolean) {
      return 1;
    }
    return -1;
  }
  
  protected DurationImpl(boolean paramBoolean, BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, BigInteger paramBigInteger4, BigInteger paramBigInteger5, BigDecimal paramBigDecimal)
  {
    years = paramBigInteger1;
    months = paramBigInteger2;
    days = paramBigInteger3;
    hours = paramBigInteger4;
    minutes = paramBigInteger5;
    seconds = paramBigDecimal;
    signum = calcSignum(paramBoolean);
    if ((paramBigInteger1 == null) && (paramBigInteger2 == null) && (paramBigInteger3 == null) && (paramBigInteger4 == null) && (paramBigInteger5 == null) && (paramBigDecimal == null)) {
      throw new IllegalArgumentException(DatatypeMessageFormatter.formatMessage(null, "AllFieldsNull", null));
    }
    testNonNegative(paramBigInteger1, DatatypeConstants.YEARS);
    testNonNegative(paramBigInteger2, DatatypeConstants.MONTHS);
    testNonNegative(paramBigInteger3, DatatypeConstants.DAYS);
    testNonNegative(paramBigInteger4, DatatypeConstants.HOURS);
    testNonNegative(paramBigInteger5, DatatypeConstants.MINUTES);
    testNonNegative(paramBigDecimal, DatatypeConstants.SECONDS);
  }
  
  private static void testNonNegative(BigInteger paramBigInteger, DatatypeConstants.Field paramField)
  {
    if ((paramBigInteger != null) && (paramBigInteger.signum() < 0)) {
      throw new IllegalArgumentException(DatatypeMessageFormatter.formatMessage(null, "NegativeField", new Object[] { paramField.toString() }));
    }
  }
  
  private static void testNonNegative(BigDecimal paramBigDecimal, DatatypeConstants.Field paramField)
  {
    if ((paramBigDecimal != null) && (paramBigDecimal.signum() < 0)) {
      throw new IllegalArgumentException(DatatypeMessageFormatter.formatMessage(null, "NegativeField", new Object[] { paramField.toString() }));
    }
  }
  
  protected DurationImpl(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
  {
    this(paramBoolean, wrap(paramInt1), wrap(paramInt2), wrap(paramInt3), wrap(paramInt4), wrap(paramInt5), paramInt6 != 0 ? BigDecimal.valueOf(paramInt6) : null);
  }
  
  private static BigInteger wrap(int paramInt)
  {
    if (paramInt == Integer.MIN_VALUE) {
      return null;
    }
    return BigInteger.valueOf(paramInt);
  }
  
  protected DurationImpl(long paramLong)
  {
    int i = 0;
    long l = paramLong;
    if (l > 0L)
    {
      signum = 1;
    }
    else if (l < 0L)
    {
      signum = -1;
      if (l == Long.MIN_VALUE)
      {
        l += 1L;
        i = 1;
      }
      l *= -1L;
    }
    else
    {
      signum = 0;
    }
    years = null;
    months = null;
    seconds = BigDecimal.valueOf(l % 60000L + (i != 0 ? 1L : 0L), 3);
    l /= 60000L;
    minutes = (l == 0L ? null : BigInteger.valueOf(l % 60L));
    l /= 60L;
    hours = (l == 0L ? null : BigInteger.valueOf(l % 24L));
    l /= 24L;
    days = (l == 0L ? null : BigInteger.valueOf(l));
  }
  
  protected DurationImpl(String paramString)
    throws IllegalArgumentException
  {
    if (paramString == null) {
      throw new NullPointerException();
    }
    String str = paramString;
    int[] arrayOfInt1 = new int[1];
    int i = str.length();
    int j = 0;
    arrayOfInt1[0] = 0;
    boolean bool;
    if ((i != arrayOfInt1[0]) && (str.charAt(arrayOfInt1[0]) == '-'))
    {
      arrayOfInt1[0] += 1;
      bool = false;
    }
    else
    {
      bool = true;
    }
    if (i != arrayOfInt1[0])
    {
      int tmp87_86 = 0;
      int[] tmp87_84 = arrayOfInt1;
      int tmp89_88 = tmp87_84[tmp87_86];
      tmp87_84[tmp87_86] = (tmp89_88 + 1);
      if (str.charAt(tmp89_88) != 'P') {
        throw new IllegalArgumentException(str);
      }
    }
    int k = 0;
    String[] arrayOfString1 = new String[3];
    int[] arrayOfInt2 = new int[3];
    while ((i != arrayOfInt1[0]) && (isDigit(str.charAt(arrayOfInt1[0]))) && (k < 3))
    {
      arrayOfInt2[k] = arrayOfInt1[0];
      arrayOfString1[(k++)] = parsePiece(str, arrayOfInt1);
    }
    if (i != arrayOfInt1[0])
    {
      int tmp192_191 = 0;
      int[] tmp192_189 = arrayOfInt1;
      int tmp194_193 = tmp192_189[tmp192_191];
      tmp192_189[tmp192_191] = (tmp194_193 + 1);
      if (str.charAt(tmp194_193) == 'T') {
        j = 1;
      } else {
        throw new IllegalArgumentException(str);
      }
    }
    int m = 0;
    String[] arrayOfString2 = new String[3];
    int[] arrayOfInt3 = new int[3];
    while ((i != arrayOfInt1[0]) && (isDigitOrPeriod(str.charAt(arrayOfInt1[0]))) && (m < 3))
    {
      arrayOfInt3[m] = arrayOfInt1[0];
      arrayOfString2[(m++)] = parsePiece(str, arrayOfInt1);
    }
    if ((j != 0) && (m == 0)) {
      throw new IllegalArgumentException(str);
    }
    if (i != arrayOfInt1[0]) {
      throw new IllegalArgumentException(str);
    }
    if ((k == 0) && (m == 0)) {
      throw new IllegalArgumentException(str);
    }
    organizeParts(str, arrayOfString1, arrayOfInt2, k, "YMD");
    organizeParts(str, arrayOfString2, arrayOfInt3, m, "HMS");
    years = parseBigInteger(str, arrayOfString1[0], arrayOfInt2[0]);
    months = parseBigInteger(str, arrayOfString1[1], arrayOfInt2[1]);
    days = parseBigInteger(str, arrayOfString1[2], arrayOfInt2[2]);
    hours = parseBigInteger(str, arrayOfString2[0], arrayOfInt3[0]);
    minutes = parseBigInteger(str, arrayOfString2[1], arrayOfInt3[1]);
    seconds = parseBigDecimal(str, arrayOfString2[2], arrayOfInt3[2]);
    signum = calcSignum(bool);
  }
  
  private static boolean isDigit(char paramChar)
  {
    return ('0' <= paramChar) && (paramChar <= '9');
  }
  
  private static boolean isDigitOrPeriod(char paramChar)
  {
    return (isDigit(paramChar)) || (paramChar == '.');
  }
  
  private static String parsePiece(String paramString, int[] paramArrayOfInt)
    throws IllegalArgumentException
  {
    int i = paramArrayOfInt[0];
    while ((paramArrayOfInt[0] < paramString.length()) && (isDigitOrPeriod(paramString.charAt(paramArrayOfInt[0])))) {
      paramArrayOfInt[0] += 1;
    }
    if (paramArrayOfInt[0] == paramString.length()) {
      throw new IllegalArgumentException(paramString);
    }
    paramArrayOfInt[0] += 1;
    return paramString.substring(i, paramArrayOfInt[0]);
  }
  
  private static void organizeParts(String paramString1, String[] paramArrayOfString, int[] paramArrayOfInt, int paramInt, String paramString2)
    throws IllegalArgumentException
  {
    int i = paramString2.length();
    for (int j = paramInt - 1; j >= 0; j--)
    {
      if (paramArrayOfString[j] == null) {
        throw new IllegalArgumentException(paramString1);
      }
      int k = paramString2.lastIndexOf(paramArrayOfString[j].charAt(paramArrayOfString[j].length() - 1), i - 1);
      if (k == -1) {
        throw new IllegalArgumentException(paramString1);
      }
      for (int m = k + 1; m < i; m++) {
        paramArrayOfString[m] = null;
      }
      i = k;
      paramArrayOfString[i] = paramArrayOfString[j];
      paramArrayOfInt[i] = paramArrayOfInt[j];
    }
    i--;
    while (i >= 0)
    {
      paramArrayOfString[i] = null;
      i--;
    }
  }
  
  private static BigInteger parseBigInteger(String paramString1, String paramString2, int paramInt)
    throws IllegalArgumentException
  {
    if (paramString2 == null) {
      return null;
    }
    paramString2 = paramString2.substring(0, paramString2.length() - 1);
    return new BigInteger(paramString2);
  }
  
  private static BigDecimal parseBigDecimal(String paramString1, String paramString2, int paramInt)
    throws IllegalArgumentException
  {
    if (paramString2 == null) {
      return null;
    }
    paramString2 = paramString2.substring(0, paramString2.length() - 1);
    return new BigDecimal(paramString2);
  }
  
  public int compare(Duration paramDuration)
  {
    BigInteger localBigInteger1 = BigInteger.valueOf(2147483647L);
    if ((years != null) && (years.compareTo(localBigInteger1) == 1)) {
      throw new UnsupportedOperationException(DatatypeMessageFormatter.formatMessage(null, "TooLarge", new Object[] { getClass().getName() + "#compare(Duration duration)" + DatatypeConstants.YEARS.toString(), years.toString() }));
    }
    if ((months != null) && (months.compareTo(localBigInteger1) == 1)) {
      throw new UnsupportedOperationException(DatatypeMessageFormatter.formatMessage(null, "TooLarge", new Object[] { getClass().getName() + "#compare(Duration duration)" + DatatypeConstants.MONTHS.toString(), months.toString() }));
    }
    if ((days != null) && (days.compareTo(localBigInteger1) == 1)) {
      throw new UnsupportedOperationException(DatatypeMessageFormatter.formatMessage(null, "TooLarge", new Object[] { getClass().getName() + "#compare(Duration duration)" + DatatypeConstants.DAYS.toString(), days.toString() }));
    }
    if ((hours != null) && (hours.compareTo(localBigInteger1) == 1)) {
      throw new UnsupportedOperationException(DatatypeMessageFormatter.formatMessage(null, "TooLarge", new Object[] { getClass().getName() + "#compare(Duration duration)" + DatatypeConstants.HOURS.toString(), hours.toString() }));
    }
    if ((minutes != null) && (minutes.compareTo(localBigInteger1) == 1)) {
      throw new UnsupportedOperationException(DatatypeMessageFormatter.formatMessage(null, "TooLarge", new Object[] { getClass().getName() + "#compare(Duration duration)" + DatatypeConstants.MINUTES.toString(), minutes.toString() }));
    }
    if ((seconds != null) && (seconds.toBigInteger().compareTo(localBigInteger1) == 1)) {
      throw new UnsupportedOperationException(DatatypeMessageFormatter.formatMessage(null, "TooLarge", new Object[] { getClass().getName() + "#compare(Duration duration)" + DatatypeConstants.SECONDS.toString(), toString(seconds) }));
    }
    BigInteger localBigInteger2 = (BigInteger)paramDuration.getField(DatatypeConstants.YEARS);
    if ((localBigInteger2 != null) && (localBigInteger2.compareTo(localBigInteger1) == 1)) {
      throw new UnsupportedOperationException(DatatypeMessageFormatter.formatMessage(null, "TooLarge", new Object[] { getClass().getName() + "#compare(Duration duration)" + DatatypeConstants.YEARS.toString(), localBigInteger2.toString() }));
    }
    BigInteger localBigInteger3 = (BigInteger)paramDuration.getField(DatatypeConstants.MONTHS);
    if ((localBigInteger3 != null) && (localBigInteger3.compareTo(localBigInteger1) == 1)) {
      throw new UnsupportedOperationException(DatatypeMessageFormatter.formatMessage(null, "TooLarge", new Object[] { getClass().getName() + "#compare(Duration duration)" + DatatypeConstants.MONTHS.toString(), localBigInteger3.toString() }));
    }
    BigInteger localBigInteger4 = (BigInteger)paramDuration.getField(DatatypeConstants.DAYS);
    if ((localBigInteger4 != null) && (localBigInteger4.compareTo(localBigInteger1) == 1)) {
      throw new UnsupportedOperationException(DatatypeMessageFormatter.formatMessage(null, "TooLarge", new Object[] { getClass().getName() + "#compare(Duration duration)" + DatatypeConstants.DAYS.toString(), localBigInteger4.toString() }));
    }
    BigInteger localBigInteger5 = (BigInteger)paramDuration.getField(DatatypeConstants.HOURS);
    if ((localBigInteger5 != null) && (localBigInteger5.compareTo(localBigInteger1) == 1)) {
      throw new UnsupportedOperationException(DatatypeMessageFormatter.formatMessage(null, "TooLarge", new Object[] { getClass().getName() + "#compare(Duration duration)" + DatatypeConstants.HOURS.toString(), localBigInteger5.toString() }));
    }
    BigInteger localBigInteger6 = (BigInteger)paramDuration.getField(DatatypeConstants.MINUTES);
    if ((localBigInteger6 != null) && (localBigInteger6.compareTo(localBigInteger1) == 1)) {
      throw new UnsupportedOperationException(DatatypeMessageFormatter.formatMessage(null, "TooLarge", new Object[] { getClass().getName() + "#compare(Duration duration)" + DatatypeConstants.MINUTES.toString(), localBigInteger6.toString() }));
    }
    BigDecimal localBigDecimal = (BigDecimal)paramDuration.getField(DatatypeConstants.SECONDS);
    BigInteger localBigInteger7 = null;
    if (localBigDecimal != null) {
      localBigInteger7 = localBigDecimal.toBigInteger();
    }
    if ((localBigInteger7 != null) && (localBigInteger7.compareTo(localBigInteger1) == 1)) {
      throw new UnsupportedOperationException(DatatypeMessageFormatter.formatMessage(null, "TooLarge", new Object[] { getClass().getName() + "#compare(Duration duration)" + DatatypeConstants.SECONDS.toString(), localBigInteger7.toString() }));
    }
    GregorianCalendar localGregorianCalendar1 = new GregorianCalendar(1970, 1, 1, 0, 0, 0);
    localGregorianCalendar1.add(1, getYears() * getSign());
    localGregorianCalendar1.add(2, getMonths() * getSign());
    localGregorianCalendar1.add(6, getDays() * getSign());
    localGregorianCalendar1.add(11, getHours() * getSign());
    localGregorianCalendar1.add(12, getMinutes() * getSign());
    localGregorianCalendar1.add(13, getSeconds() * getSign());
    GregorianCalendar localGregorianCalendar2 = new GregorianCalendar(1970, 1, 1, 0, 0, 0);
    localGregorianCalendar2.add(1, paramDuration.getYears() * paramDuration.getSign());
    localGregorianCalendar2.add(2, paramDuration.getMonths() * paramDuration.getSign());
    localGregorianCalendar2.add(6, paramDuration.getDays() * paramDuration.getSign());
    localGregorianCalendar2.add(11, paramDuration.getHours() * paramDuration.getSign());
    localGregorianCalendar2.add(12, paramDuration.getMinutes() * paramDuration.getSign());
    localGregorianCalendar2.add(13, paramDuration.getSeconds() * paramDuration.getSign());
    if (localGregorianCalendar1.equals(localGregorianCalendar2)) {
      return 0;
    }
    return compareDates(this, paramDuration);
  }
  
  private int compareDates(Duration paramDuration1, Duration paramDuration2)
  {
    int i = 2;
    int j = 2;
    XMLGregorianCalendar localXMLGregorianCalendar1 = (XMLGregorianCalendar)TEST_POINTS[0].clone();
    XMLGregorianCalendar localXMLGregorianCalendar2 = (XMLGregorianCalendar)TEST_POINTS[0].clone();
    localXMLGregorianCalendar1.add(paramDuration1);
    localXMLGregorianCalendar2.add(paramDuration2);
    i = localXMLGregorianCalendar1.compare(localXMLGregorianCalendar2);
    if (i == 2) {
      return 2;
    }
    localXMLGregorianCalendar1 = (XMLGregorianCalendar)TEST_POINTS[1].clone();
    localXMLGregorianCalendar2 = (XMLGregorianCalendar)TEST_POINTS[1].clone();
    localXMLGregorianCalendar1.add(paramDuration1);
    localXMLGregorianCalendar2.add(paramDuration2);
    j = localXMLGregorianCalendar1.compare(localXMLGregorianCalendar2);
    i = compareResults(i, j);
    if (i == 2) {
      return 2;
    }
    localXMLGregorianCalendar1 = (XMLGregorianCalendar)TEST_POINTS[2].clone();
    localXMLGregorianCalendar2 = (XMLGregorianCalendar)TEST_POINTS[2].clone();
    localXMLGregorianCalendar1.add(paramDuration1);
    localXMLGregorianCalendar2.add(paramDuration2);
    j = localXMLGregorianCalendar1.compare(localXMLGregorianCalendar2);
    i = compareResults(i, j);
    if (i == 2) {
      return 2;
    }
    localXMLGregorianCalendar1 = (XMLGregorianCalendar)TEST_POINTS[3].clone();
    localXMLGregorianCalendar2 = (XMLGregorianCalendar)TEST_POINTS[3].clone();
    localXMLGregorianCalendar1.add(paramDuration1);
    localXMLGregorianCalendar2.add(paramDuration2);
    j = localXMLGregorianCalendar1.compare(localXMLGregorianCalendar2);
    i = compareResults(i, j);
    return i;
  }
  
  private int compareResults(int paramInt1, int paramInt2)
  {
    if (paramInt2 == 2) {
      return 2;
    }
    if (paramInt1 != paramInt2) {
      return 2;
    }
    return paramInt1;
  }
  
  public int hashCode()
  {
    GregorianCalendar localGregorianCalendar = TEST_POINTS[0].toGregorianCalendar();
    addTo(localGregorianCalendar);
    return (int)getCalendarTimeInMillis(localGregorianCalendar);
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    if (signum < 0) {
      localStringBuffer.append('-');
    }
    localStringBuffer.append('P');
    if (years != null) {
      localStringBuffer.append(years).append('Y');
    }
    if (months != null) {
      localStringBuffer.append(months).append('M');
    }
    if (days != null) {
      localStringBuffer.append(days).append('D');
    }
    if ((hours != null) || (minutes != null) || (seconds != null))
    {
      localStringBuffer.append('T');
      if (hours != null) {
        localStringBuffer.append(hours).append('H');
      }
      if (minutes != null) {
        localStringBuffer.append(minutes).append('M');
      }
      if (seconds != null) {
        localStringBuffer.append(toString(seconds)).append('S');
      }
    }
    return localStringBuffer.toString();
  }
  
  private String toString(BigDecimal paramBigDecimal)
  {
    String str = paramBigDecimal.unscaledValue().toString();
    int i = paramBigDecimal.scale();
    if (i == 0) {
      return str;
    }
    int j = str.length() - i;
    if (j == 0) {
      return "0." + str;
    }
    StringBuffer localStringBuffer;
    if (j > 0)
    {
      localStringBuffer = new StringBuffer(str);
      localStringBuffer.insert(j, '.');
    }
    else
    {
      localStringBuffer = new StringBuffer(3 - j + str.length());
      localStringBuffer.append("0.");
      for (int k = 0; k < -j; k++) {
        localStringBuffer.append('0');
      }
      localStringBuffer.append(str);
    }
    return localStringBuffer.toString();
  }
  
  public boolean isSet(DatatypeConstants.Field paramField)
  {
    if (paramField == null)
    {
      str = "javax.xml.datatype.Duration#isSet(DatatypeConstants.Field field)";
      throw new NullPointerException(DatatypeMessageFormatter.formatMessage(null, "FieldCannotBeNull", new Object[] { str }));
    }
    if (paramField == DatatypeConstants.YEARS) {
      return years != null;
    }
    if (paramField == DatatypeConstants.MONTHS) {
      return months != null;
    }
    if (paramField == DatatypeConstants.DAYS) {
      return days != null;
    }
    if (paramField == DatatypeConstants.HOURS) {
      return hours != null;
    }
    if (paramField == DatatypeConstants.MINUTES) {
      return minutes != null;
    }
    if (paramField == DatatypeConstants.SECONDS) {
      return seconds != null;
    }
    String str = "javax.xml.datatype.Duration#isSet(DatatypeConstants.Field field)";
    throw new IllegalArgumentException(DatatypeMessageFormatter.formatMessage(null, "UnknownField", new Object[] { str, paramField.toString() }));
  }
  
  public Number getField(DatatypeConstants.Field paramField)
  {
    if (paramField == null)
    {
      str = "javax.xml.datatype.Duration#isSet(DatatypeConstants.Field field) ";
      throw new NullPointerException(DatatypeMessageFormatter.formatMessage(null, "FieldCannotBeNull", new Object[] { str }));
    }
    if (paramField == DatatypeConstants.YEARS) {
      return years;
    }
    if (paramField == DatatypeConstants.MONTHS) {
      return months;
    }
    if (paramField == DatatypeConstants.DAYS) {
      return days;
    }
    if (paramField == DatatypeConstants.HOURS) {
      return hours;
    }
    if (paramField == DatatypeConstants.MINUTES) {
      return minutes;
    }
    if (paramField == DatatypeConstants.SECONDS) {
      return seconds;
    }
    String str = "javax.xml.datatype.Duration#(getSet(DatatypeConstants.Field field)";
    throw new IllegalArgumentException(DatatypeMessageFormatter.formatMessage(null, "UnknownField", new Object[] { str, paramField.toString() }));
  }
  
  public int getYears()
  {
    return getInt(DatatypeConstants.YEARS);
  }
  
  public int getMonths()
  {
    return getInt(DatatypeConstants.MONTHS);
  }
  
  public int getDays()
  {
    return getInt(DatatypeConstants.DAYS);
  }
  
  public int getHours()
  {
    return getInt(DatatypeConstants.HOURS);
  }
  
  public int getMinutes()
  {
    return getInt(DatatypeConstants.MINUTES);
  }
  
  public int getSeconds()
  {
    return getInt(DatatypeConstants.SECONDS);
  }
  
  private int getInt(DatatypeConstants.Field paramField)
  {
    Number localNumber = getField(paramField);
    if (localNumber == null) {
      return 0;
    }
    return localNumber.intValue();
  }
  
  public long getTimeInMillis(Calendar paramCalendar)
  {
    Calendar localCalendar = (Calendar)paramCalendar.clone();
    addTo(localCalendar);
    return getCalendarTimeInMillis(localCalendar) - getCalendarTimeInMillis(paramCalendar);
  }
  
  public long getTimeInMillis(Date paramDate)
  {
    GregorianCalendar localGregorianCalendar = new GregorianCalendar();
    localGregorianCalendar.setTime(paramDate);
    addTo(localGregorianCalendar);
    return getCalendarTimeInMillis(localGregorianCalendar) - paramDate.getTime();
  }
  
  public Duration normalizeWith(Calendar paramCalendar)
  {
    Calendar localCalendar = (Calendar)paramCalendar.clone();
    localCalendar.add(1, getYears() * signum);
    localCalendar.add(2, getMonths() * signum);
    localCalendar.add(5, getDays() * signum);
    long l = getCalendarTimeInMillis(localCalendar) - getCalendarTimeInMillis(paramCalendar);
    int i = (int)(l / 86400000L);
    return new DurationImpl(i >= 0, null, null, wrap(Math.abs(i)), (BigInteger)getField(DatatypeConstants.HOURS), (BigInteger)getField(DatatypeConstants.MINUTES), (BigDecimal)getField(DatatypeConstants.SECONDS));
  }
  
  public Duration multiply(int paramInt)
  {
    return multiply(BigDecimal.valueOf(paramInt));
  }
  
  public Duration multiply(BigDecimal paramBigDecimal)
  {
    BigDecimal localBigDecimal1 = ZERO;
    int i = paramBigDecimal.signum();
    paramBigDecimal = paramBigDecimal.abs();
    BigDecimal[] arrayOfBigDecimal = new BigDecimal[6];
    for (int j = 0; j < 5; j++)
    {
      BigDecimal localBigDecimal2 = getFieldAsBigDecimal(FIELDS[j]);
      localBigDecimal2 = localBigDecimal2.multiply(paramBigDecimal).add(localBigDecimal1);
      arrayOfBigDecimal[j] = localBigDecimal2.setScale(0, 1);
      localBigDecimal2 = localBigDecimal2.subtract(arrayOfBigDecimal[j]);
      if (j == 1)
      {
        if (localBigDecimal2.signum() != 0) {
          throw new IllegalStateException();
        }
        localBigDecimal1 = ZERO;
      }
      else
      {
        localBigDecimal1 = localBigDecimal2.multiply(FACTORS[j]);
      }
    }
    if (seconds != null) {
      arrayOfBigDecimal[5] = seconds.multiply(paramBigDecimal).add(localBigDecimal1);
    } else {
      arrayOfBigDecimal[5] = localBigDecimal1;
    }
    return new DurationImpl(signum * i >= 0, toBigInteger(arrayOfBigDecimal[0], null == years), toBigInteger(arrayOfBigDecimal[1], null == months), toBigInteger(arrayOfBigDecimal[2], null == days), toBigInteger(arrayOfBigDecimal[3], null == hours), toBigInteger(arrayOfBigDecimal[4], null == minutes), (arrayOfBigDecimal[5].signum() == 0) && (seconds == null) ? null : arrayOfBigDecimal[5]);
  }
  
  private BigDecimal getFieldAsBigDecimal(DatatypeConstants.Field paramField)
  {
    if (paramField == DatatypeConstants.SECONDS)
    {
      if (seconds != null) {
        return seconds;
      }
      return ZERO;
    }
    BigInteger localBigInteger = (BigInteger)getField(paramField);
    if (localBigInteger == null) {
      return ZERO;
    }
    return new BigDecimal(localBigInteger);
  }
  
  private static BigInteger toBigInteger(BigDecimal paramBigDecimal, boolean paramBoolean)
  {
    if ((paramBoolean) && (paramBigDecimal.signum() == 0)) {
      return null;
    }
    return paramBigDecimal.unscaledValue();
  }
  
  public Duration add(Duration paramDuration)
  {
    DurationImpl localDurationImpl = this;
    BigDecimal[] arrayOfBigDecimal = new BigDecimal[6];
    arrayOfBigDecimal[0] = sanitize((BigInteger)localDurationImpl.getField(DatatypeConstants.YEARS), localDurationImpl.getSign()).add(sanitize((BigInteger)paramDuration.getField(DatatypeConstants.YEARS), paramDuration.getSign()));
    arrayOfBigDecimal[1] = sanitize((BigInteger)localDurationImpl.getField(DatatypeConstants.MONTHS), localDurationImpl.getSign()).add(sanitize((BigInteger)paramDuration.getField(DatatypeConstants.MONTHS), paramDuration.getSign()));
    arrayOfBigDecimal[2] = sanitize((BigInteger)localDurationImpl.getField(DatatypeConstants.DAYS), localDurationImpl.getSign()).add(sanitize((BigInteger)paramDuration.getField(DatatypeConstants.DAYS), paramDuration.getSign()));
    arrayOfBigDecimal[3] = sanitize((BigInteger)localDurationImpl.getField(DatatypeConstants.HOURS), localDurationImpl.getSign()).add(sanitize((BigInteger)paramDuration.getField(DatatypeConstants.HOURS), paramDuration.getSign()));
    arrayOfBigDecimal[4] = sanitize((BigInteger)localDurationImpl.getField(DatatypeConstants.MINUTES), localDurationImpl.getSign()).add(sanitize((BigInteger)paramDuration.getField(DatatypeConstants.MINUTES), paramDuration.getSign()));
    arrayOfBigDecimal[5] = sanitize((BigDecimal)localDurationImpl.getField(DatatypeConstants.SECONDS), localDurationImpl.getSign()).add(sanitize((BigDecimal)paramDuration.getField(DatatypeConstants.SECONDS), paramDuration.getSign()));
    alignSigns(arrayOfBigDecimal, 0, 2);
    alignSigns(arrayOfBigDecimal, 2, 6);
    int i = 0;
    for (int j = 0; j < 6; j++)
    {
      if (i * arrayOfBigDecimal[j].signum() < 0) {
        throw new IllegalStateException();
      }
      if (i == 0) {
        i = arrayOfBigDecimal[j].signum();
      }
    }
    return new DurationImpl(i >= 0, toBigInteger(sanitize(arrayOfBigDecimal[0], i), (localDurationImpl.getField(DatatypeConstants.YEARS) == null) && (paramDuration.getField(DatatypeConstants.YEARS) == null)), toBigInteger(sanitize(arrayOfBigDecimal[1], i), (localDurationImpl.getField(DatatypeConstants.MONTHS) == null) && (paramDuration.getField(DatatypeConstants.MONTHS) == null)), toBigInteger(sanitize(arrayOfBigDecimal[2], i), (localDurationImpl.getField(DatatypeConstants.DAYS) == null) && (paramDuration.getField(DatatypeConstants.DAYS) == null)), toBigInteger(sanitize(arrayOfBigDecimal[3], i), (localDurationImpl.getField(DatatypeConstants.HOURS) == null) && (paramDuration.getField(DatatypeConstants.HOURS) == null)), toBigInteger(sanitize(arrayOfBigDecimal[4], i), (localDurationImpl.getField(DatatypeConstants.MINUTES) == null) && (paramDuration.getField(DatatypeConstants.MINUTES) == null)), (arrayOfBigDecimal[5].signum() == 0) && (localDurationImpl.getField(DatatypeConstants.SECONDS) == null) && (paramDuration.getField(DatatypeConstants.SECONDS) == null) ? null : sanitize(arrayOfBigDecimal[5], i));
  }
  
  private static void alignSigns(BigDecimal[] paramArrayOfBigDecimal, int paramInt1, int paramInt2)
  {
    int i;
    do
    {
      i = 0;
      int j = 0;
      for (int k = paramInt1; k < paramInt2; k++)
      {
        if (j * paramArrayOfBigDecimal[k].signum() < 0)
        {
          i = 1;
          BigDecimal localBigDecimal = paramArrayOfBigDecimal[k].abs().divide(FACTORS[(k - 1)], 0);
          if (paramArrayOfBigDecimal[k].signum() > 0) {
            localBigDecimal = localBigDecimal.negate();
          }
          paramArrayOfBigDecimal[(k - 1)] = paramArrayOfBigDecimal[(k - 1)].subtract(localBigDecimal);
          paramArrayOfBigDecimal[k] = paramArrayOfBigDecimal[k].add(localBigDecimal.multiply(FACTORS[(k - 1)]));
        }
        if (paramArrayOfBigDecimal[k].signum() != 0) {
          j = paramArrayOfBigDecimal[k].signum();
        }
      }
    } while (i != 0);
  }
  
  private static BigDecimal sanitize(BigInteger paramBigInteger, int paramInt)
  {
    if ((paramInt == 0) || (paramBigInteger == null)) {
      return ZERO;
    }
    if (paramInt > 0) {
      return new BigDecimal(paramBigInteger);
    }
    return new BigDecimal(paramBigInteger.negate());
  }
  
  static BigDecimal sanitize(BigDecimal paramBigDecimal, int paramInt)
  {
    if ((paramInt == 0) || (paramBigDecimal == null)) {
      return ZERO;
    }
    if (paramInt > 0) {
      return paramBigDecimal;
    }
    return paramBigDecimal.negate();
  }
  
  public Duration subtract(Duration paramDuration)
  {
    return add(paramDuration.negate());
  }
  
  public Duration negate()
  {
    return new DurationImpl(signum <= 0, years, months, days, hours, minutes, seconds);
  }
  
  public int signum()
  {
    return signum;
  }
  
  public void addTo(Calendar paramCalendar)
  {
    paramCalendar.add(1, getYears() * signum);
    paramCalendar.add(2, getMonths() * signum);
    paramCalendar.add(5, getDays() * signum);
    paramCalendar.add(10, getHours() * signum);
    paramCalendar.add(12, getMinutes() * signum);
    paramCalendar.add(13, getSeconds() * signum);
    if (seconds != null)
    {
      BigDecimal localBigDecimal = seconds.subtract(seconds.setScale(0, 1));
      int i = localBigDecimal.movePointRight(3).intValue();
      paramCalendar.add(14, i * signum);
    }
  }
  
  public void addTo(Date paramDate)
  {
    GregorianCalendar localGregorianCalendar = new GregorianCalendar();
    localGregorianCalendar.setTime(paramDate);
    addTo(localGregorianCalendar);
    paramDate.setTime(getCalendarTimeInMillis(localGregorianCalendar));
  }
  
  private static long getCalendarTimeInMillis(Calendar paramCalendar)
  {
    return paramCalendar.getTime().getTime();
  }
  
  private Object writeReplace()
    throws IOException
  {
    return new SerializedDuration(toString());
  }
}
