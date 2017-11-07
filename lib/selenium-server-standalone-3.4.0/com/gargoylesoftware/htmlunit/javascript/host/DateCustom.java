package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.FastDateFormat;



























public final class DateCustom
{
  private static final TimeZone UTC_TIME_ZONE = TimeZone.getTimeZone("UTC");
  





  private DateCustom() {}
  




  public static String toLocaleDateString(Context context, Scriptable thisObj, Object[] args, Function function)
  {
    BrowserVersion browserVersion = ((Window)thisObj.getParentScope()).getBrowserVersion();
    String formatString;
    String formatString; if (browserVersion.hasFeature(BrowserVersionFeatures.JS_DATE_WITH_LEFT_TO_RIGHT_MARK))
    {
      formatString = "‎M‎/‎d‎/‎yyyy";
    } else { String formatString;
      if (browserVersion.hasFeature(BrowserVersionFeatures.JS_DATE_LOCALE_DATE_SHORT)) {
        formatString = "M/d/yyyy";
      }
      else
        formatString = "EEEE, MMMM dd, yyyy";
    }
    FastDateFormat format = FastDateFormat.getInstance(formatString, getLocale(browserVersion));
    return format.format(getDateValue(thisObj));
  }
  









  public static String toLocaleTimeString(Context context, Scriptable thisObj, Object[] args, Function function)
  {
    BrowserVersion browserVersion = ((Window)thisObj.getParentScope()).getBrowserVersion();
    String formatString;
    String formatString; if (browserVersion.hasFeature(BrowserVersionFeatures.JS_DATE_WITH_LEFT_TO_RIGHT_MARK))
    {
      formatString = "‎h‎:‎mm‎:‎ss‎ ‎a";
    }
    else {
      formatString = "h:mm:ss a";
    }
    FastDateFormat format = FastDateFormat.getInstance(formatString, getLocale(browserVersion));
    return format.format(getDateValue(thisObj));
  }
  








  public static String toUTCString(Context context, Scriptable thisObj, Object[] args, Function function)
  {
    Date date = new Date(getDateValue(thisObj));
    return DateFormatUtils.format(date, "EEE, d MMM yyyy HH:mm:ss z", UTC_TIME_ZONE, Locale.ENGLISH);
  }
  
  private static long getDateValue(Scriptable thisObj) {
    Date date = (Date)Context.jsToJava(thisObj, Date.class);
    return date.getTime();
  }
  
  private static Locale getLocale(BrowserVersion browserVersion) {
    return new Locale(browserVersion.getSystemLanguage());
  }
}
