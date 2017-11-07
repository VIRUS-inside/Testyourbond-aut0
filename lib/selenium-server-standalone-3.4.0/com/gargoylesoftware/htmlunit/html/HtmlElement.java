package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Document;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventHandler;
import com.gargoylesoftware.htmlunit.javascript.host.event.KeyboardEvent;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.sourceforge.htmlunit.corejs.javascript.BaseFunction;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;





































public abstract class HtmlElement
  extends DomElement
{
  public static enum DisplayStyle
  {
    EMPTY(
      ""), 
    NONE(
      "none"), 
    BLOCK(
      "block"), 
    INLINE(
      "inline"), 
    INLINE_BLOCK(
      "inline-block"), 
    LIST_ITEM(
      "list-item"), 
    TABLE(
      "table"), 
    TABLE_CELL(
      "table-cell"), 
    TABLE_COLUMN(
      "table-column"), 
    TABLE_COLUMN_GROUP(
      "table-column-group"), 
    TABLE_ROW(
      "table-row"), 
    TABLE_ROW_GROUP(
      "table-row-group"), 
    TABLE_HEADER_GROUP(
      "table-header-group"), 
    TABLE_FOOTER_GROUP(
      "table-footer-group"), 
    TABLE_CAPTION(
      "table-caption"), 
    RUBY(
      "ruby"), 
    RUBY_TEXT(
      "ruby-text");
    
    private final String value_;
    
    private DisplayStyle(String value) { value_ = value; }
    




    public String value()
    {
      return value_;
    }
  }
  
  private static final Log LOG = LogFactory.getLog(HtmlElement.class);
  






  public static final Short TAB_INDEX_OUT_OF_BOUNDS = new Short((short)Short.MIN_VALUE);
  


  private final Collection<HtmlAttributeChangeListener> attributeListeners_;
  

  private HtmlForm owningForm_;
  

  private boolean shiftPressed_;
  

  private boolean ctrlPressed_;
  

  private boolean altPressed_;
  


  protected HtmlElement(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super("http://www.w3.org/1999/xhtml", qualifiedName, page, attributes);
    attributeListeners_ = new LinkedHashSet();
  }
  






  public void setAttributeNS(String namespaceURI, String qualifiedName, String attributeValue, boolean notifyAttributeChangeListeners)
  {
    if (getHtmlPageOrNull() == null) {
      super.setAttributeNS(namespaceURI, qualifiedName, attributeValue, notifyAttributeChangeListeners);
      return;
    }
    
    String oldAttributeValue = getAttribute(qualifiedName);
    HtmlPage htmlPage = (HtmlPage)getPage();
    boolean mappedElement = (isAttachedToPage()) && 
      (HtmlPage.isMappedElement(htmlPage, qualifiedName));
    if (mappedElement)
    {
      htmlPage.removeMappedElement(this);
    }
    HtmlAttributeChangeEvent event;
    HtmlAttributeChangeEvent event;
    if (oldAttributeValue == ATTRIBUTE_NOT_DEFINED) {
      event = new HtmlAttributeChangeEvent(this, qualifiedName, attributeValue);
    }
    else {
      event = new HtmlAttributeChangeEvent(this, qualifiedName, oldAttributeValue);
    }
    
    super.setAttributeNS(namespaceURI, qualifiedName, attributeValue, notifyAttributeChangeListeners);
    
    if (notifyAttributeChangeListeners) {
      notifyAttributeChangeListeners(event, this, oldAttributeValue);
    }
    
    fireAttributeChangeImpl(event, htmlPage, mappedElement, qualifiedName, attributeValue, oldAttributeValue);
  }
  






  protected static void notifyAttributeChangeListeners(HtmlAttributeChangeEvent event, HtmlElement element, String oldAttributeValue)
  {
    Collection<HtmlAttributeChangeListener> listeners = attributeListeners_;
    if (oldAttributeValue == ATTRIBUTE_NOT_DEFINED) {
      synchronized (listeners) {
        for (HtmlAttributeChangeListener listener : listeners) {
          listener.attributeAdded(event);
        }
      }
    }
    
    synchronized (listeners) {
      for (HtmlAttributeChangeListener listener : listeners) {
        listener.attributeReplaced(event);
      }
    }
    
    DomNode parentNode = element.getParentNode();
    if ((parentNode instanceof HtmlElement)) {
      notifyAttributeChangeListeners(event, (HtmlElement)parentNode, oldAttributeValue);
    }
  }
  

  private void fireAttributeChangeImpl(HtmlAttributeChangeEvent event, HtmlPage htmlPage, boolean mappedElement, String qualifiedName, String attributeValue, String oldAttributeValue)
  {
    if (mappedElement) {
      htmlPage.addMappedElement(this);
    }
    
    if (oldAttributeValue == ATTRIBUTE_NOT_DEFINED) {
      fireHtmlAttributeAdded(event);
      htmlPage.fireHtmlAttributeAdded(event);
    }
    else {
      fireHtmlAttributeReplaced(event);
      htmlPage.fireHtmlAttributeReplaced(event);
    }
  }
  









  public Attr setAttributeNode(Attr attribute)
  {
    String qualifiedName = attribute.getName();
    String oldAttributeValue = getAttribute(qualifiedName);
    HtmlPage htmlPage = (HtmlPage)getPage();
    boolean mappedElement = (isAttachedToPage()) && 
      (HtmlPage.isMappedElement(htmlPage, qualifiedName));
    if (mappedElement)
    {
      htmlPage.removeMappedElement(this);
    }
    HtmlAttributeChangeEvent event;
    HtmlAttributeChangeEvent event;
    if (oldAttributeValue == ATTRIBUTE_NOT_DEFINED) {
      event = new HtmlAttributeChangeEvent(this, qualifiedName, attribute.getValue());
    }
    else {
      event = new HtmlAttributeChangeEvent(this, qualifiedName, oldAttributeValue);
    }
    notifyAttributeChangeListeners(event, this, oldAttributeValue);
    
    Attr result = super.setAttributeNode(attribute);
    
    fireAttributeChangeImpl(event, htmlPage, mappedElement, qualifiedName, attribute.getValue(), oldAttributeValue);
    
    return result;
  }
  




  public final void removeAttribute(String attributeName)
  {
    String value = getAttribute(attributeName);
    if (value == ATTRIBUTE_NOT_DEFINED) {
      return;
    }
    
    HtmlPage htmlPage = getHtmlPageOrNull();
    if (htmlPage != null) {
      htmlPage.removeMappedElement(this);
    }
    
    super.removeAttribute(attributeName);
    
    if (htmlPage != null) {
      htmlPage.addMappedElement(this);
      
      HtmlAttributeChangeEvent event = new HtmlAttributeChangeEvent(this, attributeName, value);
      fireHtmlAttributeRemoved(event);
      htmlPage.fireHtmlAttributeRemoved(event);
    }
  }
  










  protected void fireHtmlAttributeAdded(HtmlAttributeChangeEvent event)
  {
    DomNode parentNode = getParentNode();
    if ((parentNode instanceof HtmlElement)) {
      ((HtmlElement)parentNode).fireHtmlAttributeAdded(event);
    }
  }
  










  protected void fireHtmlAttributeReplaced(HtmlAttributeChangeEvent event)
  {
    DomNode parentNode = getParentNode();
    if ((parentNode instanceof HtmlElement)) {
      ((HtmlElement)parentNode).fireHtmlAttributeReplaced(event);
    }
  }
  










  protected void fireHtmlAttributeRemoved(HtmlAttributeChangeEvent event)
  {
    synchronized (attributeListeners_) {
      for (HtmlAttributeChangeListener listener : attributeListeners_) {
        listener.attributeRemoved(event);
      }
    }
    DomNode parentNode = getParentNode();
    if ((parentNode instanceof HtmlElement)) {
      ((HtmlElement)parentNode).fireHtmlAttributeRemoved(event);
    }
  }
  



  public String getNodeName()
  {
    String prefix = getPrefix();
    if (prefix != null)
    {
      StringBuilder name = new StringBuilder(prefix.toLowerCase(Locale.ROOT));
      name.append(':');
      name.append(getLocalName().toLowerCase(Locale.ROOT));
      return name.toString();
    }
    return getLocalName().toLowerCase(Locale.ROOT);
  }
  




  public final void setId(String newId)
  {
    setAttribute("id", newId);
  }
  







  public Short getTabIndex()
  {
    String index = getAttribute("tabindex");
    if ((index == null) || (index.isEmpty())) {
      return null;
    }
    try {
      long l = Long.parseLong(index);
      if ((l >= 0L) && (l <= 32767L)) {
        return Short.valueOf((short)(int)l);
      }
      return TAB_INDEX_OUT_OF_BOUNDS;
    }
    catch (NumberFormatException e) {}
    return null;
  }
  






  public HtmlElement getEnclosingElement(String tagName)
  {
    String tagNameLC = tagName.toLowerCase(Locale.ROOT);
    
    for (DomNode currentNode = getParentNode(); currentNode != null; currentNode = currentNode.getParentNode()) {
      if (((currentNode instanceof HtmlElement)) && (currentNode.getNodeName().equals(tagNameLC))) {
        return (HtmlElement)currentNode;
      }
    }
    return null;
  }
  




  public HtmlForm getEnclosingForm()
  {
    if (owningForm_ != null) {
      return owningForm_;
    }
    return (HtmlForm)getEnclosingElement("form");
  }
  




  public HtmlForm getEnclosingFormOrDie()
  {
    HtmlForm form = getEnclosingForm();
    if (form == null) {
      throw new IllegalStateException("Element is not contained within a form: " + this);
    }
    return form;
  }
  




  public void type(String text)
    throws IOException
  {
    for (char ch : text.toCharArray()) {
      type(ch);
    }
  }
  








  public Page type(char c)
    throws IOException
  {
    return type(c, false, true);
  }
  











  private Page type(char c, boolean startAtEnd, boolean lastType)
    throws IOException
  {
    if (((this instanceof DisabledElement)) && (((DisabledElement)this).isDisabled())) {
      return getPage();
    }
    

    getPage().getWebClient().setCurrentWindow(getPage().getEnclosingWindow());
    
    HtmlPage page = (HtmlPage)getPage();
    if (page.getFocusedElement() != this) {
      focus();
    }
    boolean isShiftNeeded = KeyboardEvent.isShiftNeeded(c, shiftPressed_);
    ScriptResult shiftDownResult;
    Event shiftDown;
    ScriptResult shiftDownResult;
    if (isShiftNeeded) {
      Event shiftDown = new KeyboardEvent(this, "keydown", 16, 
        true, ctrlPressed_, altPressed_);
      shiftDownResult = fireEvent(shiftDown);
    }
    else {
      shiftDown = null;
      shiftDownResult = null;
    }
    
    Event keyDown = new KeyboardEvent(this, "keydown", c, 
      shiftPressed_, ctrlPressed_, altPressed_);
    ScriptResult keyDownResult = fireEvent(keyDown);
    
    if (!keyDown.isAborted(keyDownResult)) {
      Event keyPress = new KeyboardEvent(this, "keypress", c, 
        shiftPressed_, ctrlPressed_, altPressed_);
      ScriptResult keyPressResult = fireEvent(keyPress);
      
      if (((shiftDown == null) || (!shiftDown.isAborted(shiftDownResult))) && 
        (!keyPress.isAborted(keyPressResult))) {
        doType(c, startAtEnd, lastType);
      }
    }
    
    WebClient webClient = page.getWebClient();
    if (((this instanceof HtmlTextInput)) || 
      ((this instanceof HtmlTextArea)) || 
      ((this instanceof HtmlPasswordInput))) {
      Event input = new KeyboardEvent(this, "input", c, shiftPressed_, ctrlPressed_, altPressed_);
      fireEvent(input);
    }
    
    Event keyUp = new KeyboardEvent(this, "keyup", c, shiftPressed_, ctrlPressed_, altPressed_);
    fireEvent(keyUp);
    
    if (isShiftNeeded) {
      Event shiftUp = new KeyboardEvent(this, "keyup", 16, 
        false, ctrlPressed_, altPressed_);
      fireEvent(shiftUp);
    }
    
    HtmlForm form = getEnclosingForm();
    if ((form != null) && (c == '\n') && (isSubmittableByEnter())) {
      HtmlSubmitInput submit = (HtmlSubmitInput)form.getFirstByXPath(".//input[@type='submit']");
      if (submit != null) {
        return submit.click();
      }
      form.submit((SubmittableElement)this);
      webClient.getJavaScriptEngine().processPostponedActions();
    }
    return webClient.getCurrentWindow().getEnclosedPage();
  }
  










  public Page type(int keyCode)
  {
    return type(keyCode, false, true, true, true, true);
  }
  








  public Page type(Keyboard keyboard)
    throws IOException
  {
    Page page = null;
    
    List<Object[]> keys = keyboard.getKeys();
    for (int i = 0; i < keys.size(); i++) {
      Object[] entry = (Object[])keys.get(i);
      boolean startAtEnd = (i == 0) && (keyboard.isStartAtEnd());
      if (entry.length == 1) {
        type(((Character)entry[0]).charValue(), startAtEnd, i == keys.size() - 1);
      }
      else {
        int key = ((Integer)entry[0]).intValue();
        boolean pressed = ((Boolean)entry[1]).booleanValue();
        switch (key) {
        case 16: 
          shiftPressed_ = pressed;
          break;
        
        case 17: 
          ctrlPressed_ = pressed;
          break;
        
        case 18: 
          altPressed_ = pressed;
        }
        
        

        if (pressed) {
          boolean keyPress = true;
          boolean keyUp = true;
          switch (key) {
          case 16: 
          case 17: 
          case 18: 
            keyPress = false;
            keyUp = false;
          }
          
          

          page = type(key, startAtEnd, true, keyPress, keyUp, i == keys.size() - 1);
        }
        else {
          page = type(key, startAtEnd, false, false, true, i == keys.size() - 1);
        }
      }
    }
    
    return page;
  }
  

  private Page type(int keyCode, boolean startAtEnd, boolean fireKeyDown, boolean fireKeyPress, boolean fireKeyUp, boolean lastType)
  {
    if (((this instanceof DisabledElement)) && (((DisabledElement)this).isDisabled())) {
      return getPage();
    }
    
    HtmlPage page = (HtmlPage)getPage();
    if (page.getFocusedElement() != this) {
      focus();
    }
    ScriptResult keyDownResult;
    Event keyDown;
    ScriptResult keyDownResult;
    if (fireKeyDown) {
      Event keyDown = new KeyboardEvent(this, "keydown", keyCode, shiftPressed_, ctrlPressed_, altPressed_);
      keyDownResult = fireEvent(keyDown);
    }
    else {
      keyDown = null;
      keyDownResult = null;
    }
    
    BrowserVersion browserVersion = page.getWebClient().getBrowserVersion();
    ScriptResult keyPressResult;
    Event keyPress;
    ScriptResult keyPressResult;
    if ((fireKeyPress) && (browserVersion.hasFeature(BrowserVersionFeatures.KEYBOARD_EVENT_SPECIAL_KEYPRESS))) {
      Event keyPress = new KeyboardEvent(this, "keypress", keyCode, 
        shiftPressed_, ctrlPressed_, altPressed_);
      
      keyPressResult = fireEvent(keyPress);
    }
    else {
      keyPress = null;
      keyPressResult = null;
    }
    
    if ((keyDown != null) && (!keyDown.isAborted(keyDownResult)) && (
      (keyPress == null) || (!keyPress.isAborted(keyPressResult)))) {
      doType(keyCode, startAtEnd, lastType);
    }
    
    if (((this instanceof HtmlTextInput)) || 
      ((this instanceof HtmlTextArea)) || 
      ((this instanceof HtmlPasswordInput))) {
      Event input = new KeyboardEvent(this, "input", keyCode, 
        shiftPressed_, ctrlPressed_, altPressed_);
      fireEvent(input);
    }
    
    if (fireKeyUp) {
      Event keyUp = new KeyboardEvent(this, "keyup", keyCode, 
        shiftPressed_, ctrlPressed_, altPressed_);
      fireEvent(keyUp);
    }
    












    return page.getWebClient().getCurrentWindow().getEnclosedPage();
  }
  





  protected void doType(char c, boolean startAtEnd, boolean lastType)
  {
    DomNode domNode = getDoTypeNode();
    if ((domNode instanceof DomText)) {
      ((DomText)domNode).doType(c, startAtEnd, this, lastType);
    }
    else if ((domNode instanceof HtmlElement)) {
      try {
        ((HtmlElement)domNode).type(c, startAtEnd, lastType);
      }
      catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }
  








  protected void doType(int keyCode, boolean startAtEnd, boolean lastType)
  {
    DomNode domNode = getDoTypeNode();
    if ((domNode instanceof DomText)) {
      ((DomText)domNode).doType(keyCode, startAtEnd, this, lastType);
    }
    else if ((domNode instanceof HtmlElement)) {
      ((HtmlElement)domNode).type(keyCode, startAtEnd, true, true, true, lastType);
    }
  }
  



  private DomNode getDoTypeNode()
  {
    DomNode node = null;
    HTMLElement scriptElement = (HTMLElement)getScriptableObject();
    if ((scriptElement.getIsContentEditable()) || 
      ("on".equals(((Document)scriptElement.getOwnerDocument()).getDesignMode()))) {
      DomNodeList<DomNode> children = getChildNodes();
      if (!children.isEmpty()) {
        DomNode lastChild = (DomNode)children.get(children.size() - 1);
        if ((lastChild instanceof DomText)) {
          node = lastChild;
        }
        else if ((lastChild instanceof HtmlElement)) {
          node = lastChild;
        }
      }
      
      if (node == null) {
        DomText domText = new DomText(getPage(), "");
        appendChild(domText);
        node = domText;
      }
    }
    return node;
  }
  







  protected void typeDone(String newValue, boolean notifyAttributeChangeListeners) {}
  






  protected boolean acceptChar(char c)
  {
    return ((c < 57344) || (c > 63743)) && ((c == ' ') || (!Character.isWhitespace(c)));
  }
  




  protected boolean isSubmittableByEnter()
  {
    return false;
  }
  












  public final <E extends HtmlElement> E getOneHtmlElementByAttribute(String elementName, String attributeName, String attributeValue)
    throws ElementNotFoundException
  {
    WebAssert.notNull("elementName", elementName);
    WebAssert.notNull("attributeName", attributeName);
    WebAssert.notNull("attributeValue", attributeValue);
    
    List<E> list = getElementsByAttribute(elementName, attributeName, attributeValue);
    
    if (list.isEmpty()) {
      throw new ElementNotFoundException(elementName, attributeName, attributeValue);
    }
    
    return (HtmlElement)list.get(0);
  }
  













  public final <E extends HtmlElement> List<E> getElementsByAttribute(String elementName, String attributeName, String attributeValue)
  {
    List<E> list = new ArrayList();
    String lowerCaseTagName = elementName.toLowerCase(Locale.ROOT);
    
    for (HtmlElement next : getHtmlElementDescendants()) {
      if (next.getTagName().equals(lowerCaseTagName)) {
        String attValue = next.getAttribute(attributeName);
        if ((attValue != null) && (attValue.equals(attributeValue))) {
          list.add(next);
        }
      }
    }
    return list;
  }
  








  public final HtmlElement appendChildIfNoneExists(String tagName)
  {
    List<HtmlElement> children = getElementsByTagName(tagName);
    HtmlElement child; if (children.isEmpty())
    {
      HtmlElement child = (HtmlElement)((HtmlPage)getPage()).createElement(tagName);
      appendChild(child);
    }
    else
    {
      child = (HtmlElement)children.get(0);
    }
    return child;
  }
  





  public final void removeChild(String tagName, int i)
  {
    List<HtmlElement> children = getElementsByTagName(tagName);
    if ((i >= 0) && (i < children.size())) {
      ((HtmlElement)children.get(i)).remove();
    }
  }
  






  public final boolean hasEventHandlers(String eventName)
  {
    HTMLElement jsObj = (HTMLElement)getScriptableObject();
    return jsObj.hasEventHandlers(eventName);
  }
  





  public final void setEventHandler(String eventName, Function eventHandler)
  {
    HTMLElement jsObj = (HTMLElement)getScriptableObject();
    jsObj.setEventHandler(eventName, eventHandler);
  }
  







  public final void setEventHandler(String eventName, String jsSnippet)
  {
    BaseFunction function = new EventHandler(this, eventName, jsSnippet);
    setEventHandler(eventName, function);
    if (LOG.isDebugEnabled()) {
      LOG.debug("Created event handler " + function.getFunctionName() + 
        " for " + eventName + " on " + this);
    }
  }
  




  public final void removeEventHandler(String eventName)
  {
    setEventHandler(eventName, null);
  }
  







  public void addHtmlAttributeChangeListener(HtmlAttributeChangeListener listener)
  {
    WebAssert.notNull("listener", listener);
    synchronized (attributeListeners_) {
      attributeListeners_.add(listener);
    }
  }
  







  public void removeHtmlAttributeChangeListener(HtmlAttributeChangeListener listener)
  {
    WebAssert.notNull("listener", listener);
    synchronized (attributeListeners_) {
      attributeListeners_.remove(listener);
    }
  }
  


  protected void checkChildHierarchy(Node childNode)
    throws DOMException
  {
    if ((!(childNode instanceof Element)) && (!(childNode instanceof Text)) && 
      (!(childNode instanceof Comment)) && (!(childNode instanceof ProcessingInstruction)) && 
      (!(childNode instanceof CDATASection)) && (!(childNode instanceof EntityReference))) {
      throw new DOMException((short)3, 
        "The Element may not have a child of this type: " + childNode.getNodeType());
    }
    super.checkChildHierarchy(childNode);
  }
  
  void setOwningForm(HtmlForm form) {
    owningForm_ = form;
  }
  




  protected boolean isAttributeCaseSensitive()
  {
    return false;
  }
  






  public final String getLangAttribute()
  {
    return getAttribute("lang");
  }
  






  public final String getXmlLangAttribute()
  {
    return getAttribute("xml:lang");
  }
  






  public final String getTextDirectionAttribute()
  {
    return getAttribute("dir");
  }
  






  public final String getOnClickAttribute()
  {
    return getAttribute("onclick");
  }
  






  public final String getOnDblClickAttribute()
  {
    return getAttribute("ondblclick");
  }
  






  public final String getOnMouseDownAttribute()
  {
    return getAttribute("onmousedown");
  }
  






  public final String getOnMouseUpAttribute()
  {
    return getAttribute("onmouseup");
  }
  






  public final String getOnMouseOverAttribute()
  {
    return getAttribute("onmouseover");
  }
  






  public final String getOnMouseMoveAttribute()
  {
    return getAttribute("onmousemove");
  }
  






  public final String getOnMouseOutAttribute()
  {
    return getAttribute("onmouseout");
  }
  






  public final String getOnKeyPressAttribute()
  {
    return getAttribute("onkeypress");
  }
  






  public final String getOnKeyDownAttribute()
  {
    return getAttribute("onkeydown");
  }
  






  public final String getOnKeyUpAttribute()
  {
    return getAttribute("onkeyup");
  }
  



  public String getCanonicalXPath()
  {
    DomNode parent = getParentNode();
    if (parent.getNodeType() == 9) {
      return "/" + getNodeName();
    }
    return parent.getCanonicalXPath() + '/' + getXPathToken();
  }
  


  private String getXPathToken()
  {
    DomNode parent = getParentNode();
    int total = 0;
    int nodeIndex = 0;
    for (DomNode child : parent.getChildren()) {
      if ((child.getNodeType() == 1) && (child.getNodeName().equals(getNodeName()))) {
        total++;
      }
      if (child == this) {
        nodeIndex = total;
      }
    }
    
    if ((nodeIndex == 1) && (total == 1)) {
      return getNodeName();
    }
    return getNodeName() + '[' + nodeIndex + ']';
  }
  




  public boolean isDisplayed()
  {
    if (ATTRIBUTE_NOT_DEFINED != getAttribute("hidden")) {
      return false;
    }
    return super.isDisplayed();
  }
  






  public DisplayStyle getDefaultStyleDisplay()
  {
    return DisplayStyle.BLOCK;
  }
  








  protected final String getSrcAttributeNormalized()
  {
    String attrib = getAttribute("src");
    if (ATTRIBUTE_NOT_DEFINED == attrib) {
      return attrib;
    }
    
    return StringUtils.replaceChars(attrib, "\r\n", "");
  }
  






  protected void detach()
  {
    ScriptableObject document = getPage().getScriptableObject();
    
    if ((document instanceof HTMLDocument)) {
      HTMLDocument doc = (HTMLDocument)document;
      Object activeElement = doc.getActiveElement();
      
      if (activeElement == getScriptableObject()) {
        doc.setActiveElement(null);
        if (hasFeature(BrowserVersionFeatures.HTMLELEMENT_REMOVE_ACTIVE_TRIGGERS_BLUR_EVENT)) {
          ((HtmlPage)getPage()).setFocusedElement(null);
        }
        else {
          ((HtmlPage)getPage()).setElementWithFocus(null);
        }
      }
      else {
        for (DomNode child : getChildNodes()) {
          if (activeElement == child.getScriptableObject()) {
            doc.setActiveElement(null);
            if (hasFeature(BrowserVersionFeatures.HTMLELEMENT_REMOVE_ACTIVE_TRIGGERS_BLUR_EVENT)) {
              ((HtmlPage)getPage()).setFocusedElement(null);
              break;
            }
            ((HtmlPage)getPage()).setElementWithFocus(null);
            

            break;
          }
        }
      }
    }
    super.detach();
  }
  



  public boolean handles(Event event)
  {
    if (("blur".equals(event.getType())) || ("focus".equals(event.getType()))) {
      return ((this instanceof SubmittableElement)) || (getTabIndex() != null);
    }
    
    if (((this instanceof DisabledElement)) && (((DisabledElement)this).isDisabled())) {
      return false;
    }
    
    return super.handles(event);
  }
  



  protected boolean isShiftPressed()
  {
    return shiftPressed_;
  }
  



  public boolean isCtrlPressed()
  {
    return ctrlPressed_;
  }
  



  public boolean isAltPressed()
  {
    return altPressed_;
  }
}
