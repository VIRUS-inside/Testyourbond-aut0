package org.openqa.grid.internal;

import com.google.common.base.Predicate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import net.jcip.annotations.ThreadSafe;
import org.openqa.grid.internal.listeners.Prioritizer;
import org.openqa.grid.web.servlet.handler.RequestHandler;
import org.openqa.grid.web.servlet.handler.SeleniumBasedRequest;
import org.openqa.selenium.remote.DesiredCapabilities;


























@ThreadSafe
class NewSessionRequestQueue
{
  private static final Logger log = Logger.getLogger(NewSessionRequestQueue.class.getName());
  
  private final List<RequestHandler> newSessionRequests = new ArrayList();
  

  NewSessionRequestQueue() {}
  

  public synchronized void add(RequestHandler request)
  {
    newSessionRequests.add(request);
  }
  




  public synchronized void processQueue(Predicate<RequestHandler> handlerConsumer, Prioritizer prioritizer)
  {
    List<RequestHandler> copy;
    



    if (prioritizer != null) {
      List<RequestHandler> copy = new ArrayList(newSessionRequests);
      Collections.sort(copy);
    } else {
      copy = newSessionRequests;
    }
    
    List<RequestHandler> matched = new ArrayList();
    for (RequestHandler request : copy) {
      if (handlerConsumer.apply(request)) {
        matched.add(request);
      }
    }
    for (RequestHandler req : matched) {
      boolean ok = removeNewSessionRequest(req);
      if (!ok) {
        log.severe("Bug removing request " + req);
      }
    }
  }
  


  public synchronized void clearNewSessionRequests()
  {
    newSessionRequests.clear();
  }
  




  public synchronized boolean removeNewSessionRequest(RequestHandler request)
  {
    return newSessionRequests.remove(request);
  }
  




  public synchronized Iterable<DesiredCapabilities> getDesiredCapabilities()
  {
    List<DesiredCapabilities> result = new ArrayList();
    for (RequestHandler req : newSessionRequests) {
      result.add(new DesiredCapabilities(req.getRequest().getDesiredCapabilities()));
    }
    return result;
  }
  



  public synchronized int getNewSessionRequestCount()
  {
    return newSessionRequests.size();
  }
  
  public synchronized void stop() {
    for (RequestHandler newSessionRequest : newSessionRequests) {
      newSessionRequest.stop();
    }
  }
}
