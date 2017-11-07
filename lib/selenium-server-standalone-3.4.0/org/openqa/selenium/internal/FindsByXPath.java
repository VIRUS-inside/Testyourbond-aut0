package org.openqa.selenium.internal;

import java.util.List;
import org.openqa.selenium.WebElement;

public abstract interface FindsByXPath
{
  public abstract WebElement findElementByXPath(String paramString);
  
  public abstract List<WebElement> findElementsByXPath(String paramString);
}
