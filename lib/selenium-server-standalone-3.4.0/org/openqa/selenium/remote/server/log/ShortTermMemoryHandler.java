package org.openqa.selenium.remote.server.log;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;





























public class ShortTermMemoryHandler
  extends Handler
{
  private final LogRecord[] lastRecords;
  private final int capacity;
  private final Formatter formatter;
  private int minimumLevel;
  private int currentIndex;
  
  public ShortTermMemoryHandler(int capacity, Level minimumLevel, Formatter formatter)
  {
    this.capacity = capacity;
    this.formatter = formatter;
    this.minimumLevel = minimumLevel.intValue();
    lastRecords = new LogRecord[capacity];
    currentIndex = 0;
  }
  

  public synchronized void publish(LogRecord record)
  {
    if (record.getLevel().intValue() < minimumLevel) {
      return;
    }
    lastRecords[currentIndex] = record;
    currentIndex += 1;
    if (currentIndex >= capacity) {
      currentIndex = 0;
    }
  }
  

  public synchronized void flush() {}
  

  public synchronized void close()
    throws SecurityException
  {
    for (int i = 0; i < capacity; i++) {
      lastRecords[i] = null;
    }
  }
  

  public synchronized LogRecord[] records()
  {
    ArrayList<LogRecord> validRecords = new ArrayList(capacity);
    for (int i = currentIndex; i < capacity; i++) {
      if (null != lastRecords[i]) {
        validRecords.add(lastRecords[i]);
      }
    }
    for (int i = 0; i < currentIndex; i++) {
      if (null != lastRecords[i]) {
        validRecords.add(lastRecords[i]);
      }
    }
    return (LogRecord[])validRecords.toArray(new LogRecord[validRecords.size()]);
  }
  

  public synchronized String formattedRecords()
  {
    StringWriter writer = new StringWriter();
    for (LogRecord record : records()) {
      writer.append(formatter.format(record));
      writer.append("\n");
    }
    return writer.toString();
  }
}
