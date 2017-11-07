package org.apache.http.entity.mime;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.ContentTooLongException;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHeader;

































class MultipartFormEntity
  implements HttpEntity
{
  private final AbstractMultipartForm multipart;
  private final Header contentType;
  private final long contentLength;
  
  MultipartFormEntity(AbstractMultipartForm multipart, ContentType contentType, long contentLength)
  {
    this.multipart = multipart;
    this.contentType = new BasicHeader("Content-Type", contentType.toString());
    this.contentLength = contentLength;
  }
  
  AbstractMultipartForm getMultipart() {
    return multipart;
  }
  
  public boolean isRepeatable()
  {
    return contentLength != -1L;
  }
  
  public boolean isChunked()
  {
    return !isRepeatable();
  }
  
  public boolean isStreaming()
  {
    return !isRepeatable();
  }
  
  public long getContentLength()
  {
    return contentLength;
  }
  
  public Header getContentType()
  {
    return contentType;
  }
  
  public Header getContentEncoding()
  {
    return null;
  }
  

  public void consumeContent() {}
  
  public InputStream getContent()
    throws IOException
  {
    if (contentLength < 0L)
      throw new ContentTooLongException("Content length is unknown");
    if (contentLength > 25600L) {
      throw new ContentTooLongException("Content length is too long: " + contentLength);
    }
    ByteArrayOutputStream outstream = new ByteArrayOutputStream();
    writeTo(outstream);
    outstream.flush();
    return new ByteArrayInputStream(outstream.toByteArray());
  }
  
  public void writeTo(OutputStream outstream) throws IOException
  {
    multipart.writeTo(outstream);
  }
}
