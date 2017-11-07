package org.eclipse.jetty.websocket.common;

import java.net.URI;
import org.eclipse.jetty.websocket.common.events.EventDriver;

public abstract interface SessionFactory
{
  public abstract boolean supports(EventDriver paramEventDriver);
  
  public abstract WebSocketSession createSession(URI paramURI, EventDriver paramEventDriver, LogicalConnection paramLogicalConnection);
}
