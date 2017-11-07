package org.jsoup.helper;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import org.jsoup.Connection;
import org.jsoup.Connection.KeyVal;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Request;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

public class HttpConnection implements Connection
{
  public static final String CONTENT_ENCODING = "Content-Encoding";
  public static final String DEFAULT_UA = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36";
  private static final String USER_AGENT = "User-Agent";
  private static final String CONTENT_TYPE = "Content-Type";
  private static final String MULTIPART_FORM_DATA = "multipart/form-data";
  private static final String FORM_URL_ENCODED = "application/x-www-form-urlencoded";
  private static final int HTTP_TEMP_REDIR = 307;
  private Connection.Request req;
  private Connection.Response res;
  
  public static Connection connect(String url)
  {
    Connection con = new HttpConnection();
    con.url(url);
    return con;
  }
  
  public static Connection connect(URL url) {
    Connection con = new HttpConnection();
    con.url(url);
    return con;
  }
  



  private static String encodeUrl(String url)
  {
    try
    {
      URL u = new URL(url);
      return encodeUrl(u).toExternalForm();
    } catch (Exception e) {}
    return url;
  }
  
  private static URL encodeUrl(URL u)
  {
    try
    {
      java.net.URI uri = new java.net.URI(u.toExternalForm());
      return new URL(uri.toASCIIString());
    } catch (Exception e) {}
    return u;
  }
  
  private static String encodeMimeName(String val)
  {
    if (val == null)
      return null;
    return val.replaceAll("\"", "%22");
  }
  


  private HttpConnection()
  {
    req = new Request(null);
    res = new Response();
  }
  
  public Connection url(URL url) {
    req.url(url);
    return this;
  }
  
  public Connection url(String url) {
    Validate.notEmpty(url, "Must supply a valid URL");
    try {
      req.url(new URL(encodeUrl(url)));
    } catch (java.net.MalformedURLException e) {
      throw new IllegalArgumentException("Malformed URL: " + url, e);
    }
    return this;
  }
  
  public Connection proxy(Proxy proxy) {
    req.proxy(proxy);
    return this;
  }
  
  public Connection proxy(String host, int port) {
    req.proxy(host, port);
    return this;
  }
  
  public Connection userAgent(String userAgent) {
    Validate.notNull(userAgent, "User agent must not be null");
    req.header("User-Agent", userAgent);
    return this;
  }
  
  public Connection timeout(int millis) {
    req.timeout(millis);
    return this;
  }
  
  public Connection maxBodySize(int bytes) {
    req.maxBodySize(bytes);
    return this;
  }
  
  public Connection followRedirects(boolean followRedirects) {
    req.followRedirects(followRedirects);
    return this;
  }
  
  public Connection referrer(String referrer) {
    Validate.notNull(referrer, "Referrer must not be null");
    req.header("Referer", referrer);
    return this;
  }
  
  public Connection method(Connection.Method method) {
    req.method(method);
    return this;
  }
  
  public Connection ignoreHttpErrors(boolean ignoreHttpErrors) {
    req.ignoreHttpErrors(ignoreHttpErrors);
    return this;
  }
  
  public Connection ignoreContentType(boolean ignoreContentType) {
    req.ignoreContentType(ignoreContentType);
    return this;
  }
  
  public Connection validateTLSCertificates(boolean value) {
    req.validateTLSCertificates(value);
    return this;
  }
  
  public Connection data(String key, String value) {
    req.data(KeyVal.create(key, value));
    return this;
  }
  
  public Connection data(String key, String filename, InputStream inputStream) {
    req.data(KeyVal.create(key, filename, inputStream));
    return this;
  }
  
  public Connection data(Map<String, String> data) {
    Validate.notNull(data, "Data map must not be null");
    for (Map.Entry<String, String> entry : data.entrySet()) {
      req.data(KeyVal.create((String)entry.getKey(), (String)entry.getValue()));
    }
    return this;
  }
  
  public Connection data(String... keyvals) {
    Validate.notNull(keyvals, "Data key value pairs must not be null");
    Validate.isTrue(keyvals.length % 2 == 0, "Must supply an even number of key value pairs");
    for (int i = 0; i < keyvals.length; i += 2) {
      String key = keyvals[i];
      String value = keyvals[(i + 1)];
      Validate.notEmpty(key, "Data key must not be empty");
      Validate.notNull(value, "Data value must not be null");
      req.data(KeyVal.create(key, value));
    }
    return this;
  }
  
  public Connection data(Collection<Connection.KeyVal> data) {
    Validate.notNull(data, "Data collection must not be null");
    for (Connection.KeyVal entry : data) {
      req.data(entry);
    }
    return this;
  }
  
  public Connection.KeyVal data(String key) {
    Validate.notEmpty(key, "Data key must not be empty");
    for (Connection.KeyVal keyVal : request().data()) {
      if (keyVal.key().equals(key))
        return keyVal;
    }
    return null;
  }
  
  public Connection requestBody(String body) {
    req.requestBody(body);
    return this;
  }
  
  public Connection header(String name, String value) {
    req.header(name, value);
    return this;
  }
  
  public Connection headers(Map<String, String> headers) {
    Validate.notNull(headers, "Header map must not be null");
    for (Map.Entry<String, String> entry : headers.entrySet()) {
      req.header((String)entry.getKey(), (String)entry.getValue());
    }
    return this;
  }
  
  public Connection cookie(String name, String value) {
    req.cookie(name, value);
    return this;
  }
  
  public Connection cookies(Map<String, String> cookies) {
    Validate.notNull(cookies, "Cookie map must not be null");
    for (Map.Entry<String, String> entry : cookies.entrySet()) {
      req.cookie((String)entry.getKey(), (String)entry.getValue());
    }
    return this;
  }
  
  public Connection parser(Parser parser) {
    req.parser(parser);
    return this;
  }
  
  public Document get() throws IOException {
    req.method(Connection.Method.GET);
    execute();
    return res.parse();
  }
  
  public Document post() throws IOException {
    req.method(Connection.Method.POST);
    execute();
    return res.parse();
  }
  
  public Connection.Response execute() throws IOException {
    res = Response.execute(req);
    return res;
  }
  
  public Connection.Request request() {
    return req;
  }
  
  public Connection request(Connection.Request request) {
    req = request;
    return this;
  }
  
  public Connection.Response response() {
    return res;
  }
  
  public Connection response(Connection.Response response) {
    res = response;
    return this;
  }
  
  public Connection postDataCharset(String charset) {
    req.postDataCharset(charset);
    return this;
  }
  
  private static abstract class Base<T extends org.jsoup.Connection.Base> implements org.jsoup.Connection.Base<T>
  {
    URL url;
    Connection.Method method;
    Map<String, String> headers;
    Map<String, String> cookies;
    
    private Base() {
      headers = new LinkedHashMap();
      cookies = new LinkedHashMap();
    }
    
    public URL url() {
      return url;
    }
    
    public T url(URL url) {
      Validate.notNull(url, "URL must not be null");
      this.url = url;
      return this;
    }
    
    public Connection.Method method() {
      return method;
    }
    
    public T method(Connection.Method method) {
      Validate.notNull(method, "Method must not be null");
      this.method = method;
      return this;
    }
    
    public String header(String name) {
      Validate.notNull(name, "Header name must not be null");
      String val = getHeaderCaseInsensitive(name);
      if (val != null)
      {
        val = fixHeaderEncoding(val);
      }
      return val;
    }
    
    private static String fixHeaderEncoding(String val) {
      try {
        byte[] bytes = val.getBytes("ISO-8859-1");
        if (!looksLikeUtf8(bytes))
          return val;
        return new String(bytes, "UTF-8");
      }
      catch (java.io.UnsupportedEncodingException e) {}
      return val;
    }
    
    private static boolean looksLikeUtf8(byte[] input)
    {
      int i = 0;
      
      if ((input.length >= 3) && ((input[0] & 0xFF) == 239)) { if ((((input[1] & 0xFF) == 187 ? 1 : 0) & ((input[2] & 0xFF) == 191 ? 1 : 0)) != 0)
        {
          i = 3;
        }
      }
      
      for (int j = input.length; i < j; i++) {
        int o = input[i];
        if ((o & 0x80) != 0)
        {
          int end;
          

          if ((o & 0xE0) == 192) {
            end = i + 1; } else { int end;
            if ((o & 0xF0) == 224) {
              end = i + 2; } else { int end;
              if ((o & 0xF8) == 240) {
                end = i + 3;
              } else
                return false;
            } }
          int end;
          while (i < end) {
            i++;
            o = input[i];
            if ((o & 0xC0) != 128)
              return false;
          }
        }
      }
      return true;
    }
    
    public T header(String name, String value) {
      Validate.notEmpty(name, "Header name must not be empty");
      Validate.notNull(value, "Header value must not be null");
      removeHeader(name);
      headers.put(name, value);
      return this;
    }
    
    public boolean hasHeader(String name) {
      Validate.notEmpty(name, "Header name must not be empty");
      return getHeaderCaseInsensitive(name) != null;
    }
    


    public boolean hasHeaderWithValue(String name, String value)
    {
      return (hasHeader(name)) && (header(name).equalsIgnoreCase(value));
    }
    
    public T removeHeader(String name) {
      Validate.notEmpty(name, "Header name must not be empty");
      Map.Entry<String, String> entry = scanHeaders(name);
      if (entry != null)
        headers.remove(entry.getKey());
      return this;
    }
    
    public Map<String, String> headers() {
      return headers;
    }
    
    private String getHeaderCaseInsensitive(String name) {
      Validate.notNull(name, "Header name must not be null");
      
      String value = (String)headers.get(name);
      if (value == null)
        value = (String)headers.get(org.jsoup.internal.Normalizer.lowerCase(name));
      if (value == null) {
        Map.Entry<String, String> entry = scanHeaders(name);
        if (entry != null)
          value = (String)entry.getValue();
      }
      return value;
    }
    
    private Map.Entry<String, String> scanHeaders(String name) {
      String lc = org.jsoup.internal.Normalizer.lowerCase(name);
      for (Map.Entry<String, String> entry : headers.entrySet()) {
        if (org.jsoup.internal.Normalizer.lowerCase((String)entry.getKey()).equals(lc))
          return entry;
      }
      return null;
    }
    
    public String cookie(String name) {
      Validate.notEmpty(name, "Cookie name must not be empty");
      return (String)cookies.get(name);
    }
    
    public T cookie(String name, String value) {
      Validate.notEmpty(name, "Cookie name must not be empty");
      Validate.notNull(value, "Cookie value must not be null");
      cookies.put(name, value);
      return this;
    }
    
    public boolean hasCookie(String name) {
      Validate.notEmpty(name, "Cookie name must not be empty");
      return cookies.containsKey(name);
    }
    
    public T removeCookie(String name) {
      Validate.notEmpty(name, "Cookie name must not be empty");
      cookies.remove(name);
      return this;
    }
    
    public Map<String, String> cookies() {
      return cookies;
    }
  }
  
  public static class Request extends HttpConnection.Base<Connection.Request> implements Connection.Request {
    private Proxy proxy;
    private int timeoutMilliseconds;
    private int maxBodySizeBytes;
    private boolean followRedirects;
    private Collection<Connection.KeyVal> data;
    private String body = null;
    private boolean ignoreHttpErrors = false;
    private boolean ignoreContentType = false;
    private Parser parser;
    private boolean parserDefined = false;
    private boolean validateTSLCertificates = true;
    private String postDataCharset = "UTF-8";
    
    private Request() { super();
      timeoutMilliseconds = 30000;
      maxBodySizeBytes = 1048576;
      followRedirects = true;
      data = new ArrayList();
      method = Connection.Method.GET;
      headers.put("Accept-Encoding", "gzip");
      headers.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36");
      parser = Parser.htmlParser();
    }
    
    public Proxy proxy() {
      return proxy;
    }
    
    public Request proxy(Proxy proxy) {
      this.proxy = proxy;
      return this;
    }
    
    public Request proxy(String host, int port) {
      proxy = new Proxy(java.net.Proxy.Type.HTTP, java.net.InetSocketAddress.createUnresolved(host, port));
      return this;
    }
    
    public int timeout() {
      return timeoutMilliseconds;
    }
    
    public Request timeout(int millis) {
      Validate.isTrue(millis >= 0, "Timeout milliseconds must be 0 (infinite) or greater");
      timeoutMilliseconds = millis;
      return this;
    }
    
    public int maxBodySize() {
      return maxBodySizeBytes;
    }
    
    public Connection.Request maxBodySize(int bytes) {
      Validate.isTrue(bytes >= 0, "maxSize must be 0 (unlimited) or larger");
      maxBodySizeBytes = bytes;
      return this;
    }
    
    public boolean followRedirects() {
      return followRedirects;
    }
    
    public Connection.Request followRedirects(boolean followRedirects) {
      this.followRedirects = followRedirects;
      return this;
    }
    
    public boolean ignoreHttpErrors() {
      return ignoreHttpErrors;
    }
    
    public boolean validateTLSCertificates() {
      return validateTSLCertificates;
    }
    
    public void validateTLSCertificates(boolean value) {
      validateTSLCertificates = value;
    }
    
    public Connection.Request ignoreHttpErrors(boolean ignoreHttpErrors) {
      this.ignoreHttpErrors = ignoreHttpErrors;
      return this;
    }
    
    public boolean ignoreContentType() {
      return ignoreContentType;
    }
    
    public Connection.Request ignoreContentType(boolean ignoreContentType) {
      this.ignoreContentType = ignoreContentType;
      return this;
    }
    
    public Request data(Connection.KeyVal keyval) {
      Validate.notNull(keyval, "Key val must not be null");
      data.add(keyval);
      return this;
    }
    
    public Collection<Connection.KeyVal> data() {
      return data;
    }
    
    public Connection.Request requestBody(String body) {
      this.body = body;
      return this;
    }
    
    public String requestBody() {
      return body;
    }
    
    public Request parser(Parser parser) {
      this.parser = parser;
      parserDefined = true;
      return this;
    }
    
    public Parser parser() {
      return parser;
    }
    
    public Connection.Request postDataCharset(String charset) {
      Validate.notNull(charset, "Charset must not be null");
      if (!Charset.isSupported(charset)) throw new java.nio.charset.IllegalCharsetNameException(charset);
      postDataCharset = charset;
      return this;
    }
    
    public String postDataCharset() {
      return postDataCharset;
    }
  }
  
  public static class Response extends HttpConnection.Base<Connection.Response> implements Connection.Response {
    private static final int MAX_REDIRECTS = 20;
    private static javax.net.ssl.SSLSocketFactory sslSocketFactory;
    private static final String LOCATION = "Location";
    private int statusCode;
    private String statusMessage;
    private java.nio.ByteBuffer byteData;
    private String charset;
    private String contentType;
    private boolean executed = false;
    private int numRedirects = 0;
    

    private Connection.Request req;
    

    private static final Pattern xmlContentTypeRxp = Pattern.compile("(application|text)/\\w*\\+?xml.*");
    
    Response() {
      super();
    }
    
    private Response(Response previousResponse) throws IOException {
      super();
      if (previousResponse != null) {
        numRedirects += 1;
        if (numRedirects >= 20)
          throw new IOException(String.format("Too many redirects occurred trying to load URL %s", new Object[] { previousResponse.url() }));
      }
    }
    
    static Response execute(Connection.Request req) throws IOException {
      return execute(req, null);
    }
    
    static Response execute(Connection.Request req, Response previousResponse) throws IOException {
      Validate.notNull(req, "Request must not be null");
      String protocol = req.url().getProtocol();
      if ((!protocol.equals("http")) && (!protocol.equals("https")))
        throw new java.net.MalformedURLException("Only http & https protocols supported");
      boolean methodHasBody = req.method().hasBody();
      boolean hasRequestBody = req.requestBody() != null;
      if (!methodHasBody) {
        Validate.isFalse(hasRequestBody, "Cannot set a request body for HTTP method " + req.method());
      }
      
      String mimeBoundary = null;
      if ((req.data().size() > 0) && ((!methodHasBody) || (hasRequestBody))) {
        serialiseRequestUrl(req);
      } else if (methodHasBody) {
        mimeBoundary = setOutputContentType(req);
      }
      HttpURLConnection conn = createConnection(req);
      try
      {
        conn.connect();
        if (conn.getDoOutput()) {
          writePost(req, conn.getOutputStream(), mimeBoundary);
        }
        int status = conn.getResponseCode();
        Response res = new Response(previousResponse);
        res.setupFromConnection(conn, previousResponse);
        req = req;
        

        if ((res.hasHeader("Location")) && (req.followRedirects())) {
          if (status != 307) {
            req.method(Connection.Method.GET);
            req.data().clear();
            req.requestBody(null);
            req.removeHeader("Content-Type");
          }
          
          String location = res.header("Location");
          if ((location != null) && (location.startsWith("http:/")) && (location.charAt(6) != '/'))
            location = location.substring(6);
          URL redir = StringUtil.resolve(req.url(), location);
          req.url(HttpConnection.encodeUrl(redir));
          
          for (Object localObject1 = cookies.entrySet().iterator(); ((Iterator)localObject1).hasNext();) { Map.Entry<String, String> cookie = (Map.Entry)((Iterator)localObject1).next();
            req.cookie((String)cookie.getKey(), (String)cookie.getValue());
          }
          return execute(req, res);
        }
        if (((status < 200) || (status >= 400)) && (!req.ignoreHttpErrors())) {
          throw new org.jsoup.HttpStatusException("HTTP error fetching URL", status, req.url().toString());
        }
        
        String contentType = res.contentType();
        if ((contentType != null) && 
          (!req.ignoreContentType()) && 
          (!contentType.startsWith("text/")) && 
          (!xmlContentTypeRxp.matcher(contentType).matches()))
        {

          throw new org.jsoup.UnsupportedMimeTypeException("Unhandled content type. Must be text/*, application/xml, or application/xhtml+xml", contentType, req.url().toString());
        }
        
        if ((contentType != null) && (xmlContentTypeRxp.matcher(contentType).matches()))
        {
          if (((req instanceof HttpConnection.Request)) && (!HttpConnection.Request.access$300((HttpConnection.Request)req))) {
            req.parser(Parser.xmlParser());
          }
        }
        
        charset = DataUtil.getCharsetFromContentType(contentType);
        if ((conn.getContentLength() != 0) && (req.method() != Connection.Method.HEAD)) {
          InputStream bodyStream = null;
          try {
            bodyStream = conn.getErrorStream() != null ? conn.getErrorStream() : conn.getInputStream();
            if (res.hasHeaderWithValue("Content-Encoding", "gzip")) {
              bodyStream = new java.util.zip.GZIPInputStream(bodyStream);
            }
            byteData = DataUtil.readToByteBuffer(bodyStream, req.maxBodySize());
          } finally {
            if (bodyStream == null) {}
          }
        } else {
          byteData = DataUtil.emptyByteBuffer();
        }
      }
      finally
      {
        conn.disconnect();
      }
      Response res;
      executed = true;
      return res;
    }
    
    public int statusCode() {
      return statusCode;
    }
    
    public String statusMessage() {
      return statusMessage;
    }
    
    public String charset() {
      return charset;
    }
    
    public Response charset(String charset) {
      this.charset = charset;
      return this;
    }
    
    public String contentType() {
      return contentType;
    }
    
    public Document parse() throws IOException {
      Validate.isTrue(executed, "Request must be executed (with .execute(), .get(), or .post() before parsing response");
      Document doc = DataUtil.parseByteData(byteData, charset, url.toExternalForm(), req.parser());
      byteData.rewind();
      charset = doc.outputSettings().charset().name();
      return doc;
    }
    
    public String body() {
      Validate.isTrue(executed, "Request must be executed (with .execute(), .get(), or .post() before getting response body");
      String body;
      String body;
      if (charset == null) {
        body = Charset.forName("UTF-8").decode(byteData).toString();
      } else
        body = Charset.forName(charset).decode(byteData).toString();
      byteData.rewind();
      return body;
    }
    
    public byte[] bodyAsBytes() {
      Validate.isTrue(executed, "Request must be executed (with .execute(), .get(), or .post() before getting response body");
      return byteData.array();
    }
    


    private static HttpURLConnection createConnection(Connection.Request req)
      throws IOException
    {
      HttpURLConnection conn = (HttpURLConnection)(req.proxy() == null ? req.url().openConnection() : req.url().openConnection(req.proxy()));
      

      conn.setRequestMethod(req.method().name());
      conn.setInstanceFollowRedirects(false);
      conn.setConnectTimeout(req.timeout());
      conn.setReadTimeout(req.timeout());
      
      if (((conn instanceof HttpsURLConnection)) && 
        (!req.validateTLSCertificates())) {
        initUnSecureTSL();
        ((HttpsURLConnection)conn).setSSLSocketFactory(sslSocketFactory);
        ((HttpsURLConnection)conn).setHostnameVerifier(getInsecureVerifier());
      }
      

      if (req.method().hasBody())
        conn.setDoOutput(true);
      if (req.cookies().size() > 0)
        conn.addRequestProperty("Cookie", getRequestCookieString(req));
      for (Map.Entry<String, String> header : req.headers().entrySet()) {
        conn.addRequestProperty((String)header.getKey(), (String)header.getValue());
      }
      return conn;
    }
    






    private static javax.net.ssl.HostnameVerifier getInsecureVerifier()
    {
      new javax.net.ssl.HostnameVerifier() {
        public boolean verify(String urlHostName, javax.net.ssl.SSLSession session) {
          return true;
        }
      };
    }
    







    private static synchronized void initUnSecureTSL()
      throws IOException
    {
      if (sslSocketFactory == null)
      {
        javax.net.ssl.TrustManager[] trustAllCerts = { new javax.net.ssl.X509TrustManager()
        {
          public void checkClientTrusted(X509Certificate[] chain, String authType) {}
          

          public void checkServerTrusted(X509Certificate[] chain, String authType) {}
          
          public X509Certificate[] getAcceptedIssuers()
          {
            return null;
          }
        } };
        

        try
        {
          SSLContext sslContext = SSLContext.getInstance("SSL");
          sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
          
          sslSocketFactory = sslContext.getSocketFactory();
        } catch (java.security.NoSuchAlgorithmException e) {
          throw new IOException("Can't create unsecure trust manager");
        } catch (java.security.KeyManagementException e) {
          throw new IOException("Can't create unsecure trust manager");
        }
      }
    }
    
    private void setupFromConnection(HttpURLConnection conn, Connection.Response previousResponse)
      throws IOException
    {
      method = Connection.Method.valueOf(conn.getRequestMethod());
      url = conn.getURL();
      statusCode = conn.getResponseCode();
      statusMessage = conn.getResponseMessage();
      contentType = conn.getContentType();
      
      Map<String, List<String>> resHeaders = createHeaderMap(conn);
      processResponseHeaders(resHeaders);
      

      if (previousResponse != null) {
        for (Map.Entry<String, String> prevCookie : previousResponse.cookies().entrySet()) {
          if (!hasCookie((String)prevCookie.getKey())) {
            cookie((String)prevCookie.getKey(), (String)prevCookie.getValue());
          }
        }
      }
    }
    
    private static LinkedHashMap<String, List<String>> createHeaderMap(HttpURLConnection conn) {
      LinkedHashMap<String, List<String>> headers = new LinkedHashMap();
      int i = 0;
      for (;;) {
        String key = conn.getHeaderFieldKey(i);
        String val = conn.getHeaderField(i);
        if ((key == null) && (val == null))
          break;
        i++;
        if ((key != null) && (val != null))
        {

          if (headers.containsKey(key)) {
            ((List)headers.get(key)).add(val);
          } else {
            ArrayList<String> vals = new ArrayList();
            vals.add(val);
            headers.put(key, vals);
          } }
      }
      return headers;
    }
    
    void processResponseHeaders(Map<String, List<String>> resHeaders) {
      for (Map.Entry<String, List<String>> entry : resHeaders.entrySet()) {
        String name = (String)entry.getKey();
        if (name != null)
        {

          List<String> values = (List)entry.getValue();
          if (name.equalsIgnoreCase("Set-Cookie")) {
            for (String value : values) {
              if (value != null)
              {
                org.jsoup.parser.TokenQueue cd = new org.jsoup.parser.TokenQueue(value);
                String cookieName = cd.chompTo("=").trim();
                String cookieVal = cd.consumeTo(";").trim();
                

                if (cookieName.length() > 0)
                  cookie(cookieName, cookieVal);
              }
            }
          } else if (values.size() == 1) {
            header(name, (String)values.get(0));
          } else if (values.size() > 1) {
            StringBuilder accum = new StringBuilder();
            for (int i = 0; i < values.size(); i++) {
              String val = (String)values.get(i);
              if (i != 0)
                accum.append(", ");
              accum.append(val);
            }
            header(name, accum.toString());
          }
        }
      }
    }
    
    private static String setOutputContentType(Connection.Request req) {
      String bound = null;
      if (!req.hasHeader("Content-Type"))
      {


        if (HttpConnection.needsMultipart(req)) {
          bound = DataUtil.mimeBoundary();
          req.header("Content-Type", "multipart/form-data; boundary=" + bound);
        } else {
          req.header("Content-Type", "application/x-www-form-urlencoded; charset=" + req.postDataCharset());
        } }
      return bound;
    }
    
    private static void writePost(Connection.Request req, java.io.OutputStream outputStream, String bound) throws IOException {
      Collection<Connection.KeyVal> data = req.data();
      BufferedWriter w = new BufferedWriter(new java.io.OutputStreamWriter(outputStream, req.postDataCharset()));
      Connection.KeyVal keyVal;
      boolean first; if (bound != null)
      {
        for (Iterator localIterator = data.iterator(); localIterator.hasNext();) { keyVal = (Connection.KeyVal)localIterator.next();
          w.write("--");
          w.write(bound);
          w.write("\r\n");
          w.write("Content-Disposition: form-data; name=\"");
          w.write(HttpConnection.encodeMimeName(keyVal.key()));
          w.write("\"");
          if (keyVal.hasInputStream()) {
            w.write("; filename=\"");
            w.write(HttpConnection.encodeMimeName(keyVal.value()));
            w.write("\"\r\nContent-Type: application/octet-stream\r\n\r\n");
            w.flush();
            DataUtil.crossStreams(keyVal.inputStream(), outputStream);
            outputStream.flush();
          } else {
            w.write("\r\n\r\n");
            w.write(keyVal.value());
          }
          w.write("\r\n");
        }
        w.write("--");
        w.write(bound);
        w.write("--");
      } else if (req.requestBody() != null)
      {
        w.write(req.requestBody());
      }
      else
      {
        first = true;
        for (Connection.KeyVal keyVal : data) {
          if (!first) {
            w.append('&');
          } else {
            first = false;
          }
          w.write(java.net.URLEncoder.encode(keyVal.key(), req.postDataCharset()));
          w.write(61);
          w.write(java.net.URLEncoder.encode(keyVal.value(), req.postDataCharset()));
        }
      }
      w.close();
    }
    
    private static String getRequestCookieString(Connection.Request req) {
      StringBuilder sb = new StringBuilder();
      boolean first = true;
      for (Map.Entry<String, String> cookie : req.cookies().entrySet()) {
        if (!first) {
          sb.append("; ");
        } else
          first = false;
        sb.append((String)cookie.getKey()).append('=').append((String)cookie.getValue());
      }
      
      return sb.toString();
    }
    
    private static void serialiseRequestUrl(Connection.Request req) throws IOException
    {
      URL in = req.url();
      StringBuilder url = new StringBuilder();
      boolean first = true;
      
      url
        .append(in.getProtocol())
        .append("://")
        .append(in.getAuthority())
        .append(in.getPath())
        .append("?");
      if (in.getQuery() != null) {
        url.append(in.getQuery());
        first = false;
      }
      for (Connection.KeyVal keyVal : req.data()) {
        Validate.isFalse(keyVal.hasInputStream(), "InputStream data not supported in URL query string.");
        if (!first) {
          url.append('&');
        } else {
          first = false;
        }
        

        url.append(java.net.URLEncoder.encode(keyVal.key(), "UTF-8")).append('=').append(java.net.URLEncoder.encode(keyVal.value(), "UTF-8"));
      }
      req.url(new URL(url.toString()));
      req.data().clear();
    }
  }
  
  private static boolean needsMultipart(Connection.Request req)
  {
    boolean needsMulti = false;
    for (Connection.KeyVal keyVal : req.data()) {
      if (keyVal.hasInputStream()) {
        needsMulti = true;
        break;
      }
    }
    return needsMulti;
  }
  
  public static class KeyVal implements Connection.KeyVal {
    private String key;
    private String value;
    private InputStream stream;
    
    public static KeyVal create(String key, String value) {
      return new KeyVal().key(key).value(value);
    }
    
    public static KeyVal create(String key, String filename, InputStream stream) {
      return new KeyVal().key(key).value(filename).inputStream(stream);
    }
    
    private KeyVal() {}
    
    public KeyVal key(String key) {
      Validate.notEmpty(key, "Data key must not be empty");
      this.key = key;
      return this;
    }
    
    public String key() {
      return key;
    }
    
    public KeyVal value(String value) {
      Validate.notNull(value, "Data value must not be null");
      this.value = value;
      return this;
    }
    
    public String value() {
      return value;
    }
    
    public KeyVal inputStream(InputStream inputStream) {
      Validate.notNull(value, "Data input stream must not be null");
      stream = inputStream;
      return this;
    }
    
    public InputStream inputStream() {
      return stream;
    }
    
    public boolean hasInputStream() {
      return stream != null;
    }
    
    public String toString()
    {
      return key + "=" + value;
    }
  }
}
