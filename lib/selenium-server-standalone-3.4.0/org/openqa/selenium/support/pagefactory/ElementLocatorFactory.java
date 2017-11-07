package org.openqa.selenium.support.pagefactory;

import java.lang.reflect.Field;

public abstract interface ElementLocatorFactory
{
  public abstract ElementLocator createLocator(Field paramField);
}
