package org.openqa.selenium.support.events;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public abstract interface WebDriverEventListener
{
  public abstract void beforeAlertAccept(WebDriver paramWebDriver);
  
  public abstract void afterAlertAccept(WebDriver paramWebDriver);
  
  public abstract void afterAlertDismiss(WebDriver paramWebDriver);
  
  public abstract void beforeAlertDismiss(WebDriver paramWebDriver);
  
  public abstract void beforeNavigateTo(String paramString, WebDriver paramWebDriver);
  
  public abstract void afterNavigateTo(String paramString, WebDriver paramWebDriver);
  
  public abstract void beforeNavigateBack(WebDriver paramWebDriver);
  
  public abstract void afterNavigateBack(WebDriver paramWebDriver);
  
  public abstract void beforeNavigateForward(WebDriver paramWebDriver);
  
  public abstract void afterNavigateForward(WebDriver paramWebDriver);
  
  public abstract void beforeNavigateRefresh(WebDriver paramWebDriver);
  
  public abstract void afterNavigateRefresh(WebDriver paramWebDriver);
  
  public abstract void beforeFindBy(By paramBy, WebElement paramWebElement, WebDriver paramWebDriver);
  
  public abstract void afterFindBy(By paramBy, WebElement paramWebElement, WebDriver paramWebDriver);
  
  public abstract void beforeClickOn(WebElement paramWebElement, WebDriver paramWebDriver);
  
  public abstract void afterClickOn(WebElement paramWebElement, WebDriver paramWebDriver);
  
  public abstract void beforeChangeValueOf(WebElement paramWebElement, WebDriver paramWebDriver, CharSequence[] paramArrayOfCharSequence);
  
  public abstract void afterChangeValueOf(WebElement paramWebElement, WebDriver paramWebDriver, CharSequence[] paramArrayOfCharSequence);
  
  public abstract void beforeScript(String paramString, WebDriver paramWebDriver);
  
  public abstract void afterScript(String paramString, WebDriver paramWebDriver);
  
  public abstract void onException(Throwable paramThrowable, WebDriver paramWebDriver);
}
