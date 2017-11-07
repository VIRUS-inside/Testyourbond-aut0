package org.openqa.selenium.firefox;

import java.io.IOException;
import java.net.URI;
import org.openqa.selenium.logging.NeedsLocalLogs;
import org.openqa.selenium.remote.CommandExecutor;

public abstract interface ExtensionConnection
  extends CommandExecutor, NeedsLocalLogs
{
  public abstract void start()
    throws IOException;
  
  public abstract boolean isConnected();
  
  public abstract void quit();
  
  public abstract URI getAddressOfRemoteServer();
}
