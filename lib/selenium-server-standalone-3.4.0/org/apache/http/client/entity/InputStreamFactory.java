package org.apache.http.client.entity;

import java.io.IOException;
import java.io.InputStream;

public abstract interface InputStreamFactory
{
  public abstract InputStream create(InputStream paramInputStream)
    throws IOException;
}
