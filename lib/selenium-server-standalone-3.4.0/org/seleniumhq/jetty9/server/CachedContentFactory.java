package org.seleniumhq.jetty9.server;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.seleniumhq.jetty9.http.CompressedContentFormat;
import org.seleniumhq.jetty9.http.DateGenerator;
import org.seleniumhq.jetty9.http.HttpContent;
import org.seleniumhq.jetty9.http.HttpContent.ContentFactory;
import org.seleniumhq.jetty9.http.HttpField;
import org.seleniumhq.jetty9.http.HttpHeader;
import org.seleniumhq.jetty9.http.MimeTypes;
import org.seleniumhq.jetty9.http.MimeTypes.Type;
import org.seleniumhq.jetty9.http.PreEncodedHttpField;
import org.seleniumhq.jetty9.http.PrecompressedHttpContent;
import org.seleniumhq.jetty9.http.ResourceHttpContent;
import org.seleniumhq.jetty9.util.BufferUtil;
import org.seleniumhq.jetty9.util.Trie;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;
import org.seleniumhq.jetty9.util.resource.Resource;
import org.seleniumhq.jetty9.util.resource.ResourceFactory;



















public class CachedContentFactory
  implements HttpContent.ContentFactory
{
  private static final Logger LOG = Log.getLogger(CachedContentFactory.class);
  private static final Map<CompressedContentFormat, CachedPrecompressedHttpContent> NO_PRECOMPRESSED = Collections.unmodifiableMap(Collections.emptyMap());
  
  private final ConcurrentMap<String, CachedHttpContent> _cache;
  
  private final AtomicInteger _cachedSize;
  private final AtomicInteger _cachedFiles;
  private final ResourceFactory _factory;
  private final CachedContentFactory _parent;
  private final MimeTypes _mimeTypes;
  private final boolean _etags;
  private final CompressedContentFormat[] _precompressedFormats;
  private final boolean _useFileMappedBuffer;
  private int _maxCachedFileSize = 134217728;
  private int _maxCachedFiles = 2048;
  private int _maxCacheSize = 268435456;
  









  public CachedContentFactory(CachedContentFactory parent, ResourceFactory factory, MimeTypes mimeTypes, boolean useFileMappedBuffer, boolean etags, CompressedContentFormat[] precompressedFormats)
  {
    _factory = factory;
    _cache = new ConcurrentHashMap();
    _cachedSize = new AtomicInteger();
    _cachedFiles = new AtomicInteger();
    _mimeTypes = mimeTypes;
    _parent = parent;
    _useFileMappedBuffer = useFileMappedBuffer;
    _etags = etags;
    _precompressedFormats = precompressedFormats;
  }
  

  public int getCachedSize()
  {
    return _cachedSize.get();
  }
  

  public int getCachedFiles()
  {
    return _cachedFiles.get();
  }
  

  public int getMaxCachedFileSize()
  {
    return _maxCachedFileSize;
  }
  

  public void setMaxCachedFileSize(int maxCachedFileSize)
  {
    _maxCachedFileSize = maxCachedFileSize;
    shrinkCache();
  }
  

  public int getMaxCacheSize()
  {
    return _maxCacheSize;
  }
  

  public void setMaxCacheSize(int maxCacheSize)
  {
    _maxCacheSize = maxCacheSize;
    shrinkCache();
  }
  




  public int getMaxCachedFiles()
  {
    return _maxCachedFiles;
  }
  




  public void setMaxCachedFiles(int maxCachedFiles)
  {
    _maxCachedFiles = maxCachedFiles;
    shrinkCache();
  }
  

  public boolean isUseFileMappedBuffer()
  {
    return _useFileMappedBuffer;
  }
  

  public void flushCache()
  {
    if (_cache != null)
    {
      while (_cache.size() > 0)
      {
        for (String path : _cache.keySet())
        {
          CachedHttpContent content = (CachedHttpContent)_cache.remove(path);
          if (content != null) {
            content.invalidate();
          }
        }
      }
    }
  }
  
  @Deprecated
  public HttpContent lookup(String pathInContext)
    throws IOException
  {
    return getContent(pathInContext, _maxCachedFileSize);
  }
  















  public HttpContent getContent(String pathInContext, int maxBufferSize)
    throws IOException
  {
    CachedHttpContent content = (CachedHttpContent)_cache.get(pathInContext);
    if ((content != null) && (content.isValid())) {
      return content;
    }
    
    Resource resource = _factory.getResource(pathInContext);
    HttpContent loaded = load(pathInContext, resource, maxBufferSize);
    if (loaded != null) {
      return loaded;
    }
    
    if (_parent != null)
    {
      HttpContent httpContent = _parent.getContent(pathInContext, maxBufferSize);
      if (httpContent != null) {
        return httpContent;
      }
    }
    return null;
  }
  





  protected boolean isCacheable(Resource resource)
  {
    if (_maxCachedFiles <= 0) {
      return false;
    }
    long len = resource.length();
    

    return (len > 0L) && ((_useFileMappedBuffer) || ((len < _maxCachedFileSize) && (len < _maxCacheSize)));
  }
  

  private HttpContent load(String pathInContext, Resource resource, int maxBufferSize)
    throws IOException
  {
    if ((resource == null) || (!resource.exists())) {
      return null;
    }
    if (resource.isDirectory()) {
      return new ResourceHttpContent(resource, _mimeTypes.getMimeByExtension(resource.toString()), getMaxCachedFileSize());
    }
    
    if (isCacheable(resource))
    {
      CachedHttpContent content = null;
      

      if (_precompressedFormats.length > 0)
      {
        Map<CompressedContentFormat, CachedHttpContent> precompresssedContents = new HashMap(_precompressedFormats.length);
        for (CompressedContentFormat format : _precompressedFormats)
        {
          String compressedPathInContext = pathInContext + _extension;
          CachedHttpContent compressedContent = (CachedHttpContent)_cache.get(compressedPathInContext);
          if ((compressedContent == null) || (compressedContent.isValid()))
          {
            compressedContent = null;
            Resource compressedResource = _factory.getResource(compressedPathInContext);
            if ((compressedResource.exists()) && (compressedResource.lastModified() >= resource.lastModified()) && 
              (compressedResource.length() < resource.length()))
            {
              compressedContent = new CachedHttpContent(compressedPathInContext, compressedResource, null);
              CachedHttpContent added = (CachedHttpContent)_cache.putIfAbsent(compressedPathInContext, compressedContent);
              if (added != null)
              {
                compressedContent.invalidate();
                compressedContent = added;
              }
            }
          }
          if (compressedContent != null)
            precompresssedContents.put(format, compressedContent);
        }
        content = new CachedHttpContent(pathInContext, resource, precompresssedContents);
      }
      else {
        content = new CachedHttpContent(pathInContext, resource, null);
      }
      
      CachedHttpContent added = (CachedHttpContent)_cache.putIfAbsent(pathInContext, content);
      if (added != null)
      {
        content.invalidate();
        content = added;
      }
      
      return content;
    }
    

    String mt = _mimeTypes.getMimeByExtension(pathInContext);
    if (_precompressedFormats.length > 0)
    {

      Map<CompressedContentFormat, HttpContent> compressedContents = new HashMap();
      for (CompressedContentFormat format : _precompressedFormats)
      {
        String compressedPathInContext = pathInContext + _extension;
        CachedHttpContent compressedContent = (CachedHttpContent)_cache.get(compressedPathInContext);
        if ((compressedContent != null) && (compressedContent.isValid()) && (compressedContent.getResource().lastModified() >= resource.lastModified())) {
          compressedContents.put(format, compressedContent);
        }
        
        Resource compressedResource = _factory.getResource(compressedPathInContext);
        if ((compressedResource.exists()) && (compressedResource.lastModified() >= resource.lastModified()) && 
          (compressedResource.length() < resource.length()))
          compressedContents.put(format, new ResourceHttpContent(compressedResource, _mimeTypes
            .getMimeByExtension(compressedPathInContext), maxBufferSize));
      }
      if (!compressedContents.isEmpty()) {
        return new ResourceHttpContent(resource, mt, maxBufferSize, compressedContents);
      }
    }
    return new ResourceHttpContent(resource, mt, maxBufferSize);
  }
  


  private void shrinkCache()
  {
    while ((_cache.size() > 0) && ((_cachedFiles.get() > _maxCachedFiles) || (_cachedSize.get() > _maxCacheSize)))
    {

      SortedSet<CachedHttpContent> sorted = new TreeSet(new Comparator()
      {

        public int compare(CachedContentFactory.CachedHttpContent c1, CachedContentFactory.CachedHttpContent c2)
        {
          if (_lastAccessed < _lastAccessed) {
            return -1;
          }
          if (_lastAccessed > _lastAccessed) {
            return 1;
          }
          if (_contentLengthValue < _contentLengthValue) {
            return -1;
          }
          return _key.compareTo(_key);
        }
      });
      for (CachedHttpContent content : _cache.values()) {
        sorted.add(content);
      }
      
      for (CachedHttpContent content : sorted)
      {
        if ((_cachedFiles.get() <= _maxCachedFiles) && (_cachedSize.get() <= _maxCacheSize))
          break;
        if (content == _cache.remove(content.getKey())) {
          content.invalidate();
        }
      }
    }
  }
  
  protected ByteBuffer getIndirectBuffer(Resource resource)
  {
    try
    {
      return BufferUtil.toBuffer(resource, true);
    }
    catch (IOException|IllegalArgumentException e)
    {
      LOG.warn(e); }
    return null;
  }
  




  protected ByteBuffer getMappedBuffer(Resource resource)
  {
    try
    {
      if ((_useFileMappedBuffer) && (resource.getFile() != null) && (resource.length() < 2147483647L)) {
        return BufferUtil.toMappedBuffer(resource.getFile());
      }
    }
    catch (IOException|IllegalArgumentException e) {
      LOG.warn(e);
    }
    return null;
  }
  

  protected ByteBuffer getDirectBuffer(Resource resource)
  {
    try
    {
      return BufferUtil.toBuffer(resource, true);
    }
    catch (IOException|IllegalArgumentException e)
    {
      LOG.warn(e);
    }
    return null;
  }
  


  public String toString()
  {
    return "ResourceCache[" + _parent + "," + _factory + "]@" + hashCode();
  }
  

  public class CachedHttpContent
    implements HttpContent
  {
    final String _key;
    
    final Resource _resource;
    
    final int _contentLengthValue;
    
    final HttpField _contentType;
    final String _characterEncoding;
    final MimeTypes.Type _mimeType;
    final HttpField _contentLength;
    final HttpField _lastModified;
    final long _lastModifiedValue;
    final HttpField _etag;
    final Map<CompressedContentFormat, CachedContentFactory.CachedPrecompressedHttpContent> _precompressed;
    volatile long _lastAccessed;
    AtomicReference<ByteBuffer> _indirectBuffer = new AtomicReference();
    AtomicReference<ByteBuffer> _directBuffer = new AtomicReference();
    

    CachedHttpContent(Resource pathInContext, Map<CompressedContentFormat, CachedHttpContent> resource)
    {
      _key = pathInContext;
      _resource = resource;
      
      String contentType = _mimeTypes.getMimeByExtension(_resource.toString());
      _contentType = (contentType == null ? null : new PreEncodedHttpField(HttpHeader.CONTENT_TYPE, contentType));
      _characterEncoding = (_contentType == null ? null : MimeTypes.getCharsetFromContentType(contentType));
      _mimeType = (_contentType == null ? null : (MimeTypes.Type)MimeTypes.CACHE.get(MimeTypes.getContentTypeWithoutCharset(contentType)));
      
      boolean exists = resource.exists();
      _lastModifiedValue = (exists ? resource.lastModified() : -1L);
      
      _lastModified = (_lastModifiedValue == -1L ? null : new PreEncodedHttpField(HttpHeader.LAST_MODIFIED, DateGenerator.formatDate(_lastModifiedValue)));
      
      _contentLengthValue = (exists ? (int)resource.length() : 0);
      _contentLength = new PreEncodedHttpField(HttpHeader.CONTENT_LENGTH, Long.toString(_contentLengthValue));
      
      if (_cachedFiles.incrementAndGet() > _maxCachedFiles) {
        CachedContentFactory.this.shrinkCache();
      }
      _lastAccessed = System.currentTimeMillis();
      
      _etag = (_etags ? new PreEncodedHttpField(HttpHeader.ETAG, resource.getWeakETag()) : null);
      
      if (precompressedResources != null)
      {
        _precompressed = new HashMap(precompressedResources.size());
        for (Map.Entry<CompressedContentFormat, CachedHttpContent> entry : precompressedResources.entrySet())
        {
          _precompressed.put(entry.getKey(), new CachedContentFactory.CachedPrecompressedHttpContent(CachedContentFactory.this, this, (CachedHttpContent)entry.getValue(), (CompressedContentFormat)entry.getKey()));
        }
      }
      else
      {
        _precompressed = CachedContentFactory.NO_PRECOMPRESSED;
      }
    }
    


    public String getKey()
    {
      return _key;
    }
    

    public boolean isCached()
    {
      return _key != null;
    }
    

    public boolean isMiss()
    {
      return false;
    }
    


    public Resource getResource()
    {
      return _resource;
    }
    


    public HttpField getETag()
    {
      return _etag;
    }
    


    public String getETagValue()
    {
      return _etag.getValue();
    }
    

    boolean isValid()
    {
      if ((_lastModifiedValue == _resource.lastModified()) && (_contentLengthValue == _resource.length()))
      {
        _lastAccessed = System.currentTimeMillis();
        return true;
      }
      
      if (this == _cache.remove(_key))
        invalidate();
      return false;
    }
    

    protected void invalidate()
    {
      ByteBuffer indirect = (ByteBuffer)_indirectBuffer.get();
      if ((indirect != null) && (_indirectBuffer.compareAndSet(indirect, null))) {
        _cachedSize.addAndGet(-BufferUtil.length(indirect));
      }
      ByteBuffer direct = (ByteBuffer)_directBuffer.get();
      
      if ((direct != null) && (!BufferUtil.isMappedBuffer(direct)) && (_directBuffer.compareAndSet(direct, null))) {
        _cachedSize.addAndGet(-BufferUtil.length(direct));
      }
      _cachedFiles.decrementAndGet();
      _resource.close();
    }
    


    public HttpField getLastModified()
    {
      return _lastModified;
    }
    


    public String getLastModifiedValue()
    {
      return _lastModified == null ? null : _lastModified.getValue();
    }
    


    public HttpField getContentType()
    {
      return _contentType;
    }
    


    public String getContentTypeValue()
    {
      return _contentType == null ? null : _contentType.getValue();
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
      return _characterEncoding;
    }
    


    public MimeTypes.Type getMimeType()
    {
      return _mimeType;
    }
    



    public void release() {}
    



    public ByteBuffer getIndirectBuffer()
    {
      ByteBuffer buffer = (ByteBuffer)_indirectBuffer.get();
      if (buffer == null)
      {
        ByteBuffer buffer2 = getIndirectBuffer(_resource);
        
        if (buffer2 == null) {
          CachedContentFactory.LOG.warn("Could not load " + this, new Object[0]);
        } else if (_indirectBuffer.compareAndSet(null, buffer2))
        {
          buffer = buffer2;
          if (_cachedSize.addAndGet(BufferUtil.length(buffer)) > _maxCacheSize) {
            CachedContentFactory.this.shrinkCache();
          }
        } else {
          buffer = (ByteBuffer)_indirectBuffer.get();
        } }
      if (buffer == null)
        return null;
      return buffer.slice();
    }
    


    public ByteBuffer getDirectBuffer()
    {
      ByteBuffer buffer = (ByteBuffer)_directBuffer.get();
      if (buffer == null)
      {
        ByteBuffer mapped = getMappedBuffer(_resource);
        ByteBuffer direct = mapped == null ? getDirectBuffer(_resource) : mapped;
        
        if (direct == null) {
          CachedContentFactory.LOG.warn("Could not load " + this, new Object[0]);
        } else if (_directBuffer.compareAndSet(null, direct))
        {
          buffer = direct;
          if ((mapped == null) && (_cachedSize.addAndGet(BufferUtil.length(buffer)) > _maxCacheSize)) {
            CachedContentFactory.this.shrinkCache();
          }
        } else {
          buffer = (ByteBuffer)_directBuffer.get();
        } }
      if (buffer == null)
        return null;
      return buffer.asReadOnlyBuffer();
    }
    


    public HttpField getContentLength()
    {
      return _contentLength;
    }
    


    public long getContentLengthValue()
    {
      return _contentLengthValue;
    }
    

    public InputStream getInputStream()
      throws IOException
    {
      ByteBuffer indirect = getIndirectBuffer();
      if ((indirect != null) && (indirect.hasArray())) {
        return new ByteArrayInputStream(indirect.array(), indirect.arrayOffset() + indirect.position(), indirect.remaining());
      }
      return _resource.getInputStream();
    }
    

    public ReadableByteChannel getReadableByteChannel()
      throws IOException
    {
      return _resource.getReadableByteChannel();
    }
    


    public String toString()
    {
      return String.format("CachedContent@%x{r=%s,e=%b,lm=%s,ct=%s,c=%d}", new Object[] { Integer.valueOf(hashCode()), _resource, Boolean.valueOf(_resource.exists()), _lastModified, _contentType, Integer.valueOf(_precompressed.size()) });
    }
    


    public Map<CompressedContentFormat, ? extends HttpContent> getPrecompressedContents()
    {
      if (_precompressed.size() == 0)
        return null;
      Map<CompressedContentFormat, CachedContentFactory.CachedPrecompressedHttpContent> ret = _precompressed;
      for (Map.Entry<CompressedContentFormat, CachedContentFactory.CachedPrecompressedHttpContent> entry : _precompressed.entrySet())
      {
        if (!((CachedContentFactory.CachedPrecompressedHttpContent)entry.getValue()).isValid())
        {
          if (ret == _precompressed)
            ret = new HashMap(_precompressed);
          ret.remove(entry.getKey());
        }
      }
      return ret;
    }
  }
  

  public class CachedPrecompressedHttpContent
    extends PrecompressedHttpContent
  {
    private final CachedContentFactory.CachedHttpContent _content;
    
    private final CachedContentFactory.CachedHttpContent _precompressedContent;
    private final HttpField _etag;
    
    CachedPrecompressedHttpContent(CachedContentFactory.CachedHttpContent content, CachedContentFactory.CachedHttpContent precompressedContent, CompressedContentFormat format)
    {
      super(precompressedContent, format);
      _content = content;
      _precompressedContent = precompressedContent;
      
      _etag = (_etags ? new PreEncodedHttpField(HttpHeader.ETAG, _content.getResource().getWeakETag(_etag)) : null);
    }
    
    public boolean isValid()
    {
      return (_precompressedContent.isValid()) && (_content.isValid()) && (_content.getResource().lastModified() <= _precompressedContent.getResource().lastModified());
    }
    

    public HttpField getETag()
    {
      if (_etag != null)
        return _etag;
      return super.getETag();
    }
    

    public String getETagValue()
    {
      if (_etag != null)
        return _etag.getValue();
      return super.getETagValue();
    }
    

    public String toString()
    {
      return "Cached" + super.toString();
    }
  }
}
