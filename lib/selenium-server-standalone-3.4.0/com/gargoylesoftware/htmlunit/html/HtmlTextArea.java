package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.impl.SelectableTextInput;
import com.gargoylesoftware.htmlunit.html.impl.SelectableTextSelectionDelegate;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.javascript.host.event.MouseEvent;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import org.apache.commons.lang3.StringEscapeUtils;






































public class HtmlTextArea
  extends HtmlElement
  implements DisabledElement, SubmittableElement, SelectableTextInput, FormFieldWithNameHistory
{
  public static final String TAG_NAME = "textarea";
  private String defaultValue_;
  private String valueAtFocus_;
  private String originalName_;
  private Collection<String> newNames_ = Collections.emptySet();
  
  private final SelectableTextSelectionDelegate selectionDelegate_ = new SelectableTextSelectionDelegate(this);
  
  private final DoTypeProcessor doTypeProcessor_ = new DoTypeProcessor(this);
  







  HtmlTextArea(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
    originalName_ = getNameAttribute();
  }
  




  private void initDefaultValue()
  {
    if (defaultValue_ == null) {
      defaultValue_ = readValue();
    }
  }
  



  public boolean handles(Event event)
  {
    if (((event instanceof MouseEvent)) && (hasFeature(BrowserVersionFeatures.EVENT_MOUSE_ON_DISABLED))) {
      return true;
    }
    
    return super.handles(event);
  }
  





  public final String getText()
  {
    if (hasFeature(BrowserVersionFeatures.HTMLTEXTAREA_USE_ALL_TEXT_CHILDREN)) {
      return readValueIE();
    }
    return readValue();
  }
  
  private String readValue() {
    StringBuilder builder = new StringBuilder();
    for (DomNode node : getChildren()) {
      if ((node instanceof DomText)) {
        builder.append(((DomText)node).getData());
      }
    }
    
    if ((builder.length() != 0) && (builder.charAt(0) == '\n')) {
      builder.deleteCharAt(0);
    }
    return builder.toString();
  }
  
  private String readValueIE() {
    StringBuilder builder = new StringBuilder();
    for (DomNode node : getDescendants()) {
      if ((node instanceof DomText)) {
        builder.append(((DomText)node).getData());
      }
    }
    
    if ((builder.length() != 0) && (builder.charAt(0) == '\n')) {
      builder.deleteCharAt(0);
    }
    return builder.toString();
  }
  








  public final void setText(String newValue)
  {
    setTextInternal(newValue);
    
    HtmlInput.executeOnChangeHandlerIfAppropriate(this);
  }
  
  private void setTextInternal(String newValue) {
    initDefaultValue();
    DomNode child = getFirstChild();
    if (child == null) {
      DomText newChild = new DomText(getPage(), newValue);
      appendChild(newChild);

    }
    else if (hasFeature(BrowserVersionFeatures.HTMLTEXTAREA_USE_ALL_TEXT_CHILDREN)) {
      removeAllChildren();
      DomText newChild = new DomText(getPage(), newValue);
      appendChild(newChild);
    }
    else {
      DomNode next = child.getNextSibling();
      while ((next != null) && (!(next instanceof DomText))) {
        child = next;
        next = child.getNextSibling();
      }
      
      if (next == null) {
        removeChild(child);
        DomText newChild = new DomText(getPage(), newValue);
        appendChild(newChild);
      }
      else {
        ((DomText)next).setData(newValue);
      }
    }
    

    int pos = 0;
    if (!hasFeature(BrowserVersionFeatures.JS_INPUT_SET_VALUE_MOVE_SELECTION_TO_START)) {
      pos = newValue.length();
    }
    setSelectionStart(pos);
    setSelectionEnd(pos);
  }
  



  public NameValuePair[] getSubmitNameValuePairs()
  {
    String text = getText();
    text = text.replace("\r\n", "\n").replace("\n", "\r\n");
    
    return new NameValuePair[] { new NameValuePair(getNameAttribute(), text) };
  }
  




  public void reset()
  {
    initDefaultValue();
    setText(defaultValue_);
  }
  




  public void setDefaultValue(String defaultValue)
  {
    initDefaultValue();
    if (defaultValue == null) {
      defaultValue = "";
    }
    

    if ((hasFeature(BrowserVersionFeatures.HTMLTEXTAREA_SET_DEFAULT_VALUE_UPDATES_VALUE)) && 
      (getText().equals(getDefaultValue()))) {
      setTextInternal(defaultValue);
    }
    defaultValue_ = defaultValue;
  }
  




  public String getDefaultValue()
  {
    initDefaultValue();
    return defaultValue_;
  }
  









  public void setDefaultChecked(boolean defaultChecked) {}
  








  public boolean isDefaultChecked()
  {
    return false;
  }
  






  public final String getNameAttribute()
  {
    return getAttribute("name");
  }
  






  public final String getRowsAttribute()
  {
    return getAttribute("rows");
  }
  






  public final String getColumnsAttribute()
  {
    return getAttribute("cols");
  }
  



  public final boolean isDisabled()
  {
    return hasAttribute("disabled");
  }
  



  public final String getDisabledAttribute()
  {
    return getAttribute("disabled");
  }
  






  public final String getReadOnlyAttribute()
  {
    return getAttribute("readonly");
  }
  






  public final String getTabIndexAttribute()
  {
    return getAttribute("tabindex");
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
  






  public final String getOnSelectAttribute()
  {
    return getAttribute("onselect");
  }
  






  public final String getOnChangeAttribute()
  {
    return getAttribute("onchange");
  }
  



  public void select()
  {
    selectionDelegate_.select();
  }
  



  public String getSelectedText()
  {
    return selectionDelegate_.getSelectedText();
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
  






  protected void printXml(String indent, PrintWriter printWriter)
  {
    printWriter.print(indent + "<");
    printOpeningTagContentAsXml(printWriter);
    
    printWriter.print(">");
    printWriter.print(StringEscapeUtils.escapeXml10(getText()));
    printWriter.print("</textarea>");
  }
  



  protected void doType(char c, boolean startAtEnd, boolean lastType)
  {
    if (startAtEnd) {
      selectionDelegate_.setSelectionStart(getText().length());
    }
    doTypeProcessor_.doType(getText(), selectionDelegate_, c, this, lastType);
  }
  



  protected void doType(int keyCode, boolean startAtEnd, boolean lastType)
  {
    if (startAtEnd) {
      selectionDelegate_.setSelectionStart(getText().length());
    }
    doTypeProcessor_.doType(getText(), selectionDelegate_, keyCode, this, lastType);
  }
  



  protected void typeDone(String newValue, boolean notifyAttributeChangeListeners)
  {
    setTextInternal(newValue);
  }
  



  protected boolean acceptChar(char c)
  {
    return (super.acceptChar(c)) || (c == '\n') || (c == '\r');
  }
  



  public void focus()
  {
    super.focus();
    valueAtFocus_ = getText();
  }
  



  public void removeFocus()
  {
    super.removeFocus();
    if (!valueAtFocus_.equals(getText())) {
      HtmlInput.executeOnChangeHandlerIfAppropriate(this);
    }
    valueAtFocus_ = null;
  }
  




  public void setReadOnly(boolean isReadOnly)
  {
    if (isReadOnly) {
      setAttribute("readOnly", "readOnly");
    }
    else {
      removeAttribute("readOnly");
    }
  }
  



  public boolean isReadOnly()
  {
    return hasAttribute("readOnly");
  }
  


  protected Object clone()
    throws CloneNotSupportedException
  {
    return new HtmlTextArea(getQualifiedName(), getPage(), getAttributesMap());
  }
  




  public void setAttributeNS(String namespaceURI, String qualifiedName, String attributeValue, boolean notifyAttributeChangeListeners)
  {
    if ("name".equals(qualifiedName)) {
      if (newNames_.isEmpty()) {
        newNames_ = new HashSet();
      }
      newNames_.add(attributeValue);
    }
    super.setAttributeNS(namespaceURI, qualifiedName, attributeValue, notifyAttributeChangeListeners);
  }
  



  public String getOriginalName()
  {
    return originalName_;
  }
  



  public Collection<String> getNewNames()
  {
    return newNames_;
  }
  




  protected boolean isEmptyXmlTagExpanded()
  {
    return true;
  }
  



  public HtmlElement.DisplayStyle getDefaultStyleDisplay()
  {
    if (hasFeature(BrowserVersionFeatures.CSS_DISPLAY_BLOCK)) {
      return HtmlElement.DisplayStyle.INLINE;
    }
    return HtmlElement.DisplayStyle.INLINE_BLOCK;
  }
  




  public String getPlaceholder()
  {
    return getAttribute("placeholder");
  }
  




  public void setPlaceholder(String placeholder)
  {
    setAttribute("placeholder", placeholder);
  }
}
