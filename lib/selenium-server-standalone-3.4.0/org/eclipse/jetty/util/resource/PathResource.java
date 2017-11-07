package org.eclipse.jetty.util.resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.jetty.util.IO;
import org.eclipse.jetty.util.StringUtil;
import org.eclipse.jetty.util.URIUtil;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;


















public class PathResource
  extends Resource
{
  private static final Logger LOG = Log.getLogger(PathResource.class);
  private static final LinkOption[] NO_FOLLOW_LINKS = { LinkOption.NOFOLLOW_LINKS };
  private static final LinkOption[] FOLLOW_LINKS = new LinkOption[0];
  
  private final Path path;
  private final Path alias;
  private final URI uri;
  
  private final Path checkAliasPath()
  {
    Path abs = path;
    









    if (!URIUtil.equalsIgnoreEncodings(uri, path.toUri()))
    {
      return new File(uri).toPath().toAbsolutePath();
    }
    
    if (!abs.isAbsolute())
    {
      abs = path.toAbsolutePath();
    }
    
    try
    {
      if (Files.isSymbolicLink(path))
        return path.getParent().resolve(Files.readSymbolicLink(path));
      if (Files.exists(path, new LinkOption[0]))
      {
        Path real = abs.toRealPath(FOLLOW_LINKS);
        


































        int absCount = abs.getNameCount();
        int realCount = real.getNameCount();
        if (absCount != realCount)
        {

          return real;
        }
        

        for (int i = realCount - 1; i >= 0; i--)
        {
          if (!abs.getName(i).toString().equals(real.getName(i).toString()))
          {
            return real;
          }
        }
      }
    }
    catch (IOException e)
    {
      LOG.ignore(e);
    }
    catch (Exception e)
    {
      LOG.warn("bad alias ({} {}) for {}", new Object[] { e.getClass().getName(), e.getMessage(), path });
    }
    return null;
  }
  

















  public PathResource(File file)
  {
    this(file.toPath());
  }
  





  public PathResource(Path path)
  {
    this.path = path.toAbsolutePath();
    assertValidPath(path);
    uri = this.path.toUri();
    alias = checkAliasPath();
  }
  










  private PathResource(PathResource parent, String childPath)
    throws MalformedURLException
  {
    path = path.getFileSystem().getPath(path.toString(), new String[] { childPath });
    if ((isDirectory()) && (!childPath.endsWith("/")))
      childPath = childPath + "/";
    uri = URIUtil.addDecodedPath(uri, childPath);
    alias = checkAliasPath();
  }
  







  public PathResource(URI uri)
    throws IOException
  {
    if (!uri.isAbsolute())
    {
      throw new IllegalArgumentException("not an absolute uri");
    }
    
    if (!uri.getScheme().equalsIgnoreCase("file"))
    {
      throw new IllegalArgumentException("not file: scheme");
    }
    

    try
    {
      path = Paths.get(uri);
    }
    catch (InvalidPathException e) {
      Path path;
      throw e;
    }
    catch (IllegalArgumentException e)
    {
      throw e;
    }
    catch (Exception e)
    {
      LOG.ignore(e);
      throw new IOException("Unable to build Path from: " + uri, e);
    }
    Path path;
    this.path = path.toAbsolutePath();
    this.uri = path.toUri();
    alias = checkAliasPath();
  }
  


















  public PathResource(URL url)
    throws IOException, URISyntaxException
  {
    this(url.toURI());
  }
  
  public Resource addPath(String subpath)
    throws IOException, MalformedURLException
  {
    String cpath = URIUtil.canonicalPath(subpath);
    
    if ((cpath == null) || (cpath.length() == 0)) {
      throw new MalformedURLException(subpath);
    }
    if ("/".equals(cpath)) {
      return this;
    }
    




    return new PathResource(this, subpath);
  }
  

  private void assertValidPath(Path path)
  {
    String str = path.toString();
    int idx = StringUtil.indexOfControlChars(str);
    if (idx >= 0)
    {
      throw new InvalidPathException(str, "Invalid Character at index " + idx);
    }
  }
  


  public void close() {}
  


  public boolean delete()
    throws SecurityException
  {
    try
    {
      return Files.deleteIfExists(path);
    }
    catch (IOException e)
    {
      LOG.ignore(e); }
    return false;
  }
  


  public boolean equals(Object obj)
  {
    if (this == obj)
    {
      return true;
    }
    if (obj == null)
    {
      return false;
    }
    if (getClass() != obj.getClass())
    {
      return false;
    }
    PathResource other = (PathResource)obj;
    if (path == null)
    {
      if (path != null)
      {
        return false;
      }
    }
    else if (!path.equals(path))
    {
      return false;
    }
    return true;
  }
  

  public boolean exists()
  {
    return Files.exists(path, NO_FOLLOW_LINKS);
  }
  
  public File getFile()
    throws IOException
  {
    return path.toFile();
  }
  



  public Path getPath()
  {
    return path;
  }
  




  public InputStream getInputStream()
    throws IOException
  {
    if (Files.isDirectory(path, new LinkOption[0])) {
      throw new IOException(path + " is a directory");
    }
    return Files.newInputStream(path, new OpenOption[] { StandardOpenOption.READ });
  }
  

  public String getName()
  {
    return path.toAbsolutePath().toString();
  }
  
  public ReadableByteChannel getReadableByteChannel()
    throws IOException
  {
    return FileChannel.open(path, new OpenOption[] { StandardOpenOption.READ });
  }
  

  public URI getURI()
  {
    return uri;
  }
  

  public URL getURL()
  {
    try
    {
      return path.toUri().toURL();
    }
    catch (MalformedURLException e) {}
    
    return null;
  }
  


  public int hashCode()
  {
    int prime = 31;
    int result = 1;
    result = 31 * result + (path == null ? 0 : path.hashCode());
    return result;
  }
  

  public boolean isContainedIn(Resource r)
    throws MalformedURLException
  {
    return false;
  }
  

  public boolean isDirectory()
  {
    return Files.isDirectory(path, FOLLOW_LINKS);
  }
  

  public long lastModified()
  {
    try
    {
      FileTime ft = Files.getLastModifiedTime(path, FOLLOW_LINKS);
      return ft.toMillis();
    }
    catch (IOException e)
    {
      LOG.ignore(e); }
    return 0L;
  }
  


  public long length()
  {
    try
    {
      return Files.size(path);
    }
    catch (IOException e) {}
    

    return 0L;
  }
  


  public boolean isAlias()
  {
    return alias != null;
  }
  









  public Path getAliasPath()
  {
    return alias;
  }
  

  public URI getAlias()
  {
    return alias == null ? null : alias.toUri();
  }
  
  public String[] list()
  {
    try {
      DirectoryStream<Path> dir = Files.newDirectoryStream(path);Throwable localThrowable3 = null;
      try {
        List<String> entries = new ArrayList();
        for (Iterator localIterator = dir.iterator(); localIterator.hasNext();) { entry = (Path)localIterator.next();
          
          String name = entry.getFileName().toString();
          
          if (Files.isDirectory(entry, new LinkOption[0]))
          {
            name = name + "/";
          }
          
          entries.add(name); }
        Path entry;
        int size = entries.size();
        return (String[])entries.toArray(new String[size]);
      }
      catch (Throwable localThrowable1)
      {
        localThrowable3 = localThrowable1;throw localThrowable1;






      }
      finally
      {






        if (dir != null) { if (localThrowable3 != null) try { dir.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else { dir.close();
          }
        }
      }
      




      return null;
    }
    catch (DirectoryIteratorException e)
    {
      LOG.debug(e);
    }
    catch (IOException e)
    {
      LOG.debug(e);
    }
  }
  

  public boolean renameTo(Resource dest)
    throws SecurityException
  {
    if ((dest instanceof PathResource))
    {
      PathResource destRes = (PathResource)dest;
      try
      {
        Path result = Files.move(path, path, new CopyOption[0]);
        return Files.exists(result, NO_FOLLOW_LINKS);
      }
      catch (IOException e)
      {
        LOG.ignore(e);
        return false;
      }
    }
    

    return false;
  }
  

  public void copyTo(File destination)
    throws IOException
  {
    if (isDirectory())
    {
      IO.copyDir(path.toFile(), destination);
    }
    else
    {
      Files.copy(path, destination.toPath(), new CopyOption[0]);
    }
  }
  

  public String toString()
  {
    return uri.toASCIIString();
  }
}
