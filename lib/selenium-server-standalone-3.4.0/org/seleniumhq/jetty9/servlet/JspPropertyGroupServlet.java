package org.seleniumhq.jetty9.servlet;

import java.io.IOException;
import java.util.Locale;
import javax.servlet.GenericServlet;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.seleniumhq.jetty9.server.handler.ContextHandler;
import org.seleniumhq.jetty9.util.URIUtil;
import org.seleniumhq.jetty9.util.resource.Resource;






























public class JspPropertyGroupServlet
  extends GenericServlet
{
  private static final long serialVersionUID = 3681783214726776945L;
  public static final String NAME = "__org.eclipse.jetty.servlet.JspPropertyGroupServlet__";
  private final ServletHandler _servletHandler;
  private final ContextHandler _contextHandler;
  private ServletHolder _dftServlet;
  private ServletHolder _jspServlet;
  private boolean _starJspMapped;
  
  public JspPropertyGroupServlet(ContextHandler context, ServletHandler servletHandler)
  {
    _contextHandler = context;
    _servletHandler = servletHandler;
  }
  
  public void init()
    throws ServletException
  {
    String jsp_name = "jsp";
    ServletMapping servlet_mapping = _servletHandler.getServletMapping("*.jsp");
    if (servlet_mapping != null)
    {
      _starJspMapped = true;
      

      ServletMapping[] mappings = _servletHandler.getServletMappings();
      for (ServletMapping m : mappings)
      {
        String[] paths = m.getPathSpecs();
        if (paths != null)
        {
          for (String path : paths)
          {
            if (("*.jsp".equals(path)) && (!"__org.eclipse.jetty.servlet.JspPropertyGroupServlet__".equals(m.getServletName()))) {
              servlet_mapping = m;
            }
          }
        }
      }
      jsp_name = servlet_mapping.getServletName();
    }
    _jspServlet = _servletHandler.getServlet(jsp_name);
    
    String dft_name = "default";
    ServletMapping default_mapping = _servletHandler.getServletMapping("/");
    if (default_mapping != null)
      dft_name = default_mapping.getServletName();
    _dftServlet = _servletHandler.getServlet(dft_name);
  }
  
  public void service(ServletRequest req, ServletResponse res)
    throws ServletException, IOException
  {
    HttpServletRequest request = null;
    if ((req instanceof HttpServletRequest)) {
      request = (HttpServletRequest)req;
    } else {
      throw new ServletException("Request not HttpServletRequest");
    }
    String servletPath = null;
    String pathInfo = null;
    if (request.getAttribute("javax.servlet.include.request_uri") != null)
    {
      servletPath = (String)request.getAttribute("javax.servlet.include.servlet_path");
      pathInfo = (String)request.getAttribute("javax.servlet.include.path_info");
      if (servletPath == null)
      {
        servletPath = request.getServletPath();
        pathInfo = request.getPathInfo();
      }
    }
    else
    {
      servletPath = request.getServletPath();
      pathInfo = request.getPathInfo();
    }
    
    String pathInContext = URIUtil.addPaths(servletPath, pathInfo);
    
    if (pathInContext.endsWith("/"))
    {
      _dftServlet.getServlet().service(req, res);
    }
    else if ((_starJspMapped) && (pathInContext.toLowerCase(Locale.ENGLISH).endsWith(".jsp")))
    {
      _jspServlet.getServlet().service(req, res);

    }
    else
    {
      Resource resource = _contextHandler.getResource(pathInContext);
      if ((resource != null) && (resource.isDirectory())) {
        _dftServlet.getServlet().service(req, res);
      } else {
        _jspServlet.getServlet().service(req, res);
      }
    }
  }
}
