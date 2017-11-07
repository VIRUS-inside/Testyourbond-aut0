package org.seleniumhq.jetty9.security;

import org.seleniumhq.jetty9.server.UserIdentity;























public class UserAuthentication
  extends AbstractUserAuthentication
{
  public UserAuthentication(String method, UserIdentity userIdentity)
  {
    super(method, userIdentity);
  }
  


  public String toString()
  {
    return "{User," + getAuthMethod() + "," + _userIdentity + "}";
  }
  
  public void logout()
  {
    SecurityHandler security = SecurityHandler.getCurrentSecurityHandler();
    if (security != null) {
      security.logout(this);
    }
  }
}
