package org.openqa.selenium.internal;

import java.util.List;
import org.openqa.selenium.WebElement;

public abstract interface FindsByLinkText
{
  public abstract WebElement findElementByLinkText(String paramString);
  
  public abstract List<WebElement> findElementsByLinkText(String paramString);
  
  public abstract WebElement findElementByPartialLinkText(String paramString);
  
  public abstract List<WebElement> findElementsByPartialLinkText(String paramString);
}
