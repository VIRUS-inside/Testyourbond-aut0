package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import java.util.Map;


























public class HtmlIsIndex
  extends HtmlElement
  implements SubmittableElement
{
  public static final String TAG_NAME = "isindex";
  private String value_ = "";
  







  HtmlIsIndex(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
  }
  




  public void setValue(String newValue)
  {
    WebAssert.notNull("newValue", newValue);
    value_ = newValue;
  }
  




  public String getValue()
  {
    return value_;
  }
  



  public NameValuePair[] getSubmitNameValuePairs()
  {
    return new NameValuePair[] { new NameValuePair(getPromptAttribute(), getValue()) };
  }
  




  public void reset()
  {
    value_ = "";
  }
  






  public void setDefaultValue(String defaultValue) {}
  






  public String getDefaultValue()
  {
    return "";
  }
  









  public void setDefaultChecked(boolean defaultChecked) {}
  








  public boolean isDefaultChecked()
  {
    return false;
  }
  






  public final String getPromptAttribute()
  {
    return getAttribute("prompt");
  }
  



  public HtmlElement.DisplayStyle getDefaultStyleDisplay()
  {
    return HtmlElement.DisplayStyle.INLINE;
  }
}
