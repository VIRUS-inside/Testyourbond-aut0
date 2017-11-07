package org.openqa.selenium.remote;

import org.openqa.selenium.remote.http.HttpMethod;

public abstract interface CommandCodec<T>
{
  public abstract T encode(Command paramCommand);
  
  public abstract Command decode(T paramT);
  
  public abstract void defineCommand(String paramString1, HttpMethod paramHttpMethod, String paramString2);
  
  public abstract void alias(String paramString1, String paramString2);
}
