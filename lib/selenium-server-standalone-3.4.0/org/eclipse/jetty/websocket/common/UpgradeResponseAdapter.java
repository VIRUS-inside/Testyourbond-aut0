package org.eclipse.jetty.websocket.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.eclipse.jetty.websocket.api.UpgradeResponse;
import org.eclipse.jetty.websocket.api.extensions.ExtensionConfig;
import org.eclipse.jetty.websocket.api.util.QuoteUtil;





















public class UpgradeResponseAdapter
  implements UpgradeResponse
{
  public static final String SEC_WEBSOCKET_PROTOCOL = "Sec-WebSocket-Protocol";
  private int statusCode;
  private String statusReason;
  private Map<String, List<String>> headers = new TreeMap(String.CASE_INSENSITIVE_ORDER);
  private List<ExtensionConfig> extensions = new ArrayList();
  private boolean success = false;
  
  public UpgradeResponseAdapter() {}
  
  public void addHeader(String name, String value) {
    String key = name;
    List<String> values = (List)headers.get(key);
    if (values == null)
    {
      values = new ArrayList();
    }
    values.add(value);
    headers.put(key, values);
  }
  






  public String getAcceptedSubProtocol()
  {
    return getHeader("Sec-WebSocket-Protocol");
  }
  






  public List<ExtensionConfig> getExtensions()
  {
    return extensions;
  }
  

  public String getHeader(String name)
  {
    List<String> values = getHeaders(name);
    
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
  

  public Set<String> getHeaderNames()
  {
    return headers.keySet();
  }
  

  public Map<String, List<String>> getHeaders()
  {
    return headers;
  }
  

  public List<String> getHeaders(String name)
  {
    return (List)headers.get(name);
  }
  

  public int getStatusCode()
  {
    return statusCode;
  }
  

  public String getStatusReason()
  {
    return statusReason;
  }
  

  public boolean isSuccess()
  {
    return success;
  }
  













  public void sendForbidden(String message)
    throws IOException
  {
    throw new UnsupportedOperationException("Not supported");
  }
  







  public void setAcceptedSubProtocol(String protocol)
  {
    setHeader("Sec-WebSocket-Protocol", protocol);
  }
  














  public void setExtensions(List<ExtensionConfig> extensions)
  {
    this.extensions.clear();
    if (extensions != null)
    {
      this.extensions.addAll(extensions);
    }
  }
  

  public void setHeader(String name, String value)
  {
    List<String> values = new ArrayList();
    values.add(value);
    headers.put(name, values);
  }
  

  public void setStatusCode(int statusCode)
  {
    this.statusCode = statusCode;
  }
  

  public void setStatusReason(String statusReason)
  {
    this.statusReason = statusReason;
  }
  

  public void setSuccess(boolean success)
  {
    this.success = success;
  }
}
