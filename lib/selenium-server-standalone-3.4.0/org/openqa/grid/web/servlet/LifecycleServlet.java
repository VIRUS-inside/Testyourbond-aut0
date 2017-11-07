package org.openqa.grid.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.openqa.grid.common.exception.GridException;




















public class LifecycleServlet
  extends HttpServlet
{
  public LifecycleServlet() {}
  
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    process(request, response);
  }
  
  protected void process(HttpServletRequest request, HttpServletResponse response) throws IOException
  {
    response.setContentType("text/html");
    response.setCharacterEncoding("UTF-8");
    response.setStatus(200);
    String action = request.getParameter("action");
    if ("shutdown".equals(action)) {
      Runnable initiateShutDown = new Runnable() {
        public void run() {
          try {
            Thread.sleep(500L);
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
          System.exit(0);
        }
      };
      Thread isd = new Thread(initiateShutDown);
      isd.setName("initiateShutDown");
      isd.start();
    } else {
      throw new GridException("Unknown lifecycle action: " + action);
    }
    response.getWriter().close();
  }
}
