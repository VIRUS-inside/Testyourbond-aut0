package com.sun.jna.win32;

import com.sun.jna.Function;
import com.sun.jna.FunctionMapper;
import com.sun.jna.NativeLibrary;
import java.lang.reflect.Method;












public class W32APIFunctionMapper
  implements FunctionMapper
{
  public static final FunctionMapper UNICODE = new W32APIFunctionMapper(true);
  public static final FunctionMapper ASCII = new W32APIFunctionMapper(false);
  private final String suffix;
  
  protected W32APIFunctionMapper(boolean unicode) { suffix = (unicode ? "W" : "A"); }
  

  public String getFunctionName(NativeLibrary library, Method method)
  {
    String name = method.getName();
    if ((!name.endsWith("W")) && (!name.endsWith("A"))) {
      try {
        name = library.getFunction(name + suffix, 1).getName();
      }
      catch (UnsatisfiedLinkError e) {}
    }
    

    return name;
  }
}
