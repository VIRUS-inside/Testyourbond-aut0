package com.gargoylesoftware.htmlunit.util;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;


































public class KeyDataPair
  extends NameValuePair
{
  private final File fileObject_;
  private final String fileName_;
  private final String mimeType_;
  private transient Charset charset_;
  private byte[] data_;
  
  public KeyDataPair(String key, File file, String fileName, String mimeType, String charset)
  {
    this(key, file, fileName, mimeType, Charset.forName(charset));
  }
  










  public KeyDataPair(String key, File file, String fileName, String mimeType, Charset charset)
  {
    super(key, file == null ? "" : file.getName());
    
    if ((file != null) && (file.exists())) {
      fileObject_ = file;
    }
    else {
      fileObject_ = null;
    }
    fileName_ = fileName;
    
    mimeType_ = mimeType;
    charset_ = charset;
  }
  






  public boolean equals(Object object)
  {
    return super.equals(object);
  }
  






  public int hashCode()
  {
    return super.hashCode();
  }
  


  public File getFile()
  {
    return fileObject_;
  }
  


  public String getFileName()
  {
    return fileName_;
  }
  



  public Charset getCharset()
  {
    return charset_;
  }
  



  public String getMimeType()
  {
    return mimeType_;
  }
  



  public byte[] getData()
  {
    return data_;
  }
  



  public void setData(byte[] data)
  {
    data_ = data;
  }
  
  private void writeObject(ObjectOutputStream oos) throws IOException {
    oos.defaultWriteObject();
    oos.writeObject(charset_ == null ? null : charset_.name());
  }
  
  private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
    ois.defaultReadObject();
    String charsetName = (String)ois.readObject();
    if (charsetName != null) {
      charset_ = Charset.forName(charsetName);
    }
  }
}
