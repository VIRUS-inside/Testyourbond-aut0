package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.SgmlPage;
import java.util.Map;































public class HtmlRt
  extends HtmlElement
{
  public static final String TAG_NAME = "rt";
  private boolean createdByJavascript_;
  
  HtmlRt(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
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
    if (hasFeature(BrowserVersionFeatures.CSS_RT_DISPLAY_RUBY_TEXT_ALWAYS)) {
      return HtmlElement.DisplayStyle.RUBY_TEXT;
    }
    if (wasCreatedByJavascript()) {
      if (getParentNode() == null) {
        return HtmlElement.DisplayStyle.EMPTY;
      }
    }
    else {
      return HtmlElement.DisplayStyle.BLOCK;
    }
    return HtmlElement.DisplayStyle.INLINE;
  }
}
