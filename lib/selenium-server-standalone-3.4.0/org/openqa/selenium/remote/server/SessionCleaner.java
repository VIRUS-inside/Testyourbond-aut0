package org.openqa.selenium.remote.server;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.internal.Killable;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.remote.server.handler.DeleteSession;
import org.openqa.selenium.remote.server.log.LoggingManager;
import org.openqa.selenium.remote.server.log.PerSessionLogHandler;
import org.openqa.selenium.support.events.EventFiringWebDriver;


















class SessionCleaner
  extends Thread
{
  private final DriverSessions driverSessions;
  private final long clientGoneTimeout;
  private final long insideBrowserTimeout;
  private final long sleepInterval;
  private final Logger log;
  private final Clock clock;
  private volatile boolean running = true;
  
  SessionCleaner(DriverSessions driverSessions, Logger log, long clientGoneTimeout, long insideBrowserTimeout) {
    this(driverSessions, log, new SystemClock(), clientGoneTimeout, insideBrowserTimeout);
  }
  
  SessionCleaner(DriverSessions driverSessions, Logger log, Clock clock, long clientGoneTimeout, long insideBrowserTimeout) {
    super("DriverServlet Session Cleaner");
    this.log = log;
    this.clock = clock;
    this.clientGoneTimeout = clientGoneTimeout;
    this.insideBrowserTimeout = insideBrowserTimeout;
    this.driverSessions = driverSessions;
    if ((clientGoneTimeout == 0L) && (insideBrowserTimeout == 0L)) {
      throw new IllegalStateException("SessionCleaner not supposed to start when no timeouts specified");
    }
    if ((insideBrowserTimeout > 0L) && (insideBrowserTimeout < 60000L)) {
      log.warning("The specified browser timeout is TOO LOW for safe operations and may have other side-effects.\n Please specify a slightly higher browserTimeout.");
    }
    
    long lowestNonZero = Math.min(insideBrowserTimeout > 0L ? insideBrowserTimeout : clientGoneTimeout, clientGoneTimeout > 0L ? clientGoneTimeout : insideBrowserTimeout);
    
    sleepInterval = (lowestNonZero / 10L);
    
    log.info(String.format("SessionCleaner initialized with insideBrowserTimeout %d and clientGoneTimeout %d polling every %d", new Object[] { Long.valueOf(this.insideBrowserTimeout), Long.valueOf(this.clientGoneTimeout), Long.valueOf(sleepInterval) }));
  }
  


  public void run()
  {
    while (running) {
      checkExpiry();
      clock.pass(sleepInterval);
    }
  }
  
  void stopCleaner() {
    running = false;
    synchronized (this) {
      interrupt();
    }
  }
  
  void checkExpiry()
  {
    for (SessionId sessionId : driverSessions.getSessions()) {
      Session session = driverSessions.get(sessionId);
      if (session != null) {
        boolean useDeleteSession = false;
        boolean killed = false;
        
        boolean inUse = session.isInUse();
        if ((!inUse) && (session.isTimedOut(clientGoneTimeout))) {
          useDeleteSession = true;
          log.info("Session " + session.getSessionId() + " deleted due to client timeout");
        }
        if ((inUse) && (session.isTimedOut(insideBrowserTimeout))) {
          WebDriver driver = session.getDriver();
          if ((driver instanceof EventFiringWebDriver)) {
            driver = ((EventFiringWebDriver)driver).getWrappedDriver();
          }
          if ((driver instanceof Killable))
          {
            ((Killable)driver).kill();
            killed = true;
            log.warning("Browser killed and session " + session.getSessionId() + " terminated due to in-browser timeout.");
          } else {
            useDeleteSession = true;
            log.warning("Session " + session.getSessionId() + " deleted due to in-browser timeout. Terminating driver with DeleteSession since it does not support Killable, the driver in question does not support selenium-server timeouts fully");
          }
        }
        


        if (useDeleteSession) {
          DeleteSession deleteSession = new DeleteSession(session);
          try {
            deleteSession.call();
          } catch (Exception e) {
            log.log(Level.WARNING, "Could not delete session " + session.getSessionId(), e); }
          continue;
        }
        

        if ((useDeleteSession) || (killed)) {
          driverSessions.deleteSession(sessionId);
          PerSessionLogHandler logHandler = LoggingManager.perSessionLogHandler();
          logHandler.transferThreadTempLogsToSessionLogs(sessionId);
          logHandler.removeSessionLogs(sessionId);
        }
      }
    }
  }
}
