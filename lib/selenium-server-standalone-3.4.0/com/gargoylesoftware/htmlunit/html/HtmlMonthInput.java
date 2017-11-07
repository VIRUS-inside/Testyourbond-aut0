package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.SgmlPage;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;





















public class HtmlMonthInput
  extends HtmlInput
{
  private static DateTimeFormatter FORMATTER_ = DateTimeFormatter.ofPattern("yyyy-MM");
  







  HtmlMonthInput(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
  }
  


  public void setValueAttribute(String newValue)
  {
    try
    {
      if (hasFeature(BrowserVersionFeatures.JS_INPUT_SET_VALUE_DATE_SUPPORTED)) {
        FORMATTER_.parse(newValue);
      }
      super.setValueAttribute(newValue);
    }
    catch (DateTimeParseException localDateTimeParseException) {}
  }
}
