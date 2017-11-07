package org.seleniumhq.jetty9.http;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.Map;
import org.seleniumhq.jetty9.util.resource.Resource;





















public class PrecompressedHttpContent
  implements HttpContent
{
  private final HttpContent _content;
  private final HttpContent _precompressedContent;
  private final CompressedContentFormat _format;
  
  public PrecompressedHttpContent(HttpContent content, HttpContent precompressedContent, CompressedContentFormat format)
  {
    _content = content;
    _precompressedContent = precompressedContent;
    _format = format;
    if ((_precompressedContent == null) || (_format == null))
    {
      throw new NullPointerException("Missing compressed content and/or format");
    }
  }
  

  public int hashCode()
  {
    return _content.hashCode();
  }
  

  public boolean equals(Object obj)
  {
    return _content.equals(obj);
  }
  

  public Resource getResource()
  {
    return _content.getResource();
  }
  

  public HttpField getETag()
  {
    return new HttpField(HttpHeader.ETAG, getETagValue());
  }
  

  public String getETagValue()
  {
    return _content.getResource().getWeakETag(_format._etag);
  }
  

  public HttpField getLastModified()
  {
    return _content.getLastModified();
  }
  

  public String getLastModifiedValue()
  {
    return _content.getLastModifiedValue();
  }
  

  public HttpField getContentType()
  {
    return _content.getContentType();
  }
  

  public String getContentTypeValue()
  {
    return _content.getContentTypeValue();
  }
  

  public HttpField getContentEncoding()
  {
    return _format._contentEncoding;
  }
  

  public String getContentEncodingValue()
  {
    return _format._contentEncoding.getValue();
  }
  

  public String getCharacterEncoding()
  {
    return _content.getCharacterEncoding();
  }
  

  public MimeTypes.Type getMimeType()
  {
    return _content.getMimeType();
  }
  

  public void release()
  {
    _content.release();
  }
  

  public ByteBuffer getIndirectBuffer()
  {
    return _precompressedContent.getIndirectBuffer();
  }
  

  public ByteBuffer getDirectBuffer()
  {
    return _precompressedContent.getDirectBuffer();
  }
  

  public HttpField getContentLength()
  {
    return _precompressedContent.getContentLength();
  }
  

  public long getContentLengthValue()
  {
    return _precompressedContent.getContentLengthValue();
  }
  
  public InputStream getInputStream()
    throws IOException
  {
    return _precompressedContent.getInputStream();
  }
  
  public ReadableByteChannel getReadableByteChannel()
    throws IOException
  {
    return _precompressedContent.getReadableByteChannel();
  }
  

  public String toString()
  {
    return String.format("PrecompressedHttpContent@%x{e=%s,r=%s|%s,lm=%s|%s,ct=%s}", new Object[] { Integer.valueOf(hashCode()), _format._encoding, _content
      .getResource(), _precompressedContent.getResource(), 
      Long.valueOf(_content.getResource().lastModified()), Long.valueOf(_precompressedContent.getResource().lastModified()), 
      getContentType() });
  }
  

  public Map<CompressedContentFormat, HttpContent> getPrecompressedContents()
  {
    return null;
  }
}
