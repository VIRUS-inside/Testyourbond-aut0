package org.openqa.selenium.logging;

import java.util.Set;
import org.openqa.selenium.Beta;

@Beta
public abstract interface Logs
{
  public abstract LogEntries get(String paramString);
  
  public abstract Set<String> getAvailableLogTypes();
}
