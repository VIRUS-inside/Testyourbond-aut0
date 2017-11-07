package org.apache.http.impl.conn;

import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseFactory;
import org.apache.http.NoHttpResponseException;
import org.apache.http.ProtocolException;
import org.apache.http.StatusLine;
import org.apache.http.config.MessageConstraints;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.io.AbstractMessageParser;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.message.LineParser;
import org.apache.http.message.ParserCursor;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;



































public class DefaultHttpResponseParser
  extends AbstractMessageParser<HttpResponse>
{
  private final Log log = LogFactory.getLog(getClass());
  


  private final HttpResponseFactory responseFactory;
  


  private final CharArrayBuffer lineBuf;
  


  @Deprecated
  public DefaultHttpResponseParser(SessionInputBuffer buffer, LineParser parser, HttpResponseFactory responseFactory, HttpParams params)
  {
    super(buffer, parser, params);
    Args.notNull(responseFactory, "Response factory");
    this.responseFactory = responseFactory;
    lineBuf = new CharArrayBuffer(128);
  }
  
















  public DefaultHttpResponseParser(SessionInputBuffer buffer, LineParser lineParser, HttpResponseFactory responseFactory, MessageConstraints constraints)
  {
    super(buffer, lineParser, constraints);
    this.responseFactory = (responseFactory != null ? responseFactory : DefaultHttpResponseFactory.INSTANCE);
    
    lineBuf = new CharArrayBuffer(128);
  }
  









  public DefaultHttpResponseParser(SessionInputBuffer buffer, MessageConstraints constraints)
  {
    this(buffer, null, null, constraints);
  }
  






  public DefaultHttpResponseParser(SessionInputBuffer buffer)
  {
    this(buffer, null, null, MessageConstraints.DEFAULT);
  }
  

  protected HttpResponse parseHead(SessionInputBuffer sessionBuffer)
    throws IOException, HttpException
  {
    int count = 0;
    ParserCursor cursor = null;
    for (;;)
    {
      lineBuf.clear();
      int i = sessionBuffer.readLine(lineBuf);
      if ((i == -1) && (count == 0))
      {
        throw new NoHttpResponseException("The target server failed to respond");
      }
      cursor = new ParserCursor(0, lineBuf.length());
      if (lineParser.hasProtocolVersion(lineBuf, cursor)) {
        break;
      }
      if ((i == -1) || (reject(lineBuf, count)))
      {
        throw new ProtocolException("The server failed to respond with a valid HTTP response");
      }
      
      if (log.isDebugEnabled()) {
        log.debug("Garbage in response: " + lineBuf.toString());
      }
      count++;
    }
    
    StatusLine statusline = lineParser.parseStatusLine(lineBuf, cursor);
    return responseFactory.newHttpResponse(statusline, null);
  }
  
  protected boolean reject(CharArrayBuffer line, int count) {
    return false;
  }
}
