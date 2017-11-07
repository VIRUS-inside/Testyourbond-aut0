package org.eclipse.jetty.websocket.common;

import java.nio.ByteBuffer;
import java.util.List;
import org.eclipse.jetty.io.ByteBufferPool;
import org.eclipse.jetty.util.BufferUtil;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.websocket.api.MessageTooLargeException;
import org.eclipse.jetty.websocket.api.ProtocolException;
import org.eclipse.jetty.websocket.api.WebSocketBehavior;
import org.eclipse.jetty.websocket.api.WebSocketException;
import org.eclipse.jetty.websocket.api.WebSocketPolicy;
import org.eclipse.jetty.websocket.api.extensions.Extension;
import org.eclipse.jetty.websocket.api.extensions.Frame;
import org.eclipse.jetty.websocket.api.extensions.IncomingFrames;
import org.eclipse.jetty.websocket.common.frames.BinaryFrame;
import org.eclipse.jetty.websocket.common.frames.CloseFrame;
import org.eclipse.jetty.websocket.common.frames.ContinuationFrame;
import org.eclipse.jetty.websocket.common.frames.PingFrame;
import org.eclipse.jetty.websocket.common.frames.PongFrame;
import org.eclipse.jetty.websocket.common.frames.TextFrame;
import org.eclipse.jetty.websocket.common.io.payload.DeMaskProcessor;
import org.eclipse.jetty.websocket.common.io.payload.PayloadProcessor;
























public class Parser
{
  private static enum State
  {
    START, 
    PAYLOAD_LEN, 
    PAYLOAD_LEN_BYTES, 
    MASK, 
    MASK_BYTES, 
    PAYLOAD;
    
    private State() {} }
  private static final Logger LOG = Log.getLogger(Parser.class);
  
  private final WebSocketPolicy policy;
  
  private final ByteBufferPool bufferPool;
  private State state = State.START;
  private int cursor = 0;
  
  private WebSocketFrame frame;
  
  private boolean priorDataFrame;
  private ByteBuffer payload;
  private int payloadLength;
  private PayloadProcessor maskProcessor = new DeMaskProcessor();
  











  private byte flagsInUse = 0;
  
  private IncomingFrames incomingFramesHandler;
  
  public Parser(WebSocketPolicy wspolicy, ByteBufferPool bufferPool)
  {
    this.bufferPool = bufferPool;
    policy = wspolicy;
  }
  
  private void assertSanePayloadLength(long len)
  {
    if (LOG.isDebugEnabled()) {
      LOG.debug("{} Payload Length: {} - {}", new Object[] { policy.getBehavior(), Long.valueOf(len), this });
    }
    

    if (len > 2147483647L)
    {

      throw new MessageTooLargeException("[int-sane!] cannot handle payload lengths larger than 2147483647");
    }
    
    switch (frame.getOpCode())
    {
    case 8: 
      if (len == 1L)
      {
        throw new ProtocolException("Invalid close frame payload length, [" + payloadLength + "]");
      }
    
    case 9: 
    case 10: 
      if (len > 125L)
      {
        throw new ProtocolException("Invalid control frame payload length, [" + payloadLength + "] cannot exceed [" + 125 + "]");
      }
      
      break;
    case 1: 
      policy.assertValidTextMessageSize((int)len);
      break;
    case 2: 
      policy.assertValidBinaryMessageSize((int)len);
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
  
  public IncomingFrames getIncomingFramesHandler()
  {
    return incomingFramesHandler;
  }
  
  public WebSocketPolicy getPolicy()
  {
    return policy;
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
  
  protected void notifyFrame(Frame f)
  {
    if (LOG.isDebugEnabled()) {
      LOG.debug("{} Notify {}", new Object[] { policy.getBehavior(), getIncomingFramesHandler() });
    }
    if (policy.getBehavior() == WebSocketBehavior.SERVER)
    {










      if (!f.isMasked())
      {
        throw new ProtocolException("Client MUST mask all frames (RFC-6455: Section 5.1)");
      }
    }
    else if (policy.getBehavior() == WebSocketBehavior.CLIENT)
    {

      if (f.isMasked())
      {
        throw new ProtocolException("Server MUST NOT mask any frames (RFC-6455: Section 5.1)");
      }
    }
    
    if (incomingFramesHandler == null)
    {
      return;
    }
    try
    {
      incomingFramesHandler.incomingFrame(f);
    }
    catch (WebSocketException e)
    {
      notifyWebSocketException(e);
    }
    catch (Throwable t)
    {
      LOG.warn(t);
      notifyWebSocketException(new WebSocketException(t));
    }
  }
  
  protected void notifyWebSocketException(WebSocketException e)
  {
    LOG.warn(e);
    if (incomingFramesHandler == null)
    {
      return;
    }
    incomingFramesHandler.incomingError(e);
  }
  
  public void parse(ByteBuffer buffer) throws WebSocketException
  {
    if (buffer.remaining() <= 0)
    {
      return;
    }
    
    try
    {
      while (parseFrame(buffer))
      {
        if (LOG.isDebugEnabled())
          LOG.debug("{} Parsed Frame: {}", new Object[] { policy.getBehavior(), frame });
        notifyFrame(frame);
        if (frame.isDataFrame())
        {
          priorDataFrame = (!frame.isFin());
        }
        reset();
      }
    }
    catch (WebSocketException e)
    {
      buffer.position(buffer.limit());
      reset();
      
      notifyWebSocketException(e);
      
      throw e;
    }
    catch (Throwable t)
    {
      buffer.position(buffer.limit());
      reset();
      
      WebSocketException e = new WebSocketException(t);
      notifyWebSocketException(e);
      
      throw e;
    }
  }
  
  private void reset()
  {
    if (frame != null)
      frame.reset();
    frame = null;
    bufferPool.release(payload);
    payload = null;
  }
  











  private boolean parseFrame(ByteBuffer buffer)
  {
    if (LOG.isDebugEnabled())
    {
      LOG.debug("{} Parsing {} bytes", new Object[] { policy.getBehavior(), Integer.valueOf(buffer.remaining()) });
    }
    while (buffer.hasRemaining())
    {
      switch (1.$SwitchMap$org$eclipse$jetty$websocket$common$Parser$State[state.ordinal()])
      {


      case 1: 
        byte b = buffer.get();
        boolean fin = (b & 0x80) != 0;
        
        byte opcode = (byte)(b & 0xF);
        
        if (!OpCode.isKnown(opcode))
        {
          throw new ProtocolException("Unknown opcode: " + opcode);
        }
        
        if (LOG.isDebugEnabled()) {
          LOG.debug("{} OpCode {}, fin={} rsv={}{}{}", new Object[] {policy
            .getBehavior(), 
            OpCode.name(opcode), 
            Boolean.valueOf(fin), 
            Character.valueOf((b & 0x40) != 0 ? 49 : '.'), 
            Character.valueOf((b & 0x20) != 0 ? 49 : '.'), 
            Character.valueOf((b & 0x10) != 0 ? 49 : '.') });
        }
        
        switch (opcode)
        {
        case 1: 
          frame = new TextFrame();
          
          if (priorDataFrame)
          {
            throw new ProtocolException("Unexpected " + OpCode.name(opcode) + " frame, was expecting CONTINUATION");
          }
          break;
        case 2: 
          frame = new BinaryFrame();
          
          if (priorDataFrame)
          {
            throw new ProtocolException("Unexpected " + OpCode.name(opcode) + " frame, was expecting CONTINUATION");
          }
          break;
        case 0: 
          frame = new ContinuationFrame();
          
          if (!priorDataFrame)
          {
            throw new ProtocolException("CONTINUATION frame without prior !FIN");
          }
          
          break;
        case 8: 
          frame = new CloseFrame();
          
          if (!fin)
          {
            throw new ProtocolException("Fragmented Close Frame [" + OpCode.name(opcode) + "]");
          }
          break;
        case 9: 
          frame = new PingFrame();
          
          if (!fin)
          {
            throw new ProtocolException("Fragmented Ping Frame [" + OpCode.name(opcode) + "]");
          }
          break;
        case 10: 
          frame = new PongFrame();
          
          if (!fin)
          {
            throw new ProtocolException("Fragmented Pong Frame [" + OpCode.name(opcode) + "]");
          }
          break;
        }
        
        frame.setFin(fin);
        

        if ((b & 0x70) != 0)
        {






          if ((b & 0x40) != 0)
          {
            if (isRsv1InUse()) {
              frame.setRsv1(true);
            }
            else {
              String err = "RSV1 not allowed to be set";
              if (LOG.isDebugEnabled())
              {
                LOG.debug(err + ": Remaining buffer: {}", new Object[] { BufferUtil.toDetailString(buffer) });
              }
              throw new ProtocolException(err);
            }
          }
          if ((b & 0x20) != 0)
          {
            if (isRsv2InUse()) {
              frame.setRsv2(true);
            }
            else {
              String err = "RSV2 not allowed to be set";
              if (LOG.isDebugEnabled())
              {
                LOG.debug(err + ": Remaining buffer: {}", new Object[] { BufferUtil.toDetailString(buffer) });
              }
              throw new ProtocolException(err);
            }
          }
          if ((b & 0x10) != 0)
          {
            if (isRsv3InUse()) {
              frame.setRsv3(true);
            }
            else {
              String err = "RSV3 not allowed to be set";
              if (LOG.isDebugEnabled())
              {
                LOG.debug(err + ": Remaining buffer: {}", new Object[] { BufferUtil.toDetailString(buffer) });
              }
              throw new ProtocolException(err);
            }
          }
        }
        
        state = State.PAYLOAD_LEN;
        break;
      


      case 2: 
        byte b = buffer.get();
        frame.setMasked((b & 0x80) != 0);
        payloadLength = ((byte)(0x7F & b));
        
        if (payloadLength == 127)
        {

          payloadLength = 0;
          state = State.PAYLOAD_LEN_BYTES;
          cursor = 8;

        }
        else if (payloadLength == 126)
        {

          payloadLength = 0;
          state = State.PAYLOAD_LEN_BYTES;
          cursor = 2;
        }
        else
        {
          assertSanePayloadLength(payloadLength);
          if (frame.isMasked())
          {
            state = State.MASK;

          }
          else
          {
            if (payloadLength == 0)
            {
              state = State.START;
              return true;
            }
            
            maskProcessor.reset(frame);
            state = State.PAYLOAD;
          }
        }
        break;
      


      case 3: 
        byte b = buffer.get();
        cursor -= 1;
        payloadLength |= (b & 0xFF) << 8 * cursor;
        if (cursor == 0)
        {
          assertSanePayloadLength(payloadLength);
          if (frame.isMasked())
          {
            state = State.MASK;

          }
          else
          {
            if (payloadLength == 0)
            {
              state = State.START;
              return true;
            }
            
            maskProcessor.reset(frame);
            state = State.PAYLOAD;
          }
        }
        


        break;
      case 4: 
        byte[] m = new byte[4];
        frame.setMask(m);
        if (buffer.remaining() >= 4)
        {
          buffer.get(m, 0, 4);
          
          if (payloadLength == 0)
          {
            state = State.START;
            return true;
          }
          
          maskProcessor.reset(frame);
          state = State.PAYLOAD;
        }
        else
        {
          state = State.MASK_BYTES;
          cursor = 4;
        }
        break;
      


      case 5: 
        byte b = buffer.get();
        frame.getMask()[(4 - cursor)] = b;
        cursor -= 1;
        if (cursor == 0)
        {

          if (payloadLength == 0)
          {
            state = State.START;
            return true;
          }
          
          maskProcessor.reset(frame);
          state = State.PAYLOAD;
        }
        


        break;
      case 6: 
        frame.assertValid();
        if (parsePayload(buffer))
        {

          if (frame.getOpCode() == 8)
          {

            new CloseInfo(frame);
          }
          state = State.START;
          
          return true;
        }
        
        break;
      }
      
    }
    return false;
  }
  







  private boolean parsePayload(ByteBuffer buffer)
  {
    if (payloadLength == 0)
    {
      return true;
    }
    
    if (buffer.hasRemaining())
    {



      int bytesSoFar = payload == null ? 0 : payload.position();
      int bytesExpected = payloadLength - bytesSoFar;
      int bytesAvailable = buffer.remaining();
      int windowBytes = Math.min(bytesAvailable, bytesExpected);
      int limit = buffer.limit();
      buffer.limit(buffer.position() + windowBytes);
      ByteBuffer window = buffer.slice();
      buffer.limit(limit);
      buffer.position(buffer.position() + window.remaining());
      
      if (LOG.isDebugEnabled()) {
        LOG.debug("{} Window: {}", new Object[] { policy.getBehavior(), BufferUtil.toDetailString(window) });
      }
      
      maskProcessor.process(window);
      
      if (window.remaining() == payloadLength)
      {

        frame.setPayload(window);
        return true;
      }
      

      if (payload == null)
      {
        payload = bufferPool.acquire(payloadLength, false);
        BufferUtil.clearToFill(payload);
      }
      
      payload.put(window);
      
      if (payload.position() == payloadLength)
      {
        BufferUtil.flipToFlush(payload, 0);
        frame.setPayload(payload);
        return true;
      }
    }
    
    return false;
  }
  
  public void setIncomingFramesHandler(IncomingFrames incoming)
  {
    incomingFramesHandler = incoming;
  }
  

  public String toString()
  {
    StringBuilder builder = new StringBuilder();
    builder.append("Parser@").append(Integer.toHexString(hashCode()));
    builder.append("[");
    if (incomingFramesHandler == null)
    {
      builder.append("NO_HANDLER");
    }
    else
    {
      builder.append(incomingFramesHandler.getClass().getSimpleName());
    }
    builder.append(",s=").append(state);
    builder.append(",c=").append(cursor);
    builder.append(",len=").append(payloadLength);
    builder.append(",f=").append(frame);
    
    builder.append("]");
    return builder.toString();
  }
}
