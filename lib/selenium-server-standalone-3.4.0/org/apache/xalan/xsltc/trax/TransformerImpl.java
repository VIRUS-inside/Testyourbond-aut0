package org.apache.xalan.xsltc.trax;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownServiceException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.DOMCache;
import org.apache.xalan.xsltc.StripFilter;
import org.apache.xalan.xsltc.Translet;
import org.apache.xalan.xsltc.TransletException;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.dom.DOMWSFilter;
import org.apache.xalan.xsltc.dom.SAXImpl;
import org.apache.xalan.xsltc.dom.XSLTCDTMManager;
import org.apache.xalan.xsltc.runtime.AbstractTranslet;
import org.apache.xalan.xsltc.runtime.Hashtable;
import org.apache.xalan.xsltc.runtime.MessageHandler;
import org.apache.xalan.xsltc.runtime.output.TransletOutputHandlerFactory;
import org.apache.xml.dtm.DTMWSFilter;
import org.apache.xml.serializer.OutputPropertiesFactory;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xml.utils.SystemIDResolver;
import org.apache.xml.utils.XMLReaderManager;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.LexicalHandler;


































public final class TransformerImpl
  extends Transformer
  implements DOMCache, ErrorListener
{
  private static final String EMPTY_STRING = "";
  private static final String NO_STRING = "no";
  private static final String YES_STRING = "yes";
  private static final String XML_STRING = "xml";
  private static final String LEXICAL_HANDLER_PROPERTY = "http://xml.org/sax/properties/lexical-handler";
  private static final String NAMESPACE_FEATURE = "http://xml.org/sax/features/namespaces";
  private AbstractTranslet _translet = null;
  



  private String _method = null;
  



  private String _encoding = null;
  



  private String _sourceSystemId = null;
  



  private ErrorListener _errorListener = this;
  



  private URIResolver _uriResolver = null;
  


  private Properties _properties;
  

  private Properties _propertiesClone;
  

  private TransletOutputHandlerFactory _tohFactory = null;
  



  private DOM _dom = null;
  




  private int _indentNumber;
  



  private TransformerFactoryImpl _tfactory = null;
  



  private OutputStream _ostream = null;
  




  private XSLTCDTMManager _dtmManager = null;
  



  private XMLReaderManager _readerManager = XMLReaderManager.getInstance();
  









  private boolean _isIdentity = false;
  



  private boolean _isSecureProcessing = false;
  





  private Hashtable _parameters = null;
  


  static class MessageHandler
    extends MessageHandler
  {
    private ErrorListener _errorListener;
    

    public MessageHandler(ErrorListener errorListener)
    {
      _errorListener = errorListener;
    }
    
    public void displayMessage(String msg) {
      if (_errorListener == null) {
        System.err.println(msg);
      } else {
        try
        {
          _errorListener.warning(new TransformerException(msg));
        }
        catch (TransformerException e) {}
      }
    }
  }
  



  protected TransformerImpl(Properties outputProperties, int indentNumber, TransformerFactoryImpl tfactory)
  {
    this(null, outputProperties, indentNumber, tfactory);
    _isIdentity = true;
  }
  


  protected TransformerImpl(Translet translet, Properties outputProperties, int indentNumber, TransformerFactoryImpl tfactory)
  {
    _translet = ((AbstractTranslet)translet);
    _properties = createOutputProperties(outputProperties);
    _propertiesClone = ((Properties)_properties.clone());
    _indentNumber = indentNumber;
    _tfactory = tfactory;
  }
  



  public boolean isSecureProcessing()
  {
    return _isSecureProcessing;
  }
  


  public void setSecureProcessing(boolean flag)
  {
    _isSecureProcessing = flag;
  }
  



  protected AbstractTranslet getTranslet()
  {
    return _translet;
  }
  
  public boolean isIdentity() {
    return _isIdentity;
  }
  







  public void transform(Source source, Result result)
    throws TransformerException
  {
    if (!_isIdentity) {
      if (_translet == null) {
        ErrorMsg err = new ErrorMsg("JAXP_NO_TRANSLET_ERR");
        throw new TransformerException(err.toString());
      }
      
      transferOutputProperties(_translet);
    }
    
    SerializationHandler toHandler = getOutputHandler(result);
    if (toHandler == null) {
      ErrorMsg err = new ErrorMsg("JAXP_NO_HANDLER_ERR");
      throw new TransformerException(err.toString());
    }
    
    if ((_uriResolver != null) && (!_isIdentity)) {
      _translet.setDOMCache(this);
    }
    

    if (_isIdentity) {
      transferOutputProperties(toHandler);
    }
    
    transform(source, toHandler, _encoding);
    
    if ((result instanceof DOMResult)) {
      ((DOMResult)result).setNode(_tohFactory.getNode());
    }
  }
  






  public SerializationHandler getOutputHandler(Result result)
    throws TransformerException
  {
    _method = ((String)_properties.get("method"));
    

    _encoding = _properties.getProperty("encoding");
    
    _tohFactory = TransletOutputHandlerFactory.newInstance();
    _tohFactory.setEncoding(_encoding);
    if (_method != null) {
      _tohFactory.setOutputMethod(_method);
    }
    

    if (_indentNumber >= 0) {
      _tohFactory.setIndentNumber(_indentNumber);
    }
    

    try
    {
      if ((result instanceof SAXResult)) {
        SAXResult target = (SAXResult)result;
        ContentHandler handler = target.getHandler();
        
        _tohFactory.setHandler(handler);
        





        LexicalHandler lexicalHandler = target.getLexicalHandler();
        
        if (lexicalHandler != null) {
          _tohFactory.setLexicalHandler(lexicalHandler);
        }
        
        _tohFactory.setOutputType(1);
        return _tohFactory.getSerializationHandler();
      }
      if ((result instanceof DOMResult)) {
        _tohFactory.setNode(((DOMResult)result).getNode());
        _tohFactory.setNextSibling(((DOMResult)result).getNextSibling());
        _tohFactory.setOutputType(2);
        return _tohFactory.getSerializationHandler();
      }
      if ((result instanceof StreamResult))
      {
        StreamResult target = (StreamResult)result;
        




        _tohFactory.setOutputType(0);
        

        Writer writer = target.getWriter();
        if (writer != null) {
          _tohFactory.setWriter(writer);
          return _tohFactory.getSerializationHandler();
        }
        

        OutputStream ostream = target.getOutputStream();
        if (ostream != null) {
          _tohFactory.setOutputStream(ostream);
          return _tohFactory.getSerializationHandler();
        }
        

        String systemId = result.getSystemId();
        if (systemId == null) {
          ErrorMsg err = new ErrorMsg("JAXP_NO_RESULT_ERR");
          throw new TransformerException(err.toString());
        }
        



        URL url = null;
        if (systemId.startsWith("file:")) {
          url = new URL(systemId);
          _tohFactory.setOutputStream(this._ostream = new FileOutputStream(url.getFile()));
          
          return _tohFactory.getSerializationHandler();
        }
        if (systemId.startsWith("http:")) {
          url = new URL(systemId);
          URLConnection connection = url.openConnection();
          _tohFactory.setOutputStream(this._ostream = connection.getOutputStream());
          return _tohFactory.getSerializationHandler();
        }
        

        url = new File(systemId).toURL();
        _tohFactory.setOutputStream(this._ostream = new FileOutputStream(url.getFile()));
        
        return _tohFactory.getSerializationHandler();
      }
      
    }
    catch (UnknownServiceException e)
    {
      throw new TransformerException(e);
    }
    catch (ParserConfigurationException e) {
      throw new TransformerException(e);
    }
    catch (IOException e)
    {
      throw new TransformerException(e);
    }
    return null;
  }
  


  protected void setDOM(DOM dom)
  {
    _dom = dom;
  }
  
  private DOM getDOM(Source source)
    throws TransformerException
  {
    try
    {
      DOM dom = null;
      
      if (source != null) { DTMWSFilter wsfilter;
        DTMWSFilter wsfilter;
        if ((_translet != null) && ((_translet instanceof StripFilter))) {
          wsfilter = new DOMWSFilter(_translet);
        } else {
          wsfilter = null;
        }
        
        boolean hasIdCall = _translet != null ? _translet.hasIdCall() : false;
        

        if (_dtmManager == null) {
          _dtmManager = ((XSLTCDTMManager)_tfactory.getDTMManagerClass().newInstance());
        }
        

        dom = (DOM)_dtmManager.getDTM(source, false, wsfilter, true, false, false, 0, hasIdCall);
      }
      else if (_dom != null) {
        dom = _dom;
        _dom = null;
      } else {
        return null;
      }
      
      if (!_isIdentity)
      {

        _translet.prepassDocument(dom);
      }
      
      return dom;
    }
    catch (Exception e)
    {
      if (_errorListener != null) {
        postErrorToListener(e.getMessage());
      }
      throw new TransformerException(e);
    }
  }
  



  protected TransformerFactoryImpl getTransformerFactory()
  {
    return _tfactory;
  }
  



  protected TransletOutputHandlerFactory getTransletOutputHandlerFactory()
  {
    return _tohFactory;
  }
  

  private void transformIdentity(Source source, SerializationHandler handler)
    throws Exception
  {
    if (source != null) {
      _sourceSystemId = source.getSystemId();
    }
    
    if ((source instanceof StreamSource)) {
      StreamSource stream = (StreamSource)source;
      InputStream streamInput = stream.getInputStream();
      Reader streamReader = stream.getReader();
      XMLReader reader = _readerManager.getXMLReader();
      try
      {
        try
        {
          reader.setProperty("http://xml.org/sax/properties/lexical-handler", handler);
        }
        catch (SAXException e) {}
        

        reader.setContentHandler(handler);
        


        if (streamInput != null) {
          InputSource input = new InputSource(streamInput);
          input.setSystemId(_sourceSystemId);
        }
        else if (streamReader != null) {
          InputSource input = new InputSource(streamReader);
          input.setSystemId(_sourceSystemId);
        } else { InputSource input;
          if (_sourceSystemId != null) {
            input = new InputSource(_sourceSystemId);
          }
          else {
            ErrorMsg err = new ErrorMsg("JAXP_NO_SOURCE_ERR");
            throw new TransformerException(err.toString());
          }
        }
        InputSource input;
        reader.parse(input);
      } finally {
        _readerManager.releaseXMLReader(reader);
      }
    } else if ((source instanceof SAXSource)) {
      SAXSource sax = (SAXSource)source;
      XMLReader reader = sax.getXMLReader();
      InputSource input = sax.getInputSource();
      boolean userReader = true;
      
      try
      {
        if (reader == null) {
          reader = _readerManager.getXMLReader();
          userReader = false;
        }
        
        try
        {
          reader.setProperty("http://xml.org/sax/properties/lexical-handler", handler);
        }
        catch (SAXException e) {}
        

        reader.setContentHandler(handler);
        

        reader.parse(input);
      } finally {
        if (!userReader) {
          _readerManager.releaseXMLReader(reader);
        }
      }
    } else if ((source instanceof DOMSource)) {
      DOMSource domsrc = (DOMSource)source;
      new DOM2TO(domsrc.getNode(), handler).parse();
    } else if ((source instanceof XSLTCSource)) {
      DOM dom = ((XSLTCSource)source).getDOM(null, _translet);
      ((SAXImpl)dom).copy(handler);
    } else {
      ErrorMsg err = new ErrorMsg("JAXP_NO_SOURCE_ERR");
      throw new TransformerException(err.toString());
    }
  }
  










  private void transform(Source source, SerializationHandler handler, String encoding)
    throws TransformerException
  {
    try
    {
      if ((((source instanceof StreamSource)) && (source.getSystemId() == null) && (((StreamSource)source).getInputStream() == null) && (((StreamSource)source).getReader() == null)) || (((source instanceof SAXSource)) && (((SAXSource)source).getInputSource() == null) && (((SAXSource)source).getXMLReader() == null)) || (((source instanceof DOMSource)) && (((DOMSource)source).getNode() == null)))
      {






        DocumentBuilderFactory builderF = DocumentBuilderFactory.newInstance();
        
        DocumentBuilder builder = builderF.newDocumentBuilder();
        
        String systemID = source.getSystemId();
        source = new DOMSource(builder.newDocument());
        

        if (systemID != null) {
          source.setSystemId(systemID);
        }
      }
      if (_isIdentity) {
        transformIdentity(source, handler);
      } else {
        _translet.transform(getDOM(source), handler);
      }
    } catch (TransletException e) {
      if (_errorListener != null) postErrorToListener(e.getMessage());
      throw new TransformerException(e);
    } catch (RuntimeException e) {
      if (_errorListener != null) postErrorToListener(e.getMessage());
      throw new TransformerException(e);
    } catch (Exception e) {
      if (_errorListener != null) postErrorToListener(e.getMessage());
      throw new TransformerException(e);
    } finally {
      _dtmManager = null;
    }
    

    if (_ostream != null) {
      try {
        _ostream.close();
      }
      catch (IOException e) {}
      _ostream = null;
    }
  }
  





  public ErrorListener getErrorListener()
  {
    return _errorListener;
  }
  








  public void setErrorListener(ErrorListener listener)
    throws IllegalArgumentException
  {
    if (listener == null) {
      ErrorMsg err = new ErrorMsg("ERROR_LISTENER_NULL_ERR", "Transformer");
      
      throw new IllegalArgumentException(err.toString());
    }
    _errorListener = listener;
    

    if (_translet != null) {
      _translet.setMessageHandler(new MessageHandler(_errorListener));
    }
  }
  
  private void postErrorToListener(String message)
  {
    try
    {
      _errorListener.error(new TransformerException(message));
    }
    catch (TransformerException e) {}
  }
  



  private void postWarningToListener(String message)
  {
    try
    {
      _errorListener.warning(new TransformerException(message));
    }
    catch (TransformerException e) {}
  }
  







  private String makeCDATAString(Hashtable cdata)
  {
    if (cdata == null) { return null;
    }
    StringBuffer result = new StringBuffer();
    

    Enumeration elements = cdata.keys();
    if (elements.hasMoreElements()) {
      result.append((String)elements.nextElement());
      while (elements.hasMoreElements()) {
        String element = (String)elements.nextElement();
        result.append(' ');
        result.append(element);
      }
    }
    
    return result.toString();
  }
  










  public Properties getOutputProperties()
  {
    return (Properties)_properties.clone();
  }
  









  public String getOutputProperty(String name)
    throws IllegalArgumentException
  {
    if (!validOutputProperty(name)) {
      ErrorMsg err = new ErrorMsg("JAXP_UNKNOWN_PROP_ERR", name);
      throw new IllegalArgumentException(err.toString());
    }
    return _properties.getProperty(name);
  }
  









  public void setOutputProperties(Properties properties)
    throws IllegalArgumentException
  {
    if (properties != null) {
      Enumeration names = properties.propertyNames();
      
      while (names.hasMoreElements()) {
        String name = (String)names.nextElement();
        

        if (!isDefaultProperty(name, properties))
        {
          if (validOutputProperty(name)) {
            _properties.setProperty(name, properties.getProperty(name));
          }
          else {
            ErrorMsg err = new ErrorMsg("JAXP_UNKNOWN_PROP_ERR", name);
            throw new IllegalArgumentException(err.toString());
          }
        }
      }
    } else {
      _properties = _propertiesClone;
    }
  }
  










  public void setOutputProperty(String name, String value)
    throws IllegalArgumentException
  {
    if (!validOutputProperty(name)) {
      ErrorMsg err = new ErrorMsg("JAXP_UNKNOWN_PROP_ERR", name);
      throw new IllegalArgumentException(err.toString());
    }
    _properties.setProperty(name, value);
  }
  





  private void transferOutputProperties(AbstractTranslet translet)
  {
    if (_properties == null) { return;
    }
    
    Enumeration names = _properties.propertyNames();
    while (names.hasMoreElements())
    {
      String name = (String)names.nextElement();
      String value = (String)_properties.get(name);
      

      if (value != null)
      {

        if (name.equals("encoding")) {
          _encoding = value;
        }
        else if (name.equals("method")) {
          _method = value;
        }
        else if (name.equals("doctype-public")) {
          _doctypePublic = value;
        }
        else if (name.equals("doctype-system")) {
          _doctypeSystem = value;
        }
        else if (name.equals("media-type")) {
          _mediaType = value;
        }
        else if (name.equals("standalone")) {
          _standalone = value;
        }
        else if (name.equals("version")) {
          _version = value;
        }
        else if (name.equals("omit-xml-declaration")) {
          _omitHeader = ((value != null) && (value.toLowerCase().equals("yes")));

        }
        else if (name.equals("indent")) {
          _indent = ((value != null) && (value.toLowerCase().equals("yes")));

        }
        else if ((name.equals("cdata-section-elements")) && 
          (value != null)) {
          _cdata = null;
          StringTokenizer e = new StringTokenizer(value);
          while (e.hasMoreTokens()) {
            translet.addCdataElement(e.nextToken());
          }
        }
      }
    }
  }
  





  public void transferOutputProperties(SerializationHandler handler)
  {
    if (_properties == null) { return;
    }
    String doctypePublic = null;
    String doctypeSystem = null;
    

    Enumeration names = _properties.propertyNames();
    while (names.hasMoreElements())
    {
      String name = (String)names.nextElement();
      String value = (String)_properties.get(name);
      

      if (value != null)
      {

        if (name.equals("doctype-public")) {
          doctypePublic = value;
        }
        else if (name.equals("doctype-system")) {
          doctypeSystem = value;
        }
        else if (name.equals("media-type")) {
          handler.setMediaType(value);
        }
        else if (name.equals("standalone")) {
          handler.setStandalone(value);
        }
        else if (name.equals("version")) {
          handler.setVersion(value);
        }
        else if (name.equals("omit-xml-declaration")) {
          handler.setOmitXMLDeclaration((value != null) && (value.toLowerCase().equals("yes")));

        }
        else if (name.equals("indent")) {
          handler.setIndent((value != null) && (value.toLowerCase().equals("yes")));

        }
        else if ((name.equals("cdata-section-elements")) && 
          (value != null)) {
          StringTokenizer e = new StringTokenizer(value);
          Vector uriAndLocalNames = null;
          while (e.hasMoreTokens()) {
            String token = e.nextToken();
            


            int lastcolon = token.lastIndexOf(':');
            String localName;
            String uri;
            String localName; if (lastcolon > 0) {
              String uri = token.substring(0, lastcolon);
              localName = token.substring(lastcolon + 1);
            }
            else
            {
              uri = null;
              localName = token;
            }
            
            if (uriAndLocalNames == null) {
              uriAndLocalNames = new Vector();
            }
            
            uriAndLocalNames.addElement(uri);
            uriAndLocalNames.addElement(localName);
          }
          handler.setCdataSectionElements(uriAndLocalNames);
        }
      }
    }
    

    if ((doctypePublic != null) || (doctypeSystem != null)) {
      handler.setDoctype(doctypeSystem, doctypePublic);
    }
  }
  





  private Properties createOutputProperties(Properties outputProperties)
  {
    Properties defaults = new Properties();
    setDefaults(defaults, "xml");
    

    Properties base = new Properties(defaults);
    if (outputProperties != null) {
      Enumeration names = outputProperties.propertyNames();
      while (names.hasMoreElements()) {
        String name = (String)names.nextElement();
        base.setProperty(name, outputProperties.getProperty(name));
      }
    }
    else {
      base.setProperty("encoding", _translet._encoding);
      if (_translet._method != null) {
        base.setProperty("method", _translet._method);
      }
    }
    
    String method = base.getProperty("method");
    if (method != null) {
      if (method.equals("html")) {
        setDefaults(defaults, "html");
      }
      else if (method.equals("text")) {
        setDefaults(defaults, "text");
      }
    }
    
    return base;
  }
  






  private void setDefaults(Properties props, String method)
  {
    Properties method_props = OutputPropertiesFactory.getDefaultMethodProperties(method);
    

    Enumeration names = method_props.propertyNames();
    while (names.hasMoreElements())
    {
      String name = (String)names.nextElement();
      props.setProperty(name, method_props.getProperty(name));
    }
  }
  



  private boolean validOutputProperty(String name)
  {
    return (name.equals("encoding")) || (name.equals("method")) || (name.equals("indent")) || (name.equals("doctype-public")) || (name.equals("doctype-system")) || (name.equals("cdata-section-elements")) || (name.equals("media-type")) || (name.equals("omit-xml-declaration")) || (name.equals("standalone")) || (name.equals("version")) || (name.charAt(0) == '{');
  }
  












  private boolean isDefaultProperty(String name, Properties properties)
  {
    return properties.get(name) == null;
  }
  









  public void setParameter(String name, Object value)
  {
    if (value == null) {
      ErrorMsg err = new ErrorMsg("JAXP_INVALID_SET_PARAM_VALUE", name);
      throw new IllegalArgumentException(err.toString());
    }
    
    if (_isIdentity) {
      if (_parameters == null) {
        _parameters = new Hashtable();
      }
      _parameters.put(name, value);
    }
    else {
      _translet.addParameter(name, value);
    }
  }
  




  public void clearParameters()
  {
    if ((_isIdentity) && (_parameters != null)) {
      _parameters.clear();
    }
    else {
      _translet.clearParameters();
    }
  }
  







  public final Object getParameter(String name)
  {
    if (_isIdentity) {
      return _parameters != null ? _parameters.get(name) : null;
    }
    
    return _translet.getParameter(name);
  }
  






  public URIResolver getURIResolver()
  {
    return _uriResolver;
  }
  





  public void setURIResolver(URIResolver resolver)
  {
    _uriResolver = resolver;
  }
  













  public DOM retrieveDocument(String baseURI, String href, Translet translet)
  {
    try
    {
      if (href.length() == 0) {
        href = baseURI;
      }
      








      Source resolvedSource = _uriResolver.resolve(href, baseURI);
      if (resolvedSource == null) {
        StreamSource streamSource = new StreamSource(SystemIDResolver.getAbsoluteURI(href, baseURI));
        
        return getDOM(streamSource);
      }
      
      return getDOM(resolvedSource);
    }
    catch (TransformerException e) {
      if (_errorListener != null)
        postErrorToListener("File not found: " + e.getMessage()); }
    return null;
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
  







  public void reset()
  {
    _method = null;
    _encoding = null;
    _sourceSystemId = null;
    _errorListener = this;
    _uriResolver = null;
    _dom = null;
    _parameters = null;
    _indentNumber = 0;
    setOutputProperties(null);
  }
}
