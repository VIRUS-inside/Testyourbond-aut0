package org.openqa.selenium.ie;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.ImmutableMap;
import java.io.File;
import java.io.IOException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.service.DriverService;
import org.openqa.selenium.remote.service.DriverService.Builder;
























































public class InternetExplorerDriverService
  extends DriverService
{
  public static final String IE_DRIVER_EXE_PROPERTY = "webdriver.ie.driver";
  public static final String IE_DRIVER_LOGFILE_PROPERTY = "webdriver.ie.driver.logfile";
  public static final String IE_DRIVER_LOGLEVEL_PROPERTY = "webdriver.ie.driver.loglevel";
  public static final String IE_DRIVER_ENGINE_PROPERTY = "webdriver.ie.driver.engine";
  public static final String IE_DRIVER_HOST_PROPERTY = "webdriver.ie.driver.host";
  public static final String IE_DRIVER_EXTRACT_PATH_PROPERTY = "webdriver.ie.driver.extractpath";
  public static final String IE_DRIVER_SILENT_PROPERTY = "webdriver.ie.driver.silent";
  
  private InternetExplorerDriverService(File executable, int port, ImmutableList<String> args, ImmutableMap<String, String> environment)
    throws IOException
  {
    super(executable, port, args, environment);
  }
  







  public static InternetExplorerDriverService createDefaultService()
  {
    return (InternetExplorerDriverService)((Builder)new Builder().usingAnyFreePort()).build();
  }
  

  public static class Builder
    extends DriverService.Builder<InternetExplorerDriverService, Builder>
  {
    private InternetExplorerDriverLogLevel logLevel;
    
    private InternetExplorerDriverEngine engineImplementation;
    
    private String host = null;
    private File extractPath = null;
    private Boolean silent = null;
    private Boolean forceCreateProcess = null;
    private String ieSwitches = null;
    


    public Builder() {}
    

    public Builder withLogLevel(InternetExplorerDriverLogLevel logLevel)
    {
      this.logLevel = logLevel;
      return this;
    }
    





    public Builder withEngineImplementation(InternetExplorerDriverEngine engineImplementation)
    {
      this.engineImplementation = engineImplementation;
      return this;
    }
    





    public Builder withHost(String host)
    {
      this.host = host;
      return this;
    }
    





    public Builder withExtractPath(File extractPath)
    {
      this.extractPath = extractPath;
      return this;
    }
    





    public Builder withSilent(Boolean silent)
    {
      this.silent = silent;
      return this;
    }
    
    protected File findDefaultExecutable()
    {
      return InternetExplorerDriverService.findExecutable("IEDriverServer", "webdriver.ie.driver", "https://github.com/SeleniumHQ/selenium/wiki/InternetExplorerDriver", "http://selenium-release.storage.googleapis.com/index.html");
    }
    


    protected ImmutableList<String> createArgs()
    {
      if (getLogFile() == null) {
        String logFilePath = System.getProperty("webdriver.ie.driver.logfile");
        if (logFilePath != null) {
          withLogFile(new File(logFilePath));
        }
      }
      if (logLevel == null) {
        String level = System.getProperty("webdriver.ie.driver.loglevel");
        if (level != null) {
          logLevel = InternetExplorerDriverLogLevel.valueOf(level);
        }
      }
      if (engineImplementation == null) {
        String engineToUse = System.getProperty("webdriver.ie.driver.engine");
        if (engineToUse != null) {
          engineImplementation = InternetExplorerDriverEngine.valueOf(engineToUse);
        }
      }
      if (host == null) {
        String hostProperty = System.getProperty("webdriver.ie.driver.host");
        if (hostProperty != null) {
          host = hostProperty;
        }
      }
      if (extractPath == null) {
        String extractPathProperty = System.getProperty("webdriver.ie.driver.extractpath");
        if (extractPathProperty != null) {
          extractPath = new File(extractPathProperty);
        }
      }
      if (silent == null) {
        String silentProperty = System.getProperty("webdriver.ie.driver.silent");
        if (silentProperty != null) {
          silent = Boolean.valueOf(silentProperty);
        }
      }
      
      ImmutableList.Builder<String> argsBuilder = ImmutableList.builder();
      argsBuilder.add(String.format("--port=%d", new Object[] { Integer.valueOf(getPort()) }));
      if (getLogFile() != null) {
        argsBuilder.add(String.format("--log-file=\"%s\"", new Object[] { getLogFile().getAbsolutePath() }));
      }
      if (logLevel != null) {
        argsBuilder.add(String.format("--log-level=%s", new Object[] { logLevel.toString() }));
      }
      if (engineImplementation != null) {
        argsBuilder.add(String.format("--implementation=%s", new Object[] { engineImplementation.toString() }));
      }
      if (host != null) {
        argsBuilder.add(String.format("--host=%s", new Object[] { host }));
      }
      if (extractPath != null) {
        argsBuilder.add(String.format("--extract-path=\"%s\"", new Object[] { extractPath.getAbsolutePath() }));
      }
      if ((silent != null) && (silent.equals(Boolean.TRUE))) {
        argsBuilder.add("--silent");
      }
      
      return argsBuilder.build();
    }
    

    protected InternetExplorerDriverService createDriverService(File exe, int port, ImmutableList<String> args, ImmutableMap<String, String> environment)
    {
      try
      {
        return new InternetExplorerDriverService(exe, port, args, environment, null);
      } catch (IOException e) {
        throw new WebDriverException(e);
      }
    }
  }
}
