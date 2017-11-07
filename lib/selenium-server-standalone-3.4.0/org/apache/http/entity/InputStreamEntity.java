package org.apache.http.entity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.util.Args;









































public class InputStreamEntity
  extends AbstractHttpEntity
{
  private final InputStream content;
  private final long length;
  
  public InputStreamEntity(InputStream instream)
  {
    this(instream, -1L);
  }
  






  public InputStreamEntity(InputStream instream, long length)
  {
    this(instream, length, null);
  }
  








  public InputStreamEntity(InputStream instream, ContentType contentType)
  {
    this(instream, -1L, contentType);
  }
  







  public InputStreamEntity(InputStream instream, long length, ContentType contentType)
  {
    content = ((InputStream)Args.notNull(instream, "Source input stream"));
    this.length = length;
    if (contentType != null) {
      setContentType(contentType.toString());
    }
  }
  
  public boolean isRepeatable()
  {
    return false;
  }
  



  public long getContentLength()
  {
    return length;
  }
  
  public InputStream getContent() throws IOException
  {
    return content;
  }
  






  public void writeTo(OutputStream outstream)
    throws IOException
  {
    Args.notNull(outstream, "Output stream");
    InputStream instream = content;
    try {
      byte[] buffer = new byte['က'];
      
      if (length < 0L) {
        int l;
        while ((l = instream.read(buffer)) != -1) {
          outstream.write(buffer, 0, l);
        }
      }
      
      long remaining = length;
      while (remaining > 0L) {
        int l = instream.read(buffer, 0, (int)Math.min(4096L, remaining));
        if (l == -1) {
          break;
        }
        outstream.write(buffer, 0, l);
        remaining -= l;
      }
    }
    finally {
      instream.close();
    }
  }
  
  public boolean isStreaming()
  {
    return true;
  }
}
