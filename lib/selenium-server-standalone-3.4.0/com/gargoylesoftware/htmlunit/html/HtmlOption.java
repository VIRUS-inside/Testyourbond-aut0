package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;













































public class HtmlOption
  extends HtmlElement
  implements DisabledElement
{
  public static final String TAG_NAME = "option";
  private boolean selected_;
  
  HtmlOption(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
    reset();
  }
  



  public boolean isSelected()
  {
    return selected_;
  }
  







  public Page setSelected(boolean selected)
  {
    setSelected(selected, true, false, false);
    return getPage();
  }
  








  public void setSelected(boolean selected, boolean invokeOnFocus)
  {
    setSelected(selected, invokeOnFocus, true, false);
  }
  









  private void setSelected(boolean selected, boolean invokeOnFocus, boolean shiftKey, boolean ctrlKey)
  {
    if (selected == isSelected()) {
      return;
    }
    HtmlSelect select = getEnclosingSelect();
    if (select != null) {
      if ((hasFeature(BrowserVersionFeatures.EVENT_ONMOUSEOVER_FOR_DISABLED_OPTION)) && 
        (!select.isMultipleSelectEnabled()) && (select.getOptionSize() == 1)) {
        selected = true;
      }
      select.setSelectedAttribute(this, selected, invokeOnFocus, shiftKey, ctrlKey, true);
      return;
    }
    

    setSelectedInternal(selected);
  }
  



  public void insertBefore(DomNode newNode)
  {
    super.insertBefore(newNode);
    if ((newNode instanceof HtmlOption)) {
      HtmlOption option = (HtmlOption)newNode;
      if (option.isSelected()) {
        getEnclosingSelect().setSelectedAttribute(option, true);
      }
    }
  }
  



  public HtmlSelect getEnclosingSelect()
  {
    return (HtmlSelect)getEnclosingElement("select");
  }
  


  public void reset()
  {
    setSelectedInternal(hasAttribute("selected"));
  }
  







  public final String getSelectedAttribute()
  {
    return getAttribute("selected");
  }
  






  public final boolean isDefaultSelected()
  {
    return hasAttribute("selected");
  }
  








  public final boolean isDisabled()
  {
    if (hasFeature(BrowserVersionFeatures.HTMLOPTION_PREVENT_DISABLED)) {
      return false;
    }
    return hasAttribute("disabled");
  }
  



  public final String getDisabledAttribute()
  {
    return getAttribute("disabled");
  }
  






  public final String getLabelAttribute()
  {
    return getAttribute("label");
  }
  






  public final void setLabelAttribute(String newLabel)
  {
    setAttribute("label", newLabel);
  }
  







  public final String getValueAttribute()
  {
    String value = getAttribute("value");
    if (value == ATTRIBUTE_NOT_DEFINED) {
      value = getText();
    }
    return value;
  }
  






  public final void setValueAttribute(String newValue)
  {
    setAttribute("value", newValue);
  }
  




  public Page mouseDown(boolean shiftKey, boolean ctrlKey, boolean altKey, int button)
  {
    Page page = null;
    if (hasFeature(BrowserVersionFeatures.EVENT_ONMOUSEDOWN_FOR_SELECT_OPTION_TRIGGERS_ADDITIONAL_DOWN_FOR_SELECT)) {
      page = getEnclosingSelect().mouseDown(shiftKey, ctrlKey, altKey, button);
    }
    if (hasFeature(BrowserVersionFeatures.EVENT_ONMOUSEDOWN_NOT_FOR_SELECT_OPTION)) {
      return page;
    }
    return super.mouseDown(shiftKey, ctrlKey, altKey, button);
  }
  




  public Page mouseUp(boolean shiftKey, boolean ctrlKey, boolean altKey, int button)
  {
    Page page = null;
    if (hasFeature(BrowserVersionFeatures.EVENT_ONMOUSEUP_FOR_SELECT_OPTION_TRIGGERS_ADDITIONAL_UP_FOR_SELECT)) {
      page = getEnclosingSelect().mouseUp(shiftKey, ctrlKey, altKey, button);
    }
    if (hasFeature(BrowserVersionFeatures.EVENT_ONMOUSEUP_NOT_FOR_SELECT_OPTION)) {
      return page;
    }
    return super.mouseUp(shiftKey, ctrlKey, altKey, button);
  }
  



  public <P extends Page> P click(Event event, boolean ignoreVisibility)
    throws IOException
  {
    if (hasFeature(BrowserVersionFeatures.EVENT_ONCLICK_FOR_SELECT_ONLY)) {
      SgmlPage page = getPage();
      
      if (isDisabled()) {
        return page;
      }
      
      if (isStateUpdateFirst()) {
        doClickStateUpdate(event.getShiftKey(), event.getCtrlKey());
      }
      
      return getEnclosingSelect().click(event, ignoreVisibility);
    }
    return super.click(event, ignoreVisibility);
  }
  



  protected boolean doClickStateUpdate(boolean shiftKey, boolean ctrlKey)
    throws IOException
  {
    boolean changed = false;
    if (!isSelected()) {
      setSelected(true, true, shiftKey, ctrlKey);
      changed = true;
    }
    else if (getEnclosingSelect().isMultipleSelectEnabled()) {
      if (ctrlKey) {
        setSelected(false, true, shiftKey, ctrlKey);
        changed = true;
      }
      else {
        getEnclosingSelect().setOnlySelected(this, true);
      }
    }
    super.doClickStateUpdate(shiftKey, ctrlKey);
    return changed;
  }
  



  protected DomNode getEventTargetElement()
  {
    if (hasFeature(BrowserVersionFeatures.EVENT_ONCLICK_FOR_SELECT_ONLY)) {
      HtmlSelect select = getEnclosingSelect();
      if (select != null) {
        return select;
      }
    }
    return super.getEventTargetElement();
  }
  



  protected boolean isStateUpdateFirst()
  {
    return true;
  }
  



  protected void printOpeningTagContentAsXml(PrintWriter printWriter)
  {
    super.printOpeningTagContentAsXml(printWriter);
    if ((selected_) && (getAttribute("selected") == ATTRIBUTE_NOT_DEFINED)) {
      printWriter.print(" selected=\"selected\"");
    }
  }
  




  void setSelectedInternal(boolean selected)
  {
    selected_ = selected;
  }
  






  public String asText()
  {
    return super.asText();
  }
  



  public void setText(String text)
  {
    if (((text == null) || (text.isEmpty())) && 
      (hasFeature(BrowserVersionFeatures.HTMLOPTION_EMPTY_TEXT_IS_NO_CHILDREN))) {
      removeAllChildren();
    }
    else {
      DomNode child = getFirstChild();
      if (child == null) {
        appendChild(new DomText(getPage(), text));
      }
      else {
        child.setNodeValue(text);
      }
    }
  }
  



  public String getText()
  {
    HtmlSerializer ser = new HtmlSerializer();
    ser.setIgnoreMaskedElements(false);
    return ser.asText(this);
  }
  



  public Page mouseOver(boolean shiftKey, boolean ctrlKey, boolean altKey, int button)
  {
    SgmlPage page = getPage();
    if (page.getWebClient().getBrowserVersion().hasFeature(BrowserVersionFeatures.EVENT_ONMOUSEOVER_NEVER_FOR_SELECT_OPTION)) {
      return page;
    }
    return super.mouseOver(shiftKey, ctrlKey, altKey, button);
  }
  



  public HtmlElement.DisplayStyle getDefaultStyleDisplay()
  {
    if (hasFeature(BrowserVersionFeatures.CSS_DISPLAY_BLOCK2)) {
      return HtmlElement.DisplayStyle.BLOCK;
    }
    return HtmlElement.DisplayStyle.INLINE;
  }
  



  public boolean handles(Event event)
  {
    if (("mouseover".equals(event.getType())) && 
      (getPage().getWebClient().getBrowserVersion().hasFeature(BrowserVersionFeatures.EVENT_ONMOUSEOVER_FOR_DISABLED_OPTION))) {
      return true;
    }
    return super.handles(event);
  }
}
