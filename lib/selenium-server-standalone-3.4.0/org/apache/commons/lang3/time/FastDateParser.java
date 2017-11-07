package org.apache.commons.lang3.time;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



















































public class FastDateParser
  implements DateParser, Serializable
{
  private static final long serialVersionUID = 3L;
  static final Locale JAPANESE_IMPERIAL = new Locale("ja", "JP", "JP");
  

  private final String pattern;
  
  private final TimeZone timeZone;
  
  private final Locale locale;
  
  private final int century;
  
  private final int startYear;
  
  private transient List<StrategyAndWidth> patterns;
  
  private static final Comparator<String> LONGER_FIRST_LOWERCASE = new Comparator()
  {
    public int compare(String left, String right) {
      return right.compareTo(left);
    }
  };
  










  protected FastDateParser(String pattern, TimeZone timeZone, Locale locale)
  {
    this(pattern, timeZone, locale, null);
  }
  










  protected FastDateParser(String pattern, TimeZone timeZone, Locale locale, Date centuryStart)
  {
    this.pattern = pattern;
    this.timeZone = timeZone;
    this.locale = locale;
    
    Calendar definingCalendar = Calendar.getInstance(timeZone, locale);
    int centuryStartYear;
    int centuryStartYear;
    if (centuryStart != null) {
      definingCalendar.setTime(centuryStart);
      centuryStartYear = definingCalendar.get(1);
    } else { int centuryStartYear;
      if (locale.equals(JAPANESE_IMPERIAL)) {
        centuryStartYear = 0;
      }
      else
      {
        definingCalendar.setTime(new Date());
        centuryStartYear = definingCalendar.get(1) - 80;
      } }
    century = (centuryStartYear / 100 * 100);
    startYear = (centuryStartYear - century);
    
    init(definingCalendar);
  }
  





  private void init(Calendar definingCalendar)
  {
    patterns = new ArrayList();
    
    StrategyParser fm = new StrategyParser(pattern, definingCalendar);
    for (;;) {
      StrategyAndWidth field = fm.getNextStrategy();
      if (field == null) {
        break;
      }
      patterns.add(field);
    }
  }
  


  private static class StrategyAndWidth
  {
    final FastDateParser.Strategy strategy;
    
    final int width;
    

    StrategyAndWidth(FastDateParser.Strategy strategy, int width)
    {
      this.strategy = strategy;
      this.width = width;
    }
    
    int getMaxWidth(ListIterator<StrategyAndWidth> lt) {
      if ((!strategy.isNumber()) || (!lt.hasNext())) {
        return 0;
      }
      FastDateParser.Strategy nextStrategy = nextstrategy;
      lt.previous();
      return nextStrategy.isNumber() ? width : 0;
    }
  }
  

  private class StrategyParser
  {
    private final String pattern;
    private final Calendar definingCalendar;
    private int currentIdx;
    
    StrategyParser(String pattern, Calendar definingCalendar)
    {
      this.pattern = pattern;
      this.definingCalendar = definingCalendar;
    }
    
    FastDateParser.StrategyAndWidth getNextStrategy() {
      if (currentIdx >= pattern.length()) {
        return null;
      }
      
      char c = pattern.charAt(currentIdx);
      if (FastDateParser.isFormatLetter(c)) {
        return letterPattern(c);
      }
      return literal();
    }
    
    private FastDateParser.StrategyAndWidth letterPattern(char c) {
      int begin = currentIdx;
      while (++currentIdx < pattern.length()) {
        if (pattern.charAt(currentIdx) != c) {
          break;
        }
      }
      
      int width = currentIdx - begin;
      return new FastDateParser.StrategyAndWidth(FastDateParser.this.getStrategy(c, width, definingCalendar), width);
    }
    
    private FastDateParser.StrategyAndWidth literal() {
      boolean activeQuote = false;
      
      StringBuilder sb = new StringBuilder();
      while (currentIdx < pattern.length()) {
        char c = pattern.charAt(currentIdx);
        if ((!activeQuote) && (FastDateParser.isFormatLetter(c)))
          break;
        if ((c == '\'') && ((++currentIdx == pattern.length()) || (pattern.charAt(currentIdx) != '\''))) {
          activeQuote = !activeQuote;
        }
        else {
          currentIdx += 1;
          sb.append(c);
        }
      }
      if (activeQuote) {
        throw new IllegalArgumentException("Unterminated quote");
      }
      
      String formatField = sb.toString();
      return new FastDateParser.StrategyAndWidth(new FastDateParser.CopyQuotedStrategy(formatField), formatField.length());
    }
  }
  
  private static boolean isFormatLetter(char c) {
    return ((c >= 'A') && (c <= 'Z')) || ((c >= 'a') && (c <= 'z'));
  }
  





  public String getPattern()
  {
    return pattern;
  }
  



  public TimeZone getTimeZone()
  {
    return timeZone;
  }
  



  public Locale getLocale()
  {
    return locale;
  }
  









  public boolean equals(Object obj)
  {
    if (!(obj instanceof FastDateParser)) {
      return false;
    }
    FastDateParser other = (FastDateParser)obj;
    return (pattern.equals(pattern)) && 
      (timeZone.equals(timeZone)) && 
      (locale.equals(locale));
  }
  





  public int hashCode()
  {
    return pattern.hashCode() + 13 * (timeZone.hashCode() + 13 * locale.hashCode());
  }
  





  public String toString()
  {
    return "FastDateParser[" + pattern + "," + locale + "," + timeZone.getID() + "]";
  }
  








  private void readObject(ObjectInputStream in)
    throws IOException, ClassNotFoundException
  {
    in.defaultReadObject();
    
    Calendar definingCalendar = Calendar.getInstance(timeZone, locale);
    init(definingCalendar);
  }
  


  public Object parseObject(String source)
    throws ParseException
  {
    return parse(source);
  }
  


  public Date parse(String source)
    throws ParseException
  {
    ParsePosition pp = new ParsePosition(0);
    Date date = parse(source, pp);
    if (date == null)
    {
      if (locale.equals(JAPANESE_IMPERIAL))
      {

        throw new ParseException("(The " + locale + " locale does not support dates before 1868 AD)\nUnparseable date: \"" + source, pp.getErrorIndex());
      }
      throw new ParseException("Unparseable date: " + source, pp.getErrorIndex());
    }
    return date;
  }
  



  public Object parseObject(String source, ParsePosition pos)
  {
    return parse(source, pos);
  }
  













  public Date parse(String source, ParsePosition pos)
  {
    Calendar cal = Calendar.getInstance(timeZone, locale);
    cal.clear();
    
    return parse(source, pos, cal) ? cal.getTime() : null;
  }
  













  public boolean parse(String source, ParsePosition pos, Calendar calendar)
  {
    ListIterator<StrategyAndWidth> lt = patterns.listIterator();
    while (lt.hasNext()) {
      StrategyAndWidth pattern = (StrategyAndWidth)lt.next();
      int maxWidth = pattern.getMaxWidth(lt);
      if (!strategy.parse(this, calendar, source, pos, maxWidth)) {
        return false;
      }
    }
    return true;
  }
  


  private static StringBuilder simpleQuote(StringBuilder sb, String value)
  {
    for (int i = 0; i < value.length(); i++) {
      char c = value.charAt(i);
      switch (c) {
      case '$': 
      case '(': 
      case ')': 
      case '*': 
      case '+': 
      case '.': 
      case '?': 
      case '[': 
      case '\\': 
      case '^': 
      case '{': 
      case '|': 
        sb.append('\\');
      }
      sb.append(c);
    }
    
    return sb;
  }
  







  private static Map<String, Integer> appendDisplayNames(Calendar cal, Locale locale, int field, StringBuilder regex)
  {
    Map<String, Integer> values = new HashMap();
    
    Map<String, Integer> displayNames = cal.getDisplayNames(field, 0, locale);
    TreeSet<String> sorted = new TreeSet(LONGER_FIRST_LOWERCASE);
    for (Map.Entry<String, Integer> displayName : displayNames.entrySet()) {
      String key = ((String)displayName.getKey()).toLowerCase(locale);
      if (sorted.add(key)) {
        values.put(key, displayName.getValue());
      }
    }
    for (String symbol : sorted) {
      simpleQuote(regex, symbol).append('|');
    }
    return values;
  }
  




  private int adjustYear(int twoDigitYear)
  {
    int trial = century + twoDigitYear;
    return twoDigitYear >= startYear ? trial : trial + 100;
  }
  



  private static abstract class Strategy
  {
    private Strategy() {}
    


    boolean isNumber()
    {
      return false;
    }
    
    abstract boolean parse(FastDateParser paramFastDateParser, Calendar paramCalendar, String paramString, ParsePosition paramParsePosition, int paramInt);
  }
  
  private static abstract class PatternStrategy extends FastDateParser.Strategy {
    private Pattern pattern;
    
    private PatternStrategy() { super(); }
    

    void createPattern(StringBuilder regex)
    {
      createPattern(regex.toString());
    }
    
    void createPattern(String regex) {
      pattern = Pattern.compile(regex);
    }
    






    boolean isNumber()
    {
      return false;
    }
    
    boolean parse(FastDateParser parser, Calendar calendar, String source, ParsePosition pos, int maxWidth)
    {
      Matcher matcher = pattern.matcher(source.substring(pos.getIndex()));
      if (!matcher.lookingAt()) {
        pos.setErrorIndex(pos.getIndex());
        return false;
      }
      pos.setIndex(pos.getIndex() + matcher.end(1));
      setCalendar(parser, calendar, matcher.group(1));
      return true;
    }
    



    abstract void setCalendar(FastDateParser paramFastDateParser, Calendar paramCalendar, String paramString);
  }
  


  private Strategy getStrategy(char f, int width, Calendar definingCalendar)
  {
    switch (f) {
    case 'I': case 'J': case 'L': case 'N': case 'O': case 'P': case 'Q': case 'R': case 'T': case 'U': case 'V': case '[': case '\\': case ']': case '^': case '_': case '`': case 'b': case 'c': case 'e': case 'f': case 'g': case 'i': case 'j': case 'l': case 'n': case 'o': case 'p': case 'q': case 'r': case 't': case 'v': case 'x': default: 
      throw new IllegalArgumentException("Format '" + f + "' not supported");
    case 'D': 
      return DAY_OF_YEAR_STRATEGY;
    case 'E': 
      return getLocaleSpecificStrategy(7, definingCalendar);
    case 'F': 
      return DAY_OF_WEEK_IN_MONTH_STRATEGY;
    case 'G': 
      return getLocaleSpecificStrategy(0, definingCalendar);
    case 'H': 
      return HOUR_OF_DAY_STRATEGY;
    case 'K': 
      return HOUR_STRATEGY;
    case 'M': 
      return width >= 3 ? getLocaleSpecificStrategy(2, definingCalendar) : NUMBER_MONTH_STRATEGY;
    case 'S': 
      return MILLISECOND_STRATEGY;
    case 'W': 
      return WEEK_OF_MONTH_STRATEGY;
    case 'a': 
      return getLocaleSpecificStrategy(9, definingCalendar);
    case 'd': 
      return DAY_OF_MONTH_STRATEGY;
    case 'h': 
      return HOUR12_STRATEGY;
    case 'k': 
      return HOUR24_OF_DAY_STRATEGY;
    case 'm': 
      return MINUTE_STRATEGY;
    case 's': 
      return SECOND_STRATEGY;
    case 'u': 
      return DAY_OF_WEEK_STRATEGY;
    case 'w': 
      return WEEK_OF_YEAR_STRATEGY;
    case 'Y': 
    case 'y': 
      return width > 2 ? LITERAL_YEAR_STRATEGY : ABBREVIATED_YEAR_STRATEGY;
    case 'X': 
      return ISO8601TimeZoneStrategy.getStrategy(width);
    case 'Z': 
      if (width == 2) {
        return ISO8601TimeZoneStrategy.ISO_8601_3_STRATEGY;
      }
      break;
    }
    return getLocaleSpecificStrategy(15, definingCalendar);
  }
  


  private static final ConcurrentMap<Locale, Strategy>[] caches = new ConcurrentMap[17];
  




  private static ConcurrentMap<Locale, Strategy> getCache(int field)
  {
    synchronized (caches) {
      if (caches[field] == null) {
        caches[field] = new ConcurrentHashMap(3);
      }
      return caches[field];
    }
  }
  





  private Strategy getLocaleSpecificStrategy(int field, Calendar definingCalendar)
  {
    ConcurrentMap<Locale, Strategy> cache = getCache(field);
    Strategy strategy = (Strategy)cache.get(locale);
    if (strategy == null) {
      strategy = field == 15 ? new TimeZoneStrategy(locale) : new CaseInsensitiveTextStrategy(field, definingCalendar, locale);
      

      Strategy inCache = (Strategy)cache.putIfAbsent(locale, strategy);
      if (inCache != null) {
        return inCache;
      }
    }
    return strategy;
  }
  


  private static class CopyQuotedStrategy
    extends FastDateParser.Strategy
  {
    private final String formatField;
    


    CopyQuotedStrategy(String formatField)
    {
      super();
      this.formatField = formatField;
    }
    



    boolean isNumber()
    {
      return false;
    }
    
    boolean parse(FastDateParser parser, Calendar calendar, String source, ParsePosition pos, int maxWidth)
    {
      for (int idx = 0; idx < formatField.length(); idx++) {
        int sIdx = idx + pos.getIndex();
        if (sIdx == source.length()) {
          pos.setErrorIndex(sIdx);
          return false;
        }
        if (formatField.charAt(idx) != source.charAt(sIdx)) {
          pos.setErrorIndex(sIdx);
          return false;
        }
      }
      pos.setIndex(formatField.length() + pos.getIndex());
      return true;
    }
  }
  


  private static class CaseInsensitiveTextStrategy
    extends FastDateParser.PatternStrategy
  {
    private final int field;
    
    final Locale locale;
    
    private final Map<String, Integer> lKeyValues;
    

    CaseInsensitiveTextStrategy(int field, Calendar definingCalendar, Locale locale)
    {
      super();
      this.field = field;
      this.locale = locale;
      
      StringBuilder regex = new StringBuilder();
      regex.append("((?iu)");
      lKeyValues = FastDateParser.appendDisplayNames(definingCalendar, locale, field, regex);
      regex.setLength(regex.length() - 1);
      regex.append(")");
      createPattern(regex);
    }
    



    void setCalendar(FastDateParser parser, Calendar cal, String value)
    {
      Integer iVal = (Integer)lKeyValues.get(value.toLowerCase(locale));
      cal.set(field, iVal.intValue());
    }
  }
  


  private static class NumberStrategy
    extends FastDateParser.Strategy
  {
    private final int field;
    


    NumberStrategy(int field)
    {
      super();
      this.field = field;
    }
    



    boolean isNumber()
    {
      return true;
    }
    
    boolean parse(FastDateParser parser, Calendar calendar, String source, ParsePosition pos, int maxWidth)
    {
      int idx = pos.getIndex();
      int last = source.length();
      
      if (maxWidth == 0)
      {
        for (; idx < last; idx++) {
          char c = source.charAt(idx);
          if (!Character.isWhitespace(c)) {
            break;
          }
        }
        pos.setIndex(idx);
      } else {
        int end = idx + maxWidth;
        if (last > end) {
          last = end;
        }
      }
      for (; 
          idx < last; idx++) {
        char c = source.charAt(idx);
        if (!Character.isDigit(c)) {
          break;
        }
      }
      
      if (pos.getIndex() == idx) {
        pos.setErrorIndex(idx);
        return false;
      }
      
      int value = Integer.parseInt(source.substring(pos.getIndex(), idx));
      pos.setIndex(idx);
      
      calendar.set(field, modify(parser, value));
      return true;
    }
    





    int modify(FastDateParser parser, int iValue)
    {
      return iValue;
    }
  }
  

  private static final Strategy ABBREVIATED_YEAR_STRATEGY = new NumberStrategy(1)
  {

    int modify(FastDateParser parser, int iValue)
    {

      return iValue < 100 ? parser.adjustYear(iValue) : iValue;
    }
  };
  

  static class TimeZoneStrategy
    extends FastDateParser.PatternStrategy
  {
    private static final String RFC_822_TIME_ZONE = "[+-]\\d{4}";
    
    private static final String GMT_OPTION = "GMT[+-]\\d{1,2}:\\d{2}";
    private final Locale locale;
    private final Map<String, TzInfo> tzNames = new HashMap();
    private static final int ID = 0;
    
    private static class TzInfo {
      TimeZone zone;
      int dstOffset;
      
      TzInfo(TimeZone tz, boolean useDst) { zone = tz;
        dstOffset = (useDst ? tz.getDSTSavings() : 0);
      }
    }
    







    TimeZoneStrategy(Locale locale)
    {
      super();
      this.locale = locale;
      
      StringBuilder sb = new StringBuilder();
      sb.append("((?iu)[+-]\\d{4}|GMT[+-]\\d{1,2}:\\d{2}");
      
      Set<String> sorted = new TreeSet(FastDateParser.LONGER_FIRST_LOWERCASE);
      
      String[][] zones = DateFormatSymbols.getInstance(locale).getZoneStrings();
      for (String[] zoneNames : zones)
      {
        String tzId = zoneNames[0];
        if (!tzId.equalsIgnoreCase("GMT"))
        {

          TimeZone tz = TimeZone.getTimeZone(tzId);
          

          TzInfo standard = new TzInfo(tz, false);
          TzInfo tzInfo = standard;
          for (int i = 1; i < zoneNames.length; i++) {
            switch (i)
            {
            case 3: 
              tzInfo = new TzInfo(tz, true);
              break;
            case 5: 
              tzInfo = standard;
            }
            
            String key = zoneNames[i].toLowerCase(locale);
            

            if (sorted.add(key)) {
              tzNames.put(key, tzInfo);
            }
          }
        }
      }
      
      for (??? = sorted.iterator(); ((Iterator)???).hasNext();) { String zoneName = (String)((Iterator)???).next();
        FastDateParser.simpleQuote(sb.append('|'), zoneName);
      }
      sb.append(")");
      createPattern(sb);
    }
    



    void setCalendar(FastDateParser parser, Calendar cal, String value)
    {
      if ((value.charAt(0) == '+') || (value.charAt(0) == '-')) {
        TimeZone tz = TimeZone.getTimeZone("GMT" + value);
        cal.setTimeZone(tz);
      } else if (value.regionMatches(true, 0, "GMT", 0, 3)) {
        TimeZone tz = TimeZone.getTimeZone(value.toUpperCase());
        cal.setTimeZone(tz);
      } else {
        TzInfo tzInfo = (TzInfo)tzNames.get(value.toLowerCase(locale));
        cal.set(16, dstOffset);
        cal.set(15, zone.getRawOffset());
      }
    }
  }
  


  private static class ISO8601TimeZoneStrategy
    extends FastDateParser.PatternStrategy
  {
    ISO8601TimeZoneStrategy(String pattern)
    {
      super();
      createPattern(pattern);
    }
    



    void setCalendar(FastDateParser parser, Calendar cal, String value)
    {
      if (value.equals("Z")) {
        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
      } else {
        cal.setTimeZone(TimeZone.getTimeZone("GMT" + value));
      }
    }
    
    private static final FastDateParser.Strategy ISO_8601_1_STRATEGY = new ISO8601TimeZoneStrategy("(Z|(?:[+-]\\d{2}))");
    private static final FastDateParser.Strategy ISO_8601_2_STRATEGY = new ISO8601TimeZoneStrategy("(Z|(?:[+-]\\d{2}\\d{2}))");
    private static final FastDateParser.Strategy ISO_8601_3_STRATEGY = new ISO8601TimeZoneStrategy("(Z|(?:[+-]\\d{2}(?::)\\d{2}))");
    






    static FastDateParser.Strategy getStrategy(int tokenLen)
    {
      switch (tokenLen) {
      case 1: 
        return ISO_8601_1_STRATEGY;
      case 2: 
        return ISO_8601_2_STRATEGY;
      case 3: 
        return ISO_8601_3_STRATEGY;
      }
      throw new IllegalArgumentException("invalid number of X");
    }
  }
  

  private static final Strategy NUMBER_MONTH_STRATEGY = new NumberStrategy(2)
  {
    int modify(FastDateParser parser, int iValue) {
      return iValue - 1;
    }
  };
  private static final Strategy LITERAL_YEAR_STRATEGY = new NumberStrategy(1);
  private static final Strategy WEEK_OF_YEAR_STRATEGY = new NumberStrategy(3);
  private static final Strategy WEEK_OF_MONTH_STRATEGY = new NumberStrategy(4);
  private static final Strategy DAY_OF_YEAR_STRATEGY = new NumberStrategy(6);
  private static final Strategy DAY_OF_MONTH_STRATEGY = new NumberStrategy(5);
  private static final Strategy DAY_OF_WEEK_STRATEGY = new NumberStrategy(7)
  {
    int modify(FastDateParser parser, int iValue) {
      return iValue != 7 ? iValue + 1 : 1;
    }
  };
  private static final Strategy DAY_OF_WEEK_IN_MONTH_STRATEGY = new NumberStrategy(8);
  private static final Strategy HOUR_OF_DAY_STRATEGY = new NumberStrategy(11);
  private static final Strategy HOUR24_OF_DAY_STRATEGY = new NumberStrategy(11)
  {
    int modify(FastDateParser parser, int iValue) {
      return iValue == 24 ? 0 : iValue;
    }
  };
  private static final Strategy HOUR12_STRATEGY = new NumberStrategy(10)
  {
    int modify(FastDateParser parser, int iValue) {
      return iValue == 12 ? 0 : iValue;
    }
  };
  private static final Strategy HOUR_STRATEGY = new NumberStrategy(10);
  private static final Strategy MINUTE_STRATEGY = new NumberStrategy(12);
  private static final Strategy SECOND_STRATEGY = new NumberStrategy(13);
  private static final Strategy MILLISECOND_STRATEGY = new NumberStrategy(14);
}
