package org.eclipse.jetty.websocket.api;

import java.net.HttpCookie;
import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import org.eclipse.jetty.websocket.api.extensions.ExtensionConfig;

public abstract interface UpgradeRequest
{
  public abstract void addExtensions(ExtensionConfig... paramVarArgs);
  
  public abstract void addExtensions(String... paramVarArgs);
  
  @Deprecated
  public abstract void clearHeaders();
  
  public abstract List<HttpCookie> getCookies();
  
  public abstract List<ExtensionConfig> getExtensions();
  
  public abstract String getHeader(String paramString);
  
  public abstract int getHeaderInt(String paramString);
  
  public abstract Map<String, List<String>> getHeaders();
  
  public abstract List<String> getHeaders(String paramString);
  
  public abstract String getHost();
  
  public abstract String getHttpVersion();
  
  public abstract String getMethod();
  
  public abstract String getOrigin();
  
  public abstract Map<String, List<String>> getParameterMap();
  
  public abstract String getProtocolVersion();
  
  public abstract String getQueryString();
  
  public abstract URI getRequestURI();
  
  public abstract Object getSession();
  
  public abstract List<String> getSubProtocols();
  
  public abstract Principal getUserPrincipal();
  
  public abstract boolean hasSubProtocol(String paramString);
  
  public abstract boolean isOrigin(String paramString);
  
  public abstract boolean isSecure();
  
  public abstract void setCookies(List<HttpCookie> paramList);
  
  public abstract void setExtensions(List<ExtensionConfig> paramList);
  
  public abstract void setHeader(String paramString, List<String> paramList);
  
  public abstract void setHeader(String paramString1, String paramString2);
  
  public abstract void setHeaders(Map<String, List<String>> paramMap);
  
  public abstract void setHttpVersion(String paramString);
  
  public abstract void setMethod(String paramString);
  
  public abstract void setRequestURI(URI paramURI);
  
  public abstract void setSession(Object paramObject);
  
  public abstract void setSubProtocols(List<String> paramList);
  
  public abstract void setSubProtocols(String... paramVarArgs);
}
