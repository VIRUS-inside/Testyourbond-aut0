package org.openqa.selenium.remote.server.handler;

import com.google.common.base.Preconditions;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.remote.server.KnownElements;
import org.openqa.selenium.remote.server.Session;
import org.openqa.selenium.remote.server.rest.RestishHandler;


















public abstract class WebDriverHandler<T>
  implements RestishHandler<T>, Callable<T>
{
  private final Session session;
  
  protected WebDriverHandler(Session session)
  {
    this.session = session;
  }
  
  public final T handle() throws Exception
  {
    FutureTask<T> future = new FutureTask(this);
    try {
      return getSession().execute(future);
    } catch (ExecutionException e) {
      Throwable cause = e.getCause();
      if ((cause instanceof Exception))
        throw ((Exception)cause);
      throw e;
    }
  }
  
  public SessionId getSessionId() {
    return session.getSessionId();
  }
  
  public String getScreenshot() {
    Session session = getSession();
    return session != null ? session.getAndClearScreenshot() : null;
  }
  
  protected WebDriver getDriver() {
    Session session = getSession();
    return session.getDriver();
  }
  
  protected Session getSession() {
    return session;
  }
  
  protected KnownElements getKnownElements() {
    return getSession().getKnownElements();
  }
  
  protected SessionId getRealSessionId() {
    return session == null ? new SessionId("unknown") : session.getSessionId();
  }
  
  protected BySelector newBySelector() {
    return new BySelector();
  }
  
  protected WebDriver getUnwrappedDriver() {
    WebDriver toReturn = getDriver();
    while ((toReturn instanceof WrapsDriver)) {
      toReturn = ((WrapsDriver)toReturn).getWrappedDriver();
    }
    return (WebDriver)Preconditions.checkNotNull(toReturn);
  }
}
