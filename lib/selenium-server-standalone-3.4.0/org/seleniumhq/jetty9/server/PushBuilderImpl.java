package org.seleniumhq.jetty9.server;

import java.util.Set;
import org.seleniumhq.jetty9.http.HttpField;
import org.seleniumhq.jetty9.http.HttpFields;
import org.seleniumhq.jetty9.http.HttpHeader;
import org.seleniumhq.jetty9.http.HttpMethod;
import org.seleniumhq.jetty9.http.HttpURI;
import org.seleniumhq.jetty9.http.MetaData.Request;
import org.seleniumhq.jetty9.util.URIUtil;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;























public class PushBuilderImpl
  implements PushBuilder
{
  private static final Logger LOG = Log.getLogger(PushBuilderImpl.class);
  
  private static final HttpField JettyPush = new HttpField("x-http2-push", "PushBuilder");
  
  private final Request _request;
  
  private final HttpFields _fields;
  private String _method;
  private String _queryString;
  private String _sessionId;
  private boolean _conditional;
  private String _path;
  private String _etag;
  private String _lastModified;
  
  public PushBuilderImpl(Request request, HttpFields fields, String method, String queryString, String sessionId, boolean conditional)
  {
    _request = request;
    _fields = fields;
    _method = method;
    _queryString = queryString;
    _sessionId = sessionId;
    _conditional = conditional;
    _fields.add(JettyPush);
    if (LOG.isDebugEnabled()) {
      LOG.debug("PushBuilder({} {}?{} s={} c={})", new Object[] { _method, _request.getRequestURI(), _queryString, _sessionId, Boolean.valueOf(_conditional) });
    }
  }
  

  public String getMethod()
  {
    return _method;
  }
  


  public PushBuilder method(String method)
  {
    _method = method;
    return this;
  }
  


  public String getQueryString()
  {
    return _queryString;
  }
  


  public PushBuilder queryString(String queryString)
  {
    _queryString = queryString;
    return this;
  }
  


  public String getSessionId()
  {
    return _sessionId;
  }
  


  public PushBuilder sessionId(String sessionId)
  {
    _sessionId = sessionId;
    return this;
  }
  


  public boolean isConditional()
  {
    return _conditional;
  }
  


  public PushBuilder conditional(boolean conditional)
  {
    _conditional = conditional;
    return this;
  }
  


  public Set<String> getHeaderNames()
  {
    return _fields.getFieldNamesCollection();
  }
  


  public String getHeader(String name)
  {
    return _fields.get(name);
  }
  


  public PushBuilder setHeader(String name, String value)
  {
    _fields.put(name, value);
    return this;
  }
  


  public PushBuilder addHeader(String name, String value)
  {
    _fields.add(name, value);
    return this;
  }
  


  public PushBuilder removeHeader(String name)
  {
    _fields.remove(name);
    return this;
  }
  


  public String getPath()
  {
    return _path;
  }
  


  public PushBuilder path(String path)
  {
    _path = path;
    return this;
  }
  


  public String getEtag()
  {
    return _etag;
  }
  


  public PushBuilder etag(String etag)
  {
    _etag = etag;
    return this;
  }
  


  public String getLastModified()
  {
    return _lastModified;
  }
  


  public PushBuilder lastModified(String lastModified)
  {
    _lastModified = lastModified;
    return this;
  }
  


  public void push()
  {
    if ((HttpMethod.POST.is(_method)) || (HttpMethod.PUT.is(_method))) {
      throw new IllegalStateException("Bad Method " + _method);
    }
    if ((_path == null) || (_path.length() == 0)) {
      throw new IllegalStateException("Bad Path " + _path);
    }
    String path = _path;
    String query = _queryString;
    int q = path.indexOf('?');
    if (q >= 0)
    {
      query = (query != null) && (query.length() > 0) ? path.substring(q + 1) + '&' + query : path.substring(q + 1);
      path = path.substring(0, q);
    }
    
    if (!path.startsWith("/")) {
      path = URIUtil.addPaths(_request.getContextPath(), path);
    }
    String param = null;
    if (_sessionId != null)
    {
      if (_request.isRequestedSessionIdFromURL()) {
        param = "jsessionid=" + _sessionId;
      }
    }
    

    if (_conditional)
    {
      if (_etag != null) {
        _fields.add(HttpHeader.IF_NONE_MATCH, _etag);
      } else if (_lastModified != null) {
        _fields.add(HttpHeader.IF_MODIFIED_SINCE, _lastModified);
      }
    }
    HttpURI uri = HttpURI.createHttpURI(_request.getScheme(), _request.getServerName(), _request.getServerPort(), path, param, query, null);
    MetaData.Request push = new MetaData.Request(_method, uri, _request.getHttpVersion(), _fields);
    
    if (LOG.isDebugEnabled()) {
      LOG.debug("Push {} {} inm={} ims={}", new Object[] { _method, uri, _fields.get(HttpHeader.IF_NONE_MATCH), _fields.get(HttpHeader.IF_MODIFIED_SINCE) });
    }
    _request.getHttpChannel().getHttpTransport().push(push);
    _path = null;
    _etag = null;
    _lastModified = null;
  }
}
