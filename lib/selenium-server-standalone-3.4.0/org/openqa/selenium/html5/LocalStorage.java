package org.openqa.selenium.html5;

import java.util.Set;

public abstract interface LocalStorage
{
  public abstract String getItem(String paramString);
  
  public abstract Set<String> keySet();
  
  public abstract void setItem(String paramString1, String paramString2);
  
  public abstract String removeItem(String paramString);
  
  public abstract void clear();
  
  public abstract int size();
}
