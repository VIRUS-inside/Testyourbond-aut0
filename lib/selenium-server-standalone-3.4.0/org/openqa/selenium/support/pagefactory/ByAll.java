package org.openqa.selenium.support.pagefactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;




























public class ByAll
  extends By
  implements Serializable
{
  private static final long serialVersionUID = 4573668832699497306L;
  private By[] bys;
  
  public ByAll(By... bys)
  {
    this.bys = bys;
  }
  
  public WebElement findElement(SearchContext context)
  {
    List<WebElement> elements = findElements(context);
    if (elements.isEmpty()) {
      throw new NoSuchElementException("Cannot locate an element using " + toString());
    }
    return (WebElement)elements.get(0);
  }
  
  public List<WebElement> findElements(SearchContext context)
  {
    List<WebElement> elems = new ArrayList();
    for (By by : bys) {
      elems.addAll(by.findElements(context));
    }
    
    return elems;
  }
  
  public String toString()
  {
    StringBuilder stringBuilder = new StringBuilder("By.all(");
    stringBuilder.append("{");
    
    boolean first = true;
    for (By by : bys) {
      stringBuilder.append(first ? "" : ",").append(by);
      first = false;
    }
    stringBuilder.append("})");
    return stringBuilder.toString();
  }
}
