package org.apache.commons.lang3.text.translate;

import java.io.IOException;
import java.io.Writer;






























public class NumericEntityEscaper
  extends CodePointTranslator
{
  private final int below;
  private final int above;
  private final boolean between;
  
  private NumericEntityEscaper(int below, int above, boolean between)
  {
    this.below = below;
    this.above = above;
    this.between = between;
  }
  


  public NumericEntityEscaper()
  {
    this(0, Integer.MAX_VALUE, true);
  }
  





  public static NumericEntityEscaper below(int codepoint)
  {
    return outsideOf(codepoint, Integer.MAX_VALUE);
  }
  





  public static NumericEntityEscaper above(int codepoint)
  {
    return outsideOf(0, codepoint);
  }
  






  public static NumericEntityEscaper between(int codepointLow, int codepointHigh)
  {
    return new NumericEntityEscaper(codepointLow, codepointHigh, true);
  }
  






  public static NumericEntityEscaper outsideOf(int codepointLow, int codepointHigh)
  {
    return new NumericEntityEscaper(codepointLow, codepointHigh, false);
  }
  


  public boolean translate(int codepoint, Writer out)
    throws IOException
  {
    if (between) {
      if ((codepoint < below) || (codepoint > above)) {
        return false;
      }
    }
    else if ((codepoint >= below) && (codepoint <= above)) {
      return false;
    }
    

    out.write("&#");
    out.write(Integer.toString(codepoint, 10));
    out.write(59);
    return true;
  }
}
