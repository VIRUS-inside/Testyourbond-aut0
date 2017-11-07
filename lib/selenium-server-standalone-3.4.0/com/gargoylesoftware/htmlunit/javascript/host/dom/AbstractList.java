package com.gargoylesoftware.htmlunit.javascript.host.dom;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.DomChangeEvent;
import com.gargoylesoftware.htmlunit.html.DomChangeListener;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlAttributeChangeEvent;
import com.gargoylesoftware.htmlunit.html.HtmlAttributeChangeListener;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.ClassConfiguration;
import com.gargoylesoftware.htmlunit.javascript.configuration.JavaScriptConfiguration;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;




















@JsxClass(isJSObject=false)
public class AbstractList
  extends SimpleScriptable
  implements Function
{
  private boolean avoidObjectDetection_;
  private boolean attributeChangeSensitive_;
  private List<Object> cachedElements_;
  private boolean listenerRegistered_;
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public AbstractList() {}
  
  protected static enum EffectOnCache
  {
    NONE, 
    
    RESET;
  }
  

























  public AbstractList(DomNode domeNode, boolean attributeChangeSensitive)
  {
    this(domeNode, attributeChangeSensitive, null);
  }
  





  protected AbstractList(DomNode domNode, List<?> initialElements)
  {
    this(domNode, true, new ArrayList(initialElements));
  }
  








  private AbstractList(DomNode domNode, boolean attributeChangeSensitive, List<Object> initialElements)
  {
    if (domNode != null) {
      setDomNode(domNode, false);
      ScriptableObject parentScope = domNode.getScriptableObject();
      if (parentScope != null) {
        setParentScope(parentScope);
        setPrototype(getPrototype(getClass()));
      }
    }
    attributeChangeSensitive_ = attributeChangeSensitive;
    cachedElements_ = initialElements;
  }
  




  public boolean avoidObjectDetection()
  {
    return avoidObjectDetection_;
  }
  


  public void setAvoidObjectDetection(boolean newValue)
  {
    avoidObjectDetection_ = newValue;
  }
  



  public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args)
  {
    if (args.length == 0) {
      throw Context.reportRuntimeError("Zero arguments; need an index or a key.");
    }
    Object object = getIt(args[0]);
    if (object == NOT_FOUND) {
      if (getBrowserVersion().hasFeature(BrowserVersionFeatures.HTMLCOLLECTION_NULL_IF_NOT_FOUND)) {
        return null;
      }
      return Undefined.instance;
    }
    return object;
  }
  



  public final Scriptable construct(Context cx, Scriptable scope, Object[] args)
  {
    return null;
  }
  





  private Object getIt(Object o)
  {
    if ((o instanceof Number)) {
      Number n = (Number)o;
      int i = n.intValue();
      return get(i, this);
    }
    String key = String.valueOf(o);
    return get(key, this);
  }
  




  public final Object get(int index, Scriptable start)
  {
    AbstractList array = (AbstractList)start;
    List<Object> elements = array.getElements();
    if ((index >= 0) && (index < elements.size())) {
      return getScriptableForElement(elements.get(index));
    }
    return NOT_FOUND;
  }
  
  protected void setDomNode(DomNode domNode, boolean assignScriptObject)
  {
    DomNode oldDomNode = getDomNodeOrNull();
    
    super.setDomNode(domNode, assignScriptObject);
    
    if (oldDomNode != domNode) {
      listenerRegistered_ = false;
    }
  }
  




  public List<Object> getElements()
  {
    List<Object> cachedElements = cachedElements_;
    
    if (cachedElements == null) {
      if (getParentScope() == null) {
        cachedElements = new ArrayList();
      }
      else {
        cachedElements = computeElements();
      }
      cachedElements_ = cachedElements;
      if (!listenerRegistered_) {
        DomHtmlAttributeChangeListenerImpl listener = new DomHtmlAttributeChangeListenerImpl(this, null);
        DomNode domNode = getDomNodeOrNull();
        if (domNode != null) {
          domNode.addDomChangeListener(listener);
          if (attributeChangeSensitive_) {
            if ((domNode instanceof HtmlElement)) {
              ((HtmlElement)domNode).addHtmlAttributeChangeListener(listener);
            }
            else if ((domNode instanceof HtmlPage)) {
              ((HtmlPage)domNode).addHtmlAttributeChangeListener(listener);
            }
          }
          listenerRegistered_ = true;
        }
      }
    }
    


    return cachedElements;
  }
  



  protected List<Object> computeElements()
  {
    List<Object> response = new ArrayList();
    DomNode domNode = getDomNodeOrNull();
    if (domNode == null) {
      return response;
    }
    for (DomNode node : getCandidates()) {
      if (((node instanceof DomElement)) && (isMatching(node))) {
        response.add(node);
      }
    }
    return response;
  }
  




  protected Iterable<DomNode> getCandidates()
  {
    DomNode domNode = getDomNodeOrNull();
    return domNode.getDescendants();
  }
  





  protected boolean isMatching(DomNode node)
  {
    return false;
  }
  










  protected Object getWithPreemption(String name)
  {
    if ("length".equals(name)) {
      return NOT_FOUND;
    }
    
    List<Object> elements = getElements();
    

    List<Object> matchingElements = new ArrayList();
    
    for (Object next : elements) {
      if ((next instanceof DomElement)) {
        String id = ((DomElement)next).getId();
        if (name.equals(id)) {
          matchingElements.add(next);
        }
      }
    }
    
    if (matchingElements.size() == 1) {
      return getScriptableForElement(matchingElements.get(0));
    }
    if (!matchingElements.isEmpty()) {
      AbstractList collection = create(getDomNodeOrDie(), matchingElements);
      collection.setAvoidObjectDetection(true);
      return collection;
    }
    

    return getWithPreemptionByName(name, elements);
  }
  





  protected AbstractList create(DomNode parentScope, List<?> initialElements)
  {
    return new AbstractList(parentScope, initialElements);
  }
  





  protected Object getWithPreemptionByName(String name, List<Object> elements)
  {
    List<Object> matchingElements = new ArrayList();
    for (Object next : elements) {
      if ((next instanceof DomElement)) {
        String nodeName = ((DomElement)next).getAttribute("name");
        if (name.equals(nodeName)) {
          matchingElements.add(next);
        }
      }
    }
    
    if (matchingElements.isEmpty()) {
      return NOT_FOUND;
    }
    if (matchingElements.size() == 1) {
      return getScriptableForElement(matchingElements.get(0));
    }
    

    DomNode domNode = getDomNodeOrNull();
    AbstractList collection = create(domNode, matchingElements);
    collection.setAvoidObjectDetection(true);
    return collection;
  }
  



  @JsxGetter
  public final int getLength()
  {
    return getElements().size();
  }
  





  @JsxFunction
  public Object item(Object index)
  {
    Object object = getIt(index);
    if (object == NOT_FOUND) {
      if (getBrowserVersion().hasFeature(BrowserVersionFeatures.HTMLCOLLECTION_NULL_IF_ITEM_NOT_FOUND)) {
        return null;
      }
      return Undefined.instance;
    }
    return object;
  }
  



  public String toString()
  {
    return getClass().getSimpleName() + " for " + getDomNodeOrNull();
  }
  




  protected Object equivalentValues(Object other)
  {
    if (other == this) {
      return Boolean.TRUE;
    }
    if ((other instanceof AbstractList)) {
      AbstractList otherArray = (AbstractList)other;
      DomNode domNode = getDomNodeOrNull();
      DomNode domNodeOther = otherArray.getDomNodeOrNull();
      if ((getClass() == other.getClass()) && 
        (domNode == domNodeOther) && 
        (getElements().equals(otherArray.getElements()))) {
        return Boolean.TRUE;
      }
      return NOT_FOUND;
    }
    
    return super.equivalentValues(other);
  }
  



  public boolean has(int index, Scriptable start)
  {
    return (index >= 0) && (index < getElements().size());
  }
  




  public boolean has(String name, Scriptable start)
  {
    if (isPrototype()) {
      return super.has(name, start);
    }
    try
    {
      return has(Integer.parseInt(name), start);

    }
    catch (NumberFormatException localNumberFormatException)
    {

      if ("length".equals(name)) {
        return true;
      }
      BrowserVersion browserVersion = getBrowserVersion();
      if (browserVersion.hasFeature(BrowserVersionFeatures.JS_NODE_LIST_ENUMERATE_FUNCTIONS)) {
        JavaScriptConfiguration jsConfig = getWindow().getWebWindow().getWebClient()
          .getJavaScriptEngine().getJavaScriptConfiguration();
        if (jsConfig.getClassConfiguration(getClassName()).getFunctionKeys().contains(name)) {
          return true;
        }
      }
      
      if (browserVersion.hasFeature(BrowserVersionFeatures.JS_NODE_LIST_ENUMERATE_CHILDREN)) {
        for (Object next : getElements()) {
          if ((next instanceof DomElement)) {
            DomElement element = (DomElement)next;
            if (name.equals(element.getAttribute("name"))) {
              return true;
            }
            
            String id = element.getId();
            if (name.equals(id)) {
              return true;
            }
          }
        }
      }
      if (getWithPreemption(name) != NOT_FOUND) return true; } return false;
  }
  




  public Object[] getIds()
  {
    if (isPrototype()) {
      return super.getIds();
    }
    
    List<String> idList = new ArrayList();
    List<Object> elements = getElements();
    
    BrowserVersion browserVersion = getBrowserVersion();
    if (browserVersion.hasFeature(BrowserVersionFeatures.JS_NODE_LIST_ENUMERATE_FUNCTIONS)) {
      int length = elements.size();
      for (int i = 0; i < length; i++) {
        idList.add(Integer.toString(i));
      }
      
      idList.add("length");
      JavaScriptConfiguration jsConfig = getWindow().getWebWindow().getWebClient()
        .getJavaScriptEngine().getJavaScriptConfiguration();
      for (String name : jsConfig.getClassConfiguration(getClassName()).getFunctionKeys()) {
        idList.add(name);
      }
    }
    else {
      idList.add("length");
    }
    if (browserVersion.hasFeature(BrowserVersionFeatures.JS_NODE_LIST_ENUMERATE_CHILDREN)) {
      addElementIds(idList, elements);
    }
    
    return idList.toArray();
  }
  
  private boolean isPrototype() {
    return !(getPrototype() instanceof AbstractList);
  }
  




  protected void addElementIds(List<String> idList, List<Object> elements)
  {
    int index = 0;
    for (Object next : elements) {
      HtmlElement element = (HtmlElement)next;
      String name = element.getAttribute("name");
      if (name != DomElement.ATTRIBUTE_NOT_DEFINED) {
        idList.add(name);
      }
      String id = element.getId();
      if (id != DomElement.ATTRIBUTE_NOT_DEFINED) {
        idList.add(id);
      }
      if (!getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_NODE_LIST_ENUMERATE_FUNCTIONS)) {
        idList.add(Integer.toString(index));
      }
      index++;
    }
  }
  

  private static final class DomHtmlAttributeChangeListenerImpl
    implements DomChangeListener, HtmlAttributeChangeListener
  {
    private transient WeakReference<AbstractList> nodeList_;
    
    private DomHtmlAttributeChangeListenerImpl(AbstractList nodeList)
    {
      nodeList_ = new WeakReference(nodeList);
    }
    



    public void nodeAdded(DomChangeEvent event)
    {
      clearCache();
    }
    



    public void nodeDeleted(DomChangeEvent event)
    {
      clearCache();
    }
    



    public void attributeAdded(HtmlAttributeChangeEvent event)
    {
      handleChangeOnCache(event);
    }
    



    public void attributeRemoved(HtmlAttributeChangeEvent event)
    {
      handleChangeOnCache(event);
    }
    



    public void attributeReplaced(HtmlAttributeChangeEvent event)
    {
      AbstractList nodes = (AbstractList)nodeList_.get();
      if (nodes == null) {
        return;
      }
      if (attributeChangeSensitive_) {
        handleChangeOnCache(event);
      }
    }
    
    private void handleChangeOnCache(HtmlAttributeChangeEvent event) {
      AbstractList nodes = (AbstractList)nodeList_.get();
      if (nodes == null) {
        return;
      }
      
      AbstractList.EffectOnCache effectOnCache = nodes.getEffectOnCache(event);
      if (AbstractList.EffectOnCache.NONE == effectOnCache) {
        return;
      }
      if (AbstractList.EffectOnCache.RESET == effectOnCache) {
        clearCache();
      }
    }
    
    private void clearCache() {
      AbstractList nodes = (AbstractList)nodeList_.get();
      if (nodes != null) {
        cachedElements_ = null;
      }
    }
  }
  





  protected EffectOnCache getEffectOnCache(HtmlAttributeChangeEvent event)
  {
    return EffectOnCache.RESET;
  }
  




  protected Scriptable getScriptableForElement(Object object)
  {
    if ((object instanceof Scriptable)) {
      return (Scriptable)object;
    }
    return getScriptableFor(object);
  }
}
