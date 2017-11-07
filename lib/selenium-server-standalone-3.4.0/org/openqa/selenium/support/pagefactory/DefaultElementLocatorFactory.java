package org.openqa.selenium.support.pagefactory;

import java.lang.reflect.Field;
import org.openqa.selenium.SearchContext;
















public final class DefaultElementLocatorFactory
  implements ElementLocatorFactory
{
  private final SearchContext searchContext;
  
  public DefaultElementLocatorFactory(SearchContext searchContext)
  {
    this.searchContext = searchContext;
  }
  
  public ElementLocator createLocator(Field field) {
    return new DefaultElementLocator(searchContext, field);
  }
}
