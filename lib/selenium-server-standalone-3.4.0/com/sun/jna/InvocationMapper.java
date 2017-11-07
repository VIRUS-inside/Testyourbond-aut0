package com.sun.jna;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public abstract interface InvocationMapper
{
  public abstract InvocationHandler getInvocationHandler(NativeLibrary paramNativeLibrary, Method paramMethod);
}
