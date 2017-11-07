package org.apache.xerces.xni.parser;

import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XNIException;

public class XMLParseException
  extends XNIException
{
  static final long serialVersionUID = 1732959359448549967L;
  protected String fPublicId;
  protected String fLiteralSystemId;
  protected String fExpandedSystemId;
  protected String fBaseSystemId;
  protected int fLineNumber = -1;
  protected int fColumnNumber = -1;
  protected int fCharacterOffset = -1;
  
  public XMLParseException(XMLLocator paramXMLLocator, String paramString)
  {
    super(paramString);
    if (paramXMLLocator != null)
    {
      fPublicId = paramXMLLocator.getPublicId();
      fLiteralSystemId = paramXMLLocator.getLiteralSystemId();
      fExpandedSystemId = paramXMLLocator.getExpandedSystemId();
      fBaseSystemId = paramXMLLocator.getBaseSystemId();
      fLineNumber = paramXMLLocator.getLineNumber();
      fColumnNumber = paramXMLLocator.getColumnNumber();
      fCharacterOffset = paramXMLLocator.getCharacterOffset();
    }
  }
  
  public XMLParseException(XMLLocator paramXMLLocator, String paramString, Exception paramException)
  {
    super(paramString, paramException);
    if (paramXMLLocator != null)
    {
      fPublicId = paramXMLLocator.getPublicId();
      fLiteralSystemId = paramXMLLocator.getLiteralSystemId();
      fExpandedSystemId = paramXMLLocator.getExpandedSystemId();
      fBaseSystemId = paramXMLLocator.getBaseSystemId();
      fLineNumber = paramXMLLocator.getLineNumber();
      fColumnNumber = paramXMLLocator.getColumnNumber();
      fCharacterOffset = paramXMLLocator.getCharacterOffset();
    }
  }
  
  public String getPublicId()
  {
    return fPublicId;
  }
  
  public String getExpandedSystemId()
  {
    return fExpandedSystemId;
  }
  
  public String getLiteralSystemId()
  {
    return fLiteralSystemId;
  }
  
  public String getBaseSystemId()
  {
    return fBaseSystemId;
  }
  
  public int getLineNumber()
  {
    return fLineNumber;
  }
  
  public int getColumnNumber()
  {
    return fColumnNumber;
  }
  
  public int getCharacterOffset()
  {
    return fCharacterOffset;
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    if (fPublicId != null) {
      localStringBuffer.append(fPublicId);
    }
    localStringBuffer.append(':');
    if (fLiteralSystemId != null) {
      localStringBuffer.append(fLiteralSystemId);
    }
    localStringBuffer.append(':');
    if (fExpandedSystemId != null) {
      localStringBuffer.append(fExpandedSystemId);
    }
    localStringBuffer.append(':');
    if (fBaseSystemId != null) {
      localStringBuffer.append(fBaseSystemId);
    }
    localStringBuffer.append(':');
    localStringBuffer.append(fLineNumber);
    localStringBuffer.append(':');
    localStringBuffer.append(fColumnNumber);
    localStringBuffer.append(':');
    localStringBuffer.append(fCharacterOffset);
    localStringBuffer.append(':');
    String str = getMessage();
    if (str == null)
    {
      Exception localException = getException();
      if (localException != null) {
        str = localException.getMessage();
      }
    }
    if (str != null) {
      localStringBuffer.append(str);
    }
    return localStringBuffer.toString();
  }
}
