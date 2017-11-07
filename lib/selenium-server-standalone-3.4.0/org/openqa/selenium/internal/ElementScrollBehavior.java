package org.openqa.selenium.internal;


















public enum ElementScrollBehavior
{
  TOP(0), 
  BOTTOM(1);
  
  private int value;
  
  private ElementScrollBehavior(int value)
  {
    this.value = value;
  }
  
  public String toString()
  {
    return String.valueOf(value);
  }
  
  public static ElementScrollBehavior fromString(String text) {
    for (ElementScrollBehavior b : ) {
      if (text.equalsIgnoreCase(b.toString())) {
        return b;
      }
    }
    return null;
  }
  
  public int getValue() {
    return value;
  }
}
