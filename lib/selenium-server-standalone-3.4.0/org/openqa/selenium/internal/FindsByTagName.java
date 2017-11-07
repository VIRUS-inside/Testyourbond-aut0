package org.openqa.selenium.internal;

import java.util.List;
import org.openqa.selenium.WebElement;

public abstract interface FindsByTagName
{
  public abstract WebElement findElementByTagName(String paramString);
  
  public abstract List<WebElement> findElementsByTagName(String paramString);
}
