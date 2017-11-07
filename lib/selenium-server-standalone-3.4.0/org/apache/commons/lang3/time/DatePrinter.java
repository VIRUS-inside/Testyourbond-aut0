package org.apache.commons.lang3.time;

import java.text.FieldPosition;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public abstract interface DatePrinter
{
  public abstract String format(long paramLong);
  
  public abstract String format(Date paramDate);
  
  public abstract String format(Calendar paramCalendar);
  
  @Deprecated
  public abstract StringBuffer format(long paramLong, StringBuffer paramStringBuffer);
  
  @Deprecated
  public abstract StringBuffer format(Date paramDate, StringBuffer paramStringBuffer);
  
  @Deprecated
  public abstract StringBuffer format(Calendar paramCalendar, StringBuffer paramStringBuffer);
  
  public abstract <B extends Appendable> B format(long paramLong, B paramB);
  
  public abstract <B extends Appendable> B format(Date paramDate, B paramB);
  
  public abstract <B extends Appendable> B format(Calendar paramCalendar, B paramB);
  
  public abstract String getPattern();
  
  public abstract TimeZone getTimeZone();
  
  public abstract Locale getLocale();
  
  public abstract StringBuffer format(Object paramObject, StringBuffer paramStringBuffer, FieldPosition paramFieldPosition);
}
