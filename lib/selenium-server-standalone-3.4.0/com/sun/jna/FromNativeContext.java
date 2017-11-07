package com.sun.jna;






public class FromNativeContext
{
  private Class type;
  





  FromNativeContext(Class javaType)
  {
    type = javaType;
  }
  
  public Class getTargetType() {
    return type;
  }
}
