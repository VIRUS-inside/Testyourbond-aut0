package org.eclipse.jetty.websocket.api.extensions;

import org.eclipse.jetty.websocket.api.BatchMode;
import org.eclipse.jetty.websocket.api.WriteCallback;

public abstract interface OutgoingFrames
{
  public abstract void outgoingFrame(Frame paramFrame, WriteCallback paramWriteCallback, BatchMode paramBatchMode);
}
