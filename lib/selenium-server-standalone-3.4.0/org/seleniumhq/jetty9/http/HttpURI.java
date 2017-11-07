package org.seleniumhq.jetty9.http;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.seleniumhq.jetty9.util.MultiMap;
import org.seleniumhq.jetty9.util.TypeUtil;
import org.seleniumhq.jetty9.util.URIUtil;
import org.seleniumhq.jetty9.util.UrlEncoded;




























public class HttpURI
{
  private String _scheme;
  private String _user;
  private String _host;
  private int _port;
  private String _path;
  private String _param;
  private String _query;
  private String _fragment;
  String _uri;
  String _decodedPath;
  
  private static enum State
  {
    START, 
    HOST_OR_PATH, 
    SCHEME_OR_PATH, 
    HOST, 
    IPV6, 
    PORT, 
    PATH, 
    PARAM, 
    QUERY, 
    FRAGMENT, 
    ASTERISK;
    











    private State() {}
  }
  











  public static HttpURI createHttpURI(String scheme, String host, int port, String path, String param, String query, String fragment)
  {
    if ((port == 80) && (HttpScheme.HTTP.is(scheme)))
      port = 0;
    if ((port == 443) && (HttpScheme.HTTPS.is(scheme)))
      port = 0;
    return new HttpURI(scheme, host, port, path, param, query, fragment);
  }
  


  public HttpURI() {}
  


  public HttpURI(String scheme, String host, int port, String path, String param, String query, String fragment)
  {
    _scheme = scheme;
    _host = host;
    _port = port;
    _path = path;
    _param = param;
    _query = query;
    _fragment = fragment;
  }
  

  public HttpURI(HttpURI uri)
  {
    this(_scheme, _host, _port, _path, _param, _query, _fragment);
    _uri = _uri;
  }
  

  public HttpURI(String uri)
  {
    _port = -1;
    parse(State.START, uri, 0, uri.length());
  }
  

  public HttpURI(URI uri)
  {
    _uri = null;
    
    _scheme = uri.getScheme();
    _host = uri.getHost();
    if ((_host == null) && (uri.getRawSchemeSpecificPart().startsWith("//")))
      _host = "";
    _port = uri.getPort();
    _user = uri.getUserInfo();
    _path = uri.getRawPath();
    
    _decodedPath = uri.getPath();
    if (_decodedPath != null)
    {
      int p = _decodedPath.lastIndexOf(';');
      if (p >= 0)
        _param = _decodedPath.substring(p + 1);
    }
    _query = uri.getRawQuery();
    _fragment = uri.getFragment();
    
    _decodedPath = null;
  }
  

  public HttpURI(String scheme, String host, int port, String pathQuery)
  {
    _uri = null;
    
    _scheme = scheme;
    _host = host;
    _port = port;
    
    parse(State.PATH, pathQuery, 0, pathQuery.length());
  }
  


  public void parse(String uri)
  {
    clear();
    _uri = uri;
    parse(State.START, uri, 0, uri.length());
  }
  






  public void parseRequestTarget(String method, String uri)
  {
    clear();
    _uri = uri;
    
    if (HttpMethod.CONNECT.is(method)) {
      _path = uri;
    } else {
      parse(uri.startsWith("/") ? State.PATH : State.START, uri, 0, uri.length());
    }
  }
  
  @Deprecated
  public void parseConnect(String uri)
  {
    clear();
    _uri = uri;
    _path = uri;
  }
  

  public void parse(String uri, int offset, int length)
  {
    clear();
    int end = offset + length;
    _uri = uri.substring(offset, end);
    parse(State.START, uri, offset, end);
  }
  

  private void parse(State state, String uri, int offset, int end)
  {
    boolean encoded = false;
    int mark = offset;
    int path_mark = 0;
    
    for (int i = offset; i < end; i++)
    {
      char c = uri.charAt(i);
      
      switch (1.$SwitchMap$org$eclipse$jetty$http$HttpURI$State[state.ordinal()])
      {

      case 1: 
        switch (c)
        {
        case '/': 
          mark = i;
          state = State.HOST_OR_PATH;
          break;
        case ';': 
          mark = i + 1;
          state = State.PARAM;
          break;
        
        case '?': 
          _path = "";
          mark = i + 1;
          state = State.QUERY;
          break;
        case '#': 
          mark = i + 1;
          state = State.FRAGMENT;
          break;
        case '*': 
          _path = "*";
          state = State.ASTERISK;
          break;
        
        default: 
          mark = i;
          if (_scheme == null) {
            state = State.SCHEME_OR_PATH;
          }
          else {
            path_mark = i;
            state = State.PATH;
          }
          break;
        }
        break;
      


      case 2: 
        switch (c)
        {

        case ':': 
          _scheme = uri.substring(mark, i);
          
          state = State.START;
          break;
        

        case '/': 
          state = State.PATH;
          break;
        

        case ';': 
          mark = i + 1;
          state = State.PARAM;
          break;
        

        case '?': 
          _path = uri.substring(mark, i);
          mark = i + 1;
          state = State.QUERY;
          break;
        

        case '%': 
          encoded = true;
          state = State.PATH;
          break;
        

        case '#': 
          _path = uri.substring(mark, i);
          state = State.FRAGMENT;
        }
        
        break;
      


      case 3: 
        switch (c)
        {
        case '/': 
          _host = "";
          mark = i + 1;
          state = State.HOST;
          break;
        

        case '#': 
        case ';': 
        case '?': 
        case '@': 
          i--;
          path_mark = mark;
          state = State.PATH;
          break;
        
        default: 
          path_mark = mark;
          state = State.PATH;
        }
        break;
      


      case 4: 
        switch (c)
        {
        case '/': 
          _host = uri.substring(mark, i);
          path_mark = mark = i;
          state = State.PATH;
          break;
        case ':': 
          if (i > mark)
            _host = uri.substring(mark, i);
          mark = i + 1;
          state = State.PORT;
          break;
        case '@': 
          if (_user != null)
            throw new IllegalArgumentException("Bad authority");
          _user = uri.substring(mark, i);
          mark = i + 1;
          break;
        
        case '[': 
          state = State.IPV6;
        }
        
        break;
      


      case 5: 
        switch (c)
        {
        case '/': 
          throw new IllegalArgumentException("No closing ']' for ipv6 in " + uri);
        case ']': 
          c = uri.charAt(++i);
          _host = uri.substring(mark, i);
          if (c == ':')
          {
            mark = i + 1;
            state = State.PORT;
          }
          else
          {
            path_mark = mark = i;
            state = State.PATH;
          }
          break;
        }
        
        break;
      


      case 6: 
        if (c == '@')
        {
          if (_user != null) {
            throw new IllegalArgumentException("Bad authority");
          }
          _user = (_host + ":" + uri.substring(mark, i));
          mark = i + 1;
          state = State.HOST;
        }
        else if (c == '/')
        {
          _port = TypeUtil.parseInt(uri, mark, i - mark, 10);
          path_mark = mark = i;
          state = State.PATH;
        }
        


        break;
      case 7: 
        switch (c)
        {
        case ';': 
          mark = i + 1;
          state = State.PARAM;
          break;
        case '?': 
          _path = uri.substring(path_mark, i);
          mark = i + 1;
          state = State.QUERY;
          break;
        case '#': 
          _path = uri.substring(path_mark, i);
          mark = i + 1;
          state = State.FRAGMENT;
          break;
        case '%': 
          encoded = true;
        }
        
        break;
      


      case 8: 
        switch (c)
        {
        case '?': 
          _path = uri.substring(path_mark, i);
          _param = uri.substring(mark, i);
          mark = i + 1;
          state = State.QUERY;
          break;
        case '#': 
          _path = uri.substring(path_mark, i);
          _param = uri.substring(mark, i);
          mark = i + 1;
          state = State.FRAGMENT;
          break;
        case '/': 
          encoded = true;
          
          state = State.PATH;
          break;
        
        case ';': 
          mark = i + 1;
        }
        
        break;
      


      case 9: 
        if (c == '#')
        {
          _query = uri.substring(mark, i);
          mark = i + 1;
          state = State.FRAGMENT;
        }
        


        break;
      case 10: 
        throw new IllegalArgumentException("Bad character '*'");
      


      case 11: 
        _fragment = uri.substring(mark, end);
        i = end;
      }
      
    }
    

    switch (1.$SwitchMap$org$eclipse$jetty$http$HttpURI$State[state.ordinal()])
    {
    case 1: 
      break;
    case 2: 
      _path = uri.substring(mark, end);
      break;
    
    case 3: 
      _path = uri.substring(mark, end);
      break;
    
    case 4: 
      if (end > mark) {
        _host = uri.substring(mark, end);
      }
      break;
    case 5: 
      throw new IllegalArgumentException("No closing ']' for ipv6 in " + uri);
    
    case 6: 
      _port = TypeUtil.parseInt(uri, mark, end - mark, 10);
      break;
    
    case 10: 
      break;
    
    case 11: 
      _fragment = uri.substring(mark, end);
      break;
    
    case 8: 
      _path = uri.substring(path_mark, end);
      _param = uri.substring(mark, end);
      break;
    
    case 7: 
      _path = uri.substring(path_mark, end);
      break;
    
    case 9: 
      _query = uri.substring(mark, end);
    }
    
    
    if (!encoded)
    {
      if (_param == null) {
        _decodedPath = _path;
      } else {
        _decodedPath = _path.substring(0, _path.length() - _param.length() - 1);
      }
    }
  }
  
  public String getScheme()
  {
    return _scheme;
  }
  


  public String getHost()
  {
    if ((_host != null) && (_host.length() == 0))
      return null;
    return _host;
  }
  

  public int getPort()
  {
    return _port;
  }
  






  public String getPath()
  {
    return _path;
  }
  

  public String getDecodedPath()
  {
    if ((_decodedPath == null) && (_path != null))
      _decodedPath = URIUtil.decodePath(_path);
    return _decodedPath;
  }
  

  public String getParam()
  {
    return _param;
  }
  

  public String getQuery()
  {
    return _query;
  }
  

  public boolean hasQuery()
  {
    return (_query != null) && (_query.length() > 0);
  }
  

  public String getFragment()
  {
    return _fragment;
  }
  

  public void decodeQueryTo(MultiMap<String> parameters)
  {
    if (_query == _fragment)
      return;
    UrlEncoded.decodeUtf8To(_query, parameters);
  }
  
  public void decodeQueryTo(MultiMap<String> parameters, String encoding)
    throws UnsupportedEncodingException
  {
    decodeQueryTo(parameters, Charset.forName(encoding));
  }
  
  public void decodeQueryTo(MultiMap<String> parameters, Charset encoding)
    throws UnsupportedEncodingException
  {
    if (_query == _fragment) {
      return;
    }
    if ((encoding == null) || (StandardCharsets.UTF_8.equals(encoding))) {
      UrlEncoded.decodeUtf8To(_query, parameters);
    } else {
      UrlEncoded.decodeTo(_query, parameters, encoding);
    }
  }
  
  public void clear()
  {
    _uri = null;
    
    _scheme = null;
    _host = null;
    _port = -1;
    _path = null;
    _param = null;
    _query = null;
    _fragment = null;
    
    _decodedPath = null;
  }
  

  public boolean isAbsolute()
  {
    return (_scheme != null) && (_scheme.length() > 0);
  }
  


  public String toString()
  {
    if (_uri == null)
    {
      StringBuilder out = new StringBuilder();
      
      if (_scheme != null) {
        out.append(_scheme).append(':');
      }
      if (_host != null)
      {
        out.append("//");
        if (_user != null)
          out.append(_user).append('@');
        out.append(_host);
      }
      
      if (_port > 0) {
        out.append(':').append(_port);
      }
      if (_path != null) {
        out.append(_path);
      }
      if (_query != null) {
        out.append('?').append(_query);
      }
      if (_fragment != null) {
        out.append('#').append(_fragment);
      }
      if (out.length() > 0) {
        _uri = out.toString();
      } else
        _uri = "";
    }
    return _uri;
  }
  

  public boolean equals(Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof HttpURI))
      return false;
    return toString().equals(o.toString());
  }
  

  public void setScheme(String scheme)
  {
    _scheme = scheme;
    _uri = null;
  }
  





  public void setAuthority(String host, int port)
  {
    _host = host;
    _port = port;
    _uri = null;
  }
  




  public void setPath(String path)
  {
    _uri = null;
    _path = path;
    _decodedPath = null;
  }
  

  public void setPathQuery(String path)
  {
    _uri = null;
    _path = null;
    _decodedPath = null;
    _param = null;
    _fragment = null;
    if (path != null) {
      parse(State.PATH, path, 0, path.length());
    }
  }
  
  public void setQuery(String query)
  {
    _query = query;
    _uri = null;
  }
  
  public URI toURI()
    throws URISyntaxException
  {
    return new URI(_scheme, null, _host, _port, _path, _query == null ? null : UrlEncoded.decodeString(_query), _fragment);
  }
  

  public String getPathQuery()
  {
    if (_query == null)
      return _path;
    return _path + "?" + _query;
  }
  

  public String getAuthority()
  {
    if (_port > 0)
      return _host + ":" + _port;
    return _host;
  }
  

  public String getUser()
  {
    return _user;
  }
}
