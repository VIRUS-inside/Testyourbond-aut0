package org.openqa.selenium.remote.server.log;

import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;



























public class StdOutHandler
  extends StreamHandler
{
  public StdOutHandler()
  {
    setOutputStream(System.out);
  }
  

  public synchronized void publish(LogRecord record)
  {
    super.publish(record);
    flush();
  }
}
