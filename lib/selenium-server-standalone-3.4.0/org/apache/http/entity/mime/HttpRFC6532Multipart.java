package org.apache.http.entity.mime;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;




































class HttpRFC6532Multipart
  extends AbstractMultipartForm
{
  private final List<FormBodyPart> parts;
  
  public HttpRFC6532Multipart(Charset charset, String boundary, List<FormBodyPart> parts)
  {
    super(charset, boundary);
    this.parts = parts;
  }
  
  public List<FormBodyPart> getBodyParts()
  {
    return parts;
  }
  



  protected void formatMultipartHeader(FormBodyPart part, OutputStream out)
    throws IOException
  {
    Header header = part.getHeader();
    for (MinimalField field : header) {
      writeField(field, MIME.UTF8_CHARSET, out);
    }
  }
}
