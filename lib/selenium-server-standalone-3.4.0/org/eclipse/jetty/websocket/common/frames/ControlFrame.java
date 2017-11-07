package org.eclipse.jetty.websocket.common.frames;

import java.nio.ByteBuffer;
import java.util.Arrays;
import org.eclipse.jetty.util.BufferUtil;
import org.eclipse.jetty.websocket.api.ProtocolException;
import org.eclipse.jetty.websocket.common.WebSocketFrame;




















public abstract class ControlFrame
  extends WebSocketFrame
{
  public static final int MAX_CONTROL_PAYLOAD = 125;
  
  public ControlFrame(byte opcode)
  {
    super(opcode);
  }
  
  public void assertValid()
  {
    if (isControlFrame())
    {
      if (getPayloadLength() > 125)
      {
        throw new ProtocolException("Desired payload length [" + getPayloadLength() + "] exceeds maximum control payload length [" + 125 + "]");
      }
      

      if ((finRsvOp & 0x80) == 0)
      {
        throw new ProtocolException("Cannot have FIN==false on Control frames");
      }
      
      if ((finRsvOp & 0x40) != 0)
      {
        throw new ProtocolException("Cannot have RSV1==true on Control frames");
      }
      
      if ((finRsvOp & 0x20) != 0)
      {
        throw new ProtocolException("Cannot have RSV2==true on Control frames");
      }
      
      if ((finRsvOp & 0x10) != 0)
      {
        throw new ProtocolException("Cannot have RSV3==true on Control frames");
      }
    }
  }
  

  public boolean equals(Object obj)
  {
    if (this == obj)
    {
      return true;
    }
    if (obj == null)
    {
      return false;
    }
    if (getClass() != obj.getClass())
    {
      return false;
    }
    ControlFrame other = (ControlFrame)obj;
    if (data == null)
    {
      if (data != null)
      {
        return false;
      }
    }
    else if (!data.equals(data))
    {
      return false;
    }
    if (finRsvOp != finRsvOp)
    {
      return false;
    }
    if (!Arrays.equals(mask, mask))
    {
      return false;
    }
    if (masked != masked)
    {
      return false;
    }
    return true;
  }
  
  public boolean isControlFrame()
  {
    return true;
  }
  

  public boolean isDataFrame()
  {
    return false;
  }
  

  public WebSocketFrame setPayload(ByteBuffer buf)
  {
    if ((buf != null) && (buf.remaining() > 125))
    {
      throw new ProtocolException("Control Payloads can not exceed 125 bytes in length.");
    }
    return super.setPayload(buf);
  }
  

  public ByteBuffer getPayload()
  {
    if (super.getPayload() == null)
    {
      return BufferUtil.EMPTY_BUFFER;
    }
    return super.getPayload();
  }
}
