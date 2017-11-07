package org.openqa.selenium.remote.server.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
































public class TerseFormatter
  extends Formatter
{
  private static final String PREFIX = "";
  private static final String SUFFIX = " - ";
  private final String lineSeparator = System.getProperty("line.separator");
  

  private static final int FINE = 500;
  
  private static final int INFO = 800;
  
  private static final int WARNING = 900;
  
  private static final int SEVERE = 1000;
  
  private final StringBuilder buffer;
  
  private SimpleDateFormat timestampFormatter;
  

  public TerseFormatter()
  {
    buffer = new StringBuilder();
    buffer.append("");
    timestampFormatter = new SimpleDateFormat("HH:mm:ss.SSS");
  }
  






  public synchronized String format(LogRecord record)
  {
    buffer.setLength("".length());
    buffer.append(timestampFormatter.format(new Date(record.getMillis())));
    buffer.append(' ');
    buffer.append(levelNumberToCommonsLevelName(record.getLevel()));
    buffer.append(" - ");
    buffer.append(formatMessage(record)).append(lineSeparator);
    if (record.getThrown() != null) {
      StringWriter trace = new StringWriter();
      record.getThrown().printStackTrace(new PrintWriter(trace));
      buffer.append(trace);
    }
    
    return buffer.toString();
  }
  
  private String levelNumberToCommonsLevelName(Level level)
  {
    switch (level.intValue()) {
    case 500: 
      return "DEBUG";
    case 800: 
      return "INFO";
    case 900: 
      return "WARN";
    case 1000: 
      return "ERROR";
    }
    return level.getLocalizedName();
  }
}
