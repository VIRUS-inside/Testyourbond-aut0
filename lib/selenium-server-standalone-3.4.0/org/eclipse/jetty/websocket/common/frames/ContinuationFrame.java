package org.eclipse.jetty.websocket.common.frames;

import java.nio.ByteBuffer;
import org.eclipse.jetty.util.StringUtil;
import org.eclipse.jetty.websocket.api.extensions.Frame.Type;



















public class ContinuationFrame
  extends DataFrame
{
  public ContinuationFrame()
  {
    super((byte)0);
  }
  
  public ContinuationFrame setPayload(ByteBuffer buf)
  {
    super.setPayload(buf);
    return this;
  }
  
  public ContinuationFrame setPayload(byte[] buf)
  {
    return setPayload(ByteBuffer.wrap(buf));
  }
  
  public ContinuationFrame setPayload(String message)
  {
    return setPayload(StringUtil.getUtf8Bytes(message));
  }
  

  public Frame.Type getType()
  {
    return Frame.Type.CONTINUATION;
  }
}
