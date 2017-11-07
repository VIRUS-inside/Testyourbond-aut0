package org.seleniumhq.jetty9.server.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.seleniumhq.jetty9.http.CompressedContentFormat;
import org.seleniumhq.jetty9.http.HttpField;
import org.seleniumhq.jetty9.http.HttpHeader;
import org.seleniumhq.jetty9.http.HttpMethod;
import org.seleniumhq.jetty9.http.MimeTypes;
import org.seleniumhq.jetty9.http.PreEncodedHttpField;
import org.seleniumhq.jetty9.server.Request;
import org.seleniumhq.jetty9.server.ResourceContentFactory;
import org.seleniumhq.jetty9.server.ResourceService;
import org.seleniumhq.jetty9.server.ResourceService.WelcomeFactory;
import org.seleniumhq.jetty9.util.URIUtil;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;
import org.seleniumhq.jetty9.util.resource.Resource;
import org.seleniumhq.jetty9.util.resource.ResourceFactory;




























public class ResourceHandler
  extends HandlerWrapper
  implements ResourceFactory, ResourceService.WelcomeFactory
{
  private static final Logger LOG = Log.getLogger(ResourceHandler.class);
  
  Resource _baseResource;
  ContextHandler _context;
  Resource _defaultStylesheet;
  MimeTypes _mimeTypes;
  private final ResourceService _resourceService;
  Resource _stylesheet;
  String[] _welcomes = { "index.html" };
  

  public ResourceHandler(ResourceService resourceService)
  {
    _resourceService = resourceService;
  }
  

  public ResourceHandler()
  {
    this(new ResourceService()
    {
      protected void notFound(HttpServletRequest request, HttpServletResponse response)
        throws IOException
      {}

    });
    _resourceService.setGzipEquivalentFileExtensions(new ArrayList(Arrays.asList(new String[] { ".svgz" })));
  }
  



  public String getWelcomeFile(String pathInContext)
  {
    if (_welcomes == null) {
      return null;
    }
    String welcome_servlet = null;
    for (int i = 0; i < _welcomes.length; i++)
    {
      String welcome_in_context = URIUtil.addPaths(pathInContext, _welcomes[i]);
      Resource welcome = getResource(welcome_in_context);
      if ((welcome != null) && (welcome.exists()))
        return _welcomes[i];
    }
    return welcome_servlet;
  }
  


  public void doStart()
    throws Exception
  {
    ContextHandler.Context scontext = ContextHandler.getCurrentContext();
    _context = (scontext == null ? null : scontext.getContextHandler());
    _mimeTypes = (_context == null ? new MimeTypes() : _context.getMimeTypes());
    
    _resourceService.setContentFactory(new ResourceContentFactory(this, _mimeTypes, _resourceService.getPrecompressedFormats()));
    _resourceService.setWelcomeFactory(this);
    
    super.doStart();
  }
  




  public Resource getBaseResource()
  {
    if (_baseResource == null)
      return null;
    return _baseResource;
  }
  




  public String getCacheControl()
  {
    return _resourceService.getCacheControl().getValue();
  }
  




  public List<String> getGzipEquivalentFileExtensions()
  {
    return _resourceService.getGzipEquivalentFileExtensions();
  }
  

  public MimeTypes getMimeTypes()
  {
    return _mimeTypes;
  }
  







  @Deprecated
  public int getMinAsyncContentLength()
  {
    return -1;
  }
  






  @Deprecated
  public int getMinMemoryMappedContentLength()
  {
    return -1;
  }
  




  public Resource getResource(String path)
  {
    if (LOG.isDebugEnabled()) {
      LOG.debug("{} getResource({})", new Object[] { _context == null ? _baseResource : _context, _baseResource, path });
    }
    if ((path == null) || (!path.startsWith("/"))) {
      return null;
    }
    try
    {
      Resource r = null;
      
      if (_baseResource != null)
      {
        path = URIUtil.canonicalPath(path);
        r = _baseResource.addPath(path);
        
        if ((r != null) && (r.isAlias()) && ((_context == null) || (!_context.checkAlias(path, r))))
        {
          if (LOG.isDebugEnabled())
            LOG.debug("resource={} alias={}", new Object[] { r, r.getAlias() });
          return null;
        }
      }
      else if (_context != null) {
        r = _context.getResource(path);
      }
      if (((r == null) || (!r.exists())) && (path.endsWith("/jetty-dir.css"))) {}
      return getStylesheet();

    }
    catch (Exception e)
    {

      LOG.debug(e);
    }
    
    return null;
  }
  




  public String getResourceBase()
  {
    if (_baseResource == null)
      return null;
    return _baseResource.toString();
  }
  




  public Resource getStylesheet()
  {
    if (_stylesheet != null)
    {
      return _stylesheet;
    }
    

    if (_defaultStylesheet == null)
    {
      _defaultStylesheet = Resource.newResource(getClass().getResource("/jetty-dir.css"));
    }
    return _defaultStylesheet;
  }
  


  public String[] getWelcomeFiles()
  {
    return _welcomes;
  }
  




  public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    if (baseRequest.isHandled()) {
      return;
    }
    if (!HttpMethod.GET.is(request.getMethod()))
    {
      if (!HttpMethod.HEAD.is(request.getMethod()))
      {

        super.handle(target, baseRequest, request, response);
        return;
      }
    }
    
    _resourceService.doGet(request, response);
    
    if (response.isCommitted()) {
      baseRequest.setHandled(true);
    }
    else {
      super.handle(target, baseRequest, request, response);
    }
  }
  



  public boolean isAcceptRanges()
  {
    return _resourceService.isAcceptRanges();
  }
  



  public boolean isDirAllowed()
  {
    return _resourceService.isDirAllowed();
  }
  






  public boolean isDirectoriesListed()
  {
    return _resourceService.isDirAllowed();
  }
  




  public boolean isEtags()
  {
    return _resourceService.isEtags();
  }
  




  @Deprecated
  public boolean isGzip()
  {
    for (CompressedContentFormat formats : _resourceService.getPrecompressedFormats())
    {
      if (GZIP_encoding.equals(_encoding))
        return true;
    }
    return false;
  }
  



  public CompressedContentFormat[] getPrecompressedFormats()
  {
    return _resourceService.getPrecompressedFormats();
  }
  




  public boolean isPathInfoOnly()
  {
    return _resourceService.isPathInfoOnly();
  }
  




  public boolean isRedirectWelcome()
  {
    return _resourceService.isRedirectWelcome();
  }
  




  public void setAcceptRanges(boolean acceptRanges)
  {
    _resourceService.setAcceptRanges(acceptRanges);
  }
  





  public void setBaseResource(Resource base)
  {
    _baseResource = base;
  }
  





  public void setCacheControl(String cacheControl)
  {
    _resourceService.setCacheControl(new PreEncodedHttpField(HttpHeader.CACHE_CONTROL, cacheControl));
  }
  





  public void setDirAllowed(boolean dirAllowed)
  {
    _resourceService.setDirAllowed(dirAllowed);
  }
  







  public void setDirectoriesListed(boolean directory)
  {
    _resourceService.setDirAllowed(directory);
  }
  





  public void setEtags(boolean etags)
  {
    _resourceService.setEtags(etags);
  }
  





  @Deprecated
  public void setGzip(boolean gzip)
  {
    setPrecompressedFormats(gzip ? new CompressedContentFormat[] { CompressedContentFormat.GZIP } : new CompressedContentFormat[0]);
  }
  




  public void setGzipEquivalentFileExtensions(List<String> gzipEquivalentFileExtensions)
  {
    _resourceService.setGzipEquivalentFileExtensions(gzipEquivalentFileExtensions);
  }
  





  public void setPrecompressedFormats(CompressedContentFormat[] precompressedFormats)
  {
    _resourceService.setPrecompressedFormats(precompressedFormats);
  }
  

  public void setMimeTypes(MimeTypes mimeTypes)
  {
    _mimeTypes = mimeTypes;
  }
  








  @Deprecated
  public void setMinAsyncContentLength(int minAsyncContentLength) {}
  








  @Deprecated
  public void setMinMemoryMappedContentLength(int minMemoryMappedFileSize) {}
  








  public void setPathInfoOnly(boolean pathInfoOnly)
  {
    _resourceService.setPathInfoOnly(pathInfoOnly);
  }
  







  public void setRedirectWelcome(boolean redirectWelcome)
  {
    _resourceService.setRedirectWelcome(redirectWelcome);
  }
  





  public void setResourceBase(String resourceBase)
  {
    try
    {
      setBaseResource(Resource.newResource(resourceBase));
    }
    catch (Exception e)
    {
      LOG.warn(e.toString(), new Object[0]);
      LOG.debug(e);
      throw new IllegalArgumentException(resourceBase);
    }
  }
  





  public void setStylesheet(String stylesheet)
  {
    try
    {
      _stylesheet = Resource.newResource(stylesheet);
      if (!_stylesheet.exists())
      {
        LOG.warn("unable to find custom stylesheet: " + stylesheet, new Object[0]);
        _stylesheet = null;
      }
    }
    catch (Exception e)
    {
      LOG.warn(e.toString(), new Object[0]);
      LOG.debug(e);
      throw new IllegalArgumentException(stylesheet);
    }
  }
  

  public void setWelcomeFiles(String[] welcomeFiles)
  {
    _welcomes = welcomeFiles;
  }
}
