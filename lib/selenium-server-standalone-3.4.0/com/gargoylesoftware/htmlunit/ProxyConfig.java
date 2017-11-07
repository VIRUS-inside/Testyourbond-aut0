package com.gargoylesoftware.htmlunit;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




















public class ProxyConfig
  implements Serializable
{
  private String proxyHost_;
  private int proxyPort_;
  private boolean isSocksProxy_;
  private final Map<String, Pattern> proxyBypassHosts_ = new HashMap();
  
  private String proxyAutoConfigUrl_;
  
  private String proxyAutoConfigContent_;
  
  public ProxyConfig()
  {
    this(null, 0);
  }
  




  public ProxyConfig(String proxyHost, int proxyPort)
  {
    this(proxyHost, proxyPort, false);
  }
  





  public ProxyConfig(String proxyHost, int proxyPort, boolean isSocks)
  {
    proxyHost_ = proxyHost;
    proxyPort_ = proxyPort;
    isSocksProxy_ = isSocks;
  }
  



  public String getProxyHost()
  {
    return proxyHost_;
  }
  



  public void setProxyHost(String proxyHost)
  {
    proxyHost_ = proxyHost;
  }
  



  public int getProxyPort()
  {
    return proxyPort_;
  }
  



  public void setProxyPort(int proxyPort)
  {
    proxyPort_ = proxyPort;
  }
  



  public boolean isSocksProxy()
  {
    return isSocksProxy_;
  }
  



  public void setSocksProxy(boolean isSocksProxy)
  {
    isSocksProxy_ = isSocksProxy;
  }
  





  public void addHostsToProxyBypass(String pattern)
  {
    proxyBypassHosts_.put(pattern, Pattern.compile(pattern));
  }
  




  public void removeHostsFromProxyBypass(String pattern)
  {
    proxyBypassHosts_.remove(pattern);
  }
  






  protected boolean shouldBypassProxy(String hostname)
  {
    boolean bypass = false;
    for (Pattern p : proxyBypassHosts_.values()) {
      if (p.matcher(hostname).find()) {
        bypass = true;
        break;
      }
    }
    return bypass;
  }
  



  public String getProxyAutoConfigUrl()
  {
    return proxyAutoConfigUrl_;
  }
  



  public void setProxyAutoConfigUrl(String proxyAutoConfigUrl)
  {
    proxyAutoConfigUrl_ = proxyAutoConfigUrl;
    setProxyAutoConfigContent(null);
  }
  



  protected String getProxyAutoConfigContent()
  {
    return proxyAutoConfigContent_;
  }
  



  protected void setProxyAutoConfigContent(String proxyAutoConfigContent)
  {
    proxyAutoConfigContent_ = proxyAutoConfigContent;
  }
}
