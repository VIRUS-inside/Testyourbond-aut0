package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.FormEncodingType;
import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.FormFieldWithNameHistory;
import com.gargoylesoftware.htmlunit.html.HtmlAttributeChangeEvent;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlImageInput;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.dom.AbstractList.EffectOnCache;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;
import org.apache.commons.lang3.StringUtils;
















































@JsxClass(domClass=HtmlForm.class)
public class HTMLFormElement
  extends HTMLElement
  implements Function
{
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public HTMLFormElement() {}
  
  public void setHtmlElement(HtmlElement htmlElement)
  {
    super.setHtmlElement(htmlElement);
    HtmlForm htmlForm = getHtmlForm();
    htmlForm.setScriptableObject(this);
  }
  



  @JsxGetter
  public String getName()
  {
    return getHtmlForm().getNameAttribute();
  }
  



  @JsxSetter
  public void setName(String name)
  {
    getHtmlForm().setNameAttribute(name);
  }
  



  @JsxGetter
  public HTMLCollection getElements()
  {
    final HtmlForm htmlForm = getHtmlForm();
    
    new HTMLCollection(htmlForm, false)
    {
      private boolean filterChildrenOfNestedForms_;
      
      protected List<Object> computeElements() {
        List<Object> response = super.computeElements();
        




        if (filterChildrenOfNestedForms_) {
          for (Iterator<Object> iter = response.iterator(); iter.hasNext();) {
            HtmlElement field = (HtmlElement)iter.next();
            if (field.getEnclosingForm() != htmlForm) {
              iter.remove();
            }
          }
        }
        response.addAll(htmlForm.getLostChildren());
        return response;
      }
      
      protected Object getWithPreemption(String name)
      {
        return HTMLFormElement.this.getWithPreemption(name);
      }
      
      public AbstractList.EffectOnCache getEffectOnCache(HtmlAttributeChangeEvent event)
      {
        return AbstractList.EffectOnCache.NONE;
      }
      
      protected boolean isMatching(DomNode node)
      {
        if ((node instanceof HtmlForm)) {
          filterChildrenOfNestedForms_ = true;
          return false;
        }
        
        return ((node instanceof HtmlInput)) || ((node instanceof HtmlButton)) || 
          ((node instanceof HtmlTextArea)) || ((node instanceof HtmlSelect));
      }
    };
  }
  





  @JsxGetter
  public int getLength()
  {
    int all = getElements().getLength();
    int images = getHtmlForm().getElementsByAttribute("input", "type", "image").size();
    return all - images;
  }
  



  @JsxGetter
  public String getAction()
  {
    String action = getHtmlForm().getActionAttribute();
    BrowserVersion browser = getBrowserVersion();
    if (action != DomElement.ATTRIBUTE_NOT_DEFINED) {
      if ((action.length() == 0) && (browser.hasFeature(BrowserVersionFeatures.JS_FORM_ACTION_EXPANDURL_IGNORE_EMPTY))) {
        return action;
      }
      try
      {
        action = ((HtmlPage)getHtmlForm().getPage()).getFullyQualifiedUrl(action).toExternalForm();
      }
      catch (MalformedURLException localMalformedURLException) {}
    }
    

    return action;
  }
  



  @JsxSetter
  public void setAction(String action)
  {
    WebAssert.notNull("action", action);
    getHtmlForm().setActionAttribute(action);
  }
  



  @JsxGetter
  public String getMethod()
  {
    return getHtmlForm().getMethodAttribute();
  }
  



  @JsxSetter
  public void setMethod(String method)
  {
    WebAssert.notNull("method", method);
    getHtmlForm().setMethodAttribute(method);
  }
  



  @JsxGetter
  public String getTarget()
  {
    return getHtmlForm().getTargetAttribute();
  }
  



  @JsxSetter
  public void setTarget(String target)
  {
    WebAssert.notNull("target", target);
    getHtmlForm().setTargetAttribute(target);
  }
  



  @JsxGetter
  public String getEnctype()
  {
    String encoding = getHtmlForm().getEnctypeAttribute();
    if ((!FormEncodingType.URL_ENCODED.getName().equals(encoding)) && 
      (!FormEncodingType.MULTIPART.getName().equals(encoding)) && 
      (!"text/plain".equals(encoding))) {
      return FormEncodingType.URL_ENCODED.getName();
    }
    return encoding;
  }
  



  @JsxSetter
  public void setEnctype(String enctype)
  {
    WebAssert.notNull("encoding", enctype);
    if ((getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_FORM_REJECT_INVALID_ENCODING)) && 
      (!FormEncodingType.URL_ENCODED.getName().equals(enctype)) && 
      (!FormEncodingType.MULTIPART.getName().equals(enctype))) {
      throw Context.reportRuntimeError("Cannot set the encoding property to invalid value: '" + 
        enctype + "'");
    }
    
    getHtmlForm().setEnctypeAttribute(enctype);
  }
  



  @JsxGetter
  public String getEncoding()
  {
    return getEnctype();
  }
  



  @JsxSetter
  public void setEncoding(String encoding)
  {
    setEnctype(encoding);
  }
  


  public HtmlForm getHtmlForm()
  {
    return (HtmlForm)getDomNodeOrDie();
  }
  


  @JsxFunction
  public void submit()
  {
    HtmlPage page = (HtmlPage)getDomNodeOrDie().getPage();
    WebClient webClient = page.getWebClient();
    
    String action = getHtmlForm().getActionAttribute().trim();
    if (StringUtils.startsWithIgnoreCase(action, "javascript:")) {
      String js = action.substring("javascript:".length());
      webClient.getJavaScriptEngine().execute(page, js, "Form action", 0);
    }
    else
    {
      WebRequest request = getHtmlForm().getWebRequest(null);
      String target = page.getResolvedTarget(getTarget());
      boolean forceDownload = webClient.getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_FORM_SUBMIT_FORCES_DOWNLOAD);
      boolean checkHash = 
        !webClient.getBrowserVersion().hasFeature(BrowserVersionFeatures.FORM_SUBMISSION_DOWNLOWDS_ALSO_IF_ONLY_HASH_CHANGED);
      webClient.download(page.getEnclosingWindow(), 
        target, request, checkHash, forceDownload, "JS form.submit()");
    }
  }
  









  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public Object item(Object index, Object subIndex)
  {
    if ((index instanceof Number)) {
      return getElements().item(index);
    }
    
    String name = Context.toString(index);
    Object response = getWithPreemption(name);
    if (((subIndex instanceof Number)) && ((response instanceof HTMLCollection))) {
      return ((HTMLCollection)response).item(subIndex);
    }
    
    return response;
  }
  


  @JsxFunction
  public void reset()
  {
    getHtmlForm().reset();
  }
  






  protected Object getWithPreemption(final String name)
  {
    if (getDomNodeOrNull() == null) {
      return NOT_FOUND;
    }
    List<HtmlElement> elements = findElements(name);
    
    if (elements.isEmpty()) {
      return NOT_FOUND;
    }
    if (elements.size() == 1) {
      return getScriptableFor(elements.get(0));
    }
    
    HTMLCollection collection = new HTMLCollection(getHtmlForm(), elements)
    {
      protected List<Object> computeElements() {
        return new ArrayList(HTMLFormElement.this.findElements(name));
      }
    };
    return collection;
  }
  
  private List<HtmlElement> findElements(String name) {
    List<HtmlElement> elements = new ArrayList();
    addElements(name, getHtmlForm().getHtmlElementDescendants(), elements);
    addElements(name, getHtmlForm().getLostChildren(), elements);
    

    if (elements.isEmpty()) {
      for (DomNode node : getHtmlForm().getChildren()) {
        if ((node instanceof HtmlImage)) {
          HtmlImage img = (HtmlImage)node;
          if ((name.equals(img.getId())) || (name.equals(img.getNameAttribute()))) {
            elements.add(img);
          }
        }
      }
    }
    
    return elements;
  }
  
  private void addElements(String name, Iterable<HtmlElement> nodes, List<HtmlElement> addTo)
  {
    for (HtmlElement node : nodes) {
      if (isAccessibleByIdOrName(node, name)) {
        addTo.add(node);
      }
    }
  }
  





  private boolean isAccessibleByIdOrName(HtmlElement element, String name)
  {
    if (((element instanceof FormFieldWithNameHistory)) && (!(element instanceof HtmlImageInput))) {
      if (element.getEnclosingForm() != getHtmlForm()) {
        return false;
      }
      if (name.equals(element.getId())) {
        return true;
      }
      FormFieldWithNameHistory elementWithNames = (FormFieldWithNameHistory)element;
      if (getBrowserVersion().hasFeature(BrowserVersionFeatures.FORMFIELD_REACHABLE_BY_ORIGINAL_NAME)) {
        if (name.equals(elementWithNames.getOriginalName())) {
          return true;
        }
      }
      else if (name.equals(element.getAttribute("name"))) {
        return true;
      }
      
      if ((getBrowserVersion().hasFeature(BrowserVersionFeatures.FORMFIELD_REACHABLE_BY_NEW_NAMES)) && 
        (elementWithNames.getNewNames().contains(name))) {
        return true;
      }
    }
    
    return false;
  }
  






  public Object get(int index, Scriptable start)
  {
    if (getDomNodeOrNull() == null) {
      return NOT_FOUND;
    }
    return getElements().get(index, ((HTMLFormElement)start).getElements());
  }
  



  public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args)
  {
    if (!getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_FORM_USABLE_AS_FUNCTION)) {
      throw Context.reportRuntimeError("Not a function.");
    }
    if (args.length > 0) {
      Object arg = args[0];
      if ((arg instanceof String)) {
        return ScriptableObject.getProperty(this, (String)arg);
      }
      if ((arg instanceof Number)) {
        return ScriptableObject.getProperty(this, ((Number)arg).intValue());
      }
    }
    return Undefined.instance;
  }
  



  public Scriptable construct(Context cx, Scriptable scope, Object[] args)
  {
    if (!getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_FORM_USABLE_AS_FUNCTION)) {
      throw Context.reportRuntimeError("Not a function.");
    }
    return null;
  }
  
  public boolean dispatchEvent(Event event)
  {
    boolean result = super.dispatchEvent(event);
    
    if (("submit".equals(event.getType())) && 
      (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_FORM_DISPATCHEVENT_SUBMITS))) {
      submit();
    }
    return result;
  }
}
