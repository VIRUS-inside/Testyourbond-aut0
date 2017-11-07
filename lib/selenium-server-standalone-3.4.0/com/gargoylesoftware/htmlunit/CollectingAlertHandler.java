package com.gargoylesoftware.htmlunit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;





















public class CollectingAlertHandler
  implements AlertHandler, Serializable
{
  private final List<String> collectedAlerts_;
  
  public CollectingAlertHandler()
  {
    this(new ArrayList());
  }
  




  public CollectingAlertHandler(List<String> list)
  {
    WebAssert.notNull("list", list);
    collectedAlerts_ = list;
  }
  







  public void handleAlert(Page page, String message)
  {
    collectedAlerts_.add(message);
  }
  



  public List<String> getCollectedAlerts()
  {
    return collectedAlerts_;
  }
  


  public void clear()
  {
    collectedAlerts_.clear();
  }
}
