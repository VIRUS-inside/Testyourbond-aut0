package org.apache.http.conn.scheme;

import java.util.Locale;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.util.Args;
import org.apache.http.util.LangUtils;








































































@Deprecated
@Contract(threading=ThreadingBehavior.IMMUTABLE)
public final class Scheme
{
  private final String name;
  private final SchemeSocketFactory socketFactory;
  private final int defaultPort;
  private final boolean layered;
  private String stringRep;
  
  public Scheme(String name, int port, SchemeSocketFactory factory)
  {
    Args.notNull(name, "Scheme name");
    Args.check((port > 0) && (port <= 65535), "Port is invalid");
    Args.notNull(factory, "Socket factory");
    this.name = name.toLowerCase(Locale.ENGLISH);
    defaultPort = port;
    if ((factory instanceof SchemeLayeredSocketFactory)) {
      layered = true;
      socketFactory = factory;
    } else if ((factory instanceof LayeredSchemeSocketFactory)) {
      layered = true;
      socketFactory = new SchemeLayeredSocketFactoryAdaptor2((LayeredSchemeSocketFactory)factory);
    } else {
      layered = false;
      socketFactory = factory;
    }
  }
  















  @Deprecated
  public Scheme(String name, SocketFactory factory, int port)
  {
    Args.notNull(name, "Scheme name");
    Args.notNull(factory, "Socket factory");
    Args.check((port > 0) && (port <= 65535), "Port is invalid");
    
    this.name = name.toLowerCase(Locale.ENGLISH);
    if ((factory instanceof LayeredSocketFactory)) {
      socketFactory = new SchemeLayeredSocketFactoryAdaptor((LayeredSocketFactory)factory);
      
      layered = true;
    } else {
      socketFactory = new SchemeSocketFactoryAdaptor(factory);
      layered = false;
    }
    defaultPort = port;
  }
  




  public final int getDefaultPort()
  {
    return defaultPort;
  }
  









  @Deprecated
  public final SocketFactory getSocketFactory()
  {
    if ((socketFactory instanceof SchemeSocketFactoryAdaptor)) {
      return ((SchemeSocketFactoryAdaptor)socketFactory).getFactory();
    }
    if (layered) {
      return new LayeredSocketFactoryAdaptor((LayeredSchemeSocketFactory)socketFactory);
    }
    
    return new SocketFactoryAdaptor(socketFactory);
  }
  










  public final SchemeSocketFactory getSchemeSocketFactory()
  {
    return socketFactory;
  }
  




  public final String getName()
  {
    return name;
  }
  





  public final boolean isLayered()
  {
    return layered;
  }
  








  public final int resolvePort(int port)
  {
    return port <= 0 ? defaultPort : port;
  }
  





  public final String toString()
  {
    if (stringRep == null) {
      StringBuilder buffer = new StringBuilder();
      buffer.append(name);
      buffer.append(':');
      buffer.append(Integer.toString(defaultPort));
      stringRep = buffer.toString();
    }
    return stringRep;
  }
  
  public final boolean equals(Object obj)
  {
    if (this == obj) {
      return true;
    }
    if ((obj instanceof Scheme)) {
      Scheme that = (Scheme)obj;
      return (name.equals(name)) && (defaultPort == defaultPort) && (layered == layered);
    }
    

    return false;
  }
  

  public int hashCode()
  {
    int hash = 17;
    hash = LangUtils.hashCode(hash, defaultPort);
    hash = LangUtils.hashCode(hash, name);
    hash = LangUtils.hashCode(hash, layered);
    return hash;
  }
}
