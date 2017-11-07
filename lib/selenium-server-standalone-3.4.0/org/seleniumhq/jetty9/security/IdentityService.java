package org.seleniumhq.jetty9.security;

import java.security.Principal;
import javax.security.auth.Subject;
import org.seleniumhq.jetty9.server.UserIdentity;
























public abstract interface IdentityService
{
  public static final String[] NO_ROLES = new String[0];
  
  public abstract Object associate(UserIdentity paramUserIdentity);
  
  public abstract void disassociate(Object paramObject);
  
  public abstract Object setRunAs(UserIdentity paramUserIdentity, RunAsToken paramRunAsToken);
  
  public abstract void unsetRunAs(Object paramObject);
  
  public abstract UserIdentity newUserIdentity(Subject paramSubject, Principal paramPrincipal, String[] paramArrayOfString);
  
  public abstract RunAsToken newRunAsToken(String paramString);
  
  public abstract UserIdentity getSystemUserIdentity();
}
