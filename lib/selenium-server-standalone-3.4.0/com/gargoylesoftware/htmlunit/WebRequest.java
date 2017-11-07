package com.gargoylesoftware.htmlunit;

import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.gargoylesoftware.htmlunit.util.UrlUtils;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.IDN;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;




























public class WebRequest
  implements Serializable
{
  private static final Pattern DOT_PATTERN = Pattern.compile("/\\./");
  private static final Pattern DOT_DOT_PATTERN = Pattern.compile("/(?!\\.\\.)[^/]*/\\.\\./");
  private static final Pattern REMOVE_DOTS_PATTERN = Pattern.compile("^/(\\.\\.?/)*");
  
  private String url_;
  private String proxyHost_;
  private int proxyPort_;
  private boolean isSocksProxy_;
  private HttpMethod httpMethod_ = HttpMethod.GET;
  private FormEncodingType encodingType_ = FormEncodingType.URL_ENCODED;
  private Map<String, String> additionalHeaders_ = new HashMap();
  private Credentials urlCredentials_;
  private Credentials credentials_;
  private transient Charset charset_ = StandardCharsets.ISO_8859_1;
  

  private List<NameValuePair> requestParameters_ = Collections.emptyList();
  

  private String requestBody_;
  


  public WebRequest(URL url, String acceptHeader)
  {
    setUrl(url);
    setAdditionalHeader("Accept", acceptHeader);
    setAdditionalHeader("Accept-Encoding", "gzip, deflate");
  }
  



  public WebRequest(URL url)
  {
    this(url, "*/*");
  }
  




  public WebRequest(URL url, HttpMethod submitMethod)
  {
    this(url);
    setHttpMethod(submitMethod);
  }
  



  public URL getUrl()
  {
    return UrlUtils.toUrlSafe(url_);
  }
  




  public void setUrl(URL url)
  {
    if (url != null) {
      String path = url.getPath();
      if (path.isEmpty()) {
        if ((!url.getFile().isEmpty()) || (url.getProtocol().startsWith("http"))) {
          url = buildUrlWithNewPath(url, "/");
        }
      }
      else if (path.contains("/.")) {
        url = buildUrlWithNewPath(url, removeDots(path));
      }
      String idn = IDN.toASCII(url.getHost());
      if (!idn.equals(url.getHost())) {
        try {
          url = UrlUtils.getUrlWithNewHost(url, idn);
        }
        catch (Exception e) {
          throw new RuntimeException("Cannot change hostname of URL: " + url.toExternalForm(), e);
        }
      }
      url_ = url.toExternalForm();
      

      String userInfo = url.getUserInfo();
      if (userInfo != null) {
        int splitPos = userInfo.indexOf(':');
        if (splitPos == -1) {
          urlCredentials_ = new UsernamePasswordCredentials(userInfo, "");
        }
        else {
          String username = userInfo.substring(0, splitPos);
          String password = userInfo.substring(splitPos + 1);
          urlCredentials_ = new UsernamePasswordCredentials(username, password);
        }
      }
    }
    else {
      url_ = null;
    }
  }
  







  private static String removeDots(String path)
  {
    String newPath = path;
    

    newPath = REMOVE_DOTS_PATTERN.matcher(newPath).replaceAll("/");
    if ("/..".equals(newPath)) {
      newPath = "/";
    }
    

    while (DOT_PATTERN.matcher(newPath).find()) {
      newPath = DOT_PATTERN.matcher(newPath).replaceAll("/");
    }
    


    while (DOT_DOT_PATTERN.matcher(newPath).find()) {
      newPath = DOT_DOT_PATTERN.matcher(newPath).replaceAll("/");
    }
    
    return newPath;
  }
  
  private static URL buildUrlWithNewPath(URL url, String newPath) {
    try {
      url = UrlUtils.getUrlWithNewPath(url, newPath);
    }
    catch (Exception e) {
      throw new RuntimeException("Cannot change path of URL: " + url.toExternalForm(), e);
    }
    return url;
  }
  



  public String getProxyHost()
  {
    return proxyHost_;
  }
  



  public void setProxyHost(String proxyHost)
  {
    proxyHost_ = proxyHost;
  }
  



  public int getProxyPort()
  {
    return proxyPort_;
  }
  



  public void setProxyPort(int proxyPort)
  {
    proxyPort_ = proxyPort;
  }
  



  public boolean isSocksProxy()
  {
    return isSocksProxy_;
  }
  



  public void setSocksProxy(boolean isSocksProxy)
  {
    isSocksProxy_ = isSocksProxy;
  }
  



  public FormEncodingType getEncodingType()
  {
    return encodingType_;
  }
  



  public void setEncodingType(FormEncodingType encodingType)
  {
    encodingType_ = encodingType;
  }
  





  public List<NameValuePair> getRequestParameters()
  {
    return requestParameters_;
  }
  





  public void setRequestParameters(List<NameValuePair> requestParameters)
    throws RuntimeException
  {
    if (requestBody_ != null) {
      String msg = "Trying to set the request parameters, but the request body has already been specified;the two are mutually exclusive!";
      
      throw new RuntimeException("Trying to set the request parameters, but the request body has already been specified;the two are mutually exclusive!");
    }
    requestParameters_ = requestParameters;
  }
  




  public String getRequestBody()
  {
    return requestBody_;
  }
  







  public void setRequestBody(String requestBody)
    throws RuntimeException
  {
    if ((requestParameters_ != null) && (!requestParameters_.isEmpty())) {
      String msg = "Trying to set the request body, but the request parameters have already been specified;the two are mutually exclusive!";
      
      throw new RuntimeException("Trying to set the request body, but the request parameters have already been specified;the two are mutually exclusive!");
    }
    if ((httpMethod_ != HttpMethod.POST) && (httpMethod_ != HttpMethod.PUT) && (httpMethod_ != HttpMethod.PATCH)) {
      String msg = "The request body may only be set for POST, PUT or PATCH requests!";
      throw new RuntimeException("The request body may only be set for POST, PUT or PATCH requests!");
    }
    requestBody_ = requestBody;
  }
  



  public HttpMethod getHttpMethod()
  {
    return httpMethod_;
  }
  



  public void setHttpMethod(HttpMethod submitMethod)
  {
    httpMethod_ = submitMethod;
  }
  



  public Map<String, String> getAdditionalHeaders()
  {
    return additionalHeaders_;
  }
  



  public void setAdditionalHeaders(Map<String, String> additionalHeaders)
  {
    additionalHeaders_ = additionalHeaders;
  }
  




  public boolean isAdditionalHeader(String name)
  {
    for (String key : additionalHeaders_.keySet()) {
      if (name.equalsIgnoreCase(key)) {
        return true;
      }
    }
    return false;
  }
  




  public void setAdditionalHeader(String name, String value)
  {
    for (String key : additionalHeaders_.keySet()) {
      if (name.equalsIgnoreCase(key)) {
        name = key;
        break;
      }
    }
    additionalHeaders_.put(name, value);
  }
  



  public void removeAdditionalHeader(String name)
  {
    for (String key : additionalHeaders_.keySet()) {
      if (name.equalsIgnoreCase(key)) {
        name = key;
        break;
      }
    }
    additionalHeaders_.remove(name);
  }
  



  public Credentials getUrlCredentials()
  {
    return urlCredentials_;
  }
  



  public Credentials getCredentials()
  {
    return credentials_;
  }
  



  public void setCredentials(Credentials credentials)
  {
    credentials_ = credentials;
  }
  



  public Charset getCharset()
  {
    return charset_;
  }
  





  @Deprecated
  public void setCharset(String charsetName)
  {
    charset_ = Charset.forName(charsetName);
  }
  




  public void setCharset(Charset charset)
  {
    charset_ = charset;
  }
  




  public String toString()
  {
    StringBuilder builder = new StringBuilder();
    builder.append(getClass().getSimpleName());
    builder.append("[<");
    builder.append("url=\"" + url_ + '"');
    builder.append(", " + httpMethod_);
    builder.append(", " + encodingType_);
    builder.append(", " + requestParameters_);
    builder.append(", " + additionalHeaders_);
    builder.append(", " + credentials_);
    builder.append(">]");
    return builder.toString();
  }
  
  private void writeObject(ObjectOutputStream oos) throws IOException {
    oos.defaultWriteObject();
    oos.writeObject(charset_ == null ? null : charset_.name());
  }
  
  private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
    ois.defaultReadObject();
    String charsetName = (String)ois.readObject();
    if (charsetName != null) {
      charset_ = Charset.forName(charsetName);
    }
  }
}
