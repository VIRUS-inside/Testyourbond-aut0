package org.openqa.selenium.os;

import com.google.common.annotations.VisibleForTesting;
import java.io.File;
import java.io.OutputStream;
import java.util.Map;
import java.util.Map.Entry;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriverException;






















public class CommandLine
{
  private OsProcess process;
  
  public CommandLine(String executable, String... args)
  {
    process = new UnixProcess(executable, args);
  }
  
  public CommandLine(String[] cmdarray) {
    String executable = cmdarray[0];
    int length = cmdarray.length - 1;
    String[] args = new String[length];
    System.arraycopy(cmdarray, 1, args, 0, length);
    
    process = new UnixProcess(executable, args);
  }
  
  @VisibleForTesting
  Map<String, String> getEnvironment() {
    return process.getEnvironment();
  }
  





  public void setEnvironmentVariables(Map<String, String> environment)
  {
    for (Map.Entry<String, String> entry : environment.entrySet()) {
      setEnvironmentVariable((String)entry.getKey(), (String)entry.getValue());
    }
  }
  






  public void setEnvironmentVariable(String name, String value)
  {
    process.setEnvironmentVariable(name, value);
  }
  
  public void setDynamicLibraryPath(String newLibraryPath)
  {
    if (newLibraryPath != null) {
      setEnvironmentVariable(getLibraryPathPropertyName(), newLibraryPath);
    }
  }
  
  public void updateDynamicLibraryPath(String extraPath) {
    if (extraPath != null) {
      String existing = System.getenv(getLibraryPathPropertyName());
      String ldPath = existing != null ? existing + File.separator + extraPath : extraPath;
      setEnvironmentVariable(getLibraryPathPropertyName(), ldPath);
    }
  }
  


  public static String getLibraryPathPropertyName()
  {
    Platform current = Platform.getCurrent();
    
    if (current.is(Platform.WINDOWS)) {
      return "PATH";
    }
    if (current.is(Platform.MAC)) {
      return "DYLD_LIBRARY_PATH";
    }
    
    return "LD_LIBRARY_PATH";
  }
  
  public void executeAsync()
  {
    process.executeAsync();
  }
  
  public void execute() {
    executeAsync();
    waitFor();
  }
  
  public void waitFor() {
    try {
      process.waitFor();
    } catch (InterruptedException e) {
      throw new WebDriverException(e);
    }
  }
  
  public void waitFor(long timeout) {
    try {
      process.waitFor(timeout);
    } catch (InterruptedException e) {
      throw new WebDriverException(e);
    }
  }
  
  public boolean isSuccessful() {
    return 0 == getExitCode();
  }
  
  public int getExitCode() {
    return process.getExitCode();
  }
  
  public String getStdOut() {
    return process.getStdOut();
  }
  




  public int destroy()
  {
    return process.destroy();
  }
  




  public boolean isRunning()
  {
    return process.isRunning();
  }
  
  public void setInput(String allInput) {
    process.setInput(allInput);
  }
  
  public void setWorkingDirectory(String workingDirectory) {
    process.setWorkingDirectory(new File(workingDirectory));
  }
  
  public String toString()
  {
    return process.toString();
  }
  
  public void copyOutputTo(OutputStream out) {
    process.copyOutputTo(out);
  }
  
  public void checkForError() {
    process.checkForError();
  }
}
