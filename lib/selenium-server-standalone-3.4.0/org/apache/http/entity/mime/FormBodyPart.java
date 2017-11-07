package org.apache.http.entity.mime;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.AbstractContentBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.util.Args;



































public class FormBodyPart
{
  private final String name;
  private final Header header;
  private final ContentBody body;
  
  FormBodyPart(String name, ContentBody body, Header header)
  {
    Args.notNull(name, "Name");
    Args.notNull(body, "Body");
    this.name = name;
    this.body = body;
    this.header = (header != null ? header : new Header());
  }
  



  @Deprecated
  public FormBodyPart(String name, ContentBody body)
  {
    Args.notNull(name, "Name");
    Args.notNull(body, "Body");
    this.name = name;
    this.body = body;
    header = new Header();
    
    generateContentDisp(body);
    generateContentType(body);
    generateTransferEncoding(body);
  }
  
  public String getName() {
    return name;
  }
  
  public ContentBody getBody() {
    return body;
  }
  
  public Header getHeader() {
    return header;
  }
  
  public void addField(String name, String value) {
    Args.notNull(name, "Field name");
    header.addField(new MinimalField(name, value));
  }
  


  @Deprecated
  protected void generateContentDisp(ContentBody body)
  {
    StringBuilder buffer = new StringBuilder();
    buffer.append("form-data; name=\"");
    buffer.append(getName());
    buffer.append("\"");
    if (body.getFilename() != null) {
      buffer.append("; filename=\"");
      buffer.append(body.getFilename());
      buffer.append("\"");
    }
    addField("Content-Disposition", buffer.toString());
  }
  

  @Deprecated
  protected void generateContentType(ContentBody body)
  {
    ContentType contentType;
    ContentType contentType;
    if ((body instanceof AbstractContentBody)) {
      contentType = ((AbstractContentBody)body).getContentType();
    } else {
      contentType = null;
    }
    if (contentType != null) {
      addField("Content-Type", contentType.toString());
    } else {
      StringBuilder buffer = new StringBuilder();
      buffer.append(body.getMimeType());
      if (body.getCharset() != null) {
        buffer.append("; charset=");
        buffer.append(body.getCharset());
      }
      addField("Content-Type", buffer.toString());
    }
  }
  


  @Deprecated
  protected void generateTransferEncoding(ContentBody body)
  {
    addField("Content-Transfer-Encoding", body.getTransferEncoding());
  }
}
