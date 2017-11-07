package org.openqa.selenium.remote.server.handler.interactions;

import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.remote.server.Session;
import org.openqa.selenium.remote.server.handler.WebDriverHandler;
















public class MouseDown
  extends WebDriverHandler<Void>
{
  public MouseDown(Session session)
  {
    super(session);
  }
  
  public Void call() throws Exception
  {
    Mouse mouse = ((HasInputDevices)getDriver()).getMouse();
    mouse.mouseDown(null);
    return null;
  }
  
  public String toString()
  {
    return String.format("[mousedown: no args]", new Object[0]);
  }
}
