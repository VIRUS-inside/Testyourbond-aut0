package org.eclipse.jetty.websocket.common.io;

import org.eclipse.jetty.websocket.api.BatchMode;
import org.eclipse.jetty.websocket.api.WriteCallback;
import org.eclipse.jetty.websocket.api.extensions.Frame;
import org.eclipse.jetty.websocket.api.extensions.IncomingFrames;
import org.eclipse.jetty.websocket.api.extensions.OutgoingFrames;
















public class FramePipes
{
  public FramePipes() {}
  
  private static class In2Out
    implements IncomingFrames
  {
    private OutgoingFrames outgoing;
    
    public In2Out(OutgoingFrames outgoing)
    {
      this.outgoing = outgoing;
    }
    



    public void incomingError(Throwable t) {}
    


    public void incomingFrame(Frame frame)
    {
      outgoing.outgoingFrame(frame, null, BatchMode.OFF);
    }
  }
  
  private static class Out2In implements OutgoingFrames
  {
    private IncomingFrames incoming;
    
    public Out2In(IncomingFrames incoming)
    {
      this.incoming = incoming;
    }
    

    public void outgoingFrame(Frame frame, WriteCallback callback, BatchMode batchMode)
    {
      try
      {
        incoming.incomingFrame(frame);
        callback.writeSuccess();
      }
      catch (Throwable t)
      {
        callback.writeFailed(t);
      }
    }
  }
  
  public static OutgoingFrames to(IncomingFrames incoming)
  {
    return new Out2In(incoming);
  }
  
  public static IncomingFrames to(OutgoingFrames outgoing)
  {
    return new In2Out(outgoing);
  }
}
