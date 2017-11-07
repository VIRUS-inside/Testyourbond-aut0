package com.gargoylesoftware.htmlunit.javascript.host.intl;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;























class GAEDateTimeFormatter
  implements AbstractDateTimeFormatter
{
  private DateFormat format_;
  
  GAEDateTimeFormatter(String locale, BrowserVersion browserVersion, String pattern)
  {
    format_ = new SimpleDateFormat(pattern);
    if ((locale.startsWith("ar")) && (
      (!browserVersion.hasFeature(BrowserVersionFeatures.JS_DATE_AR_DZ_ASCII_DIGITS)) || (
      (!"ar-DZ".equals(locale)) && 
      (!"ar-LY".equals(locale)) && 
      (!"ar-MA".equals(locale)) && 
      (!"ar-TN".equals(locale))))) {
      setZeroDigit('٠');
    }
  }
  
  private void setZeroDigit(char zeroDigit) {
    DecimalFormat df = (DecimalFormat)format_.getNumberFormat();
    DecimalFormatSymbols dfs = df.getDecimalFormatSymbols();
    dfs.setZeroDigit(zeroDigit);
    df.setDecimalFormatSymbols(dfs);
  }
  



  public String format(Date date)
  {
    return format_.format(date);
  }
}
