package org.openqa.selenium.support.pagefactory;

import java.lang.reflect.Field;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;



























public class DefaultElementLocator
  implements ElementLocator
{
  private final SearchContext searchContext;
  private final boolean shouldCache;
  private final By by;
  private WebElement cachedElement;
  private List<WebElement> cachedElementList;
  
  public DefaultElementLocator(SearchContext searchContext, Field field)
  {
    this(searchContext, new Annotations(field));
  }
  





  public DefaultElementLocator(SearchContext searchContext, AbstractAnnotations annotations)
  {
    this.searchContext = searchContext;
    shouldCache = annotations.isLookupCached();
    by = annotations.buildBy();
  }
  


  public WebElement findElement()
  {
    if ((cachedElement != null) && (shouldCache)) {
      return cachedElement;
    }
    
    WebElement element = searchContext.findElement(by);
    if (shouldCache) {
      cachedElement = element;
    }
    
    return element;
  }
  


  public List<WebElement> findElements()
  {
    if ((cachedElementList != null) && (shouldCache)) {
      return cachedElementList;
    }
    
    List<WebElement> elements = searchContext.findElements(by);
    if (shouldCache) {
      cachedElementList = elements;
    }
    
    return elements;
  }
  
  public String toString()
  {
    return getClass().getSimpleName() + " '" + by + "'";
  }
}
