package org.openqa.selenium;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
















public class UnhandledAlertException
  extends WebDriverException
{
  private final String alertText;
  
  public UnhandledAlertException(String message)
  {
    this(message, null);
  }
  
  public UnhandledAlertException(String message, String alertText) {
    super(message + ": " + alertText);
    this.alertText = alertText;
  }
  


  public String getAlertText()
  {
    return alertText;
  }
  
  @Beta
  public Map<String, String> getAlert()
  {
    HashMap<String, String> toReturn = new HashMap();
    toReturn.put("text", getAlertText());
    return Collections.unmodifiableMap(toReturn);
  }
}
