package org.seleniumhq.jetty9.util.ssl;

import java.net.Socket;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import javax.net.ssl.SNIMatcher;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.X509ExtendedKeyManager;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;























public class SniX509ExtendedKeyManager
  extends X509ExtendedKeyManager
{
  public static final String SNI_X509 = "org.seleniumhq.jetty9.util.ssl.snix509";
  private static final String NO_MATCHERS = "no_matchers";
  private static final Logger LOG = Log.getLogger(SniX509ExtendedKeyManager.class);
  
  private final X509ExtendedKeyManager _delegate;
  
  public SniX509ExtendedKeyManager(X509ExtendedKeyManager keyManager)
  {
    _delegate = keyManager;
  }
  

  public String chooseClientAlias(String[] keyType, Principal[] issuers, Socket socket)
  {
    return _delegate.chooseClientAlias(keyType, issuers, socket);
  }
  

  public String chooseEngineClientAlias(String[] keyType, Principal[] issuers, SSLEngine engine)
  {
    return _delegate.chooseEngineClientAlias(keyType, issuers, engine);
  }
  

  protected String chooseServerAlias(String keyType, Principal[] issuers, Collection<SNIMatcher> matchers, SSLSession session)
  {
    String[] aliases = _delegate.getServerAliases(keyType, issuers);
    if ((aliases == null) || (aliases.length == 0)) {
      return null;
    }
    
    String host = null;
    X509 x509 = null;
    Object localObject; if (matchers != null)
    {
      for (localObject = matchers.iterator(); ((Iterator)localObject).hasNext();) { m = (SNIMatcher)((Iterator)localObject).next();
        
        if ((m instanceof SslContextFactory.AliasSNIMatcher))
        {
          matcher = (SslContextFactory.AliasSNIMatcher)m;
          host = matcher.getHost();
          x509 = matcher.getX509();
          break;
        }
      } }
    SNIMatcher m;
    SslContextFactory.AliasSNIMatcher matcher;
    if (LOG.isDebugEnabled()) {
      LOG.debug("Matched {} with {} from {}", new Object[] { host, x509, Arrays.asList(aliases) });
    }
    
    if (x509 != null)
    {
      localObject = aliases;m = localObject.length; for (matcher = 0; matcher < m; matcher++) { String a = localObject[matcher];
        
        if (a.equals(x509.getAlias()))
        {
          session.putValue("org.seleniumhq.jetty9.util.ssl.snix509", x509);
          return a;
        }
      }
      return null;
    }
    return "no_matchers";
  }
  

  public String chooseServerAlias(String keyType, Principal[] issuers, Socket socket)
  {
    SSLSocket sslSocket = (SSLSocket)socket;
    
    String alias = chooseServerAlias(keyType, issuers, sslSocket.getSSLParameters().getSNIMatchers(), sslSocket.getHandshakeSession());
    if (alias == "no_matchers")
      alias = _delegate.chooseServerAlias(keyType, issuers, socket);
    if (LOG.isDebugEnabled())
      LOG.debug("Chose alias {}/{} on {}", new Object[] { alias, keyType, socket });
    return alias;
  }
  

  public String chooseEngineServerAlias(String keyType, Principal[] issuers, SSLEngine engine)
  {
    String alias = chooseServerAlias(keyType, issuers, engine.getSSLParameters().getSNIMatchers(), engine.getHandshakeSession());
    if (alias == "no_matchers")
      alias = _delegate.chooseEngineServerAlias(keyType, issuers, engine);
    if (LOG.isDebugEnabled())
      LOG.debug("Chose alias {}/{} on {}", new Object[] { alias, keyType, engine });
    return alias;
  }
  

  public X509Certificate[] getCertificateChain(String alias)
  {
    return _delegate.getCertificateChain(alias);
  }
  

  public String[] getClientAliases(String keyType, Principal[] issuers)
  {
    return _delegate.getClientAliases(keyType, issuers);
  }
  

  public PrivateKey getPrivateKey(String alias)
  {
    return _delegate.getPrivateKey(alias);
  }
  

  public String[] getServerAliases(String keyType, Principal[] issuers)
  {
    return _delegate.getServerAliases(keyType, issuers);
  }
}
