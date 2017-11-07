package org.seleniumhq.jetty9.util;






















public class ConstantThrowable
  extends Throwable
{
  public ConstantThrowable()
  {
    this(null);
  }
  
  public ConstantThrowable(String name)
  {
    super(name, null, false, false);
  }
  

  public String toString()
  {
    return String.valueOf(getMessage());
  }
}
