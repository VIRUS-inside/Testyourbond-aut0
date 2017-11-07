package org.eclipse.jetty.websocket.api;
























public class PolicyViolationException
  extends CloseException
{
  public PolicyViolationException(String message)
  {
    super(1008, message);
  }
  
  public PolicyViolationException(String message, Throwable t)
  {
    super(1008, message, t);
  }
  
  public PolicyViolationException(Throwable t)
  {
    super(1008, t);
  }
}
