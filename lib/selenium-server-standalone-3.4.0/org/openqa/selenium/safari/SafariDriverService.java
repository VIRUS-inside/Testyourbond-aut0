package org.openqa.selenium.safari;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.net.PortProber;
import org.openqa.selenium.remote.service.DriverService;
import org.openqa.selenium.remote.service.DriverService.Builder;


















public class SafariDriverService
  extends DriverService
{
  private static final File SAFARI_DRIVER_EXECUTABLE = new File("/usr/bin/safaridriver");
  private static final File TP_SAFARI_DRIVER_EXECUTABLE = new File("/Applications/Safari Technology Preview.app/Contents/MacOS/safaridriver");
  
  public SafariDriverService(File executable, int port, ImmutableList<String> args, ImmutableMap<String, String> environment)
    throws IOException
  {
    super(executable, port, args, environment);
  }
  
  public static SafariDriverService createDefaultService(SafariOptions options) {
    File exe = options.getUseTechnologyPreview() ? TP_SAFARI_DRIVER_EXECUTABLE : SAFARI_DRIVER_EXECUTABLE;
    
    if (exe.exists()) {
      return (SafariDriverService)((Builder)((Builder)new Builder().usingPort(options.getPort())).usingDriverExecutable(exe)).build();
    }
    return null;
  }
  
  protected void waitUntilAvailable() throws MalformedURLException
  {
    try {
      PortProber.waitForPortUp(getUrl().getPort(), 20, TimeUnit.SECONDS);
    } catch (RuntimeException e) {
      throw new WebDriverException(e);
    }
  }
  
  public static class Builder extends DriverService.Builder<SafariDriverService, Builder> {
    public Builder() {}
    
    public Builder usingTechnologyPreview(boolean useTechnologyPreview) {
      if (useTechnologyPreview) {
        usingDriverExecutable(SafariDriverService.TP_SAFARI_DRIVER_EXECUTABLE);
      } else {
        usingDriverExecutable(SafariDriverService.SAFARI_DRIVER_EXECUTABLE);
      }
      return this;
    }
    
    protected File findDefaultExecutable() {
      return SafariDriverService.SAFARI_DRIVER_EXECUTABLE;
    }
    
    protected ImmutableList<String> createArgs() {
      return ImmutableList.of("--port", String.valueOf(getPort()));
    }
    
    protected SafariDriverService createDriverService(File exe, int port, ImmutableList<String> args, ImmutableMap<String, String> environment)
    {
      try {
        return new SafariDriverService(exe, port, args, environment);
      } catch (IOException e) {
        throw new WebDriverException(e);
      }
    }
  }
}
