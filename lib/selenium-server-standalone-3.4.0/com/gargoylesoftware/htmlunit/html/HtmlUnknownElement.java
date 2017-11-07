package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.SgmlPage;
import java.util.Map;




































public class HtmlUnknownElement
  extends HtmlElement
{
  private boolean createdByJavascript_;
  
  HtmlUnknownElement(SgmlPage page, String tagName, Map<String, DomAttr> attributes)
  {
    super(tagName, page, attributes);
  }
  



  protected boolean isTrimmedText()
  {
    return false;
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
    String str;
    
    switch ((str = getTagName()).hashCode()) {case 3646:  if (str.equals("rp")) {} break; case 3650:  if (str.equals("rt")) {} break; case 3511770:  if (str.equals("ruby")) break; break; case 653817255:  if (!str.equals("multicol")) {
        break label196;
        if (hasFeature(BrowserVersionFeatures.CSS_RUBY_DISPLAY_INLINE)) {
          return HtmlElement.DisplayStyle.INLINE;
        }
        return HtmlElement.DisplayStyle.RUBY;
        
        if (hasFeature(BrowserVersionFeatures.CSS_RP_DISPLAY_NONE)) {
          return HtmlElement.DisplayStyle.NONE;
        }
        if ((wasCreatedByJavascript()) && (getParentNode() == null)) {
          return HtmlElement.DisplayStyle.BLOCK;
          


          if ((wasCreatedByJavascript()) && (getParentNode() == null)) {
            return HtmlElement.DisplayStyle.BLOCK;
          }
          if (hasFeature(BrowserVersionFeatures.CSS_RT_DISPLAY_RUBY_TEXT_ALWAYS)) {
            return HtmlElement.DisplayStyle.RUBY_TEXT;
          }
        }
      }
      else if (hasFeature(BrowserVersionFeatures.MULTICOL_BLOCK)) {
        return HtmlElement.DisplayStyle.BLOCK;
      }
      break;
    }
    label196:
    return HtmlElement.DisplayStyle.INLINE;
  }
  




  protected boolean isEmptyXmlTagExpanded()
  {
    return true;
  }
}
