package org.seleniumhq.jetty9.servlet;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.seleniumhq.jetty9.http.CompressedContentFormat;
import org.seleniumhq.jetty9.http.HttpContent.ContentFactory;
import org.seleniumhq.jetty9.http.HttpHeader;
import org.seleniumhq.jetty9.http.MimeTypes;
import org.seleniumhq.jetty9.http.PreEncodedHttpField;
import org.seleniumhq.jetty9.http.pathmap.MappedResource;
import org.seleniumhq.jetty9.http.pathmap.PathSpec;
import org.seleniumhq.jetty9.server.CachedContentFactory;
import org.seleniumhq.jetty9.server.ResourceContentFactory;
import org.seleniumhq.jetty9.server.ResourceService;
import org.seleniumhq.jetty9.server.ResourceService.WelcomeFactory;
import org.seleniumhq.jetty9.server.handler.ContextHandler;
import org.seleniumhq.jetty9.server.handler.ContextHandler.Context;
import org.seleniumhq.jetty9.util.URIUtil;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;
import org.seleniumhq.jetty9.util.resource.Resource;
import org.seleniumhq.jetty9.util.resource.ResourceFactory;
































































































public class DefaultServlet
  extends HttpServlet
  implements ResourceFactory, ResourceService.WelcomeFactory
{
  private static final Logger LOG = Log.getLogger(DefaultServlet.class);
  
  private static final long serialVersionUID = 4930458713846881193L;
  
  private final ResourceService _resourceService;
  
  private ServletContext _servletContext;
  private ContextHandler _contextHandler;
  private boolean _welcomeServlets = false;
  private boolean _welcomeExactServlets = false;
  
  private Resource _resourceBase;
  
  private CachedContentFactory _cache;
  private MimeTypes _mimeTypes;
  private String[] _welcomes;
  private Resource _stylesheet;
  private boolean _useFileMappedBuffer = false;
  
  private String _relativeResourceBase;
  private ServletHandler _servletHandler;
  private ServletHolder _defaultHolder;
  
  public DefaultServlet(ResourceService resourceService)
  {
    _resourceService = resourceService;
  }
  

  public DefaultServlet()
  {
    this(new ResourceService());
  }
  


  public void init()
    throws UnavailableException
  {
    _servletContext = getServletContext();
    _contextHandler = initContextHandler(_servletContext);
    
    _mimeTypes = _contextHandler.getMimeTypes();
    
    _welcomes = _contextHandler.getWelcomeFiles();
    if (_welcomes == null) {
      _welcomes = new String[] { "index.html", "index.jsp" };
    }
    _resourceService.setAcceptRanges(getInitBoolean("acceptRanges", _resourceService.isAcceptRanges()));
    _resourceService.setDirAllowed(getInitBoolean("dirAllowed", _resourceService.isDirAllowed()));
    _resourceService.setRedirectWelcome(getInitBoolean("redirectWelcome", _resourceService.isRedirectWelcome()));
    _resourceService.setPrecompressedFormats(parsePrecompressedFormats(getInitParameter("precompressed"), getInitBoolean("gzip", false)));
    _resourceService.setPathInfoOnly(getInitBoolean("pathInfoOnly", _resourceService.isPathInfoOnly()));
    _resourceService.setEtags(getInitBoolean("etags", _resourceService.isEtags()));
    
    if ("exact".equals(getInitParameter("welcomeServlets")))
    {
      _welcomeExactServlets = true;
      _welcomeServlets = false;
    }
    else {
      _welcomeServlets = getInitBoolean("welcomeServlets", _welcomeServlets);
    }
    _useFileMappedBuffer = getInitBoolean("useFileMappedBuffer", _useFileMappedBuffer);
    
    _relativeResourceBase = getInitParameter("relativeResourceBase");
    
    String rb = getInitParameter("resourceBase");
    if (rb != null)
    {
      if (_relativeResourceBase != null)
        throw new UnavailableException("resourceBase & relativeResourceBase");
      try { _resourceBase = _contextHandler.newResource(rb);
      }
      catch (Exception e) {
        LOG.warn("EXCEPTION ", e);
        throw new UnavailableException(e.toString());
      }
    }
    
    String css = getInitParameter("stylesheet");
    try
    {
      if (css != null)
      {
        _stylesheet = Resource.newResource(css);
        if (!_stylesheet.exists())
        {
          LOG.warn("!" + css, new Object[0]);
          _stylesheet = null;
        }
      }
      if (_stylesheet == null)
      {
        _stylesheet = Resource.newResource(getClass().getResource("/jetty-dir.css"));
      }
    }
    catch (Exception e)
    {
      LOG.warn(e.toString(), new Object[0]);
      LOG.debug(e);
    }
    
    int encodingHeaderCacheSize = getInitInt("encodingHeaderCacheSize", -1);
    if (encodingHeaderCacheSize >= 0) {
      _resourceService.setEncodingCacheSize(encodingHeaderCacheSize);
    }
    String cc = getInitParameter("cacheControl");
    if (cc != null) {
      _resourceService.setCacheControl(new PreEncodedHttpField(HttpHeader.CACHE_CONTROL, cc));
    }
    
    String resourceCache = getInitParameter("resourceCache");
    int max_cache_size = getInitInt("maxCacheSize", -2);
    int max_cached_file_size = getInitInt("maxCachedFileSize", -2);
    int max_cached_files = getInitInt("maxCachedFiles", -2);
    if (resourceCache != null)
    {
      if ((max_cache_size != -1) || (max_cached_file_size != -2) || (max_cached_files != -2))
        LOG.debug("ignoring resource cache configuration, using resourceCache attribute", new Object[0]);
      if ((_relativeResourceBase != null) || (_resourceBase != null))
        throw new UnavailableException("resourceCache specified with resource bases");
      _cache = ((CachedContentFactory)_servletContext.getAttribute(resourceCache));
    }
    
    try
    {
      if ((_cache == null) && ((max_cached_files != -2) || (max_cache_size != -2) || (max_cached_file_size != -2)))
      {
        _cache = new CachedContentFactory(null, this, _mimeTypes, _useFileMappedBuffer, _resourceService.isEtags(), _resourceService.getPrecompressedFormats());
        if (max_cache_size >= 0)
          _cache.setMaxCacheSize(max_cache_size);
        if (max_cached_file_size >= -1)
          _cache.setMaxCachedFileSize(max_cached_file_size);
        if (max_cached_files >= -1)
          _cache.setMaxCachedFiles(max_cached_files);
        _servletContext.setAttribute(resourceCache == null ? "resourceCache" : resourceCache, _cache);
      }
    }
    catch (Exception e)
    {
      LOG.warn("EXCEPTION ", e);
      throw new UnavailableException(e.toString());
    }
    
    HttpContent.ContentFactory contentFactory = _cache;
    if (contentFactory == null)
    {
      contentFactory = new ResourceContentFactory(this, _mimeTypes, _resourceService.getPrecompressedFormats());
      if (resourceCache != null)
        _servletContext.setAttribute(resourceCache, contentFactory);
    }
    _resourceService.setContentFactory(contentFactory);
    _resourceService.setWelcomeFactory(this);
    
    List<String> gzip_equivalent_file_extensions = new ArrayList();
    String otherGzipExtensions = getInitParameter("otherGzipFileExtensions");
    if (otherGzipExtensions != null)
    {

      tok = new StringTokenizer(otherGzipExtensions, ",", false);
      while (tok.hasMoreTokens())
      {
        s = tok.nextToken().trim();
        gzip_equivalent_file_extensions.add("." + s);
      }
      
    }
    else
    {
      gzip_equivalent_file_extensions.add(".svgz");
    }
    _resourceService.setGzipEquivalentFileExtensions(gzip_equivalent_file_extensions);
    

    _servletHandler = ((ServletHandler)_contextHandler.getChildHandlerByClass(ServletHandler.class));
    StringTokenizer tok = _servletHandler.getServlets();String s = tok.length; for (String str1 = 0; str1 < s; str1++) { ServletHolder h = tok[str1];
      if (h.getServletInstance() == this)
        _defaultHolder = h;
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("resource base = " + _resourceBase, new Object[0]);
    }
  }
  
  private CompressedContentFormat[] parsePrecompressedFormats(String precompressed, boolean gzip) {
    List<CompressedContentFormat> ret = new ArrayList();
    if ((precompressed != null) && (precompressed.indexOf('=') > 0))
    {
      for (String pair : precompressed.split(","))
      {
        String[] setting = pair.split("=");
        String encoding = setting[0].trim();
        String extension = setting[1].trim();
        ret.add(new CompressedContentFormat(encoding, extension));
        if ((gzip) && (!ret.contains(CompressedContentFormat.GZIP))) {
          ret.add(CompressedContentFormat.GZIP);
        }
      }
    } else if (precompressed != null)
    {
      if (Boolean.parseBoolean(precompressed))
      {
        ret.add(CompressedContentFormat.BR);
        ret.add(CompressedContentFormat.GZIP);
      }
    }
    else if (gzip)
    {

      ret.add(CompressedContentFormat.GZIP);
    }
    return (CompressedContentFormat[])ret.toArray(new CompressedContentFormat[ret.size()]);
  }
  








  protected ContextHandler initContextHandler(ServletContext servletContext)
  {
    ContextHandler.Context scontext = ContextHandler.getCurrentContext();
    if (scontext == null)
    {
      if ((servletContext instanceof ContextHandler.Context)) {
        return ((ContextHandler.Context)servletContext).getContextHandler();
      }
      
      throw new IllegalArgumentException("The servletContext " + servletContext + " " + servletContext.getClass().getName() + " is not " + ContextHandler.Context.class.getName());
    }
    
    return ContextHandler.getCurrentContext().getContextHandler();
  }
  


  public String getInitParameter(String name)
  {
    String value = getServletContext().getInitParameter("org.seleniumhq.jetty9.servlet.Default." + name);
    if (value == null)
      value = super.getInitParameter(name);
    return value;
  }
  

  private boolean getInitBoolean(String name, boolean dft)
  {
    String value = getInitParameter(name);
    if ((value == null) || (value.length() == 0))
      return dft;
    return (value.startsWith("t")) || 
      (value.startsWith("T")) || 
      (value.startsWith("y")) || 
      (value.startsWith("Y")) || 
      (value.startsWith("1"));
  }
  

  private int getInitInt(String name, int dft)
  {
    String value = getInitParameter(name);
    if (value == null)
      value = getInitParameter(name);
    if ((value != null) && (value.length() > 0))
      return Integer.parseInt(value);
    return dft;
  }
  









  public Resource getResource(String pathInContext)
  {
    Resource r = null;
    if (_relativeResourceBase != null) {
      pathInContext = URIUtil.addPaths(_relativeResourceBase, pathInContext);
    }
    try
    {
      if (_resourceBase != null)
      {
        r = _resourceBase.addPath(pathInContext);
        if (!_contextHandler.checkAlias(pathInContext, r)) {
          r = null;
        }
      } else if ((_servletContext instanceof ContextHandler.Context))
      {
        r = _contextHandler.getResource(pathInContext);
      }
      else
      {
        URL u = _servletContext.getResource(pathInContext);
        r = _contextHandler.newResource(u);
      }
      
      if (LOG.isDebugEnabled()) {
        LOG.debug("Resource " + pathInContext + "=" + r, new Object[0]);
      }
    }
    catch (IOException e) {
      LOG.ignore(e);
    }
    
    if (((r == null) || (!r.exists())) && (pathInContext.endsWith("/jetty-dir.css"))) {
      r = _stylesheet;
    }
    return r;
  }
  


  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    _resourceService.doGet(request, response);
  }
  


  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    doGet(request, response);
  }
  




  protected void doTrace(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException
  {
    resp.sendError(405);
  }
  


  protected void doOptions(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException
  {
    resp.setHeader("Allow", "GET,HEAD,POST,OPTIONS");
  }
  





  public void destroy()
  {
    if (_cache != null)
      _cache.flushCache();
    super.destroy();
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
      if ((welcome != null) && (welcome.exists())) {
        return _welcomes[i];
      }
      if (((_welcomeServlets) || (_welcomeExactServlets)) && (welcome_servlet == null))
      {
        MappedResource<ServletHolder> entry = _servletHandler.getHolderEntry(welcome_in_context);
        if ((entry != null) && (entry.getResource() != _defaultHolder) && ((_welcomeServlets) || ((_welcomeExactServlets) && 
          (entry.getPathSpec().getDeclaration().equals(welcome_in_context))))) {
          welcome_servlet = welcome_in_context;
        }
      }
    }
    return welcome_servlet;
  }
}
