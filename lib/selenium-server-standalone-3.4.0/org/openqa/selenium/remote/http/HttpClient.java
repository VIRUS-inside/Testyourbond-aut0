package org.openqa.selenium.remote.http;

import java.io.IOException;
import java.net.URL;

public abstract interface HttpClient
{
  public abstract HttpResponse execute(HttpRequest paramHttpRequest, boolean paramBoolean)
    throws IOException;
  
  public abstract void close()
    throws IOException;
  
  public static abstract interface Factory
  {
    public abstract HttpClient createClient(URL paramURL);
  }
}
