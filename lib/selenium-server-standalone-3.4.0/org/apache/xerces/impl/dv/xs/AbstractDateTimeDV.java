package org.apache.xerces.impl.dv.xs;

import java.math.BigDecimal;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.xerces.jaxp.datatype.DatatypeFactoryImpl;
import org.apache.xerces.xs.datatypes.XSDateTime;

public abstract class AbstractDateTimeDV
  extends TypeValidator
{
  private static final boolean DEBUG = false;
  protected static final int YEAR = 2000;
  protected static final int MONTH = 1;
  protected static final int DAY = 1;
  protected static final DatatypeFactory datatypeFactory = new DatatypeFactoryImpl();
  
  public AbstractDateTimeDV() {}
  
  public short getAllowedFacets()
  {
    return 2552;
  }
  
  public boolean isIdentical(Object paramObject1, Object paramObject2)
  {
    if ((!(paramObject1 instanceof DateTimeData)) || (!(paramObject2 instanceof DateTimeData))) {
      return false;
    }
    DateTimeData localDateTimeData1 = (DateTimeData)paramObject1;
    DateTimeData localDateTimeData2 = (DateTimeData)paramObject2;
    if ((timezoneHr == timezoneHr) && (timezoneMin == timezoneMin)) {
      return localDateTimeData1.equals(localDateTimeData2);
    }
    return false;
  }
  
  public int compare(Object paramObject1, Object paramObject2)
  {
    return compareDates((DateTimeData)paramObject1, (DateTimeData)paramObject2, true);
  }
  
  protected short compareDates(DateTimeData paramDateTimeData1, DateTimeData paramDateTimeData2, boolean paramBoolean)
  {
    if (utc == utc) {
      return compareOrder(paramDateTimeData1, paramDateTimeData2);
    }
    DateTimeData localDateTimeData = new DateTimeData(null, this);
    short s1;
    short s2;
    if (utc == 90)
    {
      cloneDate(paramDateTimeData2, localDateTimeData);
      timezoneHr = 14;
      timezoneMin = 0;
      utc = 43;
      normalize(localDateTimeData);
      s1 = compareOrder(paramDateTimeData1, localDateTimeData);
      if (s1 == -1) {
        return s1;
      }
      cloneDate(paramDateTimeData2, localDateTimeData);
      timezoneHr = -14;
      timezoneMin = 0;
      utc = 45;
      normalize(localDateTimeData);
      s2 = compareOrder(paramDateTimeData1, localDateTimeData);
      if (s2 == 1) {
        return s2;
      }
      return 2;
    }
    if (utc == 90)
    {
      cloneDate(paramDateTimeData1, localDateTimeData);
      timezoneHr = -14;
      timezoneMin = 0;
      utc = 45;
      normalize(localDateTimeData);
      s1 = compareOrder(localDateTimeData, paramDateTimeData2);
      if (s1 == -1) {
        return s1;
      }
      cloneDate(paramDateTimeData1, localDateTimeData);
      timezoneHr = 14;
      timezoneMin = 0;
      utc = 43;
      normalize(localDateTimeData);
      s2 = compareOrder(localDateTimeData, paramDateTimeData2);
      if (s2 == 1) {
        return s2;
      }
      return 2;
    }
    return 2;
  }
  
  protected short compareOrder(DateTimeData paramDateTimeData1, DateTimeData paramDateTimeData2)
  {
    if (position < 1)
    {
      if (year < year) {
        return -1;
      }
      if (year > year) {
        return 1;
      }
    }
    if (position < 2)
    {
      if (month < month) {
        return -1;
      }
      if (month > month) {
        return 1;
      }
    }
    if (day < day) {
      return -1;
    }
    if (day > day) {
      return 1;
    }
    if (hour < hour) {
      return -1;
    }
    if (hour > hour) {
      return 1;
    }
    if (minute < minute) {
      return -1;
    }
    if (minute > minute) {
      return 1;
    }
    if (second < second) {
      return -1;
    }
    if (second > second) {
      return 1;
    }
    if (utc < utc) {
      return -1;
    }
    if (utc > utc) {
      return 1;
    }
    return 0;
  }
  
  protected void getTime(String paramString, int paramInt1, int paramInt2, DateTimeData paramDateTimeData)
    throws RuntimeException
  {
    int i = paramInt1 + 2;
    hour = parseInt(paramString, paramInt1, i);
    if (paramString.charAt(i++) != ':') {
      throw new RuntimeException("Error in parsing time zone");
    }
    paramInt1 = i;
    i += 2;
    minute = parseInt(paramString, paramInt1, i);
    if (paramString.charAt(i++) != ':') {
      throw new RuntimeException("Error in parsing time zone");
    }
    int j = findUTCSign(paramString, paramInt1, paramInt2);
    paramInt1 = i;
    i = j < 0 ? paramInt2 : j;
    second = parseSecond(paramString, paramInt1, i);
    if (j > 0) {
      getTimeZone(paramString, paramDateTimeData, j, paramInt2);
    }
  }
  
  protected int getDate(String paramString, int paramInt1, int paramInt2, DateTimeData paramDateTimeData)
    throws RuntimeException
  {
    paramInt1 = getYearMonth(paramString, paramInt1, paramInt2, paramDateTimeData);
    if (paramString.charAt(paramInt1++) != '-') {
      throw new RuntimeException("CCYY-MM must be followed by '-' sign");
    }
    int i = paramInt1 + 2;
    day = parseInt(paramString, paramInt1, i);
    return i;
  }
  
  protected int getYearMonth(String paramString, int paramInt1, int paramInt2, DateTimeData paramDateTimeData)
    throws RuntimeException
  {
    if (paramString.charAt(0) == '-') {
      paramInt1++;
    }
    int i = indexOf(paramString, paramInt1, paramInt2, '-');
    if (i == -1) {
      throw new RuntimeException("Year separator is missing or misplaced");
    }
    int j = i - paramInt1;
    if (j < 4) {
      throw new RuntimeException("Year must have 'CCYY' format");
    }
    if ((j > 4) && (paramString.charAt(paramInt1) == '0')) {
      throw new RuntimeException("Leading zeros are required if the year value would otherwise have fewer than four digits; otherwise they are forbidden");
    }
    year = parseIntYear(paramString, i);
    if (paramString.charAt(i) != '-') {
      throw new RuntimeException("CCYY must be followed by '-' sign");
    }
    i++;
    paramInt1 = i;
    i = paramInt1 + 2;
    month = parseInt(paramString, paramInt1, i);
    return i;
  }
  
  protected void parseTimeZone(String paramString, int paramInt1, int paramInt2, DateTimeData paramDateTimeData)
    throws RuntimeException
  {
    if (paramInt1 < paramInt2)
    {
      if (!isNextCharUTCSign(paramString, paramInt1, paramInt2)) {
        throw new RuntimeException("Error in month parsing");
      }
      getTimeZone(paramString, paramDateTimeData, paramInt1, paramInt2);
    }
  }
  
  protected void getTimeZone(String paramString, DateTimeData paramDateTimeData, int paramInt1, int paramInt2)
    throws RuntimeException
  {
    utc = paramString.charAt(paramInt1);
    if (paramString.charAt(paramInt1) == 'Z')
    {
      if (paramInt2 > ++paramInt1) {
        throw new RuntimeException("Error in parsing time zone");
      }
      return;
    }
    if (paramInt1 <= paramInt2 - 6)
    {
      int i = paramString.charAt(paramInt1) == '-' ? -1 : 1;
      paramInt1++;
      int j = paramInt1 + 2;
      timezoneHr = (i * parseInt(paramString, paramInt1, j));
      if (paramString.charAt(j++) != ':') {
        throw new RuntimeException("Error in parsing time zone");
      }
      timezoneMin = (i * parseInt(paramString, j, j + 2));
      if (j + 2 != paramInt2) {
        throw new RuntimeException("Error in parsing time zone");
      }
      if ((timezoneHr != 0) || (timezoneMin != 0)) {
        normalized = false;
      }
    }
    else
    {
      throw new RuntimeException("Error in parsing time zone");
    }
  }
  
  protected int indexOf(String paramString, int paramInt1, int paramInt2, char paramChar)
  {
    for (int i = paramInt1; i < paramInt2; i++) {
      if (paramString.charAt(i) == paramChar) {
        return i;
      }
    }
    return -1;
  }
  
  protected void validateDateTime(DateTimeData paramDateTimeData)
  {
    if (year == 0) {
      throw new RuntimeException("The year \"0000\" is an illegal year value");
    }
    if ((month < 1) || (month > 12)) {
      throw new RuntimeException("The month must have values 1 to 12");
    }
    if ((day > maxDayInMonthFor(year, month)) || (day < 1)) {
      throw new RuntimeException("The day must have values 1 to 31");
    }
    if ((hour > 23) || (hour < 0)) {
      if ((hour == 24) && (minute == 0) && (second == 0.0D))
      {
        hour = 0;
        if (++day > maxDayInMonthFor(year, month))
        {
          day = 1;
          if (++month > 12)
          {
            month = 1;
            if (++year == 0) {
              year = 1;
            }
          }
        }
      }
      else
      {
        throw new RuntimeException("Hour must have values 0-23, unless 24:00:00");
      }
    }
    if ((minute > 59) || (minute < 0)) {
      throw new RuntimeException("Minute must have values 0-59");
    }
    if ((second >= 60.0D) || (second < 0.0D)) {
      throw new RuntimeException("Second must have values 0-59");
    }
    if ((timezoneHr > 14) || (timezoneHr < -14)) {
      throw new RuntimeException("Time zone should have range -14:00 to +14:00");
    }
    if (((timezoneHr == 14) || (timezoneHr == -14)) && (timezoneMin != 0)) {
      throw new RuntimeException("Time zone should have range -14:00 to +14:00");
    }
    if ((timezoneMin > 59) || (timezoneMin < -59)) {
      throw new RuntimeException("Minute must have values 0-59");
    }
  }
  
  protected int findUTCSign(String paramString, int paramInt1, int paramInt2)
  {
    for (int j = paramInt1; j < paramInt2; j++)
    {
      int i = paramString.charAt(j);
      if ((i == 90) || (i == 43) || (i == 45)) {
        return j;
      }
    }
    return -1;
  }
  
  protected final boolean isNextCharUTCSign(String paramString, int paramInt1, int paramInt2)
  {
    if (paramInt1 < paramInt2)
    {
      int i = paramString.charAt(paramInt1);
      return (i == 90) || (i == 43) || (i == 45);
    }
    return false;
  }
  
  protected int parseInt(String paramString, int paramInt1, int paramInt2)
    throws NumberFormatException
  {
    int i = 10;
    int j = 0;
    int k = 0;
    int m = -2147483647;
    int n = m / i;
    int i1 = paramInt1;
    do
    {
      k = TypeValidator.getDigit(paramString.charAt(i1));
      if (k < 0) {
        throw new NumberFormatException("'" + paramString + "' has wrong format");
      }
      if (j < n) {
        throw new NumberFormatException("'" + paramString + "' has wrong format");
      }
      j *= i;
      if (j < m + k) {
        throw new NumberFormatException("'" + paramString + "' has wrong format");
      }
      j -= k;
      i1++;
    } while (i1 < paramInt2);
    return -j;
  }
  
  protected int parseIntYear(String paramString, int paramInt)
  {
    int i = 10;
    int j = 0;
    int k = 0;
    int m = 0;
    int i2 = 0;
    int n;
    if (paramString.charAt(0) == '-')
    {
      k = 1;
      n = Integer.MIN_VALUE;
      m++;
    }
    else
    {
      n = -2147483647;
    }
    int i1 = n / i;
    while (m < paramInt)
    {
      i2 = TypeValidator.getDigit(paramString.charAt(m++));
      if (i2 < 0) {
        throw new NumberFormatException("'" + paramString + "' has wrong format");
      }
      if (j < i1) {
        throw new NumberFormatException("'" + paramString + "' has wrong format");
      }
      j *= i;
      if (j < n + i2) {
        throw new NumberFormatException("'" + paramString + "' has wrong format");
      }
      j -= i2;
    }
    if (k != 0)
    {
      if (m > 1) {
        return j;
      }
      throw new NumberFormatException("'" + paramString + "' has wrong format");
    }
    return -j;
  }
  
  protected void normalize(DateTimeData paramDateTimeData)
  {
    int i = -1;
    int j = minute + i * timezoneMin;
    int k = fQuotient(j, 60);
    minute = mod(j, 60, k);
    j = hour + i * timezoneHr + k;
    k = fQuotient(j, 24);
    hour = mod(j, 24, k);
    day += k;
    for (;;)
    {
      j = maxDayInMonthFor(year, month);
      if (day < 1)
      {
        day += maxDayInMonthFor(year, month - 1);
        k = -1;
      }
      else
      {
        if (day <= j) {
          break;
        }
        day -= j;
        k = 1;
      }
      j = month + k;
      month = modulo(j, 1, 13);
      year += fQuotient(j, 1, 13);
      if (year == 0) {
        year = ((timezoneHr < 0) || (timezoneMin < 0) ? 1 : -1);
      }
    }
    utc = 90;
  }
  
  protected void saveUnnormalized(DateTimeData paramDateTimeData)
  {
    unNormYear = year;
    unNormMonth = month;
    unNormDay = day;
    unNormHour = hour;
    unNormMinute = minute;
    unNormSecond = second;
  }
  
  protected void resetDateObj(DateTimeData paramDateTimeData)
  {
    year = 0;
    month = 0;
    day = 0;
    hour = 0;
    minute = 0;
    second = 0.0D;
    utc = 0;
    timezoneHr = 0;
    timezoneMin = 0;
  }
  
  protected int maxDayInMonthFor(int paramInt1, int paramInt2)
  {
    if ((paramInt2 == 4) || (paramInt2 == 6) || (paramInt2 == 9) || (paramInt2 == 11)) {
      return 30;
    }
    if (paramInt2 == 2)
    {
      if (isLeapYear(paramInt1)) {
        return 29;
      }
      return 28;
    }
    return 31;
  }
  
  private boolean isLeapYear(int paramInt)
  {
    return (paramInt % 4 == 0) && ((paramInt % 100 != 0) || (paramInt % 400 == 0));
  }
  
  protected int mod(int paramInt1, int paramInt2, int paramInt3)
  {
    return paramInt1 - paramInt3 * paramInt2;
  }
  
  protected int fQuotient(int paramInt1, int paramInt2)
  {
    return (int)Math.floor(paramInt1 / paramInt2);
  }
  
  protected int modulo(int paramInt1, int paramInt2, int paramInt3)
  {
    int i = paramInt1 - paramInt2;
    int j = paramInt3 - paramInt2;
    return mod(i, j, fQuotient(i, j)) + paramInt2;
  }
  
  protected int fQuotient(int paramInt1, int paramInt2, int paramInt3)
  {
    return fQuotient(paramInt1 - paramInt2, paramInt3 - paramInt2);
  }
  
  protected String dateToString(DateTimeData paramDateTimeData)
  {
    StringBuffer localStringBuffer = new StringBuffer(25);
    append(localStringBuffer, year, 4);
    localStringBuffer.append('-');
    append(localStringBuffer, month, 2);
    localStringBuffer.append('-');
    append(localStringBuffer, day, 2);
    localStringBuffer.append('T');
    append(localStringBuffer, hour, 2);
    localStringBuffer.append(':');
    append(localStringBuffer, minute, 2);
    localStringBuffer.append(':');
    append(localStringBuffer, second);
    append(localStringBuffer, (char)utc, 0);
    return localStringBuffer.toString();
  }
  
  protected final void append(StringBuffer paramStringBuffer, int paramInt1, int paramInt2)
  {
    if (paramInt1 == Integer.MIN_VALUE)
    {
      paramStringBuffer.append(paramInt1);
      return;
    }
    if (paramInt1 < 0)
    {
      paramStringBuffer.append('-');
      paramInt1 = -paramInt1;
    }
    if (paramInt2 == 4)
    {
      if (paramInt1 < 10) {
        paramStringBuffer.append("000");
      } else if (paramInt1 < 100) {
        paramStringBuffer.append("00");
      } else if (paramInt1 < 1000) {
        paramStringBuffer.append('0');
      }
      paramStringBuffer.append(paramInt1);
    }
    else if (paramInt2 == 2)
    {
      if (paramInt1 < 10) {
        paramStringBuffer.append('0');
      }
      paramStringBuffer.append(paramInt1);
    }
    else if (paramInt1 != 0)
    {
      paramStringBuffer.append((char)paramInt1);
    }
  }
  
  protected final void append(StringBuffer paramStringBuffer, double paramDouble)
  {
    if (paramDouble < 0.0D)
    {
      paramStringBuffer.append('-');
      paramDouble = -paramDouble;
    }
    if (paramDouble < 10.0D) {
      paramStringBuffer.append('0');
    }
    append2(paramStringBuffer, paramDouble);
  }
  
  protected final void append2(StringBuffer paramStringBuffer, double paramDouble)
  {
    int i = (int)paramDouble;
    if (paramDouble == i) {
      paramStringBuffer.append(i);
    } else {
      append3(paramStringBuffer, paramDouble);
    }
  }
  
  private void append3(StringBuffer paramStringBuffer, double paramDouble)
  {
    String str = String.valueOf(paramDouble);
    int i = str.indexOf('E');
    if (i == -1)
    {
      paramStringBuffer.append(str);
      return;
    }
    int j;
    int n;
    int i1;
    if (paramDouble < 1.0D)
    {
      try
      {
        j = parseInt(str, i + 2, str.length());
      }
      catch (Exception localException1)
      {
        paramStringBuffer.append(str);
        return;
      }
      paramStringBuffer.append("0.");
      for (int k = 1; k < j; k++) {
        paramStringBuffer.append('0');
      }
      for (n = i - 1; n > 0; n--)
      {
        i1 = str.charAt(n);
        if (i1 != 48) {
          break;
        }
      }
      for (i1 = 0; i1 <= n; i1++)
      {
        char c = str.charAt(i1);
        if (c != '.') {
          paramStringBuffer.append(c);
        }
      }
    }
    else
    {
      try
      {
        j = parseInt(str, i + 1, str.length());
      }
      catch (Exception localException2)
      {
        paramStringBuffer.append(str);
        return;
      }
      int m = j + 2;
      for (n = 0; n < i; n++)
      {
        i1 = str.charAt(n);
        if (i1 != 46)
        {
          if (n == m) {
            paramStringBuffer.append('.');
          }
          paramStringBuffer.append(i1);
        }
      }
      for (int i2 = m - i; i2 > 0; i2--) {
        paramStringBuffer.append('0');
      }
    }
  }
  
  protected double parseSecond(String paramString, int paramInt1, int paramInt2)
    throws NumberFormatException
  {
    int i = -1;
    for (int j = paramInt1; j < paramInt2; j++)
    {
      int k = paramString.charAt(j);
      if (k == 46) {
        i = j;
      } else if ((k > 57) || (k < 48)) {
        throw new NumberFormatException("'" + paramString + "' has wrong format");
      }
    }
    if (i == -1)
    {
      if (paramInt1 + 2 != paramInt2) {
        throw new NumberFormatException("'" + paramString + "' has wrong format");
      }
    }
    else if ((paramInt1 + 2 != i) || (i + 1 == paramInt2)) {
      throw new NumberFormatException("'" + paramString + "' has wrong format");
    }
    return Double.parseDouble(paramString.substring(paramInt1, paramInt2));
  }
  
  private void cloneDate(DateTimeData paramDateTimeData1, DateTimeData paramDateTimeData2)
  {
    year = year;
    month = month;
    day = day;
    hour = hour;
    minute = minute;
    second = second;
    utc = utc;
    timezoneHr = timezoneHr;
    timezoneMin = timezoneMin;
  }
  
  protected XMLGregorianCalendar getXMLGregorianCalendar(DateTimeData paramDateTimeData)
  {
    return null;
  }
  
  protected Duration getDuration(DateTimeData paramDateTimeData)
  {
    return null;
  }
  
  protected final BigDecimal getFractionalSecondsAsBigDecimal(DateTimeData paramDateTimeData)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    append3(localStringBuffer, unNormSecond);
    String str = localStringBuffer.toString();
    int i = str.indexOf('.');
    if (i == -1) {
      return null;
    }
    str = str.substring(i);
    BigDecimal localBigDecimal = new BigDecimal(str);
    if (localBigDecimal.compareTo(BigDecimal.valueOf(0L)) == 0) {
      return null;
    }
    return localBigDecimal;
  }
  
  static final class DateTimeData
    implements XSDateTime
  {
    int year;
    int month;
    int day;
    int hour;
    int minute;
    int utc;
    double second;
    int timezoneHr;
    int timezoneMin;
    private String originalValue;
    boolean normalized = true;
    int unNormYear;
    int unNormMonth;
    int unNormDay;
    int unNormHour;
    int unNormMinute;
    double unNormSecond;
    int position;
    final AbstractDateTimeDV type;
    private String canonical;
    
    public DateTimeData(String paramString, AbstractDateTimeDV paramAbstractDateTimeDV)
    {
      originalValue = paramString;
      type = paramAbstractDateTimeDV;
    }
    
    public DateTimeData(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, double paramDouble, int paramInt6, String paramString, boolean paramBoolean, AbstractDateTimeDV paramAbstractDateTimeDV)
    {
      year = paramInt1;
      month = paramInt2;
      day = paramInt3;
      hour = paramInt4;
      minute = paramInt5;
      second = paramDouble;
      utc = paramInt6;
      type = paramAbstractDateTimeDV;
      originalValue = paramString;
    }
    
    public boolean equals(Object paramObject)
    {
      if (!(paramObject instanceof DateTimeData)) {
        return false;
      }
      return type.compareDates(this, (DateTimeData)paramObject, true) == 0;
    }
    
    public synchronized String toString()
    {
      if (canonical == null) {
        canonical = type.dateToString(this);
      }
      return canonical;
    }
    
    public int getYears()
    {
      if ((type instanceof DurationDV)) {
        return 0;
      }
      return normalized ? year : unNormYear;
    }
    
    public int getMonths()
    {
      if ((type instanceof DurationDV)) {
        return year * 12 + month;
      }
      return normalized ? month : unNormMonth;
    }
    
    public int getDays()
    {
      if ((type instanceof DurationDV)) {
        return 0;
      }
      return normalized ? day : unNormDay;
    }
    
    public int getHours()
    {
      if ((type instanceof DurationDV)) {
        return 0;
      }
      return normalized ? hour : unNormHour;
    }
    
    public int getMinutes()
    {
      if ((type instanceof DurationDV)) {
        return 0;
      }
      return normalized ? minute : unNormMinute;
    }
    
    public double getSeconds()
    {
      if ((type instanceof DurationDV)) {
        return day * 24 * 60 * 60 + hour * 60 * 60 + minute * 60 + second;
      }
      return normalized ? second : unNormSecond;
    }
    
    public boolean hasTimeZone()
    {
      return utc != 0;
    }
    
    public int getTimeZoneHours()
    {
      return timezoneHr;
    }
    
    public int getTimeZoneMinutes()
    {
      return timezoneMin;
    }
    
    public String getLexicalValue()
    {
      return originalValue;
    }
    
    public XSDateTime normalize()
    {
      if (!normalized)
      {
        DateTimeData localDateTimeData = (DateTimeData)clone();
        normalized = true;
        return localDateTimeData;
      }
      return this;
    }
    
    public boolean isNormalized()
    {
      return normalized;
    }
    
    public Object clone()
    {
      DateTimeData localDateTimeData = new DateTimeData(year, month, day, hour, minute, second, utc, originalValue, normalized, type);
      canonical = canonical;
      position = position;
      timezoneHr = timezoneHr;
      timezoneMin = timezoneMin;
      unNormYear = unNormYear;
      unNormMonth = unNormMonth;
      unNormDay = unNormDay;
      unNormHour = unNormHour;
      unNormMinute = unNormMinute;
      unNormSecond = unNormSecond;
      return localDateTimeData;
    }
    
    public XMLGregorianCalendar getXMLGregorianCalendar()
    {
      return type.getXMLGregorianCalendar(this);
    }
    
    public Duration getDuration()
    {
      return type.getDuration(this);
    }
  }
}
