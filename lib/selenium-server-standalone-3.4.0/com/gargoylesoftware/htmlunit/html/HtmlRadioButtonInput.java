package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import java.io.IOException;
import java.util.List;
import java.util.Map;















































public class HtmlRadioButtonInput
  extends HtmlInput
{
  private static final String DEFAULT_VALUE = "on";
  private boolean defaultCheckedState_;
  private boolean checkedState_;
  
  HtmlRadioButtonInput(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, addValueIfNeeded(page, attributes));
    

    if (getAttribute("value") == "on") {
      setDefaultValue(ATTRIBUTE_NOT_DEFINED, false);
    }
    
    defaultCheckedState_ = hasAttribute("checked");
    checkedState_ = defaultCheckedState_;
  }
  






  private static Map<String, DomAttr> addValueIfNeeded(SgmlPage page, Map<String, DomAttr> attributes)
  {
    for (String key : attributes.keySet()) {
      if ("value".equalsIgnoreCase(key)) {
        return attributes;
      }
    }
    

    DomAttr newAttr = new DomAttr(page, null, "value", "on", true);
    attributes.put("value", newAttr);
    
    return attributes;
  }
  




  public boolean isChecked()
  {
    return checkedState_;
  }
  




  public void reset()
  {
    setChecked(defaultCheckedState_);
  }
  
  void setCheckedInternal(boolean isChecked) {
    checkedState_ = isChecked;
  }
  







  public Page setChecked(boolean isChecked)
  {
    Page page = getPage();
    
    boolean changed = isChecked() ^ isChecked;
    checkedState_ = isChecked;
    if (isChecked) {
      HtmlForm form = getEnclosingForm();
      if (form != null) {
        form.setCheckedRadioButton(this);
      }
      else if ((page != null) && (page.isHtmlPage())) {
        setCheckedForPage((HtmlPage)page);
      }
    }
    
    if (changed) {
      ScriptResult scriptResult = fireEvent("change");
      if (scriptResult != null) {
        page = scriptResult.getNewPage();
      }
    }
    return page;
  }
  






  protected boolean doClickStateUpdate(boolean shiftKey, boolean ctrlKey)
    throws IOException
  {
    HtmlForm form = getEnclosingForm();
    boolean changed = !isChecked();
    
    Page page = getPage();
    if (form != null) {
      form.setCheckedRadioButton(this);
    }
    else if ((page != null) && (page.isHtmlPage())) {
      setCheckedForPage((HtmlPage)page);
    }
    super.doClickStateUpdate(shiftKey, ctrlKey);
    return changed;
  }
  





  private void setCheckedForPage(HtmlPage htmlPage)
  {
    List<HtmlRadioButtonInput> pageInputs = 
      htmlPage.getByXPath("//input[lower-case(@type)='radio' and @name='" + 
      getNameAttribute() + "']");
    List<HtmlRadioButtonInput> formInputs = 
      htmlPage.getByXPath("//form//input[lower-case(@type)='radio' and @name='" + 
      getNameAttribute() + "']");
    
    pageInputs.removeAll(formInputs);
    
    boolean foundInPage = false;
    for (HtmlRadioButtonInput input : pageInputs) {
      if (input == this) {
        setCheckedInternal(true);
        foundInPage = true;
      }
      else {
        input.setCheckedInternal(false);
      }
    }
    
    if ((!foundInPage) && (!formInputs.contains(this))) {
      setCheckedInternal(true);
    }
  }
  



  protected ScriptResult doClickFireClickEvent(Event event)
  {
    if (!hasFeature(BrowserVersionFeatures.EVENT_ONCHANGE_AFTER_ONCLICK)) {
      executeOnChangeHandlerIfAppropriate(this);
    }
    
    return super.doClickFireClickEvent(event);
  }
  



  protected void doClickFireChangeEvent()
  {
    if (hasFeature(BrowserVersionFeatures.EVENT_ONCHANGE_AFTER_ONCLICK)) {
      executeOnChangeHandlerIfAppropriate(this);
    }
  }
  






  public String asText()
  {
    return super.asText();
  }
  



  protected void preventDefault()
  {
    checkedState_ = (!checkedState_);
  }
  





  public void setDefaultValue(String defaultValue)
  {
    super.setDefaultValue(defaultValue);
    setValueAttribute(defaultValue);
  }
  




  public void setDefaultChecked(boolean defaultChecked)
  {
    defaultCheckedState_ = defaultChecked;
    setChecked(isDefaultChecked());
  }
  




  public boolean isDefaultChecked()
  {
    return defaultCheckedState_;
  }
  



  protected boolean isStateUpdateFirst()
  {
    return true;
  }
  



  protected void onAddedToPage()
  {
    super.onAddedToPage();
    setChecked(isChecked());
  }
  
  Object getInternalValue()
  {
    return Boolean.valueOf(isChecked());
  }
  



  void handleFocusLostValueChanged() {}
  



  public void setAttributeNS(String namespaceURI, String qualifiedName, String attributeValue, boolean notifyAttributeChangeListeners)
  {
    if ("value".equals(qualifiedName)) {
      setDefaultValue(attributeValue, false);
    }
    if ("checked".equals(qualifiedName)) {
      checkedState_ = true;
    }
    super.setAttributeNS(namespaceURI, qualifiedName, attributeValue, notifyAttributeChangeListeners);
  }
  



  protected boolean propagateClickStateUpdateToParent()
  {
    return (!hasFeature(BrowserVersionFeatures.HTMLINPUT_CHECKBOX_DOES_NOT_CLICK_SURROUNDING_ANCHOR)) && 
      (super.propagateClickStateUpdateToParent());
  }
}
