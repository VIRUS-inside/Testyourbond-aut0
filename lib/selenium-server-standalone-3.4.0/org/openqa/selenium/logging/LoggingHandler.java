package org.openqa.selenium.logging;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;


























public class LoggingHandler
  extends Handler
{
  private static final int MAX_RECORDS = 1000;
  private LinkedList<LogEntry> records = Lists.newLinkedList();
  private static final LoggingHandler INSTANCE = new LoggingHandler();
  
  private LoggingHandler() {}
  
  public static LoggingHandler getInstance() {
    return INSTANCE;
  }
  


  public synchronized List<LogEntry> getRecords()
  {
    return Collections.unmodifiableList(records);
  }
  
  public synchronized void publish(LogRecord logRecord)
  {
    if (isLoggable(logRecord)) {
      if (records.size() > 1000) {
        records.remove();
      }
      records.add(new LogEntry(logRecord.getLevel(), logRecord
        .getMillis(), logRecord
        .getLoggerName() + " " + logRecord
        .getSourceClassName() + "." + logRecord.getSourceMethodName() + " " + logRecord
        .getMessage()));
    }
  }
  
  public void flush()
  {
    records = Lists.newLinkedList();
  }
  
  public synchronized void close() throws SecurityException
  {
    records.clear();
  }
}
