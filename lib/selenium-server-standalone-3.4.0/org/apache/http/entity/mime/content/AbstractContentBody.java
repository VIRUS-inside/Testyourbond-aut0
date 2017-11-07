package org.apache.http.entity.mime.content;

import java.nio.charset.Charset;
import org.apache.http.entity.ContentType;
import org.apache.http.util.Args;



































public abstract class AbstractContentBody
  implements ContentBody
{
  private final ContentType contentType;
  
  public AbstractContentBody(ContentType contentType)
  {
    Args.notNull(contentType, "Content type");
    this.contentType = contentType;
  }
  


  @Deprecated
  public AbstractContentBody(String mimeType)
  {
    this(ContentType.parse(mimeType));
  }
  


  public ContentType getContentType()
  {
    return contentType;
  }
  
  public String getMimeType()
  {
    return contentType.getMimeType();
  }
  
  public String getMediaType()
  {
    String mimeType = contentType.getMimeType();
    int i = mimeType.indexOf('/');
    if (i != -1) {
      return mimeType.substring(0, i);
    }
    return mimeType;
  }
  

  public String getSubType()
  {
    String mimeType = contentType.getMimeType();
    int i = mimeType.indexOf('/');
    if (i != -1) {
      return mimeType.substring(i + 1);
    }
    return null;
  }
  

  public String getCharset()
  {
    Charset charset = contentType.getCharset();
    return charset != null ? charset.name() : null;
  }
}
