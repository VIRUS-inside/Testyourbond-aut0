package org.eclipse.jetty.websocket.common.extensions.compress;

import java.nio.ByteBuffer;
import java.util.zip.DataFormatException;
import org.eclipse.jetty.websocket.api.BadPayloadException;
import org.eclipse.jetty.websocket.api.extensions.Frame;
import org.eclipse.jetty.websocket.api.extensions.Frame.Type;





















public class DeflateFrameExtension
  extends CompressExtension
{
  public DeflateFrameExtension() {}
  
  public String getName()
  {
    return "deflate-frame";
  }
  

  int getRsvUseMode()
  {
    return 0;
  }
  

  int getTailDropMode()
  {
    return 1;
  }
  





  public void incomingFrame(Frame frame)
  {
    if ((frame.getType().isControl()) || (!frame.isRsv1()) || (!frame.hasPayload()))
    {
      nextIncomingFrame(frame);
      return;
    }
    
    try
    {
      ByteAccumulator accumulator = newByteAccumulator();
      decompress(accumulator, frame.getPayload());
      decompress(accumulator, TAIL_BYTES_BUF.slice());
      forwardIncoming(frame, accumulator);
    }
    catch (DataFormatException e)
    {
      throw new BadPayloadException(e);
    }
  }
}
