package com.gargoylesoftware.htmlunit.svg;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.DomAttr;
import java.util.Map;





























public class SvgTSpan
  extends SvgElement
{
  public static final String TAG_NAME = "tspan";
  
  SvgTSpan(String namespaceURI, String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(namespaceURI, qualifiedName, page, attributes);
  }
}
