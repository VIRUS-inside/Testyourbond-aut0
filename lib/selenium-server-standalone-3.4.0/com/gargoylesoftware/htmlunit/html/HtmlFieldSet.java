package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.SgmlPage;
import java.util.Map;
































public class HtmlFieldSet
  extends HtmlElement
{
  public static final String TAG_NAME = "fieldset";
  
  HtmlFieldSet(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
  }
}
