package org.openqa.selenium;

import java.util.List;

public abstract interface WebElement
  extends SearchContext, TakesScreenshot
{
  public abstract void click();
  
  public abstract void submit();
  
  public abstract void sendKeys(CharSequence... paramVarArgs);
  
  public abstract void clear();
  
  public abstract String getTagName();
  
  public abstract String getAttribute(String paramString);
  
  public abstract boolean isSelected();
  
  public abstract boolean isEnabled();
  
  public abstract String getText();
  
  public abstract List<WebElement> findElements(By paramBy);
  
  public abstract WebElement findElement(By paramBy);
  
  public abstract boolean isDisplayed();
  
  public abstract Point getLocation();
  
  public abstract Dimension getSize();
  
  public abstract Rectangle getRect();
  
  public abstract String getCssValue(String paramString);
}
