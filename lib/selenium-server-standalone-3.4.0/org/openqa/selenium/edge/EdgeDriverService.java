package org.openqa.selenium.edge;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.ImmutableMap;
import java.io.File;
import java.io.IOException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.service.DriverService;
import org.openqa.selenium.remote.service.DriverService.Builder;
























public class EdgeDriverService
  extends DriverService
{
  public static final String EDGE_DRIVER_EXE_PROPERTY = "webdriver.edge.driver";
  
  public EdgeDriverService(File executable, int port, ImmutableList<String> args, ImmutableMap<String, String> environment)
    throws IOException
  {
    super(executable, port, args, environment);
  }
  







  public static EdgeDriverService createDefaultService()
  {
    return (EdgeDriverService)((Builder)new Builder().usingAnyFreePort()).build();
  }
  
  public static class Builder extends DriverService.Builder<EdgeDriverService, Builder>
  {
    public Builder() {}
    
    protected File findDefaultExecutable() {
      return EdgeDriverService.findExecutable("MicrosoftWebDriver", "webdriver.edge.driver", "https://github.com/SeleniumHQ/selenium/wiki/MicrosoftWebDriver", "http://go.microsoft.com/fwlink/?LinkId=619687");
    }
    


    protected ImmutableList<String> createArgs()
    {
      ImmutableList.Builder<String> argsBuilder = ImmutableList.builder();
      argsBuilder.add(String.format("--port=%d", new Object[] { Integer.valueOf(getPort()) }));
      
      return argsBuilder.build();
    }
    
    protected EdgeDriverService createDriverService(File exe, int port, ImmutableList<String> args, ImmutableMap<String, String> environment)
    {
      try
      {
        return new EdgeDriverService(exe, port, args, environment);
      } catch (IOException e) {
        throw new WebDriverException(e);
      }
    }
  }
}
