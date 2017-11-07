package org.eclipse.jetty.websocket.common.frames;

import java.nio.ByteBuffer;
import org.eclipse.jetty.util.StringUtil;
import org.eclipse.jetty.websocket.api.extensions.Frame.Type;



















public class PongFrame
  extends ControlFrame
{
  public PongFrame()
  {
    super((byte)10);
  }
  
  public PongFrame setPayload(byte[] bytes)
  {
    setPayload(ByteBuffer.wrap(bytes));
    return this;
  }
  
  public PongFrame setPayload(String payload)
  {
    setPayload(StringUtil.getUtf8Bytes(payload));
    return this;
  }
  

  public Frame.Type getType()
  {
    return Frame.Type.PONG;
  }
}
