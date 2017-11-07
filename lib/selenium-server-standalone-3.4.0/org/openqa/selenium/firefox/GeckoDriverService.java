package org.openqa.selenium.firefox;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.ByteStreams;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.net.PortProber;
import org.openqa.selenium.remote.service.DriverService;
import org.openqa.selenium.remote.service.DriverService.Builder;

































public class GeckoDriverService
  extends DriverService
{
  public static final String GECKO_DRIVER_EXE_PROPERTY = "webdriver.gecko.driver";
  
  public GeckoDriverService(File executable, int port, ImmutableList<String> args, ImmutableMap<String, String> environment)
    throws IOException
  {
    super(executable, port, args, environment);
  }
  







  public static GeckoDriverService createDefaultService()
  {
    return (GeckoDriverService)((Builder)new Builder().usingAnyFreePort()).build();
  }
  
  protected void waitUntilAvailable() throws MalformedURLException
  {
    PortProber.waitForPortUp(getUrl().getPort(), 20, TimeUnit.SECONDS);
  }
  



  public static class Builder
    extends DriverService.Builder<GeckoDriverService, Builder>
  {
    private FirefoxBinary firefoxBinary;
    



    public Builder() {}
    


    @Deprecated
    public Builder(FirefoxBinary binary)
    {
      firefoxBinary = binary;
    }
    





    public Builder usingFirefoxBinary(FirefoxBinary firefoxBinary)
    {
      Preconditions.checkNotNull(firefoxBinary);
      GeckoDriverService.checkExecutable(firefoxBinary.getFile());
      this.firefoxBinary = firefoxBinary;
      return this;
    }
    
    protected File findDefaultExecutable()
    {
      return GeckoDriverService.findExecutable("geckodriver", "webdriver.gecko.driver", "https://github.com/mozilla/geckodriver", "https://github.com/mozilla/geckodriver/releases");
    }
    



    protected ImmutableList<String> createArgs()
    {
      ImmutableList.Builder<String> argsBuilder = ImmutableList.builder();
      argsBuilder.add(String.format("--port=%d", new Object[] { Integer.valueOf(getPort()) }));
      if (firefoxBinary != null) {
        argsBuilder.add("-b");
        argsBuilder.add(firefoxBinary.getPath());
      }
      return argsBuilder.build();
    }
    

    protected GeckoDriverService createDriverService(File exe, int port, ImmutableList<String> args, ImmutableMap<String, String> environment)
    {
      try
      {
        GeckoDriverService service = new GeckoDriverService(exe, port, args, environment);
        if (getLogFile() != null)
        {
          service.sendOutputTo(new FileOutputStream(getLogFile()));
        } else {
          String firefoxLogFile = System.getProperty("webdriver.firefox.logfile");
          if (firefoxLogFile != null) {
            if ("/dev/stdout".equals(firefoxLogFile)) {
              service.sendOutputTo(System.out);
            } else if ("/dev/stderr".equals(firefoxLogFile)) {
              service.sendOutputTo(System.err);
            } else if ("/dev/null".equals(firefoxLogFile)) {
              service.sendOutputTo(ByteStreams.nullOutputStream());
            }
            else {
              service.sendOutputTo(new FileOutputStream(firefoxLogFile));
            }
          }
        }
        return service;
      } catch (IOException e) {
        throw new WebDriverException(e);
      }
    }
  }
}
