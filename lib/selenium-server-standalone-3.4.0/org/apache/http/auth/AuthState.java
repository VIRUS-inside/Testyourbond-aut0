package org.apache.http.auth;

import java.util.Queue;
import org.apache.http.util.Args;










































public class AuthState
{
  private AuthProtocolState state;
  private AuthScheme authScheme;
  private AuthScope authScope;
  private Credentials credentials;
  private Queue<AuthOption> authOptions;
  
  public AuthState()
  {
    state = AuthProtocolState.UNCHALLENGED;
  }
  




  public void reset()
  {
    state = AuthProtocolState.UNCHALLENGED;
    authOptions = null;
    authScheme = null;
    authScope = null;
    credentials = null;
  }
  


  public AuthProtocolState getState()
  {
    return state;
  }
  


  public void setState(AuthProtocolState state)
  {
    this.state = (state != null ? state : AuthProtocolState.UNCHALLENGED);
  }
  


  public AuthScheme getAuthScheme()
  {
    return authScheme;
  }
  


  public Credentials getCredentials()
  {
    return credentials;
  }
  







  public void update(AuthScheme authScheme, Credentials credentials)
  {
    Args.notNull(authScheme, "Auth scheme");
    Args.notNull(credentials, "Credentials");
    this.authScheme = authScheme;
    this.credentials = credentials;
    authOptions = null;
  }
  




  public Queue<AuthOption> getAuthOptions()
  {
    return authOptions;
  }
  





  public boolean hasAuthOptions()
  {
    return (authOptions != null) && (!authOptions.isEmpty());
  }
  






  public void update(Queue<AuthOption> authOptions)
  {
    Args.notEmpty(authOptions, "Queue of auth options");
    this.authOptions = authOptions;
    authScheme = null;
    credentials = null;
  }
  




  @Deprecated
  public void invalidate()
  {
    reset();
  }
  


  @Deprecated
  public boolean isValid()
  {
    return authScheme != null;
  }
  






  @Deprecated
  public void setAuthScheme(AuthScheme authScheme)
  {
    if (authScheme == null) {
      reset();
      return;
    }
    this.authScheme = authScheme;
  }
  






  @Deprecated
  public void setCredentials(Credentials credentials)
  {
    this.credentials = credentials;
  }
  






  @Deprecated
  public AuthScope getAuthScope()
  {
    return authScope;
  }
  






  @Deprecated
  public void setAuthScope(AuthScope authScope)
  {
    this.authScope = authScope;
  }
  
  public String toString()
  {
    StringBuilder buffer = new StringBuilder();
    buffer.append("state:").append(state).append(";");
    if (authScheme != null) {
      buffer.append("auth scheme:").append(authScheme.getSchemeName()).append(";");
    }
    if (credentials != null) {
      buffer.append("credentials present");
    }
    return buffer.toString();
  }
}
