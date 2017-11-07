package org.openqa.selenium.remote;

import com.google.common.collect.ImmutableMap;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.Beta;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.internal.FindsByClassName;
import org.openqa.selenium.internal.FindsByCssSelector;
import org.openqa.selenium.internal.FindsById;
import org.openqa.selenium.internal.FindsByLinkText;
import org.openqa.selenium.internal.FindsByName;
import org.openqa.selenium.internal.FindsByTagName;
import org.openqa.selenium.internal.FindsByXPath;
import org.openqa.selenium.internal.HasIdentity;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.internal.WrapsElement;
import org.openqa.selenium.io.Zip;


















public class RemoteWebElement
  implements WebElement, FindsByLinkText, FindsById, FindsByName, FindsByTagName, FindsByClassName, FindsByCssSelector, FindsByXPath, WrapsDriver, Locatable, HasIdentity, TakesScreenshot
{
  private String foundBy;
  protected String id;
  protected RemoteWebDriver parent;
  protected FileDetector fileDetector;
  
  public RemoteWebElement() {}
  
  protected void setFoundBy(SearchContext foundFrom, String locator, String term)
  {
    foundBy = String.format("[%s] -> %s: %s", new Object[] { foundFrom, locator, term });
  }
  
  public void setParent(RemoteWebDriver parent) {
    this.parent = parent;
  }
  
  public String getId() {
    return id;
  }
  
  public void setId(String id) {
    this.id = id;
  }
  
  public void setFileDetector(FileDetector detector) {
    fileDetector = detector;
  }
  
  public void click() {
    execute("clickElement", ImmutableMap.of("id", id));
  }
  
  public void submit() {
    execute("submitElement", ImmutableMap.of("id", id));
  }
  
  public void sendKeys(CharSequence... keysToSend) {
    File localFile = fileDetector.getLocalFile(keysToSend);
    if (localFile != null) {
      String remotePath = upload(localFile);
      keysToSend = new CharSequence[] { remotePath };
    }
    
    execute("sendKeysToElement", ImmutableMap.of("id", id, "value", keysToSend));
  }
  
  private String upload(File localFile) {
    if (!localFile.isFile()) {
      throw new WebDriverException("You may only upload files: " + localFile);
    }
    try
    {
      String zip = Zip.zip(localFile);
      Response response = execute("uploadFile", ImmutableMap.of("file", zip));
      return (String)response.getValue();
    } catch (IOException e) {
      throw new WebDriverException("Cannot upload " + localFile, e);
    }
  }
  
  public void clear() {
    execute("clearElement", ImmutableMap.of("id", id));
  }
  
  public String getTagName() {
    return 
      (String)execute("getElementTagName", ImmutableMap.of("id", id)).getValue();
  }
  
  public String getAttribute(String name) {
    return stringValueOf(
      execute("getElementAttribute", ImmutableMap.of("id", id, "name", name))
      .getValue());
  }
  
  private static String stringValueOf(Object o) {
    if (o == null) {
      return null;
    }
    return String.valueOf(o);
  }
  
  public boolean isSelected()
  {
    Object value = execute("isElementSelected", ImmutableMap.of("id", id)).getValue();
    try {
      return ((Boolean)value).booleanValue();
    } catch (ClassCastException ex) {
      throw new WebDriverException("Returned value cannot be converted to Boolean: " + value, ex);
    }
  }
  
  public boolean isEnabled()
  {
    Object value = execute("isElementEnabled", ImmutableMap.of("id", id)).getValue();
    try {
      return ((Boolean)value).booleanValue();
    } catch (ClassCastException ex) {
      throw new WebDriverException("Returned value cannot be converted to Boolean: " + value, ex);
    }
  }
  
  public String getText() {
    Response response = execute("getElementText", ImmutableMap.of("id", id));
    return (String)response.getValue();
  }
  
  public String getCssValue(String propertyName) {
    Response response = execute("getElementValueOfCssProperty", 
      ImmutableMap.of("id", id, "propertyName", propertyName));
    return (String)response.getValue();
  }
  
  public List<WebElement> findElements(By by) {
    return by.findElements(this);
  }
  
  public WebElement findElement(By by) {
    return by.findElement(this);
  }
  
  protected WebElement findElement(String using, String value) {
    Response response = execute("findChildElement", 
      ImmutableMap.of("id", id, "using", using, "value", value));
    
    Object responseValue = response.getValue();
    try
    {
      element = (WebElement)responseValue;
    } catch (ClassCastException ex) { WebElement element;
      throw new WebDriverException("Returned value cannot be converted to WebElement: " + value, ex); }
    WebElement element;
    parent.setFoundBy(this, element, using, value);
    return element;
  }
  
  protected List<WebElement> findElements(String using, String value)
  {
    Response response = execute("findChildElements", 
      ImmutableMap.of("id", id, "using", using, "value", value));
    Object responseValue = response.getValue();
    try
    {
      allElements = (List)responseValue;
    } catch (ClassCastException ex) { List<WebElement> allElements;
      throw new WebDriverException("Returned value cannot be converted to List<WebElement>: " + responseValue, ex); }
    List<WebElement> allElements;
    for (WebElement element : allElements) {
      parent.setFoundBy(this, element, using, value);
    }
    
    return allElements;
  }
  
  public WebElement findElementById(String using) {
    return findElement("id", using);
  }
  
  public List<WebElement> findElementsById(String using) {
    return findElements("id", using);
  }
  
  public WebElement findElementByLinkText(String using) {
    return findElement("link text", using);
  }
  
  public List<WebElement> findElementsByLinkText(String using) {
    return findElements("link text", using);
  }
  
  public WebElement findElementByName(String using) {
    return findElement("name", using);
  }
  
  public List<WebElement> findElementsByName(String using) {
    return findElements("name", using);
  }
  
  public WebElement findElementByClassName(String using) {
    return findElement("class name", using);
  }
  
  public List<WebElement> findElementsByClassName(String using) {
    return findElements("class name", using);
  }
  
  public WebElement findElementByCssSelector(String using) {
    return findElement("css selector", using);
  }
  
  public List<WebElement> findElementsByCssSelector(String using) {
    return findElements("css selector", using);
  }
  
  public WebElement findElementByXPath(String using) {
    return findElement("xpath", using);
  }
  
  public List<WebElement> findElementsByXPath(String using) {
    return findElements("xpath", using);
  }
  
  public WebElement findElementByPartialLinkText(String using) {
    return findElement("partial link text", using);
  }
  
  public List<WebElement> findElementsByPartialLinkText(String using) {
    return findElements("partial link text", using);
  }
  
  public WebElement findElementByTagName(String using) {
    return findElement("tag name", using);
  }
  
  public List<WebElement> findElementsByTagName(String using) {
    return findElements("tag name", using);
  }
  
  protected Response execute(String command, Map<String, ?> parameters) {
    return parent.execute(command, parameters);
  }
  
  public boolean equals(Object obj)
  {
    if (!(obj instanceof WebElement)) {
      return false;
    }
    
    WebElement other = (WebElement)obj;
    while ((other instanceof WrapsElement)) {
      other = ((WrapsElement)other).getWrappedElement();
    }
    
    if (!(other instanceof RemoteWebElement)) {
      return false;
    }
    
    RemoteWebElement otherRemoteWebElement = (RemoteWebElement)other;
    
    return id.equals(id);
  }
  



  public int hashCode()
  {
    return id.hashCode();
  }
  




  public WebDriver getWrappedDriver()
  {
    return parent;
  }
  
  public boolean isDisplayed()
  {
    Object value = execute("isElementDisplayed", ImmutableMap.of("id", id)).getValue();
    try {
      return ((Boolean)value).booleanValue();
    } catch (ClassCastException ex) {
      throw new WebDriverException("Returned value cannot be converted to Boolean: " + value, ex);
    }
  }
  
  public Point getLocation()
  {
    Response response = execute("getElementLocation", ImmutableMap.of("id", id));
    Map<String, Object> rawPoint = (Map)response.getValue();
    int x = ((Number)rawPoint.get("x")).intValue();
    int y = ((Number)rawPoint.get("y")).intValue();
    return new Point(x, y);
  }
  
  public Dimension getSize()
  {
    Response response = execute("getElementSize", ImmutableMap.of("id", id));
    Map<String, Object> rawSize = (Map)response.getValue();
    int width = ((Number)rawSize.get("width")).intValue();
    int height = ((Number)rawSize.get("height")).intValue();
    return new Dimension(width, height);
  }
  
  public Rectangle getRect() {
    Response response = execute("getElementRect", ImmutableMap.of("id", id));
    Map<String, Object> rawRect = (Map)response.getValue();
    int x = ((Number)rawRect.get("x")).intValue();
    int y = ((Number)rawRect.get("y")).intValue();
    int width = ((Number)rawRect.get("width")).intValue();
    int height = ((Number)rawRect.get("height")).intValue();
    return new Rectangle(x, y, height, width);
  }
  
  public Coordinates getCoordinates() {
    new Coordinates()
    {
      public Point onScreen() {
        throw new UnsupportedOperationException("Not supported yet.");
      }
      
      public Point inViewPort() {
        Response response = execute("getElementLocationOnceScrolledIntoView", 
          ImmutableMap.of("id", getId()));
        

        Map<String, Number> mapped = (Map)response.getValue();
        return new Point(((Number)mapped.get("x")).intValue(), ((Number)mapped.get("y")).intValue());
      }
      
      public Point onPage() {
        return getLocation();
      }
      
      public Object getAuxiliary() {
        return getId();
      }
    };
  }
  
  @Beta
  public <X> X getScreenshotAs(OutputType<X> outputType) throws WebDriverException {
    Response response = execute("elementScreenshot", ImmutableMap.of("id", id));
    Object result = response.getValue();
    if ((result instanceof String)) {
      String base64EncodedPng = (String)result;
      return outputType.convertFromBase64Png(base64EncodedPng); }
    if ((result instanceof byte[])) {
      String base64EncodedPng = new String((byte[])result);
      return outputType.convertFromBase64Png(base64EncodedPng);
    }
    throw new RuntimeException(String.format("Unexpected result for %s command: %s", new Object[] { "elementScreenshot", result
    
      .getClass().getName() + " instance" }));
  }
  
  public String toString()
  {
    if (foundBy == null) {
      return String.format("[%s -> unknown locator]", new Object[] { super.toString() });
    }
    return String.format("[%s]", new Object[] { foundBy });
  }
  
  public Map<String, Object> toJson() {
    return ImmutableMap.of(Dialect.OSS
      .getEncodedElementKey(), getId(), Dialect.W3C
      .getEncodedElementKey(), getId());
  }
}
