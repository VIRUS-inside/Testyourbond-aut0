package org.apache.http.client.methods;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.client.utils.CloneUtils;




































public abstract class HttpEntityEnclosingRequestBase
  extends HttpRequestBase
  implements HttpEntityEnclosingRequest
{
  private HttpEntity entity;
  
  public HttpEntityEnclosingRequestBase() {}
  
  public HttpEntity getEntity()
  {
    return entity;
  }
  
  public void setEntity(HttpEntity entity)
  {
    this.entity = entity;
  }
  
  public boolean expectContinue()
  {
    Header expect = getFirstHeader("Expect");
    return (expect != null) && ("100-continue".equalsIgnoreCase(expect.getValue()));
  }
  
  public Object clone() throws CloneNotSupportedException
  {
    HttpEntityEnclosingRequestBase clone = (HttpEntityEnclosingRequestBase)super.clone();
    
    if (entity != null) {
      entity = ((HttpEntity)CloneUtils.cloneObject(entity));
    }
    return clone;
  }
}
