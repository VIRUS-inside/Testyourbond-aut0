package org.eclipse.jetty.websocket.common.frames;

import java.nio.ByteBuffer;
import org.eclipse.jetty.util.BufferUtil;
import org.eclipse.jetty.util.StringUtil;
import org.eclipse.jetty.websocket.api.extensions.Frame.Type;



















public class TextFrame
  extends DataFrame
{
  public TextFrame()
  {
    super((byte)1);
  }
  

  public Frame.Type getType()
  {
    if (getOpCode() == 0)
      return Frame.Type.CONTINUATION;
    return Frame.Type.TEXT;
  }
  
  public TextFrame setPayload(String str)
  {
    setPayload(ByteBuffer.wrap(StringUtil.getUtf8Bytes(str)));
    return this;
  }
  
  public String getPayloadAsUTF8()
  {
    if (data == null)
    {
      return null;
    }
    return BufferUtil.toUTF8String(data);
  }
}
