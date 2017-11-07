package org.apache.http.entity.mime;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;
import org.apache.http.entity.mime.content.ContentBody;


































class HttpBrowserCompatibleMultipart
  extends AbstractMultipartForm
{
  private final List<FormBodyPart> parts;
  
  public HttpBrowserCompatibleMultipart(Charset charset, String boundary, List<FormBodyPart> parts)
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
    MinimalField cd = header.getField("Content-Disposition");
    writeField(cd, charset, out);
    String filename = part.getBody().getFilename();
    if (filename != null) {
      MinimalField ct = header.getField("Content-Type");
      writeField(ct, charset, out);
    }
  }
}
