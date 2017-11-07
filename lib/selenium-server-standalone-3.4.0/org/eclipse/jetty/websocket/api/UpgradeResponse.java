package org.eclipse.jetty.websocket.api;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.eclipse.jetty.websocket.api.extensions.ExtensionConfig;

public abstract interface UpgradeResponse
{
  public abstract void addHeader(String paramString1, String paramString2);
  
  public abstract String getAcceptedSubProtocol();
  
  public abstract List<ExtensionConfig> getExtensions();
  
  public abstract String getHeader(String paramString);
  
  public abstract Set<String> getHeaderNames();
  
  public abstract Map<String, List<String>> getHeaders();
  
  public abstract List<String> getHeaders(String paramString);
  
  public abstract int getStatusCode();
  
  public abstract String getStatusReason();
  
  public abstract boolean isSuccess();
  
  public abstract void sendForbidden(String paramString)
    throws IOException;
  
  public abstract void setAcceptedSubProtocol(String paramString);
  
  public abstract void setExtensions(List<ExtensionConfig> paramList);
  
  public abstract void setHeader(String paramString1, String paramString2);
  
  public abstract void setStatusCode(int paramInt);
  
  public abstract void setStatusReason(String paramString);
  
  public abstract void setSuccess(boolean paramBoolean);
}
