package org.seleniumhq.jetty9.server;

import javax.servlet.ServletRequest;

public abstract interface Authentication {
  public static abstract interface SendSuccess extends Authentication.ResponseSent
  {}
  
  public static abstract interface Failure extends Authentication.ResponseSent
  {}
  
  public static abstract interface Challenge extends Authentication.ResponseSent
  {}
  
  public static abstract interface ResponseSent extends Authentication
  {}
  
  public static abstract interface Deferred extends Authentication {
    public abstract Authentication authenticate(ServletRequest paramServletRequest);
    
    public abstract Authentication authenticate(ServletRequest paramServletRequest, javax.servlet.ServletResponse paramServletResponse);
    
    public abstract Authentication login(String paramString, Object paramObject, ServletRequest paramServletRequest);
  }
  
  public static abstract interface Wrapped extends Authentication {
    public abstract javax.servlet.http.HttpServletRequest getHttpServletRequest();
    
    public abstract javax.servlet.http.HttpServletResponse getHttpServletResponse();
  }
  
  public static abstract interface User extends Authentication { public abstract String getAuthMethod();
    
    public abstract UserIdentity getUserIdentity();
    
    public abstract boolean isUserInRole(UserIdentity.Scope paramScope, String paramString);
    
    public abstract void logout();
  }
  
  public static class Failed extends QuietServletException { public Failed(String message) { super(); }
  }
  





























































































  public static final Authentication UNAUTHENTICATED = new Authentication() {
    public String toString() { return "UNAUTHENTICATED"; }
  };
  





  public static final Authentication NOT_CHECKED = new Authentication() {
    public String toString() { return "NOT CHECKED"; }
  };
  




  public static final Authentication SEND_CONTINUE = new Challenge() {
    public String toString() { return "CHALLENGE"; }
  };
  




  public static final Authentication SEND_FAILURE = new Failure() {
    public String toString() { return "FAILURE"; } };
  public static final Authentication SEND_SUCCESS = new SendSuccess() {
    public String toString() { return "SEND_SUCCESS"; }
  };
}
