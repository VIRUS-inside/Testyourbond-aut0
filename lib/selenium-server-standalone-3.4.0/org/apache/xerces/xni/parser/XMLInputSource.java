package org.apache.xerces.xni.parser;

import java.io.InputStream;
import java.io.Reader;
import org.apache.xerces.xni.XMLResourceIdentifier;

public class XMLInputSource
{
  protected String fPublicId;
  protected String fSystemId;
  protected String fBaseSystemId;
  protected InputStream fByteStream;
  protected Reader fCharStream;
  protected String fEncoding;
  
  public XMLInputSource(String paramString1, String paramString2, String paramString3)
  {
    fPublicId = paramString1;
    fSystemId = paramString2;
    fBaseSystemId = paramString3;
  }
  
  public XMLInputSource(XMLResourceIdentifier paramXMLResourceIdentifier)
  {
    fPublicId = paramXMLResourceIdentifier.getPublicId();
    fSystemId = paramXMLResourceIdentifier.getLiteralSystemId();
    fBaseSystemId = paramXMLResourceIdentifier.getBaseSystemId();
  }
  
  public XMLInputSource(String paramString1, String paramString2, String paramString3, InputStream paramInputStream, String paramString4)
  {
    fPublicId = paramString1;
    fSystemId = paramString2;
    fBaseSystemId = paramString3;
    fByteStream = paramInputStream;
    fEncoding = paramString4;
  }
  
  public XMLInputSource(String paramString1, String paramString2, String paramString3, Reader paramReader, String paramString4)
  {
    fPublicId = paramString1;
    fSystemId = paramString2;
    fBaseSystemId = paramString3;
    fCharStream = paramReader;
    fEncoding = paramString4;
  }
  
  public void setPublicId(String paramString)
  {
    fPublicId = paramString;
  }
  
  public String getPublicId()
  {
    return fPublicId;
  }
  
  public void setSystemId(String paramString)
  {
    fSystemId = paramString;
  }
  
  public String getSystemId()
  {
    return fSystemId;
  }
  
  public void setBaseSystemId(String paramString)
  {
    fBaseSystemId = paramString;
  }
  
  public String getBaseSystemId()
  {
    return fBaseSystemId;
  }
  
  public void setByteStream(InputStream paramInputStream)
  {
    fByteStream = paramInputStream;
  }
  
  public InputStream getByteStream()
  {
    return fByteStream;
  }
  
  public void setCharacterStream(Reader paramReader)
  {
    fCharStream = paramReader;
  }
  
  public Reader getCharacterStream()
  {
    return fCharStream;
  }
  
  public void setEncoding(String paramString)
  {
    fEncoding = paramString;
  }
  
  public String getEncoding()
  {
    return fEncoding;
  }
}
