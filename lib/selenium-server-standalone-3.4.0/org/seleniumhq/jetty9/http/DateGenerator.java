package org.seleniumhq.jetty9.http;

import java.util.GregorianCalendar;
import java.util.TimeZone;
import org.seleniumhq.jetty9.util.StringUtil;
























public class DateGenerator
{
  private static final TimeZone __GMT = TimeZone.getTimeZone("GMT");
  
  static {
    __GMT.setID("GMT");
  }
  
  static final String[] DAYS = { "Sat", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
  
  static final String[] MONTHS = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec", "Jan" };
  


  private static final ThreadLocal<DateGenerator> __dateGenerator = new ThreadLocal()
  {

    protected DateGenerator initialValue()
    {
      return new DateGenerator();
    }
  };
  

  public static final String __01Jan1970 = formatDate(0L);
  





  public static String formatDate(long date)
  {
    return ((DateGenerator)__dateGenerator.get()).doFormatDate(date);
  }
  





  public static void formatCookieDate(StringBuilder buf, long date)
  {
    ((DateGenerator)__dateGenerator.get()).doFormatCookieDate(buf, date);
  }
  





  public static String formatCookieDate(long date)
  {
    StringBuilder buf = new StringBuilder(28);
    formatCookieDate(buf, date);
    return buf.toString();
  }
  
  private final StringBuilder buf = new StringBuilder(32);
  private final GregorianCalendar gc = new GregorianCalendar(__GMT);
  





  public String doFormatDate(long date)
  {
    buf.setLength(0);
    gc.setTimeInMillis(date);
    
    int day_of_week = gc.get(7);
    int day_of_month = gc.get(5);
    int month = gc.get(2);
    int year = gc.get(1);
    int century = year / 100;
    year %= 100;
    
    int hours = gc.get(11);
    int minutes = gc.get(12);
    int seconds = gc.get(13);
    
    buf.append(DAYS[day_of_week]);
    buf.append(',');
    buf.append(' ');
    StringUtil.append2digits(buf, day_of_month);
    
    buf.append(' ');
    buf.append(MONTHS[month]);
    buf.append(' ');
    StringUtil.append2digits(buf, century);
    StringUtil.append2digits(buf, year);
    
    buf.append(' ');
    StringUtil.append2digits(buf, hours);
    buf.append(':');
    StringUtil.append2digits(buf, minutes);
    buf.append(':');
    StringUtil.append2digits(buf, seconds);
    buf.append(" GMT");
    return buf.toString();
  }
  





  public void doFormatCookieDate(StringBuilder buf, long date)
  {
    gc.setTimeInMillis(date);
    
    int day_of_week = gc.get(7);
    int day_of_month = gc.get(5);
    int month = gc.get(2);
    int year = gc.get(1);
    year %= 10000;
    
    int epoch = (int)(date / 1000L % 86400L);
    int seconds = epoch % 60;
    epoch /= 60;
    int minutes = epoch % 60;
    int hours = epoch / 60;
    
    buf.append(DAYS[day_of_week]);
    buf.append(',');
    buf.append(' ');
    StringUtil.append2digits(buf, day_of_month);
    
    buf.append('-');
    buf.append(MONTHS[month]);
    buf.append('-');
    StringUtil.append2digits(buf, year / 100);
    StringUtil.append2digits(buf, year % 100);
    
    buf.append(' ');
    StringUtil.append2digits(buf, hours);
    buf.append(':');
    StringUtil.append2digits(buf, minutes);
    buf.append(':');
    StringUtil.append2digits(buf, seconds);
    buf.append(" GMT");
  }
  
  public DateGenerator() {}
}
