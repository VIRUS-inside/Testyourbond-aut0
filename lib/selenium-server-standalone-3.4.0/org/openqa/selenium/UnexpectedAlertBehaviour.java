package org.openqa.selenium;


















public enum UnexpectedAlertBehaviour
{
  ACCEPT("accept"), 
  DISMISS("dismiss"), 
  IGNORE("ignore");
  
  private String text;
  
  private UnexpectedAlertBehaviour(String text)
  {
    this.text = text;
  }
  
  public String toString()
  {
    return String.valueOf(text);
  }
  
  public static UnexpectedAlertBehaviour fromString(String text) {
    if (text != null) {
      for (UnexpectedAlertBehaviour b : values()) {
        if (text.equalsIgnoreCase(text)) {
          return b;
        }
      }
    }
    return null;
  }
}
