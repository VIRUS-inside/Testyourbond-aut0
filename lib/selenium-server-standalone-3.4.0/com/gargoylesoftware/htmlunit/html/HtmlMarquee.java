package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.SgmlPage;
import java.util.Map;






























public class HtmlMarquee
  extends HtmlElement
{
  public static final String TAG_NAME = "marquee";
  
  HtmlMarquee(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
  }
  



  public HtmlElement.DisplayStyle getDefaultStyleDisplay()
  {
    if (hasFeature(BrowserVersionFeatures.CSS_DISPLAY_BLOCK2)) {
      return HtmlElement.DisplayStyle.INLINE_BLOCK;
    }
    return HtmlElement.DisplayStyle.BLOCK;
  }
}
