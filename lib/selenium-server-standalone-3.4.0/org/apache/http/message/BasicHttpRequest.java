package org.apache.http.message;

import org.apache.http.HttpRequest;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolVersion;
import org.apache.http.RequestLine;
import org.apache.http.util.Args;







































public class BasicHttpRequest
  extends AbstractHttpMessage
  implements HttpRequest
{
  private final String method;
  private final String uri;
  private RequestLine requestline;
  
  public BasicHttpRequest(String method, String uri)
  {
    this.method = ((String)Args.notNull(method, "Method name"));
    this.uri = ((String)Args.notNull(uri, "Request URI"));
    requestline = null;
  }
  







  public BasicHttpRequest(String method, String uri, ProtocolVersion ver)
  {
    this(new BasicRequestLine(method, uri, ver));
  }
  





  public BasicHttpRequest(RequestLine requestline)
  {
    this.requestline = ((RequestLine)Args.notNull(requestline, "Request line"));
    method = requestline.getMethod();
    uri = requestline.getUri();
  }
  





  public ProtocolVersion getProtocolVersion()
  {
    return getRequestLine().getProtocolVersion();
  }
  





  public RequestLine getRequestLine()
  {
    if (requestline == null) {
      requestline = new BasicRequestLine(method, uri, HttpVersion.HTTP_1_1);
    }
    return requestline;
  }
  
  public String toString()
  {
    return method + ' ' + uri + ' ' + headergroup;
  }
}
