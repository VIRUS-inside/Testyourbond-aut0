package org.seleniumhq.jetty9.security.authentication;

import java.security.Principal;
import javax.security.auth.Subject;
import org.seleniumhq.jetty9.security.IdentityService;
































public class LoginCallbackImpl
  implements LoginCallback
{
  private final Subject subject;
  private final String userName;
  private Object credential;
  private boolean success;
  private Principal userPrincipal;
  private String[] roles = IdentityService.NO_ROLES;
  

  public LoginCallbackImpl(Subject subject, String userName, Object credential)
  {
    this.subject = subject;
    this.userName = userName;
    this.credential = credential;
  }
  
  public Subject getSubject()
  {
    return subject;
  }
  
  public String getUserName()
  {
    return userName;
  }
  
  public Object getCredential()
  {
    return credential;
  }
  
  public boolean isSuccess()
  {
    return success;
  }
  
  public void setSuccess(boolean success)
  {
    this.success = success;
  }
  
  public Principal getUserPrincipal()
  {
    return userPrincipal;
  }
  
  public void setUserPrincipal(Principal userPrincipal)
  {
    this.userPrincipal = userPrincipal;
  }
  
  public String[] getRoles()
  {
    return roles;
  }
  
  public void setRoles(String[] groups)
  {
    roles = groups;
  }
  
  public void clearPassword()
  {
    if (credential != null)
    {
      credential = null;
    }
  }
}
