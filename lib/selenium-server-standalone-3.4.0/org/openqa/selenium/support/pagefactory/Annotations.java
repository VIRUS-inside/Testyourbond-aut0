package org.openqa.selenium.support.pagefactory;

import java.lang.reflect.Field;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ByIdOrName;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;



















public class Annotations
  extends AbstractAnnotations
{
  private Field field;
  
  public Annotations(Field field)
  {
    this.field = field;
  }
  




  public boolean isLookupCached()
  {
    return field.getAnnotation(CacheLookup.class) != null;
  }
  








  public By buildBy()
  {
    assertValidAnnotations();
    
    By ans = null;
    
    FindBys findBys = (FindBys)field.getAnnotation(FindBys.class);
    if (findBys != null) {
      ans = buildByFromFindBys(findBys);
    }
    
    FindAll findAll = (FindAll)field.getAnnotation(FindAll.class);
    if ((ans == null) && (findAll != null)) {
      ans = buildBysFromFindByOneOf(findAll);
    }
    
    FindBy findBy = (FindBy)field.getAnnotation(FindBy.class);
    if ((ans == null) && (findBy != null)) {
      ans = buildByFromFindBy(findBy);
    }
    
    if (ans == null) {
      ans = buildByFromDefault();
    }
    
    if (ans == null) {
      throw new IllegalArgumentException("Cannot determine how to locate element " + field);
    }
    
    return ans;
  }
  
  protected Field getField() {
    return field;
  }
  
  protected By buildByFromDefault() {
    return new ByIdOrName(field.getName());
  }
  
  protected void assertValidAnnotations() {
    FindBys findBys = (FindBys)field.getAnnotation(FindBys.class);
    FindAll findAll = (FindAll)field.getAnnotation(FindAll.class);
    FindBy findBy = (FindBy)field.getAnnotation(FindBy.class);
    if ((findBys != null) && (findBy != null)) {
      throw new IllegalArgumentException("If you use a '@FindBys' annotation, you must not also use a '@FindBy' annotation");
    }
    
    if ((findAll != null) && (findBy != null)) {
      throw new IllegalArgumentException("If you use a '@FindAll' annotation, you must not also use a '@FindBy' annotation");
    }
    
    if ((findAll != null) && (findBys != null)) {
      throw new IllegalArgumentException("If you use a '@FindAll' annotation, you must not also use a '@FindBys' annotation");
    }
  }
}
