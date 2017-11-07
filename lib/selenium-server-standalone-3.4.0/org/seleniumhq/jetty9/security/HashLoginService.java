package org.seleniumhq.jetty9.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.security.auth.Subject;
import org.seleniumhq.jetty9.server.UserIdentity;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;
import org.seleniumhq.jetty9.util.resource.Resource;







































public class HashLoginService
  extends AbstractLoginService
{
  private static final Logger LOG = Log.getLogger(HashLoginService.class);
  
  protected PropertyUserStore _propertyUserStore;
  protected String _config;
  protected Resource _configResource;
  protected boolean hotReload = false;
  



  public HashLoginService() {}
  


  public HashLoginService(String name)
  {
    setName(name);
  }
  

  public HashLoginService(String name, String config)
  {
    setName(name);
    setConfig(config);
  }
  

  public String getConfig()
  {
    return _config;
  }
  

  public void getConfig(String config)
  {
    _config = config;
  }
  

  public Resource getConfigResource()
  {
    return _configResource;
  }
  







  public void setConfig(String config)
  {
    _config = config;
  }
  





  public boolean isHotReload()
  {
    return hotReload;
  }
  





  public void setHotReload(boolean enable)
  {
    if (isRunning())
    {
      throw new IllegalStateException("Cannot set hot reload while user store is running");
    }
    hotReload = enable;
  }
  




  protected String[] loadRoleInfo(AbstractLoginService.UserPrincipal user)
  {
    UserIdentity id = _propertyUserStore.getUserIdentity(user.getName());
    if (id == null) {
      return null;
    }
    
    Set<AbstractLoginService.RolePrincipal> roles = id.getSubject().getPrincipals(AbstractLoginService.RolePrincipal.class);
    if (roles == null) {
      return null;
    }
    List<String> list = new ArrayList();
    for (AbstractLoginService.RolePrincipal r : roles) {
      list.add(r.getName());
    }
    return (String[])list.toArray(new String[roles.size()]);
  }
  





  protected AbstractLoginService.UserPrincipal loadUserInfo(String userName)
  {
    UserIdentity id = _propertyUserStore.getUserIdentity(userName);
    if (id != null)
    {
      return (AbstractLoginService.UserPrincipal)id.getUserPrincipal();
    }
    
    return null;
  }
  






  protected void doStart()
    throws Exception
  {
    super.doStart();
    
    if (_propertyUserStore == null)
    {
      if (LOG.isDebugEnabled()) {
        LOG.debug("doStart: Starting new PropertyUserStore. PropertiesFile: " + _config + " hotReload: " + hotReload, new Object[0]);
      }
      _propertyUserStore = new PropertyUserStore();
      _propertyUserStore.setHotReload(hotReload);
      _propertyUserStore.setConfigPath(_config);
      _propertyUserStore.start();
    }
  }
  




  protected void doStop()
    throws Exception
  {
    super.doStop();
    if (_propertyUserStore != null)
      _propertyUserStore.stop();
    _propertyUserStore = null;
  }
}
