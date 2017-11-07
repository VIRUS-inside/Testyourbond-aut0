package org.seleniumhq.jetty9.security;

import java.security.Principal;
import javax.security.auth.Subject;
import org.seleniumhq.jetty9.server.UserIdentity;








































public class DefaultIdentityService
  implements IdentityService
{
  public DefaultIdentityService() {}
  
  public Object associate(UserIdentity user)
  {
    return null;
  }
  


  public void disassociate(Object previous) {}
  


  public Object setRunAs(UserIdentity user, RunAsToken token)
  {
    return token;
  }
  


  public void unsetRunAs(Object lastToken) {}
  


  public RunAsToken newRunAsToken(String runAsName)
  {
    return new RoleRunAsToken(runAsName);
  }
  

  public UserIdentity getSystemUserIdentity()
  {
    return null;
  }
  

  public UserIdentity newUserIdentity(Subject subject, Principal userPrincipal, String[] roles)
  {
    return new DefaultUserIdentity(subject, userPrincipal, roles);
  }
}
