package com.gargoylesoftware.htmlunit.javascript.host.intl;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.gae.GAEUtils;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.NativeArray;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;























@JsxClass
public class DateTimeFormat
  extends SimpleScriptable
{
  private static Map<String, String> FF_45_FORMATS_ = new HashMap();
  private static Map<String, String> CHROME_FORMATS_ = new HashMap();
  private static Map<String, String> IE_FORMATS_ = new HashMap();
  private AbstractDateTimeFormatter formatter_;
  
  static
  {
    String ddSlash = "‎dd‎/‎MM‎/‎YYYY";
    String ddDash = "‎dd‎-‎MM‎-‎YYYY";
    String ddDot = "‎dd‎.‎MM‎.‎YYYY";
    String ddDotDot = "‎dd‎.‎MM‎.‎YYYY‎.";
    String ddDotBlank = "‎dd‎. ‎MM‎. ‎YYYY";
    String ddDotBlankDot = "‎dd‎. ‎MM‎. ‎YYYY.";
    String mmSlash = "‎MM‎/‎dd‎/‎YYYY";
    String yyyySlash = "‎YYYY‎/‎MM‎/‎dd";
    String yyyyDash = "‎YYYY‎-‎MM‎-‎dd";
    String yyyyDot = "‎YYYY‎.‎MM‎.‎dd";
    String yyyyDotBlankDot = "‎YYYY‎. ‎MM‎. ‎dd.";
    String yyyyDotDot = "‎YYYY‎.‎MM‎.‎dd‎.";
    String rightToLeft = "‏dd‏/‏MM‏/‏YYYY";
    
    FF_45_FORMATS_.put("", "‎MM‎/‎dd‎/‎YYYY");
    FF_45_FORMATS_.put("ar", "dd‏/MM‏/YYYY");
    FF_45_FORMATS_.put("be", "‎dd‎.‎MM‎.‎YYYY");
    FF_45_FORMATS_.put("bg", "‎dd‎.‎MM‎.‎YYYY‎ г.");
    FF_45_FORMATS_.put("ca", "‎dd‎/‎MM‎/‎YYYY");
    FF_45_FORMATS_.put("cs", "‎dd‎. ‎MM‎. ‎YYYY");
    FF_45_FORMATS_.put("da", "‎dd‎/‎MM‎/‎YYYY");
    FF_45_FORMATS_.put("de", "‎dd‎.‎MM‎.‎YYYY");
    FF_45_FORMATS_.put("el", "‎dd‎/‎MM‎/‎YYYY");
    FF_45_FORMATS_.put("en-NZ", "‎dd‎/‎MM‎/‎YYYY");
    FF_45_FORMATS_.put("en-PA", "‎dd‎/‎MM‎/‎YYYY");
    FF_45_FORMATS_.put("en-PR", "‎dd‎/‎MM‎/‎YYYY");
    FF_45_FORMATS_.put("en-AU", "‎dd‎/‎MM‎/‎YYYY");
    FF_45_FORMATS_.put("en-GB", "‎dd‎/‎MM‎/‎YYYY");
    FF_45_FORMATS_.put("en-IE", "‎dd‎/‎MM‎/‎YYYY");
    FF_45_FORMATS_.put("en-IN", "‎dd‎/‎MM‎/‎YYYY");
    FF_45_FORMATS_.put("en-MT", "‎dd‎/‎MM‎/‎YYYY");
    FF_45_FORMATS_.put("en-SG", "‎dd‎/‎MM‎/‎YYYY");
    FF_45_FORMATS_.put("en-ZA", "‎YYYY‎/‎MM‎/‎dd");
    FF_45_FORMATS_.put("es", "‎dd‎/‎MM‎/‎YYYY");
    FF_45_FORMATS_.put("es-CL", "‎dd‎-‎MM‎-‎YYYY");
    FF_45_FORMATS_.put("es-PA", "‎MM‎/‎dd‎/‎YYYY");
    FF_45_FORMATS_.put("es-PR", "‎MM‎/‎dd‎/‎YYYY");
    FF_45_FORMATS_.put("es-US", "‎MM‎/‎dd‎/‎YYYY");
    FF_45_FORMATS_.put("et", "‎dd‎.‎MM‎.‎YYYY");
    FF_45_FORMATS_.put("fi", "‎dd‎.‎MM‎.‎YYYY");
    FF_45_FORMATS_.put("fr", "‎dd‎/‎MM‎/‎YYYY");
    FF_45_FORMATS_.put("fr-CA", "‎YYYY‎-‎MM‎-‎dd");
    FF_45_FORMATS_.put("ga", "‎YYYY‎-‎MM‎-‎dd");
    FF_45_FORMATS_.put("hi", "‎dd‎/‎MM‎/‎YYYY");
    FF_45_FORMATS_.put("hr", "‎dd‎. ‎MM‎. ‎YYYY.");
    FF_45_FORMATS_.put("hu", "‎YYYY‎. ‎MM‎. ‎dd.");
    FF_45_FORMATS_.put("id", "‎dd‎/‎MM‎/‎YYYY");
    FF_45_FORMATS_.put("in", "‎dd‎/‎MM‎/‎YYYY");
    FF_45_FORMATS_.put("is", "‎dd‎.‎MM‎.‎YYYY");
    FF_45_FORMATS_.put("it", "‎dd‎/‎MM‎/‎YYYY");
    FF_45_FORMATS_.put("iw", "‎dd‎.‎MM‎.‎YYYY");
    FF_45_FORMATS_.put("ja", "‎YYYY‎/‎MM‎/‎dd");
    FF_45_FORMATS_.put("ko", "‎YYYY‎. ‎MM‎. ‎dd.");
    FF_45_FORMATS_.put("lt", "‎YYYY‎-‎MM‎-‎dd");
    FF_45_FORMATS_.put("lv", "‎dd‎.‎MM‎.‎YYYY‎.");
    FF_45_FORMATS_.put("mk", "‎dd‎.‎MM‎.‎YYYY");
    FF_45_FORMATS_.put("ms", "‎dd‎/‎MM‎/‎YYYY");
    FF_45_FORMATS_.put("mt", "‎YYYY‎-‎MM‎-‎dd");
    FF_45_FORMATS_.put("nl", "‎dd‎-‎MM‎-‎YYYY");
    FF_45_FORMATS_.put("pl", "‎dd‎.‎MM‎.‎YYYY");
    FF_45_FORMATS_.put("pt", "‎dd‎/‎MM‎/‎YYYY");
    FF_45_FORMATS_.put("ro", "‎dd‎.‎MM‎.‎YYYY");
    FF_45_FORMATS_.put("ru", "‎dd‎.‎MM‎.‎YYYY");
    FF_45_FORMATS_.put("sk", "‎dd‎.‎MM‎.‎YYYY");
    FF_45_FORMATS_.put("sl", "‎dd‎. ‎MM‎. ‎YYYY");
    FF_45_FORMATS_.put("sq", "‎dd‎/‎MM‎/‎YYYY");
    FF_45_FORMATS_.put("sr", "‎dd‎. ‎MM‎. ‎YYYY.");
    FF_45_FORMATS_.put("sv", "‎YYYY‎-‎MM‎-‎dd");
    FF_45_FORMATS_.put("th", "‎dd‎/‎MM‎/‎YYYY");
    FF_45_FORMATS_.put("tr", "‎dd‎.‎MM‎.‎YYYY");
    FF_45_FORMATS_.put("uk", "‎dd‎.‎MM‎.‎YYYY");
    FF_45_FORMATS_.put("vi", "‎dd‎/‎MM‎/‎YYYY");
    FF_45_FORMATS_.put("zh", "‎YYYY‎/‎MM‎/‎dd");
    FF_45_FORMATS_.put("zh-HK", "‎dd‎/‎MM‎/‎YYYY");
    FF_45_FORMATS_.put("zh-SG", "‎YYYY‎年‎MM‎月‎dd‎日");
    
    CHROME_FORMATS_.putAll(FF_45_FORMATS_);
    IE_FORMATS_.putAll(FF_45_FORMATS_);
    
    FF_45_FORMATS_.put("ar-SA", "d‏/M‏/YYYY هـ");
    FF_45_FORMATS_.put("en-CA", "‎YYYY‎-‎MM‎-‎dd");
    FF_45_FORMATS_.put("en-PH", "‎dd‎/‎MM‎/‎YYYY");
    FF_45_FORMATS_.put("es-US", "‎dd‎/‎MM‎/‎YYYY");
    FF_45_FORMATS_.put("ga", "‎dd‎/‎MM‎/‎YYYY");
    FF_45_FORMATS_.put("hr", "‎dd‎.‎MM‎.‎YYYY‎.");
    FF_45_FORMATS_.put("ja-JP-u-ca-japanese", "yy/MM/dd");
    FF_45_FORMATS_.put("sk", "‎dd‎. ‎MM‎. ‎YYYY");
    FF_45_FORMATS_.put("sr", "‎dd‎.‎MM‎.‎YYYY‎.");
    FF_45_FORMATS_.put("sq", "‎dd‎.‎MM‎.‎YYYY");
    
    CHROME_FORMATS_.put("be", "‎YYYY‎-‎MM‎-‎dd");
    CHROME_FORMATS_.put("en-CA", "‎YYYY‎-‎MM‎-‎dd");
    CHROME_FORMATS_.put("en-IE", "‎MM‎/‎dd‎/‎YYYY");
    CHROME_FORMATS_.put("en-MT", "‎MM‎/‎dd‎/‎YYYY");
    CHROME_FORMATS_.put("en-SG", "‎MM‎/‎dd‎/‎YYYY");
    CHROME_FORMATS_.put("es-CL", "‎dd‎/‎MM‎/‎YYYY");
    CHROME_FORMATS_.put("es-PA", "‎dd‎/‎MM‎/‎YYYY");
    CHROME_FORMATS_.put("es-PR", "‎dd‎/‎MM‎/‎YYYY");
    CHROME_FORMATS_.put("es-US", "‎dd‎/‎MM‎/‎YYYY");
    CHROME_FORMATS_.put("hr", "‎dd‎.‎MM‎.‎YYYY‎.");
    CHROME_FORMATS_.put("in", "‎MM‎/‎dd‎/‎YYYY");
    CHROME_FORMATS_.put("in-ID", "‎dd‎/‎MM‎/‎YYYY");
    CHROME_FORMATS_.put("in", "‎MM‎/‎dd‎/‎YYYY");
    CHROME_FORMATS_.put("is", "‎YYYY‎-‎MM‎-‎dd");
    CHROME_FORMATS_.put("iw", "‎MM‎/‎dd‎/‎YYYY");
    CHROME_FORMATS_.put("iw-IL", "‎dd‎.‎MM‎.‎YYYY");
    CHROME_FORMATS_.put("ja-JP-u-ca-japanese", "平成yy/MM/dd");
    CHROME_FORMATS_.put("sk", "‎dd‎. ‎MM‎. ‎YYYY");
    CHROME_FORMATS_.put("sq", "‎YYYY‎-‎MM‎-‎dd");
    CHROME_FORMATS_.put("sr", "‎dd‎.‎MM‎.‎YYYY‎.");
    CHROME_FORMATS_.put("mk", "‎YYYY‎-‎MM‎-‎dd");
    
    IE_FORMATS_.put("ar", "‏dd‏/‏MM‏/‏YYYY");
    IE_FORMATS_.put("ar-AE", "‏dd‏/‏MM‏/‏YYYY");
    IE_FORMATS_.put("ar-BH", "‏dd‏/‏MM‏/‏YYYY");
    IE_FORMATS_.put("ar-DZ", "‏dd‏-‏MM‏-‏YYYY");
    IE_FORMATS_.put("ar-LY", "‏dd‏/‏MM‏/‏YYYY");
    IE_FORMATS_.put("ar-MA", "‏dd‏-‏MM‏-‏YYYY");
    IE_FORMATS_.put("ar-TN", "‏dd‏-‏MM‏-‏YYYY");
    IE_FORMATS_.put("ar-EG", "‏dd‏/‏MM‏/‏YYYY");
    IE_FORMATS_.put("ar-IQ", "‏dd‏/‏MM‏/‏YYYY");
    IE_FORMATS_.put("ar-JO", "‏dd‏/‏MM‏/‏YYYY");
    IE_FORMATS_.put("ar-KW", "‏dd‏/‏MM‏/‏YYYY");
    IE_FORMATS_.put("ar-LB", "‏dd‏/‏MM‏/‏YYYY");
    IE_FORMATS_.put("ar-OM", "‏dd‏/‏MM‏/‏YYYY");
    IE_FORMATS_.put("ar-QA", "‏dd‏/‏MM‏/‏YYYY");
    IE_FORMATS_.put("ar-SA", "‏dd‏/‏MM‏/‏YYYY");
    IE_FORMATS_.put("ar-SD", "‏dd‏/‏MM‏/‏YYYY");
    IE_FORMATS_.put("ar-SY", "‏dd‏/‏MM‏/‏YYYY");
    IE_FORMATS_.put("ar-YE", "‏dd‏/‏MM‏/‏YYYY");
    IE_FORMATS_.put("cs", "‎dd‎.‎MM‎.‎YYYY");
    IE_FORMATS_.put("da", "‎dd‎-‎MM‎-‎YYYY");
    IE_FORMATS_.put("en-IN", "‎dd‎-‎MM‎-‎YYYY");
    IE_FORMATS_.put("en-MT", "‎MM‎/‎dd‎/‎YYYY");
    IE_FORMATS_.put("en-CA", "‎dd‎/‎MM‎/‎YYYY");
    IE_FORMATS_.put("es-PR", "‎dd‎/‎MM‎/‎YYYY");
    IE_FORMATS_.put("es-US", "‎MM‎/‎dd‎/‎YYYY");
    IE_FORMATS_.put("fr-CH", "‎dd‎.‎MM‎.‎YYYY");
    IE_FORMATS_.put("ga", "‎dd‎/‎MM‎/‎YYYY");
    IE_FORMATS_.put("hi", "‎dd‎-‎MM‎-‎YYYY");
    IE_FORMATS_.put("hr", "‎dd‎.‎MM‎.‎YYYY‎.");
    IE_FORMATS_.put("hu", "‎YYYY‎.‎MM‎.‎dd‎.");
    IE_FORMATS_.put("iw", "‎dd‎/‎MM‎/‎YYYY");
    IE_FORMATS_.put("it-CH", "‎dd‎.‎MM‎.‎YYYY");
    IE_FORMATS_.put("ja", "‎YYYY‎年‎MM‎月‎dd‎日");
    IE_FORMATS_.put("ja-JP-u-ca-japanese", "‎平成‎ ‎yy‎年‎MM‎月‎dd‎日");
    IE_FORMATS_.put("ko", "‎YYYY‎년 ‎MM‎월 ‎dd‎일");
    IE_FORMATS_.put("lt", "‎YYYY‎.‎MM‎.‎dd");
    IE_FORMATS_.put("lv", "‎YYYY‎.‎MM‎.‎dd‎.");
    IE_FORMATS_.put("mt", "‎dd‎/‎MM‎/‎YYYY");
    IE_FORMATS_.put("nl-BE", "‎dd‎/‎MM‎/‎YYYY");
    IE_FORMATS_.put("no", "‎dd‎.‎MM‎.‎YYYY");
    IE_FORMATS_.put("pl", "‎YYYY‎-‎MM‎-‎dd");
    IE_FORMATS_.put("pt-PT", "‎dd‎-‎MM‎-‎YYYY");
    IE_FORMATS_.put("sk", "‎dd‎. ‎MM‎. ‎YYYY");
    IE_FORMATS_.put("sl", "‎dd‎.‎MM‎.‎YYYY");
    IE_FORMATS_.put("sq", "‎YYYY‎-‎MM‎-‎dd");
    IE_FORMATS_.put("sr", "‎dd‎.‎MM‎.‎YYYY");
    IE_FORMATS_.put("sr-BA", "‎MM‎/‎dd‎/‎YYYY");
    IE_FORMATS_.put("sr-CS", "‎MM‎/‎dd‎/‎YYYY");
    IE_FORMATS_.put("sr-ME", "‎MM‎/‎dd‎/‎YYYY");
    IE_FORMATS_.put("sr-RS", "‎MM‎/‎dd‎/‎YYYY");
    IE_FORMATS_.put("zh", "‎YYYY‎年‎MM‎月‎dd‎日");
    IE_FORMATS_.put("zh-HK", "‎YYYY‎年‎MM‎月‎dd‎日");
  }
  

  public DateTimeFormat() {}
  

  private DateTimeFormat(String[] locales, BrowserVersion browserVersion)
  {
    Map<String, String> formats;
    Map<String, String> formats;
    if (browserVersion.isChrome()) {
      formats = CHROME_FORMATS_;
    } else { Map<String, String> formats;
      if (browserVersion.isIE()) {
        formats = IE_FORMATS_;
      }
      else {
        formats = FF_45_FORMATS_;
      }
    }
    String locale = "";
    String pattern = null;
    
    for (String l : locales) {
      pattern = getPattern(formats, l);
      if (pattern != null) {
        locale = l;
      }
    }
    
    if (pattern == null) {
      pattern = (String)formats.get("");
    }
    if ((!browserVersion.hasFeature(BrowserVersionFeatures.JS_DATE_WITH_LEFT_TO_RIGHT_MARK)) && (!locale.startsWith("ar"))) {
      pattern = pattern.replace("‎", "");
    }
    
    if (GAEUtils.isGaeMode()) {
      formatter_ = new GAEDateTimeFormatter(locale, browserVersion, pattern);
    }
    else {
      formatter_ = new DefaultDateTimeFormatter(locale, browserVersion, pattern);
    }
  }
  
  private static String getPattern(Map<String, String> formats, String locale) {
    if ("no-NO-NY".equals(locale)) {
      throw ScriptRuntime.rangeError("Invalid language tag: " + locale);
    }
    String pattern = (String)formats.get(locale);
    if ((pattern == null) && (locale.indexOf('-') != -1)) {
      pattern = (String)formats.get(locale.substring(0, locale.indexOf('-')));
    }
    return pattern;
  }
  



  @JsxConstructor
  public static Scriptable jsConstructor(Context cx, Object[] args, Function ctorObj, boolean inNewExpr)
  {
    String[] locales;
    

    String[] locales;
    

    if (args.length != 0) {
      if ((args[0] instanceof NativeArray)) {
        NativeArray array = (NativeArray)args[0];
        String[] locales = new String[(int)array.getLength()];
        for (int i = 0; i < locales.length; i++) {
          locales[i] = Context.toString(array.get(i));
        }
      }
      else {
        locales = new String[] { Context.toString(args[0]) };
      }
    }
    else {
      locales = new String[] { "" };
    }
    Window window = getWindow(ctorObj);
    DateTimeFormat format = new DateTimeFormat(locales, window.getBrowserVersion());
    format.setParentScope(window);
    format.setPrototype(window.getPrototype(format.getClass()));
    return format;
  }
  




  @JsxFunction
  public String format(Object object)
  {
    Date date = (Date)Context.jsToJava(object, Date.class);
    return formatter_.format(date);
  }
}
