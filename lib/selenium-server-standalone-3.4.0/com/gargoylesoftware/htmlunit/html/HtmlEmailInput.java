package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.impl.SelectableTextInput;
import com.gargoylesoftware.htmlunit.html.impl.SelectableTextSelectionDelegate;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;





















public class HtmlEmailInput
  extends HtmlInput
  implements SelectableTextInput
{
  private final SelectableTextSelectionDelegate selectionDelegate_ = new SelectableTextSelectionDelegate(this);
  
  private final DoTypeProcessor doTypeProcessor_ = new DoTypeProcessor(this);
  







  HtmlEmailInput(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
  }
  



  public void setValueAttribute(String newValue)
  {
    if ((StringUtils.isBlank(newValue)) && (hasFeature(BrowserVersionFeatures.JS_INPUT_SET_VALUE_EMAIL_TRIMMED))) {
      newValue = "";
    }
    super.setValueAttribute(newValue);
  }
  



  public int getSelectionStart()
  {
    return selectionDelegate_.getSelectionStart();
  }
  



  public void setSelectionStart(int selectionStart)
  {
    selectionDelegate_.setSelectionStart(selectionStart);
  }
  



  public int getSelectionEnd()
  {
    return selectionDelegate_.getSelectionEnd();
  }
  



  public void setSelectionEnd(int selectionEnd)
  {
    selectionDelegate_.setSelectionEnd(selectionEnd);
  }
  



  public String getSelectedText()
  {
    return selectionDelegate_.getSelectedText();
  }
  



  public void select()
  {
    selectionDelegate_.select();
  }
  



  public void setText(String text)
  {
    setValueAttribute(text);
  }
  



  public String getText()
  {
    return getValueAttribute();
  }
  



  protected void doType(char c, boolean startAtEnd, boolean lastType)
  {
    if (startAtEnd) {
      selectionDelegate_.setSelectionStart(getValueAttribute().length());
    }
    doTypeProcessor_.doType(getValueAttribute(), selectionDelegate_, c, this, lastType);
  }
  



  protected void doType(int keyCode, boolean startAtEnd, boolean lastType)
  {
    if (startAtEnd) {
      selectionDelegate_.setSelectionStart(getValueAttribute().length());
    }
    doTypeProcessor_.doType(getValueAttribute(), selectionDelegate_, keyCode, this, lastType);
  }
  



  protected void typeDone(String newValue, boolean notifyAttributeChangeListeners)
  {
    if (newValue.length() <= getMaxLength()) {
      setAttributeNS(null, "value", newValue, notifyAttributeChangeListeners);
    }
  }
}
