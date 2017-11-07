package org.openqa.selenium.remote.server.log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.LogRecord;
import org.openqa.selenium.remote.SessionId;


















public class SessionLogsToFileRepository
{
  private Map<SessionId, LogFile> sessionToLogFileMap;
  
  public SessionLogsToFileRepository()
  {
    sessionToLogFileMap = new HashMap();
  }
  








  public void createLogFileAndAddToMap(SessionId sessionId)
    throws IOException
  {
    File rcLogFile = File.createTempFile(sessionId.toString(), ".rclog");
    rcLogFile.deleteOnExit();
    LogFile logFile = new LogFile(rcLogFile.getAbsolutePath());
    sessionToLogFileMap.put(sessionId, logFile);
  }
  








  public synchronized void flushRecordsToLogFile(SessionId sessionId, List<LogRecord> records)
    throws IOException
  {
    LogFile logFile = (LogFile)sessionToLogFileMap.get(sessionId);
    
    if (logFile == null) {
      createLogFileAndAddToMap(sessionId);
      logFile = (LogFile)sessionToLogFileMap.get(sessionId);
    }
    
    logFile.openLogWriter();
    for (LogRecord record : records) {
      logFile.getLogWriter().writeObject(record);
    }
    logFile.closeLogWriter();
  }
  






  public List<LogRecord> getLogRecords(SessionId sessionId)
    throws IOException
  {
    LogFile logFile = (LogFile)sessionToLogFileMap.get(sessionId);
    if (logFile == null) {
      return new ArrayList();
    }
    
    List<LogRecord> logRecords = new ArrayList();
    try {
      logFile.openLogReader();
      ObjectInputStream logObjInStream = logFile.getLogReader();
      
      LogRecord tmpLogRecord;
      while (null != (tmpLogRecord = (LogRecord)logObjInStream.readObject())) {
        logRecords.add(tmpLogRecord);
      }
    } catch (IOException ex) {
      logFile.closeLogReader();
      return logRecords;
    } catch (ClassNotFoundException e) {
      logFile.closeLogReader();
      return logRecords;
    }
    logFile.closeLogReader();
    return logRecords;
  }
  
  public void removeLogFile(SessionId sessionId) {
    LogFile logFile = (LogFile)sessionToLogFileMap.get(sessionId);
    sessionToLogFileMap.remove(sessionId);
    if (logFile == null) {
      return;
    }
    try {
      logFile.removeLogFile();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  static class LogFile {
    private String logName;
    private ObjectOutputStream logWriter;
    private ObjectInputStream logReader;
    
    public LogFile(String logName) {
      this.logName = logName;
    }
    
    public void openLogWriter() throws IOException {
      logWriter = new ObjectOutputStream(new FileOutputStream(logName));
    }
    
    public void closeLogWriter() throws IOException {
      if (logWriter != null) {
        logWriter.close();
      }
    }
    
    public void openLogReader() throws IOException {
      logReader = new ObjectInputStream(new FileInputStream(logName));
    }
    
    public void closeLogReader() throws IOException {
      if (logReader != null) {
        logReader.close();
      }
    }
    
    public ObjectOutputStream getLogWriter() {
      return logWriter;
    }
    
    public ObjectInputStream getLogReader() {
      return logReader;
    }
    
    public void removeLogFile() throws IOException {
      if (logName != null) {
        closeLogReader();
        closeLogWriter();
        new File(logName).delete();
      }
    }
  }
}
