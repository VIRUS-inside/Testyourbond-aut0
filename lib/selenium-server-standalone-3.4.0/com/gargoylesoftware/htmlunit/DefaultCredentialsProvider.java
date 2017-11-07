package com.gargoylesoftware.htmlunit;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.NTCredentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;


























public class DefaultCredentialsProvider
  implements CredentialsProvider, Serializable
{
  private final Map<AuthScopeProxy, CredentialsFactory> credentialsMap_ = new HashMap();
  



  public DefaultCredentialsProvider() {}
  



  public void addCredentials(String username, String password)
  {
    addCredentials(username, password, AuthScope.ANY_HOST, -1, AuthScope.ANY_REALM);
  }
  










  public void addCredentials(String username, String password, String host, int port, String realm)
  {
    AuthScope authscope = new AuthScope(host, port, realm, AuthScope.ANY_SCHEME);
    Credentials credentials = new UsernamePasswordCredentials(username, password);
    setCredentials(authscope, credentials);
  }
  











  public void addNTLMCredentials(String username, String password, String host, int port, String workstation, String domain)
  {
    AuthScope authscope = new AuthScope(host, port, AuthScope.ANY_REALM, AuthScope.ANY_SCHEME);
    Credentials credentials = new NTCredentials(username, password, workstation, domain);
    setCredentials(authscope, credentials);
  }
  



  public synchronized void setCredentials(AuthScope authscope, Credentials credentials)
  {
    if (authscope == null) {
      throw new IllegalArgumentException("Authentication scope may not be null");
    }
    CredentialsFactory factory;
    if ((credentials instanceof UsernamePasswordCredentials)) {
      UsernamePasswordCredentials userCredentials = (UsernamePasswordCredentials)credentials;
      factory = new UsernamePasswordCredentialsFactory(userCredentials.getUserName(), 
        userCredentials.getPassword());
    } else { CredentialsFactory factory;
      if ((credentials instanceof NTCredentials)) {
        NTCredentials ntCredentials = (NTCredentials)credentials;
        factory = new NTCredentialsFactory(ntCredentials.getUserName(), ntCredentials.getPassword(), 
          ntCredentials.getWorkstation(), ntCredentials.getDomain());
      }
      else {
        throw new IllegalArgumentException("Unsupported Credential type: " + credentials.getClass().getName()); } }
    CredentialsFactory factory;
    credentialsMap_.put(new AuthScopeProxy(authscope), factory);
  }
  







  private static Credentials matchCredentials(Map<AuthScopeProxy, CredentialsFactory> map, AuthScope authscope)
  {
    CredentialsFactory factory = (CredentialsFactory)map.get(new AuthScopeProxy(authscope));
    Credentials creds = null;
    if (factory == null) {
      int bestMatchFactor = -1;
      AuthScope bestMatch = null;
      for (AuthScopeProxy proxy : map.keySet()) {
        AuthScope current = proxy.getAuthScope();
        int factor = authscope.match(current);
        if (factor > bestMatchFactor) {
          bestMatchFactor = factor;
          bestMatch = current;
        }
      }
      if (bestMatch != null) {
        creds = ((CredentialsFactory)map.get(new AuthScopeProxy(bestMatch))).getInstance();
      }
    }
    else {
      creds = factory.getInstance();
    }
    return creds;
  }
  



  public synchronized Credentials getCredentials(AuthScope authscope)
  {
    if (authscope == null) {
      throw new IllegalArgumentException("Authentication scope may not be null");
    }
    return matchCredentials(credentialsMap_, authscope);
  }
  




  public synchronized boolean removeCredentials(AuthScope authscope)
  {
    if (authscope == null) {
      throw new IllegalArgumentException("Authentication scope may not be null");
    }
    int bestMatchFactor = -1;
    AuthScopeProxy bestMatch = null;
    for (AuthScopeProxy proxy : credentialsMap_.keySet()) {
      AuthScope current = proxy.getAuthScope();
      int factor = authscope.match(current);
      if (factor > bestMatchFactor) {
        bestMatchFactor = factor;
        bestMatch = proxy;
      }
    }
    return credentialsMap_.remove(bestMatch) != null;
  }
  



  public String toString()
  {
    return credentialsMap_.toString();
  }
  



  public synchronized void clear()
  {
    credentialsMap_.clear();
  }
  
  private static class AuthScopeProxy
    implements Serializable
  {
    private AuthScope authScope_;
    
    AuthScopeProxy(AuthScope authScope)
    {
      authScope_ = authScope;
    }
    
    public AuthScope getAuthScope() { return authScope_; }
    
    private void writeObject(ObjectOutputStream stream) throws IOException {
      stream.writeObject(authScope_.getHost());
      stream.writeInt(authScope_.getPort());
      stream.writeObject(authScope_.getRealm());
      stream.writeObject(authScope_.getScheme());
    }
    
    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException { String host = (String)stream.readObject();
      int port = stream.readInt();
      String realm = (String)stream.readObject();
      String scheme = (String)stream.readObject();
      authScope_ = new AuthScope(host, port, realm, scheme);
    }
    
    public int hashCode() {
      return authScope_.hashCode();
    }
    
    public boolean equals(Object obj) {
      return ((obj instanceof AuthScopeProxy)) && (authScope_.equals(((AuthScopeProxy)obj).getAuthScope()));
    }
  }
  
  private static class UsernamePasswordCredentialsFactory
    implements DefaultCredentialsProvider.CredentialsFactory, Serializable
  {
    private String username_;
    private String password_;
    
    UsernamePasswordCredentialsFactory(String username, String password)
    {
      username_ = username;
      password_ = password;
    }
    
    public Credentials getInstance()
    {
      return new UsernamePasswordCredentials(username_, password_);
    }
    
    public String toString()
    {
      return getInstance().toString();
    }
  }
  

  private static class NTCredentialsFactory
    implements DefaultCredentialsProvider.CredentialsFactory, Serializable
  {
    private String username_;
    private String password_;
    private String workstation_;
    private String domain_;
    
    NTCredentialsFactory(String username, String password, String workstation, String domain)
    {
      username_ = username;
      password_ = password;
      workstation_ = workstation;
      domain_ = domain;
    }
    
    public Credentials getInstance()
    {
      return new NTCredentials(username_, password_, workstation_, domain_);
    }
    
    public String toString()
    {
      return getInstance().toString();
    }
  }
  
  private static abstract interface CredentialsFactory
  {
    public abstract Credentials getInstance();
  }
}
