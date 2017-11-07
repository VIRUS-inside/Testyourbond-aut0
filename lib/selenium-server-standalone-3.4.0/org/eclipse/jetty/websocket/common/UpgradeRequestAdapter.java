package org.eclipse.jetty.websocket.common;

import java.net.HttpCookie;
import java.net.URI;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import org.eclipse.jetty.websocket.api.UpgradeRequest;
import org.eclipse.jetty.websocket.api.extensions.ExtensionConfig;
import org.eclipse.jetty.websocket.api.util.QuoteUtil;


















public class UpgradeRequestAdapter
  implements UpgradeRequest
{
  private URI requestURI;
  private List<String> subProtocols = new ArrayList(1);
  private List<ExtensionConfig> extensions = new ArrayList(1);
  private List<HttpCookie> cookies = new ArrayList(1);
  private Map<String, List<String>> headers = new TreeMap(String.CASE_INSENSITIVE_ORDER);
  private Map<String, List<String>> parameters = new HashMap(1);
  
  private Object session;
  
  private String httpVersion;
  
  private String method;
  private String host;
  private boolean secure;
  
  protected UpgradeRequestAdapter() {}
  
  public UpgradeRequestAdapter(String requestURI)
  {
    this(URI.create(requestURI));
  }
  
  public UpgradeRequestAdapter(URI requestURI)
  {
    setRequestURI(requestURI);
  }
  

  public void addExtensions(ExtensionConfig... configs)
  {
    Collections.addAll(extensions, configs);
  }
  

  public void addExtensions(String... configs)
  {
    for (String config : configs)
    {
      extensions.add(ExtensionConfig.parse(config));
    }
  }
  

  public void clearHeaders()
  {
    headers.clear();
  }
  

  public List<HttpCookie> getCookies()
  {
    return cookies;
  }
  

  public List<ExtensionConfig> getExtensions()
  {
    return extensions;
  }
  

  public String getHeader(String name)
  {
    List<String> values = (List)headers.get(name);
    
    if (values == null)
    {
      return null;
    }
    int size = values.size();
    
    if (size <= 0)
    {
      return null;
    }
    
    if (size == 1)
    {
      return (String)values.get(0);
    }
    
    boolean needsDelim = false;
    StringBuilder ret = new StringBuilder();
    for (String value : values)
    {
      if (needsDelim)
      {
        ret.append(", ");
      }
      QuoteUtil.quoteIfNeeded(ret, value, "\"'\\\n\r\t\f\b%+ ;=");
      needsDelim = true;
    }
    return ret.toString();
  }
  

  public int getHeaderInt(String name)
  {
    List<String> values = (List)headers.get(name);
    
    if (values == null)
    {
      return -1;
    }
    int size = values.size();
    
    if (size <= 0)
    {
      return -1;
    }
    
    if (size == 1)
    {
      return Integer.parseInt((String)values.get(0));
    }
    throw new NumberFormatException("Cannot convert multi-value header into int");
  }
  

  public Map<String, List<String>> getHeaders()
  {
    return headers;
  }
  

  public List<String> getHeaders(String name)
  {
    return (List)headers.get(name);
  }
  

  public String getHost()
  {
    return host;
  }
  

  public String getHttpVersion()
  {
    return httpVersion;
  }
  

  public String getMethod()
  {
    return method;
  }
  

  public String getOrigin()
  {
    return getHeader("Origin");
  }
  






  public Map<String, List<String>> getParameterMap()
  {
    return Collections.unmodifiableMap(parameters);
  }
  

  public String getProtocolVersion()
  {
    String version = getHeader("Sec-WebSocket-Version");
    if (version == null)
    {
      return "13";
    }
    return version;
  }
  

  public String getQueryString()
  {
    return requestURI.getQuery();
  }
  

  public URI getRequestURI()
  {
    return requestURI;
  }
  








  public Object getSession()
  {
    return session;
  }
  

  public List<String> getSubProtocols()
  {
    return subProtocols;
  }
  









  public Principal getUserPrincipal()
  {
    return null;
  }
  

  public boolean hasSubProtocol(String test)
  {
    for (String protocol : subProtocols)
    {
      if (protocol.equalsIgnoreCase(test))
      {
        return true;
      }
    }
    return false;
  }
  

  public boolean isOrigin(String test)
  {
    return test.equalsIgnoreCase(getOrigin());
  }
  

  public boolean isSecure()
  {
    return secure;
  }
  

  public void setCookies(List<HttpCookie> cookies)
  {
    this.cookies.clear();
    if ((cookies != null) && (!cookies.isEmpty()))
    {
      this.cookies.addAll(cookies);
    }
  }
  

  public void setExtensions(List<ExtensionConfig> configs)
  {
    extensions.clear();
    if (configs != null)
    {
      extensions.addAll(configs);
    }
  }
  

  public void setHeader(String name, List<String> values)
  {
    headers.put(name, values);
  }
  

  public void setHeader(String name, String value)
  {
    List<String> values = new ArrayList();
    values.add(value);
    setHeader(name, values);
  }
  

  public void setHeaders(Map<String, List<String>> headers)
  {
    clearHeaders();
    
    for (Map.Entry<String, List<String>> entry : headers.entrySet())
    {
      String name = (String)entry.getKey();
      List<String> values = (List)entry.getValue();
      setHeader(name, values);
    }
  }
  

  public void setHttpVersion(String httpVersion)
  {
    this.httpVersion = httpVersion;
  }
  

  public void setMethod(String method)
  {
    this.method = method;
  }
  
  protected void setParameterMap(Map<String, List<String>> parameters)
  {
    this.parameters.clear();
    this.parameters.putAll(parameters);
  }
  

  public void setRequestURI(URI uri)
  {
    requestURI = uri;
    String scheme = uri.getScheme();
    if ("ws".equalsIgnoreCase(scheme))
    {
      secure = false;
    }
    else if ("wss".equalsIgnoreCase(scheme))
    {
      secure = true;
    }
    else
    {
      throw new IllegalArgumentException("URI scheme must be 'ws' or 'wss'");
    }
    host = requestURI.getHost();
    parameters.clear();
  }
  

  public void setSession(Object session)
  {
    this.session = session;
  }
  

  public void setSubProtocols(List<String> subProtocols)
  {
    this.subProtocols.clear();
    if (subProtocols != null)
    {
      this.subProtocols.addAll(subProtocols);
    }
  }
  







  public void setSubProtocols(String... protocols)
  {
    subProtocols.clear();
    Collections.addAll(subProtocols, protocols);
  }
}
