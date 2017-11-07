package org.seleniumhq.jetty9.server.session;









public class UnreadableSessionDataException
  extends Exception
{
  private static final long serialVersionUID = 1806303483488966566L;
  







  private String _id;
  






  private SessionContext _sessionContext;
  







  public String getId()
  {
    return _id;
  }
  



  public SessionContext getSessionContext()
  {
    return _sessionContext;
  }
  






  public UnreadableSessionDataException(String id, SessionContext contextId, Throwable t)
  {
    super("Unreadable session " + id + " for " + contextId, t);
    _sessionContext = contextId;
    _id = id;
  }
}
