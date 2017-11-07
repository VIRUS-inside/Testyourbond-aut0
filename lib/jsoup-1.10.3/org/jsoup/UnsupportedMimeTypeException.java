package org.jsoup;

import java.io.IOException;

public class UnsupportedMimeTypeException
  extends IOException
{
  private String mimeType;
  private String url;
  
  public UnsupportedMimeTypeException(String message, String mimeType, String url)
  {
    super(message);
    this.mimeType = mimeType;
    this.url = url;
  }
  
  public String getMimeType() {
    return mimeType;
  }
  
  public String getUrl() {
    return url;
  }
  
  public String toString()
  {
    return super.toString() + ". Mimetype=" + mimeType + ", URL=" + url;
  }
}
