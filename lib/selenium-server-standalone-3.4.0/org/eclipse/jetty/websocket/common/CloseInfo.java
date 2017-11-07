package org.eclipse.jetty.websocket.common;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import org.eclipse.jetty.util.BufferUtil;
import org.eclipse.jetty.util.Utf8Appendable.NotUtf8Exception;
import org.eclipse.jetty.util.Utf8StringBuilder;
import org.eclipse.jetty.websocket.api.BadPayloadException;
import org.eclipse.jetty.websocket.api.ProtocolException;
import org.eclipse.jetty.websocket.api.extensions.Frame;
import org.eclipse.jetty.websocket.common.frames.CloseFrame;






















public class CloseInfo
{
  private int statusCode;
  private byte[] reasonBytes;
  
  public CloseInfo()
  {
    this(1005, null);
  }
  






  public CloseInfo(ByteBuffer payload, boolean validate)
  {
    statusCode = 1005;
    
    if ((payload == null) || (payload.remaining() == 0))
    {
      return;
    }
    
    ByteBuffer data = payload.slice();
    if ((data.remaining() == 1) && (validate))
    {
      throw new ProtocolException("Invalid 1 byte payload");
    }
    
    if (data.remaining() >= 2)
    {

      statusCode = 0;
      statusCode |= (data.get() & 0xFF) << 8;
      statusCode |= data.get() & 0xFF;
      
      if (validate)
      {
        if ((statusCode < 1000) || (statusCode == 1004) || (statusCode == 1006) || (statusCode == 1005) || ((statusCode > 1011) && (statusCode <= 2999)) || (statusCode >= 5000))
        {

          throw new ProtocolException("Invalid close code: " + statusCode);
        }
      }
      
      if (data.remaining() > 0)
      {

        int len = Math.min(data.remaining(), 123);
        reasonBytes = new byte[len];
        data.get(reasonBytes, 0, len);
        

        if (validate)
        {
          try
          {
            Utf8StringBuilder utf = new Utf8StringBuilder();
            
            utf.append(reasonBytes, 0, reasonBytes.length);
          }
          catch (Utf8Appendable.NotUtf8Exception e)
          {
            throw new BadPayloadException("Invalid Close Reason", e);
          }
        }
      }
    }
  }
  
  public CloseInfo(Frame frame)
  {
    this(frame.getPayload(), false);
  }
  
  public CloseInfo(Frame frame, boolean validate)
  {
    this(frame.getPayload(), validate);
  }
  
  public CloseInfo(int statusCode)
  {
    this(statusCode, null);
  }
  






  public CloseInfo(int statusCode, String reason)
  {
    this.statusCode = statusCode;
    if (reason != null)
    {
      byte[] utf8Bytes = reason.getBytes(StandardCharsets.UTF_8);
      if (utf8Bytes.length > 123)
      {
        reasonBytes = new byte[123];
        System.arraycopy(utf8Bytes, 0, reasonBytes, 0, 123);
      }
      else
      {
        reasonBytes = utf8Bytes;
      }
    }
  }
  
  private ByteBuffer asByteBuffer()
  {
    if ((statusCode == 1006) || (statusCode == 1005) || (statusCode == -1))
    {

      return null;
    }
    
    int len = 2;
    boolean hasReason = (reasonBytes != null) && (reasonBytes.length > 0);
    if (hasReason)
    {
      len += reasonBytes.length;
    }
    
    ByteBuffer buf = BufferUtil.allocate(len);
    BufferUtil.flipToFill(buf);
    buf.put((byte)(statusCode >>> 8 & 0xFF));
    buf.put((byte)(statusCode >>> 0 & 0xFF));
    
    if (hasReason)
    {
      buf.put(reasonBytes, 0, reasonBytes.length);
    }
    BufferUtil.flipToFlush(buf, 0);
    
    return buf;
  }
  
  public CloseFrame asFrame()
  {
    CloseFrame frame = new CloseFrame();
    frame.setFin(true);
    if ((statusCode >= 1000) && (statusCode != 1006) && (statusCode != 1005))
    {
      if (statusCode == 1015)
      {
        throw new ProtocolException("Close Frame with status code " + statusCode + " not allowed (per RFC6455)");
      }
      frame.setPayload(asByteBuffer());
    }
    return frame;
  }
  
  public String getReason()
  {
    if (reasonBytes == null)
    {
      return null;
    }
    return new String(reasonBytes, StandardCharsets.UTF_8);
  }
  
  public int getStatusCode()
  {
    return statusCode;
  }
  
  public boolean isHarsh()
  {
    return (statusCode != 1000) && (statusCode != 1005);
  }
  
  public boolean isAbnormal()
  {
    return statusCode != 1000;
  }
  

  public String toString()
  {
    return String.format("CloseInfo[code=%d,reason=%s]", new Object[] { Integer.valueOf(statusCode), getReason() });
  }
}
