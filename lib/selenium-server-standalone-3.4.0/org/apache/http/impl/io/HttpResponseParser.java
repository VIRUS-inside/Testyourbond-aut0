package org.apache.http.impl.io;

import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.HttpMessage;
import org.apache.http.HttpResponseFactory;
import org.apache.http.NoHttpResponseException;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.message.LineParser;
import org.apache.http.message.ParserCursor;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;























































@Deprecated
public class HttpResponseParser
  extends AbstractMessageParser<HttpMessage>
{
  private final HttpResponseFactory responseFactory;
  private final CharArrayBuffer lineBuf;
  
  public HttpResponseParser(SessionInputBuffer buffer, LineParser parser, HttpResponseFactory responseFactory, HttpParams params)
  {
    super(buffer, parser, params);
    this.responseFactory = ((HttpResponseFactory)Args.notNull(responseFactory, "Response factory"));
    lineBuf = new CharArrayBuffer(128);
  }
  


  protected HttpMessage parseHead(SessionInputBuffer sessionBuffer)
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
