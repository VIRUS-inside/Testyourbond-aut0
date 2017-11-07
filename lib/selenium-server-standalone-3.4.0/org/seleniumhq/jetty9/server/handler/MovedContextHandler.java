package org.seleniumhq.jetty9.server.handler;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.seleniumhq.jetty9.http.HttpHeader;
import org.seleniumhq.jetty9.server.HandlerContainer;
import org.seleniumhq.jetty9.server.Request;
import org.seleniumhq.jetty9.util.URIUtil;


























public class MovedContextHandler
  extends ContextHandler
{
  final Redirector _redirector;
  String _newContextURL;
  boolean _discardPathInfo;
  boolean _discardQuery;
  boolean _permanent;
  String _expires;
  
  public MovedContextHandler()
  {
    _redirector = new Redirector(null);
    setHandler(_redirector);
    setAllowNullPathInfo(true);
  }
  
  public MovedContextHandler(HandlerContainer parent, String contextPath, String newContextURL)
  {
    super(parent, contextPath);
    _newContextURL = newContextURL;
    _redirector = new Redirector(null);
    setHandler(_redirector);
  }
  
  public boolean isDiscardPathInfo()
  {
    return _discardPathInfo;
  }
  
  public void setDiscardPathInfo(boolean discardPathInfo)
  {
    _discardPathInfo = discardPathInfo;
  }
  
  public String getNewContextURL()
  {
    return _newContextURL;
  }
  
  public void setNewContextURL(String newContextURL)
  {
    _newContextURL = newContextURL;
  }
  
  public boolean isPermanent()
  {
    return _permanent;
  }
  
  public void setPermanent(boolean permanent)
  {
    _permanent = permanent;
  }
  
  public boolean isDiscardQuery()
  {
    return _discardQuery;
  }
  
  public void setDiscardQuery(boolean discardQuery)
  {
    _discardQuery = discardQuery;
  }
  
  private class Redirector extends AbstractHandler {
    private Redirector() {}
    
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
      if (_newContextURL == null) {
        return;
      }
      String path = _newContextURL;
      if ((!_discardPathInfo) && (request.getPathInfo() != null)) {
        path = URIUtil.addPaths(path, request.getPathInfo());
      }
      StringBuilder location = URIUtil.hasScheme(path) ? new StringBuilder() : baseRequest.getRootURL();
      
      location.append(path);
      if ((!_discardQuery) && (request.getQueryString() != null))
      {
        location.append('?');
        String q = request.getQueryString();
        q = q.replaceAll("\r\n?&=", "!");
        location.append(q);
      }
      
      response.setHeader(HttpHeader.LOCATION.asString(), location.toString());
      
      if (_expires != null) {
        response.setHeader(HttpHeader.EXPIRES.asString(), _expires);
      }
      response.setStatus(_permanent ? 301 : 302);
      response.setContentLength(0);
      baseRequest.setHandled(true);
    }
  }
  





  public String getExpires()
  {
    return _expires;
  }
  




  public void setExpires(String expires)
  {
    _expires = expires;
  }
}
