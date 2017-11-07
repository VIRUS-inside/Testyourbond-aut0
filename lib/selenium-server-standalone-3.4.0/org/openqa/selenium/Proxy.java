package org.openqa.selenium;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

























public class Proxy
{
  public Proxy() {}
  
  public static enum ProxyType
  {
    DIRECT, 
    MANUAL, 
    PAC, 
    
    RESERVED_1, 
    
    AUTODETECT, 
    SYSTEM, 
    
    UNSPECIFIED;
    
    private ProxyType() {} }
  private ProxyType proxyType = ProxyType.UNSPECIFIED;
  private boolean autodetect = false;
  
  private String ftpProxy;
  
  private String httpProxy;
  
  private String noProxy;
  private String sslProxy;
  private String socksProxy;
  private String socksUsername;
  private String socksPassword;
  private String proxyAutoconfigUrl;
  
  public Proxy(Map<String, ?> raw)
  {
    if ((raw.containsKey("proxyType")) && (raw.get("proxyType") != null)) {
      setProxyType(ProxyType.valueOf(((String)raw.get("proxyType")).toUpperCase()));
    }
    if ((raw.containsKey("ftpProxy")) && (raw.get("ftpProxy") != null)) {
      setFtpProxy((String)raw.get("ftpProxy"));
    }
    if ((raw.containsKey("httpProxy")) && (raw.get("httpProxy") != null)) {
      setHttpProxy((String)raw.get("httpProxy"));
    }
    if ((raw.containsKey("noProxy")) && (raw.get("noProxy") != null)) {
      setNoProxy((String)raw.get("noProxy"));
    }
    if ((raw.containsKey("sslProxy")) && (raw.get("sslProxy") != null)) {
      setSslProxy((String)raw.get("sslProxy"));
    }
    if ((raw.containsKey("socksProxy")) && (raw.get("socksProxy") != null)) {
      setSocksProxy((String)raw.get("socksProxy"));
    }
    if ((raw.containsKey("socksUsername")) && (raw.get("socksUsername") != null)) {
      setSocksUsername((String)raw.get("socksUsername"));
    }
    if ((raw.containsKey("socksPassword")) && (raw.get("socksPassword") != null)) {
      setSocksPassword((String)raw.get("socksPassword"));
    }
    if ((raw.containsKey("proxyAutoconfigUrl")) && (raw.get("proxyAutoconfigUrl") != null)) {
      setProxyAutoconfigUrl((String)raw.get("proxyAutoconfigUrl"));
    }
    if ((raw.containsKey("autodetect")) && (raw.get("autodetect") != null)) {
      setAutodetect(((Boolean)raw.get("autodetect")).booleanValue());
    }
  }
  
  public Map<String, Object> toJson() {
    Map<String, Object> m = new HashMap();
    
    if (proxyType != ProxyType.UNSPECIFIED) {
      m.put("proxyType", proxyType.toString().toLowerCase());
    }
    if (ftpProxy != null) {
      m.put("ftpProxy", ftpProxy);
    }
    if (httpProxy != null) {
      m.put("httpProxy", httpProxy);
    }
    if (noProxy != null) {
      m.put("noProxy", noProxy);
    }
    if (sslProxy != null) {
      m.put("sslProxy", sslProxy);
    }
    if (socksProxy != null) {
      m.put("socksProxy", socksProxy);
    }
    if (socksUsername != null) {
      m.put("socksUsername", socksUsername);
    }
    if (socksPassword != null) {
      m.put("socksPassword", socksPassword);
    }
    if (proxyAutoconfigUrl != null) {
      m.put("proxyAutoconfigUrl", proxyAutoconfigUrl);
    }
    if (autodetect) {
      m.put("autodetect", Boolean.valueOf(true));
    }
    return m;
  }
  






  public ProxyType getProxyType()
  {
    return proxyType;
  }
  





  public Proxy setProxyType(ProxyType proxyType)
  {
    verifyProxyTypeCompatibility(proxyType);
    this.proxyType = proxyType;
    return this;
  }
  




  public boolean isAutodetect()
  {
    return autodetect;
  }
  






  public Proxy setAutodetect(boolean autodetect)
  {
    if (this.autodetect == autodetect) {
      return this;
    }
    if (autodetect) {
      verifyProxyTypeCompatibility(ProxyType.AUTODETECT);
      proxyType = ProxyType.AUTODETECT;
    } else {
      proxyType = ProxyType.UNSPECIFIED;
    }
    this.autodetect = autodetect;
    return this;
  }
  




  public String getFtpProxy()
  {
    return ftpProxy;
  }
  





  public Proxy setFtpProxy(String ftpProxy)
  {
    verifyProxyTypeCompatibility(ProxyType.MANUAL);
    proxyType = ProxyType.MANUAL;
    this.ftpProxy = ftpProxy;
    return this;
  }
  




  public String getHttpProxy()
  {
    return httpProxy;
  }
  





  public Proxy setHttpProxy(String httpProxy)
  {
    verifyProxyTypeCompatibility(ProxyType.MANUAL);
    proxyType = ProxyType.MANUAL;
    this.httpProxy = httpProxy;
    return this;
  }
  




  public String getNoProxy()
  {
    return noProxy;
  }
  





  public Proxy setNoProxy(String noProxy)
  {
    verifyProxyTypeCompatibility(ProxyType.MANUAL);
    proxyType = ProxyType.MANUAL;
    this.noProxy = noProxy;
    return this;
  }
  




  public String getSslProxy()
  {
    return sslProxy;
  }
  





  public Proxy setSslProxy(String sslProxy)
  {
    verifyProxyTypeCompatibility(ProxyType.MANUAL);
    proxyType = ProxyType.MANUAL;
    this.sslProxy = sslProxy;
    return this;
  }
  




  public String getSocksProxy()
  {
    return socksProxy;
  }
  





  public Proxy setSocksProxy(String socksProxy)
  {
    verifyProxyTypeCompatibility(ProxyType.MANUAL);
    proxyType = ProxyType.MANUAL;
    this.socksProxy = socksProxy;
    return this;
  }
  




  public String getSocksUsername()
  {
    return socksUsername;
  }
  





  public Proxy setSocksUsername(String username)
  {
    verifyProxyTypeCompatibility(ProxyType.MANUAL);
    proxyType = ProxyType.MANUAL;
    socksUsername = username;
    return this;
  }
  




  public String getSocksPassword()
  {
    return socksPassword;
  }
  





  public Proxy setSocksPassword(String password)
  {
    verifyProxyTypeCompatibility(ProxyType.MANUAL);
    proxyType = ProxyType.MANUAL;
    socksPassword = password;
    return this;
  }
  




  public String getProxyAutoconfigUrl()
  {
    return proxyAutoconfigUrl;
  }
  







  public Proxy setProxyAutoconfigUrl(String proxyAutoconfigUrl)
  {
    verifyProxyTypeCompatibility(ProxyType.PAC);
    proxyType = ProxyType.PAC;
    this.proxyAutoconfigUrl = proxyAutoconfigUrl;
    return this;
  }
  
  private void verifyProxyTypeCompatibility(ProxyType compatibleProxy) {
    if ((proxyType != ProxyType.UNSPECIFIED) && (proxyType != compatibleProxy)) {
      throw new IllegalStateException(String.format("Specified proxy type (%s) not compatible with current setting (%s)", new Object[] { compatibleProxy, proxyType }));
    }
  }
  


  public static Proxy extractFrom(Capabilities capabilities)
  {
    Object rawProxy = capabilities.getCapability("proxy");
    Proxy proxy = null;
    if (rawProxy != null) {
      if ((rawProxy instanceof Proxy)) {
        proxy = (Proxy)rawProxy;
      } else if ((rawProxy instanceof Map)) {
        proxy = new Proxy((Map)rawProxy);
      }
    }
    return proxy;
  }
  
  public String toString()
  {
    StringBuilder builder = new StringBuilder("Proxy(");
    
    switch (1.$SwitchMap$org$openqa$selenium$Proxy$ProxyType[getProxyType().ordinal()]) {
    case 1: 
    case 2: 
    case 3: 
    case 4: 
      builder.append(getProxyType().toString().toLowerCase());
      break;
    
    case 5: 
      builder.append("pac: ").append(getProxyAutoconfigUrl());
      break;
    }
    
    


    String p = getFtpProxy();
    if (p != null) {
      builder.append(", ftp=").append(p);
    }
    p = getHttpProxy();
    if (p != null) {
      builder.append(", http=").append(p);
    }
    p = getSocksProxy();
    if (p != null) {
      builder.append(", socks=").append(p);
    }
    p = getSslProxy();
    if (p != null) {
      builder.append(", ssl=").append(p);
    }
    
    builder.append(")");
    return builder.toString();
  }
  
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if ((o == null) || (getClass() != o.getClass())) {
      return false;
    }
    Proxy proxy = (Proxy)o;
    return (isAutodetect() == proxy.isAutodetect()) && 
      (getProxyType() == proxy.getProxyType()) && 
      (Objects.equals(getFtpProxy(), proxy.getFtpProxy())) && 
      (Objects.equals(getHttpProxy(), proxy.getHttpProxy())) && 
      (Objects.equals(getNoProxy(), proxy.getNoProxy())) && 
      (Objects.equals(getSslProxy(), proxy.getSslProxy())) && 
      (Objects.equals(getSocksProxy(), proxy.getSocksProxy())) && 
      (Objects.equals(getSocksUsername(), proxy.getSocksUsername())) && 
      (Objects.equals(getSocksPassword(), proxy.getSocksPassword())) && 
      (Objects.equals(getProxyAutoconfigUrl(), proxy.getProxyAutoconfigUrl()));
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] {
      getProxyType(), 
      Boolean.valueOf(isAutodetect()), 
      getFtpProxy(), 
      getHttpProxy(), 
      getNoProxy(), 
      getSslProxy(), 
      getSocksProxy(), 
      getSocksUsername(), 
      getSocksPassword(), 
      getProxyAutoconfigUrl() });
  }
}
