package org.openqa.selenium.support.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;



























public class Select
  implements ISelect
{
  private final WebElement element;
  private final boolean isMulti;
  
  public Select(WebElement element)
  {
    String tagName = element.getTagName();
    
    if ((null == tagName) || (!"select".equals(tagName.toLowerCase()))) {
      throw new UnexpectedTagNameException("select", tagName);
    }
    
    this.element = element;
    
    String value = element.getAttribute("multiple");
    

    isMulti = ((value != null) && (!"false".equals(value)));
  }
  



  public boolean isMultiple()
  {
    return isMulti;
  }
  


  public List<WebElement> getOptions()
  {
    return element.findElements(By.tagName("option"));
  }
  


  public List<WebElement> getAllSelectedOptions()
  {
    List<WebElement> toReturn = new ArrayList();
    
    for (WebElement option : getOptions()) {
      if (option.isSelected()) {
        toReturn.add(option);
      }
    }
    
    return toReturn;
  }
  




  public WebElement getFirstSelectedOption()
  {
    for (WebElement option : getOptions()) {
      if (option.isSelected()) {
        return option;
      }
    }
    
    throw new NoSuchElementException("No options are selected");
  }
  











  public void selectByVisibleText(String text)
  {
    List<WebElement> options = element.findElements(By.xpath(".//option[normalize-space(.) = " + Quotes.escape(text) + "]"));
    
    boolean matched = false;
    for (WebElement option : options) {
      setSelected(option, true);
      if (!isMultiple()) {
        return;
      }
      matched = true;
    }
    
    if ((options.isEmpty()) && (text.contains(" "))) {
      String subStringWithoutSpace = getLongestSubstringWithoutSpace(text);
      List<WebElement> candidates;
      List<WebElement> candidates; if ("".equals(subStringWithoutSpace))
      {
        candidates = element.findElements(By.tagName("option"));
      }
      else
      {
        candidates = element.findElements(By.xpath(".//option[contains(., " + 
          Quotes.escape(subStringWithoutSpace) + ")]"));
      }
      for (WebElement option : candidates) {
        if (text.equals(option.getText())) {
          setSelected(option, true);
          if (!isMultiple()) {
            return;
          }
          matched = true;
        }
      }
    }
    
    if (!matched) {
      throw new NoSuchElementException("Cannot locate element with text: " + text);
    }
  }
  
  private String getLongestSubstringWithoutSpace(String s) {
    String result = "";
    StringTokenizer st = new StringTokenizer(s, " ");
    while (st.hasMoreTokens()) {
      String t = st.nextToken();
      if (t.length() > result.length()) {
        result = t;
      }
    }
    return result;
  }
  






  public void selectByIndex(int index)
  {
    String match = String.valueOf(index);
    
    for (WebElement option : getOptions()) {
      if (match.equals(option.getAttribute("index"))) {
        setSelected(option, true);
        return;
      }
    }
    throw new NoSuchElementException("Cannot locate option with index: " + index);
  }
  








  public void selectByValue(String value)
  {
    List<WebElement> options = element.findElements(By.xpath(".//option[@value = " + 
      Quotes.escape(value) + "]"));
    
    boolean matched = false;
    for (WebElement option : options) {
      setSelected(option, true);
      if (!isMultiple()) {
        return;
      }
      matched = true;
    }
    
    if (!matched) {
      throw new NoSuchElementException("Cannot locate option with value: " + value);
    }
  }
  




  public void deselectAll()
  {
    if (!isMultiple()) {
      throw new UnsupportedOperationException("You may only deselect all options of a multi-select");
    }
    

    for (WebElement option : getOptions()) {
      setSelected(option, false);
    }
  }
  









  public void deselectByValue(String value)
  {
    if (!isMultiple()) {
      throw new UnsupportedOperationException("You may only deselect options of a multi-select");
    }
    

    List<WebElement> options = element.findElements(By.xpath(".//option[@value = " + 
      Quotes.escape(value) + "]"));
    boolean matched = false;
    for (WebElement option : options) {
      setSelected(option, false);
      matched = true;
    }
    if (!matched) {
      throw new NoSuchElementException("Cannot locate option with value: " + value);
    }
  }
  







  public void deselectByIndex(int index)
  {
    if (!isMultiple()) {
      throw new UnsupportedOperationException("You may only deselect options of a multi-select");
    }
    

    String match = String.valueOf(index);
    
    for (WebElement option : getOptions()) {
      if (match.equals(option.getAttribute("index"))) {
        setSelected(option, false);
        return;
      }
    }
    throw new NoSuchElementException("Cannot locate option with index: " + index);
  }
  









  public void deselectByVisibleText(String text)
  {
    if (!isMultiple()) {
      throw new UnsupportedOperationException("You may only deselect options of a multi-select");
    }
    

    List<WebElement> options = element.findElements(By.xpath(".//option[normalize-space(.) = " + 
      Quotes.escape(text) + "]"));
    
    boolean matched = false;
    for (WebElement option : options) {
      setSelected(option, false);
      matched = true;
    }
    
    if (!matched) {
      throw new NoSuchElementException("Cannot locate element with text: " + text);
    }
  }
  








  private void setSelected(WebElement option, boolean select)
  {
    boolean isSelected = option.isSelected();
    if (((!isSelected) && (select)) || ((isSelected) && (!select))) {
      option.click();
    }
  }
}
