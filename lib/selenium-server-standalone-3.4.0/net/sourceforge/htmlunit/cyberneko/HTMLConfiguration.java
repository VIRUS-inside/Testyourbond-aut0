package net.sourceforge.htmlunit.cyberneko;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import net.sourceforge.htmlunit.cyberneko.filters.NamespaceBinder;
import net.sourceforge.htmlunit.cyberneko.xercesbridge.XercesBridge;
import org.apache.xerces.util.DefaultErrorHandler;
import org.apache.xerces.util.ParserConfigurationSettings;
import org.apache.xerces.xni.XMLDTDContentModelHandler;
import org.apache.xerces.xni.XMLDTDHandler;
import org.apache.xerces.xni.XMLDocumentHandler;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLDocumentFilter;
import org.apache.xerces.xni.parser.XMLDocumentSource;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLErrorHandler;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.apache.xerces.xni.parser.XMLParseException;
import org.apache.xerces.xni.parser.XMLPullParserConfiguration;



































































































public class HTMLConfiguration
  extends ParserConfigurationSettings
  implements XMLPullParserConfiguration
{
  protected static final String NAMESPACES = "http://xml.org/sax/features/namespaces";
  protected static final String AUGMENTATIONS = "http://cyberneko.org/html/features/augmentations";
  protected static final String REPORT_ERRORS = "http://cyberneko.org/html/features/report-errors";
  protected static final String SIMPLE_ERROR_FORMAT = "http://cyberneko.org/html/features/report-errors/simple";
  protected static final String BALANCE_TAGS = "http://cyberneko.org/html/features/balance-tags";
  protected static final String NAMES_ELEMS = "http://cyberneko.org/html/properties/names/elems";
  protected static final String NAMES_ATTRS = "http://cyberneko.org/html/properties/names/attrs";
  protected static final String FILTERS = "http://cyberneko.org/html/properties/filters";
  protected static final String ERROR_REPORTER = "http://cyberneko.org/html/properties/error-reporter";
  protected static final String ERROR_DOMAIN = "http://cyberneko.org/html";
  protected XMLDocumentHandler fDocumentHandler;
  protected XMLDTDHandler fDTDHandler;
  protected XMLDTDContentModelHandler fDTDContentModelHandler;
  protected XMLErrorHandler fErrorHandler = new DefaultErrorHandler();
  


  protected XMLEntityResolver fEntityResolver;
  


  protected Locale fLocale = Locale.getDefault();
  





  protected boolean fCloseStream;
  




  protected final List<HTMLComponent> fHTMLComponents = new ArrayList(2);
  



  protected final HTMLScanner fDocumentScanner = createDocumentScanner();
  

  protected final HTMLTagBalancer fTagBalancer = new HTMLTagBalancer(this);
  

  protected final NamespaceBinder fNamespaceBinder = new NamespaceBinder(this);
  



  protected final HTMLErrorReporter fErrorReporter = new ErrorReporter();
  



  protected static boolean XERCES_2_0_0 = false;
  

  protected static boolean XERCES_2_0_1 = false;
  

  protected static boolean XML4J_4_0_x = false;
  
  public final HTMLElements htmlElements_;
  
  static
  {
    try
    {
      String VERSION = "org.apache.xerces.impl.Version";
      Object version = ObjectFactory.createObject(VERSION, VERSION);
      Field field = version.getClass().getField("fVersion");
      String versionStr = String.valueOf(field.get(version));
      XERCES_2_0_0 = versionStr.equals("Xerces-J 2.0.0");
      XERCES_2_0_1 = versionStr.equals("Xerces-J 2.0.1");
      XML4J_4_0_x = versionStr.startsWith("XML4J 4.0.");
    }
    catch (Throwable localThrowable) {}
  }
  







  public HTMLConfiguration()
  {
    this(new HTMLElements());
  }
  
  public HTMLConfiguration(HTMLElements htmlElements) {
    htmlElements_ = htmlElements;
    

    addComponent(fDocumentScanner);
    addComponent(fTagBalancer);
    addComponent(fNamespaceBinder);
    





    String VALIDATION = "http://xml.org/sax/features/validation";
    String[] recognizedFeatures = {
      "http://cyberneko.org/html/features/augmentations", 
      "http://xml.org/sax/features/namespaces", 
      VALIDATION, 
      "http://cyberneko.org/html/features/report-errors", 
      "http://cyberneko.org/html/features/report-errors/simple", 
      "http://cyberneko.org/html/features/balance-tags" };
    
    addRecognizedFeatures(recognizedFeatures);
    setFeature("http://cyberneko.org/html/features/augmentations", false);
    setFeature("http://xml.org/sax/features/namespaces", true);
    setFeature(VALIDATION, false);
    setFeature("http://cyberneko.org/html/features/report-errors", false);
    setFeature("http://cyberneko.org/html/features/report-errors/simple", false);
    setFeature("http://cyberneko.org/html/features/balance-tags", true);
    

    if (XERCES_2_0_0)
    {


      recognizedFeatures = new String[] {
        "http://apache.org/xml/features/scanner/notify-builtin-refs" };
      
      addRecognizedFeatures(recognizedFeatures);
    }
    

    if ((XERCES_2_0_0) || (XERCES_2_0_1) || (XML4J_4_0_x))
    {


      recognizedFeatures = new String[] {
        "http://apache.org/xml/features/validation/schema/normalized-value", 
        "http://apache.org/xml/features/scanner/notify-char-refs" };
      
      addRecognizedFeatures(recognizedFeatures);
    }
    





    String[] recognizedProperties = {
      "http://cyberneko.org/html/properties/names/elems", 
      "http://cyberneko.org/html/properties/names/attrs", 
      "http://cyberneko.org/html/properties/filters", 
      "http://cyberneko.org/html/properties/error-reporter" };
    
    addRecognizedProperties(recognizedProperties);
    setProperty("http://cyberneko.org/html/properties/names/elems", "upper");
    setProperty("http://cyberneko.org/html/properties/names/attrs", "lower");
    setProperty("http://cyberneko.org/html/properties/error-reporter", fErrorReporter);
    

    if (XERCES_2_0_0)
    {




      String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
      recognizedProperties = new String[] {
        SYMBOL_TABLE };
      
      addRecognizedProperties(recognizedProperties);
      Object symbolTable = ObjectFactory.createObject("org.apache.xerces.util.SymbolTable", 
        "org.apache.xerces.util.SymbolTable");
      setProperty(SYMBOL_TABLE, symbolTable);
    }
  }
  
  protected HTMLScanner createDocumentScanner()
  {
    return new HTMLScanner(this);
  }
  



















  public void pushInputSource(XMLInputSource inputSource)
  {
    fDocumentScanner.pushInputSource(inputSource);
  }
  







  public void evaluateInputSource(XMLInputSource inputSource)
  {
    fDocumentScanner.evaluateInputSource(inputSource);
  }
  




  public void setFeature(String featureId, boolean state)
    throws XMLConfigurationException
  {
    super.setFeature(featureId, state);
    int size = fHTMLComponents.size();
    for (int i = 0; i < size; i++) {
      HTMLComponent component = (HTMLComponent)fHTMLComponents.get(i);
      component.setFeature(featureId, state);
    }
  }
  

  public void setProperty(String propertyId, Object value)
    throws XMLConfigurationException
  {
    super.setProperty(propertyId, value);
    
    if (propertyId.equals("http://cyberneko.org/html/properties/filters")) {
      XMLDocumentFilter[] filters = (XMLDocumentFilter[])getProperty("http://cyberneko.org/html/properties/filters");
      if (filters != null) {
        for (int i = 0; i < filters.length; i++) {
          XMLDocumentFilter filter = filters[i];
          if ((filter instanceof HTMLComponent)) {
            addComponent((HTMLComponent)filter);
          }
        }
      }
    }
    
    int size = fHTMLComponents.size();
    for (int i = 0; i < size; i++) {
      HTMLComponent component = (HTMLComponent)fHTMLComponents.get(i);
      component.setProperty(propertyId, value);
    }
  }
  

  public void setDocumentHandler(XMLDocumentHandler handler)
  {
    fDocumentHandler = handler;
    if ((handler instanceof HTMLTagBalancingListener)) {
      fTagBalancer.setTagBalancingListener((HTMLTagBalancingListener)handler);
    }
  }
  

  public XMLDocumentHandler getDocumentHandler()
  {
    return fDocumentHandler;
  }
  

  public void setDTDHandler(XMLDTDHandler handler)
  {
    fDTDHandler = handler;
  }
  

  public XMLDTDHandler getDTDHandler()
  {
    return fDTDHandler;
  }
  

  public void setDTDContentModelHandler(XMLDTDContentModelHandler handler)
  {
    fDTDContentModelHandler = handler;
  }
  

  public XMLDTDContentModelHandler getDTDContentModelHandler()
  {
    return fDTDContentModelHandler;
  }
  

  public void setErrorHandler(XMLErrorHandler handler)
  {
    fErrorHandler = handler;
  }
  

  public XMLErrorHandler getErrorHandler()
  {
    return fErrorHandler;
  }
  

  public void setEntityResolver(XMLEntityResolver resolver)
  {
    fEntityResolver = resolver;
  }
  

  public XMLEntityResolver getEntityResolver()
  {
    return fEntityResolver;
  }
  

  public void setLocale(Locale locale)
  {
    if (locale == null) {
      locale = Locale.getDefault();
    }
    fLocale = locale;
  }
  

  public Locale getLocale()
  {
    return fLocale;
  }
  
  public void parse(XMLInputSource source)
    throws XNIException, IOException
  {
    setInputSource(source);
    parse(true);
  }
  


















  public void setInputSource(XMLInputSource inputSource)
    throws XMLConfigurationException, IOException
  {
    reset();
    fCloseStream = ((inputSource.getByteStream() == null) && 
      (inputSource.getCharacterStream() == null));
    fDocumentScanner.setInputSource(inputSource);
  }
  














  public boolean parse(boolean complete)
    throws XNIException, IOException
  {
    try
    {
      boolean more = fDocumentScanner.scanDocument(complete);
      if (!more) {
        cleanup();
      }
      return more;
    }
    catch (XNIException e) {
      cleanup();
      throw e;
    }
    catch (IOException e) {
      cleanup();
      throw e;
    }
  }
  





  public void cleanup()
  {
    fDocumentScanner.cleanup(fCloseStream);
  }
  






  protected void addComponent(HTMLComponent component)
  {
    fHTMLComponents.add(component);
    

    String[] features = component.getRecognizedFeatures();
    addRecognizedFeatures(features);
    int featureCount = features != null ? features.length : 0;
    for (int i = 0; i < featureCount; i++) {
      Boolean state = component.getFeatureDefault(features[i]);
      if (state != null) {
        setFeature(features[i], state.booleanValue());
      }
    }
    

    String[] properties = component.getRecognizedProperties();
    addRecognizedProperties(properties);
    int propertyCount = properties != null ? properties.length : 0;
    for (int i = 0; i < propertyCount; i++) {
      Object value = component.getPropertyDefault(properties[i]);
      if (value != null) {
        setProperty(properties[i], value);
      }
    }
  }
  


  protected void reset()
    throws XMLConfigurationException
  {
    int size = fHTMLComponents.size();
    for (int i = 0; i < size; i++) {
      HTMLComponent component = (HTMLComponent)fHTMLComponents.get(i);
      component.reset(this);
    }
    

    XMLDocumentSource lastSource = fDocumentScanner;
    if (getFeature("http://xml.org/sax/features/namespaces")) {
      lastSource.setDocumentHandler(fNamespaceBinder);
      fNamespaceBinder.setDocumentSource(fTagBalancer);
      lastSource = fNamespaceBinder;
    }
    if (getFeature("http://cyberneko.org/html/features/balance-tags")) {
      lastSource.setDocumentHandler(fTagBalancer);
      fTagBalancer.setDocumentSource(fDocumentScanner);
      lastSource = fTagBalancer;
    }
    XMLDocumentFilter[] filters = (XMLDocumentFilter[])getProperty("http://cyberneko.org/html/properties/filters");
    if (filters != null) {
      for (int i = 0; i < filters.length; i++) {
        XMLDocumentFilter filter = filters[i];
        XercesBridge.getInstance().XMLDocumentFilter_setDocumentSource(filter, lastSource);
        lastSource.setDocumentHandler(filter);
        lastSource = filter;
      }
    }
    lastSource.setDocumentHandler(fDocumentHandler);
  }
  









  protected class ErrorReporter
    implements HTMLErrorReporter
  {
    protected Locale fLastLocale;
    







    protected ResourceBundle fErrorMessages;
    








    protected ErrorReporter() {}
    








    public String formatMessage(String key, Object[] args)
    {
      if (!getFeature("http://cyberneko.org/html/features/report-errors/simple")) {
        if (!fLocale.equals(fLastLocale)) {
          fErrorMessages = null;
          fLastLocale = fLocale;
        }
        if (fErrorMessages == null) {
          fErrorMessages = 
            ResourceBundle.getBundle("net/sourceforge/htmlunit/cyberneko/res/ErrorMessages", 
            fLocale);
        }
        try {
          String value = fErrorMessages.getString(key);
          return MessageFormat.format(value, args);
        }
        catch (MissingResourceException localMissingResourceException) {}
      }
      


      return formatSimpleMessage(key, args);
    }
    

    public void reportWarning(String key, Object[] args)
      throws XMLParseException
    {
      if (fErrorHandler != null) {
        fErrorHandler.warning("http://cyberneko.org/html", key, createException(key, args));
      }
    }
    

    public void reportError(String key, Object[] args)
      throws XMLParseException
    {
      if (fErrorHandler != null) {
        fErrorHandler.error("http://cyberneko.org/html", key, createException(key, args));
      }
    }
    




    protected XMLParseException createException(String key, Object[] args)
    {
      String message = formatMessage(key, args);
      return new XMLParseException(fDocumentScanner, message);
    }
    
    protected String formatSimpleMessage(String key, Object[] args)
    {
      StringBuilder str = new StringBuilder();
      str.append("http://cyberneko.org/html");
      str.append('#');
      str.append(key);
      if ((args != null) && (args.length > 0)) {
        str.append('\t');
        for (int i = 0; i < args.length; i++) {
          if (i > 0) {
            str.append('\t');
          }
          str.append(String.valueOf(args[i]));
        }
      }
      return str.toString();
    }
  }
}
