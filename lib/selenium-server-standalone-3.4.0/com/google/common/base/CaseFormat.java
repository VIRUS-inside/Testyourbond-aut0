package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import javax.annotation.Nullable;


























@GwtCompatible
public enum CaseFormat
{
  LOWER_HYPHEN(CharMatcher.is('-'), "-"), 
  



















  LOWER_UNDERSCORE(CharMatcher.is('_'), "_"), 
  



















  LOWER_CAMEL(CharMatcher.inRange('A', 'Z'), ""), 
  








  UPPER_CAMEL(CharMatcher.inRange('A', 'Z'), ""), 
  








  UPPER_UNDERSCORE(CharMatcher.is('_'), "_");
  





  private final CharMatcher wordBoundary;
  




  private final String wordSeparator;
  





  private CaseFormat(CharMatcher wordBoundary, String wordSeparator)
  {
    this.wordBoundary = wordBoundary;
    this.wordSeparator = wordSeparator;
  }
  




  public final String to(CaseFormat format, String str)
  {
    Preconditions.checkNotNull(format);
    Preconditions.checkNotNull(str);
    return format == this ? str : convert(format, str);
  }
  



  String convert(CaseFormat format, String s)
  {
    StringBuilder out = null;
    int i = 0;
    int j = -1;
    while ((j = wordBoundary.indexIn(s, ++j)) != -1) {
      if (i == 0)
      {
        out = new StringBuilder(s.length() + 4 * wordSeparator.length());
        out.append(format.normalizeFirstWord(s.substring(i, j)));
      } else {
        out.append(format.normalizeWord(s.substring(i, j)));
      }
      out.append(wordSeparator);
      i = j + wordSeparator.length();
    }
    return 
    
      format.normalizeWord(s.substring(i));
  }
  






  public Converter<String, String> converterTo(CaseFormat targetFormat) { return new StringConverter(this, targetFormat); }
  
  abstract String normalizeWord(String paramString);
  
  private static final class StringConverter extends Converter<String, String> implements Serializable {
    private final CaseFormat sourceFormat;
    private final CaseFormat targetFormat;
    private static final long serialVersionUID = 0L;
    
    StringConverter(CaseFormat sourceFormat, CaseFormat targetFormat) {
      this.sourceFormat = ((CaseFormat)Preconditions.checkNotNull(sourceFormat));
      this.targetFormat = ((CaseFormat)Preconditions.checkNotNull(targetFormat));
    }
    
    protected String doForward(String s)
    {
      return sourceFormat.to(targetFormat, s);
    }
    
    protected String doBackward(String s)
    {
      return targetFormat.to(sourceFormat, s);
    }
    
    public boolean equals(@Nullable Object object)
    {
      if ((object instanceof StringConverter)) {
        StringConverter that = (StringConverter)object;
        return (sourceFormat.equals(sourceFormat)) && (targetFormat.equals(targetFormat));
      }
      return false;
    }
    
    public int hashCode()
    {
      return sourceFormat.hashCode() ^ targetFormat.hashCode();
    }
    
    public String toString()
    {
      return sourceFormat + ".converterTo(" + targetFormat + ")";
    }
  }
  



  private String normalizeFirstWord(String word)
  {
    return this == LOWER_CAMEL ? Ascii.toLowerCase(word) : normalizeWord(word);
  }
  
  private static String firstCharOnlyToUpper(String word) {
    return 
    
      Ascii.toUpperCase(word.charAt(0)) + Ascii.toLowerCase(word.substring(1));
  }
}
