package javax.servlet;

import java.io.IOException;

public abstract interface FilterChain
{
  public abstract void doFilter(ServletRequest paramServletRequest, ServletResponse paramServletResponse)
    throws IOException, ServletException;
}
