package org.apache.http.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpServerConnection;
import org.apache.http.StatusLine;
import org.apache.http.config.MessageConstraints;
import org.apache.http.entity.ContentLengthStrategy;
import org.apache.http.impl.entity.DisallowIdentityContentLengthStrategy;
import org.apache.http.impl.io.DefaultHttpRequestParserFactory;
import org.apache.http.impl.io.DefaultHttpResponseWriterFactory;
import org.apache.http.io.HttpMessageParser;
import org.apache.http.io.HttpMessageParserFactory;
import org.apache.http.io.HttpMessageWriter;
import org.apache.http.io.HttpMessageWriterFactory;
import org.apache.http.util.Args;



























































public class DefaultBHttpServerConnection
  extends BHttpConnectionBase
  implements HttpServerConnection
{
  private final HttpMessageParser<HttpRequest> requestParser;
  private final HttpMessageWriter<HttpResponse> responseWriter;
  
  public DefaultBHttpServerConnection(int buffersize, int fragmentSizeHint, CharsetDecoder chardecoder, CharsetEncoder charencoder, MessageConstraints constraints, ContentLengthStrategy incomingContentStrategy, ContentLengthStrategy outgoingContentStrategy, HttpMessageParserFactory<HttpRequest> requestParserFactory, HttpMessageWriterFactory<HttpResponse> responseWriterFactory)
  {
    super(buffersize, fragmentSizeHint, chardecoder, charencoder, constraints, incomingContentStrategy != null ? incomingContentStrategy : DisallowIdentityContentLengthStrategy.INSTANCE, outgoingContentStrategy);
    

    requestParser = (requestParserFactory != null ? requestParserFactory : DefaultHttpRequestParserFactory.INSTANCE).create(getSessionInputBuffer(), constraints);
    
    responseWriter = (responseWriterFactory != null ? responseWriterFactory : DefaultHttpResponseWriterFactory.INSTANCE).create(getSessionOutputBuffer());
  }
  




  public DefaultBHttpServerConnection(int buffersize, CharsetDecoder chardecoder, CharsetEncoder charencoder, MessageConstraints constraints)
  {
    this(buffersize, buffersize, chardecoder, charencoder, constraints, null, null, null, null);
  }
  
  public DefaultBHttpServerConnection(int buffersize) {
    this(buffersize, buffersize, null, null, null, null, null, null, null);
  }
  

  protected void onRequestReceived(HttpRequest request) {}
  
  protected void onResponseSubmitted(HttpResponse response) {}
  
  public void bind(Socket socket)
    throws IOException
  {
    super.bind(socket);
  }
  
  public HttpRequest receiveRequestHeader()
    throws HttpException, IOException
  {
    ensureOpen();
    HttpRequest request = (HttpRequest)requestParser.parse();
    onRequestReceived(request);
    incrementRequestCount();
    return request;
  }
  
  public void receiveRequestEntity(HttpEntityEnclosingRequest request)
    throws HttpException, IOException
  {
    Args.notNull(request, "HTTP request");
    ensureOpen();
    HttpEntity entity = prepareInput(request);
    request.setEntity(entity);
  }
  
  public void sendResponseHeader(HttpResponse response)
    throws HttpException, IOException
  {
    Args.notNull(response, "HTTP response");
    ensureOpen();
    responseWriter.write(response);
    onResponseSubmitted(response);
    if (response.getStatusLine().getStatusCode() >= 200) {
      incrementResponseCount();
    }
  }
  
  public void sendResponseEntity(HttpResponse response)
    throws HttpException, IOException
  {
    Args.notNull(response, "HTTP response");
    ensureOpen();
    HttpEntity entity = response.getEntity();
    if (entity == null) {
      return;
    }
    OutputStream outstream = prepareOutput(response);
    entity.writeTo(outstream);
    outstream.close();
  }
  
  public void flush() throws IOException
  {
    ensureOpen();
    doFlush();
  }
}
