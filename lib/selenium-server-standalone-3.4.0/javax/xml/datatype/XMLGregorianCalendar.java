package javax.xml.datatype;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import javax.xml.namespace.QName;

public abstract class XMLGregorianCalendar
  implements Cloneable
{
  public XMLGregorianCalendar() {}
  
  public abstract void clear();
  
  public abstract void reset();
  
  public abstract void setYear(BigInteger paramBigInteger);
  
  public abstract void setYear(int paramInt);
  
  public abstract void setMonth(int paramInt);
  
  public abstract void setDay(int paramInt);
  
  public abstract void setTimezone(int paramInt);
  
  public void setTime(int paramInt1, int paramInt2, int paramInt3)
  {
    setTime(paramInt1, paramInt2, paramInt3, null);
  }
  
  public abstract void setHour(int paramInt);
  
  public abstract void setMinute(int paramInt);
  
  public abstract void setSecond(int paramInt);
  
  public abstract void setMillisecond(int paramInt);
  
  public abstract void setFractionalSecond(BigDecimal paramBigDecimal);
  
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
  
  public abstract BigInteger getEon();
  
  public abstract int getYear();
  
  public abstract BigInteger getEonAndYear();
  
  public abstract int getMonth();
  
  public abstract int getDay();
  
  public abstract int getTimezone();
  
  public abstract int getHour();
  
  public abstract int getMinute();
  
  public abstract int getSecond();
  
  public int getMillisecond()
  {
    BigDecimal localBigDecimal = getFractionalSecond();
    if (localBigDecimal == null) {
      return Integer.MIN_VALUE;
    }
    return getFractionalSecond().movePointRight(3).intValue();
  }
  
  public abstract BigDecimal getFractionalSecond();
  
  public abstract int compare(XMLGregorianCalendar paramXMLGregorianCalendar);
  
  public abstract XMLGregorianCalendar normalize();
  
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
    XMLGregorianCalendar localXMLGregorianCalendar = this;
    if (i != 0) {
      localXMLGregorianCalendar = normalize();
    }
    return localXMLGregorianCalendar.getYear() + localXMLGregorianCalendar.getMonth() + localXMLGregorianCalendar.getDay() + localXMLGregorianCalendar.getHour() + localXMLGregorianCalendar.getMinute() + localXMLGregorianCalendar.getSecond();
  }
  
  public abstract String toXMLFormat();
  
  public abstract QName getXMLSchemaType();
  
  public String toString()
  {
    return toXMLFormat();
  }
  
  public abstract boolean isValid();
  
  public abstract void add(Duration paramDuration);
  
  public abstract GregorianCalendar toGregorianCalendar();
  
  public abstract GregorianCalendar toGregorianCalendar(TimeZone paramTimeZone, Locale paramLocale, XMLGregorianCalendar paramXMLGregorianCalendar);
  
  public abstract TimeZone getTimeZone(int paramInt);
  
  public abstract Object clone();
}
