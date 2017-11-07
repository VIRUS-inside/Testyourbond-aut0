package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import java.util.Arrays;
import java.util.BitSet;






























































































@GwtCompatible(emulated=true)
public abstract class CharMatcher
  implements Predicate<Character>
{
  public static CharMatcher any()
  {
    return Any.INSTANCE;
  }
  




  public static CharMatcher none()
  {
    return None.INSTANCE;
  }
  












  public static CharMatcher whitespace()
  {
    return Whitespace.INSTANCE;
  }
  






  public static CharMatcher breakingWhitespace()
  {
    return BreakingWhitespace.INSTANCE;
  }
  




  public static CharMatcher ascii()
  {
    return Ascii.INSTANCE;
  }
  






  public static CharMatcher digit()
  {
    return Digit.INSTANCE;
  }
  






  public static CharMatcher javaDigit()
  {
    return JavaDigit.INSTANCE;
  }
  






  public static CharMatcher javaLetter()
  {
    return JavaLetter.INSTANCE;
  }
  





  public static CharMatcher javaLetterOrDigit()
  {
    return JavaLetterOrDigit.INSTANCE;
  }
  





  public static CharMatcher javaUpperCase()
  {
    return JavaUpperCase.INSTANCE;
  }
  





  public static CharMatcher javaLowerCase()
  {
    return JavaLowerCase.INSTANCE;
  }
  





  public static CharMatcher javaIsoControl()
  {
    return JavaIsoControl.INSTANCE;
  }
  






  public static CharMatcher invisible()
  {
    return Invisible.INSTANCE;
  }
  









  public static CharMatcher singleWidth()
  {
    return SingleWidth.INSTANCE;
  }
  
















  @Deprecated
  public static final CharMatcher WHITESPACE = ;
  









  @Deprecated
  public static final CharMatcher BREAKING_WHITESPACE = breakingWhitespace();
  







  @Deprecated
  public static final CharMatcher ASCII = ascii();
  









  @Deprecated
  public static final CharMatcher DIGIT = digit();
  








  @Deprecated
  public static final CharMatcher JAVA_DIGIT = javaDigit();
  









  @Deprecated
  public static final CharMatcher JAVA_LETTER = javaLetter();
  







  @Deprecated
  public static final CharMatcher JAVA_LETTER_OR_DIGIT = javaLetterOrDigit();
  







  @Deprecated
  public static final CharMatcher JAVA_UPPER_CASE = javaUpperCase();
  







  @Deprecated
  public static final CharMatcher JAVA_LOWER_CASE = javaLowerCase();
  







  @Deprecated
  public static final CharMatcher JAVA_ISO_CONTROL = javaIsoControl();
  








  @Deprecated
  public static final CharMatcher INVISIBLE = invisible();
  











  @Deprecated
  public static final CharMatcher SINGLE_WIDTH = singleWidth();
  






  @Deprecated
  public static final CharMatcher ANY = any();
  






  @Deprecated
  public static final CharMatcher NONE = none();
  

  private static final int DISTINCT_CHARS = 65536;
  

  public static CharMatcher is(char match)
  {
    return new Is(match);
  }
  




  public static CharMatcher isNot(char match)
  {
    return new IsNot(match);
  }
  



  public static CharMatcher anyOf(CharSequence sequence)
  {
    switch (sequence.length()) {
    case 0: 
      return none();
    case 1: 
      return is(sequence.charAt(0));
    case 2: 
      return isEither(sequence.charAt(0), sequence.charAt(1));
    }
    
    
    return new AnyOf(sequence);
  }
  




  public static CharMatcher noneOf(CharSequence sequence)
  {
    return anyOf(sequence).negate();
  }
  






  public static CharMatcher inRange(char startInclusive, char endInclusive)
  {
    return new InRange(startInclusive, endInclusive);
  }
  



  public static CharMatcher forPredicate(Predicate<? super Character> predicate)
  {
    return (predicate instanceof CharMatcher) ? (CharMatcher)predicate : new ForPredicate(predicate);
  }
  





  protected CharMatcher() {}
  




  public abstract boolean matches(char paramChar);
  




  public CharMatcher negate()
  {
    return new Negated(this);
  }
  


  public CharMatcher and(CharMatcher other)
  {
    return new And(this, other);
  }
  


  public CharMatcher or(CharMatcher other)
  {
    return new Or(this, other);
  }
  








  public CharMatcher precomputed()
  {
    return Platform.precomputeCharMatcher(this);
  }
  











  @GwtIncompatible
  CharMatcher precomputedInternal()
  {
    BitSet table = new BitSet();
    setBits(table);
    int totalCharacters = table.cardinality();
    if (totalCharacters * 2 <= 65536) {
      return precomputedPositive(totalCharacters, table, toString());
    }
    
    table.flip(0, 65536);
    int negatedCharacters = 65536 - totalCharacters;
    String suffix = ".negate()";
    final String description = toString();
    

    String negatedDescription = description + suffix;
    
    new NegatedFastMatcher(
      precomputedPositive(negatedCharacters, table, negatedDescription))
      {
        public String toString()
        {
          return description;
        }
      };
    }
    




    @GwtIncompatible
    private static CharMatcher precomputedPositive(int totalCharacters, BitSet table, String description)
    {
      switch (totalCharacters) {
      case 0: 
        return none();
      case 1: 
        return is((char)table.nextSetBit(0));
      case 2: 
        char c1 = (char)table.nextSetBit(0);
        char c2 = (char)table.nextSetBit(c1 + '\001');
        return isEither(c1, c2);
      }
      return isSmall(totalCharacters, table.length()) ? 
        SmallCharMatcher.from(table, description) : new BitSetMatcher(table, description, null);
    }
    

    @GwtIncompatible
    private static boolean isSmall(int totalCharacters, int tableLength)
    {
      return (totalCharacters <= 1023) && (tableLength > totalCharacters * 4 * 16);
    }
    




    @GwtIncompatible
    void setBits(BitSet table)
    {
      for (int c = 65535; c >= 0; c--) {
        if (matches((char)c)) {
          table.set(c);
        }
      }
    }
    












    public boolean matchesAnyOf(CharSequence sequence)
    {
      return !matchesNoneOf(sequence);
    }
    









    public boolean matchesAllOf(CharSequence sequence)
    {
      for (int i = sequence.length() - 1; i >= 0; i--) {
        if (!matches(sequence.charAt(i))) {
          return false;
        }
      }
      return true;
    }
    










    public boolean matchesNoneOf(CharSequence sequence)
    {
      return indexIn(sequence) == -1;
    }
    









    public int indexIn(CharSequence sequence)
    {
      return indexIn(sequence, 0);
    }
    














    public int indexIn(CharSequence sequence, int start)
    {
      int length = sequence.length();
      Preconditions.checkPositionIndex(start, length);
      for (int i = start; i < length; i++) {
        if (matches(sequence.charAt(i))) {
          return i;
        }
      }
      return -1;
    }
    









    public int lastIndexIn(CharSequence sequence)
    {
      for (int i = sequence.length() - 1; i >= 0; i--) {
        if (matches(sequence.charAt(i))) {
          return i;
        }
      }
      return -1;
    }
    


    public int countIn(CharSequence sequence)
    {
      int count = 0;
      for (int i = 0; i < sequence.length(); i++) {
        if (matches(sequence.charAt(i))) {
          count++;
        }
      }
      return count;
    }
    







    public String removeFrom(CharSequence sequence)
    {
      String string = sequence.toString();
      int pos = indexIn(string);
      if (pos == -1) {
        return string;
      }
      
      char[] chars = string.toCharArray();
      int spread = 1;
      

      for (;;)
      {
        pos++;
        for (;;) {
          if (pos == chars.length) {
            break label79;
          }
          if (matches(chars[pos])) {
            break;
          }
          chars[(pos - spread)] = chars[pos];
          pos++;
        }
        spread++; }
      label79:
      return new String(chars, 0, pos - spread);
    }
    







    public String retainFrom(CharSequence sequence)
    {
      return negate().removeFrom(sequence);
    }
    
















    public String replaceFrom(CharSequence sequence, char replacement)
    {
      String string = sequence.toString();
      int pos = indexIn(string);
      if (pos == -1) {
        return string;
      }
      char[] chars = string.toCharArray();
      chars[pos] = replacement;
      for (int i = pos + 1; i < chars.length; i++) {
        if (matches(chars[i])) {
          chars[i] = replacement;
        }
      }
      return new String(chars);
    }
    















    public String replaceFrom(CharSequence sequence, CharSequence replacement)
    {
      int replacementLen = replacement.length();
      if (replacementLen == 0) {
        return removeFrom(sequence);
      }
      if (replacementLen == 1) {
        return replaceFrom(sequence, replacement.charAt(0));
      }
      
      String string = sequence.toString();
      int pos = indexIn(string);
      if (pos == -1) {
        return string;
      }
      
      int len = string.length();
      StringBuilder buf = new StringBuilder(len * 3 / 2 + 16);
      
      int oldpos = 0;
      do {
        buf.append(string, oldpos, pos);
        buf.append(replacement);
        oldpos = pos + 1;
        pos = indexIn(string, oldpos);
      } while (pos != -1);
      
      buf.append(string, oldpos, len);
      return buf.toString();
    }
    













    public String trimFrom(CharSequence sequence)
    {
      int len = sequence.length();
      


      for (int first = 0; first < len; first++) {
        if (!matches(sequence.charAt(first))) {
          break;
        }
      }
      for (int last = len - 1; last > first; last--) {
        if (!matches(sequence.charAt(last))) {
          break;
        }
      }
      
      return sequence.subSequence(first, last + 1).toString();
    }
    







    public String trimLeadingFrom(CharSequence sequence)
    {
      int len = sequence.length();
      for (int first = 0; first < len; first++) {
        if (!matches(sequence.charAt(first))) {
          return sequence.subSequence(first, len).toString();
        }
      }
      return "";
    }
    







    public String trimTrailingFrom(CharSequence sequence)
    {
      int len = sequence.length();
      for (int last = len - 1; last >= 0; last--) {
        if (!matches(sequence.charAt(last))) {
          return sequence.subSequence(0, last + 1).toString();
        }
      }
      return "";
    }
    


















    public String collapseFrom(CharSequence sequence, char replacement)
    {
      int len = sequence.length();
      for (int i = 0; i < len; i++) {
        char c = sequence.charAt(i);
        if (matches(c)) {
          if ((c == replacement) && ((i == len - 1) || (!matches(sequence.charAt(i + 1)))))
          {
            i++;
          } else {
            StringBuilder builder = new StringBuilder(len).append(sequence, 0, i).append(replacement);
            return finishCollapseFrom(sequence, i + 1, len, replacement, builder, true);
          }
        }
      }
      
      return sequence.toString();
    }
    





    public String trimAndCollapseFrom(CharSequence sequence, char replacement)
    {
      int len = sequence.length();
      int first = 0;
      int last = len - 1;
      
      while ((first < len) && (matches(sequence.charAt(first)))) {
        first++;
      }
      
      while ((last > first) && (matches(sequence.charAt(last)))) {
        last--;
      }
      
      return (first == 0) && (last == len - 1) ? 
        collapseFrom(sequence, replacement) : 
        finishCollapseFrom(sequence, first, last + 1, replacement, new StringBuilder(last + 1 - first), false);
    }
    






    private String finishCollapseFrom(CharSequence sequence, int start, int end, char replacement, StringBuilder builder, boolean inMatchingGroup)
    {
      for (int i = start; i < end; i++) {
        char c = sequence.charAt(i);
        if (matches(c)) {
          if (!inMatchingGroup) {
            builder.append(replacement);
            inMatchingGroup = true;
          }
        } else {
          builder.append(c);
          inMatchingGroup = false;
        }
      }
      return builder.toString();
    }
    




    @Deprecated
    public boolean apply(Character character)
    {
      return matches(character.charValue());
    }
    




    public String toString()
    {
      return super.toString();
    }
    



    private static String showCharacter(char c)
    {
      String hex = "0123456789ABCDEF";
      char[] tmp = { '\\', 'u', '\000', '\000', '\000', '\000' };
      for (int i = 0; i < 4; i++) {
        tmp[(5 - i)] = hex.charAt(c & 0xF);
        c = (char)(c >> '\004');
      }
      return String.copyValueOf(tmp);
    }
    
    static abstract class FastMatcher
      extends CharMatcher
    {
      FastMatcher() {}
      
      public final CharMatcher precomputed()
      {
        return this;
      }
      
      public CharMatcher negate()
      {
        return new CharMatcher.NegatedFastMatcher(this);
      }
    }
    
    static abstract class NamedFastMatcher extends CharMatcher.FastMatcher
    {
      private final String description;
      
      NamedFastMatcher(String description)
      {
        this.description = ((String)Preconditions.checkNotNull(description));
      }
      
      public final String toString()
      {
        return description;
      }
    }
    
    static class NegatedFastMatcher extends CharMatcher.Negated
    {
      NegatedFastMatcher(CharMatcher original)
      {
        super();
      }
      
      public final CharMatcher precomputed()
      {
        return this;
      }
    }
    
    @GwtIncompatible
    private static final class BitSetMatcher extends CharMatcher.NamedFastMatcher
    {
      private final BitSet table;
      
      private BitSetMatcher(BitSet table, String description)
      {
        super();
        if (table.length() + 64 < table.size()) {
          table = (BitSet)table.clone();
        }
        
        this.table = table;
      }
      
      public boolean matches(char c)
      {
        return table.get(c);
      }
      
      void setBits(BitSet bitSet)
      {
        bitSet.or(table);
      }
    }
    


    private static final class Any
      extends CharMatcher.NamedFastMatcher
    {
      static final Any INSTANCE = new Any();
      
      private Any() {
        super();
      }
      
      public boolean matches(char c)
      {
        return true;
      }
      
      public int indexIn(CharSequence sequence)
      {
        return sequence.length() == 0 ? -1 : 0;
      }
      
      public int indexIn(CharSequence sequence, int start)
      {
        int length = sequence.length();
        Preconditions.checkPositionIndex(start, length);
        return start == length ? -1 : start;
      }
      
      public int lastIndexIn(CharSequence sequence)
      {
        return sequence.length() - 1;
      }
      
      public boolean matchesAllOf(CharSequence sequence)
      {
        Preconditions.checkNotNull(sequence);
        return true;
      }
      
      public boolean matchesNoneOf(CharSequence sequence)
      {
        return sequence.length() == 0;
      }
      
      public String removeFrom(CharSequence sequence)
      {
        Preconditions.checkNotNull(sequence);
        return "";
      }
      
      public String replaceFrom(CharSequence sequence, char replacement)
      {
        char[] array = new char[sequence.length()];
        Arrays.fill(array, replacement);
        return new String(array);
      }
      
      public String replaceFrom(CharSequence sequence, CharSequence replacement)
      {
        StringBuilder result = new StringBuilder(sequence.length() * replacement.length());
        for (int i = 0; i < sequence.length(); i++) {
          result.append(replacement);
        }
        return result.toString();
      }
      
      public String collapseFrom(CharSequence sequence, char replacement)
      {
        return sequence.length() == 0 ? "" : String.valueOf(replacement);
      }
      
      public String trimFrom(CharSequence sequence)
      {
        Preconditions.checkNotNull(sequence);
        return "";
      }
      
      public int countIn(CharSequence sequence)
      {
        return sequence.length();
      }
      
      public CharMatcher and(CharMatcher other)
      {
        return (CharMatcher)Preconditions.checkNotNull(other);
      }
      
      public CharMatcher or(CharMatcher other)
      {
        Preconditions.checkNotNull(other);
        return this;
      }
      
      public CharMatcher negate()
      {
        return none();
      }
    }
    
    private static final class None
      extends CharMatcher.NamedFastMatcher
    {
      static final None INSTANCE = new None();
      
      private None() {
        super();
      }
      
      public boolean matches(char c)
      {
        return false;
      }
      
      public int indexIn(CharSequence sequence)
      {
        Preconditions.checkNotNull(sequence);
        return -1;
      }
      
      public int indexIn(CharSequence sequence, int start)
      {
        int length = sequence.length();
        Preconditions.checkPositionIndex(start, length);
        return -1;
      }
      
      public int lastIndexIn(CharSequence sequence)
      {
        Preconditions.checkNotNull(sequence);
        return -1;
      }
      
      public boolean matchesAllOf(CharSequence sequence)
      {
        return sequence.length() == 0;
      }
      
      public boolean matchesNoneOf(CharSequence sequence)
      {
        Preconditions.checkNotNull(sequence);
        return true;
      }
      
      public String removeFrom(CharSequence sequence)
      {
        return sequence.toString();
      }
      
      public String replaceFrom(CharSequence sequence, char replacement)
      {
        return sequence.toString();
      }
      
      public String replaceFrom(CharSequence sequence, CharSequence replacement)
      {
        Preconditions.checkNotNull(replacement);
        return sequence.toString();
      }
      
      public String collapseFrom(CharSequence sequence, char replacement)
      {
        return sequence.toString();
      }
      
      public String trimFrom(CharSequence sequence)
      {
        return sequence.toString();
      }
      
      public String trimLeadingFrom(CharSequence sequence)
      {
        return sequence.toString();
      }
      
      public String trimTrailingFrom(CharSequence sequence)
      {
        return sequence.toString();
      }
      
      public int countIn(CharSequence sequence)
      {
        Preconditions.checkNotNull(sequence);
        return 0;
      }
      
      public CharMatcher and(CharMatcher other)
      {
        Preconditions.checkNotNull(other);
        return this;
      }
      
      public CharMatcher or(CharMatcher other)
      {
        return (CharMatcher)Preconditions.checkNotNull(other);
      }
      
      public CharMatcher negate()
      {
        return any();
      }
    }
    


    @VisibleForTesting
    static final class Whitespace
      extends CharMatcher.NamedFastMatcher
    {
      static final String TABLE = " 　\r   　 \013　   　 \t     \f 　 　　 \n 　";
      
      static final int MULTIPLIER = 1682554634;
      
      static final int SHIFT = Integer.numberOfLeadingZeros(" 　\r   　 \013　   　 \t     \f 　 　　 \n 　".length() - 1);
      
      static final Whitespace INSTANCE = new Whitespace();
      
      Whitespace() {
        super();
      }
      
      public boolean matches(char c)
      {
        return " 　\r   　 \013　   　 \t     \f 　 　　 \n 　".charAt(1682554634 * c >>> SHIFT) == c;
      }
      
      @GwtIncompatible
      void setBits(BitSet table)
      {
        for (int i = 0; i < " 　\r   　 \013　   　 \t     \f 　 　　 \n 　".length(); i++) {
          table.set(" 　\r   　 \013　   　 \t     \f 　 　　 \n 　".charAt(i));
        }
      }
    }
    
    private static final class BreakingWhitespace
      extends CharMatcher
    {
      static final CharMatcher INSTANCE = new BreakingWhitespace();
      
      private BreakingWhitespace() {}
      
      public boolean matches(char c) { switch (c) {
        case '\t': 
        case '\n': 
        case '\013': 
        case '\f': 
        case '\r': 
        case ' ': 
        case '': 
        case ' ': 
        case ' ': 
        case ' ': 
        case ' ': 
        case '　': 
          return true;
        case ' ': 
          return false;
        }
        return (c >= ' ') && (c <= ' ');
      }
      

      public String toString()
      {
        return "CharMatcher.breakingWhitespace()";
      }
    }
    
    private static final class Ascii
      extends CharMatcher.NamedFastMatcher
    {
      static final Ascii INSTANCE = new Ascii();
      
      Ascii() {
        super();
      }
      
      public boolean matches(char c)
      {
        return c <= '';
      }
    }
    
    private static class RangesMatcher extends CharMatcher
    {
      private final String description;
      private final char[] rangeStarts;
      private final char[] rangeEnds;
      
      RangesMatcher(String description, char[] rangeStarts, char[] rangeEnds)
      {
        this.description = description;
        this.rangeStarts = rangeStarts;
        this.rangeEnds = rangeEnds;
        Preconditions.checkArgument(rangeStarts.length == rangeEnds.length);
        for (int i = 0; i < rangeStarts.length; i++) {
          Preconditions.checkArgument(rangeStarts[i] <= rangeEnds[i]);
          if (i + 1 < rangeStarts.length) {
            Preconditions.checkArgument(rangeEnds[i] < rangeStarts[(i + 1)]);
          }
        }
      }
      
      public boolean matches(char c)
      {
        int index = Arrays.binarySearch(rangeStarts, c);
        if (index >= 0) {
          return true;
        }
        index = (index ^ 0xFFFFFFFF) - 1;
        return (index >= 0) && (c <= rangeEnds[index]);
      }
      

      public String toString()
      {
        return description;
      }
    }
    


    private static final class Digit
      extends CharMatcher.RangesMatcher
    {
      private static final String ZEROES = "0٠۰߀०০੦૦୦௦౦೦൦๐໐༠၀႐០᠐᥆᧐᭐᮰᱀᱐꘠꣐꤀꩐０";
      

      private static char[] zeroes()
      {
        return "0٠۰߀०০੦૦୦௦౦೦൦๐໐༠၀႐០᠐᥆᧐᭐᮰᱀᱐꘠꣐꤀꩐０".toCharArray();
      }
      
      private static char[] nines() {
        char[] nines = new char["0٠۰߀०০੦૦୦௦౦೦൦๐໐༠၀႐០᠐᥆᧐᭐᮰᱀᱐꘠꣐꤀꩐０".length()];
        for (int i = 0; i < "0٠۰߀०০੦૦୦௦౦೦൦๐໐༠၀႐០᠐᥆᧐᭐᮰᱀᱐꘠꣐꤀꩐０".length(); i++) {
          nines[i] = ((char)("0٠۰߀०০੦૦୦௦౦೦൦๐໐༠၀႐០᠐᥆᧐᭐᮰᱀᱐꘠꣐꤀꩐０".charAt(i) + '\t'));
        }
        return nines;
      }
      
      static final Digit INSTANCE = new Digit();
      
      private Digit() {
        super(zeroes(), nines());
      }
    }
    
    private static final class JavaDigit
      extends CharMatcher
    {
      static final JavaDigit INSTANCE = new JavaDigit();
      
      private JavaDigit() {}
      
      public boolean matches(char c) { return Character.isDigit(c); }
      

      public String toString()
      {
        return "CharMatcher.javaDigit()";
      }
    }
    
    private static final class JavaLetter
      extends CharMatcher
    {
      static final JavaLetter INSTANCE = new JavaLetter();
      
      private JavaLetter() {}
      
      public boolean matches(char c) { return Character.isLetter(c); }
      

      public String toString()
      {
        return "CharMatcher.javaLetter()";
      }
    }
    
    private static final class JavaLetterOrDigit
      extends CharMatcher
    {
      static final JavaLetterOrDigit INSTANCE = new JavaLetterOrDigit();
      
      private JavaLetterOrDigit() {}
      
      public boolean matches(char c) { return Character.isLetterOrDigit(c); }
      

      public String toString()
      {
        return "CharMatcher.javaLetterOrDigit()";
      }
    }
    
    private static final class JavaUpperCase
      extends CharMatcher
    {
      static final JavaUpperCase INSTANCE = new JavaUpperCase();
      
      private JavaUpperCase() {}
      
      public boolean matches(char c) { return Character.isUpperCase(c); }
      

      public String toString()
      {
        return "CharMatcher.javaUpperCase()";
      }
    }
    
    private static final class JavaLowerCase
      extends CharMatcher
    {
      static final JavaLowerCase INSTANCE = new JavaLowerCase();
      
      private JavaLowerCase() {}
      
      public boolean matches(char c) { return Character.isLowerCase(c); }
      

      public String toString()
      {
        return "CharMatcher.javaLowerCase()";
      }
    }
    
    private static final class JavaIsoControl
      extends CharMatcher.NamedFastMatcher
    {
      static final JavaIsoControl INSTANCE = new JavaIsoControl();
      
      private JavaIsoControl() {
        super();
      }
      
      public boolean matches(char c)
      {
        return (c <= '\037') || ((c >= '') && (c <= ''));
      }
    }
    


    private static final class Invisible
      extends CharMatcher.RangesMatcher
    {
      private static final String RANGE_STARTS = "\000­؀؜۝܏ ᠎   ⁦⁧⁨⁩⁪　?﻿￹￺";
      

      private static final String RANGE_ENDS = "  ­؄؜۝܏ ᠎‏ ⁤⁦⁧⁨⁩⁯　﻿￹￻";
      
      static final Invisible INSTANCE = new Invisible();
      
      private Invisible() {
        super("\000­؀؜۝܏ ᠎   ⁦⁧⁨⁩⁪　?﻿￹￺".toCharArray(), "  ­؄؜۝܏ ᠎‏ ⁤⁦⁧⁨⁩⁯　﻿￹￻".toCharArray());
      }
    }
    
    private static final class SingleWidth
      extends CharMatcher.RangesMatcher
    {
      static final SingleWidth INSTANCE = new SingleWidth();
      
      private SingleWidth() {
        super("\000־א׳؀ݐ฀Ḁ℀ﭐﹰ｡"
        
          .toCharArray(), "ӹ־ת״ۿݿ๿₯℺﷿﻿ￜ"
          .toCharArray());
      }
    }
    

    private static class Negated
      extends CharMatcher
    {
      final CharMatcher original;
      
      Negated(CharMatcher original)
      {
        this.original = ((CharMatcher)Preconditions.checkNotNull(original));
      }
      
      public boolean matches(char c)
      {
        return !original.matches(c);
      }
      
      public boolean matchesAllOf(CharSequence sequence)
      {
        return original.matchesNoneOf(sequence);
      }
      
      public boolean matchesNoneOf(CharSequence sequence)
      {
        return original.matchesAllOf(sequence);
      }
      
      public int countIn(CharSequence sequence)
      {
        return sequence.length() - original.countIn(sequence);
      }
      
      @GwtIncompatible
      void setBits(BitSet table)
      {
        BitSet tmp = new BitSet();
        original.setBits(tmp);
        tmp.flip(0, 65536);
        table.or(tmp);
      }
      
      public CharMatcher negate()
      {
        return original;
      }
      
      public String toString()
      {
        return original + ".negate()";
      }
    }
    
    private static final class And extends CharMatcher
    {
      final CharMatcher first;
      final CharMatcher second;
      
      And(CharMatcher a, CharMatcher b)
      {
        first = ((CharMatcher)Preconditions.checkNotNull(a));
        second = ((CharMatcher)Preconditions.checkNotNull(b));
      }
      
      public boolean matches(char c)
      {
        return (first.matches(c)) && (second.matches(c));
      }
      
      @GwtIncompatible
      void setBits(BitSet table)
      {
        BitSet tmp1 = new BitSet();
        first.setBits(tmp1);
        BitSet tmp2 = new BitSet();
        second.setBits(tmp2);
        tmp1.and(tmp2);
        table.or(tmp1);
      }
      
      public String toString()
      {
        return "CharMatcher.and(" + first + ", " + second + ")";
      }
    }
    
    private static final class Or extends CharMatcher
    {
      final CharMatcher first;
      final CharMatcher second;
      
      Or(CharMatcher a, CharMatcher b)
      {
        first = ((CharMatcher)Preconditions.checkNotNull(a));
        second = ((CharMatcher)Preconditions.checkNotNull(b));
      }
      
      @GwtIncompatible
      void setBits(BitSet table)
      {
        first.setBits(table);
        second.setBits(table);
      }
      
      public boolean matches(char c)
      {
        return (first.matches(c)) || (second.matches(c));
      }
      
      public String toString()
      {
        return "CharMatcher.or(" + first + ", " + second + ")";
      }
    }
    

    private static final class Is
      extends CharMatcher.FastMatcher
    {
      private final char match;
      
      Is(char match)
      {
        this.match = match;
      }
      
      public boolean matches(char c)
      {
        return c == match;
      }
      
      public String replaceFrom(CharSequence sequence, char replacement)
      {
        return sequence.toString().replace(match, replacement);
      }
      
      public CharMatcher and(CharMatcher other)
      {
        return other.matches(match) ? this : none();
      }
      
      public CharMatcher or(CharMatcher other)
      {
        return other.matches(match) ? other : super.or(other);
      }
      
      public CharMatcher negate()
      {
        return isNot(match);
      }
      
      @GwtIncompatible
      void setBits(BitSet table)
      {
        table.set(match);
      }
      
      public String toString()
      {
        return "CharMatcher.is('" + CharMatcher.showCharacter(match) + "')";
      }
    }
    
    private static final class IsNot extends CharMatcher.FastMatcher
    {
      private final char match;
      
      IsNot(char match)
      {
        this.match = match;
      }
      
      public boolean matches(char c)
      {
        return c != match;
      }
      
      public CharMatcher and(CharMatcher other)
      {
        return other.matches(match) ? super.and(other) : other;
      }
      
      public CharMatcher or(CharMatcher other)
      {
        return other.matches(match) ? any() : this;
      }
      
      @GwtIncompatible
      void setBits(BitSet table)
      {
        table.set(0, match);
        table.set(match + '\001', 65536);
      }
      
      public CharMatcher negate()
      {
        return is(match);
      }
      
      public String toString()
      {
        return "CharMatcher.isNot('" + CharMatcher.showCharacter(match) + "')";
      }
    }
    
    private static IsEither isEither(char c1, char c2) {
      return new IsEither(c1, c2);
    }
    
    private static final class IsEither extends CharMatcher.FastMatcher
    {
      private final char match1;
      private final char match2;
      
      IsEither(char match1, char match2)
      {
        this.match1 = match1;
        this.match2 = match2;
      }
      
      public boolean matches(char c)
      {
        return (c == match1) || (c == match2);
      }
      
      @GwtIncompatible
      void setBits(BitSet table)
      {
        table.set(match1);
        table.set(match2);
      }
      
      public String toString()
      {
        return "CharMatcher.anyOf(\"" + CharMatcher.showCharacter(match1) + CharMatcher.showCharacter(match2) + "\")";
      }
    }
    
    private static final class AnyOf extends CharMatcher
    {
      private final char[] chars;
      
      public AnyOf(CharSequence chars)
      {
        this.chars = chars.toString().toCharArray();
        Arrays.sort(this.chars);
      }
      
      public boolean matches(char c)
      {
        return Arrays.binarySearch(chars, c) >= 0;
      }
      
      @GwtIncompatible
      void setBits(BitSet table)
      {
        for (char c : chars) {
          table.set(c);
        }
      }
      
      public String toString()
      {
        StringBuilder description = new StringBuilder("CharMatcher.anyOf(\"");
        for (char c : chars) {
          description.append(CharMatcher.showCharacter(c));
        }
        description.append("\")");
        return description.toString();
      }
    }
    
    private static final class InRange extends CharMatcher.FastMatcher
    {
      private final char startInclusive;
      private final char endInclusive;
      
      InRange(char startInclusive, char endInclusive)
      {
        Preconditions.checkArgument(endInclusive >= startInclusive);
        this.startInclusive = startInclusive;
        this.endInclusive = endInclusive;
      }
      
      public boolean matches(char c)
      {
        return (startInclusive <= c) && (c <= endInclusive);
      }
      
      @GwtIncompatible
      void setBits(BitSet table)
      {
        table.set(startInclusive, endInclusive + '\001');
      }
      
      public String toString()
      {
        return 
        

          "CharMatcher.inRange('" + CharMatcher.showCharacter(startInclusive) + "', '" + CharMatcher.showCharacter(endInclusive) + "')";
      }
    }
    
    private static final class ForPredicate
      extends CharMatcher
    {
      private final Predicate<? super Character> predicate;
      
      ForPredicate(Predicate<? super Character> predicate)
      {
        this.predicate = ((Predicate)Preconditions.checkNotNull(predicate));
      }
      
      public boolean matches(char c)
      {
        return predicate.apply(Character.valueOf(c));
      }
      

      public boolean apply(Character character)
      {
        return predicate.apply(Preconditions.checkNotNull(character));
      }
      
      public String toString()
      {
        return "CharMatcher.forPredicate(" + predicate + ")";
      }
    }
  }
