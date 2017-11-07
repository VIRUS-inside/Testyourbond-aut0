package org.openqa.selenium;

import org.openqa.selenium.security.Credentials;

public abstract interface Alert
{
  public abstract void dismiss();
  
  public abstract void accept();
  
  public abstract String getText();
  
  public abstract void sendKeys(String paramString);
  
  @Beta
  public abstract void setCredentials(Credentials paramCredentials);
  
  @Beta
  public abstract void authenticateUsing(Credentials paramCredentials);
}
