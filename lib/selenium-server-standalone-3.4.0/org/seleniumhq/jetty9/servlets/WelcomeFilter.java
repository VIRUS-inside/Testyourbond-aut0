package org.seleniumhq.jetty9.servlets;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;






























public class WelcomeFilter
  implements Filter
{
  private String welcome;
  
  public WelcomeFilter() {}
  
  public void init(FilterConfig filterConfig)
  {
    welcome = filterConfig.getInitParameter("welcome");
    if (welcome == null) {
      welcome = "index.html";
    }
  }
  


  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
    throws IOException, ServletException
  {
    String path = ((HttpServletRequest)request).getServletPath();
    if ((welcome != null) && (path.endsWith("/"))) {
      request.getRequestDispatcher(path + welcome).forward(request, response);
    } else {
      chain.doFilter(request, response);
    }
  }
  
  public void destroy() {}
}
