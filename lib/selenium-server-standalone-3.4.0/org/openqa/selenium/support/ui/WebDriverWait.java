package org.openqa.selenium.support.ui;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;




























public class WebDriverWait
  extends FluentWait<WebDriver>
{
  public static final long DEFAULT_SLEEP_TIMEOUT = 500L;
  private final WebDriver driver;
  
  public WebDriverWait(WebDriver driver, long timeOutInSeconds)
  {
    this(driver, new SystemClock(), Sleeper.SYSTEM_SLEEPER, timeOutInSeconds, 500L);
  }
  









  public WebDriverWait(WebDriver driver, long timeOutInSeconds, long sleepInMillis)
  {
    this(driver, new SystemClock(), Sleeper.SYSTEM_SLEEPER, timeOutInSeconds, sleepInMillis);
  }
  







  public WebDriverWait(WebDriver driver, Clock clock, Sleeper sleeper, long timeOutInSeconds, long sleepTimeOut)
  {
    super(driver, clock, sleeper);
    withTimeout(timeOutInSeconds, TimeUnit.SECONDS);
    pollingEvery(sleepTimeOut, TimeUnit.MILLISECONDS);
    ignoring(NotFoundException.class);
    this.driver = driver;
  }
  
  protected RuntimeException timeoutException(String message, Throwable lastException)
  {
    TimeoutException ex = new TimeoutException(message, lastException);
    ex.addInfo("Driver info", driver.getClass().getName());
    if ((driver instanceof RemoteWebDriver)) {
      RemoteWebDriver remote = (RemoteWebDriver)driver;
      if (remote.getSessionId() != null) {
        ex.addInfo("Session ID", remote.getSessionId().toString());
      }
      if (remote.getCapabilities() != null) {
        ex.addInfo("Capabilities", remote.getCapabilities().toString());
      }
    }
    throw ex;
  }
}
