package org.openqa.selenium.html5;

public abstract interface WebStorage
{
  public abstract LocalStorage getLocalStorage();
  
  public abstract SessionStorage getSessionStorage();
}
