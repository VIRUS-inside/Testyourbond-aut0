package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebClient;
import java.io.IOException;
import java.util.Map;




































public class HtmlResetInput
  extends HtmlInput
{
  private static final String DEFAULT_VALUE = "Reset";
  
  HtmlResetInput(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, addValueIfNeeded(page, attributes));
  }
  






  private static Map<String, DomAttr> addValueIfNeeded(SgmlPage page, Map<String, DomAttr> attributes)
  {
    BrowserVersion browserVersion = page.getWebClient().getBrowserVersion();
    if (browserVersion.hasFeature(BrowserVersionFeatures.RESETINPUT_DEFAULT_VALUE_IF_VALUE_NOT_DEFINED)) {
      for (String key : attributes.keySet()) {
        if ("value".equalsIgnoreCase(key)) {
          return attributes;
        }
      }
      

      DomAttr newAttr = new DomAttr(page, null, "value", "Reset", true);
      attributes.put("value", newAttr);
    }
    
    return attributes;
  }
  


  protected boolean doClickStateUpdate(boolean shiftKey, boolean ctrlKey)
    throws IOException
  {
    HtmlForm form = getEnclosingForm();
    if (form != null) {
      form.reset();
      return false;
    }
    super.doClickStateUpdate(shiftKey, ctrlKey);
    return false;
  }
  





  public void reset() {}
  





  public String asText()
  {
    String text = getValueAttribute();
    if (text == ATTRIBUTE_NOT_DEFINED) {
      text = "Reset";
    }
    return text;
  }
  




  public void setAttributeNS(String namespaceURI, String qualifiedName, String attributeValue, boolean notifyAttributeChangeListeners)
  {
    if ("value".equals(qualifiedName)) {
      setDefaultValue(attributeValue, false);
    }
    super.setAttributeNS(namespaceURI, qualifiedName, attributeValue, notifyAttributeChangeListeners);
  }
  



  protected boolean propagateClickStateUpdateToParent()
  {
    return true;
  }
  



  protected boolean isRequiredSupported()
  {
    return false;
  }
}
