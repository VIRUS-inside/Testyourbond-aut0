package org.openqa.selenium.remote.server.xdrpc;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;






























public class CrossDomainRpc
{
  private final String method;
  private final String path;
  private final String data;
  
  public CrossDomainRpc(String method, String path, String data)
  {
    this.method = ((String)Preconditions.checkNotNull(method));
    this.path = ((String)Preconditions.checkNotNull(path));
    this.data = ((String)Preconditions.checkNotNull(data));
  }
  
  public String getMethod() {
    return method;
  }
  
  public String getPath() {
    return path;
  }
  
  public String getData() {
    return data;
  }
  
  public byte[] getContent() {
    return data.getBytes(Charsets.UTF_8);
  }
}
