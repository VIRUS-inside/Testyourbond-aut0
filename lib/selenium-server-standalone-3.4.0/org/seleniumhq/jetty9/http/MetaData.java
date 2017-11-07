package org.seleniumhq.jetty9.http;

import java.util.Collections;
import java.util.Iterator;


















public class MetaData
  implements Iterable<HttpField>
{
  private HttpVersion _httpVersion;
  private HttpFields _fields;
  private long _contentLength;
  
  public MetaData(HttpVersion version, HttpFields fields)
  {
    this(version, fields, Long.MIN_VALUE);
  }
  
  public MetaData(HttpVersion version, HttpFields fields, long contentLength)
  {
    _httpVersion = version;
    _fields = fields;
    _contentLength = contentLength;
  }
  
  protected void recycle()
  {
    _httpVersion = null;
    if (_fields != null)
      _fields.clear();
    _contentLength = Long.MIN_VALUE;
  }
  
  public boolean isRequest()
  {
    return false;
  }
  
  public boolean isResponse()
  {
    return false;
  }
  



  @Deprecated
  public HttpVersion getVersion()
  {
    return getHttpVersion();
  }
  



  public HttpVersion getHttpVersion()
  {
    return _httpVersion;
  }
  



  public void setHttpVersion(HttpVersion httpVersion)
  {
    _httpVersion = httpVersion;
  }
  



  public HttpFields getFields()
  {
    return _fields;
  }
  



  public long getContentLength()
  {
    if (_contentLength == Long.MIN_VALUE)
    {
      if (_fields != null)
      {
        HttpField field = _fields.getField(HttpHeader.CONTENT_LENGTH);
        _contentLength = (field == null ? -1L : field.getLongValue());
      }
    }
    return _contentLength;
  }
  




  public Iterator<HttpField> iterator()
  {
    HttpFields fields = getFields();
    return fields == null ? Collections.emptyIterator() : fields.iterator();
  }
  

  public String toString()
  {
    StringBuilder out = new StringBuilder();
    for (HttpField field : this)
      out.append(field).append(System.lineSeparator());
    return out.toString();
  }
  
  public static class Request extends MetaData
  {
    private String _method;
    private HttpURI _uri;
    
    public Request(HttpFields fields)
    {
      this(null, null, null, fields);
    }
    
    public Request(String method, HttpURI uri, HttpVersion version, HttpFields fields)
    {
      this(method, uri, version, fields, Long.MIN_VALUE);
    }
    
    public Request(String method, HttpURI uri, HttpVersion version, HttpFields fields, long contentLength)
    {
      super(fields, contentLength);
      _method = method;
      _uri = uri;
    }
    
    public Request(String method, HttpScheme scheme, HostPortHttpField hostPort, String uri, HttpVersion version, HttpFields fields)
    {
      this(method, new HttpURI(scheme == null ? null : scheme.asString(), hostPort.getHost(), hostPort.getPort(), uri), version, fields);
    }
    
    public Request(String method, HttpScheme scheme, HostPortHttpField hostPort, String uri, HttpVersion version, HttpFields fields, long contentLength)
    {
      this(method, new HttpURI(scheme == null ? null : scheme.asString(), hostPort.getHost(), hostPort.getPort(), uri), version, fields, contentLength);
    }
    
    public Request(String method, String scheme, HostPortHttpField hostPort, String uri, HttpVersion version, HttpFields fields, long contentLength)
    {
      this(method, new HttpURI(scheme, hostPort.getHost(), hostPort.getPort(), uri), version, fields, contentLength);
    }
    
    public Request(Request request)
    {
      this(request.getMethod(), new HttpURI(request.getURI()), request.getHttpVersion(), new HttpFields(request.getFields()), request.getContentLength());
    }
    
    public void recycle()
    {
      super.recycle();
      _method = null;
      if (_uri != null) {
        _uri.clear();
      }
    }
    
    public boolean isRequest()
    {
      return true;
    }
    



    public String getMethod()
    {
      return _method;
    }
    



    public void setMethod(String method)
    {
      _method = method;
    }
    



    public HttpURI getURI()
    {
      return _uri;
    }
    



    public String getURIString()
    {
      return _uri == null ? null : _uri.toString();
    }
    



    public void setURI(HttpURI uri)
    {
      _uri = uri;
    }
    

    public String toString()
    {
      HttpFields fields = getFields();
      return String.format("%s{u=%s,%s,h=%d,cl=%d}", new Object[] {
        getMethod(), getURI(), getHttpVersion(), Integer.valueOf(fields == null ? -1 : fields.size()), Long.valueOf(getContentLength()) });
    }
  }
  
  public static class Response extends MetaData
  {
    private int _status;
    private String _reason;
    
    public Response()
    {
      this(null, 0, null);
    }
    
    public Response(HttpVersion version, int status, HttpFields fields)
    {
      this(version, status, fields, Long.MIN_VALUE);
    }
    
    public Response(HttpVersion version, int status, HttpFields fields, long contentLength)
    {
      super(fields, contentLength);
      _status = status;
    }
    
    public Response(HttpVersion version, int status, String reason, HttpFields fields, long contentLength)
    {
      super(fields, contentLength);
      _reason = reason;
      _status = status;
    }
    

    public boolean isResponse()
    {
      return true;
    }
    



    public int getStatus()
    {
      return _status;
    }
    



    public String getReason()
    {
      return _reason;
    }
    



    public void setStatus(int status)
    {
      _status = status;
    }
    



    public void setReason(String reason)
    {
      _reason = reason;
    }
    

    public String toString()
    {
      HttpFields fields = getFields();
      return String.format("%s{s=%d,h=%d,cl=%d}", new Object[] { getHttpVersion(), Integer.valueOf(getStatus()), Integer.valueOf(fields == null ? -1 : fields.size()), Long.valueOf(getContentLength()) });
    }
  }
}
