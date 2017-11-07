package org.eclipse.jetty.util.ssl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.CRL;
import java.security.cert.CertStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.PKIXBuilderParameters;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.net.ssl.CertPathTrustManagerParameters;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SNIHostName;
import javax.net.ssl.SNIMatcher;
import javax.net.ssl.SNIServerName;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSessionContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509ExtendedKeyManager;
import javax.net.ssl.X509TrustManager;
import org.eclipse.jetty.util.StringUtil;
import org.eclipse.jetty.util.component.AbstractLifeCycle;
import org.eclipse.jetty.util.component.ContainerLifeCycle;
import org.eclipse.jetty.util.component.Dumpable;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.security.CertificateUtils;
import org.eclipse.jetty.util.security.CertificateValidator;
import org.eclipse.jetty.util.security.Password;

























public class SslContextFactory
  extends AbstractLifeCycle
  implements Dumpable
{
  public static final TrustManager[] TRUST_ALL_CERTS = { new X509TrustManager()
  {
    public X509Certificate[] getAcceptedIssuers()
    {
      return new X509Certificate[0];
    }
    
    public void checkClientTrusted(X509Certificate[] certs, String authType) {}
    
    public void checkServerTrusted(X509Certificate[] certs, String authType) {}
  } };
  














  private static final Logger LOG = Log.getLogger(SslContextFactory.class);
  

  public static final String DEFAULT_KEYMANAGERFACTORY_ALGORITHM = Security.getProperty("ssl.KeyManagerFactory.algorithm") == null ? 
    KeyManagerFactory.getDefaultAlgorithm() : Security.getProperty("ssl.KeyManagerFactory.algorithm");
  

  public static final String DEFAULT_TRUSTMANAGERFACTORY_ALGORITHM = Security.getProperty("ssl.TrustManagerFactory.algorithm") == null ? 
    TrustManagerFactory.getDefaultAlgorithm() : Security.getProperty("ssl.TrustManagerFactory.algorithm");
  

  public static final String KEYPASSWORD_PROPERTY = "org.eclipse.jetty.ssl.keypassword";
  

  public static final String PASSWORD_PROPERTY = "org.eclipse.jetty.ssl.password";
  
  private final Set<String> _excludeProtocols = new LinkedHashSet();
  private final Set<String> _includeProtocols = new LinkedHashSet();
  private final Set<String> _excludeCipherSuites = new LinkedHashSet();
  private final List<String> _includeCipherSuites = new ArrayList();
  private final Map<String, X509> _aliasX509 = new HashMap();
  private final Map<String, X509> _certHosts = new HashMap();
  private final Map<String, X509> _certWilds = new HashMap();
  private String[] _selectedProtocols;
  private boolean _useCipherSuitesOrder = true;
  private Comparator<String> _cipherComparator;
  private String[] _selectedCipherSuites;
  private Resource _keyStoreResource;
  private String _keyStoreProvider;
  private String _keyStoreType = "JKS";
  private String _certAlias;
  private Resource _trustStoreResource;
  private String _trustStoreProvider;
  private String _trustStoreType = "JKS";
  private boolean _needClientAuth = false;
  private boolean _wantClientAuth = false;
  private Password _keyStorePassword;
  private Password _keyManagerPassword;
  private Password _trustStorePassword;
  private String _sslProvider;
  private String _sslProtocol = "TLS";
  private String _secureRandomAlgorithm;
  private String _keyManagerFactoryAlgorithm = DEFAULT_KEYMANAGERFACTORY_ALGORITHM;
  private String _trustManagerFactoryAlgorithm = DEFAULT_TRUSTMANAGERFACTORY_ALGORITHM;
  private boolean _validateCerts;
  private boolean _validatePeerCerts;
  private int _maxCertPathLength = -1;
  private String _crlPath;
  private boolean _enableCRLDP = false;
  private boolean _enableOCSP = false;
  private String _ocspResponderURL;
  private KeyStore _setKeyStore;
  private KeyStore _setTrustStore;
  private boolean _sessionCachingEnabled = true;
  private int _sslSessionCacheSize = -1;
  private int _sslSessionTimeout = -1;
  private SSLContext _setContext;
  private String _endpointIdentificationAlgorithm = null;
  private boolean _trustAll;
  private boolean _renegotiationAllowed = true;
  

  private Factory _factory;
  


  public SslContextFactory()
  {
    this(false);
  }
  







  public SslContextFactory(boolean trustAll)
  {
    this(trustAll, null);
  }
  





  public SslContextFactory(String keyStorePath)
  {
    this(false, keyStorePath);
  }
  
  private SslContextFactory(boolean trustAll, String keyStorePath)
  {
    setTrustAll(trustAll);
    addExcludeProtocols(new String[] { "SSL", "SSLv2", "SSLv2Hello", "SSLv3" });
    setExcludeCipherSuites(new String[] { "^.*_(MD5|SHA|SHA1)$" });
    if (keyStorePath != null) {
      setKeyStorePath(keyStorePath);
    }
  }
  


  protected void doStart()
    throws Exception
  {
    super.doStart();
    synchronized (this)
    {
      load();
    }
  }
  
  private void load() throws Exception
  {
    SSLContext context = _setContext;
    KeyStore keyStore = _setKeyStore;
    KeyStore trustStore = _setTrustStore;
    
    if (context == null)
    {
      String algorithm;
      if ((keyStore == null) && (_keyStoreResource == null) && (trustStore == null) && (_trustStoreResource == null))
      {
        TrustManager[] trust_managers = null;
        
        if (isTrustAll())
        {
          if (LOG.isDebugEnabled()) {
            LOG.debug("No keystore or trust store configured.  ACCEPTING UNTRUSTED CERTIFICATES!!!!!", new Object[0]);
          }
          trust_managers = TRUST_ALL_CERTS;
        }
        
        algorithm = getSecureRandomAlgorithm();
        SecureRandom secureRandom = algorithm == null ? null : SecureRandom.getInstance(algorithm);
        context = _sslProvider == null ? SSLContext.getInstance(_sslProtocol) : SSLContext.getInstance(_sslProtocol, _sslProvider);
        context.init(null, trust_managers, secureRandom);
      }
      else
      {
        if (keyStore == null)
          keyStore = loadKeyStore(_keyStoreResource);
        if (trustStore == null) {
          trustStore = loadTrustStore(_trustStoreResource);
        }
        Collection<? extends CRL> crls = loadCRL(getCrlPath());
        

        if (keyStore != null)
        {
          for (String alias : Collections.list(keyStore.aliases()))
          {
            Certificate certificate = keyStore.getCertificate(alias);
            if ((certificate != null) && ("X.509".equals(certificate.getType())))
            {
              X509Certificate x509C = (X509Certificate)certificate;
              

              if (X509.isCertSign(x509C))
              {
                if (LOG.isDebugEnabled()) {
                  LOG.debug("Skipping " + x509C, new Object[0]);
                }
              } else {
                x509 = new X509(alias, x509C);
                _aliasX509.put(alias, x509);
                
                if (isValidateCerts())
                {
                  validator = new CertificateValidator(trustStore, crls);
                  validator.setMaxCertPathLength(getMaxCertPathLength());
                  validator.setEnableCRLDP(isEnableCRLDP());
                  validator.setEnableOCSP(isEnableOCSP());
                  validator.setOcspResponderURL(getOcspResponderURL());
                  validator.validate(keyStore, x509C);
                }
                
                LOG.info("x509={} for {}", new Object[] { x509, this });
                
                for (String h : x509.getHosts())
                  _certHosts.put(h, x509);
                for (String w : x509.getWilds())
                  _certWilds.put(w, x509);
              }
            }
          } }
        X509 x509;
        CertificateValidator validator;
        KeyManager[] keyManagers = getKeyManagers(keyStore);
        TrustManager[] trustManagers = getTrustManagers(trustStore, crls);
        

        SecureRandom secureRandom = _secureRandomAlgorithm == null ? null : SecureRandom.getInstance(_secureRandomAlgorithm);
        context = _sslProvider == null ? SSLContext.getInstance(_sslProtocol) : SSLContext.getInstance(_sslProtocol, _sslProvider);
        context.init(keyManagers, trustManagers, secureRandom);
      }
    }
    

    SSLSessionContext serverContext = context.getServerSessionContext();
    if (serverContext != null)
    {
      if (getSslSessionCacheSize() > -1)
        serverContext.setSessionCacheSize(getSslSessionCacheSize());
      if (getSslSessionTimeout() > -1) {
        serverContext.setSessionTimeout(getSslSessionTimeout());
      }
    }
    
    SSLParameters enabled = context.getDefaultSSLParameters();
    SSLParameters supported = context.getSupportedSSLParameters();
    selectCipherSuites(enabled.getCipherSuites(), supported.getCipherSuites());
    selectProtocols(enabled.getProtocols(), supported.getProtocols());
    
    _factory = new Factory(keyStore, trustStore, context);
    if (LOG.isDebugEnabled())
    {
      LOG.debug("Selected Protocols {} of {}", new Object[] { Arrays.asList(_selectedProtocols), Arrays.asList(supported.getProtocols()) });
      LOG.debug("Selected Ciphers   {} of {}", new Object[] { Arrays.asList(_selectedCipherSuites), Arrays.asList(supported.getCipherSuites()) });
    }
  }
  

  public String dump()
  {
    return ContainerLifeCycle.dump(this);
  }
  
  public void dump(Appendable out, String indent)
    throws IOException
  {
    out.append(String.valueOf(this)).append(" trustAll=").append(Boolean.toString(_trustAll)).append(System.lineSeparator());
    




    try
    {
      SSLEngine sslEngine = SSLContext.getDefault().createSSLEngine();
      
      List<Object> selections = new ArrayList();
      

      selections.add(new SslSelectionDump("Protocol", sslEngine
        .getSupportedProtocols(), sslEngine
        .getEnabledProtocols(), 
        getExcludeProtocols(), 
        getIncludeProtocols()));
      

      selections.add(new SslSelectionDump("Cipher Suite", sslEngine
        .getSupportedCipherSuites(), sslEngine
        .getEnabledCipherSuites(), 
        getExcludeCipherSuites(), 
        getIncludeCipherSuites()));
      
      ContainerLifeCycle.dump(out, indent, new Collection[] { selections });
    }
    catch (NoSuchAlgorithmException ignore)
    {
      LOG.ignore(ignore);
    }
  }
  
  protected void doStop()
    throws Exception
  {
    synchronized (this)
    {
      unload();
    }
    super.doStop();
  }
  
  private void unload()
  {
    _factory = null;
    _selectedProtocols = null;
    _selectedCipherSuites = null;
    _aliasX509.clear();
    _certHosts.clear();
    _certWilds.clear();
  }
  
  public String[] getSelectedProtocols()
  {
    return (String[])Arrays.copyOf(_selectedProtocols, _selectedProtocols.length);
  }
  
  public String[] getSelectedCipherSuites()
  {
    return (String[])Arrays.copyOf(_selectedCipherSuites, _selectedCipherSuites.length);
  }
  
  public Comparator<String> getCipherComparator()
  {
    return _cipherComparator;
  }
  
  public void setCipherComparator(Comparator<String> cipherComparator)
  {
    if (cipherComparator != null)
      setUseCipherSuitesOrder(true);
    _cipherComparator = cipherComparator;
  }
  
  public Set<String> getAliases()
  {
    return Collections.unmodifiableSet(_aliasX509.keySet());
  }
  
  public X509 getX509(String alias)
  {
    return (X509)_aliasX509.get(alias);
  }
  




  public String[] getExcludeProtocols()
  {
    return (String[])_excludeProtocols.toArray(new String[0]);
  }
  




  public void setExcludeProtocols(String... protocols)
  {
    _excludeProtocols.clear();
    _excludeProtocols.addAll(Arrays.asList(protocols));
  }
  



  public void addExcludeProtocols(String... protocol)
  {
    _excludeProtocols.addAll(Arrays.asList(protocol));
  }
  




  public String[] getIncludeProtocols()
  {
    return (String[])_includeProtocols.toArray(new String[0]);
  }
  




  public void setIncludeProtocols(String... protocols)
  {
    _includeProtocols.clear();
    _includeProtocols.addAll(Arrays.asList(protocols));
  }
  




  public String[] getExcludeCipherSuites()
  {
    return (String[])_excludeCipherSuites.toArray(new String[0]);
  }
  






  public void setExcludeCipherSuites(String... cipherSuites)
  {
    _excludeCipherSuites.clear();
    _excludeCipherSuites.addAll(Arrays.asList(cipherSuites));
  }
  



  public void addExcludeCipherSuites(String... cipher)
  {
    _excludeCipherSuites.addAll(Arrays.asList(cipher));
  }
  




  public String[] getIncludeCipherSuites()
  {
    return (String[])_includeCipherSuites.toArray(new String[0]);
  }
  






  public void setIncludeCipherSuites(String... cipherSuites)
  {
    _includeCipherSuites.clear();
    _includeCipherSuites.addAll(Arrays.asList(cipherSuites));
  }
  
  public boolean isUseCipherSuitesOrder()
  {
    return _useCipherSuitesOrder;
  }
  
  public void setUseCipherSuitesOrder(boolean useCipherSuitesOrder)
  {
    _useCipherSuitesOrder = useCipherSuitesOrder;
  }
  



  public String getKeyStorePath()
  {
    return _keyStoreResource.toString();
  }
  



  public void setKeyStorePath(String keyStorePath)
  {
    try
    {
      _keyStoreResource = Resource.newResource(keyStorePath);
    }
    catch (Exception e)
    {
      throw new IllegalArgumentException(e);
    }
  }
  



  public String getKeyStoreProvider()
  {
    return _keyStoreProvider;
  }
  



  public void setKeyStoreProvider(String keyStoreProvider)
  {
    _keyStoreProvider = keyStoreProvider;
  }
  



  public String getKeyStoreType()
  {
    return _keyStoreType;
  }
  



  public void setKeyStoreType(String keyStoreType)
  {
    _keyStoreType = keyStoreType;
  }
  



  public String getCertAlias()
  {
    return _certAlias;
  }
  









  public void setCertAlias(String certAlias)
  {
    _certAlias = certAlias;
  }
  



  public void setTrustStorePath(String trustStorePath)
  {
    try
    {
      _trustStoreResource = Resource.newResource(trustStorePath);
    }
    catch (Exception e)
    {
      throw new IllegalArgumentException(e);
    }
  }
  



  public String getTrustStoreProvider()
  {
    return _trustStoreProvider;
  }
  



  public void setTrustStoreProvider(String trustStoreProvider)
  {
    _trustStoreProvider = trustStoreProvider;
  }
  



  public String getTrustStoreType()
  {
    return _trustStoreType;
  }
  



  public void setTrustStoreType(String trustStoreType)
  {
    _trustStoreType = trustStoreType;
  }
  




  public boolean getNeedClientAuth()
  {
    return _needClientAuth;
  }
  




  public void setNeedClientAuth(boolean needClientAuth)
  {
    _needClientAuth = needClientAuth;
  }
  




  public boolean getWantClientAuth()
  {
    return _wantClientAuth;
  }
  




  public void setWantClientAuth(boolean wantClientAuth)
  {
    _wantClientAuth = wantClientAuth;
  }
  



  public boolean isValidateCerts()
  {
    return _validateCerts;
  }
  



  public void setValidateCerts(boolean validateCerts)
  {
    _validateCerts = validateCerts;
  }
  



  public boolean isValidatePeerCerts()
  {
    return _validatePeerCerts;
  }
  



  public void setValidatePeerCerts(boolean validatePeerCerts)
  {
    _validatePeerCerts = validatePeerCerts;
  }
  







  public void setKeyStorePassword(String password)
  {
    if (password == null)
    {
      if (_keyStoreResource != null) {
        _keyStorePassword = getPassword("org.eclipse.jetty.ssl.password");
      } else {
        _keyStorePassword = null;
      }
    }
    else {
      _keyStorePassword = newPassword(password);
    }
  }
  






  public void setKeyManagerPassword(String password)
  {
    if (password == null)
    {
      if (System.getProperty("org.eclipse.jetty.ssl.keypassword") != null) {
        _keyManagerPassword = getPassword("org.eclipse.jetty.ssl.keypassword");
      } else {
        _keyManagerPassword = null;
      }
    }
    else {
      _keyManagerPassword = newPassword(password);
    }
  }
  







  public void setTrustStorePassword(String password)
  {
    if (password == null)
    {
      if ((_trustStoreResource != null) && (!_trustStoreResource.equals(_keyStoreResource))) {
        _trustStorePassword = getPassword("org.eclipse.jetty.ssl.password");
      } else {
        _trustStorePassword = null;
      }
    }
    else {
      _trustStorePassword = newPassword(password);
    }
  }
  




  public String getProvider()
  {
    return _sslProvider;
  }
  




  public void setProvider(String provider)
  {
    _sslProvider = provider;
  }
  




  public String getProtocol()
  {
    return _sslProtocol;
  }
  




  public void setProtocol(String protocol)
  {
    _sslProtocol = protocol;
  }
  





  public String getSecureRandomAlgorithm()
  {
    return _secureRandomAlgorithm;
  }
  





  public void setSecureRandomAlgorithm(String algorithm)
  {
    _secureRandomAlgorithm = algorithm;
  }
  



  public String getKeyManagerFactoryAlgorithm()
  {
    return _keyManagerFactoryAlgorithm;
  }
  



  public void setKeyManagerFactoryAlgorithm(String algorithm)
  {
    _keyManagerFactoryAlgorithm = algorithm;
  }
  



  public String getTrustManagerFactoryAlgorithm()
  {
    return _trustManagerFactoryAlgorithm;
  }
  



  public boolean isTrustAll()
  {
    return _trustAll;
  }
  



  public void setTrustAll(boolean trustAll)
  {
    _trustAll = trustAll;
    if (trustAll) {
      setEndpointIdentificationAlgorithm(null);
    }
  }
  



  public void setTrustManagerFactoryAlgorithm(String algorithm)
  {
    _trustManagerFactoryAlgorithm = algorithm;
  }
  



  public boolean isRenegotiationAllowed()
  {
    return _renegotiationAllowed;
  }
  



  public void setRenegotiationAllowed(boolean renegotiationAllowed)
  {
    _renegotiationAllowed = renegotiationAllowed;
  }
  



  public String getCrlPath()
  {
    return _crlPath;
  }
  



  public void setCrlPath(String crlPath)
  {
    _crlPath = crlPath;
  }
  




  public int getMaxCertPathLength()
  {
    return _maxCertPathLength;
  }
  




  public void setMaxCertPathLength(int maxCertPathLength)
  {
    _maxCertPathLength = maxCertPathLength;
  }
  



  public SSLContext getSslContext()
  {
    if (!isStarted()) {
      return _setContext;
    }
    synchronized (this)
    {
      return _factory._context;
    }
  }
  



  public void setSslContext(SSLContext sslContext)
  {
    _setContext = sslContext;
  }
  



  public String getEndpointIdentificationAlgorithm()
  {
    return _endpointIdentificationAlgorithm;
  }
  





  public void setEndpointIdentificationAlgorithm(String endpointIdentificationAlgorithm)
  {
    _endpointIdentificationAlgorithm = endpointIdentificationAlgorithm;
  }
  






  protected KeyStore loadKeyStore(Resource resource)
    throws Exception
  {
    String storePassword = _keyStorePassword == null ? null : _keyStorePassword.toString();
    return CertificateUtils.getKeyStore(resource, getKeyStoreType(), getKeyStoreProvider(), storePassword);
  }
  






  protected KeyStore loadTrustStore(Resource resource)
    throws Exception
  {
    String type = getTrustStoreType();
    String provider = getTrustStoreProvider();
    String passwd = _trustStorePassword == null ? null : _trustStorePassword.toString();
    if ((resource == null) || (resource.equals(_keyStoreResource)))
    {
      resource = _keyStoreResource;
      if (type == null)
        type = _keyStoreType;
      if (provider == null)
        provider = _keyStoreProvider;
      if (passwd == null)
        passwd = _keyStorePassword == null ? null : _keyStorePassword.toString();
    }
    return CertificateUtils.getKeyStore(resource, type, provider, passwd);
  }
  









  protected Collection<? extends CRL> loadCRL(String crlPath)
    throws Exception
  {
    return CertificateUtils.loadCRL(crlPath);
  }
  
  protected KeyManager[] getKeyManagers(KeyStore keyStore) throws Exception
  {
    KeyManager[] managers = null;
    
    if (keyStore != null)
    {
      KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(getKeyManagerFactoryAlgorithm());
      keyManagerFactory.init(keyStore, _keyManagerPassword == null ? _keyStorePassword.toString().toCharArray() : _keyStorePassword == null ? null : _keyManagerPassword.toString().toCharArray());
      managers = keyManagerFactory.getKeyManagers();
      
      if (managers != null)
      {
        String alias = getCertAlias();
        if (alias != null)
        {
          for (int idx = 0; idx < managers.length; idx++)
          {
            if ((managers[idx] instanceof X509ExtendedKeyManager)) {
              managers[idx] = new AliasedX509ExtendedKeyManager((X509ExtendedKeyManager)managers[idx], alias);
            }
          }
        }
        if ((!_certHosts.isEmpty()) || (!_certWilds.isEmpty()))
        {
          for (int idx = 0; idx < managers.length; idx++)
          {
            if ((managers[idx] instanceof X509ExtendedKeyManager)) {
              managers[idx] = new SniX509ExtendedKeyManager((X509ExtendedKeyManager)managers[idx]);
            }
          }
        }
      }
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("managers={} for {}", new Object[] { managers, this });
    }
    return managers;
  }
  
  protected TrustManager[] getTrustManagers(KeyStore trustStore, Collection<? extends CRL> crls) throws Exception
  {
    TrustManager[] managers = null;
    if (trustStore != null)
    {

      if ((isValidatePeerCerts()) && ("PKIX".equalsIgnoreCase(getTrustManagerFactoryAlgorithm())))
      {
        PKIXBuilderParameters pbParams = new PKIXBuilderParameters(trustStore, new X509CertSelector());
        

        pbParams.setMaxPathLength(_maxCertPathLength);
        

        pbParams.setRevocationEnabled(true);
        
        if ((crls != null) && (!crls.isEmpty()))
        {
          pbParams.addCertStore(CertStore.getInstance("Collection", new CollectionCertStoreParameters(crls)));
        }
        
        if (_enableCRLDP)
        {

          System.setProperty("com.sun.security.enableCRLDP", "true");
        }
        
        if (_enableOCSP)
        {

          Security.setProperty("ocsp.enable", "true");
          
          if (_ocspResponderURL != null)
          {

            Security.setProperty("ocsp.responderURL", _ocspResponderURL);
          }
        }
        
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(_trustManagerFactoryAlgorithm);
        trustManagerFactory.init(new CertPathTrustManagerParameters(pbParams));
        
        managers = trustManagerFactory.getTrustManagers();
      }
      else
      {
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(_trustManagerFactoryAlgorithm);
        trustManagerFactory.init(trustStore);
        
        managers = trustManagerFactory.getTrustManagers();
      }
    }
    
    return managers;
  }
  








  public void selectProtocols(String[] enabledProtocols, String[] supportedProtocols)
  {
    Set<String> selected_protocols = new LinkedHashSet();
    

    if (!_includeProtocols.isEmpty())
    {

      for (String protocol : _includeProtocols)
      {
        if (Arrays.asList(supportedProtocols).contains(protocol)) {
          selected_protocols.add(protocol);
        } else {
          LOG.info("Protocol {} not supported in {}", new Object[] { protocol, Arrays.asList(supportedProtocols) });
        }
      }
    } else {
      selected_protocols.addAll(Arrays.asList(enabledProtocols));
    }
    
    selected_protocols.removeAll(_excludeProtocols);
    
    if (selected_protocols.isEmpty()) {
      LOG.warn("No selected protocols from {}", new Object[] { Arrays.asList(supportedProtocols) });
    }
    _selectedProtocols = ((String[])selected_protocols.toArray(new String[0]));
  }
  








  protected void selectCipherSuites(String[] enabledCipherSuites, String[] supportedCipherSuites)
  {
    List<String> selected_ciphers = new ArrayList();
    

    if (_includeCipherSuites.isEmpty()) {
      selected_ciphers.addAll(Arrays.asList(enabledCipherSuites));
    } else {
      processIncludeCipherSuites(supportedCipherSuites, selected_ciphers);
    }
    removeExcludedCipherSuites(selected_ciphers);
    
    if (selected_ciphers.isEmpty()) {
      LOG.warn("No supported ciphers from {}", new Object[] { Arrays.asList(supportedCipherSuites) });
    }
    Comparator<String> comparator = getCipherComparator();
    if (comparator != null)
    {
      if (LOG.isDebugEnabled())
        LOG.debug("Sorting selected ciphers with {}", new Object[] { comparator });
      Collections.sort(selected_ciphers, comparator);
    }
    
    _selectedCipherSuites = ((String[])selected_ciphers.toArray(new String[0]));
  }
  
  protected void processIncludeCipherSuites(String[] supportedCipherSuites, List<String> selected_ciphers)
  {
    for (String cipherSuite : _includeCipherSuites)
    {
      Pattern p = Pattern.compile(cipherSuite);
      boolean added = false;
      for (String supportedCipherSuite : supportedCipherSuites)
      {
        Matcher m = p.matcher(supportedCipherSuite);
        if (m.matches())
        {
          added = true;
          selected_ciphers.add(supportedCipherSuite);
        }
      }
      
      if (!added) {
        LOG.info("No Cipher matching '{}' is supported", new Object[] { cipherSuite });
      }
    }
  }
  
  protected void removeExcludedCipherSuites(List<String> selected_ciphers) {
    for (String excludeCipherSuite : _excludeCipherSuites)
    {
      excludeCipherPattern = Pattern.compile(excludeCipherSuite);
      for (i = selected_ciphers.iterator(); i.hasNext();)
      {
        String selectedCipherSuite = (String)i.next();
        Matcher m = excludeCipherPattern.matcher(selectedCipherSuite);
        if (m.matches()) {
          i.remove();
        }
      }
    }
    Pattern excludeCipherPattern;
    Iterator<String> i;
  }
  
  private void checkIsStarted()
  {
    if (!isStarted()) {
      throw new IllegalStateException("!STARTED: " + this);
    }
  }
  


  public boolean isEnableCRLDP()
  {
    return _enableCRLDP;
  }
  





  public void setEnableCRLDP(boolean enableCRLDP)
  {
    _enableCRLDP = enableCRLDP;
  }
  



  public boolean isEnableOCSP()
  {
    return _enableOCSP;
  }
  





  public void setEnableOCSP(boolean enableOCSP)
  {
    _enableOCSP = enableOCSP;
  }
  



  public String getOcspResponderURL()
  {
    return _ocspResponderURL;
  }
  





  public void setOcspResponderURL(String ocspResponderURL)
  {
    _ocspResponderURL = ocspResponderURL;
  }
  





  public void setKeyStore(KeyStore keyStore)
  {
    _setKeyStore = keyStore;
  }
  
  public KeyStore getKeyStore()
  {
    if (!isStarted()) {
      return _setKeyStore;
    }
    synchronized (this)
    {
      return _factory._keyStore;
    }
  }
  





  public void setTrustStore(KeyStore trustStore)
  {
    _setTrustStore = trustStore;
  }
  
  public KeyStore getTrustStore()
  {
    if (!isStarted()) {
      return _setTrustStore;
    }
    synchronized (this)
    {
      return _factory._trustStore;
    }
  }
  





  public void setKeyStoreResource(Resource resource)
  {
    _keyStoreResource = resource;
  }
  
  public Resource getKeyStoreResource()
  {
    return _keyStoreResource;
  }
  





  public void setTrustStoreResource(Resource resource)
  {
    _trustStoreResource = resource;
  }
  
  public Resource getTrustStoreResource()
  {
    return _trustStoreResource;
  }
  



  public boolean isSessionCachingEnabled()
  {
    return _sessionCachingEnabled;
  }
  










  public void setSessionCachingEnabled(boolean enableSessionCaching)
  {
    _sessionCachingEnabled = enableSessionCaching;
  }
  






  public int getSslSessionCacheSize()
  {
    return _sslSessionCacheSize;
  }
  








  public void setSslSessionCacheSize(int sslSessionCacheSize)
  {
    _sslSessionCacheSize = sslSessionCacheSize;
  }
  





  public int getSslSessionTimeout()
  {
    return _sslSessionTimeout;
  }
  








  public void setSslSessionTimeout(int sslSessionTimeout)
  {
    _sslSessionTimeout = sslSessionTimeout;
  }
  






  protected Password getPassword(String realm)
  {
    return Password.getPassword(realm, null, null);
  }
  






  public Password newPassword(String password)
  {
    return new Password(password);
  }
  
  public SSLServerSocket newSslServerSocket(String host, int port, int backlog) throws IOException
  {
    checkIsStarted();
    
    SSLContext context = getSslContext();
    SSLServerSocketFactory factory = context.getServerSocketFactory();
    


    SSLServerSocket socket = (SSLServerSocket)(host == null ? factory.createServerSocket(port, backlog) : factory.createServerSocket(port, backlog, InetAddress.getByName(host)));
    socket.setSSLParameters(customize(socket.getSSLParameters()));
    
    return socket;
  }
  
  public SSLSocket newSslSocket() throws IOException
  {
    checkIsStarted();
    
    SSLContext context = getSslContext();
    SSLSocketFactory factory = context.getSocketFactory();
    SSLSocket socket = (SSLSocket)factory.createSocket();
    socket.setSSLParameters(customize(socket.getSSLParameters()));
    
    return socket;
  }
  









  public SSLEngine newSSLEngine()
  {
    checkIsStarted();
    
    SSLContext context = getSslContext();
    SSLEngine sslEngine = context.createSSLEngine();
    customize(sslEngine);
    
    return sslEngine;
  }
  








  public SSLEngine newSSLEngine(String host, int port)
  {
    checkIsStarted();
    
    SSLContext context = getSslContext();
    

    SSLEngine sslEngine = isSessionCachingEnabled() ? context.createSSLEngine(host, port) : context.createSSLEngine();
    customize(sslEngine);
    
    return sslEngine;
  }
  


















  public SSLEngine newSSLEngine(InetSocketAddress address)
  {
    if (address == null) {
      return newSSLEngine();
    }
    boolean useHostName = getNeedClientAuth();
    String hostName = useHostName ? address.getHostName() : address.getAddress().getHostAddress();
    return newSSLEngine(hostName, address.getPort());
  }
  






  public void customize(SSLEngine sslEngine)
  {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Customize {}", new Object[] { sslEngine });
    }
    sslEngine.setSSLParameters(customize(sslEngine.getSSLParameters()));
  }
  






  public SSLParameters customize(SSLParameters sslParams)
  {
    sslParams.setEndpointIdentificationAlgorithm(getEndpointIdentificationAlgorithm());
    sslParams.setUseCipherSuitesOrder(isUseCipherSuitesOrder());
    if ((!_certHosts.isEmpty()) || (!_certWilds.isEmpty()))
      sslParams.setSNIMatchers(Collections.singletonList(new AliasSNIMatcher()));
    if (_selectedCipherSuites != null)
      sslParams.setCipherSuites(_selectedCipherSuites);
    if (_selectedProtocols != null)
      sslParams.setProtocols(_selectedProtocols);
    if (getWantClientAuth())
      sslParams.setWantClientAuth(true);
    if (getNeedClientAuth())
      sslParams.setNeedClientAuth(true);
    return sslParams;
  }
  
  public void reload(Consumer<SslContextFactory> consumer) throws Exception
  {
    synchronized (this)
    {
      consumer.accept(this);
      unload();
      load();
    }
  }
  
  public static X509Certificate[] getCertChain(SSLSession sslSession)
  {
    try
    {
      Certificate[] javaxCerts = sslSession.getPeerCertificates();
      if ((javaxCerts == null) || (javaxCerts.length == 0)) {
        return null;
      }
      int length = javaxCerts.length;
      X509Certificate[] javaCerts = new X509Certificate[length];
      
      CertificateFactory cf = CertificateFactory.getInstance("X.509");
      for (int i = 0; i < length; i++)
      {
        byte[] bytes = javaxCerts[i].getEncoded();
        ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
        javaCerts[i] = ((X509Certificate)cf.generateCertificate(stream));
      }
      
      return javaCerts;
    }
    catch (SSLPeerUnverifiedException pue)
    {
      return null;
    }
    catch (Exception e)
    {
      LOG.warn("EXCEPTION ", e); }
    return null;
  }
  


























  public static int deduceKeyLength(String cipherSuite)
  {
    if (cipherSuite == null)
      return 0;
    if (cipherSuite.contains("WITH_AES_256_"))
      return 256;
    if (cipherSuite.contains("WITH_RC4_128_"))
      return 128;
    if (cipherSuite.contains("WITH_AES_128_"))
      return 128;
    if (cipherSuite.contains("WITH_RC4_40_"))
      return 40;
    if (cipherSuite.contains("WITH_3DES_EDE_CBC_"))
      return 168;
    if (cipherSuite.contains("WITH_IDEA_CBC_"))
      return 128;
    if (cipherSuite.contains("WITH_RC2_CBC_40_"))
      return 40;
    if (cipherSuite.contains("WITH_DES40_CBC_"))
      return 40;
    if (cipherSuite.contains("WITH_DES_CBC_")) {
      return 56;
    }
    return 0;
  }
  

  public String toString()
  {
    return String.format("%s@%x(%s,%s)", new Object[] {
      getClass().getSimpleName(), 
      Integer.valueOf(hashCode()), _keyStoreResource, _trustStoreResource });
  }
  

  class Factory
  {
    private final KeyStore _keyStore;
    
    private final KeyStore _trustStore;
    
    private final SSLContext _context;
    
    Factory(KeyStore keyStore, KeyStore trustStore, SSLContext context)
    {
      _keyStore = keyStore;
      _trustStore = trustStore;
      _context = context;
    }
  }
  
  class AliasSNIMatcher extends SNIMatcher
  {
    private String _host;
    private X509 _x509;
    
    AliasSNIMatcher()
    {
      super();
    }
    

    public boolean matches(SNIServerName serverName)
    {
      if (SslContextFactory.LOG.isDebugEnabled()) {
        SslContextFactory.LOG.debug("SNI matching for {}", new Object[] { serverName });
      }
      if ((serverName instanceof SNIHostName))
      {
        String host = this._host = ((SNIHostName)serverName).getAsciiName();
        host = StringUtil.asciiToLowerCase(host);
        

        _x509 = ((X509)_certHosts.get(host));
        

        if (_x509 == null)
        {
          _x509 = ((X509)_certWilds.get(host));
          

          if (_x509 == null)
          {
            int dot = host.indexOf('.');
            if (dot >= 0)
            {
              String domain = host.substring(dot + 1);
              _x509 = ((X509)_certWilds.get(domain));
            }
          }
        }
        
        if (SslContextFactory.LOG.isDebugEnabled()) {
          SslContextFactory.LOG.debug("SNI matched {}->{}", new Object[] { host, _x509 });
        }
        
      }
      else if (SslContextFactory.LOG.isDebugEnabled()) {
        SslContextFactory.LOG.debug("SNI no match for {}", new Object[] { serverName });
      }
      



      return true;
    }
    
    public String getHost()
    {
      return _host;
    }
    
    public X509 getX509()
    {
      return _x509;
    }
  }
}
