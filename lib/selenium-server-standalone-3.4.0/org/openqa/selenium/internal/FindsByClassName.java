package org.openqa.selenium.internal;

import java.util.List;
import org.openqa.selenium.WebElement;

public abstract interface FindsByClassName
{
  public abstract WebElement findElementByClassName(String paramString);
  
  public abstract List<WebElement> findElementsByClassName(String paramString);
}
