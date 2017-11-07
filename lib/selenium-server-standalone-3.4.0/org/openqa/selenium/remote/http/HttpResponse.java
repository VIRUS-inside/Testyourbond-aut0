package org.openqa.selenium.remote.http;







public class HttpResponse
  extends HttpMessage
{
  public static final String HTTP_TARGET_HOST = "http.target.host";
  





  public HttpResponse() {}
  





  private int status = 200;
  
  public int getStatus() {
    return status;
  }
  
  public void setStatus(int status) {
    this.status = status;
  }
  




  public void setTargetHost(String host)
  {
    setAttribute("http.target.host", host);
  }
  




  public String getTargetHost()
  {
    return (String)getAttribute("http.target.host");
  }
}
