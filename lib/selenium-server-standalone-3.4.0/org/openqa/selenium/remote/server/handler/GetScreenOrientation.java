package org.openqa.selenium.remote.server.handler;

import org.openqa.selenium.Rotatable;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.remote.server.Session;
















public class GetScreenOrientation
  extends WebDriverHandler<ScreenOrientation>
{
  public GetScreenOrientation(Session session)
  {
    super(session);
  }
  
  public ScreenOrientation call() throws Exception
  {
    return ((Rotatable)getUnwrappedDriver()).getOrientation();
  }
  
  public String toString()
  {
    return "[get screen orientation]";
  }
}
