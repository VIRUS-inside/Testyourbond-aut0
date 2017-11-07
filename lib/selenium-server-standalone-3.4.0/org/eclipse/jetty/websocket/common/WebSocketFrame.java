package org.eclipse.jetty.websocket.common;

import java.nio.ByteBuffer;
import java.util.Arrays;
import org.eclipse.jetty.util.BufferUtil;
import org.eclipse.jetty.websocket.api.extensions.Frame;
import org.eclipse.jetty.websocket.api.extensions.Frame.Type;
import org.eclipse.jetty.websocket.common.frames.BinaryFrame;
import org.eclipse.jetty.websocket.common.frames.CloseFrame;
import org.eclipse.jetty.websocket.common.frames.ContinuationFrame;
import org.eclipse.jetty.websocket.common.frames.PingFrame;
import org.eclipse.jetty.websocket.common.frames.PongFrame;
import org.eclipse.jetty.websocket.common.frames.TextFrame;



































public abstract class WebSocketFrame
  implements Frame
{
  protected byte finRsvOp;
  
  public static WebSocketFrame copy(Frame original)
  {
    WebSocketFrame copy;
    WebSocketFrame copy;
    WebSocketFrame copy;
    WebSocketFrame copy;
    WebSocketFrame copy;
    WebSocketFrame copy;
    switch (original.getOpCode())
    {
    case 2: 
      copy = new BinaryFrame();
      break;
    case 1: 
      copy = new TextFrame();
      break;
    case 8: 
      copy = new CloseFrame();
      break;
    case 0: 
      copy = new ContinuationFrame();
      break;
    case 9: 
      copy = new PingFrame();
      break;
    case 10: 
      copy = new PongFrame();
      break;
    case 3: case 4: case 5: case 6: case 7: default: 
      throw new IllegalArgumentException("Cannot copy frame with opcode " + original.getOpCode() + " - " + original);
    }
    WebSocketFrame copy;
    copy.copyHeaders(original);
    ByteBuffer payload = original.getPayload();
    if (payload != null)
    {
      ByteBuffer payloadCopy = ByteBuffer.allocate(payload.remaining());
      payloadCopy.put(payload.slice()).flip();
      copy.setPayload(payloadCopy);
    }
    return copy;
  }
  












  protected boolean masked = false;
  



  protected byte[] mask;
  


  protected ByteBuffer data;
  



  protected WebSocketFrame(byte opcode)
  {
    reset();
    setOpCode(opcode);
  }
  
  public abstract void assertValid();
  
  protected void copyHeaders(Frame frame)
  {
    finRsvOp = 0;
    finRsvOp = ((byte)(finRsvOp | (frame.isFin() ? 128 : 0)));
    finRsvOp = ((byte)(finRsvOp | (frame.isRsv1() ? 64 : 0)));
    finRsvOp = ((byte)(finRsvOp | (frame.isRsv2() ? 32 : 0)));
    finRsvOp = ((byte)(finRsvOp | (frame.isRsv3() ? 16 : 0)));
    finRsvOp = ((byte)(finRsvOp | frame.getOpCode() & 0xF));
    
    masked = frame.isMasked();
    if (masked)
    {
      mask = frame.getMask();
    }
    else
    {
      mask = null;
    }
  }
  
  protected void copyHeaders(WebSocketFrame copy)
  {
    finRsvOp = finRsvOp;
    masked = masked;
    mask = null;
    if (mask != null) {
      mask = Arrays.copyOf(mask, mask.length);
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
    WebSocketFrame other = (WebSocketFrame)obj;
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
  

  public byte[] getMask()
  {
    return mask;
  }
  

  public final byte getOpCode()
  {
    return (byte)(finRsvOp & 0xF);
  }
  




  public ByteBuffer getPayload()
  {
    return data;
  }
  
  public String getPayloadAsUTF8()
  {
    return BufferUtil.toUTF8String(getPayload());
  }
  

  public int getPayloadLength()
  {
    if (data == null)
    {
      return 0;
    }
    return data.remaining();
  }
  

  public Frame.Type getType()
  {
    return Frame.Type.from(getOpCode());
  }
  

  public int hashCode()
  {
    int prime = 31;
    int result = 1;
    result = 31 * result + (data == null ? 0 : data.hashCode());
    result = 31 * result + finRsvOp;
    result = 31 * result + Arrays.hashCode(mask);
    return result;
  }
  

  public boolean hasPayload()
  {
    return (data != null) && (data.hasRemaining());
  }
  

  public abstract boolean isControlFrame();
  
  public abstract boolean isDataFrame();
  
  public boolean isFin()
  {
    return (byte)(finRsvOp & 0x80) != 0;
  }
  

  public boolean isLast()
  {
    return isFin();
  }
  

  public boolean isMasked()
  {
    return masked;
  }
  

  public boolean isRsv1()
  {
    return (byte)(finRsvOp & 0x40) != 0;
  }
  

  public boolean isRsv2()
  {
    return (byte)(finRsvOp & 0x20) != 0;
  }
  

  public boolean isRsv3()
  {
    return (byte)(finRsvOp & 0x10) != 0;
  }
  
  public void reset()
  {
    finRsvOp = Byte.MIN_VALUE;
    masked = false;
    data = null;
    mask = null;
  }
  

  public WebSocketFrame setFin(boolean fin)
  {
    finRsvOp = ((byte)(finRsvOp & 0x7F | (fin ? 128 : 0)));
    return this;
  }
  
  public Frame setMask(byte[] maskingKey)
  {
    mask = maskingKey;
    masked = (mask != null);
    return this;
  }
  
  public Frame setMasked(boolean mask)
  {
    masked = mask;
    return this;
  }
  
  protected WebSocketFrame setOpCode(byte op)
  {
    finRsvOp = ((byte)(finRsvOp & 0xF0 | op & 0xF));
    return this;
  }
  











  public WebSocketFrame setPayload(ByteBuffer buf)
  {
    data = buf;
    return this;
  }
  

  public WebSocketFrame setRsv1(boolean rsv1)
  {
    finRsvOp = ((byte)(finRsvOp & 0xBF | (rsv1 ? 64 : 0)));
    return this;
  }
  

  public WebSocketFrame setRsv2(boolean rsv2)
  {
    finRsvOp = ((byte)(finRsvOp & 0xDF | (rsv2 ? 32 : 0)));
    return this;
  }
  

  public WebSocketFrame setRsv3(boolean rsv3)
  {
    finRsvOp = ((byte)(finRsvOp & 0xEF | (rsv3 ? 16 : 0)));
    return this;
  }
  

  public String toString()
  {
    StringBuilder b = new StringBuilder();
    b.append(OpCode.name((byte)(finRsvOp & 0xF)));
    b.append('[');
    b.append("len=").append(getPayloadLength());
    b.append(",fin=").append((finRsvOp & 0x80) != 0);
    b.append(",rsv=");
    b.append((finRsvOp & 0x40) != 0 ? '1' : '.');
    b.append((finRsvOp & 0x20) != 0 ? '1' : '.');
    b.append((finRsvOp & 0x10) != 0 ? '1' : '.');
    b.append(",masked=").append(masked);
    b.append(']');
    return b.toString();
  }
}
