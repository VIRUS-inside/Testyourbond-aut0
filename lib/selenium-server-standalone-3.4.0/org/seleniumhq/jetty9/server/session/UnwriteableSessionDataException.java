package org.seleniumhq.jetty9.server.session;










public class UnwriteableSessionDataException
  extends Exception
{
  private String _id;
  







  private SessionContext _sessionContext;
  








  public UnwriteableSessionDataException(String id, SessionContext contextId, Throwable t)
  {
    super("Unwriteable session " + id + " for " + contextId, t);
    _id = id;
  }
  
  public String getId()
  {
    return _id;
  }
  
  public SessionContext getSessionContext()
  {
    return _sessionContext;
  }
}
