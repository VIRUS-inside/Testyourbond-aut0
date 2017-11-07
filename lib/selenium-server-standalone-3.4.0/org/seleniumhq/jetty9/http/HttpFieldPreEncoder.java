package org.seleniumhq.jetty9.http;

public abstract interface HttpFieldPreEncoder
{
  public abstract HttpVersion getHttpVersion();
  
  public abstract byte[] getEncodedField(HttpHeader paramHttpHeader, String paramString1, String paramString2);
}
