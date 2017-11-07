package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.impl.SelectableTextInput;
import com.gargoylesoftware.htmlunit.html.impl.SelectableTextSelectionDelegate;
import java.util.Map;





















public class HtmlNumberInput
  extends HtmlInput
  implements SelectableTextInput
{
  private final SelectableTextSelectionDelegate selectionDelegate_ = new SelectableTextSelectionDelegate(this);
  
  private final DoTypeProcessor doTypeProcessor_ = new DoTypeProcessor(this);
  







  HtmlNumberInput(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
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
  



  protected boolean isSubmittableByEnter()
  {
    return true;
  }
  



  public void select()
  {
    selectionDelegate_.select();
  }
  



  public String getSelectedText()
  {
    return selectionDelegate_.getSelectedText();
  }
  



  public String getText()
  {
    return getValueAttribute();
  }
  



  public void setText(String text)
  {
    setValueAttribute(text);
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
  




  public void setAttributeNS(String namespaceURI, String qualifiedName, String attributeValue, boolean notifyAttributeChangeListeners)
  {
    super.setAttributeNS(namespaceURI, qualifiedName, attributeValue, notifyAttributeChangeListeners);
    if ("value".equals(qualifiedName)) {
      SgmlPage page = getPage();
      if ((page != null) && (page.isHtmlPage())) {
        int pos = 0;
        if (!hasFeature(BrowserVersionFeatures.JS_INPUT_SET_VALUE_MOVE_SELECTION_TO_START)) {
          pos = attributeValue.length();
        }
        setSelectionStart(pos);
        setSelectionEnd(pos);
      }
    }
  }
  


  protected Object clone()
    throws CloneNotSupportedException
  {
    return new HtmlNumberInput(getQualifiedName(), getPage(), getAttributesMap());
  }
  



  public void setDefaultValue(String defaultValue)
  {
    boolean modifyValue = getValueAttribute().equals(getDefaultValue());
    setDefaultValue(defaultValue, modifyValue);
  }
  


  public void setValueAttribute(String newValue)
  {
    try
    {
      if (!newValue.isEmpty()) {
        Long.parseLong(newValue);
      }
      super.setValueAttribute(newValue);
    }
    catch (NumberFormatException localNumberFormatException) {}
  }
}
