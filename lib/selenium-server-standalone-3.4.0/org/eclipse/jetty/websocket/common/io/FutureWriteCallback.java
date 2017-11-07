package org.eclipse.jetty.websocket.common.io;

import org.eclipse.jetty.util.FutureCallback;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.websocket.api.WriteCallback;






















public class FutureWriteCallback
  extends FutureCallback
  implements WriteCallback
{
  private static final Logger LOG = Log.getLogger(FutureWriteCallback.class);
  
  public FutureWriteCallback() {}
  
  public void writeFailed(Throwable cause) {
    if (LOG.isDebugEnabled())
      LOG.debug(".writeFailed", cause);
    failed(cause);
  }
  

  public void writeSuccess()
  {
    if (LOG.isDebugEnabled())
      LOG.debug(".writeSuccess", new Object[0]);
    succeeded();
  }
}
