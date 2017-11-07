package org.seleniumhq.jetty9.security;

import javax.servlet.ServletRequest;
import org.seleniumhq.jetty9.server.UserIdentity;

public abstract interface LoginService
{
  public abstract String getName();
  
  public abstract UserIdentity login(String paramString, Object paramObject, ServletRequest paramServletRequest);
  
  public abstract boolean validate(UserIdentity paramUserIdentity);
  
  public abstract IdentityService getIdentityService();
  
  public abstract void setIdentityService(IdentityService paramIdentityService);
  
  public abstract void logout(UserIdentity paramUserIdentity);
}
