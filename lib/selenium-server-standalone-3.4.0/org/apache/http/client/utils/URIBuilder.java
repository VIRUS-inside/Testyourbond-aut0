package org.apache.http.client.utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.conn.util.InetAddressUtils;
import org.apache.http.message.BasicNameValuePair;




































public class URIBuilder
{
  private String scheme;
  private String encodedSchemeSpecificPart;
  private String encodedAuthority;
  private String userInfo;
  private String encodedUserInfo;
  private String host;
  private int port;
  private String path;
  private String encodedPath;
  private String encodedQuery;
  private List<NameValuePair> queryParams;
  private String query;
  private Charset charset;
  private String fragment;
  private String encodedFragment;
  
  public URIBuilder()
  {
    port = -1;
  }
  





  public URIBuilder(String string)
    throws URISyntaxException
  {
    digestURI(new URI(string));
  }
  




  public URIBuilder(URI uri)
  {
    digestURI(uri);
  }
  


  public URIBuilder setCharset(Charset charset)
  {
    this.charset = charset;
    return this;
  }
  


  public Charset getCharset()
  {
    return charset;
  }
  
  private List<NameValuePair> parseQuery(String query, Charset charset) {
    if ((query != null) && (!query.isEmpty())) {
      return URLEncodedUtils.parse(query, charset);
    }
    return null;
  }
  

  public URI build()
    throws URISyntaxException
  {
    return new URI(buildString());
  }
  
  private String buildString() {
    StringBuilder sb = new StringBuilder();
    if (scheme != null) {
      sb.append(scheme).append(':');
    }
    if (encodedSchemeSpecificPart != null) {
      sb.append(encodedSchemeSpecificPart);
    } else {
      if (encodedAuthority != null) {
        sb.append("//").append(encodedAuthority);
      } else if (host != null) {
        sb.append("//");
        if (encodedUserInfo != null) {
          sb.append(encodedUserInfo).append("@");
        } else if (userInfo != null) {
          sb.append(encodeUserInfo(userInfo)).append("@");
        }
        if (InetAddressUtils.isIPv6Address(host)) {
          sb.append("[").append(host).append("]");
        } else {
          sb.append(host);
        }
        if (port >= 0) {
          sb.append(":").append(port);
        }
      }
      if (encodedPath != null) {
        sb.append(normalizePath(encodedPath));
      } else if (path != null) {
        sb.append(encodePath(normalizePath(path)));
      }
      if (encodedQuery != null) {
        sb.append("?").append(encodedQuery);
      } else if (queryParams != null) {
        sb.append("?").append(encodeUrlForm(queryParams));
      } else if (query != null) {
        sb.append("?").append(encodeUric(query));
      }
    }
    if (encodedFragment != null) {
      sb.append("#").append(encodedFragment);
    } else if (fragment != null) {
      sb.append("#").append(encodeUric(fragment));
    }
    return sb.toString();
  }
  
  private void digestURI(URI uri) {
    scheme = uri.getScheme();
    encodedSchemeSpecificPart = uri.getRawSchemeSpecificPart();
    encodedAuthority = uri.getRawAuthority();
    host = uri.getHost();
    port = uri.getPort();
    encodedUserInfo = uri.getRawUserInfo();
    userInfo = uri.getUserInfo();
    encodedPath = uri.getRawPath();
    path = uri.getPath();
    encodedQuery = uri.getRawQuery();
    queryParams = parseQuery(uri.getRawQuery(), charset != null ? charset : Consts.UTF_8);
    encodedFragment = uri.getRawFragment();
    fragment = uri.getFragment();
  }
  
  private String encodeUserInfo(String userInfo) {
    return URLEncodedUtils.encUserInfo(userInfo, charset != null ? charset : Consts.UTF_8);
  }
  
  private String encodePath(String path) {
    return URLEncodedUtils.encPath(path, charset != null ? charset : Consts.UTF_8);
  }
  
  private String encodeUrlForm(List<NameValuePair> params) {
    return URLEncodedUtils.format(params, charset != null ? charset : Consts.UTF_8);
  }
  
  private String encodeUric(String fragment) {
    return URLEncodedUtils.encUric(fragment, charset != null ? charset : Consts.UTF_8);
  }
  


  public URIBuilder setScheme(String scheme)
  {
    this.scheme = scheme;
    return this;
  }
  



  public URIBuilder setUserInfo(String userInfo)
  {
    this.userInfo = userInfo;
    encodedSchemeSpecificPart = null;
    encodedAuthority = null;
    encodedUserInfo = null;
    return this;
  }
  



  public URIBuilder setUserInfo(String username, String password)
  {
    return setUserInfo(username + ':' + password);
  }
  


  public URIBuilder setHost(String host)
  {
    this.host = host;
    encodedSchemeSpecificPart = null;
    encodedAuthority = null;
    return this;
  }
  


  public URIBuilder setPort(int port)
  {
    this.port = (port < 0 ? -1 : port);
    encodedSchemeSpecificPart = null;
    encodedAuthority = null;
    return this;
  }
  


  public URIBuilder setPath(String path)
  {
    this.path = path;
    encodedSchemeSpecificPart = null;
    encodedPath = null;
    return this;
  }
  


  public URIBuilder removeQuery()
  {
    queryParams = null;
    query = null;
    encodedQuery = null;
    encodedSchemeSpecificPart = null;
    return this;
  }
  








  @Deprecated
  public URIBuilder setQuery(String query)
  {
    queryParams = parseQuery(query, charset != null ? charset : Consts.UTF_8);
    this.query = null;
    encodedQuery = null;
    encodedSchemeSpecificPart = null;
    return this;
  }
  









  public URIBuilder setParameters(List<NameValuePair> nvps)
  {
    if (queryParams == null) {
      queryParams = new ArrayList();
    } else {
      queryParams.clear();
    }
    queryParams.addAll(nvps);
    encodedQuery = null;
    encodedSchemeSpecificPart = null;
    query = null;
    return this;
  }
  









  public URIBuilder addParameters(List<NameValuePair> nvps)
  {
    if (queryParams == null) {
      queryParams = new ArrayList();
    }
    queryParams.addAll(nvps);
    encodedQuery = null;
    encodedSchemeSpecificPart = null;
    query = null;
    return this;
  }
  









  public URIBuilder setParameters(NameValuePair... nvps)
  {
    if (queryParams == null) {
      queryParams = new ArrayList();
    } else {
      queryParams.clear();
    }
    for (NameValuePair nvp : nvps) {
      queryParams.add(nvp);
    }
    encodedQuery = null;
    encodedSchemeSpecificPart = null;
    query = null;
    return this;
  }
  







  public URIBuilder addParameter(String param, String value)
  {
    if (queryParams == null) {
      queryParams = new ArrayList();
    }
    queryParams.add(new BasicNameValuePair(param, value));
    encodedQuery = null;
    encodedSchemeSpecificPart = null;
    query = null;
    return this;
  }
  







  public URIBuilder setParameter(String param, String value)
  {
    if (queryParams == null)
      queryParams = new ArrayList();
    Iterator<NameValuePair> it;
    if (!queryParams.isEmpty()) {
      for (it = queryParams.iterator(); it.hasNext();) {
        NameValuePair nvp = (NameValuePair)it.next();
        if (nvp.getName().equals(param)) {
          it.remove();
        }
      }
    }
    queryParams.add(new BasicNameValuePair(param, value));
    encodedQuery = null;
    encodedSchemeSpecificPart = null;
    query = null;
    return this;
  }
  




  public URIBuilder clearParameters()
  {
    queryParams = null;
    encodedQuery = null;
    encodedSchemeSpecificPart = null;
    return this;
  }
  









  public URIBuilder setCustomQuery(String query)
  {
    this.query = query;
    encodedQuery = null;
    encodedSchemeSpecificPart = null;
    queryParams = null;
    return this;
  }
  



  public URIBuilder setFragment(String fragment)
  {
    this.fragment = fragment;
    encodedFragment = null;
    return this;
  }
  


  public boolean isAbsolute()
  {
    return scheme != null;
  }
  


  public boolean isOpaque()
  {
    return path == null;
  }
  
  public String getScheme() {
    return scheme;
  }
  
  public String getUserInfo() {
    return userInfo;
  }
  
  public String getHost() {
    return host;
  }
  
  public int getPort() {
    return port;
  }
  
  public String getPath() {
    return path;
  }
  
  public List<NameValuePair> getQueryParams() {
    if (queryParams != null) {
      return new ArrayList(queryParams);
    }
    return new ArrayList();
  }
  
  public String getFragment()
  {
    return fragment;
  }
  
  public String toString()
  {
    return buildString();
  }
  
  private static String normalizePath(String path) {
    String s = path;
    if (s == null) {
      return "/";
    }
    for (int n = 0; 
        n < s.length(); n++) {
      if (s.charAt(n) != '/') {
        break;
      }
    }
    if (n > 1) {
      s = s.substring(n - 1);
    }
    if (!s.startsWith("/")) {
      s = "/" + s;
    }
    return s;
  }
}
