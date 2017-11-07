package org.openqa.selenium.os;

import java.io.File;
import java.io.OutputStream;
import java.util.Map;

abstract interface OsProcess
{
  public abstract Map<String, String> getEnvironment();
  
  public abstract void setEnvironmentVariable(String paramString1, String paramString2);
  
  public abstract void copyOutputTo(OutputStream paramOutputStream);
  
  public abstract void setInput(String paramString);
  
  public abstract void setWorkingDirectory(File paramFile);
  
  public abstract void executeAsync();
  
  public abstract void waitFor()
    throws InterruptedException;
  
  public abstract void waitFor(long paramLong)
    throws InterruptedException;
  
  public abstract int destroy();
  
  public abstract int getExitCode();
  
  public abstract String getStdOut();
  
  public abstract boolean isRunning();
  
  public abstract void checkForError();
}
