package org.openqa.selenium.phantomjs;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.ImmutableMap;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.logging.Logger;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.net.PortProber;
import org.openqa.selenium.os.ExecutableFinder;
import org.openqa.selenium.remote.service.DriverService;







































public class PhantomJSDriverService
  extends DriverService
{
  private static final Logger LOG = Logger.getLogger(PhantomJSDriverService.class.getName());
  





  public static final String PHANTOMJS_EXECUTABLE_PATH_PROPERTY = "phantomjs.binary.path";
  





  public static final String PHANTOMJS_GHOSTDRIVER_PATH_PROPERTY = "phantomjs.ghostdriver.path";
  





  public static final String PHANTOMJS_CLI_ARGS = "phantomjs.cli.args";
  





  public static final String PHANTOMJS_GHOSTDRIVER_CLI_ARGS = "phantomjs.ghostdriver.cli.args";
  





  public static final String PHANTOMJS_PAGE_SETTINGS_PREFIX = "phantomjs.page.settings.";
  




  public static final String PHANTOMJS_PAGE_CUSTOMHEADERS_PREFIX = "phantomjs.page.customHeaders.";
  




  private static final String PHANTOMJS_DEFAULT_LOGFILE = "phantomjsdriver.log";
  




  private static final String PHANTOMJS_DEFAULT_EXECUTABLE = "phantomjs";
  




  private static final String PHANTOMJS_DOC_LINK = "https://github.com/ariya/phantomjs/wiki";
  




  private static final String PHANTOMJS_DOWNLOAD_LINK = "http://phantomjs.org/download.html";
  




  private static final String GHOSTDRIVER_DOC_LINK = "https://github.com/detro/ghostdriver/blob/master/README.md";
  




  private static final String GHOSTDRIVER_DOWNLOAD_LINK = "https://github.com/detro/ghostdriver/downloads";
  





  private PhantomJSDriverService(File executable, int port, ImmutableList<String> args, ImmutableMap<String, String> environment)
    throws IOException
  {
    super(executable, port, args, environment);
    

    LOG.info("executable: " + executable.getAbsolutePath());
    LOG.info("port: " + port);
    LOG.info("arguments: " + args.toString());
    LOG.info("environment: " + environment.toString());
  }
  

















  public static PhantomJSDriverService createDefaultService(Capabilities desiredCapabilities)
  {
    Proxy proxy = null;
    if (desiredCapabilities != null) {
      proxy = Proxy.extractFrom(desiredCapabilities);
    }
    

    File phantomjsfile = findPhantomJS(desiredCapabilities, "https://github.com/ariya/phantomjs/wiki", "http://phantomjs.org/download.html");
    

    File ghostDriverfile = findGhostDriver(desiredCapabilities, "https://github.com/detro/ghostdriver/blob/master/README.md", "https://github.com/detro/ghostdriver/downloads");
    










    return new Builder().usingPhantomJSExecutable(phantomjsfile).usingGhostDriver(ghostDriverfile).usingAnyFreePort().withProxy(proxy).withLogFile(new File("phantomjsdriver.log")).usingCommandLineArguments(findCLIArgumentsFromCaps(desiredCapabilities, "phantomjs.cli.args")).usingGhostDriverCommandLineArguments(findCLIArgumentsFromCaps(desiredCapabilities, "phantomjs.ghostdriver.cli.args")).build();
  }
  







  public static PhantomJSDriverService createDefaultService()
  {
    return createDefaultService(null);
  }
  





  protected static File findPhantomJS(Capabilities desiredCapabilities, String docsLink, String downloadLink)
  {
    String phantomjspath;
    



    String phantomjspath;
    



    if ((desiredCapabilities != null) && 
      (desiredCapabilities.getCapability("phantomjs.binary.path") != null)) {
      phantomjspath = (String)desiredCapabilities.getCapability("phantomjs.binary.path");
    } else {
      phantomjspath = new ExecutableFinder().find("phantomjs");
      phantomjspath = System.getProperty("phantomjs.binary.path", phantomjspath);
    }
    
    Preconditions.checkState(phantomjspath != null, "The path to the driver executable must be set by the %s capability/system property/PATH variable; for more information, see %s. The latest version can be downloaded from %s", "phantomjs.binary.path", docsLink, downloadLink);
    






    File phantomjs = new File(phantomjspath);
    checkExecutable(phantomjs);
    return phantomjs;
  }
  





  protected static File findGhostDriver(Capabilities desiredCapabilities, String docsLink, String downloadLink)
  {
    String ghostdriverpath;
    



    String ghostdriverpath;
    



    if ((desiredCapabilities != null) && 
      (desiredCapabilities.getCapability("phantomjs.ghostdriver.path") != null)) {
      ghostdriverpath = (String)desiredCapabilities.getCapability("phantomjs.ghostdriver.path");
    } else {
      ghostdriverpath = System.getProperty("phantomjs.ghostdriver.path");
    }
    
    if (ghostdriverpath != null)
    {
      File ghostdriver = new File(ghostdriverpath);
      Preconditions.checkState(ghostdriver.exists(), "The GhostDriver does not exist: %s", ghostdriver
      
        .getAbsolutePath());
      Preconditions.checkState(ghostdriver.isFile(), "The GhostDriver is a directory: %s", ghostdriver
      
        .getAbsolutePath());
      Preconditions.checkState(ghostdriver.canRead(), "The GhostDriver is not a readable file: %s", ghostdriver
      
        .getAbsolutePath());
      
      return ghostdriver;
    }
    

    return null;
  }
  
  private static String[] findCLIArgumentsFromCaps(Capabilities desiredCapabilities, String capabilityName) {
    if (desiredCapabilities != null) {
      Object cap = desiredCapabilities.getCapability(capabilityName);
      if (cap != null) {
        if ((cap instanceof String[]))
          return (String[])cap;
        if ((cap instanceof Collection)) {
          try
          {
            Collection<String> capCollection = (Collection)cap;
            return (String[])capCollection.toArray(new String[capCollection.size()]);
          }
          catch (Exception e) {
            LOG.warning(String.format("Unable to set Capability '%s' as it was neither a String[] or a Collection<String>", new Object[] { capabilityName }));
          }
        }
      }
    }
    

    return new String[0];
  }
  



  public static class Builder
  {
    private int port = 0;
    private File phantomjs = null;
    private File ghostdriver = null;
    private ImmutableMap<String, String> environment = ImmutableMap.of();
    private File logFile;
    private Proxy proxy = null;
    private String[] commandLineArguments = null;
    private String[] ghostdriverCommandLineArguments = null;
    


    public Builder() {}
    

    public Builder usingPhantomJSExecutable(File file)
    {
      Preconditions.checkNotNull(file);
      PhantomJSDriverService.checkExecutable(file);
      phantomjs = file;
      return this;
    }
    





    public Builder usingGhostDriver(File file)
    {
      ghostdriver = file;
      return this;
    }
    






    public Builder usingPort(int port)
    {
      Preconditions.checkArgument(port >= 0, "Invalid port number: %d", port);
      this.port = port;
      return this;
    }
    




    public Builder usingAnyFreePort()
    {
      port = 0;
      return this;
    }
    






    public Builder withEnvironment(Map<String, String> environment)
    {
      this.environment = ImmutableMap.copyOf(environment);
      return this;
    }
    





    public Builder withLogFile(File logFile)
    {
      this.logFile = logFile;
      return this;
    }
    









    public Builder withProxy(Proxy proxy)
    {
      this.proxy = proxy;
      return this;
    }
    




    public Builder usingCommandLineArguments(String[] commandLineArguments)
    {
      this.commandLineArguments = commandLineArguments;
      return this;
    }
    




    public Builder usingGhostDriverCommandLineArguments(String[] ghostdriverCommandLineArguments)
    {
      this.ghostdriverCommandLineArguments = ghostdriverCommandLineArguments;
      return this;
    }
    






    public PhantomJSDriverService build()
    {
      port = (port == 0 ? PortProber.findFreePort() : port);
      

      Preconditions.checkState(phantomjs != null, "Path to PhantomJS executable not specified");
      
      try
      {
        ImmutableList.Builder<String> argsBuilder = ImmutableList.builder();
        

        if (proxy != null) {
          switch (PhantomJSDriverService.1.$SwitchMap$org$openqa$selenium$Proxy$ProxyType[proxy.getProxyType().ordinal()]) {
          case 1: 
            if ((proxy.getHttpProxy() != null) && (!proxy.getHttpProxy().isEmpty())) {
              argsBuilder.add("--proxy-type=http");
              argsBuilder.add(String.format("--proxy=%s", new Object[] { proxy.getHttpProxy() }));
            } else if ((proxy.getSocksProxy() != null) && (!proxy.getSocksProxy().isEmpty())) {
              argsBuilder.add("--proxy-type=socks5");
              argsBuilder.add(String.format("--proxy=%s", new Object[] { proxy.getSocksProxy() }));
              if ((proxy.getSocksUsername() != null) && (!proxy.getSocksUsername().isEmpty()) && 
                (proxy.getSocksPassword() != null) && (!proxy.getSocksPassword().isEmpty())) {
                argsBuilder.add(String.format("--proxy-auth=%s:%s", new Object[] { proxy.getSocksUsername(), proxy
                  .getSocksPassword() }));
              }
            }
            else {
              Preconditions.checkArgument(true, "PhantomJS supports only HTTP and Socks5 Proxy currently");
            }
            break;
          
          case 2: 
            Preconditions.checkArgument(true, "PhantomJS doesn't support Proxy PAC files");
            break;
          case 3: 
            argsBuilder.add("--proxy-type=system");
            break;
          
          case 4: 
            Preconditions.checkArgument(true, "PhantomJS doesn't support Proxy Auto-configuration");
            break;
          case 5: 
          default: 
            argsBuilder.add("--proxy-type=none");
          }
          
        }
        

        if (commandLineArguments != null) {
          argsBuilder.add(commandLineArguments);
        }
        

        if (ghostdriver != null)
        {
          argsBuilder.add(ghostdriver.getCanonicalPath());
          

          if (!argsContains(ghostdriverCommandLineArguments, "port")) {
            argsBuilder.add(String.format("--port=%d", new Object[] { Integer.valueOf(port) }));
          }
          

          if ((logFile != null) && (!argsContains(ghostdriverCommandLineArguments, "logFile"))) {
            argsBuilder.add(String.format("--logFile=%s", new Object[] { logFile.getAbsolutePath() }));
          }
          

          if (ghostdriverCommandLineArguments != null) {
            argsBuilder.add(ghostdriverCommandLineArguments);
          }
        }
        else
        {
          if (!argsContains(commandLineArguments, "webdriver")) {
            argsBuilder.add(String.format("--webdriver=%d", new Object[] { Integer.valueOf(port) }));
          }
          

          if ((logFile != null) && (!argsContains(commandLineArguments, "webdriver-logfile"))) {
            argsBuilder.add(String.format("--webdriver-logfile=%s", new Object[] { logFile.getAbsolutePath() }));
          }
        }
        

        return new PhantomJSDriverService(phantomjs, port, argsBuilder.build(), environment, null);
      } catch (IOException e) {
        throw new WebDriverException(e);
      }
    }
    
    private boolean argsContains(String[] args, String targetArg) {
      if (args != null) {
        for (int i = 0; i < args.length; i++) {
          String arg = args[i];
          if (arg.startsWith("--" + targetArg + "=")) {
            return true;
          }
        }
      }
      
      return false;
    }
  }
}
