package com.gargoylesoftware.htmlunit;

import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;































public final class WebAssert
{
  private WebAssert() {}
  
  public static void assertTitleEquals(HtmlPage page, String title)
  {
    String s = page.getTitleText();
    if (!s.equals(title)) {
      String msg = "Actual page title '" + s + "' does not match expected page title '" + title + "'.";
      throw new AssertionError(msg);
    }
  }
  





  public static void assertTitleContains(HtmlPage page, String titlePortion)
  {
    String s = page.getTitleText();
    if (s.indexOf(titlePortion) == -1) {
      String msg = "Page title '" + s + "' does not contain the substring '" + titlePortion + "'.";
      throw new AssertionError(msg);
    }
  }
  





  public static void assertTitleMatches(HtmlPage page, String regex)
  {
    String s = page.getTitleText();
    if (!s.matches(regex)) {
      String msg = "Page title '" + s + "' does not match the regular expression '" + regex + "'.";
      throw new AssertionError(msg);
    }
  }
  




  public static void assertElementPresent(HtmlPage page, String id)
  {
    try
    {
      page.getHtmlElementById(id);
    }
    catch (ElementNotFoundException e) {
      String msg = "The page does not contain an element with ID '" + id + "'.";
      throw new AssertionError(msg);
    }
  }
  





  public static void assertElementPresentByXPath(HtmlPage page, String xpath)
  {
    List<?> elements = page.getByXPath(xpath);
    if (elements.isEmpty()) {
      String msg = "The page does not contain any elements matching the XPath expression '" + xpath + 
        "'.";
      throw new AssertionError(msg);
    }
  }
  




  public static void assertElementNotPresent(HtmlPage page, String id)
  {
    try
    {
      page.getHtmlElementById(id);
    }
    catch (ElementNotFoundException e) {
      return;
    }
    String msg = "The page contains an element with ID '" + id + "'.";
    throw new AssertionError(msg);
  }
  






  public static void assertElementNotPresentByXPath(HtmlPage page, String xpath)
  {
    List<?> elements = page.getByXPath(xpath);
    if (!elements.isEmpty()) {
      String msg = "The page does not contain any elements matching the XPath expression '" + xpath + 
        "'.";
      throw new AssertionError(msg);
    }
  }
  





  public static void assertTextPresent(HtmlPage page, String text)
  {
    if (page.asText().indexOf(text) == -1) {
      String msg = "The page does not contain the text '" + text + "'.";
      throw new AssertionError(msg);
    }
  }
  






  public static void assertTextPresentInElement(HtmlPage page, String text, String id)
  {
    try
    {
      HtmlElement element = page.getHtmlElementById(id);
      if (element.asText().indexOf(text) == -1) {
        String msg = "The element with ID '" + id + "' does not contain the text '" + text + "'.";
        throw new AssertionError(msg);
      }
    }
    catch (ElementNotFoundException e) {
      String msg = "Unable to verify that the element with ID '" + id + "' contains the text '" + text + 
        "' because the specified element does not exist.";
      throw new AssertionError(msg);
    }
  }
  





  public static void assertTextNotPresent(HtmlPage page, String text)
  {
    if (page.asText().contains(text)) {
      String msg = "The page contains the text '" + text + "'.";
      throw new AssertionError(msg);
    }
  }
  






  public static void assertTextNotPresentInElement(HtmlPage page, String text, String id)
  {
    try
    {
      HtmlElement element = page.getHtmlElementById(id);
      if (element.asText().contains(text)) {
        String msg = "The element with ID '" + id + "' contains the text '" + text + "'.";
        throw new AssertionError(msg);
      }
    }
    catch (ElementNotFoundException e) {
      String msg = "Unable to verify that the element with ID '" + id + "' does not contain the text '" + 
        text + "' because the specified element does not exist.";
      throw new AssertionError(msg);
    }
  }
  




  public static void assertLinkPresent(HtmlPage page, String id)
  {
    try
    {
      page.getDocumentElement().getOneHtmlElementByAttribute("a", "id", id);
    }
    catch (ElementNotFoundException e) {
      String msg = "The page does not contain a link with ID '" + id + "'.";
      throw new AssertionError(msg);
    }
  }
  




  public static void assertLinkNotPresent(HtmlPage page, String id)
  {
    try
    {
      page.getDocumentElement().getOneHtmlElementByAttribute("a", "id", id);
      
      String msg = "The page contains a link with ID '" + id + "'.";
      throw new AssertionError(msg);
    }
    catch (ElementNotFoundException localElementNotFoundException) {}
  }
  








  public static void assertLinkPresentWithText(HtmlPage page, String text)
  {
    boolean found = false;
    for (HtmlAnchor a : page.getAnchors()) {
      if (a.asText().contains(text)) {
        found = true;
        break;
      }
    }
    if (!found) {
      String msg = "The page does not contain a link with text '" + text + "'.";
      throw new AssertionError(msg);
    }
  }
  






  public static void assertLinkNotPresentWithText(HtmlPage page, String text)
  {
    boolean found = false;
    for (HtmlAnchor a : page.getAnchors()) {
      if (a.asText().contains(text)) {
        found = true;
        break;
      }
    }
    if (found) {
      String msg = "The page contains a link with text '" + text + "'.";
      throw new AssertionError(msg);
    }
  }
  




  public static void assertFormPresent(HtmlPage page, String name)
  {
    try
    {
      page.getFormByName(name);
    }
    catch (ElementNotFoundException e) {
      String msg = "The page does not contain a form named '" + name + "'.";
      throw new AssertionError(msg);
    }
  }
  




  public static void assertFormNotPresent(HtmlPage page, String name)
  {
    try
    {
      page.getFormByName(name);
    }
    catch (ElementNotFoundException e) {
      return;
    }
    String msg = "The page contains a form named '" + name + "'.";
    throw new AssertionError(msg);
  }
  





  public static void assertInputPresent(HtmlPage page, String name)
  {
    String xpath = "//input[@name='" + name + "']";
    List<?> list = page.getByXPath(xpath);
    if (list.isEmpty()) {
      throw new AssertionError("Unable to find an input element named '" + name + "'.");
    }
  }
  





  public static void assertInputNotPresent(HtmlPage page, String name)
  {
    String xpath = "//input[@name='" + name + "']";
    List<?> list = page.getByXPath(xpath);
    if (!list.isEmpty()) {
      throw new AssertionError("Unable to find an input element named '" + name + "'.");
    }
  }
  







  public static void assertInputContainsValue(HtmlPage page, String name, String value)
  {
    String xpath = "//input[@name='" + name + "']";
    List<?> list = page.getByXPath(xpath);
    if (list.isEmpty()) {
      throw new AssertionError("Unable to find an input element named '" + name + "'.");
    }
    HtmlInput input = (HtmlInput)list.get(0);
    String s = input.getValueAttribute();
    if (!s.equals(value)) {
      throw new AssertionError("The input element named '" + name + "' contains the value '" + s + 
        "', not the expected value '" + value + "'.");
    }
  }
  







  public static void assertInputDoesNotContainValue(HtmlPage page, String name, String value)
  {
    String xpath = "//input[@name='" + name + "']";
    List<?> list = page.getByXPath(xpath);
    if (list.isEmpty()) {
      throw new AssertionError("Unable to find an input element named '" + name + "'.");
    }
    HtmlInput input = (HtmlInput)list.get(0);
    String s = input.getValueAttribute();
    if (s.equals(value)) {
      throw new AssertionError("The input element named '" + name + "' contains the value '" + s + 
        "', not the expected value '" + value + "'.");
    }
  }
  










  public static void assertAllTabIndexAttributesSet(HtmlPage page)
  {
    List<String> tags = 
      Arrays.asList(new String[] { "a", "area", "button", "input", "object", "select", "textarea" });
    Iterator localIterator2;
    for (Iterator localIterator1 = tags.iterator(); localIterator1.hasNext(); 
        localIterator2.hasNext())
    {
      String tag = (String)localIterator1.next();
      localIterator2 = page.getDocumentElement().getElementsByTagName(tag).iterator(); continue;HtmlElement element = (HtmlElement)localIterator2.next();
      Short tabIndex = element.getTabIndex();
      if ((tabIndex == null) || (tabIndex == HtmlElement.TAB_INDEX_OUT_OF_BOUNDS)) {
        String s = element.getAttribute("tabindex");
        throw new AssertionError("Illegal value for tab index: '" + s + "'.");
      }
    }
  }
  







  public static void assertAllAccessKeyAttributesUnique(HtmlPage page)
  {
    List<String> list = new ArrayList();
    for (HtmlElement element : page.getHtmlElementDescendants()) {
      String key = element.getAttribute("accesskey");
      if ((key != null) && (!key.isEmpty())) {
        if (list.contains(key)) {
          throw new AssertionError("The access key '" + key + "' is not unique.");
        }
        list.add(key);
      }
    }
  }
  




  public static void assertAllIdAttributesUnique(HtmlPage page)
  {
    List<String> list = new ArrayList();
    for (HtmlElement element : page.getHtmlElementDescendants()) {
      String id = element.getId();
      if ((id != null) && (!id.isEmpty())) {
        if (list.contains(id)) {
          throw new AssertionError("The element ID '" + id + "' is not unique.");
        }
        list.add(id);
      }
    }
  }
  





  public static void notNull(String description, Object object)
  {
    if (object == null) {
      throw new NullPointerException(description);
    }
  }
}
