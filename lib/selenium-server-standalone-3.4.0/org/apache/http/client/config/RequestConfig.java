package org.apache.http.client.config;

import java.net.InetAddress;
import java.util.Collection;
import org.apache.http.HttpHost;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;

































@Contract(threading=ThreadingBehavior.IMMUTABLE)
public class RequestConfig
  implements Cloneable
{
  public static final RequestConfig DEFAULT = new Builder().build();
  
  private final boolean expectContinueEnabled;
  
  private final HttpHost proxy;
  
  private final InetAddress localAddress;
  private final boolean staleConnectionCheckEnabled;
  private final String cookieSpec;
  private final boolean redirectsEnabled;
  private final boolean relativeRedirectsAllowed;
  private final boolean circularRedirectsAllowed;
  private final int maxRedirects;
  private final boolean authenticationEnabled;
  private final Collection<String> targetPreferredAuthSchemes;
  private final Collection<String> proxyPreferredAuthSchemes;
  private final int connectionRequestTimeout;
  private final int connectTimeout;
  private final int socketTimeout;
  private final boolean contentCompressionEnabled;
  
  protected RequestConfig()
  {
    this(false, null, null, false, null, false, false, false, 0, false, null, null, 0, 0, 0, true);
  }
  
















  RequestConfig(boolean expectContinueEnabled, HttpHost proxy, InetAddress localAddress, boolean staleConnectionCheckEnabled, String cookieSpec, boolean redirectsEnabled, boolean relativeRedirectsAllowed, boolean circularRedirectsAllowed, int maxRedirects, boolean authenticationEnabled, Collection<String> targetPreferredAuthSchemes, Collection<String> proxyPreferredAuthSchemes, int connectionRequestTimeout, int connectTimeout, int socketTimeout, boolean contentCompressionEnabled)
  {
    this.expectContinueEnabled = expectContinueEnabled;
    this.proxy = proxy;
    this.localAddress = localAddress;
    this.staleConnectionCheckEnabled = staleConnectionCheckEnabled;
    this.cookieSpec = cookieSpec;
    this.redirectsEnabled = redirectsEnabled;
    this.relativeRedirectsAllowed = relativeRedirectsAllowed;
    this.circularRedirectsAllowed = circularRedirectsAllowed;
    this.maxRedirects = maxRedirects;
    this.authenticationEnabled = authenticationEnabled;
    this.targetPreferredAuthSchemes = targetPreferredAuthSchemes;
    this.proxyPreferredAuthSchemes = proxyPreferredAuthSchemes;
    this.connectionRequestTimeout = connectionRequestTimeout;
    this.connectTimeout = connectTimeout;
    this.socketTimeout = socketTimeout;
    this.contentCompressionEnabled = contentCompressionEnabled;
  }
  





















  public boolean isExpectContinueEnabled()
  {
    return expectContinueEnabled;
  }
  





  public HttpHost getProxy()
  {
    return proxy;
  }
  










  public InetAddress getLocalAddress()
  {
    return localAddress;
  }
  











  @Deprecated
  public boolean isStaleConnectionCheckEnabled()
  {
    return staleConnectionCheckEnabled;
  }
  






  public String getCookieSpec()
  {
    return cookieSpec;
  }
  





  public boolean isRedirectsEnabled()
  {
    return redirectsEnabled;
  }
  






  public boolean isRelativeRedirectsAllowed()
  {
    return relativeRedirectsAllowed;
  }
  







  public boolean isCircularRedirectsAllowed()
  {
    return circularRedirectsAllowed;
  }
  






  public int getMaxRedirects()
  {
    return maxRedirects;
  }
  





  public boolean isAuthenticationEnabled()
  {
    return authenticationEnabled;
  }
  






  public Collection<String> getTargetPreferredAuthSchemes()
  {
    return targetPreferredAuthSchemes;
  }
  






  public Collection<String> getProxyPreferredAuthSchemes()
  {
    return proxyPreferredAuthSchemes;
  }
  











  public int getConnectionRequestTimeout()
  {
    return connectionRequestTimeout;
  }
  










  public int getConnectTimeout()
  {
    return connectTimeout;
  }
  











  public int getSocketTimeout()
  {
    return socketTimeout;
  }
  








  @Deprecated
  public boolean isDecompressionEnabled()
  {
    return contentCompressionEnabled;
  }
  







  public boolean isContentCompressionEnabled()
  {
    return contentCompressionEnabled;
  }
  
  protected RequestConfig clone() throws CloneNotSupportedException
  {
    return (RequestConfig)super.clone();
  }
  
  public String toString()
  {
    StringBuilder builder = new StringBuilder();
    builder.append("[");
    builder.append("expectContinueEnabled=").append(expectContinueEnabled);
    builder.append(", proxy=").append(proxy);
    builder.append(", localAddress=").append(localAddress);
    builder.append(", cookieSpec=").append(cookieSpec);
    builder.append(", redirectsEnabled=").append(redirectsEnabled);
    builder.append(", relativeRedirectsAllowed=").append(relativeRedirectsAllowed);
    builder.append(", maxRedirects=").append(maxRedirects);
    builder.append(", circularRedirectsAllowed=").append(circularRedirectsAllowed);
    builder.append(", authenticationEnabled=").append(authenticationEnabled);
    builder.append(", targetPreferredAuthSchemes=").append(targetPreferredAuthSchemes);
    builder.append(", proxyPreferredAuthSchemes=").append(proxyPreferredAuthSchemes);
    builder.append(", connectionRequestTimeout=").append(connectionRequestTimeout);
    builder.append(", connectTimeout=").append(connectTimeout);
    builder.append(", socketTimeout=").append(socketTimeout);
    builder.append(", contentCompressionEnabled=").append(contentCompressionEnabled);
    builder.append("]");
    return builder.toString();
  }
  
  public static Builder custom() {
    return new Builder();
  }
  
  public static Builder copy(RequestConfig config)
  {
    return new Builder().setExpectContinueEnabled(config.isExpectContinueEnabled()).setProxy(config.getProxy()).setLocalAddress(config.getLocalAddress()).setStaleConnectionCheckEnabled(config.isStaleConnectionCheckEnabled()).setCookieSpec(config.getCookieSpec()).setRedirectsEnabled(config.isRedirectsEnabled()).setRelativeRedirectsAllowed(config.isRelativeRedirectsAllowed()).setCircularRedirectsAllowed(config.isCircularRedirectsAllowed()).setMaxRedirects(config.getMaxRedirects()).setAuthenticationEnabled(config.isAuthenticationEnabled()).setTargetPreferredAuthSchemes(config.getTargetPreferredAuthSchemes()).setProxyPreferredAuthSchemes(config.getProxyPreferredAuthSchemes()).setConnectionRequestTimeout(config.getConnectionRequestTimeout()).setConnectTimeout(config.getConnectTimeout()).setSocketTimeout(config.getSocketTimeout()).setDecompressionEnabled(config.isDecompressionEnabled()).setContentCompressionEnabled(config.isContentCompressionEnabled());
  }
  

  public static class Builder
  {
    private boolean expectContinueEnabled;
    
    private HttpHost proxy;
    
    private InetAddress localAddress;
    
    private boolean staleConnectionCheckEnabled;
    
    private String cookieSpec;
    
    private boolean redirectsEnabled;
    
    private boolean relativeRedirectsAllowed;
    
    private boolean circularRedirectsAllowed;
    
    private int maxRedirects;
    
    private boolean authenticationEnabled;
    
    private Collection<String> targetPreferredAuthSchemes;
    
    private Collection<String> proxyPreferredAuthSchemes;
    
    private int connectionRequestTimeout;
    
    private int connectTimeout;
    
    private int socketTimeout;
    
    private boolean contentCompressionEnabled;
    

    Builder()
    {
      staleConnectionCheckEnabled = false;
      redirectsEnabled = true;
      maxRedirects = 50;
      relativeRedirectsAllowed = true;
      authenticationEnabled = true;
      connectionRequestTimeout = -1;
      connectTimeout = -1;
      socketTimeout = -1;
      contentCompressionEnabled = true;
    }
    
    public Builder setExpectContinueEnabled(boolean expectContinueEnabled) {
      this.expectContinueEnabled = expectContinueEnabled;
      return this;
    }
    
    public Builder setProxy(HttpHost proxy) {
      this.proxy = proxy;
      return this;
    }
    
    public Builder setLocalAddress(InetAddress localAddress) {
      this.localAddress = localAddress;
      return this;
    }
    



    @Deprecated
    public Builder setStaleConnectionCheckEnabled(boolean staleConnectionCheckEnabled)
    {
      this.staleConnectionCheckEnabled = staleConnectionCheckEnabled;
      return this;
    }
    
    public Builder setCookieSpec(String cookieSpec) {
      this.cookieSpec = cookieSpec;
      return this;
    }
    
    public Builder setRedirectsEnabled(boolean redirectsEnabled) {
      this.redirectsEnabled = redirectsEnabled;
      return this;
    }
    
    public Builder setRelativeRedirectsAllowed(boolean relativeRedirectsAllowed) {
      this.relativeRedirectsAllowed = relativeRedirectsAllowed;
      return this;
    }
    
    public Builder setCircularRedirectsAllowed(boolean circularRedirectsAllowed) {
      this.circularRedirectsAllowed = circularRedirectsAllowed;
      return this;
    }
    
    public Builder setMaxRedirects(int maxRedirects) {
      this.maxRedirects = maxRedirects;
      return this;
    }
    
    public Builder setAuthenticationEnabled(boolean authenticationEnabled) {
      this.authenticationEnabled = authenticationEnabled;
      return this;
    }
    
    public Builder setTargetPreferredAuthSchemes(Collection<String> targetPreferredAuthSchemes) {
      this.targetPreferredAuthSchemes = targetPreferredAuthSchemes;
      return this;
    }
    
    public Builder setProxyPreferredAuthSchemes(Collection<String> proxyPreferredAuthSchemes) {
      this.proxyPreferredAuthSchemes = proxyPreferredAuthSchemes;
      return this;
    }
    
    public Builder setConnectionRequestTimeout(int connectionRequestTimeout) {
      this.connectionRequestTimeout = connectionRequestTimeout;
      return this;
    }
    
    public Builder setConnectTimeout(int connectTimeout) {
      this.connectTimeout = connectTimeout;
      return this;
    }
    
    public Builder setSocketTimeout(int socketTimeout) {
      this.socketTimeout = socketTimeout;
      return this;
    }
    



    @Deprecated
    public Builder setDecompressionEnabled(boolean decompressionEnabled)
    {
      contentCompressionEnabled = decompressionEnabled;
      return this;
    }
    
    public Builder setContentCompressionEnabled(boolean contentCompressionEnabled) {
      this.contentCompressionEnabled = contentCompressionEnabled;
      return this;
    }
    
    public RequestConfig build() {
      return new RequestConfig(expectContinueEnabled, proxy, localAddress, staleConnectionCheckEnabled, cookieSpec, redirectsEnabled, relativeRedirectsAllowed, circularRedirectsAllowed, maxRedirects, authenticationEnabled, targetPreferredAuthSchemes, proxyPreferredAuthSchemes, connectionRequestTimeout, connectTimeout, socketTimeout, contentCompressionEnabled);
    }
  }
}
