package org.apache.commons.exec;

import java.util.Enumeration;
import java.util.Vector;

























public class Watchdog
  implements Runnable
{
  private final Vector<TimeoutObserver> observers = new Vector(1);
  
  private final long timeout;
  
  private boolean stopped = false;
  
  public Watchdog(long timeout) {
    if (timeout < 1L) {
      throw new IllegalArgumentException("timeout must not be less than 1.");
    }
    this.timeout = timeout;
  }
  
  public void addTimeoutObserver(TimeoutObserver to) {
    observers.addElement(to);
  }
  
  public void removeTimeoutObserver(TimeoutObserver to) {
    observers.removeElement(to);
  }
  
  protected final void fireTimeoutOccured() {
    Enumeration<TimeoutObserver> e = observers.elements();
    while (e.hasMoreElements()) {
      ((TimeoutObserver)e.nextElement()).timeoutOccured(this);
    }
  }
  
  public synchronized void start() {
    stopped = false;
    Thread t = new Thread(this, "WATCHDOG");
    t.setDaemon(true);
    t.start();
  }
  
  public synchronized void stop() {
    stopped = true;
    notifyAll();
  }
  
  public void run() {
    long startTime = System.currentTimeMillis();
    boolean isWaiting;
    synchronized (this) {
      long timeLeft = timeout - (System.currentTimeMillis() - startTime);
      isWaiting = timeLeft > 0L;
      while ((!stopped) && (isWaiting)) {
        try {
          wait(timeLeft);
        }
        catch (InterruptedException e) {}
        timeLeft = timeout - (System.currentTimeMillis() - startTime);
        isWaiting = timeLeft > 0L;
      }
    }
    

    if (!isWaiting) {
      fireTimeoutOccured();
    }
  }
}
