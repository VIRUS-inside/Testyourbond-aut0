package org.apache.commons.lang3.time;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;













































































public class FastDateFormat
  extends Format
  implements DateParser, DatePrinter
{
  private static final long serialVersionUID = 2L;
  public static final int FULL = 0;
  public static final int LONG = 1;
  public static final int MEDIUM = 2;
  public static final int SHORT = 3;
  private static final FormatCache<FastDateFormat> cache = new FormatCache()
  {
    protected FastDateFormat createInstance(String pattern, TimeZone timeZone, Locale locale) {
      return new FastDateFormat(pattern, timeZone, locale);
    }
  };
  


  private final FastDatePrinter printer;
  

  private final FastDateParser parser;
  


  public static FastDateFormat getInstance()
  {
    return (FastDateFormat)cache.getInstance();
  }
  








  public static FastDateFormat getInstance(String pattern)
  {
    return (FastDateFormat)cache.getInstance(pattern, null, null);
  }
  










  public static FastDateFormat getInstance(String pattern, TimeZone timeZone)
  {
    return (FastDateFormat)cache.getInstance(pattern, timeZone, null);
  }
  









  public static FastDateFormat getInstance(String pattern, Locale locale)
  {
    return (FastDateFormat)cache.getInstance(pattern, null, locale);
  }
  












  public static FastDateFormat getInstance(String pattern, TimeZone timeZone, Locale locale)
  {
    return (FastDateFormat)cache.getInstance(pattern, timeZone, locale);
  }
  










  public static FastDateFormat getDateInstance(int style)
  {
    return (FastDateFormat)cache.getDateInstance(style, null, null);
  }
  










  public static FastDateFormat getDateInstance(int style, Locale locale)
  {
    return (FastDateFormat)cache.getDateInstance(style, null, locale);
  }
  











  public static FastDateFormat getDateInstance(int style, TimeZone timeZone)
  {
    return (FastDateFormat)cache.getDateInstance(style, timeZone, null);
  }
  











  public static FastDateFormat getDateInstance(int style, TimeZone timeZone, Locale locale)
  {
    return (FastDateFormat)cache.getDateInstance(style, timeZone, locale);
  }
  










  public static FastDateFormat getTimeInstance(int style)
  {
    return (FastDateFormat)cache.getTimeInstance(style, null, null);
  }
  










  public static FastDateFormat getTimeInstance(int style, Locale locale)
  {
    return (FastDateFormat)cache.getTimeInstance(style, null, locale);
  }
  











  public static FastDateFormat getTimeInstance(int style, TimeZone timeZone)
  {
    return (FastDateFormat)cache.getTimeInstance(style, timeZone, null);
  }
  











  public static FastDateFormat getTimeInstance(int style, TimeZone timeZone, Locale locale)
  {
    return (FastDateFormat)cache.getTimeInstance(style, timeZone, locale);
  }
  











  public static FastDateFormat getDateTimeInstance(int dateStyle, int timeStyle)
  {
    return (FastDateFormat)cache.getDateTimeInstance(dateStyle, timeStyle, null, null);
  }
  











  public static FastDateFormat getDateTimeInstance(int dateStyle, int timeStyle, Locale locale)
  {
    return (FastDateFormat)cache.getDateTimeInstance(dateStyle, timeStyle, null, locale);
  }
  












  public static FastDateFormat getDateTimeInstance(int dateStyle, int timeStyle, TimeZone timeZone)
  {
    return getDateTimeInstance(dateStyle, timeStyle, timeZone, null);
  }
  












  public static FastDateFormat getDateTimeInstance(int dateStyle, int timeStyle, TimeZone timeZone, Locale locale)
  {
    return (FastDateFormat)cache.getDateTimeInstance(dateStyle, timeStyle, timeZone, locale);
  }
  









  protected FastDateFormat(String pattern, TimeZone timeZone, Locale locale)
  {
    this(pattern, timeZone, locale, null);
  }
  










  protected FastDateFormat(String pattern, TimeZone timeZone, Locale locale, Date centuryStart)
  {
    printer = new FastDatePrinter(pattern, timeZone, locale);
    parser = new FastDateParser(pattern, timeZone, locale, centuryStart);
  }
  












  public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos)
  {
    return toAppendTo.append(printer.format(obj));
  }
  







  public String format(long millis)
  {
    return printer.format(millis);
  }
  






  public String format(Date date)
  {
    return printer.format(date);
  }
  






  public String format(Calendar calendar)
  {
    return printer.format(calendar);
  }
  










  @Deprecated
  public StringBuffer format(long millis, StringBuffer buf)
  {
    return printer.format(millis, buf);
  }
  









  @Deprecated
  public StringBuffer format(Date date, StringBuffer buf)
  {
    return printer.format(date, buf);
  }
  









  @Deprecated
  public StringBuffer format(Calendar calendar, StringBuffer buf)
  {
    return printer.format(calendar, buf);
  }
  









  public <B extends Appendable> B format(long millis, B buf)
  {
    return printer.format(millis, buf);
  }
  









  public <B extends Appendable> B format(Date date, B buf)
  {
    return printer.format(date, buf);
  }
  









  public <B extends Appendable> B format(Calendar calendar, B buf)
  {
    return printer.format(calendar, buf);
  }
  






  public Date parse(String source)
    throws ParseException
  {
    return parser.parse(source);
  }
  



  public Date parse(String source, ParsePosition pos)
  {
    return parser.parse(source, pos);
  }
  




  public boolean parse(String source, ParsePosition pos, Calendar calendar)
  {
    return parser.parse(source, pos, calendar);
  }
  



  public Object parseObject(String source, ParsePosition pos)
  {
    return parser.parseObject(source, pos);
  }
  







  public String getPattern()
  {
    return printer.getPattern();
  }
  







  public TimeZone getTimeZone()
  {
    return printer.getTimeZone();
  }
  





  public Locale getLocale()
  {
    return printer.getLocale();
  }
  








  public int getMaxLengthEstimate()
  {
    return printer.getMaxLengthEstimate();
  }
  








  public boolean equals(Object obj)
  {
    if (!(obj instanceof FastDateFormat)) {
      return false;
    }
    FastDateFormat other = (FastDateFormat)obj;
    
    return printer.equals(printer);
  }
  





  public int hashCode()
  {
    return printer.hashCode();
  }
  





  public String toString()
  {
    return "FastDateFormat[" + printer.getPattern() + "," + printer.getLocale() + "," + printer.getTimeZone().getID() + "]";
  }
  








  @Deprecated
  protected StringBuffer applyRules(Calendar calendar, StringBuffer buf)
  {
    return printer.applyRules(calendar, buf);
  }
}
