package org.apache.commons.exec;

public abstract interface ExecuteResultHandler
{
  public abstract void onProcessComplete(int paramInt);
  
  public abstract void onProcessFailed(ExecuteException paramExecuteException);
}
