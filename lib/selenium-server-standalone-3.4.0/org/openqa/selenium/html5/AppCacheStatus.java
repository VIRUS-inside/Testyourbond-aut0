package org.openqa.selenium.html5;




















public enum AppCacheStatus
{
  UNCACHED(0), 
  IDLE(1), 
  CHECKING(2), 
  DOWNLOADING(3), 
  UPDATE_READY(4), 
  OBSOLETE(5);
  
  private final int value;
  
  private AppCacheStatus(int value) {
    this.value = value;
  }
  
  public int value() {
    return value;
  }
  





  public static AppCacheStatus getEnum(int value)
  {
    for (AppCacheStatus status : ) {
      if (value == status.value()) {
        return status;
      }
    }
    return null;
  }
  
  public static AppCacheStatus getEnum(String value) {
    for (AppCacheStatus status : ) {
      if (status.toString().equalsIgnoreCase(value)) {
        return status;
      }
    }
    return null;
  }
}
