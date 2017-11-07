package org.eclipse.jetty.websocket.api;






public class WebSocketAdapter
  implements WebSocketListener
{
  private volatile Session session;
  




  private RemoteEndpoint remote;
  





  public WebSocketAdapter() {}
  





  public RemoteEndpoint getRemote()
  {
    return remote;
  }
  
  public Session getSession()
  {
    return session;
  }
  
  public boolean isConnected()
  {
    Session sess = session;
    return (sess != null) && (sess.isOpen());
  }
  
  public boolean isNotConnected()
  {
    Session sess = session;
    return (sess == null) || (!sess.isOpen());
  }
  



  public void onWebSocketBinary(byte[] payload, int offset, int len) {}
  


  public void onWebSocketClose(int statusCode, String reason)
  {
    session = null;
    remote = null;
  }
  

  public void onWebSocketConnect(Session sess)
  {
    session = sess;
    remote = sess.getRemote();
  }
  
  public void onWebSocketError(Throwable cause) {}
  
  public void onWebSocketText(String message) {}
}
