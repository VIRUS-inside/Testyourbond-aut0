package org.seleniumhq.jetty9.security.authentication;

import java.security.Principal;
import javax.security.auth.Subject;

public abstract interface LoginCallback
{
  public abstract Subject getSubject();
  
  public abstract String getUserName();
  
  public abstract Object getCredential();
  
  public abstract boolean isSuccess();
  
  public abstract void setSuccess(boolean paramBoolean);
  
  public abstract Principal getUserPrincipal();
  
  public abstract void setUserPrincipal(Principal paramPrincipal);
  
  public abstract String[] getRoles();
  
  public abstract void setRoles(String[] paramArrayOfString);
  
  public abstract void clearPassword();
}
