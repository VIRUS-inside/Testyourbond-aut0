package org.apache.xerces.impl.dv.xs;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidationContext;

public class YearMonthDV
  extends AbstractDateTimeDV
{
  public YearMonthDV() {}
  
  public Object getActualValue(String paramString, ValidationContext paramValidationContext)
    throws InvalidDatatypeValueException
  {
    try
    {
      return parse(paramString);
    }
    catch (Exception localException)
    {
      throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[] { paramString, "gYearMonth" });
    }
  }
  
  protected AbstractDateTimeDV.DateTimeData parse(String paramString)
    throws SchemaDateTimeException
  {
    AbstractDateTimeDV.DateTimeData localDateTimeData = new AbstractDateTimeDV.DateTimeData(paramString, this);
    int i = paramString.length();
    int j = getYearMonth(paramString, 0, i, localDateTimeData);
    day = 1;
    parseTimeZone(paramString, j, i, localDateTimeData);
    validateDateTime(localDateTimeData);
    saveUnnormalized(localDateTimeData);
    if ((utc != 0) && (utc != 90)) {
      normalize(localDateTimeData);
    }
    position = 0;
    return localDateTimeData;
  }
  
  protected String dateToString(AbstractDateTimeDV.DateTimeData paramDateTimeData)
  {
    StringBuffer localStringBuffer = new StringBuffer(25);
    append(localStringBuffer, year, 4);
    localStringBuffer.append('-');
    append(localStringBuffer, month, 2);
    append(localStringBuffer, (char)utc, 0);
    return localStringBuffer.toString();
  }
  
  protected XMLGregorianCalendar getXMLGregorianCalendar(AbstractDateTimeDV.DateTimeData paramDateTimeData)
  {
    return AbstractDateTimeDV.datatypeFactory.newXMLGregorianCalendar(unNormYear, unNormMonth, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, paramDateTimeData.hasTimeZone() ? timezoneHr * 60 + timezoneMin : Integer.MIN_VALUE);
  }
}
