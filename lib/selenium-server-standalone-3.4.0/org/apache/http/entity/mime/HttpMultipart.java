package org.apache.http.entity.mime;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.entity.mime.content.ContentBody;















































@Deprecated
public class HttpMultipart
  extends AbstractMultipartForm
{
  private final HttpMultipartMode mode;
  private final List<FormBodyPart> parts;
  private final String subType;
  
  public HttpMultipart(String subType, Charset charset, String boundary, HttpMultipartMode mode)
  {
    super(charset, boundary);
    this.subType = subType;
    this.mode = mode;
    parts = new ArrayList();
  }
  









  public HttpMultipart(String subType, Charset charset, String boundary)
  {
    this(subType, charset, boundary, HttpMultipartMode.STRICT);
  }
  
  public HttpMultipart(String subType, String boundary) {
    this(subType, null, boundary);
  }
  
  public HttpMultipartMode getMode() {
    return mode;
  }
  
  protected void formatMultipartHeader(FormBodyPart part, OutputStream out)
    throws IOException
  {
    Header header = part.getHeader();
    switch (1.$SwitchMap$org$apache$http$entity$mime$HttpMultipartMode[mode.ordinal()])
    {

    case 1: 
      MinimalField cd = header.getField("Content-Disposition");
      writeField(cd, charset, out);
      String filename = part.getBody().getFilename();
      if (filename != null) {
        MinimalField ct = header.getField("Content-Type");
        writeField(ct, charset, out); }
      break;
    
    default: 
      for (MinimalField field : header) {
        writeField(field, out);
      }
    }
    
  }
  
  public List<FormBodyPart> getBodyParts() {
    return parts;
  }
  
  public void addBodyPart(FormBodyPart part) {
    if (part == null) {
      return;
    }
    parts.add(part);
  }
  
  public String getSubType() {
    return subType;
  }
  
  public Charset getCharset() {
    return charset;
  }
  
  public String getBoundary() {
    return boundary;
  }
}
