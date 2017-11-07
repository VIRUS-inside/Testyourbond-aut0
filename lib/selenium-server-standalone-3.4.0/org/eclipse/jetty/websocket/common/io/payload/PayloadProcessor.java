package org.eclipse.jetty.websocket.common.io.payload;

import java.nio.ByteBuffer;
import org.eclipse.jetty.websocket.api.extensions.Frame;

public abstract interface PayloadProcessor
{
  public abstract void process(ByteBuffer paramByteBuffer);
  
  public abstract void reset(Frame paramFrame);
}
