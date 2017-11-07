package javax.servlet.http;

import java.io.IOException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.ResourceBundle;
import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;









































































































public abstract class HttpServlet
  extends GenericServlet
{
  private static final String METHOD_DELETE = "DELETE";
  private static final String METHOD_HEAD = "HEAD";
  private static final String METHOD_GET = "GET";
  private static final String METHOD_OPTIONS = "OPTIONS";
  private static final String METHOD_POST = "POST";
  private static final String METHOD_PUT = "PUT";
  private static final String METHOD_TRACE = "TRACE";
  private static final String HEADER_IFMODSINCE = "If-Modified-Since";
  private static final String HEADER_LASTMOD = "Last-Modified";
  private static final String LSTRING_FILE = "javax.servlet.http.LocalStrings";
  private static ResourceBundle lStrings = ResourceBundle.getBundle("javax.servlet.http.LocalStrings");
  





































  public HttpServlet() {}
  





































  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException
  {
    String protocol = req.getProtocol();
    String msg = lStrings.getString("http.method_get_not_supported");
    if (protocol.endsWith("1.1")) {
      resp.sendError(405, msg);
    } else {
      resp.sendError(400, msg);
    }
  }
  























  protected long getLastModified(HttpServletRequest req)
  {
    return -1L;
  }
  


































  protected void doHead(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException
  {
    NoBodyResponse response = new NoBodyResponse(resp);
    
    doGet(req, response);
    response.setContentLength();
  }
  



























































  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException
  {
    String protocol = req.getProtocol();
    String msg = lStrings.getString("http.method_post_not_supported");
    if (protocol.endsWith("1.1")) {
      resp.sendError(405, msg);
    } else {
      resp.sendError(400, msg);
    }
  }
  











































  protected void doPut(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException
  {
    String protocol = req.getProtocol();
    String msg = lStrings.getString("http.method_put_not_supported");
    if (protocol.endsWith("1.1")) {
      resp.sendError(405, msg);
    } else {
      resp.sendError(400, msg);
    }
  }
  



































  protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException
  {
    String protocol = req.getProtocol();
    String msg = lStrings.getString("http.method_delete_not_supported");
    if (protocol.endsWith("1.1")) {
      resp.sendError(405, msg);
    } else {
      resp.sendError(400, msg);
    }
  }
  

  private Method[] getAllDeclaredMethods(Class<? extends HttpServlet> c)
  {
    Class<?> clazz = c;
    Method[] allMethods = null;
    
    while (!clazz.equals(HttpServlet.class)) {
      Method[] thisMethods = clazz.getDeclaredMethods();
      if ((allMethods != null) && (allMethods.length > 0)) {
        Method[] subClassMethods = allMethods;
        allMethods = new Method[thisMethods.length + subClassMethods.length];
        
        System.arraycopy(thisMethods, 0, allMethods, 0, thisMethods.length);
        
        System.arraycopy(subClassMethods, 0, allMethods, thisMethods.length, subClassMethods.length);
      }
      else {
        allMethods = thisMethods;
      }
      
      clazz = clazz.getSuperclass();
    }
    
    return allMethods != null ? allMethods : new Method[0];
  }
  
































  protected void doOptions(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException
  {
    Method[] methods = getAllDeclaredMethods(getClass());
    
    boolean ALLOW_GET = false;
    boolean ALLOW_HEAD = false;
    boolean ALLOW_POST = false;
    boolean ALLOW_PUT = false;
    boolean ALLOW_DELETE = false;
    boolean ALLOW_TRACE = true;
    boolean ALLOW_OPTIONS = true;
    
    for (int i = 0; i < methods.length; i++) {
      String methodName = methods[i].getName();
      
      if (methodName.equals("doGet")) {
        ALLOW_GET = true;
        ALLOW_HEAD = true;
      } else if (methodName.equals("doPost")) {
        ALLOW_POST = true;
      } else if (methodName.equals("doPut")) {
        ALLOW_PUT = true;
      } else if (methodName.equals("doDelete")) {
        ALLOW_DELETE = true;
      }
    }
    



    StringBuilder allow = new StringBuilder();
    if (ALLOW_GET) {
      allow.append("GET");
    }
    if (ALLOW_HEAD) {
      if (allow.length() > 0) {
        allow.append(", ");
      }
      allow.append("HEAD");
    }
    if (ALLOW_POST) {
      if (allow.length() > 0) {
        allow.append(", ");
      }
      allow.append("POST");
    }
    if (ALLOW_PUT) {
      if (allow.length() > 0) {
        allow.append(", ");
      }
      allow.append("PUT");
    }
    if (ALLOW_DELETE) {
      if (allow.length() > 0) {
        allow.append(", ");
      }
      allow.append("DELETE");
    }
    if (ALLOW_TRACE) {
      if (allow.length() > 0) {
        allow.append(", ");
      }
      allow.append("TRACE");
    }
    if (ALLOW_OPTIONS) {
      if (allow.length() > 0) {
        allow.append(", ");
      }
      allow.append("OPTIONS");
    }
    
    resp.setHeader("Allow", allow.toString());
  }
  




























  protected void doTrace(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException
  {
    String CRLF = "\r\n";
    StringBuilder buffer = new StringBuilder("TRACE ").append(req.getRequestURI()).append(" ").append(req.getProtocol());
    

    Enumeration<String> reqHeaderEnum = req.getHeaderNames();
    
    while (reqHeaderEnum.hasMoreElements()) {
      String headerName = (String)reqHeaderEnum.nextElement();
      buffer.append(CRLF).append(headerName).append(": ").append(req.getHeader(headerName));
    }
    

    buffer.append(CRLF);
    
    int responseLength = buffer.length();
    
    resp.setContentType("message/http");
    resp.setContentLength(responseLength);
    ServletOutputStream out = resp.getOutputStream();
    out.print(buffer.toString());
  }
  


























  protected void service(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException
  {
    String method = req.getMethod();
    
    if (method.equals("GET")) {
      long lastModified = getLastModified(req);
      if (lastModified == -1L)
      {

        doGet(req, resp);
      } else {
        long ifModifiedSince = req.getDateHeader("If-Modified-Since");
        if (ifModifiedSince < lastModified)
        {


          maybeSetLastModified(resp, lastModified);
          doGet(req, resp);
        } else {
          resp.setStatus(304);
        }
      }
    }
    else if (method.equals("HEAD")) {
      long lastModified = getLastModified(req);
      maybeSetLastModified(resp, lastModified);
      doHead(req, resp);
    }
    else if (method.equals("POST")) {
      doPost(req, resp);
    }
    else if (method.equals("PUT")) {
      doPut(req, resp);
    }
    else if (method.equals("DELETE")) {
      doDelete(req, resp);
    }
    else if (method.equals("OPTIONS")) {
      doOptions(req, resp);
    }
    else if (method.equals("TRACE")) {
      doTrace(req, resp);


    }
    else
    {


      String errMsg = lStrings.getString("http.method_not_implemented");
      Object[] errArgs = new Object[1];
      errArgs[0] = method;
      errMsg = MessageFormat.format(errMsg, errArgs);
      
      resp.sendError(501, errMsg);
    }
  }
  








  private void maybeSetLastModified(HttpServletResponse resp, long lastModified)
  {
    if (resp.containsHeader("Last-Modified"))
      return;
    if (lastModified >= 0L) {
      resp.setDateHeader("Last-Modified", lastModified);
    }
  }
  


























  public void service(ServletRequest req, ServletResponse res)
    throws ServletException, IOException
  {
    if ((!(req instanceof HttpServletRequest)) || (!(res instanceof HttpServletResponse)))
    {
      throw new ServletException("non-HTTP request or response");
    }
    
    HttpServletRequest request = (HttpServletRequest)req;
    HttpServletResponse response = (HttpServletResponse)res;
    
    service(request, response);
  }
}
