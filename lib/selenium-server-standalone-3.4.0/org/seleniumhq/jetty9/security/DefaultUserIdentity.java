package org.seleniumhq.jetty9.security;

import java.security.Principal;
import java.util.Map;
import javax.security.auth.Subject;
import org.seleniumhq.jetty9.server.UserIdentity;
import org.seleniumhq.jetty9.server.UserIdentity.Scope;
























public class DefaultUserIdentity
  implements UserIdentity
{
  private final Subject _subject;
  private final Principal _userPrincipal;
  private final String[] _roles;
  
  public DefaultUserIdentity(Subject subject, Principal userPrincipal, String[] roles)
  {
    _subject = subject;
    _userPrincipal = userPrincipal;
    _roles = roles;
  }
  
  public Subject getSubject()
  {
    return _subject;
  }
  
  public Principal getUserPrincipal()
  {
    return _userPrincipal;
  }
  

  public boolean isUserInRole(String role, UserIdentity.Scope scope)
  {
    if ("*".equals(role)) {
      return false;
    }
    String roleToTest = null;
    if ((scope != null) && (scope.getRoleRefMap() != null)) {
      roleToTest = (String)scope.getRoleRefMap().get(role);
    }
    
    if (roleToTest == null) {
      roleToTest = role;
    }
    for (String r : _roles)
      if (r.equals(roleToTest))
        return true;
    return false;
  }
  

  public String toString()
  {
    return DefaultUserIdentity.class.getSimpleName() + "('" + _userPrincipal + "')";
  }
}
