package org.openqa.selenium.remote.mobile;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.mobile.NetworkConnection;
import org.openqa.selenium.remote.AugmenterProvider;
import org.openqa.selenium.remote.ExecuteMethod;
import org.openqa.selenium.remote.InterfaceImplementation;
















public class AddNetworkConnection
  implements AugmenterProvider
{
  public AddNetworkConnection() {}
  
  public Class<?> getDescribedInterface()
  {
    return NetworkConnection.class;
  }
  
  public InterfaceImplementation getImplementation(Object value)
  {
    new InterfaceImplementation()
    {

      public Object invoke(ExecuteMethod executeMethod, Object self, Method method, Object... args)
      {
        NetworkConnection connection = new RemoteNetworkConnection(executeMethod);
        try {
          return method.invoke(connection, args);
        } catch (IllegalAccessException e) {
          throw new WebDriverException(e);
        } catch (InvocationTargetException e) {
          throw new RuntimeException(e.getCause());
        }
      }
    };
  }
}
