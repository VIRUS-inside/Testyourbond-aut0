package org.openqa.selenium.remote.server.log;

import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.logging.Logs;
import org.openqa.selenium.logging.SessionLogs;
import org.openqa.selenium.remote.SessionId;



























public class PerSessionLogHandler
  extends Handler
{
  private final Map<SessionId, List<LogRecord>> perSessionRecords;
  private final Map<SessionId, Map<String, LogEntries>> perSessionDriverEntries;
  private final Map<ThreadKey, List<LogRecord>> perThreadTempRecords;
  private final Formatter formatter;
  private Map<ThreadKey, SessionId> threadToSessionMap;
  private Map<SessionId, ThreadKey> sessionToThreadMap;
  private SessionLogsToFileRepository logFileRepository;
  private int capacity;
  private boolean storeLogsOnSessionQuit = false;
  
  private Level serverLogLevel = Level.INFO;
  









  public PerSessionLogHandler(int capacity, Formatter formatter, boolean captureLogsOnQuit)
  {
    this.capacity = capacity;
    this.formatter = formatter;
    storeLogsOnSessionQuit = captureLogsOnQuit;
    perSessionRecords = Maps.newHashMap();
    perThreadTempRecords = Maps.newHashMap();
    threadToSessionMap = Maps.newHashMap();
    sessionToThreadMap = Maps.newHashMap();
    logFileRepository = new SessionLogsToFileRepository();
    perSessionDriverEntries = Maps.newHashMap();
  }
  
  public synchronized void attachToCurrentThread(SessionId sessionId)
  {
    ThreadKey threadId = new ThreadKey();
    if ((threadToSessionMap.get(threadId) == null) || 
      (((SessionId)threadToSessionMap.get(threadId)).equals(sessionId))) {
      threadToSessionMap.put(threadId, sessionId);
      sessionToThreadMap.put(sessionId, threadId);
    }
    transferThreadTempLogsToSessionLogs(sessionId);
  }
  
  public void transferThreadTempLogsToSessionLogs(SessionId sessionId) {
    ThreadKey threadId = new ThreadKey();
    List<LogRecord> threadRecords = (List)perThreadTempRecords.get(threadId);
    List<LogRecord> sessionRecords = (List)perSessionRecords.get(sessionId);
    
    if (threadRecords != null) {
      if (sessionRecords == null) {
        sessionRecords = new ArrayList();
        perSessionRecords.put(sessionId, sessionRecords);
      }
      sessionRecords.addAll(threadRecords);
    }
    clearThreadTempLogs();
  }
  
  public synchronized void detachFromCurrentThread()
  {
    ThreadKey threadId = new ThreadKey();
    SessionId sessionId = (SessionId)threadToSessionMap.get(threadId);
    if (sessionId != null) {
      threadToSessionMap.remove(threadId);
      sessionToThreadMap.remove(sessionId);
      clearThreadTempLogs();
    }
  }
  






  public synchronized void removeSessionLogs(SessionId sessionId)
  {
    if (storeLogsOnSessionQuit) {
      return;
    }
    ThreadKey threadId = (ThreadKey)sessionToThreadMap.get(sessionId);
    SessionId sessionIdForThread = (SessionId)threadToSessionMap.get(threadId);
    if ((threadId != null) && (sessionIdForThread != null) && (sessionIdForThread.equals(sessionId))) {
      threadToSessionMap.remove(threadId);
      sessionToThreadMap.remove(sessionId);
    }
    perSessionRecords.remove(sessionId);
    logFileRepository.removeLogFile(sessionId);
  }
  











  public synchronized void clearThreadTempLogs()
  {
    ThreadKey threadId = new ThreadKey();
    perThreadTempRecords.remove(threadId);
  }
  







  public synchronized String getLog(SessionId sessionId)
    throws IOException
  {
    String logs = formattedRecords(sessionId);
    logs = "\n<RC_Logs RC_Session_ID=" + sessionId + ">\n" + logs + "\n</RC_Logs>\n";
    
    return logs;
  }
  









  public synchronized List<SessionId> getLoggedSessions()
  {
    ImmutableList.Builder<SessionId> builder = new ImmutableList.Builder();
    builder.addAll(perSessionDriverEntries.keySet());
    return builder.build();
  }
  





  public synchronized SessionLogs getAllLogsForSession(SessionId sessionId)
  {
    SessionLogs sessionLogs = new SessionLogs();
    if (perSessionDriverEntries.containsKey(sessionId)) {
      Map<String, LogEntries> typeToEntriesMap = (Map)perSessionDriverEntries.get(sessionId);
      for (String logType : typeToEntriesMap.keySet()) {
        sessionLogs.addLog(logType, (LogEntries)typeToEntriesMap.get(logType));
      }
      perSessionDriverEntries.remove(sessionId);
    }
    return sessionLogs;
  }
  





  public synchronized LogEntries getSessionLog(SessionId sessionId)
    throws IOException
  {
    List<LogEntry> entries = Lists.newLinkedList();
    LogRecord[] records = records(sessionId);
    if (records != null) {
      for (LogRecord record : records) {
        if (record.getLevel().intValue() >= serverLogLevel.intValue())
          entries.add(new LogEntry(record.getLevel(), record.getMillis(), record.getMessage()));
      }
    }
    return new LogEntries(entries);
  }
  






  public synchronized void fetchAndStoreLogsFromDriver(SessionId sessionId, WebDriver driver)
    throws IOException
  {
    if (!perSessionDriverEntries.containsKey(sessionId)) {
      perSessionDriverEntries.put(sessionId, Maps.newHashMap());
    }
    Map<String, LogEntries> typeToEntriesMap = (Map)perSessionDriverEntries.get(sessionId);
    if (storeLogsOnSessionQuit) {
      typeToEntriesMap.put("server", getSessionLog(sessionId));
      Set<String> logTypeSet = driver.manage().logs().getAvailableLogTypes();
      for (String logType : logTypeSet) {
        typeToEntriesMap.put(logType, driver.manage().logs().get(logType));
      }
    }
  }
  





  public void configureLogging(LoggingPreferences prefs)
  {
    if (prefs == null) {
      return;
    }
    if (prefs.getEnabledLogTypes().contains("server")) {
      serverLogLevel = prefs.getLevel("server");
    }
  }
  
  public synchronized void publish(LogRecord record)
  {
    ThreadKey threadId = new ThreadKey();
    SessionId sessionId = (SessionId)threadToSessionMap.get(threadId);
    
    if (sessionId != null) {
      List<LogRecord> records = (List)perSessionRecords.get(sessionId);
      if (records == null) {
        records = new ArrayList();
      }
      records.add(record);
      perSessionRecords.put(sessionId, records);
      
      if (records.size() > capacity) {
        perSessionRecords.put(sessionId, new ArrayList());
        try
        {
          logFileRepository.flushRecordsToLogFile(sessionId, records);
          
          records.clear();
        } catch (IOException ex) {
          ex.printStackTrace();
        }
      }
    } else {
      List<LogRecord> records = (List)perThreadTempRecords.get(threadId);
      if (records == null) {
        records = new ArrayList();
        perThreadTempRecords.put(threadId, records);
      }
      records.add(record);
    }
  }
  

  public void flush() {}
  

  public synchronized void close()
    throws SecurityException
  {
    perSessionRecords.clear();
    perThreadTempRecords.clear();
  }
  
  private LogRecord[] records(SessionId sessionId) throws IOException {
    List<LogRecord> logFileRecords = logFileRepository.getLogRecords(sessionId);
    List<LogRecord> records = (List)perSessionRecords.remove(sessionId);
    if (records != null) {
      logFileRecords.addAll(records);
    }
    logFileRepository.removeLogFile(sessionId);
    return (LogRecord[])logFileRecords.toArray(new LogRecord[logFileRecords.size()]);
  }
  
  private String formattedRecords(SessionId sessionId)
    throws IOException
  {
    StringWriter writer = new StringWriter();
    for (LogRecord record : records(sessionId)) {
      writer.append(formatter.format(record));
    }
    return writer.toString();
  }
  
  protected static class ThreadKey
  {
    private final String name;
    private final Long id;
    
    ThreadKey() {
      name = Thread.currentThread().toString();
      id = Long.valueOf(Thread.currentThread().getId());
    }
    
    public boolean equals(Object o)
    {
      if (this == o) {
        return true;
      }
      if ((o == null) || (getClass() != o.getClass())) {
        return false;
      }
      
      ThreadKey threadKey = (ThreadKey)o;
      
      return id != null ? id.equals(id) : id == null;
    }
    

    public int hashCode()
    {
      return id != null ? id.hashCode() : 0;
    }
    
    public String toString()
    {
      return "id" + id + "," + name;
    }
  }
}
