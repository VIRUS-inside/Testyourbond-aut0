package org.seleniumhq.jetty9.servlet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.seleniumhq.jetty9.server.handler.ContextHandler;
import org.seleniumhq.jetty9.server.handler.ErrorHandler;
import org.seleniumhq.jetty9.server.handler.ErrorHandler.ErrorPageMapper;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;























public class ErrorPageErrorHandler
  extends ErrorHandler
  implements ErrorHandler.ErrorPageMapper
{
  public static final String GLOBAL_ERROR_PAGE = "org.seleniumhq.jetty9.server.error_page.global";
  private static final Logger LOG = Log.getLogger(ErrorPageErrorHandler.class);
  protected ServletContext _servletContext;
  public ErrorPageErrorHandler() {}
  
  private static enum PageLookupTechnique { THROWABLE,  STATUS_CODE,  GLOBAL;
    
    private PageLookupTechnique() {} }
  
  private final Map<String, String> _errorPages = new HashMap();
  private final List<ErrorCodeRange> _errorPageList = new ArrayList();
  

  public String getErrorPage(HttpServletRequest request)
  {
    String error_page = null;
    
    PageLookupTechnique pageSource = null;
    
    Class<?> matchedThrowable = null;
    Throwable th = (Throwable)request.getAttribute("javax.servlet.error.exception");
    

    while ((error_page == null) && (th != null))
    {
      pageSource = PageLookupTechnique.THROWABLE;
      
      Class<?> exClass = th.getClass();
      error_page = (String)_errorPages.get(exClass.getName());
      

      while (error_page == null)
      {
        exClass = exClass.getSuperclass();
        if (exClass == null)
          break;
        error_page = (String)_errorPages.get(exClass.getName());
      }
      
      if (error_page != null) {
        matchedThrowable = exClass;
      }
      th = (th instanceof ServletException) ? ((ServletException)th).getRootCause() : null;
    }
    
    Integer errorStatusCode = null;
    
    if (error_page == null)
    {
      pageSource = PageLookupTechnique.STATUS_CODE;
      

      errorStatusCode = (Integer)request.getAttribute("javax.servlet.error.status_code");
      if (errorStatusCode != null)
      {
        error_page = (String)_errorPages.get(Integer.toString(errorStatusCode.intValue()));
        

        if (error_page == null)
        {

          for (ErrorCodeRange errCode : _errorPageList)
          {
            if (errCode.isInRange(errorStatusCode.intValue()))
            {
              error_page = errCode.getUri();
              break;
            }
          }
        }
      }
    }
    

    if (error_page == null)
    {
      pageSource = PageLookupTechnique.GLOBAL;
      error_page = (String)_errorPages.get("org.seleniumhq.jetty9.server.error_page.global");
    }
    
    if (LOG.isDebugEnabled())
    {
      StringBuilder dbg = new StringBuilder();
      dbg.append("getErrorPage(");
      dbg.append(request.getMethod()).append(' ');
      dbg.append(request.getRequestURI());
      dbg.append(") => error_page=").append(error_page);
      switch (1.$SwitchMap$org$eclipse$jetty$servlet$ErrorPageErrorHandler$PageLookupTechnique[pageSource.ordinal()])
      {
      case 1: 
        dbg.append(" (using matched Throwable ");
        dbg.append(matchedThrowable.getName());
        dbg.append(" / actually thrown as ");
        Throwable originalThrowable = (Throwable)request.getAttribute("javax.servlet.error.exception");
        dbg.append(originalThrowable.getClass().getName());
        dbg.append(')');
        LOG.debug(dbg.toString(), th);
        break;
      case 2: 
        dbg.append(" (from status code ");
        dbg.append(errorStatusCode);
        dbg.append(')');
        LOG.debug(dbg.toString(), new Object[0]);
        break;
      case 3: 
        dbg.append(" (from global default)");
        LOG.debug(dbg.toString(), new Object[0]);
      }
      
    }
    
    return error_page;
  }
  
  public Map<String, String> getErrorPages()
  {
    return _errorPages;
  }
  



  public void setErrorPages(Map<String, String> errorPages)
  {
    _errorPages.clear();
    if (errorPages != null) {
      _errorPages.putAll(errorPages);
    }
  }
  







  public void addErrorPage(Class<? extends Throwable> exception, String uri)
  {
    _errorPages.put(exception.getName(), uri);
  }
  








  public void addErrorPage(String exceptionClassName, String uri)
  {
    _errorPages.put(exceptionClassName, uri);
  }
  








  public void addErrorPage(int code, String uri)
  {
    _errorPages.put(Integer.toString(code), uri);
  }
  








  public void addErrorPage(int from, int to, String uri)
  {
    _errorPageList.add(new ErrorCodeRange(from, to, uri));
  }
  
  protected void doStart()
    throws Exception
  {
    super.doStart();
    _servletContext = ContextHandler.getCurrentContext();
  }
  
  private static class ErrorCodeRange
  {
    private int _from;
    private int _to;
    private String _uri;
    
    ErrorCodeRange(int from, int to, String uri)
      throws IllegalArgumentException
    {
      if (from > to) {
        throw new IllegalArgumentException("from>to");
      }
      _from = from;
      _to = to;
      _uri = uri;
    }
    
    boolean isInRange(int value)
    {
      return (_from <= value) && (value <= _to);
    }
    
    String getUri()
    {
      return _uri;
    }
    

    public String toString()
    {
      return "from: " + _from + ",to: " + _to + ",uri: " + _uri;
    }
  }
}
