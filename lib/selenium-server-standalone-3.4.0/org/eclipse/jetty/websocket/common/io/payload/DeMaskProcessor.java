package org.eclipse.jetty.websocket.common.io.payload;

import java.nio.ByteBuffer;
import org.eclipse.jetty.websocket.api.extensions.Frame;


















public class DeMaskProcessor
  implements PayloadProcessor
{
  private byte[] maskBytes;
  private int maskInt;
  private int maskOffset;
  
  public DeMaskProcessor() {}
  
  public void process(ByteBuffer payload)
  {
    if (maskBytes == null)
    {
      return;
    }
    
    int maskInt = this.maskInt;
    int start = payload.position();
    int end = payload.limit();
    int offset = maskOffset;
    int remaining;
    while ((remaining = end - start) > 0)
    {
      if ((remaining >= 4) && ((offset & 0x3) == 0))
      {
        payload.putInt(start, payload.getInt(start) ^ maskInt);
        start += 4;
        offset += 4;
      }
      else
      {
        payload.put(start, (byte)(payload.get(start) ^ maskBytes[(offset & 0x3)]));
        start++;
        offset++;
      }
    }
    maskOffset = offset;
  }
  
  public void reset(byte[] mask)
  {
    maskBytes = mask;
    int maskInt = 0;
    if (mask != null)
    {
      for (byte maskByte : mask)
        maskInt = (maskInt << 8) + (maskByte & 0xFF);
    }
    this.maskInt = maskInt;
    maskOffset = 0;
  }
  

  public void reset(Frame frame)
  {
    reset(frame.getMask());
  }
}
