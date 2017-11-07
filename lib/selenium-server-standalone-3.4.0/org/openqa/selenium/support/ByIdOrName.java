package org.openqa.selenium.support;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

















public class ByIdOrName
  extends By
  implements Serializable
{
  private static final long serialVersionUID = 3986638402799576701L;
  private By idFinder;
  private By nameFinder;
  private String idOrName;
  
  public ByIdOrName(String idOrName)
  {
    this.idOrName = idOrName;
    idFinder = By.id(idOrName);
    nameFinder = By.name(idOrName);
  }
  
  public WebElement findElement(SearchContext context)
  {
    try
    {
      return idFinder.findElement(context);
    }
    catch (NoSuchElementException e) {}
    return nameFinder.findElement(context);
  }
  

  public List<WebElement> findElements(SearchContext context)
  {
    List<WebElement> elements = new ArrayList();
    

    elements.addAll(idFinder.findElements(context));
    
    elements.addAll(nameFinder.findElements(context));
    
    return elements;
  }
  
  public String toString()
  {
    return "by id or name \"" + idOrName + '"';
  }
}
