package org.apache.xml.serialize;

import java.io.UnsupportedEncodingException;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLDocument;

/**
 * @deprecated
 */
public class OutputFormat
{
  private String _method;
  private String _version;
  private int _indent = 0;
  private String _encoding = "UTF-8";
  private EncodingInfo _encodingInfo = null;
  private boolean _allowJavaNames = false;
  private String _mediaType;
  private String _doctypeSystem;
  private String _doctypePublic;
  private boolean _omitXmlDeclaration = false;
  private boolean _omitDoctype = false;
  private boolean _omitComments = false;
  private boolean _standalone = false;
  private String[] _cdataElements;
  private String[] _nonEscapingElements;
  private String _lineSeparator = "\n";
  private int _lineWidth = 72;
  private boolean _preserve = false;
  private boolean _preserveEmptyAttributes = false;
  
  public OutputFormat() {}
  
  public OutputFormat(String paramString1, String paramString2, boolean paramBoolean)
  {
    setMethod(paramString1);
    setEncoding(paramString2);
    setIndenting(paramBoolean);
  }
  
  public OutputFormat(Document paramDocument)
  {
    setMethod(whichMethod(paramDocument));
    setDoctype(whichDoctypePublic(paramDocument), whichDoctypeSystem(paramDocument));
    setMediaType(whichMediaType(getMethod()));
  }
  
  public OutputFormat(Document paramDocument, String paramString, boolean paramBoolean)
  {
    this(paramDocument);
    setEncoding(paramString);
    setIndenting(paramBoolean);
  }
  
  public String getMethod()
  {
    return _method;
  }
  
  public void setMethod(String paramString)
  {
    _method = paramString;
  }
  
  public String getVersion()
  {
    return _version;
  }
  
  public void setVersion(String paramString)
  {
    _version = paramString;
  }
  
  public int getIndent()
  {
    return _indent;
  }
  
  public boolean getIndenting()
  {
    return _indent > 0;
  }
  
  public void setIndent(int paramInt)
  {
    if (paramInt < 0) {
      _indent = 0;
    } else {
      _indent = paramInt;
    }
  }
  
  public void setIndenting(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      _indent = 4;
      _lineWidth = 72;
    }
    else
    {
      _indent = 0;
      _lineWidth = 0;
    }
  }
  
  public String getEncoding()
  {
    return _encoding;
  }
  
  public void setEncoding(String paramString)
  {
    _encoding = paramString;
    _encodingInfo = null;
  }
  
  public void setEncoding(EncodingInfo paramEncodingInfo)
  {
    _encoding = paramEncodingInfo.getIANAName();
    _encodingInfo = paramEncodingInfo;
  }
  
  public EncodingInfo getEncodingInfo()
    throws UnsupportedEncodingException
  {
    if (_encodingInfo == null) {
      _encodingInfo = Encodings.getEncodingInfo(_encoding, _allowJavaNames);
    }
    return _encodingInfo;
  }
  
  public void setAllowJavaNames(boolean paramBoolean)
  {
    _allowJavaNames = paramBoolean;
  }
  
  public boolean setAllowJavaNames()
  {
    return _allowJavaNames;
  }
  
  public String getMediaType()
  {
    return _mediaType;
  }
  
  public void setMediaType(String paramString)
  {
    _mediaType = paramString;
  }
  
  public void setDoctype(String paramString1, String paramString2)
  {
    _doctypePublic = paramString1;
    _doctypeSystem = paramString2;
  }
  
  public String getDoctypePublic()
  {
    return _doctypePublic;
  }
  
  public String getDoctypeSystem()
  {
    return _doctypeSystem;
  }
  
  public boolean getOmitComments()
  {
    return _omitComments;
  }
  
  public void setOmitComments(boolean paramBoolean)
  {
    _omitComments = paramBoolean;
  }
  
  public boolean getOmitDocumentType()
  {
    return _omitDoctype;
  }
  
  public void setOmitDocumentType(boolean paramBoolean)
  {
    _omitDoctype = paramBoolean;
  }
  
  public boolean getOmitXMLDeclaration()
  {
    return _omitXmlDeclaration;
  }
  
  public void setOmitXMLDeclaration(boolean paramBoolean)
  {
    _omitXmlDeclaration = paramBoolean;
  }
  
  public boolean getStandalone()
  {
    return _standalone;
  }
  
  public void setStandalone(boolean paramBoolean)
  {
    _standalone = paramBoolean;
  }
  
  public String[] getCDataElements()
  {
    return _cdataElements;
  }
  
  public boolean isCDataElement(String paramString)
  {
    if (_cdataElements == null) {
      return false;
    }
    for (int i = 0; i < _cdataElements.length; i++) {
      if (_cdataElements[i].equals(paramString)) {
        return true;
      }
    }
    return false;
  }
  
  public void setCDataElements(String[] paramArrayOfString)
  {
    _cdataElements = paramArrayOfString;
  }
  
  public String[] getNonEscapingElements()
  {
    return _nonEscapingElements;
  }
  
  public boolean isNonEscapingElement(String paramString)
  {
    if (_nonEscapingElements == null) {
      return false;
    }
    for (int i = 0; i < _nonEscapingElements.length; i++) {
      if (_nonEscapingElements[i].equals(paramString)) {
        return true;
      }
    }
    return false;
  }
  
  public void setNonEscapingElements(String[] paramArrayOfString)
  {
    _nonEscapingElements = paramArrayOfString;
  }
  
  public String getLineSeparator()
  {
    return _lineSeparator;
  }
  
  public void setLineSeparator(String paramString)
  {
    if (paramString == null) {
      _lineSeparator = "\n";
    } else {
      _lineSeparator = paramString;
    }
  }
  
  public boolean getPreserveSpace()
  {
    return _preserve;
  }
  
  public void setPreserveSpace(boolean paramBoolean)
  {
    _preserve = paramBoolean;
  }
  
  public int getLineWidth()
  {
    return _lineWidth;
  }
  
  public void setLineWidth(int paramInt)
  {
    if (paramInt <= 0) {
      _lineWidth = 0;
    } else {
      _lineWidth = paramInt;
    }
  }
  
  public boolean getPreserveEmptyAttributes()
  {
    return _preserveEmptyAttributes;
  }
  
  public void setPreserveEmptyAttributes(boolean paramBoolean)
  {
    _preserveEmptyAttributes = paramBoolean;
  }
  
  public char getLastPrintable()
  {
    if ((getEncoding() != null) && (getEncoding().equalsIgnoreCase("ASCII"))) {
      return 'ÿ';
    }
    return 65535;
  }
  
  public static String whichMethod(Document paramDocument)
  {
    if ((paramDocument instanceof HTMLDocument)) {
      return "html";
    }
    for (Node localNode = paramDocument.getFirstChild(); localNode != null; localNode = localNode.getNextSibling())
    {
      if (localNode.getNodeType() == 1)
      {
        if (localNode.getNodeName().equalsIgnoreCase("html")) {
          return "html";
        }
        if (localNode.getNodeName().equalsIgnoreCase("root")) {
          return "fop";
        }
        return "xml";
      }
      if (localNode.getNodeType() == 3)
      {
        String str = localNode.getNodeValue();
        for (int i = 0; i < str.length(); i++) {
          if ((str.charAt(i) != ' ') && (str.charAt(i) != '\n') && (str.charAt(i) != '\t') && (str.charAt(i) != '\r')) {
            return "xml";
          }
        }
      }
    }
    return "xml";
  }
  
  public static String whichDoctypePublic(Document paramDocument)
  {
    DocumentType localDocumentType = paramDocument.getDoctype();
    if (localDocumentType != null) {
      try
      {
        return localDocumentType.getPublicId();
      }
      catch (Error localError) {}
    }
    if ((paramDocument instanceof HTMLDocument)) {
      return "-//W3C//DTD XHTML 1.0 Strict//EN";
    }
    return null;
  }
  
  public static String whichDoctypeSystem(Document paramDocument)
  {
    DocumentType localDocumentType = paramDocument.getDoctype();
    if (localDocumentType != null) {
      try
      {
        return localDocumentType.getSystemId();
      }
      catch (Error localError) {}
    }
    if ((paramDocument instanceof HTMLDocument)) {
      return "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd";
    }
    return null;
  }
  
  public static String whichMediaType(String paramString)
  {
    if (paramString.equalsIgnoreCase("xml")) {
      return "text/xml";
    }
    if (paramString.equalsIgnoreCase("html")) {
      return "text/html";
    }
    if (paramString.equalsIgnoreCase("xhtml")) {
      return "text/html";
    }
    if (paramString.equalsIgnoreCase("text")) {
      return "text/plain";
    }
    if (paramString.equalsIgnoreCase("fop")) {
      return "application/pdf";
    }
    return null;
  }
  
  /**
   * @deprecated
   */
  public static class Defaults
  {
    public static final int Indent = 4;
    public static final String Encoding = "UTF-8";
    public static final int LineWidth = 72;
    
    public Defaults() {}
  }
  
  /**
   * @deprecated
   */
  public static class DTD
  {
    public static final String HTMLPublicId = "-//W3C//DTD HTML 4.01//EN";
    public static final String HTMLSystemId = "http://www.w3.org/TR/html4/strict.dtd";
    public static final String XHTMLPublicId = "-//W3C//DTD XHTML 1.0 Strict//EN";
    public static final String XHTMLSystemId = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd";
    
    public DTD() {}
  }
}
