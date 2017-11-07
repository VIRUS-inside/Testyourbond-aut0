package org.seleniumhq.jetty9.server;

import java.io.IOException;
import org.seleniumhq.jetty9.io.EndPoint;

















public class HttpInputOverHTTP
  extends HttpInput
{
  public HttpInputOverHTTP(HttpChannelState state)
  {
    super(state);
  }
  
  protected void produceContent()
    throws IOException
  {
    ((HttpConnection)getHttpChannelState().getHttpChannel().getEndPoint().getConnection()).fillAndParseForContent();
  }
  
  protected void blockForContent()
    throws IOException
  {
    ((HttpConnection)getHttpChannelState().getHttpChannel().getEndPoint().getConnection()).blockingReadFillInterested();
    try
    {
      super.blockForContent();
    }
    catch (Throwable e)
    {
      ((HttpConnection)getHttpChannelState().getHttpChannel().getEndPoint().getConnection()).blockingReadException(e);
    }
  }
}
