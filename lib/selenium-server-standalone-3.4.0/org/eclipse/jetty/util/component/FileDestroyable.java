package org.eclipse.jetty.util.component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.eclipse.jetty.util.IO;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.util.resource.Resource;



















public class FileDestroyable
  implements Destroyable
{
  private static final Logger LOG = Log.getLogger(FileDestroyable.class);
  final List<File> _files = new ArrayList();
  

  public FileDestroyable() {}
  
  public FileDestroyable(String file)
    throws IOException
  {
    _files.add(Resource.newResource(file).getFile());
  }
  
  public FileDestroyable(File file)
  {
    _files.add(file);
  }
  
  public void addFile(String file) throws IOException
  {
    Resource r = Resource.newResource(file);Throwable localThrowable3 = null;
    try {
      _files.add(r.getFile());
    }
    catch (Throwable localThrowable1)
    {
      localThrowable3 = localThrowable1;throw localThrowable1;
    }
    finally {
      if (r != null) if (localThrowable3 != null) try { r.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else r.close();
    }
  }
  
  public void addFile(File file) {
    _files.add(file);
  }
  
  public void addFiles(Collection<File> files)
  {
    _files.addAll(files);
  }
  
  public void removeFile(String file) throws IOException
  {
    Resource r = Resource.newResource(file);Throwable localThrowable3 = null;
    try {
      _files.remove(r.getFile());
    }
    catch (Throwable localThrowable1)
    {
      localThrowable3 = localThrowable1;throw localThrowable1;
    }
    finally {
      if (r != null) if (localThrowable3 != null) try { r.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else r.close();
    }
  }
  
  public void removeFile(File file) {
    _files.remove(file);
  }
  

  public void destroy()
  {
    for (File file : _files)
    {
      if (file.exists())
      {
        if (LOG.isDebugEnabled())
          LOG.debug("Destroy {}", new Object[] { file });
        IO.delete(file);
      }
    }
  }
}
