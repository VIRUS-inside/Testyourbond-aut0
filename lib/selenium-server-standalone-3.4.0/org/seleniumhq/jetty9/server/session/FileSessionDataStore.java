package org.seleniumhq.jetty9.server.session;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import org.seleniumhq.jetty9.util.ClassLoadingObjectInputStream;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;
























public class FileSessionDataStore
  extends AbstractSessionDataStore
{
  private static final Logger LOG = Log.getLogger("org.seleniumhq.jetty9.server.session");
  private File _storeDir;
  private boolean _deleteUnrestorableFiles = false;
  
  public FileSessionDataStore() {}
  
  protected void doStart()
    throws Exception
  {
    initializeStore();
    super.doStart();
  }
  
  protected void doStop()
    throws Exception
  {
    super.doStop();
  }
  
  public File getStoreDir()
  {
    return _storeDir;
  }
  
  public void setStoreDir(File storeDir)
  {
    checkStarted();
    _storeDir = storeDir;
  }
  
  public boolean isDeleteUnrestorableFiles()
  {
    return _deleteUnrestorableFiles;
  }
  
  public void setDeleteUnrestorableFiles(boolean deleteUnrestorableFiles)
  {
    checkStarted();
    _deleteUnrestorableFiles = deleteUnrestorableFiles;
  }
  





  public boolean delete(String id)
    throws Exception
  {
    File file = null;
    if (_storeDir != null)
    {
      file = getFile(_storeDir, id);
      if ((file != null) && (file.exists()) && (file.getParentFile().equals(_storeDir)))
      {
        return file.delete();
      }
    }
    
    return false;
  }
  





  public Set<String> doGetExpired(Set<String> candidates)
  {
    final long now = System.currentTimeMillis();
    HashSet<String> expired = new HashSet();
    
    File[] files = _storeDir.listFiles(new FilenameFilter()
    {

      public boolean accept(File dir, String name)
      {

        if (dir != _storeDir) {
          return false;
        }
        String s = name.substring(0, name.indexOf('_'));
        long expiry = s == null ? 0L : Long.parseLong(s);
        
        if ((expiry > 0L) && (expiry < now)) {
          return true;
        }
        return false;
      }
    });
    
    if (files != null)
    {
      for (File f : files)
      {
        expired.add(getIdFromFile(f));
      }
    }
    


    for (??? = candidates.iterator(); ((Iterator)???).hasNext();) { String c = (String)((Iterator)???).next();
      
      if (!expired.contains(c))
      {

        File f = getFile(_storeDir, c);
        if ((f == null) || (!f.exists())) {
          expired.add(c);
        }
      }
    }
    return expired;
  }
  





  public SessionData load(final String id)
    throws Exception
  {
    final AtomicReference<SessionData> reference = new AtomicReference();
    final AtomicReference<Exception> exception = new AtomicReference();
    Runnable r = new Runnable()
    {
      public void run()
      {
        File file = FileSessionDataStore.this.getFile(_storeDir, id);
        
        if ((file == null) || (!file.exists()))
        {
          if (FileSessionDataStore.LOG.isDebugEnabled())
            FileSessionDataStore.LOG.debug("No file: {}", new Object[] { file });
          return;
        }
        try {
          FileInputStream in = new FileInputStream(file);Throwable localThrowable3 = null;
          try {
            SessionData data = FileSessionDataStore.this.load(in);
            
            file.delete();
            reference.set(data);
          }
          catch (Throwable localThrowable1)
          {
            localThrowable3 = localThrowable1;throw localThrowable1;

          }
          finally
          {

            if (in != null) if (localThrowable3 != null) try { in.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else in.close();
          }
        } catch (UnreadableSessionDataException e) {
          if ((isDeleteUnrestorableFiles()) && (file.exists()) && (file.getParentFile().equals(_storeDir))) {}
          
          file.delete();
          FileSessionDataStore.LOG.warn("Deleted unrestorable file for session {}", new Object[] { id });
          
          exception.set(e);
        }
        catch (Exception e)
        {
          exception.set(e);
        }
        
      }
    };
    _context.run(r);
    
    if (exception.get() != null) {
      throw ((Exception)exception.get());
    }
    return (SessionData)reference.get();
  }
  





  public void doStore(String id, SessionData data, long lastSaveTime)
    throws Exception
  {
    File file = null;
    if (_storeDir != null)
    {

      file = getFile(_storeDir, id);
      if ((file != null) && (file.exists())) {
        file.delete();
      }
      
      file = new File(_storeDir, getFileNameWithExpiry(data));
      try {
        FileOutputStream fos = new FileOutputStream(file, false);Throwable localThrowable3 = null;
        try {
          save(fos, id, data);
        }
        catch (Throwable localThrowable1)
        {
          localThrowable3 = localThrowable1;throw localThrowable1;
        }
        finally {
          if (fos != null) if (localThrowable3 != null) try { fos.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else fos.close();
        }
      } catch (Exception e) {
        e.printStackTrace();
        if (file != null)
          file.delete();
        throw new UnwriteableSessionDataException(id, _context, e);
      }
    }
  }
  



  public void initializeStore()
  {
    if (_storeDir == null) {
      throw new IllegalStateException("No file store specified");
    }
    if (!_storeDir.exists()) {
      _storeDir.mkdirs();
    }
  }
  



  public boolean isPassivating()
  {
    return true;
  }
  






  public boolean exists(String id)
    throws Exception
  {
    File sessionFile = getFile(_storeDir, id);
    if ((sessionFile == null) || (!sessionFile.exists())) {
      return false;
    }
    
    long expiry = getExpiryFromFile(sessionFile);
    if (expiry <= 0L) {
      return true;
    }
    return expiry > System.currentTimeMillis();
  }
  






  private void save(OutputStream os, String id, SessionData data)
    throws IOException
  {
    DataOutputStream out = new DataOutputStream(os);
    out.writeUTF(id);
    out.writeUTF(_context.getCanonicalContextPath());
    out.writeUTF(_context.getVhost());
    out.writeUTF(data.getLastNode());
    out.writeLong(data.getCreated());
    out.writeLong(data.getAccessed());
    out.writeLong(data.getLastAccessed());
    out.writeLong(data.getCookieSet());
    out.writeLong(data.getExpiry());
    out.writeLong(data.getMaxInactiveMs());
    
    List<String> keys = new ArrayList(data.getKeys());
    out.writeInt(keys.size());
    ObjectOutputStream oos = new ObjectOutputStream(out);
    for (String name : keys)
    {
      oos.writeUTF(name);
      oos.writeObject(data.getAttribute(name));
    }
  }
  




  private String getFileName(String id)
  {
    return _context.getCanonicalContextPath() + "_" + _context.getVhost() + "_" + id;
  }
  
  private String getFileNameWithExpiry(SessionData data)
  {
    return "" + data.getExpiry() + "_" + getFileName(data.getId());
  }
  
  private String getIdFromFile(File file)
  {
    if (file == null)
      return null;
    String name = file.getName();
    
    return name.substring(name.lastIndexOf('_') + 1);
  }
  
  private long getExpiryFromFile(File file)
  {
    if (file == null) {
      return 0L;
    }
    String name = file.getName();
    String s = name.substring(0, name.indexOf('_'));
    return s == null ? 0L : Long.parseLong(s);
  }
  








  private File getFile(final File storeDir, final String id)
  {
    File[] files = storeDir.listFiles(new FilenameFilter()
    {


      public boolean accept(File dir, String name)
      {


        if (dir != storeDir)
          return false;
        return name.contains(FileSessionDataStore.this.getFileName(id));
      }
    });
    

    if ((files == null) || (files.length < 1))
      return null;
    return files[0];
  }
  





  private SessionData load(InputStream is)
    throws Exception
  {
    String id = null;
    
    try
    {
      SessionData data = null;
      DataInputStream di = new DataInputStream(is);
      
      id = di.readUTF();
      String contextPath = di.readUTF();
      String vhost = di.readUTF();
      String lastNode = di.readUTF();
      long created = di.readLong();
      long accessed = di.readLong();
      long lastAccessed = di.readLong();
      long cookieSet = di.readLong();
      long expiry = di.readLong();
      long maxIdle = di.readLong();
      
      data = newSessionData(id, created, accessed, lastAccessed, maxIdle);
      data.setContextPath(contextPath);
      data.setVhost(vhost);
      data.setLastNode(lastNode);
      data.setCookieSet(cookieSet);
      data.setExpiry(expiry);
      data.setMaxInactiveMs(maxIdle);
      

      restoreAttributes(di, di.readInt(), data);
      
      return data;
    }
    catch (Exception e)
    {
      throw new UnreadableSessionDataException(id, _context, e);
    }
  }
  






  private void restoreAttributes(InputStream is, int size, SessionData data)
    throws Exception
  {
    if (size > 0)
    {

      Map<String, Object> attributes = new HashMap();
      ClassLoadingObjectInputStream ois = new ClassLoadingObjectInputStream(is);
      for (int i = 0; i < size; i++)
      {
        String key = ois.readUTF();
        Object value = ois.readObject();
        attributes.put(key, value);
      }
      data.putAllAttributes(attributes);
    }
  }
  




  public String toString()
  {
    return String.format("%s[dir=%s,deleteUnrestorableFiles=%b]", new Object[] { super.toString(), _storeDir, Boolean.valueOf(_deleteUnrestorableFiles) });
  }
}
