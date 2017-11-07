package com.gargoylesoftware.htmlunit.javascript.host.intl;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import java.time.Instant;
import java.time.ZoneId;
import java.time.chrono.Chronology;
import java.time.chrono.HijrahChronology;
import java.time.chrono.JapaneseChronology;
import java.time.chrono.ThaiBuddhistChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DecimalStyle;
import java.time.temporal.TemporalAccessor;
import java.util.Date;




















class DefaultDateTimeFormatter
  implements AbstractDateTimeFormatter
{
  private DateTimeFormatter formatter_;
  private Chronology chronology_;
  
  DefaultDateTimeFormatter(String locale, BrowserVersion browserVersion, String pattern)
  {
    formatter_ = DateTimeFormatter.ofPattern(pattern);
    DecimalStyle decimalStyle; if ((locale.startsWith("ar")) && (
      (!browserVersion.hasFeature(BrowserVersionFeatures.JS_DATE_AR_DZ_ASCII_DIGITS)) || (
      (!"ar-DZ".equals(locale)) && 
      (!"ar-LY".equals(locale)) && 
      (!"ar-MA".equals(locale)) && 
      (!"ar-TN".equals(locale))))) {
      decimalStyle = DecimalStyle.STANDARD.withZeroDigit('٠');
      formatter_ = formatter_.withDecimalStyle(decimalStyle);
    }
    
    switch ((decimalStyle = locale).hashCode()) {case 3121:  if (decimalStyle.equals("ar")) break; break; case 3700:  if (decimalStyle.equals("th")) {} break; case 93023594:  if (decimalStyle.equals("ar-SA")) {} break; case 93023597:  if (decimalStyle.equals("ar-SD")) {} break; case 110272621:  if (decimalStyle.equals("th-TH")) {} case 837927031:  if ((goto 311) && (decimalStyle.equals("ja-JP-u-ca-japanese")))
      {
        chronology_ = JapaneseChronology.INSTANCE;
        return;
        

        if (browserVersion.hasFeature(BrowserVersionFeatures.JS_DATE_WITH_LEFT_TO_RIGHT_MARK)) {
          chronology_ = HijrahChronology.INSTANCE;
          
          return;
          

          if (browserVersion.hasFeature(BrowserVersionFeatures.JS_DATE_AR_DZ_ASCII_DIGITS)) {
            chronology_ = HijrahChronology.INSTANCE;
            
            return;
            

            if (browserVersion.hasFeature(BrowserVersionFeatures.JS_DATE_WITH_LEFT_TO_RIGHT_MARK)) {
              chronology_ = HijrahChronology.INSTANCE;
              
              return;
              


              chronology_ = ThaiBuddhistChronology.INSTANCE;
            }
          }
        }
      }
      break;
    }
    
  }
  
  public String format(Date date)
  {
    TemporalAccessor zonedDateTime = date.toInstant().atZone(ZoneId.systemDefault());
    if (chronology_ != null) {
      zonedDateTime = chronology_.date(zonedDateTime);
    }
    return formatter_.format(zonedDateTime);
  }
}
