package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.javascript.host.event.MouseEvent;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Node;
































public class HtmlSelect
  extends HtmlElement
  implements DisabledElement, SubmittableElement, FormFieldWithNameHistory
{
  public static final String TAG_NAME = "select";
  private final String originalName_;
  private Collection<String> newNames_ = Collections.emptySet();
  
  private int lastSelectedIndex_ = -1;
  







  HtmlSelect(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
    originalName_ = getNameAttribute();
  }
  



  protected void onAllChildrenAddedToPage(boolean postponed)
  {
    int size;
    


    try
    {
      int size = Integer.parseInt(getSizeAttribute());
      if (size < 0) {
        removeAttribute("size");
        size = 0;
      }
    }
    catch (NumberFormatException e) {
      removeAttribute("size");
      size = 0;
    }
    

    if ((getSelectedOptions().isEmpty()) && (size <= 1) && (!isMultipleSelectEnabled())) {
      List<HtmlOption> options = getOptions();
      if (!options.isEmpty()) {
        HtmlOption first = (HtmlOption)options.get(0);
        first.setSelectedInternal(true);
      }
    }
  }
  



  public boolean handles(Event event)
  {
    if (((event instanceof MouseEvent)) && (hasFeature(BrowserVersionFeatures.EVENT_MOUSE_ON_DISABLED))) {
      return true;
    }
    
    return super.handles(event);
  }
  





  public List<HtmlOption> getSelectedOptions()
  {
    List<HtmlOption> result;
    



    if (isMultipleSelectEnabled())
    {
      List<HtmlOption> result = new ArrayList();
      for (HtmlElement element : getHtmlElementDescendants()) {
        if (((element instanceof HtmlOption)) && (((HtmlOption)element).isSelected())) {
          result.add((HtmlOption)element);
        }
      }
    }
    else
    {
      result = new ArrayList(1);
      HtmlOption lastSelected = null;
      for (HtmlElement element : getHtmlElementDescendants()) {
        if ((element instanceof HtmlOption)) {
          HtmlOption option = (HtmlOption)element;
          if (option.isSelected()) {
            lastSelected = option;
          }
        }
      }
      if (lastSelected != null) {
        result.add(lastSelected);
      }
    }
    return Collections.unmodifiableList(result);
  }
  



  public List<HtmlOption> getOptions()
  {
    return Collections.unmodifiableList(getElementsByTagNameImpl("option"));
  }
  





  public HtmlOption getOption(int index)
  {
    return (HtmlOption)getElementsByTagNameImpl("option").get(index);
  }
  



  public int getOptionSize()
  {
    return getElementsByTagName("option").size();
  }
  




  public void setOptionSize(int newLength)
  {
    List<HtmlElement> elementList = getElementsByTagName("option");
    
    for (int i = elementList.size() - 1; i >= newLength; i--) {
      ((HtmlElement)elementList.get(i)).remove();
    }
  }
  



  public void removeOption(int index)
  {
    DomElement.ChildElementsIterator iterator = new DomElement.ChildElementsIterator(this);
    for (int i = 0; iterator.hasNext();) {
      DomElement element = iterator.nextElement();
      if ((element instanceof HtmlOption)) {
        if (i == index) {
          element.remove();
          ensureSelectedIndex();
          return;
        }
        i++;
      }
    }
  }
  




  public void replaceOption(int index, HtmlOption newOption)
  {
    DomElement.ChildElementsIterator iterator = new DomElement.ChildElementsIterator(this);
    for (int i = 0; iterator.hasNext();) {
      DomElement element = iterator.nextElement();
      if ((element instanceof HtmlOption)) {
        if (i == index) {
          element.replace(newOption);
          ensureSelectedIndex();
          return;
        }
        i++;
      }
    }
    
    if (newOption.isSelected()) {
      setSelectedAttribute(newOption, true);
    }
  }
  



  public void appendOption(HtmlOption newOption)
  {
    appendChild(newOption);
    
    ensureSelectedIndex();
  }
  



  public DomNode appendChild(Node node)
  {
    DomNode response = super.appendChild(node);
    if ((node instanceof HtmlOption)) {
      HtmlOption option = (HtmlOption)node;
      if (option.isSelected()) {
        doSelectOption(option, true, false, false, false);
      }
    }
    return response;
  }
  











  public <P extends Page> P setSelectedAttribute(String optionValue, boolean isSelected)
  {
    return setSelectedAttribute(optionValue, isSelected, true);
  }
  















  public <P extends Page> P setSelectedAttribute(String optionValue, boolean isSelected, boolean invokeOnFocus)
  {
    try
    {
      boolean attributeOnly = (hasFeature(BrowserVersionFeatures.JS_SELECT_SET_VALUES_CHECKS_ONLY_VALUE_ATTRIBUTE)) && 
        (!optionValue.isEmpty());
      HtmlOption selected;
      HtmlOption selected; if (attributeOnly) {
        selected = getOptionByValueStrict(optionValue);
      }
      else {
        selected = getOptionByValue(optionValue);
      }
      return setSelectedAttribute(selected, isSelected, invokeOnFocus, true, false, true);
    }
    catch (ElementNotFoundException e) {
      if (hasFeature(BrowserVersionFeatures.SELECT_DESELECT_ALL_IF_SWITCHING_UNKNOWN)) {
        for (HtmlOption o : getSelectedOptions())
          o.setSelected(false);
      }
    }
    return getPage();
  }
  













  public <P extends Page> P setSelectedAttribute(HtmlOption selectedOption, boolean isSelected)
  {
    return setSelectedAttribute(selectedOption, isSelected, true, true, false, true);
  }
  



















  public <P extends Page> P setSelectedAttribute(HtmlOption selectedOption, boolean isSelected, boolean invokeOnFocus, boolean shiftKey, boolean ctrlKey, boolean isClick)
  {
    if ((isSelected) && (invokeOnFocus)) {
      ((HtmlPage)getPage()).setFocusedElement(this);
    }
    
    boolean changeSelectedState = selectedOption.isSelected() ^ isSelected;
    
    if (changeSelectedState) {
      doSelectOption(selectedOption, isSelected, shiftKey, ctrlKey, isClick);
      HtmlInput.executeOnChangeHandlerIfAppropriate(this);
    }
    
    return getPage().getWebClient().getCurrentWindow().getEnclosedPage();
  }
  


  private void doSelectOption(HtmlOption selectedOption, boolean isSelected, boolean shiftKey, boolean ctrlKey, boolean isClick)
  {
    if (isMultipleSelectEnabled()) {
      selectedOption.setSelectedInternal(isSelected);
      if ((isClick) && (!ctrlKey)) {
        if (!shiftKey) {
          setOnlySelected(selectedOption, isSelected);
          lastSelectedIndex_ = getOptions().indexOf(selectedOption);
        }
        else if ((isSelected) && (lastSelectedIndex_ != -1)) {
          List<HtmlOption> options = getOptions();
          int newIndex = options.indexOf(selectedOption);
          for (int i = 0; i < options.size(); i++) {
            ((HtmlOption)options.get(i)).setSelectedInternal(isBetween(i, lastSelectedIndex_, newIndex));
          }
        }
      }
    }
    else {
      setOnlySelected(selectedOption, isSelected);
    }
  }
  




  void setOnlySelected(HtmlOption selectedOption, boolean isSelected)
  {
    for (HtmlOption option : getOptions()) {
      option.setSelectedInternal((option == selectedOption) && (isSelected));
    }
  }
  
  private static boolean isBetween(int number, int min, int max) {
    return (number >= min) && (number <= max);
  }
  



  public NameValuePair[] getSubmitNameValuePairs()
  {
    String name = getNameAttribute();
    
    List<HtmlOption> selectedOptions = getSelectedOptions();
    
    NameValuePair[] pairs = new NameValuePair[selectedOptions.size()];
    
    int i = 0;
    for (HtmlOption option : selectedOptions) {
      pairs[(i++)] = new NameValuePair(name, option.getValueAttribute());
    }
    return pairs;
  }
  



  boolean isValidForSubmission()
  {
    return getOptionSize() > 0;
  }
  



  public void reset()
  {
    for (HtmlOption option : getOptions()) {
      option.reset();
    }
    onAllChildrenAddedToPage(false);
  }
  




  public void setDefaultValue(String defaultValue)
  {
    setSelectedAttribute(defaultValue, true);
  }
  




  public String getDefaultValue()
  {
    List<HtmlOption> options = getSelectedOptions();
    if (options.size() > 0) {
      return ((HtmlOption)options.get(0)).getValueAttribute();
    }
    return "";
  }
  










  public void setDefaultChecked(boolean defaultChecked) {}
  









  public boolean isDefaultChecked()
  {
    return false;
  }
  



  public boolean isMultipleSelectEnabled()
  {
    return getAttribute("multiple") != ATTRIBUTE_NOT_DEFINED;
  }
  





  public HtmlOption getOptionByValue(String value)
    throws ElementNotFoundException
  {
    WebAssert.notNull("value", value);
    for (HtmlOption option : getOptions()) {
      if (option.getValueAttribute().equals(value)) {
        return option;
      }
    }
    throw new ElementNotFoundException("option", "value", value);
  }
  
  private HtmlOption getOptionByValueStrict(String value) throws ElementNotFoundException {
    WebAssert.notNull("value", value);
    for (HtmlOption option : getOptions()) {
      if (option.getAttribute("value").equals(value)) {
        return option;
      }
    }
    throw new ElementNotFoundException("option", "value", value);
  }
  





  public HtmlOption getOptionByText(String text)
    throws ElementNotFoundException
  {
    WebAssert.notNull("text", text);
    for (HtmlOption option : getOptions()) {
      if (option.getText().equals(text)) {
        return option;
      }
    }
    throw new ElementNotFoundException("option", "text", text);
  }
  



  public String asText()
  {
    List<HtmlOption> options;
    


    List<HtmlOption> options;
    

    if (isMultipleSelectEnabled()) {
      options = getOptions();
    }
    else {
      options = getSelectedOptions();
    }
    
    StringBuilder builder = new StringBuilder();
    for (Iterator<HtmlOption> i = options.iterator(); i.hasNext();) {
      HtmlOption currentOption = (HtmlOption)i.next();
      if (currentOption != null) {
        builder.append(currentOption.asText());
      }
      if (i.hasNext()) {
        builder.append("\n");
      }
    }
    
    return builder.toString();
  }
  





  public final String getNameAttribute()
  {
    return getAttribute("name");
  }
  






  public final String getSizeAttribute()
  {
    return getAttribute("size");
  }
  





  public final String getMultipleAttribute()
  {
    return getAttribute("multiple");
  }
  



  public final String getDisabledAttribute()
  {
    return getAttribute("disabled");
  }
  



  public final boolean isDisabled()
  {
    return hasAttribute("disabled");
  }
  





  public final String getTabIndexAttribute()
  {
    return getAttribute("tabindex");
  }
  





  public final String getOnFocusAttribute()
  {
    return getAttribute("onfocus");
  }
  





  public final String getOnBlurAttribute()
  {
    return getAttribute("onblur");
  }
  





  public final String getOnChangeAttribute()
  {
    return getAttribute("onchange");
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
  



  public HtmlElement.DisplayStyle getDefaultStyleDisplay()
  {
    return HtmlElement.DisplayStyle.INLINE_BLOCK;
  }
  



  public int getSelectedIndex()
  {
    List<HtmlOption> selectedOptions = getSelectedOptions();
    if (selectedOptions.isEmpty()) {
      return -1;
    }
    List<HtmlOption> allOptions = getOptions();
    return allOptions.indexOf(selectedOptions.get(0));
  }
  



  public void setSelectedIndex(int index)
  {
    for (HtmlOption itemToUnSelect : getSelectedOptions()) {
      setSelectedAttribute(itemToUnSelect, false);
    }
    if (index < 0) {
      return;
    }
    
    List<HtmlOption> allOptions = getOptions();
    
    if (index < allOptions.size()) {
      HtmlOption itemToSelect = (HtmlOption)allOptions.get(index);
      setSelectedAttribute(itemToSelect, true, false, true, false, true);
    }
  }
  




  public void ensureSelectedIndex()
  {
    if (getOptionSize() == 0) {
      setSelectedIndex(-1);
    }
    else if ((getSelectedIndex() == -1) && (!isMultipleSelectEnabled())) {
      setSelectedIndex(0);
    }
  }
  





  public int indexOf(HtmlOption option)
  {
    if (option == null) {
      return 0;
    }
    
    int index = 0;
    for (HtmlElement element : getHtmlElementDescendants()) {
      if (option == element) {
        return index;
      }
      index++;
    }
    return 0;
  }
}
