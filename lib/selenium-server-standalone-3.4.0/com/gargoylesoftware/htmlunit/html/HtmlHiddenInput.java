package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.SgmlPage;
import java.util.Map;


































public class HtmlHiddenInput
  extends HtmlInput
{
  HtmlHiddenInput(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
  }
  




  public void setAttributeNS(String namespaceURI, String qualifiedName, String attributeValue, boolean notifyAttributeChangeListeners)
  {
    if ("value".equals(qualifiedName)) {
      setDefaultValue(attributeValue, false);
    }
    super.setAttributeNS(namespaceURI, qualifiedName, attributeValue, notifyAttributeChangeListeners);
  }
  





  public String asText()
  {
    return "";
  }
  



  public boolean mayBeDisplayed()
  {
    return false;
  }
  



  protected boolean isRequiredSupported()
  {
    return false;
  }
}
