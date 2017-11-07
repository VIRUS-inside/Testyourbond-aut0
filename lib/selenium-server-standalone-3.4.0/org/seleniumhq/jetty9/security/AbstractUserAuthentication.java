package org.seleniumhq.jetty9.security;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import org.seleniumhq.jetty9.server.Authentication.User;
import org.seleniumhq.jetty9.server.UserIdentity;
import org.seleniumhq.jetty9.server.UserIdentity.Scope;


























public abstract class AbstractUserAuthentication
  implements Authentication.User, Serializable
{
  private static final long serialVersionUID = -6290411814232723403L;
  protected String _method;
  protected transient UserIdentity _userIdentity;
  
  public AbstractUserAuthentication(String method, UserIdentity userIdentity)
  {
    _method = method;
    _userIdentity = userIdentity;
  }
  


  public String getAuthMethod()
  {
    return _method;
  }
  

  public UserIdentity getUserIdentity()
  {
    return _userIdentity;
  }
  

  public boolean isUserInRole(UserIdentity.Scope scope, String role)
  {
    String roleToTest = null;
    if ((scope != null) && (scope.getRoleRefMap() != null))
      roleToTest = (String)scope.getRoleRefMap().get(role);
    if (roleToTest == null) {
      roleToTest = role;
    }
    if ("**".equals(roleToTest.trim()))
    {



      if (!declaredRolesContains("**")) {
        return true;
      }
      return _userIdentity.isUserInRole(role, scope);
    }
    
    return _userIdentity.isUserInRole(role, scope);
  }
  
  public boolean declaredRolesContains(String roleName)
  {
    SecurityHandler security = SecurityHandler.getCurrentSecurityHandler();
    if (security == null) {
      return false;
    }
    if ((security instanceof ConstraintAware))
    {
      Set<String> declaredRoles = ((ConstraintAware)security).getRoles();
      return (declaredRoles != null) && (declaredRoles.contains(roleName));
    }
    
    return false;
  }
}
