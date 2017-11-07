package com.sun.jna;

import java.lang.reflect.Method;










public class CallbackParameterContext
  extends FromNativeContext
{
  private Method method;
  private Object[] args;
  private int index;
  
  CallbackParameterContext(Class javaType, Method m, Object[] args, int index)
  {
    super(javaType);
    method = m;
    this.args = args;
    this.index = index; }
  
  public Method getMethod() { return method; }
  public Object[] getArguments() { return args; }
  public int getIndex() { return index; }
}
