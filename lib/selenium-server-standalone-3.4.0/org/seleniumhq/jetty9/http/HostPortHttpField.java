package org.seleniumhq.jetty9.http;

import org.seleniumhq.jetty9.util.HostPort;
























public class HostPortHttpField
  extends HttpField
{
  final HostPort _hostPort;
  
  public HostPortHttpField(String authority)
  {
    this(HttpHeader.HOST, HttpHeader.HOST.asString(), authority);
  }
  

  protected HostPortHttpField(HttpHeader header, String name, String authority)
  {
    super(header, name, authority);
    try
    {
      _hostPort = new HostPort(authority);
    }
    catch (Exception e)
    {
      throw new BadMessageException(400, "Bad HostPort", e);
    }
  }
  




  public String getHost()
  {
    return _hostPort.getHost();
  }
  




  public int getPort()
  {
    return _hostPort.getPort();
  }
  





  public int getPort(int defaultPort)
  {
    return _hostPort.getPort(defaultPort);
  }
}
