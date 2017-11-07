package org.apache.http.client.params;

import java.util.Collection;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.params.HttpAbstractParamBean;
import org.apache.http.params.HttpParams;




































@Deprecated
public class ClientParamBean
  extends HttpAbstractParamBean
{
  public ClientParamBean(HttpParams params)
  {
    super(params);
  }
  


  @Deprecated
  public void setConnectionManagerFactoryClassName(String factory)
  {
    params.setParameter("http.connection-manager.factory-class-name", factory);
  }
  
  public void setHandleRedirects(boolean handle) {
    params.setBooleanParameter("http.protocol.handle-redirects", handle);
  }
  
  public void setRejectRelativeRedirect(boolean reject) {
    params.setBooleanParameter("http.protocol.reject-relative-redirect", reject);
  }
  
  public void setMaxRedirects(int maxRedirects) {
    params.setIntParameter("http.protocol.max-redirects", maxRedirects);
  }
  
  public void setAllowCircularRedirects(boolean allow) {
    params.setBooleanParameter("http.protocol.allow-circular-redirects", allow);
  }
  
  public void setHandleAuthentication(boolean handle) {
    params.setBooleanParameter("http.protocol.handle-authentication", handle);
  }
  
  public void setCookiePolicy(String policy) {
    params.setParameter("http.protocol.cookie-policy", policy);
  }
  
  public void setVirtualHost(HttpHost host) {
    params.setParameter("http.virtual-host", host);
  }
  
  public void setDefaultHeaders(Collection<Header> headers) {
    params.setParameter("http.default-headers", headers);
  }
  
  public void setDefaultHost(HttpHost host) {
    params.setParameter("http.default-host", host);
  }
  


  public void setConnectionManagerTimeout(long timeout)
  {
    params.setLongParameter("http.conn-manager.timeout", timeout);
  }
}
