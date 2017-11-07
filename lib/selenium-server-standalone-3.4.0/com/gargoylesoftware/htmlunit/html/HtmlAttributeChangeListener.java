package com.gargoylesoftware.htmlunit.html;

import java.io.Serializable;

public abstract interface HtmlAttributeChangeListener
  extends Serializable
{
  public abstract void attributeAdded(HtmlAttributeChangeEvent paramHtmlAttributeChangeEvent);
  
  public abstract void attributeRemoved(HtmlAttributeChangeEvent paramHtmlAttributeChangeEvent);
  
  public abstract void attributeReplaced(HtmlAttributeChangeEvent paramHtmlAttributeChangeEvent);
}
