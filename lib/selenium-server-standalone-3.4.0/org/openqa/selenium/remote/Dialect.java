package org.openqa.selenium.remote;

import org.openqa.selenium.remote.http.HttpRequest;
import org.openqa.selenium.remote.http.HttpResponse;
import org.openqa.selenium.remote.http.JsonHttpCommandCodec;
import org.openqa.selenium.remote.http.JsonHttpResponseCodec;
import org.openqa.selenium.remote.http.W3CHttpCommandCodec;
import org.openqa.selenium.remote.http.W3CHttpResponseCodec;

public enum Dialect
{
  OSS,  W3C;
  
  private Dialect() {}
  
  public abstract CommandCodec<HttpRequest> getCommandCodec();
  
  public abstract ResponseCodec<HttpResponse> getResponseCodec();
  
  public abstract String getEncodedElementKey();
}
