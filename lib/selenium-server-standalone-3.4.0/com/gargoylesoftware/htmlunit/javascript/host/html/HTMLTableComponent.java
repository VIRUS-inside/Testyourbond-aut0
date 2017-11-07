package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;





















@JsxClass(isJSObject=false)
public class HTMLTableComponent
  extends HTMLElement
{
  private static final String[] VALIGN_VALID_VALUES_IE = { "top", "bottom", "middle", "baseline" };
  
  private static final String VALIGN_DEFAULT_VALUE = "top";
  

  public HTMLTableComponent() {}
  

  @JsxGetter
  public String getAlign()
  {
    boolean invalidValues = getBrowserVersion().hasFeature(BrowserVersionFeatures.HTMLELEMENT_ALIGN_INVALID);
    return getAlign(invalidValues);
  }
  



  @JsxSetter
  public void setAlign(String align)
  {
    setAlign(align, false);
  }
  



  @JsxGetter
  public String getVAlign()
  {
    return getVAlign(getValidVAlignValues(), "top");
  }
  



  @JsxSetter
  public void setVAlign(Object vAlign)
  {
    setVAlign(vAlign, getValidVAlignValues());
  }
  

  private String[] getValidVAlignValues()
  {
    String[] valid;
    
    String[] valid;
    if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_TABLE_VALIGN_SUPPORTS_IE_VALUES)) {
      valid = VALIGN_VALID_VALUES_IE;
    }
    else {
      valid = null;
    }
    return valid;
  }
  




  @JsxGetter
  public String getCh()
  {
    return super.getCh();
  }
  




  @JsxSetter
  public void setCh(String ch)
  {
    super.setCh(ch);
  }
  




  @JsxGetter
  public String getChOff()
  {
    return super.getChOff();
  }
  




  @JsxSetter
  public void setChOff(String chOff)
  {
    super.setChOff(chOff);
  }
}
