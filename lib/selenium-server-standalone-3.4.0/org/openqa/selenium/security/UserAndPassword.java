package org.openqa.selenium.security;

import org.openqa.selenium.Beta;



















@Beta
public class UserAndPassword
  implements Credentials
{
  private final String username;
  private final String password;
  
  public UserAndPassword(String username, String password)
  {
    this.username = username;
    this.password = password;
  }
  
  public String getUsername() {
    return username;
  }
  
  public String getPassword() {
    return password;
  }
}
