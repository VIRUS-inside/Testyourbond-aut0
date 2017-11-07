package org.eclipse.jetty.websocket.common.io.http;

import java.nio.ByteBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.eclipse.jetty.util.BufferUtil;
import org.eclipse.jetty.util.StringUtil;
import org.eclipse.jetty.util.Utf8LineParser;























public class HttpResponseHeaderParser
{
  public static class ParseException
    extends RuntimeException
  {
    public ParseException(String message)
    {
      super();
    }
    
    public ParseException(String message, Throwable cause)
    {
      super(cause);
    }
  }
  
  private static enum State
  {
    STATUS_LINE, 
    HEADER, 
    END;
    
    private State() {} }
  private static final Pattern PAT_HEADER = Pattern.compile("([^:]+):\\s*(.*)");
  private static final Pattern PAT_STATUS_LINE = Pattern.compile("^HTTP/1.[01]\\s+(\\d+)\\s+(.*)", 2);
  
  private final HttpResponseHeaderParseListener listener;
  private final Utf8LineParser lineParser;
  private State state;
  
  public HttpResponseHeaderParser(HttpResponseHeaderParseListener listener)
  {
    this.listener = listener;
    lineParser = new Utf8LineParser();
    state = State.STATUS_LINE;
  }
  
  public boolean isDone()
  {
    return state == State.END;
  }
  
  public HttpResponseHeaderParseListener parse(ByteBuffer buf) throws HttpResponseHeaderParser.ParseException
  {
    while ((!isDone()) && (buf.remaining() > 0))
    {
      String line = lineParser.parse(buf);
      if (line != null)
      {
        if (parseHeader(line))
        {



          ByteBuffer copy = ByteBuffer.allocate(buf.remaining());
          BufferUtil.put(buf, copy);
          BufferUtil.flipToFlush(copy, 0);
          listener.setRemainingBuffer(copy);
          return listener;
        }
      }
    }
    return null;
  }
  
  private boolean parseHeader(String line) throws HttpResponseHeaderParser.ParseException
  {
    switch (1.$SwitchMap$org$eclipse$jetty$websocket$common$io$http$HttpResponseHeaderParser$State[state.ordinal()])
    {

    case 1: 
      Matcher mat = PAT_STATUS_LINE.matcher(line);
      if (!mat.matches())
      {
        throw new ParseException("Unexpected HTTP response status line [" + line + "]");
      }
      
      try
      {
        listener.setStatusCode(Integer.parseInt(mat.group(1)));
      }
      catch (NumberFormatException e)
      {
        throw new ParseException("Unexpected HTTP response status code", e);
      }
      listener.setStatusReason(mat.group(2));
      state = State.HEADER;
      break;
    

    case 2: 
      if (StringUtil.isBlank(line))
      {
        state = State.END;
        return parseHeader(line);
      }
      
      Matcher header = PAT_HEADER.matcher(line);
      if (header.matches())
      {
        String headerName = header.group(1);
        String headerValue = header.group(2);
        
        listener.addHeader(headerName, headerValue); }
      break;
    

    case 3: 
      state = State.STATUS_LINE;
      return true; }
    
    return false;
  }
}
