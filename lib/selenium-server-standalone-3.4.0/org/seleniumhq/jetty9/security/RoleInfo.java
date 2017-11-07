package org.seleniumhq.jetty9.security;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
































public class RoleInfo
{
  private boolean _isAnyAuth;
  private boolean _isAnyRole;
  private boolean _checked;
  private boolean _forbidden;
  private UserDataConstraint _userDataConstraint;
  private final Set<String> _roles = new CopyOnWriteArraySet();
  

  public RoleInfo() {}
  

  public boolean isChecked()
  {
    return _checked;
  }
  
  public void setChecked(boolean checked)
  {
    _checked = checked;
    if (!checked)
    {
      _forbidden = false;
      _roles.clear();
      _isAnyRole = false;
      _isAnyAuth = false;
    }
  }
  
  public boolean isForbidden()
  {
    return _forbidden;
  }
  
  public void setForbidden(boolean forbidden)
  {
    _forbidden = forbidden;
    if (forbidden)
    {
      _checked = true;
      _userDataConstraint = null;
      _isAnyRole = false;
      _isAnyAuth = false;
      _roles.clear();
    }
  }
  
  public boolean isAnyRole()
  {
    return _isAnyRole;
  }
  
  public void setAnyRole(boolean anyRole)
  {
    _isAnyRole = anyRole;
    if (anyRole) {
      _checked = true;
    }
  }
  
  public boolean isAnyAuth() {
    return _isAnyAuth;
  }
  
  public void setAnyAuth(boolean anyAuth)
  {
    _isAnyAuth = anyAuth;
    if (anyAuth) {
      _checked = true;
    }
  }
  
  public UserDataConstraint getUserDataConstraint() {
    return _userDataConstraint;
  }
  
  public void setUserDataConstraint(UserDataConstraint userDataConstraint)
  {
    if (userDataConstraint == null) throw new NullPointerException("Null UserDataConstraint");
    if (_userDataConstraint == null)
    {

      _userDataConstraint = userDataConstraint;
    }
    else
    {
      _userDataConstraint = _userDataConstraint.combine(userDataConstraint);
    }
  }
  
  public Set<String> getRoles()
  {
    return _roles;
  }
  
  public void addRole(String role)
  {
    _roles.add(role);
  }
  
  public void combine(RoleInfo other)
  {
    if (_forbidden) {
      setForbidden(true);
    } else if (!_checked) {
      setChecked(true);
    } else if (_isAnyRole) {
      setAnyRole(true);
    } else if (_isAnyAuth) {
      setAnyAuth(true);
    } else if (!_isAnyRole)
    {
      for (String r : _roles) {
        _roles.add(r);
      }
    }
    setUserDataConstraint(_userDataConstraint);
  }
  

  public String toString()
  {
    return "{RoleInfo" + (_forbidden ? ",F" : "") + (_checked ? ",C" : "") + (_isAnyRole ? ",*" : _roles) + (_userDataConstraint != null ? "," + _userDataConstraint : "") + "}";
  }
}
