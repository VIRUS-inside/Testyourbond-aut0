package org.openqa.selenium.remote;

import com.google.common.collect.ImmutableMap;
import java.lang.reflect.Method;
import org.openqa.selenium.DeviceRotation;
import org.openqa.selenium.Rotatable;
import org.openqa.selenium.ScreenOrientation;
















public class AddRotatable
  implements AugmenterProvider
{
  public AddRotatable() {}
  
  public Class<?> getDescribedInterface()
  {
    return Rotatable.class;
  }
  
  public InterfaceImplementation getImplementation(Object value) {
    new InterfaceImplementation() {
      public Object invoke(ExecuteMethod executeMethod, Object self, Method method, Object... args) {
        String m = method.getName();
        Object response;
        Object response; switch (m) {
        case "rotate":  Object response;
          if ((args[0] instanceof ScreenOrientation)) {
            response = executeMethod.execute("setScreenOrientation", ImmutableMap.of("orientation", args[0])); } else { Object response;
            if ((args[0] instanceof DeviceRotation)) {
              response = executeMethod.execute("setScreenOrientation", ((DeviceRotation)args[0]).parameters());
            } else
              throw new IllegalArgumentException("rotate parameter must be either of type 'ScreenOrientation' or 'DeviceRotation'");
          }
          break;
        case "getOrientation": 
          response = ScreenOrientation.valueOf((String)executeMethod.execute("getScreenOrientation", null));
          break;
        case "rotation": 
          response = (DeviceRotation)executeMethod.execute("getScreenRotation", null);
          break;
        default: 
          throw new IllegalArgumentException(method.getName() + ", Not defined in rotatable interface"); }
        Object response;
        return response;
      }
    };
  }
}
