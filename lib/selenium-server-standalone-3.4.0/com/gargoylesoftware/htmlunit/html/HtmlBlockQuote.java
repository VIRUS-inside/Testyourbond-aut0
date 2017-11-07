package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.SgmlPage;
import java.util.Map;
































public class HtmlBlockQuote
  extends HtmlElement
{
  public static final String TAG_NAME = "blockquote";
  
  HtmlBlockQuote(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
  }
  






  public final String getCiteAttribute()
  {
    return getAttribute("cite");
  }
}
