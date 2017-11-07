package org.apache.http.impl.conn;

import java.io.IOException;
import java.nio.charset.Charset;
import org.apache.http.Consts;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.io.HttpTransportMetrics;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.util.CharArrayBuffer;










































@Deprecated
@Contract(threading=ThreadingBehavior.IMMUTABLE)
public class LoggingSessionOutputBuffer
  implements SessionOutputBuffer
{
  private final SessionOutputBuffer out;
  private final Wire wire;
  private final String charset;
  
  public LoggingSessionOutputBuffer(SessionOutputBuffer out, Wire wire, String charset)
  {
    this.out = out;
    this.wire = wire;
    this.charset = (charset != null ? charset : Consts.ASCII.name());
  }
  
  public LoggingSessionOutputBuffer(SessionOutputBuffer out, Wire wire) {
    this(out, wire, null);
  }
  
  public void write(byte[] b, int off, int len) throws IOException
  {
    out.write(b, off, len);
    if (wire.enabled()) {
      wire.output(b, off, len);
    }
  }
  
  public void write(int b) throws IOException
  {
    out.write(b);
    if (wire.enabled()) {
      wire.output(b);
    }
  }
  
  public void write(byte[] b) throws IOException
  {
    out.write(b);
    if (wire.enabled()) {
      wire.output(b);
    }
  }
  
  public void flush() throws IOException
  {
    out.flush();
  }
  
  public void writeLine(CharArrayBuffer buffer) throws IOException
  {
    out.writeLine(buffer);
    if (wire.enabled()) {
      String s = new String(buffer.buffer(), 0, buffer.length());
      String tmp = s + "\r\n";
      wire.output(tmp.getBytes(charset));
    }
  }
  
  public void writeLine(String s) throws IOException
  {
    out.writeLine(s);
    if (wire.enabled()) {
      String tmp = s + "\r\n";
      wire.output(tmp.getBytes(charset));
    }
  }
  
  public HttpTransportMetrics getMetrics()
  {
    return out.getMetrics();
  }
}
