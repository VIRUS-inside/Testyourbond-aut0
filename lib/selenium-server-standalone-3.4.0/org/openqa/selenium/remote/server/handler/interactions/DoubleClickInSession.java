package org.openqa.selenium.remote.server.handler.interactions;

import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.remote.server.Session;
import org.openqa.selenium.remote.server.handler.WebDriverHandler;
















public class DoubleClickInSession
  extends WebDriverHandler<Void>
{
  public DoubleClickInSession(Session session)
  {
    super(session);
  }
  
  public Void call() throws Exception
  {
    Mouse mouse = ((HasInputDevices)getDriver()).getMouse();
    mouse.doubleClick(null);
    return null;
  }
  
  public String toString()
  {
    return String.format("[doubleclick: no args]", new Object[0]);
  }
}
