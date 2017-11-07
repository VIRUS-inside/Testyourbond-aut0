package org.apache.http.impl.io;

import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseFactory;
import org.apache.http.NoHttpResponseException;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.config.MessageConstraints;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.message.LineParser;
import org.apache.http.message.ParserCursor;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;



















































public class DefaultHttpResponseParser
  extends AbstractMessageParser<HttpResponse>
{
  private final HttpResponseFactory responseFactory;
  private final CharArrayBuffer lineBuf;
  
  @Deprecated
  public DefaultHttpResponseParser(SessionInputBuffer buffer, LineParser lineParser, HttpResponseFactory responseFactory, HttpParams params)
  {
    super(buffer, lineParser, params);
    this.responseFactory = ((HttpResponseFactory)Args.notNull(responseFactory, "Response factory"));
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
    throws IOException, HttpException, ParseException
  {
    lineBuf.clear();
    int i = sessionBuffer.readLine(lineBuf);
    if (i == -1) {
      throw new NoHttpResponseException("The target server failed to respond");
    }
    
    ParserCursor cursor = new ParserCursor(0, lineBuf.length());
    StatusLine statusline = lineParser.parseStatusLine(lineBuf, cursor);
    return responseFactory.newHttpResponse(statusline, null);
  }
}
