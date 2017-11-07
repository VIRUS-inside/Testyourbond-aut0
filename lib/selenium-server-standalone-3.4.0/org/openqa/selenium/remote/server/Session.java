package org.openqa.selenium.remote.server;

import java.util.concurrent.FutureTask;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.io.TemporaryFilesystem;
import org.openqa.selenium.remote.SessionId;

public abstract interface Session
{
  public abstract void close();
  
  public abstract <X> X execute(FutureTask<X> paramFutureTask)
    throws Exception;
  
  public abstract WebDriver getDriver();
  
  public abstract KnownElements getKnownElements();
  
  public abstract Capabilities getCapabilities();
  
  public abstract void attachScreenshot(String paramString);
  
  public abstract String getAndClearScreenshot();
  
  public abstract boolean isTimedOut(long paramLong);
  
  public abstract boolean isInUse();
  
  public abstract void interrupt();
  
  public abstract void updateLastAccessTime();
  
  public abstract SessionId getSessionId();
  
  public abstract TemporaryFilesystem getTemporaryFileSystem();
}
