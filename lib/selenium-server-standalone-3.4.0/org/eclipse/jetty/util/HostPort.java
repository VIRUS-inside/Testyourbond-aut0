package org.eclipse.jetty.util;









public class HostPort
{
  private final String _host;
  






  private final int _port;
  







  public HostPort(String authority)
    throws IllegalArgumentException
  {
    if (authority == null) {
      throw new IllegalArgumentException("No Authority");
    }
    try {
      if (authority.isEmpty())
      {
        _host = authority;
        _port = 0;
      }
      else if (authority.charAt(0) == '[')
      {

        int close = authority.lastIndexOf(']');
        if (close < 0)
          throw new IllegalArgumentException("Bad IPv6 host");
        _host = authority.substring(0, close + 1);
        
        if (authority.length() > close + 1)
        {
          if (authority.charAt(close + 1) != ':')
            throw new IllegalArgumentException("Bad IPv6 port");
          _port = StringUtil.toInt(authority, close + 2);
        }
        else {
          _port = 0;
        }
      }
      else
      {
        int c = authority.lastIndexOf(':');
        if (c >= 0)
        {
          _host = authority.substring(0, c);
          _port = StringUtil.toInt(authority, c + 1);
        }
        else
        {
          _host = authority;
          _port = 0;
        }
      }
    }
    catch (IllegalArgumentException iae)
    {
      throw iae;
    }
    catch (Exception ex)
    {
      throw new IllegalArgumentException("Bad HostPort") {};
    }
    


    if (_host == null)
      throw new IllegalArgumentException("Bad host");
    if (_port < 0) {
      throw new IllegalArgumentException("Bad port");
    }
  }
  



  public String getHost()
  {
    return _host;
  }
  




  public int getPort()
  {
    return _port;
  }
  





  public int getPort(int defaultPort)
  {
    return _port > 0 ? _port : defaultPort;
  }
  






  public static String normalizeHost(String host)
  {
    if ((host.isEmpty()) || (host.charAt(0) == '[') || (host.indexOf(':') < 0)) {
      return host;
    }
    
    return "[" + host + "]";
  }
}
