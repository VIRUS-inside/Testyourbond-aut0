package org.apache.xalan.xsltc.trax;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TemplatesHandler;
import javax.xml.transform.sax.TransformerHandler;
import org.apache.xalan.xsltc.compiler.SourceLoader;
import org.apache.xalan.xsltc.compiler.XSLTC;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.dom.XSLTCDTMManager;
import org.apache.xml.utils.StopParseException;
import org.apache.xml.utils.StylesheetPIHandler;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLFilter;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;




































public class TransformerFactoryImpl
  extends SAXTransformerFactory
  implements SourceLoader, ErrorListener
{
  public static final String TRANSLET_NAME = "translet-name";
  public static final String DESTINATION_DIRECTORY = "destination-directory";
  public static final String PACKAGE_NAME = "package-name";
  public static final String JAR_NAME = "jar-name";
  public static final String GENERATE_TRANSLET = "generate-translet";
  public static final String AUTO_TRANSLET = "auto-translet";
  public static final String USE_CLASSPATH = "use-classpath";
  public static final String DEBUG = "debug";
  public static final String ENABLE_INLINING = "enable-inlining";
  public static final String INDENT_NUMBER = "indent-number";
  private ErrorListener _errorListener = this;
  



  private URIResolver _uriResolver = null;
  







  protected static final String DEFAULT_TRANSLET_NAME = "GregorSamsa";
  






  private String _transletName = "GregorSamsa";
  



  private String _destinationDirectory = null;
  



  private String _packageName = null;
  



  private String _jarFileName = null;
  




  private Hashtable _piParams = null;
  


  private static class PIParamWrapper
  {
    public String _media = null;
    public String _title = null;
    public String _charset = null;
    
    public PIParamWrapper(String media, String title, String charset) {
      _media = media;
      _title = title;
      _charset = charset;
    }
  }
  



  private boolean _debug = false;
  



  private boolean _enableInlining = false;
  




  private boolean _generateTranslet = false;
  






  private boolean _autoTranslet = false;
  




  private boolean _useClasspath = false;
  



  private int _indentNumber = -1;
  





  private Class m_DTMManagerClass;
  




  private boolean _isSecureProcessing = false;
  


  public TransformerFactoryImpl()
  {
    m_DTMManagerClass = XSLTCDTMManager.getDTMManagerClass();
  }
  









  public void setErrorListener(ErrorListener listener)
    throws IllegalArgumentException
  {
    if (listener == null) {
      ErrorMsg err = new ErrorMsg("ERROR_LISTENER_NULL_ERR", "TransformerFactory");
      
      throw new IllegalArgumentException(err.toString());
    }
    _errorListener = listener;
  }
  





  public ErrorListener getErrorListener()
  {
    return _errorListener;
  }
  









  public Object getAttribute(String name)
    throws IllegalArgumentException
  {
    if (name.equals("translet-name")) {
      return _transletName;
    }
    if (name.equals("generate-translet")) {
      return _generateTranslet ? Boolean.TRUE : Boolean.FALSE;
    }
    if (name.equals("auto-translet")) {
      return _autoTranslet ? Boolean.TRUE : Boolean.FALSE;
    }
    if (name.equals("enable-inlining")) {
      if (_enableInlining) {
        return Boolean.TRUE;
      }
      return Boolean.FALSE;
    }
    

    ErrorMsg err = new ErrorMsg("JAXP_INVALID_ATTR_ERR", name);
    throw new IllegalArgumentException(err.toString());
  }
  










  public void setAttribute(String name, Object value)
    throws IllegalArgumentException
  {
    if ((name.equals("translet-name")) && ((value instanceof String))) {
      _transletName = ((String)value);
      return;
    }
    if ((name.equals("destination-directory")) && ((value instanceof String))) {
      _destinationDirectory = ((String)value);
      return;
    }
    if ((name.equals("package-name")) && ((value instanceof String))) {
      _packageName = ((String)value);
      return;
    }
    if ((name.equals("jar-name")) && ((value instanceof String))) {
      _jarFileName = ((String)value);
      return;
    }
    if (name.equals("generate-translet")) {
      if ((value instanceof Boolean)) {
        _generateTranslet = ((Boolean)value).booleanValue();
        return;
      }
      if ((value instanceof String)) {
        _generateTranslet = ((String)value).equalsIgnoreCase("true");
      }
      
    }
    else if (name.equals("auto-translet")) {
      if ((value instanceof Boolean)) {
        _autoTranslet = ((Boolean)value).booleanValue();
        return;
      }
      if ((value instanceof String)) {
        _autoTranslet = ((String)value).equalsIgnoreCase("true");
      }
      
    }
    else if (name.equals("use-classpath")) {
      if ((value instanceof Boolean)) {
        _useClasspath = ((Boolean)value).booleanValue();
        return;
      }
      if ((value instanceof String)) {
        _useClasspath = ((String)value).equalsIgnoreCase("true");
      }
      
    }
    else if (name.equals("debug")) {
      if ((value instanceof Boolean)) {
        _debug = ((Boolean)value).booleanValue();
        return;
      }
      if ((value instanceof String)) {
        _debug = ((String)value).equalsIgnoreCase("true");
      }
      
    }
    else if (name.equals("enable-inlining")) {
      if ((value instanceof Boolean)) {
        _enableInlining = ((Boolean)value).booleanValue();
        return;
      }
      if ((value instanceof String)) {
        _enableInlining = ((String)value).equalsIgnoreCase("true");
      }
      
    }
    else if (name.equals("indent-number")) {
      if ((value instanceof String)) {
        try {
          _indentNumber = Integer.parseInt((String)value);
          return;

        }
        catch (NumberFormatException e) {}

      }
      else if ((value instanceof Integer)) {
        _indentNumber = ((Integer)value).intValue();
        return;
      }
    }
    

    ErrorMsg err = new ErrorMsg("JAXP_INVALID_ATTR_ERR", name);
    
    throw new IllegalArgumentException(err.toString());
  }
  






















  public void setFeature(String name, boolean value)
    throws TransformerConfigurationException
  {
    if (name == null) {
      ErrorMsg err = new ErrorMsg("JAXP_SET_FEATURE_NULL_NAME");
      throw new NullPointerException(err.toString());
    }
    
    if (name.equals("http://javax.xml.XMLConstants/feature/secure-processing")) {
      _isSecureProcessing = value;
      
      return;
    }
    

    ErrorMsg err = new ErrorMsg("JAXP_UNSUPPORTED_FEATURE", name);
    throw new TransformerConfigurationException(err.toString());
  }
  










  public boolean getFeature(String name)
  {
    String[] features = { "http://javax.xml.transform.dom.DOMSource/feature", "http://javax.xml.transform.dom.DOMResult/feature", "http://javax.xml.transform.sax.SAXSource/feature", "http://javax.xml.transform.sax.SAXResult/feature", "http://javax.xml.transform.stream.StreamSource/feature", "http://javax.xml.transform.stream.StreamResult/feature", "http://javax.xml.transform.sax.SAXTransformerFactory/feature", "http://javax.xml.transform.sax.SAXTransformerFactory/feature/xmlfilter" };
    










    if (name == null) {
      ErrorMsg err = new ErrorMsg("JAXP_GET_FEATURE_NULL_NAME");
      throw new NullPointerException(err.toString());
    }
    

    for (int i = 0; i < features.length; i++) {
      if (name.equals(features[i])) {
        return true;
      }
    }
    
    if (name.equals("http://javax.xml.XMLConstants/feature/secure-processing")) {
      return _isSecureProcessing;
    }
    

    return false;
  }
  







  public URIResolver getURIResolver()
  {
    return _uriResolver;
  }
  









  public void setURIResolver(URIResolver resolver)
  {
    _uriResolver = resolver;
  }
  

















  public Source getAssociatedStylesheet(Source source, String media, String title, String charset)
    throws TransformerConfigurationException
  {
    XMLReader reader = null;
    InputSource isource = null;
    




    StylesheetPIHandler _stylesheetPIHandler = new StylesheetPIHandler(null, media, title, charset);
    
    try
    {
      if ((source instanceof DOMSource)) {
        DOMSource domsrc = (DOMSource)source;
        String baseId = domsrc.getSystemId();
        Node node = domsrc.getNode();
        DOM2SAX dom2sax = new DOM2SAX(node);
        
        _stylesheetPIHandler.setBaseId(baseId);
        
        dom2sax.setContentHandler(_stylesheetPIHandler);
        dom2sax.parse();
      } else {
        isource = SAXSource.sourceToInputSource(source);
        String baseId = isource.getSystemId();
        
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        
        if (_isSecureProcessing) {
          try {
            factory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
          }
          catch (SAXException e) {}
        }
        
        SAXParser jaxpParser = factory.newSAXParser();
        
        reader = jaxpParser.getXMLReader();
        if (reader == null) {
          reader = XMLReaderFactory.createXMLReader();
        }
        
        _stylesheetPIHandler.setBaseId(baseId);
        reader.setContentHandler(_stylesheetPIHandler);
        reader.parse(isource);
      }
      

      if (_uriResolver != null) {
        _stylesheetPIHandler.setURIResolver(_uriResolver);
      }
      

    }
    catch (StopParseException e) {}catch (ParserConfigurationException e)
    {

      throw new TransformerConfigurationException("getAssociatedStylesheets failed", e);

    }
    catch (SAXException se)
    {
      throw new TransformerConfigurationException("getAssociatedStylesheets failed", se);

    }
    catch (IOException ioe)
    {
      throw new TransformerConfigurationException("getAssociatedStylesheets failed", ioe);
    }
    


    return _stylesheetPIHandler.getAssociatedStylesheet();
  }
  








  public Transformer newTransformer()
    throws TransformerConfigurationException
  {
    TransformerImpl result = new TransformerImpl(new Properties(), _indentNumber, this);
    
    if (_uriResolver != null) {
      result.setURIResolver(_uriResolver);
    }
    
    if (_isSecureProcessing) {
      result.setSecureProcessing(true);
    }
    return result;
  }
  










  public Transformer newTransformer(Source source)
    throws TransformerConfigurationException
  {
    Templates templates = newTemplates(source);
    Transformer transformer = templates.newTransformer();
    if (_uriResolver != null) {
      transformer.setURIResolver(_uriResolver);
    }
    return transformer;
  }
  



  private void passWarningsToListener(Vector messages)
    throws TransformerException
  {
    if ((_errorListener == null) || (messages == null)) {
      return;
    }
    
    int count = messages.size();
    for (int pos = 0; pos < count; pos++) {
      ErrorMsg msg = (ErrorMsg)messages.elementAt(pos);
      
      if (msg.isWarningError()) {
        _errorListener.error(new TransformerConfigurationException(msg.toString()));
      }
      else {
        _errorListener.warning(new TransformerConfigurationException(msg.toString()));
      }
    }
  }
  

  private void passErrorsToListener(Vector messages)
  {
    try
    {
      if ((_errorListener == null) || (messages == null)) {
        return;
      }
      
      int count = messages.size();
      for (int pos = 0; pos < count; pos++) {
        String message = messages.elementAt(pos).toString();
        _errorListener.error(new TransformerException(message));
      }
    }
    catch (TransformerException e) {}
  }
  














  public Templates newTemplates(Source source)
    throws TransformerConfigurationException
  {
    if (_useClasspath) {
      String transletName = getTransletBaseName(source);
      
      if (_packageName != null) {
        transletName = _packageName + "." + transletName;
      }
      try {
        Class clazz = ObjectFactory.findProviderClass(transletName, ObjectFactory.findClassLoader(), true);
        
        resetTransientAttributes();
        
        return new TemplatesImpl(new Class[] { clazz }, transletName, null, _indentNumber, this);
      }
      catch (ClassNotFoundException cnfe) {
        ErrorMsg err = new ErrorMsg("CLASS_NOT_FOUND_ERR", transletName);
        throw new TransformerConfigurationException(err.toString());
      }
      catch (Exception e) {
        ErrorMsg err = new ErrorMsg(new ErrorMsg("RUNTIME_ERROR_KEY") + e.getMessage());
        

        throw new TransformerConfigurationException(err.toString());
      }
    }
    


    if (_autoTranslet) {
      byte[][] bytecodes = (byte[][])null;
      String transletClassName = getTransletBaseName(source);
      
      if (_packageName != null) {
        transletClassName = _packageName + "." + transletClassName;
      }
      if (_jarFileName != null) {
        bytecodes = getBytecodesFromJar(source, transletClassName);
      } else {
        bytecodes = getBytecodesFromClasses(source, transletClassName);
      }
      if (bytecodes != null) {
        if (_debug) {
          if (_jarFileName != null) {
            System.err.println(new ErrorMsg("TRANSFORM_WITH_JAR_STR", transletClassName, _jarFileName));
          }
          else {
            System.err.println(new ErrorMsg("TRANSFORM_WITH_TRANSLET_STR", transletClassName));
          }
        }
        


        resetTransientAttributes();
        
        return new TemplatesImpl(bytecodes, transletClassName, null, _indentNumber, this);
      }
    }
    

    XSLTC xsltc = new XSLTC();
    if (_debug) xsltc.setDebug(true);
    if (_enableInlining) {
      xsltc.setTemplateInlining(true);
    } else
      xsltc.setTemplateInlining(false);
    if (_isSecureProcessing) xsltc.setSecureProcessing(true);
    xsltc.init();
    

    if (_uriResolver != null) {
      xsltc.setSourceLoader(this);
    }
    


    if ((_piParams != null) && (_piParams.get(source) != null))
    {
      PIParamWrapper p = (PIParamWrapper)_piParams.get(source);
      
      if (p != null) {
        xsltc.setPIParameters(_media, _title, _charset);
      }
    }
    

    int outputType = 2;
    if ((_generateTranslet) || (_autoTranslet))
    {
      xsltc.setClassName(getTransletBaseName(source));
      
      if (_destinationDirectory != null) {
        xsltc.setDestDirectory(_destinationDirectory);
      } else {
        String xslName = getStylesheetFileName(source);
        if (xslName != null) {
          File xslFile = new File(xslName);
          String xslDir = xslFile.getParent();
          
          if (xslDir != null) {
            xsltc.setDestDirectory(xslDir);
          }
        }
      }
      if (_packageName != null) {
        xsltc.setPackageName(_packageName);
      }
      if (_jarFileName != null) {
        xsltc.setJarFileName(_jarFileName);
        outputType = 5;
      }
      else {
        outputType = 4;
      }
    }
    
    InputSource input = Util.getInputSource(xsltc, source);
    byte[][] bytecodes = xsltc.compile(null, input, outputType);
    String transletName = xsltc.getClassName();
    

    if (((_generateTranslet) || (_autoTranslet)) && (bytecodes != null) && (_jarFileName != null)) {
      try
      {
        xsltc.outputToJar();
      }
      catch (IOException e) {}
    }
    


    resetTransientAttributes();
    

    if (_errorListener != this) {
      try {
        passWarningsToListener(xsltc.getWarnings());
      }
      catch (TransformerException e) {
        throw new TransformerConfigurationException(e);
      }
      
    } else {
      xsltc.printWarnings();
    }
    

    if (bytecodes == null)
    {
      ErrorMsg err = new ErrorMsg("JAXP_COMPILE_ERR");
      TransformerConfigurationException exc = new TransformerConfigurationException(err.toString());
      

      if (_errorListener != null) {
        passErrorsToListener(xsltc.getErrors());
        


        try
        {
          _errorListener.fatalError(exc);
        }
        catch (TransformerException te) {}
      }
      else
      {
        xsltc.printErrors();
      }
      throw exc;
    }
    
    return new TemplatesImpl(bytecodes, transletName, xsltc.getOutputProperties(), _indentNumber, this);
  }
  









  public TemplatesHandler newTemplatesHandler()
    throws TransformerConfigurationException
  {
    TemplatesHandlerImpl handler = new TemplatesHandlerImpl(_indentNumber, this);
    
    if (_uriResolver != null) {
      handler.setURIResolver(_uriResolver);
    }
    return handler;
  }
  








  public TransformerHandler newTransformerHandler()
    throws TransformerConfigurationException
  {
    Transformer transformer = newTransformer();
    if (_uriResolver != null) {
      transformer.setURIResolver(_uriResolver);
    }
    return new TransformerHandlerImpl((TransformerImpl)transformer);
  }
  










  public TransformerHandler newTransformerHandler(Source src)
    throws TransformerConfigurationException
  {
    Transformer transformer = newTransformer(src);
    if (_uriResolver != null) {
      transformer.setURIResolver(_uriResolver);
    }
    return new TransformerHandlerImpl((TransformerImpl)transformer);
  }
  










  public TransformerHandler newTransformerHandler(Templates templates)
    throws TransformerConfigurationException
  {
    Transformer transformer = templates.newTransformer();
    TransformerImpl internal = (TransformerImpl)transformer;
    return new TransformerHandlerImpl(internal);
  }
  









  public XMLFilter newXMLFilter(Source src)
    throws TransformerConfigurationException
  {
    Templates templates = newTemplates(src);
    if (templates == null) return null;
    return newXMLFilter(templates);
  }
  








  public XMLFilter newXMLFilter(Templates templates)
    throws TransformerConfigurationException
  {
    try
    {
      return new TrAXFilter(templates);
    }
    catch (TransformerConfigurationException e1) {
      if (_errorListener != null) {
        try {
          _errorListener.fatalError(e1);
          return null;
        }
        catch (TransformerException e2) {
          new TransformerConfigurationException(e2);
        }
      }
      throw e1;
    }
  }
  











  public void error(TransformerException e)
    throws TransformerException
  {
    Throwable wrapped = e.getException();
    if (wrapped != null) {
      System.err.println(new ErrorMsg("ERROR_PLUS_WRAPPED_MSG", e.getMessageAndLocation(), wrapped.getMessage()));
    }
    else
    {
      System.err.println(new ErrorMsg("ERROR_MSG", e.getMessageAndLocation()));
    }
    
    throw e;
  }
  













  public void fatalError(TransformerException e)
    throws TransformerException
  {
    Throwable wrapped = e.getException();
    if (wrapped != null) {
      System.err.println(new ErrorMsg("FATAL_ERR_PLUS_WRAPPED_MSG", e.getMessageAndLocation(), wrapped.getMessage()));
    }
    else
    {
      System.err.println(new ErrorMsg("FATAL_ERR_MSG", e.getMessageAndLocation()));
    }
    
    throw e;
  }
  













  public void warning(TransformerException e)
    throws TransformerException
  {
    Throwable wrapped = e.getException();
    if (wrapped != null) {
      System.err.println(new ErrorMsg("WARNING_PLUS_WRAPPED_MSG", e.getMessageAndLocation(), wrapped.getMessage()));
    }
    else
    {
      System.err.println(new ErrorMsg("WARNING_MSG", e.getMessageAndLocation()));
    }
  }
  








  public InputSource loadSource(String href, String context, XSLTC xsltc)
  {
    try
    {
      if (_uriResolver != null) {
        Source source = _uriResolver.resolve(href, context);
        if (source != null) {
          return Util.getInputSource(xsltc, source);
        }
      }
    }
    catch (TransformerException e) {}
    

    return null;
  }
  


  private void resetTransientAttributes()
  {
    _transletName = "GregorSamsa";
    _destinationDirectory = null;
    _packageName = null;
    _jarFileName = null;
  }
  








  private byte[][] getBytecodesFromClasses(Source source, String fullClassName)
  {
    if (fullClassName == null) {
      return (byte[][])null;
    }
    String xslFileName = getStylesheetFileName(source);
    File xslFile = null;
    if (xslFileName != null) {
      xslFile = new File(xslFileName);
    }
    

    int lastDotIndex = fullClassName.lastIndexOf('.');
    String transletName; String transletName; if (lastDotIndex > 0) {
      transletName = fullClassName.substring(lastDotIndex + 1);
    } else {
      transletName = fullClassName;
    }
    
    String transletPath = fullClassName.replace('.', '/');
    if (_destinationDirectory != null) {
      transletPath = _destinationDirectory + "/" + transletPath + ".class";

    }
    else if ((xslFile != null) && (xslFile.getParent() != null)) {
      transletPath = xslFile.getParent() + "/" + transletPath + ".class";
    } else {
      transletPath = transletPath + ".class";
    }
    

    File transletFile = new File(transletPath);
    if (!transletFile.exists()) {
      return (byte[][])null;
    }
    



    if ((xslFile != null) && (xslFile.exists())) {
      long xslTimestamp = xslFile.lastModified();
      long transletTimestamp = transletFile.lastModified();
      if (transletTimestamp < xslTimestamp) {
        return (byte[][])null;
      }
    }
    
    List bytecodes = new ArrayList();
    int fileLength = (int)transletFile.length();
    if (fileLength > 0) {
      FileInputStream input = null;
      try {
        input = new FileInputStream(transletFile);
      }
      catch (FileNotFoundException e) {
        return (byte[][])null;
      }
      
      byte[] bytes = new byte[fileLength];
      try {
        readFromInputStream(bytes, input, fileLength);
        input.close();
      }
      catch (IOException e) {
        return (byte[][])null;
      }
      
      bytecodes.add(bytes);
    }
    else {
      return (byte[][])null;
    }
    
    String transletParentDir = transletFile.getParent();
    if (transletParentDir == null) {
      transletParentDir = System.getProperty("user.dir");
    }
    File transletParentFile = new File(transletParentDir);
    

    String transletAuxPrefix = transletName + "$";
    File[] auxfiles = transletParentFile.listFiles(new FilenameFilter() {
      private final String val$transletAuxPrefix;
      
      public boolean accept(File dir, String name) { return (name.endsWith(".class")) && (name.startsWith(val$transletAuxPrefix)); }
    });
    


    for (int i = 0; i < auxfiles.length; i++)
    {
      File auxfile = auxfiles[i];
      int auxlength = (int)auxfile.length();
      if (auxlength > 0) {
        FileInputStream auxinput = null;
        try {
          auxinput = new FileInputStream(auxfile);
        }
        catch (FileNotFoundException e)
        {
          continue;
        }
        byte[] bytes = new byte[auxlength];
        try
        {
          readFromInputStream(bytes, auxinput, auxlength);
          auxinput.close();
        }
        catch (IOException e)
        {
          continue;
        }
        bytecodes.add(bytes);
      }
    }
    

    int count = bytecodes.size();
    if (count > 0) {
      byte[][] result = new byte[count][1];
      for (int i = 0; i < count; i++) {
        result[i] = ((byte[])(byte[])bytecodes.get(i));
      }
      
      return result;
    }
    
    return (byte[][])null;
  }
  







  private byte[][] getBytecodesFromJar(Source source, String fullClassName)
  {
    String xslFileName = getStylesheetFileName(source);
    File xslFile = null;
    if (xslFileName != null) {
      xslFile = new File(xslFileName);
    }
    
    String jarPath = null;
    if (_destinationDirectory != null) {
      jarPath = _destinationDirectory + "/" + _jarFileName;
    }
    else if ((xslFile != null) && (xslFile.getParent() != null)) {
      jarPath = xslFile.getParent() + "/" + _jarFileName;
    } else {
      jarPath = _jarFileName;
    }
    

    File file = new File(jarPath);
    if (!file.exists()) {
      return (byte[][])null;
    }
    

    if ((xslFile != null) && (xslFile.exists())) {
      long xslTimestamp = xslFile.lastModified();
      long transletTimestamp = file.lastModified();
      if (transletTimestamp < xslTimestamp) {
        return (byte[][])null;
      }
    }
    
    ZipFile jarFile = null;
    try {
      jarFile = new ZipFile(file);
    }
    catch (IOException e) {
      return (byte[][])null;
    }
    
    String transletPath = fullClassName.replace('.', '/');
    String transletAuxPrefix = transletPath + "$";
    String transletFullName = transletPath + ".class";
    
    List bytecodes = new ArrayList();
    


    Enumeration entries = jarFile.entries();
    while (entries.hasMoreElements())
    {
      ZipEntry entry = (ZipEntry)entries.nextElement();
      String entryName = entry.getName();
      if ((entry.getSize() > 0L) && ((entryName.equals(transletFullName)) || ((entryName.endsWith(".class")) && (entryName.startsWith(transletAuxPrefix)))))
      {

        try
        {

          InputStream input = jarFile.getInputStream(entry);
          int size = (int)entry.getSize();
          byte[] bytes = new byte[size];
          readFromInputStream(bytes, input, size);
          input.close();
          bytecodes.add(bytes);
        }
        catch (IOException e) {
          return (byte[][])null;
        }
      }
    }
    

    int count = bytecodes.size();
    if (count > 0) {
      byte[][] result = new byte[count][1];
      for (int i = 0; i < count; i++) {
        result[i] = ((byte[])(byte[])bytecodes.get(i));
      }
      
      return result;
    }
    
    return (byte[][])null;
  }
  







  private void readFromInputStream(byte[] bytes, InputStream input, int size)
    throws IOException
  {
    int n = 0;
    int offset = 0;
    int length = size;
    while ((length > 0) && ((n = input.read(bytes, offset, length)) > 0)) {
      offset += n;
      length -= n;
    }
  }
  











  private String getTransletBaseName(Source source)
  {
    String transletBaseName = null;
    if (!_transletName.equals("GregorSamsa")) {
      return _transletName;
    }
    String systemId = source.getSystemId();
    if (systemId != null) {
      String baseName = Util.baseName(systemId);
      if (baseName != null) {
        baseName = Util.noExtName(baseName);
        transletBaseName = Util.toJavaName(baseName);
      }
    }
    

    return transletBaseName != null ? transletBaseName : "GregorSamsa";
  }
  







  private String getStylesheetFileName(Source source)
  {
    String systemId = source.getSystemId();
    if (systemId != null) {
      File file = new File(systemId);
      if (file.exists()) {
        return systemId;
      }
      URL url = null;
      try {
        url = new URL(systemId);
      }
      catch (MalformedURLException e) {
        return null;
      }
      
      if ("file".equals(url.getProtocol())) {
        return url.getFile();
      }
      return null;
    }
    

    return null;
  }
  


  protected Class getDTMManagerClass()
  {
    return m_DTMManagerClass;
  }
}
