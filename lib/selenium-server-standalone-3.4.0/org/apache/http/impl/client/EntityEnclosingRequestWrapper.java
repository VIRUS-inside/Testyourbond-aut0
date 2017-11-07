package org.apache.http.impl.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.ProtocolException;
import org.apache.http.entity.HttpEntityWrapper;









































@Deprecated
public class EntityEnclosingRequestWrapper
  extends RequestWrapper
  implements HttpEntityEnclosingRequest
{
  private HttpEntity entity;
  private boolean consumed;
  
  public EntityEnclosingRequestWrapper(HttpEntityEnclosingRequest request)
    throws ProtocolException
  {
    super(request);
    setEntity(request.getEntity());
  }
  
  public HttpEntity getEntity()
  {
    return entity;
  }
  
  public void setEntity(HttpEntity entity)
  {
    this.entity = (entity != null ? new EntityWrapper(entity) : null);
    consumed = false;
  }
  
  public boolean expectContinue()
  {
    Header expect = getFirstHeader("Expect");
    return (expect != null) && ("100-continue".equalsIgnoreCase(expect.getValue()));
  }
  
  public boolean isRepeatable()
  {
    return (entity == null) || (entity.isRepeatable()) || (!consumed);
  }
  
  class EntityWrapper extends HttpEntityWrapper
  {
    EntityWrapper(HttpEntity entity) {
      super();
    }
    
    public void consumeContent() throws IOException
    {
      consumed = true;
      super.consumeContent();
    }
    
    public InputStream getContent() throws IOException
    {
      consumed = true;
      return super.getContent();
    }
    
    public void writeTo(OutputStream outstream) throws IOException
    {
      consumed = true;
      super.writeTo(outstream);
    }
  }
}
