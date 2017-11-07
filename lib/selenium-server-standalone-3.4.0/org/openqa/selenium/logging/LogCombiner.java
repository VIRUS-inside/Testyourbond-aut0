package org.openqa.selenium.logging;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.Comparator;


















public class LogCombiner
{
  private static final Comparator<LogEntry> LOG_ENTRY_TIMESTAMP_COMPARATOR = new Comparator()
  {

    public int compare(LogEntry left, LogEntry right) { return new Long(left.getTimestamp()).compareTo(Long.valueOf(right.getTimestamp())); }
  };
  
  public LogCombiner() {}
  
  public static LogEntries combine(LogEntries... entries) { return new LogEntries(Iterables.mergeSorted(Lists.newArrayList(entries), LOG_ENTRY_TIMESTAMP_COMPARATOR)); }
}
