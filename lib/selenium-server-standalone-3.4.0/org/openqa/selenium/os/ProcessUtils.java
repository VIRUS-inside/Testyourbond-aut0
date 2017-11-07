package org.openqa.selenium.os;

import com.google.common.io.Closeables;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.Platform;


























public class ProcessUtils
{
  private static Logger LOG = Logger.getLogger(ProcessUtils.class.getName());
  


  public ProcessUtils() {}
  


  private static int waitForProcessDeath(Process p, long timeout)
  {
    ProcessWaiter pw = new ProcessWaiter(p);
    Thread waiter = new Thread(pw);
    waiter.start();
    try {
      waiter.join(timeout);
    } catch (InterruptedException e) {
      throw new RuntimeException("Bug? Main interrupted while waiting for process", e);
    }
    if (waiter.isAlive()) {
      waiter.interrupt();
    }
    try {
      waiter.join();
    } catch (InterruptedException e) {
      throw new RuntimeException("Bug? Main interrupted while waiting for dead process waiter", e);
    }
    InterruptedException ie = pw.getException();
    if (ie != null) {
      throw new ProcessStillAliveException("Timeout waiting for process to die", ie);
    }
    
    return p.exitValue();
  }
  





  public static int killProcess(Process process)
  {
    if (WindowsUtils.thisIsWindows()) {
      return killWinProcess(process);
    }
    return killUnixProcess(process);
  }
  


  private static int killUnixProcess(Process process)
  {
    try
    {
      int exitValue = waitForProcessDeath(process, 1000L);
      closeAllStreamsAndDestroyProcess(process);
      if (exitValue == 0) {
        return exitValue;
      }
    }
    catch (Exception localException1) {}
    

    process.destroy();
    try {
      int exitValue = waitForProcessDeath(process, 10000L);
      closeAllStreamsAndDestroyProcess(process);
    } catch (ProcessStillAliveException ex) {
      if (Platform.getCurrent().is(Platform.WINDOWS)) {
        throw ex;
      }
      try {
        LOG.info("Process didn't die after 10 seconds");
        kill9(process);
        int exitValue = waitForProcessDeath(process, 10000L);
        closeAllStreamsAndDestroyProcess(process);
      } catch (Exception e) {
        LOG.log(Level.WARNING, "Process refused to die after 10 seconds, and couldn't kill9 it", ex);
        
        throw new RuntimeException("Process refused to die after 10 seconds, and couldn't kill9 it: " + e.getMessage(), ex);
      }
    }
    int exitValue;
    return exitValue;
  }
  
  private static int killWinProcess(Process process)
  {
    try
    {
      WindowsUtils.killPID("" + getProcessId(process));
      exitValue = waitForProcessDeath(process, 10000L);
    } catch (Exception ex) { int exitValue;
      LOG.log(Level.WARNING, "Process refused to die after 10 seconds, and couldn't taskkill it", ex);
      
      throw new RuntimeException("Process refused to die after 10 seconds, and couldn't taskkill it: " + ex.getMessage(), ex);
    }
    int exitValue;
    return exitValue;
  }
  
  private static class ProcessWaiter implements Runnable
  {
    private volatile InterruptedException t;
    private final Process p;
    
    public ProcessWaiter(Process p) {
      this.p = p;
    }
    
    public InterruptedException getException() {
      return t;
    }
    
    public void run() {
      try {
        p.waitFor();
      } catch (InterruptedException e) {
        t = e;
      }
    }
  }
  
  public static class ProcessStillAliveException extends RuntimeException {
    public ProcessStillAliveException(String message, Throwable cause) {
      super(cause);
    }
  }
  
  private static void closeAllStreamsAndDestroyProcess(Process process) {
    try {
      Closeables.close(process.getInputStream(), true);
      Closeables.close(process.getErrorStream(), true);
      Closeables.close(process.getOutputStream(), true);
    }
    catch (IOException localIOException) {}
    process.destroy();
  }
  
  static int getProcessId(Process p) {
    try {
      if (Platform.getCurrent().is(Platform.WINDOWS)) {
        Field f = p.getClass().getDeclaredField("handle");
        f.setAccessible(true);
        long hndl = f.getLong(p);
        
        Kernel32 kernel = Kernel32.INSTANCE;
        WinNT.HANDLE handle = new WinNT.HANDLE();
        handle.setPointer(Pointer.createConstant(hndl));
        return kernel.GetProcessId(handle);
      }
      
      Field f = p.getClass().getDeclaredField("pid");
      f.setAccessible(true);
      return ((Integer)f.get(p)).intValue();
    }
    catch (Exception e) {
      throw new RuntimeException("Couldn't detect pid", e);
    }
  }
  
  private static void kill9(Integer pid)
  {
    LOG.fine("kill -9 " + pid);
    
    CommandLine command = new CommandLine("kill", new String[] { "-9", pid.toString() });
    command.execute();
    String result = command.getStdOut();
    int output = command.getExitCode();
    LOG.fine(String.valueOf(output));
    if (!command.isSuccessful()) {
      throw new RuntimeException("exec return code " + result + ": " + output);
    }
  }
  
  private static void kill9(Process p)
  {
    kill9(Integer.valueOf(getProcessId(p)));
  }
}
