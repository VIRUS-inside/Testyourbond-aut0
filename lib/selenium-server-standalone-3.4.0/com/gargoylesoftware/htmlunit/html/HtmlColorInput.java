package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.SgmlPage;
import java.awt.Color;
import java.util.Map;





























public class HtmlColorInput
  extends HtmlInput
{
  HtmlColorInput(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
    if ((getValueAttribute() == ATTRIBUTE_NOT_DEFINED) && (!hasFeature(BrowserVersionFeatures.JS_INPUT_SET_VALUE_MOVE_SELECTION_TO_START))) {
      setValueAttribute("#" + Integer.toHexString(Color.black.getRGB()).substring(2));
    }
  }
  



  public void setValueAttribute(String newValue)
  {
    if ((hasFeature(BrowserVersionFeatures.JS_INPUT_SET_VALUE_MOVE_SELECTION_TO_START)) || (isValid(newValue))) {
      super.setValueAttribute(newValue);
    }
  }
  
  private static boolean isValid(String value) {
    boolean valid = false;
    if ((value.length() == 7) && (value.charAt(0) == '#')) {
      try {
        new Color(
          Integer.valueOf(value.substring(1, 3), 16).intValue(), 
          Integer.valueOf(value.substring(3, 5), 16).intValue(), 
          Integer.valueOf(value.substring(5, 7), 16).intValue());
        valid = true;
      }
      catch (NumberFormatException localNumberFormatException) {}catch (IllegalArgumentException localIllegalArgumentException) {}
    }
    




    return valid;
  }
  



  protected boolean isRequiredSupported()
  {
    return false;
  }
}
