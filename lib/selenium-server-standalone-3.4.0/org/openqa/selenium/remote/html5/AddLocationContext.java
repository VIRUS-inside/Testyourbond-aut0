package org.openqa.selenium.remote.html5;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.html5.LocationContext;
import org.openqa.selenium.remote.AugmenterProvider;
import org.openqa.selenium.remote.ExecuteMethod;
import org.openqa.selenium.remote.InterfaceImplementation;
















public class AddLocationContext
  implements AugmenterProvider
{
  public AddLocationContext() {}
  
  public Class<?> getDescribedInterface()
  {
    return LocationContext.class;
  }
  
  public InterfaceImplementation getImplementation(Object value)
  {
    new InterfaceImplementation()
    {
      public Object invoke(ExecuteMethod executeMethod, Object self, Method method, Object... args)
      {
        LocationContext context = new RemoteLocationContext(executeMethod);
        try {
          return method.invoke(context, args);
        } catch (IllegalAccessException e) {
          throw new WebDriverException(e);
        } catch (InvocationTargetException e) {
          throw new RuntimeException(e.getCause());
        }
      }
    };
  }
}
