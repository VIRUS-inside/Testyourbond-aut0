package org.seleniumhq.jetty9.security;

import java.security.Principal;
import java.util.List;
import javax.security.auth.Subject;
import org.seleniumhq.jetty9.server.UserIdentity;
import org.seleniumhq.jetty9.server.UserIdentity.Scope;



















public class SpnegoUserIdentity
  implements UserIdentity
{
  private Subject _subject;
  private Principal _principal;
  private List<String> _roles;
  
  public SpnegoUserIdentity(Subject subject, Principal principal, List<String> roles)
  {
    _subject = subject;
    _principal = principal;
    _roles = roles;
  }
  

  public Subject getSubject()
  {
    return _subject;
  }
  
  public Principal getUserPrincipal()
  {
    return _principal;
  }
  
  public boolean isUserInRole(String role, UserIdentity.Scope scope)
  {
    return _roles.contains(role);
  }
}
