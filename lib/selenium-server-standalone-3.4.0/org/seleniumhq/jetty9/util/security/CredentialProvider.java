package org.seleniumhq.jetty9.util.security;

public abstract interface CredentialProvider
{
  public abstract Credential getCredential(String paramString);
  
  public abstract String getPrefix();
}
