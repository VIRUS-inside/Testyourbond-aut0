package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.SgmlPage;
import java.util.Map;


































public class HtmlFrame
  extends BaseFrameElement
{
  public static final String TAG_NAME = "frame";
  
  HtmlFrame(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
  }
  



  public HtmlElement.DisplayStyle getDefaultStyleDisplay()
  {
    if (hasFeature(BrowserVersionFeatures.CSS_DISPLAY_BLOCK)) {
      return HtmlElement.DisplayStyle.INLINE;
    }
    return HtmlElement.DisplayStyle.BLOCK;
  }
}
