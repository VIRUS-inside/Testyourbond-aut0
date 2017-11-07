package org.apache.http.entity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.util.Args;









































public class HttpEntityWrapper
  implements HttpEntity
{
  protected HttpEntity wrappedEntity;
  
  public HttpEntityWrapper(HttpEntity wrappedEntity)
  {
    this.wrappedEntity = ((HttpEntity)Args.notNull(wrappedEntity, "Wrapped entity"));
  }
  
  public boolean isRepeatable()
  {
    return wrappedEntity.isRepeatable();
  }
  
  public boolean isChunked()
  {
    return wrappedEntity.isChunked();
  }
  
  public long getContentLength()
  {
    return wrappedEntity.getContentLength();
  }
  
  public Header getContentType()
  {
    return wrappedEntity.getContentType();
  }
  
  public Header getContentEncoding()
  {
    return wrappedEntity.getContentEncoding();
  }
  
  public InputStream getContent()
    throws IOException
  {
    return wrappedEntity.getContent();
  }
  
  public void writeTo(OutputStream outstream)
    throws IOException
  {
    wrappedEntity.writeTo(outstream);
  }
  
  public boolean isStreaming()
  {
    return wrappedEntity.isStreaming();
  }
  



  @Deprecated
  public void consumeContent()
    throws IOException
  {
    wrappedEntity.consumeContent();
  }
}
