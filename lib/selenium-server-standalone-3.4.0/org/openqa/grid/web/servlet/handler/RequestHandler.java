package org.openqa.grid.web.servlet.handler;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import org.openqa.grid.common.exception.ClientGoneException;
import org.openqa.grid.common.exception.GridException;
import org.openqa.grid.internal.ExternalSessionKey;
import org.openqa.grid.internal.Registry;
import org.openqa.grid.internal.RemoteProxy;
import org.openqa.grid.internal.SessionTerminationReason;
import org.openqa.grid.internal.TestSession;
import org.openqa.grid.internal.TestSlot;
import org.openqa.grid.internal.exception.NewSessionException;
import org.openqa.grid.internal.listeners.Prioritizer;
import org.openqa.grid.internal.listeners.TestSessionListener;
import org.openqa.grid.internal.utils.configuration.GridHubConfiguration;
import org.openqa.selenium.remote.DesiredCapabilities;

























public class RequestHandler
  implements Comparable<RequestHandler>
{
  private final Registry registry;
  private final SeleniumBasedRequest request;
  private final HttpServletResponse response;
  private volatile TestSession session = null;
  

  private final CountDownLatch sessionAssigned = new CountDownLatch(1);
  
  private static final Logger log = Logger.getLogger(RequestHandler.class.getName());
  

  private final Thread waitingThread;
  

  public RequestHandler(SeleniumBasedRequest request, HttpServletResponse response, Registry registry)
  {
    this.request = request;
    this.response = response;
    this.registry = registry;
    waitingThread = Thread.currentThread();
  }
  







  public void forwardNewSessionRequestAndUpdateRegistry(TestSession session)
    throws NewSessionException
  {
    try
    {
      session.forward(getRequest(), getResponse(), true);
    }
    catch (IOException e) {
      throw new NewSessionException("Error forwarding the request " + e.getMessage(), e);
    }
  }
  
  protected void forwardRequest(TestSession session, RequestHandler handler) throws IOException {
    session.forward(request, response, false);
  }
  


  public void process()
  {
    switch (1.$SwitchMap$org$openqa$grid$web$servlet$handler$RequestType[request.getRequestType().ordinal()]) {
    case 1: 
      log.info("Got a request to create a new session: " + new DesiredCapabilities(request
        .getDesiredCapabilities()));
      try {
        registry.addNewSessionRequest(this);
        waitForSessionBound();
        beforeSessionEvent();
        forwardNewSessionRequestAndUpdateRegistry(session);
      } catch (Exception e) {
        cleanup();
        throw new GridException("Error forwarding the new session " + e.getMessage(), e);
      }
    
    case 2: 
    case 3: 
      session = getSession();
      if (session == null) {
        ExternalSessionKey sessionKey = null;
        try {
          sessionKey = request.extractSession();
        }
        catch (RuntimeException localRuntimeException) {}
        throw new GridException("Session [" + sessionKey + "] not available - " + registry.getActiveSessions());
      }
      try {
        forwardRequest(session, this);
      } catch (ClientGoneException e) {
        log.log(Level.WARNING, "The client is gone for session " + session + ", terminating");
        registry.terminate(session, SessionTerminationReason.CLIENT_GONE);
      } catch (SocketTimeoutException e) {
        log.log(Level.SEVERE, "Socket timed out for session " + session + ", " + e.getMessage());
        registry.terminate(session, SessionTerminationReason.SO_TIMEOUT);
      } catch (Throwable t) {
        log.log(Level.SEVERE, "cannot forward the request " + t.getMessage(), t);
        registry.terminate(session, SessionTerminationReason.FORWARDING_TO_NODE_FAILED);
        throw new GridException("cannot forward the request " + t.getMessage(), t);
      }
      
      if (request.getRequestType() == RequestType.STOP_SESSION) {
        registry.terminate(session, SessionTerminationReason.CLIENT_STOPPED_SESSION);
      }
      break;
    default: 
      throw new RuntimeException("NI");
    }
  }
  
  private void cleanup()
  {
    registry.removeNewSessionRequest(this);
    if (session != null) {
      registry.terminate(session, SessionTerminationReason.CREATIONFAILED);
    }
  }
  




  private void beforeSessionEvent()
    throws NewSessionException
  {
    RemoteProxy p = session.getSlot().getProxy();
    if ((p instanceof TestSessionListener)) {
      try {
        ((TestSessionListener)p).beforeSession(session);
      } catch (Exception e) {
        log.severe("Error running the beforeSessionListener : " + e.getMessage());
        e.printStackTrace();
        throw new NewSessionException("The listener threw an exception ( listener bug )", e);
      }
    }
  }
  







  public void waitForSessionBound()
    throws InterruptedException, TimeoutException
  {
    Integer newSessionWaitTimeout = Integer.valueOf(registry.getConfiguration().newSessionWaitTimeout != null ? 
      registry.getConfiguration().newSessionWaitTimeout.intValue() : 0);
    if (newSessionWaitTimeout.intValue() > 0) {
      if (!sessionAssigned.await(newSessionWaitTimeout.longValue(), TimeUnit.MILLISECONDS)) {
        throw new TimeoutException("Request timed out waiting for a node to become available.");
      }
    }
    else {
      sessionAssigned.await();
    }
  }
  


  public SeleniumBasedRequest getRequest()
  {
    return request;
  }
  


  public HttpServletResponse getResponse()
  {
    return response;
  }
  
  public int compareTo(RequestHandler o) {
    if (registry.getConfiguration().prioritizer != null) {
      return registry.getConfiguration().prioritizer.compareTo(getRequest().getDesiredCapabilities(), o.getRequest()
        .getDesiredCapabilities());
    }
    return 0;
  }
  
  protected void setSession(TestSession session) {
    this.session = session;
  }
  
  public void bindSession(TestSession session) {
    this.session = session;
    sessionAssigned.countDown();
  }
  
  public TestSession getSession() {
    if (session == null) {
      ExternalSessionKey externalKey = request.extractSession();
      session = registry.getExistingSession(externalKey);
    }
    return session;
  }
  




  public ExternalSessionKey getServerSession()
  {
    if (session == null) {
      return null;
    }
    return session.getExternalKey();
  }
  
  public void stop() {
    waitingThread.interrupt();
  }
  
  public String toString()
  {
    StringBuilder b = new StringBuilder();
    b.append("session:").append(session).append(", ");
    b.append("caps: ").append(request.getDesiredCapabilities());
    b.append("\n");
    return b.toString();
  }
  
  public String debug() {
    StringBuilder b = new StringBuilder();
    b.append("\nmethod: ").append(request.getMethod());
    b.append("\npathInfo: ").append(request.getPathInfo());
    b.append("\nuri: ").append(request.getRequestURI());
    b.append("\ncontent :").append(request.getBody());
    return b.toString();
  }
  
  public int hashCode()
  {
    int prime = 31;
    int result = 1;
    result = 31 * result + (session == null ? 0 : session.hashCode());
    return result;
  }
  
  public boolean equals(Object obj)
  {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    RequestHandler other = (RequestHandler)obj;
    if (session == null) {
      if (session != null) {
        return false;
      }
    } else if (!session.equals(session)) {
      return false;
    }
    return true;
  }
  
  public Registry getRegistry() {
    return registry;
  }
}
