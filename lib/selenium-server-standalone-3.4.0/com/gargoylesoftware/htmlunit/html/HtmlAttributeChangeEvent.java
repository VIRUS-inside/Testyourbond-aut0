package com.gargoylesoftware.htmlunit.html;

import java.util.EventObject;



























public class HtmlAttributeChangeEvent
  extends EventObject
{
  private final String name_;
  private final String value_;
  
  public HtmlAttributeChangeEvent(HtmlElement element, String name, String value)
  {
    super(element);
    name_ = name;
    value_ = value;
  }
  



  public HtmlElement getHtmlElement()
  {
    return (HtmlElement)getSource();
  }
  



  public String getName()
  {
    return name_;
  }
  







  public String getValue()
  {
    return value_;
  }
}
