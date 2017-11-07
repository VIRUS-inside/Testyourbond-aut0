package org.openqa.selenium.remote;

public abstract interface ResponseCodec<T>
{
  public abstract T encode(Response paramResponse);
  
  public abstract Response decode(T paramT);
}
