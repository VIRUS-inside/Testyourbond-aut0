package org.seleniumhq.jetty9.security;

import java.io.PrintStream;
import java.io.Serializable;
import java.security.Principal;
import java.util.Set;
import javax.security.auth.Subject;
import javax.servlet.ServletRequest;
import org.seleniumhq.jetty9.server.UserIdentity;
import org.seleniumhq.jetty9.util.component.AbstractLifeCycle;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;
import org.seleniumhq.jetty9.util.security.Credential;






















public abstract class AbstractLoginService
  extends AbstractLifeCycle
  implements LoginService
{
  private static final Logger LOG = Log.getLogger(AbstractLoginService.class);
  
  protected IdentityService _identityService = new DefaultIdentityService();
  protected String _name;
  protected boolean _fullValidate = false;
  public AbstractLoginService() {}
  
  protected abstract String[] loadRoleInfo(UserPrincipal paramUserPrincipal);
  
  protected abstract UserPrincipal loadUserInfo(String paramString);
  
  public static class RolePrincipal implements Principal, Serializable
  {
    private static final long serialVersionUID = 2998397924051854402L;
    private final String _roleName;
    
    public RolePrincipal(String name) {
      _roleName = name;
    }
    
    public String getName() {
      return _roleName;
    }
  }
  


  public static class UserPrincipal
    implements Principal, Serializable
  {
    private static final long serialVersionUID = -6226920753748399662L;
    

    private final String _name;
    
    private final Credential _credential;
    

    public UserPrincipal(String name, Credential credential)
    {
      _name = name;
      _credential = credential;
    }
    

    public boolean authenticate(Object credentials)
    {
      return (_credential != null) && (_credential.check(credentials));
    }
    

    public boolean authenticate(Credential c)
    {
      return (_credential != null) && (c != null) && (_credential.equals(c));
    }
    

    public String getName()
    {
      return _name;
    }
    




    public String toString()
    {
      return _name;
    }
  }
  











  public String getName()
  {
    return _name;
  }
  




  public void setIdentityService(IdentityService identityService)
  {
    if (isRunning())
      throw new IllegalStateException("Running");
    _identityService = identityService;
  }
  




  public void setName(String name)
  {
    if (isRunning())
      throw new IllegalStateException("Running");
    _name = name;
  }
  


  public String toString()
  {
    return getClass().getSimpleName() + "[" + _name + "]";
  }
  






  public UserIdentity login(String username, Object credentials, ServletRequest request)
  {
    if (username == null) {
      return null;
    }
    UserPrincipal userPrincipal = loadUserInfo(username);
    if ((userPrincipal != null) && (userPrincipal.authenticate(credentials)))
    {

      String[] roles = loadRoleInfo(userPrincipal);
      
      Subject subject = new Subject();
      subject.getPrincipals().add(userPrincipal);
      subject.getPrivateCredentials().add(_credential);
      if (roles != null)
        for (String role : roles)
          subject.getPrincipals().add(new RolePrincipal(role));
      subject.setReadOnly();
      return _identityService.newUserIdentity(subject, userPrincipal, roles);
    }
    
    return null;
  }
  







  public boolean validate(UserIdentity user)
  {
    if (!isFullValidate()) {
      return true;
    }
    
    UserPrincipal fresh = loadUserInfo(user.getUserPrincipal().getName());
    if (fresh == null) {
      return false;
    }
    if ((user.getUserPrincipal() instanceof UserPrincipal))
    {
      System.err.println("VALIDATING user " + fresh.getName());
      return fresh.authenticate(getUserPrincipal_credential);
    }
    
    throw new IllegalStateException("UserPrincipal not KnownUser");
  }
  






  public IdentityService getIdentityService()
  {
    return _identityService;
  }
  






  public void logout(UserIdentity user) {}
  





  public boolean isFullValidate()
  {
    return _fullValidate;
  }
  

  public void setFullValidate(boolean fullValidate)
  {
    _fullValidate = fullValidate;
  }
}
