package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import java.util.Map;
































public class HtmlNoScript
  extends HtmlElement
{
  public static final String TAG_NAME = "noscript";
  
  HtmlNoScript(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
  }
  



  public HtmlElement.DisplayStyle getDefaultStyleDisplay()
  {
    if (!getPage().getWebClient().getOptions().isJavaScriptEnabled()) {
      return HtmlElement.DisplayStyle.BLOCK;
    }
    if (hasFeature(BrowserVersionFeatures.CSS_NOSCRIPT_DISPLAY_INLINE)) {
      return HtmlElement.DisplayStyle.INLINE;
    }
    return HtmlElement.DisplayStyle.NONE;
  }
  





  protected boolean isEmptyXmlTagExpanded()
  {
    return true;
  }
}
