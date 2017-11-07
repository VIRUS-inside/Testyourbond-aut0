package org.apache.xml.serializer.dom3;

import java.io.OutputStream;
import java.io.Writer;
import org.w3c.dom.ls.LSOutput;






















































final class DOMOutputImpl
  implements LSOutput
{
  private Writer fCharStream = null;
  private OutputStream fByteStream = null;
  private String fSystemId = null;
  private String fEncoding = null;
  





  DOMOutputImpl() {}
  





  public Writer getCharacterStream()
  {
    return fCharStream;
  }
  







  public void setCharacterStream(Writer characterStream)
  {
    fCharStream = characterStream;
  }
  







  public OutputStream getByteStream()
  {
    return fByteStream;
  }
  







  public void setByteStream(OutputStream byteStream)
  {
    fByteStream = byteStream;
  }
  








  public String getSystemId()
  {
    return fSystemId;
  }
  








  public void setSystemId(String systemId)
  {
    fSystemId = systemId;
  }
  










  public String getEncoding()
  {
    return fEncoding;
  }
  










  public void setEncoding(String encoding)
  {
    fEncoding = encoding;
  }
}
