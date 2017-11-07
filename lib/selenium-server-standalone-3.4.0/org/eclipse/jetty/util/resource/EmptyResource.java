package org.eclipse.jetty.util.resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.ReadableByteChannel;























public class EmptyResource
  extends Resource
{
  public static final Resource INSTANCE = new EmptyResource();
  

  private EmptyResource() {}
  

  public boolean isContainedIn(Resource r)
    throws MalformedURLException
  {
    return false;
  }
  


  public void close() {}
  


  public boolean exists()
  {
    return false;
  }
  

  public boolean isDirectory()
  {
    return false;
  }
  

  public long lastModified()
  {
    return 0L;
  }
  

  public long length()
  {
    return 0L;
  }
  

  public URL getURL()
  {
    return null;
  }
  
  public File getFile()
    throws IOException
  {
    return null;
  }
  

  public String getName()
  {
    return null;
  }
  
  public InputStream getInputStream()
    throws IOException
  {
    return null;
  }
  
  public ReadableByteChannel getReadableByteChannel()
    throws IOException
  {
    return null;
  }
  
  public boolean delete()
    throws SecurityException
  {
    return false;
  }
  
  public boolean renameTo(Resource dest)
    throws SecurityException
  {
    return false;
  }
  

  public String[] list()
  {
    return null;
  }
  
  public Resource addPath(String path)
    throws IOException, MalformedURLException
  {
    return null;
  }
}
