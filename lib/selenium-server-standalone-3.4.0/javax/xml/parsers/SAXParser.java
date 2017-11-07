package javax.xml.parsers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.validation.Schema;
import org.xml.sax.HandlerBase;
import org.xml.sax.InputSource;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public abstract class SAXParser
{
  private static final boolean DEBUG = false;
  
  protected SAXParser() {}
  
  public void reset()
  {
    throw new UnsupportedOperationException("This SAXParser, \"" + getClass().getName() + "\", does not support the reset functionality." + "  Specification \"" + getClass().getPackage().getSpecificationTitle() + "\"" + " version \"" + getClass().getPackage().getSpecificationVersion() + "\"");
  }
  
  public void parse(InputStream paramInputStream, HandlerBase paramHandlerBase)
    throws SAXException, IOException
  {
    if (paramInputStream == null) {
      throw new IllegalArgumentException("InputStream cannot be null");
    }
    InputSource localInputSource = new InputSource(paramInputStream);
    parse(localInputSource, paramHandlerBase);
  }
  
  public void parse(InputStream paramInputStream, HandlerBase paramHandlerBase, String paramString)
    throws SAXException, IOException
  {
    if (paramInputStream == null) {
      throw new IllegalArgumentException("InputStream cannot be null");
    }
    InputSource localInputSource = new InputSource(paramInputStream);
    localInputSource.setSystemId(paramString);
    parse(localInputSource, paramHandlerBase);
  }
  
  public void parse(InputStream paramInputStream, DefaultHandler paramDefaultHandler)
    throws SAXException, IOException
  {
    if (paramInputStream == null) {
      throw new IllegalArgumentException("InputStream cannot be null");
    }
    InputSource localInputSource = new InputSource(paramInputStream);
    parse(localInputSource, paramDefaultHandler);
  }
  
  public void parse(InputStream paramInputStream, DefaultHandler paramDefaultHandler, String paramString)
    throws SAXException, IOException
  {
    if (paramInputStream == null) {
      throw new IllegalArgumentException("InputStream cannot be null");
    }
    InputSource localInputSource = new InputSource(paramInputStream);
    localInputSource.setSystemId(paramString);
    parse(localInputSource, paramDefaultHandler);
  }
  
  public void parse(String paramString, HandlerBase paramHandlerBase)
    throws SAXException, IOException
  {
    if (paramString == null) {
      throw new IllegalArgumentException("uri cannot be null");
    }
    InputSource localInputSource = new InputSource(paramString);
    parse(localInputSource, paramHandlerBase);
  }
  
  public void parse(String paramString, DefaultHandler paramDefaultHandler)
    throws SAXException, IOException
  {
    if (paramString == null) {
      throw new IllegalArgumentException("uri cannot be null");
    }
    InputSource localInputSource = new InputSource(paramString);
    parse(localInputSource, paramDefaultHandler);
  }
  
  public void parse(File paramFile, HandlerBase paramHandlerBase)
    throws SAXException, IOException
  {
    if (paramFile == null) {
      throw new IllegalArgumentException("File cannot be null");
    }
    String str = FilePathToURI.filepath2URI(paramFile.getAbsolutePath());
    InputSource localInputSource = new InputSource(str);
    parse(localInputSource, paramHandlerBase);
  }
  
  public void parse(File paramFile, DefaultHandler paramDefaultHandler)
    throws SAXException, IOException
  {
    if (paramFile == null) {
      throw new IllegalArgumentException("File cannot be null");
    }
    String str = FilePathToURI.filepath2URI(paramFile.getAbsolutePath());
    InputSource localInputSource = new InputSource(str);
    parse(localInputSource, paramDefaultHandler);
  }
  
  public void parse(InputSource paramInputSource, HandlerBase paramHandlerBase)
    throws SAXException, IOException
  {
    if (paramInputSource == null) {
      throw new IllegalArgumentException("InputSource cannot be null");
    }
    Parser localParser = getParser();
    if (paramHandlerBase != null)
    {
      localParser.setDocumentHandler(paramHandlerBase);
      localParser.setEntityResolver(paramHandlerBase);
      localParser.setErrorHandler(paramHandlerBase);
      localParser.setDTDHandler(paramHandlerBase);
    }
    localParser.parse(paramInputSource);
  }
  
  public void parse(InputSource paramInputSource, DefaultHandler paramDefaultHandler)
    throws SAXException, IOException
  {
    if (paramInputSource == null) {
      throw new IllegalArgumentException("InputSource cannot be null");
    }
    XMLReader localXMLReader = getXMLReader();
    if (paramDefaultHandler != null)
    {
      localXMLReader.setContentHandler(paramDefaultHandler);
      localXMLReader.setEntityResolver(paramDefaultHandler);
      localXMLReader.setErrorHandler(paramDefaultHandler);
      localXMLReader.setDTDHandler(paramDefaultHandler);
    }
    localXMLReader.parse(paramInputSource);
  }
  
  public abstract Parser getParser()
    throws SAXException;
  
  public abstract XMLReader getXMLReader()
    throws SAXException;
  
  public abstract boolean isNamespaceAware();
  
  public abstract boolean isValidating();
  
  public abstract void setProperty(String paramString, Object paramObject)
    throws SAXNotRecognizedException, SAXNotSupportedException;
  
  public abstract Object getProperty(String paramString)
    throws SAXNotRecognizedException, SAXNotSupportedException;
  
  public Schema getSchema()
  {
    throw new UnsupportedOperationException("This parser does not support specification \"" + getClass().getPackage().getSpecificationTitle() + "\" version \"" + getClass().getPackage().getSpecificationVersion() + "\"");
  }
  
  public boolean isXIncludeAware()
  {
    throw new UnsupportedOperationException("This parser does not support specification \"" + getClass().getPackage().getSpecificationTitle() + "\" version \"" + getClass().getPackage().getSpecificationVersion() + "\"");
  }
}
