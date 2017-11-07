package com.gargoylesoftware.htmlunit.css;

import java.io.Serializable;
































public class StyleElement
  implements Comparable<StyleElement>, Serializable
{
  public static final String PRIORITY_IMPORTANT = "important";
  private static long ElementIndex_ = 0L;
  

  private final String name_;
  

  private final String value_;
  

  private final String priority_;
  
  private final long index_;
  
  private final SelectorSpecificity specificity_;
  

  public StyleElement(String name, String value, String priority, SelectorSpecificity specificity, long index)
  {
    name_ = name;
    value_ = value;
    priority_ = priority;
    specificity_ = specificity;
    index_ = index;
  }
  







  public StyleElement(String name, String value, String priority, SelectorSpecificity specificity)
  {
    this(name, value, priority, specificity, ElementIndex_++);
  }
  




  public StyleElement(String name, String value)
  {
    this(name, value, "", SelectorSpecificity.FROM_STYLE_ATTRIBUTE);
  }
  



  public String getName()
  {
    return name_;
  }
  



  public String getValue()
  {
    return value_;
  }
  



  public String getPriority()
  {
    return priority_;
  }
  



  public SelectorSpecificity getSpecificity()
  {
    return specificity_;
  }
  



  public long getIndex()
  {
    return index_;
  }
  



  public String toString()
  {
    return "[" + index_ + "]" + name_ + "=" + value_;
  }
  



  public int compareTo(StyleElement e)
  {
    if (e != null) {
      long styleIndex = index_;
      
      return Long.compare(index_, styleIndex);
    }
    return 1;
  }
}
