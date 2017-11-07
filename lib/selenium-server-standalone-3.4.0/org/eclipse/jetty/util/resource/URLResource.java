package org.eclipse.jetty.util.resource;

import java.io.File;
import java.io.FilePermission;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.ReadableByteChannel;
import java.security.Permission;
import org.eclipse.jetty.util.URIUtil;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;





















public class URLResource
  extends Resource
{
  private static final Logger LOG = Log.getLogger(URLResource.class);
  
  protected final URL _url;
  protected final String _urlString;
  protected URLConnection _connection;
  protected InputStream _in = null;
  transient boolean _useCaches = Resource.__defaultUseCaches;
  

  protected URLResource(URL url, URLConnection connection)
  {
    _url = url;
    _urlString = _url.toExternalForm();
    _connection = connection;
  }
  

  protected URLResource(URL url, URLConnection connection, boolean useCaches)
  {
    this(url, connection);
    _useCaches = useCaches;
  }
  

  protected synchronized boolean checkConnection()
  {
    if (_connection == null) {
      try
      {
        _connection = _url.openConnection();
        _connection.setUseCaches(_useCaches);
      }
      catch (IOException e)
      {
        LOG.ignore(e);
      }
    }
    return _connection != null;
  }
  




  public synchronized void close()
  {
    if (_in != null) {
      try {
        _in.close(); } catch (IOException e) { LOG.ignore(e); }
      _in = null;
    }
    
    if (_connection != null) {
      _connection = null;
    }
  }
  




  public boolean exists()
  {
    try
    {
      synchronized (this)
      {
        if ((checkConnection()) && (_in == null)) {
          _in = _connection.getInputStream();
        }
      }
    }
    catch (IOException e) {
      LOG.ignore(e);
    }
    return _in != null;
  }
  







  public boolean isDirectory()
  {
    return (exists()) && (_urlString.endsWith("/"));
  }
  






  public long lastModified()
  {
    if (checkConnection())
      return _connection.getLastModified();
    return -1L;
  }
  






  public long length()
  {
    if (checkConnection())
      return _connection.getContentLength();
    return -1L;
  }
  





  public URL getURL()
  {
    return _url;
  }
  







  public File getFile()
    throws IOException
  {
    if (checkConnection())
    {
      Permission perm = _connection.getPermission();
      if ((perm instanceof FilePermission)) {
        return new File(perm.getName());
      }
    }
    try {
      return new File(_url.getFile());
    } catch (Exception e) { LOG.ignore(e);
    }
    
    return null;
  }
  





  public String getName()
  {
    return _url.toExternalForm();
  }
  







  public synchronized InputStream getInputStream()
    throws IOException
  {
    return getInputStream(true);
  }
  














  protected synchronized InputStream getInputStream(boolean resetConnection)
    throws IOException
  {
    if (!checkConnection()) {
      throw new IOException("Invalid resource");
    }
    try {
      InputStream in;
      if (_in != null)
      {
        in = _in;
        _in = null;
        return in;
      }
      return _connection.getInputStream();
    }
    finally
    {
      if (resetConnection)
      {
        _connection = null;
        if (LOG.isDebugEnabled()) { LOG.debug("Connection nulled", new Object[0]);
        }
      }
    }
  }
  
  public ReadableByteChannel getReadableByteChannel()
    throws IOException
  {
    return null;
  }
  





  public boolean delete()
    throws SecurityException
  {
    throw new SecurityException("Delete not supported");
  }
  





  public boolean renameTo(Resource dest)
    throws SecurityException
  {
    throw new SecurityException("RenameTo not supported");
  }
  





  public String[] list()
  {
    return null;
  }
  






  public Resource addPath(String path)
    throws IOException, MalformedURLException
  {
    if (path == null) {
      return null;
    }
    path = URIUtil.canonicalPath(path);
    
    return newResource(URIUtil.addPaths(_url.toExternalForm(), URIUtil.encodePath(path)), _useCaches);
  }
  


  public String toString()
  {
    return _urlString;
  }
  


  public int hashCode()
  {
    return _urlString.hashCode();
  }
  


  public boolean equals(Object o)
  {
    return ((o instanceof URLResource)) && (_urlString.equals(_urlString));
  }
  

  public boolean getUseCaches()
  {
    return _useCaches;
  }
  

  public boolean isContainedIn(Resource containingResource)
    throws MalformedURLException
  {
    return false;
  }
}
