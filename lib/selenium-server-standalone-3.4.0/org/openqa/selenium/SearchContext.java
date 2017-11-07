package org.openqa.selenium;

import java.util.List;

public abstract interface SearchContext
{
  public abstract List<WebElement> findElements(By paramBy);
  
  public abstract WebElement findElement(By paramBy);
}
