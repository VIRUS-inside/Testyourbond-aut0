package org.apache.http.auth;

import java.io.Serializable;
import java.security.Principal;
import java.util.Locale;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.util.Args;
import org.apache.http.util.LangUtils;














































@Contract(threading=ThreadingBehavior.IMMUTABLE)
public class NTCredentials
  implements Credentials, Serializable
{
  private static final long serialVersionUID = -7385699315228907265L;
  private final NTUserPrincipal principal;
  private final String password;
  private final String workstation;
  
  @Deprecated
  public NTCredentials(String usernamePassword)
  {
    Args.notNull(usernamePassword, "Username:password string");
    
    int atColon = usernamePassword.indexOf(':');
    String username; if (atColon >= 0) {
      String username = usernamePassword.substring(0, atColon);
      password = usernamePassword.substring(atColon + 1);
    } else {
      username = usernamePassword;
      password = null;
    }
    int atSlash = username.indexOf('/');
    if (atSlash >= 0) {
      principal = new NTUserPrincipal(username.substring(0, atSlash).toUpperCase(Locale.ROOT), username.substring(atSlash + 1));
    }
    else
    {
      principal = new NTUserPrincipal(null, username.substring(atSlash + 1));
    }
    

    workstation = null;
  }
  













  public NTCredentials(String userName, String password, String workstation, String domain)
  {
    Args.notNull(userName, "User name");
    principal = new NTUserPrincipal(domain, userName);
    this.password = password;
    if (workstation != null) {
      this.workstation = workstation.toUpperCase(Locale.ROOT);
    } else {
      this.workstation = null;
    }
  }
  
  public Principal getUserPrincipal()
  {
    return principal;
  }
  
  public String getUserName() {
    return principal.getUsername();
  }
  
  public String getPassword()
  {
    return password;
  }
  




  public String getDomain()
  {
    return principal.getDomain();
  }
  




  public String getWorkstation()
  {
    return workstation;
  }
  
  public int hashCode()
  {
    int hash = 17;
    hash = LangUtils.hashCode(hash, principal);
    hash = LangUtils.hashCode(hash, workstation);
    return hash;
  }
  
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if ((o instanceof NTCredentials)) {
      NTCredentials that = (NTCredentials)o;
      if ((LangUtils.equals(principal, principal)) && (LangUtils.equals(workstation, workstation)))
      {
        return true;
      }
    }
    return false;
  }
  
  public String toString()
  {
    StringBuilder buffer = new StringBuilder();
    buffer.append("[principal: ");
    buffer.append(principal);
    buffer.append("][workstation: ");
    buffer.append(workstation);
    buffer.append("]");
    return buffer.toString();
  }
}
