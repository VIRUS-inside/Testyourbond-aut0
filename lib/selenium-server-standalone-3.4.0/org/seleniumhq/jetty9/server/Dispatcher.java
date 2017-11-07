package org.seleniumhq.jetty9.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.seleniumhq.jetty9.http.HttpURI;
import org.seleniumhq.jetty9.server.handler.ContextHandler;
import org.seleniumhq.jetty9.util.Attributes;
import org.seleniumhq.jetty9.util.MultiMap;























public class Dispatcher
  implements RequestDispatcher
{
  public static final String __ERROR_DISPATCH = "org.seleniumhq.jetty9.server.Dispatcher.ERROR";
  public static final String __INCLUDE_PREFIX = "javax.servlet.include.";
  public static final String __FORWARD_PREFIX = "javax.servlet.forward.";
  private final ContextHandler _contextHandler;
  private final HttpURI _uri;
  private final String _pathInContext;
  private final String _named;
  
  public Dispatcher(ContextHandler contextHandler, HttpURI uri, String pathInContext)
  {
    _contextHandler = contextHandler;
    _uri = uri;
    _pathInContext = pathInContext;
    _named = null;
  }
  
  public Dispatcher(ContextHandler contextHandler, String name) throws IllegalStateException
  {
    _contextHandler = contextHandler;
    _uri = null;
    _pathInContext = null;
    _named = name;
  }
  
  public void forward(ServletRequest request, ServletResponse response)
    throws ServletException, IOException
  {
    forward(request, response, DispatcherType.FORWARD);
  }
  
  public void error(ServletRequest request, ServletResponse response) throws ServletException, IOException
  {
    try
    {
      request.setAttribute("org.seleniumhq.jetty9.server.Dispatcher.ERROR", Boolean.TRUE);
      forward(request, response, DispatcherType.ERROR);
      


      request.setAttribute("org.seleniumhq.jetty9.server.Dispatcher.ERROR", null); } finally { request.setAttribute("org.seleniumhq.jetty9.server.Dispatcher.ERROR", null);
    }
  }
  
  public void include(ServletRequest request, ServletResponse response)
    throws ServletException, IOException
  {
    Request baseRequest = Request.getBaseRequest(request);
    
    if (!(request instanceof HttpServletRequest))
      request = new ServletRequestHttpWrapper(request);
    if (!(response instanceof HttpServletResponse)) {
      response = new ServletResponseHttpWrapper(response);
    }
    DispatcherType old_type = baseRequest.getDispatcherType();
    Attributes old_attr = baseRequest.getAttributes();
    MultiMap<String> old_query_params = baseRequest.getQueryParameters();
    try
    {
      baseRequest.setDispatcherType(DispatcherType.INCLUDE);
      baseRequest.getResponse().include();
      if (_named != null)
      {
        _contextHandler.handle(_named, baseRequest, (HttpServletRequest)request, (HttpServletResponse)response);
      }
      else
      {
        IncludeAttributes attr = new IncludeAttributes(old_attr);
        
        _requestURI = _uri.getPath();
        _contextPath = _contextHandler.getContextPath();
        _servletPath = null;
        _pathInfo = _pathInContext;
        _query = _uri.getQuery();
        
        if (_query != null)
          baseRequest.mergeQueryParameters(baseRequest.getQueryString(), _query, false);
        baseRequest.setAttributes(attr);
        
        _contextHandler.handle(_pathInContext, baseRequest, (HttpServletRequest)request, (HttpServletResponse)response);
      }
    }
    finally
    {
      baseRequest.setAttributes(old_attr);
      baseRequest.getResponse().included();
      baseRequest.setQueryParameters(old_query_params);
      baseRequest.resetParameters();
      baseRequest.setDispatcherType(old_type);
    }
  }
  
  protected void forward(ServletRequest request, ServletResponse response, DispatcherType dispatch) throws ServletException, IOException
  {
    Request baseRequest = Request.getBaseRequest(request);
    Response base_response = baseRequest.getResponse();
    base_response.resetForForward();
    
    if (!(request instanceof HttpServletRequest))
      request = new ServletRequestHttpWrapper(request);
    if (!(response instanceof HttpServletResponse)) {
      response = new ServletResponseHttpWrapper(response);
    }
    HttpURI old_uri = baseRequest.getHttpURI();
    String old_context_path = baseRequest.getContextPath();
    String old_servlet_path = baseRequest.getServletPath();
    String old_path_info = baseRequest.getPathInfo();
    
    MultiMap<String> old_query_params = baseRequest.getQueryParameters();
    Attributes old_attr = baseRequest.getAttributes();
    DispatcherType old_type = baseRequest.getDispatcherType();
    
    try
    {
      baseRequest.setDispatcherType(dispatch);
      
      if (_named != null)
      {
        _contextHandler.handle(_named, baseRequest, (HttpServletRequest)request, (HttpServletResponse)response);
      }
      else
      {
        ForwardAttributes attr = new ForwardAttributes(old_attr);
        




        if (old_attr.getAttribute("javax.servlet.forward.request_uri") != null)
        {
          _pathInfo = ((String)old_attr.getAttribute("javax.servlet.forward.path_info"));
          _query = ((String)old_attr.getAttribute("javax.servlet.forward.query_string"));
          _requestURI = ((String)old_attr.getAttribute("javax.servlet.forward.request_uri"));
          _contextPath = ((String)old_attr.getAttribute("javax.servlet.forward.context_path"));
          _servletPath = ((String)old_attr.getAttribute("javax.servlet.forward.servlet_path"));
        }
        else
        {
          _pathInfo = old_path_info;
          _query = old_uri.getQuery();
          _requestURI = old_uri.getPath();
          _contextPath = old_context_path;
          _servletPath = old_servlet_path;
        }
        

        HttpURI uri = new HttpURI(old_uri.getScheme(), old_uri.getHost(), old_uri.getPort(), _uri.getPath(), _uri.getParam(), _uri.getQuery(), _uri.getFragment());
        
        baseRequest.setHttpURI(uri);
        
        baseRequest.setContextPath(_contextHandler.getContextPath());
        baseRequest.setServletPath(null);
        baseRequest.setPathInfo(_pathInContext);
        if ((_uri.getQuery() != null) || (old_uri.getQuery() != null)) {
          baseRequest.mergeQueryParameters(old_uri.getQuery(), _uri.getQuery(), true);
        }
        baseRequest.setAttributes(attr);
        
        _contextHandler.handle(_pathInContext, baseRequest, (HttpServletRequest)request, (HttpServletResponse)response);
        
        if (!baseRequest.getHttpChannelState().isAsync()) {
          commitResponse(response, baseRequest);
        }
      }
    }
    finally {
      baseRequest.setHttpURI(old_uri);
      baseRequest.setContextPath(old_context_path);
      baseRequest.setServletPath(old_servlet_path);
      baseRequest.setPathInfo(old_path_info);
      baseRequest.setQueryParameters(old_query_params);
      baseRequest.resetParameters();
      baseRequest.setAttributes(old_attr);
      baseRequest.setDispatcherType(old_type);
    }
  }
  

  public String toString()
  {
    return String.format("Dispatcher@0x%x{%s,%s}", new Object[] { Integer.valueOf(hashCode()), _named, _uri });
  }
  
  private void commitResponse(ServletResponse response, Request baseRequest) throws IOException
  {
    if (baseRequest.getResponse().isWriting())
    {
      try
      {
        response.getWriter().close();
      }
      catch (IllegalStateException e)
      {
        response.getOutputStream().close();
      }
      
    }
    else {
      try
      {
        response.getOutputStream().close();
      }
      catch (IllegalStateException e)
      {
        response.getWriter().close();
      }
    }
  }
  
  private class ForwardAttributes
    implements Attributes
  {
    final Attributes _attr;
    String _requestURI;
    String _contextPath;
    String _servletPath;
    String _pathInfo;
    String _query;
    
    ForwardAttributes(Attributes attributes)
    {
      _attr = attributes;
    }
    


    public Object getAttribute(String key)
    {
      if (_named == null)
      {
        if (key.equals("javax.servlet.forward.path_info"))
          return _pathInfo;
        if (key.equals("javax.servlet.forward.request_uri"))
          return _requestURI;
        if (key.equals("javax.servlet.forward.servlet_path"))
          return _servletPath;
        if (key.equals("javax.servlet.forward.context_path"))
          return _contextPath;
        if (key.equals("javax.servlet.forward.query_string")) {
          return _query;
        }
      }
      if (key.startsWith("javax.servlet.include.")) {
        return null;
      }
      return _attr.getAttribute(key);
    }
    

    public Enumeration<String> getAttributeNames()
    {
      HashSet<String> set = new HashSet();
      Enumeration<String> e = _attr.getAttributeNames();
      while (e.hasMoreElements())
      {
        String name = (String)e.nextElement();
        if ((!name.startsWith("javax.servlet.include.")) && 
          (!name.startsWith("javax.servlet.forward."))) {
          set.add(name);
        }
      }
      if (_named == null)
      {
        if (_pathInfo != null) {
          set.add("javax.servlet.forward.path_info");
        } else
          set.remove("javax.servlet.forward.path_info");
        set.add("javax.servlet.forward.request_uri");
        set.add("javax.servlet.forward.servlet_path");
        set.add("javax.servlet.forward.context_path");
        if (_query != null) {
          set.add("javax.servlet.forward.query_string");
        } else {
          set.remove("javax.servlet.forward.query_string");
        }
      }
      return Collections.enumeration(set);
    }
    

    public void setAttribute(String key, Object value)
    {
      if ((_named == null) && (key.startsWith("javax.servlet.")))
      {
        if (key.equals("javax.servlet.forward.path_info")) {
          _pathInfo = ((String)value);
        } else if (key.equals("javax.servlet.forward.request_uri")) {
          _requestURI = ((String)value);
        } else if (key.equals("javax.servlet.forward.servlet_path")) {
          _servletPath = ((String)value);
        } else if (key.equals("javax.servlet.forward.context_path")) {
          _contextPath = ((String)value);
        } else if (key.equals("javax.servlet.forward.query_string")) {
          _query = ((String)value);
        }
        else if (value == null) {
          _attr.removeAttribute(key);
        } else {
          _attr.setAttribute(key, value);
        }
      } else if (value == null) {
        _attr.removeAttribute(key);
      } else {
        _attr.setAttribute(key, value);
      }
    }
    
    public String toString()
    {
      return "FORWARD+" + _attr.toString();
    }
    

    public void clearAttributes()
    {
      throw new IllegalStateException();
    }
    

    public void removeAttribute(String name)
    {
      setAttribute(name, null);
    }
  }
  
  private class IncludeAttributes
    implements Attributes
  {
    final Attributes _attr;
    String _requestURI;
    String _contextPath;
    String _servletPath;
    String _pathInfo;
    String _query;
    
    IncludeAttributes(Attributes attributes)
    {
      _attr = attributes;
    }
    

    public Object getAttribute(String key)
    {
      if (_named == null)
      {
        if (key.equals("javax.servlet.include.path_info")) return _pathInfo;
        if (key.equals("javax.servlet.include.servlet_path")) return _servletPath;
        if (key.equals("javax.servlet.include.context_path")) return _contextPath;
        if (key.equals("javax.servlet.include.query_string")) return _query;
        if (key.equals("javax.servlet.include.request_uri")) return _requestURI;
      }
      else if (key.startsWith("javax.servlet.include.")) {
        return null;
      }
      
      return _attr.getAttribute(key);
    }
    

    public Enumeration<String> getAttributeNames()
    {
      HashSet<String> set = new HashSet();
      Enumeration<String> e = _attr.getAttributeNames();
      while (e.hasMoreElements())
      {
        String name = (String)e.nextElement();
        if (!name.startsWith("javax.servlet.include.")) {
          set.add(name);
        }
      }
      if (_named == null)
      {
        if (_pathInfo != null) {
          set.add("javax.servlet.include.path_info");
        } else
          set.remove("javax.servlet.include.path_info");
        set.add("javax.servlet.include.request_uri");
        set.add("javax.servlet.include.servlet_path");
        set.add("javax.servlet.include.context_path");
        if (_query != null) {
          set.add("javax.servlet.include.query_string");
        } else {
          set.remove("javax.servlet.include.query_string");
        }
      }
      return Collections.enumeration(set);
    }
    

    public void setAttribute(String key, Object value)
    {
      if ((_named == null) && (key.startsWith("javax.servlet.")))
      {
        if (key.equals("javax.servlet.include.path_info")) { _pathInfo = ((String)value);
        } else if (key.equals("javax.servlet.include.request_uri")) { _requestURI = ((String)value);
        } else if (key.equals("javax.servlet.include.servlet_path")) { _servletPath = ((String)value);
        } else if (key.equals("javax.servlet.include.context_path")) { _contextPath = ((String)value);
        } else if (key.equals("javax.servlet.include.query_string")) { _query = ((String)value);
        } else if (value == null) {
          _attr.removeAttribute(key);
        } else {
          _attr.setAttribute(key, value);
        }
      } else if (value == null) {
        _attr.removeAttribute(key);
      } else {
        _attr.setAttribute(key, value);
      }
    }
    
    public String toString()
    {
      return "INCLUDE+" + _attr.toString();
    }
    

    public void clearAttributes()
    {
      throw new IllegalStateException();
    }
    

    public void removeAttribute(String name)
    {
      setAttribute(name, null);
    }
  }
}
