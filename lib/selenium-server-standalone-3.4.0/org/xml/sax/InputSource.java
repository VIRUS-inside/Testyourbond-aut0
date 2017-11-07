package org.xml.sax;

import java.io.InputStream;
import java.io.Reader;

public class InputSource
{
  private String publicId;
  private String systemId;
  private InputStream byteStream;
  private String encoding;
  private Reader characterStream;
  
  public InputSource() {}
  
  public InputSource(String paramString)
  {
    setSystemId(paramString);
  }
  
  public InputSource(InputStream paramInputStream)
  {
    setByteStream(paramInputStream);
  }
  
  public InputSource(Reader paramReader)
  {
    setCharacterStream(paramReader);
  }
  
  public void setPublicId(String paramString)
  {
    publicId = paramString;
  }
  
  public String getPublicId()
  {
    return publicId;
  }
  
  public void setSystemId(String paramString)
  {
    systemId = paramString;
  }
  
  public String getSystemId()
  {
    return systemId;
  }
  
  public void setByteStream(InputStream paramInputStream)
  {
    byteStream = paramInputStream;
  }
  
  public InputStream getByteStream()
  {
    return byteStream;
  }
  
  public void setEncoding(String paramString)
  {
    encoding = paramString;
  }
  
  public String getEncoding()
  {
    return encoding;
  }
  
  public void setCharacterStream(Reader paramReader)
  {
    characterStream = paramReader;
  }
  
  public Reader getCharacterStream()
  {
    return characterStream;
  }
}
