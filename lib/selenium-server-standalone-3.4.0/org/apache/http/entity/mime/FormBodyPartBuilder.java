package org.apache.http.entity.mime;

import java.util.List;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.AbstractContentBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

































public class FormBodyPartBuilder
{
  private String name;
  private ContentBody body;
  private final Header header;
  
  public static FormBodyPartBuilder create(String name, ContentBody body)
  {
    return new FormBodyPartBuilder(name, body);
  }
  
  public static FormBodyPartBuilder create() {
    return new FormBodyPartBuilder();
  }
  
  FormBodyPartBuilder(String name, ContentBody body) {
    this();
    this.name = name;
    this.body = body;
  }
  
  FormBodyPartBuilder() {
    header = new Header();
  }
  
  public FormBodyPartBuilder setName(String name) {
    this.name = name;
    return this;
  }
  
  public FormBodyPartBuilder setBody(ContentBody body) {
    this.body = body;
    return this;
  }
  
  public FormBodyPartBuilder addField(String name, String value) {
    Args.notNull(name, "Field name");
    header.addField(new MinimalField(name, value));
    return this;
  }
  
  public FormBodyPartBuilder setField(String name, String value) {
    Args.notNull(name, "Field name");
    header.setField(new MinimalField(name, value));
    return this;
  }
  
  public FormBodyPartBuilder removeFields(String name) {
    Args.notNull(name, "Field name");
    header.removeFields(name);
    return this;
  }
  
  public FormBodyPart build() {
    Asserts.notBlank(name, "Name");
    Asserts.notNull(body, "Content body");
    Header headerCopy = new Header();
    List<MinimalField> fields = header.getFields();
    for (MinimalField field : fields) {
      headerCopy.addField(field);
    }
    if (headerCopy.getField("Content-Disposition") == null) {
      StringBuilder buffer = new StringBuilder();
      buffer.append("form-data; name=\"");
      buffer.append(name);
      buffer.append("\"");
      if (body.getFilename() != null) {
        buffer.append("; filename=\"");
        buffer.append(body.getFilename());
        buffer.append("\"");
      }
      headerCopy.addField(new MinimalField("Content-Disposition", buffer.toString()));
    }
    if (headerCopy.getField("Content-Type") == null) { ContentType contentType;
      ContentType contentType;
      if ((body instanceof AbstractContentBody)) {
        contentType = ((AbstractContentBody)body).getContentType();
      } else {
        contentType = null;
      }
      if (contentType != null) {
        headerCopy.addField(new MinimalField("Content-Type", contentType.toString()));
      } else {
        StringBuilder buffer = new StringBuilder();
        buffer.append(body.getMimeType());
        if (body.getCharset() != null) {
          buffer.append("; charset=");
          buffer.append(body.getCharset());
        }
        headerCopy.addField(new MinimalField("Content-Type", buffer.toString()));
      }
    }
    if (headerCopy.getField("Content-Transfer-Encoding") == null)
    {
      headerCopy.addField(new MinimalField("Content-Transfer-Encoding", body.getTransferEncoding()));
    }
    return new FormBodyPart(name, body, headerCopy);
  }
}
