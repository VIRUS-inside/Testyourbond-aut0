package org.apache.http.entity.mime;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.Args;





































public class MultipartEntityBuilder
{
  private static final char[] MULTIPART_CHARS = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
  

  private static final String DEFAULT_SUBTYPE = "form-data";
  
  private ContentType contentType;
  
  private HttpMultipartMode mode = HttpMultipartMode.STRICT;
  private String boundary = null;
  private Charset charset = null;
  private List<FormBodyPart> bodyParts = null;
  
  public static MultipartEntityBuilder create() {
    return new MultipartEntityBuilder();
  }
  
  MultipartEntityBuilder() {}
  
  public MultipartEntityBuilder setMode(HttpMultipartMode mode)
  {
    this.mode = mode;
    return this;
  }
  
  public MultipartEntityBuilder setLaxMode() {
    mode = HttpMultipartMode.BROWSER_COMPATIBLE;
    return this;
  }
  
  public MultipartEntityBuilder setStrictMode() {
    mode = HttpMultipartMode.STRICT;
    return this;
  }
  
  public MultipartEntityBuilder setBoundary(String boundary) {
    this.boundary = boundary;
    return this;
  }
  


  public MultipartEntityBuilder setMimeSubtype(String subType)
  {
    Args.notBlank(subType, "MIME subtype");
    contentType = ContentType.create("multipart/" + subType);
    return this;
  }
  




  @Deprecated
  public MultipartEntityBuilder seContentType(ContentType contentType)
  {
    return setContentType(contentType);
  }
  


  public MultipartEntityBuilder setContentType(ContentType contentType)
  {
    Args.notNull(contentType, "Content type");
    this.contentType = contentType;
    return this;
  }
  
  public MultipartEntityBuilder setCharset(Charset charset) {
    this.charset = charset;
    return this;
  }
  


  public MultipartEntityBuilder addPart(FormBodyPart bodyPart)
  {
    if (bodyPart == null) {
      return this;
    }
    if (bodyParts == null) {
      bodyParts = new ArrayList();
    }
    bodyParts.add(bodyPart);
    return this;
  }
  
  public MultipartEntityBuilder addPart(String name, ContentBody contentBody) {
    Args.notNull(name, "Name");
    Args.notNull(contentBody, "Content body");
    return addPart(FormBodyPartBuilder.create(name, contentBody).build());
  }
  
  public MultipartEntityBuilder addTextBody(String name, String text, ContentType contentType)
  {
    return addPart(name, new StringBody(text, contentType));
  }
  
  public MultipartEntityBuilder addTextBody(String name, String text)
  {
    return addTextBody(name, text, ContentType.DEFAULT_TEXT);
  }
  
  public MultipartEntityBuilder addBinaryBody(String name, byte[] b, ContentType contentType, String filename)
  {
    return addPart(name, new ByteArrayBody(b, contentType, filename));
  }
  
  public MultipartEntityBuilder addBinaryBody(String name, byte[] b)
  {
    return addBinaryBody(name, b, ContentType.DEFAULT_BINARY, null);
  }
  
  public MultipartEntityBuilder addBinaryBody(String name, File file, ContentType contentType, String filename)
  {
    return addPart(name, new FileBody(file, contentType, filename));
  }
  
  public MultipartEntityBuilder addBinaryBody(String name, File file)
  {
    return addBinaryBody(name, file, ContentType.DEFAULT_BINARY, file != null ? file.getName() : null);
  }
  

  public MultipartEntityBuilder addBinaryBody(String name, InputStream stream, ContentType contentType, String filename)
  {
    return addPart(name, new InputStreamBody(stream, contentType, filename));
  }
  
  public MultipartEntityBuilder addBinaryBody(String name, InputStream stream) {
    return addBinaryBody(name, stream, ContentType.DEFAULT_BINARY, null);
  }
  
  private String generateBoundary() {
    StringBuilder buffer = new StringBuilder();
    Random rand = new Random();
    int count = rand.nextInt(11) + 30;
    for (int i = 0; i < count; i++) {
      buffer.append(MULTIPART_CHARS[rand.nextInt(MULTIPART_CHARS.length)]);
    }
    return buffer.toString();
  }
  
  MultipartFormEntity buildEntity() {
    String boundaryCopy = boundary;
    if ((boundaryCopy == null) && (contentType != null)) {
      boundaryCopy = contentType.getParameter("boundary");
    }
    if (boundaryCopy == null) {
      boundaryCopy = generateBoundary();
    }
    Charset charsetCopy = charset;
    if ((charsetCopy == null) && (contentType != null)) {
      charsetCopy = contentType.getCharset();
    }
    List<NameValuePair> paramsList = new ArrayList(2);
    paramsList.add(new BasicNameValuePair("boundary", boundaryCopy));
    if (charsetCopy != null) {
      paramsList.add(new BasicNameValuePair("charset", charsetCopy.name()));
    }
    NameValuePair[] params = (NameValuePair[])paramsList.toArray(new NameValuePair[paramsList.size()]);
    ContentType contentTypeCopy = contentType != null ? contentType.withParameters(params) : ContentType.create("multipart/form-data", params);
    

    List<FormBodyPart> bodyPartsCopy = bodyParts != null ? new ArrayList(bodyParts) : Collections.emptyList();
    
    HttpMultipartMode modeCopy = mode != null ? mode : HttpMultipartMode.STRICT;
    AbstractMultipartForm form;
    switch (1.$SwitchMap$org$apache$http$entity$mime$HttpMultipartMode[modeCopy.ordinal()]) {
    case 1: 
      form = new HttpBrowserCompatibleMultipart(charsetCopy, boundaryCopy, bodyPartsCopy);
      break;
    case 2: 
      form = new HttpRFC6532Multipart(charsetCopy, boundaryCopy, bodyPartsCopy);
      break;
    default: 
      form = new HttpStrictMultipart(charsetCopy, boundaryCopy, bodyPartsCopy);
    }
    return new MultipartFormEntity(form, contentTypeCopy, form.getTotalLength());
  }
  
  public HttpEntity build() {
    return buildEntity();
  }
}
