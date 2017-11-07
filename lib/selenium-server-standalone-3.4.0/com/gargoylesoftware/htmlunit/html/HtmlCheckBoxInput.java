package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import java.io.IOException;
import java.util.Map;














































public class HtmlCheckBoxInput
  extends HtmlInput
{
  private static final String DEFAULT_VALUE = "on";
  private boolean defaultCheckedState_;
  private boolean checkedState_;
  
  HtmlCheckBoxInput(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
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
  





  public void reset()
  {
    setChecked(defaultCheckedState_);
  }
  




  public boolean isChecked()
  {
    return checkedState_;
  }
  



  public Page setChecked(boolean isChecked)
  {
    checkedState_ = isChecked;
    
    return executeOnChangeHandlerIfAppropriate(this);
  }
  






  public String asText()
  {
    return super.asText();
  }
  


  protected boolean doClickStateUpdate(boolean shiftKey, boolean ctrlKey)
    throws IOException
  {
    checkedState_ = (!isChecked());
    super.doClickStateUpdate(shiftKey, ctrlKey);
    return true;
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
  





  protected boolean isStateUpdateFirst()
  {
    return true;
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
    setChecked(defaultChecked);
  }
  




  public boolean isDefaultChecked()
  {
    return defaultCheckedState_;
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
