package org.apache.http.protocol;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;






































@Contract(threading=ThreadingBehavior.SAFE)
public class HttpDateGenerator
{
  public static final String PATTERN_RFC1123 = "EEE, dd MMM yyyy HH:mm:ss zzz";
  public static final TimeZone GMT = TimeZone.getTimeZone("GMT");
  
  private final DateFormat dateformat;
  private long dateAsLong = 0L;
  private String dateAsText = null;
  
  public HttpDateGenerator()
  {
    dateformat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
    dateformat.setTimeZone(GMT);
  }
  
  public synchronized String getCurrentDate() {
    long now = System.currentTimeMillis();
    if (now - dateAsLong > 1000L)
    {
      dateAsText = dateformat.format(new Date(now));
      dateAsLong = now;
    }
    return dateAsText;
  }
}
