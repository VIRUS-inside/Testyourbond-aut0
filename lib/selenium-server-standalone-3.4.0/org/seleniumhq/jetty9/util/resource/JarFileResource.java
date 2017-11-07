package org.seleniumhq.jetty9.util.resource;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;



















class JarFileResource
  extends JarResource
{
  private static final Logger LOG = Log.getLogger(JarFileResource.class);
  
  private JarFile _jarFile;
  private File _file;
  private String[] _list;
  private JarEntry _entry;
  private boolean _directory;
  private String _jarUrl;
  private String _path;
  private boolean _exists;
  
  protected JarFileResource(URL url)
  {
    super(url);
  }
  

  protected JarFileResource(URL url, boolean useCaches)
  {
    super(url, useCaches);
  }
  


  public synchronized void close()
  {
    _exists = false;
    _list = null;
    _entry = null;
    _file = null;
    

    if (!getUseCaches())
    {
      if (_jarFile != null)
      {
        try
        {
          if (LOG.isDebugEnabled())
            LOG.debug("Closing JarFile " + _jarFile.getName(), new Object[0]);
          _jarFile.close();
        }
        catch (IOException ioe)
        {
          LOG.ignore(ioe);
        }
      }
    }
    _jarFile = null;
    super.close();
  }
  


  protected synchronized boolean checkConnection()
  {
    try
    {
      super.checkConnection();
      


      if (_jarConnection == null)
      {
        _entry = null;
        _file = null;
        _jarFile = null;
        _list = null;
      }
    }
    finally
    {
      if (_jarConnection == null)
      {
        _entry = null;
        _file = null;
        _jarFile = null;
        _list = null;
      }
    }
    return _jarFile != null;
  }
  



  protected synchronized void newConnection()
    throws IOException
  {
    super.newConnection();
    
    _entry = null;
    _file = null;
    _jarFile = null;
    _list = null;
    
    int sep = _urlString.lastIndexOf("!/");
    _jarUrl = _urlString.substring(0, sep + 2);
    _path = _urlString.substring(sep + 2);
    if (_path.length() == 0)
      _path = null;
    _jarFile = _jarConnection.getJarFile();
    _file = new File(_jarFile.getName());
  }
  







  public boolean exists()
  {
    if (_exists) {
      return true;
    }
    if (_urlString.endsWith("!/"))
    {
      String file_url = _urlString.substring(4, _urlString.length() - 2);
      try { return newResource(file_url).exists();
      } catch (Exception e) { LOG.ignore(e);return false;
      }
    }
    boolean check = checkConnection();
    

    if ((_jarUrl != null) && (_path == null))
    {

      _directory = check;
      return true;
    }
    


    boolean close_jar_file = false;
    JarFile jar_file = null;
    if (check)
    {
      jar_file = _jarFile;
    }
    else
    {
      try
      {
        JarURLConnection c = (JarURLConnection)new URL(_jarUrl).openConnection();
        c.setUseCaches(getUseCaches());
        jar_file = c.getJarFile();
        close_jar_file = !getUseCaches();
      }
      catch (Exception e)
      {
        LOG.ignore(e);
      }
    }
    

    if ((jar_file != null) && (_entry == null) && (!_directory))
    {

      JarEntry entry = jar_file.getJarEntry(_path);
      if (entry == null)
      {

        _exists = false;
      }
      else if (entry.isDirectory())
      {
        _directory = true;
        _entry = entry;

      }
      else
      {
        JarEntry directory = jar_file.getJarEntry(_path + '/');
        if (directory != null)
        {
          _directory = true;
          _entry = directory;

        }
        else
        {
          _directory = false;
          _entry = entry;
        }
      }
    }
    
    if ((close_jar_file) && (jar_file != null))
    {
      try
      {
        jar_file.close();
      }
      catch (IOException ioe)
      {
        LOG.ignore(ioe);
      }
    }
    

    _exists = ((_directory) || (_entry != null));
    return _exists;
  }
  








  public boolean isDirectory()
  {
    return (_urlString.endsWith("/")) || ((exists()) && (_directory));
  }
  





  public long lastModified()
  {
    if ((checkConnection()) && (_file != null))
    {
      if ((exists()) && (_entry != null))
        return _entry.getTime();
      return _file.lastModified();
    }
    return -1L;
  }
  


  public synchronized String[] list()
  {
    if ((isDirectory()) && (_list == null))
    {
      List<String> list = null;
      try
      {
        list = listEntries();



      }
      catch (Exception e)
      {



        LOG.warn("Retrying list:" + e, new Object[0]);
        LOG.debug(e);
        close();
        list = listEntries();
      }
      
      if (list != null)
      {
        _list = new String[list.size()];
        list.toArray(_list);
      }
    }
    return _list;
  }
  


  private List<String> listEntries()
  {
    checkConnection();
    
    ArrayList<String> list = new ArrayList(32);
    JarFile jarFile = _jarFile;
    if (jarFile == null)
    {
      try
      {
        JarURLConnection jc = (JarURLConnection)new URL(_jarUrl).openConnection();
        jc.setUseCaches(getUseCaches());
        jarFile = jc.getJarFile();

      }
      catch (Exception e)
      {
        e.printStackTrace();
        LOG.ignore(e);
      }
      if (jarFile == null) {
        throw new IllegalStateException();
      }
    }
    Enumeration<JarEntry> e = jarFile.entries();
    String dir = _urlString.substring(_urlString.lastIndexOf("!/") + 2);
    while (e.hasMoreElements())
    {
      JarEntry entry = (JarEntry)e.nextElement();
      String name = entry.getName().replace('\\', '/');
      if ((name.startsWith(dir)) && (name.length() != dir.length()))
      {


        String listName = name.substring(dir.length());
        int dash = listName.indexOf('/');
        if (dash >= 0)
        {


          if ((dash != 0) || (listName.length() != 1))
          {


            if (dash == 0) {
              listName = listName.substring(dash + 1, listName.length());
            } else {
              listName = listName.substring(0, dash + 1);
            }
            if (list.contains(listName)) {}
          }
        }
        else
          list.add(listName);
      }
    }
    return list;
  }
  









  public long length()
  {
    if (isDirectory()) {
      return -1L;
    }
    if (_entry != null) {
      return _entry.getSize();
    }
    return -1L;
  }
  







  public static Resource getNonCachingResource(Resource resource)
  {
    if (!(resource instanceof JarFileResource)) {
      return resource;
    }
    JarFileResource oldResource = (JarFileResource)resource;
    
    JarFileResource newResource = new JarFileResource(oldResource.getURL(), false);
    return newResource;
  }
  








  public boolean isContainedIn(Resource resource)
    throws MalformedURLException
  {
    String string = _urlString;
    int index = string.lastIndexOf("!/");
    if (index > 0)
      string = string.substring(0, index);
    if (string.startsWith("jar:"))
      string = string.substring(4);
    URL url = new URL(string);
    return url.sameFile(resource.getURI().toURL());
  }
}
