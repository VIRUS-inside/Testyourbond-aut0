package org.apache.http.impl.conn;

import java.io.IOException;
import java.nio.charset.Charset;
import org.apache.http.Consts;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.io.EofSensor;
import org.apache.http.io.HttpTransportMetrics;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.util.CharArrayBuffer;













































@Deprecated
@Contract(threading=ThreadingBehavior.IMMUTABLE)
public class LoggingSessionInputBuffer
  implements SessionInputBuffer, EofSensor
{
  private final SessionInputBuffer in;
  private final EofSensor eofSensor;
  private final Wire wire;
  private final String charset;
  
  public LoggingSessionInputBuffer(SessionInputBuffer in, Wire wire, String charset)
  {
    this.in = in;
    eofSensor = ((in instanceof EofSensor) ? (EofSensor)in : null);
    this.wire = wire;
    this.charset = (charset != null ? charset : Consts.ASCII.name());
  }
  
  public LoggingSessionInputBuffer(SessionInputBuffer in, Wire wire) {
    this(in, wire, null);
  }
  
  public boolean isDataAvailable(int timeout) throws IOException
  {
    return in.isDataAvailable(timeout);
  }
  
  public int read(byte[] b, int off, int len) throws IOException
  {
    int l = in.read(b, off, len);
    if ((wire.enabled()) && (l > 0)) {
      wire.input(b, off, l);
    }
    return l;
  }
  
  public int read() throws IOException
  {
    int l = in.read();
    if ((wire.enabled()) && (l != -1)) {
      wire.input(l);
    }
    return l;
  }
  
  public int read(byte[] b) throws IOException
  {
    int l = in.read(b);
    if ((wire.enabled()) && (l > 0)) {
      wire.input(b, 0, l);
    }
    return l;
  }
  
  public String readLine() throws IOException
  {
    String s = in.readLine();
    if ((wire.enabled()) && (s != null)) {
      String tmp = s + "\r\n";
      wire.input(tmp.getBytes(charset));
    }
    return s;
  }
  
  public int readLine(CharArrayBuffer buffer) throws IOException
  {
    int l = in.readLine(buffer);
    if ((wire.enabled()) && (l >= 0)) {
      int pos = buffer.length() - l;
      String s = new String(buffer.buffer(), pos, l);
      String tmp = s + "\r\n";
      wire.input(tmp.getBytes(charset));
    }
    return l;
  }
  
  public HttpTransportMetrics getMetrics()
  {
    return in.getMetrics();
  }
  
  public boolean isEof()
  {
    if (eofSensor != null) {
      return eofSensor.isEof();
    }
    return false;
  }
}
