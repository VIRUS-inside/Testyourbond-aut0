package com.gargoylesoftware.htmlunit.javascript.host.css;

import com.gargoylesoftware.htmlunit.BrowserVersion;




















class BrowserConfiguration
{
  private String browserFamily_;
  private String defaultValue_;
  
  BrowserConfiguration() {}
  
  private int minVersionNumber_ = -1;
  private int maxVersionNumber_ = Integer.MAX_VALUE;
  private boolean iteratable_ = true;
  
  static BrowserConfiguration ff(String defaultValue) {
    BrowserConfiguration browserConfiguration = new BrowserConfiguration();
    browserFamily_ = "FF";
    defaultValue_ = defaultValue;
    return browserConfiguration;
  }
  
  static BrowserConfiguration ie(String defaultValue) {
    BrowserConfiguration browserConfiguration = new BrowserConfiguration();
    browserFamily_ = "IE";
    defaultValue_ = defaultValue;
    return browserConfiguration;
  }
  
  static BrowserConfiguration chrome(String defaultValue) {
    BrowserConfiguration browserConfiguration = new BrowserConfiguration();
    browserFamily_ = "Chrome";
    defaultValue_ = defaultValue;
    return browserConfiguration;
  }
  
  static BrowserConfiguration ff52up(String defaultValue) {
    return ff(defaultValue).startingWith(52);
  }
  


  static BrowserConfiguration getMatchingConfiguration(BrowserVersion browserVersion, BrowserConfiguration[] browserConfigurations)
  {
    for (BrowserConfiguration browserConfiguration : browserConfigurations) {
      if ((browserVersion.getNickname().startsWith(browserFamily_)) && 
        (browserVersion.getBrowserVersionNumeric() >= minVersionNumber_) && 
        (browserVersion.getBrowserVersionNumeric() <= maxVersionNumber_)) {
        return browserConfiguration;
      }
    }
    
    return null;
  }
  
  String getDefaultValue() {
    return defaultValue_;
  }
  
  BrowserConfiguration startingWith(int minVersionNumber) {
    if (minVersionNumber_ != -1) {
      throw new IllegalStateException("startingWith has already been set to " + minVersionNumber_);
    }
    minVersionNumber_ = minVersionNumber;
    return this;
  }
  
  BrowserConfiguration upTo(int maxVersionNumber) {
    if (maxVersionNumber_ != Integer.MAX_VALUE) {
      throw new IllegalStateException("below has already been set to " + maxVersionNumber_);
    }
    maxVersionNumber_ = maxVersionNumber;
    return this;
  }
  
  BrowserConfiguration setIteratable(boolean iteratable) {
    iteratable_ = iteratable;
    return this;
  }
  
  boolean isIteratable() {
    return iteratable_;
  }
}
