package org.apache.http.auth;

import java.io.Serializable;
import java.security.Principal;
import java.util.Locale;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.util.Args;
import org.apache.http.util.LangUtils;



































@Contract(threading=ThreadingBehavior.IMMUTABLE)
public class NTUserPrincipal
  implements Principal, Serializable
{
  private static final long serialVersionUID = -6870169797924406894L;
  private final String username;
  private final String domain;
  private final String ntname;
  
  public NTUserPrincipal(String domain, String username)
  {
    Args.notNull(username, "User name");
    this.username = username;
    if (domain != null) {
      this.domain = domain.toUpperCase(Locale.ROOT);
    } else {
      this.domain = null;
    }
    if ((this.domain != null) && (!this.domain.isEmpty())) {
      StringBuilder buffer = new StringBuilder();
      buffer.append(this.domain);
      buffer.append('\\');
      buffer.append(this.username);
      ntname = buffer.toString();
    } else {
      ntname = this.username;
    }
  }
  
  public String getName()
  {
    return ntname;
  }
  
  public String getDomain() {
    return domain;
  }
  
  public String getUsername() {
    return username;
  }
  
  public int hashCode()
  {
    int hash = 17;
    hash = LangUtils.hashCode(hash, username);
    hash = LangUtils.hashCode(hash, domain);
    return hash;
  }
  
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if ((o instanceof NTUserPrincipal)) {
      NTUserPrincipal that = (NTUserPrincipal)o;
      if ((LangUtils.equals(username, username)) && (LangUtils.equals(domain, domain)))
      {
        return true;
      }
    }
    return false;
  }
  
  public String toString()
  {
    return ntname;
  }
}