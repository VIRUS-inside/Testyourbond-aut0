package org.seleniumhq.jetty9.io;

import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import org.seleniumhq.jetty9.util.thread.Scheduler;
























@Deprecated
public class SelectChannelEndPoint
  extends SocketChannelEndPoint
{
  public SelectChannelEndPoint(SelectableChannel channel, ManagedSelector selector, SelectionKey key, Scheduler scheduler, long idleTimeout)
  {
    super(channel, selector, key, scheduler);
    setIdleTimeout(idleTimeout);
  }
}
