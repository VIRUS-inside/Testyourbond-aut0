package org.seleniumhq.jetty9.server;

import java.io.IOException;
import org.seleniumhq.jetty9.util.annotation.ManagedObject;
import org.seleniumhq.jetty9.util.log.Slf4jLog;























@ManagedObject("NCSA standard format request log to slf4j bridge")
public class Slf4jRequestLog
  extends AbstractNCSARequestLog
{
  private Slf4jLog logger;
  private String loggerName;
  
  public Slf4jRequestLog()
  {
    loggerName = "org.seleniumhq.jetty9.server.RequestLog";
  }
  
  public void setLoggerName(String loggerName)
  {
    this.loggerName = loggerName;
  }
  
  public String getLoggerName()
  {
    return loggerName;
  }
  

  protected boolean isEnabled()
  {
    return logger != null;
  }
  
  public void write(String requestEntry)
    throws IOException
  {
    logger.info(requestEntry, new Object[0]);
  }
  
  protected synchronized void doStart()
    throws Exception
  {
    logger = new Slf4jLog(loggerName);
    super.doStart();
  }
}
