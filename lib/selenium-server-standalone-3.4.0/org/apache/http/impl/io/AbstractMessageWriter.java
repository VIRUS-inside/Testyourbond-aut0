package org.apache.http.impl.io;

import java.io.IOException;
import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpException;
import org.apache.http.HttpMessage;
import org.apache.http.io.HttpMessageWriter;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.message.BasicLineFormatter;
import org.apache.http.message.LineFormatter;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;















































public abstract class AbstractMessageWriter<T extends HttpMessage>
  implements HttpMessageWriter<T>
{
  protected final SessionOutputBuffer sessionBuffer;
  protected final CharArrayBuffer lineBuf;
  protected final LineFormatter lineFormatter;
  
  @Deprecated
  public AbstractMessageWriter(SessionOutputBuffer buffer, LineFormatter formatter, HttpParams params)
  {
    Args.notNull(buffer, "Session input buffer");
    sessionBuffer = buffer;
    lineBuf = new CharArrayBuffer(128);
    lineFormatter = (formatter != null ? formatter : BasicLineFormatter.INSTANCE);
  }
  











  public AbstractMessageWriter(SessionOutputBuffer buffer, LineFormatter formatter)
  {
    sessionBuffer = ((SessionOutputBuffer)Args.notNull(buffer, "Session input buffer"));
    lineFormatter = (formatter != null ? formatter : BasicLineFormatter.INSTANCE);
    lineBuf = new CharArrayBuffer(128);
  }
  



  protected abstract void writeHeadLine(T paramT)
    throws IOException;
  


  public void write(T message)
    throws IOException, HttpException
  {
    Args.notNull(message, "HTTP message");
    writeHeadLine(message);
    for (HeaderIterator it = message.headerIterator(); it.hasNext();) {
      Header header = it.nextHeader();
      sessionBuffer.writeLine(lineFormatter.formatHeader(lineBuf, header));
    }
    
    lineBuf.clear();
    sessionBuffer.writeLine(lineBuf);
  }
}
