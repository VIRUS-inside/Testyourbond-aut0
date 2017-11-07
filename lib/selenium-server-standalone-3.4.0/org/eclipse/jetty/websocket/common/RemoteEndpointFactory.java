package org.eclipse.jetty.websocket.common;

import org.eclipse.jetty.websocket.api.BatchMode;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.extensions.OutgoingFrames;

public abstract interface RemoteEndpointFactory
{
  public abstract RemoteEndpoint newRemoteEndpoint(LogicalConnection paramLogicalConnection, OutgoingFrames paramOutgoingFrames, BatchMode paramBatchMode);
}
