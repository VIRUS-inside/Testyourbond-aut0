package org.openqa.selenium;

import java.io.Serializable;
import java.util.List;
import org.openqa.selenium.internal.FindsByClassName;
import org.openqa.selenium.internal.FindsByCssSelector;
import org.openqa.selenium.internal.FindsById;
import org.openqa.selenium.internal.FindsByLinkText;
import org.openqa.selenium.internal.FindsByName;
import org.openqa.selenium.internal.FindsByTagName;
import org.openqa.selenium.internal.FindsByXPath;


































public abstract class By
{
  public By() {}
  
  public static By id(String id)
  {
    if (id == null) {
      throw new IllegalArgumentException("Cannot find elements with a null id attribute.");
    }
    
    return new ById(id);
  }
  



  public static By linkText(String linkText)
  {
    if (linkText == null) {
      throw new IllegalArgumentException("Cannot find elements when link text is null.");
    }
    
    return new ByLinkText(linkText);
  }
  



  public static By partialLinkText(String linkText)
  {
    if (linkText == null) {
      throw new IllegalArgumentException("Cannot find elements when link text is null.");
    }
    
    return new ByPartialLinkText(linkText);
  }
  



  public static By name(String name)
  {
    if (name == null) {
      throw new IllegalArgumentException("Cannot find elements when name text is null.");
    }
    
    return new ByName(name);
  }
  



  public static By tagName(String name)
  {
    if (name == null) {
      throw new IllegalArgumentException("Cannot find elements when name tag name is null.");
    }
    
    return new ByTagName(name);
  }
  



  public static By xpath(String xpathExpression)
  {
    if (xpathExpression == null) {
      throw new IllegalArgumentException("Cannot find elements when the XPath expression is null.");
    }
    
    return new ByXPath(xpathExpression);
  }
  







  public static By className(String className)
  {
    if (className == null) {
      throw new IllegalArgumentException("Cannot find elements when the class name expression is null.");
    }
    
    return new ByClassName(className);
  }
  







  public static By cssSelector(String selector)
  {
    if (selector == null) {
      throw new IllegalArgumentException("Cannot find elements when the selector is null");
    }
    
    return new ByCssSelector(selector);
  }
  






  public WebElement findElement(SearchContext context)
  {
    List<WebElement> allElements = findElements(context);
    if ((allElements == null) || (allElements.isEmpty()))
    {
      throw new NoSuchElementException("Cannot locate an element using " + toString()); }
    return (WebElement)allElements.get(0);
  }
  



  public abstract List<WebElement> findElements(SearchContext paramSearchContext);
  



  public boolean equals(Object o)
  {
    if (this == o)
      return true;
    if ((o == null) || (getClass() != o.getClass())) {
      return false;
    }
    By by = (By)o;
    
    return toString().equals(by.toString());
  }
  
  public int hashCode()
  {
    return toString().hashCode();
  }
  

  public String toString()
  {
    return "[unknown locator]";
  }
  
  public static class ById extends By implements Serializable
  {
    private static final long serialVersionUID = 5341968046120372169L;
    private final String id;
    
    public ById(String id)
    {
      this.id = id;
    }
    
    public List<WebElement> findElements(SearchContext context)
    {
      if ((context instanceof FindsById))
        return ((FindsById)context).findElementsById(id);
      return ((FindsByXPath)context).findElementsByXPath(".//*[@id = '" + id + "']");
    }
    

    public WebElement findElement(SearchContext context)
    {
      if ((context instanceof FindsById))
        return ((FindsById)context).findElementById(id);
      return ((FindsByXPath)context).findElementByXPath(".//*[@id = '" + id + "']");
    }
    

    public String toString()
    {
      return "By.id: " + id;
    }
  }
  
  public static class ByLinkText extends By implements Serializable
  {
    private static final long serialVersionUID = 1967414585359739708L;
    private final String linkText;
    
    public ByLinkText(String linkText)
    {
      this.linkText = linkText;
    }
    
    public List<WebElement> findElements(SearchContext context)
    {
      return ((FindsByLinkText)context).findElementsByLinkText(linkText);
    }
    
    public WebElement findElement(SearchContext context)
    {
      return ((FindsByLinkText)context).findElementByLinkText(linkText);
    }
    
    public String toString()
    {
      return "By.linkText: " + linkText;
    }
  }
  
  public static class ByPartialLinkText extends By implements Serializable
  {
    private static final long serialVersionUID = 1163955344140679054L;
    private final String linkText;
    
    public ByPartialLinkText(String linkText)
    {
      this.linkText = linkText;
    }
    
    public List<WebElement> findElements(SearchContext context)
    {
      return 
        ((FindsByLinkText)context).findElementsByPartialLinkText(linkText);
    }
    
    public WebElement findElement(SearchContext context)
    {
      return ((FindsByLinkText)context).findElementByPartialLinkText(linkText);
    }
    
    public String toString()
    {
      return "By.partialLinkText: " + linkText;
    }
  }
  
  public static class ByName extends By implements Serializable
  {
    private static final long serialVersionUID = 376317282960469555L;
    private final String name;
    
    public ByName(String name)
    {
      this.name = name;
    }
    
    public List<WebElement> findElements(SearchContext context)
    {
      if ((context instanceof FindsByName))
        return ((FindsByName)context).findElementsByName(name);
      return ((FindsByXPath)context).findElementsByXPath(".//*[@name = '" + name + "']");
    }
    

    public WebElement findElement(SearchContext context)
    {
      if ((context instanceof FindsByName))
        return ((FindsByName)context).findElementByName(name);
      return ((FindsByXPath)context).findElementByXPath(".//*[@name = '" + name + "']");
    }
    

    public String toString()
    {
      return "By.name: " + name;
    }
  }
  
  public static class ByTagName extends By implements Serializable
  {
    private static final long serialVersionUID = 4699295846984948351L;
    private final String name;
    
    public ByTagName(String name)
    {
      this.name = name;
    }
    
    public List<WebElement> findElements(SearchContext context)
    {
      if ((context instanceof FindsByTagName))
        return ((FindsByTagName)context).findElementsByTagName(name);
      return ((FindsByXPath)context).findElementsByXPath(".//" + name);
    }
    
    public WebElement findElement(SearchContext context)
    {
      if ((context instanceof FindsByTagName))
        return ((FindsByTagName)context).findElementByTagName(name);
      return ((FindsByXPath)context).findElementByXPath(".//" + name);
    }
    
    public String toString()
    {
      return "By.tagName: " + name;
    }
  }
  
  public static class ByXPath extends By implements Serializable
  {
    private static final long serialVersionUID = -6727228887685051584L;
    private final String xpathExpression;
    
    public ByXPath(String xpathExpression)
    {
      this.xpathExpression = xpathExpression;
    }
    
    public List<WebElement> findElements(SearchContext context)
    {
      return ((FindsByXPath)context).findElementsByXPath(xpathExpression);
    }
    
    public WebElement findElement(SearchContext context)
    {
      return ((FindsByXPath)context).findElementByXPath(xpathExpression);
    }
    
    public String toString()
    {
      return "By.xpath: " + xpathExpression;
    }
  }
  
  public static class ByClassName extends By implements Serializable
  {
    private static final long serialVersionUID = -8737882849130394673L;
    private final String className;
    
    public ByClassName(String className)
    {
      this.className = className;
    }
    
    public List<WebElement> findElements(SearchContext context)
    {
      if ((context instanceof FindsByClassName))
        return ((FindsByClassName)context).findElementsByClassName(className);
      return ((FindsByXPath)context).findElementsByXPath(".//*[" + 
        containingWord("class", className) + "]");
    }
    
    public WebElement findElement(SearchContext context)
    {
      if ((context instanceof FindsByClassName))
        return ((FindsByClassName)context).findElementByClassName(className);
      return ((FindsByXPath)context).findElementByXPath(".//*[" + 
        containingWord("class", className) + "]");
    }
    








    private String containingWord(String attribute, String word)
    {
      return "contains(concat(' ',normalize-space(@" + attribute + "),' '),' " + word + " ')";
    }
    

    public String toString()
    {
      return "By.className: " + className;
    }
  }
  
  public static class ByCssSelector extends By implements Serializable
  {
    private static final long serialVersionUID = -3910258723099459239L;
    private final String selector;
    
    public ByCssSelector(String selector)
    {
      this.selector = selector;
    }
    
    public WebElement findElement(SearchContext context)
    {
      if ((context instanceof FindsByCssSelector)) {
        return 
          ((FindsByCssSelector)context).findElementByCssSelector(selector);
      }
      
      throw new WebDriverException("Driver does not support finding an element by selector: " + selector);
    }
    

    public List<WebElement> findElements(SearchContext context)
    {
      if ((context instanceof FindsByCssSelector)) {
        return 
          ((FindsByCssSelector)context).findElementsByCssSelector(selector);
      }
      
      throw new WebDriverException("Driver does not support finding elements by selector: " + selector);
    }
    

    public String toString()
    {
      return "By.cssSelector: " + selector;
    }
  }
}
