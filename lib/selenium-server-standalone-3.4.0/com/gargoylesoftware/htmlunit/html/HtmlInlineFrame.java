package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.SgmlPage;
import java.util.Map;

































public class HtmlInlineFrame
  extends BaseFrameElement
{
  public static final String TAG_NAME = "iframe";
  
  HtmlInlineFrame(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
  }
  





  protected boolean isEmptyXmlTagExpanded()
  {
    return true;
  }
  



  public HtmlElement.DisplayStyle getDefaultStyleDisplay()
  {
    return HtmlElement.DisplayStyle.INLINE;
  }
}
