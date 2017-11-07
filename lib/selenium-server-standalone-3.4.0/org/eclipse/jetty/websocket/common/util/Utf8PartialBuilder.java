package org.eclipse.jetty.websocket.common.util;

import java.nio.ByteBuffer;
import org.eclipse.jetty.util.Utf8Appendable;




























public class Utf8PartialBuilder
{
  private final StringBuilder str;
  private final Utf8Appendable utf8;
  
  public Utf8PartialBuilder()
  {
    str = new StringBuilder();
    utf8 = new Utf8Appendable(str)
    {

      public int length()
      {
        return str.length();
      }
    };
  }
  
  public String toPartialString(ByteBuffer buf)
  {
    if (buf == null)
    {

      return "";
    }
    utf8.append(buf);
    String ret = str.toString();
    str.setLength(0);
    return ret;
  }
}
