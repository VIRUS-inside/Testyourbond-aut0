package org.apache.xerces.dom;

import java.io.OutputStream;
import java.io.Writer;
import org.w3c.dom.ls.LSOutput;

public class DOMOutputImpl
  implements LSOutput
{
  protected Writer fCharStream = null;
  protected OutputStream fByteStream = null;
  protected String fSystemId = null;
  protected String fEncoding = null;
  
  public DOMOutputImpl() {}
  
  public Writer getCharacterStream()
  {
    return fCharStream;
  }
  
  public void setCharacterStream(Writer paramWriter)
  {
    fCharStream = paramWriter;
  }
  
  public OutputStream getByteStream()
  {
    return fByteStream;
  }
  
  public void setByteStream(OutputStream paramOutputStream)
  {
    fByteStream = paramOutputStream;
  }
  
  public String getSystemId()
  {
    return fSystemId;
  }
  
  public void setSystemId(String paramString)
  {
    fSystemId = paramString;
  }
  
  public String getEncoding()
  {
    return fEncoding;
  }
  
  public void setEncoding(String paramString)
  {
    fEncoding = paramString;
  }
}
