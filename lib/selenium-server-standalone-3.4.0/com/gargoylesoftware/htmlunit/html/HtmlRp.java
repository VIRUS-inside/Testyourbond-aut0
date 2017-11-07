package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.SgmlPage;
import java.util.Map;































public class HtmlRp
  extends HtmlElement
{
  public static final String TAG_NAME = "rp";
  private boolean createdByJavascript_;
  
  HtmlRp(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
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
      return HtmlElement.DisplayStyle.INLINE;
    }
    if (wasCreatedByJavascript()) {
      if (getParentNode() == null) {
        return HtmlElement.DisplayStyle.EMPTY;
      }
    }
    else {
      return HtmlElement.DisplayStyle.NONE;
    }
    return HtmlElement.DisplayStyle.INLINE;
  }
}
