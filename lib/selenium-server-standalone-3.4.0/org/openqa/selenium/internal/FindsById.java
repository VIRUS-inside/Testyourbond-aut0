package org.openqa.selenium.internal;

import java.util.List;
import org.openqa.selenium.WebElement;

public abstract interface FindsById
{
  public abstract WebElement findElementById(String paramString);
  
  public abstract List<WebElement> findElementsById(String paramString);
}
