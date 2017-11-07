package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.SgmlPage;
import java.util.Map;































public class HtmlProgress
  extends HtmlElement
{
  public static final String TAG_NAME = "progress";
  
  HtmlProgress(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
  }
  



  public HtmlElement.DisplayStyle getDefaultStyleDisplay()
  {
    if (hasFeature(BrowserVersionFeatures.CSS_PROGRESS_DISPLAY_INLINE)) {
      return HtmlElement.DisplayStyle.INLINE;
    }
    return HtmlElement.DisplayStyle.INLINE_BLOCK;
  }
}
