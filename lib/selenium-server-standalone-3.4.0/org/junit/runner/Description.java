package org.junit.runner;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


















public class Description
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private static final Pattern METHOD_AND_CLASS_NAME_PATTERN = Pattern.compile("([\\s\\S]*)\\((.*)\\)");
  








  public static Description createSuiteDescription(String name, Annotation... annotations)
  {
    return new Description(null, name, annotations);
  }
  








  public static Description createSuiteDescription(String name, Serializable uniqueId, Annotation... annotations)
  {
    return new Description(null, name, uniqueId, annotations);
  }
  










  public static Description createTestDescription(String className, String name, Annotation... annotations)
  {
    return new Description(null, formatDisplayName(name, className), annotations);
  }
  








  public static Description createTestDescription(Class<?> clazz, String name, Annotation... annotations)
  {
    return new Description(clazz, formatDisplayName(name, clazz.getName()), annotations);
  }
  








  public static Description createTestDescription(Class<?> clazz, String name)
  {
    return new Description(clazz, formatDisplayName(name, clazz.getName()), new Annotation[0]);
  }
  






  public static Description createTestDescription(String className, String name, Serializable uniqueId)
  {
    return new Description(null, formatDisplayName(name, className), uniqueId, new Annotation[0]);
  }
  
  private static String formatDisplayName(String name, String className) {
    return String.format("%s(%s)", new Object[] { name, className });
  }
  





  public static Description createSuiteDescription(Class<?> testClass)
  {
    return new Description(testClass, testClass.getName(), testClass.getAnnotations());
  }
  



  public static final Description EMPTY = new Description(null, "No Tests", new Annotation[0]);
  





  public static final Description TEST_MECHANISM = new Description(null, "Test mechanism", new Annotation[0]);
  





  private final Collection<Description> fChildren = new ConcurrentLinkedQueue();
  private final String fDisplayName;
  private final Serializable fUniqueId;
  private final Annotation[] fAnnotations;
  private volatile Class<?> fTestClass;
  
  private Description(Class<?> clazz, String displayName, Annotation... annotations) {
    this(clazz, displayName, displayName, annotations);
  }
  
  private Description(Class<?> testClass, String displayName, Serializable uniqueId, Annotation... annotations) {
    if ((displayName == null) || (displayName.length() == 0)) {
      throw new IllegalArgumentException("The display name must not be empty.");
    }
    
    if (uniqueId == null) {
      throw new IllegalArgumentException("The unique id must not be null.");
    }
    
    fTestClass = testClass;
    fDisplayName = displayName;
    fUniqueId = uniqueId;
    fAnnotations = annotations;
  }
  


  public String getDisplayName()
  {
    return fDisplayName;
  }
  




  public void addChild(Description description)
  {
    fChildren.add(description);
  }
  



  public ArrayList<Description> getChildren()
  {
    return new ArrayList(fChildren);
  }
  


  public boolean isSuite()
  {
    return !isTest();
  }
  


  public boolean isTest()
  {
    return fChildren.isEmpty();
  }
  


  public int testCount()
  {
    if (isTest()) {
      return 1;
    }
    int result = 0;
    for (Description child : fChildren) {
      result += child.testCount();
    }
    return result;
  }
  
  public int hashCode()
  {
    return fUniqueId.hashCode();
  }
  
  public boolean equals(Object obj)
  {
    if (!(obj instanceof Description)) {
      return false;
    }
    Description d = (Description)obj;
    return fUniqueId.equals(fUniqueId);
  }
  
  public String toString()
  {
    return getDisplayName();
  }
  


  public boolean isEmpty()
  {
    return equals(EMPTY);
  }
  



  public Description childlessCopy()
  {
    return new Description(fTestClass, fDisplayName, fAnnotations);
  }
  



  public <T extends Annotation> T getAnnotation(Class<T> annotationType)
  {
    for (Annotation each : fAnnotations) {
      if (each.annotationType().equals(annotationType)) {
        return (Annotation)annotationType.cast(each);
      }
    }
    return null;
  }
  


  public Collection<Annotation> getAnnotations()
  {
    return Arrays.asList(fAnnotations);
  }
  



  public Class<?> getTestClass()
  {
    if (fTestClass != null) {
      return fTestClass;
    }
    String name = getClassName();
    if (name == null) {
      return null;
    }
    try {
      fTestClass = Class.forName(name, false, getClass().getClassLoader());
      return fTestClass;
    } catch (ClassNotFoundException e) {}
    return null;
  }
  




  public String getClassName()
  {
    return fTestClass != null ? fTestClass.getName() : methodAndClassNamePatternGroupOrDefault(2, toString());
  }
  



  public String getMethodName()
  {
    return methodAndClassNamePatternGroupOrDefault(1, null);
  }
  
  private String methodAndClassNamePatternGroupOrDefault(int group, String defaultString)
  {
    Matcher matcher = METHOD_AND_CLASS_NAME_PATTERN.matcher(toString());
    return matcher.matches() ? matcher.group(group) : defaultString;
  }
}
