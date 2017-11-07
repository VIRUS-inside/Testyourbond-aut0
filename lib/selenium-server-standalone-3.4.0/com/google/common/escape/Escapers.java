package com.google.common.escape;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;
























@Beta
@GwtCompatible
public final class Escapers
{
  private Escapers() {}
  
  public static Escaper nullEscaper()
  {
    return NULL_ESCAPER;
  }
  


  private static final Escaper NULL_ESCAPER = new CharEscaper()
  {
    public String escape(String string)
    {
      return (String)Preconditions.checkNotNull(string);
    }
    

    protected char[] escape(char c)
    {
      return null;
    }
  };
  














  public static Builder builder()
  {
    return new Builder(null);
  }
  










  @Beta
  public static final class Builder
  {
    private final Map<Character, String> replacementMap = new HashMap();
    private char safeMin = '\000';
    private char safeMax = 65535;
    private String unsafeReplacement = null;
    





    private Builder() {}
    




    @CanIgnoreReturnValue
    public Builder setSafeRange(char safeMin, char safeMax)
    {
      this.safeMin = safeMin;
      this.safeMax = safeMax;
      return this;
    }
    







    @CanIgnoreReturnValue
    public Builder setUnsafeReplacement(@Nullable String unsafeReplacement)
    {
      this.unsafeReplacement = unsafeReplacement;
      return this;
    }
    









    @CanIgnoreReturnValue
    public Builder addEscape(char c, String replacement)
    {
      Preconditions.checkNotNull(replacement);
      
      replacementMap.put(Character.valueOf(c), replacement);
      return this;
    }
    


    public Escaper build()
    {
      new ArrayBasedCharEscaper(replacementMap, safeMin, safeMax)
      {
        private final char[] replacementChars = unsafeReplacement != null ? unsafeReplacement.toCharArray() : null;
        
        protected char[] escapeUnsafe(char c)
        {
          return replacementChars;
        }
      };
    }
  }
  













  static UnicodeEscaper asUnicodeEscaper(Escaper escaper)
  {
    Preconditions.checkNotNull(escaper);
    if ((escaper instanceof UnicodeEscaper))
      return (UnicodeEscaper)escaper;
    if ((escaper instanceof CharEscaper)) {
      return wrap((CharEscaper)escaper);
    }
    


    throw new IllegalArgumentException("Cannot create a UnicodeEscaper from: " + escaper.getClass().getName());
  }
  








  public static String computeReplacement(CharEscaper escaper, char c)
  {
    return stringOrNull(escaper.escape(c));
  }
  








  public static String computeReplacement(UnicodeEscaper escaper, int cp)
  {
    return stringOrNull(escaper.escape(cp));
  }
  
  private static String stringOrNull(char[] in) {
    return in == null ? null : new String(in);
  }
  
  private static UnicodeEscaper wrap(CharEscaper escaper)
  {
    new UnicodeEscaper()
    {
      protected char[] escape(int cp)
      {
        if (cp < 65536) {
          return val$escaper.escape((char)cp);
        }
        



        char[] surrogateChars = new char[2];
        Character.toChars(cp, surrogateChars, 0);
        char[] hiChars = val$escaper.escape(surrogateChars[0]);
        char[] loChars = val$escaper.escape(surrogateChars[1]);
        




        if ((hiChars == null) && (loChars == null))
        {
          return null;
        }
        
        int hiCount = hiChars != null ? hiChars.length : 1;
        int loCount = loChars != null ? loChars.length : 1;
        char[] output = new char[hiCount + loCount];
        if (hiChars != null)
        {
          for (int n = 0; n < hiChars.length; n++) {
            output[n] = hiChars[n];
          }
        } else {
          output[0] = surrogateChars[0];
        }
        if (loChars != null) {
          for (int n = 0; n < loChars.length; n++) {
            output[(hiCount + n)] = loChars[n];
          }
        } else {
          output[hiCount] = surrogateChars[1];
        }
        return output;
      }
    };
  }
}
