package com.sun.jna;

import java.lang.reflect.Method;

public class CallbackResultContext
  extends ToNativeContext {
  private Method method;
  
  CallbackResultContext(Method callbackMethod) { method = callbackMethod; }
  
  public Method getMethod() { return method; }
}
