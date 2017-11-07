package com.gargoylesoftware.htmlunit.httpclient;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.protocol.HttpContext;

















public final class HtmlUnitRedirectStrategie
  extends DefaultRedirectStrategy
{
  public HtmlUnitRedirectStrategie() {}
  
  public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context)
    throws ProtocolException
  {
    return (super.isRedirected(request, response, context)) && (response.getFirstHeader("location") != null);
  }
}
