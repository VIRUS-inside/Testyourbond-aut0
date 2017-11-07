package org.openqa.selenium.remote.service;

import com.google.common.base.Throwables;
import java.io.IOException;
import java.net.ConnectException;
import java.util.Map;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.Command;
import org.openqa.selenium.remote.CommandInfo;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.openqa.selenium.remote.Response;






























public class DriverCommandExecutor
  extends HttpCommandExecutor
{
  private final DriverService service;
  
  public DriverCommandExecutor(DriverService service)
  {
    super(service.getUrl());
    this.service = service;
  }
  







  protected DriverCommandExecutor(DriverService service, Map<String, CommandInfo> additionalCommands)
  {
    super(additionalCommands, service.getUrl());
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
        throw new WebDriverException("The driver server has unexpectedly died!", t);
      }
      Throwables.throwIfUnchecked(t);
      throw new WebDriverException(t);
    } finally {
      if ("quit".equals(command.getName())) {
        service.stop();
      }
    }
  }
}
