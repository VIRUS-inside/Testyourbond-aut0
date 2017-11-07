package org.seleniumhq.jetty9.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.seleniumhq.jetty9.util.URIUtil;
















































public class ConcatServlet
  extends HttpServlet
{
  private boolean _development;
  private long _lastModified;
  
  public ConcatServlet() {}
  
  public void init()
    throws ServletException
  {
    _lastModified = System.currentTimeMillis();
    _development = Boolean.parseBoolean(getInitParameter("development"));
  }
  




  protected long getLastModified(HttpServletRequest req)
  {
    return _development ? -1L : _lastModified;
  }
  
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    String query = request.getQueryString();
    if (query == null)
    {
      response.sendError(204);
      return;
    }
    
    List<RequestDispatcher> dispatchers = new ArrayList();
    String[] parts = query.split("\\&");
    String type = null;
    for (String part : parts)
    {
      String path = URIUtil.canonicalPath(URIUtil.decodePath(part));
      if (path == null)
      {
        response.sendError(404);
        return;
      }
      

      if ((startsWith(path, "/WEB-INF/")) || (startsWith(path, "/META-INF/")))
      {
        response.sendError(404);
        return;
      }
      
      String t = getServletContext().getMimeType(path);
      if (t != null)
      {
        if (type == null)
        {
          type = t;
        }
        else if (!type.equals(t))
        {
          response.sendError(415);
          return;
        }
      }
      
      RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(path);
      if (dispatcher != null) {
        dispatchers.add(dispatcher);
      }
    }
    if (type != null) {
      response.setContentType(type);
    }
    for (??? = dispatchers.iterator(); ((Iterator)???).hasNext();) { RequestDispatcher dispatcher = (RequestDispatcher)((Iterator)???).next();
      dispatcher.include(request, response);
    }
  }
  
  private boolean startsWith(String path, String prefix)
  {
    return prefix.regionMatches(true, 0, path, 0, prefix.length());
  }
}
