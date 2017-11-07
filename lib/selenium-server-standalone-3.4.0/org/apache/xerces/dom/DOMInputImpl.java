package org.apache.xerces.dom;

import java.io.InputStream;
import java.io.Reader;
import org.w3c.dom.ls.LSInput;

public class DOMInputImpl
  implements LSInput
{
  protected String fPublicId = null;
  protected String fSystemId = null;
  protected String fBaseSystemId = null;
  protected InputStream fByteStream = null;
  protected Reader fCharStream = null;
  protected String fData = null;
  protected String fEncoding = null;
  protected boolean fCertifiedText = false;
  
  public DOMInputImpl() {}
  
  public DOMInputImpl(String paramString1, String paramString2, String paramString3)
  {
    fPublicId = paramString1;
    fSystemId = paramString2;
    fBaseSystemId = paramString3;
  }
  
  public DOMInputImpl(String paramString1, String paramString2, String paramString3, InputStream paramInputStream, String paramString4)
  {
    fPublicId = paramString1;
    fSystemId = paramString2;
    fBaseSystemId = paramString3;
    fByteStream = paramInputStream;
    fEncoding = paramString4;
  }
  
  public DOMInputImpl(String paramString1, String paramString2, String paramString3, Reader paramReader, String paramString4)
  {
    fPublicId = paramString1;
    fSystemId = paramString2;
    fBaseSystemId = paramString3;
    fCharStream = paramReader;
    fEncoding = paramString4;
  }
  
  public DOMInputImpl(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
  {
    fPublicId = paramString1;
    fSystemId = paramString2;
    fBaseSystemId = paramString3;
    fData = paramString4;
    fEncoding = paramString5;
  }
  
  public InputStream getByteStream()
  {
    return fByteStream;
  }
  
  public void setByteStream(InputStream paramInputStream)
  {
    fByteStream = paramInputStream;
  }
  
  public Reader getCharacterStream()
  {
    return fCharStream;
  }
  
  public void setCharacterStream(Reader paramReader)
  {
    fCharStream = paramReader;
  }
  
  public String getStringData()
  {
    return fData;
  }
  
  public void setStringData(String paramString)
  {
    fData = paramString;
  }
  
  public String getEncoding()
  {
    return fEncoding;
  }
  
  public void setEncoding(String paramString)
  {
    fEncoding = paramString;
  }
  
  public String getPublicId()
  {
    return fPublicId;
  }
  
  public void setPublicId(String paramString)
  {
    fPublicId = paramString;
  }
  
  public String getSystemId()
  {
    return fSystemId;
  }
  
  public void setSystemId(String paramString)
  {
    fSystemId = paramString;
  }
  
  public String getBaseURI()
  {
    return fBaseSystemId;
  }
  
  public void setBaseURI(String paramString)
  {
    fBaseSystemId = paramString;
  }
  
  public boolean getCertifiedText()
  {
    return fCertifiedText;
  }
  
  public void setCertifiedText(boolean paramBoolean)
  {
    fCertifiedText = paramBoolean;
  }
}
