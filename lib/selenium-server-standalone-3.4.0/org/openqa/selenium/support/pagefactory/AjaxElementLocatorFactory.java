package org.openqa.selenium.support.pagefactory;

import java.lang.reflect.Field;
import org.openqa.selenium.SearchContext;
















public class AjaxElementLocatorFactory
  implements ElementLocatorFactory
{
  private final SearchContext searchContext;
  private final int timeOutInSeconds;
  
  public AjaxElementLocatorFactory(SearchContext searchContext, int timeOutInSeconds)
  {
    this.searchContext = searchContext;
    this.timeOutInSeconds = timeOutInSeconds;
  }
  
  public ElementLocator createLocator(Field field) {
    return new AjaxElementLocator(searchContext, field, timeOutInSeconds);
  }
}
