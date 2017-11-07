package org.openqa.selenium.remote;

import java.lang.reflect.Method;
import org.openqa.selenium.interactions.HasTouchScreen;

















public class AddRemoteTouchScreen
  implements AugmenterProvider
{
  public AddRemoteTouchScreen() {}
  
  public Class<?> getDescribedInterface()
  {
    return HasTouchScreen.class;
  }
  
  public InterfaceImplementation getImplementation(Object value)
  {
    new InterfaceImplementation()
    {

      public Object invoke(ExecuteMethod executeMethod, Object self, Method method, Object... args)
      {
        if ("getTouch".equals(method.getName())) {
          return new RemoteTouchScreen(executeMethod);
        }
        return null;
      }
    };
  }
}
