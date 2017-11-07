package org.seleniumhq.jetty9.servlets;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import org.seleniumhq.jetty9.util.IO;
import org.seleniumhq.jetty9.util.StringUtil;
import org.seleniumhq.jetty9.util.URIUtil;


































public class PutFilter
  implements Filter
{
  public static final String __PUT = "PUT";
  public static final String __DELETE = "DELETE";
  public static final String __MOVE = "MOVE";
  public static final String __OPTIONS = "OPTIONS";
  Set<String> _operations = new HashSet();
  private ConcurrentMap<String, String> _hidden = new ConcurrentHashMap();
  private ServletContext _context;
  private String _baseURI;
  private boolean _delAllowed;
  private boolean _putAtomic;
  private File _tmpdir;
  
  public PutFilter() {}
  
  public void init(FilterConfig config)
    throws ServletException
  {
    _context = config.getServletContext();
    
    _tmpdir = ((File)_context.getAttribute("javax.servlet.context.tempdir"));
    
    if (_context.getRealPath("/") == null) {
      throw new UnavailableException("Packed war");
    }
    String b = config.getInitParameter("baseURI");
    if (b != null)
    {
      _baseURI = b;
    }
    else
    {
      File base = new File(_context.getRealPath("/"));
      _baseURI = base.toURI().toString();
    }
    
    _delAllowed = getInitBoolean(config, "delAllowed");
    _putAtomic = getInitBoolean(config, "putAtomic");
    
    _operations.add("OPTIONS");
    _operations.add("PUT");
    if (_delAllowed)
    {
      _operations.add("DELETE");
      _operations.add("MOVE");
    }
  }
  

  private boolean getInitBoolean(FilterConfig config, String name)
  {
    String value = config.getInitParameter(name);
    return (value != null) && (value.length() > 0) && ((value.startsWith("t")) || (value.startsWith("T")) || (value.startsWith("y")) || (value.startsWith("Y")) || (value.startsWith("1")));
  }
  
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
    throws IOException, ServletException
  {
    HttpServletRequest request = (HttpServletRequest)req;
    HttpServletResponse response = (HttpServletResponse)res;
    
    String servletPath = request.getServletPath();
    String pathInfo = request.getPathInfo();
    String pathInContext = URIUtil.addPaths(servletPath, pathInfo);
    
    String resource = URIUtil.addPaths(_baseURI, pathInContext);
    
    String method = request.getMethod();
    boolean op = _operations.contains(method);
    
    if (op)
    {
      File file = null;
      try
      {
        if (method.equals("OPTIONS")) {
          handleOptions(chain, request, response);
        }
        else {
          file = new File(new URI(resource));
          boolean exists = file.exists();
          if ((exists) && (!passConditionalHeaders(request, response, file))) {
            return;
          }
          if (method.equals("PUT")) {
            handlePut(request, response, pathInContext, file);
          } else if (method.equals("DELETE")) {
            handleDelete(request, response, pathInContext, file);
          } else if (method.equals("MOVE")) {
            handleMove(request, response, pathInContext, file);
          } else {
            throw new IllegalStateException();
          }
        }
      }
      catch (Exception e) {
        _context.log(e.toString(), e);
        response.sendError(500);
      }
    }
    else
    {
      if (isHidden(pathInContext)) {
        response.sendError(404);
      } else
        chain.doFilter(request, response);
      return;
    }
  }
  

  private boolean isHidden(String pathInContext)
  {
    return _hidden.containsKey(pathInContext);
  }
  


  public void destroy() {}
  

  public void handlePut(HttpServletRequest request, HttpServletResponse response, String pathInContext, File file)
    throws ServletException, IOException
  {
    boolean exists = file.exists();
    if (pathInContext.endsWith("/"))
    {
      if (!exists)
      {
        if (!file.mkdirs()) {
          response.sendError(403);
        }
        else {
          response.setStatus(201);
          response.flushBuffer();
        }
      }
      else
      {
        response.setStatus(200);
        response.flushBuffer();
      }
    }
    else
    {
      boolean ok = false;
      try
      {
        _hidden.put(pathInContext, pathInContext);
        File parent = file.getParentFile();
        parent.mkdirs();
        int toRead = request.getContentLength();
        InputStream in = request.getInputStream();
        
        OutputStream out;
        if (_putAtomic)
        {
          File tmp = File.createTempFile(file.getName(), null, _tmpdir);
          out = new FileOutputStream(tmp, false);Throwable localThrowable6 = null;
          try {
            if (toRead >= 0) {
              IO.copy(in, out, toRead);
            } else {
              IO.copy(in, out);
            }
          }
          catch (Throwable localThrowable1)
          {
            localThrowable6 = localThrowable1;throw localThrowable1;

          }
          finally
          {

            if (out != null) if (localThrowable6 != null) try { out.close(); } catch (Throwable localThrowable2) { localThrowable6.addSuppressed(localThrowable2); } else out.close();
          }
          if (!tmp.renameTo(file)) {
            throw new IOException("rename from " + tmp + " to " + file + " failed");
          }
        }
        else {
          OutputStream out = new FileOutputStream(file, false);out = null;
          try {
            if (toRead >= 0) {
              IO.copy(in, out, toRead);
            } else {
              IO.copy(in, out);
            }
          }
          catch (Throwable localThrowable8)
          {
            out = localThrowable8;throw localThrowable8;

          }
          finally
          {

            if (out != null) if (out != null) try { out.close(); } catch (Throwable localThrowable5) { out.addSuppressed(localThrowable5); } else out.close();
          }
        }
        response.setStatus(exists ? 200 : 201);
        response.flushBuffer();
        ok = true;
      }
      catch (Exception ex)
      {
        _context.log(ex.toString(), ex);
        response.sendError(403);
      }
      finally
      {
        if (!ok)
        {
          try
          {
            if (file.exists()) {
              file.delete();
            }
          }
          catch (Exception e) {
            _context.log(e.toString(), e);
          }
        }
        _hidden.remove(pathInContext);
      }
    }
  }
  

  public void handleDelete(HttpServletRequest request, HttpServletResponse response, String pathInContext, File file)
    throws ServletException, IOException
  {
    try
    {
      if (file.delete())
      {
        response.setStatus(204);
        response.flushBuffer();
      }
      else {
        response.sendError(403);
      }
    }
    catch (SecurityException sex) {
      _context.log(sex.toString(), sex);
      response.sendError(403);
    }
  }
  

  public void handleMove(HttpServletRequest request, HttpServletResponse response, String pathInContext, File file)
    throws ServletException, IOException, URISyntaxException
  {
    String newPath = URIUtil.canonicalPath(request.getHeader("new-uri"));
    if (newPath == null)
    {
      response.sendError(400);
      return;
    }
    
    String contextPath = request.getContextPath();
    if ((contextPath != null) && (!newPath.startsWith(contextPath)))
    {
      response.sendError(405);
      return;
    }
    String newInfo = newPath;
    if (contextPath != null) {
      newInfo = newInfo.substring(contextPath.length());
    }
    String new_resource = URIUtil.addPaths(_baseURI, newInfo);
    File new_file = new File(new URI(new_resource));
    
    file.renameTo(new_file);
    
    response.setStatus(204);
    response.flushBuffer();
  }
  
  public void handleOptions(FilterChain chain, HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    chain.doFilter(request, new HttpServletResponseWrapper(response)
    {

      public void setHeader(String name, String value)
      {
        if ("Allow".equalsIgnoreCase(name))
        {
          Set<String> options = new HashSet();
          options.addAll(Arrays.asList(StringUtil.csvSplit(value)));
          options.addAll(_operations);
          value = null;
          for (String o : options) {
            value = value + ", " + o;
          }
        }
        super.setHeader(name, value);
      }
    });
  }
  




  protected boolean passConditionalHeaders(HttpServletRequest request, HttpServletResponse response, File file)
    throws IOException
  {
    long date = 0L;
    
    if ((date = request.getDateHeader("if-unmodified-since")) > 0L)
    {
      if (file.lastModified() / 1000L > date / 1000L)
      {
        response.sendError(412);
        return false;
      }
    }
    
    if ((date = request.getDateHeader("if-modified-since")) > 0L)
    {
      if (file.lastModified() / 1000L <= date / 1000L)
      {
        response.reset();
        response.setStatus(304);
        response.flushBuffer();
        return false;
      }
    }
    return true;
  }
}
