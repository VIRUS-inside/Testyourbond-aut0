package org.seleniumhq.jetty9.servlets;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;
























@Deprecated
public class GzipFilter
  implements Filter
{
  private static final Logger LOG = Log.getLogger(GzipFilter.class);
  
  public GzipFilter() {}
  
  public void init(FilterConfig filterConfig) throws ServletException {
    LOG.warn("GzipFilter is deprecated. Use GzipHandler", new Object[0]);
  }
  
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
    throws IOException, ServletException
  {
    chain.doFilter(request, response);
  }
  
  public void destroy() {}
}
