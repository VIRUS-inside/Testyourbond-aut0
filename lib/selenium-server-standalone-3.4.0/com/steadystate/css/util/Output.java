package com.steadystate.css.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;




















public final class Output
{
  private static final String NEW_LINE = System.getProperty("line.separator");
  

  private Writer writer_;
  
  private StringBuffer currentIndent_;
  
  private boolean afterNewLine_;
  
  private final String indent_;
  

  public Output(Writer aWriter, String anIndent)
  {
    writer_ = new BufferedWriter(aWriter);
    indent_ = anIndent;
    currentIndent_ = new StringBuffer();
  }
  





  public Output print(char aChar)
    throws IOException
  {
    writeIndentIfNeeded();
    writer_.write(aChar);
    return this;
  }
  





  public Output print(String aString)
    throws IOException
  {
    if (null != aString) {
      writeIndentIfNeeded();
      writer_.write(aString);
    }
    return this;
  }
  





  public Output println(String aString)
    throws IOException
  {
    writeIndentIfNeeded();
    writer_.write(aString);
    writer_.write(NEW_LINE);
    afterNewLine_ = true;
    return this;
  }
  




  public Output println()
    throws IOException
  {
    writer_.write(NEW_LINE);
    afterNewLine_ = true;
    return this;
  }
  




  public Output flush()
    throws IOException
  {
    writer_.flush();
    return this;
  }
  




  public Output indent()
  {
    currentIndent_.append(indent_);
    return this;
  }
  




  public Output unindent()
  {
    currentIndent_.setLength(Math.max(0, currentIndent_.length() - indent_.length()));
    return this;
  }
  




  private void writeIndentIfNeeded()
    throws IOException
  {
    if (afterNewLine_) {
      writer_.write(currentIndent_.toString());
      afterNewLine_ = false;
    }
  }
}
