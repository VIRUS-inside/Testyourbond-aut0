package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.css.SelectorSpecificity;
import com.gargoylesoftware.htmlunit.css.StyleElement;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventTarget;
import com.gargoylesoftware.htmlunit.javascript.host.event.MouseEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.PointerEvent;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.SortedSet;
import java.util.TreeSet;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ContextAction;
import net.sourceforge.htmlunit.corejs.javascript.ContextFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.TypeInfo;

























public class DomElement
  extends DomNamespaceNode
  implements Element
{
  private static final Log LOG = LogFactory.getLog(DomElement.class);
  

  public static final String ATTRIBUTE_NOT_DEFINED = new String("");
  

  public static final String ATTRIBUTE_VALUE_EMPTY = new String();
  

  private NamedAttrNodeMapImpl attributes_ = new NamedAttrNodeMapImpl(this, isAttributeCaseSensitive());
  

  private Map<String, String> namespaces_ = new HashMap();
  

  private String styleString_ = new String();
  




  private Map<String, StyleElement> styleMap_;
  



  private boolean mouseOver_;
  




  public DomElement(String namespaceURI, String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(namespaceURI, qualifiedName, page);
    if ((attributes != null) && (!attributes.isEmpty())) {
      attributes_ = new NamedAttrNodeMapImpl(this, isAttributeCaseSensitive(), attributes);
      for (DomAttr entry : attributes_.values()) {
        entry.setParentNode(this);
        String attrNamespaceURI = entry.getNamespaceURI();
        if (attrNamespaceURI != null) {
          namespaces_.put(attrNamespaceURI, entry.getPrefix());
        }
      }
    }
  }
  



  public String getNodeName()
  {
    return getQualifiedName();
  }
  



  public final short getNodeType()
  {
    return 1;
  }
  




  public final String getTagName()
  {
    return getNodeName();
  }
  



  public final boolean hasAttributes()
  {
    return !attributes_.isEmpty();
  }
  







  public boolean hasAttribute(String attributeName)
  {
    return attributes_.containsKey(attributeName);
  }
  









  public void replaceStyleAttribute(String name, String value, String priority)
  {
    if (org.apache.commons.lang3.StringUtils.isBlank(value)) {
      removeStyleAttribute(name);
      return;
    }
    
    Map<String, StyleElement> styleMap = getStyleMap();
    StyleElement old = (StyleElement)styleMap.get(name);
    StyleElement element;
    StyleElement element; if (old == null) {
      element = new StyleElement(name, value, priority, SelectorSpecificity.FROM_STYLE_ATTRIBUTE);
    }
    else {
      element = new StyleElement(name, value, priority, 
        SelectorSpecificity.FROM_STYLE_ATTRIBUTE, old.getIndex());
    }
    styleMap.put(name, element);
    writeStyleToElement(styleMap);
  }
  






  public String removeStyleAttribute(String name)
  {
    Map<String, StyleElement> styleMap = getStyleMap();
    StyleElement value = (StyleElement)styleMap.get(name);
    if (value == null) {
      return "";
    }
    styleMap.remove(name);
    writeStyleToElement(styleMap);
    return value.getValue();
  }
  







  public StyleElement getStyleElement(String name)
  {
    Map<String, StyleElement> map = getStyleMap();
    if (map != null) {
      return (StyleElement)map.get(name);
    }
    return null;
  }
  








  public StyleElement getStyleElementCaseInSensitive(String name)
  {
    Map<String, StyleElement> map = getStyleMap();
    for (Map.Entry<String, StyleElement> entry : map.entrySet()) {
      if (((String)entry.getKey()).equalsIgnoreCase(name)) {
        return (StyleElement)entry.getValue();
      }
    }
    return null;
  }
  







  public Map<String, StyleElement> getStyleMap()
  {
    String styleAttribute = getAttribute("style");
    if (styleString_ == styleAttribute) {
      return styleMap_;
    }
    
    Map<String, StyleElement> styleMap = new LinkedHashMap();
    if ((ATTRIBUTE_NOT_DEFINED == styleAttribute) || (ATTRIBUTE_VALUE_EMPTY == styleAttribute)) {
      styleMap_ = styleMap;
      styleString_ = styleAttribute;
      return styleMap_;
    }
    
    for (String token : org.apache.commons.lang3.StringUtils.split(styleAttribute, ';')) {
      int index = token.indexOf(":");
      if (index != -1) {
        String key = token.substring(0, index).trim().toLowerCase(Locale.ROOT);
        String value = token.substring(index + 1).trim();
        String priority = "";
        if (org.apache.commons.lang3.StringUtils.endsWithIgnoreCase(value, "!important")) {
          priority = "important";
          value = value.substring(0, value.length() - 10);
          value = value.trim();
        }
        StyleElement element = new StyleElement(key, value, priority, 
          SelectorSpecificity.FROM_STYLE_ATTRIBUTE);
        styleMap.put(key, element);
      }
    }
    
    styleMap_ = styleMap;
    styleString_ = styleAttribute;
    return styleMap_;
  }
  




  protected void printOpeningTagContentAsXml(PrintWriter printWriter)
  {
    printWriter.print(getTagName());
    for (Map.Entry<String, DomAttr> entry : attributes_.entrySet()) {
      printWriter.print(" ");
      printWriter.print((String)entry.getKey());
      printWriter.print("=\"");
      printWriter.print(com.gargoylesoftware.htmlunit.util.StringUtils.escapeXmlAttributeValue(((DomAttr)entry.getValue()).getNodeValue()));
      printWriter.print("\"");
    }
  }
  






  protected void printXml(String indent, PrintWriter printWriter)
  {
    boolean hasChildren = getFirstChild() != null;
    printWriter.print(indent + "<");
    printOpeningTagContentAsXml(printWriter);
    
    if ((hasChildren) || (isEmptyXmlTagExpanded())) {
      printWriter.print(">\r\n");
      printChildrenAsXml(indent, printWriter);
      printWriter.print(indent);
      printWriter.print("</");
      printWriter.print(getTagName());
      printWriter.print(">\r\n");
    }
    else {
      printWriter.print("/>\r\n");
    }
  }
  




  protected boolean isEmptyXmlTagExpanded()
  {
    return false;
  }
  


  String getQualifiedName(String namespaceURI, String localName)
  {
    String qualifiedName;
    

    String qualifiedName;
    

    if (namespaceURI == null) {
      qualifiedName = localName;
    }
    else {
      String prefix = (String)namespaces_.get(namespaceURI);
      String qualifiedName; if (prefix == null) {
        qualifiedName = null;
      }
      else {
        qualifiedName = prefix + ':' + localName;
      }
    }
    return qualifiedName;
  }
  









  public String getAttribute(String attributeName)
  {
    DomAttr attr = attributes_.get(attributeName);
    if (attr != null) {
      return attr.getNodeValue();
    }
    return ATTRIBUTE_NOT_DEFINED;
  }
  




  public void removeAttribute(String attributeName)
  {
    attributes_.remove(attributeName);
  }
  





  public final void removeAttributeNS(String namespaceURI, String localName)
  {
    String qualifiedName = getQualifiedName(namespaceURI, localName);
    if (qualifiedName != null) {
      removeAttribute(qualifiedName);
    }
  }
  




  public final Attr removeAttributeNode(Attr attribute)
  {
    throw new UnsupportedOperationException("DomElement.removeAttributeNode is not yet implemented.");
  }
  








  public final boolean hasAttributeNS(String namespaceURI, String localName)
  {
    String qualifiedName = getQualifiedName(namespaceURI, localName);
    if (qualifiedName != null) {
      return attributes_.get(qualifiedName) != null;
    }
    return false;
  }
  



  public final Map<String, DomAttr> getAttributesMap()
  {
    return attributes_;
  }
  



  public NamedNodeMap getAttributes()
  {
    return attributes_;
  }
  






  public void setAttribute(String attributeName, String attributeValue)
  {
    setAttributeNS(null, attributeName, attributeValue, true);
  }
  








  public void setAttributeNS(String namespaceURI, String qualifiedName, String attributeValue)
  {
    setAttributeNS(namespaceURI, qualifiedName, attributeValue, true);
  }
  








  protected void setAttributeNS(String namespaceURI, String qualifiedName, String attributeValue, boolean notifyAttributeChangeListeners)
  {
    String value = attributeValue;
    DomAttr newAttr = new DomAttr(getPage(), namespaceURI, qualifiedName, value, true);
    newAttr.setParentNode(this);
    attributes_.put(qualifiedName, newAttr);
    
    if (namespaceURI != null) {
      namespaces_.put(namespaceURI, newAttr.getPrefix());
    }
  }
  



  protected boolean isAttributeCaseSensitive()
  {
    return true;
  }
  










  public final String getAttributeNS(String namespaceURI, String localName)
  {
    String qualifiedName = getQualifiedName(namespaceURI, localName);
    if (qualifiedName != null) {
      return getAttribute(qualifiedName);
    }
    return ATTRIBUTE_NOT_DEFINED;
  }
  



  public DomAttr getAttributeNode(String name)
  {
    return attributes_.get(name);
  }
  



  public DomAttr getAttributeNodeNS(String namespaceURI, String localName)
  {
    String qualifiedName = getQualifiedName(namespaceURI, localName);
    if (qualifiedName != null) {
      return attributes_.get(qualifiedName);
    }
    return null;
  }
  




  public void writeStyleToElement(Map<String, StyleElement> styleMap)
  {
    StringBuilder builder = new StringBuilder();
    SortedSet<StyleElement> sortedValues = new TreeSet(styleMap.values());
    for (StyleElement e : sortedValues) {
      if (builder.length() != 0) {
        builder.append(" ");
      }
      builder.append(e.getName());
      builder.append(": ");
      builder.append(e.getValue());
      
      String prio = e.getPriority();
      if (org.apache.commons.lang3.StringUtils.isNotBlank(prio)) {
        builder.append(" !");
        builder.append(prio);
      }
      builder.append(";");
    }
    String value = builder.toString();
    setAttribute("style", value);
  }
  



  public DomNodeList<HtmlElement> getElementsByTagName(String tagName)
  {
    return getElementsByTagNameImpl(tagName);
  }
  




  <E extends HtmlElement> DomNodeList<E> getElementsByTagNameImpl(final String tagName)
  {
    new AbstractDomNodeList(this)
    {
      protected List<E> provideElements()
      {
        List<E> res = new LinkedList();
        for (HtmlElement elem : getDomNode().getHtmlElementDescendants()) {
          if (elem.getLocalName().equalsIgnoreCase(tagName)) {
            res.add(elem);
          }
        }
        return res;
      }
    };
  }
  




  public DomNodeList<HtmlElement> getElementsByTagNameNS(String namespace, String localName)
  {
    throw new UnsupportedOperationException("DomElement.getElementsByTagNameNS is not yet implemented.");
  }
  




  public TypeInfo getSchemaTypeInfo()
  {
    throw new UnsupportedOperationException("DomElement.getSchemaTypeInfo is not yet implemented.");
  }
  




  public void setIdAttribute(String name, boolean isId)
  {
    throw new UnsupportedOperationException("DomElement.setIdAttribute is not yet implemented.");
  }
  




  public void setIdAttributeNS(String namespaceURI, String localName, boolean isId)
  {
    throw new UnsupportedOperationException("DomElement.setIdAttributeNS is not yet implemented.");
  }
  



  public Attr setAttributeNode(Attr attribute)
  {
    attributes_.setNamedItem(attribute);
    return null;
  }
  




  public Attr setAttributeNodeNS(Attr attribute)
  {
    throw new UnsupportedOperationException("DomElement.setAttributeNodeNS is not yet implemented.");
  }
  




  public final void setIdAttributeNode(Attr idAttr, boolean isId)
  {
    throw new UnsupportedOperationException("DomElement.setIdAttributeNode is not yet implemented.");
  }
  



  public DomNode cloneNode(boolean deep)
  {
    DomElement clone = (DomElement)super.cloneNode(deep);
    attributes_ = new NamedAttrNodeMapImpl(clone, isAttributeCaseSensitive());
    attributes_.putAll(attributes_);
    return clone;
  }
  


  public final String getId()
  {
    return getAttribute("id");
  }
  



  public DomElement getFirstElementChild()
  {
    Iterator<DomElement> i = getChildElements().iterator();
    if (i.hasNext()) {
      return (DomElement)i.next();
    }
    return null;
  }
  



  public DomElement getLastElementChild()
  {
    DomElement lastChild = null;
    Iterator<DomElement> i = getChildElements().iterator();
    while (i.hasNext()) {
      lastChild = (DomElement)i.next();
    }
    return lastChild;
  }
  





  public DomElement getPreviousElementSibling()
  {
    DomNode node = getPreviousSibling();
    while ((node != null) && (!(node instanceof DomElement))) {
      node = node.getPreviousSibling();
    }
    return (DomElement)node;
  }
  





  public DomElement getNextElementSibling()
  {
    DomNode node = getNextSibling();
    while ((node != null) && (!(node instanceof DomElement))) {
      node = node.getNextSibling();
    }
    return (DomElement)node;
  }
  





  public int getChildElementCount()
  {
    int counter = 0;
    Iterator<DomElement> i = getChildElements().iterator();
    while (i.hasNext()) {
      i.next();
      counter++;
    }
    return counter;
  }
  


  public final Iterable<DomElement> getChildElements()
  {
    return new ChildElementsIterable(this);
  }
  


  private static class ChildElementsIterable
    implements Iterable<DomElement>
  {
    private final Iterator<DomElement> iterator_;
    

    protected ChildElementsIterable(DomNode domNode)
    {
      iterator_ = new DomElement.ChildElementsIterator(domNode);
    }
    
    public Iterator<DomElement> iterator()
    {
      return iterator_;
    }
  }
  


  protected static class ChildElementsIterator
    implements Iterator<DomElement>
  {
    private DomElement nextElement_;
    


    protected ChildElementsIterator(DomNode domNode)
    {
      DomNode child = domNode.getFirstChild();
      if (child != null) {
        if ((child instanceof DomElement)) {
          nextElement_ = ((DomElement)child);
        }
        else {
          setNextElement(child);
        }
      }
    }
    

    public boolean hasNext()
    {
      return nextElement_ != null;
    }
    

    public DomElement next()
    {
      return nextElement();
    }
    

    public void remove()
    {
      if (nextElement_ == null) {
        throw new IllegalStateException();
      }
      DomNode sibling = nextElement_.getPreviousSibling();
      if (sibling != null) {
        sibling.remove();
      }
    }
    
    public DomElement nextElement()
    {
      if (nextElement_ != null) {
        DomElement result = nextElement_;
        setNextElement(nextElement_);
        return result;
      }
      throw new NoSuchElementException();
    }
    
    private void setNextElement(DomNode node) {
      DomNode next = node.getNextSibling();
      while ((next != null) && (!(next instanceof HtmlElement))) {
        next = next.getNextSibling();
      }
      nextElement_ = ((DomElement)next);
    }
  }
  




  public String toString()
  {
    StringWriter writer = new StringWriter();
    PrintWriter printWriter = new PrintWriter(writer);
    
    printWriter.print(getClass().getSimpleName());
    printWriter.print("[<");
    printOpeningTagContentAsXml(printWriter);
    printWriter.print(">]");
    printWriter.flush();
    return writer.toString();
  }
  









  public <P extends Page> P click()
    throws IOException
  {
    return click(false, false, false);
  }
  













  public <P extends Page> P click(boolean shiftKey, boolean ctrlKey, boolean altKey)
    throws IOException
  {
    return click(shiftKey, ctrlKey, altKey, true);
  }
  
















  protected <P extends Page> P click(boolean shiftKey, boolean ctrlKey, boolean altKey, boolean triggerMouseEvents)
    throws IOException
  {
    SgmlPage page = getPage();
    page.getWebClient().setCurrentWindow(page.getEnclosingWindow());
    
    if ((!isDisplayed()) || (!(page instanceof HtmlPage)) || (
      ((this instanceof DisabledElement)) && (((DisabledElement)this).isDisabled()))) {
      return page;
    }
    
    synchronized (page) {
      if (triggerMouseEvents) {
        mouseDown(shiftKey, ctrlKey, altKey, 0);
      }
      

      DomElement elementToFocus = null;
      if (((this instanceof SubmittableElement)) || ((this instanceof HtmlAnchor)) || (
        ((this instanceof HtmlElement)) && (((HtmlElement)this).getTabIndex() != null))) {
        elementToFocus = this;
      }
      else if ((this instanceof HtmlOption)) {
        elementToFocus = ((HtmlOption)this).getEnclosingSelect();
      }
      
      ((HtmlPage)page).setFocusedElement(elementToFocus);
      
      if (triggerMouseEvents) {
        mouseUp(shiftKey, ctrlKey, altKey, 0);
      }
      Event event;
      Event event;
      if (getPage().getWebClient().getBrowserVersion().hasFeature(BrowserVersionFeatures.EVENT_ONCLICK_USES_POINTEREVENT)) {
        event = new PointerEvent(getEventTargetElement(), "click", shiftKey, 
          ctrlKey, altKey, 0);
      }
      else {
        event = new MouseEvent(getEventTargetElement(), "click", shiftKey, 
          ctrlKey, altKey, 0);
      }
      return click(event, false);
    }
  }
  




  protected DomNode getEventTargetElement()
  {
    return this;
  }
  













  public <P extends Page> P click(Event event, boolean ignoreVisibility)
    throws IOException
  {
    SgmlPage page = getPage();
    
    if (((!ignoreVisibility) && (!isDisplayed())) || (
      ((this instanceof DisabledElement)) && (((DisabledElement)this).isDisabled()))) {
      return page;
    }
    


    Page contentPage = page.getEnclosingWindow().getEnclosedPage();
    
    boolean stateUpdated = false;
    boolean changed = false;
    if (isStateUpdateFirst()) {
      changed = doClickStateUpdate(event.getShiftKey(), event.getCtrlKey());
      stateUpdated = true;
    }
    
    JavaScriptEngine jsEngine = page.getWebClient().getJavaScriptEngine();
    jsEngine.holdPosponedActions();
    try {
      ScriptResult scriptResult = doClickFireClickEvent(event);
      boolean eventIsAborted = event.isAborted(scriptResult);
      
      boolean pageAlreadyChanged = contentPage != page.getEnclosingWindow().getEnclosedPage();
      if ((!pageAlreadyChanged) && (!stateUpdated) && (!eventIsAborted)) {
        changed = doClickStateUpdate(event.getShiftKey(), event.getCtrlKey());
      }
    }
    finally {
      jsEngine.processPostponedActions();
    }
    
    if (changed) {
      doClickFireChangeEvent();
    }
    
    return getPage().getWebClient().getCurrentWindow().getEnclosedPage();
  }
  










  protected boolean doClickStateUpdate(boolean shiftKey, boolean ctrlKey)
    throws IOException
  {
    if (propagateClickStateUpdateToParent())
    {


      DomNode parent = getParentNode();
      if ((parent instanceof DomElement)) {
        return ((DomElement)parent).doClickStateUpdate(false, false);
      }
    }
    
    return false;
  }
  






  protected boolean propagateClickStateUpdateToParent()
  {
    return true;
  }
  





  protected void doClickFireChangeEvent() {}
  




  protected ScriptResult doClickFireClickEvent(Event event)
  {
    return fireEvent(event);
  }
  









  public <P extends Page> P dblClick()
    throws IOException
  {
    return dblClick(false, false, false);
  }
  














  public <P extends Page> P dblClick(boolean shiftKey, boolean ctrlKey, boolean altKey)
    throws IOException
  {
    if (((this instanceof DisabledElement)) && (((DisabledElement)this).isDisabled())) {
      return getPage();
    }
    

    P clickPage = click(shiftKey, ctrlKey, altKey);
    if (clickPage != getPage()) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("dblClick() is ignored, as click() loaded a different page.");
      }
      return clickPage;
    }
    

    clickPage = click(shiftKey, ctrlKey, altKey);
    if (clickPage != getPage()) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("dblClick() is ignored, as click() loaded a different page.");
      }
      return clickPage;
    }
    Event event;
    Event event;
    if (getPage().getWebClient().getBrowserVersion().hasFeature(BrowserVersionFeatures.EVENT_ONCLICK_USES_POINTEREVENT)) {
      event = new PointerEvent(this, "dblclick", shiftKey, ctrlKey, altKey, 
        0);
    }
    else {
      event = new MouseEvent(this, "dblclick", shiftKey, ctrlKey, altKey, 
        0);
    }
    ScriptResult scriptResult = fireEvent(event);
    if (scriptResult == null) {
      return clickPage;
    }
    return scriptResult.getNewPage();
  }
  






  public Page mouseOver()
  {
    return mouseOver(false, false, false, 0);
  }
  











  public Page mouseOver(boolean shiftKey, boolean ctrlKey, boolean altKey, int button)
  {
    return doMouseEvent("mouseover", shiftKey, ctrlKey, altKey, button);
  }
  






  public Page mouseMove()
  {
    return mouseMove(false, false, false, 0);
  }
  











  public Page mouseMove(boolean shiftKey, boolean ctrlKey, boolean altKey, int button)
  {
    return doMouseEvent("mousemove", shiftKey, ctrlKey, altKey, button);
  }
  






  public Page mouseOut()
  {
    return mouseOut(false, false, false, 0);
  }
  











  public Page mouseOut(boolean shiftKey, boolean ctrlKey, boolean altKey, int button)
  {
    return doMouseEvent("mouseout", shiftKey, ctrlKey, altKey, button);
  }
  






  public Page mouseDown()
  {
    return mouseDown(false, false, false, 0);
  }
  











  public Page mouseDown(boolean shiftKey, boolean ctrlKey, boolean altKey, int button)
  {
    return doMouseEvent("mousedown", shiftKey, ctrlKey, altKey, button);
  }
  






  public Page mouseUp()
  {
    return mouseUp(false, false, false, 0);
  }
  











  public Page mouseUp(boolean shiftKey, boolean ctrlKey, boolean altKey, int button)
  {
    return doMouseEvent("mouseup", shiftKey, ctrlKey, altKey, button);
  }
  






  public Page rightClick()
  {
    return rightClick(false, false, false);
  }
  









  public Page rightClick(boolean shiftKey, boolean ctrlKey, boolean altKey)
  {
    Page mouseDownPage = mouseDown(shiftKey, ctrlKey, altKey, 2);
    if (mouseDownPage != getPage()) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("rightClick() is incomplete, as mouseDown() loaded a different page.");
      }
      return mouseDownPage;
    }
    Page mouseUpPage = mouseUp(shiftKey, ctrlKey, altKey, 2);
    if (mouseUpPage != getPage()) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("rightClick() is incomplete, as mouseUp() loaded a different page.");
      }
      return mouseUpPage;
    }
    return doMouseEvent("contextmenu", shiftKey, ctrlKey, altKey, 2);
  }
  












  private Page doMouseEvent(String eventType, boolean shiftKey, boolean ctrlKey, boolean altKey, int button)
  {
    SgmlPage page = getPage();
    Event event;
    Event event;
    if (("contextmenu".equals(eventType)) && 
      (getPage().getWebClient().getBrowserVersion().hasFeature(BrowserVersionFeatures.EVENT_ONCLICK_USES_POINTEREVENT))) {
      event = new PointerEvent(this, eventType, shiftKey, ctrlKey, altKey, button);
    }
    else {
      event = new MouseEvent(this, eventType, shiftKey, ctrlKey, altKey, button);
    }
    ScriptResult scriptResult = fireEvent(event);
    Page currentPage;
    Page currentPage; if (scriptResult == null) {
      currentPage = page;
    }
    else {
      currentPage = scriptResult.getNewPage();
    }
    
    boolean mouseOver = !"mouseout".equals(eventType);
    if (mouseOver_ != mouseOver) {
      mouseOver_ = mouseOver;
      
      SimpleScriptable scriptable = (SimpleScriptable)getScriptableObject();
      scriptable.getWindow().clearComputedStyles();
    }
    
    return currentPage;
  }
  






  public ScriptResult fireEvent(String eventType)
  {
    return fireEvent(new Event(this, eventType));
  }
  






  public ScriptResult fireEvent(final Event event)
  {
    WebClient client = getPage().getWebClient();
    if (!client.getOptions().isJavaScriptEnabled()) {
      return null;
    }
    
    if (!handles(event)) {
      return null;
    }
    
    if (LOG.isDebugEnabled()) {
      LOG.debug("Firing " + event);
    }
    final EventTarget jsElt = (EventTarget)getScriptableObject();
    ContextAction action = new ContextAction()
    {
      public Object run(Context cx) {
        return jsElt.fireEvent(event);
      }
      
    };
    ContextFactory cf = client.getJavaScriptEngine().getContextFactory();
    ScriptResult result = (ScriptResult)cf.call(action);
    if (event.isAborted(result)) {
      preventDefault();
    }
    return result;
  }
  





  protected void preventDefault() {}
  





  public void focus()
  {
    HtmlPage page = (HtmlPage)getPage();
    page.setFocusedElement(this);
    HTMLElement jsElt = (HTMLElement)getScriptableObject();
    jsElt.setActive();
  }
  


  public void blur()
  {
    ((HtmlPage)getPage()).setFocusedElement(null);
  }
  






  public void removeFocus() {}
  






  protected boolean isStateUpdateFirst()
  {
    return false;
  }
  



  public boolean isMouseOver()
  {
    if (mouseOver_) {
      return true;
    }
    for (DomElement child : getChildElements()) {
      if (child.isMouseOver()) {
        return true;
      }
    }
    return false;
  }
}
