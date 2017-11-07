package org.openqa.selenium.remote.html5;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.html5.WebStorage;
import org.openqa.selenium.remote.AugmenterProvider;
import org.openqa.selenium.remote.ExecuteMethod;
import org.openqa.selenium.remote.InterfaceImplementation;
















public class AddWebStorage
  implements AugmenterProvider
{
  public AddWebStorage() {}
  
  public Class<?> getDescribedInterface()
  {
    return WebStorage.class;
  }
  
  public InterfaceImplementation getImplementation(Object value)
  {
    new InterfaceImplementation()
    {
      public Object invoke(ExecuteMethod executeMethod, Object self, Method method, Object... args)
      {
        RemoteWebStorage storage = new RemoteWebStorage(executeMethod);
        try {
          return method.invoke(storage, args);
        } catch (IllegalAccessException e) {
          throw new WebDriverException(e);
        } catch (InvocationTargetException e) {
          throw new RuntimeException(e.getCause());
        }
      }
    };
  }
}
