package org.openqa.selenium.firefox;

import java.io.IOException;
import java.net.URL;















public class NotConnectedException
  extends IOException
{
  public NotConnectedException(URL url, long timeToWaitInMilliSeconds, String consoleOutput)
  {
    super(getMessage(url, timeToWaitInMilliSeconds, consoleOutput));
  }
  
  private static String getMessage(URL url, long timeToWaitInMilliSeconds, String consoleOutput) {
    return String.format("Unable to connect to host %s on port %d after %d ms. Firefox console output:\n%s", new Object[] {url
      .getHost(), Integer.valueOf(url.getPort()), Long.valueOf(timeToWaitInMilliSeconds), consoleOutput });
  }
}
