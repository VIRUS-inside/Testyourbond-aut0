package org.openqa.selenium.io;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import org.openqa.selenium.WebDriverException;






















public class TemporaryFilesystem
{
  private final Set<File> temporaryFiles = new CopyOnWriteArraySet();
  private final File baseDir;
  private final Thread shutdownHook = new Thread()
  {
    public void run() {
      deleteTemporaryFiles();
    }
  };
  
  private static File sysTemp = new File(System.getProperty("java.io.tmpdir"));
  private static TemporaryFilesystem instance = new TemporaryFilesystem(sysTemp);
  
  public static TemporaryFilesystem getDefaultTmpFS() {
    return instance;
  }
  
  public static void setTemporaryDirectory(File directory) {
    synchronized (TemporaryFilesystem.class) {
      instance = new TemporaryFilesystem(directory);
    }
  }
  
  public static TemporaryFilesystem getTmpFsBasedOn(File directory) {
    return new TemporaryFilesystem(directory);
  }
  
  private TemporaryFilesystem(File baseDir)
  {
    this.baseDir = baseDir;
    
    Runtime.getRuntime().addShutdownHook(shutdownHook);
    
    if (!baseDir.exists()) {
      throw new WebDriverException("Unable to find tmp dir: " + baseDir.getAbsolutePath());
    }
    if (!baseDir.canWrite()) {
      throw new WebDriverException("Unable to write to tmp dir: " + baseDir.getAbsolutePath());
    }
  }
  







  public File createTempDir(String prefix, String suffix)
  {
    try
    {
      File file = File.createTempFile(prefix, suffix, baseDir);
      file.delete();
      

      File dir = new File(file.getAbsolutePath());
      if (!dir.mkdirs()) {
        throw new WebDriverException("Cannot create profile directory at " + dir.getAbsolutePath());
      }
      

      FileHandler.createDir(dir);
      
      temporaryFiles.add(dir);
      return dir;
    }
    catch (IOException e) {
      throw new WebDriverException("Unable to create temporary file at " + baseDir.getAbsolutePath());
    }
  }
  





  public void deleteTempDir(File file)
  {
    if (!shouldReap()) {
      return;
    }
    

    if (temporaryFiles.remove(file)) {
      FileHandler.delete(file);
    }
  }
  


  public void deleteTemporaryFiles()
  {
    if (!shouldReap()) {
      return;
    }
    
    for (File file : temporaryFiles) {
      try {
        FileHandler.delete(file);
      }
      catch (WebDriverException localWebDriverException) {}
    }
  }
  





  boolean shouldReap()
  {
    String reap = System.getProperty("webdriver.reap_profile", "true");
    return Boolean.valueOf(reap).booleanValue();
  }
  
  public boolean deleteBaseDir() {
    boolean wasDeleted = baseDir.delete();
    if (!baseDir.exists()) {
      Runtime.getRuntime().removeShutdownHook(shutdownHook);
    }
    return wasDeleted;
  }
}
