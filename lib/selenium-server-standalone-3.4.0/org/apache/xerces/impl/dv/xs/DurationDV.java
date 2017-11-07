package org.apache.xerces.impl.dv.xs;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidationContext;

public class DurationDV
  extends AbstractDateTimeDV
{
  public static final int DURATION_TYPE = 0;
  public static final int YEARMONTHDURATION_TYPE = 1;
  public static final int DAYTIMEDURATION_TYPE = 2;
  private static final AbstractDateTimeDV.DateTimeData[] DATETIMES = { new AbstractDateTimeDV.DateTimeData(1696, 9, 1, 0, 0, 0.0D, 90, null, true, null), new AbstractDateTimeDV.DateTimeData(1697, 2, 1, 0, 0, 0.0D, 90, null, true, null), new AbstractDateTimeDV.DateTimeData(1903, 3, 1, 0, 0, 0.0D, 90, null, true, null), new AbstractDateTimeDV.DateTimeData(1903, 7, 1, 0, 0, 0.0D, 90, null, true, null) };
  
  public DurationDV() {}
  
  public Object getActualValue(String paramString, ValidationContext paramValidationContext)
    throws InvalidDatatypeValueException
  {
    try
    {
      return parse(paramString, 0);
    }
    catch (Exception localException)
    {
      throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[] { paramString, "duration" });
    }
  }
  
  protected AbstractDateTimeDV.DateTimeData parse(String paramString, int paramInt)
    throws SchemaDateTimeException
  {
    int i = paramString.length();
    AbstractDateTimeDV.DateTimeData localDateTimeData = new AbstractDateTimeDV.DateTimeData(paramString, this);
    int j = 0;
    int k = paramString.charAt(j++);
    if ((k != 80) && (k != 45)) {
      throw new SchemaDateTimeException();
    }
    utc = (k == 45 ? 45 : 0);
    if ((k == 45) && (paramString.charAt(j++) != 'P')) {
      throw new SchemaDateTimeException();
    }
    int m = 1;
    if (utc == 45) {
      m = -1;
    }
    int n = 0;
    int i1 = indexOf(paramString, j, i, 'T');
    if (i1 == -1) {
      i1 = i;
    } else if (paramInt == 1) {
      throw new SchemaDateTimeException();
    }
    int i2 = indexOf(paramString, j, i1, 'Y');
    if (i2 != -1)
    {
      if (paramInt == 2) {
        throw new SchemaDateTimeException();
      }
      year = (m * parseInt(paramString, j, i2));
      j = i2 + 1;
      n = 1;
    }
    i2 = indexOf(paramString, j, i1, 'M');
    if (i2 != -1)
    {
      if (paramInt == 2) {
        throw new SchemaDateTimeException();
      }
      month = (m * parseInt(paramString, j, i2));
      j = i2 + 1;
      n = 1;
    }
    i2 = indexOf(paramString, j, i1, 'D');
    if (i2 != -1)
    {
      if (paramInt == 1) {
        throw new SchemaDateTimeException();
      }
      day = (m * parseInt(paramString, j, i2));
      j = i2 + 1;
      n = 1;
    }
    if ((i == i1) && (j != i)) {
      throw new SchemaDateTimeException();
    }
    if (i != i1)
    {
      i2 = indexOf(paramString, ++j, i, 'H');
      if (i2 != -1)
      {
        hour = (m * parseInt(paramString, j, i2));
        j = i2 + 1;
        n = 1;
      }
      i2 = indexOf(paramString, j, i, 'M');
      if (i2 != -1)
      {
        minute = (m * parseInt(paramString, j, i2));
        j = i2 + 1;
        n = 1;
      }
      i2 = indexOf(paramString, j, i, 'S');
      if (i2 != -1)
      {
        second = (m * parseSecond(paramString, j, i2));
        j = i2 + 1;
        n = 1;
      }
      if ((j != i) || (paramString.charAt(--j) == 'T')) {
        throw new SchemaDateTimeException();
      }
    }
    if (n == 0) {
      throw new SchemaDateTimeException();
    }
    return localDateTimeData;
  }
  
  protected short compareDates(AbstractDateTimeDV.DateTimeData paramDateTimeData1, AbstractDateTimeDV.DateTimeData paramDateTimeData2, boolean paramBoolean)
  {
    short s2 = 2;
    short s1 = compareOrder(paramDateTimeData1, paramDateTimeData2);
    if (s1 == 0) {
      return 0;
    }
    AbstractDateTimeDV.DateTimeData[] arrayOfDateTimeData = new AbstractDateTimeDV.DateTimeData[2];
    arrayOfDateTimeData[0] = new AbstractDateTimeDV.DateTimeData(null, this);
    arrayOfDateTimeData[1] = new AbstractDateTimeDV.DateTimeData(null, this);
    AbstractDateTimeDV.DateTimeData localDateTimeData1 = addDuration(paramDateTimeData1, DATETIMES[0], arrayOfDateTimeData[0]);
    AbstractDateTimeDV.DateTimeData localDateTimeData2 = addDuration(paramDateTimeData2, DATETIMES[0], arrayOfDateTimeData[1]);
    s1 = compareOrder(localDateTimeData1, localDateTimeData2);
    if (s1 == 2) {
      return 2;
    }
    localDateTimeData1 = addDuration(paramDateTimeData1, DATETIMES[1], arrayOfDateTimeData[0]);
    localDateTimeData2 = addDuration(paramDateTimeData2, DATETIMES[1], arrayOfDateTimeData[1]);
    s2 = compareOrder(localDateTimeData1, localDateTimeData2);
    s1 = compareResults(s1, s2, paramBoolean);
    if (s1 == 2) {
      return 2;
    }
    localDateTimeData1 = addDuration(paramDateTimeData1, DATETIMES[2], arrayOfDateTimeData[0]);
    localDateTimeData2 = addDuration(paramDateTimeData2, DATETIMES[2], arrayOfDateTimeData[1]);
    s2 = compareOrder(localDateTimeData1, localDateTimeData2);
    s1 = compareResults(s1, s2, paramBoolean);
    if (s1 == 2) {
      return 2;
    }
    localDateTimeData1 = addDuration(paramDateTimeData1, DATETIMES[3], arrayOfDateTimeData[0]);
    localDateTimeData2 = addDuration(paramDateTimeData2, DATETIMES[3], arrayOfDateTimeData[1]);
    s2 = compareOrder(localDateTimeData1, localDateTimeData2);
    s1 = compareResults(s1, s2, paramBoolean);
    return s1;
  }
  
  private short compareResults(short paramShort1, short paramShort2, boolean paramBoolean)
  {
    if (paramShort2 == 2) {
      return 2;
    }
    if ((paramShort1 != paramShort2) && (paramBoolean)) {
      return 2;
    }
    if ((paramShort1 != paramShort2) && (!paramBoolean))
    {
      if ((paramShort1 != 0) && (paramShort2 != 0)) {
        return 2;
      }
      return paramShort1 != 0 ? paramShort1 : paramShort2;
    }
    return paramShort1;
  }
  
  private AbstractDateTimeDV.DateTimeData addDuration(AbstractDateTimeDV.DateTimeData paramDateTimeData1, AbstractDateTimeDV.DateTimeData paramDateTimeData2, AbstractDateTimeDV.DateTimeData paramDateTimeData3)
  {
    resetDateObj(paramDateTimeData3);
    int i = month + month;
    month = modulo(i, 1, 13);
    int j = fQuotient(i, 1, 13);
    year = (year + year + j);
    double d = second + second;
    j = (int)Math.floor(d / 60.0D);
    second = (d - j * 60);
    i = minute + minute + j;
    j = fQuotient(i, 60);
    minute = mod(i, 60, j);
    i = hour + hour + j;
    j = fQuotient(i, 24);
    hour = mod(i, 24, j);
    day = (day + day + j);
    for (;;)
    {
      i = maxDayInMonthFor(year, month);
      if (day < 1)
      {
        day += maxDayInMonthFor(year, month - 1);
        j = -1;
      }
      else
      {
        if (day <= i) {
          break;
        }
        day -= i;
        j = 1;
      }
      i = month + j;
      month = modulo(i, 1, 13);
      year += fQuotient(i, 1, 13);
    }
    utc = 90;
    return paramDateTimeData3;
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
    if (i + 1 == paramInt2) {
      throw new NumberFormatException("'" + paramString + "' has wrong format");
    }
    double d = Double.parseDouble(paramString.substring(paramInt1, paramInt2));
    if (d == Double.POSITIVE_INFINITY) {
      throw new NumberFormatException("'" + paramString + "' has wrong format");
    }
    return d;
  }
  
  protected String dateToString(AbstractDateTimeDV.DateTimeData paramDateTimeData)
  {
    StringBuffer localStringBuffer = new StringBuffer(30);
    if ((year < 0) || (month < 0) || (day < 0) || (hour < 0) || (minute < 0) || (second < 0.0D)) {
      localStringBuffer.append('-');
    }
    localStringBuffer.append('P');
    localStringBuffer.append((year < 0 ? -1 : 1) * year);
    localStringBuffer.append('Y');
    localStringBuffer.append((month < 0 ? -1 : 1) * month);
    localStringBuffer.append('M');
    localStringBuffer.append((day < 0 ? -1 : 1) * day);
    localStringBuffer.append('D');
    localStringBuffer.append('T');
    localStringBuffer.append((hour < 0 ? -1 : 1) * hour);
    localStringBuffer.append('H');
    localStringBuffer.append((minute < 0 ? -1 : 1) * minute);
    localStringBuffer.append('M');
    append2(localStringBuffer, (second < 0.0D ? -1.0D : 1.0D) * second);
    localStringBuffer.append('S');
    return localStringBuffer.toString();
  }
  
  protected Duration getDuration(AbstractDateTimeDV.DateTimeData paramDateTimeData)
  {
    int i = 1;
    if ((year < 0) || (month < 0) || (day < 0) || (hour < 0) || (minute < 0) || (second < 0.0D)) {
      i = -1;
    }
    return AbstractDateTimeDV.datatypeFactory.newDuration(i == 1, year != Integer.MIN_VALUE ? BigInteger.valueOf(i * year) : null, month != Integer.MIN_VALUE ? BigInteger.valueOf(i * month) : null, day != Integer.MIN_VALUE ? BigInteger.valueOf(i * day) : null, hour != Integer.MIN_VALUE ? BigInteger.valueOf(i * hour) : null, minute != Integer.MIN_VALUE ? BigInteger.valueOf(i * minute) : null, second != -2.147483648E9D ? new BigDecimal(String.valueOf(i * second)) : null);
  }
}
