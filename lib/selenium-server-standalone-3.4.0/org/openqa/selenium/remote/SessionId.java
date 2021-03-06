package org.openqa.selenium.remote;









public class SessionId
{
  private final String opaqueKey;
  








  public SessionId(String opaqueKey)
  {
    this.opaqueKey = opaqueKey;
  }
  
  public String toString()
  {
    return opaqueKey;
  }
  
  public int hashCode()
  {
    return opaqueKey.hashCode();
  }
  
  public boolean equals(Object obj)
  {
    if ((obj instanceof SessionId)) {
      return opaqueKey.equals(opaqueKey);
    }
    return false;
  }
}
