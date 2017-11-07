package org.eclipse.jetty.websocket.common.frames;

import java.nio.ByteBuffer;
import org.eclipse.jetty.util.StringUtil;
import org.eclipse.jetty.websocket.api.extensions.Frame.Type;



















public class PingFrame
  extends ControlFrame
{
  public PingFrame()
  {
    super((byte)9);
  }
  
  public PingFrame setPayload(byte[] bytes)
  {
    setPayload(ByteBuffer.wrap(bytes));
    return this;
  }
  
  public PingFrame setPayload(String payload)
  {
    setPayload(ByteBuffer.wrap(StringUtil.getUtf8Bytes(payload)));
    return this;
  }
  

  public Frame.Type getType()
  {
    return Frame.Type.PING;
  }
}
