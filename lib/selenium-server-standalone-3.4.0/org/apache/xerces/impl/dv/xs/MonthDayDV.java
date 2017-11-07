package org.apache.xerces.impl.dv.xs;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidationContext;

public class MonthDayDV
  extends AbstractDateTimeDV
{
  private static final int MONTHDAY_SIZE = 7;
  
  public MonthDayDV() {}
  
  public Object getActualValue(String paramString, ValidationContext paramValidationContext)
    throws InvalidDatatypeValueException
  {
    try
    {
      return parse(paramString);
    }
    catch (Exception localException)
    {
      throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[] { paramString, "gMonthDay" });
    }
  }
  
  protected AbstractDateTimeDV.DateTimeData parse(String paramString)
    throws SchemaDateTimeException
  {
    AbstractDateTimeDV.DateTimeData localDateTimeData = new AbstractDateTimeDV.DateTimeData(paramString, this);
    int i = paramString.length();
    year = 2000;
    if ((paramString.charAt(0) != '-') || (paramString.charAt(1) != '-')) {
      throw new SchemaDateTimeException("Invalid format for gMonthDay: " + paramString);
    }
    month = parseInt(paramString, 2, 4);
    int j = 4;
    if (paramString.charAt(j++) != '-') {
      throw new SchemaDateTimeException("Invalid format for gMonthDay: " + paramString);
    }
    day = parseInt(paramString, j, j + 2);
    if (7 < i)
    {
      if (!isNextCharUTCSign(paramString, 7, i)) {
        throw new SchemaDateTimeException("Error in month parsing:" + paramString);
      }
      getTimeZone(paramString, localDateTimeData, 7, i);
    }
    validateDateTime(localDateTimeData);
    saveUnnormalized(localDateTimeData);
    if ((utc != 0) && (utc != 90)) {
      normalize(localDateTimeData);
    }
    position = 1;
    return localDateTimeData;
  }
  
  protected String dateToString(AbstractDateTimeDV.DateTimeData paramDateTimeData)
  {
    StringBuffer localStringBuffer = new StringBuffer(8);
    localStringBuffer.append('-');
    localStringBuffer.append('-');
    append(localStringBuffer, month, 2);
    localStringBuffer.append('-');
    append(localStringBuffer, day, 2);
    append(localStringBuffer, (char)utc, 0);
    return localStringBuffer.toString();
  }
  
  protected XMLGregorianCalendar getXMLGregorianCalendar(AbstractDateTimeDV.DateTimeData paramDateTimeData)
  {
    return AbstractDateTimeDV.datatypeFactory.newXMLGregorianCalendar(Integer.MIN_VALUE, unNormMonth, unNormDay, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, paramDateTimeData.hasTimeZone() ? timezoneHr * 60 + timezoneMin : Integer.MIN_VALUE);
  }
}
