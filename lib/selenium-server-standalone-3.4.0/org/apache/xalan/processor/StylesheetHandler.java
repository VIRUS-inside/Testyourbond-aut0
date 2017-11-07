package org.apache.xalan.processor;

import java.util.EmptyStackException;
import java.util.Stack;
import java.util.Vector;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Source;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.sax.TemplatesHandler;
import org.apache.xalan.extensions.ExpressionVisitor;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.templates.ElemForEach;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.FuncDocument;
import org.apache.xalan.templates.FuncFormatNumb;
import org.apache.xalan.templates.Stylesheet;
import org.apache.xalan.templates.StylesheetRoot;
import org.apache.xml.utils.BoolStack;
import org.apache.xml.utils.NamespaceSupport2;
import org.apache.xml.utils.NodeConsumer;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.SAXSourceLocator;
import org.apache.xml.utils.XMLCharacterRecognizer;
import org.apache.xpath.XPath;
import org.apache.xpath.compiler.FunctionTable;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.NamespaceSupport;






























public class StylesheetHandler
  extends DefaultHandler
  implements TemplatesHandler, PrefixResolver, NodeConsumer
{
  private FunctionTable m_funcTable = new FunctionTable();
  



  private boolean m_optimize = true;
  



  private boolean m_incremental = false;
  



  private boolean m_source_location = false;
  









  public StylesheetHandler(TransformerFactoryImpl processor)
    throws TransformerConfigurationException
  {
    Class func = FuncDocument.class;
    m_funcTable.installFunction("document", func);
    


    func = FuncFormatNumb.class;
    
    m_funcTable.installFunction("format-number", func);
    
    m_optimize = ((Boolean)processor.getAttribute("http://xml.apache.org/xalan/features/optimize")).booleanValue();
    
    m_incremental = ((Boolean)processor.getAttribute("http://xml.apache.org/xalan/features/incremental")).booleanValue();
    
    m_source_location = ((Boolean)processor.getAttribute("http://xml.apache.org/xalan/properties/source-location")).booleanValue();
    

    init(processor);
  }
  






  void init(TransformerFactoryImpl processor)
  {
    m_stylesheetProcessor = processor;
    

    m_processors.push(m_schema.getElementProcessor());
    pushNewNamespaceSupport();
  }
  














  public XPath createXPath(String str, ElemTemplateElement owningTemplate)
    throws TransformerException
  {
    ErrorListener handler = m_stylesheetProcessor.getErrorListener();
    XPath xpath = new XPath(str, owningTemplate, this, 0, handler, m_funcTable);
    

    xpath.callVisitors(xpath, new ExpressionVisitor(getStylesheetRoot()));
    return xpath;
  }
  










  XPath createMatchPatternXPath(String str, ElemTemplateElement owningTemplate)
    throws TransformerException
  {
    ErrorListener handler = m_stylesheetProcessor.getErrorListener();
    XPath xpath = new XPath(str, owningTemplate, this, 1, handler, m_funcTable);
    

    xpath.callVisitors(xpath, new ExpressionVisitor(getStylesheetRoot()));
    return xpath;
  }
  









  public String getNamespaceForPrefix(String prefix)
  {
    return getNamespaceSupport().getURI(prefix);
  }
  














  public String getNamespaceForPrefix(String prefix, Node context)
  {
    assertion(true, "can't process a context node in StylesheetHandler!");
    
    return null;
  }
  









  private boolean stackContains(Stack stack, String url)
  {
    int n = stack.size();
    boolean contains = false;
    
    for (int i = 0; i < n; i++)
    {
      String url2 = (String)stack.elementAt(i);
      
      if (url2.equals(url))
      {
        contains = true;
        
        break;
      }
    }
    
    return contains;
  }
  
















  public Templates getTemplates()
  {
    return getStylesheetRoot();
  }
  







  public void setSystemId(String baseID)
  {
    pushBaseIndentifier(baseID);
  }
  






  public String getSystemId()
  {
    return getBaseIdentifier();
  }
  
















  public InputSource resolveEntity(String publicId, String systemId)
    throws SAXException
  {
    return getCurrentProcessor().resolveEntity(this, publicId, systemId);
  }
  

















  public void notationDecl(String name, String publicId, String systemId)
  {
    getCurrentProcessor().notationDecl(this, name, publicId, systemId);
  }
  











  public void unparsedEntityDecl(String name, String publicId, String systemId, String notationName)
  {
    getCurrentProcessor().unparsedEntityDecl(this, name, publicId, systemId, notationName);
  }
  
















  XSLTElementProcessor getProcessorFor(String uri, String localName, String rawName)
    throws SAXException
  {
    XSLTElementProcessor currentProcessor = getCurrentProcessor();
    XSLTElementDef def = currentProcessor.getElemDef();
    XSLTElementProcessor elemProcessor = def.getProcessorFor(uri, localName);
    
    if ((null == elemProcessor) && (!(currentProcessor instanceof ProcessorStylesheetDoc)) && ((null == getStylesheet()) || (Double.valueOf(getStylesheet().getVersion()).doubleValue() > 1.0D) || ((!uri.equals("http://www.w3.org/1999/XSL/Transform")) && ((currentProcessor instanceof ProcessorStylesheetElement))) || (getElemVersion() > 1.0D)))
    {








      elemProcessor = def.getProcessorForUnknown(uri, localName);
    }
    
    if (null == elemProcessor) {
      error(XSLMessages.createMessage("ER_NOT_ALLOWED_IN_POSITION", new Object[] { rawName }), null);
    }
    
    return elemProcessor;
  }
  

















  public void setDocumentLocator(Locator locator)
  {
    m_stylesheetLocatorStack.push(new SAXSourceLocator(locator));
  }
  



  private int m_stylesheetLevel = -1;
  







  public void startDocument()
    throws SAXException
  {
    m_stylesheetLevel += 1;
    pushSpaceHandling(false);
  }
  




  private boolean m_parsingComplete = false;
  









  public boolean isStylesheetParsingComplete()
  {
    return m_parsingComplete;
  }
  








  public void endDocument()
    throws SAXException
  {
    try
    {
      if (null != getStylesheetRoot())
      {
        if (0 == m_stylesheetLevel) {
          getStylesheetRoot().recompose();
        }
      } else {
        throw new TransformerException(XSLMessages.createMessage("ER_NO_STYLESHEETROOT", null));
      }
      XSLTElementProcessor elemProcessor = getCurrentProcessor();
      
      if (null != elemProcessor) {
        elemProcessor.startNonText(this);
      }
      m_stylesheetLevel -= 1;
      
      popSpaceHandling();
      





      m_parsingComplete = (m_stylesheetLevel < 0);
    }
    catch (TransformerException te)
    {
      throw new SAXException(te);
    }
  }
  
  private Vector m_prefixMappings = new Vector();
  




















  public void startPrefixMapping(String prefix, String uri)
    throws SAXException
  {
    m_prefixMappings.addElement(prefix);
    m_prefixMappings.addElement(uri);
  }
  











  public void endPrefixMapping(String prefix)
    throws SAXException
  {}
  










  private void flushCharacters()
    throws SAXException
  {
    XSLTElementProcessor elemProcessor = getCurrentProcessor();
    
    if (null != elemProcessor) {
      elemProcessor.startNonText(this);
    }
  }
  










  public void startElement(String uri, String localName, String rawName, Attributes attributes)
    throws SAXException
  {
    NamespaceSupport nssupport = getNamespaceSupport();
    nssupport.pushContext();
    
    int n = m_prefixMappings.size();
    
    for (int i = 0; i < n; i++)
    {
      String prefix = (String)m_prefixMappings.elementAt(i++);
      String nsURI = (String)m_prefixMappings.elementAt(i);
      nssupport.declarePrefix(prefix, nsURI);
    }
    
    m_prefixMappings.removeAllElements();
    
    m_elementID += 1;
    




















    checkForFragmentID(attributes);
    
    if (!m_shouldProcess) {
      return;
    }
    flushCharacters();
    
    pushSpaceHandling(attributes);
    
    XSLTElementProcessor elemProcessor = getProcessorFor(uri, localName, rawName);
    

    if (null != elemProcessor)
    {
      pushProcessor(elemProcessor);
      elemProcessor.startElement(this, uri, localName, rawName, attributes);
    }
    else
    {
      m_shouldProcess = false;
      popSpaceHandling();
    }
  }
  













  public void endElement(String uri, String localName, String rawName)
    throws SAXException
  {
    m_elementID -= 1;
    
    if (!m_shouldProcess) {
      return;
    }
    if (m_elementID + 1 == m_fragmentID) {
      m_shouldProcess = false;
    }
    flushCharacters();
    
    popSpaceHandling();
    
    XSLTElementProcessor p = getCurrentProcessor();
    
    p.endElement(this, uri, localName, rawName);
    popProcessor();
    getNamespaceSupport().popContext();
  }
  













  public void characters(char[] ch, int start, int length)
    throws SAXException
  {
    if (!m_shouldProcess) {
      return;
    }
    XSLTElementProcessor elemProcessor = getCurrentProcessor();
    XSLTElementDef def = elemProcessor.getElemDef();
    
    if (def.getType() != 2) {
      elemProcessor = def.getProcessorFor(null, "text()");
    }
    if (null == elemProcessor)
    {


      if (!XMLCharacterRecognizer.isWhiteSpace(ch, start, length)) {
        error(XSLMessages.createMessage("ER_NONWHITESPACE_NOT_ALLOWED_IN_POSITION", null), null);
      }
      
    }
    else {
      elemProcessor.characters(this, ch, start, length);
    }
  }
  












  public void ignorableWhitespace(char[] ch, int start, int length)
    throws SAXException
  {
    if (!m_shouldProcess) {
      return;
    }
    getCurrentProcessor().ignorableWhitespace(this, ch, start, length);
  }
  
























  public void processingInstruction(String target, String data)
    throws SAXException
  {
    if (!m_shouldProcess) {
      return;
    }
    








    String prefix = "";String ns = "";String localName = target;
    int colon = target.indexOf(':');
    if (colon >= 0)
    {
      ns = getNamespaceForPrefix(prefix = target.substring(0, colon));
      localName = target.substring(colon + 1);
    }
    







    try
    {
      if (("xalan-doc-cache-off".equals(target)) || ("xalan:doc-cache-off".equals(target)) || (("doc-cache-off".equals(localName)) && (ns.equals("org.apache.xalan.xslt.extensions.Redirect"))))
      {





        if (!(m_elems.peek() instanceof ElemForEach)) {
          throw new TransformerException("xalan:doc-cache-off not allowed here!", getLocator());
        }
        
        ElemForEach elem = (ElemForEach)m_elems.peek();
        
        m_doc_cache_off = true;
      }
    }
    catch (Exception e) {}
    







    flushCharacters();
    getCurrentProcessor().processingInstruction(this, target, data);
  }
  














  public void skippedEntity(String name)
    throws SAXException
  {
    if (!m_shouldProcess) {
      return;
    }
    getCurrentProcessor().skippedEntity(this, name);
  }
  













  public void warn(String msg, Object[] args)
    throws SAXException
  {
    String formattedMsg = XSLMessages.createWarning(msg, args);
    SAXSourceLocator locator = getLocator();
    ErrorListener handler = m_stylesheetProcessor.getErrorListener();
    
    try
    {
      if (null != handler) {
        handler.warning(new TransformerException(formattedMsg, locator));
      }
    }
    catch (TransformerException te) {
      throw new SAXException(te);
    }
  }
  








  private void assertion(boolean condition, String msg)
    throws RuntimeException
  {
    if (!condition) {
      throw new RuntimeException(msg);
    }
  }
  













  protected void error(String msg, Exception e)
    throws SAXException
  {
    SAXSourceLocator locator = getLocator();
    ErrorListener handler = m_stylesheetProcessor.getErrorListener();
    TransformerException pe;
    TransformerException pe;
    if (!(e instanceof TransformerException))
    {
      pe = null == e ? new TransformerException(msg, locator) : new TransformerException(msg, locator, e);

    }
    else
    {
      pe = (TransformerException)e;
    }
    if (null != handler)
    {
      try
      {
        handler.error(pe);
      }
      catch (TransformerException te)
      {
        throw new SAXException(te);
      }
      
    } else {
      throw new SAXException(pe);
    }
  }
  















  protected void error(String msg, Object[] args, Exception e)
    throws SAXException
  {
    String formattedMsg = XSLMessages.createMessage(msg, args);
    
    error(formattedMsg, e);
  }
  











  public void warning(SAXParseException e)
    throws SAXException
  {
    String formattedMsg = e.getMessage();
    SAXSourceLocator locator = getLocator();
    ErrorListener handler = m_stylesheetProcessor.getErrorListener();
    
    try
    {
      handler.warning(new TransformerException(formattedMsg, locator));
    }
    catch (TransformerException te)
    {
      throw new SAXException(te);
    }
  }
  











  public void error(SAXParseException e)
    throws SAXException
  {
    String formattedMsg = e.getMessage();
    SAXSourceLocator locator = getLocator();
    ErrorListener handler = m_stylesheetProcessor.getErrorListener();
    
    try
    {
      handler.error(new TransformerException(formattedMsg, locator));
    }
    catch (TransformerException te)
    {
      throw new SAXException(te);
    }
  }
  











  public void fatalError(SAXParseException e)
    throws SAXException
  {
    String formattedMsg = e.getMessage();
    SAXSourceLocator locator = getLocator();
    ErrorListener handler = m_stylesheetProcessor.getErrorListener();
    
    try
    {
      handler.fatalError(new TransformerException(formattedMsg, locator));
    }
    catch (TransformerException te)
    {
      throw new SAXException(te);
    }
  }
  





  private boolean m_shouldProcess = true;
  







  private String m_fragmentIDString;
  






  private int m_elementID = 0;
  




  private int m_fragmentID = 0;
  
  private TransformerFactoryImpl m_stylesheetProcessor;
  
  public static final int STYPE_ROOT = 1;
  
  public static final int STYPE_INCLUDE = 2;
  public static final int STYPE_IMPORT = 3;
  
  private void checkForFragmentID(Attributes attributes)
  {
    if (!m_shouldProcess)
    {
      if ((null != attributes) && (null != m_fragmentIDString))
      {
        int n = attributes.getLength();
        
        for (int i = 0; i < n; i++)
        {
          String name = attributes.getQName(i);
          
          if (name.equals("id"))
          {
            String val = attributes.getValue(i);
            
            if (val.equalsIgnoreCase(m_fragmentIDString))
            {
              m_shouldProcess = true;
              m_fragmentID = m_elementID;
            }
          }
        }
      }
    }
  }
  











  public TransformerFactoryImpl getStylesheetProcessor()
  {
    return m_stylesheetProcessor;
  }
  






















  private int m_stylesheetType = 1;
  






  int getStylesheetType()
  {
    return m_stylesheetType;
  }
  






  void setStylesheetType(int type)
  {
    m_stylesheetType = type;
  }
  



  private Stack m_stylesheets = new Stack();
  

  StylesheetRoot m_stylesheetRoot;
  
  Stylesheet m_lastPoppedStylesheet;
  

  Stylesheet getStylesheet()
  {
    return m_stylesheets.size() == 0 ? null : (Stylesheet)m_stylesheets.peek();
  }
  






  Stylesheet getLastPoppedStylesheet()
  {
    return m_lastPoppedStylesheet;
  }
  





  public StylesheetRoot getStylesheetRoot()
  {
    if (m_stylesheetRoot != null) {
      m_stylesheetRoot.setOptimizer(m_optimize);
      m_stylesheetRoot.setIncremental(m_incremental);
      m_stylesheetRoot.setSource_location(m_source_location);
    }
    return m_stylesheetRoot;
  }
  














  public void pushStylesheet(Stylesheet s)
  {
    if (m_stylesheets.size() == 0) {
      m_stylesheetRoot = ((StylesheetRoot)s);
    }
    m_stylesheets.push(s);
  }
  











  Stylesheet popStylesheet()
  {
    if (!m_stylesheetLocatorStack.isEmpty()) {
      m_stylesheetLocatorStack.pop();
    }
    if (!m_stylesheets.isEmpty()) {
      m_lastPoppedStylesheet = ((Stylesheet)m_stylesheets.pop());
    }
    
    return m_lastPoppedStylesheet;
  }
  



  private Stack m_processors = new Stack();
  





  XSLTElementProcessor getCurrentProcessor()
  {
    return (XSLTElementProcessor)m_processors.peek();
  }
  





  void pushProcessor(XSLTElementProcessor processor)
  {
    m_processors.push(processor);
  }
  




  XSLTElementProcessor popProcessor()
  {
    return (XSLTElementProcessor)m_processors.pop();
  }
  






  private XSLTSchema m_schema = new XSLTSchema();
  







  public XSLTSchema getSchema()
  {
    return m_schema;
  }
  



  private Stack m_elems = new Stack();
  





  ElemTemplateElement getElemTemplateElement()
  {
    try
    {
      return (ElemTemplateElement)m_elems.peek();
    }
    catch (EmptyStackException ese) {}
    
    return null;
  }
  




  private int m_docOrderCount = 0;
  



  int nextUid()
  {
    return m_docOrderCount++;
  }
  









  void pushElemTemplateElement(ElemTemplateElement elem)
  {
    if (elem.getUid() == -1) {
      elem.setUid(nextUid());
    }
    m_elems.push(elem);
  }
  




  ElemTemplateElement popElemTemplateElement()
  {
    return (ElemTemplateElement)m_elems.pop();
  }
  




  Stack m_baseIdentifiers = new Stack();
  









  void pushBaseIndentifier(String baseID)
  {
    if (null != baseID)
    {
      int posOfHash = baseID.indexOf('#');
      
      if (posOfHash > -1)
      {
        m_fragmentIDString = baseID.substring(posOfHash + 1);
        m_shouldProcess = false;
      }
      else {
        m_shouldProcess = true;
      }
    } else {
      m_shouldProcess = true;
    }
    m_baseIdentifiers.push(baseID);
  }
  




  String popBaseIndentifier()
  {
    return (String)m_baseIdentifiers.pop();
  }
  









  public String getBaseIdentifier()
  {
    String base = (String)(m_baseIdentifiers.isEmpty() ? null : m_baseIdentifiers.peek());
    


    if (null == base)
    {
      SourceLocator locator = getLocator();
      
      base = null == locator ? "" : locator.getSystemId();
    }
    
    return base;
  }
  




  private Stack m_stylesheetLocatorStack = new Stack();
  






  public SAXSourceLocator getLocator()
  {
    if (m_stylesheetLocatorStack.isEmpty())
    {
      SAXSourceLocator locator = new SAXSourceLocator();
      
      locator.setSystemId(getStylesheetProcessor().getDOMsystemID());
      
      return locator;
    }
    


    return (SAXSourceLocator)m_stylesheetLocatorStack.peek();
  }
  




  private Stack m_importStack = new Stack();
  





  private Stack m_importSourceStack = new Stack();
  






  void pushImportURL(String hrefUrl)
  {
    m_importStack.push(hrefUrl);
  }
  





  void pushImportSource(Source sourceFromURIResolver)
  {
    m_importSourceStack.push(sourceFromURIResolver);
  }
  








  boolean importStackContains(String hrefUrl)
  {
    return stackContains(m_importStack, hrefUrl);
  }
  





  String popImportURL()
  {
    return (String)m_importStack.pop();
  }
  
  String peekImportURL()
  {
    return (String)m_importStack.peek();
  }
  
  Source peekSourceFromURIResolver()
  {
    return (Source)m_importSourceStack.peek();
  }
  




  Source popImportSource()
  {
    return (Source)m_importSourceStack.pop();
  }
  




  private boolean warnedAboutOldXSLTNamespace = false;
  

  Stack m_nsSupportStack = new Stack();
  
  private Node m_originatingNode;
  

  void pushNewNamespaceSupport()
  {
    m_nsSupportStack.push(new NamespaceSupport2());
  }
  




  void popNamespaceSupport()
  {
    m_nsSupportStack.pop();
  }
  






  NamespaceSupport getNamespaceSupport()
  {
    return (NamespaceSupport)m_nsSupportStack.peek();
  }
  













  public void setOriginatingNode(Node n)
  {
    m_originatingNode = n;
  }
  






  public Node getOriginatingNode()
  {
    return m_originatingNode;
  }
  




  private BoolStack m_spacePreserveStack = new BoolStack();
  






  boolean isSpacePreserve()
  {
    return m_spacePreserveStack.peek();
  }
  



  void popSpaceHandling()
  {
    m_spacePreserveStack.pop();
  }
  





  void pushSpaceHandling(boolean b)
    throws SAXParseException
  {
    m_spacePreserveStack.push(b);
  }
  






  void pushSpaceHandling(Attributes attrs)
    throws SAXParseException
  {
    String value = attrs.getValue("xml:space");
    if (null == value)
    {
      m_spacePreserveStack.push(m_spacePreserveStack.peekOrFalse());
    }
    else if (value.equals("preserve"))
    {
      m_spacePreserveStack.push(true);
    }
    else if (value.equals("default"))
    {
      m_spacePreserveStack.push(false);
    }
    else
    {
      SAXSourceLocator locator = getLocator();
      ErrorListener handler = m_stylesheetProcessor.getErrorListener();
      
      try
      {
        handler.error(new TransformerException(XSLMessages.createMessage("ER_ILLEGAL_XMLSPACE_VALUE", null), locator));
      }
      catch (TransformerException te)
      {
        throw new SAXParseException(te.getMessage(), locator, te);
      }
      m_spacePreserveStack.push(m_spacePreserveStack.peek());
    }
  }
  
  private double getElemVersion()
  {
    ElemTemplateElement elem = getElemTemplateElement();
    double version = -1.0D;
    while (((version == -1.0D) || (version == 1.0D)) && (elem != null))
    {
      try {
        version = Double.valueOf(elem.getXmlVersion()).doubleValue();
      }
      catch (Exception ex)
      {
        version = -1.0D;
      }
      elem = elem.getParentElem();
    }
    return version == -1.0D ? 1.0D : version;
  }
  

  public boolean handlesNullPrefixes()
  {
    return false;
  }
  


  public boolean getOptimize()
  {
    return m_optimize;
  }
  


  public boolean getIncremental()
  {
    return m_incremental;
  }
  


  public boolean getSource_location()
  {
    return m_source_location;
  }
}
