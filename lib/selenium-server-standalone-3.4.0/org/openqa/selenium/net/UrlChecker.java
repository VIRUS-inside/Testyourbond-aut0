package org.openqa.selenium.net;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Throwables;
import com.google.common.util.concurrent.SimpleTimeLimiter;
import com.google.common.util.concurrent.TimeLimiter;
import com.google.common.util.concurrent.UncheckedTimeoutException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;


























public class UrlChecker
{
  private static final Logger log = Logger.getLogger(UrlChecker.class.getName());
  
  private static final int CONNECT_TIMEOUT_MS = 500;
  
  private static final int READ_TIMEOUT_MS = 1000;
  private static final long MAX_POLL_INTERVAL_MS = 320L;
  private static final long MIN_POLL_INTERVAL_MS = 10L;
  private static final AtomicInteger THREAD_COUNTER = new AtomicInteger(1);
  
  private static final ExecutorService THREAD_POOL = Executors.newCachedThreadPool(new ThreadFactory()
  {
    public Thread newThread(Runnable r) {
      Thread t = new Thread(r, "UrlChecker-" + UrlChecker.THREAD_COUNTER.incrementAndGet());
      t.setDaemon(true);
      return t;
    }
  });
  


  private final TimeLimiter timeLimiter;
  



  public UrlChecker()
  {
    this(new SimpleTimeLimiter(THREAD_POOL));
  }
  
  @VisibleForTesting
  UrlChecker(TimeLimiter timeLimiter) {
    this.timeLimiter = timeLimiter;
  }
  
  public void waitUntilAvailable(long timeout, TimeUnit unit, final URL... urls) throws UrlChecker.TimeoutException
  {
    long start = System.nanoTime();
    log.fine("Waiting for " + Arrays.toString(urls));
    try {
      timeLimiter.callWithTimeout(new Callable() {
        public Void call() throws InterruptedException {
          HttpURLConnection connection = null;
          
          long sleepMillis = 10L;
          for (;;) {
            for (URL url : urls) {
              try {
                UrlChecker.log.fine("Polling " + url);
                connection = UrlChecker.this.connectToUrl(url);
                if (connection.getResponseCode() == 200) {
                  return null;
                }
              }
              catch (IOException localIOException1) {}finally
              {
                if (connection != null) {
                  connection.disconnect();
                }
              }
            }
            TimeUnit.MILLISECONDS.sleep(sleepMillis);
            sleepMillis = sleepMillis >= 320L ? sleepMillis : sleepMillis * 2L; } } }, timeout, unit, true);

    }
    catch (UncheckedTimeoutException e)
    {
      throw new TimeoutException(String.format("Timed out waiting for %s to be available after %d ms", new Object[] {
      
        Arrays.toString(urls), Long.valueOf(TimeUnit.MILLISECONDS.convert(System.nanoTime() - start, TimeUnit.NANOSECONDS)) }), e);
    } catch (Exception e) {
      throw Throwables.propagate(e);
    }
  }
  
  public void waitUntilUnavailable(long timeout, TimeUnit unit, final URL url) throws UrlChecker.TimeoutException
  {
    long start = System.nanoTime();
    log.fine("Waiting for " + url);
    try {
      timeLimiter.callWithTimeout(new Callable() {
        public Void call() throws InterruptedException {
          HttpURLConnection connection = null;
          
          long sleepMillis = 10L;
          for (;;) {
            try {
              UrlChecker.log.fine("Polling " + url);
              connection = UrlChecker.this.connectToUrl(url);
              if (connection.getResponseCode() != 200) {
                return null;
              }
            } catch (IOException e) {
              return null;
            } finally {
              if (connection != null) {
                connection.disconnect();
              }
            }
            
            TimeUnit.MILLISECONDS.sleep(sleepMillis);
            sleepMillis = sleepMillis >= 320L ? sleepMillis : sleepMillis * 2L; } } }, timeout, unit, true);

    }
    catch (UncheckedTimeoutException e)
    {
      throw new TimeoutException(String.format("Timed out waiting for %s to become unavailable after %d ms", new Object[] { url, 
      
        Long.valueOf(TimeUnit.MILLISECONDS.convert(System.nanoTime() - start, TimeUnit.NANOSECONDS)) }), e);
    } catch (Exception e) {
      throw Throwables.propagate(e);
    }
  }
  
  private HttpURLConnection connectToUrl(URL url) throws IOException {
    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
    connection.setConnectTimeout(500);
    connection.setReadTimeout(1000);
    connection.connect();
    return connection;
  }
  
  public static class TimeoutException extends Exception {
    public TimeoutException(String s, Throwable throwable) {
      super(throwable);
    }
  }
}
