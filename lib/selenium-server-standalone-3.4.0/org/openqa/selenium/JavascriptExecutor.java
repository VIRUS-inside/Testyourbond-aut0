package org.openqa.selenium;

public abstract interface JavascriptExecutor
{
  public abstract Object executeScript(String paramString, Object... paramVarArgs);
  
  public abstract Object executeAsyncScript(String paramString, Object... paramVarArgs);
}
