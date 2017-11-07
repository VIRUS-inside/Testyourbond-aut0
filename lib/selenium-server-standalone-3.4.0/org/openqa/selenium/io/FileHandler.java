package org.openqa.selenium.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import org.openqa.selenium.Platform;




















public class FileHandler
{
  public FileHandler() {}
  
  public static void copyResource(File outputDir, Class<?> forClassLoader, String... names)
    throws IOException
  {
    for (String name : names) {
      InputStream is = locateResource(forClassLoader, name);Throwable localThrowable3 = null;
      try { Zip.unzipFile(outputDir, is, name);
      }
      catch (Throwable localThrowable1)
      {
        localThrowable3 = localThrowable1;throw localThrowable1;
      } finally {
        if (is != null) if (localThrowable3 != null) try { is.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else is.close();
      }
    }
  }
  
  private static InputStream locateResource(Class<?> forClassLoader, String name) throws IOException {
    String arch = System.getProperty("os.arch").toLowerCase() + "/";
    
    List<String> alternatives = Arrays.asList(new String[] { name, "/" + name, arch + name, "/" + arch + name });
    if (Platform.getCurrent().is(Platform.MAC)) {
      alternatives.add("mac/" + name);
      alternatives.add("/mac/" + name);
    }
    

    for (String possibility : alternatives) {
      InputStream stream = FileHandler.class.getResourceAsStream(possibility);
      if (stream != null) {
        return stream;
      }
      stream = forClassLoader.getResourceAsStream(possibility);
      if (stream != null) {
        return stream;
      }
    }
    
    throw new IOException("Unable to locate: " + name);
  }
  
  public static boolean createDir(File dir) throws IOException
  {
    if (((dir.exists()) || (dir.mkdirs())) && (dir.canWrite())) {
      return true;
    }
    if (dir.exists()) {
      makeWritable(dir);
      return dir.canWrite();
    }
    


    return createDir(dir.getParentFile());
  }
  
  public static boolean makeWritable(File file) throws IOException {
    return (file.canWrite()) || (file.setWritable(true));
  }
  
  public static boolean isZipped(String fileName) {
    return (fileName.endsWith(".zip")) || (fileName.endsWith(".xpi"));
  }
  
  public static boolean delete(File toDelete) {
    boolean deleted = true;
    
    if (toDelete.isDirectory()) {
      File[] children = toDelete.listFiles();
      if (children != null) {
        for (File child : children) {
          deleted &= ((child.canWrite()) && (delete(child)));
        }
      }
    }
    
    return (deleted) && (toDelete.canWrite()) && (toDelete.delete());
  }
  
  public static void copy(File from, File to) throws IOException {
    if (!from.exists()) {
      return;
    }
    
    if (from.isDirectory()) {
      copyDir(from, to);
    } else {
      copyFile(from, to);
    }
  }
  
  private static void copyDir(File from, File to) throws IOException
  {
    createDir(to);
    

    String[] children = from.list();
    if (children == null) {
      throw new IOException("Could not copy directory " + from.getPath());
    }
    for (String child : children) {
      if ((!".parentlock".equals(child)) && (!"parent.lock".equals(child))) {
        copy(new File(from, child), new File(to, child));
      }
    }
  }
  
  private static void copyFile(File from, File to) throws IOException {
    OutputStream out = new FileOutputStream(to);Throwable localThrowable3 = null;
    try { long copied = Files.copy(from.toPath(), out);
      long length = from.length();
      if (copied != length) {
        throw new IOException("Could not transfer all bytes from " + from + " to " + to);
      }
    }
    catch (Throwable localThrowable1)
    {
      localThrowable3 = localThrowable1;throw localThrowable1;

    }
    finally
    {

      if (out != null) if (localThrowable3 != null) try { out.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else out.close();
    }
  }
}
