package org.seleniumhq.jetty9.security.authentication;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.Principal;
import java.security.cert.CRL;
import java.security.cert.X509Certificate;
import java.util.Collection;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.seleniumhq.jetty9.security.ServerAuthException;
import org.seleniumhq.jetty9.security.UserAuthentication;
import org.seleniumhq.jetty9.server.Authentication;
import org.seleniumhq.jetty9.server.Authentication.User;
import org.seleniumhq.jetty9.server.UserIdentity;
import org.seleniumhq.jetty9.util.B64Code;
import org.seleniumhq.jetty9.util.resource.Resource;
import org.seleniumhq.jetty9.util.security.CertificateUtils;
import org.seleniumhq.jetty9.util.security.CertificateValidator;
import org.seleniumhq.jetty9.util.security.Password;


























public class ClientCertAuthenticator
  extends LoginAuthenticator
{
  private static final String PASSWORD_PROPERTY = "org.seleniumhq.jetty9.ssl.password";
  private String _trustStorePath;
  private String _trustStoreProvider;
  private String _trustStoreType = "JKS";
  

  private transient Password _trustStorePassword;
  
  private boolean _validateCerts;
  
  private String _crlPath;
  
  private int _maxCertPathLength = -1;
  
  private boolean _enableCRLDP = false;
  
  private boolean _enableOCSP = false;
  

  private String _ocspResponderURL;
  


  public ClientCertAuthenticator() {}
  

  public String getAuthMethod()
  {
    return "CLIENT_CERT";
  }
  
  public Authentication validateRequest(ServletRequest req, ServletResponse res, boolean mandatory)
    throws ServerAuthException
  {
    if (!mandatory) {
      return new DeferredAuthentication(this);
    }
    HttpServletRequest request = (HttpServletRequest)req;
    HttpServletResponse response = (HttpServletResponse)res;
    X509Certificate[] certs = (X509Certificate[])request.getAttribute("javax.servlet.request.X509Certificate");
    

    try
    {
      if ((certs != null) && (certs.length > 0))
      {

        if (_validateCerts)
        {
          trustStore = getKeyStore(_trustStorePath, _trustStoreType, _trustStoreProvider, _trustStorePassword == null ? null : _trustStorePassword
          
            .toString());
          crls = loadCRL(_crlPath);
          validator = new CertificateValidator(trustStore, crls);
          validator.validate(certs);
        }
        
        KeyStore trustStore = certs;Collection<? extends CRL> crls = trustStore.length; for (CertificateValidator validator = 0; validator < crls; validator++) { X509Certificate cert = trustStore[validator];
          
          if (cert != null)
          {

            Principal principal = cert.getSubjectDN();
            if (principal == null) principal = cert.getIssuerDN();
            String username = principal == null ? "clientcert" : principal.getName();
            
            char[] credential = B64Code.encode(cert.getSignature());
            
            UserIdentity user = login(username, credential, req);
            if (user != null)
            {
              return new UserAuthentication(getAuthMethod(), user);
            }
          }
        }
      }
      if (!DeferredAuthentication.isDeferred(response))
      {
        response.sendError(403);
        return Authentication.SEND_FAILURE;
      }
      
      return Authentication.UNAUTHENTICATED;
    }
    catch (Exception e)
    {
      throw new ServerAuthException(e.getMessage());
    }
  }
  
  @Deprecated
  protected KeyStore getKeyStore(InputStream storeStream, String storePath, String storeType, String storeProvider, String storePassword) throws Exception
  {
    return getKeyStore(storePath, storeType, storeProvider, storePassword);
  }
  













  protected KeyStore getKeyStore(String storePath, String storeType, String storeProvider, String storePassword)
    throws Exception
  {
    return CertificateUtils.getKeyStore(Resource.newResource(storePath), storeType, storeProvider, storePassword);
  }
  











  protected Collection<? extends CRL> loadCRL(String crlPath)
    throws Exception
  {
    return CertificateUtils.loadCRL(crlPath);
  }
  
  public boolean secureResponse(ServletRequest req, ServletResponse res, boolean mandatory, Authentication.User validatedUser)
    throws ServerAuthException
  {
    return true;
  }
  




  public boolean isValidateCerts()
  {
    return _validateCerts;
  }
  





  public void setValidateCerts(boolean validateCerts)
  {
    _validateCerts = validateCerts;
  }
  




  public String getTrustStore()
  {
    return _trustStorePath;
  }
  





  public void setTrustStore(String trustStorePath)
  {
    _trustStorePath = trustStorePath;
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
  





  public void setTrustStorePassword(String password)
  {
    _trustStorePassword = Password.getPassword("org.seleniumhq.jetty9.ssl.password", password, null);
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
}
