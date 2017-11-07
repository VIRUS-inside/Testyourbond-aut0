package org.openqa.selenium.logging.profiler;

import java.util.logging.Level;
import org.openqa.selenium.logging.LogEntry;
















public class ProfilerLogEntry
  extends LogEntry
{
  public ProfilerLogEntry(EventType eventType, String message)
  {
    super(Level.INFO, System.currentTimeMillis(), message);
  }
}
