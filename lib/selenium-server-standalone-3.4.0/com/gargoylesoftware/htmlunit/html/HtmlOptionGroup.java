package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.SgmlPage;
import java.util.Map;



































public class HtmlOptionGroup
  extends HtmlElement
  implements DisabledElement
{
  public static final String TAG_NAME = "optgroup";
  
  HtmlOptionGroup(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
  }
  








  public final boolean isDisabled()
  {
    return hasAttribute("disabled");
  }
  



  public final String getDisabledAttribute()
  {
    return getAttribute("disabled");
  }
  






  public final String getLabelAttribute()
  {
    return getAttribute("label");
  }
  






  public final void setLabelAttribute(String newLabel)
  {
    setAttribute("label", newLabel);
  }
  



  public HtmlSelect getEnclosingSelect()
  {
    return (HtmlSelect)getEnclosingElement("select");
  }
  



  public HtmlElement.DisplayStyle getDefaultStyleDisplay()
  {
    if (hasFeature(BrowserVersionFeatures.CSS_DISPLAY_BLOCK2)) {
      return HtmlElement.DisplayStyle.BLOCK;
    }
    return HtmlElement.DisplayStyle.INLINE;
  }
}
