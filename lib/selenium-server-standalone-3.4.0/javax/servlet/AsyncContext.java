package javax.servlet;

public abstract interface AsyncContext
{
  public static final String ASYNC_REQUEST_URI = "javax.servlet.async.request_uri";
  public static final String ASYNC_CONTEXT_PATH = "javax.servlet.async.context_path";
  public static final String ASYNC_PATH_INFO = "javax.servlet.async.path_info";
  public static final String ASYNC_SERVLET_PATH = "javax.servlet.async.servlet_path";
  public static final String ASYNC_QUERY_STRING = "javax.servlet.async.query_string";
  
  public abstract ServletRequest getRequest();
  
  public abstract ServletResponse getResponse();
  
  public abstract boolean hasOriginalRequestAndResponse();
  
  public abstract void dispatch();
  
  public abstract void dispatch(String paramString);
  
  public abstract void dispatch(ServletContext paramServletContext, String paramString);
  
  public abstract void complete();
  
  public abstract void start(Runnable paramRunnable);
  
  public abstract void addListener(AsyncListener paramAsyncListener);
  
  public abstract void addListener(AsyncListener paramAsyncListener, ServletRequest paramServletRequest, ServletResponse paramServletResponse);
  
  public abstract <T extends AsyncListener> T createListener(Class<T> paramClass)
    throws ServletException;
  
  public abstract void setTimeout(long paramLong);
  
  public abstract long getTimeout();
}
