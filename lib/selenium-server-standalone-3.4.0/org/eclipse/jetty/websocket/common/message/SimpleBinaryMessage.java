package org.eclipse.jetty.websocket.common.message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.eclipse.jetty.util.BufferUtil;
import org.eclipse.jetty.websocket.api.WebSocketPolicy;
import org.eclipse.jetty.websocket.common.events.EventDriver;


















public class SimpleBinaryMessage
  implements MessageAppender
{
  private static final int BUFFER_SIZE = 65535;
  private final EventDriver onEvent;
  protected final ByteArrayOutputStream out;
  private int size;
  protected boolean finished;
  
  public SimpleBinaryMessage(EventDriver onEvent)
  {
    this.onEvent = onEvent;
    out = new ByteArrayOutputStream(65535);
    finished = false;
  }
  
  public void appendFrame(ByteBuffer payload, boolean isLast)
    throws IOException
  {
    if (finished)
    {
      throw new IOException("Cannot append to finished buffer");
    }
    
    if (payload == null)
    {

      return;
    }
    
    onEvent.getPolicy().assertValidBinaryMessageSize(size + payload.remaining());
    size += payload.remaining();
    
    BufferUtil.writeTo(payload, out);
  }
  

  public void messageComplete()
  {
    finished = true;
    byte[] data = out.toByteArray();
    onEvent.onBinaryMessage(data);
  }
}
