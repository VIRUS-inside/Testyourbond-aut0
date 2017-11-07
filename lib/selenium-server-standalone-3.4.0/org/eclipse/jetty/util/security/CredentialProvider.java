package org.eclipse.jetty.util.security;

public abstract interface CredentialProvider
{
  public abstract Credential getCredential(String paramString);
  
  public abstract String getPrefix();
}
