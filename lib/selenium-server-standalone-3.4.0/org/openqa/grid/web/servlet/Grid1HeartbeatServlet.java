package org.openqa.grid.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.openqa.grid.internal.Registry;
import org.openqa.grid.internal.RemoteProxy;



















@Deprecated
public class Grid1HeartbeatServlet
  extends RegistryBasedServlet
{
  private static final long serialVersionUID = 7653463271803124556L;
  
  public Grid1HeartbeatServlet()
  {
    this(null);
  }
  
  public Grid1HeartbeatServlet(Registry registry) {
    super(registry);
  }
  
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    response.setContentType("text/html");
    response.setCharacterEncoding("UTF-8");
    response.setStatus(200);
    

    Map<String, String[]> queryParams = request.getParameterMap();
    
    String nodeUrl = String.format("http://%s:%s", new Object[] {((String[])queryParams.get("host"))[0], ((String[])queryParams.get("port"))[0] });
    

    boolean alreadyRegistered = false;
    for (RemoteProxy proxy : getRegistry().getAllProxies()) {
      if (proxy.getRemoteHost().toString().equals(nodeUrl)) {
        alreadyRegistered = true;
      }
    }
    
    if (alreadyRegistered) {
      response.getWriter().print("Hub : OK");
    } else {
      response.getWriter().print("Hub : Not Registered");
    }
  }
}
