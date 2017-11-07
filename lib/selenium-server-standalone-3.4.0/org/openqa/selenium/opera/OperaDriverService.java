package org.openqa.selenium.opera;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.ImmutableMap;
import java.io.File;
import java.io.IOException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.service.DriverService;
import org.openqa.selenium.remote.service.DriverService.Builder;
















































public class OperaDriverService
  extends DriverService
{
  public static final String OPERA_DRIVER_EXE_PROPERTY = "webdriver.opera.driver";
  public static final String OPERA_DRIVER_LOG_PROPERTY = "webdriver.opera.logfile";
  public static final String OPERA_DRIVER_VERBOSE_LOG_PROPERTY = "webdriver.opera.verboseLogging";
  public static final String OPERA_DRIVER_SILENT_OUTPUT_PROPERTY = "webdriver.opera.silentOutput";
  
  public OperaDriverService(File executable, int port, ImmutableList<String> args, ImmutableMap<String, String> environment)
    throws IOException
  {
    super(executable, port, args, environment);
  }
  







  public static OperaDriverService createDefaultService()
  {
    return (OperaDriverService)((Builder)new Builder().usingAnyFreePort()).build();
  }
  



  public static class Builder
    extends DriverService.Builder<OperaDriverService, Builder>
  {
    private boolean verbose = Boolean.getBoolean("webdriver.opera.verboseLogging");
    private boolean silent = Boolean.getBoolean("webdriver.opera.silentOutput");
    


    public Builder() {}
    

    public Builder withVerbose(boolean verbose)
    {
      this.verbose = verbose;
      return this;
    }
    





    public Builder withSilent(boolean silent)
    {
      this.silent = silent;
      return this;
    }
    
    protected File findDefaultExecutable()
    {
      return OperaDriverService.findExecutable("operadriver", "webdriver.opera.driver", "https://github.com/operasoftware/operachromiumdriver", "https://github.com/operasoftware/operachromiumdriver/releases");
    }
    


    protected ImmutableList<String> createArgs()
    {
      if (getLogFile() == null) {
        String logFilePath = System.getProperty("webdriver.opera.logfile");
        if (logFilePath != null) {
          withLogFile(new File(logFilePath));
        }
      }
      
      ImmutableList.Builder<String> argsBuilder = ImmutableList.builder();
      argsBuilder.add(String.format("--port=%d", new Object[] { Integer.valueOf(getPort()) }));
      if (getLogFile() != null) {
        argsBuilder.add(String.format("--log-path=%s", new Object[] { getLogFile().getAbsolutePath() }));
      }
      if (verbose) {
        argsBuilder.add("--verbose");
      }
      if (silent) {
        argsBuilder.add("--silent");
      }
      
      return argsBuilder.build();
    }
    

    protected OperaDriverService createDriverService(File exe, int port, ImmutableList<String> args, ImmutableMap<String, String> environment)
    {
      try
      {
        return new OperaDriverService(exe, port, args, environment);
      } catch (IOException e) {
        throw new WebDriverException(e);
      }
    }
  }
}
