package org.seleniumhq.jetty9.server.handler.gzip;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;
import java.util.zip.Deflater;
import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.seleniumhq.jetty9.http.CompressedContentFormat;
import org.seleniumhq.jetty9.http.HttpField;
import org.seleniumhq.jetty9.http.HttpFields;
import org.seleniumhq.jetty9.http.HttpHeader;
import org.seleniumhq.jetty9.http.HttpMethod;
import org.seleniumhq.jetty9.http.MimeTypes;
import org.seleniumhq.jetty9.http.pathmap.PathSpecSet;
import org.seleniumhq.jetty9.server.Handler;
import org.seleniumhq.jetty9.server.HttpChannel;
import org.seleniumhq.jetty9.server.HttpInput;
import org.seleniumhq.jetty9.server.HttpOutput;
import org.seleniumhq.jetty9.server.HttpOutput.Interceptor;
import org.seleniumhq.jetty9.server.Request;
import org.seleniumhq.jetty9.server.Response;
import org.seleniumhq.jetty9.server.handler.HandlerWrapper;
import org.seleniumhq.jetty9.util.IncludeExclude;
import org.seleniumhq.jetty9.util.RegexSet;
import org.seleniumhq.jetty9.util.StringUtil;
import org.seleniumhq.jetty9.util.URIUtil;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;























public class GzipHandler
  extends HandlerWrapper
  implements GzipFactory
{
  private static final Logger LOG = Log.getLogger(GzipHandler.class);
  
  public static final String GZIP = "gzip";
  public static final String DEFLATE = "deflate";
  public static final int DEFAULT_MIN_GZIP_SIZE = 16;
  private int _minGzipSize = 16;
  private int _compressionLevel = -1;
  private boolean _checkGzExists = true;
  private boolean _syncFlush = false;
  private int _inflateBufferSize = -1;
  private EnumSet<DispatcherType> _dispatchers = EnumSet.of(DispatcherType.REQUEST);
  

  private final ThreadLocal<Deflater> _deflater = new ThreadLocal();
  
  private final IncludeExclude<String> _agentPatterns = new IncludeExclude(RegexSet.class);
  private final IncludeExclude<String> _methods = new IncludeExclude();
  private final IncludeExclude<String> _paths = new IncludeExclude(PathSpecSet.class);
  private final IncludeExclude<String> _mimeTypes = new IncludeExclude();
  





  private HttpField _vary;
  





  public GzipHandler()
  {
    _methods.include(HttpMethod.GET.asString());
    for (String type : MimeTypes.getKnownMimeTypes())
    {
      if ("image/svg+xml".equals(type)) {
        _paths.exclude("*.svgz");
      } else if ((type.startsWith("image/")) || 
        (type.startsWith("audio/")) || 
        (type.startsWith("video/")))
        _mimeTypes.exclude(type);
    }
    _mimeTypes.exclude("application/compress");
    _mimeTypes.exclude("application/zip");
    _mimeTypes.exclude("application/gzip");
    _mimeTypes.exclude("application/bzip2");
    _mimeTypes.exclude("application/brotli");
    _mimeTypes.exclude("application/x-xz");
    _mimeTypes.exclude("application/x-rar-compressed");
    LOG.debug("{} mime types {}", new Object[] { this, _mimeTypes });
    
    _agentPatterns.exclude(".*MSIE 6.0.*");
  }
  




  public void addExcludedAgentPatterns(String... patterns)
  {
    _agentPatterns.exclude(patterns);
  }
  




  public void addExcludedMethods(String... methods)
  {
    for (String m : methods) {
      _methods.exclude(m);
    }
  }
  

  public EnumSet<DispatcherType> getDispatcherTypes()
  {
    return _dispatchers;
  }
  

  public void setDispatcherTypes(EnumSet<DispatcherType> dispatchers)
  {
    _dispatchers = dispatchers;
  }
  

  public void setDispatcherTypes(DispatcherType... dispatchers)
  {
    _dispatchers = EnumSet.copyOf(Arrays.asList(dispatchers));
  }
  







  public void addExcludedMimeTypes(String... types)
  {
    for (String t : types) {
      _mimeTypes.exclude(StringUtil.csvSplit(t));
    }
  }
  

























  public void addExcludedPaths(String... pathspecs)
  {
    for (String p : pathspecs) {
      _paths.exclude(StringUtil.csvSplit(p));
    }
  }
  



  public void addIncludedAgentPatterns(String... patterns)
  {
    _agentPatterns.include(patterns);
  }
  




  public void addIncludedMethods(String... methods)
  {
    for (String m : methods) {
      _methods.include(m);
    }
  }
  



  public boolean isSyncFlush()
  {
    return _syncFlush;
  }
  







  public void setSyncFlush(boolean syncFlush)
  {
    _syncFlush = syncFlush;
  }
  








  public void addIncludedMimeTypes(String... types)
  {
    for (String t : types) {
      _mimeTypes.include(StringUtil.csvSplit(t));
    }
  }
  























  public void addIncludedPaths(String... pathspecs)
  {
    for (String p : pathspecs) {
      _paths.include(StringUtil.csvSplit(p));
    }
  }
  
  protected void doStart()
    throws Exception
  {
    _vary = (_agentPatterns.size() > 0 ? GzipHttpOutputInterceptor.VARY_ACCEPT_ENCODING_USER_AGENT : GzipHttpOutputInterceptor.VARY_ACCEPT_ENCODING);
    super.doStart();
  }
  

  public boolean getCheckGzExists()
  {
    return _checkGzExists;
  }
  

  public int getCompressionLevel()
  {
    return _compressionLevel;
  }
  


  public Deflater getDeflater(Request request, long content_length)
  {
    String ua = request.getHttpFields().get(HttpHeader.USER_AGENT);
    if ((ua != null) && (!isAgentGzipable(ua)))
    {
      LOG.debug("{} excluded user agent {}", new Object[] { this, request });
      return null;
    }
    
    if ((content_length >= 0L) && (content_length < _minGzipSize))
    {
      LOG.debug("{} excluded minGzipSize {}", new Object[] { this, request });
      return null;
    }
    

    HttpField accept = request.getHttpFields().getField(HttpHeader.ACCEPT_ENCODING);
    
    if (accept == null)
    {
      LOG.debug("{} excluded !accept {}", new Object[] { this, request });
      return null;
    }
    boolean gzip = accept.contains("gzip");
    
    if (!gzip)
    {
      LOG.debug("{} excluded not gzip accept {}", new Object[] { this, request });
      return null;
    }
    
    Deflater df = (Deflater)_deflater.get();
    if (df == null) {
      df = new Deflater(_compressionLevel, true);
    } else {
      _deflater.set(null);
    }
    return df;
  }
  

  public String[] getExcludedAgentPatterns()
  {
    Set<String> excluded = _agentPatterns.getExcluded();
    return (String[])excluded.toArray(new String[excluded.size()]);
  }
  

  public String[] getExcludedMethods()
  {
    Set<String> excluded = _methods.getExcluded();
    return (String[])excluded.toArray(new String[excluded.size()]);
  }
  

  public String[] getExcludedMimeTypes()
  {
    Set<String> excluded = _mimeTypes.getExcluded();
    return (String[])excluded.toArray(new String[excluded.size()]);
  }
  

  public String[] getExcludedPaths()
  {
    Set<String> excluded = _paths.getExcluded();
    return (String[])excluded.toArray(new String[excluded.size()]);
  }
  

  public String[] getIncludedAgentPatterns()
  {
    Set<String> includes = _agentPatterns.getIncluded();
    return (String[])includes.toArray(new String[includes.size()]);
  }
  

  public String[] getIncludedMethods()
  {
    Set<String> includes = _methods.getIncluded();
    return (String[])includes.toArray(new String[includes.size()]);
  }
  

  public String[] getIncludedMimeTypes()
  {
    Set<String> includes = _mimeTypes.getIncluded();
    return (String[])includes.toArray(new String[includes.size()]);
  }
  

  public String[] getIncludedPaths()
  {
    Set<String> includes = _paths.getIncluded();
    return (String[])includes.toArray(new String[includes.size()]);
  }
  

  @Deprecated
  public String[] getMethods()
  {
    return getIncludedMethods();
  }
  






  public int getMinGzipSize()
  {
    return _minGzipSize;
  }
  

  protected HttpField getVaryField()
  {
    return _vary;
  }
  




  public int getInflateBufferSize()
  {
    return _inflateBufferSize;
  }
  




  public void setInflateBufferSize(int size)
  {
    _inflateBufferSize = size;
  }
  




  public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    ServletContext context = baseRequest.getServletContext();
    String path = context == null ? baseRequest.getRequestURI() : URIUtil.addPaths(baseRequest.getServletPath(), baseRequest.getPathInfo());
    LOG.debug("{} handle {} in {}", new Object[] { this, baseRequest, context });
    
    if (!_dispatchers.contains(baseRequest.getDispatcherType()))
    {
      LOG.debug("{} excluded by dispatcherType {}", new Object[] { this, baseRequest.getDispatcherType() });
      _handler.handle(target, baseRequest, request, response);
      return;
    }
    

    if (_inflateBufferSize > 0)
    {
      HttpField ce = baseRequest.getHttpFields().getField(HttpHeader.CONTENT_ENCODING);
      if ((ce != null) && ("gzip".equalsIgnoreCase(ce.getValue())))
      {

        baseRequest.getHttpFields().remove(HttpHeader.CONTENT_ENCODING);
        baseRequest.getHttpFields().add(new HttpField("X-Content-Encoding", ce.getValue()));
        baseRequest.getHttpInput().addInterceptor(new GzipHttpInputInterceptor(baseRequest.getHttpChannel().getByteBufferPool(), _inflateBufferSize));
      }
    }
    

    HttpOutput out = baseRequest.getResponse().getHttpOutput();
    HttpOutput.Interceptor interceptor = out.getInterceptor();
    while (interceptor != null)
    {
      if ((interceptor instanceof GzipHttpOutputInterceptor))
      {
        LOG.debug("{} already intercepting {}", new Object[] { this, request });
        _handler.handle(target, baseRequest, request, response);
        return;
      }
      interceptor = interceptor.getNextInterceptor();
    }
    

    if (!_methods.test(baseRequest.getMethod()))
    {
      LOG.debug("{} excluded by method {}", new Object[] { this, request });
      _handler.handle(target, baseRequest, request, response);
      return;
    }
    


    if (!isPathGzipable(path))
    {
      LOG.debug("{} excluded by path {}", new Object[] { this, request });
      _handler.handle(target, baseRequest, request, response);
      return;
    }
    

    String mimeType = context == null ? MimeTypes.getDefaultMimeByExtension(path) : context.getMimeType(path);
    if (mimeType != null)
    {
      mimeType = MimeTypes.getContentTypeWithoutCharset(mimeType);
      if (!isMimeTypeGzipable(mimeType))
      {
        LOG.debug("{} excluded by path suffix mime type {}", new Object[] { this, request });
        
        _handler.handle(target, baseRequest, request, response);
        return;
      }
    }
    
    if ((_checkGzExists) && (context != null))
    {
      String realpath = request.getServletContext().getRealPath(path);
      if (realpath != null)
      {
        File gz = new File(realpath + ".gz");
        if (gz.exists())
        {
          LOG.debug("{} gzip exists {}", new Object[] { this, request });
          
          _handler.handle(target, baseRequest, request, response);
          return;
        }
      }
    }
    

    String etag = baseRequest.getHttpFields().get(HttpHeader.IF_NONE_MATCH);
    if (etag != null)
    {
      int i = etag.indexOf(GZIP_etagQuote);
      if (i > 0)
      {
        baseRequest.setAttribute("o.e.j.s.h.gzip.GzipHandler.etag", etag);
        while (i >= 0)
        {
          etag = etag.substring(0, i) + etag.substring(i + GZIP_etag.length());
          i = etag.indexOf(GZIP_etagQuote, i);
        }
        baseRequest.getHttpFields().put(new HttpField(HttpHeader.IF_NONE_MATCH, etag));
      }
    }
    
    HttpOutput.Interceptor orig_interceptor = out.getInterceptor();
    
    try
    {
      out.setInterceptor(new GzipHttpOutputInterceptor(this, getVaryField(), baseRequest.getHttpChannel(), orig_interceptor, isSyncFlush()));
      
      if (_handler != null) {
        _handler.handle(target, baseRequest, request, response);
      }
    }
    finally
    {
      if ((!baseRequest.isHandled()) && (!baseRequest.isAsyncStarted())) {
        out.setInterceptor(orig_interceptor);
      }
    }
  }
  






  protected boolean isAgentGzipable(String ua)
  {
    if (ua == null) {
      return false;
    }
    return _agentPatterns.test(ua);
  }
  


  public boolean isMimeTypeGzipable(String mimetype)
  {
    return _mimeTypes.test(mimetype);
  }
  








  protected boolean isPathGzipable(String requestURI)
  {
    if (requestURI == null) {
      return true;
    }
    return _paths.test(requestURI);
  }
  


  public void recycle(Deflater deflater)
  {
    deflater.reset();
    if (_deflater.get() == null) {
      _deflater.set(deflater);
    }
  }
  




  public void setCheckGzExists(boolean checkGzExists)
  {
    _checkGzExists = checkGzExists;
  }
  




  public void setCompressionLevel(int compressionLevel)
  {
    _compressionLevel = compressionLevel;
  }
  




  public void setExcludedAgentPatterns(String... patterns)
  {
    _agentPatterns.getExcluded().clear();
    addExcludedAgentPatterns(patterns);
  }
  




  public void setExcludedMethods(String... method)
  {
    _methods.getExcluded().clear();
    _methods.exclude(method);
  }
  





  public void setExcludedMimeTypes(String... types)
  {
    _mimeTypes.getExcluded().clear();
    _mimeTypes.exclude(types);
  }
  






  public void setExcludedPaths(String... pathspecs)
  {
    _paths.getExcluded().clear();
    _paths.exclude(pathspecs);
  }
  




  public void setIncludedAgentPatterns(String... patterns)
  {
    _agentPatterns.getIncluded().clear();
    addIncludedAgentPatterns(patterns);
  }
  




  public void setIncludedMethods(String... methods)
  {
    _methods.getIncluded().clear();
    _methods.include(methods);
  }
  






  public void setIncludedMimeTypes(String... types)
  {
    _mimeTypes.getIncluded().clear();
    _mimeTypes.include(types);
  }
  







  public void setIncludedPaths(String... pathspecs)
  {
    _paths.getIncluded().clear();
    _paths.include(pathspecs);
  }
  






  public void setMinGzipSize(int minGzipSize)
  {
    _minGzipSize = minGzipSize;
  }
}
