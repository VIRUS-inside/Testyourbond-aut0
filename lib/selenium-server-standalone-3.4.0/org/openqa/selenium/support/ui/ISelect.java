package org.openqa.selenium.support.ui;

import java.util.List;
import org.openqa.selenium.WebElement;

public abstract interface ISelect
{
  public abstract boolean isMultiple();
  
  public abstract List<WebElement> getOptions();
  
  public abstract List<WebElement> getAllSelectedOptions();
  
  public abstract WebElement getFirstSelectedOption();
  
  public abstract void selectByVisibleText(String paramString);
  
  public abstract void selectByIndex(int paramInt);
  
  public abstract void selectByValue(String paramString);
  
  public abstract void deselectAll();
  
  public abstract void deselectByValue(String paramString);
  
  public abstract void deselectByIndex(int paramInt);
  
  public abstract void deselectByVisibleText(String paramString);
}
