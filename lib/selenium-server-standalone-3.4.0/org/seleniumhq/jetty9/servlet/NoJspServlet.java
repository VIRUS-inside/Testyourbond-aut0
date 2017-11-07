package org.seleniumhq.jetty9.servlet;

import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



















public class NoJspServlet
  extends HttpServlet
{
  private boolean _warned;
  
  public NoJspServlet() {}
  
  protected void doGet(HttpServletRequest req, HttpServletResponse response)
    throws ServletException, IOException
  {
    if (!_warned)
      getServletContext().log("No JSP support.  Check that JSP jars are in lib/jsp and that the JSP option has been specified to start.jar");
    _warned = true;
    
    response.sendError(500, "JSP support not configured");
  }
}
