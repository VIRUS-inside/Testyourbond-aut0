package com.gargoylesoftware.htmlunit.activex.javascript.msxml;

import com.gargoylesoftware.htmlunit.html.DomChangeEvent;
import com.gargoylesoftware.htmlunit.html.DomChangeListener;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlAttributeChangeEvent;
import com.gargoylesoftware.htmlunit.html.HtmlAttributeChangeListener;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



























@JsxClass(browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
public class XMLDOMNodeList
  extends MSXMLScriptable
  implements Function, NodeList
{
  private String description_;
  private final boolean attributeChangeSensitive_;
  private List<DomNode> cachedElements_;
  private boolean listenerRegistered_;
  
  protected static enum EffectOnCache
  {
    NONE, 
    
    RESET;
  }
  















  private int currentIndex_ = 0;
  


  public XMLDOMNodeList()
  {
    attributeChangeSensitive_ = true;
  }
  







  private XMLDOMNodeList(ScriptableObject parentScope, boolean attributeChangeSensitive, String description)
  {
    setParentScope(parentScope);
    setPrototype(getPrototype(getClass()));
    description_ = description;
    attributeChangeSensitive_ = attributeChangeSensitive;
  }
  






  public XMLDOMNodeList(DomNode parentScope, boolean attributeChangeSensitive, String description)
  {
    this(parentScope.getScriptableObject(), attributeChangeSensitive, description);
    setDomNode(parentScope, false);
  }
  




  protected XMLDOMNodeList(DomNode parentScope, List<DomNode> initialElements)
  {
    this(parentScope.getScriptableObject(), true, null);
    cachedElements_ = new ArrayList(initialElements);
  }
  



  @JsxGetter
  public final int getLength()
  {
    return getElements().size();
  }
  




  @JsxFunction
  public final Object item(Object index)
  {
    return nullIfNotFound(getIt(index));
  }
  




  @JsxFunction
  public Object nextNode()
  {
    List<DomNode> elements = getElements();
    Object nextNode; Object nextNode; if ((currentIndex_ >= 0) && (currentIndex_ < elements.size())) {
      nextNode = ((DomNode)elements.get(currentIndex_)).getScriptableObject();
    }
    else {
      nextNode = null;
    }
    currentIndex_ += 1;
    return nextNode;
  }
  


  @JsxFunction
  public void reset()
  {
    currentIndex_ = 0;
  }
  




  public static XMLDOMNodeList emptyCollection(MSXMLScriptable parentScope)
  {
    final List<DomNode> list = Collections.emptyList();
    new XMLDOMNodeList(parentScope, true, null, list)
    {
      protected List<DomNode> getElements() {
        return list;
      }
    };
  }
  



  public final Scriptable construct(Context cx, Scriptable scope, Object[] args)
  {
    return null;
  }
  



  public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args)
  {
    if (args.length == 0) {
      throw Context.reportRuntimeError("Zero arguments; need an index or a key.");
    }
    return nullIfNotFound(getIt(args[0]));
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
    XMLDOMNodeList array = (XMLDOMNodeList)start;
    List<DomNode> elements = array.getElements();
    if ((index >= 0) && (index < elements.size())) {
      return getScriptableForElement(elements.get(index));
    }
    return NOT_FOUND;
  }
  




  protected List<DomNode> getElements()
  {
    List<DomNode> cachedElements = cachedElements_;
    
    if (cachedElements == null) {
      cachedElements = computeElements();
      cachedElements_ = cachedElements;
      if (!listenerRegistered_) {
        DomHtmlAttributeChangeListenerImpl listener = new DomHtmlAttributeChangeListenerImpl(this, null);
        DomNode domNode = getDomNodeOrNull();
        if (domNode != null) {
          domNode.addDomChangeListener(listener);
          if ((attributeChangeSensitive_) && ((domNode instanceof HtmlElement))) {
            ((HtmlElement)domNode).addHtmlAttributeChangeListener(listener);
          }
        }
        listenerRegistered_ = true;
      }
    }
    


    return cachedElements;
  }
  



  protected List<DomNode> computeElements()
  {
    List<DomNode> response = new ArrayList();
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
    
    List<DomNode> elements = getElements();
    

    List<DomNode> matchingElements = new ArrayList();
    
    for (DomNode next : elements) {
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
      XMLDOMNodeList collection = new XMLDOMNodeList(getDomNodeOrDie(), matchingElements);
      return collection;
    }
    

    for (DomNode next : elements) {
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
    XMLDOMNodeList collection = new XMLDOMNodeList(domNode, matchingElements);
    return collection;
  }
  






  private static Object nullIfNotFound(Object object)
  {
    if (object == NOT_FOUND) {
      return null;
    }
    return object;
  }
  



  public String toString()
  {
    return description_ != null ? description_ : super.toString();
  }
  




  protected Object equivalentValues(Object other)
  {
    if (other == this) {
      return Boolean.TRUE;
    }
    if ((other instanceof XMLDOMNodeList)) {
      XMLDOMNodeList otherArray = (XMLDOMNodeList)other;
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
    return index >= 0;
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
      if (getWithPreemption(name) != NOT_FOUND) return true; } return false;
  }
  




  public Object[] getIds()
  {
    if (isPrototype()) {
      return super.getIds();
    }
    
    List<String> idList = new ArrayList();
    
    List<DomNode> elements = getElements();
    
    idList.add("length");
    
    addElementIds(idList, elements);
    return idList.toArray();
  }
  
  private boolean isPrototype() {
    return !(getPrototype() instanceof XMLDOMNodeList);
  }
  




  protected void addElementIds(List<String> idList, List<DomNode> elements)
  {
    int index = 0;
    for (DomNode next : elements) {
      HtmlElement element = (HtmlElement)next;
      String name = element.getAttribute("name");
      if (name != DomElement.ATTRIBUTE_NOT_DEFINED) {
        idList.add(name);
      }
      else {
        String id = element.getId();
        if (id != DomElement.ATTRIBUTE_NOT_DEFINED) {
          idList.add(id);
        }
        else {
          idList.add(Integer.toString(index));
        }
      }
      index++;
    }
  }
  

  private static final class DomHtmlAttributeChangeListenerImpl
    implements DomChangeListener, HtmlAttributeChangeListener
  {
    private final transient WeakReference<XMLDOMNodeList> nodeList_;
    
    private DomHtmlAttributeChangeListenerImpl(XMLDOMNodeList nodeList)
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
      XMLDOMNodeList nodes = (XMLDOMNodeList)nodeList_.get();
      if (nodes == null) {
        return;
      }
      if (attributeChangeSensitive_) {
        handleChangeOnCache(nodes, event);
      }
    }
    
    private void handleChangeOnCache(HtmlAttributeChangeEvent event) {
      XMLDOMNodeList nodes = (XMLDOMNodeList)nodeList_.get();
      if (nodes == null) {
        return;
      }
      handleChangeOnCache(nodes, event);
    }
    
    private void handleChangeOnCache(XMLDOMNodeList nodes, HtmlAttributeChangeEvent event) {
      XMLDOMNodeList.EffectOnCache effectOnCache = nodes.getEffectOnCache(event);
      if (XMLDOMNodeList.EffectOnCache.NONE == effectOnCache) {
        return;
      }
      if (XMLDOMNodeList.EffectOnCache.RESET == effectOnCache) {
        clearCache();
      }
    }
    
    private void clearCache() {
      XMLDOMNodeList nodes = (XMLDOMNodeList)nodeList_.get();
      if (nodes != null) {
        cachedElements_ = null;
      }
    }
  }
  





  protected EffectOnCache getEffectOnCache(HtmlAttributeChangeEvent event)
  {
    return EffectOnCache.RESET;
  }
  



  public Node item(int index)
  {
    return (Node)getElements().get(index);
  }
  




  protected Scriptable getScriptableForElement(Object object)
  {
    if ((object instanceof Scriptable)) {
      return (Scriptable)object;
    }
    return getScriptableFor(object);
  }
}
