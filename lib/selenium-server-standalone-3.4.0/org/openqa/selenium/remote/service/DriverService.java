package org.openqa.selenium.remote.service;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import org.openqa.selenium.Beta;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.net.PortProber;
import org.openqa.selenium.net.UrlChecker;
import org.openqa.selenium.net.UrlChecker.TimeoutException;
import org.openqa.selenium.os.CommandLine;
import org.openqa.selenium.os.ExecutableFinder;







































public class DriverService
{
  private final URL url;
  private final ReentrantLock lock = new ReentrantLock();
  




  private CommandLine process = null;
  
  private final String executable;
  private final ImmutableList<String> args;
  private final ImmutableMap<String, String> environment;
  private OutputStream outputStream = System.err;
  










  protected DriverService(File executable, int port, ImmutableList<String> args, ImmutableMap<String, String> environment)
    throws IOException
  {
    this.executable = executable.getCanonicalPath();
    this.args = args;
    this.environment = environment;
    
    url = getUrl(port);
  }
  
  protected URL getUrl(int port) throws IOException {
    return new URL(String.format("http://localhost:%d", new Object[] { Integer.valueOf(port) }));
  }
  


  public URL getUrl()
  {
    return url;
  }
  













  protected static File findExecutable(String exeName, String exeProperty, String exeDocs, String exeDownload)
  {
    String defaultPath = new ExecutableFinder().find(exeName);
    String exePath = System.getProperty(exeProperty, defaultPath);
    Preconditions.checkState(exePath != null, "The path to the driver executable must be set by the %s system property; for more information, see %s. The latest version can be downloaded from %s", exeProperty, exeDocs, exeDownload);
    




    File exe = new File(exePath);
    checkExecutable(exe);
    return exe;
  }
  
  protected static void checkExecutable(File exe) {
    Preconditions.checkState(exe.exists(), "The driver executable does not exist: %s", exe
      .getAbsolutePath());
    Preconditions.checkState(!exe.isDirectory(), "The driver executable is a directory: %s", exe
      .getAbsolutePath());
    Preconditions.checkState(exe.canExecute(), "The driver is not executable: %s", exe
      .getAbsolutePath());
  }
  




  public boolean isRunning()
  {
    lock.lock();
    try { boolean bool1;
      if (process == null) {
        return false;
      }
      return process.isRunning();
    } catch (IllegalThreadStateException e) {
      return true;
    } finally {
      lock.unlock();
    }
  }
  





  public void start()
    throws IOException
  {
    lock.lock();
    try {
      if (process != null) {
        return;
      }
      process = new CommandLine(executable, (String[])args.toArray(new String[0]));
      process.setEnvironmentVariables(environment);
      process.copyOutputTo(getOutputStream());
      process.executeAsync();
      
      waitUntilAvailable();
    } finally {
      lock.unlock();
    }
  }
  
  protected void waitUntilAvailable() throws MalformedURLException {
    try {
      URL status = new URL(url.toString() + "/status");
      new UrlChecker().waitUntilAvailable(20L, TimeUnit.SECONDS, new URL[] { status });
    } catch (UrlChecker.TimeoutException e) {
      process.checkForError();
      throw new WebDriverException("Timed out waiting for driver server to start.", e);
    }
  }
  





  public void stop()
  {
    lock.lock();
    
    WebDriverException toThrow = null;
    try {
      if (process == null) {
        return;
      }
      try
      {
        URL killUrl = new URL(url.toString() + "/shutdown");
        new UrlChecker().waitUntilUnavailable(3L, TimeUnit.SECONDS, killUrl);
      } catch (MalformedURLException e) {
        toThrow = new WebDriverException(e);
      } catch (UrlChecker.TimeoutException e) {
        toThrow = new WebDriverException("Timed out waiting for driver server to shutdown.", e);
      }
      
      process.destroy();
    } finally {
      process = null;
      lock.unlock();
    }
    
    if (toThrow != null) {
      throw toThrow;
    }
  }
  
  public void sendOutputTo(OutputStream outputStream) {
    this.outputStream = ((OutputStream)Preconditions.checkNotNull(outputStream));
  }
  
  protected OutputStream getOutputStream() {
    return outputStream;
  }
  
  public static abstract class Builder<DS extends DriverService, B extends Builder<?, ?>>
  {
    private int port = 0;
    private File exe = null;
    private ImmutableMap<String, String> environment = ImmutableMap.of();
    

    private File logFile;
    

    public Builder() {}
    

    public B usingDriverExecutable(File file)
    {
      Preconditions.checkNotNull(file);
      DriverService.checkExecutable(file);
      exe = file;
      return this;
    }
    






    public B usingPort(int port)
    {
      Preconditions.checkArgument(port >= 0, "Invalid port number: %s", port);
      this.port = port;
      return this;
    }
    
    protected int getPort() {
      return port;
    }
    




    public B usingAnyFreePort()
    {
      port = 0;
      return this;
    }
    








    @Beta
    public B withEnvironment(Map<String, String> environment)
    {
      this.environment = ImmutableMap.copyOf(environment);
      return this;
    }
    





    public B withLogFile(File logFile)
    {
      this.logFile = logFile;
      return this;
    }
    
    protected File getLogFile() {
      return logFile;
    }
    





    public DS build()
    {
      if (port == 0) {
        port = PortProber.findFreePort();
      }
      
      if (exe == null) {
        exe = findDefaultExecutable();
      }
      
      ImmutableList<String> args = createArgs();
      
      return createDriverService(exe, port, args, environment);
    }
    
    protected abstract File findDefaultExecutable();
    
    protected abstract ImmutableList<String> createArgs();
    
    protected abstract DS createDriverService(File paramFile, int paramInt, ImmutableList<String> paramImmutableList, ImmutableMap<String, String> paramImmutableMap);
  }
}
