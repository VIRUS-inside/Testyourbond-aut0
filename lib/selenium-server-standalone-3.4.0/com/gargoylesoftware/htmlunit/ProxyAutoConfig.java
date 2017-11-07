package com.gargoylesoftware.htmlunit;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.FunctionObject;
import net.sourceforge.htmlunit.corejs.javascript.NativeFunction;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;






















public final class ProxyAutoConfig
{
  private static final Pattern DOT_SPLIT_PATTERN = Pattern.compile("\\.");
  



  private ProxyAutoConfig() {}
  



  public static String evaluate(String content, URL url)
  {
    Context cx = Context.enter();
    try {
      ProxyAutoConfig config = new ProxyAutoConfig();
      Scriptable scope = cx.initStandardObjects();
      
      config.defineMethod("isPlainHostName", scope);
      config.defineMethod("dnsDomainIs", scope);
      config.defineMethod("localHostOrDomainIs", scope);
      config.defineMethod("isResolvable", scope);
      config.defineMethod("isInNet", scope);
      config.defineMethod("dnsResolve", scope);
      config.defineMethod("myIpAddress", scope);
      config.defineMethod("dnsDomainLevels", scope);
      config.defineMethod("shExpMatch", scope);
      config.defineMethod("weekdayRange", scope);
      config.defineMethod("dateRange", scope);
      config.defineMethod("timeRange", scope);
      
      cx.evaluateString(scope, "var ProxyConfig = function() {}; ProxyConfig.bindings = {}", "<init>", 1, null);
      cx.evaluateString(scope, content, "<Proxy Auto-Config>", 1, null);
      Object[] functionArgs = { url.toExternalForm(), url.getHost() };
      Object fObj = scope.get("FindProxyForURL", scope);
      
      NativeFunction f = (NativeFunction)fObj;
      Object result = f.call(cx, scope, scope, functionArgs);
      return Context.toString(result);
    }
    finally {
      Context.exit();
    }
  }
  
  private void defineMethod(String methodName, Scriptable scope) {
    for (Method method : getClass().getMethods()) {
      if (method.getName().equals(methodName)) {
        FunctionObject functionObject = new FunctionObject(methodName, method, scope);
        ((ScriptableObject)scope).defineProperty(methodName, functionObject, 0);
      }
    }
  }
  




  public static boolean isPlainHostName(String host)
  {
    return host.indexOf('.') == -1;
  }
  





  public static boolean dnsDomainIs(String host, String domain)
  {
    return host.endsWith(domain);
  }
  







  public static boolean localHostOrDomainIs(String host, String hostdom)
  {
    return ((host.length() > 1) && (host.equals(hostdom))) || ((host.indexOf('.') == -1) && (hostdom.startsWith(host)));
  }
  




  public static boolean isResolvable(String host)
  {
    return dnsResolve(host) != null;
  }
  








  public static boolean isInNet(String host, String pattern, String mask)
  {
    String dnsResolve = dnsResolve(host);
    if (dnsResolve == null) {
      return false;
    }
    
    String[] hostTokens = DOT_SPLIT_PATTERN.split(dnsResolve(host));
    String[] patternTokens = DOT_SPLIT_PATTERN.split(pattern);
    String[] maskTokens = DOT_SPLIT_PATTERN.split(mask);
    for (int i = 0; i < hostTokens.length; i++) {
      if ((Integer.parseInt(maskTokens[i]) != 0) && (!hostTokens[i].equals(patternTokens[i]))) {
        return false;
      }
    }
    return true;
  }
  



  public static String dnsResolve(String host)
  {
    try
    {
      return InetAddress.getByName(host).getHostAddress();
    }
    catch (Exception e) {}
    return null;
  }
  



  public static String myIpAddress()
  {
    try
    {
      return InetAddress.getLocalHost().getHostAddress();
    }
    catch (Exception e) {
      throw Context.throwAsScriptRuntimeEx(e);
    }
  }
  




  public static int dnsDomainLevels(String host)
  {
    int levels = 0;
    for (int i = host.length() - 1; i >= 0; i--) {
      if (host.charAt(i) == '.') {
        levels++;
      }
    }
    return levels;
  }
  





  public static boolean shExpMatch(String str, String shexp)
  {
    String regexp = shexp.replace(".", "\\.").replace("*", ".*").replace("?", ".");
    return str.matches(regexp);
  }
  






  public static boolean weekdayRange(String wd1, Object wd2, Object gmt)
  {
    TimeZone timezone = TimeZone.getDefault();
    if (("GMT".equals(Context.toString(gmt))) || ("GMT".equals(Context.toString(wd2)))) {
      timezone = TimeZone.getTimeZone("GMT");
    }
    if ((wd2 == Undefined.instance) || ("GMT".equals(Context.toString(wd2)))) {
      wd2 = wd1;
    }
    Calendar calendar = Calendar.getInstance(timezone);
    for (int i = 0; i < 7; i++) {
      String day = new SimpleDateFormat("EEE", Locale.ROOT)
        .format(calendar.getTime()).toUpperCase(Locale.ROOT);
      if (day.equals(wd2)) {
        return true;
      }
      if (day.equals(wd1)) {
        return i == 0;
      }
      calendar.add(7, 1);
    }
    return false;
  }
  











  public static boolean dateRange(String value1, Object value2, Object value3, Object value4, Object value5, Object value6, Object value7)
  {
    Object[] values = { value1, value2, value3, value4, value5, value6, value7 };
    TimeZone timezone = TimeZone.getDefault();
    


    for (int length = values.length - 1; length >= 0; length--) {
      if ("GMT".equals(Context.toString(values[length]))) {
        timezone = TimeZone.getTimeZone("GMT");
        break;
      }
      if (values[length] != Undefined.instance) {
        length++;
        break;
      } }
    Calendar cal2;
    Calendar cal2;
    Calendar cal2;
    Calendar cal1;
    Calendar cal2;
    switch (length) {
    case 1: 
      int day = getSmallInt(value1);
      int month = dateRange_getMonth(value1);
      int year = dateRange_getYear(value1);
      Calendar cal1 = dateRange_createCalendar(timezone, day, month, year);
      cal2 = (Calendar)cal1.clone();
      break;
    
    case 2: 
      int day1 = getSmallInt(value1);
      int month1 = dateRange_getMonth(value1);
      int year1 = dateRange_getYear(value1);
      Calendar cal1 = dateRange_createCalendar(timezone, day1, month1, year1);
      int day2 = getSmallInt(value2);
      int month2 = dateRange_getMonth(value2);
      int year2 = dateRange_getYear(value2);
      cal2 = dateRange_createCalendar(timezone, day2, month2, year2);
      break;
    
    case 4: 
      int day1 = getSmallInt(value1);
      Calendar cal2; if (day1 != -1) {
        int month1 = dateRange_getMonth(value2);
        int day2 = getSmallInt(value3);
        int month2 = dateRange_getMonth(value4);
        Calendar cal1 = dateRange_createCalendar(timezone, day1, month1, -1);
        cal2 = dateRange_createCalendar(timezone, day2, month2, -1);
      }
      else {
        int month1 = dateRange_getMonth(value1);
        int year1 = dateRange_getMonth(value2);
        int month2 = getSmallInt(value3);
        int year2 = dateRange_getMonth(value4);
        Calendar cal1 = dateRange_createCalendar(timezone, -1, month1, year1);
        cal2 = dateRange_createCalendar(timezone, -1, month2, year2);
      }
      break;
    case 3: 
    default: 
      int day1 = getSmallInt(value1);
      int month1 = dateRange_getMonth(value2);
      int year1 = dateRange_getYear(value3);
      int day2 = getSmallInt(value4);
      int month2 = dateRange_getMonth(value5);
      int year2 = dateRange_getYear(value6);
      cal1 = dateRange_createCalendar(timezone, day1, month1, year1);
      cal2 = dateRange_createCalendar(timezone, day2, month2, year2);
    }
    
    Calendar today = Calendar.getInstance(timezone);
    today.set(14, 0);
    today.set(13, 0);
    cal1.set(14, 0);
    cal1.set(13, 0);
    cal2.set(14, 0);
    cal2.set(13, 0);
    return (today.equals(cal1)) || ((today.after(cal1)) && (today.before(cal2))) || (today.equals(cal2));
  }
  
  private static Calendar dateRange_createCalendar(TimeZone timezone, int day, int month, int year)
  {
    Calendar calendar = Calendar.getInstance(timezone);
    if (day != -1) {
      calendar.set(5, day);
    }
    if (month != -1) {
      calendar.set(2, month);
    }
    if (year != -1) {
      calendar.set(1, year);
    }
    return calendar;
  }
  
  private static int getSmallInt(Object object) {
    String s = Context.toString(object);
    if (Character.isDigit(s.charAt(0))) {
      int i = Integer.parseInt(s);
      if (i < 70) {
        return i;
      }
    }
    return -1;
  }
  
  private static int dateRange_getMonth(Object object) {
    String s = Context.toString(object);
    if (Character.isLetter(s.charAt(0))) {
      try {
        Calendar cal = Calendar.getInstance(Locale.ROOT);
        cal.clear();
        cal.setTime(new SimpleDateFormat("MMM", Locale.ROOT).parse(s));
        return cal.get(2);
      }
      catch (Exception localException) {}
    }
    

    return -1;
  }
  
  private static int dateRange_getYear(Object object) {
    String s = Context.toString(object);
    if (Character.isDigit(s.charAt(0))) {
      int i = Integer.parseInt(s);
      if (i > 1000) {
        return i;
      }
    }
    return -1;
  }
  











  public static boolean timeRange(String value1, Object value2, Object value3, Object value4, Object value5, Object value6, Object value7)
  {
    Object[] values = { value1, value2, value3, value4, value5, value6, value7 };
    TimeZone timezone = TimeZone.getDefault();
    


    for (int length = values.length - 1; length >= 0; length--) {
      if ("GMT".equals(Context.toString(values[length]))) {
        timezone = TimeZone.getTimeZone("GMT");
        break;
      }
      if (values[length] != Undefined.instance) {
        length++;
        break;
      }
    }
    Calendar cal2;
    Calendar cal2;
    Calendar cal1;
    Calendar cal2;
    switch (length) {
    case 1: 
      int hour1 = getSmallInt(value1);
      Calendar cal1 = timeRange_createCalendar(timezone, hour1, -1, -1);
      Calendar cal2 = (Calendar)cal1.clone();
      cal2.add(11, 1);
      break;
    
    case 2: 
      int hour1 = getSmallInt(value1);
      Calendar cal1 = timeRange_createCalendar(timezone, hour1, -1, -1);
      int hour2 = getSmallInt(value2);
      cal2 = timeRange_createCalendar(timezone, hour2, -1, -1);
      break;
    
    case 4: 
      int hour1 = getSmallInt(value1);
      int min1 = getSmallInt(value2);
      int hour2 = getSmallInt(value3);
      int min2 = getSmallInt(value4);
      Calendar cal1 = dateRange_createCalendar(timezone, hour1, min1, -1);
      cal2 = dateRange_createCalendar(timezone, hour2, min2, -1);
      break;
    case 3: 
    default: 
      int hour1 = getSmallInt(value1);
      int min1 = getSmallInt(value2);
      int second1 = getSmallInt(value3);
      int hour2 = getSmallInt(value4);
      int min2 = getSmallInt(value5);
      int second2 = getSmallInt(value6);
      cal1 = dateRange_createCalendar(timezone, hour1, min1, second1);
      cal2 = dateRange_createCalendar(timezone, hour2, min2, second2);
    }
    
    Calendar now = Calendar.getInstance(timezone);
    return (now.equals(cal1)) || ((now.after(cal1)) && (now.before(cal2))) || (now.equals(cal2));
  }
  
  private static Calendar timeRange_createCalendar(TimeZone timezone, int hour, int minute, int second)
  {
    Calendar calendar = Calendar.getInstance(timezone);
    if (hour != -1) {
      calendar.set(11, hour);
    }
    if (minute != -1) {
      calendar.set(12, minute);
    }
    if (second != -1) {
      calendar.set(13, second);
    }
    return calendar;
  }
}
