package org.apache.xerces.impl.dv.xs;

import java.math.BigInteger;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidationContext;

class YearMonthDurationDV
  extends DurationDV
{
  YearMonthDurationDV() {}
  
  public Object getActualValue(String paramString, ValidationContext paramValidationContext)
    throws InvalidDatatypeValueException
  {
    try
    {
      return parse(paramString, 1);
    }
    catch (Exception localException)
    {
      throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[] { paramString, "yearMonthDuration" });
    }
  }
  
  protected Duration getDuration(AbstractDateTimeDV.DateTimeData paramDateTimeData)
  {
    int i = 1;
    if ((year < 0) || (month < 0)) {
      i = -1;
    }
    return AbstractDateTimeDV.datatypeFactory.newDuration(i == 1, year != Integer.MIN_VALUE ? BigInteger.valueOf(i * year) : null, month != Integer.MIN_VALUE ? BigInteger.valueOf(i * month) : null, null, null, null, null);
  }
}
