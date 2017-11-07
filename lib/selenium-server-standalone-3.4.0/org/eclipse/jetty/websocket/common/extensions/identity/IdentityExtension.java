package org.eclipse.jetty.websocket.common.extensions.identity;

import org.eclipse.jetty.util.QuotedStringTokenizer;
import org.eclipse.jetty.util.annotation.ManagedObject;
import org.eclipse.jetty.websocket.api.BatchMode;
import org.eclipse.jetty.websocket.api.WriteCallback;
import org.eclipse.jetty.websocket.api.extensions.ExtensionConfig;
import org.eclipse.jetty.websocket.api.extensions.Frame;
import org.eclipse.jetty.websocket.common.extensions.AbstractExtension;
















@ManagedObject("Identity Extension")
public class IdentityExtension
  extends AbstractExtension
{
  private String id;
  
  public IdentityExtension() {}
  
  public String getParam(String key)
  {
    return getConfig().getParameter(key, "?");
  }
  

  public String getName()
  {
    return "identity";
  }
  


  public void incomingError(Throwable e)
  {
    nextIncomingError(e);
  }
  


  public void incomingFrame(Frame frame)
  {
    nextIncomingFrame(frame);
  }
  


  public void outgoingFrame(Frame frame, WriteCallback callback, BatchMode batchMode)
  {
    nextOutgoingFrame(frame, callback, batchMode);
  }
  

  public void setConfig(ExtensionConfig config)
  {
    super.setConfig(config);
    StringBuilder s = new StringBuilder();
    s.append(config.getName());
    s.append("@").append(Integer.toHexString(hashCode()));
    s.append("[");
    boolean delim = false;
    for (String param : config.getParameterKeys())
    {
      if (delim)
      {
        s.append(';');
      }
      s.append(param).append('=').append(QuotedStringTokenizer.quoteIfNeeded(config.getParameter(param, ""), ";="));
      delim = true;
    }
    s.append("]");
    id = s.toString();
  }
  

  public String toString()
  {
    return id;
  }
}
