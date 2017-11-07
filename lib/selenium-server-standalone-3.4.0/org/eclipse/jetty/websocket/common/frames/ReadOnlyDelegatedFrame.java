package org.eclipse.jetty.websocket.common.frames;

import java.nio.ByteBuffer;
import org.eclipse.jetty.websocket.api.extensions.Frame;
import org.eclipse.jetty.websocket.api.extensions.Frame.Type;





















public class ReadOnlyDelegatedFrame
  implements Frame
{
  private final Frame delegate;
  
  public ReadOnlyDelegatedFrame(Frame frame)
  {
    delegate = frame;
  }
  

  public byte[] getMask()
  {
    return delegate.getMask();
  }
  

  public byte getOpCode()
  {
    return delegate.getOpCode();
  }
  

  public ByteBuffer getPayload()
  {
    if (!delegate.hasPayload()) {
      return null;
    }
    return delegate.getPayload().asReadOnlyBuffer();
  }
  

  public int getPayloadLength()
  {
    return delegate.getPayloadLength();
  }
  

  public Frame.Type getType()
  {
    return delegate.getType();
  }
  

  public boolean hasPayload()
  {
    return delegate.hasPayload();
  }
  

  public boolean isFin()
  {
    return delegate.isFin();
  }
  

  @Deprecated
  public boolean isLast()
  {
    return delegate.isLast();
  }
  

  public boolean isMasked()
  {
    return delegate.isMasked();
  }
  

  public boolean isRsv1()
  {
    return delegate.isRsv1();
  }
  

  public boolean isRsv2()
  {
    return delegate.isRsv2();
  }
  

  public boolean isRsv3()
  {
    return delegate.isRsv3();
  }
}
