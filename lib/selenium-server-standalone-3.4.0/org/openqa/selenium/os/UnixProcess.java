package org.openqa.selenium.os;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DaemonExecutor;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.io.CircularOutputStream;
import org.openqa.selenium.io.MultiOutputStream;



















class UnixProcess
  implements OsProcess
{
  private static final Logger log = Logger.getLogger(UnixProcess.class.getName());
  
  private final CircularOutputStream inputOut = new CircularOutputStream(32768);
  private volatile String allInput;
  private final DefaultExecuteResultHandler handler = new DefaultExecuteResultHandler();
  private final Executor executor = new DaemonExecutor();
  
  private volatile OutputStream drainTo;
  private SeleniumWatchDog executeWatchdog = new SeleniumWatchDog(-1L);
  
  private final CommandLine cl;
  
  private final Map<String, String> env = new ConcurrentHashMap();
  
  public UnixProcess(String executable, String... args) {
    String actualExe = (String)Preconditions.checkNotNull(new ExecutableFinder().find(executable), "Unable to find executable for: %s", executable);
    
    cl = new CommandLine(actualExe);
    cl.addArguments(args, false);
  }
  
  public void setEnvironmentVariable(String name, String value) {
    if (name == null) {
      throw new IllegalArgumentException("Cannot have a null environment variable name!");
    }
    if (value == null) {
      throw new IllegalArgumentException("Cannot have a null value for environment variable " + name);
    }
    
    env.put(name, value);
  }
  
  @VisibleForTesting
  public Map<String, String> getEnvironment() {
    return ImmutableMap.copyOf(env);
  }
  
  private Map<String, String> getMergedEnv() {
    HashMap<String, String> newEnv = Maps.newHashMap(System.getenv());
    newEnv.putAll(env);
    return newEnv;
  }
  
  private ByteArrayInputStream getInputStream() {
    return allInput != null ? new ByteArrayInputStream(allInput.getBytes()) : null;
  }
  
  public void executeAsync() {
    try {
      OutputStream outputStream = getOutputStream();
      executeWatchdog.reset();
      executor.setWatchdog(executeWatchdog);
      executor.setStreamHandler(new PumpStreamHandler(outputStream, outputStream, 
        getInputStream()));
      executor.execute(cl, getMergedEnv(), handler);
    } catch (IOException e) {
      throw new WebDriverException(e);
    }
  }
  
  private OutputStream getOutputStream() {
    return drainTo == null ? inputOut : new MultiOutputStream(inputOut, drainTo);
  }
  
  public int destroy()
  {
    SeleniumWatchDog watchdog = executeWatchdog;
    watchdog.waitForProcessStarted();
    
    if (!WindowsUtils.thisIsWindows()) {
      watchdog.destroyProcess();
      watchdog.waitForTerminationAfterDestroy(2, TimeUnit.SECONDS);
      if (!isRunning()) {
        return getExitCode();
      }
      log.info("Command failed to close cleanly. Destroying forcefully (v2). " + this);
    }
    
    watchdog.destroyHarder();
    watchdog.waitForTerminationAfterDestroy(1, TimeUnit.SECONDS);
    if (!isRunning()) {
      return getExitCode();
    }
    
    log.severe(String.format("Unable to kill process with PID %s", new Object[] {watchdog
      .getPID() }));
    int exitCode = -1;
    executor.setExitValue(exitCode);
    return exitCode;
  }
  
  public void waitFor() throws InterruptedException {
    handler.waitFor();
  }
  
  public void waitFor(long timeout) throws InterruptedException {
    long until = System.currentTimeMillis() + timeout;
    boolean timedOut = true;
    while (System.currentTimeMillis() < until) {
      if (handler.hasResult()) {
        timedOut = false;
        break;
      }
      Thread.sleep(50L);
    }
    if (timedOut)
    {
      throw new InterruptedException(String.format("Process timed out after waiting for %d ms.", new Object[] {Long.valueOf(timeout) }));
    }
  }
  

  public boolean isRunning()
  {
    return !handler.hasResult();
  }
  
  public int getExitCode() {
    if (isRunning()) {
      throw new IllegalStateException("Cannot get exit code before executing command line: " + cl);
    }
    
    return handler.getExitValue();
  }
  
  public void checkForError() {
    if (handler.getException() != null) {
      log.severe(handler.getException().toString());
    }
  }
  
  public String getStdOut() {
    return inputOut.toString();
  }
  
  public void setInput(String allInput) {
    this.allInput = allInput;
  }
  
  public void setWorkingDirectory(File workingDirectory) {
    executor.setWorkingDirectory(workingDirectory);
  }
  
  public String toString()
  {
    return cl.toString() + "[ " + env + "]";
  }
  
  public void copyOutputTo(OutputStream out) {
    drainTo = out;
  }
  
  class SeleniumWatchDog extends ExecuteWatchdog
  {
    private volatile Process process;
    private volatile boolean starting = true;
    
    SeleniumWatchDog(long timeout) {
      super();
    }
    
    public synchronized void start(Process process)
    {
      this.process = process;
      starting = false;
      super.start(process);
    }
    
    public void reset() {
      starting = true;
    }
    
    private String getPID() {
      return String.valueOf(ProcessUtils.getProcessId(process));
    }
    
    private void waitForProcessStarted() {
      while (starting) {
        try {
          Thread.sleep(50L);
        } catch (InterruptedException e) {
          throw new WebDriverException(e);
        }
      }
    }
    
    private void waitForTerminationAfterDestroy(int duration, TimeUnit unit) {
      long end = System.currentTimeMillis() + unit.toMillis(duration);
      while ((isRunning()) && (System.currentTimeMillis() < end)) {
        try {
          Thread.sleep(50L);
        } catch (InterruptedException e) {
          throw new WebDriverException(e);
        }
      }
    }
    
    private void destroyHarder() {
      ProcessUtils.killProcess(process);
    }
  }
}
