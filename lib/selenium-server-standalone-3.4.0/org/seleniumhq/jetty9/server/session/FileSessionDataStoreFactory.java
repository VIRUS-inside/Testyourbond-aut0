package org.seleniumhq.jetty9.server.session;

import java.io.File;



























public class FileSessionDataStoreFactory
  extends AbstractSessionDataStoreFactory
{
  boolean _deleteUnrestorableFiles;
  File _storeDir;
  
  public FileSessionDataStoreFactory() {}
  
  public boolean isDeleteUnrestorableFiles()
  {
    return _deleteUnrestorableFiles;
  }
  




  public void setDeleteUnrestorableFiles(boolean deleteUnrestorableFiles)
  {
    _deleteUnrestorableFiles = deleteUnrestorableFiles;
  }
  



  public File getStoreDir()
  {
    return _storeDir;
  }
  




  public void setStoreDir(File storeDir)
  {
    _storeDir = storeDir;
  }
  





  public SessionDataStore getSessionDataStore(SessionHandler handler)
  {
    FileSessionDataStore fsds = new FileSessionDataStore();
    fsds.setDeleteUnrestorableFiles(isDeleteUnrestorableFiles());
    fsds.setStoreDir(getStoreDir());
    fsds.setGracePeriodSec(getGracePeriodSec());
    return fsds;
  }
}
