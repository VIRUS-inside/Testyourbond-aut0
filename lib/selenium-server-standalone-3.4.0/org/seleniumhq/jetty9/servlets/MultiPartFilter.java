package org.seleniumhq.jetty9.servlets;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.Part;
import org.seleniumhq.jetty9.http.MimeTypes;
import org.seleniumhq.jetty9.util.IO;
import org.seleniumhq.jetty9.util.LazyList;
import org.seleniumhq.jetty9.util.MultiMap;
import org.seleniumhq.jetty9.util.MultiPartInputStreamParser;
import org.seleniumhq.jetty9.util.MultiPartInputStreamParser.MultiPart;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;



























































@Deprecated
public class MultiPartFilter
  implements Filter
{
  private static final Logger LOG = Log.getLogger(MultiPartFilter.class);
  public static final String CONTENT_TYPE_SUFFIX = ".org.eclipse.jetty.servlet.contentType";
  private static final String MULTIPART = "org.seleniumhq.jetty9.servlet.MultiPartFile.multiPartInputStream";
  private File tempdir;
  private boolean _deleteFiles;
  private ServletContext _context;
  private int _fileOutputBuffer = 0;
  private boolean _writeFilesWithFilenames = false;
  private long _maxFileSize = -1L;
  private long _maxRequestSize = -1L;
  private int _maxFormKeys = Integer.getInteger("org.seleniumhq.jetty9.server.Request.maxFormKeys", 1000).intValue();
  

  public MultiPartFilter() {}
  
  public void init(FilterConfig filterConfig)
    throws ServletException
  {
    tempdir = ((File)filterConfig.getServletContext().getAttribute("javax.servlet.context.tempdir"));
    _deleteFiles = "true".equals(filterConfig.getInitParameter("deleteFiles"));
    String fileOutputBuffer = filterConfig.getInitParameter("fileOutputBuffer");
    if (fileOutputBuffer != null)
      _fileOutputBuffer = Integer.parseInt(fileOutputBuffer);
    String maxFileSize = filterConfig.getInitParameter("maxFileSize");
    if (maxFileSize != null)
      _maxFileSize = Long.parseLong(maxFileSize.trim());
    String maxRequestSize = filterConfig.getInitParameter("maxRequestSize");
    if (maxRequestSize != null) {
      _maxRequestSize = Long.parseLong(maxRequestSize.trim());
    }
    _context = filterConfig.getServletContext();
    String mfks = filterConfig.getInitParameter("maxFormKeys");
    if (mfks != null)
      _maxFormKeys = Integer.parseInt(mfks);
    _writeFilesWithFilenames = "true".equalsIgnoreCase(filterConfig.getInitParameter("writeFilesWithFilenames"));
  }
  





  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
    throws IOException, ServletException
  {
    HttpServletRequest srequest = (HttpServletRequest)request;
    if ((srequest.getContentType() == null) || (!srequest.getContentType().startsWith("multipart/form-data")))
    {
      chain.doFilter(request, response);
      return;
    }
    
    String content_type = srequest.getContentType();
    

    MultiMap params = new MultiMap();
    for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet())
    {
      Object value = entry.getValue();
      if ((value instanceof String[])) {
        params.addValues((String)entry.getKey(), (String[])value);
      } else {
        params.add((String)entry.getKey(), value);
      }
    }
    MultipartConfigElement config = new MultipartConfigElement(tempdir.getCanonicalPath(), _maxFileSize, _maxRequestSize, _fileOutputBuffer);
    MultiPartInputStreamParser mpis = new MultiPartInputStreamParser(request.getInputStream(), content_type, config, tempdir);
    mpis.setDeleteOnExit(_deleteFiles);
    mpis.setWriteFilesWithFilenames(_writeFilesWithFilenames);
    request.setAttribute("org.seleniumhq.jetty9.servlet.MultiPartFile.multiPartInputStream", mpis);
    try
    {
      Collection<Part> parts = mpis.getParts();
      if (parts != null)
      {
        Iterator<Part> itor = parts.iterator();
        while ((itor.hasNext()) && (params.size() < _maxFormKeys))
        {
          Part p = (Part)itor.next();
          if (LOG.isDebugEnabled())
            LOG.debug("{}", new Object[] { p });
          MultiPartInputStreamParser.MultiPart mp = (MultiPartInputStreamParser.MultiPart)p;
          if (mp.getFile() != null)
          {
            request.setAttribute(mp.getName(), mp.getFile());
            if (mp.getContentDispositionFilename() != null)
            {
              params.add(mp.getName(), mp.getContentDispositionFilename());
              if (mp.getContentType() != null) {
                params.add(mp.getName() + ".org.eclipse.jetty.servlet.contentType", mp.getContentType());
              }
            }
          }
          else {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            IO.copy(p.getInputStream(), bytes);
            params.add(p.getName(), bytes.toByteArray());
            if (p.getContentType() != null) {
              params.add(p.getName() + ".org.eclipse.jetty.servlet.contentType", p.getContentType());
            }
          }
        }
      }
      
      chain.doFilter(new Wrapper(srequest, params), response);
    }
    finally
    {
      deleteFiles(request);
    }
  }
  


  private void deleteFiles(ServletRequest request)
  {
    if (!_deleteFiles) {
      return;
    }
    MultiPartInputStreamParser mpis = (MultiPartInputStreamParser)request.getAttribute("org.seleniumhq.jetty9.servlet.MultiPartFile.multiPartInputStream");
    if (mpis != null)
    {
      try
      {
        mpis.deleteParts();
      }
      catch (Exception e)
      {
        _context.log("Error deleting multipart tmp files", e);
      }
    }
    request.removeAttribute("org.seleniumhq.jetty9.servlet.MultiPartFile.multiPartInputStream");
  }
  




  public void destroy() {}
  



  private static class Wrapper
    extends HttpServletRequestWrapper
  {
    Charset _encoding = StandardCharsets.UTF_8;
    
    MultiMap<Object> _params;
    
    public Wrapper(HttpServletRequest request, MultiMap map)
    {
      super();
      _params = map;
    }
    





    public int getContentLength()
    {
      return 0;
    }
    





    public String getParameter(String name)
    {
      Object o = _params.get(name);
      if ((!(o instanceof byte[])) && (LazyList.size(o) > 0)) {
        o = LazyList.get(o, 0);
      }
      if ((o instanceof byte[]))
      {
        try
        {
          return getParameterBytesAsString(name, (byte[])o);
        }
        catch (Exception e)
        {
          MultiPartFilter.LOG.warn(e);
        }
        
      } else if (o != null)
        return String.valueOf(o);
      return null;
    }
    





    public Map<String, String[]> getParameterMap()
    {
      Map<String, String[]> cmap = new HashMap();
      
      for (Object key : _params.keySet())
      {
        cmap.put((String)key, getParameterValues((String)key));
      }
      
      return Collections.unmodifiableMap(cmap);
    }
    





    public Enumeration<String> getParameterNames()
    {
      return Collections.enumeration(_params.keySet());
    }
    





    public String[] getParameterValues(String name)
    {
      List l = _params.getValues(name);
      if ((l == null) || (l.size() == 0))
        return new String[0];
      String[] v = new String[l.size()];
      for (int i = 0; i < l.size(); i++)
      {
        Object o = l.get(i);
        if ((o instanceof byte[]))
        {
          try
          {
            v[i] = getParameterBytesAsString(name, (byte[])(byte[])o);
          }
          catch (Exception e)
          {
            throw new RuntimeException(e);
          }
          
        } else if ((o instanceof String))
          v[i] = ((String)o);
      }
      return v;
    }
    





    public void setCharacterEncoding(String enc)
      throws UnsupportedEncodingException
    {
      try
      {
        _encoding = Charset.forName(enc);
      }
      catch (UnsupportedCharsetException e)
      {
        throw new UnsupportedEncodingException(e.getMessage());
      }
    }
    



    private String getParameterBytesAsString(String name, byte[] bytes)
      throws UnsupportedEncodingException
    {
      Object ct = _params.getValue(name + ".org.eclipse.jetty.servlet.contentType", 0);
      
      Charset contentType = _encoding;
      if (ct != null)
      {
        String tmp = MimeTypes.getCharsetFromContentType((String)ct);
        try
        {
          contentType = tmp == null ? _encoding : Charset.forName(tmp);
        }
        catch (UnsupportedCharsetException e)
        {
          throw new UnsupportedEncodingException(e.getMessage());
        }
      }
      
      return new String(bytes, contentType);
    }
  }
}
