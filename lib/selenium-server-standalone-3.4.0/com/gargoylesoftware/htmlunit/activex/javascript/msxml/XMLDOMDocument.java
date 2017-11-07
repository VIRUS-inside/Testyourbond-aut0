package com.gargoylesoftware.htmlunit.activex.javascript.msxml;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.StringWebResponse;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomComment;
import com.gargoylesoftware.htmlunit.html.DomDocumentFragment;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomProcessingInstruction;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.xml.XmlPage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Element;






























@JsxClass(browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
public class XMLDOMDocument
  extends XMLDOMNode
{
  private static final Log LOG = LogFactory.getLog(XMLDOMDocument.class);
  
  private boolean async_ = true;
  private XMLDOMImplementation implementation_;
  private boolean preserveWhiteSpace_;
  private boolean preserveWhiteSpaceDuringLoad_ = true;
  private XMLDOMParseError parseError_;
  private Map<String, String> properties_ = new HashMap();
  private String url_ = "";
  


  public XMLDOMDocument()
  {
    this(null);
  }
  



  public XMLDOMDocument(WebWindow enclosingWindow)
  {
    if (enclosingWindow != null) {
      try {
        XmlPage page = new XmlPage(null, enclosingWindow, true, false);
        setDomNode(page);
      }
      catch (IOException e) {
        throw Context.reportRuntimeError("IOException: " + e);
      }
    }
  }
  



  @JsxGetter
  public boolean getAsync()
  {
    return async_;
  }
  



  @JsxSetter
  public void setAsync(boolean async)
  {
    async_ = async;
  }
  



  @JsxGetter
  public XMLDOMDocumentType getDoctype()
  {
    Object documentType = getPage().getDoctype();
    if (documentType == null) {
      return null;
    }
    return (XMLDOMDocumentType)getScriptableFor(documentType);
  }
  



  @JsxGetter
  public XMLDOMElement getDocumentElement()
  {
    Object documentElement = getPage().getDocumentElement();
    if (documentElement == null)
    {
      return null;
    }
    return (XMLDOMElement)getScriptableFor(documentElement);
  }
  



  @JsxSetter
  public void setDocumentElement(XMLDOMElement element)
  {
    if (element == null) {
      throw Context.reportRuntimeError("Type mismatch.");
    }
    
    XMLDOMElement documentElement = getDocumentElement();
    if (documentElement != null) {
      documentElement.getDomNodeOrDie().remove();
    }
    
    appendChild(element);
  }
  



  @JsxGetter
  public XMLDOMImplementation getImplementation()
  {
    if (implementation_ == null) {
      implementation_ = new XMLDOMImplementation();
      implementation_.setParentScope(getWindow());
      implementation_.setPrototype(getPrototype(implementation_.getClass()));
    }
    return implementation_;
  }
  




  public void setNodeValue(String value)
  {
    if ((value == null) || ("null".equals(value))) {
      throw Context.reportRuntimeError("Type mismatch.");
    }
    throw Context.reportRuntimeError("This operation cannot be performed with a node of type DOCUMENT.");
  }
  



  public Object getOwnerDocument()
  {
    return null;
  }
  



  @JsxGetter
  public XMLDOMParseError getParseError()
  {
    if (parseError_ == null) {
      parseError_ = new XMLDOMParseError();
      parseError_.setParentScope(getParentScope());
      parseError_.setPrototype(getPrototype(parseError_.getClass()));
    }
    return parseError_;
  }
  



  @JsxGetter
  public boolean getPreserveWhiteSpace()
  {
    return preserveWhiteSpace_;
  }
  



  @JsxSetter
  public void setPreserveWhiteSpace(boolean preserveWhiteSpace)
  {
    preserveWhiteSpace_ = preserveWhiteSpace;
  }
  



  public Object getText()
  {
    XMLDOMElement element = getDocumentElement();
    if (element == null) {
      return "";
    }
    return element.getText();
  }
  




  public void setText(Object text)
  {
    if ((text == null) || ("null".equals(text))) {
      throw Context.reportRuntimeError("Type mismatch.");
    }
    throw Context.reportRuntimeError("This operation cannot be performed with a node of type DOCUMENT.");
  }
  



  @JsxGetter
  public String getUrl()
  {
    return url_;
  }
  




  @JsxGetter
  public String getXml()
  {
    XMLSerializer seralizer = new XMLSerializer(preserveWhiteSpaceDuringLoad_);
    return seralizer.serializeToString(getDocumentElement());
  }
  



  public Object appendChild(Object newChild)
  {
    verifyChild(newChild);
    
    return super.appendChild(newChild);
  }
  
  private void verifyChild(Object newChild) {
    if ((newChild == null) || ("null".equals(newChild)) || (!(newChild instanceof XMLDOMNode))) {
      throw Context.reportRuntimeError("Type mismatch.");
    }
    if ((newChild instanceof XMLDOMCDATASection)) {
      throw Context.reportRuntimeError("This operation cannot be performed with a node of type CDATA.");
    }
    if ((newChild instanceof XMLDOMText)) {
      throw Context.reportRuntimeError("This operation cannot be performed with a node of type TEXT.");
    }
    if (((newChild instanceof XMLDOMElement)) && (getDocumentElement() != null)) {
      throw Context.reportRuntimeError("Only one top level element is allowed in an XML document.");
    }
    if ((newChild instanceof XMLDOMDocumentFragment)) {
      boolean elementFound = false;
      XMLDOMNode child = ((XMLDOMDocumentFragment)newChild).getFirstChild();
      while (child != null) {
        if ((child instanceof XMLDOMCDATASection)) {
          throw Context.reportRuntimeError("This operation cannot be performed with a node of type CDATA.");
        }
        if ((child instanceof XMLDOMText)) {
          throw Context.reportRuntimeError("This operation cannot be performed with a node of type TEXT.");
        }
        if ((child instanceof XMLDOMElement)) {
          if (elementFound) {
            throw Context.reportRuntimeError("Only one top level element is allowed in an XML document.");
          }
          elementFound = true;
        }
        child = child.getNextSibling();
      }
    }
  }
  





  @JsxFunction
  public Object createAttribute(String name)
  {
    if ((name == null) || ("null".equals(name))) {
      throw Context.reportRuntimeError("Type mismatch.");
    }
    if ((StringUtils.isBlank(name)) || (name.indexOf('<') >= 0) || (name.indexOf('>') >= 0)) {
      throw Context.reportRuntimeError("To create a node of type ATTR a valid name must be given.");
    }
    
    DomAttr domAttr = getPage().createAttribute(name);
    return getScriptableFor(domAttr);
  }
  




  @JsxFunction
  public Object createCDATASection(String data)
  {
    CDATASection domCDATASection = getPage().createCDATASection(data);
    return getScriptableFor(domCDATASection);
  }
  




  @JsxFunction
  public Object createComment(String data)
  {
    DomComment domComment = new DomComment(getPage(), data);
    return getScriptableFor(domComment);
  }
  



  @JsxFunction
  public Object createDocumentFragment()
  {
    DomDocumentFragment domDocumentFragment = new DomDocumentFragment(getPage());
    return getScriptableFor(domDocumentFragment);
  }
  




  @JsxFunction
  public Object createElement(String tagName)
  {
    if ((tagName == null) || ("null".equals(tagName))) {
      throw Context.reportRuntimeError("Type mismatch.");
    }
    if ((StringUtils.isBlank(tagName)) || (tagName.indexOf('<') >= 0) || (tagName.indexOf('>') >= 0)) {
      throw Context.reportRuntimeError("To create a node of type ELEMENT a valid name must be given.");
    }
    
    Object result = NOT_FOUND;
    try {
      DomElement domElement = (DomElement)getPage().createElement(tagName);
      Object jsElement = getScriptableFor(domElement);
      
      if (jsElement == NOT_FOUND) {
        if (LOG.isDebugEnabled()) {
          LOG.debug("createElement(" + tagName + 
            ") cannot return a result as there isn't a JavaScript object for the element " + 
            domElement.getClass().getName());
        }
      }
      else {
        result = jsElement;
      }
    }
    catch (ElementNotFoundException localElementNotFoundException) {}
    

    return result;
  }
  









  @JsxFunction
  public Object createNode(Object type, String name, Object namespaceURI)
  {
    switch ((short)(int)Context.toNumber(type)) {
    case 1: 
      return createElementNS((String)namespaceURI, name);
    case 2: 
      return createAttribute(name);
    }
    
    throw Context.reportRuntimeError("xmlDoc.createNode(): Unsupported type " + 
      (short)(int)Context.toNumber(type));
  }
  







  private Object createElementNS(String namespaceURI, String qualifiedName)
  {
    if ("http://www.mozilla.org/keymaster/gatekeeper/there.is.only.xul".equals(namespaceURI))
      throw Context.reportRuntimeError("XUL not available");
    Element element;
    Element element; if (("http://www.w3.org/1999/xhtml".equals(namespaceURI)) || 
      ("http://www.w3.org/2000/svg".equals(namespaceURI))) {
      element = getPage().createElementNS(namespaceURI, qualifiedName);
    }
    else {
      element = new DomElement(namespaceURI, qualifiedName, getPage(), null);
    }
    return getScriptableFor(element);
  }
  





  @JsxFunction
  public Object createProcessingInstruction(String target, String data)
  {
    DomProcessingInstruction domProcessingInstruction = 
      ((XmlPage)getPage()).createProcessingInstruction(target, data);
    return getScriptableFor(domProcessingInstruction);
  }
  




  @JsxFunction
  public Object createTextNode(String data)
  {
    Object result = NOT_FOUND;
    try {
      DomText domText = new DomText(getPage(), data);
      Object jsElement = getScriptableFor(domText);
      
      if (jsElement == NOT_FOUND) {
        if (LOG.isDebugEnabled()) {
          LOG.debug("createTextNode(" + data + 
            ") cannot return a result as there isn't a JavaScript object for the DOM node " + 
            domText.getClass().getName());
        }
      }
      else {
        result = jsElement;
      }
    }
    catch (ElementNotFoundException localElementNotFoundException) {}
    

    return result;
  }
  




  @JsxFunction
  public XMLDOMNodeList getElementsByTagName(final String tagName)
  {
    DomNode firstChild = getDomNodeOrDie().getFirstChild();
    if (firstChild == null) {
      return XMLDOMNodeList.emptyCollection(this);
    }
    
    XMLDOMNodeList collection = new XMLDOMNodeList(getDomNodeOrDie(), false, 
      "XMLDOMDocument.getElementsByTagName")
      {
        protected boolean isMatching(DomNode node)
        {
          return node.getNodeName().equals(tagName);
        }
        
      };
      return collection;
    }
    





    @JsxFunction
    public String getProperty(String name)
    {
      return (String)properties_.get(name);
    }
    



    protected Object insertBeforeImpl(Object[] args)
    {
      Object newChild = args[0];
      verifyChild(newChild);
      if (args.length != 2) {
        throw Context.reportRuntimeError("Wrong number of arguments or invalid property assignment.");
      }
      
      return super.insertBeforeImpl(args);
    }
    




    @JsxFunction
    public boolean load(String xmlSource)
    {
      if ((async_) && (LOG.isDebugEnabled())) {
        LOG.debug("XMLDOMDocument.load(): 'async' is true, currently treated as false.");
      }
      try {
        HtmlPage htmlPage = (HtmlPage)getWindow().getWebWindow().getEnclosedPage();
        URL fullyQualifiedURL = htmlPage.getFullyQualifiedUrl(xmlSource);
        WebRequest request = new WebRequest(fullyQualifiedURL);
        WebResponse webResponse = getWindow().getWebWindow().getWebClient().loadWebResponse(request);
        XmlPage page = new XmlPage(webResponse, getWindow().getWebWindow(), false, false);
        setDomNode(page);
        
        preserveWhiteSpaceDuringLoad_ = preserveWhiteSpace_;
        url_ = fullyQualifiedURL.toExternalForm();
        return true;
      }
      catch (IOException e) {
        XMLDOMParseError parseError = getParseError();
        parseError.setErrorCode(-1);
        parseError.setFilepos(1);
        parseError.setLine(1);
        parseError.setLinepos(1);
        parseError.setReason(e.getMessage());
        parseError.setSrcText("xml");
        parseError.setUrl(xmlSource);
        if (LOG.isDebugEnabled())
          LOG.debug("Error parsing XML from '" + xmlSource + "'", e);
      }
      return false;
    }
    





    @JsxFunction
    public boolean loadXML(String strXML)
    {
      try
      {
        WebWindow webWindow = getWindow().getWebWindow();
        
        WebResponse webResponse = new StringWebResponse(strXML, webWindow.getEnclosedPage().getUrl());
        XmlPage page = new XmlPage(webResponse, webWindow, false, false);
        setDomNode(page);
        
        preserveWhiteSpaceDuringLoad_ = preserveWhiteSpace_;
        url_ = "";
        return true;
      }
      catch (IOException e) {
        XMLDOMParseError parseError = getParseError();
        parseError.setErrorCode(-1);
        parseError.setFilepos(1);
        parseError.setLine(1);
        parseError.setLinepos(1);
        parseError.setReason(e.getMessage());
        parseError.setSrcText("xml");
        parseError.setUrl("");
        if (LOG.isDebugEnabled())
          LOG.debug("Error parsing XML\n" + strXML, e);
      }
      return false;
    }
    





    @JsxFunction
    public Object nodeFromID(String id)
    {
      return null;
    }
    




    @JsxFunction
    public void setProperty(String name, String value)
    {
      properties_.put(name, value);
    }
    


    public boolean isPreserveWhiteSpaceDuringLoad()
    {
      return preserveWhiteSpaceDuringLoad_;
    }
    


    protected SgmlPage getPage()
    {
      return (SgmlPage)getDomNodeOrDie();
    }
    


    public MSXMLScriptable makeScriptableFor(DomNode domNode)
    {
      MSXMLScriptable scriptable;
      

      if (((domNode instanceof DomElement)) && (!(domNode instanceof HtmlElement))) {
        scriptable = new XMLDOMElement();
      } else { MSXMLScriptable scriptable;
        if ((domNode instanceof DomAttr)) {
          scriptable = new XMLDOMAttribute();
        }
        else
          return (MSXMLScriptable)super.makeScriptableFor(domNode);
      }
      MSXMLScriptable scriptable;
      scriptable.setParentScope(this);
      scriptable.setPrototype(getPrototype(scriptable.getClass()));
      scriptable.setDomNode(domNode);
      
      return scriptable;
    }
    



    protected void initParentScope(DomNode domNode, SimpleScriptable scriptable)
    {
      scriptable.setParentScope(getParentScope());
    }
  }
