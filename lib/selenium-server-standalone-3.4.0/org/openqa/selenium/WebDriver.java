package org.openqa.selenium;

import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.logging.Logs;

public abstract interface WebDriver
  extends SearchContext
{
  public abstract void get(String paramString);
  
  public abstract String getCurrentUrl();
  
  public abstract String getTitle();
  
  public abstract List<WebElement> findElements(By paramBy);
  
  public abstract WebElement findElement(By paramBy);
  
  public abstract String getPageSource();
  
  public abstract void close();
  
  public abstract void quit();
  
  public abstract Set<String> getWindowHandles();
  
  public abstract String getWindowHandle();
  
  public abstract TargetLocator switchTo();
  
  public abstract Navigation navigate();
  
  public abstract Options manage();
  
  @Beta
  public static abstract interface Window
  {
    public abstract void setSize(Dimension paramDimension);
    
    public abstract void setPosition(Point paramPoint);
    
    public abstract Dimension getSize();
    
    public abstract Point getPosition();
    
    public abstract void maximize();
    
    public abstract void fullscreen();
  }
  
  public static abstract interface ImeHandler
  {
    public abstract List<String> getAvailableEngines();
    
    public abstract String getActiveEngine();
    
    public abstract boolean isActivated();
    
    public abstract void deactivate();
    
    public abstract void activateEngine(String paramString);
  }
  
  public static abstract interface Navigation
  {
    public abstract void back();
    
    public abstract void forward();
    
    public abstract void to(String paramString);
    
    public abstract void to(URL paramURL);
    
    public abstract void refresh();
  }
  
  public static abstract interface TargetLocator
  {
    public abstract WebDriver frame(int paramInt);
    
    public abstract WebDriver frame(String paramString);
    
    public abstract WebDriver frame(WebElement paramWebElement);
    
    public abstract WebDriver parentFrame();
    
    public abstract WebDriver window(String paramString);
    
    public abstract WebDriver defaultContent();
    
    public abstract WebElement activeElement();
    
    public abstract Alert alert();
  }
  
  public static abstract interface Timeouts
  {
    public abstract Timeouts implicitlyWait(long paramLong, TimeUnit paramTimeUnit);
    
    public abstract Timeouts setScriptTimeout(long paramLong, TimeUnit paramTimeUnit);
    
    public abstract Timeouts pageLoadTimeout(long paramLong, TimeUnit paramTimeUnit);
  }
  
  public static abstract interface Options
  {
    public abstract void addCookie(Cookie paramCookie);
    
    public abstract void deleteCookieNamed(String paramString);
    
    public abstract void deleteCookie(Cookie paramCookie);
    
    public abstract void deleteAllCookies();
    
    public abstract Set<Cookie> getCookies();
    
    public abstract Cookie getCookieNamed(String paramString);
    
    public abstract WebDriver.Timeouts timeouts();
    
    public abstract WebDriver.ImeHandler ime();
    
    public abstract WebDriver.Window window();
    
    @Beta
    public abstract Logs logs();
  }
}
