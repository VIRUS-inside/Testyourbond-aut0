package org.eclipse.jetty.websocket.common;

import java.nio.ByteBuffer;
import java.util.List;
import org.eclipse.jetty.io.ByteBufferPool;
import org.eclipse.jetty.util.BufferUtil;
import org.eclipse.jetty.websocket.api.ProtocolException;
import org.eclipse.jetty.websocket.api.WebSocketBehavior;
import org.eclipse.jetty.websocket.api.WebSocketPolicy;
import org.eclipse.jetty.websocket.api.extensions.Extension;
import org.eclipse.jetty.websocket.api.extensions.Frame;



























































public class Generator
{
  public static final int MAX_HEADER_LENGTH = 28;
  private final WebSocketBehavior behavior;
  private final ByteBufferPool bufferPool;
  private final boolean validating;
  private final boolean readOnly;
  private byte flagsInUse = 0;
  








  public Generator(WebSocketPolicy policy, ByteBufferPool bufferPool)
  {
    this(policy, bufferPool, true, false);
  }
  










  public Generator(WebSocketPolicy policy, ByteBufferPool bufferPool, boolean validating)
  {
    this(policy, bufferPool, validating, false);
  }
  












  public Generator(WebSocketPolicy policy, ByteBufferPool bufferPool, boolean validating, boolean readOnly)
  {
    behavior = policy.getBehavior();
    this.bufferPool = bufferPool;
    this.validating = validating;
    this.readOnly = readOnly;
  }
  
  public void assertFrameValid(Frame frame)
  {
    if (!validating)
    {
      return;
    }
    






    if ((frame.isRsv1()) && (!isRsv1InUse()))
    {
      throw new ProtocolException("RSV1 not allowed to be set");
    }
    
    if ((frame.isRsv2()) && (!isRsv2InUse()))
    {
      throw new ProtocolException("RSV2 not allowed to be set");
    }
    
    if ((frame.isRsv3()) && (!isRsv3InUse()))
    {
      throw new ProtocolException("RSV3 not allowed to be set");
    }
    
    if (OpCode.isControlFrame(frame.getOpCode()))
    {





      if (frame.getPayloadLength() > 125)
      {
        throw new ProtocolException("Invalid control frame payload length");
      }
      
      if (!frame.isFin())
      {
        throw new ProtocolException("Control Frames must be FIN=true");
      }
      





      if (frame.getOpCode() == 8)
      {

        ByteBuffer payload = frame.getPayload();
        if (payload != null)
        {
          new CloseInfo(payload, true);
        }
      }
    }
  }
  

  public void configureFromExtensions(List<? extends Extension> exts)
  {
    flagsInUse = 0;
    

    for (Extension ext : exts)
    {
      if (ext.isRsv1User())
      {
        flagsInUse = ((byte)(flagsInUse | 0x40));
      }
      if (ext.isRsv2User())
      {
        flagsInUse = ((byte)(flagsInUse | 0x20));
      }
      if (ext.isRsv3User())
      {
        flagsInUse = ((byte)(flagsInUse | 0x10));
      }
    }
  }
  
  public ByteBuffer generateHeaderBytes(Frame frame)
  {
    ByteBuffer buffer = bufferPool.acquire(28, true);
    generateHeaderBytes(frame, buffer);
    return buffer;
  }
  
  public void generateHeaderBytes(Frame frame, ByteBuffer buffer)
  {
    int p = BufferUtil.flipToFill(buffer);
    

    assertFrameValid(frame);
    



    byte b = 0;
    

    if (frame.isFin())
    {
      b = (byte)(b | 0x80);
    }
    

    if (frame.isRsv1())
    {
      b = (byte)(b | 0x40);
    }
    if (frame.isRsv2())
    {
      b = (byte)(b | 0x20);
    }
    if (frame.isRsv3())
    {
      b = (byte)(b | 0x10);
    }
    

    byte opcode = frame.getOpCode();
    
    if (frame.getOpCode() == 0)
    {

      opcode = 0;
    }
    
    b = (byte)(b | opcode & 0xF);
    
    buffer.put(b);
    

    b = frame.isMasked() ? Byte.MIN_VALUE : 0;
    

    int payloadLength = frame.getPayloadLength();
    



    if (payloadLength > 65535)
    {

      b = (byte)(b | 0x7F);
      buffer.put(b);
      buffer.put((byte)0);
      buffer.put((byte)0);
      buffer.put((byte)0);
      buffer.put((byte)0);
      buffer.put((byte)(payloadLength >> 24 & 0xFF));
      buffer.put((byte)(payloadLength >> 16 & 0xFF));
      buffer.put((byte)(payloadLength >> 8 & 0xFF));
      buffer.put((byte)(payloadLength & 0xFF));



    }
    else if (payloadLength >= 126)
    {
      b = (byte)(b | 0x7E);
      buffer.put(b);
      buffer.put((byte)(payloadLength >> 8));
      buffer.put((byte)(payloadLength & 0xFF));


    }
    else
    {

      b = (byte)(b | payloadLength & 0x7F);
      buffer.put(b);
    }
    

    if ((frame.isMasked()) && (!readOnly))
    {
      byte[] mask = frame.getMask();
      buffer.put(mask);
      int maskInt = 0;
      for (byte maskByte : mask) {
        maskInt = (maskInt << 8) + (maskByte & 0xFF);
      }
      
      ByteBuffer payload = frame.getPayload();
      if ((payload != null) && (payload.remaining() > 0))
      {
        int maskOffset = 0;
        int start = payload.position();
        int end = payload.limit();
        int remaining;
        while ((remaining = end - start) > 0)
        {
          if (remaining >= 4)
          {
            payload.putInt(start, payload.getInt(start) ^ maskInt);
            start += 4;
          }
          else
          {
            payload.put(start, (byte)(payload.get(start) ^ mask[(maskOffset & 0x3)]));
            start++;
            maskOffset++;
          }
        }
      }
    }
    
    BufferUtil.flipToFlush(buffer, p);
  }
  










  public void generateWholeFrame(Frame frame, ByteBuffer buf)
  {
    buf.put(generateHeaderBytes(frame));
    if (frame.hasPayload())
    {
      if (readOnly)
      {
        buf.put(frame.getPayload().slice());
      }
      else
      {
        buf.put(frame.getPayload());
      }
    }
  }
  
  public ByteBufferPool getBufferPool()
  {
    return bufferPool;
  }
  
  public void setRsv1InUse(boolean rsv1InUse)
  {
    if (readOnly)
    {
      throw new RuntimeException("Not allowed to modify read-only frame");
    }
    flagsInUse = ((byte)(flagsInUse & 0xBF | (rsv1InUse ? 64 : 0)));
  }
  
  public void setRsv2InUse(boolean rsv2InUse)
  {
    if (readOnly)
    {
      throw new RuntimeException("Not allowed to modify read-only frame");
    }
    flagsInUse = ((byte)(flagsInUse & 0xDF | (rsv2InUse ? 32 : 0)));
  }
  
  public void setRsv3InUse(boolean rsv3InUse)
  {
    if (readOnly)
    {
      throw new RuntimeException("Not allowed to modify read-only frame");
    }
    flagsInUse = ((byte)(flagsInUse & 0xEF | (rsv3InUse ? 16 : 0)));
  }
  
  public boolean isRsv1InUse()
  {
    return (flagsInUse & 0x40) != 0;
  }
  
  public boolean isRsv2InUse()
  {
    return (flagsInUse & 0x20) != 0;
  }
  
  public boolean isRsv3InUse()
  {
    return (flagsInUse & 0x10) != 0;
  }
  

  public String toString()
  {
    StringBuilder builder = new StringBuilder();
    builder.append("Generator[");
    builder.append(behavior);
    if (validating)
    {
      builder.append(",validating");
    }
    if (isRsv1InUse())
    {
      builder.append(",+rsv1");
    }
    if (isRsv2InUse())
    {
      builder.append(",+rsv2");
    }
    if (isRsv3InUse())
    {
      builder.append(",+rsv3");
    }
    builder.append("]");
    return builder.toString();
  }
}
