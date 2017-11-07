package org.xml.sax.helpers;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;
import org.xml.sax.AttributeList;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.DocumentHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

public class ParserAdapter
  implements XMLReader, DocumentHandler
{
  private static final String FEATURES = "http://xml.org/sax/features/";
  private static final String NAMESPACES = "http://xml.org/sax/features/namespaces";
  private static final String NAMESPACE_PREFIXES = "http://xml.org/sax/features/namespace-prefixes";
  private static final String XMLNS_URIs = "http://xml.org/sax/features/xmlns-uris";
  private NamespaceSupport nsSupport;
  private AttributeListAdapter attAdapter;
  private boolean parsing = false;
  private String[] nameParts = new String[3];
  private Parser parser = null;
  private AttributesImpl atts = null;
  private boolean namespaces = true;
  private boolean prefixes = false;
  private boolean uris = false;
  Locator locator;
  EntityResolver entityResolver = null;
  DTDHandler dtdHandler = null;
  ContentHandler contentHandler = null;
  ErrorHandler errorHandler = null;
  
  public ParserAdapter()
    throws SAXException
  {
    String str = System.getProperty("org.xml.sax.parser");
    try
    {
      setup(ParserFactory.makeParser());
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      throw new SAXException("Cannot find SAX1 driver class " + str, localClassNotFoundException);
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      throw new SAXException("SAX1 driver class " + str + " found but cannot be loaded", localIllegalAccessException);
    }
    catch (InstantiationException localInstantiationException)
    {
      throw new SAXException("SAX1 driver class " + str + " loaded but cannot be instantiated", localInstantiationException);
    }
    catch (ClassCastException localClassCastException)
    {
      throw new SAXException("SAX1 driver class " + str + " does not implement org.xml.sax.Parser");
    }
    catch (NullPointerException localNullPointerException)
    {
      throw new SAXException("System property org.xml.sax.parser not specified");
    }
  }
  
  public ParserAdapter(Parser paramParser)
  {
    setup(paramParser);
  }
  
  private void setup(Parser paramParser)
  {
    if (paramParser == null) {
      throw new NullPointerException("Parser argument must not be null");
    }
    parser = paramParser;
    atts = new AttributesImpl();
    nsSupport = new NamespaceSupport();
    attAdapter = new AttributeListAdapter();
  }
  
  public void setFeature(String paramString, boolean paramBoolean)
    throws SAXNotRecognizedException, SAXNotSupportedException
  {
    if (paramString.equals("http://xml.org/sax/features/namespaces"))
    {
      checkNotParsing("feature", paramString);
      namespaces = paramBoolean;
      if ((!namespaces) && (!prefixes)) {
        prefixes = true;
      }
    }
    else if (paramString.equals("http://xml.org/sax/features/namespace-prefixes"))
    {
      checkNotParsing("feature", paramString);
      prefixes = paramBoolean;
      if ((!prefixes) && (!namespaces)) {
        namespaces = true;
      }
    }
    else if (paramString.equals("http://xml.org/sax/features/xmlns-uris"))
    {
      checkNotParsing("feature", paramString);
      uris = paramBoolean;
    }
    else
    {
      throw new SAXNotRecognizedException("Feature: " + paramString);
    }
  }
  
  public boolean getFeature(String paramString)
    throws SAXNotRecognizedException, SAXNotSupportedException
  {
    if (paramString.equals("http://xml.org/sax/features/namespaces")) {
      return namespaces;
    }
    if (paramString.equals("http://xml.org/sax/features/namespace-prefixes")) {
      return prefixes;
    }
    if (paramString.equals("http://xml.org/sax/features/xmlns-uris")) {
      return uris;
    }
    throw new SAXNotRecognizedException("Feature: " + paramString);
  }
  
  public void setProperty(String paramString, Object paramObject)
    throws SAXNotRecognizedException, SAXNotSupportedException
  {
    throw new SAXNotRecognizedException("Property: " + paramString);
  }
  
  public Object getProperty(String paramString)
    throws SAXNotRecognizedException, SAXNotSupportedException
  {
    throw new SAXNotRecognizedException("Property: " + paramString);
  }
  
  public void setEntityResolver(EntityResolver paramEntityResolver)
  {
    entityResolver = paramEntityResolver;
  }
  
  public EntityResolver getEntityResolver()
  {
    return entityResolver;
  }
  
  public void setDTDHandler(DTDHandler paramDTDHandler)
  {
    dtdHandler = paramDTDHandler;
  }
  
  public DTDHandler getDTDHandler()
  {
    return dtdHandler;
  }
  
  public void setContentHandler(ContentHandler paramContentHandler)
  {
    contentHandler = paramContentHandler;
  }
  
  public ContentHandler getContentHandler()
  {
    return contentHandler;
  }
  
  public void setErrorHandler(ErrorHandler paramErrorHandler)
  {
    errorHandler = paramErrorHandler;
  }
  
  public ErrorHandler getErrorHandler()
  {
    return errorHandler;
  }
  
  public void parse(String paramString)
    throws IOException, SAXException
  {
    parse(new InputSource(paramString));
  }
  
  public void parse(InputSource paramInputSource)
    throws IOException, SAXException
  {
    if (parsing) {
      throw new SAXException("Parser is already in use");
    }
    setupParser();
    parsing = true;
    try
    {
      parser.parse(paramInputSource);
    }
    finally
    {
      parsing = false;
    }
    parsing = false;
  }
  
  public void setDocumentLocator(Locator paramLocator)
  {
    locator = paramLocator;
    if (contentHandler != null) {
      contentHandler.setDocumentLocator(paramLocator);
    }
  }
  
  public void startDocument()
    throws SAXException
  {
    if (contentHandler != null) {
      contentHandler.startDocument();
    }
  }
  
  public void endDocument()
    throws SAXException
  {
    if (contentHandler != null) {
      contentHandler.endDocument();
    }
  }
  
  public void startElement(String paramString, AttributeList paramAttributeList)
    throws SAXException
  {
    Vector localVector = null;
    if (!namespaces)
    {
      if (contentHandler != null)
      {
        attAdapter.setAttributeList(paramAttributeList);
        contentHandler.startElement("", "", paramString.intern(), attAdapter);
      }
      return;
    }
    nsSupport.pushContext();
    int i = paramAttributeList.getLength();
    String str2;
    String str4;
    for (int j = 0; j < i; j++)
    {
      String str1 = paramAttributeList.getName(j);
      if (str1.startsWith("xmlns"))
      {
        int n = str1.indexOf(':');
        if ((n == -1) && (str1.length() == 5))
        {
          str2 = "";
        }
        else
        {
          if (n != 5) {
            continue;
          }
          str2 = str1.substring(n + 1);
        }
        str4 = paramAttributeList.getValue(j);
        if (!nsSupport.declarePrefix(str2, str4)) {
          reportError("Illegal Namespace prefix: " + str2);
        } else if (contentHandler != null) {
          contentHandler.startPrefixMapping(str2, str4);
        }
      }
    }
    atts.clear();
    for (int k = 0; k < i; k++)
    {
      str2 = paramAttributeList.getName(k);
      String str3 = paramAttributeList.getType(k);
      str4 = paramAttributeList.getValue(k);
      Object localObject;
      if (str2.startsWith("xmlns"))
      {
        int i1 = str2.indexOf(':');
        if ((i1 == -1) && (str2.length() == 5)) {
          localObject = "";
        } else if (i1 != 5) {
          localObject = null;
        } else {
          localObject = str2.substring(6);
        }
        if (localObject != null)
        {
          if (!prefixes) {
            continue;
          }
          if (uris)
          {
            atts.addAttribute("http://www.w3.org/XML/1998/namespace", (String)localObject, str2.intern(), str3, str4);
            continue;
          }
          atts.addAttribute("", "", str2.intern(), str3, str4);
          continue;
        }
      }
      try
      {
        localObject = processName(str2, true, true);
        atts.addAttribute(localObject[0], localObject[1], localObject[2], str3, str4);
      }
      catch (SAXException localSAXException)
      {
        if (localVector == null) {
          localVector = new Vector();
        }
        localVector.addElement(localSAXException);
        atts.addAttribute("", str2, str2, str3, str4);
      }
    }
    if ((localVector != null) && (errorHandler != null)) {
      for (int m = 0; m < localVector.size(); m++) {
        errorHandler.error((SAXParseException)localVector.elementAt(m));
      }
    }
    if (contentHandler != null)
    {
      String[] arrayOfString = processName(paramString, false, false);
      contentHandler.startElement(arrayOfString[0], arrayOfString[1], arrayOfString[2], atts);
    }
  }
  
  public void endElement(String paramString)
    throws SAXException
  {
    if (!namespaces)
    {
      if (contentHandler != null) {
        contentHandler.endElement("", "", paramString.intern());
      }
      return;
    }
    String[] arrayOfString = processName(paramString, false, false);
    if (contentHandler != null)
    {
      contentHandler.endElement(arrayOfString[0], arrayOfString[1], arrayOfString[2]);
      Enumeration localEnumeration = nsSupport.getDeclaredPrefixes();
      while (localEnumeration.hasMoreElements())
      {
        String str = (String)localEnumeration.nextElement();
        contentHandler.endPrefixMapping(str);
      }
    }
    nsSupport.popContext();
  }
  
  public void characters(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws SAXException
  {
    if (contentHandler != null) {
      contentHandler.characters(paramArrayOfChar, paramInt1, paramInt2);
    }
  }
  
  public void ignorableWhitespace(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws SAXException
  {
    if (contentHandler != null) {
      contentHandler.ignorableWhitespace(paramArrayOfChar, paramInt1, paramInt2);
    }
  }
  
  public void processingInstruction(String paramString1, String paramString2)
    throws SAXException
  {
    if (contentHandler != null) {
      contentHandler.processingInstruction(paramString1, paramString2);
    }
  }
  
  private void setupParser()
  {
    if ((!prefixes) && (!namespaces)) {
      throw new IllegalStateException();
    }
    nsSupport.reset();
    if (uris) {
      nsSupport.setNamespaceDeclUris(true);
    }
    if (entityResolver != null) {
      parser.setEntityResolver(entityResolver);
    }
    if (dtdHandler != null) {
      parser.setDTDHandler(dtdHandler);
    }
    if (errorHandler != null) {
      parser.setErrorHandler(errorHandler);
    }
    parser.setDocumentHandler(this);
    locator = null;
  }
  
  private String[] processName(String paramString, boolean paramBoolean1, boolean paramBoolean2)
    throws SAXException
  {
    String[] arrayOfString = nsSupport.processName(paramString, nameParts, paramBoolean1);
    if (arrayOfString == null)
    {
      if (paramBoolean2) {
        throw makeException("Undeclared prefix: " + paramString);
      }
      reportError("Undeclared prefix: " + paramString);
      arrayOfString = new String[3];
      String tmp85_83 = "";
      arrayOfString[1] = tmp85_83;
      arrayOfString[0] = tmp85_83;
      arrayOfString[2] = paramString.intern();
    }
    return arrayOfString;
  }
  
  void reportError(String paramString)
    throws SAXException
  {
    if (errorHandler != null) {
      errorHandler.error(makeException(paramString));
    }
  }
  
  private SAXParseException makeException(String paramString)
  {
    if (locator != null) {
      return new SAXParseException(paramString, locator);
    }
    return new SAXParseException(paramString, null, null, -1, -1);
  }
  
  private void checkNotParsing(String paramString1, String paramString2)
    throws SAXNotSupportedException
  {
    if (parsing) {
      throw new SAXNotSupportedException("Cannot change " + paramString1 + ' ' + paramString2 + " while parsing");
    }
  }
  
  final class AttributeListAdapter
    implements Attributes
  {
    private AttributeList qAtts;
    
    AttributeListAdapter() {}
    
    void setAttributeList(AttributeList paramAttributeList)
    {
      qAtts = paramAttributeList;
    }
    
    public int getLength()
    {
      return qAtts.getLength();
    }
    
    public String getURI(int paramInt)
    {
      return "";
    }
    
    public String getLocalName(int paramInt)
    {
      return "";
    }
    
    public String getQName(int paramInt)
    {
      return qAtts.getName(paramInt).intern();
    }
    
    public String getType(int paramInt)
    {
      return qAtts.getType(paramInt).intern();
    }
    
    public String getValue(int paramInt)
    {
      return qAtts.getValue(paramInt);
    }
    
    public int getIndex(String paramString1, String paramString2)
    {
      return -1;
    }
    
    public int getIndex(String paramString)
    {
      int i = atts.getLength();
      for (int j = 0; j < i; j++) {
        if (qAtts.getName(j).equals(paramString)) {
          return j;
        }
      }
      return -1;
    }
    
    public String getType(String paramString1, String paramString2)
    {
      return null;
    }
    
    public String getType(String paramString)
    {
      return qAtts.getType(paramString).intern();
    }
    
    public String getValue(String paramString1, String paramString2)
    {
      return null;
    }
    
    public String getValue(String paramString)
    {
      return qAtts.getValue(paramString);
    }
  }
}
