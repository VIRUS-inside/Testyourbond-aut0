package org.eclipse.jetty.websocket.common.events;

import org.eclipse.jetty.websocket.common.events.annotated.CallableMethod;
import org.eclipse.jetty.websocket.common.events.annotated.OptionalSessionCallableMethod;
























public class JettyAnnotatedMetadata
{
  public CallableMethod onConnect;
  public OptionalSessionCallableMethod onBinary;
  public OptionalSessionCallableMethod onText;
  public OptionalSessionCallableMethod onFrame;
  public OptionalSessionCallableMethod onError;
  public OptionalSessionCallableMethod onClose;
  
  public JettyAnnotatedMetadata() {}
  
  public String toString()
  {
    StringBuilder s = new StringBuilder();
    s.append("JettyPojoMetadata[");
    s.append("onConnect=").append(onConnect);
    s.append(",onBinary=").append(onBinary);
    s.append(",onText=").append(onText);
    s.append(",onFrame=").append(onFrame);
    s.append(",onError=").append(onError);
    s.append(",onClose=").append(onClose);
    s.append("]");
    return s.toString();
  }
}
