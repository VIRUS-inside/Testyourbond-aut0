package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.SgmlPage;
import java.util.Map;






























public class HtmlNoEmbed
  extends HtmlElement
{
  public static final String TAG_NAME = "noembed";
  
  HtmlNoEmbed(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
  }
  



  public HtmlElement.DisplayStyle getDefaultStyleDisplay()
  {
    if (hasFeature(BrowserVersionFeatures.CSS_NOEMBED_INLINE)) {
      return HtmlElement.DisplayStyle.INLINE;
    }
    return HtmlElement.DisplayStyle.NONE;
  }
}
