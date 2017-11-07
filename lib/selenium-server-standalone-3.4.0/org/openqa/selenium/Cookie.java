package org.openqa.selenium;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;


























public class Cookie
  implements Serializable
{
  private static final long serialVersionUID = 4115876353625612383L;
  private final String name;
  private final String value;
  private final String path;
  private final String domain;
  private final Date expiry;
  private final boolean isSecure;
  private final boolean isHttpOnly;
  
  public Cookie(String name, String value, String path, Date expiry)
  {
    this(name, value, null, path, expiry);
  }
  










  public Cookie(String name, String value, String domain, String path, Date expiry)
  {
    this(name, value, domain, path, expiry, false);
  }
  











  public Cookie(String name, String value, String domain, String path, Date expiry, boolean isSecure)
  {
    this(name, value, domain, path, expiry, isSecure, false);
  }
  












  public Cookie(String name, String value, String domain, String path, Date expiry, boolean isSecure, boolean isHttpOnly)
  {
    this.name = name;
    this.value = value;
    this.path = ((path == null) || ("".equals(path)) ? "/" : path);
    
    this.domain = stripPort(domain);
    this.isSecure = isSecure;
    this.isHttpOnly = isHttpOnly;
    
    if (expiry != null)
    {
      this.expiry = new Date(expiry.getTime() / 1000L * 1000L);
    } else {
      this.expiry = null;
    }
  }
  





  public Cookie(String name, String value)
  {
    this(name, value, "/", null);
  }
  






  public Cookie(String name, String value, String path)
  {
    this(name, value, path, null);
  }
  
  public String getName() {
    return name;
  }
  
  public String getValue() {
    return value;
  }
  
  public String getDomain() {
    return domain;
  }
  
  public String getPath() {
    return path;
  }
  
  public boolean isSecure() {
    return isSecure;
  }
  
  public boolean isHttpOnly() {
    return isHttpOnly;
  }
  
  public Date getExpiry() {
    return expiry;
  }
  
  private static String stripPort(String domain) {
    return domain == null ? null : domain.split(":")[0];
  }
  
  public void validate() {
    if ((name == null) || ("".equals(name)) || (value == null) || (path == null)) {
      throw new IllegalArgumentException("Required attributes are not set or any non-null attribute set to null");
    }
    

    if (name.indexOf(';') != -1) {
      throw new IllegalArgumentException("Cookie names cannot contain a ';': " + name);
    }
    

    if ((domain != null) && (domain.contains(":"))) {
      throw new IllegalArgumentException("Domain should not contain a port: " + domain);
    }
  }
  
  public String toString()
  {
    return 
    


      name + "=" + value + (expiry == null ? "" : new StringBuilder().append("; expires=").append(new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss z").format(expiry)).toString()) + ("".equals(path) ? "" : new StringBuilder().append("; path=").append(path).toString()) + (domain == null ? "" : new StringBuilder().append("; domain=").append(domain).toString()) + (isSecure ? ";secure;" : "");
  }
  





  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Cookie)) {
      return false;
    }
    
    Cookie cookie = (Cookie)o;
    
    if (!name.equals(name)) {
      return false;
    }
    return value != null ? value.equals(value) : value == null;
  }
  
  public int hashCode()
  {
    return name.hashCode();
  }
  
  public static class Builder
  {
    private final String name;
    private final String value;
    private String path;
    private String domain;
    private Date expiry;
    private boolean secure;
    private boolean httpOnly;
    
    public Builder(String name, String value) {
      this.name = name;
      this.value = value;
    }
    
    public Builder domain(String host) {
      domain = Cookie.stripPort(host);
      return this;
    }
    
    public Builder path(String path) {
      this.path = path;
      return this;
    }
    
    public Builder expiresOn(Date expiry) {
      this.expiry = expiry;
      return this;
    }
    
    public Builder isSecure(boolean secure) {
      this.secure = secure;
      return this;
    }
    
    public Builder isHttpOnly(boolean httpOnly) {
      this.httpOnly = httpOnly;
      return this;
    }
    
    public Cookie build() {
      return new Cookie(name, value, domain, path, expiry, secure, httpOnly);
    }
  }
}
