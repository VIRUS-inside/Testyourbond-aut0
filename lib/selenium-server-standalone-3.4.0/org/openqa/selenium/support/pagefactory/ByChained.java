package org.openqa.selenium.support.pagefactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;




























public class ByChained
  extends By
  implements Serializable
{
  private static final long serialVersionUID = 1563769051170172451L;
  private By[] bys;
  
  public ByChained(By... bys)
  {
    this.bys = bys;
  }
  
  public WebElement findElement(SearchContext context)
  {
    List<WebElement> elements = findElements(context);
    if (elements.isEmpty())
      throw new NoSuchElementException("Cannot locate an element using " + toString());
    return (WebElement)elements.get(0);
  }
  
  public List<WebElement> findElements(SearchContext context)
  {
    if (bys.length == 0) {
      return new ArrayList();
    }
    
    List<WebElement> elems = null;
    for (By by : bys) {
      List<WebElement> newElems = new ArrayList();
      
      if (elems == null) {
        newElems.addAll(by.findElements(context));
      } else {
        for (WebElement elem : elems) {
          newElems.addAll(elem.findElements(by));
        }
      }
      elems = newElems;
    }
    
    return elems;
  }
  
  public String toString()
  {
    StringBuilder stringBuilder = new StringBuilder("By.chained(");
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
