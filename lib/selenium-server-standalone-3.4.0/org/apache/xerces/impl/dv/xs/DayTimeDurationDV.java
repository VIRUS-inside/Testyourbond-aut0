package org.apache.xerces.impl.dv.xs;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidationContext;

class DayTimeDurationDV
  extends DurationDV
{
  DayTimeDurationDV() {}
  
  public Object getActualValue(String paramString, ValidationContext paramValidationContext)
    throws InvalidDatatypeValueException
  {
    try
    {
      return parse(paramString, 2);
    }
    catch (Exception localException)
    {
      throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[] { paramString, "dayTimeDuration" });
    }
  }
  
  protected Duration getDuration(AbstractDateTimeDV.DateTimeData paramDateTimeData)
  {
    int i = 1;
    if ((day < 0) || (hour < 0) || (minute < 0) || (second < 0.0D)) {
      i = -1;
    }
    return AbstractDateTimeDV.datatypeFactory.newDuration(i == 1, null, null, day != Integer.MIN_VALUE ? BigInteger.valueOf(i * day) : null, hour != Integer.MIN_VALUE ? BigInteger.valueOf(i * hour) : null, minute != Integer.MIN_VALUE ? BigInteger.valueOf(i * minute) : null, second != -2.147483648E9D ? new BigDecimal(String.valueOf(i * second)) : null);
  }
}
