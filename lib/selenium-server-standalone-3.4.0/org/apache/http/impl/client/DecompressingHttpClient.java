package org.apache.http.impl.client;

import java.io.IOException;
import java.net.URI;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.RequestAcceptEncoding;
import org.apache.http.client.protocol.ResponseContentEncoding;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

























































@Deprecated
public class DecompressingHttpClient
  implements HttpClient
{
  private final HttpClient backend;
  private final HttpRequestInterceptor acceptEncodingInterceptor;
  private final HttpResponseInterceptor contentEncodingInterceptor;
  
  public DecompressingHttpClient()
  {
    this(new DefaultHttpClient());
  }
  





  public DecompressingHttpClient(HttpClient backend)
  {
    this(backend, new RequestAcceptEncoding(), new ResponseContentEncoding());
  }
  

  DecompressingHttpClient(HttpClient backend, HttpRequestInterceptor requestInterceptor, HttpResponseInterceptor responseInterceptor)
  {
    this.backend = backend;
    acceptEncodingInterceptor = requestInterceptor;
    contentEncodingInterceptor = responseInterceptor;
  }
  
  public HttpParams getParams()
  {
    return backend.getParams();
  }
  
  public ClientConnectionManager getConnectionManager()
  {
    return backend.getConnectionManager();
  }
  
  public HttpResponse execute(HttpUriRequest request)
    throws IOException, ClientProtocolException
  {
    return execute(getHttpHost(request), request, (HttpContext)null);
  }
  




  public HttpClient getHttpClient()
  {
    return backend;
  }
  
  HttpHost getHttpHost(HttpUriRequest request) {
    URI uri = request.getURI();
    return URIUtils.extractHost(uri);
  }
  
  public HttpResponse execute(HttpUriRequest request, HttpContext context)
    throws IOException, ClientProtocolException
  {
    return execute(getHttpHost(request), request, context);
  }
  
  public HttpResponse execute(HttpHost target, HttpRequest request)
    throws IOException, ClientProtocolException
  {
    return execute(target, request, (HttpContext)null);
  }
  
  /* Error */
  public HttpResponse execute(HttpHost target, HttpRequest request, HttpContext context)
    throws IOException, ClientProtocolException
  {
    // Byte code:
    //   0: aload_3
    //   1: ifnull +7 -> 8
    //   4: aload_3
    //   5: goto +10 -> 15
    //   8: new 20	org/apache/http/protocol/BasicHttpContext
    //   11: dup
    //   12: invokespecial 21	org/apache/http/protocol/BasicHttpContext:<init>	()V
    //   15: astore 4
    //   17: aload_2
    //   18: instanceof 22
    //   21: ifeq +19 -> 40
    //   24: new 23	org/apache/http/impl/client/EntityEnclosingRequestWrapper
    //   27: dup
    //   28: aload_2
    //   29: checkcast 22	org/apache/http/HttpEntityEnclosingRequest
    //   32: invokespecial 24	org/apache/http/impl/client/EntityEnclosingRequestWrapper:<init>	(Lorg/apache/http/HttpEntityEnclosingRequest;)V
    //   35: astore 5
    //   37: goto +13 -> 50
    //   40: new 25	org/apache/http/impl/client/RequestWrapper
    //   43: dup
    //   44: aload_2
    //   45: invokespecial 26	org/apache/http/impl/client/RequestWrapper:<init>	(Lorg/apache/http/HttpRequest;)V
    //   48: astore 5
    //   50: aload_0
    //   51: getfield 11	org/apache/http/impl/client/DecompressingHttpClient:acceptEncodingInterceptor	Lorg/apache/http/HttpRequestInterceptor;
    //   54: aload 5
    //   56: aload 4
    //   58: invokeinterface 27 3 0
    //   63: aload_0
    //   64: getfield 10	org/apache/http/impl/client/DecompressingHttpClient:backend	Lorg/apache/http/client/HttpClient;
    //   67: aload_1
    //   68: aload 5
    //   70: aload 4
    //   72: invokeinterface 28 4 0
    //   77: astore 6
    //   79: aload_0
    //   80: getfield 12	org/apache/http/impl/client/DecompressingHttpClient:contentEncodingInterceptor	Lorg/apache/http/HttpResponseInterceptor;
    //   83: aload 6
    //   85: aload 4
    //   87: invokeinterface 29 3 0
    //   92: getstatic 30	java/lang/Boolean:TRUE	Ljava/lang/Boolean;
    //   95: aload 4
    //   97: ldc 31
    //   99: invokeinterface 32 2 0
    //   104: invokevirtual 33	java/lang/Boolean:equals	(Ljava/lang/Object;)Z
    //   107: ifeq +30 -> 137
    //   110: aload 6
    //   112: ldc 34
    //   114: invokeinterface 35 2 0
    //   119: aload 6
    //   121: ldc 36
    //   123: invokeinterface 35 2 0
    //   128: aload 6
    //   130: ldc 37
    //   132: invokeinterface 35 2 0
    //   137: aload 6
    //   139: areturn
    //   140: astore 7
    //   142: aload 6
    //   144: invokeinterface 39 1 0
    //   149: invokestatic 40	org/apache/http/util/EntityUtils:consume	(Lorg/apache/http/HttpEntity;)V
    //   152: aload 7
    //   154: athrow
    //   155: astore 7
    //   157: aload 6
    //   159: invokeinterface 39 1 0
    //   164: invokestatic 40	org/apache/http/util/EntityUtils:consume	(Lorg/apache/http/HttpEntity;)V
    //   167: aload 7
    //   169: athrow
    //   170: astore 7
    //   172: aload 6
    //   174: invokeinterface 39 1 0
    //   179: invokestatic 40	org/apache/http/util/EntityUtils:consume	(Lorg/apache/http/HttpEntity;)V
    //   182: aload 7
    //   184: athrow
    //   185: astore 4
    //   187: new 43	org/apache/http/client/ClientProtocolException
    //   190: dup
    //   191: aload 4
    //   193: invokespecial 44	org/apache/http/client/ClientProtocolException:<init>	(Ljava/lang/Throwable;)V
    //   196: athrow
    // Line number table:
    //   Java source line #156	-> byte code offset #0
    //   Java source line #158	-> byte code offset #17
    //   Java source line #159	-> byte code offset #24
    //   Java source line #161	-> byte code offset #40
    //   Java source line #163	-> byte code offset #50
    //   Java source line #164	-> byte code offset #63
    //   Java source line #166	-> byte code offset #79
    //   Java source line #167	-> byte code offset #92
    //   Java source line #168	-> byte code offset #110
    //   Java source line #169	-> byte code offset #119
    //   Java source line #170	-> byte code offset #128
    //   Java source line #172	-> byte code offset #137
    //   Java source line #173	-> byte code offset #140
    //   Java source line #174	-> byte code offset #142
    //   Java source line #175	-> byte code offset #152
    //   Java source line #176	-> byte code offset #155
    //   Java source line #177	-> byte code offset #157
    //   Java source line #178	-> byte code offset #167
    //   Java source line #179	-> byte code offset #170
    //   Java source line #180	-> byte code offset #172
    //   Java source line #181	-> byte code offset #182
    //   Java source line #183	-> byte code offset #185
    //   Java source line #184	-> byte code offset #187
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	197	0	this	DecompressingHttpClient
    //   0	197	1	target	HttpHost
    //   0	197	2	request	HttpRequest
    //   0	197	3	context	HttpContext
    //   15	81	4	localContext	HttpContext
    //   185	7	4	e	org.apache.http.HttpException
    //   35	3	5	wrapped	HttpRequest
    //   48	21	5	wrapped	HttpRequest
    //   77	96	6	response	HttpResponse
    //   140	13	7	ex	org.apache.http.HttpException
    //   155	13	7	ex	IOException
    //   170	13	7	ex	RuntimeException
    // Exception table:
    //   from	to	target	type
    //   79	139	140	org/apache/http/HttpException
    //   79	139	155	java/io/IOException
    //   79	139	170	java/lang/RuntimeException
    //   0	139	185	org/apache/http/HttpException
    //   140	185	185	org/apache/http/HttpException
  }
  
  public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler)
    throws IOException, ClientProtocolException
  {
    return execute(getHttpHost(request), request, responseHandler);
  }
  

  public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler, HttpContext context)
    throws IOException, ClientProtocolException
  {
    return execute(getHttpHost(request), request, responseHandler, context);
  }
  

  public <T> T execute(HttpHost target, HttpRequest request, ResponseHandler<? extends T> responseHandler)
    throws IOException, ClientProtocolException
  {
    return execute(target, request, responseHandler, null);
  }
  

  public <T> T execute(HttpHost target, HttpRequest request, ResponseHandler<? extends T> responseHandler, HttpContext context)
    throws IOException, ClientProtocolException
  {
    HttpResponse response = execute(target, request, context);
    try { HttpEntity entity;
      return responseHandler.handleResponse(response);
    } finally {
      HttpEntity entity = response.getEntity();
      if (entity != null) {
        EntityUtils.consume(entity);
      }
    }
  }
}
