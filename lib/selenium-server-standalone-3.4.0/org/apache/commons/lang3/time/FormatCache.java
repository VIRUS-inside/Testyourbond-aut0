package org.apache.commons.lang3.time;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


























abstract class FormatCache<F extends Format>
{
  static final int NONE = -1;
  private final ConcurrentMap<MultipartKey, F> cInstanceCache = new ConcurrentHashMap(7);
  

  private static final ConcurrentMap<MultipartKey, String> cDateTimeInstanceCache = new ConcurrentHashMap(7);
  


  FormatCache() {}
  


  public F getInstance()
  {
    return getDateTimeInstance(3, 3, TimeZone.getDefault(), Locale.getDefault());
  }
  











  public F getInstance(String pattern, TimeZone timeZone, Locale locale)
  {
    if (pattern == null) {
      throw new NullPointerException("pattern must not be null");
    }
    if (timeZone == null) {
      timeZone = TimeZone.getDefault();
    }
    if (locale == null) {
      locale = Locale.getDefault();
    }
    MultipartKey key = new MultipartKey(new Object[] { pattern, timeZone, locale });
    F format = (Format)cInstanceCache.get(key);
    if (format == null) {
      format = createInstance(pattern, timeZone, locale);
      F previousValue = (Format)cInstanceCache.putIfAbsent(key, format);
      if (previousValue != null)
      {

        format = previousValue;
      }
    }
    return format;
  }
  












  protected abstract F createInstance(String paramString, TimeZone paramTimeZone, Locale paramLocale);
  












  private F getDateTimeInstance(Integer dateStyle, Integer timeStyle, TimeZone timeZone, Locale locale)
  {
    if (locale == null) {
      locale = Locale.getDefault();
    }
    String pattern = getPatternForStyle(dateStyle, timeStyle, locale);
    return getInstance(pattern, timeZone, locale);
  }
  













  F getDateTimeInstance(int dateStyle, int timeStyle, TimeZone timeZone, Locale locale)
  {
    return getDateTimeInstance(Integer.valueOf(dateStyle), Integer.valueOf(timeStyle), timeZone, locale);
  }
  












  F getDateInstance(int dateStyle, TimeZone timeZone, Locale locale)
  {
    return getDateTimeInstance(Integer.valueOf(dateStyle), null, timeZone, locale);
  }
  












  F getTimeInstance(int timeStyle, TimeZone timeZone, Locale locale)
  {
    return getDateTimeInstance(null, Integer.valueOf(timeStyle), timeZone, locale);
  }
  









  static String getPatternForStyle(Integer dateStyle, Integer timeStyle, Locale locale)
  {
    MultipartKey key = new MultipartKey(new Object[] { dateStyle, timeStyle, locale });
    
    String pattern = (String)cDateTimeInstanceCache.get(key);
    if (pattern == null) {
      try { DateFormat formatter;
        DateFormat formatter;
        if (dateStyle == null) {
          formatter = DateFormat.getTimeInstance(timeStyle.intValue(), locale);
        } else { DateFormat formatter;
          if (timeStyle == null) {
            formatter = DateFormat.getDateInstance(dateStyle.intValue(), locale);
          }
          else
            formatter = DateFormat.getDateTimeInstance(dateStyle.intValue(), timeStyle.intValue(), locale);
        }
        pattern = ((SimpleDateFormat)formatter).toPattern();
        String previous = (String)cDateTimeInstanceCache.putIfAbsent(key, pattern);
        if (previous != null)
        {


          pattern = previous;
        }
      } catch (ClassCastException ex) {
        throw new IllegalArgumentException("No date time pattern for locale: " + locale);
      }
    }
    return pattern;
  }
  


  private static class MultipartKey
  {
    private final Object[] keys;
    

    private int hashCode;
    


    public MultipartKey(Object... keys)
    {
      this.keys = keys;
    }
    






    public boolean equals(Object obj)
    {
      return Arrays.equals(keys, keys);
    }
    



    public int hashCode()
    {
      if (hashCode == 0) {
        int rc = 0;
        for (Object key : keys) {
          if (key != null) {
            rc = rc * 7 + key.hashCode();
          }
        }
        hashCode = rc;
      }
      return hashCode;
    }
  }
}
