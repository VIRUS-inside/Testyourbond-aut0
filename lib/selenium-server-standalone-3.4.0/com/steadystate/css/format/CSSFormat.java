package com.steadystate.css.format;

import java.util.Arrays;


















public class CSSFormat
{
  public CSSFormat() {}
  
  private static final String NEW_LINE = System.getProperty("line.separator");
  private boolean rgbAsHex_;
  private boolean propertiesInSeparateLines_;
  
  private String propertiesIndent_ = "";
  
  public boolean isRgbAsHex() {
    return rgbAsHex_;
  }
  
  public CSSFormat setRgbAsHex(boolean rgbAsHex) {
    rgbAsHex_ = rgbAsHex;
    return this;
  }
  
  public boolean getPropertiesInSeparateLines() {
    return propertiesInSeparateLines_;
  }
  
  public String getPropertiesIndent() {
    return propertiesIndent_;
  }
  







  public CSSFormat setPropertiesInSeparateLines(int anIndent)
  {
    propertiesInSeparateLines_ = (anIndent > -1);
    
    if (anIndent > 0) {
      char[] chars = new char[anIndent];
      Arrays.fill(chars, ' ');
      propertiesIndent_ = new String(chars);
    }
    else {
      propertiesIndent_ = "";
    }
    return this;
  }
  
  public String getNewLine() {
    return NEW_LINE;
  }
}
