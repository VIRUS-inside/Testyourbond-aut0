package org.openqa.selenium.phantomjs;

import com.google.common.base.Throwables;
import java.io.IOException;
import java.net.ConnectException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.Command;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.openqa.selenium.remote.Response;













































class PhantomJSCommandExecutor
  extends HttpCommandExecutor
{
  private final PhantomJSDriverService service;
  
  PhantomJSCommandExecutor(PhantomJSDriverService service)
  {
    super(PhantomJSDriver.getCustomCommands(), service.getUrl());
    this.service = service;
  }
  








  public Response execute(Command command)
    throws IOException
  {
    if ("newSession".equals(command.getName())) {
      service.start();
    }
    try
    {
      return super.execute(command);
    } catch (Throwable t) {
      Throwable rootCause = Throwables.getRootCause(t);
      if (((rootCause instanceof ConnectException)) && 
        ("Connection refused".equals(rootCause.getMessage())) && 
        (!service.isRunning())) {
        throw new WebDriverException("The PhantomJS/GhostDriver server has unexpectedly died!", t);
      }
      Throwables.propagateIfPossible(t);
      throw new WebDriverException(t);
    } finally {
      if ("quit".equals(command.getName())) {
        service.stop();
      }
    }
  }
}
