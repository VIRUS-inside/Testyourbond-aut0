package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.SgmlPage;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;


































public class HtmlMap
  extends HtmlElement
{
  public static final String TAG_NAME = "map";
  
  HtmlMap(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
  }
  







  public final String getNameAttribute()
  {
    return getAttribute("name");
  }
  



  public HtmlElement.DisplayStyle getDefaultStyleDisplay()
  {
    return HtmlElement.DisplayStyle.INLINE;
  }
  
  public boolean isDisplayed()
  {
    HtmlImage image = findReferencingImage();
    if (image != null) {
      return image.isDisplayed();
    }
    return false;
  }
  
  private HtmlImage findReferencingImage() {
    HtmlPage page = getHtmlPageOrNull();
    String name = getNameAttribute();
    if ((page != null) && (StringUtils.isNotBlank(name))) {
      name = "#" + name.trim();
      for (HtmlElement elem : page.getDocumentElement().getElementsByTagName("img")) {
        HtmlImage image = (HtmlImage)elem;
        if (name.equals(image.getUseMapAttribute())) {
          return image;
        }
      }
    }
    return null;
  }
}
