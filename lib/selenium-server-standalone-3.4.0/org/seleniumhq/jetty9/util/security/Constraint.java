package org.seleniumhq.jetty9.util.security;

import java.io.Serializable;
import java.util.Arrays;




















public class Constraint
  implements Cloneable, Serializable
{
  public static final String __BASIC_AUTH = "BASIC";
  public static final String __FORM_AUTH = "FORM";
  public static final String __DIGEST_AUTH = "DIGEST";
  public static final String __CERT_AUTH = "CLIENT_CERT";
  public static final String __CERT_AUTH2 = "CLIENT-CERT";
  public static final String __SPNEGO_AUTH = "SPNEGO";
  public static final String __NEGOTIATE_AUTH = "NEGOTIATE";
  public static final int DC_UNSET = -1;
  public static final int DC_NONE = 0;
  public static final int DC_INTEGRAL = 1;
  public static final int DC_CONFIDENTIAL = 2;
  public static final int DC_FORBIDDEN = 3;
  public static final String NONE = "NONE";
  public static final String ANY_ROLE = "*";
  public static final String ANY_AUTH = "**";
  private String _name;
  private String[] _roles;
  
  public static boolean validateMethod(String method)
  {
    if (method == null)
      return false;
    method = method.trim();
    return (method.equals("FORM")) || 
      (method.equals("BASIC")) || 
      (method.equals("DIGEST")) || 
      (method.equals("CLIENT_CERT")) || 
      (method.equals("CLIENT-CERT")) || 
      (method.equals("SPNEGO")) || 
      (method.equals("NEGOTIATE"));
  }
  















  private int _dataConstraint = -1;
  
  private boolean _anyRole = false;
  
  private boolean _anyAuth = false;
  
  private boolean _authenticate = false;
  







  public Constraint() {}
  






  public Constraint(String name, String role)
  {
    setName(name);
    setRoles(new String[] { role });
  }
  

  public Object clone()
    throws CloneNotSupportedException
  {
    return super.clone();
  }
  




  public void setName(String name)
  {
    _name = name;
  }
  

  public String getName()
  {
    return _name;
  }
  

  public void setRoles(String[] roles)
  {
    _roles = roles;
    _anyRole = false;
    _anyAuth = false;
    int i; if (roles != null)
    {
      for (i = roles.length; i-- > 0;)
      {
        _anyRole |= "*".equals(roles[i]);
        _anyAuth |= "**".equals(roles[i]);
      }
    }
  }
  




  public boolean isAnyRole()
  {
    return _anyRole;
  }
  





  public boolean isAnyAuth()
  {
    return _anyAuth;
  }
  




  public String[] getRoles()
  {
    return _roles;
  }
  





  public boolean hasRole(String role)
  {
    if (_anyRole) return true;
    int i; if (_roles != null) for (i = _roles.length; i-- > 0;)
        if (role.equals(_roles[i])) return true;
    return false;
  }
  




  public void setAuthenticate(boolean authenticate)
  {
    _authenticate = authenticate;
  }
  




  public boolean getAuthenticate()
  {
    return _authenticate;
  }
  




  public boolean isForbidden()
  {
    return (_authenticate) && (!_anyRole) && ((_roles == null) || (_roles.length == 0));
  }
  





  public void setDataConstraint(int c)
  {
    if ((c < 0) || (c > 2)) throw new IllegalArgumentException("Constraint out of range");
    _dataConstraint = c;
  }
  





  public int getDataConstraint()
  {
    return _dataConstraint;
  }
  




  public boolean hasDataConstraint()
  {
    return _dataConstraint >= 0;
  }
  


  public String toString()
  {
    return 
    
      "SC{" + _name + "," + (_roles == null ? "-" : _anyRole ? "*" : Arrays.asList(_roles).toString()) + "," + (_dataConstraint == 1 ? "INTEGRAL}" : _dataConstraint == 0 ? "NONE}" : _dataConstraint == -1 ? "DC_UNSET}" : "CONFIDENTIAL}");
  }
}
