package org.openqa.selenium.support.pagefactory;

import java.util.List;
import org.openqa.selenium.WebElement;

public abstract interface ElementLocator
{
  public abstract WebElement findElement();
  
  public abstract List<WebElement> findElements();
}
