package net.sourceforge.htmlunit.cyberneko;

import java.io.EOFException;
import java.io.File;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.BitSet;
import java.util.Locale;
import java.util.Stack;
import net.sourceforge.htmlunit.cyberneko.xercesbridge.XercesBridge;
import org.apache.xerces.util.EncodingMap;
import org.apache.xerces.util.NamespaceSupport;
import org.apache.xerces.util.URI;
import org.apache.xerces.util.URI.MalformedURIException;
import org.apache.xerces.util.XMLAttributesImpl;
import org.apache.xerces.util.XMLResourceIdentifierImpl;
import org.apache.xerces.util.XMLStringBuffer;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLDocumentHandler;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLDocumentScanner;
import org.apache.xerces.xni.parser.XMLInputSource;





























































































































































public class HTMLScanner
  implements XMLDocumentScanner, XMLLocator, HTMLComponent
{
  public static final String HTML_4_01_STRICT_PUBID = "-//W3C//DTD HTML 4.01//EN";
  public static final String HTML_4_01_STRICT_SYSID = "http://www.w3.org/TR/html4/strict.dtd";
  public static final String HTML_4_01_TRANSITIONAL_PUBID = "-//W3C//DTD HTML 4.01 Transitional//EN";
  public static final String HTML_4_01_TRANSITIONAL_SYSID = "http://www.w3.org/TR/html4/loose.dtd";
  public static final String HTML_4_01_FRAMESET_PUBID = "-//W3C//DTD HTML 4.01 Frameset//EN";
  public static final String HTML_4_01_FRAMESET_SYSID = "http://www.w3.org/TR/html4/frameset.dtd";
  protected static final String AUGMENTATIONS = "http://cyberneko.org/html/features/augmentations";
  protected static final String REPORT_ERRORS = "http://cyberneko.org/html/features/report-errors";
  public static final String NOTIFY_CHAR_REFS = "http://apache.org/xml/features/scanner/notify-char-refs";
  public static final String NOTIFY_XML_BUILTIN_REFS = "http://apache.org/xml/features/scanner/notify-builtin-refs";
  public static final String NOTIFY_HTML_BUILTIN_REFS = "http://cyberneko.org/html/features/scanner/notify-builtin-refs";
  public static final String FIX_MSWINDOWS_REFS = "http://cyberneko.org/html/features/scanner/fix-mswindows-refs";
  public static final String SCRIPT_STRIP_COMMENT_DELIMS = "http://cyberneko.org/html/features/scanner/script/strip-comment-delims";
  public static final String SCRIPT_STRIP_CDATA_DELIMS = "http://cyberneko.org/html/features/scanner/script/strip-cdata-delims";
  public static final String STYLE_STRIP_COMMENT_DELIMS = "http://cyberneko.org/html/features/scanner/style/strip-comment-delims";
  public static final String STYLE_STRIP_CDATA_DELIMS = "http://cyberneko.org/html/features/scanner/style/strip-cdata-delims";
  public static final String IGNORE_SPECIFIED_CHARSET = "http://cyberneko.org/html/features/scanner/ignore-specified-charset";
  public static final String CDATA_SECTIONS = "http://cyberneko.org/html/features/scanner/cdata-sections";
  public static final String OVERRIDE_DOCTYPE = "http://cyberneko.org/html/features/override-doctype";
  public static final String INSERT_DOCTYPE = "http://cyberneko.org/html/features/insert-doctype";
  public static final String PARSE_NOSCRIPT_CONTENT = "http://cyberneko.org/html/features/parse-noscript-content";
  public static final String ALLOW_SELFCLOSING_IFRAME = "http://cyberneko.org/html/features/scanner/allow-selfclosing-iframe";
  public static final String ALLOW_SELFCLOSING_TAGS = "http://cyberneko.org/html/features/scanner/allow-selfclosing-tags";
  protected static final String NORMALIZE_ATTRIBUTES = "http://cyberneko.org/html/features/scanner/normalize-attrs";
  private static final String[] RECOGNIZED_FEATURES = {
    "http://cyberneko.org/html/features/augmentations", 
    "http://cyberneko.org/html/features/report-errors", 
    "http://apache.org/xml/features/scanner/notify-char-refs", 
    "http://apache.org/xml/features/scanner/notify-builtin-refs", 
    "http://cyberneko.org/html/features/scanner/notify-builtin-refs", 
    "http://cyberneko.org/html/features/scanner/fix-mswindows-refs", 
    "http://cyberneko.org/html/features/scanner/script/strip-cdata-delims", 
    "http://cyberneko.org/html/features/scanner/script/strip-comment-delims", 
    "http://cyberneko.org/html/features/scanner/style/strip-cdata-delims", 
    "http://cyberneko.org/html/features/scanner/style/strip-comment-delims", 
    "http://cyberneko.org/html/features/scanner/ignore-specified-charset", 
    "http://cyberneko.org/html/features/scanner/cdata-sections", 
    "http://cyberneko.org/html/features/override-doctype", 
    "http://cyberneko.org/html/features/insert-doctype", 
    "http://cyberneko.org/html/features/scanner/normalize-attrs", 
    "http://cyberneko.org/html/features/parse-noscript-content", 
    "http://cyberneko.org/html/features/scanner/allow-selfclosing-iframe", 
    "http://cyberneko.org/html/features/scanner/allow-selfclosing-tags" };
  


  private static final Boolean[] RECOGNIZED_FEATURES_DEFAULTS = {
  

    0, 0, Boolean.FALSE, 
    Boolean.FALSE, 
    Boolean.FALSE, 
    Boolean.FALSE, 
    Boolean.FALSE, 
    Boolean.FALSE, 
    Boolean.FALSE, 
    Boolean.FALSE, 
    Boolean.FALSE, 
    Boolean.FALSE, 
    Boolean.FALSE, 
    Boolean.FALSE, 
    Boolean.FALSE, 
    Boolean.TRUE, 
    Boolean.FALSE, 
    Boolean.FALSE };
  


  protected static final String NAMES_ELEMS = "http://cyberneko.org/html/properties/names/elems";
  


  protected static final String NAMES_ATTRS = "http://cyberneko.org/html/properties/names/attrs";
  


  protected static final String DEFAULT_ENCODING = "http://cyberneko.org/html/properties/default-encoding";
  

  protected static final String ERROR_REPORTER = "http://cyberneko.org/html/properties/error-reporter";
  

  protected static final String DOCTYPE_PUBID = "http://cyberneko.org/html/properties/doctype/pubid";
  

  protected static final String DOCTYPE_SYSID = "http://cyberneko.org/html/properties/doctype/sysid";
  

  private static final String[] RECOGNIZED_PROPERTIES = {
    "http://cyberneko.org/html/properties/names/elems", 
    "http://cyberneko.org/html/properties/names/attrs", 
    "http://cyberneko.org/html/properties/default-encoding", 
    "http://cyberneko.org/html/properties/error-reporter", 
    "http://cyberneko.org/html/properties/doctype/pubid", 
    "http://cyberneko.org/html/properties/doctype/sysid" };
  


  private static final Object[] RECOGNIZED_PROPERTIES_DEFAULTS = {
  

    0, 0, "Windows-1252", 
    
    0, "-//W3C//DTD HTML 4.01 Transitional//EN", 
    "http://www.w3.org/TR/html4/loose.dtd" };
  


  protected static final short STATE_CONTENT = 0;
  


  protected static final short STATE_MARKUP_BRACKET = 1;
  


  protected static final short STATE_START_DOCUMENT = 10;
  


  protected static final short STATE_END_DOCUMENT = 11;
  


  protected static final short NAMES_NO_CHANGE = 0;
  


  protected static final short NAMES_UPPERCASE = 1;
  


  protected static final short NAMES_LOWERCASE = 2;
  


  protected static final int DEFAULT_BUFFER_SIZE = 2048;
  


  private static final boolean DEBUG_SCANNER = false;
  


  private static final boolean DEBUG_SCANNER_STATE = false;
  


  private static final boolean DEBUG_BUFFER = false;
  


  private static final boolean DEBUG_CHARSET = false;
  

  protected static final boolean DEBUG_CALLBACKS = false;
  

  protected static final HTMLEventInfo SYNTHESIZED_ITEM = new HTMLEventInfo.SynthesizedItem();
  
  private static final BitSet ENTITY_CHARS = new BitSet();
  
  static { String str = "-.0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ_abcdefghijklmnopqrstuvwxyz";
    for (int i = 0; i < "-.0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ_abcdefghijklmnopqrstuvwxyz".length(); i++) {
      char c = "-.0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ_abcdefghijklmnopqrstuvwxyz".charAt(i);
      ENTITY_CHARS.set(c);
    }
  }
  



  protected boolean fAugmentations;
  


  protected boolean fReportErrors;
  


  protected boolean fNotifyCharRefs;
  


  protected boolean fNotifyXmlBuiltinRefs;
  


  protected boolean fNotifyHtmlBuiltinRefs;
  


  protected boolean fFixWindowsCharRefs;
  


  protected boolean fScriptStripCDATADelims;
  


  protected boolean fScriptStripCommentDelims;
  


  protected boolean fStyleStripCDATADelims;
  


  protected boolean fStyleStripCommentDelims;
  

  protected boolean fIgnoreSpecifiedCharset;
  

  protected boolean fCDATASections;
  

  protected boolean fOverrideDoctype;
  

  protected boolean fInsertDoctype;
  

  protected boolean fNormalizeAttributes;
  

  protected boolean fParseNoScriptContent;
  

  protected boolean fParseNoFramesContent;
  

  protected boolean fAllowSelfclosingIframe;
  

  protected boolean fAllowSelfclosingTags;
  

  protected short fNamesElems;
  

  protected short fNamesAttrs;
  

  protected String fDefaultIANAEncoding;
  

  protected HTMLErrorReporter fErrorReporter;
  

  protected String fDoctypePubid;
  

  protected String fDoctypeSysid;
  

  protected int fBeginLineNumber;
  

  protected int fBeginColumnNumber;
  

  protected int fBeginCharacterOffset;
  

  protected int fEndLineNumber;
  

  protected int fEndColumnNumber;
  

  protected int fEndCharacterOffset;
  

  protected PlaybackInputStream fByteStream;
  

  protected CurrentEntity fCurrentEntity;
  

  protected final Stack<CurrentEntity> fCurrentEntityStack = new Stack();
  


  protected Scanner fScanner;
  


  protected short fScannerState;
  

  protected XMLDocumentHandler fDocumentHandler;
  

  protected String fIANAEncoding;
  

  protected String fJavaEncoding;
  

  protected boolean fIso8859Encoding;
  

  protected int fElementCount;
  

  protected int fElementDepth;
  

  protected Scanner fContentScanner = new ContentScanner();
  





  protected SpecialScanner fSpecialScanner = new SpecialScanner();
  



  protected final XMLStringBuffer fStringBuffer = new XMLStringBuffer(1024);
  

  private final XMLStringBuffer fStringBuffer2 = new XMLStringBuffer(1024);
  

  private final XMLStringBuffer fNonNormAttr = new XMLStringBuffer(128);
  

  private final HTMLAugmentations fInfosetAugs = new HTMLAugmentations();
  

  private final LocationItem fLocationItem = new LocationItem();
  

  private final boolean[] fSingleBoolean = new boolean[1];
  

  private final XMLResourceIdentifierImpl fResourceId = new XMLResourceIdentifierImpl();
  
  private final char REPLACEMENT_CHARACTER = 65533;
  private final HTMLConfiguration htmlConfiguration_;
  
  HTMLScanner(HTMLConfiguration htmlConfiguration)
  {
    htmlConfiguration_ = htmlConfiguration;
  }
  

















  public void pushInputSource(XMLInputSource inputSource)
  {
    Reader reader = getReader(inputSource);
    
    fCurrentEntityStack.push(fCurrentEntity);
    String encoding = inputSource.getEncoding();
    String publicId = inputSource.getPublicId();
    String baseSystemId = inputSource.getBaseSystemId();
    String literalSystemId = inputSource.getSystemId();
    String expandedSystemId = expandSystemId(literalSystemId, baseSystemId);
    fCurrentEntity = new CurrentEntity(reader, encoding, 
      publicId, baseSystemId, 
      literalSystemId, expandedSystemId);
  }
  
  private Reader getReader(XMLInputSource inputSource) {
    Reader reader = inputSource.getCharacterStream();
    if (reader == null) {
      try {
        return new InputStreamReader(inputSource.getByteStream(), fJavaEncoding);
      }
      catch (UnsupportedEncodingException localUnsupportedEncodingException) {}
    }
    

    return reader;
  }
  






  public void evaluateInputSource(XMLInputSource inputSource)
  {
    Scanner previousScanner = fScanner;
    short previousScannerState = fScannerState;
    CurrentEntity previousEntity = fCurrentEntity;
    Reader reader = getReader(inputSource);
    
    String encoding = inputSource.getEncoding();
    String publicId = inputSource.getPublicId();
    String baseSystemId = inputSource.getBaseSystemId();
    String literalSystemId = inputSource.getSystemId();
    String expandedSystemId = expandSystemId(literalSystemId, baseSystemId);
    fCurrentEntity = new CurrentEntity(reader, encoding, 
      publicId, baseSystemId, 
      literalSystemId, expandedSystemId);
    setScanner(fContentScanner);
    setScannerState((short)0);
    try {
      do {
        fScanner.scan(false);
      } while (fScannerState != 11);
    }
    catch (IOException localIOException) {}
    

    setScanner(previousScanner);
    setScannerState(previousScannerState);
    fCurrentEntity = previousEntity;
  }
  









  public void cleanup(boolean closeall)
  {
    int size = fCurrentEntityStack.size();
    if (size > 0)
    {
      if (fCurrentEntity != null) {
        fCurrentEntity.closeQuietly();
      }
      
      for (int i = closeall ? 0 : 1; i < size; i++) {
        fCurrentEntity = ((CurrentEntity)fCurrentEntityStack.pop());
        fCurrentEntity.closeQuietly();
      }
    }
    else if ((closeall) && (fCurrentEntity != null)) {
      fCurrentEntity.closeQuietly();
    }
  }
  





  public String getEncoding()
  {
    return fCurrentEntity != null ? fCurrentEntity.encoding : null;
  }
  

  public String getPublicId()
  {
    return fCurrentEntity != null ? fCurrentEntity.publicId : null;
  }
  

  public String getBaseSystemId()
  {
    return fCurrentEntity != null ? fCurrentEntity.baseSystemId : null;
  }
  

  public String getLiteralSystemId()
  {
    return fCurrentEntity != null ? fCurrentEntity.literalSystemId : null;
  }
  

  public String getExpandedSystemId()
  {
    return fCurrentEntity != null ? fCurrentEntity.expandedSystemId : null;
  }
  

  public int getLineNumber()
  {
    return fCurrentEntity != null ? fCurrentEntity.getLineNumber() : -1;
  }
  

  public int getColumnNumber()
  {
    return fCurrentEntity != null ? fCurrentEntity.getColumnNumber() : -1;
  }
  

  public String getXMLVersion()
  {
    fCurrentEntity.getClass();return fCurrentEntity != null ? "1.0" : null;
  }
  

  public int getCharacterOffset()
  {
    return fCurrentEntity != null ? fCurrentEntity.getCharacterOffset() : -1;
  }
  





  public Boolean getFeatureDefault(String featureId)
  {
    int length = RECOGNIZED_FEATURES != null ? RECOGNIZED_FEATURES.length : 0;
    for (int i = 0; i < length; i++) {
      if (RECOGNIZED_FEATURES[i].equals(featureId)) {
        return RECOGNIZED_FEATURES_DEFAULTS[i];
      }
    }
    return null;
  }
  

  public Object getPropertyDefault(String propertyId)
  {
    int length = RECOGNIZED_PROPERTIES != null ? RECOGNIZED_PROPERTIES.length : 0;
    for (int i = 0; i < length; i++) {
      if (RECOGNIZED_PROPERTIES[i].equals(propertyId)) {
        return RECOGNIZED_PROPERTIES_DEFAULTS[i];
      }
    }
    return null;
  }
  





  public String[] getRecognizedFeatures()
  {
    return RECOGNIZED_FEATURES;
  }
  

  public String[] getRecognizedProperties()
  {
    return RECOGNIZED_PROPERTIES;
  }
  



  public void reset(XMLComponentManager manager)
    throws XMLConfigurationException
  {
    fAugmentations = manager.getFeature("http://cyberneko.org/html/features/augmentations");
    fReportErrors = manager.getFeature("http://cyberneko.org/html/features/report-errors");
    fNotifyCharRefs = manager.getFeature("http://apache.org/xml/features/scanner/notify-char-refs");
    fNotifyXmlBuiltinRefs = manager.getFeature("http://apache.org/xml/features/scanner/notify-builtin-refs");
    fNotifyHtmlBuiltinRefs = manager.getFeature("http://cyberneko.org/html/features/scanner/notify-builtin-refs");
    fFixWindowsCharRefs = manager.getFeature("http://cyberneko.org/html/features/scanner/fix-mswindows-refs");
    fScriptStripCDATADelims = manager.getFeature("http://cyberneko.org/html/features/scanner/script/strip-cdata-delims");
    fScriptStripCommentDelims = manager.getFeature("http://cyberneko.org/html/features/scanner/script/strip-comment-delims");
    fStyleStripCDATADelims = manager.getFeature("http://cyberneko.org/html/features/scanner/style/strip-cdata-delims");
    fStyleStripCommentDelims = manager.getFeature("http://cyberneko.org/html/features/scanner/style/strip-comment-delims");
    fIgnoreSpecifiedCharset = manager.getFeature("http://cyberneko.org/html/features/scanner/ignore-specified-charset");
    fCDATASections = manager.getFeature("http://cyberneko.org/html/features/scanner/cdata-sections");
    fOverrideDoctype = manager.getFeature("http://cyberneko.org/html/features/override-doctype");
    fInsertDoctype = manager.getFeature("http://cyberneko.org/html/features/insert-doctype");
    fNormalizeAttributes = manager.getFeature("http://cyberneko.org/html/features/scanner/normalize-attrs");
    fParseNoScriptContent = manager.getFeature("http://cyberneko.org/html/features/parse-noscript-content");
    fAllowSelfclosingIframe = manager.getFeature("http://cyberneko.org/html/features/scanner/allow-selfclosing-iframe");
    fAllowSelfclosingTags = manager.getFeature("http://cyberneko.org/html/features/scanner/allow-selfclosing-tags");
    

    fNamesElems = getNamesValue(String.valueOf(manager.getProperty("http://cyberneko.org/html/properties/names/elems")));
    fNamesAttrs = getNamesValue(String.valueOf(manager.getProperty("http://cyberneko.org/html/properties/names/attrs")));
    fDefaultIANAEncoding = String.valueOf(manager.getProperty("http://cyberneko.org/html/properties/default-encoding"));
    fErrorReporter = ((HTMLErrorReporter)manager.getProperty("http://cyberneko.org/html/properties/error-reporter"));
    fDoctypePubid = String.valueOf(manager.getProperty("http://cyberneko.org/html/properties/doctype/pubid"));
    fDoctypeSysid = String.valueOf(manager.getProperty("http://cyberneko.org/html/properties/doctype/sysid"));
  }
  



  public void setFeature(String featureId, boolean state)
  {
    if (featureId.equals("http://cyberneko.org/html/features/augmentations")) {
      fAugmentations = state;
    }
    else if (featureId.equals("http://cyberneko.org/html/features/scanner/ignore-specified-charset")) {
      fIgnoreSpecifiedCharset = state;
    }
    else if (featureId.equals("http://apache.org/xml/features/scanner/notify-char-refs")) {
      fNotifyCharRefs = state;
    }
    else if (featureId.equals("http://apache.org/xml/features/scanner/notify-builtin-refs")) {
      fNotifyXmlBuiltinRefs = state;
    }
    else if (featureId.equals("http://cyberneko.org/html/features/scanner/notify-builtin-refs")) {
      fNotifyHtmlBuiltinRefs = state;
    }
    else if (featureId.equals("http://cyberneko.org/html/features/scanner/fix-mswindows-refs")) {
      fFixWindowsCharRefs = state;
    }
    else if (featureId.equals("http://cyberneko.org/html/features/scanner/script/strip-cdata-delims")) {
      fScriptStripCDATADelims = state;
    }
    else if (featureId.equals("http://cyberneko.org/html/features/scanner/script/strip-comment-delims")) {
      fScriptStripCommentDelims = state;
    }
    else if (featureId.equals("http://cyberneko.org/html/features/scanner/style/strip-cdata-delims")) {
      fStyleStripCDATADelims = state;
    }
    else if (featureId.equals("http://cyberneko.org/html/features/scanner/style/strip-comment-delims")) {
      fStyleStripCommentDelims = state;
    }
    else if (featureId.equals("http://cyberneko.org/html/features/parse-noscript-content")) {
      fParseNoScriptContent = state;
    }
    else if (featureId.equals("http://cyberneko.org/html/features/scanner/allow-selfclosing-iframe")) {
      fAllowSelfclosingIframe = state;
    }
    else if (featureId.equals("http://cyberneko.org/html/features/scanner/allow-selfclosing-tags")) {
      fAllowSelfclosingTags = state;
    }
  }
  



  public void setProperty(String propertyId, Object value)
    throws XMLConfigurationException
  {
    if (propertyId.equals("http://cyberneko.org/html/properties/names/elems")) {
      fNamesElems = getNamesValue(String.valueOf(value));
      return;
    }
    
    if (propertyId.equals("http://cyberneko.org/html/properties/names/attrs")) {
      fNamesAttrs = getNamesValue(String.valueOf(value));
      return;
    }
    
    if (propertyId.equals("http://cyberneko.org/html/properties/default-encoding")) {
      fDefaultIANAEncoding = String.valueOf(value);
      return;
    }
  }
  







  public void setInputSource(XMLInputSource source)
    throws IOException
  {
    fElementCount = 0;
    fElementDepth = -1;
    fByteStream = null;
    fCurrentEntityStack.removeAllElements();
    
    fBeginLineNumber = 1;
    fBeginColumnNumber = 1;
    fBeginCharacterOffset = 0;
    fEndLineNumber = fBeginLineNumber;
    fEndColumnNumber = fBeginColumnNumber;
    fEndCharacterOffset = fBeginCharacterOffset;
    

    fIANAEncoding = fDefaultIANAEncoding;
    fJavaEncoding = fIANAEncoding;
    

    String encoding = source.getEncoding();
    String publicId = source.getPublicId();
    String baseSystemId = source.getBaseSystemId();
    String literalSystemId = source.getSystemId();
    String expandedSystemId = expandSystemId(literalSystemId, baseSystemId);
    

    Reader reader = source.getCharacterStream();
    if (reader == null) {
      InputStream inputStream = source.getByteStream();
      if (inputStream == null) {
        URL url = new URL(expandedSystemId);
        inputStream = url.openStream();
      }
      fByteStream = new PlaybackInputStream(inputStream);
      String[] encodings = new String[2];
      if (encoding == null) {
        fByteStream.detectEncoding(encodings);
      }
      else {
        encodings[0] = encoding;
      }
      if (encodings[0] == null) {
        encodings[0] = fDefaultIANAEncoding;
        if (fReportErrors) {
          fErrorReporter.reportWarning("HTML1000", null);
        }
      }
      if (encodings[1] == null) {
        encodings[1] = EncodingMap.getIANA2JavaMapping(encodings[0].toUpperCase(Locale.ENGLISH));
        if (encodings[1] == null) {
          encodings[1] = encodings[0];
          if (fReportErrors) {
            fErrorReporter.reportWarning("HTML1001", new Object[] { encodings[0] });
          }
        }
      }
      fIANAEncoding = encodings[0];
      fJavaEncoding = encodings[1];
      
      fIso8859Encoding = ((fIANAEncoding == null) || 
        (fIANAEncoding.toUpperCase(Locale.ENGLISH).startsWith("ISO-8859")) || 
        (fIANAEncoding.equalsIgnoreCase(fDefaultIANAEncoding)));
      encoding = fIANAEncoding;
      reader = new InputStreamReader(fByteStream, fJavaEncoding);
    }
    fCurrentEntity = new CurrentEntity(reader, encoding, 
      publicId, baseSystemId, 
      literalSystemId, expandedSystemId);
    

    setScanner(fContentScanner);
    setScannerState((short)10);
  }
  
  public boolean scanDocument(boolean complete)
    throws XNIException, IOException
  {
    do
    {
      if (!fScanner.scan(complete)) {
        return false;
      }
    } while (complete);
    return true;
  }
  

  public void setDocumentHandler(XMLDocumentHandler handler)
  {
    fDocumentHandler = handler;
  }
  



  public XMLDocumentHandler getDocumentHandler()
  {
    return fDocumentHandler;
  }
  




  protected static String getValue(XMLAttributes attrs, String aname)
  {
    int length = attrs != null ? attrs.getLength() : 0;
    for (int i = 0; i < length; i++) {
      if (attrs.getQName(i).equalsIgnoreCase(aname)) {
        return attrs.getValue(i);
      }
    }
    return null;
  }
  














  public static String expandSystemId(String systemId, String baseSystemId)
  {
    if ((systemId == null) || (systemId.length() == 0)) {
      return systemId;
    }
    try
    {
      URI uri = new URI(systemId);
      if (uri != null) {
        return systemId;
      }
      

    }
    catch (URI.MalformedURIException localMalformedURIException1)
    {
      String id = fixURI(systemId);
      

      URI base = null;
      URI uri = null;
      try {
        if ((baseSystemId == null) || (baseSystemId.length() == 0) || 
          (baseSystemId.equals(systemId))) {
          String dir;
          try {
            dir = fixURI(System.getProperty("user.dir"));
          } catch (SecurityException se) {
            String dir;
            dir = "";
          }
          if (!dir.endsWith("/")) {
            dir = dir + "/";
          }
          base = new URI("file", "", dir, null, null);
        }
        else {
          try {
            base = new URI(fixURI(baseSystemId));
          }
          catch (URI.MalformedURIException e) {
            String dir;
            try {
              dir = fixURI(System.getProperty("user.dir"));
            } catch (SecurityException se) {
              String dir;
              dir = "";
            }
            if (baseSystemId.indexOf(':') != -1)
            {

              base = new URI("file", "", fixURI(baseSystemId), null, null);
            }
            else {
              if (!dir.endsWith("/")) {
                dir = dir + "/";
              }
              dir = dir + fixURI(baseSystemId);
              base = new URI("file", "", dir, null, null);
            }
          }
        }
        
        uri = new URI(base, id);
      }
      catch (URI.MalformedURIException localMalformedURIException2) {}
      


      if (uri == null) {
        return systemId;
      }
      return uri.toString();
    }
  }
  








  protected static String fixURI(String str)
  {
    str = str.replace(File.separatorChar, '/');
    

    if (str.length() >= 2) {
      char ch1 = str.charAt(1);
      
      if (ch1 == ':') {
        char ch0 = String.valueOf(str.charAt(0)).toUpperCase(Locale.ENGLISH).charAt(0);
        if ((ch0 >= 'A') && (ch0 <= 'Z')) {
          str = "/" + str;
        }
        
      }
      else if ((ch1 == '/') && (str.charAt(0) == '/')) {
        str = "file:" + str;
      }
    }
    

    return str;
  }
  

  protected static final String modifyName(String name, short mode)
  {
    switch (mode) {
    case 1:  return name.toUpperCase(Locale.ENGLISH);
    case 2:  return name.toLowerCase(Locale.ENGLISH);
    }
    return name;
  }
  






  protected static final short getNamesValue(String value)
  {
    if (value.equals("lower")) {
      return 2;
    }
    if (value.equals("upper")) {
      return 1;
    }
    return 0;
  }
  






  protected int fixWindowsCharacter(int origChar)
  {
    switch (origChar) {
    case 130:  return 8218;
    case 131:  return 402;
    case 132:  return 8222;
    case 133:  return 8230;
    case 134:  return 8224;
    case 135:  return 8225;
    case 136:  return 710;
    case 137:  return 8240;
    case 138:  return 352;
    case 139:  return 8249;
    case 140:  return 338;
    case 145:  return 8216;
    case 146:  return 8217;
    case 147:  return 8220;
    case 148:  return 8221;
    case 149:  return 8226;
    case 150:  return 8211;
    case 151:  return 8212;
    case 152:  return 732;
    case 153:  return 8482;
    case 154:  return 353;
    case 155:  return 8250;
    case 156:  return 339;
    case 159:  return 376;
    }
    return origChar;
  }
  




  protected int read()
    throws IOException
  {
    return fCurrentEntity.read();
  }
  



  protected void setScanner(Scanner scanner)
  {
    fScanner = scanner;
  }
  





  protected void setScannerState(short state)
  {
    fScannerState = state;
  }
  











  protected void scanDoctype()
    throws IOException
  {
    String root = null;
    String pubid = null;
    String sysid = null;
    
    if (skipSpaces()) {
      root = scanName(true);
      if (root == null) {
        if (fReportErrors) {
          fErrorReporter.reportError("HTML1014", null);
        }
      }
      else {
        root = modifyName(root, fNamesElems);
      }
      if (skipSpaces()) {
        if (skip("PUBLIC", false)) {
          skipSpaces();
          pubid = scanLiteral();
          if (skipSpaces()) {
            sysid = scanLiteral();
          }
        }
        else if (skip("SYSTEM", false)) {
          skipSpaces();
          sysid = scanLiteral();
        }
      }
    }
    int c;
    while ((c = fCurrentEntity.read()) != -1) { int c;
      if (c == 60) {
        fCurrentEntity.rewind();
        break;
      }
      if (c == 62) {
        break;
      }
      if (c == 91) {
        skipMarkup(true);
        break;
      }
    }
    
    if (fDocumentHandler != null) {
      if (fOverrideDoctype) {
        pubid = fDoctypePubid;
        sysid = fDoctypeSysid;
      }
      fEndLineNumber = fCurrentEntity.getLineNumber();
      fEndColumnNumber = fCurrentEntity.getColumnNumber();
      fEndCharacterOffset = fCurrentEntity.getCharacterOffset();
      fDocumentHandler.doctypeDecl(root, pubid, sysid, locationAugs());
    }
  }
  
  protected String scanLiteral()
    throws IOException
  {
    int quote = fCurrentEntity.read();
    if ((quote == 39) || (quote == 34)) {
      StringBuilder str = new StringBuilder();
      int c;
      while ((c = fCurrentEntity.read()) != -1) { int c;
        if (c == quote) {
          break;
        }
        if ((c == 13) || (c == 10)) {
          fCurrentEntity.rewind();
          

          skipNewlines();
          str.append(' ');
        } else {
          if (c == 60) {
            fCurrentEntity.rewind();
            break;
          }
          
          appendChar(str, c);
        }
      }
      if (c == -1) {
        if (fReportErrors) {
          fErrorReporter.reportError("HTML1007", null);
        }
        throw new EOFException();
      }
      return str.toString();
    }
    fCurrentEntity.rewind();
    return null;
  }
  


  protected String scanName(boolean strict)
    throws IOException
  {
    if ((fCurrentEntity.offset == fCurrentEntity.length) && 
      (fCurrentEntity.load(0) == -1))
    {


      return null;
    }
    
    int offset = fCurrentEntity.offset;
    for (;;)
    {
      char c = fCurrentEntity.getNextChar();
      if (((strict) && (!Character.isLetterOrDigit(c)) && (c != '-') && (c != '.') && (c != ':') && (c != '_')) || (
        (!strict) && ((Character.isWhitespace(c)) || (c == '=') || (c == '/') || (c == '>')))) {
        fCurrentEntity.rewind();
      }
      while (!fCurrentEntity.hasNext())
      {






        if (fCurrentEntity.offset != fCurrentEntity.length) break label205;
        int length = fCurrentEntity.length - offset;
        System.arraycopy(fCurrentEntity.buffer, offset, fCurrentEntity.buffer, 0, length);
        int count = fCurrentEntity.load(length);
        offset = 0;
        if (count == -1) {
          break label205;
        }
      }
    }
    
    label205:
    
    int length = fCurrentEntity.offset - offset;
    String name = length > 0 ? new String(fCurrentEntity.buffer, offset, length) : null;
    


    return name;
  }
  
  protected int scanEntityRef(XMLStringBuffer str, boolean content)
    throws IOException
  {
    str.clear();
    str.append('&');
    boolean endsWithSemicolon = false;
    for (;;) {
      int c = fCurrentEntity.read();
      if (c == 59) {
        str.append(';');
        endsWithSemicolon = true;
        break;
      }
      if (c == -1) {
        break;
      }
      if ((!ENTITY_CHARS.get(c)) && (c != 35)) {
        fCurrentEntity.rewind();
        break;
      }
      appendChar(str, c);
    }
    
    if ((!endsWithSemicolon) && 
      (fReportErrors)) {
      fErrorReporter.reportWarning("HTML1004", null);
    }
    
    if (length == 1) {
      if ((content) && (fDocumentHandler != null) && (fElementCount >= fElementDepth)) {
        fEndLineNumber = fCurrentEntity.getLineNumber();
        fEndColumnNumber = fCurrentEntity.getColumnNumber();
        fEndCharacterOffset = fCurrentEntity.getCharacterOffset();
        fDocumentHandler.characters(str, locationAugs());
      }
      return -1;
    }
    String name;
    String name;
    if (endsWithSemicolon) {
      name = str.toString().substring(1, length - 1);
    } else {
      name = str.toString().substring(1);
    }
    if (name.startsWith("#")) {
      int value = -1;
      try {
        if ((name.startsWith("#x")) || (name.startsWith("#X"))) {
          value = Integer.parseInt(name.substring(2), 16);
        }
        else {
          value = Integer.parseInt(name.substring(1));
        }
        
        if ((fFixWindowsCharRefs) && (fIso8859Encoding)) {
          value = fixWindowsCharacter(value);
        }
        if ((content) && (fDocumentHandler != null) && (fElementCount >= fElementDepth)) {
          fEndLineNumber = fCurrentEntity.getLineNumber();
          fEndColumnNumber = fCurrentEntity.getColumnNumber();
          fEndCharacterOffset = fCurrentEntity.getCharacterOffset();
          if (fNotifyCharRefs) {
            XMLResourceIdentifier id = resourceId();
            String encoding = null;
            fDocumentHandler.startGeneralEntity(name, id, encoding, locationAugs());
          }
          str.clear();
          try {
            appendChar(str, value);
          }
          catch (IllegalArgumentException e) {
            if (fReportErrors) {
              fErrorReporter.reportError("HTML1005", new Object[] { name });
            }
            str.append(65533);
          }
          fDocumentHandler.characters(str, locationAugs());
          if (fNotifyCharRefs) {
            fDocumentHandler.endGeneralEntity(name, locationAugs());
          }
        }
      }
      catch (NumberFormatException e) {
        if (fReportErrors) {
          fErrorReporter.reportError("HTML1005", new Object[] { name });
        }
        if ((content) && (fDocumentHandler != null) && (fElementCount >= fElementDepth)) {
          fEndLineNumber = fCurrentEntity.getLineNumber();
          fEndColumnNumber = fCurrentEntity.getColumnNumber();
          fEndCharacterOffset = fCurrentEntity.getCharacterOffset();
          fDocumentHandler.characters(str, locationAugs());
        }
      }
      return value;
    }
    
    int c = HTMLEntities.get(name);
    


    boolean invalidEntityInAttribute = (!content) && (!endsWithSemicolon) && (c > 256);
    if ((c == -1) || (invalidEntityInAttribute)) {
      if (fReportErrors) {
        fErrorReporter.reportWarning("HTML1006", new Object[] { name });
      }
      if ((content) && (fDocumentHandler != null) && (fElementCount >= fElementDepth)) {
        fEndLineNumber = fCurrentEntity.getLineNumber();
        fEndColumnNumber = fCurrentEntity.getColumnNumber();
        fEndCharacterOffset = fCurrentEntity.getCharacterOffset();
        fDocumentHandler.characters(str, locationAugs());
      }
      return -1;
    }
    if ((content) && (fDocumentHandler != null) && (fElementCount >= fElementDepth)) {
      fEndLineNumber = fCurrentEntity.getLineNumber();
      fEndColumnNumber = fCurrentEntity.getColumnNumber();
      fEndCharacterOffset = fCurrentEntity.getCharacterOffset();
      boolean notify = (fNotifyHtmlBuiltinRefs) || ((fNotifyXmlBuiltinRefs) && (builtinXmlRef(name)));
      if (notify) {
        XMLResourceIdentifier id = resourceId();
        String encoding = null;
        fDocumentHandler.startGeneralEntity(name, id, encoding, locationAugs());
      }
      str.clear();
      appendChar(str, c);
      fDocumentHandler.characters(str, locationAugs());
      if (notify) {
        fDocumentHandler.endGeneralEntity(name, locationAugs());
      }
    }
    return c;
  }
  
  protected boolean skip(String s, boolean caseSensitive)
    throws IOException
  {
    int length = s != null ? s.length() : 0;
    for (int i = 0; i < length; i++) {
      if (fCurrentEntity.offset == fCurrentEntity.length) {
        System.arraycopy(fCurrentEntity.buffer, fCurrentEntity.offset - i, fCurrentEntity.buffer, 0, i);
        if (fCurrentEntity.load(i) == -1) {
          fCurrentEntity.offset = 0;
          return false;
        }
      }
      char c0 = s.charAt(i);
      char c1 = fCurrentEntity.getNextChar();
      if (!caseSensitive) {
        c0 = String.valueOf(c0).toUpperCase(Locale.ENGLISH).charAt(0);
        c1 = String.valueOf(c1).toUpperCase(Locale.ENGLISH).charAt(0);
      }
      if (c0 != c1) {
        fCurrentEntity.rewind(i + 1);
        return false;
      }
    }
    return true;
  }
  


  protected boolean skipMarkup(boolean balance)
    throws IOException
  {
    int depth = 1;
    boolean slashgt = false;
    for (;; 
        




        fCurrentEntity.hasNext()) {
      if ((fCurrentEntity.offset == fCurrentEntity.length) && 
        (fCurrentEntity.load(0) == -1))
      {



        break;char c = fCurrentEntity.getNextChar();
        if ((balance) && (c == '<')) {
          depth++;
        }
        else if (c == '>') {
          depth--;
          if (depth == 0) {
            break;
          }
        }
        else if (c == '/') {
          if ((fCurrentEntity.offset == fCurrentEntity.length) && 
            (fCurrentEntity.load(0) == -1)) {
            break;
          }
          
          c = fCurrentEntity.getNextChar();
          if (c == '>') {
            slashgt = true;
            depth--;
            if (depth == 0) {
              break;
            }
          }
          else {
            fCurrentEntity.rewind();
          }
        }
        else if ((c == '\r') || (c == '\n')) {
          fCurrentEntity.rewind();
          skipNewlines();
        }
      }
    }
    


    return slashgt;
  }
  


  protected boolean skipSpaces()
    throws IOException
  {
    boolean spaces = false;
    
    while ((fCurrentEntity.offset != fCurrentEntity.length) || 
      (fCurrentEntity.load(0) != -1))
    {


      char c = fCurrentEntity.getNextChar();
      if (!Character.isWhitespace(c)) {
        fCurrentEntity.rewind();
        break;
      }
      spaces = true;
      if ((c == '\r') || (c == '\n')) {
        fCurrentEntity.rewind();
        skipNewlines();
      }
    }
    



    return spaces;
  }
  



  protected int skipNewlines()
    throws IOException
  {
    if ((!fCurrentEntity.hasNext()) && 
      (fCurrentEntity.load(0) == -1))
    {


      return 0;
    }
    
    char c = fCurrentEntity.getCurrentChar();
    int newlines = 0;
    if ((c == '\n') || (c == '\r')) {
      do {
        c = fCurrentEntity.getNextChar();
        if (c == '\r') {
          newlines++;
          if (fCurrentEntity.offset == fCurrentEntity.length) {
            fCurrentEntity.offset = newlines;
            if (fCurrentEntity.load(newlines) == -1) {
              break;
            }
          }
          if (fCurrentEntity.getCurrentChar() == '\n') {
            fCurrentEntity.offset += 1;
            fCurrentEntity.characterOffset_ += 1;
          }
        }
        else if (c == '\n') {
          newlines++;
          if (fCurrentEntity.offset == fCurrentEntity.length) {
            fCurrentEntity.offset = newlines;
            if (fCurrentEntity.load(newlines) == -1) {
              break;
            }
          }
        }
        else {
          fCurrentEntity.rewind();
          break;
        }
      } while (
      


























        fCurrentEntity.offset < fCurrentEntity.length - 1);
      fCurrentEntity.incLine(newlines);
    }
    


    return newlines;
  }
  


  protected final Augmentations locationAugs()
  {
    HTMLAugmentations augs = null;
    if (fAugmentations) {
      fLocationItem.setValues(fBeginLineNumber, fBeginColumnNumber, 
        fBeginCharacterOffset, fEndLineNumber, 
        fEndColumnNumber, fEndCharacterOffset);
      augs = fInfosetAugs;
      augs.removeAllItems();
      augs.putItem("http://cyberneko.org/html/features/augmentations", fLocationItem);
    }
    return augs;
  }
  
  protected final Augmentations synthesizedAugs()
  {
    HTMLAugmentations augs = null;
    if (fAugmentations) {
      augs = fInfosetAugs;
      augs.removeAllItems();
      augs.putItem("http://cyberneko.org/html/features/augmentations", SYNTHESIZED_ITEM);
    }
    return augs;
  }
  

  protected final XMLResourceIdentifier resourceId()
  {
    fResourceId.clear();
    return fResourceId;
  }
  










  protected static boolean builtinXmlRef(String name)
  {
    return (name.equals("amp")) || (name.equals("lt")) || (name.equals("gt")) || 
      (name.equals("quot")) || (name.equals("apos"));
  }
  











  private void appendChar(XMLStringBuffer str, int value)
  {
    if (value > 65535)
    {
      char[] chars = Character.toChars(value);
      
      str.append(chars, 0, chars.length);
    }
    else
    {
      str.append((char)value);
    }
  }
  







  private void appendChar(StringBuilder str, int value)
  {
    if (value > 65535)
    {
      char[] chars = Character.toChars(value);
      
      str.append(chars, 0, chars.length);
    }
    else
    {
      str.append((char)value);
    }
  }
  








  public static class CurrentEntity
  {
    private Reader stream_;
    







    private String encoding;
    







    public final String publicId;
    







    public final String baseSystemId;
    







    public final String literalSystemId;
    







    public final String expandedSystemId;
    







    public final String version = "1.0";
    

    private int lineNumber_ = 1;
    

    private int columnNumber_ = 1;
    

    public int characterOffset_ = 0;
    



    public char[] buffer = new char['ࠀ'];
    

    public int offset = 0;
    

    public int length = 0;
    
    private boolean endReached_ = false;
    






    public CurrentEntity(Reader stream, String encoding, String publicId, String baseSystemId, String literalSystemId, String expandedSystemId)
    {
      stream_ = stream;
      this.encoding = encoding;
      this.publicId = publicId;
      this.baseSystemId = baseSystemId;
      this.literalSystemId = literalSystemId;
      this.expandedSystemId = expandedSystemId;
    }
    
    private char getCurrentChar() {
      return buffer[offset];
    }
    



    private char getNextChar()
    {
      characterOffset_ += 1;
      columnNumber_ += 1;
      return buffer[(offset++)];
    }
    
    private void closeQuietly() {
      try { stream_.close();
      }
      catch (IOException localIOException) {}
    }
    




    boolean hasNext()
    {
      return offset < length;
    }
    








    protected int load(int offset)
      throws IOException
    {
      if (offset == buffer.length) {
        int adjust = buffer.length / 4;
        char[] array = new char[buffer.length + adjust];
        System.arraycopy(buffer, 0, array, 0, length);
        buffer = array;
      }
      
      int count = stream_.read(buffer, offset, buffer.length - offset);
      if (count == -1) {
        endReached_ = true;
      }
      length = (count != -1 ? count + offset : offset);
      this.offset = offset;
      


      return count;
    }
    


    protected int read()
      throws IOException
    {
      if (offset == length) {
        if (endReached_) {
          return -1;
        }
        if (load(0) == -1)
        {


          return -1;
        }
      }
      char c = buffer[(offset++)];
      characterOffset_ += 1;
      columnNumber_ += 1;
      



      return c;
    }
    
    private void debugBufferIfNeeded(String prefix)
    {
      debugBufferIfNeeded(prefix, "");
    }
    






















    private void debugBufferIfNeeded(String prefix, String suffix) {}
    






















    private void setStream(InputStreamReader inputStreamReader)
    {
      stream_ = inputStreamReader;
      offset = (this.length = this.characterOffset_ = 0);
      lineNumber_ = (this.columnNumber_ = 1);
      encoding = inputStreamReader.getEncoding();
    }
    


    private void rewind()
    {
      offset -= 1;
      characterOffset_ -= 1;
      columnNumber_ -= 1;
    }
    
    private void rewind(int i) { offset -= i;
      characterOffset_ -= i;
      columnNumber_ -= i;
    }
    
    private void incLine() {
      lineNumber_ += 1;
      columnNumber_ = 1;
    }
    
    private void incLine(int nbLines) {
      lineNumber_ += nbLines;
      columnNumber_ = 1;
    }
    
    public int getLineNumber() {
      return lineNumber_;
    }
    
    private void resetBuffer(XMLStringBuffer buffer, int lineNumber, int columnNumber, int characterOffset)
    {
      lineNumber_ = lineNumber;
      columnNumber_ = columnNumber;
      characterOffset_ = characterOffset;
      this.buffer = ch;
      offset = offset;
      length = length;
    }
    
    private int getColumnNumber() {
      return columnNumber_;
    }
    
    private void restorePosition(int originalOffset, int originalColumnNumber, int originalCharacterOffset)
    {
      offset = originalOffset;
      columnNumber_ = originalColumnNumber;
      characterOffset_ = originalCharacterOffset;
    }
    
    private int getCharacterOffset() {
      return characterOffset_;
    }
  }
  












  public class ContentScanner
    implements HTMLScanner.Scanner
  {
    private final QName fQName = new QName();
    

    private final XMLAttributesImpl fAttributes = new XMLAttributesImpl();
    
    public ContentScanner() {}
    
    public boolean scan(boolean complete)
      throws IOException
    {
      boolean next;
      do
      {
        try
        {
          boolean next = false;
          switch (fScannerState) {
          case 0: 
            fBeginLineNumber = fCurrentEntity.getLineNumber();
            fBeginColumnNumber = HTMLScanner.CurrentEntity.access$0(fCurrentEntity);
            fBeginCharacterOffset = HTMLScanner.CurrentEntity.access$1(fCurrentEntity);
            int c = fCurrentEntity.read();
            if (c == 60) {
              setScannerState((short)1);
              next = true;
            }
            else if (c == 38) {
              scanEntityRef(fStringBuffer, true);
            } else {
              if (c == -1) {
                throw new EOFException();
              }
              
              HTMLScanner.CurrentEntity.access$2(fCurrentEntity);
              scanCharacters();
            }
            break;
          
          case 1: 
            int c = fCurrentEntity.read();
            if (c == 33) {
              if (skip("--", false)) {
                scanComment();
              }
              else if (skip("[CDATA[", false)) {
                scanCDATA();
              }
              else if (skip("DOCTYPE", false)) {
                scanDoctype();
              }
              else {
                if (fReportErrors) {
                  fErrorReporter.reportError("HTML1002", null);
                }
                skipMarkup(true);
              }
            }
            else if (c == 63) {
              scanPI();
            }
            else if (c == 47) {
              scanEndElement();
            } else {
              if (c == -1) {
                if (fReportErrors) {
                  fErrorReporter.reportError("HTML1003", null);
                }
                if ((fDocumentHandler != null) && (fElementCount >= fElementDepth)) {
                  fStringBuffer.clear();
                  fStringBuffer.append('<');
                  fDocumentHandler.characters(fStringBuffer, null);
                }
                throw new EOFException();
              }
              
              HTMLScanner.CurrentEntity.access$2(fCurrentEntity);
              fElementCount += 1;
              fSingleBoolean[0] = 0;
              String ename = scanStartElement(fSingleBoolean);
              String enameLC = ename == null ? null : ename.toLowerCase();
              fBeginLineNumber = fCurrentEntity.getLineNumber();
              fBeginColumnNumber = HTMLScanner.CurrentEntity.access$0(fCurrentEntity);
              fBeginCharacterOffset = HTMLScanner.CurrentEntity.access$1(fCurrentEntity);
              if ("script".equals(enameLC)) {
                scanScriptContent();
              }
              else if ((!fAllowSelfclosingTags) && (!fAllowSelfclosingIframe) && ("iframe".equals(enameLC))) {
                scanUntilEndTag("iframe");
              }
              else if ((!fParseNoScriptContent) && ("noscript".equals(enameLC))) {
                scanUntilEndTag("noscript");
              }
              else if ((!fParseNoFramesContent) && ("noframes".equals(enameLC))) {
                scanUntilEndTag("noframes");
              }
              else if ((ename != null) && (fSingleBoolean[0] == 0) && 
                (htmlConfiguration_.htmlElements_.getElement(enameLC).isSpecial()) && (
                (!ename.equalsIgnoreCase("TITLE")) || (isEnded(enameLC)))) {
                if (ename.equalsIgnoreCase("PLAINTEXT")) {
                  setScanner(new HTMLScanner.PlainTextScanner(HTMLScanner.this));
                }
                else {
                  setScanner(fSpecialScanner.setElementName(ename));
                  setScannerState((short)0);
                }
                return true;
              }
            }
            setScannerState((short)0);
            break;
          
          case 10: 
            if ((fDocumentHandler != null) && (fElementCount >= fElementDepth))
            {


              XMLLocator locator = HTMLScanner.this;
              String encoding = fIANAEncoding;
              Augmentations augs = locationAugs();
              NamespaceContext nscontext = new NamespaceSupport();
              XercesBridge.getInstance().XMLDocumentHandler_startDocument(fDocumentHandler, locator, encoding, nscontext, augs);
            }
            if ((fInsertDoctype) && (fDocumentHandler != null)) {
              String root = htmlConfiguration_.htmlElements_.getElement((short)54).name;
              root = HTMLScanner.modifyName(root, fNamesElems);
              String pubid = fDoctypePubid;
              String sysid = fDoctypeSysid;
              fDocumentHandler.doctypeDecl(root, pubid, sysid, 
                synthesizedAugs());
            }
            setScannerState((short)0);
            break;
          
          case 11: 
            if ((fDocumentHandler != null) && (fElementCount >= fElementDepth) && (complete))
            {


              fEndLineNumber = fCurrentEntity.getLineNumber();
              fEndColumnNumber = HTMLScanner.CurrentEntity.access$0(fCurrentEntity);
              fEndCharacterOffset = HTMLScanner.CurrentEntity.access$1(fCurrentEntity);
              fDocumentHandler.endDocument(locationAugs());
            }
            return false;
          
          default: 
            throw new RuntimeException("unknown scanner state: " + fScannerState);
          }
        }
        catch (EOFException e)
        {
          if (fCurrentEntityStack.empty()) {
            setScannerState((short)11);
          }
          else {
            fCurrentEntity = ((HTMLScanner.CurrentEntity)fCurrentEntityStack.pop());
          }
          next = true;
        }
      } while ((next) || (complete));
      return true;
    }
    




    private void scanUntilEndTag(String tagName)
      throws IOException
    {
      XMLStringBuffer buffer = new XMLStringBuffer();
      String end = "/" + tagName;
      int lengthToScan = tagName.length() + 2;
      for (;;)
      {
        int c = fCurrentEntity.read();
        if (c == -1) {
          break;
        }
        if (c == 60) {
          String next = nextContent(lengthToScan) + " ";
          if ((next.length() >= lengthToScan) && (end.equalsIgnoreCase(next.substring(0, end.length()))) && (
            ('>' == next.charAt(lengthToScan - 1)) || (Character.isWhitespace(next.charAt(lengthToScan - 1))))) {
            HTMLScanner.CurrentEntity.access$2(fCurrentEntity);
            break;
          }
        }
        if ((c == 13) || (c == 10)) {
          HTMLScanner.CurrentEntity.access$2(fCurrentEntity);
          int newlines = skipNewlines();
          for (int i = 0; i < newlines; i++) {
            buffer.append('\n');
          }
        }
        else {
          HTMLScanner.this.appendChar(buffer, c);
        }
      }
      if ((length > 0) && (fDocumentHandler != null)) {
        fEndLineNumber = fCurrentEntity.getLineNumber();
        fEndColumnNumber = HTMLScanner.CurrentEntity.access$0(fCurrentEntity);
        fEndCharacterOffset = HTMLScanner.CurrentEntity.access$1(fCurrentEntity);
        fDocumentHandler.characters(buffer, locationAugs());
      }
    }
    
    private void scanScriptContent() throws IOException
    {
      XMLStringBuffer buffer = new XMLStringBuffer();
      boolean waitForEndComment = false;
      for (;;) {
        int c = fCurrentEntity.read();
        if (c == -1) {
          break;
        }
        if ((c == 45) && (HTMLScanner.this.endsWith(buffer, "<!-")))
        {
          waitForEndComment = HTMLScanner.this.endCommentAvailable();
        }
        else if ((!waitForEndComment) && (c == 60)) {
          String next = nextContent(8) + " ";
          if ((next.length() >= 8) && ("/script".equalsIgnoreCase(next.substring(0, 7))) && (
            ('>' == next.charAt(7)) || (Character.isWhitespace(next.charAt(7))))) {
            HTMLScanner.CurrentEntity.access$2(fCurrentEntity);
            break;
          }
        }
        else if ((c == 62) && (HTMLScanner.this.endsWith(buffer, "--"))) {
          waitForEndComment = false;
        }
        
        if ((c == 13) || (c == 10)) {
          HTMLScanner.CurrentEntity.access$2(fCurrentEntity);
          int newlines = skipNewlines();
          for (int i = 0; i < newlines; i++) {
            buffer.append('\n');
          }
        }
        else {
          HTMLScanner.this.appendChar(buffer, c);
        }
      }
      
      if (fScriptStripCommentDelims) {
        HTMLScanner.reduceToContent(buffer, "<!--", "-->");
      }
      if (fScriptStripCDATADelims) {
        HTMLScanner.reduceToContent(buffer, "<![CDATA[", "]]>");
      }
      
      if ((length > 0) && (fDocumentHandler != null) && (fElementCount >= fElementDepth))
      {


        fEndLineNumber = fCurrentEntity.getLineNumber();
        fEndColumnNumber = HTMLScanner.CurrentEntity.access$0(fCurrentEntity);
        fEndCharacterOffset = HTMLScanner.CurrentEntity.access$1(fCurrentEntity);
        fDocumentHandler.characters(buffer, locationAugs());
      }
    }
    





    protected String nextContent(int len)
      throws IOException
    {
      int originalOffset = fCurrentEntity.offset;
      int originalColumnNumber = HTMLScanner.CurrentEntity.access$0(fCurrentEntity);
      int originalCharacterOffset = HTMLScanner.CurrentEntity.access$1(fCurrentEntity);
      
      char[] buff = new char[len];
      int nbRead = 0;
      for (nbRead = 0; nbRead < len; nbRead++)
      {
        if (fCurrentEntity.offset == fCurrentEntity.length) {
          if (fCurrentEntity.length != fCurrentEntity.buffer.length) break;
          fCurrentEntity.load(fCurrentEntity.buffer.length);
        }
        




        int c = fCurrentEntity.read();
        if (c == -1) {
          break;
        }
        buff[nbRead] = ((char)c);
      }
      HTMLScanner.CurrentEntity.access$3(fCurrentEntity, originalOffset, originalColumnNumber, originalCharacterOffset);
      return new String(buff, 0, nbRead);
    }
    






    protected void scanCharacters()
      throws IOException
    {
      fStringBuffer.clear();
      int next;
      do { int newlines = skipNewlines();
        if ((newlines == 0) && (fCurrentEntity.offset == fCurrentEntity.length)) {
          break;
        }
        



        int offset = fCurrentEntity.offset - newlines;
        for (int i = offset; i < fCurrentEntity.offset; i++) {
          fCurrentEntity.buffer[i] = '\n';
        }
        while (fCurrentEntity.hasNext()) {
          char c = HTMLScanner.CurrentEntity.access$4(fCurrentEntity);
          if ((c == '<') || (c == '&') || (c == '\n') || (c == '\r')) {
            HTMLScanner.CurrentEntity.access$2(fCurrentEntity);
            break;
          }
        }
        if ((fCurrentEntity.offset > offset) && 
          (fDocumentHandler != null) && (fElementCount >= fElementDepth))
        {



          fEndLineNumber = fCurrentEntity.getLineNumber();
          fEndColumnNumber = HTMLScanner.CurrentEntity.access$0(fCurrentEntity);
          fEndCharacterOffset = HTMLScanner.CurrentEntity.access$1(fCurrentEntity);
          fStringBuffer.append(fCurrentEntity.buffer, offset, fCurrentEntity.offset - offset);
        }
        



        boolean hasNext = fCurrentEntity.offset < fCurrentEntity.buffer.length;
        next = hasNext ? HTMLScanner.CurrentEntity.access$5(fCurrentEntity) : -1;
      }
      while ((next != 38) && (next != 60) && (next != -1));
      




      if (fStringBuffer.length != 0) {
        fDocumentHandler.characters(fStringBuffer, locationAugs());
      }
    }
    



    protected void scanCDATA()
      throws IOException
    {
      fStringBuffer.clear();
      if (fCDATASections) {
        if ((fDocumentHandler != null) && (fElementCount >= fElementDepth)) {
          fEndLineNumber = fCurrentEntity.getLineNumber();
          fEndColumnNumber = HTMLScanner.CurrentEntity.access$0(fCurrentEntity);
          fEndCharacterOffset = HTMLScanner.CurrentEntity.access$1(fCurrentEntity);
          


          fDocumentHandler.startCDATA(locationAugs());
        }
      }
      else {
        fStringBuffer.append("[CDATA[");
      }
      boolean eof = scanMarkupContent(fStringBuffer, ']');
      if (!fCDATASections) {
        fStringBuffer.append("]]");
      }
      if ((fDocumentHandler != null) && (fElementCount >= fElementDepth)) {
        fEndLineNumber = fCurrentEntity.getLineNumber();
        fEndColumnNumber = HTMLScanner.CurrentEntity.access$0(fCurrentEntity);
        fEndCharacterOffset = HTMLScanner.CurrentEntity.access$1(fCurrentEntity);
        if (fCDATASections)
        {


          fDocumentHandler.characters(fStringBuffer, locationAugs());
          


          fDocumentHandler.endCDATA(locationAugs());

        }
        else
        {

          fDocumentHandler.comment(fStringBuffer, locationAugs());
        }
      }
      


      if (eof) {
        throw new EOFException();
      }
    }
    


    protected void scanComment()
      throws IOException
    {
      fEndLineNumber = fCurrentEntity.getLineNumber();
      fEndColumnNumber = HTMLScanner.CurrentEntity.access$0(fCurrentEntity);
      fEndCharacterOffset = HTMLScanner.CurrentEntity.access$1(fCurrentEntity);
      XMLStringBuffer buffer = new XMLStringBuffer();
      boolean eof = scanMarkupContent(buffer, '-');
      
      if (eof) {
        HTMLScanner.CurrentEntity.access$6(fCurrentEntity, buffer, fEndLineNumber, fEndColumnNumber, fEndCharacterOffset);
        buffer = new XMLStringBuffer();
        for (;;) {
          int c = fCurrentEntity.read();
          if (c == -1) {
            if (fReportErrors) {
              fErrorReporter.reportError("HTML1007", null);
            }
            eof = true;
            break label234;
          }
          if (c != 62) {
            HTMLScanner.this.appendChar(buffer, c);
          }
          else {
            if ((c != 10) && (c != 13)) break;
            HTMLScanner.CurrentEntity.access$2(fCurrentEntity);
            int newlines = skipNewlines();
            for (int i = 0; i < newlines; i++) {
              buffer.append('\n');
            }
          }
        }
        eof = false;
      }
      
      label234:
      if ((fDocumentHandler != null) && (fElementCount >= fElementDepth))
      {


        fEndLineNumber = fCurrentEntity.getLineNumber();
        fEndColumnNumber = HTMLScanner.CurrentEntity.access$0(fCurrentEntity);
        fEndCharacterOffset = HTMLScanner.CurrentEntity.access$1(fCurrentEntity);
        fDocumentHandler.comment(buffer, locationAugs());
      }
      


      if (eof) {
        throw new EOFException();
      }
    }
    
    protected boolean scanMarkupContent(XMLStringBuffer buffer, char cend)
      throws IOException
    {
      int c = -1;
      for (;;) {
        c = fCurrentEntity.read();
        if (c == cend) {
          int count = 1;
          for (;;) {
            c = fCurrentEntity.read();
            if (c != cend) break;
            count++;
          }
          


          if (c == -1) {
            if (!fReportErrors) break;
            fErrorReporter.reportError("HTML1007", null);
            
            break;
          }
          if (count < 2) {
            buffer.append(cend);
            
            HTMLScanner.CurrentEntity.access$2(fCurrentEntity);


          }
          else if (c != 62) {
            for (int i = 0; i < count; i++) {
              buffer.append(cend);
            }
            HTMLScanner.CurrentEntity.access$2(fCurrentEntity);
          }
          else {
            for (int i = 0; i < count - 2; i++) {
              buffer.append(cend);
            }
            break;
          }
        } else if ((c == 10) || (c == 13)) {
          HTMLScanner.CurrentEntity.access$2(fCurrentEntity);
          int newlines = skipNewlines();
          for (int i = 0; i < newlines; i++) {
            buffer.append('\n');
          }
        }
        else {
          if (c == -1) {
            if (!fReportErrors) break;
            fErrorReporter.reportError("HTML1007", null);
            
            break;
          }
          HTMLScanner.this.appendChar(buffer, c);
        } }
      return c == -1;
    }
    


    protected void scanPI()
      throws IOException
    {
      if (fReportErrors) {
        fErrorReporter.reportWarning("HTML1008", null);
      }
      

      String target = scanName(true);
      if ((target != null) && (!target.equalsIgnoreCase("xml"))) {
        int c;
        do { for (;;) { c = fCurrentEntity.read();
            if ((c != 13) && (c != 10)) break;
            if (c == 13) {
              c = fCurrentEntity.read();
              if (c != 10) {
                fCurrentEntity.offset -= 1;
                fCurrentEntity.characterOffset_ -= 1;
              }
            }
            HTMLScanner.CurrentEntity.access$7(fCurrentEntity);
          }
          
          if (c == -1) {
            break;
          }
        } while ((c == 32) || (c == 9));
        HTMLScanner.CurrentEntity.access$2(fCurrentEntity);
        


        fStringBuffer.clear();
        for (;;) {
          int c = fCurrentEntity.read();
          if ((c == 63) || (c == 47)) {
            char c0 = (char)c;
            c = fCurrentEntity.read();
            if (c == 62) {
              break;
            }
            fStringBuffer.append(c0);
            HTMLScanner.CurrentEntity.access$2(fCurrentEntity);

          }
          else if ((c == 13) || (c == 10)) {
            fStringBuffer.append('\n');
            if (c == 13) {
              c = fCurrentEntity.read();
              if (c != 10) {
                fCurrentEntity.offset -= 1;
                fCurrentEntity.characterOffset_ -= 1;
              }
            }
            HTMLScanner.CurrentEntity.access$7(fCurrentEntity);
          }
          else {
            if (c == -1) {
              break;
            }
            
            HTMLScanner.this.appendChar(fStringBuffer, c);
          }
        }
        XMLString data = fStringBuffer;
        if (fDocumentHandler != null) {
          fEndLineNumber = fCurrentEntity.getLineNumber();
          fEndColumnNumber = HTMLScanner.CurrentEntity.access$0(fCurrentEntity);
          fEndCharacterOffset = HTMLScanner.CurrentEntity.access$1(fCurrentEntity);
          fDocumentHandler.processingInstruction(target, data, locationAugs());
        }
        
      }
      else
      {
        int beginLineNumber = fBeginLineNumber;
        int beginColumnNumber = fBeginColumnNumber;
        int beginCharacterOffset = fBeginCharacterOffset;
        fAttributes.removeAllAttributes();
        int aindex = 0;
        while (scanPseudoAttribute(fAttributes))
        {
          if (fAttributes.getValue(aindex).length() == 0) {
            fAttributes.removeAttributeAt(aindex);
          }
          else {
            fAttributes.getName(aindex, fQName);
            fQName.rawname = fQName.rawname.toLowerCase();
            fAttributes.setName(aindex, fQName);
            aindex++;
          }
        }
        if (fDocumentHandler != null) {
          String version = fAttributes.getValue("version");
          String encoding = fAttributes.getValue("encoding");
          String standalone = fAttributes.getValue("standalone");
          


          boolean xmlDeclNow = (fIgnoreSpecifiedCharset) || (!changeEncoding(encoding));
          if (xmlDeclNow) {
            fBeginLineNumber = beginLineNumber;
            fBeginColumnNumber = beginColumnNumber;
            fBeginCharacterOffset = beginCharacterOffset;
            fEndLineNumber = fCurrentEntity.getLineNumber();
            fEndColumnNumber = HTMLScanner.CurrentEntity.access$0(fCurrentEntity);
            fEndCharacterOffset = HTMLScanner.CurrentEntity.access$1(fCurrentEntity);
            fDocumentHandler.xmlDecl(version, encoding, standalone, 
              locationAugs());
          }
        }
      }
    }
    








    protected String scanStartElement(boolean[] empty)
      throws IOException
    {
      String ename = scanName(true);
      int length = ename != null ? ename.length() : 0;
      int c = length > 0 ? ename.charAt(0) : -1;
      if ((length == 0) || (((c < 97) || (c > 122)) && ((c < 65) || (c > 90)))) {
        if (fReportErrors) {
          fErrorReporter.reportError("HTML1009", null);
        }
        if ((fDocumentHandler != null) && (fElementCount >= fElementDepth)) {
          fStringBuffer.clear();
          fStringBuffer.append('<');
          if (length > 0) {
            fStringBuffer.append(ename);
          }
          fDocumentHandler.characters(fStringBuffer, null);
        }
        return null;
      }
      ename = HTMLScanner.modifyName(ename, fNamesElems);
      fAttributes.removeAllAttributes();
      int beginLineNumber = fBeginLineNumber;
      int beginColumnNumber = fBeginColumnNumber;
      int beginCharacterOffset = fBeginCharacterOffset;
      while (scanAttribute(fAttributes, empty)) {}
      

      fBeginLineNumber = beginLineNumber;
      fBeginColumnNumber = beginColumnNumber;
      fBeginCharacterOffset = beginCharacterOffset;
      if ((fByteStream != null) && (fElementDepth == -1)) {
        if ((ename.equalsIgnoreCase("META")) && (!fIgnoreSpecifiedCharset))
        {


          String httpEquiv = HTMLScanner.getValue(fAttributes, "http-equiv");
          if ((httpEquiv != null) && (httpEquiv.equalsIgnoreCase("content-type")))
          {


            String content = HTMLScanner.getValue(fAttributes, "content");
            if (content != null) {
              content = removeSpaces(content);
              int index1 = content.toLowerCase().indexOf("charset=");
              if (index1 != -1) {
                int index2 = content.indexOf(';', index1);
                String charset = index2 != -1 ? content.substring(index1 + 8, index2) : content.substring(index1 + 8);
                changeEncoding(charset);
              }
            }
          }
          else {
            String metaCharset = HTMLScanner.getValue(fAttributes, "charset");
            if (metaCharset != null) {
              changeEncoding(metaCharset);
            }
          }
        }
        else if (ename.equalsIgnoreCase("BODY")) {
          fByteStream.clear();
          fByteStream = null;
        }
        else {
          HTMLElements.Element element = htmlConfiguration_.htmlElements_.getElement(ename);
          if ((parent != null) && (parent.length > 0) && 
            (parent[0].code == 16)) {
            fByteStream.clear();
            fByteStream = null;
          }
        }
      }
      
      if ((fDocumentHandler != null) && (fElementCount >= fElementDepth)) {
        fQName.setValues(null, ename, ename, null);
        


        fEndLineNumber = fCurrentEntity.getLineNumber();
        fEndColumnNumber = HTMLScanner.CurrentEntity.access$0(fCurrentEntity);
        fEndCharacterOffset = HTMLScanner.CurrentEntity.access$1(fCurrentEntity);
        if ((empty[0] != 0) && (!"BR".equalsIgnoreCase(ename))) {
          fDocumentHandler.emptyElement(fQName, fAttributes, locationAugs());
        }
        else {
          fDocumentHandler.startElement(fQName, fAttributes, locationAugs());
        }
      }
      return ename;
    }
    


    private String removeSpaces(String content)
    {
      StringBuilder sb = null;
      for (int i = content.length() - 1; i >= 0; i--) {
        if (Character.isWhitespace(content.charAt(i))) {
          if (sb == null) {
            sb = new StringBuilder(content);
          }
          sb.deleteCharAt(i);
        }
      }
      return sb == null ? content : sb.toString();
    }
    




    private boolean changeEncoding(String charset)
    {
      if ((charset == null) || (fByteStream == null)) {
        return false;
      }
      charset = charset.trim();
      boolean encodingChanged = false;
      try {
        String ianaEncoding = charset;
        String javaEncoding = EncodingMap.getIANA2JavaMapping(ianaEncoding.toUpperCase(Locale.ENGLISH));
        



        if (javaEncoding == null) {
          javaEncoding = ianaEncoding;
          if (fReportErrors) {
            fErrorReporter.reportError("HTML1001", new Object[] { ianaEncoding });
          }
        }
        
        if (!javaEncoding.equals(fJavaEncoding)) {
          if (!isEncodingCompatible(javaEncoding, fJavaEncoding)) {
            if (fReportErrors) {
              fErrorReporter.reportError("HTML1015", new Object[] { javaEncoding, fJavaEncoding });
            }
          }
          else
          {
            fIso8859Encoding = 
              ((ianaEncoding.toUpperCase(Locale.ENGLISH).startsWith("ISO-8859")) || 
              (ianaEncoding.equalsIgnoreCase(fDefaultIANAEncoding)));
            fJavaEncoding = javaEncoding;
            HTMLScanner.CurrentEntity.access$8(fCurrentEntity, new InputStreamReader(fByteStream, javaEncoding));
            fByteStream.playback();
            fElementDepth = fElementCount;
            fElementCount = 0;
            encodingChanged = true;
          }
        }
      }
      catch (UnsupportedEncodingException e) {
        if (fReportErrors) {
          fErrorReporter.reportError("HTML1010", new Object[] { charset });
        }
        


        fByteStream.clear();
        fByteStream = null;
      }
      return encodingChanged;
    }
    








    protected boolean scanAttribute(XMLAttributesImpl attributes, boolean[] empty)
      throws IOException
    {
      return scanAttribute(attributes, empty, '/');
    }
    




    protected boolean scanPseudoAttribute(XMLAttributesImpl attributes)
      throws IOException
    {
      return scanAttribute(attributes, fSingleBoolean, '?');
    }
    










    protected boolean scanAttribute(XMLAttributesImpl attributes, boolean[] empty, char endc)
      throws IOException
    {
      boolean skippedSpaces = skipSpaces();
      fBeginLineNumber = fCurrentEntity.getLineNumber();
      fBeginColumnNumber = HTMLScanner.CurrentEntity.access$0(fCurrentEntity);
      fBeginCharacterOffset = HTMLScanner.CurrentEntity.access$1(fCurrentEntity);
      int c = fCurrentEntity.read();
      if (c == -1) {
        if (fReportErrors) {
          fErrorReporter.reportError("HTML1007", null);
        }
        return false;
      }
      if (c == 62) {
        return false;
      }
      if (c == 60) {
        HTMLScanner.CurrentEntity.access$2(fCurrentEntity);
        return false;
      }
      HTMLScanner.CurrentEntity.access$2(fCurrentEntity);
      String aname = scanName(false);
      if (aname == null) {
        if (fReportErrors) {
          fErrorReporter.reportError("HTML1011", null);
        }
        empty[0] = skipMarkup(false);
        return false;
      }
      if ((!skippedSpaces) && (fReportErrors)) {
        fErrorReporter.reportError("HTML1013", new Object[] { aname });
      }
      aname = HTMLScanner.modifyName(aname, fNamesAttrs);
      skipSpaces();
      c = fCurrentEntity.read();
      if (c == -1) {
        if (fReportErrors) {
          fErrorReporter.reportError("HTML1007", null);
        }
        throw new EOFException();
      }
      if ((c == 47) || (c == 62)) {
        fQName.setValues(null, aname, aname, null);
        attributes.addAttribute(fQName, "CDATA", "");
        attributes.setSpecified(attributes.getLength() - 1, true);
        if (fAugmentations) {
          addLocationItem(attributes, attributes.getLength() - 1);
        }
        if (c == 47) {
          HTMLScanner.CurrentEntity.access$2(fCurrentEntity);
          empty[0] = skipMarkup(false);
        }
        return false;
      }
      
















      if (c == 61) {
        skipSpaces();
        c = fCurrentEntity.read();
        if (c == -1) {
          if (fReportErrors) {
            fErrorReporter.reportError("HTML1007", null);
          }
          throw new EOFException();
        }
        
        if (c == 62) {
          fQName.setValues(null, aname, aname, null);
          attributes.addAttribute(fQName, "CDATA", "");
          attributes.setSpecified(attributes.getLength() - 1, true);
          if (fAugmentations) {
            addLocationItem(attributes, attributes.getLength() - 1);
          }
          return false;
        }
        fStringBuffer.clear();
        fNonNormAttr.clear();
        if ((c != 39) && (c != 34)) {
          HTMLScanner.CurrentEntity.access$2(fCurrentEntity);
          for (;;) {
            c = fCurrentEntity.read();
            
            if ((Character.isWhitespace((char)c)) || (c == 62))
            {
              HTMLScanner.CurrentEntity.access$2(fCurrentEntity);
              break;
            }
            if (c == -1) {
              if (fReportErrors) {
                fErrorReporter.reportError("HTML1007", null);
              }
              throw new EOFException();
            }
            if (c == 38) {
              int ce = scanEntityRef(fStringBuffer2, false);
              if (ce != -1) {
                HTMLScanner.this.appendChar(fStringBuffer, ce);
              }
              else {
                fStringBuffer.append(fStringBuffer2);
              }
              fNonNormAttr.append(fStringBuffer2);
            }
            else {
              HTMLScanner.this.appendChar(fStringBuffer, c);
              HTMLScanner.this.appendChar(fNonNormAttr, c);
            }
          }
          fQName.setValues(null, aname, aname, null);
          String avalue = fStringBuffer.toString();
          attributes.addAttribute(fQName, "CDATA", avalue);
          
          int lastattr = attributes.getLength() - 1;
          attributes.setSpecified(lastattr, true);
          attributes.setNonNormalizedValue(lastattr, fNonNormAttr.toString());
          if (fAugmentations) {
            addLocationItem(attributes, attributes.getLength() - 1);
          }
          return true;
        }
        char quote = (char)c;
        boolean isStart = true;
        boolean prevSpace = false;
        do {
          boolean acceptSpace = (!fNormalizeAttributes) || ((!isStart) && (!prevSpace));
          c = fCurrentEntity.read();
          if (c == -1) {
            if (fReportErrors) {
              fErrorReporter.reportError("HTML1007", null);
            }
            throw new EOFException();
          }
          if (c == 38) {
            isStart = false;
            int ce = scanEntityRef(fStringBuffer2, false);
            if (ce != -1) {
              HTMLScanner.this.appendChar(fStringBuffer, ce);
            }
            else {
              fStringBuffer.append(fStringBuffer2);
            }
            fNonNormAttr.append(fStringBuffer2);
          }
          else if ((c == 32) || (c == 9)) {
            if (acceptSpace) {
              fStringBuffer.append(fNormalizeAttributes ? ' ' : (char)c);
            }
            fNonNormAttr.append((char)c);
          }
          else if ((c == 13) || (c == 10)) {
            if (c == 13) {
              int c2 = fCurrentEntity.read();
              if (c2 == 10) {
                fNonNormAttr.append('\r');
                c = c2;
              }
              else if (c2 != -1) {
                HTMLScanner.CurrentEntity.access$2(fCurrentEntity);
              }
            }
            if (acceptSpace) {
              fStringBuffer.append(fNormalizeAttributes ? ' ' : '\n');
            }
            HTMLScanner.CurrentEntity.access$7(fCurrentEntity);
            fNonNormAttr.append((char)c);
          }
          else if (c != quote) {
            isStart = false;
            HTMLScanner.this.appendChar(fStringBuffer, c);
            HTMLScanner.this.appendChar(fNonNormAttr, c);
          }
          prevSpace = (c == 32) || (c == 9) || (c == 13) || (c == 10);
          isStart = (isStart) && (prevSpace);
        } while (c != quote);
        
        if ((fNormalizeAttributes) && (fStringBuffer.length > 0))
        {
          if (fStringBuffer.ch[(fStringBuffer.length - 1)] == ' ') {
            XMLStringBuffer tmp1424_1421 = fStringBuffer;14241421length = (14241421length - 1);
          }
        }
        
        fQName.setValues(null, aname, aname, null);
        String avalue = fStringBuffer.toString();
        attributes.addAttribute(fQName, "CDATA", avalue);
        
        int lastattr = attributes.getLength() - 1;
        attributes.setSpecified(lastattr, true);
        attributes.setNonNormalizedValue(lastattr, fNonNormAttr.toString());
        if (fAugmentations) {
          addLocationItem(attributes, attributes.getLength() - 1);
        }
      }
      else {
        fQName.setValues(null, aname, aname, null);
        attributes.addAttribute(fQName, "CDATA", "");
        attributes.setSpecified(attributes.getLength() - 1, true);
        HTMLScanner.CurrentEntity.access$2(fCurrentEntity);
        if (fAugmentations) {
          addLocationItem(attributes, attributes.getLength() - 1);
        }
      }
      return true;
    }
    
    protected void addLocationItem(XMLAttributes attributes, int index)
    {
      fEndLineNumber = fCurrentEntity.getLineNumber();
      fEndColumnNumber = HTMLScanner.CurrentEntity.access$0(fCurrentEntity);
      fEndCharacterOffset = HTMLScanner.CurrentEntity.access$1(fCurrentEntity);
      HTMLScanner.LocationItem locationItem = new HTMLScanner.LocationItem();
      locationItem.setValues(fBeginLineNumber, fBeginColumnNumber, 
        fBeginCharacterOffset, fEndLineNumber, 
        fEndColumnNumber, fEndCharacterOffset);
      Augmentations augs = attributes.getAugmentations(index);
      augs.putItem("http://cyberneko.org/html/features/augmentations", locationItem);
    }
    
    protected void scanEndElement() throws IOException
    {
      String ename = scanName(true);
      if ((fReportErrors) && (ename == null)) {
        fErrorReporter.reportError("HTML1012", null);
      }
      skipMarkup(false);
      if (ename != null) {
        ename = HTMLScanner.modifyName(ename, fNamesElems);
        if ((fDocumentHandler != null) && (fElementCount >= fElementDepth)) {
          fQName.setValues(null, ename, ename, null);
          


          fEndLineNumber = fCurrentEntity.getLineNumber();
          fEndColumnNumber = HTMLScanner.CurrentEntity.access$0(fCurrentEntity);
          fEndCharacterOffset = HTMLScanner.CurrentEntity.access$1(fCurrentEntity);
          fDocumentHandler.endElement(fQName, locationAugs());
        }
      }
    }
    






    private boolean isEnded(String ename)
    {
      String content = new String(fCurrentEntity.buffer, fCurrentEntity.offset, 
        fCurrentEntity.length - fCurrentEntity.offset);
      return content.toLowerCase().indexOf("</" + ename.toLowerCase() + ">") != -1;
    }
  }
  





  public class SpecialScanner
    implements HTMLScanner.Scanner
  {
    protected String fElementName;
    




    protected boolean fStyle;
    




    protected boolean fTextarea;
    



    protected boolean fTitle;
    



    private final QName fQName = new QName();
    

    private final XMLStringBuffer fStringBuffer = new XMLStringBuffer();
    

    public SpecialScanner() {}
    

    public HTMLScanner.Scanner setElementName(String ename)
    {
      fElementName = ename;
      fStyle = fElementName.equalsIgnoreCase("STYLE");
      fTextarea = fElementName.equalsIgnoreCase("TEXTAREA");
      fTitle = fElementName.equalsIgnoreCase("TITLE");
      return this;
    }
    

    public boolean scan(boolean complete)
      throws IOException
    {
      boolean next;
      
      do
      {
        try
        {
          boolean next = false;
          switch (fScannerState) {
          case 0: 
            fBeginLineNumber = fCurrentEntity.getLineNumber();
            fBeginColumnNumber = fCurrentEntity.getColumnNumber();
            fBeginCharacterOffset = fCurrentEntity.getCharacterOffset();
            int c = fCurrentEntity.read();
            if (c == 60) {
              setScannerState((short)1);
            }
            else {
              if (c == 38) {
                if ((fTextarea) || (fTitle)) {
                  scanEntityRef(fStringBuffer, true);
                  continue;
                }
                fStringBuffer.clear();
                fStringBuffer.append('&');
              } else {
                if (c == -1) {
                  if (fReportErrors) {
                    fErrorReporter.reportError("HTML1007", null);
                  }
                  throw new EOFException();
                }
                
                fCurrentEntity.rewind();
                fStringBuffer.clear();
              }
              scanCharacters(fStringBuffer, -1); }
            break;
          
          case 1: 
            int delimiter = -1;
            int c = fCurrentEntity.read();
            if (c == 47) {
              String ename = scanName(true);
              if (ename != null) {
                if (ename.equalsIgnoreCase(fElementName)) {
                  if (fCurrentEntity.read() == 62) {
                    ename = HTMLScanner.modifyName(ename, fNamesElems);
                    if ((fDocumentHandler != null) && (fElementCount >= fElementDepth)) {
                      fQName.setValues(null, ename, ename, null);
                      


                      fEndLineNumber = fCurrentEntity.getLineNumber();
                      fEndColumnNumber = fCurrentEntity.getColumnNumber();
                      fEndCharacterOffset = fCurrentEntity.getCharacterOffset();
                      fDocumentHandler.endElement(fQName, locationAugs());
                    }
                    setScanner(fContentScanner);
                    setScannerState((short)0);
                    return true;
                  }
                  fCurrentEntity.rewind();
                }
                fStringBuffer.clear();
                fStringBuffer.append("</");
                fStringBuffer.append(ename);
              }
              else {
                fStringBuffer.clear();
                fStringBuffer.append("</");
              }
            }
            else {
              fStringBuffer.clear();
              fStringBuffer.append('<');
              HTMLScanner.this.appendChar(fStringBuffer, c);
            }
            scanCharacters(fStringBuffer, delimiter);
            setScannerState((short)0);
          }
          
        }
        catch (EOFException e)
        {
          setScanner(fContentScanner);
          if (fCurrentEntityStack.empty()) {
            setScannerState((short)11);
          }
          else {
            fCurrentEntity = ((HTMLScanner.CurrentEntity)fCurrentEntityStack.pop());
            setScannerState((short)0);
          }
          return true;
        }
        
      } while ((next) || (complete));
      return true;
    }
    







    protected void scanCharacters(XMLStringBuffer buffer, int delimiter)
      throws IOException
    {
      for (;;)
      {
        int c = fCurrentEntity.read();
        
        if ((c == -1) || (c == 60) || (c == 38)) {
          if (c == -1) break;
          fCurrentEntity.rewind();
          
          break;
        }
        
        if ((c == 13) || (c == 10)) {
          fCurrentEntity.rewind();
          int newlines = skipNewlines();
          for (int i = 0; i < newlines; i++) {
            buffer.append('\n');
          }
        }
        else {
          HTMLScanner.this.appendChar(buffer, c);
          if (c == 10) {
            fCurrentEntity.incLine();
          }
        }
      }
      
      if (fStyle) {
        if (fStyleStripCommentDelims) {
          HTMLScanner.reduceToContent(buffer, "<!--", "-->");
        }
        if (fStyleStripCDATADelims) {
          HTMLScanner.reduceToContent(buffer, "<![CDATA[", "]]>");
        }
      }
      
      if ((length > 0) && (fDocumentHandler != null) && (fElementCount >= fElementDepth))
      {


        fEndLineNumber = fCurrentEntity.getLineNumber();
        fEndColumnNumber = fCurrentEntity.getColumnNumber();
        fEndCharacterOffset = fCurrentEntity.getCharacterOffset();
        fDocumentHandler.characters(buffer, locationAugs());
      }
    }
  }
  






  public class PlainTextScanner
    implements HTMLScanner.Scanner
  {
    private final XMLStringBuffer fStringBuffer = new XMLStringBuffer();
    
    public PlainTextScanner() {}
    
    public boolean scan(boolean complete) throws IOException { scanCharacters(fStringBuffer);
      return false;
    }
    
    protected void scanCharacters(XMLStringBuffer buffer) throws IOException {
      for (;;) {
        int c = fCurrentEntity.read();
        
        if (c == -1) {
          break;
        }
        HTMLScanner.this.appendChar(buffer, c);
        if (c == 10) {
          fCurrentEntity.incLine();
        }
      }
      
      if ((length > 0) && (fDocumentHandler != null) && (fElementCount >= fElementDepth))
      {


        fEndLineNumber = fCurrentEntity.getLineNumber();
        fEndColumnNumber = fCurrentEntity.getColumnNumber();
        fEndCharacterOffset = fCurrentEntity.getCharacterOffset();
        fDocumentHandler.characters(buffer, locationAugs());
        fDocumentHandler.endDocument(locationAugs());
      }
    }
  }
  




















  public static class PlaybackInputStream
    extends FilterInputStream
  {
    private static final boolean DEBUG_PLAYBACK = false;
    


















    protected boolean fPlayback = false;
    

    protected boolean fCleared = false;
    

    protected boolean fDetected = false;
    



    protected byte[] fByteBuffer = new byte['Ѐ'];
    

    protected int fByteOffset = 0;
    

    protected int fByteLength = 0;
    

    public int fPushbackOffset = 0;
    

    public int fPushbackLength = 0;
    




    public PlaybackInputStream(InputStream in)
    {
      super();
    }
    



    public void detectEncoding(String[] encodings)
      throws IOException
    {
      if (fDetected) {
        throw new IOException("Should not detect encoding twice.");
      }
      fDetected = true;
      int b1 = read();
      if (b1 == -1) {
        return;
      }
      int b2 = read();
      if (b2 == -1) {
        fPushbackLength = 1;
        return;
      }
      
      if ((b1 == 239) && (b2 == 187)) {
        int b3 = read();
        if (b3 == 191) {
          fPushbackOffset = 3;
          encodings[0] = "UTF-8";
          encodings[1] = "UTF8";
          return;
        }
        fPushbackLength = 3;
      }
      
      if ((b1 == 255) && (b2 == 254)) {
        encodings[0] = "UTF-16";
        encodings[1] = "UnicodeLittleUnmarked";
        return;
      }
      
      if ((b1 == 254) && (b2 == 255)) {
        encodings[0] = "UTF-16";
        encodings[1] = "UnicodeBigUnmarked";
        return;
      }
      
      fPushbackLength = 2;
    }
    
    public void playback()
    {
      fPlayback = true;
    }
    







    public void clear()
    {
      if (!fPlayback) {
        fCleared = true;
        fByteBuffer = null;
      }
    }
    







    public int read()
      throws IOException
    {
      if (fPushbackOffset < fPushbackLength) {
        return fByteBuffer[(fPushbackOffset++)];
      }
      if (fCleared) {
        return in.read();
      }
      if (fPlayback) {
        int c = fByteBuffer[(fByteOffset++)];
        if (fByteOffset == fByteLength) {
          fCleared = true;
          fByteBuffer = null;
        }
        


        return c;
      }
      int c = in.read();
      if (c != -1) {
        if (fByteLength == fByteBuffer.length) {
          byte[] newarray = new byte[fByteLength + 1024];
          System.arraycopy(fByteBuffer, 0, newarray, 0, fByteLength);
          fByteBuffer = newarray;
        }
        fByteBuffer[(fByteLength++)] = ((byte)c);
      }
      


      return c;
    }
    
    public int read(byte[] array)
      throws IOException
    {
      return read(array, 0, array.length);
    }
    



    public int read(byte[] array, int offset, int length)
      throws IOException
    {
      if (fPushbackOffset < fPushbackLength) {
        int count = fPushbackLength - fPushbackOffset;
        if (count > length) {
          count = length;
        }
        System.arraycopy(fByteBuffer, fPushbackOffset, array, offset, count);
        fPushbackOffset += count;
        return count;
      }
      if (fCleared) {
        return in.read(array, offset, length);
      }
      if (fPlayback) {
        if (fByteOffset + length > fByteLength) {
          length = fByteLength - fByteOffset;
        }
        System.arraycopy(fByteBuffer, fByteOffset, array, offset, length);
        fByteOffset += length;
        if (fByteOffset == fByteLength) {
          fCleared = true;
          fByteBuffer = null;
        }
        return length;
      }
      int count = in.read(array, offset, length);
      if (count != -1) {
        if (fByteLength + count > fByteBuffer.length) {
          byte[] newarray = new byte[fByteLength + count + 512];
          System.arraycopy(fByteBuffer, 0, newarray, 0, fByteLength);
          fByteBuffer = newarray;
        }
        System.arraycopy(array, offset, fByteBuffer, fByteLength, count);
        fByteLength += count;
      }
      


      return count;
    }
  }
  



  protected static class LocationItem
    implements HTMLEventInfo, Cloneable
  {
    protected int fBeginLineNumber;
    


    protected int fBeginColumnNumber;
    


    protected int fBeginCharacterOffset;
    


    protected int fEndLineNumber;
    


    protected int fEndColumnNumber;
    


    protected int fEndCharacterOffset;
    



    public LocationItem() {}
    



    LocationItem(LocationItem other)
    {
      setValues(fBeginLineNumber, fBeginColumnNumber, fBeginCharacterOffset, 
        fEndLineNumber, fEndColumnNumber, fEndCharacterOffset);
    }
    

    public void setValues(int beginLine, int beginColumn, int beginOffset, int endLine, int endColumn, int endOffset)
    {
      fBeginLineNumber = beginLine;
      fBeginColumnNumber = beginColumn;
      fBeginCharacterOffset = beginOffset;
      fEndLineNumber = endLine;
      fEndColumnNumber = endColumn;
      fEndCharacterOffset = endOffset;
    }
    







    public int getBeginLineNumber()
    {
      return fBeginLineNumber;
    }
    

    public int getBeginColumnNumber()
    {
      return fBeginColumnNumber;
    }
    

    public int getBeginCharacterOffset()
    {
      return fBeginCharacterOffset;
    }
    

    public int getEndLineNumber()
    {
      return fEndLineNumber;
    }
    

    public int getEndColumnNumber()
    {
      return fEndColumnNumber;
    }
    

    public int getEndCharacterOffset()
    {
      return fEndCharacterOffset;
    }
    



    public boolean isSynthesized()
    {
      return false;
    }
    





    public String toString()
    {
      StringBuilder str = new StringBuilder();
      str.append(fBeginLineNumber);
      str.append(':');
      str.append(fBeginColumnNumber);
      str.append(':');
      str.append(fBeginCharacterOffset);
      str.append(':');
      str.append(fEndLineNumber);
      str.append(':');
      str.append(fEndColumnNumber);
      str.append(':');
      str.append(fEndCharacterOffset);
      return str.toString();
    }
  }
  





  boolean isEncodingCompatible(String encoding1, String encoding2)
  {
    try
    {
      return canRoundtrip(encoding1, encoding2);
    }
    catch (UnsupportedOperationException e)
    {
      try {
        return canRoundtrip(encoding2, encoding1);
      }
      catch (UnsupportedOperationException e1)
      {
        return false;
      }
    }
    catch (UnsupportedEncodingException e) {}
    
    return false;
  }
  
  private boolean canRoundtrip(String encodeCharset, String decodeCharset) throws UnsupportedEncodingException
  {
    String reference = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=";
    byte[] bytesEncoding1 = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=".getBytes(encodeCharset);
    String referenceWithEncoding2 = new String(bytesEncoding1, decodeCharset);
    return "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=".equals(referenceWithEncoding2);
  }
  
  private boolean endsWith(XMLStringBuffer buffer, String string) {
    int l = string.length();
    if (length < l) {
      return false;
    }
    String s = new String(ch, length - l, l);
    return string.equals(s);
  }
  


  protected int readPreservingBufferContent()
    throws IOException
  {
    if ((fCurrentEntity.offset == fCurrentEntity.length) && 
      (fCurrentEntity.load(fCurrentEntity.length) < 1))
    {


      return -1;
    }
    
    char c = fCurrentEntity.getNextChar();
    


    return c;
  }
  

  private boolean endCommentAvailable()
    throws IOException
  {
    int nbCaret = 0;
    int originalOffset = fCurrentEntity.offset;
    int originalColumnNumber = fCurrentEntity.getColumnNumber();
    int originalCharacterOffset = fCurrentEntity.getCharacterOffset();
    for (;;)
    {
      int c = readPreservingBufferContent();
      if (c == -1) {
        fCurrentEntity.restorePosition(originalOffset, originalColumnNumber, originalCharacterOffset);
        return false;
      }
      if ((c == 62) && (nbCaret >= 2)) {
        fCurrentEntity.restorePosition(originalOffset, originalColumnNumber, originalCharacterOffset);
        return true;
      }
      if (c == 45) {
        nbCaret++;
      }
      else {
        nbCaret = 0;
      }
    }
  }
  



  static void reduceToContent(XMLStringBuffer buffer, String startMarker, String endMarker)
  {
    int i = 0;
    int startContent = -1;
    int l1 = startMarker.length();
    int l2 = endMarker.length();
    while (i < length - l1 - l2) {
      char c = ch[(offset + i)];
      if (Character.isWhitespace(c)) {
        i++;
      } else {
        if ((c == startMarker.charAt(0)) && 
          (startMarker.equals(new String(ch, offset + i, l1)))) {
          startContent = offset + i + l1;
          break;
        }
        
        return;
      }
    }
    if (startContent == -1) {
      return;
    }
    
    i = length - 1;
    while (i > startContent + l2) {
      char c = ch[(offset + i)];
      if (Character.isWhitespace(c)) {
        i--;
      } else {
        if ((c == endMarker.charAt(l2 - 1)) && 
          (endMarker.equals(new String(ch, offset + i - l2 + 1, l2))))
        {
          length = (offset + i - startContent - 2);
          offset = startContent;
          return;
        }
        
        return;
      }
    }
  }
  
  public static abstract interface Scanner
  {
    public abstract boolean scan(boolean paramBoolean)
      throws IOException;
  }
}
