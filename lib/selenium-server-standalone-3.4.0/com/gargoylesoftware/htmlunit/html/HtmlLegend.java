package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.SgmlPage;
import java.util.Map;


































public class HtmlLegend
  extends HtmlElement
{
  public static final String TAG_NAME = "legend";
  
  HtmlLegend(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
  }
  







  public final String getAccessKeyAttribute()
  {
    return getAttribute("accesskey");
  }
  







  public final String getAlignAttribute()
  {
    return getAttribute("align");
  }
  



  public HtmlElement.DisplayStyle getDefaultStyleDisplay()
  {
    if (hasFeature(BrowserVersionFeatures.CSS_DISPLAY_BLOCK2)) {
      return HtmlElement.DisplayStyle.BLOCK;
    }
    return HtmlElement.DisplayStyle.INLINE;
  }
}
