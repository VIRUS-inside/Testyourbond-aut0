package org.seleniumhq.jetty9.util.resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;






























public class ResourceCollection
  extends Resource
{
  private static final Logger LOG = Log.getLogger(ResourceCollection.class);
  


  private Resource[] _resources;
  



  public ResourceCollection()
  {
    _resources = new Resource[0];
  }
  






  public ResourceCollection(Resource... resources)
  {
    List<Resource> list = new ArrayList();
    for (Resource r : resources)
    {
      if (r != null)
      {
        if ((r instanceof ResourceCollection))
        {
          for (Resource r2 : ((ResourceCollection)r).getResources()) {
            list.add(r2);
          }
        } else
          list.add(r); }
    }
    _resources = ((Resource[])list.toArray(new Resource[list.size()]));
    for (Resource r : _resources)
    {
      if ((!r.exists()) || (!r.isDirectory())) {
        throw new IllegalArgumentException(r + " is not an existing directory.");
      }
    }
  }
  






  public ResourceCollection(String[] resources)
  {
    _resources = new Resource[resources.length];
    try
    {
      for (int i = 0; i < resources.length; i++)
      {
        _resources[i] = Resource.newResource(resources[i]);
        if ((!_resources[i].exists()) || (!_resources[i].isDirectory())) {
          throw new IllegalArgumentException(_resources[i] + " is not an existing directory.");
        }
      }
    }
    catch (IllegalArgumentException e) {
      throw e;
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }
  






  public ResourceCollection(String csvResources)
  {
    setResourcesAsCSV(csvResources);
  }
  






  public Resource[] getResources()
  {
    return _resources;
  }
  






  public void setResources(Resource[] resources)
  {
    _resources = (resources != null ? resources : new Resource[0]);
  }
  








  public void setResourcesAsCSV(String csvResources)
  {
    StringTokenizer tokenizer = new StringTokenizer(csvResources, ",;");
    int len = tokenizer.countTokens();
    if (len == 0)
    {
      throw new IllegalArgumentException("ResourceCollection@setResourcesAsCSV(String)  argument must be a string containing one or more comma-separated resource strings.");
    }
    

    List<Resource> resources = new ArrayList();
    
    try
    {
      while (tokenizer.hasMoreTokens())
      {
        Resource resource = Resource.newResource(tokenizer.nextToken().trim());
        if ((!resource.exists()) || (!resource.isDirectory())) {
          LOG.warn(" !exist " + resource, new Object[0]);
        } else {
          resources.add(resource);
        }
      }
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
    
    _resources = ((Resource[])resources.toArray(new Resource[resources.size()]));
  }
  





  public Resource addPath(String path)
    throws IOException, MalformedURLException
  {
    if (_resources == null) {
      throw new IllegalStateException("*resources* not set.");
    }
    if (path == null) {
      throw new MalformedURLException();
    }
    if ((path.length() == 0) || ("/".equals(path))) {
      return this;
    }
    Resource resource = null;
    ArrayList<Resource> resources = null;
    for (int i = 0; 
        i < _resources.length; i++)
    {
      resource = _resources[i].addPath(path);
      if (resource.exists())
      {
        if (resource.isDirectory())
          break;
        return resource;
      }
    }
    
    for (i++; i < _resources.length; i++)
    {
      Resource r = _resources[i].addPath(path);
      if ((r.exists()) && (r.isDirectory()))
      {
        if (resources == null) {
          resources = new ArrayList();
        }
        if (resource != null)
        {
          resources.add(resource);
          resource = null;
        }
        
        resources.add(r);
      }
    }
    
    if (resource != null)
      return resource;
    if (resources != null)
      return new ResourceCollection((Resource[])resources.toArray(new Resource[resources.size()]));
    return null;
  }
  






  protected Object findResource(String path)
    throws IOException, MalformedURLException
  {
    Resource resource = null;
    ArrayList<Resource> resources = null;
    for (int i = 0; 
        i < _resources.length; i++)
    {
      resource = _resources[i].addPath(path);
      if (resource.exists())
      {
        if (resource.isDirectory()) {
          break;
        }
        return resource;
      }
    }
    
    for (i++; i < _resources.length; i++)
    {
      Resource r = _resources[i].addPath(path);
      if ((r.exists()) && (r.isDirectory()))
      {
        if (resource != null)
        {
          resources = new ArrayList();
          resources.add(resource);
        }
        resources.add(r);
      }
    }
    
    if (resource != null)
      return resource;
    if (resources != null)
      return resources;
    return null;
  }
  

  public boolean delete()
    throws SecurityException
  {
    throw new UnsupportedOperationException();
  }
  


  public boolean exists()
  {
    if (_resources == null) {
      throw new IllegalStateException("*resources* not set.");
    }
    return true;
  }
  

  public File getFile()
    throws IOException
  {
    if (_resources == null) {
      throw new IllegalStateException("*resources* not set.");
    }
    for (Resource r : _resources)
    {
      File f = r.getFile();
      if (f != null)
        return f;
    }
    return null;
  }
  

  public InputStream getInputStream()
    throws IOException
  {
    if (_resources == null) {
      throw new IllegalStateException("*resources* not set.");
    }
    for (Resource r : _resources)
    {
      InputStream is = r.getInputStream();
      if (is != null)
        return is;
    }
    return null;
  }
  

  public ReadableByteChannel getReadableByteChannel()
    throws IOException
  {
    if (_resources == null) {
      throw new IllegalStateException("*resources* not set.");
    }
    for (Resource r : _resources)
    {
      ReadableByteChannel channel = r.getReadableByteChannel();
      if (channel != null)
        return channel;
    }
    return null;
  }
  


  public String getName()
  {
    if (_resources == null) {
      throw new IllegalStateException("*resources* not set.");
    }
    for (Resource r : _resources)
    {
      String name = r.getName();
      if (name != null)
        return name;
    }
    return null;
  }
  


  public URL getURL()
  {
    if (_resources == null) {
      throw new IllegalStateException("*resources* not set.");
    }
    for (Resource r : _resources)
    {
      URL url = r.getURL();
      if (url != null)
        return url;
    }
    return null;
  }
  


  public boolean isDirectory()
  {
    if (_resources == null) {
      throw new IllegalStateException("*resources* not set.");
    }
    return true;
  }
  


  public long lastModified()
  {
    if (_resources == null) {
      throw new IllegalStateException("*resources* not set.");
    }
    for (Resource r : _resources)
    {
      long lm = r.lastModified();
      if (lm != -1L)
        return lm;
    }
    return -1L;
  }
  


  public long length()
  {
    return -1L;
  }
  





  public String[] list()
  {
    if (_resources == null) {
      throw new IllegalStateException("*resources* not set.");
    }
    HashSet<String> set = new HashSet();
    for (Resource r : _resources)
    {
      for (String s : r.list())
        set.add(s);
    }
    String[] result = (String[])set.toArray(new String[set.size()]);
    Arrays.sort(result);
    return result;
  }
  


  public void close()
  {
    if (_resources == null) {
      throw new IllegalStateException("*resources* not set.");
    }
    for (Resource r : _resources) {
      r.close();
    }
  }
  
  public boolean renameTo(Resource dest)
    throws SecurityException
  {
    throw new UnsupportedOperationException();
  }
  


  public void copyTo(File destination)
    throws IOException
  {
    for (int r = _resources.length; r-- > 0;) {
      _resources[r].copyTo(destination);
    }
  }
  




  public String toString()
  {
    if (_resources == null) {
      return "[]";
    }
    return String.valueOf(Arrays.asList(_resources));
  }
  


  public boolean isContainedIn(Resource r)
    throws MalformedURLException
  {
    return false;
  }
}
