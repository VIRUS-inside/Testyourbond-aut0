package org.openqa.selenium.remote;

import com.google.common.collect.ImmutableMap;
import java.lang.reflect.Method;
import java.util.Map;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.internal.FindsByCssSelector;
















public class AddFindsChildByCss
  implements AugmenterProvider
{
  public AddFindsChildByCss() {}
  
  public Class<?> getDescribedInterface()
  {
    return FindsByCssSelector.class;
  }
  
  public InterfaceImplementation getImplementation(Object value) {
    new InterfaceImplementation() {
      public Object invoke(ExecuteMethod executeMethod, Object self, Method method, Object... args) {
        Object id = ((RemoteWebElement)self).getId();
        
        Map<String, ?> commandArgs = ImmutableMap.of("id", id, "using", "css selector", "value", args[0]);
        
        if ("findElementByCssSelector".equals(method.getName()))
          return executeMethod.execute("findElement", commandArgs);
        if ("findElementsByCssSelector".equals(method.getName())) {
          return executeMethod.execute("findElements", commandArgs);
        }
        
        throw new WebDriverException("Unmapped method: " + method.getName());
      }
    };
  }
}
