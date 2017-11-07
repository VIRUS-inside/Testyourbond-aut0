package org.openqa.selenium.remote.http;






public class HttpRequest
  extends HttpMessage
{
  private final HttpMethod method;
  




  private final String uri;
  





  public HttpRequest(HttpMethod method, String uri)
  {
    this.method = method;
    this.uri = uri;
  }
  
  public String getUri() {
    return uri;
  }
  
  public HttpMethod getMethod() {
    return method;
  }
}
