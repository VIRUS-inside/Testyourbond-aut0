package org.openqa.selenium;




















public enum ScreenOrientation
{
  LANDSCAPE("landscape"), 
  PORTRAIT("portrait");
  
  private final String value;
  
  private ScreenOrientation(String value) {
    this.value = value;
  }
  
  public String value() {
    return value;
  }
}
