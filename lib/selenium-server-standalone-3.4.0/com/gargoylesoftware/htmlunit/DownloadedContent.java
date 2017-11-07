package com.gargoylesoftware.htmlunit;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;












public abstract interface DownloadedContent
  extends Serializable
{
  public abstract InputStream getInputStream()
    throws IOException;
  
  public abstract void cleanUp();
  
  public abstract boolean isEmpty();
  
  public abstract long length();
  
  public static class InMemory
    implements DownloadedContent
  {
    private final byte[] bytes_;
    
    InMemory(byte[] byteArray)
    {
      if (byteArray == null) {
        bytes_ = ArrayUtils.EMPTY_BYTE_ARRAY;
      }
      else {
        bytes_ = byteArray;
      }
    }
    
    public InputStream getInputStream()
    {
      return new ByteArrayInputStream(bytes_);
    }
    


    public void cleanUp() {}
    

    public boolean isEmpty()
    {
      return length() == 0L;
    }
    
    public long length()
    {
      return bytes_.length;
    }
  }
  


  public static class OnFile
    implements DownloadedContent
  {
    private final File file_;
    
    private final boolean temporary_;
    

    OnFile(File file, boolean temporary)
    {
      file_ = file;
      temporary_ = temporary;
    }
    
    public InputStream getInputStream() throws FileNotFoundException
    {
      return new FileInputStream(file_);
    }
    
    public void cleanUp()
    {
      if (temporary_) {
        FileUtils.deleteQuietly(file_);
      }
    }
    
    public boolean isEmpty()
    {
      return false;
    }
    
    protected void finalize() throws Throwable
    {
      super.finalize();
      cleanUp();
    }
    
    public long length()
    {
      if (file_ == null) {
        return 0L;
      }
      return file_.length();
    }
  }
}
