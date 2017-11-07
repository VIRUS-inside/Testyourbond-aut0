package org.openqa.selenium.logging;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;





















public class LogEntry
{
  private static final ThreadLocal<SimpleDateFormat> DATE_FORMAT = new ThreadLocal()
  {
    protected SimpleDateFormat initialValue()
    {
      return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
    }
  };
  

  private final Level level;
  
  private final long timestamp;
  
  private final String message;
  

  public LogEntry(Level level, long timestamp, String message)
  {
    this.level = level;
    this.timestamp = timestamp;
    this.message = message;
  }
  




  public Level getLevel()
  {
    return level;
  }
  




  public long getTimestamp()
  {
    return timestamp;
  }
  




  public String getMessage()
  {
    return message;
  }
  
  public String toString()
  {
    return String.format("[%s] [%s] %s", new Object[] {
      ((SimpleDateFormat)DATE_FORMAT.get()).format(new Date(timestamp)), level, message });
  }
  
  public Map<String, Object> toMap() {
    Map<String, Object> map = new HashMap();
    map.put("timestamp", Long.valueOf(timestamp));
    map.put("level", level);
    map.put("message", message);
    return map;
  }
}
