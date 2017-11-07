package org.seleniumhq.jetty9.security;

import java.util.Properties;
import java.util.Set;
import javax.security.auth.Subject;
import javax.servlet.ServletRequest;
import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.GSSManager;
import org.ietf.jgss.GSSName;
import org.ietf.jgss.Oid;
import org.seleniumhq.jetty9.server.UserIdentity;
import org.seleniumhq.jetty9.util.B64Code;
import org.seleniumhq.jetty9.util.component.AbstractLifeCycle;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;
import org.seleniumhq.jetty9.util.resource.Resource;


















public class SpnegoLoginService
  extends AbstractLifeCycle
  implements LoginService
{
  private static final Logger LOG = Log.getLogger(SpnegoLoginService.class);
  
  protected IdentityService _identityService;
  
  protected String _name;
  
  private String _config;
  
  private String _targetName;
  

  public SpnegoLoginService() {}
  
  public SpnegoLoginService(String name)
  {
    setName(name);
  }
  
  public SpnegoLoginService(String name, String config)
  {
    setName(name);
    setConfig(config);
  }
  

  public String getName()
  {
    return _name;
  }
  
  public void setName(String name)
  {
    if (isRunning())
    {
      throw new IllegalStateException("Running");
    }
    
    _name = name;
  }
  
  public String getConfig()
  {
    return _config;
  }
  
  public void setConfig(String config)
  {
    if (isRunning())
    {
      throw new IllegalStateException("Running");
    }
    
    _config = config;
  }
  


  protected void doStart()
    throws Exception
  {
    Properties properties = new Properties();
    Resource resource = Resource.newResource(_config);
    properties.load(resource.getInputStream());
    
    _targetName = properties.getProperty("targetName");
    
    LOG.debug("Target Name {}", new Object[] { _targetName });
    
    super.doStart();
  }
  




  public UserIdentity login(String username, Object credentials, ServletRequest request)
  {
    String encodedAuthToken = (String)credentials;
    
    byte[] authToken = B64Code.decode(encodedAuthToken);
    
    GSSManager manager = GSSManager.getInstance();
    try
    {
      Oid krb5Oid = new Oid("1.3.6.1.5.5.2");
      GSSName gssName = manager.createName(_targetName, null);
      GSSCredential serverCreds = manager.createCredential(gssName, Integer.MAX_VALUE, krb5Oid, 2);
      GSSContext gContext = manager.createContext(serverCreds);
      
      if (gContext == null)
      {
        LOG.debug("SpnegoUserRealm: failed to establish GSSContext", new Object[0]);
      }
      else
      {
        while (!gContext.isEstablished())
        {
          authToken = gContext.acceptSecContext(authToken, 0, authToken.length);
        }
        if (gContext.isEstablished())
        {
          String clientName = gContext.getSrcName().toString();
          String role = clientName.substring(clientName.indexOf('@') + 1);
          
          LOG.debug("SpnegoUserRealm: established a security context", new Object[0]);
          LOG.debug("Client Principal is: " + gContext.getSrcName(), new Object[0]);
          LOG.debug("Server Principal is: " + gContext.getTargName(), new Object[0]);
          LOG.debug("Client Default Role: " + role, new Object[0]);
          
          SpnegoUserPrincipal user = new SpnegoUserPrincipal(clientName, authToken);
          
          Subject subject = new Subject();
          subject.getPrincipals().add(user);
          
          return _identityService.newUserIdentity(subject, user, new String[] { role });
        }
        
      }
    }
    catch (GSSException gsse)
    {
      LOG.warn(gsse);
    }
    
    return null;
  }
  

  public boolean validate(UserIdentity user)
  {
    return false;
  }
  

  public IdentityService getIdentityService()
  {
    return _identityService;
  }
  

  public void setIdentityService(IdentityService service)
  {
    _identityService = service;
  }
  
  public void logout(UserIdentity user) {}
}
