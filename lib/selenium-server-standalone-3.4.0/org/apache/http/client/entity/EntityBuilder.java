package org.apache.http.client.entity;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.SerializableEntity;
import org.apache.http.entity.StringEntity;
















































public class EntityBuilder
{
  private String text;
  private byte[] binary;
  private InputStream stream;
  private List<NameValuePair> parameters;
  private Serializable serializable;
  private File file;
  private ContentType contentType;
  private String contentEncoding;
  private boolean chunked;
  private boolean gzipCompress;
  
  EntityBuilder() {}
  
  public static EntityBuilder create()
  {
    return new EntityBuilder();
  }
  
  private void clearContent() {
    text = null;
    binary = null;
    stream = null;
    parameters = null;
    serializable = null;
    file = null;
  }
  


  public String getText()
  {
    return text;
  }
  








  public EntityBuilder setText(String text)
  {
    clearContent();
    this.text = text;
    return this;
  }
  



  public byte[] getBinary()
  {
    return binary;
  }
  








  public EntityBuilder setBinary(byte[] binary)
  {
    clearContent();
    this.binary = binary;
    return this;
  }
  



  public InputStream getStream()
  {
    return stream;
  }
  








  public EntityBuilder setStream(InputStream stream)
  {
    clearContent();
    this.stream = stream;
    return this;
  }
  




  public List<NameValuePair> getParameters()
  {
    return parameters;
  }
  







  public EntityBuilder setParameters(List<NameValuePair> parameters)
  {
    clearContent();
    this.parameters = parameters;
    return this;
  }
  







  public EntityBuilder setParameters(NameValuePair... parameters)
  {
    return setParameters(Arrays.asList(parameters));
  }
  



  public Serializable getSerializable()
  {
    return serializable;
  }
  








  public EntityBuilder setSerializable(Serializable serializable)
  {
    clearContent();
    this.serializable = serializable;
    return this;
  }
  



  public File getFile()
  {
    return file;
  }
  








  public EntityBuilder setFile(File file)
  {
    clearContent();
    this.file = file;
    return this;
  }
  


  public ContentType getContentType()
  {
    return contentType;
  }
  


  public EntityBuilder setContentType(ContentType contentType)
  {
    this.contentType = contentType;
    return this;
  }
  


  public String getContentEncoding()
  {
    return contentEncoding;
  }
  


  public EntityBuilder setContentEncoding(String contentEncoding)
  {
    this.contentEncoding = contentEncoding;
    return this;
  }
  


  public boolean isChunked()
  {
    return chunked;
  }
  


  public EntityBuilder chunked()
  {
    chunked = true;
    return this;
  }
  


  public boolean isGzipCompress()
  {
    return gzipCompress;
  }
  


  public EntityBuilder gzipCompress()
  {
    gzipCompress = true;
    return this;
  }
  
  private ContentType getContentOrDefault(ContentType def) {
    return contentType != null ? contentType : def;
  }
  

  public HttpEntity build()
  {
    AbstractHttpEntity e;
    AbstractHttpEntity e;
    if (text != null) {
      e = new StringEntity(text, getContentOrDefault(ContentType.DEFAULT_TEXT)); } else { AbstractHttpEntity e;
      if (binary != null) {
        e = new ByteArrayEntity(binary, getContentOrDefault(ContentType.DEFAULT_BINARY)); } else { AbstractHttpEntity e;
        if (stream != null) {
          e = new InputStreamEntity(stream, -1L, getContentOrDefault(ContentType.DEFAULT_BINARY)); } else { AbstractHttpEntity e;
          if (parameters != null) {
            e = new UrlEncodedFormEntity(parameters, contentType != null ? contentType.getCharset() : null);
          }
          else if (serializable != null) {
            AbstractHttpEntity e = new SerializableEntity(serializable);
            e.setContentType(ContentType.DEFAULT_BINARY.toString()); } else { AbstractHttpEntity e;
            if (file != null) {
              e = new FileEntity(file, getContentOrDefault(ContentType.DEFAULT_BINARY));
            } else
              e = new BasicHttpEntity();
          } } } }
    if ((e.getContentType() != null) && (contentType != null)) {
      e.setContentType(contentType.toString());
    }
    e.setContentEncoding(contentEncoding);
    e.setChunked(chunked);
    if (gzipCompress) {
      return new GzipCompressingEntity(e);
    }
    return e;
  }
}
