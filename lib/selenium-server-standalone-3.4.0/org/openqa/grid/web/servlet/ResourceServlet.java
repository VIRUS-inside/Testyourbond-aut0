package org.openqa.grid.web.servlet;

import com.google.common.io.ByteStreams;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;






















public class ResourceServlet
  extends HttpServlet
{
  private static final long serialVersionUID = 7253742807937667039L;
  
  public ResourceServlet() {}
  
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    process(request, response);
  }
  
  protected void process(HttpServletRequest request, HttpServletResponse response) throws IOException
  {
    String resource = request.getPathInfo().replace(request.getServletPath(), "");
    if (resource.startsWith("/"))
      resource = resource.replaceFirst("/", "");
    InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
    if (in == null) {
      throw new Error("Cannot find resource " + resource);
    }
    try
    {
      ByteStreams.copy(in, response.getOutputStream());
    } finally { Calendar c;
      in.close();
      Calendar c = Calendar.getInstance();
      c.setTime(new Date());
      c.add(5, 10);
      response.setDateHeader("Expires", c.getTime().getTime());
      response.setHeader("Cache-Control", "max-age=864000");
      response.flushBuffer();
    }
  }
}
