package org.eclipse.jetty.websocket.common.frames;

import org.eclipse.jetty.websocket.api.extensions.Frame;
import org.eclipse.jetty.websocket.common.WebSocketFrame;






















public class DataFrame
  extends WebSocketFrame
{
  protected DataFrame(byte opcode)
  {
    super(opcode);
  }
  






  public DataFrame(Frame basedOn)
  {
    this(basedOn, false);
  }
  







  public DataFrame(Frame basedOn, boolean continuation)
  {
    super(basedOn.getOpCode());
    copyHeaders(basedOn);
    if (continuation)
    {
      setOpCode((byte)0);
    }
  }
  



  public void assertValid() {}
  


  public boolean isControlFrame()
  {
    return false;
  }
  

  public boolean isDataFrame()
  {
    return true;
  }
  



  public void setIsContinuation()
  {
    setOpCode((byte)0);
  }
}
