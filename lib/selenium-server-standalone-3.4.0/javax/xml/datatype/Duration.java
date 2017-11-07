package javax.xml.datatype;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.xml.namespace.QName;

public abstract class Duration
{
  public Duration() {}
  
  public QName getXMLSchemaType()
  {
    boolean bool1 = isSet(DatatypeConstants.YEARS);
    boolean bool2 = isSet(DatatypeConstants.MONTHS);
    boolean bool3 = isSet(DatatypeConstants.DAYS);
    boolean bool4 = isSet(DatatypeConstants.HOURS);
    boolean bool5 = isSet(DatatypeConstants.MINUTES);
    boolean bool6 = isSet(DatatypeConstants.SECONDS);
    if ((bool1) && (bool2) && (bool3) && (bool4) && (bool5) && (bool6)) {
      return DatatypeConstants.DURATION;
    }
    if ((!bool1) && (!bool2) && (bool3) && (bool4) && (bool5) && (bool6)) {
      return DatatypeConstants.DURATION_DAYTIME;
    }
    if ((bool1) && (bool2) && (!bool3) && (!bool4) && (!bool5) && (!bool6)) {
      return DatatypeConstants.DURATION_YEARMONTH;
    }
    throw new IllegalStateException("javax.xml.datatype.Duration#getXMLSchemaType(): this Duration does not match one of the XML Schema date/time datatypes: year set = " + bool1 + " month set = " + bool2 + " day set = " + bool3 + " hour set = " + bool4 + " minute set = " + bool5 + " second set = " + bool6);
  }
  
  public abstract int getSign();
  
  public int getYears()
  {
    return getFieldValueAsInt(DatatypeConstants.YEARS);
  }
  
  public int getMonths()
  {
    return getFieldValueAsInt(DatatypeConstants.MONTHS);
  }
  
  public int getDays()
  {
    return getFieldValueAsInt(DatatypeConstants.DAYS);
  }
  
  public int getHours()
  {
    return getFieldValueAsInt(DatatypeConstants.HOURS);
  }
  
  public int getMinutes()
  {
    return getFieldValueAsInt(DatatypeConstants.MINUTES);
  }
  
  public int getSeconds()
  {
    return getFieldValueAsInt(DatatypeConstants.SECONDS);
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
  
  public abstract Number getField(DatatypeConstants.Field paramField);
  
  private int getFieldValueAsInt(DatatypeConstants.Field paramField)
  {
    Number localNumber = getField(paramField);
    if (localNumber != null) {
      return localNumber.intValue();
    }
    return 0;
  }
  
  public abstract boolean isSet(DatatypeConstants.Field paramField);
  
  public abstract Duration add(Duration paramDuration);
  
  public abstract void addTo(Calendar paramCalendar);
  
  public void addTo(Date paramDate)
  {
    if (paramDate == null) {
      throw new NullPointerException("Cannot call " + getClass().getName() + "#addTo(Date date) with date == null.");
    }
    GregorianCalendar localGregorianCalendar = new GregorianCalendar();
    localGregorianCalendar.setTime(paramDate);
    addTo(localGregorianCalendar);
    paramDate.setTime(getCalendarTimeInMillis(localGregorianCalendar));
  }
  
  public Duration subtract(Duration paramDuration)
  {
    return add(paramDuration.negate());
  }
  
  public Duration multiply(int paramInt)
  {
    return multiply(BigDecimal.valueOf(paramInt));
  }
  
  public abstract Duration multiply(BigDecimal paramBigDecimal);
  
  public abstract Duration negate();
  
  public abstract Duration normalizeWith(Calendar paramCalendar);
  
  public abstract int compare(Duration paramDuration);
  
  public boolean isLongerThan(Duration paramDuration)
  {
    return compare(paramDuration) == 1;
  }
  
  public boolean isShorterThan(Duration paramDuration)
  {
    return compare(paramDuration) == -1;
  }
  
  public boolean equals(Object paramObject)
  {
    if (paramObject == this) {
      return true;
    }
    if ((paramObject instanceof Duration)) {
      return compare((Duration)paramObject) == 0;
    }
    return false;
  }
  
  public abstract int hashCode();
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    if (getSign() < 0) {
      localStringBuffer.append('-');
    }
    localStringBuffer.append('P');
    BigInteger localBigInteger1 = (BigInteger)getField(DatatypeConstants.YEARS);
    if (localBigInteger1 != null) {
      localStringBuffer.append(localBigInteger1).append('Y');
    }
    BigInteger localBigInteger2 = (BigInteger)getField(DatatypeConstants.MONTHS);
    if (localBigInteger2 != null) {
      localStringBuffer.append(localBigInteger2).append('M');
    }
    BigInteger localBigInteger3 = (BigInteger)getField(DatatypeConstants.DAYS);
    if (localBigInteger3 != null) {
      localStringBuffer.append(localBigInteger3).append('D');
    }
    BigInteger localBigInteger4 = (BigInteger)getField(DatatypeConstants.HOURS);
    BigInteger localBigInteger5 = (BigInteger)getField(DatatypeConstants.MINUTES);
    BigDecimal localBigDecimal = (BigDecimal)getField(DatatypeConstants.SECONDS);
    if ((localBigInteger4 != null) || (localBigInteger5 != null) || (localBigDecimal != null))
    {
      localStringBuffer.append('T');
      if (localBigInteger4 != null) {
        localStringBuffer.append(localBigInteger4).append('H');
      }
      if (localBigInteger5 != null) {
        localStringBuffer.append(localBigInteger5).append('M');
      }
      if (localBigDecimal != null) {
        localStringBuffer.append(toString(localBigDecimal)).append('S');
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
  
  private static long getCalendarTimeInMillis(Calendar paramCalendar)
  {
    return paramCalendar.getTime().getTime();
  }
}
