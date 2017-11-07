package org.apache.commons.lang3.text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;















































































public class StrTokenizer
  implements ListIterator<String>, Cloneable
{
  private static final StrTokenizer CSV_TOKENIZER_PROTOTYPE = new StrTokenizer();
  static { CSV_TOKENIZER_PROTOTYPE.setDelimiterMatcher(StrMatcher.commaMatcher());
    CSV_TOKENIZER_PROTOTYPE.setQuoteMatcher(StrMatcher.doubleQuoteMatcher());
    CSV_TOKENIZER_PROTOTYPE.setIgnoredMatcher(StrMatcher.noneMatcher());
    CSV_TOKENIZER_PROTOTYPE.setTrimmerMatcher(StrMatcher.trimMatcher());
    CSV_TOKENIZER_PROTOTYPE.setEmptyTokenAsNull(false);
    CSV_TOKENIZER_PROTOTYPE.setIgnoreEmptyTokens(false);
    
    TSV_TOKENIZER_PROTOTYPE = new StrTokenizer();
    TSV_TOKENIZER_PROTOTYPE.setDelimiterMatcher(StrMatcher.tabMatcher());
    TSV_TOKENIZER_PROTOTYPE.setQuoteMatcher(StrMatcher.doubleQuoteMatcher());
    TSV_TOKENIZER_PROTOTYPE.setIgnoredMatcher(StrMatcher.noneMatcher());
    TSV_TOKENIZER_PROTOTYPE.setTrimmerMatcher(StrMatcher.trimMatcher());
    TSV_TOKENIZER_PROTOTYPE.setEmptyTokenAsNull(false);
    TSV_TOKENIZER_PROTOTYPE.setIgnoreEmptyTokens(false);
  }
  

  private static final StrTokenizer TSV_TOKENIZER_PROTOTYPE;
  
  private char[] chars;
  
  private String[] tokens;
  
  private int tokenPos;
  private StrMatcher delimMatcher = StrMatcher.splitMatcher();
  
  private StrMatcher quoteMatcher = StrMatcher.noneMatcher();
  
  private StrMatcher ignoredMatcher = StrMatcher.noneMatcher();
  
  private StrMatcher trimmerMatcher = StrMatcher.noneMatcher();
  

  private boolean emptyAsNull = false;
  
  private boolean ignoreEmptyTokens = true;
  






  private static StrTokenizer getCSVClone()
  {
    return (StrTokenizer)CSV_TOKENIZER_PROTOTYPE.clone();
  }
  








  public static StrTokenizer getCSVInstance()
  {
    return getCSVClone();
  }
  








  public static StrTokenizer getCSVInstance(String input)
  {
    StrTokenizer tok = getCSVClone();
    tok.reset(input);
    return tok;
  }
  








  public static StrTokenizer getCSVInstance(char[] input)
  {
    StrTokenizer tok = getCSVClone();
    tok.reset(input);
    return tok;
  }
  




  private static StrTokenizer getTSVClone()
  {
    return (StrTokenizer)TSV_TOKENIZER_PROTOTYPE.clone();
  }
  








  public static StrTokenizer getTSVInstance()
  {
    return getTSVClone();
  }
  






  public static StrTokenizer getTSVInstance(String input)
  {
    StrTokenizer tok = getTSVClone();
    tok.reset(input);
    return tok;
  }
  






  public static StrTokenizer getTSVInstance(char[] input)
  {
    StrTokenizer tok = getTSVClone();
    tok.reset(input);
    return tok;
  }
  







  public StrTokenizer()
  {
    chars = null;
  }
  






  public StrTokenizer(String input)
  {
    if (input != null) {
      chars = input.toCharArray();
    } else {
      chars = null;
    }
  }
  





  public StrTokenizer(String input, char delim)
  {
    this(input);
    setDelimiterChar(delim);
  }
  





  public StrTokenizer(String input, String delim)
  {
    this(input);
    setDelimiterString(delim);
  }
  





  public StrTokenizer(String input, StrMatcher delim)
  {
    this(input);
    setDelimiterMatcher(delim);
  }
  







  public StrTokenizer(String input, char delim, char quote)
  {
    this(input, delim);
    setQuoteChar(quote);
  }
  







  public StrTokenizer(String input, StrMatcher delim, StrMatcher quote)
  {
    this(input, delim);
    setQuoteMatcher(quote);
  }
  






  public StrTokenizer(char[] input)
  {
    chars = ArrayUtils.clone(input);
  }
  





  public StrTokenizer(char[] input, char delim)
  {
    this(input);
    setDelimiterChar(delim);
  }
  





  public StrTokenizer(char[] input, String delim)
  {
    this(input);
    setDelimiterString(delim);
  }
  





  public StrTokenizer(char[] input, StrMatcher delim)
  {
    this(input);
    setDelimiterMatcher(delim);
  }
  







  public StrTokenizer(char[] input, char delim, char quote)
  {
    this(input, delim);
    setQuoteChar(quote);
  }
  







  public StrTokenizer(char[] input, StrMatcher delim, StrMatcher quote)
  {
    this(input, delim);
    setQuoteMatcher(quote);
  }
  






  public int size()
  {
    checkTokenized();
    return tokens.length;
  }
  






  public String nextToken()
  {
    if (hasNext()) {
      return tokens[(tokenPos++)];
    }
    return null;
  }
  




  public String previousToken()
  {
    if (hasPrevious()) {
      return tokens[(--tokenPos)];
    }
    return null;
  }
  




  public String[] getTokenArray()
  {
    checkTokenized();
    return (String[])tokens.clone();
  }
  




  public List<String> getTokenList()
  {
    checkTokenized();
    List<String> list = new ArrayList(tokens.length);
    for (String element : tokens) {
      list.add(element);
    }
    return list;
  }
  






  public StrTokenizer reset()
  {
    tokenPos = 0;
    tokens = null;
    return this;
  }
  







  public StrTokenizer reset(String input)
  {
    reset();
    if (input != null) {
      chars = input.toCharArray();
    } else {
      chars = null;
    }
    return this;
  }
  







  public StrTokenizer reset(char[] input)
  {
    reset();
    chars = ArrayUtils.clone(input);
    return this;
  }
  







  public boolean hasNext()
  {
    checkTokenized();
    return tokenPos < tokens.length;
  }
  






  public String next()
  {
    if (hasNext()) {
      return tokens[(tokenPos++)];
    }
    throw new NoSuchElementException();
  }
  





  public int nextIndex()
  {
    return tokenPos;
  }
  





  public boolean hasPrevious()
  {
    checkTokenized();
    return tokenPos > 0;
  }
  





  public String previous()
  {
    if (hasPrevious()) {
      return tokens[(--tokenPos)];
    }
    throw new NoSuchElementException();
  }
  





  public int previousIndex()
  {
    return tokenPos - 1;
  }
  





  public void remove()
  {
    throw new UnsupportedOperationException("remove() is unsupported");
  }
  





  public void set(String obj)
  {
    throw new UnsupportedOperationException("set() is unsupported");
  }
  





  public void add(String obj)
  {
    throw new UnsupportedOperationException("add() is unsupported");
  }
  




  private void checkTokenized()
  {
    if (tokens == null) {
      if (chars == null)
      {
        List<String> split = tokenize(null, 0, 0);
        tokens = ((String[])split.toArray(new String[split.size()]));
      } else {
        List<String> split = tokenize(chars, 0, chars.length);
        tokens = ((String[])split.toArray(new String[split.size()]));
      }
    }
  }
  



















  protected List<String> tokenize(char[] srcChars, int offset, int count)
  {
    if ((srcChars == null) || (count == 0)) {
      return Collections.emptyList();
    }
    StrBuilder buf = new StrBuilder();
    List<String> tokenList = new ArrayList();
    int pos = offset;
    

    while ((pos >= 0) && (pos < count))
    {
      pos = readNextToken(srcChars, pos, count, buf, tokenList);
      

      if (pos >= count) {
        addToken(tokenList, "");
      }
    }
    return tokenList;
  }
  





  private void addToken(List<String> list, String tok)
  {
    if (StringUtils.isEmpty(tok)) {
      if (isIgnoreEmptyTokens()) {
        return;
      }
      if (isEmptyTokenAsNull()) {
        tok = null;
      }
    }
    list.add(tok);
  }
  












  private int readNextToken(char[] srcChars, int start, int len, StrBuilder workArea, List<String> tokenList)
  {
    while (start < len) {
      int removeLen = Math.max(
        getIgnoredMatcher().isMatch(srcChars, start, start, len), 
        getTrimmerMatcher().isMatch(srcChars, start, start, len));
      if ((removeLen == 0) || 
        (getDelimiterMatcher().isMatch(srcChars, start, start, len) > 0) || 
        (getQuoteMatcher().isMatch(srcChars, start, start, len) > 0)) {
        break;
      }
      start += removeLen;
    }
    

    if (start >= len) {
      addToken(tokenList, "");
      return -1;
    }
    

    int delimLen = getDelimiterMatcher().isMatch(srcChars, start, start, len);
    if (delimLen > 0) {
      addToken(tokenList, "");
      return start + delimLen;
    }
    

    int quoteLen = getQuoteMatcher().isMatch(srcChars, start, start, len);
    if (quoteLen > 0) {
      return readWithQuotes(srcChars, start + quoteLen, len, workArea, tokenList, start, quoteLen);
    }
    return readWithQuotes(srcChars, start, len, workArea, tokenList, 0, 0);
  }
  
















  private int readWithQuotes(char[] srcChars, int start, int len, StrBuilder workArea, List<String> tokenList, int quoteStart, int quoteLen)
  {
    workArea.clear();
    int pos = start;
    boolean quoting = quoteLen > 0;
    int trimStart = 0;
    
    while (pos < len)
    {


      if (quoting)
      {





        if (isQuote(srcChars, pos, len, quoteStart, quoteLen)) {
          if (isQuote(srcChars, pos + quoteLen, len, quoteStart, quoteLen))
          {
            workArea.append(srcChars, pos, quoteLen);
            pos += quoteLen * 2;
            trimStart = workArea.size();

          }
          else
          {
            quoting = false;
            pos += quoteLen;
          }
        }
        else
        {
          workArea.append(srcChars[(pos++)]);
          trimStart = workArea.size();
        }
        
      }
      else
      {
        int delimLen = getDelimiterMatcher().isMatch(srcChars, pos, start, len);
        if (delimLen > 0)
        {
          addToken(tokenList, workArea.substring(0, trimStart));
          return pos + delimLen;
        }
        

        if ((quoteLen > 0) && (isQuote(srcChars, pos, len, quoteStart, quoteLen))) {
          quoting = true;
          pos += quoteLen;

        }
        else
        {
          int ignoredLen = getIgnoredMatcher().isMatch(srcChars, pos, start, len);
          if (ignoredLen > 0) {
            pos += ignoredLen;


          }
          else
          {

            int trimmedLen = getTrimmerMatcher().isMatch(srcChars, pos, start, len);
            if (trimmedLen > 0) {
              workArea.append(srcChars, pos, trimmedLen);
              pos += trimmedLen;

            }
            else
            {
              workArea.append(srcChars[(pos++)]);
              trimStart = workArea.size();
            }
          }
        }
      } }
    addToken(tokenList, workArea.substring(0, trimStart));
    return -1;
  }
  










  private boolean isQuote(char[] srcChars, int pos, int len, int quoteStart, int quoteLen)
  {
    for (int i = 0; i < quoteLen; i++) {
      if ((pos + i >= len) || (srcChars[(pos + i)] != srcChars[(quoteStart + i)])) {
        return false;
      }
    }
    return true;
  }
  






  public StrMatcher getDelimiterMatcher()
  {
    return delimMatcher;
  }
  







  public StrTokenizer setDelimiterMatcher(StrMatcher delim)
  {
    if (delim == null) {
      delimMatcher = StrMatcher.noneMatcher();
    } else {
      delimMatcher = delim;
    }
    return this;
  }
  





  public StrTokenizer setDelimiterChar(char delim)
  {
    return setDelimiterMatcher(StrMatcher.charMatcher(delim));
  }
  





  public StrTokenizer setDelimiterString(String delim)
  {
    return setDelimiterMatcher(StrMatcher.stringMatcher(delim));
  }
  










  public StrMatcher getQuoteMatcher()
  {
    return quoteMatcher;
  }
  








  public StrTokenizer setQuoteMatcher(StrMatcher quote)
  {
    if (quote != null) {
      quoteMatcher = quote;
    }
    return this;
  }
  








  public StrTokenizer setQuoteChar(char quote)
  {
    return setQuoteMatcher(StrMatcher.charMatcher(quote));
  }
  










  public StrMatcher getIgnoredMatcher()
  {
    return ignoredMatcher;
  }
  








  public StrTokenizer setIgnoredMatcher(StrMatcher ignored)
  {
    if (ignored != null) {
      ignoredMatcher = ignored;
    }
    return this;
  }
  








  public StrTokenizer setIgnoredChar(char ignored)
  {
    return setIgnoredMatcher(StrMatcher.charMatcher(ignored));
  }
  










  public StrMatcher getTrimmerMatcher()
  {
    return trimmerMatcher;
  }
  








  public StrTokenizer setTrimmerMatcher(StrMatcher trimmer)
  {
    if (trimmer != null) {
      trimmerMatcher = trimmer;
    }
    return this;
  }
  






  public boolean isEmptyTokenAsNull()
  {
    return emptyAsNull;
  }
  






  public StrTokenizer setEmptyTokenAsNull(boolean emptyAsNull)
  {
    this.emptyAsNull = emptyAsNull;
    return this;
  }
  






  public boolean isIgnoreEmptyTokens()
  {
    return ignoreEmptyTokens;
  }
  






  public StrTokenizer setIgnoreEmptyTokens(boolean ignoreEmptyTokens)
  {
    this.ignoreEmptyTokens = ignoreEmptyTokens;
    return this;
  }
  





  public String getContent()
  {
    if (chars == null) {
      return null;
    }
    return new String(chars);
  }
  







  public Object clone()
  {
    try
    {
      return cloneReset();
    } catch (CloneNotSupportedException ex) {}
    return null;
  }
  







  Object cloneReset()
    throws CloneNotSupportedException
  {
    StrTokenizer cloned = (StrTokenizer)super.clone();
    if (chars != null) {
      chars = ((char[])chars.clone());
    }
    cloned.reset();
    return cloned;
  }
  






  public String toString()
  {
    if (tokens == null) {
      return "StrTokenizer[not tokenized yet]";
    }
    return "StrTokenizer" + getTokenList();
  }
}
