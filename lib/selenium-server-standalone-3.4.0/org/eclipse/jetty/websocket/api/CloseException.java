package org.eclipse.jetty.websocket.api;










public class CloseException
  extends WebSocketException
{
  private int statusCode;
  









  public CloseException(int closeCode, String message)
  {
    super(message);
    statusCode = closeCode;
  }
  
  public CloseException(int closeCode, String message, Throwable cause)
  {
    super(message, cause);
    statusCode = closeCode;
  }
  
  public CloseException(int closeCode, Throwable cause)
  {
    super(cause);
    statusCode = closeCode;
  }
  
  public int getStatusCode()
  {
    return statusCode;
  }
}
