package org.seleniumhq.jetty9.http;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.seleniumhq.jetty9.util.BufferUtil;
import org.seleniumhq.jetty9.util.Trie;
import org.seleniumhq.jetty9.util.resource.Resource;


























public class ResourceHttpContent
  implements HttpContent
{
  final Resource _resource;
  final String _contentType;
  final int _maxBuffer;
  Map<CompressedContentFormat, HttpContent> _precompressedContents;
  String _etag;
  
  public ResourceHttpContent(Resource resource, String contentType)
  {
    this(resource, contentType, -1, null);
  }
  

  public ResourceHttpContent(Resource resource, String contentType, int maxBuffer)
  {
    this(resource, contentType, maxBuffer, null);
  }
  

  public ResourceHttpContent(Resource resource, String contentType, int maxBuffer, Map<CompressedContentFormat, HttpContent> precompressedContents)
  {
    _resource = resource;
    _contentType = contentType;
    _maxBuffer = maxBuffer;
    if (precompressedContents == null)
    {
      _precompressedContents = null;
    }
    else
    {
      _precompressedContents = new HashMap(precompressedContents.size());
      for (Map.Entry<CompressedContentFormat, HttpContent> entry : precompressedContents.entrySet())
      {
        _precompressedContents.put(entry.getKey(), new PrecompressedHttpContent(this, (HttpContent)entry.getValue(), (CompressedContentFormat)entry.getKey()));
      }
    }
  }
  


  public String getContentTypeValue()
  {
    return _contentType;
  }
  


  public HttpField getContentType()
  {
    return _contentType == null ? null : new HttpField(HttpHeader.CONTENT_TYPE, _contentType);
  }
  


  public HttpField getContentEncoding()
  {
    return null;
  }
  


  public String getContentEncodingValue()
  {
    return null;
  }
  


  public String getCharacterEncoding()
  {
    return _contentType == null ? null : MimeTypes.getCharsetFromContentType(_contentType);
  }
  


  public MimeTypes.Type getMimeType()
  {
    return _contentType == null ? null : (MimeTypes.Type)MimeTypes.CACHE.get(MimeTypes.getContentTypeWithoutCharset(_contentType));
  }
  


  public HttpField getLastModified()
  {
    long lm = _resource.lastModified();
    return lm >= 0L ? new HttpField(HttpHeader.LAST_MODIFIED, DateGenerator.formatDate(lm)) : null;
  }
  


  public String getLastModifiedValue()
  {
    long lm = _resource.lastModified();
    return lm >= 0L ? DateGenerator.formatDate(lm) : null;
  }
  


  public ByteBuffer getDirectBuffer()
  {
    if ((_resource.length() <= 0L) || ((_maxBuffer > 0) && (_maxBuffer < _resource.length()))) {
      return null;
    }
    try {
      return BufferUtil.toBuffer(_resource, true);
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
  }
  


  public HttpField getETag()
  {
    return new HttpField(HttpHeader.ETAG, getETagValue());
  }
  


  public String getETagValue()
  {
    return _resource.getWeakETag();
  }
  


  public ByteBuffer getIndirectBuffer()
  {
    if ((_resource.length() <= 0L) || ((_maxBuffer > 0) && (_maxBuffer < _resource.length()))) {
      return null;
    }
    try {
      return BufferUtil.toBuffer(_resource, false);
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
  }
  


  public HttpField getContentLength()
  {
    long l = _resource.length();
    return l == -1L ? null : new HttpField.LongValueHttpField(HttpHeader.CONTENT_LENGTH, _resource.length());
  }
  


  public long getContentLengthValue()
  {
    return _resource.length();
  }
  

  public InputStream getInputStream()
    throws IOException
  {
    return _resource.getInputStream();
  }
  

  public ReadableByteChannel getReadableByteChannel()
    throws IOException
  {
    return _resource.getReadableByteChannel();
  }
  


  public Resource getResource()
  {
    return _resource;
  }
  


  public void release()
  {
    _resource.close();
  }
  


  public String toString()
  {
    return String.format("%s@%x{r=%s,c=%b}", new Object[] { getClass().getSimpleName(), Integer.valueOf(hashCode()), _resource, Boolean.valueOf(_precompressedContents != null ? 1 : false) });
  }
  


  public Map<CompressedContentFormat, HttpContent> getPrecompressedContents()
  {
    return _precompressedContents;
  }
}
