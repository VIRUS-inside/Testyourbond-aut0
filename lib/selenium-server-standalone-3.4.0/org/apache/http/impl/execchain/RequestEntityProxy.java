package org.apache.http.impl.execchain;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;





























class RequestEntityProxy
  implements HttpEntity
{
  private final HttpEntity original;
  
  static void enhance(HttpEntityEnclosingRequest request)
  {
    HttpEntity entity = request.getEntity();
    if ((entity != null) && (!entity.isRepeatable()) && (!isEnhanced(entity))) {
      request.setEntity(new RequestEntityProxy(entity));
    }
  }
  
  static boolean isEnhanced(HttpEntity entity) {
    return entity instanceof RequestEntityProxy;
  }
  
  static boolean isRepeatable(HttpRequest request) {
    if ((request instanceof HttpEntityEnclosingRequest)) {
      HttpEntity entity = ((HttpEntityEnclosingRequest)request).getEntity();
      if (entity != null) {
        if (isEnhanced(entity)) {
          RequestEntityProxy proxy = (RequestEntityProxy)entity;
          if (!proxy.isConsumed()) {
            return true;
          }
        }
        return entity.isRepeatable();
      }
    }
    return true;
  }
  

  private boolean consumed = false;
  
  RequestEntityProxy(HttpEntity original)
  {
    this.original = original;
  }
  
  public HttpEntity getOriginal() {
    return original;
  }
  
  public boolean isConsumed() {
    return consumed;
  }
  
  public boolean isRepeatable()
  {
    return original.isRepeatable();
  }
  
  public boolean isChunked()
  {
    return original.isChunked();
  }
  
  public long getContentLength()
  {
    return original.getContentLength();
  }
  
  public Header getContentType()
  {
    return original.getContentType();
  }
  
  public Header getContentEncoding()
  {
    return original.getContentEncoding();
  }
  
  public InputStream getContent() throws IOException, IllegalStateException
  {
    return original.getContent();
  }
  
  public void writeTo(OutputStream outstream) throws IOException
  {
    consumed = true;
    original.writeTo(outstream);
  }
  
  public boolean isStreaming()
  {
    return original.isStreaming();
  }
  
  @Deprecated
  public void consumeContent() throws IOException
  {
    consumed = true;
    original.consumeContent();
  }
  
  public String toString()
  {
    StringBuilder sb = new StringBuilder("RequestEntityProxy{");
    sb.append(original);
    sb.append('}');
    return sb.toString();
  }
}
