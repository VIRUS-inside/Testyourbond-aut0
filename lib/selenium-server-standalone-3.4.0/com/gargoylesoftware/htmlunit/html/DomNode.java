package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.IncorrectnessListener;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.xpath.XPathUtils;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleDeclaration;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleSheet;
import com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;
import com.gargoylesoftware.htmlunit.xml.XmlPage;
import com.steadystate.css.parser.CSSOMParser;
import com.steadystate.css.parser.SACParserCSS3;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import org.apache.xml.utils.PrefixResolver;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.CSSParseException;
import org.w3c.css.sac.ErrorHandler;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SelectorList;
import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.UserDataHandler;


















































































public abstract class DomNode
  implements Cloneable, Serializable, Node
{
  protected static final String AS_TEXT_BLOCK_SEPARATOR = "§bs§";
  protected static final String AS_TEXT_NEW_LINE = "§nl§";
  protected static final String AS_TEXT_BLANK = "§blank§";
  protected static final String AS_TEXT_TAB = "§tab§";
  public static final String READY_STATE_UNINITIALIZED = "uninitialized";
  public static final String READY_STATE_LOADING = "loading";
  public static final String READY_STATE_LOADED = "loaded";
  public static final String READY_STATE_INTERACTIVE = "interactive";
  public static final String READY_STATE_COMPLETE = "complete";
  public static final String PROPERTY_ELEMENT = "element";
  private SgmlPage page_;
  private DomNode parent_;
  private DomNode previousSibling_;
  private DomNode nextSibling_;
  private DomNode firstChild_;
  private Object scriptObject_;
  private String readyState_;
  private int startLineNumber_ = -1;
  



  private int startColumnNumber_ = -1;
  



  private int endLineNumber_ = -1;
  



  private int endColumnNumber_ = -1;
  
  private boolean attachedToPage_;
  
  private final Object listeners_lock_ = new Serializable() {};
  
  private Collection<CharacterDataChangeListener> characterDataListeners_;
  
  private List<CharacterDataChangeListener> characterDataListenersList_;
  
  private Collection<DomChangeListener> domListeners_;
  
  private List<DomChangeListener> domListenersList_;
  
  private Map<String, Object> userData_;
  

  protected DomNode(SgmlPage page)
  {
    readyState_ = "loading";
    page_ = page;
  }
  





  void setStartLocation(int startLineNumber, int startColumnNumber)
  {
    startLineNumber_ = startLineNumber;
    startColumnNumber_ = startColumnNumber;
  }
  





  void setEndLocation(int endLineNumber, int endColumnNumber)
  {
    endLineNumber_ = endLineNumber;
    endColumnNumber_ = endColumnNumber;
  }
  



  public int getStartLineNumber()
  {
    return startLineNumber_;
  }
  



  public int getStartColumnNumber()
  {
    return startColumnNumber_;
  }
  




  public int getEndLineNumber()
  {
    return endLineNumber_;
  }
  




  public int getEndColumnNumber()
  {
    return endColumnNumber_;
  }
  



  public SgmlPage getPage()
  {
    return page_;
  }
  



  public HtmlPage getHtmlPageOrNull()
  {
    if ((page_ == null) || (!page_.isHtmlPage())) {
      return null;
    }
    return (HtmlPage)page_;
  }
  



  public org.w3c.dom.Document getOwnerDocument()
  {
    return getPage();
  }
  







  public void setScriptableObject(Object scriptObject)
  {
    scriptObject_ = scriptObject;
  }
  



  public DomNode getLastChild()
  {
    if (firstChild_ != null)
    {
      return firstChild_.previousSibling_;
    }
    return null;
  }
  



  public DomNode getParentNode()
  {
    return parent_;
  }
  



  protected void setParentNode(DomNode parent)
  {
    parent_ = parent;
  }
  



  public int getIndex()
  {
    int index = 0;
    for (DomNode n = previousSibling_; (n != null) && (nextSibling_ != null); n = previousSibling_) {
      index++;
    }
    return index;
  }
  



  public DomNode getPreviousSibling()
  {
    if ((parent_ == null) || (this == parent_.firstChild_))
    {
      return null;
    }
    return previousSibling_;
  }
  



  public DomNode getNextSibling()
  {
    return nextSibling_;
  }
  



  public DomNode getFirstChild()
  {
    return firstChild_;
  }
  





  public boolean isAncestorOf(DomNode node)
  {
    while (node != null) {
      if (node == this) {
        return true;
      }
      node = node.getParentNode();
    }
    return false;
  }
  





  public boolean isAncestorOfAny(DomNode... nodes)
  {
    for (DomNode node : nodes) {
      if (isAncestorOf(node)) {
        return true;
      }
    }
    return false;
  }
  
  protected void setPreviousSibling(DomNode previous)
  {
    previousSibling_ = previous;
  }
  
  protected void setNextSibling(DomNode next)
  {
    nextSibling_ = next;
  }
  





  public abstract short getNodeType();
  




  public abstract String getNodeName();
  




  public String getNamespaceURI()
  {
    return null;
  }
  



  public String getLocalName()
  {
    return null;
  }
  



  public String getPrefix()
  {
    return null;
  }
  





  public void setPrefix(String prefix) {}
  




  public boolean hasChildNodes()
  {
    return firstChild_ != null;
  }
  



  public DomNodeList<DomNode> getChildNodes()
  {
    return new SiblingDomNodeList(this);
  }
  




  public boolean isSupported(String namespace, String featureName)
  {
    throw new UnsupportedOperationException("DomNode.isSupported is not yet implemented.");
  }
  



  public void normalize()
  {
    for (DomNode child = getFirstChild(); child != null; child = child.getNextSibling()) {
      if ((child instanceof DomText)) {
        boolean removeChildTextNodes = hasFeature(BrowserVersionFeatures.DOM_NORMALIZE_REMOVE_CHILDREN);
        StringBuilder dataBuilder = new StringBuilder();
        DomNode toRemove = child;
        DomText firstText = null;
        
        while (((toRemove instanceof DomText)) && (!(toRemove instanceof DomCDataSection))) {
          DomNode nextChild = toRemove.getNextSibling();
          dataBuilder.append(toRemove.getTextContent());
          if ((removeChildTextNodes) || (firstText != null)) {
            toRemove.remove();
          }
          if (firstText == null) {
            firstText = (DomText)toRemove;
          }
          toRemove = nextChild;
        }
        if (firstText != null) {
          if (removeChildTextNodes) {
            DomText newText = new DomText(getPage(), dataBuilder.toString());
            insertBefore(newText, toRemove);
          }
          else {
            firstText.setData(dataBuilder.toString());
          }
        }
      }
    }
  }
  




  public String getBaseURI()
  {
    throw new UnsupportedOperationException("DomNode.getBaseURI is not yet implemented.");
  }
  



  public short compareDocumentPosition(Node other)
  {
    if (other == this) {
      return 0;
    }
    

    List<Node> myAncestors = getAncestors();
    List<Node> otherAncestors = ((DomNode)other).getAncestors();
    
    int max = Math.min(myAncestors.size(), otherAncestors.size());
    
    int i = 1;
    while ((i < max) && (myAncestors.get(i) == otherAncestors.get(i))) {
      i++;
    }
    
    if ((i != 1) && (i == max)) {
      if (myAncestors.size() == max) {
        return 20;
      }
      return 10;
    }
    
    if (max == 1) {
      if (myAncestors.contains(other)) {
        return 8;
      }
      if (otherAncestors.contains(this)) {
        return 20;
      }
      return 33;
    }
    

    Node myAncestor = (Node)myAncestors.get(i);
    Node otherAncestor = (Node)otherAncestors.get(i);
    Node node = myAncestor;
    while ((node != otherAncestor) && (node != null)) {
      node = node.getPreviousSibling();
    }
    if (node == null) {
      return 4;
    }
    return 2;
  }
  



  protected List<Node> getAncestors()
  {
    List<Node> list = new ArrayList();
    list.add(this);
    
    Node node = getParentNode();
    while (node != null) {
      list.add(0, node);
      node = node.getParentNode();
    }
    return list;
  }
  



  public String getTextContent()
  {
    switch (getNodeType()) {
    case 1: 
    case 2: 
    case 5: 
    case 6: 
    case 11: 
      StringBuilder builder = new StringBuilder();
      for (DomNode child : getChildren()) {
        short childType = child.getNodeType();
        if ((childType != 8) && (childType != 7)) {
          builder.append(child.getTextContent());
        }
      }
      return builder.toString();
    
    case 3: 
    case 4: 
    case 7: 
    case 8: 
      return getNodeValue();
    }
    
    return null;
  }
  




  public void setTextContent(String textContent)
  {
    removeAllChildren();
    if ((textContent != null) && (!textContent.isEmpty())) {
      appendChild(new DomText(getPage(), textContent));
    }
  }
  



  public boolean isSameNode(Node other)
  {
    return other == this;
  }
  




  public String lookupPrefix(String namespaceURI)
  {
    throw new UnsupportedOperationException("DomNode.lookupPrefix is not yet implemented.");
  }
  




  public boolean isDefaultNamespace(String namespaceURI)
  {
    throw new UnsupportedOperationException("DomNode.isDefaultNamespace is not yet implemented.");
  }
  




  public String lookupNamespaceURI(String prefix)
  {
    throw new UnsupportedOperationException("DomNode.lookupNamespaceURI is not yet implemented.");
  }
  




  public boolean isEqualNode(Node arg)
  {
    throw new UnsupportedOperationException("DomNode.isEqualNode is not yet implemented.");
  }
  




  public Object getFeature(String feature, String version)
  {
    throw new UnsupportedOperationException("DomNode.getFeature is not yet implemented.");
  }
  



  public Object getUserData(String key)
  {
    Object value = null;
    if (userData_ != null) {
      value = userData_.get(key);
    }
    return value;
  }
  



  public Object setUserData(String key, Object data, UserDataHandler handler)
  {
    if (userData_ == null) {
      userData_ = new HashMap();
    }
    return userData_.put(key, data);
  }
  



  public boolean hasAttributes()
  {
    return false;
  }
  



  public NamedNodeMap getAttributes()
  {
    return NamedAttrNodeMapImpl.EMPTY_MAP;
  }
  







  protected boolean isTrimmedText()
  {
    return true;
  }
  













  public boolean isDisplayed()
  {
    if (!mayBeDisplayed()) {
      return false;
    }
    
    HtmlPage htmlPage = getHtmlPageOrNull();
    if ((htmlPage != null) && (htmlPage.getEnclosingWindow().getWebClient().getOptions().isCssEnabled()))
    {

      List<Node> ancestors = getAncestors();
      ArrayList<CSSStyleDeclaration> styles = new ArrayList(ancestors.size());
      
      for (Node node : ancestors) {
        Object scriptableObject = ((DomNode)node).getScriptableObject();
        if ((scriptableObject instanceof HTMLElement)) {
          HTMLElement elem = (HTMLElement)scriptableObject;
          CSSStyleDeclaration style = elem.getWindow().getComputedStyle(elem, null);
          if (HtmlElement.DisplayStyle.NONE.value().equals(style.getDisplay())) {
            return false;
          }
          styles.add(style);
        }
      }
      


      for (int i = styles.size() - 1; i >= 0; i--) {
        CSSStyleDeclaration style = (CSSStyleDeclaration)styles.get(i);
        String visibility = style.getStyleAttribute(StyleAttributes.Definition.VISIBILITY);
        if (visibility.length() > 5) {
          if ("visible".equals(visibility)) {
            return true;
          }
          if (("hidden".equals(visibility)) || ("collapse".equals(visibility))) {
            return false;
          }
        }
      }
    }
    return true;
  }
  





  public boolean mayBeDisplayed()
  {
    return true;
  }
  








  public String asText()
  {
    if ((getPage() instanceof XmlPage)) {
      XmlSerializer ser = new XmlSerializer();
      return ser.asText(this);
    }
    
    HtmlSerializer ser = new HtmlSerializer();
    return ser.asText(this);
  }
  




  public String asXml()
  {
    Charset charsetName = null;
    HtmlPage htmlPage = getHtmlPageOrNull();
    if (htmlPage != null) {
      charsetName = htmlPage.getCharset();
    }
    
    StringWriter stringWriter = new StringWriter();
    Object localObject1 = null;Object localObject4 = null; Object localObject3; try { PrintWriter printWriter = new PrintWriter(stringWriter);
      try { if ((charsetName != null) && ((this instanceof HtmlHtml))) {
          printWriter.print("<?xml version=\"1.0\" encoding=\"");
          printWriter.print(charsetName);
          printWriter.print("\"?>\r\n");
        }
        printXml("", printWriter);
        return stringWriter.toString();
      } finally { if (printWriter != null) printWriter.close(); } } finally { if (localObject2 == null) localObject3 = localThrowable; else if (localObject3 != localThrowable) { localObject3.addSuppressed(localThrowable);
      }
    }
  }
  



  protected void printXml(String indent, PrintWriter printWriter)
  {
    printWriter.print(indent);
    printWriter.print(this);
    printWriter.print("\r\n");
    printChildrenAsXml(indent, printWriter);
  }
  





  protected void printChildrenAsXml(String indent, PrintWriter printWriter)
  {
    DomNode child = getFirstChild();
    while (child != null) {
      child.printXml(indent + "  ", printWriter);
      child = child.getNextSibling();
    }
  }
  



  public String getNodeValue()
  {
    return null;
  }
  





  public void setNodeValue(String value) {}
  




  public DomNode cloneNode(boolean deep)
  {
    try
    {
      newnode = (DomNode)clone();
    } catch (CloneNotSupportedException e) {
      DomNode newnode;
      throw new IllegalStateException("Clone not supported for node [" + this + "]");
    }
    DomNode newnode;
    parent_ = null;
    nextSibling_ = null;
    previousSibling_ = null;
    scriptObject_ = null;
    firstChild_ = null;
    attachedToPage_ = false;
    

    if (deep) {
      for (DomNode child = firstChild_; child != null; child = nextSibling_) {
        newnode.appendChild(child.cloneNode(true));
      }
    }
    
    return newnode;
  }
  









  public ScriptableObject getScriptableObject()
  {
    if (scriptObject_ == null) {
      SgmlPage page = getPage();
      if (this == page) {
        StringBuilder msg = new StringBuilder("No script object associated with the Page.");
        
        msg.append(" class: '");
        msg.append(page.getClass().getName());
        msg.append("'");
        try {
          msg.append(" url: '").append(page.getUrl()).append('\'');
          msg.append(" content: ");
          msg.append(page.getWebResponse().getContentAsString());
        }
        catch (Exception e)
        {
          msg.append(" no details: '").append(e).append('\'');
        }
        throw new IllegalStateException(msg.toString());
      }
      scriptObject_ = ((SimpleScriptable)page.getScriptableObject()).makeScriptableFor(this);
    }
    return (ScriptableObject)scriptObject_;
  }
  



  public DomNode appendChild(Node node)
  {
    if (node == this) {
      Context.throwAsScriptRuntimeEx(new Exception("Can not add not to itself " + this));
      return this;
    }
    DomNode domNode = (DomNode)node;
    if (domNode.isAncestorOf(this)) {
      Context.throwAsScriptRuntimeEx(new Exception("Can not add (grand)parent to itself " + this));
    }
    
    if ((domNode instanceof DomDocumentFragment)) {
      DomDocumentFragment fragment = (DomDocumentFragment)domNode;
      for (DomNode child : fragment.getChildren()) {
        appendChild(child);
      }
    }
    else
    {
      if ((domNode != this) && (domNode.getParentNode() != null)) {
        domNode.detach();
      }
      
      basicAppend(domNode);
      
      fireAddition(domNode);
    }
    
    return domNode;
  }
  





  private void basicAppend(DomNode node)
  {
    node.setPage(getPage());
    if (firstChild_ == null) {
      firstChild_ = node;
      firstChild_.previousSibling_ = node;
    }
    else {
      DomNode last = getLastChild();
      nextSibling_ = node;
      previousSibling_ = last;
      nextSibling_ = null;
      firstChild_.previousSibling_ = node;
    }
    parent_ = this;
  }
  



  public Node insertBefore(Node newChild, Node refChild)
  {
    if ((newChild instanceof DomDocumentFragment)) {
      DomDocumentFragment fragment = (DomDocumentFragment)newChild;
      for (DomNode child : fragment.getChildren()) {
        insertBefore(child, refChild);
      }
      
    }
    else if (refChild == null) {
      appendChild(newChild);
    }
    else {
      if (refChild.getParentNode() != this) {
        throw new DOMException((short)8, "Reference node is not a child of this node.");
      }
      ((DomNode)refChild).insertBefore((DomNode)newChild);
    }
    
    return newChild;
  }
  





  public void insertBefore(DomNode newNode)
  {
    if (previousSibling_ == null) {
      throw new IllegalStateException("Previous sibling for " + this + " is null.");
    }
    
    if (newNode == this) {
      return;
    }
    

    if (newNode.getParentNode() != null) {
      newNode.detach();
    }
    
    basicInsertBefore(newNode);
    
    fireAddition(newNode);
  }
  





  private void basicInsertBefore(DomNode node)
  {
    node.setPage(page_);
    if (parent_.firstChild_ == this) {
      parent_.firstChild_ = node;
    }
    else {
      previousSibling_.nextSibling_ = node;
    }
    previousSibling_ = previousSibling_;
    nextSibling_ = this;
    previousSibling_ = node;
    parent_ = parent_;
  }
  
  private void fireAddition(DomNode domNode) {
    boolean wasAlreadyAttached = domNode.isAttachedToPage();
    attachedToPage_ = isAttachedToPage();
    
    if (isAttachedToPage())
    {
      Page page = getPage();
      if ((page != null) && (page.isHtmlPage())) {
        ((HtmlPage)page).notifyNodeAdded(domNode);
      }
      

      if ((!domNode.isBodyParsed()) && (!wasAlreadyAttached)) {
        for (DomNode child : domNode.getDescendants()) {
          attachedToPage_ = true;
          child.onAllChildrenAddedToPage(true);
        }
        domNode.onAllChildrenAddedToPage(true);
      }
    }
    
    if ((this instanceof DomDocumentFragment)) {
      onAddedToDocumentFragment();
    }
    
    fireNodeAdded(this, domNode);
  }
  



  private boolean isBodyParsed()
  {
    return (getStartLineNumber() != -1) && (getEndLineNumber() == -1);
  }
  



  private void setPage(SgmlPage newPage)
  {
    if (page_ == newPage) {
      return;
    }
    
    page_ = newPage;
    for (DomNode node : getChildren()) {
      node.setPage(newPage);
    }
  }
  



  public Node removeChild(Node child)
  {
    if (child.getParentNode() != this) {
      throw new DOMException((short)8, "Node is not a child of this node.");
    }
    ((DomNode)child).remove();
    return child;
  }
  


  public void removeAllChildren()
  {
    while (getFirstChild() != null) {
      getFirstChild().remove();
    }
  }
  



  public void remove()
  {
    detach();
  }
  





  protected void detach()
  {
    DomNode exParent = parent_;
    
    basicRemove();
    
    fireRemoval(exParent);
  }
  


  protected void basicRemove()
  {
    if ((parent_ != null) && (parent_.firstChild_ == this)) {
      parent_.firstChild_ = nextSibling_;
    }
    else if ((previousSibling_ != null) && (previousSibling_.nextSibling_ == this)) {
      previousSibling_.nextSibling_ = nextSibling_;
    }
    if ((nextSibling_ != null) && (nextSibling_.previousSibling_ == this)) {
      nextSibling_.previousSibling_ = previousSibling_;
    }
    if ((parent_ != null) && (this == parent_.getLastChild())) {
      parent_.firstChild_.previousSibling_ = previousSibling_;
    }
    
    nextSibling_ = null;
    previousSibling_ = null;
    parent_ = null;
  }
  
  private void fireRemoval(DomNode exParent) {
    HtmlPage htmlPage = getHtmlPageOrNull();
    if (htmlPage != null)
    {

      parent_ = exParent;
      htmlPage.notifyNodeRemoved(this);
      parent_ = null;
    }
    
    if (exParent != null) {
      fireNodeDeleted(exParent, this);
      
      exParent.fireNodeDeleted(exParent, this);
    }
  }
  



  public Node replaceChild(Node newChild, Node oldChild)
  {
    if (oldChild.getParentNode() != this) {
      throw new DOMException((short)8, "Node is not a child of this node.");
    }
    ((DomNode)oldChild).replace((DomNode)newChild);
    return oldChild;
  }
  




  public void replace(DomNode newNode)
  {
    if (newNode != this) {
      DomNode exParent = parent_;
      DomNode exNextSibling = nextSibling_;
      
      remove();
      
      exParent.insertBefore(newNode, exNextSibling);
    }
  }
  






  void quietlyRemoveAndMoveChildrenTo(DomNode destination)
  {
    if (destination.getPage() != getPage()) {
      throw new RuntimeException("Cannot perform quiet move on nodes from different pages.");
    }
    for (DomNode child : getChildren()) {
      child.basicRemove();
      destination.basicAppend(child);
    }
    basicRemove();
  }
  










  protected void checkChildHierarchy(Node newChild)
    throws DOMException
  {
    Node parentNode = this;
    while (parentNode != null) {
      if (parentNode == newChild) {
        throw new DOMException((short)3, "Child node is already a parent.");
      }
      parentNode = parentNode.getParentNode();
    }
    org.w3c.dom.Document thisDocument = getOwnerDocument();
    org.w3c.dom.Document childDocument = newChild.getOwnerDocument();
    if ((childDocument != thisDocument) && (childDocument != null)) {
      throw new DOMException((short)4, "Child node " + newChild.getNodeName() + 
        " is not in the same Document as this " + getNodeName() + ".");
    }
  }
  





  protected void onAddedToPage()
  {
    if (firstChild_ != null) {
      for (DomNode child : getChildren()) {
        child.onAddedToPage();
      }
    }
  }
  








  protected void onAllChildrenAddedToPage(boolean postponed) {}
  







  protected void onAddedToDocumentFragment()
  {
    if (firstChild_ != null) {
      for (DomNode child : getChildren()) {
        child.onAddedToDocumentFragment();
      }
    }
  }
  


  public final Iterable<DomNode> getChildren()
  {
    new Iterable()
    {
      public Iterator<DomNode> iterator() {
        return new DomNode.ChildIterator(DomNode.this);
      }
    };
  }
  


  protected class ChildIterator
    implements Iterator<DomNode>
  {
    private DomNode nextNode_ = firstChild_;
    private DomNode currentNode_ = null;
    
    protected ChildIterator() {}
    
    public boolean hasNext() {
      return nextNode_ != null;
    }
    

    public DomNode next()
    {
      if (nextNode_ != null) {
        currentNode_ = nextNode_;
        nextNode_ = nextNode_.nextSibling_;
        return currentNode_;
      }
      throw new NoSuchElementException();
    }
    

    public void remove()
    {
      if (currentNode_ == null) {
        throw new IllegalStateException();
      }
      currentNode_.remove();
    }
  }
  





  public final Iterable<DomNode> getDescendants()
  {
    new Iterable()
    {
      public Iterator<DomNode> iterator() {
        return new DomNode.DescendantElementsIterator(DomNode.this, DomNode.class);
      }
    };
  }
  







  public final Iterable<HtmlElement> getHtmlElementDescendants()
  {
    new Iterable()
    {
      public Iterator<HtmlElement> iterator() {
        return new DomNode.DescendantElementsIterator(DomNode.this, HtmlElement.class);
      }
    };
  }
  







  public final Iterable<DomElement> getDomElementDescendants()
  {
    new Iterable()
    {
      public Iterator<DomElement> iterator() {
        return new DomNode.DescendantElementsIterator(DomNode.this, DomElement.class);
      }
    };
  }
  


  protected class DescendantElementsIterator<T extends DomNode>
    implements Iterator<T>
  {
    private DomNode currentNode_;
    

    private DomNode nextNode_;
    
    private final Class<T> type_;
    

    public DescendantElementsIterator()
    {
      type_ = type;
      nextNode_ = getFirstChildElement(DomNode.this);
    }
    

    public boolean hasNext()
    {
      return nextNode_ != null;
    }
    

    public T next()
    {
      return nextNode();
    }
    

    public void remove()
    {
      if (currentNode_ == null) {
        throw new IllegalStateException("Unable to remove current node, because there is no current node.");
      }
      DomNode current = currentNode_;
      while ((nextNode_ != null) && (current.isAncestorOf(nextNode_))) {
        next();
      }
      current.remove();
    }
    

    public T nextNode()
    {
      currentNode_ = nextNode_;
      setNextElement();
      return currentNode_;
    }
    
    private void setNextElement() {
      DomNode next = getFirstChildElement(nextNode_);
      if (next == null) {
        next = getNextDomSibling(nextNode_);
      }
      if (next == null) {
        next = getNextElementUpwards(nextNode_);
      }
      nextNode_ = next;
    }
    
    private DomNode getNextElementUpwards(DomNode startingNode) {
      if (startingNode == DomNode.this) {
        return null;
      }
      DomNode parent = startingNode.getParentNode();
      if ((parent == null) || (parent == DomNode.this)) {
        return null;
      }
      DomNode next = parent.getNextSibling();
      while ((next != null) && (!isAccepted(next))) {
        next = next.getNextSibling();
      }
      if (next == null) {
        return getNextElementUpwards(parent);
      }
      return next;
    }
    
    private DomNode getFirstChildElement(DomNode parent) {
      DomNode node = parent.getFirstChild();
      while ((node != null) && (!isAccepted(node))) {
        node = node.getNextSibling();
      }
      return node;
    }
    




    protected boolean isAccepted(DomNode node)
    {
      return type_.isAssignableFrom(node.getClass());
    }
    
    private DomNode getNextDomSibling(DomNode element) {
      DomNode node = element.getNextSibling();
      while ((node != null) && (!isAccepted(node))) {
        node = node.getNextSibling();
      }
      return node;
    }
  }
  



  public String getReadyState()
  {
    return readyState_;
  }
  



  public void setReadyState(String state)
  {
    readyState_ = state;
  }
  







  private static Map<String, String> parseSelectionNamespaces(String selectionNS)
  {
    Map<String, String> result = new HashMap();
    String[] toks = selectionNS.split("\\s");
    for (String tok : toks) {
      if (tok.startsWith("xmlns=")) {
        result.put("", tok.substring(7, tok.length() - 7));
      }
      else if (tok.startsWith("xmlns:")) {
        String[] prefix = tok.substring(6).split("=");
        result.put(prefix[0], prefix[1].substring(1, prefix[1].length() - 1));
      }
    }
    return result.isEmpty() ? null : result;
  }
  








  public <T> List<T> getByXPath(String xpathExpr)
  {
    PrefixResolver prefixResolver = null;
    if (hasFeature(BrowserVersionFeatures.XPATH_SELECTION_NAMESPACES))
    {



      org.w3c.dom.Document doc = getOwnerDocument();
      if ((doc instanceof XmlPage)) {
        ScriptableObject scriptable = ((XmlPage)doc).getScriptableObject();
        if (ScriptableObject.hasProperty(scriptable, "getProperty")) {
          Object selectionNS = 
            ScriptableObject.callMethod(scriptable, "getProperty", new Object[] { "SelectionNamespaces" });
          if ((selectionNS != null) && (!selectionNS.toString().isEmpty())) {
            final Map<String, String> namespaces = parseSelectionNamespaces(selectionNS.toString());
            if (namespaces != null) {
              prefixResolver = new PrefixResolver()
              {
                public String getBaseIdentifier() {
                  return (String)namespaces.get("");
                }
                
                public String getNamespaceForPrefix(String prefix)
                {
                  return (String)namespaces.get(prefix);
                }
                
                public String getNamespaceForPrefix(String prefix, Node node)
                {
                  throw new UnsupportedOperationException();
                }
                
                public boolean handlesNullPrefixes()
                {
                  return false;
                }
              };
            }
          }
        }
      }
    }
    return XPathUtils.getByXPath(this, xpathExpr, prefixResolver);
  }
  








  public List<?> getByXPath(String xpathExpr, PrefixResolver resolver)
  {
    return XPathUtils.getByXPath(this, xpathExpr, resolver);
  }
  









  public <X> X getFirstByXPath(String xpathExpr)
  {
    return getFirstByXPath(xpathExpr, null);
  }
  











  public <X> X getFirstByXPath(String xpathExpr, PrefixResolver resolver)
  {
    List<?> results = getByXPath(xpathExpr, resolver);
    if (results.isEmpty()) {
      return null;
    }
    return results.get(0);
  }
  










  public String getCanonicalXPath()
  {
    throw new RuntimeException("Method getCanonicalXPath() not implemented for nodes of type " + getNodeType());
  }
  



  protected void notifyIncorrectness(String message)
  {
    WebClient client = getPage().getEnclosingWindow().getWebClient();
    IncorrectnessListener incorrectnessListener = client.getIncorrectnessListener();
    incorrectnessListener.notify(message, this);
  }
  






  public void addDomChangeListener(DomChangeListener listener)
  {
    WebAssert.notNull("listener", listener);
    
    synchronized (listeners_lock_) {
      if (domListeners_ == null) {
        domListeners_ = new LinkedHashSet();
      }
      domListeners_.add(listener);
      domListenersList_ = null;
    }
  }
  






  public void removeDomChangeListener(DomChangeListener listener)
  {
    WebAssert.notNull("listener", listener);
    
    synchronized (listeners_lock_) {
      if (domListeners_ != null) {
        domListeners_.remove(listener);
        domListenersList_ = null;
      }
    }
  }
  








  protected void fireNodeAdded(DomNode parentNode, DomNode addedNode)
  {
    List<DomChangeListener> listeners = safeGetDomListeners();
    if (listeners != null) {
      DomChangeEvent event = new DomChangeEvent(parentNode, addedNode);
      for (DomChangeListener listener : listeners) {
        listener.nodeAdded(event);
      }
    }
    if (parent_ != null) {
      parent_.fireNodeAdded(parentNode, addedNode);
    }
  }
  






  public void addCharacterDataChangeListener(CharacterDataChangeListener listener)
  {
    WebAssert.notNull("listener", listener);
    
    synchronized (listeners_lock_) {
      if (characterDataListeners_ == null) {
        characterDataListeners_ = new LinkedHashSet();
      }
      characterDataListeners_.add(listener);
      characterDataListenersList_ = null;
    }
  }
  






  public void removeCharacterDataChangeListener(CharacterDataChangeListener listener)
  {
    WebAssert.notNull("listener", listener);
    
    synchronized (listeners_lock_) {
      if (characterDataListeners_ != null) {
        characterDataListeners_.remove(listener);
        characterDataListenersList_ = null;
      }
    }
  }
  







  protected void fireCharacterDataChanged(DomCharacterData charcterData, String oldValue)
  {
    List<CharacterDataChangeListener> listeners = safeGetCharacterDataListeners();
    if (listeners != null) {
      CharacterDataChangeEvent event = new CharacterDataChangeEvent(charcterData, oldValue);
      for (CharacterDataChangeListener listener : listeners) {
        listener.characterDataChanged(event);
      }
    }
    if (parent_ != null) {
      parent_.fireCharacterDataChanged(charcterData, oldValue);
    }
  }
  








  protected void fireNodeDeleted(DomNode parentNode, DomNode deletedNode)
  {
    List<DomChangeListener> listeners = safeGetDomListeners();
    if (listeners != null) {
      DomChangeEvent event = new DomChangeEvent(parentNode, deletedNode);
      for (DomChangeListener listener : listeners) {
        listener.nodeDeleted(event);
      }
    }
    if (parent_ != null) {
      parent_.fireNodeDeleted(parentNode, deletedNode);
    }
  }
  
  private List<DomChangeListener> safeGetDomListeners() {
    synchronized (listeners_lock_) {
      if (domListeners_ == null) {
        return null;
      }
      if (domListenersList_ == null) {
        domListenersList_ = new ArrayList(domListeners_);
      }
      return domListenersList_;
    }
  }
  
  private List<CharacterDataChangeListener> safeGetCharacterDataListeners() {
    synchronized (listeners_lock_) {
      if (characterDataListeners_ == null) {
        return null;
      }
      if (characterDataListenersList_ == null) {
        characterDataListenersList_ = new ArrayList(characterDataListeners_);
      }
      return characterDataListenersList_;
    }
  }
  




  public DomNodeList<DomNode> querySelectorAll(String selectors)
  {
    try
    {
      WebClient webClient = getPage().getWebClient();
      CSSOMParser parser = new CSSOMParser(new SACParserCSS3());
      CheckErrorHandler errorHandler = new CheckErrorHandler();
      parser.setErrorHandler(errorHandler);
      
      SelectorList selectorList = parser.parseSelectors(new InputSource(new StringReader(selectors)));
      
      if (errorHandler.errorDetected()) {
        throw new CSSException("Invalid selectors: " + selectors);
      }
      
      List<DomNode> elements = new ArrayList();
      if (selectorList != null) {
        BrowserVersion browserVersion = webClient.getBrowserVersion();
        int documentMode = 9;
        if (browserVersion.hasFeature(BrowserVersionFeatures.QUERYSELECTORALL_NOT_IN_QUIRKS)) {
          Object sobj = getPage().getScriptableObject();
          if ((sobj instanceof HTMLDocument)) {
            documentMode = ((HTMLDocument)sobj).getDocumentMode();
          }
        }
        CSSStyleSheet.validateSelectors(selectorList, documentMode, this);
        
        for (DomElement child : getDomElementDescendants()) {
          for (int i = 0; i < selectorList.getLength(); i++) {
            Selector selector = selectorList.item(i);
            if (CSSStyleSheet.selects(browserVersion, selector, child, null, true)) {
              elements.add(child);
              break;
            }
          }
        }
      }
      return new StaticDomNodeList(elements);
    }
    catch (IOException e) {
      throw new CSSException("Error parsing CSS selectors from '" + selectors + "': " + e.getMessage());
    }
  }
  






  public <N extends DomNode> N querySelector(String selectors)
  {
    DomNodeList<DomNode> list = querySelectorAll(selectors);
    if (!list.isEmpty()) {
      return (DomNode)list.get(0);
    }
    return null;
  }
  





  public boolean isAttachedToPage()
  {
    return attachedToPage_;
  }
  









  public void processImportNode(com.gargoylesoftware.htmlunit.javascript.host.dom.Document doc) {}
  








  public boolean hasFeature(BrowserVersionFeatures feature)
  {
    return getPage().getWebClient().getBrowserVersion().hasFeature(feature);
  }
  
  private static final class CheckErrorHandler implements ErrorHandler {
    private boolean errorDetected_;
    
    protected CheckErrorHandler() {
      errorDetected_ = false;
    }
    
    protected boolean errorDetected() {
      return errorDetected_;
    }
    
    public void warning(CSSParseException exception)
      throws CSSException
    {}
    
    public void fatalError(CSSParseException exception)
      throws CSSException
    {
      errorDetected_ = true;
    }
    
    public void error(CSSParseException exception) throws CSSException
    {
      errorDetected_ = true;
    }
  }
  





  public boolean handles(Event event)
  {
    return true;
  }
}
