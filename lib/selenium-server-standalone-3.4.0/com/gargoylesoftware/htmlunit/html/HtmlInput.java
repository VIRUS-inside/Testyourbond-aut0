package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.javascript.host.event.MouseEvent;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;






























public abstract class HtmlInput
  extends HtmlElement
  implements DisabledElement, SubmittableElement, FormFieldWithNameHistory
{
  public static final String TAG_NAME = "input";
  private String defaultValue_;
  private String originalName_;
  private Collection<String> newNames_ = Collections.emptySet();
  

  private boolean createdByJavascript_;
  

  private Object valueAtFocus_;
  

  public HtmlInput(SgmlPage page, Map<String, DomAttr> attributes)
  {
    this("input", page, attributes);
  }
  







  public HtmlInput(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
    defaultValue_ = getValueAttribute();
    originalName_ = getNameAttribute();
  }
  



  public void setAttribute(String attributeName, String attributeValue)
  {
    if ("value".equals(attributeName)) {
      setValueAttribute(attributeValue);
    }
    else {
      super.setAttribute(attributeName, attributeValue);
    }
  }
  




  public void setValueAttribute(String newValue)
  {
    WebAssert.notNull("newValue", newValue);
    super.setAttribute("value", newValue);
  }
  



  public NameValuePair[] getSubmitNameValuePairs()
  {
    return new NameValuePair[] { new NameValuePair(getNameAttribute(), getValueAttribute()) };
  }
  






  public final String getTypeAttribute()
  {
    String type = getAttribute("type");
    if (ATTRIBUTE_NOT_DEFINED == type) {
      return "text";
    }
    return type;
  }
  






  public final String getNameAttribute()
  {
    return getAttribute("name");
  }
  






  public final String getValueAttribute()
  {
    return getAttribute("value");
  }
  






  public final String getCheckedAttribute()
  {
    return getAttribute("checked");
  }
  



  public final String getDisabledAttribute()
  {
    return getAttribute("disabled");
  }
  



  public final boolean isDisabled()
  {
    return hasAttribute("disabled");
  }
  







  public final String getReadOnlyAttribute()
  {
    return getAttribute("readonly");
  }
  







  public final String getSizeAttribute()
  {
    return getAttribute("size");
  }
  







  public final String getMaxLengthAttribute()
  {
    return getAttribute("maxLength");
  }
  



  protected int getMaxLength()
  {
    String maxLength = getMaxLengthAttribute();
    if (maxLength.isEmpty()) {
      return Integer.MAX_VALUE;
    }
    try
    {
      return Integer.parseInt(maxLength.trim());
    }
    catch (NumberFormatException e) {}
    return Integer.MAX_VALUE;
  }
  








  public final String getSrcAttribute()
  {
    return getSrcAttributeNormalized();
  }
  







  public final String getAltAttribute()
  {
    return getAttribute("alt");
  }
  







  public final String getUseMapAttribute()
  {
    return getAttribute("usemap");
  }
  







  public final String getTabIndexAttribute()
  {
    return getAttribute("tabindex");
  }
  







  public final String getAccessKeyAttribute()
  {
    return getAttribute("accesskey");
  }
  







  public final String getOnFocusAttribute()
  {
    return getAttribute("onfocus");
  }
  







  public final String getOnBlurAttribute()
  {
    return getAttribute("onblur");
  }
  







  public final String getOnSelectAttribute()
  {
    return getAttribute("onselect");
  }
  







  public final String getOnChangeAttribute()
  {
    return getAttribute("onchange");
  }
  







  public final String getAcceptAttribute()
  {
    return getAttribute("accept");
  }
  







  public final String getAlignAttribute()
  {
    return getAttribute("align");
  }
  




  public void reset()
  {
    setValueAttribute(defaultValue_);
  }
  





  public void setDefaultValue(String defaultValue)
  {
    setDefaultValue(defaultValue, true);
  }
  




  protected void setDefaultValue(String defaultValue, boolean modifyValue)
  {
    defaultValue_ = defaultValue;
    if (modifyValue) {
      if ((this instanceof HtmlFileInput)) {
        super.setAttribute("value", defaultValue);
      }
      else {
        setValueAttribute(defaultValue);
      }
    }
  }
  




  public String getDefaultValue()
  {
    return defaultValue_;
  }
  









  public void setDefaultChecked(boolean defaultChecked) {}
  








  public boolean isDefaultChecked()
  {
    return false;
  }
  








  public Page setChecked(boolean isChecked)
  {
    return getPage();
  }
  




  public void setReadOnly(boolean isReadOnly)
  {
    if (isReadOnly) {
      setAttribute("readOnly", "readOnly");
    }
    else {
      removeAttribute("readOnly");
    }
  }
  



  public boolean isChecked()
  {
    return hasAttribute("checked");
  }
  



  public boolean isReadOnly()
  {
    return hasAttribute("readOnly");
  }
  













  @Deprecated
  public <P extends Page> P click(int x, int y)
    throws IOException, ElementNotFoundException
  {
    return click();
  }
  



  protected boolean propagateClickStateUpdateToParent()
  {
    return !hasFeature(BrowserVersionFeatures.HTMLINPUT_DOES_NOT_CLICK_SURROUNDING_ANCHOR);
  }
  



  public boolean handles(Event event)
  {
    if (((event instanceof MouseEvent)) && (hasFeature(BrowserVersionFeatures.EVENT_MOUSE_ON_DISABLED))) {
      return true;
    }
    
    return super.handles(event);
  }
  








  static Page executeOnChangeHandlerIfAppropriate(HtmlElement htmlElement)
  {
    SgmlPage page = htmlElement.getPage();
    
    JavaScriptEngine engine = htmlElement.getPage().getWebClient().getJavaScriptEngine();
    if (engine.isScriptRunning()) {
      return page;
    }
    ScriptResult scriptResult = htmlElement.fireEvent("change");
    
    if (page.getWebClient().containsWebWindow(page.getEnclosingWindow()))
    {
      return page.getEnclosingWindow().getEnclosedPage();
    }
    
    if (scriptResult != null)
    {
      return scriptResult.getNewPage();
    }
    
    return page;
  }
  




  public void setAttributeNS(String namespaceURI, String qualifiedName, String attributeValue, boolean notifyAttributeChangeListeners)
  {
    if ("name".equals(qualifiedName)) {
      if (newNames_.isEmpty()) {
        newNames_ = new HashSet();
      }
      newNames_.add(attributeValue);
    }
    super.setAttributeNS(namespaceURI, qualifiedName, attributeValue, notifyAttributeChangeListeners);
  }
  



  public String getOriginalName()
  {
    return originalName_;
  }
  



  public Collection<String> getNewNames()
  {
    return newNames_;
  }
  





  public void markAsCreatedByJavascript()
  {
    createdByJavascript_ = true;
  }
  






  public boolean wasCreatedByJavascript()
  {
    return createdByJavascript_;
  }
  



  public final void focus()
  {
    super.focus();
    
    valueAtFocus_ = getInternalValue();
  }
  



  public final void removeFocus()
  {
    super.removeFocus();
    
    if (!valueAtFocus_.equals(getInternalValue())) {
      handleFocusLostValueChanged();
    }
    valueAtFocus_ = null;
  }
  
  void handleFocusLostValueChanged() {
    executeOnChangeHandlerIfAppropriate(this);
  }
  
  Object getInternalValue() {
    return getValueAttribute();
  }
  



  public HtmlElement.DisplayStyle getDefaultStyleDisplay()
  {
    if (hasFeature(BrowserVersionFeatures.CSS_INPUT_DISPLAY_INLINE_BLOCK)) {
      return HtmlElement.DisplayStyle.INLINE_BLOCK;
    }
    return HtmlElement.DisplayStyle.INLINE;
  }
  



  @JsxGetter
  public boolean isRequired()
  {
    return hasAttribute("required");
  }
  



  @JsxSetter
  public void setRequired(boolean required)
  {
    if (required) {
      setAttribute("required", "required");
    }
    else {
      removeAttribute("required");
    }
  }
  




  public String getSize()
  {
    return getAttribute("size");
  }
  




  public void setSize(String size)
  {
    setAttribute("size", size);
  }
  




  public void setMaxLength(int maxLength)
  {
    setAttribute("maxLength", String.valueOf(maxLength));
  }
  




  public void setMinLength(int minLength)
  {
    setAttribute("minLength", String.valueOf(minLength));
  }
  




  public String getAccept()
  {
    return getAttribute("accept");
  }
  




  public void setAccept(String accept)
  {
    setAttribute("accept", accept);
  }
  




  public String getAutocomplete()
  {
    return getAttribute("autocomplete");
  }
  




  public void setAutocomplete(String autocomplete)
  {
    setAttribute("autocomplete", autocomplete);
  }
  




  public String getPlaceholder()
  {
    return getAttribute("placeholder");
  }
  




  public void setPlaceholder(String placeholder)
  {
    setAttribute("placeholder", placeholder);
  }
  



  public boolean isValid()
  {
    return (!isRequiredSupported()) || (getAttribute("required") == ATTRIBUTE_NOT_DEFINED) || 
      (!getValueAttribute().isEmpty());
  }
  



  protected boolean isRequiredSupported()
  {
    return true;
  }
}
