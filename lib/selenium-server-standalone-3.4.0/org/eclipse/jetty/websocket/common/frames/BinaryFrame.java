package org.eclipse.jetty.websocket.common.frames;

import java.nio.ByteBuffer;
import org.eclipse.jetty.util.StringUtil;
import org.eclipse.jetty.websocket.api.extensions.Frame.Type;



















public class BinaryFrame
  extends DataFrame
{
  public BinaryFrame()
  {
    super((byte)2);
  }
  
  public BinaryFrame setPayload(ByteBuffer buf)
  {
    super.setPayload(buf);
    return this;
  }
  
  public BinaryFrame setPayload(byte[] buf)
  {
    setPayload(ByteBuffer.wrap(buf));
    return this;
  }
  
  public BinaryFrame setPayload(String payload)
  {
    setPayload(StringUtil.getUtf8Bytes(payload));
    return this;
  }
  

  public Frame.Type getType()
  {
    if (getOpCode() == 0)
      return Frame.Type.CONTINUATION;
    return Frame.Type.BINARY;
  }
}
