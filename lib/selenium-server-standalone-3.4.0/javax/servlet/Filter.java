package javax.servlet;

import java.io.IOException;

public abstract interface Filter
{
  public abstract void init(FilterConfig paramFilterConfig)
    throws ServletException;
  
  public abstract void doFilter(ServletRequest paramServletRequest, ServletResponse paramServletResponse, FilterChain paramFilterChain)
    throws IOException, ServletException;
  
  public abstract void destroy();
}
