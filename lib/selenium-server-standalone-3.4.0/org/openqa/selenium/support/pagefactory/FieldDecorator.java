package org.openqa.selenium.support.pagefactory;

import java.lang.reflect.Field;

public abstract interface FieldDecorator
{
  public abstract Object decorate(ClassLoader paramClassLoader, Field paramField);
}
