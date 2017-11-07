package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.SgmlPage;
import java.util.Map;

































@Deprecated
public class HtmlKeygen
  extends HtmlElement
{
  public static final String TAG_NAME = "keygen";
  private boolean createdByJavascript_;
  
  HtmlKeygen(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
  }
  





  public void markAsCreatedByJavascript()
  {
    createdByJavascript_ = true;
  }
  






  public boolean wasCreatedByJavascript()
  {
    return createdByJavascript_;
  }
  



  public HtmlElement.DisplayStyle getDefaultStyleDisplay()
  {
    if (hasFeature(BrowserVersionFeatures.CSS_KEYGEN_DISPLAY_INLINE_ALWAYS)) {
      return HtmlElement.DisplayStyle.INLINE;
    }
    if (wasCreatedByJavascript()) {
      if (getParentNode() == null) {
        if (hasFeature(BrowserVersionFeatures.CSS_KEYGEN_DISPLAY_INLINE_JS)) {
          return HtmlElement.DisplayStyle.BLOCK;
        }
        return HtmlElement.DisplayStyle.EMPTY;
      }
      if (hasFeature(BrowserVersionFeatures.CSS_KEYGEN_DISPLAY_INLINE_JS)) {
        return HtmlElement.DisplayStyle.INLINE;
      }
    }
    return HtmlElement.DisplayStyle.INLINE_BLOCK;
  }
}
