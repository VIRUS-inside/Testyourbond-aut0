package org.xml.sax;

public class SAXParseException
  extends SAXException
{
  private String publicId;
  private String systemId;
  private int lineNumber;
  private int columnNumber;
  static final long serialVersionUID = -5651165872476709336L;
  
  public SAXParseException(String paramString, Locator paramLocator)
  {
    super(paramString);
    if (paramLocator != null) {
      init(paramLocator.getPublicId(), paramLocator.getSystemId(), paramLocator.getLineNumber(), paramLocator.getColumnNumber());
    } else {
      init(null, null, -1, -1);
    }
  }
  
  public SAXParseException(String paramString, Locator paramLocator, Exception paramException)
  {
    super(paramString, paramException);
    if (paramLocator != null) {
      init(paramLocator.getPublicId(), paramLocator.getSystemId(), paramLocator.getLineNumber(), paramLocator.getColumnNumber());
    } else {
      init(null, null, -1, -1);
    }
  }
  
  public SAXParseException(String paramString1, String paramString2, String paramString3, int paramInt1, int paramInt2)
  {
    super(paramString1);
    init(paramString2, paramString3, paramInt1, paramInt2);
  }
  
  public SAXParseException(String paramString1, String paramString2, String paramString3, int paramInt1, int paramInt2, Exception paramException)
  {
    super(paramString1, paramException);
    init(paramString2, paramString3, paramInt1, paramInt2);
  }
  
  private void init(String paramString1, String paramString2, int paramInt1, int paramInt2)
  {
    publicId = paramString1;
    systemId = paramString2;
    lineNumber = paramInt1;
    columnNumber = paramInt2;
  }
  
  public String getPublicId()
  {
    return publicId;
  }
  
  public String getSystemId()
  {
    return systemId;
  }
  
  public int getLineNumber()
  {
    return lineNumber;
  }
  
  public int getColumnNumber()
  {
    return columnNumber;
  }
}
