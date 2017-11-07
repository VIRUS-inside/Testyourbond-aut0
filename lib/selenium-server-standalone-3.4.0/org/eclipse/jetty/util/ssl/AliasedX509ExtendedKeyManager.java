package org.eclipse.jetty.util.ssl;

import java.net.Socket;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.X509ExtendedKeyManager;
























public class AliasedX509ExtendedKeyManager
  extends X509ExtendedKeyManager
{
  private final String _alias;
  private final X509ExtendedKeyManager _delegate;
  
  public AliasedX509ExtendedKeyManager(X509ExtendedKeyManager keyManager, String keyAlias)
  {
    _alias = keyAlias;
    _delegate = keyManager;
  }
  
  public X509ExtendedKeyManager getDelegate()
  {
    return _delegate;
  }
  

  public String chooseClientAlias(String[] keyType, Principal[] issuers, Socket socket)
  {
    if (_alias == null) {
      return _delegate.chooseClientAlias(keyType, issuers, socket);
    }
    for (String kt : keyType)
    {
      String[] aliases = _delegate.getClientAliases(kt, issuers);
      if (aliases != null)
      {
        for (String a : aliases) {
          if (_alias.equals(a))
            return _alias;
        }
      }
    }
    return null;
  }
  

  public String chooseServerAlias(String keyType, Principal[] issuers, Socket socket)
  {
    if (_alias == null) {
      return _delegate.chooseServerAlias(keyType, issuers, socket);
    }
    String[] aliases = _delegate.getServerAliases(keyType, issuers);
    if (aliases != null)
    {
      for (String a : aliases) {
        if (_alias.equals(a))
          return _alias;
      }
    }
    return null;
  }
  

  public String[] getClientAliases(String keyType, Principal[] issuers)
  {
    return _delegate.getClientAliases(keyType, issuers);
  }
  

  public String[] getServerAliases(String keyType, Principal[] issuers)
  {
    return _delegate.getServerAliases(keyType, issuers);
  }
  

  public X509Certificate[] getCertificateChain(String alias)
  {
    return _delegate.getCertificateChain(alias);
  }
  

  public PrivateKey getPrivateKey(String alias)
  {
    return _delegate.getPrivateKey(alias);
  }
  

  public String chooseEngineServerAlias(String keyType, Principal[] issuers, SSLEngine engine)
  {
    if (_alias == null) {
      return _delegate.chooseEngineServerAlias(keyType, issuers, engine);
    }
    String[] aliases = _delegate.getServerAliases(keyType, issuers);
    if (aliases != null)
    {
      for (String a : aliases) {
        if (_alias.equals(a))
          return _alias;
      }
    }
    return null;
  }
  

  public String chooseEngineClientAlias(String[] keyType, Principal[] issuers, SSLEngine engine)
  {
    if (_alias == null) {
      return _delegate.chooseEngineClientAlias(keyType, issuers, engine);
    }
    for (String kt : keyType)
    {
      String[] aliases = _delegate.getClientAliases(kt, issuers);
      if (aliases != null)
      {
        for (String a : aliases) {
          if (_alias.equals(a))
            return _alias;
        }
      }
    }
    return null;
  }
}
