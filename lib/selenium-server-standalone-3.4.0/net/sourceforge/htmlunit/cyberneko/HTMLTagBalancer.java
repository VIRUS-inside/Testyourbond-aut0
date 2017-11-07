package net.sourceforge.htmlunit.cyberneko;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import net.sourceforge.htmlunit.cyberneko.xercesbridge.XercesBridge;
import org.apache.xerces.util.XMLAttributesImpl;
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
import org.apache.xerces.xni.parser.XMLDocumentFilter;
import org.apache.xerces.xni.parser.XMLDocumentSource;









































































public class HTMLTagBalancer
  implements XMLDocumentFilter, HTMLComponent
{
  protected static final String NAMESPACES = "http://xml.org/sax/features/namespaces";
  protected static final String AUGMENTATIONS = "http://cyberneko.org/html/features/augmentations";
  protected static final String REPORT_ERRORS = "http://cyberneko.org/html/features/report-errors";
  protected static final String DOCUMENT_FRAGMENT_DEPRECATED = "http://cyberneko.org/html/features/document-fragment";
  protected static final String DOCUMENT_FRAGMENT = "http://cyberneko.org/html/features/balance-tags/document-fragment";
  protected static final String IGNORE_OUTSIDE_CONTENT = "http://cyberneko.org/html/features/balance-tags/ignore-outside-content";
  private static final String[] RECOGNIZED_FEATURES = {
    "http://xml.org/sax/features/namespaces", 
    "http://cyberneko.org/html/features/augmentations", 
    "http://cyberneko.org/html/features/report-errors", 
    "http://cyberneko.org/html/features/document-fragment", 
    "http://cyberneko.org/html/features/balance-tags/document-fragment", 
    "http://cyberneko.org/html/features/balance-tags/ignore-outside-content" };
  


  private static final Boolean[] RECOGNIZED_FEATURES_DEFAULTS = {
  



    0, 0, 0, 0, Boolean.FALSE, 
    Boolean.FALSE };
  



  protected static final String NAMES_ELEMS = "http://cyberneko.org/html/properties/names/elems";
  


  protected static final String NAMES_ATTRS = "http://cyberneko.org/html/properties/names/attrs";
  


  protected static final String ERROR_REPORTER = "http://cyberneko.org/html/properties/error-reporter";
  


  public static final String FRAGMENT_CONTEXT_STACK = "http://cyberneko.org/html/properties/balance-tags/fragment-context-stack";
  


  private static final String[] RECOGNIZED_PROPERTIES = {
    "http://cyberneko.org/html/properties/names/elems", 
    "http://cyberneko.org/html/properties/names/attrs", 
    "http://cyberneko.org/html/properties/error-reporter", 
    "http://cyberneko.org/html/properties/balance-tags/fragment-context-stack" };
  


  private static final Object[] RECOGNIZED_PROPERTIES_DEFAULTS = new Object[4];
  



  protected static final short NAMES_NO_CHANGE = 0;
  



  protected static final short NAMES_MATCH = 0;
  



  protected static final short NAMES_UPPERCASE = 1;
  



  protected static final short NAMES_LOWERCASE = 2;
  



  protected static final HTMLEventInfo SYNTHESIZED_ITEM = new HTMLEventInfo.SynthesizedItem();
  


  protected boolean fNamespaces;
  


  protected boolean fAugmentations;
  


  protected boolean fReportErrors;
  


  protected boolean fDocumentFragment;
  


  protected boolean fIgnoreOutsideContent;
  


  protected boolean fAllowSelfclosingIframe;
  


  protected boolean fAllowSelfclosingTags;
  


  protected short fNamesElems;
  


  protected short fNamesAttrs;
  


  protected HTMLErrorReporter fErrorReporter;
  


  protected XMLDocumentSource fDocumentSource;
  


  protected XMLDocumentHandler fDocumentHandler;
  

  protected final InfoStack fElementStack = new InfoStack();
  

  protected final InfoStack fInlineStack = new InfoStack();
  


  protected boolean fSeenAnything;
  


  protected boolean fSeenDoctype;
  


  protected boolean fSeenRootElement;
  


  protected boolean fSeenRootElementEnd;
  


  protected boolean fSeenHeadElement;
  


  protected boolean fSeenBodyElement;
  

  private boolean fSeenBodyElementEnd;
  

  private boolean fSeenFramesetElement;
  

  private boolean fSeenCharacters;
  

  protected boolean fOpenedForm;
  

  private final QName fQName = new QName();
  

  private final XMLAttributes fEmptyAttrs = new XMLAttributesImpl();
  

  private final HTMLAugmentations fInfosetAugs = new HTMLAugmentations();
  
  protected HTMLTagBalancingListener tagBalancingListener;
  private LostText lostText_ = new LostText();
  
  private boolean forcedStartElement_ = false;
  private boolean forcedEndElement_ = false;
  



  private QName[] fragmentContextStack_ = null;
  private int fragmentContextStackSize_ = 0;
  
  private List<ElementEntry> endElementsBuffer_ = new ArrayList();
  private List<String> discardedStartElements = new ArrayList();
  private final HTMLConfiguration htmlConfiguration_;
  
  HTMLTagBalancer(HTMLConfiguration htmlConfiguration)
  {
    htmlConfiguration_ = htmlConfiguration;
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
    fNamespaces = manager.getFeature("http://xml.org/sax/features/namespaces");
    fAugmentations = manager.getFeature("http://cyberneko.org/html/features/augmentations");
    fReportErrors = manager.getFeature("http://cyberneko.org/html/features/report-errors");
    fDocumentFragment = ((manager.getFeature("http://cyberneko.org/html/features/balance-tags/document-fragment")) || 
      (manager.getFeature("http://cyberneko.org/html/features/document-fragment")));
    fIgnoreOutsideContent = manager.getFeature("http://cyberneko.org/html/features/balance-tags/ignore-outside-content");
    fAllowSelfclosingIframe = manager.getFeature("http://cyberneko.org/html/features/scanner/allow-selfclosing-iframe");
    fAllowSelfclosingTags = manager.getFeature("http://cyberneko.org/html/features/scanner/allow-selfclosing-tags");
    

    fNamesElems = getNamesValue(String.valueOf(manager.getProperty("http://cyberneko.org/html/properties/names/elems")));
    fNamesAttrs = getNamesValue(String.valueOf(manager.getProperty("http://cyberneko.org/html/properties/names/attrs")));
    fErrorReporter = ((HTMLErrorReporter)manager.getProperty("http://cyberneko.org/html/properties/error-reporter"));
    
    fragmentContextStack_ = ((QName[])manager.getProperty("http://cyberneko.org/html/properties/balance-tags/fragment-context-stack"));
    fSeenAnything = false;
    fSeenDoctype = false;
    fSeenRootElement = false;
    fSeenRootElementEnd = false;
    fSeenHeadElement = false;
    fSeenBodyElement = false;
    fSeenBodyElementEnd = false;
    fSeenFramesetElement = false;
    fSeenCharacters = false;
  }
  



  public void setFeature(String featureId, boolean state)
    throws XMLConfigurationException
  {
    if (featureId.equals("http://cyberneko.org/html/features/augmentations")) {
      fAugmentations = state;
      return;
    }
    if (featureId.equals("http://cyberneko.org/html/features/report-errors")) {
      fReportErrors = state;
      return;
    }
    if (featureId.equals("http://cyberneko.org/html/features/balance-tags/ignore-outside-content")) {
      fIgnoreOutsideContent = state;
      return;
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
  }
  






  public void setDocumentHandler(XMLDocumentHandler handler)
  {
    fDocumentHandler = handler;
  }
  



  public XMLDocumentHandler getDocumentHandler()
  {
    return fDocumentHandler;
  }
  










  public void startDocument(XMLLocator locator, String encoding, NamespaceContext nscontext, Augmentations augs)
    throws XNIException
  {
    fElementStack.top = 0;
    if (fragmentContextStack_ != null) {
      fragmentContextStackSize_ = fragmentContextStack_.length;
      for (int i = 0; i < fragmentContextStack_.length; i++) {
        QName name = fragmentContextStack_[i];
        HTMLElements.Element elt = htmlConfiguration_.htmlElements_.getElement(localpart);
        fElementStack.push(new Info(elt, name));
      }
    }
    else
    {
      fragmentContextStackSize_ = 0;
    }
    

    if (fDocumentHandler != null) {
      XercesBridge.getInstance().XMLDocumentHandler_startDocument(fDocumentHandler, locator, encoding, nscontext, augs);
    }
  }
  




  public void xmlDecl(String version, String encoding, String standalone, Augmentations augs)
    throws XNIException
  {
    if ((!fSeenAnything) && (fDocumentHandler != null)) {
      fDocumentHandler.xmlDecl(version, encoding, standalone, augs);
    }
  }
  

  public void doctypeDecl(String rootElementName, String publicId, String systemId, Augmentations augs)
    throws XNIException
  {
    fSeenAnything = true;
    if (fReportErrors) {
      if (fSeenRootElement) {
        fErrorReporter.reportError("HTML2010", null);
      }
      else if (fSeenDoctype) {
        fErrorReporter.reportError("HTML2011", null);
      }
    }
    if ((!fSeenRootElement) && (!fSeenDoctype)) {
      fSeenDoctype = true;
      if (fDocumentHandler != null) {
        fDocumentHandler.doctypeDecl(rootElementName, publicId, systemId, augs);
      }
    }
  }
  


  public void endDocument(Augmentations augs)
    throws XNIException
  {
    fIgnoreOutsideContent = true;
    consumeBufferedEndElements();
    

    if ((!fSeenRootElement) && (!fDocumentFragment)) {
      if (fReportErrors) {
        fErrorReporter.reportError("HTML2000", null);
      }
      if (fDocumentHandler != null) {
        fSeenRootElementEnd = false;
        forceStartBody();
        String body = modifyName("body", fNamesElems);
        fQName.setValues(null, body, body, null);
        callEndElement(fQName, synthesizedAugs());
        
        String ename = modifyName("html", fNamesElems);
        fQName.setValues(null, ename, ename, null);
        callEndElement(fQName, synthesizedAugs());
      }
      
    }
    else
    {
      int length = fElementStack.top - fragmentContextStackSize_;
      for (int i = 0; i < length; i++) {
        Info info = fElementStack.pop();
        if (fReportErrors) {
          String ename = qname.rawname;
          fErrorReporter.reportWarning("HTML2001", new Object[] { ename });
        }
        if (fDocumentHandler != null) {
          callEndElement(qname, synthesizedAugs());
        }
      }
    }
    

    if (fDocumentHandler != null) {
      fDocumentHandler.endDocument(augs);
    }
  }
  




  private void consumeBufferedEndElements()
  {
    List<ElementEntry> toConsume = new ArrayList(endElementsBuffer_);
    endElementsBuffer_.clear();
    for (ElementEntry entry : toConsume) {
      forcedEndElement_ = true;
      endElement(name_, augs_);
    }
    endElementsBuffer_.clear();
  }
  
  public void comment(XMLString text, Augmentations augs)
    throws XNIException
  {
    fSeenAnything = true;
    consumeEarlyTextIfNeeded();
    if (fDocumentHandler != null) {
      fDocumentHandler.comment(text, augs);
    }
  }
  
  private void consumeEarlyTextIfNeeded() {
    if (!lostText_.isEmpty()) {
      if (!fSeenBodyElement) {
        forceStartBody();
      }
      lostText_.refeed(this);
    }
  }
  

  public void processingInstruction(String target, XMLString data, Augmentations augs)
    throws XNIException
  {
    fSeenAnything = true;
    consumeEarlyTextIfNeeded();
    if (fDocumentHandler != null) {
      fDocumentHandler.processingInstruction(target, data, augs);
    }
  }
  

  public void startElement(QName elem, XMLAttributes attrs, Augmentations augs)
    throws XNIException
  {
    fSeenAnything = true;
    
    boolean isForcedCreation = forcedStartElement_;
    forcedStartElement_ = false;
    

    if (fSeenRootElementEnd) {
      notifyDiscardedStartElement(elem, attrs, augs);
      return;
    }
    

    HTMLElements.Element element = getElement(elem);
    short elementCode = code;
    

    if ((isForcedCreation) && ((elementCode == 115) || (elementCode == 103))) {
      return;
    }
    

    if ((fSeenRootElement) && (elementCode == 54)) {
      notifyDiscardedStartElement(elem, attrs, augs);
      return;
    }
    
    if ((fSeenFramesetElement) && (elementCode != 43) && (elementCode != 44) && (elementCode != 81)) {
      notifyDiscardedStartElement(elem, attrs, augs);
      return;
    }
    
    if (elementCode == 51) {
      if (fSeenHeadElement) {
        notifyDiscardedStartElement(elem, attrs, augs);
        return;
      }
      fSeenHeadElement = true;
    }
    else if (elementCode == 44) {
      if ((fSeenBodyElement) && (fSeenCharacters)) {
        notifyDiscardedStartElement(elem, attrs, augs);
        return;
      }
      
      if (!fSeenHeadElement) {
        QName head = createQName("head");
        forceStartElement(head, null, synthesizedAugs());
        endElement(head, synthesizedAugs());
      }
      consumeBufferedEndElements();
      fSeenFramesetElement = true;
    }
    else if (elementCode == 16)
    {
      if (!fSeenHeadElement) {
        QName head = createQName("head");
        forceStartElement(head, null, synthesizedAugs());
        endElement(head, synthesizedAugs());
      }
      consumeBufferedEndElements();
      
      if (fSeenBodyElement) {
        notifyDiscardedStartElement(elem, attrs, augs);
        return;
      }
      fSeenBodyElement = true;
    }
    else if (elementCode == 42) {
      if (fOpenedForm) {
        notifyDiscardedStartElement(elem, attrs, augs);
        return;
      }
      fOpenedForm = true;
    }
    else if (elementCode == 133) {
      consumeBufferedEndElements();
    }
    else if (elementCode == 90)
    {
      int length = fElementStack.top - fragmentContextStackSize_;
      for (int i = 0; i < length; i++) {
        Info info = fElementStack.peek();
        if ((element.code != 16) && (element.code != 54)) {
          info = fElementStack.pop();
          if (fReportErrors) {
            String ename = qname.rawname;
            fErrorReporter.reportWarning("HTML2001", new Object[] { ename });
          }
          if (fDocumentHandler != null) {
            callEndElement(qname, synthesizedAugs());
          }
        }
      }
    }
    

    if (parent != null) {
      HTMLElements.Element preferedParent = parent[0];
      if ((!fDocumentFragment) || ((code != 51) && (code != 16)))
      {

        if ((!fSeenRootElement) && (!fDocumentFragment)) {
          String pname = name;
          pname = modifyName(pname, fNamesElems);
          if (fReportErrors) {
            String ename = rawname;
            fErrorReporter.reportWarning("HTML2002", new Object[] { ename, pname });
          }
          QName qname = new QName(null, pname, pname, null);
          boolean parentCreated = forceStartElement(qname, null, synthesizedAugs());
          if (!parentCreated) {
            if (!isForcedCreation) {
              notifyDiscardedStartElement(elem, attrs, augs);
            }
            
          }
          
        }
        else if ((code != 51) || ((!fSeenBodyElement) && (!fDocumentFragment))) {
          int depth = getParentDepth(parent, bounds);
          if (depth == -1) {
            String pname = modifyName(name, fNamesElems);
            QName qname = new QName(null, pname, pname, null);
            if (fReportErrors) {
              String ename = rawname;
              fErrorReporter.reportWarning("HTML2004", new Object[] { ename, pname });
            }
            boolean parentCreated = forceStartElement(qname, null, synthesizedAugs());
            if (!parentCreated) {
              if (!isForcedCreation) {
                notifyDiscardedStartElement(elem, attrs, augs);
              }
              return;
            }
          }
        }
      }
    }
    

    int depth = 0;
    if (flags == 0) {
      int length = fElementStack.top;
      fInlineStack.top = 0;
      for (int i = length - 1; i >= 0; i--) {
        Info info = fElementStack.data[i];
        if (!element.isInline()) {
          break;
        }
        fInlineStack.push(info);
        endElement(qname, synthesizedAugs());
      }
      depth = fInlineStack.top;
    }
    



    if (((fElementStack.top > 1) && 
      (fElementStack.peek().element.code == 101)) || (
      (fElementStack.top > 2) && (fElementStack.data[(fElementStack.top - 2)].element.code == 51))) {
      Info info = fElementStack.pop();
      if (fDocumentHandler != null) {
        callEndElement(qname, synthesizedAugs());
      }
    }
    if (closes != null) {
      int length = fElementStack.top;
      for (int i = length - 1; i >= 0; i--) {
        Info info = fElementStack.data[i];
        

        if (element.closes(element.code)) {
          if (fReportErrors) {
            String ename = rawname;
            String iname = qname.rawname;
            fErrorReporter.reportWarning("HTML2005", new Object[] { ename, iname });
          }
          for (int j = length - 1; j >= i; j--) {
            info = fElementStack.pop();
            if (fDocumentHandler != null)
            {
              callEndElement(qname, synthesizedAugs());
            }
          }
          length = i;

        }
        else
        {
          if ((element.isBlock()) || (element.isParent(element))) {
            break;
          }
        }
      }
    }
    
    fSeenRootElement = true;
    if (element.isEmpty()) {
      if (attrs == null) {
        attrs = emptyAttributes();
      }
      if (fDocumentHandler != null) {
        fDocumentHandler.emptyElement(elem, attrs, augs);
      }
    }
    else {
      boolean inline = element.isInline();
      fElementStack.push(new Info(element, elem, inline ? attrs : null));
      if (attrs == null) {
        attrs = emptyAttributes();
      }
      if (fDocumentHandler != null) {
        callStartElement(elem, attrs, augs);
      }
    }
    

    for (int i = 0; i < depth; i++) {
      Info info = fInlineStack.pop();
      forceStartElement(qname, attributes, synthesizedAugs());
    }
    
    if (elementCode == 16) {
      lostText_.refeed(this);
    }
  }
  





  private boolean forceStartElement(QName elem, XMLAttributes attrs, Augmentations augs)
    throws XNIException
  {
    forcedStartElement_ = true;
    startElement(elem, attrs, augs);
    
    return (fElementStack.top > 0) && (elem.equals(fElementStack.peek().qname));
  }
  
  private QName createQName(String tagName) {
    tagName = modifyName(tagName, fNamesElems);
    return new QName(null, tagName, tagName, "http://www.w3.org/1999/xhtml");
  }
  

  public void emptyElement(QName element, XMLAttributes attrs, Augmentations augs)
    throws XNIException
  {
    startElement(element, attrs, augs);
    
    HTMLElements.Element elem = getElement(element);
    if ((elem.isEmpty()) || 
      (fAllowSelfclosingTags) || 
      (code == 133) || (
      (code == 56) && (fAllowSelfclosingIframe))) {
      endElement(element, augs);
    }
  }
  



  public void startGeneralEntity(String name, XMLResourceIdentifier id, String encoding, Augmentations augs)
    throws XNIException
  {
    fSeenAnything = true;
    

    if (fSeenRootElementEnd) {
      return;
    }
    

    if (!fDocumentFragment) {
      boolean insertBody = !fSeenRootElement;
      if (!insertBody) {
        Info info = fElementStack.peek();
        if ((element.code == 51) || 
          (element.code == 54)) {
          String hname = modifyName("head", fNamesElems);
          String bname = modifyName("body", fNamesElems);
          if (fReportErrors) {
            fErrorReporter.reportWarning("HTML2009", new Object[] { hname, bname });
          }
          fQName.setValues(null, hname, hname, null);
          endElement(fQName, synthesizedAugs());
          insertBody = true;
        }
      }
      if (insertBody) {
        forceStartBody();
      }
    }
    

    if (fDocumentHandler != null) {
      fDocumentHandler.startGeneralEntity(name, id, encoding, augs);
    }
  }
  



  private void forceStartBody()
  {
    QName body = createQName("body");
    if (fReportErrors) {
      fErrorReporter.reportWarning("HTML2006", new Object[] { localpart });
    }
    forceStartElement(body, null, synthesizedAugs());
  }
  

  public void textDecl(String version, String encoding, Augmentations augs)
    throws XNIException
  {
    fSeenAnything = true;
    

    if (fSeenRootElementEnd) {
      return;
    }
    

    if (fDocumentHandler != null) {
      fDocumentHandler.textDecl(version, encoding, augs);
    }
  }
  



  public void endGeneralEntity(String name, Augmentations augs)
    throws XNIException
  {
    if (fSeenRootElementEnd) {
      return;
    }
    

    if (fDocumentHandler != null) {
      fDocumentHandler.endGeneralEntity(name, augs);
    }
  }
  

  public void startCDATA(Augmentations augs)
    throws XNIException
  {
    fSeenAnything = true;
    
    consumeEarlyTextIfNeeded();
    

    if (fSeenRootElementEnd) {
      return;
    }
    

    if (fDocumentHandler != null) {
      fDocumentHandler.startCDATA(augs);
    }
  }
  



  public void endCDATA(Augmentations augs)
    throws XNIException
  {
    if (fSeenRootElementEnd) {
      return;
    }
    

    if (fDocumentHandler != null) {
      fDocumentHandler.endCDATA(augs);
    }
  }
  


  public void characters(XMLString text, Augmentations augs)
    throws XNIException
  {
    if ((fSeenRootElementEnd) || (fSeenBodyElementEnd)) {
      return;
    }
    
    if ((fElementStack.top == 0) && (!fDocumentFragment))
    {
      lostText_.add(text, augs);
      return;
    }
    

    boolean whitespace = true;
    int limit = offset + length;
    for (int i = offset; i < limit; i++) {
      if (!Character.isWhitespace(ch[i])) {
        whitespace = false;
        break;
      }
    }
    
    if (!fDocumentFragment)
    {
      if (!fSeenRootElement) {
        if (whitespace) {
          return;
        }
        forceStartBody();
      }
      
      if ((whitespace) && ((fElementStack.top < 2) || (endElementsBuffer_.size() == 1)))
      {
        return;
      }
      




      if (!whitespace) {
        Info info = fElementStack.peek();
        if ((element.code == 51) || 
          (element.code == 54)) {
          String hname = modifyName("head", fNamesElems);
          String bname = modifyName("body", fNamesElems);
          if (fReportErrors) {
            fErrorReporter.reportWarning("HTML2009", new Object[] { hname, bname });
          }
          forceStartBody();
        }
      }
    }
    
    fSeenCharacters = ((fSeenCharacters) || (!whitespace));
    

    if (fDocumentHandler != null) {
      fDocumentHandler.characters(text, augs);
    }
  }
  


  public void ignorableWhitespace(XMLString text, Augmentations augs)
    throws XNIException
  {
    characters(text, augs);
  }
  
  public void endElement(QName element, Augmentations augs)
    throws XNIException
  {
    boolean forcedEndElement = forcedEndElement_;
    
    if (fSeenRootElementEnd) {
      notifyDiscardedEndElement(element, augs);
      return;
    }
    

    HTMLElements.Element elem = getElement(element);
    

    if ((!fIgnoreOutsideContent) && (
      (code == 16) || (code == 54))) {
      for (Iterator<String> it = discardedStartElements.iterator(); it.hasNext();) {
        if (rawname.equals(it.next())) {
          it.remove();
          return;
        }
      }
      
      endElementsBuffer_.add(new ElementEntry(element, augs));
      return;
    }
    

    if ((fSeenFramesetElement) && (code != 43) && (code != 44)) {
      notifyDiscardedEndElement(element, augs);
      return;
    }
    

    if (code == 54) {
      fSeenRootElementEnd = true;
    }
    else if (fIgnoreOutsideContent) {
      if (code == 16) {
        fSeenBodyElementEnd = true;
      }
      else if (fSeenBodyElementEnd) {
        notifyDiscardedEndElement(element, augs);
      }
      
    }
    else if (code == 42) {
      fOpenedForm = false;
    }
    else if ((code == 51) && (!forcedEndElement))
    {
      endElementsBuffer_.add(new ElementEntry(element, augs));
      return;
    }
    

    int depth = getElementDepth(elem);
    if (depth == -1) {
      if (code == 88) {
        forceStartElement(element, emptyAttributes(), synthesizedAugs());
        endElement(element, augs);
      }
      else if (code == 17) {
        forceStartElement(element, emptyAttributes(), synthesizedAugs());
      }
      else if (!elem.isEmpty()) {
        notifyDiscardedEndElement(element, augs);
      }
      return;
    }
    

    if ((depth > 1) && (elem.isInline())) {
      int size = fElementStack.top;
      fInlineStack.top = 0;
      for (int i = 0; i < depth - 1; i++) {
        Info info = fElementStack.data[(size - i - 1)];
        HTMLElements.Element pelem = element;
        if ((pelem.isInline()) || (code == 40))
        {


          fInlineStack.push(info);
        }
      }
    }
    

    for (int i = 0; i < depth; i++) {
      Info info = fElementStack.pop();
      if ((fReportErrors) && (i < depth - 1)) {
        String ename = modifyName(rawname, fNamesElems);
        String iname = qname.rawname;
        fErrorReporter.reportWarning("HTML2007", new Object[] { ename, iname });
      }
      if (fDocumentHandler != null)
      {
        callEndElement(qname, i < depth - 1 ? synthesizedAugs() : augs);
      }
    }
    

    if (depth > 1) {
      int size = fInlineStack.top;
      for (int i = 0; i < size; i++) {
        Info info = fInlineStack.pop();
        XMLAttributes attributes = attributes;
        if (fReportErrors) {
          String iname = qname.rawname;
          fErrorReporter.reportWarning("HTML2008", new Object[] { iname });
        }
        forceStartElement(qname, attributes, synthesizedAugs());
      }
    }
  }
  




  public void setDocumentSource(XMLDocumentSource source)
  {
    fDocumentSource = source;
  }
  

  public XMLDocumentSource getDocumentSource()
  {
    return fDocumentSource;
  }
  


  public void startDocument(XMLLocator locator, String encoding, Augmentations augs)
    throws XNIException
  {
    startDocument(locator, encoding, null, augs);
  }
  


  public void startPrefixMapping(String prefix, String uri, Augmentations augs)
    throws XNIException
  {
    if (fSeenRootElementEnd) {
      return;
    }
    

    if (fDocumentHandler != null) {
      XercesBridge.getInstance().XMLDocumentHandler_startPrefixMapping(fDocumentHandler, prefix, uri, augs);
    }
  }
  



  public void endPrefixMapping(String prefix, Augmentations augs)
    throws XNIException
  {
    if (fSeenRootElementEnd) {
      return;
    }
    

    if (fDocumentHandler != null) {
      XercesBridge.getInstance().XMLDocumentHandler_endPrefixMapping(fDocumentHandler, prefix, augs);
    }
  }
  





  protected HTMLElements.Element getElement(QName elementName)
  {
    String name = rawname;
    if ((fNamespaces) && ("http://www.w3.org/1999/xhtml".equals(uri))) {
      int index = name.indexOf(':');
      if (index != -1) {
        name = name.substring(index + 1);
      }
    }
    return htmlConfiguration_.htmlElements_.getElement(name);
  }
  

  protected final void callStartElement(QName element, XMLAttributes attrs, Augmentations augs)
    throws XNIException
  {
    fDocumentHandler.startElement(element, attrs, augs);
  }
  
  protected final void callEndElement(QName element, Augmentations augs)
    throws XNIException
  {
    fDocumentHandler.endElement(element, augs);
  }
  





  protected final int getElementDepth(HTMLElements.Element element)
  {
    boolean container = element.isContainer();
    short elementCode = code;
    boolean tableBodyOrHtml = (elementCode == 115) || 
      (elementCode == 16) || (elementCode == 54);
    int depth = -1;
    for (int i = fElementStack.top - 1; i >= fragmentContextStackSize_; i--) {
      Info info = fElementStack.data[i];
      if ((element.code == code) && (
        (elementCode != 133) || ((elementCode == 133) && (name.equals(element.name))))) {
        depth = fElementStack.top - i;

      }
      else if ((container) || (!element.isBlock()))
      {

        if ((element.code == 115) && (!tableBodyOrHtml)) {
          return -1;
        }
        if (element.isParent(element))
          break;
      }
    }
    return depth;
  }
  





  protected int getParentDepth(HTMLElements.Element[] parents, short bounds)
  {
    if (parents != null) {
      for (int i = fElementStack.top - 1; i >= 0; i--) {
        Info info = fElementStack.data[i];
        if (element.code == bounds) {
          break;
        }
        for (int j = 0; j < parents.length; j++) {
          if (element.code == code) {
            return fElementStack.top - i;
          }
        }
      }
    }
    return -1;
  }
  
  protected final XMLAttributes emptyAttributes()
  {
    fEmptyAttrs.removeAllAttributes();
    return fEmptyAttrs;
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
  










  public static class Info
  {
    public HTMLElements.Element element;
    









    public QName qname;
    









    public XMLAttributes attributes;
    









    public Info(HTMLElements.Element element, QName qname)
    {
      this(element, qname, null);
    }
    









    public Info(HTMLElements.Element element, QName qname, XMLAttributes attributes)
    {
      this.element = element;
      this.qname = new QName(qname);
      if (attributes != null) {
        int length = attributes.getLength();
        if (length > 0) {
          QName aqname = new QName();
          XMLAttributes newattrs = new XMLAttributesImpl();
          for (int i = 0; i < length; i++) {
            attributes.getName(i, aqname);
            String type = attributes.getType(i);
            String value = attributes.getValue(i);
            String nonNormalizedValue = attributes.getNonNormalizedValue(i);
            boolean specified = attributes.isSpecified(i);
            newattrs.addAttribute(aqname, type, value);
            newattrs.setNonNormalizedValue(i, nonNormalizedValue);
            newattrs.setSpecified(i, specified);
          }
          this.attributes = newattrs;
        }
      }
    }
    



    public String toString()
    {
      return super.toString() + qname;
    }
  }
  




  public static class InfoStack
  {
    public int top;
    



    public HTMLTagBalancer.Info[] data = new HTMLTagBalancer.Info[10];
    

    public InfoStack() {}
    

    public void push(HTMLTagBalancer.Info info)
    {
      if (top == data.length) {
        HTMLTagBalancer.Info[] newarray = new HTMLTagBalancer.Info[top + 10];
        System.arraycopy(data, 0, newarray, 0, top);
        data = newarray;
      }
      data[(top++)] = info;
    }
    
    public HTMLTagBalancer.Info peek()
    {
      return data[(top - 1)];
    }
    
    public HTMLTagBalancer.Info pop()
    {
      return data[(--top)];
    }
    



    public String toString()
    {
      StringBuilder sb = new StringBuilder("InfoStack(");
      for (int i = top - 1; i >= 0; i--) {
        sb.append(data[i]);
        if (i != 0)
          sb.append(", ");
      }
      sb.append(")");
      return sb.toString();
    }
  }
  

  void setTagBalancingListener(HTMLTagBalancingListener tagBalancingListener)
  {
    this.tagBalancingListener = tagBalancingListener;
  }
  


  private void notifyDiscardedStartElement(QName elem, XMLAttributes attrs, Augmentations augs)
  {
    if (tagBalancingListener != null) {
      tagBalancingListener.ignoredStartElement(elem, attrs, augs);
    }
    discardedStartElements.add(rawname);
  }
  


  private void notifyDiscardedEndElement(QName element, Augmentations augs)
  {
    if (tagBalancingListener != null) {
      tagBalancingListener.ignoredEndElement(element, augs);
    }
  }
  
  static class ElementEntry
  {
    private final QName name_;
    private final Augmentations augs_;
    
    ElementEntry(QName element, Augmentations augs) {
      name_ = new QName(element);
      augs_ = (augs == null ? null : new HTMLAugmentations(augs));
    }
  }
}
