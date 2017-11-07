package org.eclipse.jetty.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;




































public class DateCache
{
  public static final String DEFAULT_FORMAT = "EEE MMM dd HH:mm:ss zzz yyyy";
  private final String _formatString;
  private final String _tzFormatString;
  private final SimpleDateFormat _tzFormat;
  private final Locale _locale;
  private volatile Tick _tick;
  
  public static class Tick
  {
    final long _seconds;
    final String _string;
    
    public Tick(long seconds, String string)
    {
      _seconds = seconds;
      _string = string;
    }
  }
  





  public DateCache()
  {
    this("EEE MMM dd HH:mm:ss zzz yyyy");
  }
  





  public DateCache(String format)
  {
    this(format, null, TimeZone.getDefault());
  }
  

  public DateCache(String format, Locale l)
  {
    this(format, l, TimeZone.getDefault());
  }
  

  public DateCache(String format, Locale l, String tz)
  {
    this(format, l, TimeZone.getTimeZone(tz));
  }
  

  public DateCache(String format, Locale l, TimeZone tz)
  {
    _formatString = format;
    _locale = l;
    

    int zIndex = _formatString.indexOf("ZZZ");
    if (zIndex >= 0)
    {
      String ss1 = _formatString.substring(0, zIndex);
      String ss2 = _formatString.substring(zIndex + 3);
      int tzOffset = tz.getRawOffset();
      
      StringBuilder sb = new StringBuilder(_formatString.length() + 10);
      sb.append(ss1);
      sb.append("'");
      if (tzOffset >= 0) {
        sb.append('+');
      }
      else {
        tzOffset = -tzOffset;
        sb.append('-');
      }
      
      int raw = tzOffset / 60000;
      int hr = raw / 60;
      int min = raw % 60;
      
      if (hr < 10)
        sb.append('0');
      sb.append(hr);
      if (min < 10)
        sb.append('0');
      sb.append(min);
      sb.append('\'');
      
      sb.append(ss2);
      _tzFormatString = sb.toString();
    }
    else {
      _tzFormatString = _formatString;
    }
    if (_locale != null)
    {
      _tzFormat = new SimpleDateFormat(_tzFormatString, _locale);
    }
    else
    {
      _tzFormat = new SimpleDateFormat(_tzFormatString);
    }
    _tzFormat.setTimeZone(tz);
    
    _tick = null;
  }
  


  public TimeZone getTimeZone()
  {
    return _tzFormat.getTimeZone();
  }
  






  public String format(Date inDate)
  {
    long seconds = inDate.getTime() / 1000L;
    
    Tick tick = _tick;
    

    if ((tick == null) || (seconds != _seconds))
    {

      synchronized (this)
      {
        return _tzFormat.format(inDate);
      }
    }
    
    return _string;
  }
  







  public String format(long inDate)
  {
    long seconds = inDate / 1000L;
    
    Tick tick = _tick;
    

    if ((tick == null) || (seconds != _seconds))
    {

      Date d = new Date(inDate);
      synchronized (this)
      {
        return _tzFormat.format(d);
      }
    }
    
    return _string;
  }
  








  public String formatNow(long now)
  {
    long seconds = now / 1000L;
    
    Tick tick = _tick;
    

    if ((tick != null) && (_seconds == seconds))
      return _string;
    return formatTick_string;
  }
  

  public String now()
  {
    return formatNow(System.currentTimeMillis());
  }
  

  public Tick tick()
  {
    return formatTick(System.currentTimeMillis());
  }
  

  protected Tick formatTick(long now)
  {
    long seconds = now / 1000L;
    

    synchronized (this)
    {

      if ((_tick == null) || (_tick._seconds != seconds))
      {
        String s = _tzFormat.format(new Date(now));
        return this._tick = new Tick(seconds, s);
      }
      return _tick;
    }
  }
  

  public String getFormatString()
  {
    return _formatString;
  }
}
