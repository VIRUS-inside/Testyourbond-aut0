package org.apache.http.entity.mime;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Random;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.content.ContentBody;






































@Deprecated
public class MultipartEntity
  implements HttpEntity
{
  private static final char[] MULTIPART_CHARS = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
  



  private final MultipartEntityBuilder builder;
  



  private volatile MultipartFormEntity entity;
  




  public MultipartEntity(HttpMultipartMode mode, String boundary, Charset charset)
  {
    builder = new MultipartEntityBuilder().setMode(mode).setCharset(charset != null ? charset : MIME.DEFAULT_CHARSET).setBoundary(boundary);
    


    entity = null;
  }
  




  public MultipartEntity(HttpMultipartMode mode)
  {
    this(mode, null, null);
  }
  


  public MultipartEntity()
  {
    this(HttpMultipartMode.STRICT, null, null);
  }
  

  protected String generateContentType(String boundary, Charset charset)
  {
    StringBuilder buffer = new StringBuilder();
    buffer.append("multipart/form-data; boundary=");
    buffer.append(boundary);
    if (charset != null) {
      buffer.append("; charset=");
      buffer.append(charset.name());
    }
    return buffer.toString();
  }
  
  protected String generateBoundary() {
    StringBuilder buffer = new StringBuilder();
    Random rand = new Random();
    int count = rand.nextInt(11) + 30;
    for (int i = 0; i < count; i++) {
      buffer.append(MULTIPART_CHARS[rand.nextInt(MULTIPART_CHARS.length)]);
    }
    return buffer.toString();
  }
  
  private MultipartFormEntity getEntity() {
    if (entity == null) {
      entity = builder.buildEntity();
    }
    return entity;
  }
  
  public void addPart(FormBodyPart bodyPart) {
    builder.addPart(bodyPart);
    entity = null;
  }
  
  public void addPart(String name, ContentBody contentBody) {
    addPart(new FormBodyPart(name, contentBody));
  }
  
  public boolean isRepeatable()
  {
    return getEntity().isRepeatable();
  }
  
  public boolean isChunked()
  {
    return getEntity().isChunked();
  }
  
  public boolean isStreaming()
  {
    return getEntity().isStreaming();
  }
  
  public long getContentLength()
  {
    return getEntity().getContentLength();
  }
  
  public Header getContentType()
  {
    return getEntity().getContentType();
  }
  
  public Header getContentEncoding()
  {
    return getEntity().getContentEncoding();
  }
  
  public void consumeContent()
    throws IOException, UnsupportedOperationException
  {
    if (isStreaming()) {
      throw new UnsupportedOperationException("Streaming entity does not implement #consumeContent()");
    }
  }
  
  public InputStream getContent()
    throws IOException, UnsupportedOperationException
  {
    throw new UnsupportedOperationException("Multipart form entity does not implement #getContent()");
  }
  
  public void writeTo(OutputStream outstream)
    throws IOException
  {
    getEntity().writeTo(outstream);
  }
}
