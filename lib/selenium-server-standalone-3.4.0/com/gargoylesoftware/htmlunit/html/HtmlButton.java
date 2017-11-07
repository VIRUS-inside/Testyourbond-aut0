package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.javascript.host.event.MouseEvent;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;






























public class HtmlButton
  extends HtmlElement
  implements DisabledElement, SubmittableElement, FormFieldWithNameHistory
{
  private static final Log LOG = LogFactory.getLog(HtmlButton.class);
  
  public static final String TAG_NAME = "button";
  
  private String originalName_;
  private Collection<String> newNames_ = Collections.emptySet();
  







  HtmlButton(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
    originalName_ = getNameAttribute();
  }
  




  public void setValueAttribute(String newValue)
  {
    setAttribute("value", newValue);
  }
  


  protected boolean doClickStateUpdate(boolean shiftKey, boolean ctrlKey)
    throws IOException
  {
    String type = getTypeAttribute().toLowerCase(Locale.ROOT);
    
    HtmlForm form = null;
    String formId = getAttribute("form");
    if (DomElement.ATTRIBUTE_NOT_DEFINED == formId) {
      form = getEnclosingForm();

    }
    else if (hasFeature(BrowserVersionFeatures.FORM_FORM_ATTRIBUTE_SUPPORTED)) {
      DomElement elem = getHtmlPageOrNull().getElementById(formId);
      if ((elem instanceof HtmlForm)) {
        form = (HtmlForm)elem;
      }
    }
    

    if (form != null) {
      if ("button".equals(type)) {
        return false;
      }
      
      if ("submit".equals(type)) {
        form.submit(this);
        return false;
      }
      
      if ("reset".equals(type)) {
        form.reset();
        return false;
      }
      
      form.submit(this);
      return false;
    }
    
    super.doClickStateUpdate(shiftKey, ctrlKey);
    return false;
  }
  



  public final boolean isDisabled()
  {
    return hasAttribute("disabled");
  }
  



  public NameValuePair[] getSubmitNameValuePairs()
  {
    return new NameValuePair[] { new NameValuePair(getNameAttribute(), getValueAttribute()) };
  }
  





  public void reset()
  {
    if (LOG.isDebugEnabled()) {
      LOG.debug("reset() not implemented for this element");
    }
  }
  





  public void setDefaultValue(String defaultValue)
  {
    if (LOG.isDebugEnabled()) {
      LOG.debug("setDefaultValue() not implemented for this element");
    }
  }
  





  public String getDefaultValue()
  {
    if (LOG.isDebugEnabled()) {
      LOG.debug("getDefaultValue() not implemented for this element");
    }
    return "";
  }
  












  public void setDefaultChecked(boolean defaultChecked) {}
  











  public boolean isDefaultChecked()
  {
    return false;
  }
  



  public boolean handles(Event event)
  {
    if (((event instanceof MouseEvent)) && (hasFeature(BrowserVersionFeatures.EVENT_MOUSE_ON_DISABLED))) {
      return true;
    }
    
    return super.handles(event);
  }
  






  public final String getNameAttribute()
  {
    return getAttribute("name");
  }
  






  public final String getValueAttribute()
  {
    return getAttribute("value");
  }
  








  public String getAttribute(String attributeName)
  {
    String type = super.getAttribute(attributeName);
    
    if ((type == DomElement.ATTRIBUTE_NOT_DEFINED) && ("type".equalsIgnoreCase(attributeName))) {
      type = "submit";
    }
    return type;
  }
  






  public final String getTypeAttribute()
  {
    return getAttribute("type");
  }
  







  public final String getDisabledAttribute()
  {
    return getAttribute("disabled");
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
  



  public HtmlElement.DisplayStyle getDefaultStyleDisplay()
  {
    return HtmlElement.DisplayStyle.INLINE_BLOCK;
  }
  




  protected boolean isEmptyXmlTagExpanded()
  {
    return true;
  }
}
