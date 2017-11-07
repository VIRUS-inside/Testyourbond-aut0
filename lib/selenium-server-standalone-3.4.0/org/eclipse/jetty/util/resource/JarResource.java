package org.eclipse.jetty.util.resource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;
import org.eclipse.jetty.util.IO;
import org.eclipse.jetty.util.URIUtil;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;





















public class JarResource
  extends URLResource
{
  private static final Logger LOG = Log.getLogger(JarResource.class);
  
  protected JarURLConnection _jarConnection;
  
  protected JarResource(URL url)
  {
    super(url, null);
  }
  

  protected JarResource(URL url, boolean useCaches)
  {
    super(url, null, useCaches);
  }
  


  public synchronized void close()
  {
    _jarConnection = null;
    super.close();
  }
  


  protected synchronized boolean checkConnection()
  {
    super.checkConnection();
    try
    {
      if (_jarConnection != _connection) {
        newConnection();
      }
    }
    catch (IOException e) {
      LOG.ignore(e);
      _jarConnection = null;
    }
    
    return _jarConnection != null;
  }
  



  protected void newConnection()
    throws IOException
  {
    _jarConnection = ((JarURLConnection)_connection);
  }
  





  public boolean exists()
  {
    if (_urlString.endsWith("!/")) {
      return checkConnection();
    }
    return super.exists();
  }
  


  public File getFile()
    throws IOException
  {
    return null;
  }
  


  public InputStream getInputStream()
    throws IOException
  {
    checkConnection();
    if (!_urlString.endsWith("!/"))
      new FilterInputStream(getInputStream(false))
      {
        public void close() throws IOException {
          in = IO.getClosedStream();
        }
      };
    URL url = new URL(_urlString.substring(4, _urlString.length() - 2));
    InputStream is = url.openStream();
    return is;
  }
  





  public void copyTo(File directory)
    throws IOException
  {
    if (!exists()) {
      return;
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("Extract " + this + " to " + directory, new Object[0]);
    }
    String urlString = getURL().toExternalForm().trim();
    int endOfJarUrl = urlString.indexOf("!/");
    int startOfJarUrl = endOfJarUrl >= 0 ? 4 : 0;
    
    if (endOfJarUrl < 0) {
      throw new IOException("Not a valid jar url: " + urlString);
    }
    URL jarFileURL = new URL(urlString.substring(startOfJarUrl, endOfJarUrl));
    String subEntryName = endOfJarUrl + 2 < urlString.length() ? urlString.substring(endOfJarUrl + 2) : null;
    boolean subEntryIsDir = (subEntryName != null) && (subEntryName.endsWith("/"));
    
    if (LOG.isDebugEnabled())
      LOG.debug("Extracting entry = " + subEntryName + " from jar " + jarFileURL, new Object[0]);
    URLConnection c = jarFileURL.openConnection();
    c.setUseCaches(false);
    InputStream is = c.getInputStream();Throwable localThrowable12 = null;
    try { JarInputStream jin = new JarInputStream(is);Throwable localThrowable13 = null;
      try {
        JarEntry entry;
        OutputStream fout;
        while ((entry = jin.getNextJarEntry()) != null)
        {
          String entryName = entry.getName();
          boolean shouldExtract; boolean shouldExtract; if ((subEntryName != null) && (entryName.startsWith(subEntryName)))
          {

            if ((!subEntryIsDir) && (subEntryName.length() + 1 == entryName.length()) && (entryName.endsWith("/"))) {
              subEntryIsDir = true;
            }
            
            boolean shouldExtract;
            if (subEntryIsDir)
            {




              entryName = entryName.substring(subEntryName.length());
              boolean shouldExtract; if (!entryName.equals(""))
              {

                shouldExtract = true;
              }
              else {
                shouldExtract = false;
              }
            } else {
              shouldExtract = true;
            } } else { boolean shouldExtract;
            if ((subEntryName != null) && (!entryName.startsWith(subEntryName)))
            {


              shouldExtract = false;

            }
            else
            {
              shouldExtract = true;
            }
          }
          if (!shouldExtract)
          {
            if (LOG.isDebugEnabled()) {
              LOG.debug("Skipping entry: " + entryName, new Object[0]);
            }
          }
          else {
            String dotCheck = entryName.replace('\\', '/');
            dotCheck = URIUtil.canonicalPath(dotCheck);
            if (dotCheck == null)
            {
              if (LOG.isDebugEnabled()) {
                LOG.debug("Invalid entry: " + entryName, new Object[0]);
              }
            }
            else {
              File file = new File(directory, entryName);
              
              if (entry.isDirectory())
              {

                if (!file.exists()) {
                  file.mkdirs();
                }
              }
              else
              {
                File dir = new File(file.getParent());
                if (!dir.exists()) {
                  dir.mkdirs();
                }
                
                fout = new FileOutputStream(file);Throwable localThrowable14 = null;
                try {
                  IO.copy(jin, fout);
                }
                catch (Throwable localThrowable1)
                {
                  localThrowable14 = localThrowable1;throw localThrowable1;
                }
                finally {}
                


                if (entry.getTime() >= 0L)
                  file.setLastModified(entry.getTime());
              }
            }
          } }
        if ((subEntryName == null) || ((subEntryName != null) && (subEntryName.equalsIgnoreCase("META-INF/MANIFEST.MF"))))
        {
          Manifest manifest = jin.getManifest();
          if (manifest != null)
          {
            File metaInf = new File(directory, "META-INF");
            metaInf.mkdir();
            File f = new File(metaInf, "MANIFEST.MF");
            OutputStream fout = new FileOutputStream(f);fout = null;
            try {
              manifest.write(fout);
            }
            catch (Throwable localThrowable16)
            {
              fout = localThrowable16;throw localThrowable16;
            }
            finally {}
          }
        }
      }
      catch (Throwable localThrowable7)
      {
        localThrowable13 = localThrowable7;throw localThrowable7; } finally {} } catch (Throwable localThrowable10) { localThrowable12 = localThrowable10;throw localThrowable10;


















































    }
    finally
    {

















































      if (is != null) if (localThrowable12 != null) try { is.close(); } catch (Throwable localThrowable11) { localThrowable12.addSuppressed(localThrowable11); } else is.close();
    }
  }
  
  public static Resource newJarResource(Resource resource) throws IOException {
    if ((resource instanceof JarResource))
      return resource;
    return Resource.newResource("jar:" + resource + "!/");
  }
}
