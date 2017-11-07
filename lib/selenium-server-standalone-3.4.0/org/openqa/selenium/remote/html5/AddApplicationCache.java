package org.openqa.selenium.remote.html5;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.html5.ApplicationCache;
import org.openqa.selenium.remote.AugmenterProvider;
import org.openqa.selenium.remote.ExecuteMethod;
import org.openqa.selenium.remote.InterfaceImplementation;
















public class AddApplicationCache
  implements AugmenterProvider
{
  public AddApplicationCache() {}
  
  public Class<?> getDescribedInterface()
  {
    return ApplicationCache.class;
  }
  
  public InterfaceImplementation getImplementation(Object value)
  {
    new InterfaceImplementation()
    {
      public Object invoke(ExecuteMethod executeMethod, Object self, Method method, Object... args)
      {
        RemoteApplicationCache cache = new RemoteApplicationCache(executeMethod);
        try {
          return method.invoke(cache, args);
        } catch (IllegalAccessException e) {
          throw new WebDriverException(e);
        } catch (InvocationTargetException e) {
          throw new RuntimeException(e.getCause());
        }
      }
    };
  }
}
