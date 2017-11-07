package org.openqa.selenium.support.pagefactory;

import java.util.HashSet;
import java.util.Set;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ByIdOrName;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.How;


































public abstract class AbstractAnnotations
{
  public AbstractAnnotations() {}
  
  public abstract By buildBy();
  
  public abstract boolean isLookupCached();
  
  protected By buildByFromFindBys(FindBys findBys)
  {
    assertValidFindBys(findBys);
    
    FindBy[] findByArray = findBys.value();
    By[] byArray = new By[findByArray.length];
    for (int i = 0; i < findByArray.length; i++) {
      byArray[i] = buildByFromFindBy(findByArray[i]);
    }
    
    return new ByChained(byArray);
  }
  
  protected By buildBysFromFindByOneOf(FindAll findBys) {
    assertValidFindAll(findBys);
    
    FindBy[] findByArray = findBys.value();
    By[] byArray = new By[findByArray.length];
    for (int i = 0; i < findByArray.length; i++) {
      byArray[i] = buildByFromFindBy(findByArray[i]);
    }
    
    return new ByAll(byArray);
  }
  
  protected By buildByFromFindBy(FindBy findBy) {
    assertValidFindBy(findBy);
    
    By ans = buildByFromShortFindBy(findBy);
    if (ans == null) {
      ans = buildByFromLongFindBy(findBy);
    }
    
    return ans;
  }
  
  protected By buildByFromLongFindBy(FindBy findBy) {
    How how = findBy.how();
    String using = findBy.using();
    
    switch (1.$SwitchMap$org$openqa$selenium$support$How[how.ordinal()]) {
    case 1: 
      return By.className(using);
    
    case 2: 
      return By.cssSelector(using);
    
    case 3: 
    case 4: 
      return By.id(using);
    
    case 5: 
      return new ByIdOrName(using);
    
    case 6: 
      return By.linkText(using);
    
    case 7: 
      return By.name(using);
    
    case 8: 
      return By.partialLinkText(using);
    
    case 9: 
      return By.tagName(using);
    
    case 10: 
      return By.xpath(using);
    }
    
    

    throw new IllegalArgumentException("Cannot determine how to locate element ");
  }
  
  protected By buildByFromShortFindBy(FindBy findBy)
  {
    if (!"".equals(findBy.className())) {
      return By.className(findBy.className());
    }
    if (!"".equals(findBy.css())) {
      return By.cssSelector(findBy.css());
    }
    if (!"".equals(findBy.id())) {
      return By.id(findBy.id());
    }
    if (!"".equals(findBy.linkText())) {
      return By.linkText(findBy.linkText());
    }
    if (!"".equals(findBy.name())) {
      return By.name(findBy.name());
    }
    if (!"".equals(findBy.partialLinkText())) {
      return By.partialLinkText(findBy.partialLinkText());
    }
    if (!"".equals(findBy.tagName())) {
      return By.tagName(findBy.tagName());
    }
    if (!"".equals(findBy.xpath())) {
      return By.xpath(findBy.xpath());
    }
    
    return null;
  }
  
  private void assertValidFindBys(FindBys findBys) {
    for (FindBy findBy : findBys.value()) {
      assertValidFindBy(findBy);
    }
  }
  
  private void assertValidFindAll(FindAll findBys) {
    for (FindBy findBy : findBys.value()) {
      assertValidFindBy(findBy);
    }
  }
  
  private void assertValidFindBy(FindBy findBy) {
    if ((findBy.how() != null) && 
      (findBy.using() == null)) {
      throw new IllegalArgumentException("If you set the 'how' property, you must also set 'using'");
    }
    


    Set<String> finders = new HashSet();
    if (!"".equals(findBy.using())) finders.add("how: " + findBy.using());
    if (!"".equals(findBy.className())) finders.add("class name:" + findBy.className());
    if (!"".equals(findBy.css())) finders.add("css:" + findBy.css());
    if (!"".equals(findBy.id())) finders.add("id: " + findBy.id());
    if (!"".equals(findBy.linkText())) finders.add("link text: " + findBy.linkText());
    if (!"".equals(findBy.name())) finders.add("name: " + findBy.name());
    if (!"".equals(findBy.partialLinkText()))
      finders.add("partial link text: " + findBy.partialLinkText());
    if (!"".equals(findBy.tagName())) finders.add("tag name: " + findBy.tagName());
    if (!"".equals(findBy.xpath())) { finders.add("xpath: " + findBy.xpath());
    }
    
    if (finders.size() > 1)
    {
      throw new IllegalArgumentException(String.format("You must specify at most one location strategy. Number found: %d (%s)", new Object[] {
        Integer.valueOf(finders.size()), finders.toString() }));
    }
  }
}
