package org.openqa.selenium;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
























public abstract interface OutputType<T>
{
  public static final OutputType<String> BASE64 = new OutputType() {
    public String convertFromBase64Png(String base64Png) {
      return base64Png;
    }
    
    public String convertFromPngBytes(byte[] png) {
      return Base64.getEncoder().encodeToString(png);
    }
    
    public String toString() {
      return "OutputType.BASE64";
    }
  };
  



  public static final OutputType<byte[]> BYTES = new OutputType() {
    public byte[] convertFromBase64Png(String base64Png) {
      return Base64.getMimeDecoder().decode(base64Png);
    }
    
    public byte[] convertFromPngBytes(byte[] png) {
      return png;
    }
    
    public String toString() {
      return "OutputType.BYTES";
    }
  };
  




  public static final OutputType<File> FILE = new OutputType() {
    public File convertFromBase64Png(String base64Png) {
      return save((byte[])BYTES.convertFromBase64Png(base64Png));
    }
    
    public File convertFromPngBytes(byte[] data) {
      return save(data);
    }
    
    private File save(byte[] data) {
      OutputStream stream = null;
      try
      {
        File tmpFile = File.createTempFile("screenshot", ".png");
        tmpFile.deleteOnExit();
        
        stream = new FileOutputStream(tmpFile);
        stream.write(data);
        
        return tmpFile;
      } catch (IOException e) {
        throw new WebDriverException(e);
      } finally {
        if (stream != null) {
          try {
            stream.close();
          }
          catch (IOException localIOException2) {}
        }
      }
    }
    
    public String toString()
    {
      return "OutputType.FILE";
    }
  };
  
  public abstract T convertFromBase64Png(String paramString);
  
  public abstract T convertFromPngBytes(byte[] paramArrayOfByte);
}
