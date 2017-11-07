package org.apache.xerces.util;

import java.io.InputStream;
import java.io.Reader;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

public final class SAXInputSource
  extends XMLInputSource
{
  private XMLReader fXMLReader;
  private InputSource fInputSource;
  
  public SAXInputSource()
  {
    this(null);
  }
  
  public SAXInputSource(InputSource paramInputSource)
  {
    this(null, paramInputSource);
  }
  
  public SAXInputSource(XMLReader paramXMLReader, InputSource paramInputSource)
  {
    super(paramInputSource != null ? paramInputSource.getPublicId() : null, paramInputSource != null ? paramInputSource.getSystemId() : null, null);
    if (paramInputSource != null)
    {
      setByteStream(paramInputSource.getByteStream());
      setCharacterStream(paramInputSource.getCharacterStream());
      setEncoding(paramInputSource.getEncoding());
    }
    fInputSource = paramInputSource;
    fXMLReader = paramXMLReader;
  }
  
  public void setXMLReader(XMLReader paramXMLReader)
  {
    fXMLReader = paramXMLReader;
  }
  
  public XMLReader getXMLReader()
  {
    return fXMLReader;
  }
  
  public void setInputSource(InputSource paramInputSource)
  {
    if (paramInputSource != null)
    {
      setPublicId(paramInputSource.getPublicId());
      setSystemId(paramInputSource.getSystemId());
      setByteStream(paramInputSource.getByteStream());
      setCharacterStream(paramInputSource.getCharacterStream());
      setEncoding(paramInputSource.getEncoding());
    }
    else
    {
      setPublicId(null);
      setSystemId(null);
      setByteStream(null);
      setCharacterStream(null);
      setEncoding(null);
    }
    fInputSource = paramInputSource;
  }
  
  public InputSource getInputSource()
  {
    return fInputSource;
  }
  
  public void setPublicId(String paramString)
  {
    super.setPublicId(paramString);
    if (fInputSource == null) {
      fInputSource = new InputSource();
    }
    fInputSource.setPublicId(paramString);
  }
  
  public void setSystemId(String paramString)
  {
    super.setSystemId(paramString);
    if (fInputSource == null) {
      fInputSource = new InputSource();
    }
    fInputSource.setSystemId(paramString);
  }
  
  public void setByteStream(InputStream paramInputStream)
  {
    super.setByteStream(paramInputStream);
    if (fInputSource == null) {
      fInputSource = new InputSource();
    }
    fInputSource.setByteStream(paramInputStream);
  }
  
  public void setCharacterStream(Reader paramReader)
  {
    super.setCharacterStream(paramReader);
    if (fInputSource == null) {
      fInputSource = new InputSource();
    }
    fInputSource.setCharacterStream(paramReader);
  }
  
  public void setEncoding(String paramString)
  {
    super.setEncoding(paramString);
    if (fInputSource == null) {
      fInputSource = new InputSource();
    }
    fInputSource.setEncoding(paramString);
  }
}
