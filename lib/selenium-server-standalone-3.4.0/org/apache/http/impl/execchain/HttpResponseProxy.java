package org.apache.http.impl.execchain;

import java.io.IOException;
import java.util.Locale;
import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.params.HttpParams;

































class HttpResponseProxy
  implements CloseableHttpResponse
{
  private final HttpResponse original;
  private final ConnectionHolder connHolder;
  
  public HttpResponseProxy(HttpResponse original, ConnectionHolder connHolder)
  {
    this.original = original;
    this.connHolder = connHolder;
    ResponseEntityProxy.enchance(original, connHolder);
  }
  
  public void close() throws IOException
  {
    if (connHolder != null) {
      connHolder.close();
    }
  }
  
  public StatusLine getStatusLine()
  {
    return original.getStatusLine();
  }
  
  public void setStatusLine(StatusLine statusline)
  {
    original.setStatusLine(statusline);
  }
  
  public void setStatusLine(ProtocolVersion ver, int code)
  {
    original.setStatusLine(ver, code);
  }
  
  public void setStatusLine(ProtocolVersion ver, int code, String reason)
  {
    original.setStatusLine(ver, code, reason);
  }
  
  public void setStatusCode(int code) throws IllegalStateException
  {
    original.setStatusCode(code);
  }
  
  public void setReasonPhrase(String reason) throws IllegalStateException
  {
    original.setReasonPhrase(reason);
  }
  
  public HttpEntity getEntity()
  {
    return original.getEntity();
  }
  
  public void setEntity(HttpEntity entity)
  {
    original.setEntity(entity);
  }
  
  public Locale getLocale()
  {
    return original.getLocale();
  }
  
  public void setLocale(Locale loc)
  {
    original.setLocale(loc);
  }
  
  public ProtocolVersion getProtocolVersion()
  {
    return original.getProtocolVersion();
  }
  
  public boolean containsHeader(String name)
  {
    return original.containsHeader(name);
  }
  
  public Header[] getHeaders(String name)
  {
    return original.getHeaders(name);
  }
  
  public Header getFirstHeader(String name)
  {
    return original.getFirstHeader(name);
  }
  
  public Header getLastHeader(String name)
  {
    return original.getLastHeader(name);
  }
  
  public Header[] getAllHeaders()
  {
    return original.getAllHeaders();
  }
  
  public void addHeader(Header header)
  {
    original.addHeader(header);
  }
  
  public void addHeader(String name, String value)
  {
    original.addHeader(name, value);
  }
  
  public void setHeader(Header header)
  {
    original.setHeader(header);
  }
  
  public void setHeader(String name, String value)
  {
    original.setHeader(name, value);
  }
  
  public void setHeaders(Header[] headers)
  {
    original.setHeaders(headers);
  }
  
  public void removeHeader(Header header)
  {
    original.removeHeader(header);
  }
  
  public void removeHeaders(String name)
  {
    original.removeHeaders(name);
  }
  
  public HeaderIterator headerIterator()
  {
    return original.headerIterator();
  }
  
  public HeaderIterator headerIterator(String name)
  {
    return original.headerIterator(name);
  }
  
  @Deprecated
  public HttpParams getParams()
  {
    return original.getParams();
  }
  
  @Deprecated
  public void setParams(HttpParams params)
  {
    original.setParams(params);
  }
  
  public String toString()
  {
    StringBuilder sb = new StringBuilder("HttpResponseProxy{");
    sb.append(original);
    sb.append('}');
    return sb.toString();
  }
}
