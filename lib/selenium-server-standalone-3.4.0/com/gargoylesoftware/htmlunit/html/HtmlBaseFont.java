package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.SgmlPage;
import java.util.Map;
































public class HtmlBaseFont
  extends HtmlElement
{
  public static final String TAG_NAME = "basefont";
  
  HtmlBaseFont(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
  }
  






  public final String getIdAttribute()
  {
    return getId();
  }
  






  public final String getSizeAttribute()
  {
    String size = getAttribute("size");
    if (ATTRIBUTE_NOT_DEFINED == size) {
      return "3";
    }
    return size;
  }
  






  public final String getColorAttribute()
  {
    return getAttribute("color");
  }
  






  public final String getFaceAttribute()
  {
    return getAttribute("face");
  }
  



  public HtmlElement.DisplayStyle getDefaultStyleDisplay()
  {
    if (hasFeature(BrowserVersionFeatures.CSS_DISPLAY_BLOCK)) {
      return HtmlElement.DisplayStyle.NONE;
    }
    return HtmlElement.DisplayStyle.INLINE;
  }
}
