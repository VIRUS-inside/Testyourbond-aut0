package org.openqa.selenium.internal;

import java.util.List;
import org.openqa.selenium.WebElement;

public abstract interface FindsByName
{
  public abstract WebElement findElementByName(String paramString);
  
  public abstract List<WebElement> findElementsByName(String paramString);
}
