package com.gargoylesoftware.htmlunit.javascript.regexp;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.RegExpProxy;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;
import net.sourceforge.htmlunit.corejs.javascript.regexp.NativeRegExp;
import net.sourceforge.htmlunit.corejs.javascript.regexp.RegExpImpl;
import net.sourceforge.htmlunit.corejs.javascript.regexp.SubString;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



























public class HtmlUnitRegExpProxy
  extends RegExpImpl
{
  private static final Log LOG = LogFactory.getLog(HtmlUnitRegExpProxy.class);
  

  private final RegExpProxy wrapped_;
  

  private final BrowserVersion browserVersion_;
  

  public HtmlUnitRegExpProxy(RegExpProxy wrapped, BrowserVersion browserVersion)
  {
    wrapped_ = wrapped;
    browserVersion_ = browserVersion;
  }
  




  public Object action(Context cx, Scriptable scope, Scriptable thisObj, Object[] args, int actionType)
  {
    try
    {
      return doAction(cx, scope, thisObj, args, actionType);

    }
    catch (StackOverflowError e)
    {
      LOG.warn(e.getMessage(), e); }
    return wrapped_.action(cx, scope, thisObj, args, actionType);
  }
  


  private Object doAction(Context cx, Scriptable scope, Scriptable thisObj, Object[] args, int actionType)
  {
    if ((2 == actionType) && (args.length == 2) && ((args[1] instanceof String))) {
      String thisString = Context.toString(thisObj);
      String replacement = (String)args[1];
      Object arg0 = args[0];
      if ((arg0 instanceof String))
      {
        return doStringReplacement(thisString, (String)arg0, replacement);
      }
      
      if ((arg0 instanceof NativeRegExp)) {
        try {
          NativeRegExp regexp = (NativeRegExp)arg0;
          RegExpData reData = new RegExpData(regexp);
          String regex = reData.getJavaPattern();
          int flags = reData.getJavaFlags();
          Pattern pattern = Pattern.compile(regex, flags);
          Matcher matcher = pattern.matcher(thisString);
          return doReplacement(thisString, replacement, matcher, reData.hasFlag('g'));
        }
        catch (PatternSyntaxException e) {
          LOG.warn(e.getMessage(), e);
        }
      }
    }
    else if ((1 == actionType) || (3 == actionType)) {
      if (args.length == 0) {
        return null;
      }
      Object arg0 = args[0];
      String thisString = Context.toString(thisObj);
      RegExpData reData;
      RegExpData reData; if ((arg0 instanceof NativeRegExp)) {
        reData = new RegExpData((NativeRegExp)arg0);
      }
      else {
        reData = new RegExpData(Context.toString(arg0));
      }
      
      Pattern pattern = Pattern.compile(reData.getJavaPattern(), reData.getJavaFlags());
      Matcher matcher = pattern.matcher(thisString);
      
      boolean found = matcher.find();
      if (3 == actionType) {
        if (found) {
          setProperties(matcher, thisString, matcher.start(), matcher.end());
          return Integer.valueOf(matcher.start());
        }
        return Integer.valueOf(-1);
      }
      
      if (!found) {
        return null;
      }
      int index = matcher.start(0);
      List<Object> groups = new ArrayList();
      if (reData.hasFlag('g')) {
        groups.add(matcher.group(0));
        setProperties(matcher, thisString, matcher.start(0), matcher.end(0));
        
        while (matcher.find()) {
          groups.add(matcher.group(0));
          setProperties(matcher, thisString, matcher.start(0), matcher.end(0));
        }
      }
      else {
        for (int i = 0; i <= matcher.groupCount(); i++) {
          Object group = matcher.group(i);
          if (group == null) {
            group = Undefined.instance;
          }
          groups.add(group);
        }
        
        setProperties(matcher, thisString, matcher.start(), matcher.end());
      }
      Scriptable response = cx.newArray(scope, groups.toArray());
      
      response.put("index", response, Integer.valueOf(index));
      response.put("input", response, thisString);
      return response;
    }
    
    return wrappedAction(cx, scope, thisObj, args, actionType);
  }
  
  private String doStringReplacement(String originalString, String searchString, String replacement)
  {
    if (originalString == null) {
      return "";
    }
    
    StaticStringMatcher matcher = new StaticStringMatcher(originalString, searchString, null);
    if (matcher.start() > -1) {
      StringBuilder sb = new StringBuilder();
      sb.append(originalString.substring(0, start_));
      
      String localReplacement = replacement;
      if (replacement.contains("$")) {
        localReplacement = computeReplacementValue(localReplacement, originalString, matcher, false);
      }
      
      sb.append(localReplacement);
      sb.append(originalString.substring(end_));
      return sb.toString();
    }
    return originalString;
  }
  

  private String doReplacement(String originalString, String replacement, Matcher matcher, boolean replaceAll)
  {
    StringBuilder sb = new StringBuilder();
    int previousIndex = 0;
    while (matcher.find()) {
      sb.append(originalString, previousIndex, matcher.start());
      String localReplacement = replacement;
      if (replacement.contains("$")) {
        localReplacement = computeReplacementValue(replacement, originalString, matcher, 
          browserVersion_.hasFeature(BrowserVersionFeatures.JS_REGEXP_GROUP0_RETURNS_WHOLE_MATCH));
      }
      sb.append(localReplacement);
      previousIndex = matcher.end();
      
      setProperties(matcher, originalString, matcher.start(), previousIndex);
      if (!replaceAll) {
        break;
      }
    }
    sb.append(originalString, previousIndex, originalString.length());
    return sb.toString();
  }
  

  String computeReplacementValue(String replacement, String originalString, MatchResult matcher, boolean group0ReturnsWholeMatch)
  {
    int lastIndex = 0;
    StringBuilder result = new StringBuilder();
    int i;
    while ((i = replacement.indexOf('$', lastIndex)) > -1) { int i;
      if (i > 0) {
        result.append(replacement, lastIndex, i);
      }
      String ss = null;
      if ((i < replacement.length() - 1) && ((i == lastIndex) || (replacement.charAt(i - 1) != '$'))) {
        char next = replacement.charAt(i + 1);
        
        if ((next >= '1') && (next <= '9')) {
          int num1digit = next - '0';
          char next2 = i + 2 < replacement.length() ? replacement.charAt(i + 2) : 'x';
          
          int num2digits;
          int num2digits;
          if ((next2 >= '1') && (next2 <= '9')) {
            num2digits = num1digit * 10 + (next2 - '0');
          }
          else {
            num2digits = Integer.MAX_VALUE;
          }
          if (num2digits <= matcher.groupCount()) {
            ss = matcher.group(num2digits);
            i++;
          }
          else if (num1digit <= matcher.groupCount()) {
            ss = StringUtils.defaultString(matcher.group(num1digit));
          }
        }
        else {
          switch (next) {
          case '&': 
            ss = matcher.group();
            break;
          case '0': 
            if (group0ReturnsWholeMatch) {
              ss = matcher.group();
            }
            break;
          case '`': 
            ss = originalString.substring(0, matcher.start());
            break;
          case '\'': 
            ss = originalString.substring(matcher.end());
            break;
          case '$': 
            ss = "$";
          }
          
        }
      }
      
      if (ss == null) {
        result.append('$');
        lastIndex = i + 1;
      }
      else {
        result.append(ss);
        lastIndex = i + 2;
      }
    }
    
    result.append(replacement, lastIndex, replacement.length());
    
    return result.toString();
  }
  





  private Object wrappedAction(Context cx, Scriptable scope, Scriptable thisObj, Object[] args, int actionType)
  {
    try
    {
      ScriptRuntime.setRegExpProxy(cx, wrapped_);
      return wrapped_.action(cx, scope, thisObj, args, actionType);
    }
    finally {
      ScriptRuntime.setRegExpProxy(cx, this);
    }
  }
  
  private void setProperties(Matcher matcher, String thisString, int startPos, int endPos)
  {
    String match = matcher.group();
    if (match == null) {
      lastMatch = new SubString();
    }
    else {
      lastMatch = new SubString(match, 0, match.length());
    }
    

    int groupCount = matcher.groupCount();
    if (groupCount == 0) {
      parens = null;
    }
    else {
      int count = Math.min(9, groupCount);
      parens = new SubString[count];
      for (int i = 0; i < count; i++) {
        String group = matcher.group(i + 1);
        if (group == null) {
          parens[i] = new SubString();
        }
        else {
          parens[i] = new SubString(group, 0, group.length());
        }
      }
    }
    

    if (groupCount > 0) {
      if ((groupCount > 9) && (browserVersion_.hasFeature(BrowserVersionFeatures.JS_REGEXP_EMPTY_LASTPAREN_IF_TOO_MANY_GROUPS))) {
        lastParen = new SubString();
      }
      else {
        String last = matcher.group(groupCount);
        if (last == null) {
          lastParen = new SubString();
        }
        else {
          lastParen = new SubString(last, 0, last.length());
        }
      }
    }
    

    if (startPos > 0) {
      leftContext = new SubString(thisString, 0, startPos);
    }
    else {
      leftContext = new SubString();
    }
    

    int length = thisString.length();
    if (endPos < length) {
      rightContext = new SubString(thisString, endPos, length - endPos);
    }
    else {
      rightContext = new SubString();
    }
  }
  


  public Object compileRegExp(Context cx, String source, String flags)
  {
    try
    {
      return wrapped_.compileRegExp(cx, source, flags);
    }
    catch (Exception e) {
      LOG.warn("compileRegExp() threw for >" + source + "<, flags: >" + flags + "<. " + 
        "Replacing with a '####shouldNotFindAnything###'"); }
    return wrapped_.compileRegExp(cx, "####shouldNotFindAnything###", "");
  }
  






  public int find_split(Context cx, Scriptable scope, String target, String separator, Scriptable re, int[] ip, int[] matchlen, boolean[] matched, String[][] parensp)
  {
    return wrapped_.find_split(cx, scope, target, separator, re, ip, matchlen, matched, parensp);
  }
  



  public boolean isRegExp(Scriptable obj)
  {
    return wrapped_.isRegExp(obj);
  }
  



  public Scriptable wrapRegExp(Context cx, Scriptable scope, Object compiled)
  {
    return wrapped_.wrapRegExp(cx, scope, compiled);
  }
  
  private static class RegExpData {
    private final String jsSource_;
    private final String jsFlags_;
    
    RegExpData(NativeRegExp re) {
      String str = re.toString();
      jsSource_ = StringUtils.substringBeforeLast(str.substring(1), "/");
      jsFlags_ = StringUtils.substringAfterLast(str, "/");
    }
    
    RegExpData(String string) { jsSource_ = string;
      jsFlags_ = "";
    }
    



    public int getJavaFlags()
    {
      int flags = 0;
      if (jsFlags_.contains("i")) {
        flags |= 0x2;
      }
      if (jsFlags_.contains("m")) {
        flags |= 0x8;
      }
      return flags;
    }
    
    public String getJavaPattern() { return HtmlUnitRegExpProxy.jsRegExpToJavaRegExp(jsSource_); }
    
    boolean hasFlag(char c)
    {
      return jsFlags_.indexOf(c) != -1;
    }
  }
  




  static String jsRegExpToJavaRegExp(String re)
  {
    RegExpJsToJavaConverter regExpJsToJavaFSM = new RegExpJsToJavaConverter();
    return regExpJsToJavaFSM.convert(re);
  }
  
  private static final class StaticStringMatcher
    implements MatchResult
  {
    private final String group_;
    private final int start_;
    private final int end_;
    
    private StaticStringMatcher(String originalString, String searchString)
    {
      int pos = originalString.indexOf(searchString);
      group_ = searchString;
      start_ = pos;
      end_ = (pos + searchString.length());
    }
    
    public String group()
    {
      return group_;
    }
    
    public int start()
    {
      return start_;
    }
    
    public int end()
    {
      return end_;
    }
    

    public int start(int group)
    {
      return 0;
    }
    

    public int end(int group)
    {
      return 0;
    }
    

    public String group(int group)
    {
      return null;
    }
    

    public int groupCount()
    {
      return 0;
    }
  }
}
