package org.seleniumhq.jetty9.security;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import javax.security.auth.Subject;
import org.seleniumhq.jetty9.server.UserIdentity;
import org.seleniumhq.jetty9.util.PathWatcher;
import org.seleniumhq.jetty9.util.PathWatcher.Listener;
import org.seleniumhq.jetty9.util.PathWatcher.PathWatchEvent;
import org.seleniumhq.jetty9.util.StringUtil;
import org.seleniumhq.jetty9.util.component.AbstractLifeCycle;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;
import org.seleniumhq.jetty9.util.resource.PathResource;
import org.seleniumhq.jetty9.util.resource.Resource;
import org.seleniumhq.jetty9.util.security.Credential;
































public class PropertyUserStore
  extends AbstractLifeCycle
  implements PathWatcher.Listener
{
  private static final Logger LOG = Log.getLogger(PropertyUserStore.class);
  
  protected Path _configPath;
  
  protected Resource _configResource;
  protected PathWatcher pathWatcher;
  protected boolean hotReload = false;
  
  protected IdentityService _identityService = new DefaultIdentityService();
  protected boolean _firstLoad = true;
  protected final List<String> _knownUsers = new ArrayList();
  protected final Map<String, UserIdentity> _knownUserIdentities = new HashMap();
  
  protected List<UserListener> _listeners;
  

  public PropertyUserStore() {}
  

  @Deprecated
  public String getConfig()
  {
    return _configPath.toString();
  }
  





  @Deprecated
  public void setConfig(String configFile)
  {
    setConfigPath(configFile);
  }
  




  public Path getConfigPath()
  {
    return _configPath;
  }
  




  public void setConfigPath(String configFile)
  {
    if (configFile == null)
    {
      _configPath = null;
    }
    else
    {
      _configPath = new File(configFile).toPath();
    }
  }
  




  public void setConfigPath(File configFile)
  {
    _configPath = configFile.toPath();
  }
  




  public void setConfigPath(Path configPath)
  {
    _configPath = configPath;
  }
  

  public UserIdentity getUserIdentity(String userName)
  {
    return (UserIdentity)_knownUserIdentities.get(userName);
  }
  




  public Resource getConfigResource()
    throws IOException
  {
    if (_configResource == null)
    {
      _configResource = new PathResource(_configPath);
    }
    
    return _configResource;
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
  



  public String toString()
  {
    StringBuilder s = new StringBuilder();
    s.append(getClass().getName());
    s.append("[");
    s.append("users.count=").append(_knownUsers.size());
    s.append("identityService=").append(_identityService);
    s.append("]");
    return s.toString();
  }
  
  protected void loadUsers()
    throws IOException
  {
    if (_configPath == null) {
      return;
    }
    if (LOG.isDebugEnabled())
    {
      LOG.debug("Loading " + this + " from " + _configPath, new Object[0]);
    }
    
    Properties properties = new Properties();
    if (getConfigResource().exists()) {
      properties.load(getConfigResource().getInputStream());
    }
    Set<String> known = new HashSet();
    
    for (Map.Entry<Object, Object> entry : properties.entrySet())
    {
      String username = ((String)entry.getKey()).trim();
      String credentials = ((String)entry.getValue()).trim();
      String roles = null;
      int c = credentials.indexOf(',');
      if (c > 0)
      {
        roles = credentials.substring(c + 1).trim();
        credentials = credentials.substring(0, c).trim();
      }
      
      if ((username != null) && (username.length() > 0) && (credentials != null) && (credentials.length() > 0))
      {
        String[] roleArray = IdentityService.NO_ROLES;
        if ((roles != null) && (roles.length() > 0))
        {
          roleArray = StringUtil.csvSplit(roles);
        }
        known.add(username);
        Credential credential = Credential.getCredential(credentials);
        
        Principal userPrincipal = new AbstractLoginService.UserPrincipal(username, credential);
        Subject subject = new Subject();
        subject.getPrincipals().add(userPrincipal);
        subject.getPrivateCredentials().add(credential);
        
        if (roles != null)
        {
          for (String role : roleArray)
          {
            subject.getPrincipals().add(new AbstractLoginService.RolePrincipal(role));
          }
        }
        
        subject.setReadOnly();
        
        _knownUserIdentities.put(username, _identityService.newUserIdentity(subject, userPrincipal, roleArray));
        notifyUpdate(username, credential, roleArray);
      }
    }
    
    synchronized (_knownUsers)
    {



      if (!_firstLoad)
      {
        Iterator<String> users = _knownUsers.iterator();
        while (users.hasNext())
        {
          String user = (String)users.next();
          if (!known.contains(user))
          {
            _knownUserIdentities.remove(user);
            notifyRemove(user);
          }
        }
      }
      




      _knownUsers.clear();
      _knownUsers.addAll(known);
    }
    




    _firstLoad = false;
    
    if (LOG.isDebugEnabled())
    {
      LOG.debug("Loaded " + this + " from " + _configPath, new Object[0]);
    }
  }
  







  protected void doStart()
    throws Exception
  {
    super.doStart();
    
    loadUsers();
    if ((isHotReload()) && (_configPath != null))
    {
      pathWatcher = new PathWatcher();
      pathWatcher.watch(_configPath);
      pathWatcher.addListener(this);
      pathWatcher.setNotifyExistingOnStart(false);
      pathWatcher.start();
    }
  }
  


  public void onPathWatchEvent(PathWatcher.PathWatchEvent event)
  {
    try
    {
      loadUsers();
    }
    catch (IOException e)
    {
      LOG.warn(e);
    }
  }
  



  protected void doStop()
    throws Exception
  {
    super.doStop();
    if (pathWatcher != null)
      pathWatcher.stop();
    pathWatcher = null;
  }
  



  private void notifyUpdate(String username, Credential credential, String[] roleArray)
  {
    Iterator<UserListener> i;
    


    if (_listeners != null)
    {
      for (i = _listeners.iterator(); i.hasNext();)
      {
        ((UserListener)i.next()).update(username, credential, roleArray);
      }
    }
  }
  


  private void notifyRemove(String username)
  {
    Iterator<UserListener> i;
    

    if (_listeners != null)
    {
      for (i = _listeners.iterator(); i.hasNext();)
      {
        ((UserListener)i.next()).remove(username);
      }
    }
  }
  




  public void registerUserListener(UserListener listener)
  {
    if (_listeners == null)
    {
      _listeners = new ArrayList();
    }
    _listeners.add(listener);
  }
  
  public static abstract interface UserListener
  {
    public abstract void update(String paramString, Credential paramCredential, String[] paramArrayOfString);
    
    public abstract void remove(String paramString);
  }
}
