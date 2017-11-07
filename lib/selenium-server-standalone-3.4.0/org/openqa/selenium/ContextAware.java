package org.openqa.selenium;

import java.util.Set;

public abstract interface ContextAware
{
  public abstract WebDriver context(String paramString);
  
  public abstract Set<String> getContextHandles();
  
  public abstract String getContext();
}
