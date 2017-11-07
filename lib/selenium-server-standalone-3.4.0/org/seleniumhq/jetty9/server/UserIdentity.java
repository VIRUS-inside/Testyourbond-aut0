package org.seleniumhq.jetty9.server;

import java.security.Principal;
import java.util.Map;
import javax.security.auth.Subject;





















































































public abstract interface UserIdentity
{
  public static final UserIdentity UNAUTHENTICATED_IDENTITY = new UnauthenticatedUserIdentity()
  {
    public Subject getSubject()
    {
      return null;
    }
    
    public Principal getUserPrincipal()
    {
      return null;
    }
    
    public boolean isUserInRole(String role, UserIdentity.Scope scope)
    {
      return false;
    }
    

    public String toString()
    {
      return "UNAUTHENTICATED";
    }
  };
  
  public abstract Subject getSubject();
  
  public abstract Principal getUserPrincipal();
  
  public abstract boolean isUserInRole(String paramString, Scope paramScope);
  
  public static abstract interface UnauthenticatedUserIdentity
    extends UserIdentity
  {}
  
  public static abstract interface Scope
  {
    public abstract String getContextPath();
    
    public abstract String getName();
    
    public abstract Map<String, String> getRoleRefMap();
  }
}
