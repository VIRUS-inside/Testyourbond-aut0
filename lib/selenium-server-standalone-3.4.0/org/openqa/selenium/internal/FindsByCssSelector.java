package org.openqa.selenium.internal;

import java.util.List;
import org.openqa.selenium.WebElement;

public abstract interface FindsByCssSelector
{
  public abstract WebElement findElementByCssSelector(String paramString);
  
  public abstract List<WebElement> findElementsByCssSelector(String paramString);
}
