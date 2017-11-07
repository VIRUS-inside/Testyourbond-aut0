package org.seleniumhq.jetty9.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.seleniumhq.jetty9.http.CompressedContentFormat;
import org.seleniumhq.jetty9.http.HttpContent;
import org.seleniumhq.jetty9.http.HttpContent.ContentFactory;
import org.seleniumhq.jetty9.http.MimeTypes;
import org.seleniumhq.jetty9.http.ResourceHttpContent;
import org.seleniumhq.jetty9.util.resource.Resource;
import org.seleniumhq.jetty9.util.resource.ResourceFactory;


























public class ResourceContentFactory
  implements HttpContent.ContentFactory
{
  private final ResourceFactory _factory;
  private final MimeTypes _mimeTypes;
  private final CompressedContentFormat[] _precompressedFormats;
  
  public ResourceContentFactory(ResourceFactory factory, MimeTypes mimeTypes, CompressedContentFormat[] precompressedFormats)
  {
    _factory = factory;
    _mimeTypes = mimeTypes;
    _precompressedFormats = precompressedFormats;
  }
  



  public HttpContent getContent(String pathInContext, int maxBufferSize)
    throws IOException
  {
    Resource resource = _factory.getResource(pathInContext);
    HttpContent loaded = load(pathInContext, resource, maxBufferSize);
    return loaded;
  }
  


  private HttpContent load(String pathInContext, Resource resource, int maxBufferSize)
    throws IOException
  {
    if ((resource == null) || (!resource.exists())) {
      return null;
    }
    if (resource.isDirectory()) {
      return new ResourceHttpContent(resource, _mimeTypes.getMimeByExtension(resource.toString()), maxBufferSize);
    }
    
    String mt = _mimeTypes.getMimeByExtension(pathInContext);
    if (_precompressedFormats.length > 0)
    {

      Map<CompressedContentFormat, HttpContent> compressedContents = new HashMap(_precompressedFormats.length);
      for (CompressedContentFormat format : _precompressedFormats)
      {
        String compressedPathInContext = pathInContext + _extension;
        Resource compressedResource = _factory.getResource(compressedPathInContext);
        if ((compressedResource.exists()) && (compressedResource.lastModified() >= resource.lastModified()) && 
          (compressedResource.length() < resource.length()))
          compressedContents.put(format, new ResourceHttpContent(compressedResource, _mimeTypes
            .getMimeByExtension(compressedPathInContext), maxBufferSize));
      }
      if (!compressedContents.isEmpty())
        return new ResourceHttpContent(resource, mt, maxBufferSize, compressedContents);
    }
    return new ResourceHttpContent(resource, mt, maxBufferSize);
  }
  



  public String toString()
  {
    return "ResourceContentFactory[" + _factory + "]@" + hashCode();
  }
}
