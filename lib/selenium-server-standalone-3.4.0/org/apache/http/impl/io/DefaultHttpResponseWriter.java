package org.apache.http.impl.io;

import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.message.LineFormatter;










































public class DefaultHttpResponseWriter
  extends AbstractMessageWriter<HttpResponse>
{
  public DefaultHttpResponseWriter(SessionOutputBuffer buffer, LineFormatter formatter)
  {
    super(buffer, formatter);
  }
  
  public DefaultHttpResponseWriter(SessionOutputBuffer buffer) {
    super(buffer, null);
  }
  
  protected void writeHeadLine(HttpResponse message) throws IOException
  {
    lineFormatter.formatStatusLine(lineBuf, message.getStatusLine());
    sessionBuffer.writeLine(lineBuf);
  }
}
