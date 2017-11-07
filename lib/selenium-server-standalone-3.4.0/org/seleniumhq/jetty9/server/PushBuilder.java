package org.seleniumhq.jetty9.server;

import java.util.Set;

public abstract interface PushBuilder
{
  public abstract PushBuilder method(String paramString);
  
  public abstract PushBuilder queryString(String paramString);
  
  public abstract PushBuilder sessionId(String paramString);
  
  public abstract PushBuilder conditional(boolean paramBoolean);
  
  public abstract PushBuilder setHeader(String paramString1, String paramString2);
  
  public abstract PushBuilder addHeader(String paramString1, String paramString2);
  
  public abstract PushBuilder removeHeader(String paramString);
  
  public abstract PushBuilder path(String paramString);
  
  public abstract PushBuilder etag(String paramString);
  
  public abstract PushBuilder lastModified(String paramString);
  
  public abstract void push();
  
  public abstract String getMethod();
  
  public abstract String getQueryString();
  
  public abstract String getSessionId();
  
  public abstract boolean isConditional();
  
  public abstract Set<String> getHeaderNames();
  
  public abstract String getHeader(String paramString);
  
  public abstract String getPath();
  
  public abstract String getEtag();
  
  public abstract String getLastModified();
}
