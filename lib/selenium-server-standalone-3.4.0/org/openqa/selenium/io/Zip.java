package org.openqa.selenium.io;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;












public class Zip
{
  private static final int BUF_SIZE = 16384;
  
  public Zip() {}
  
  public static String zip(File input)
    throws IOException
  {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();Throwable localThrowable6 = null;
    try { ZipOutputStream zos = new ZipOutputStream(bos);Throwable localThrowable7 = null;
      try { if (input.isDirectory()) {
          addToZip(input.getAbsolutePath(), zos, input);
        } else {
          addToZip(input.getParentFile().getAbsolutePath(), zos, input);
        }
      }
      catch (Throwable localThrowable1)
      {
        localThrowable7 = localThrowable1;throw localThrowable1;
      }
      finally {}
      



      return Base64.getEncoder().encodeToString(bos.toByteArray());
    }
    catch (Throwable localThrowable4)
    {
      localThrowable6 = localThrowable4;throw localThrowable4;



    }
    finally
    {


      if (bos != null) if (localThrowable6 != null) try { bos.close(); } catch (Throwable localThrowable5) { localThrowable6.addSuppressed(localThrowable5); } else bos.close();
    }
  }
  
  private static void addToZip(String basePath, ZipOutputStream zos, File toAdd) throws IOException { if (toAdd.isDirectory()) {
      File[] files = toAdd.listFiles();
      if (files != null) {
        for (File file : files) {
          addToZip(basePath, zos, file);
        }
      }
    } else {
      FileInputStream fis = new FileInputStream(toAdd);
      String name = toAdd.getAbsolutePath().substring(basePath.length() + 1);
      
      ZipEntry entry = new ZipEntry(name.replace('\\', '/'));
      zos.putNextEntry(entry);
      

      byte[] buffer = new byte['က'];
      int len; while ((len = fis.read(buffer)) != -1) {
        zos.write(buffer, 0, len);
      }
      
      fis.close();
      zos.closeEntry();
    }
  }
  
  public static File unzipToTempDir(String source, String prefix, String suffix) throws IOException {
    File output = TemporaryFilesystem.getDefaultTmpFS().createTempDir(prefix, suffix);
    unzip(source, output);
    return output;
  }
  
  public static void unzip(String source, File outputDir) throws IOException {
    byte[] bytes = Base64.getMimeDecoder().decode(source);
    
    ByteArrayInputStream bis = new ByteArrayInputStream(bytes);Throwable localThrowable3 = null;
    try { unzip(bis, outputDir);
    }
    catch (Throwable localThrowable1)
    {
      localThrowable3 = localThrowable1;throw localThrowable1;
    } finally {
      if (bis != null) if (localThrowable3 != null) try { bis.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else bis.close();
    }
  }
  
  public static File unzipToTempDir(InputStream source, String prefix, String suffix) throws IOException { File output = TemporaryFilesystem.getDefaultTmpFS().createTempDir(prefix, suffix);
    unzip(source, output);
    return output;
  }
  
  public static void unzip(InputStream source, File outputDir) throws IOException {
    ZipInputStream zis = new ZipInputStream(source);Throwable localThrowable3 = null;
    try { ZipEntry entry;
      while ((entry = zis.getNextEntry()) != null) {
        File file = new File(outputDir, entry.getName());
        if (entry.isDirectory()) {
          FileHandler.createDir(file);
        }
        else
        {
          unzipFile(outputDir, zis, entry.getName());
        }
      }
    }
    catch (Throwable localThrowable1)
    {
      localThrowable3 = localThrowable1;throw localThrowable1;




    }
    finally
    {



      if (zis != null) if (localThrowable3 != null) try { zis.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else zis.close();
    }
  }
  
  public static void unzipFile(File output, InputStream zipStream, String name) throws IOException { File toWrite = new File(output, name);
    
    if (!FileHandler.createDir(toWrite.getParentFile())) {
      throw new IOException("Cannot create parent directory for: " + name);
    }
    OutputStream out = new BufferedOutputStream(new FileOutputStream(toWrite), 16384);Throwable localThrowable3 = null;
    try { byte[] buffer = new byte['䀀'];
      int read;
      while ((read = zipStream.read(buffer)) != -1) {
        out.write(buffer, 0, read);
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
