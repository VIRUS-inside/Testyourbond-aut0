package org.eclipse.jetty.websocket.common.message;

import java.io.IOException;
import java.nio.ByteBuffer;

public abstract interface MessageAppender
{
  public abstract void appendFrame(ByteBuffer paramByteBuffer, boolean paramBoolean)
    throws IOException;
  
  public abstract void messageComplete();
}
