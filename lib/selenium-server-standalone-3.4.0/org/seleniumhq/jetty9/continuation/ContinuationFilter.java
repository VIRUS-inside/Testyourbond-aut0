package org.seleniumhq.jetty9.continuation;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;





































public class ContinuationFilter
  implements Filter
{
  static boolean _initialized;
  static boolean __debug;
  private boolean _faux;
  private boolean _filtered;
  ServletContext _context;
  private boolean _debug;
  
  public ContinuationFilter() {}
  
  public void init(FilterConfig filterConfig)
    throws ServletException
  {
    boolean jetty_7_or_greater = "org.seleniumhq.jetty9.servlet".equals(filterConfig.getClass().getPackage().getName());
    _context = filterConfig.getServletContext();
    
    String param = filterConfig.getInitParameter("debug");
    _debug = ((param != null) && (Boolean.parseBoolean(param)));
    if (_debug) {
      __debug = true;
    }
    param = filterConfig.getInitParameter("partial");
    param = filterConfig.getInitParameter("faux");
    if (param != null) {
      _faux = Boolean.parseBoolean(param);
    } else {
      _faux = ((!jetty_7_or_greater) && (_context.getMajorVersion() < 3));
    }
    _filtered = _faux;
    if (_debug) {
      _context.log("ContinuationFilter  jetty=" + jetty_7_or_greater + " faux=" + _faux + " filtered=" + _filtered + " servlet3=" + ContinuationSupport.__servlet3);
    }
    


    _initialized = true;
  }
  
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
  {
    if (_filtered)
    {
      Continuation c = (Continuation)request.getAttribute("org.seleniumhq.jetty9.continuation");
      FilteredContinuation fc;
      if ((_faux) && ((c == null) || (!(c instanceof FauxContinuation))))
      {
        FilteredContinuation fc = new FauxContinuation(request);
        request.setAttribute("org.seleniumhq.jetty9.continuation", fc);
      }
      else {
        fc = (FilteredContinuation)c;
      }
      boolean complete = false;
      while (!complete)
      {
        try
        {
          if ((fc == null) || (fc.enter(response))) {
            chain.doFilter(request, response);
          }
        }
        catch (ContinuationThrowable e) {
          debug("faux", e);
        }
        finally
        {
          if (fc == null) {
            fc = (FilteredContinuation)request.getAttribute("org.seleniumhq.jetty9.continuation");
          }
          complete = (fc == null) || (fc.exit());
        }
      }
    }
    else
    {
      try
      {
        chain.doFilter(request, response);
      }
      catch (ContinuationThrowable e)
      {
        debug("caught", e);
      }
    }
  }
  
  private void debug(String string)
  {
    if (_debug)
    {
      _context.log(string);
    }
  }
  
  private void debug(String string, Throwable th)
  {
    if (_debug)
    {
      if ((th instanceof ContinuationThrowable)) {
        _context.log(string + ":" + th);
      } else {
        _context.log(string, th);
      }
    }
  }
  
  public void destroy() {}
  
  public static abstract interface FilteredContinuation
    extends Continuation
  {
    public abstract boolean enter(ServletResponse paramServletResponse);
    
    public abstract boolean exit();
  }
}
