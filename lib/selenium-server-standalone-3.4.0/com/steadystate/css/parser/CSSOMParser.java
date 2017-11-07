package com.steadystate.css.parser;

import com.steadystate.css.dom.CSSCharsetRuleImpl;
import com.steadystate.css.dom.CSSFontFaceRuleImpl;
import com.steadystate.css.dom.CSSImportRuleImpl;
import com.steadystate.css.dom.CSSMediaRuleImpl;
import com.steadystate.css.dom.CSSOMObject;
import com.steadystate.css.dom.CSSPageRuleImpl;
import com.steadystate.css.dom.CSSRuleListImpl;
import com.steadystate.css.dom.CSSStyleDeclarationImpl;
import com.steadystate.css.dom.CSSStyleRuleImpl;
import com.steadystate.css.dom.CSSStyleSheetImpl;
import com.steadystate.css.dom.CSSUnknownRuleImpl;
import com.steadystate.css.dom.CSSValueImpl;
import com.steadystate.css.dom.MediaListImpl;
import com.steadystate.css.dom.Property;
import com.steadystate.css.sac.DocumentHandlerExt;
import com.steadystate.css.userdata.UserDataConstants;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Stack;
import java.util.logging.Logger;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.ErrorHandler;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.css.sac.Locator;
import org.w3c.css.sac.Parser;
import org.w3c.css.sac.SACMediaList;
import org.w3c.css.sac.SelectorList;
import org.w3c.css.sac.helpers.ParserFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleSheet;
import org.w3c.dom.css.CSSValue;





















public class CSSOMParser
{
  private static final Object LOCK = new Object();
  
  private static final String DEFAULT_PARSER = "com.steadystate.css.parser.SACParserCSS21";
  
  private static String LastFailed_;
  private Parser parser_;
  private CSSStyleSheetImpl parentStyleSheet_;
  
  public CSSOMParser()
  {
    this(null);
  }
  




  public CSSOMParser(Parser parser)
  {
    synchronized (LOCK) {
      if (null != parser) {
        System.setProperty("org.w3c.css.sac.parser", parser.getClass().getCanonicalName());
        parser_ = parser;
        return;
      }
      

      String currentParser = System.getProperty("org.w3c.css.sac.parser");
      try
      {
        if ((null != LastFailed_) && (LastFailed_.equals(currentParser))) {
          parser_ = new SACParserCSS21();
        }
        else {
          if (null == currentParser) {
            System.setProperty("org.w3c.css.sac.parser", "com.steadystate.css.parser.SACParserCSS21");
            currentParser = "com.steadystate.css.parser.SACParserCSS21";
          }
          ParserFactory factory = new ParserFactory();
          parser_ = factory.makeParser();
        }
      }
      catch (Exception e) {
        Logger log = Logger.getLogger("com.steadystate.css");
        log.warning(e.toString());
        log.warning("using the default 'SACParserCSS21' instead");
        log.throwing("CSSOMParser", "consturctor", e);
        LastFailed_ = currentParser;
        parser_ = new SACParserCSS21();
      }
    }
  }
  
  public void setErrorHandler(ErrorHandler eh) {
    parser_.setErrorHandler(eh);
  }
  










  public CSSStyleSheet parseStyleSheet(InputSource source, Node ownerNode, String href)
    throws IOException
  {
    CSSOMHandler handler = new CSSOMHandler();
    handler.setOwnerNode(ownerNode);
    handler.setHref(href);
    parser_.setDocumentHandler(handler);
    parser_.parseStyleSheet(source);
    Object o = handler.getRoot();
    if ((o instanceof CSSStyleSheet)) {
      return (CSSStyleSheet)o;
    }
    return null;
  }
  





  public CSSStyleDeclaration parseStyleDeclaration(InputSource source)
    throws IOException
  {
    CSSStyleDeclarationImpl sd = new CSSStyleDeclarationImpl(null);
    parseStyleDeclaration(sd, source);
    return sd;
  }
  
  public void parseStyleDeclaration(CSSStyleDeclaration sd, InputSource source) throws IOException {
    Stack<Object> nodeStack = new Stack();
    nodeStack.push(sd);
    CSSOMHandler handler = new CSSOMHandler(nodeStack);
    parser_.setDocumentHandler(handler);
    parser_.parseStyleDeclaration(source);
  }
  
  public CSSValue parsePropertyValue(InputSource source) throws IOException {
    CSSOMHandler handler = new CSSOMHandler();
    parser_.setDocumentHandler(handler);
    LexicalUnit lu = parser_.parsePropertyValue(source);
    if (null == lu) {
      return null;
    }
    return new CSSValueImpl(lu);
  }
  
  public CSSRule parseRule(InputSource source) throws IOException {
    CSSOMHandler handler = new CSSOMHandler();
    parser_.setDocumentHandler(handler);
    parser_.parseRule(source);
    return (CSSRule)handler.getRoot();
  }
  
  public SelectorList parseSelectors(InputSource source) throws IOException {
    HandlerBase handler = new HandlerBase();
    parser_.setDocumentHandler(handler);
    return parser_.parseSelectors(source);
  }
  
  public SACMediaList parseMedia(InputSource source) throws IOException {
    HandlerBase handler = new HandlerBase();
    parser_.setDocumentHandler(handler);
    if ((parser_ instanceof AbstractSACParser)) {
      return ((AbstractSACParser)parser_).parseMedia(source);
    }
    return null;
  }
  
  public void setParentStyleSheet(CSSStyleSheetImpl parentStyleSheet) {
    parentStyleSheet_ = parentStyleSheet;
  }
  
  protected CSSStyleSheetImpl getParentStyleSheet() {
    return parentStyleSheet_;
  }
  
  class CSSOMHandler implements DocumentHandlerExt {
    private Stack<Object> nodeStack_;
    private Object root_;
    private Node ownerNode_;
    private String href_;
    
    private Node getOwnerNode() {
      return ownerNode_;
    }
    
    private void setOwnerNode(Node ownerNode) {
      ownerNode_ = ownerNode;
    }
    
    private String getHref() {
      return href_;
    }
    
    private void setHref(String href) {
      href_ = href;
    }
    
    CSSOMHandler() {
      nodeStack_ = nodeStack;
    }
    
    CSSOMHandler() {
      nodeStack_ = new Stack();
    }
    
    Object getRoot() {
      return root_;
    }
    
    public void startDocument(InputSource source) throws CSSException {
      if (nodeStack_.empty()) {
        CSSStyleSheetImpl ss = new CSSStyleSheetImpl();
        setParentStyleSheet(ss);
        ss.setOwnerNode(getOwnerNode());
        ss.setBaseUri(source.getURI());
        ss.setHref(getHref());
        ss.setMediaText(source.getMedia());
        ss.setTitle(source.getTitle());
        
        CSSRuleListImpl rules = new CSSRuleListImpl();
        ss.setCssRules(rules);
        nodeStack_.push(ss);
        nodeStack_.push(rules);
      }
    }
    


    public void endDocument(InputSource source)
      throws CSSException
    {
      nodeStack_.pop();
      root_ = nodeStack_.pop();
    }
    
    public void comment(String text) throws CSSException
    {}
    
    public void ignorableAtRule(String atRule) throws CSSException
    {
      ignorableAtRule(atRule, null);
    }
    

    public void ignorableAtRule(String atRule, Locator locator)
      throws CSSException
    {
      CSSUnknownRuleImpl ir = new CSSUnknownRuleImpl(getParentStyleSheet(), getParentRule(), atRule);
      
      addLocator(locator, ir);
      if (!nodeStack_.empty()) {
        ((CSSRuleListImpl)nodeStack_.peek()).add(ir);
      }
      else {
        root_ = ir;
      }
    }
    

    public void namespaceDeclaration(String prefix, String uri)
      throws CSSException
    {}
    
    public void charset(String characterEncoding, Locator locator)
      throws CSSException
    {
      CSSCharsetRuleImpl cr = new CSSCharsetRuleImpl(getParentStyleSheet(), getParentRule(), characterEncoding);
      
      addLocator(locator, cr);
      if (!nodeStack_.empty()) {
        ((CSSRuleListImpl)nodeStack_.peek()).add(cr);
      }
      else {
        root_ = cr;
      }
    }
    

    public void importStyle(String uri, SACMediaList media, String defaultNamespaceURI)
      throws CSSException
    {
      importStyle(uri, media, defaultNamespaceURI, null);
    }
    


    public void importStyle(String uri, SACMediaList media, String defaultNamespaceURI, Locator locator)
      throws CSSException
    {
      CSSImportRuleImpl ir = new CSSImportRuleImpl(getParentStyleSheet(), getParentRule(), uri, new MediaListImpl(media));
      

      addLocator(locator, ir);
      if (!nodeStack_.empty()) {
        ((CSSRuleListImpl)nodeStack_.peek()).add(ir);
      }
      else {
        root_ = ir;
      }
    }
    
    public void startMedia(SACMediaList media) throws CSSException {
      startMedia(media, null);
    }
    
    public void startMedia(SACMediaList media, Locator locator) throws CSSException {
      MediaListImpl ml = new MediaListImpl(media);
      


      CSSMediaRuleImpl mr = new CSSMediaRuleImpl(getParentStyleSheet(), getParentRule(), ml);
      
      addLocator(locator, mr);
      if (!nodeStack_.empty()) {
        ((CSSRuleListImpl)nodeStack_.peek()).add(mr);
      }
      

      CSSRuleListImpl rules = new CSSRuleListImpl();
      mr.setRuleList(rules);
      nodeStack_.push(mr);
      nodeStack_.push(rules);
    }
    
    public void endMedia(SACMediaList media) throws CSSException
    {
      nodeStack_.pop();
      root_ = nodeStack_.pop();
    }
    
    public void startPage(String name, String pseudoPage) throws CSSException {
      startPage(name, pseudoPage, null);
    }
    


    public void startPage(String name, String pseudoPage, Locator locator)
      throws CSSException
    {
      CSSPageRuleImpl pr = new CSSPageRuleImpl(getParentStyleSheet(), getParentRule(), pseudoPage);
      addLocator(locator, pr);
      if (!nodeStack_.empty()) {
        ((CSSRuleListImpl)nodeStack_.peek()).add(pr);
      }
      

      CSSStyleDeclarationImpl decl = new CSSStyleDeclarationImpl(pr);
      pr.setStyle(decl);
      nodeStack_.push(pr);
      nodeStack_.push(decl);
    }
    
    public void endPage(String name, String pseudoPage) throws CSSException
    {
      nodeStack_.pop();
      root_ = nodeStack_.pop();
    }
    
    public void startFontFace() throws CSSException {
      startFontFace(null);
    }
    

    public void startFontFace(Locator locator)
      throws CSSException
    {
      CSSFontFaceRuleImpl ffr = new CSSFontFaceRuleImpl(getParentStyleSheet(), getParentRule());
      addLocator(locator, ffr);
      if (!nodeStack_.empty()) {
        ((CSSRuleListImpl)nodeStack_.peek()).add(ffr);
      }
      

      CSSStyleDeclarationImpl decl = new CSSStyleDeclarationImpl(ffr);
      ffr.setStyle(decl);
      nodeStack_.push(ffr);
      nodeStack_.push(decl);
    }
    
    public void endFontFace() throws CSSException
    {
      nodeStack_.pop();
      root_ = nodeStack_.pop();
    }
    
    public void startSelector(SelectorList selectors) throws CSSException {
      startSelector(selectors, null);
    }
    

    public void startSelector(SelectorList selectors, Locator locator)
      throws CSSException
    {
      CSSStyleRuleImpl sr = new CSSStyleRuleImpl(getParentStyleSheet(), getParentRule(), selectors);
      addLocator(locator, sr);
      if (!nodeStack_.empty()) {
        Object o = nodeStack_.peek();
        ((CSSRuleListImpl)o).add(sr);
      }
      

      CSSStyleDeclarationImpl decl = new CSSStyleDeclarationImpl(sr);
      sr.setStyle(decl);
      nodeStack_.push(sr);
      nodeStack_.push(decl);
    }
    
    public void endSelector(SelectorList selectors) throws CSSException
    {
      nodeStack_.pop();
      root_ = nodeStack_.pop();
    }
    
    public void property(String name, LexicalUnit value, boolean important) throws CSSException {
      property(name, value, important, null);
    }
    
    public void property(String name, LexicalUnit value, boolean important, Locator locator)
    {
      CSSStyleDeclarationImpl decl = (CSSStyleDeclarationImpl)nodeStack_.peek();
      try {
        Property property = new Property(name, new CSSValueImpl(value), important);
        addLocator(locator, property);
        decl.addProperty(property);
      }
      catch (DOMException e) {
        if ((parser_ instanceof AbstractSACParser)) {
          AbstractSACParser parser = (AbstractSACParser)parser_;
          parser.getErrorHandler().error(parser.toCSSParseException(e));
        }
      }
    }
    

    private CSSRule getParentRule()
    {
      if ((!nodeStack_.empty()) && (nodeStack_.size() > 1)) {
        Object node = nodeStack_.get(nodeStack_.size() - 2);
        if ((node instanceof CSSRule)) {
          return (CSSRule)node;
        }
      }
      return null;
    }
    
    private void addLocator(Locator locator, CSSOMObject cssomObject) {
      if (locator == null) {
        Parser parser = parser_;
        try {
          Method getLocatorMethod = parser.getClass().getMethod("getLocator", (Class[])null);
          
          locator = (Locator)getLocatorMethod.invoke(parser, (Object[])null);
        }
        catch (SecurityException e) {}catch (NoSuchMethodException e) {}catch (IllegalArgumentException e) {}catch (IllegalAccessException e) {}catch (InvocationTargetException e) {}
      }
      














      if (locator != null) {
        cssomObject.setUserData(UserDataConstants.KEY_LOCATOR, locator);
      }
    }
  }
}
