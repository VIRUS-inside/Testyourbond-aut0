package com.gargoylesoftware.htmlunit.javascript;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.javascript.configuration.CanSetReadOnly;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLUnknownElement;
import java.lang.reflect.Method;
import java.util.Stack;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;




























public class SimpleScriptable
  extends HtmlUnitScriptable
  implements Cloneable
{
  private static final Log LOG = LogFactory.getLog(SimpleScriptable.class);
  
  private DomNode domNode_;
  private boolean caseSensitive_ = true;
  



  public SimpleScriptable() {}
  



  public Object get(String name, Scriptable start)
  {
    if (!caseSensitive_) {
      for (Object o : getAllIds()) {
        String objectName = Context.toString(o);
        if (name.equalsIgnoreCase(objectName)) {
          name = objectName;
          break;
        }
      }
    }
    
    Object response = super.get(name, start);
    if (response != NOT_FOUND) {
      return response;
    }
    if (this == start) {
      response = getWithPreemption(name);
    }
    if ((response == NOT_FOUND) && ((start instanceof Window))) {
      response = ((Window)start).getWithFallback(name);
    }
    return response;
  }
  









  protected Object getWithPreemption(String name)
  {
    return NOT_FOUND;
  }
  
  public boolean has(int index, Scriptable start)
  {
    Object found = get(index, start);
    if ((Undefined.instance != found) && (Scriptable.NOT_FOUND != found)) {
      return true;
    }
    return super.has(index, start);
  }
  




  public DomNode getDomNodeOrDie()
  {
    if (domNode_ == null) {
      String clazz = getClass().getName();
      throw new IllegalStateException("DomNode has not been set for this SimpleScriptable: " + clazz);
    }
    return domNode_;
  }
  




  public DomNode getDomNodeOrNull()
  {
    return domNode_;
  }
  



  public void setDomNode(DomNode domNode)
  {
    setDomNode(domNode, true);
  }
  




  protected void setDomNode(DomNode domNode, boolean assignScriptObject)
  {
    WebAssert.notNull("domNode", domNode);
    domNode_ = domNode;
    if (assignScriptObject) {
      domNode_.setScriptableObject(this);
    }
  }
  



  public void setHtmlElement(HtmlElement htmlElement)
  {
    setDomNode(htmlElement);
  }
  







  protected SimpleScriptable getScriptableFor(Object object)
  {
    if ((object instanceof WebWindow)) {
      return (SimpleScriptable)((WebWindow)object).getScriptableObject();
    }
    
    DomNode domNode = (DomNode)object;
    
    Object scriptObject = domNode.getScriptableObject();
    if (scriptObject != null) {
      return (SimpleScriptable)scriptObject;
    }
    return makeScriptableFor(domNode);
  }
  







  public SimpleScriptable makeScriptableFor(DomNode domNode)
  {
    Class<? extends SimpleScriptable> javaScriptClass = null;
    if (((domNode instanceof HtmlImage)) && ("image".equals(((HtmlImage)domNode).getOriginalQualifiedName())) && 
      (((HtmlImage)domNode).wasCreatedByJavascript())) {
      if (domNode.hasFeature(BrowserVersionFeatures.HTMLIMAGE_HTMLELEMENT)) {
        javaScriptClass = HTMLElement.class;
      }
      else if (domNode.hasFeature(BrowserVersionFeatures.HTMLIMAGE_HTMLUNKNOWNELEMENT)) {
        javaScriptClass = HTMLUnknownElement.class;
      }
    }
    if (javaScriptClass == null) {
      JavaScriptEngine javaScriptEngine = getWindow().getWebWindow().getWebClient().getJavaScriptEngine();
      for (Class<?> c = domNode.getClass(); (javaScriptClass == null) && (c != null); c = c.getSuperclass()) {
        javaScriptClass = javaScriptEngine.getJavaScriptClass(c);
      }
    }
    

    if (javaScriptClass == null)
    {
      SimpleScriptable scriptable = new HTMLElement();
      if (LOG.isDebugEnabled()) {
        LOG.debug("No JavaScript class found for element <" + domNode.getNodeName() + ">. Using HTMLElement");
      }
    }
    else {
      try {
        scriptable = (SimpleScriptable)javaScriptClass.newInstance();
      } catch (Exception e) {
        SimpleScriptable scriptable;
        throw Context.throwAsScriptRuntimeEx(e);
      } }
    SimpleScriptable scriptable;
    initParentScope(domNode, scriptable);
    
    scriptable.setPrototype(getPrototype(javaScriptClass));
    scriptable.setDomNode(domNode);
    
    return scriptable;
  }
  




  protected void initParentScope(DomNode domNode, SimpleScriptable scriptable)
  {
    SgmlPage page = domNode.getPage();
    WebWindow enclosingWindow = page.getEnclosingWindow();
    if ((enclosingWindow != null) && (enclosingWindow.getEnclosedPage() == page)) {
      scriptable.setParentScope(enclosingWindow.getScriptableObject());
    }
    else {
      scriptable.setParentScope(ScriptableObject.getTopLevelScope(page.getScriptableObject()));
    }
  }
  





  public Scriptable getPrototype(Class<? extends SimpleScriptable> javaScriptClass)
  {
    Scriptable prototype = getWindow().getPrototype(javaScriptClass);
    if ((prototype == null) && (javaScriptClass != SimpleScriptable.class)) {
      return getPrototype(javaScriptClass.getSuperclass());
    }
    return prototype;
  }
  






  public Object getDefaultValue(Class<?> hint)
  {
    if ((String.class.equals(hint)) || (hint == null)) {
      return "[object " + getClassName() + "]";
    }
    return super.getDefaultValue(hint);
  }
  



  public Window getWindow()
    throws RuntimeException
  {
    return getWindow(this);
  }
  




  protected static Window getWindow(Scriptable s)
    throws RuntimeException
  {
    Scriptable top = ScriptableObject.getTopLevelScope(s);
    if ((top instanceof Window)) {
      return (Window)top;
    }
    throw new RuntimeException("Unable to find window associated with " + s);
  }
  





  protected Scriptable getStartingScope()
  {
    Stack<Scriptable> stack = 
      (Stack)Context.getCurrentContext().getThreadLocal("startingScope");
    if (stack == null) {
      return null;
    }
    return (Scriptable)stack.peek();
  }
  



  public BrowserVersion getBrowserVersion()
  {
    DomNode node = getDomNodeOrNull();
    if (node != null) {
      return node.getPage().getWebClient().getBrowserVersion();
    }
    return getWindow().getWebWindow().getWebClient().getBrowserVersion();
  }
  



  public boolean hasInstance(Scriptable instance)
  {
    if (getPrototype() == null)
    {

      Object prototype = get("prototype", this);
      if (!(prototype instanceof ScriptableObject)) {
        Context.throwAsScriptRuntimeEx(new Exception("Null prototype"));
      }
      return ((ScriptableObject)prototype).hasInstance(instance);
    }
    
    return super.hasInstance(instance);
  }
  



  protected Object equivalentValues(Object value)
  {
    if ((value instanceof SimpleScriptableProxy)) {
      value = ((SimpleScriptableProxy)value).getDelegee();
    }
    return super.equivalentValues(value);
  }
  


  public SimpleScriptable clone()
  {
    try
    {
      return (SimpleScriptable)super.clone();
    }
    catch (Exception e) {
      throw new IllegalStateException("Clone not supported");
    }
  }
  



  public void setCaseSensitive(boolean caseSensitive)
  {
    caseSensitive_ = caseSensitive;
    Scriptable prototype = getPrototype();
    if ((prototype instanceof SimpleScriptable)) {
      ((SimpleScriptable)prototype).setCaseSensitive(caseSensitive);
    }
  }
  
  protected boolean isReadOnlySettable(String name, Object value)
  {
    for (Method m : getClass().getMethods()) {
      JsxGetter jsxGetter = (JsxGetter)m.getAnnotation(JsxGetter.class);
      if (jsxGetter != null) {
        String methodProperty;
        if (jsxGetter.propertyName().isEmpty()) {
          int prefix = m.getName().startsWith("is") ? 2 : 3;
          String methodProperty = m.getName().substring(prefix);
          methodProperty = Character.toLowerCase(methodProperty.charAt(0)) + methodProperty.substring(1);
        }
        else {
          methodProperty = jsxGetter.propertyName();
        }
        if (methodProperty.equals(name)) {
          CanSetReadOnly canSetReadOnly = (CanSetReadOnly)m.getAnnotation(CanSetReadOnly.class);
          if (canSetReadOnly != null) {
            switch (canSetReadOnly.value()) {
            case EXCEPTION: 
              return true;
            case IGNORE: 
              return false;
            case YES: 
              throw ScriptRuntime.typeError3("msg.set.prop.no.setter", 
                name, getClassName(), Context.toString(value));
            }
            
          }
        }
      }
    }
    return true;
  }
}
