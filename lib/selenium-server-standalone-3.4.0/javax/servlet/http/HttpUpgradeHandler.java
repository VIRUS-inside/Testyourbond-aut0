package javax.servlet.http;

public abstract interface HttpUpgradeHandler
{
  public abstract void init(WebConnection paramWebConnection);
  
  public abstract void destroy();
}
