package org.apache.http.impl.conn;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.conn.SchemePortResolver;
import org.apache.http.protocol.HttpContext;






































@Contract(threading=ThreadingBehavior.IMMUTABLE_CONDITIONAL)
public class SystemDefaultRoutePlanner
  extends DefaultRoutePlanner
{
  private final ProxySelector proxySelector;
  
  public SystemDefaultRoutePlanner(SchemePortResolver schemePortResolver, ProxySelector proxySelector)
  {
    super(schemePortResolver);
    this.proxySelector = proxySelector;
  }
  


  public SystemDefaultRoutePlanner(ProxySelector proxySelector)
  {
    this(null, proxySelector);
  }
  

  protected HttpHost determineProxy(HttpHost target, HttpRequest request, HttpContext context)
    throws HttpException
  {
    URI targetURI;
    try
    {
      targetURI = new URI(target.toURI());
    } catch (URISyntaxException ex) {
      throw new HttpException("Cannot convert host to URI: " + target, ex);
    }
    ProxySelector proxySelectorInstance = proxySelector;
    if (proxySelectorInstance == null) {
      proxySelectorInstance = ProxySelector.getDefault();
    }
    if (proxySelectorInstance == null)
    {
      return null;
    }
    List<Proxy> proxies = proxySelectorInstance.select(targetURI);
    Proxy p = chooseProxy(proxies);
    HttpHost result = null;
    if (p.type() == Proxy.Type.HTTP)
    {
      if (!(p.address() instanceof InetSocketAddress)) {
        throw new HttpException("Unable to handle non-Inet proxy address: " + p.address());
      }
      InetSocketAddress isa = (InetSocketAddress)p.address();
      
      result = new HttpHost(getHost(isa), isa.getPort());
    }
    
    return result;
  }
  



  private String getHost(InetSocketAddress isa)
  {
    return isa.isUnresolved() ? isa.getHostName() : isa.getAddress().getHostAddress();
  }
  

  private Proxy chooseProxy(List<Proxy> proxies)
  {
    Proxy result = null;
    
    for (int i = 0; (result == null) && (i < proxies.size()); i++) {
      Proxy p = (Proxy)proxies.get(i);
      switch (1.$SwitchMap$java$net$Proxy$Type[p.type().ordinal()])
      {
      case 1: 
      case 2: 
        result = p;
      }
      
    }
    




    if (result == null)
    {


      result = Proxy.NO_PROXY;
    }
    return result;
  }
}
