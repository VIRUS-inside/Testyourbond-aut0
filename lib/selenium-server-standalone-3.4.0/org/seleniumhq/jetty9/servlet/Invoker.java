package org.seleniumhq.jetty9.servlet;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import org.seleniumhq.jetty9.http.pathmap.MappedResource;
import org.seleniumhq.jetty9.server.Handler;
import org.seleniumhq.jetty9.server.Request;
import org.seleniumhq.jetty9.server.handler.ContextHandler;
import org.seleniumhq.jetty9.server.handler.ContextHandler.Context;
import org.seleniumhq.jetty9.server.handler.HandlerWrapper;
import org.seleniumhq.jetty9.util.ArrayUtil;
import org.seleniumhq.jetty9.util.URIUtil;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;











































public class Invoker
  extends HttpServlet
{
  private static final Logger LOG = Log.getLogger(Invoker.class);
  
  private ContextHandler _contextHandler;
  private ServletHandler _servletHandler;
  private MappedResource<ServletHolder> _invokerEntry;
  private Map<String, String> _parameters;
  private boolean _nonContextServlets;
  private boolean _verbose;
  
  public Invoker() {}
  
  public void init()
  {
    ServletContext config = getServletContext();
    _contextHandler = ((ContextHandler.Context)config).getContextHandler();
    
    Handler handler = _contextHandler.getHandler();
    while ((handler != null) && (!(handler instanceof ServletHandler)) && ((handler instanceof HandlerWrapper)))
      handler = ((HandlerWrapper)handler).getHandler();
    _servletHandler = ((ServletHandler)handler);
    Enumeration<String> e = getInitParameterNames();
    while (e.hasMoreElements())
    {
      String param = (String)e.nextElement();
      String value = getInitParameter(param);
      String lvalue = value.toLowerCase(Locale.ENGLISH);
      if ("nonContextServlets".equals(param))
      {
        _nonContextServlets = ((value.length() > 0) && (lvalue.startsWith("t")));
      }
      if ("verbose".equals(param))
      {
        _verbose = ((value.length() > 0) && (lvalue.startsWith("t")));
      }
      else
      {
        if (_parameters == null)
          _parameters = new HashMap();
        _parameters.put(param, value);
      }
    }
  }
  


  protected void service(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    boolean included = false;
    String servlet_path = (String)request.getAttribute("javax.servlet.include.servlet_path");
    if (servlet_path == null) {
      servlet_path = request.getServletPath();
    } else
      included = true;
    String path_info = (String)request.getAttribute("javax.servlet.include.path_info");
    if (path_info == null) {
      path_info = request.getPathInfo();
    }
    
    String servlet = path_info;
    if ((servlet == null) || (servlet.length() <= 1))
    {
      response.sendError(404);
      return;
    }
    

    int i0 = servlet.charAt(0) == '/' ? 1 : 0;
    int i1 = servlet.indexOf('/', i0);
    servlet = i1 < 0 ? servlet.substring(i0) : servlet.substring(i0, i1);
    

    ServletHolder[] holders = _servletHandler.getServlets();
    ServletHolder holder = getHolder(holders, servlet);
    
    if (holder != null)
    {


      if (LOG.isDebugEnabled())
        LOG.debug("Adding servlet mapping for named servlet:" + servlet + ":" + URIUtil.addPaths(servlet_path, servlet) + "/*", new Object[0]);
      ServletMapping mapping = new ServletMapping();
      mapping.setServletName(servlet);
      mapping.setPathSpec(URIUtil.addPaths(servlet_path, servlet) + "/*");
      _servletHandler.setServletMappings((ServletMapping[])ArrayUtil.addToArray(_servletHandler.getServletMappings(), mapping, ServletMapping.class));

    }
    else
    {
      if (servlet.endsWith(".class"))
        servlet = servlet.substring(0, servlet.length() - 6);
      if ((servlet == null) || (servlet.length() == 0))
      {
        response.sendError(404);
        return;
      }
      
      synchronized (_servletHandler)
      {

        _invokerEntry = _servletHandler.getHolderEntry(servlet_path);
        

        String path = URIUtil.addPaths(servlet_path, servlet);
        MappedResource<ServletHolder> entry = _servletHandler.getHolderEntry(path);
        
        if ((entry != null) && (!entry.equals(_invokerEntry)))
        {

          holder = (ServletHolder)entry.getResource();

        }
        else
        {
          if (LOG.isDebugEnabled())
            LOG.debug("Making new servlet=" + servlet + " with path=" + path + "/*", new Object[0]);
          holder = _servletHandler.addServletWithMapping(servlet, path + "/*");
          
          if (_parameters != null)
            holder.setInitParameters(_parameters);
          try {
            holder.start();
          }
          catch (Exception e) {
            LOG.debug(e);
            throw new UnavailableException(e.toString());
          }
          

          if (!_nonContextServlets)
          {
            Object s = holder.getServlet();
            

            if (_contextHandler.getClassLoader() != s.getClass().getClassLoader())
            {
              try
              {
                holder.stop();
              }
              catch (Exception e)
              {
                LOG.ignore(e);
              }
              
              LOG.warn("Dynamic servlet " + s + " not loaded from context " + request
              
                .getContextPath(), new Object[0]);
              throw new UnavailableException("Not in context");
            }
          }
          
          if ((_verbose) && (LOG.isDebugEnabled())) {
            LOG.debug("Dynamic load '" + servlet + "' at " + path, new Object[0]);
          }
        }
      }
    }
    if (holder != null)
    {
      Request baseRequest = Request.getBaseRequest(request);
      holder.handle(baseRequest, new InvokedRequest(request, included, servlet, servlet_path, path_info), response);

    }
    else
    {

      LOG.info("Can't find holder for servlet: " + servlet, new Object[0]);
      response.sendError(404);
    }
  }
  


  class InvokedRequest
    extends HttpServletRequestWrapper
  {
    String _servletPath;
    

    String _pathInfo;
    

    boolean _included;
    

    InvokedRequest(HttpServletRequest request, boolean included, String name, String servletPath, String pathInfo)
    {
      super();
      _included = included;
      _servletPath = URIUtil.addPaths(servletPath, name);
      _pathInfo = pathInfo.substring(name.length() + 1);
      if (_pathInfo.length() == 0) {
        _pathInfo = null;
      }
    }
    
    public String getServletPath()
    {
      if (_included)
        return super.getServletPath();
      return _servletPath;
    }
    

    public String getPathInfo()
    {
      if (_included)
        return super.getPathInfo();
      return _pathInfo;
    }
    

    public Object getAttribute(String name)
    {
      if (_included)
      {
        if (name.equals("javax.servlet.include.request_uri"))
          return URIUtil.addPaths(URIUtil.addPaths(getContextPath(), _servletPath), _pathInfo);
        if (name.equals("javax.servlet.include.path_info"))
          return _pathInfo;
        if (name.equals("javax.servlet.include.servlet_path"))
          return _servletPath;
      }
      return super.getAttribute(name);
    }
  }
  

  private ServletHolder getHolder(ServletHolder[] holders, String servlet)
  {
    if (holders == null) {
      return null;
    }
    ServletHolder holder = null;
    for (int i = 0; (holder == null) && (i < holders.length); i++)
    {
      if (holders[i].getName().equals(servlet))
      {
        holder = holders[i];
      }
    }
    return holder;
  }
}
