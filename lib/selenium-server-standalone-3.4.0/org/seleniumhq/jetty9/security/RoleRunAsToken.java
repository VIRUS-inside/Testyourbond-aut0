package org.seleniumhq.jetty9.security;












public class RoleRunAsToken
  implements RunAsToken
{
  private final String _runAsRole;
  











  public RoleRunAsToken(String runAsRole)
  {
    _runAsRole = runAsRole;
  }
  
  public String getRunAsRole()
  {
    return _runAsRole;
  }
  
  public String toString()
  {
    return "RoleRunAsToken(" + _runAsRole + ")";
  }
}
