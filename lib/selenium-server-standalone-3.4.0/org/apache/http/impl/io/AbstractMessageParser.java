package org.apache.http.impl.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpMessage;
import org.apache.http.MessageConstraintException;
import org.apache.http.ParseException;
import org.apache.http.ProtocolException;
import org.apache.http.config.MessageConstraints;
import org.apache.http.io.HttpMessageParser;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.message.BasicLineParser;
import org.apache.http.message.LineParser;
import org.apache.http.params.HttpParamConfig;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;


















































public abstract class AbstractMessageParser<T extends HttpMessage>
  implements HttpMessageParser<T>
{
  private static final int HEAD_LINE = 0;
  private static final int HEADERS = 1;
  private final SessionInputBuffer sessionBuffer;
  private final MessageConstraints messageConstraints;
  private final List<CharArrayBuffer> headerLines;
  protected final LineParser lineParser;
  private int state;
  private T message;
  
  @Deprecated
  public AbstractMessageParser(SessionInputBuffer buffer, LineParser parser, HttpParams params)
  {
    Args.notNull(buffer, "Session input buffer");
    Args.notNull(params, "HTTP parameters");
    sessionBuffer = buffer;
    messageConstraints = HttpParamConfig.getMessageConstraints(params);
    lineParser = (parser != null ? parser : BasicLineParser.INSTANCE);
    headerLines = new ArrayList();
    state = 0;
  }
  














  public AbstractMessageParser(SessionInputBuffer buffer, LineParser lineParser, MessageConstraints constraints)
  {
    sessionBuffer = ((SessionInputBuffer)Args.notNull(buffer, "Session input buffer"));
    this.lineParser = (lineParser != null ? lineParser : BasicLineParser.INSTANCE);
    messageConstraints = (constraints != null ? constraints : MessageConstraints.DEFAULT);
    headerLines = new ArrayList();
    state = 0;
  }
  





















  public static Header[] parseHeaders(SessionInputBuffer inbuffer, int maxHeaderCount, int maxLineLen, LineParser parser)
    throws HttpException, IOException
  {
    List<CharArrayBuffer> headerLines = new ArrayList();
    return parseHeaders(inbuffer, maxHeaderCount, maxLineLen, parser != null ? parser : BasicLineParser.INSTANCE, headerLines);
  }
  





























  public static Header[] parseHeaders(SessionInputBuffer inbuffer, int maxHeaderCount, int maxLineLen, LineParser parser, List<CharArrayBuffer> headerLines)
    throws HttpException, IOException
  {
    Args.notNull(inbuffer, "Session input buffer");
    Args.notNull(parser, "Line parser");
    Args.notNull(headerLines, "Header line list");
    
    CharArrayBuffer current = null;
    CharArrayBuffer previous = null;
    for (;;) {
      if (current == null) {
        current = new CharArrayBuffer(64);
      } else {
        current.clear();
      }
      int l = inbuffer.readLine(current);
      if ((l == -1) || (current.length() < 1)) {
        break;
      }
      



      if (((current.charAt(0) == ' ') || (current.charAt(0) == '\t')) && (previous != null))
      {

        int i = 0;
        while (i < current.length()) {
          char ch = current.charAt(i);
          if ((ch != ' ') && (ch != '\t')) {
            break;
          }
          i++;
        }
        if ((maxLineLen > 0) && (previous.length() + 1 + current.length() - i > maxLineLen))
        {
          throw new MessageConstraintException("Maximum line length limit exceeded");
        }
        previous.append(' ');
        previous.append(current, i, current.length() - i);
      } else {
        headerLines.add(current);
        previous = current;
        current = null;
      }
      if ((maxHeaderCount > 0) && (headerLines.size() >= maxHeaderCount)) {
        throw new MessageConstraintException("Maximum header count exceeded");
      }
    }
    Header[] headers = new Header[headerLines.size()];
    for (int i = 0; i < headerLines.size(); i++) {
      CharArrayBuffer buffer = (CharArrayBuffer)headerLines.get(i);
      try {
        headers[i] = parser.parseHeader(buffer);
      } catch (ParseException ex) {
        throw new ProtocolException(ex.getMessage());
      }
    }
    return headers;
  }
  







  protected abstract T parseHead(SessionInputBuffer paramSessionInputBuffer)
    throws IOException, HttpException, ParseException;
  






  public T parse()
    throws IOException, HttpException
  {
    int st = state;
    switch (st) {
    case 0: 
      try {
        message = parseHead(sessionBuffer);
      } catch (ParseException px) {
        throw new ProtocolException(px.getMessage(), px);
      }
      state = 1;
    
    case 1: 
      Header[] headers = parseHeaders(sessionBuffer, messageConstraints.getMaxHeaderCount(), messageConstraints.getMaxLineLength(), lineParser, headerLines);
      




      message.setHeaders(headers);
      T result = message;
      message = null;
      headerLines.clear();
      state = 0;
      return result;
    }
    throw new IllegalStateException("Inconsistent parser state");
  }
}
