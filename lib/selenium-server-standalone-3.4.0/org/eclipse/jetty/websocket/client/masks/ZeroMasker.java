package org.eclipse.jetty.websocket.client.masks;

import java.util.Arrays;
import org.eclipse.jetty.websocket.common.WebSocketFrame;



















public class ZeroMasker
  implements Masker
{
  private final byte[] mask;
  
  public ZeroMasker()
  {
    mask = new byte[4];
    Arrays.fill(mask, (byte)0);
  }
  

  public void setMask(WebSocketFrame frame)
  {
    frame.setMask(mask);
  }
}
