package com.google.common.escape;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;













































































@GwtCompatible
public abstract class Escaper
{
  private final Function<String, String> asFunction = new Function()
  {
    public String apply(String from)
    {
      return escape(from);
    }
  };
  
  protected Escaper() {}
  
  public abstract String escape(String paramString);
  
  public final Function<String, String> asFunction() { return asFunction; }
}
