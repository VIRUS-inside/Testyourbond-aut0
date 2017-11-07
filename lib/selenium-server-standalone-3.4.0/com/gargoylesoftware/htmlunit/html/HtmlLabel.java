package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import java.io.IOException;
import java.util.Map;


































public class HtmlLabel
  extends HtmlElement
{
  public static final String TAG_NAME = "label";
  
  HtmlLabel(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
  }
  







  public final String getForAttribute()
  {
    return getAttribute("for");
  }
  







  public final String getAccessKeyAttribute()
  {
    return getAttribute("accesskey");
  }
  







  public final String getOnFocusAttribute()
  {
    return getAttribute("onfocus");
  }
  







  public final String getOnBlurAttribute()
  {
    return getAttribute("onblur");
  }
  



  public void blur()
  {
    HtmlElement element = getReferencedElement();
    if (element != null) {
      element.blur();
    }
  }
  



  public void focus()
  {
    HtmlElement element = getReferencedElement();
    if (element != null) {
      element.focus();
    }
  }
  




  public HtmlElement getReferencedElement()
  {
    String elementId = getForAttribute();
    if (!ATTRIBUTE_NOT_DEFINED.equals(elementId)) {
      try {
        return ((HtmlPage)getPage()).getHtmlElementById(elementId);
      }
      catch (ElementNotFoundException e) {
        return null;
      }
    }
    for (DomNode element : getChildren()) {
      if ((element instanceof HtmlInput)) {
        return (HtmlInput)element;
      }
    }
    return null;
  }
  




  public <P extends Page> P click(Event event, boolean ignoreVisibility)
    throws IOException
  {
    P page = super.click(event, ignoreVisibility);
    




    HtmlElement element = getReferencedElement();
    P response; P response; if (element != null) {
      response = element.click(false, false, false, false);
    }
    else {
      response = page;
    }
    
    return response;
  }
  



  public HtmlElement.DisplayStyle getDefaultStyleDisplay()
  {
    return HtmlElement.DisplayStyle.INLINE;
  }
}
