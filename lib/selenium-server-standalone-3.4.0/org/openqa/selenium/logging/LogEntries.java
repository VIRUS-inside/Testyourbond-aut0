package org.openqa.selenium.logging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import org.openqa.selenium.Beta;





















@Beta
public class LogEntries
  implements Iterable<LogEntry>
{
  private final List<LogEntry> entries;
  
  public LogEntries(Iterable<LogEntry> entries)
  {
    List<LogEntry> mutableEntries = new ArrayList();
    for (LogEntry entry : entries) {
      mutableEntries.add(entry);
    }
    this.entries = Collections.unmodifiableList(mutableEntries);
  }
  




  public List<LogEntry> getAll()
  {
    return entries;
  }
  



  public List<LogEntry> filter(Level level)
  {
    List<LogEntry> toReturn = new ArrayList();
    
    for (LogEntry entry : entries) {
      if (entry.getLevel().intValue() >= level.intValue()) {
        toReturn.add(entry);
      }
    }
    
    return toReturn;
  }
  
  public Iterator<LogEntry> iterator() {
    return entries.iterator();
  }
}
