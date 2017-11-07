package org.jsoup;
import java.util.Map;

public abstract interface Connection { public static abstract interface KeyVal { public abstract KeyVal key(String paramString);
    public abstract String key();
    public abstract KeyVal value(String paramString);
    public abstract String value();
    public abstract KeyVal inputStream(java.io.InputStream paramInputStream);
    public abstract java.io.InputStream inputStream();
    public abstract boolean hasInputStream(); }
  public static abstract interface Response extends Connection.Base<Response> { public abstract int statusCode();
    public abstract String statusMessage();
    public abstract String charset();
    public abstract Response charset(String paramString);
    public abstract String contentType();
    public abstract org.jsoup.nodes.Document parse() throws java.io.IOException;
    public abstract String body();
    public abstract byte[] bodyAsBytes(); }
  public static abstract interface Request extends Connection.Base<Request> { public abstract java.net.Proxy proxy();
    public abstract Request proxy(java.net.Proxy paramProxy);
    public abstract Request proxy(String paramString, int paramInt);
    public abstract int timeout();
    public abstract Request timeout(int paramInt);
    public abstract int maxBodySize();
    public abstract Request maxBodySize(int paramInt);
    public abstract boolean followRedirects();
    public abstract Request followRedirects(boolean paramBoolean);
    public abstract boolean ignoreHttpErrors();
    public abstract Request ignoreHttpErrors(boolean paramBoolean);
    public abstract boolean ignoreContentType();
    public abstract Request ignoreContentType(boolean paramBoolean); public abstract boolean validateTLSCertificates(); public abstract void validateTLSCertificates(boolean paramBoolean); public abstract Request data(Connection.KeyVal paramKeyVal); public abstract java.util.Collection<Connection.KeyVal> data(); public abstract Request requestBody(String paramString); public abstract String requestBody(); public abstract Request parser(org.jsoup.parser.Parser paramParser); public abstract org.jsoup.parser.Parser parser(); public abstract Request postDataCharset(String paramString); public abstract String postDataCharset(); } public static abstract interface Base<T extends Base> { public abstract java.net.URL url(); public abstract T url(java.net.URL paramURL); public abstract Connection.Method method(); public abstract T method(Connection.Method paramMethod); public abstract String header(String paramString); public abstract T header(String paramString1, String paramString2); public abstract boolean hasHeader(String paramString); public abstract boolean hasHeaderWithValue(String paramString1, String paramString2); public abstract T removeHeader(String paramString); public abstract Map<String, String> headers(); public abstract String cookie(String paramString); public abstract T cookie(String paramString1, String paramString2); public abstract boolean hasCookie(String paramString); public abstract T removeCookie(String paramString); public abstract Map<String, String> cookies(); } public static enum Method { GET(false),  POST(true),  PUT(true),  DELETE(false),  PATCH(true),  HEAD(false),  OPTIONS(false),  TRACE(false);
    
    private final boolean hasBody;
    
    private Method(boolean hasBody) {
      this.hasBody = hasBody;
    }
    



    public final boolean hasBody()
    {
      return hasBody;
    }
  }
  
  public abstract Connection url(java.net.URL paramURL);
  
  public abstract Connection url(String paramString);
  
  public abstract Connection proxy(java.net.Proxy paramProxy);
  
  public abstract Connection proxy(String paramString, int paramInt);
  
  public abstract Connection userAgent(String paramString);
  
  public abstract Connection timeout(int paramInt);
  
  public abstract Connection maxBodySize(int paramInt);
  
  public abstract Connection referrer(String paramString);
  
  public abstract Connection followRedirects(boolean paramBoolean);
  
  public abstract Connection method(Method paramMethod);
  
  public abstract Connection ignoreHttpErrors(boolean paramBoolean);
  
  public abstract Connection ignoreContentType(boolean paramBoolean);
  
  public abstract Connection validateTLSCertificates(boolean paramBoolean);
  
  public abstract Connection data(String paramString1, String paramString2);
  
  public abstract Connection data(String paramString1, String paramString2, java.io.InputStream paramInputStream);
  
  public abstract Connection data(java.util.Collection<KeyVal> paramCollection);
  
  public abstract Connection data(Map<String, String> paramMap);
  
  public abstract Connection data(String... paramVarArgs);
  
  public abstract KeyVal data(String paramString);
  
  public abstract Connection requestBody(String paramString);
  
  public abstract Connection header(String paramString1, String paramString2);
  
  public abstract Connection headers(Map<String, String> paramMap);
  
  public abstract Connection cookie(String paramString1, String paramString2);
  
  public abstract Connection cookies(Map<String, String> paramMap);
  
  public abstract Connection parser(org.jsoup.parser.Parser paramParser);
  
  public abstract Connection postDataCharset(String paramString);
  
  public abstract org.jsoup.nodes.Document get()
    throws java.io.IOException;
  
  public abstract org.jsoup.nodes.Document post()
    throws java.io.IOException;
  
  public abstract Response execute()
    throws java.io.IOException;
  
  public abstract Request request();
  
  public abstract Connection request(Request paramRequest);
  
  public abstract Response response();
  
  public abstract Connection response(Response paramResponse);
}
