package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.SgmlPage;
import java.util.Map;



























public class HtmlRangeInput
  extends HtmlInput
{
  HtmlRangeInput(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
    setValueAttribute("50");
  }
  


  public void setValueAttribute(String newValue)
  {
    try
    {
      int value = Integer.parseInt(newValue);
      if ((value >= 0) && (value <= 100)) {
        super.setValueAttribute(newValue);
      }
    }
    catch (NumberFormatException localNumberFormatException) {}
  }
  





  protected boolean isRequiredSupported()
  {
    return false;
  }
}
