package org.apache.xerces.jaxp.datatype;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import org.apache.xerces.util.DatatypeMessageFormatter;

class XMLGregorianCalendarImpl
  extends XMLGregorianCalendar
  implements Serializable, Cloneable
{
  private static final long serialVersionUID = 3905403108073447394L;
  private BigInteger orig_eon;
  private int orig_year = Integer.MIN_VALUE;
  private int orig_month = Integer.MIN_VALUE;
  private int orig_day = Integer.MIN_VALUE;
  private int orig_hour = Integer.MIN_VALUE;
  private int orig_minute = Integer.MIN_VALUE;
  private int orig_second = Integer.MIN_VALUE;
  private BigDecimal orig_fracSeconds;
  private int orig_timezone = Integer.MIN_VALUE;
  private BigInteger eon = null;
  private int year = Integer.MIN_VALUE;
  private int month = Integer.MIN_VALUE;
  private int day = Integer.MIN_VALUE;
  private int timezone = Integer.MIN_VALUE;
  private int hour = Integer.MIN_VALUE;
  private int minute = Integer.MIN_VALUE;
  private int second = Integer.MIN_VALUE;
  private BigDecimal fractionalSecond = null;
  private static final BigInteger BILLION_B = BigInteger.valueOf(1000000000L);
  private static final int BILLION_I = 1000000000;
  private static final Date PURE_GREGORIAN_CHANGE = new Date(Long.MIN_VALUE);
  private static final int YEAR = 0;
  private static final int MONTH = 1;
  private static final int DAY = 2;
  private static final int HOUR = 3;
  private static final int MINUTE = 4;
  private static final int SECOND = 5;
  private static final int MILLISECOND = 6;
  private static final int TIMEZONE = 7;
  private static final int[] MIN_FIELD_VALUE = { Integer.MIN_VALUE, 1, 1, 0, 0, 0, 0, 64696 };
  private static final int[] MAX_FIELD_VALUE = { Integer.MAX_VALUE, 12, 31, 24, 59, 60, 999, 840 };
  private static final String[] FIELD_NAME = { "Year", "Month", "Day", "Hour", "Minute", "Second", "Millisecond", "Timezone" };
  public static final XMLGregorianCalendar LEAP_YEAR_DEFAULT = createDateTime(400, 1, 1, 0, 0, 0, Integer.MIN_VALUE, Integer.MIN_VALUE);
  private static final BigInteger FOUR = BigInteger.valueOf(4L);
  private static final BigInteger HUNDRED = BigInteger.valueOf(100L);
  private static final BigInteger FOUR_HUNDRED = BigInteger.valueOf(400L);
  private static final BigInteger SIXTY = BigInteger.valueOf(60L);
  private static final BigInteger TWENTY_FOUR = BigInteger.valueOf(24L);
  private static final BigInteger TWELVE = BigInteger.valueOf(12L);
  private static final BigDecimal DECIMAL_ZERO = BigDecimal.valueOf(0L);
  private static final BigDecimal DECIMAL_ONE = BigDecimal.valueOf(1L);
  private static final BigDecimal DECIMAL_SIXTY = BigDecimal.valueOf(60L);
  
  protected XMLGregorianCalendarImpl(String paramString)
    throws IllegalArgumentException
  {
    String str1 = null;
    String str2 = paramString;
    int i = str2.length();
    if (str2.indexOf('T') != -1)
    {
      str1 = "%Y-%M-%DT%h:%m:%s%z";
    }
    else if ((i >= 3) && (str2.charAt(2) == ':'))
    {
      str1 = "%h:%m:%s%z";
    }
    else if (str2.startsWith("--"))
    {
      if ((i >= 3) && (str2.charAt(2) == '-'))
      {
        str1 = "---%D%z";
      }
      else if ((i == 4) || ((i >= 6) && ((str2.charAt(4) == '+') || ((str2.charAt(4) == '-') && ((str2.charAt(5) == '-') || (i == 10))))))
      {
        str1 = "--%M--%z";
        Parser localParser1 = new Parser(str1, str2, null);
        try
        {
          localParser1.parse();
          if (!isValid()) {
            throw new IllegalArgumentException(DatatypeMessageFormatter.formatMessage(null, "InvalidXGCRepresentation", new Object[] { paramString }));
          }
          save();
          return;
        }
        catch (IllegalArgumentException localIllegalArgumentException)
        {
          str1 = "--%M%z";
        }
      }
      else
      {
        str1 = "--%M-%D%z";
      }
    }
    else
    {
      int j = 0;
      int k = str2.indexOf(':');
      if (k != -1) {
        i -= 6;
      }
      for (int m = 1; m < i; m++) {
        if (str2.charAt(m) == '-') {
          j++;
        }
      }
      if (j == 0) {
        str1 = "%Y%z";
      } else if (j == 1) {
        str1 = "%Y-%M%z";
      } else {
        str1 = "%Y-%M-%D%z";
      }
    }
    Parser localParser2 = new Parser(str1, str2, null);
    localParser2.parse();
    if (!isValid()) {
      throw new IllegalArgumentException(DatatypeMessageFormatter.formatMessage(null, "InvalidXGCRepresentation", new Object[] { paramString }));
    }
    save();
  }
  
  private void save()
  {
    orig_eon = eon;
    orig_year = year;
    orig_month = month;
    orig_day = day;
    orig_hour = hour;
    orig_minute = minute;
    orig_second = second;
    orig_fracSeconds = fractionalSecond;
    orig_timezone = timezone;
  }
  
  public XMLGregorianCalendarImpl() {}
  
  protected XMLGregorianCalendarImpl(BigInteger paramBigInteger, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, BigDecimal paramBigDecimal, int paramInt6)
  {
    setYear(paramBigInteger);
    setMonth(paramInt1);
    setDay(paramInt2);
    setTime(paramInt3, paramInt4, paramInt5, paramBigDecimal);
    setTimezone(paramInt6);
    if (!isValid()) {
      throw new IllegalArgumentException(DatatypeMessageFormatter.formatMessage(null, "InvalidXGCValue-fractional", new Object[] { paramBigInteger, new Integer(paramInt1), new Integer(paramInt2), new Integer(paramInt3), new Integer(paramInt4), new Integer(paramInt5), paramBigDecimal, new Integer(paramInt6) }));
    }
    save();
  }
  
  private XMLGregorianCalendarImpl(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8)
  {
    setYear(paramInt1);
    setMonth(paramInt2);
    setDay(paramInt3);
    setTime(paramInt4, paramInt5, paramInt6);
    setTimezone(paramInt8);
    BigDecimal localBigDecimal = null;
    if (paramInt7 != Integer.MIN_VALUE) {
      localBigDecimal = BigDecimal.valueOf(paramInt7, 3);
    }
    setFractionalSecond(localBigDecimal);
    if (!isValid()) {
      throw new IllegalArgumentException(DatatypeMessageFormatter.formatMessage(null, "InvalidXGCValue-milli", new Object[] { new Integer(paramInt1), new Integer(paramInt2), new Integer(paramInt3), new Integer(paramInt4), new Integer(paramInt5), new Integer(paramInt6), new Integer(paramInt7), new Integer(paramInt8) }));
    }
    save();
  }
  
  public XMLGregorianCalendarImpl(GregorianCalendar paramGregorianCalendar)
  {
    int i = paramGregorianCalendar.get(1);
    if (paramGregorianCalendar.get(0) == 0) {
      i = -i;
    }
    setYear(i);
    setMonth(paramGregorianCalendar.get(2) + 1);
    setDay(paramGregorianCalendar.get(5));
    setTime(paramGregorianCalendar.get(11), paramGregorianCalendar.get(12), paramGregorianCalendar.get(13), paramGregorianCalendar.get(14));
    int j = (paramGregorianCalendar.get(15) + paramGregorianCalendar.get(16)) / 60000;
    setTimezone(j);
    save();
  }
  
  public static XMLGregorianCalendar createDateTime(BigInteger paramBigInteger, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, BigDecimal paramBigDecimal, int paramInt6)
  {
    return new XMLGregorianCalendarImpl(paramBigInteger, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramBigDecimal, paramInt6);
  }
  
  public static XMLGregorianCalendar createDateTime(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
  {
    return new XMLGregorianCalendarImpl(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, Integer.MIN_VALUE, Integer.MIN_VALUE);
  }
  
  public static XMLGregorianCalendar createDateTime(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8)
  {
    return new XMLGregorianCalendarImpl(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8);
  }
  
  public static XMLGregorianCalendar createDate(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    return new XMLGregorianCalendarImpl(paramInt1, paramInt2, paramInt3, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, paramInt4);
  }
  
  public static XMLGregorianCalendar createTime(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    return new XMLGregorianCalendarImpl(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, paramInt1, paramInt2, paramInt3, Integer.MIN_VALUE, paramInt4);
  }
  
  public static XMLGregorianCalendar createTime(int paramInt1, int paramInt2, int paramInt3, BigDecimal paramBigDecimal, int paramInt4)
  {
    return new XMLGregorianCalendarImpl(null, Integer.MIN_VALUE, Integer.MIN_VALUE, paramInt1, paramInt2, paramInt3, paramBigDecimal, paramInt4);
  }
  
  public static XMLGregorianCalendar createTime(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    return new XMLGregorianCalendarImpl(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
  }
  
  public BigInteger getEon()
  {
    return eon;
  }
  
  public int getYear()
  {
    return year;
  }
  
  public BigInteger getEonAndYear()
  {
    if ((year != Integer.MIN_VALUE) && (eon != null)) {
      return eon.add(BigInteger.valueOf(year));
    }
    if ((year != Integer.MIN_VALUE) && (eon == null)) {
      return BigInteger.valueOf(year);
    }
    return null;
  }
  
  public int getMonth()
  {
    return month;
  }
  
  public int getDay()
  {
    return day;
  }
  
  public int getTimezone()
  {
    return timezone;
  }
  
  public int getHour()
  {
    return hour;
  }
  
  public int getMinute()
  {
    return minute;
  }
  
  public int getSecond()
  {
    return second;
  }
  
  private BigDecimal getSeconds()
  {
    if (second == Integer.MIN_VALUE) {
      return DECIMAL_ZERO;
    }
    BigDecimal localBigDecimal = BigDecimal.valueOf(second);
    if (fractionalSecond != null) {
      return localBigDecimal.add(fractionalSecond);
    }
    return localBigDecimal;
  }
  
  public int getMillisecond()
  {
    if (fractionalSecond == null) {
      return Integer.MIN_VALUE;
    }
    return fractionalSecond.movePointRight(3).intValue();
  }
  
  public BigDecimal getFractionalSecond()
  {
    return fractionalSecond;
  }
  
  public void setYear(BigInteger paramBigInteger)
  {
    if (paramBigInteger == null)
    {
      eon = null;
      year = Integer.MIN_VALUE;
    }
    else
    {
      BigInteger localBigInteger = paramBigInteger.remainder(BILLION_B);
      year = localBigInteger.intValue();
      setEon(paramBigInteger.subtract(localBigInteger));
    }
  }
  
  public void setYear(int paramInt)
  {
    if (paramInt == Integer.MIN_VALUE)
    {
      year = Integer.MIN_VALUE;
      eon = null;
    }
    else if (Math.abs(paramInt) < 1000000000)
    {
      year = paramInt;
      eon = null;
    }
    else
    {
      BigInteger localBigInteger1 = BigInteger.valueOf(paramInt);
      BigInteger localBigInteger2 = localBigInteger1.remainder(BILLION_B);
      year = localBigInteger2.intValue();
      setEon(localBigInteger1.subtract(localBigInteger2));
    }
  }
  
  private void setEon(BigInteger paramBigInteger)
  {
    if ((paramBigInteger != null) && (paramBigInteger.compareTo(BigInteger.ZERO) == 0)) {
      eon = null;
    } else {
      eon = paramBigInteger;
    }
  }
  
  public void setMonth(int paramInt)
  {
    checkFieldValueConstraint(1, paramInt);
    month = paramInt;
  }
  
  public void setDay(int paramInt)
  {
    checkFieldValueConstraint(2, paramInt);
    day = paramInt;
  }
  
  public void setTimezone(int paramInt)
  {
    checkFieldValueConstraint(7, paramInt);
    timezone = paramInt;
  }
  
  public void setTime(int paramInt1, int paramInt2, int paramInt3)
  {
    setTime(paramInt1, paramInt2, paramInt3, null);
  }
  
  private void checkFieldValueConstraint(int paramInt1, int paramInt2)
    throws IllegalArgumentException
  {
    if (((paramInt2 < MIN_FIELD_VALUE[paramInt1]) && (paramInt2 != Integer.MIN_VALUE)) || (paramInt2 > MAX_FIELD_VALUE[paramInt1])) {
      throw new IllegalArgumentException(DatatypeMessageFormatter.formatMessage(null, "InvalidFieldValue", new Object[] { new Integer(paramInt2), FIELD_NAME[paramInt1] }));
    }
  }
  
  public void setHour(int paramInt)
  {
    checkFieldValueConstraint(3, paramInt);
    hour = paramInt;
  }
  
  public void setMinute(int paramInt)
  {
    checkFieldValueConstraint(4, paramInt);
    minute = paramInt;
  }
  
  public void setSecond(int paramInt)
  {
    checkFieldValueConstraint(5, paramInt);
    second = paramInt;
  }
  
  public void setTime(int paramInt1, int paramInt2, int paramInt3, BigDecimal paramBigDecimal)
  {
    setHour(paramInt1);
    setMinute(paramInt2);
    setSecond(paramInt3);
    setFractionalSecond(paramBigDecimal);
  }
  
  public void setTime(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    setHour(paramInt1);
    setMinute(paramInt2);
    setSecond(paramInt3);
    setMillisecond(paramInt4);
  }
  
  public int compare(XMLGregorianCalendar paramXMLGregorianCalendar)
  {
    int i = 2;
    XMLGregorianCalendarImpl localXMLGregorianCalendarImpl = this;
    Object localObject = paramXMLGregorianCalendar;
    if (localXMLGregorianCalendarImpl.getTimezone() == ((XMLGregorianCalendar)localObject).getTimezone()) {
      return internalCompare(localXMLGregorianCalendarImpl, (XMLGregorianCalendar)localObject);
    }
    if ((localXMLGregorianCalendarImpl.getTimezone() != Integer.MIN_VALUE) && (((XMLGregorianCalendar)localObject).getTimezone() != Integer.MIN_VALUE))
    {
      localXMLGregorianCalendarImpl = (XMLGregorianCalendarImpl)localXMLGregorianCalendarImpl.normalize();
      localObject = (XMLGregorianCalendarImpl)((XMLGregorianCalendar)localObject).normalize();
      return internalCompare(localXMLGregorianCalendarImpl, (XMLGregorianCalendar)localObject);
    }
    if (localXMLGregorianCalendarImpl.getTimezone() != Integer.MIN_VALUE)
    {
      if (localXMLGregorianCalendarImpl.getTimezone() != 0) {
        localXMLGregorianCalendarImpl = (XMLGregorianCalendarImpl)localXMLGregorianCalendarImpl.normalize();
      }
      localXMLGregorianCalendar1 = normalizeToTimezone((XMLGregorianCalendar)localObject, 840);
      i = internalCompare(localXMLGregorianCalendarImpl, localXMLGregorianCalendar1);
      if (i == -1) {
        return i;
      }
      localXMLGregorianCalendar2 = normalizeToTimezone((XMLGregorianCalendar)localObject, 64696);
      i = internalCompare(localXMLGregorianCalendarImpl, localXMLGregorianCalendar2);
      if (i == 1) {
        return i;
      }
      return 2;
    }
    if (((XMLGregorianCalendar)localObject).getTimezone() != 0) {
      localObject = (XMLGregorianCalendarImpl)normalizeToTimezone((XMLGregorianCalendar)localObject, ((XMLGregorianCalendar)localObject).getTimezone());
    }
    XMLGregorianCalendar localXMLGregorianCalendar1 = normalizeToTimezone(localXMLGregorianCalendarImpl, 64696);
    i = internalCompare(localXMLGregorianCalendar1, (XMLGregorianCalendar)localObject);
    if (i == -1) {
      return i;
    }
    XMLGregorianCalendar localXMLGregorianCalendar2 = normalizeToTimezone(localXMLGregorianCalendarImpl, 840);
    i = internalCompare(localXMLGregorianCalendar2, (XMLGregorianCalendar)localObject);
    if (i == 1) {
      return i;
    }
    return 2;
  }
  
  public XMLGregorianCalendar normalize()
  {
    XMLGregorianCalendar localXMLGregorianCalendar = normalizeToTimezone(this, timezone);
    if (getTimezone() == Integer.MIN_VALUE) {
      localXMLGregorianCalendar.setTimezone(Integer.MIN_VALUE);
    }
    if (getMillisecond() == Integer.MIN_VALUE) {
      localXMLGregorianCalendar.setMillisecond(Integer.MIN_VALUE);
    }
    return localXMLGregorianCalendar;
  }
  
  private XMLGregorianCalendar normalizeToTimezone(XMLGregorianCalendar paramXMLGregorianCalendar, int paramInt)
  {
    int i = paramInt;
    XMLGregorianCalendar localXMLGregorianCalendar = (XMLGregorianCalendar)paramXMLGregorianCalendar.clone();
    i = -i;
    DurationImpl localDurationImpl = new DurationImpl(i >= 0, 0, 0, 0, 0, i < 0 ? -i : i, 0);
    localXMLGregorianCalendar.add(localDurationImpl);
    localXMLGregorianCalendar.setTimezone(0);
    return localXMLGregorianCalendar;
  }
  
  private static int internalCompare(XMLGregorianCalendar paramXMLGregorianCalendar1, XMLGregorianCalendar paramXMLGregorianCalendar2)
  {
    if (paramXMLGregorianCalendar1.getEon() == paramXMLGregorianCalendar2.getEon())
    {
      i = compareField(paramXMLGregorianCalendar1.getYear(), paramXMLGregorianCalendar2.getYear());
      if (i != 0) {
        return i;
      }
    }
    else
    {
      i = compareField(paramXMLGregorianCalendar1.getEonAndYear(), paramXMLGregorianCalendar2.getEonAndYear());
      if (i != 0) {
        return i;
      }
    }
    int i = compareField(paramXMLGregorianCalendar1.getMonth(), paramXMLGregorianCalendar2.getMonth());
    if (i != 0) {
      return i;
    }
    i = compareField(paramXMLGregorianCalendar1.getDay(), paramXMLGregorianCalendar2.getDay());
    if (i != 0) {
      return i;
    }
    i = compareField(paramXMLGregorianCalendar1.getHour(), paramXMLGregorianCalendar2.getHour());
    if (i != 0) {
      return i;
    }
    i = compareField(paramXMLGregorianCalendar1.getMinute(), paramXMLGregorianCalendar2.getMinute());
    if (i != 0) {
      return i;
    }
    i = compareField(paramXMLGregorianCalendar1.getSecond(), paramXMLGregorianCalendar2.getSecond());
    if (i != 0) {
      return i;
    }
    i = compareField(paramXMLGregorianCalendar1.getFractionalSecond(), paramXMLGregorianCalendar2.getFractionalSecond());
    return i;
  }
  
  private static int compareField(int paramInt1, int paramInt2)
  {
    if (paramInt1 == paramInt2) {
      return 0;
    }
    if ((paramInt1 == Integer.MIN_VALUE) || (paramInt2 == Integer.MIN_VALUE)) {
      return 2;
    }
    return paramInt1 < paramInt2 ? -1 : 1;
  }
  
  private static int compareField(BigInteger paramBigInteger1, BigInteger paramBigInteger2)
  {
    if (paramBigInteger1 == null) {
      return paramBigInteger2 == null ? 0 : 2;
    }
    if (paramBigInteger2 == null) {
      return 2;
    }
    return paramBigInteger1.compareTo(paramBigInteger2);
  }
  
  private static int compareField(BigDecimal paramBigDecimal1, BigDecimal paramBigDecimal2)
  {
    if (paramBigDecimal1 == paramBigDecimal2) {
      return 0;
    }
    if (paramBigDecimal1 == null) {
      paramBigDecimal1 = DECIMAL_ZERO;
    }
    if (paramBigDecimal2 == null) {
      paramBigDecimal2 = DECIMAL_ZERO;
    }
    return paramBigDecimal1.compareTo(paramBigDecimal2);
  }
  
  public boolean equals(Object paramObject)
  {
    if (paramObject == this) {
      return true;
    }
    if ((paramObject instanceof XMLGregorianCalendar)) {
      return compare((XMLGregorianCalendar)paramObject) == 0;
    }
    return false;
  }
  
  public int hashCode()
  {
    int i = getTimezone();
    if (i == Integer.MIN_VALUE) {
      i = 0;
    }
    Object localObject = this;
    if (i != 0) {
      localObject = normalizeToTimezone(this, getTimezone());
    }
    return ((XMLGregorianCalendar)localObject).getYear() + ((XMLGregorianCalendar)localObject).getMonth() + ((XMLGregorianCalendar)localObject).getDay() + ((XMLGregorianCalendar)localObject).getHour() + ((XMLGregorianCalendar)localObject).getMinute() + ((XMLGregorianCalendar)localObject).getSecond();
  }
  
  public static XMLGregorianCalendar parse(String paramString)
  {
    return new XMLGregorianCalendarImpl(paramString);
  }
  
  public String toXMLFormat()
  {
    QName localQName = getXMLSchemaType();
    String str = null;
    if (localQName == DatatypeConstants.DATETIME) {
      str = "%Y-%M-%DT%h:%m:%s%z";
    } else if (localQName == DatatypeConstants.DATE) {
      str = "%Y-%M-%D%z";
    } else if (localQName == DatatypeConstants.TIME) {
      str = "%h:%m:%s%z";
    } else if (localQName == DatatypeConstants.GMONTH) {
      str = "--%M--%z";
    } else if (localQName == DatatypeConstants.GDAY) {
      str = "---%D%z";
    } else if (localQName == DatatypeConstants.GYEAR) {
      str = "%Y%z";
    } else if (localQName == DatatypeConstants.GYEARMONTH) {
      str = "%Y-%M%z";
    } else if (localQName == DatatypeConstants.GMONTHDAY) {
      str = "--%M-%D%z";
    }
    return format(str);
  }
  
  public QName getXMLSchemaType()
  {
    if ((year != Integer.MIN_VALUE) && (month != Integer.MIN_VALUE) && (day != Integer.MIN_VALUE) && (hour != Integer.MIN_VALUE) && (minute != Integer.MIN_VALUE) && (second != Integer.MIN_VALUE)) {
      return DatatypeConstants.DATETIME;
    }
    if ((year != Integer.MIN_VALUE) && (month != Integer.MIN_VALUE) && (day != Integer.MIN_VALUE) && (hour == Integer.MIN_VALUE) && (minute == Integer.MIN_VALUE) && (second == Integer.MIN_VALUE)) {
      return DatatypeConstants.DATE;
    }
    if ((year == Integer.MIN_VALUE) && (month == Integer.MIN_VALUE) && (day == Integer.MIN_VALUE) && (hour != Integer.MIN_VALUE) && (minute != Integer.MIN_VALUE) && (second != Integer.MIN_VALUE)) {
      return DatatypeConstants.TIME;
    }
    if ((year != Integer.MIN_VALUE) && (month != Integer.MIN_VALUE) && (day == Integer.MIN_VALUE) && (hour == Integer.MIN_VALUE) && (minute == Integer.MIN_VALUE) && (second == Integer.MIN_VALUE)) {
      return DatatypeConstants.GYEARMONTH;
    }
    if ((year == Integer.MIN_VALUE) && (month != Integer.MIN_VALUE) && (day != Integer.MIN_VALUE) && (hour == Integer.MIN_VALUE) && (minute == Integer.MIN_VALUE) && (second == Integer.MIN_VALUE)) {
      return DatatypeConstants.GMONTHDAY;
    }
    if ((year != Integer.MIN_VALUE) && (month == Integer.MIN_VALUE) && (day == Integer.MIN_VALUE) && (hour == Integer.MIN_VALUE) && (minute == Integer.MIN_VALUE) && (second == Integer.MIN_VALUE)) {
      return DatatypeConstants.GYEAR;
    }
    if ((year == Integer.MIN_VALUE) && (month != Integer.MIN_VALUE) && (day == Integer.MIN_VALUE) && (hour == Integer.MIN_VALUE) && (minute == Integer.MIN_VALUE) && (second == Integer.MIN_VALUE)) {
      return DatatypeConstants.GMONTH;
    }
    if ((year == Integer.MIN_VALUE) && (month == Integer.MIN_VALUE) && (day != Integer.MIN_VALUE) && (hour == Integer.MIN_VALUE) && (minute == Integer.MIN_VALUE) && (second == Integer.MIN_VALUE)) {
      return DatatypeConstants.GDAY;
    }
    throw new IllegalStateException(getClass().getName() + "#getXMLSchemaType() :" + DatatypeMessageFormatter.formatMessage(null, "InvalidXGCFields", null));
  }
  
  public boolean isValid()
  {
    if ((month != Integer.MIN_VALUE) && (day != Integer.MIN_VALUE)) {
      if (year != Integer.MIN_VALUE)
      {
        if (eon == null)
        {
          if (day > maximumDayInMonthFor(year, month)) {
            return false;
          }
        }
        else if (day > maximumDayInMonthFor(getEonAndYear(), month)) {
          return false;
        }
      }
      else if (day > maximumDayInMonthFor(2000, month)) {
        return false;
      }
    }
    if ((hour == 24) && ((minute != 0) || (second != 0) || ((fractionalSecond != null) && (fractionalSecond.compareTo(DECIMAL_ZERO) != 0)))) {
      return false;
    }
    return (eon != null) || (year != 0);
  }
  
  public void add(Duration paramDuration)
  {
    boolean[] arrayOfBoolean = { false, false, false, false, false, false };
    int i = paramDuration.getSign();
    int j = getMonth();
    if (j == Integer.MIN_VALUE)
    {
      j = MIN_FIELD_VALUE[1];
      arrayOfBoolean[1] = true;
    }
    BigInteger localBigInteger1 = sanitize(paramDuration.getField(DatatypeConstants.MONTHS), i);
    BigInteger localBigInteger2 = BigInteger.valueOf(j).add(localBigInteger1);
    setMonth(localBigInteger2.subtract(BigInteger.ONE).mod(TWELVE).intValue() + 1);
    BigInteger localBigInteger3 = new BigDecimal(localBigInteger2.subtract(BigInteger.ONE)).divide(new BigDecimal(TWELVE), 3).toBigInteger();
    BigInteger localBigInteger4 = getEonAndYear();
    if (localBigInteger4 == null)
    {
      arrayOfBoolean[0] = true;
      localBigInteger4 = BigInteger.ZERO;
    }
    BigInteger localBigInteger5 = sanitize(paramDuration.getField(DatatypeConstants.YEARS), i);
    BigInteger localBigInteger6 = localBigInteger4.add(localBigInteger5).add(localBigInteger3);
    setYear(localBigInteger6);
    BigDecimal localBigDecimal1;
    if (getSecond() == Integer.MIN_VALUE)
    {
      arrayOfBoolean[5] = true;
      localBigDecimal1 = DECIMAL_ZERO;
    }
    else
    {
      localBigDecimal1 = getSeconds();
    }
    BigDecimal localBigDecimal2 = DurationImpl.sanitize((BigDecimal)paramDuration.getField(DatatypeConstants.SECONDS), i);
    BigDecimal localBigDecimal3 = localBigDecimal1.add(localBigDecimal2);
    BigDecimal localBigDecimal4 = new BigDecimal(new BigDecimal(localBigDecimal3.toBigInteger()).divide(DECIMAL_SIXTY, 3).toBigInteger());
    BigDecimal localBigDecimal5 = localBigDecimal3.subtract(localBigDecimal4.multiply(DECIMAL_SIXTY));
    localBigInteger3 = localBigDecimal4.toBigInteger();
    setSecond(localBigDecimal5.intValue());
    BigDecimal localBigDecimal6 = localBigDecimal5.subtract(new BigDecimal(BigInteger.valueOf(getSecond())));
    if (localBigDecimal6.compareTo(DECIMAL_ZERO) < 0)
    {
      setFractionalSecond(DECIMAL_ONE.add(localBigDecimal6));
      if (getSecond() == 0)
      {
        setSecond(59);
        localBigInteger3 = localBigInteger3.subtract(BigInteger.ONE);
      }
      else
      {
        setSecond(getSecond() - 1);
      }
    }
    else
    {
      setFractionalSecond(localBigDecimal6);
    }
    int k = getMinute();
    if (k == Integer.MIN_VALUE)
    {
      arrayOfBoolean[4] = true;
      k = MIN_FIELD_VALUE[4];
    }
    BigInteger localBigInteger7 = sanitize(paramDuration.getField(DatatypeConstants.MINUTES), i);
    localBigInteger2 = BigInteger.valueOf(k).add(localBigInteger7).add(localBigInteger3);
    setMinute(localBigInteger2.mod(SIXTY).intValue());
    localBigInteger3 = new BigDecimal(localBigInteger2).divide(DECIMAL_SIXTY, 3).toBigInteger();
    int m = getHour();
    if (m == Integer.MIN_VALUE)
    {
      arrayOfBoolean[3] = true;
      m = MIN_FIELD_VALUE[3];
    }
    BigInteger localBigInteger8 = sanitize(paramDuration.getField(DatatypeConstants.HOURS), i);
    localBigInteger2 = BigInteger.valueOf(m).add(localBigInteger8).add(localBigInteger3);
    setHour(localBigInteger2.mod(TWENTY_FOUR).intValue());
    localBigInteger3 = new BigDecimal(localBigInteger2).divide(new BigDecimal(TWENTY_FOUR), 3).toBigInteger();
    int n = getDay();
    if (n == Integer.MIN_VALUE)
    {
      arrayOfBoolean[2] = true;
      n = MIN_FIELD_VALUE[2];
    }
    BigInteger localBigInteger10 = sanitize(paramDuration.getField(DatatypeConstants.DAYS), i);
    int i1 = maximumDayInMonthFor(getEonAndYear(), getMonth());
    BigInteger localBigInteger9;
    if (n > i1) {
      localBigInteger9 = BigInteger.valueOf(i1);
    } else if (n < 1) {
      localBigInteger9 = BigInteger.ONE;
    } else {
      localBigInteger9 = BigInteger.valueOf(n);
    }
    BigInteger localBigInteger11 = localBigInteger9.add(localBigInteger10).add(localBigInteger3);
    for (;;)
    {
      int i2;
      if (localBigInteger11.compareTo(BigInteger.ONE) < 0)
      {
        BigInteger localBigInteger12 = null;
        if (month >= 2) {
          localBigInteger12 = BigInteger.valueOf(maximumDayInMonthFor(getEonAndYear(), getMonth() - 1));
        } else {
          localBigInteger12 = BigInteger.valueOf(maximumDayInMonthFor(getEonAndYear().subtract(BigInteger.valueOf(1L)), 12));
        }
        localBigInteger11 = localBigInteger11.add(localBigInteger12);
        i2 = -1;
      }
      else
      {
        if (localBigInteger11.compareTo(BigInteger.valueOf(maximumDayInMonthFor(getEonAndYear(), getMonth()))) <= 0) {
          break;
        }
        localBigInteger11 = localBigInteger11.add(BigInteger.valueOf(-maximumDayInMonthFor(getEonAndYear(), getMonth())));
        i2 = 1;
      }
      int i3 = getMonth() + i2;
      i4 = (i3 - 1) % 12;
      int i5;
      if (i4 < 0)
      {
        i4 = 12 + i4 + 1;
        i5 = BigDecimal.valueOf(i3 - 1).divide(new BigDecimal(TWELVE), 0).intValue();
      }
      else
      {
        i5 = (i3 - 1) / 12;
        i4++;
      }
      setMonth(i4);
      if (i5 != 0) {
        setYear(getEonAndYear().add(BigInteger.valueOf(i5)));
      }
    }
    setDay(localBigInteger11.intValue());
    for (int i4 = 0; i4 <= 5; i4++) {
      if (arrayOfBoolean[i4] != 0) {
        switch (i4)
        {
        case 0: 
          setYear(Integer.MIN_VALUE);
          break;
        case 1: 
          setMonth(Integer.MIN_VALUE);
          break;
        case 2: 
          setDay(Integer.MIN_VALUE);
          break;
        case 3: 
          setHour(Integer.MIN_VALUE);
          break;
        case 4: 
          setMinute(Integer.MIN_VALUE);
          break;
        case 5: 
          setSecond(Integer.MIN_VALUE);
          setFractionalSecond(null);
        }
      }
    }
  }
  
  private static int maximumDayInMonthFor(BigInteger paramBigInteger, int paramInt)
  {
    if (paramInt != 2) {
      return DaysInMonth.table[paramInt];
    }
    if ((paramBigInteger.mod(FOUR_HUNDRED).equals(BigInteger.ZERO)) || ((!paramBigInteger.mod(HUNDRED).equals(BigInteger.ZERO)) && (paramBigInteger.mod(FOUR).equals(BigInteger.ZERO)))) {
      return 29;
    }
    return DaysInMonth.table[paramInt];
  }
  
  private static int maximumDayInMonthFor(int paramInt1, int paramInt2)
  {
    if (paramInt2 != 2) {
      return DaysInMonth.table[paramInt2];
    }
    if ((paramInt1 % 400 == 0) || ((paramInt1 % 100 != 0) && (paramInt1 % 4 == 0))) {
      return 29;
    }
    return DaysInMonth.table[2];
  }
  
  public GregorianCalendar toGregorianCalendar()
  {
    GregorianCalendar localGregorianCalendar = null;
    TimeZone localTimeZone = getTimeZone(Integer.MIN_VALUE);
    Locale localLocale = Locale.getDefault();
    localGregorianCalendar = new GregorianCalendar(localTimeZone, localLocale);
    localGregorianCalendar.clear();
    localGregorianCalendar.setGregorianChange(PURE_GREGORIAN_CHANGE);
    if (year != Integer.MIN_VALUE) {
      if (eon == null)
      {
        localGregorianCalendar.set(0, year < 0 ? 0 : 1);
        localGregorianCalendar.set(1, Math.abs(year));
      }
      else
      {
        BigInteger localBigInteger = getEonAndYear();
        localGregorianCalendar.set(0, localBigInteger.signum() == -1 ? 0 : 1);
        localGregorianCalendar.set(1, localBigInteger.abs().intValue());
      }
    }
    if (month != Integer.MIN_VALUE) {
      localGregorianCalendar.set(2, month - 1);
    }
    if (day != Integer.MIN_VALUE) {
      localGregorianCalendar.set(5, day);
    }
    if (hour != Integer.MIN_VALUE) {
      localGregorianCalendar.set(11, hour);
    }
    if (minute != Integer.MIN_VALUE) {
      localGregorianCalendar.set(12, minute);
    }
    if (second != Integer.MIN_VALUE) {
      localGregorianCalendar.set(13, second);
    }
    if (fractionalSecond != null) {
      localGregorianCalendar.set(14, getMillisecond());
    }
    return localGregorianCalendar;
  }
  
  public GregorianCalendar toGregorianCalendar(TimeZone paramTimeZone, Locale paramLocale, XMLGregorianCalendar paramXMLGregorianCalendar)
  {
    GregorianCalendar localGregorianCalendar = null;
    TimeZone localTimeZone = paramTimeZone;
    if (localTimeZone == null)
    {
      int i = Integer.MIN_VALUE;
      if (paramXMLGregorianCalendar != null) {
        i = paramXMLGregorianCalendar.getTimezone();
      }
      localTimeZone = getTimeZone(i);
    }
    if (paramLocale == null) {
      paramLocale = Locale.getDefault();
    }
    localGregorianCalendar = new GregorianCalendar(localTimeZone, paramLocale);
    localGregorianCalendar.clear();
    localGregorianCalendar.setGregorianChange(PURE_GREGORIAN_CHANGE);
    int j;
    if (year != Integer.MIN_VALUE)
    {
      if (eon == null)
      {
        localGregorianCalendar.set(0, year < 0 ? 0 : 1);
        localGregorianCalendar.set(1, Math.abs(year));
      }
      else
      {
        BigInteger localBigInteger1 = getEonAndYear();
        localGregorianCalendar.set(0, localBigInteger1.signum() == -1 ? 0 : 1);
        localGregorianCalendar.set(1, localBigInteger1.abs().intValue());
      }
    }
    else if (paramXMLGregorianCalendar != null)
    {
      j = paramXMLGregorianCalendar.getYear();
      if (j != Integer.MIN_VALUE) {
        if (paramXMLGregorianCalendar.getEon() == null)
        {
          localGregorianCalendar.set(0, j < 0 ? 0 : 1);
          localGregorianCalendar.set(1, Math.abs(j));
        }
        else
        {
          BigInteger localBigInteger2 = paramXMLGregorianCalendar.getEonAndYear();
          localGregorianCalendar.set(0, localBigInteger2.signum() == -1 ? 0 : 1);
          localGregorianCalendar.set(1, localBigInteger2.abs().intValue());
        }
      }
    }
    if (month != Integer.MIN_VALUE)
    {
      localGregorianCalendar.set(2, month - 1);
    }
    else
    {
      j = paramXMLGregorianCalendar != null ? paramXMLGregorianCalendar.getMonth() : Integer.MIN_VALUE;
      if (j != Integer.MIN_VALUE) {
        localGregorianCalendar.set(2, j - 1);
      }
    }
    if (day != Integer.MIN_VALUE)
    {
      localGregorianCalendar.set(5, day);
    }
    else
    {
      j = paramXMLGregorianCalendar != null ? paramXMLGregorianCalendar.getDay() : Integer.MIN_VALUE;
      if (j != Integer.MIN_VALUE) {
        localGregorianCalendar.set(5, j);
      }
    }
    if (hour != Integer.MIN_VALUE)
    {
      localGregorianCalendar.set(11, hour);
    }
    else
    {
      j = paramXMLGregorianCalendar != null ? paramXMLGregorianCalendar.getHour() : Integer.MIN_VALUE;
      if (j != Integer.MIN_VALUE) {
        localGregorianCalendar.set(11, j);
      }
    }
    if (minute != Integer.MIN_VALUE)
    {
      localGregorianCalendar.set(12, minute);
    }
    else
    {
      j = paramXMLGregorianCalendar != null ? paramXMLGregorianCalendar.getMinute() : Integer.MIN_VALUE;
      if (j != Integer.MIN_VALUE) {
        localGregorianCalendar.set(12, j);
      }
    }
    if (second != Integer.MIN_VALUE)
    {
      localGregorianCalendar.set(13, second);
    }
    else
    {
      j = paramXMLGregorianCalendar != null ? paramXMLGregorianCalendar.getSecond() : Integer.MIN_VALUE;
      if (j != Integer.MIN_VALUE) {
        localGregorianCalendar.set(13, j);
      }
    }
    if (fractionalSecond != null)
    {
      localGregorianCalendar.set(14, getMillisecond());
    }
    else
    {
      Object localObject = paramXMLGregorianCalendar != null ? paramXMLGregorianCalendar.getFractionalSecond() : null;
      if (localObject != null) {
        localGregorianCalendar.set(14, paramXMLGregorianCalendar.getMillisecond());
      }
    }
    return localGregorianCalendar;
  }
  
  public TimeZone getTimeZone(int paramInt)
  {
    TimeZone localTimeZone = null;
    int i = getTimezone();
    if (i == Integer.MIN_VALUE) {
      i = paramInt;
    }
    if (i == Integer.MIN_VALUE)
    {
      localTimeZone = TimeZone.getDefault();
    }
    else
    {
      char c = i < 0 ? '-' : '+';
      if (c == '-') {
        i = -i;
      }
      int j = i / 60;
      int k = i - j * 60;
      StringBuffer localStringBuffer = new StringBuffer(8);
      localStringBuffer.append("GMT");
      localStringBuffer.append(c);
      localStringBuffer.append(j);
      if (k != 0)
      {
        if (k < 10) {
          localStringBuffer.append('0');
        }
        localStringBuffer.append(k);
      }
      localTimeZone = TimeZone.getTimeZone(localStringBuffer.toString());
    }
    return localTimeZone;
  }
  
  public Object clone()
  {
    return new XMLGregorianCalendarImpl(getEonAndYear(), month, day, hour, minute, second, fractionalSecond, timezone);
  }
  
  public void clear()
  {
    eon = null;
    year = Integer.MIN_VALUE;
    month = Integer.MIN_VALUE;
    day = Integer.MIN_VALUE;
    timezone = Integer.MIN_VALUE;
    hour = Integer.MIN_VALUE;
    minute = Integer.MIN_VALUE;
    second = Integer.MIN_VALUE;
    fractionalSecond = null;
  }
  
  public void setMillisecond(int paramInt)
  {
    if (paramInt == Integer.MIN_VALUE)
    {
      fractionalSecond = null;
    }
    else
    {
      checkFieldValueConstraint(6, paramInt);
      fractionalSecond = BigDecimal.valueOf(paramInt, 3);
    }
  }
  
  public void setFractionalSecond(BigDecimal paramBigDecimal)
  {
    if ((paramBigDecimal != null) && ((paramBigDecimal.compareTo(DECIMAL_ZERO) < 0) || (paramBigDecimal.compareTo(DECIMAL_ONE) > 0))) {
      throw new IllegalArgumentException(DatatypeMessageFormatter.formatMessage(null, "InvalidFractional", new Object[] { paramBigDecimal }));
    }
    fractionalSecond = paramBigDecimal;
  }
  
  private static boolean isDigit(char paramChar)
  {
    return ('0' <= paramChar) && (paramChar <= '9');
  }
  
  private String format(String paramString)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    int i = 0;
    int j = paramString.length();
    while (i < j)
    {
      char c = paramString.charAt(i++);
      if (c != '%') {
        localStringBuffer.append(c);
      } else {
        switch (paramString.charAt(i++))
        {
        case 'Y': 
          if (eon == null)
          {
            int k = year;
            if (k < 0)
            {
              localStringBuffer.append('-');
              k = -year;
            }
            printNumber(localStringBuffer, k, 4);
          }
          else
          {
            printNumber(localStringBuffer, getEonAndYear(), 4);
          }
          break;
        case 'M': 
          printNumber(localStringBuffer, getMonth(), 2);
          break;
        case 'D': 
          printNumber(localStringBuffer, getDay(), 2);
          break;
        case 'h': 
          printNumber(localStringBuffer, getHour(), 2);
          break;
        case 'm': 
          printNumber(localStringBuffer, getMinute(), 2);
          break;
        case 's': 
          printNumber(localStringBuffer, getSecond(), 2);
          if (getFractionalSecond() != null)
          {
            String str = toString(getFractionalSecond());
            localStringBuffer.append(str.substring(1, str.length()));
          }
          break;
        case 'z': 
          int m = getTimezone();
          if (m == 0)
          {
            localStringBuffer.append('Z');
          }
          else if (m != Integer.MIN_VALUE)
          {
            if (m < 0)
            {
              localStringBuffer.append('-');
              m *= -1;
            }
            else
            {
              localStringBuffer.append('+');
            }
            printNumber(localStringBuffer, m / 60, 2);
            localStringBuffer.append(':');
            printNumber(localStringBuffer, m % 60, 2);
          }
          break;
        default: 
          throw new InternalError();
        }
      }
    }
    return localStringBuffer.toString();
  }
  
  private void printNumber(StringBuffer paramStringBuffer, int paramInt1, int paramInt2)
  {
    String str = String.valueOf(paramInt1);
    for (int i = str.length(); i < paramInt2; i++) {
      paramStringBuffer.append('0');
    }
    paramStringBuffer.append(str);
  }
  
  private void printNumber(StringBuffer paramStringBuffer, BigInteger paramBigInteger, int paramInt)
  {
    String str = paramBigInteger.toString();
    for (int i = str.length(); i < paramInt; i++) {
      paramStringBuffer.append('0');
    }
    paramStringBuffer.append(str);
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
  
  static BigInteger sanitize(Number paramNumber, int paramInt)
  {
    if ((paramInt == 0) || (paramNumber == null)) {
      return BigInteger.ZERO;
    }
    return paramInt < 0 ? ((BigInteger)paramNumber).negate() : (BigInteger)paramNumber;
  }
  
  public void reset()
  {
    eon = orig_eon;
    year = orig_year;
    month = orig_month;
    day = orig_day;
    hour = orig_hour;
    minute = orig_minute;
    second = orig_second;
    fractionalSecond = orig_fracSeconds;
    timezone = orig_timezone;
  }
  
  private Object writeReplace()
    throws IOException
  {
    return new SerializedXMLGregorianCalendar(toXMLFormat());
  }
  
  private final class Parser
  {
    private final String format;
    private final String value;
    private final int flen;
    private final int vlen;
    private int fidx;
    private int vidx;
    
    private Parser(String paramString1, String paramString2)
    {
      format = paramString1;
      value = paramString2;
      flen = paramString1.length();
      vlen = paramString2.length();
    }
    
    public void parse()
      throws IllegalArgumentException
    {
      while (fidx < flen)
      {
        char c = format.charAt(fidx++);
        if (c != '%') {
          skip(c);
        } else {
          switch (format.charAt(fidx++))
          {
          case 'Y': 
            parseYear();
            break;
          case 'M': 
            setMonth(parseInt(2, 2));
            break;
          case 'D': 
            setDay(parseInt(2, 2));
            break;
          case 'h': 
            setHour(parseInt(2, 2));
            break;
          case 'm': 
            setMinute(parseInt(2, 2));
            break;
          case 's': 
            setSecond(parseInt(2, 2));
            if (peek() == '.') {
              setFractionalSecond(parseBigDecimal());
            }
            break;
          case 'z': 
            int i = peek();
            if (i == 90)
            {
              vidx += 1;
              setTimezone(0);
            }
            else if ((i == 43) || (i == 45))
            {
              vidx += 1;
              int j = parseInt(2, 2);
              skip(':');
              int k = parseInt(2, 2);
              setTimezone((j * 60 + k) * (i == 43 ? 1 : -1));
            }
            break;
          default: 
            throw new InternalError();
          }
        }
      }
      if (vidx != vlen) {
        throw new IllegalArgumentException(value);
      }
    }
    
    private char peek()
      throws IllegalArgumentException
    {
      if (vidx == vlen) {
        return 65535;
      }
      return value.charAt(vidx);
    }
    
    private char read()
      throws IllegalArgumentException
    {
      if (vidx == vlen) {
        throw new IllegalArgumentException(value);
      }
      return value.charAt(vidx++);
    }
    
    private void skip(char paramChar)
      throws IllegalArgumentException
    {
      if (read() != paramChar) {
        throw new IllegalArgumentException(value);
      }
    }
    
    private void parseYear()
      throws IllegalArgumentException
    {
      int i = vidx;
      int j = 0;
      if (peek() == '-')
      {
        vidx += 1;
        j = 1;
      }
      while (XMLGregorianCalendarImpl.isDigit(peek())) {
        vidx += 1;
      }
      int k = vidx - i - j;
      if (k < 4) {
        throw new IllegalArgumentException(value);
      }
      String str = value.substring(i, vidx);
      if (k < 10) {
        setYear(Integer.parseInt(str));
      } else {
        setYear(new BigInteger(str));
      }
    }
    
    private int parseInt(int paramInt1, int paramInt2)
      throws IllegalArgumentException
    {
      int i = vidx;
      while ((XMLGregorianCalendarImpl.isDigit(peek())) && (vidx - i < paramInt2)) {
        vidx += 1;
      }
      if (vidx - i < paramInt1) {
        throw new IllegalArgumentException(value);
      }
      return Integer.parseInt(value.substring(i, vidx));
    }
    
    private BigDecimal parseBigDecimal()
      throws IllegalArgumentException
    {
      int i = vidx;
      if (peek() == '.') {
        vidx += 1;
      }
      while (XMLGregorianCalendarImpl.isDigit(peek())) {
        vidx += 1;
      }
      return new BigDecimal(value.substring(i, vidx));
    }
    
    Parser(String paramString1, String paramString2, XMLGregorianCalendarImpl.1 param1)
    {
      this(paramString1, paramString2);
    }
  }
  
  private static class DaysInMonth
  {
    private static final int[] table = { 0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
    
    private DaysInMonth() {}
  }
}
