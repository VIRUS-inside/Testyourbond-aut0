package org.apache.xalan.templates;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.UnImplNode;
import org.apache.xpath.ExpressionNode;
import org.apache.xpath.WhitespaceStrippingElementMatcher;
import org.apache.xpath.XPathContext;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.NamespaceSupport;









































public class ElemTemplateElement
  extends UnImplNode
  implements PrefixResolver, Serializable, ExpressionNode, WhitespaceStrippingElementMatcher, XSLTVisitable
{
  static final long serialVersionUID = 4440018597841834447L;
  private int m_lineNumber;
  private int m_endLineNumber;
  private int m_columnNumber;
  private int m_endColumnNumber;
  
  public ElemTemplateElement() {}
  
  public boolean isCompiledTemplate()
  {
    return false;
  }
  







  public int getXSLToken()
  {
    return -1;
  }
  





  public String getNodeName()
  {
    return "Unknown XSLT Element";
  }
  







  public String getLocalName()
  {
    return getNodeName();
  }
  








  public void runtimeInit(TransformerImpl transformer)
    throws TransformerException
  {}
  








  public void execute(TransformerImpl transformer)
    throws TransformerException
  {}
  







  public StylesheetComposed getStylesheetComposed()
  {
    return m_parentNode.getStylesheetComposed();
  }
  







  public Stylesheet getStylesheet()
  {
    return null == m_parentNode ? null : m_parentNode.getStylesheet();
  }
  








  public StylesheetRoot getStylesheetRoot()
  {
    return m_parentNode.getStylesheetRoot();
  }
  





  public void recompose(StylesheetRoot root)
    throws TransformerException
  {}
  




  public void compose(StylesheetRoot sroot)
    throws TransformerException
  {
    resolvePrefixTables();
    ElemTemplateElement t = getFirstChildElem();
    m_hasTextLitOnly = ((t != null) && (t.getXSLToken() == 78) && (t.getNextSiblingElem() == null));
    


    StylesheetRoot.ComposeState cstate = sroot.getComposeState();
    cstate.pushStackMark();
  }
  


  public void endCompose(StylesheetRoot sroot)
    throws TransformerException
  {
    StylesheetRoot.ComposeState cstate = sroot.getComposeState();
    cstate.popStackMark();
  }
  







  public void error(String msg, Object[] args)
  {
    String themsg = XSLMessages.createMessage(msg, args);
    
    throw new RuntimeException(XSLMessages.createMessage("ER_ELEMTEMPLATEELEM_ERR", new Object[] { themsg }));
  }
  








  public void error(String msg)
  {
    error(msg, null);
  }
  















  public Node appendChild(Node newChild)
    throws DOMException
  {
    if (null == newChild)
    {
      error("ER_NULL_CHILD", null);
    }
    
    ElemTemplateElement elem = (ElemTemplateElement)newChild;
    
    if (null == m_firstChild)
    {
      m_firstChild = elem;
    }
    else
    {
      ElemTemplateElement last = (ElemTemplateElement)getLastChild();
      
      m_nextSibling = elem;
    }
    
    m_parentNode = this;
    
    return newChild;
  }
  













  public ElemTemplateElement appendChild(ElemTemplateElement elem)
  {
    if (null == elem)
    {
      error("ER_NULL_CHILD", null);
    }
    
    if (null == m_firstChild)
    {
      m_firstChild = elem;
    }
    else
    {
      ElemTemplateElement last = getLastChildElem();
      
      m_nextSibling = elem;
    }
    
    elem.setParentElem(this);
    
    return elem;
  }
  






  public boolean hasChildNodes()
  {
    return null != m_firstChild;
  }
  





  public short getNodeType()
  {
    return 1;
  }
  





  public NodeList getChildNodes()
  {
    return this;
  }
  















  public ElemTemplateElement removeChild(ElemTemplateElement childETE)
  {
    if ((childETE == null) || (m_parentNode != this)) {
      return null;
    }
    
    if (childETE == m_firstChild) {
      m_firstChild = m_nextSibling;
    }
    else {
      ElemTemplateElement prev = childETE.getPreviousSiblingElem();
      
      m_nextSibling = m_nextSibling;
    }
    

    m_parentNode = null;
    m_nextSibling = null;
    
    return childETE;
  }
  










  public Node replaceChild(Node newChild, Node oldChild)
    throws DOMException
  {
    if ((oldChild == null) || (oldChild.getParentNode() != this)) {
      return null;
    }
    ElemTemplateElement newChildElem = (ElemTemplateElement)newChild;
    ElemTemplateElement oldChildElem = (ElemTemplateElement)oldChild;
    

    ElemTemplateElement prev = (ElemTemplateElement)oldChildElem.getPreviousSibling();
    

    if (null != prev) {
      m_nextSibling = newChildElem;
    }
    
    if (m_firstChild == oldChildElem) {
      m_firstChild = newChildElem;
    }
    m_parentNode = this;
    m_parentNode = null;
    m_nextSibling = m_nextSibling;
    m_nextSibling = null;
    


    return newChildElem;
  }
  









  public Node insertBefore(Node newChild, Node refChild)
    throws DOMException
  {
    if (null == refChild)
    {
      appendChild(newChild);
      return newChild;
    }
    
    if (newChild == refChild)
    {

      return newChild;
    }
    
    Node node = m_firstChild;
    Node prev = null;
    boolean foundit = false;
    
    while (null != node)
    {

      if (newChild == node)
      {
        if (null != prev) {
          m_nextSibling = ((ElemTemplateElement)node.getNextSibling());
        }
        else
          m_firstChild = ((ElemTemplateElement)node.getNextSibling());
        node = node.getNextSibling();

      }
      else if (refChild == node)
      {
        if (null != prev)
        {
          m_nextSibling = ((ElemTemplateElement)newChild);
        }
        else
        {
          m_firstChild = ((ElemTemplateElement)newChild);
        }
        m_nextSibling = ((ElemTemplateElement)refChild);
        ((ElemTemplateElement)newChild).setParentElem(this);
        prev = newChild;
        node = node.getNextSibling();
        foundit = true;
      }
      else {
        prev = node;
        node = node.getNextSibling();
      }
    }
    if (!foundit) {
      throw new DOMException((short)8, "refChild was not found in insertBefore method!");
    }
    
    return newChild;
  }
  













  public ElemTemplateElement replaceChild(ElemTemplateElement newChildElem, ElemTemplateElement oldChildElem)
  {
    if ((oldChildElem == null) || (oldChildElem.getParentElem() != this)) {
      return null;
    }
    
    ElemTemplateElement prev = oldChildElem.getPreviousSiblingElem();
    

    if (null != prev) {
      m_nextSibling = newChildElem;
    }
    
    if (m_firstChild == oldChildElem) {
      m_firstChild = newChildElem;
    }
    m_parentNode = this;
    m_parentNode = null;
    m_nextSibling = m_nextSibling;
    m_nextSibling = null;
    


    return newChildElem;
  }
  









  public int getLength()
  {
    int count = 0;
    
    for (ElemTemplateElement node = m_firstChild; node != null; 
        node = m_nextSibling)
    {
      count++;
    }
    
    return count;
  }
  











  public Node item(int index)
  {
    ElemTemplateElement node = m_firstChild;
    
    for (int i = 0; (i < index) && (node != null); i++)
    {
      node = m_nextSibling;
    }
    
    return node;
  }
  





  public Document getOwnerDocument()
  {
    return getStylesheet();
  }
  





  public ElemTemplate getOwnerXSLTemplate()
  {
    ElemTemplateElement el = this;
    int type = el.getXSLToken();
    while ((null != el) && (type != 19))
    {
      el = el.getParentElem();
      if (null != el)
        type = el.getXSLToken();
    }
    return (ElemTemplate)el;
  }
  






  public String getTagName()
  {
    return getNodeName();
  }
  




  public boolean hasTextLitOnly()
  {
    return m_hasTextLitOnly;
  }
  







  public String getBaseIdentifier()
  {
    return getSystemId();
  }
  















  public int getEndLineNumber()
  {
    return m_endLineNumber;
  }
  







  public int getLineNumber()
  {
    return m_lineNumber;
  }
  
















  public int getEndColumnNumber()
  {
    return m_endColumnNumber;
  }
  








  public int getColumnNumber()
  {
    return m_columnNumber;
  }
  







  public String getPublicId()
  {
    return null != m_parentNode ? m_parentNode.getPublicId() : null;
  }
  










  public String getSystemId()
  {
    Stylesheet sheet = getStylesheet();
    return sheet == null ? null : sheet.getHref();
  }
  





  public void setLocaterInfo(SourceLocator locator)
  {
    m_lineNumber = locator.getLineNumber();
    m_columnNumber = locator.getColumnNumber();
  }
  





  public void setEndLocaterInfo(SourceLocator locator)
  {
    m_endLineNumber = locator.getLineNumber();
    m_endColumnNumber = locator.getColumnNumber();
  }
  





  private boolean m_defaultSpace = true;
  




  private boolean m_hasTextLitOnly = false;
  




  protected boolean m_hasVariableDecl = false;
  private List m_declaredPrefixes;
  private List m_prefixTable;
  
  public boolean hasVariableDecl() { return m_hasVariableDecl; }
  












  public void setXmlSpace(int v)
  {
    m_defaultSpace = (2 == v);
  }
  










  public boolean getXmlSpace()
  {
    return m_defaultSpace;
  }
  













  public List getDeclaredPrefixes()
  {
    return m_declaredPrefixes;
  }
  









  public void setPrefixes(NamespaceSupport nsSupport)
    throws TransformerException
  {
    setPrefixes(nsSupport, false);
  }
  












  public void setPrefixes(NamespaceSupport nsSupport, boolean excludeXSLDecl)
    throws TransformerException
  {
    Enumeration decls = nsSupport.getDeclaredPrefixes();
    
    while (decls.hasMoreElements())
    {
      String prefix = (String)decls.nextElement();
      
      if (null == m_declaredPrefixes) {
        m_declaredPrefixes = new ArrayList();
      }
      String uri = nsSupport.getURI(prefix);
      
      if ((!excludeXSLDecl) || (!uri.equals("http://www.w3.org/1999/XSL/Transform")))
      {


        XMLNSDecl decl = new XMLNSDecl(prefix, uri, false);
        
        m_declaredPrefixes.add(decl);
      }
    }
  }
  









  public String getNamespaceForPrefix(String prefix, Node context)
  {
    error("ER_CANT_RESOLVE_NSPREFIX", null);
    
    return null;
  }
  



















  public String getNamespaceForPrefix(String prefix)
  {
    List nsDecls = m_declaredPrefixes;
    
    if (null != nsDecls)
    {
      int n = nsDecls.size();
      if (prefix.equals("#default"))
      {
        prefix = "";
      }
      
      for (int i = 0; i < n; i++)
      {
        XMLNSDecl decl = (XMLNSDecl)nsDecls.get(i);
        
        if (prefix.equals(decl.getPrefix())) {
          return decl.getURI();
        }
      }
    }
    
    if (null != m_parentNode) {
      return m_parentNode.getNamespaceForPrefix(prefix);
    }
    


    if ("xml".equals(prefix)) {
      return "http://www.w3.org/XML/1998/namespace";
    }
    
    return null;
  }
  













  List getPrefixTable()
  {
    return m_prefixTable;
  }
  
  void setPrefixTable(List list) {
    m_prefixTable = list;
  }
  










  public boolean containsExcludeResultPrefix(String prefix, String uri)
  {
    ElemTemplateElement parent = getParentElem();
    if (null != parent) {
      return parent.containsExcludeResultPrefix(prefix, uri);
    }
    return false;
  }
  













  private boolean excludeResultNSDecl(String prefix, String uri)
    throws TransformerException
  {
    if (uri != null)
    {
      if ((uri.equals("http://www.w3.org/1999/XSL/Transform")) || (getStylesheet().containsExtensionElementURI(uri)))
      {
        return true;
      }
      if (containsExcludeResultPrefix(prefix, uri)) {
        return true;
      }
    }
    return false;
  }
  











  public void resolvePrefixTables()
    throws TransformerException
  {
    setPrefixTable(null);
    




    if (null != m_declaredPrefixes)
    {
      StylesheetRoot stylesheet = getStylesheetRoot();
      


      int n = m_declaredPrefixes.size();
      
      for (int i = 0; i < n; i++)
      {
        XMLNSDecl decl = (XMLNSDecl)m_declaredPrefixes.get(i);
        String prefix = decl.getPrefix();
        String uri = decl.getURI();
        if (null == uri)
          uri = "";
        boolean shouldExclude = excludeResultNSDecl(prefix, uri);
        

        if (null == m_prefixTable) {
          setPrefixTable(new ArrayList());
        }
        NamespaceAlias nsAlias = stylesheet.getNamespaceAliasComposed(uri);
        if (null != nsAlias)
        {






          decl = new XMLNSDecl(nsAlias.getStylesheetPrefix(), nsAlias.getResultNamespace(), shouldExclude);
        }
        else
        {
          decl = new XMLNSDecl(prefix, uri, shouldExclude);
        }
        m_prefixTable.add(decl);
      }
    }
    

    ElemTemplateElement parent = getParentNodeElem();
    
    if (null != parent)
    {


      List prefixes = m_prefixTable;
      
      if ((null == m_prefixTable) && (!needToCheckExclude()))
      {


        setPrefixTable(m_prefixTable);

      }
      else
      {

        int n = prefixes.size();
        
        for (int i = 0; i < n; i++)
        {
          XMLNSDecl decl = (XMLNSDecl)prefixes.get(i);
          boolean shouldExclude = excludeResultNSDecl(decl.getPrefix(), decl.getURI());
          

          if (shouldExclude != decl.getIsExcluded())
          {
            decl = new XMLNSDecl(decl.getPrefix(), decl.getURI(), shouldExclude);
          }
          


          addOrReplaceDecls(decl);
        }
      }
    }
    else if (null == m_prefixTable)
    {


      setPrefixTable(new ArrayList());
    }
  }
  






  void addOrReplaceDecls(XMLNSDecl newDecl)
  {
    int n = m_prefixTable.size();
    
    for (int i = n - 1; i >= 0; i--)
    {
      XMLNSDecl decl = (XMLNSDecl)m_prefixTable.get(i);
      
      if (decl.getPrefix().equals(newDecl.getPrefix()))
      {
        return;
      }
    }
    m_prefixTable.add(newDecl);
  }
  





  boolean needToCheckExclude()
  {
    return false;
  }
  







  void executeNSDecls(TransformerImpl transformer)
    throws TransformerException
  {
    executeNSDecls(transformer, null);
  }
  








  void executeNSDecls(TransformerImpl transformer, String ignorePrefix)
    throws TransformerException
  {
    try
    {
      if (null != m_prefixTable)
      {
        SerializationHandler rhandler = transformer.getResultTreeHandler();
        int n = m_prefixTable.size();
        
        for (int i = n - 1; i >= 0; i--)
        {
          XMLNSDecl decl = (XMLNSDecl)m_prefixTable.get(i);
          
          if ((!decl.getIsExcluded()) && ((null == ignorePrefix) || (!decl.getPrefix().equals(ignorePrefix))))
          {
            rhandler.startPrefixMapping(decl.getPrefix(), decl.getURI(), true);
          }
        }
      }
    }
    catch (SAXException se)
    {
      throw new TransformerException(se);
    }
  }
  







  void unexecuteNSDecls(TransformerImpl transformer)
    throws TransformerException
  {
    unexecuteNSDecls(transformer, null);
  }
  









  void unexecuteNSDecls(TransformerImpl transformer, String ignorePrefix)
    throws TransformerException
  {
    try
    {
      if (null != m_prefixTable)
      {
        SerializationHandler rhandler = transformer.getResultTreeHandler();
        int n = m_prefixTable.size();
        
        for (int i = 0; i < n; i++)
        {
          XMLNSDecl decl = (XMLNSDecl)m_prefixTable.get(i);
          
          if ((!decl.getIsExcluded()) && ((null == ignorePrefix) || (!decl.getPrefix().equals(ignorePrefix))))
          {
            rhandler.endPrefixMapping(decl.getPrefix());
          }
        }
      }
    }
    catch (SAXException se)
    {
      throw new TransformerException(se);
    }
  }
  


  protected int m_docOrderNumber = -1;
  
  protected ElemTemplateElement m_parentNode;
  ElemTemplateElement m_nextSibling;
  ElemTemplateElement m_firstChild;
  private transient Node m_DOMBackPointer;
  
  public void setUid(int i)
  {
    m_docOrderNumber = i;
  }
  





  public int getUid()
  {
    return m_docOrderNumber;
  }
  












  public Node getParentNode()
  {
    return m_parentNode;
  }
  





  public ElemTemplateElement getParentElem()
  {
    return m_parentNode;
  }
  





  public void setParentElem(ElemTemplateElement p)
  {
    m_parentNode = p;
  }
  











  public Node getNextSibling()
  {
    return m_nextSibling;
  }
  










  public Node getPreviousSibling()
  {
    Node walker = getParentNode();Node prev = null;
    
    if (walker != null) {
      for (walker = walker.getFirstChild(); walker != null; 
          walker = walker.getNextSibling())
      {
        if (walker == this) {
          return prev;
        }
        prev = walker;
      }
    }
    


    return null;
  }
  










  public ElemTemplateElement getPreviousSiblingElem()
  {
    ElemTemplateElement walker = getParentNodeElem();
    ElemTemplateElement prev = null;
    
    if (walker != null) {
      for (walker = walker.getFirstChildElem(); walker != null; 
          walker = walker.getNextSiblingElem())
      {
        if (walker == this) {
          return prev;
        }
        prev = walker;
      }
    }
    


    return null;
  }
  






  public ElemTemplateElement getNextSiblingElem()
  {
    return m_nextSibling;
  }
  





  public ElemTemplateElement getParentNodeElem()
  {
    return m_parentNode;
  }
  












  public Node getFirstChild()
  {
    return m_firstChild;
  }
  





  public ElemTemplateElement getFirstChildElem()
  {
    return m_firstChild;
  }
  






  public Node getLastChild()
  {
    ElemTemplateElement lastChild = null;
    
    for (ElemTemplateElement node = m_firstChild; node != null; 
        node = m_nextSibling)
    {
      lastChild = node;
    }
    
    return lastChild;
  }
  






  public ElemTemplateElement getLastChildElem()
  {
    ElemTemplateElement lastChild = null;
    
    for (ElemTemplateElement node = m_firstChild; node != null; 
        node = m_nextSibling)
    {
      lastChild = node;
    }
    
    return lastChild;
  }
  











  public Node getDOMBackPointer()
  {
    return m_DOMBackPointer;
  }
  







  public void setDOMBackPointer(Node n)
  {
    m_DOMBackPointer = n;
  }
  












  public int compareTo(Object o)
    throws ClassCastException
  {
    ElemTemplateElement ro = (ElemTemplateElement)o;
    int roPrecedence = ro.getStylesheetComposed().getImportCountComposed();
    int myPrecedence = getStylesheetComposed().getImportCountComposed();
    
    if (myPrecedence < roPrecedence)
      return -1;
    if (myPrecedence > roPrecedence) {
      return 1;
    }
    return getUid() - ro.getUid();
  }
  












  public boolean shouldStripWhiteSpace(XPathContext support, Element targetElement)
    throws TransformerException
  {
    StylesheetRoot sroot = getStylesheetRoot();
    return null != sroot ? sroot.shouldStripWhiteSpace(support, targetElement) : false;
  }
  






  public boolean canStripWhiteSpace()
  {
    StylesheetRoot sroot = getStylesheetRoot();
    return null != sroot ? sroot.canStripWhiteSpace() : false;
  }
  




  public boolean canAcceptVariables()
  {
    return true;
  }
  








  public void exprSetParent(ExpressionNode n)
  {
    setParentElem((ElemTemplateElement)n);
  }
  



  public ExpressionNode exprGetParent()
  {
    return getParentElem();
  }
  





  public void exprAddChild(ExpressionNode n, int i)
  {
    appendChild((ElemTemplateElement)n);
  }
  


  public ExpressionNode exprGetChild(int i)
  {
    return (ExpressionNode)item(i);
  }
  

  public int exprGetNumChildren()
  {
    return getLength();
  }
  







  protected boolean accept(XSLTVisitor visitor)
  {
    return visitor.visitInstruction(this);
  }
  



  public void callVisitors(XSLTVisitor visitor)
  {
    if (accept(visitor))
    {
      callChildVisitors(visitor);
    }
  }
  




  protected void callChildVisitors(XSLTVisitor visitor, boolean callAttributes)
  {
    for (ElemTemplateElement node = m_firstChild; 
        node != null; 
        node = m_nextSibling)
    {
      node.callVisitors(visitor);
    }
  }
  




  protected void callChildVisitors(XSLTVisitor visitor)
  {
    callChildVisitors(visitor, true);
  }
  



  public boolean handlesNullPrefixes()
  {
    return false;
  }
}
