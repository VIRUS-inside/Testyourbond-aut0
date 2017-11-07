package org.eclipse.jetty.websocket.client.masks;

import java.util.Random;
import org.eclipse.jetty.websocket.common.WebSocketFrame;



















public class RandomMasker
  implements Masker
{
  private final Random random;
  
  public RandomMasker()
  {
    this(new Random());
  }
  
  public RandomMasker(Random random)
  {
    this.random = random;
  }
  

  public void setMask(WebSocketFrame frame)
  {
    byte[] mask = new byte[4];
    random.nextBytes(mask);
    frame.setMask(mask);
  }
}
