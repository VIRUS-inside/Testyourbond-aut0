package org.apache.commons.exec;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public abstract interface Executor
{
  public static final int INVALID_EXITVALUE = -559038737;
  
  public abstract void setExitValue(int paramInt);
  
  public abstract void setExitValues(int[] paramArrayOfInt);
  
  public abstract boolean isFailure(int paramInt);
  
  public abstract ExecuteStreamHandler getStreamHandler();
  
  public abstract void setStreamHandler(ExecuteStreamHandler paramExecuteStreamHandler);
  
  public abstract ExecuteWatchdog getWatchdog();
  
  public abstract void setWatchdog(ExecuteWatchdog paramExecuteWatchdog);
  
  public abstract ProcessDestroyer getProcessDestroyer();
  
  public abstract void setProcessDestroyer(ProcessDestroyer paramProcessDestroyer);
  
  public abstract File getWorkingDirectory();
  
  public abstract void setWorkingDirectory(File paramFile);
  
  public abstract int execute(CommandLine paramCommandLine)
    throws ExecuteException, IOException;
  
  public abstract int execute(CommandLine paramCommandLine, Map<String, String> paramMap)
    throws ExecuteException, IOException;
  
  public abstract void execute(CommandLine paramCommandLine, ExecuteResultHandler paramExecuteResultHandler)
    throws ExecuteException, IOException;
  
  public abstract void execute(CommandLine paramCommandLine, Map<String, String> paramMap, ExecuteResultHandler paramExecuteResultHandler)
    throws ExecuteException, IOException;
}
