package com.google.gson.stream;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.Writer;







































































































































public class JsonWriter
  implements Closeable, Flushable
{
  private static final String[] REPLACEMENT_CHARS = new String[''];
  static { for (int i = 0; i <= 31; i++) {
      REPLACEMENT_CHARS[i] = String.format("\\u%04x", new Object[] { Integer.valueOf(i) });
    }
    REPLACEMENT_CHARS[34] = "\\\"";
    REPLACEMENT_CHARS[92] = "\\\\";
    REPLACEMENT_CHARS[9] = "\\t";
    REPLACEMENT_CHARS[8] = "\\b";
    REPLACEMENT_CHARS[10] = "\\n";
    REPLACEMENT_CHARS[13] = "\\r";
    REPLACEMENT_CHARS[12] = "\\f";
    HTML_SAFE_REPLACEMENT_CHARS = (String[])REPLACEMENT_CHARS.clone();
    HTML_SAFE_REPLACEMENT_CHARS[60] = "\\u003c";
    HTML_SAFE_REPLACEMENT_CHARS[62] = "\\u003e";
    HTML_SAFE_REPLACEMENT_CHARS[38] = "\\u0026";
    HTML_SAFE_REPLACEMENT_CHARS[61] = "\\u003d";
    HTML_SAFE_REPLACEMENT_CHARS[39] = "\\u0027";
  }
  

  private static final String[] HTML_SAFE_REPLACEMENT_CHARS;
  private final Writer out;
  private int[] stack = new int[32];
  private int stackSize = 0;
  
  public JsonWriter(Writer out) { push(6);
    










    separator = ":";
    






    serializeNulls = true;
    






    if (out == null) {
      throw new NullPointerException("out == null");
    }
    this.out = out;
  }
  

  private String indent;
  
  private String separator;
  
  private boolean lenient;
  
  public final void setIndent(String indent)
  {
    if (indent.length() == 0) {
      this.indent = null;
      separator = ":";
    } else {
      this.indent = indent;
      separator = ": ";
    }
  }
  


  private boolean htmlSafe;
  

  private String deferredName;
  

  private boolean serializeNulls;
  

  public final void setLenient(boolean lenient)
  {
    this.lenient = lenient;
  }
  


  public boolean isLenient()
  {
    return lenient;
  }
  






  public final void setHtmlSafe(boolean htmlSafe)
  {
    this.htmlSafe = htmlSafe;
  }
  



  public final boolean isHtmlSafe()
  {
    return htmlSafe;
  }
  



  public final void setSerializeNulls(boolean serializeNulls)
  {
    this.serializeNulls = serializeNulls;
  }
  



  public final boolean getSerializeNulls()
  {
    return serializeNulls;
  }
  




  public JsonWriter beginArray()
    throws IOException
  {
    writeDeferredName();
    return open(1, "[");
  }
  



  public JsonWriter endArray()
    throws IOException
  {
    return close(1, 2, "]");
  }
  




  public JsonWriter beginObject()
    throws IOException
  {
    writeDeferredName();
    return open(3, "{");
  }
  



  public JsonWriter endObject()
    throws IOException
  {
    return close(3, 5, "}");
  }
  


  private JsonWriter open(int empty, String openBracket)
    throws IOException
  {
    beforeValue();
    push(empty);
    out.write(openBracket);
    return this;
  }
  



  private JsonWriter close(int empty, int nonempty, String closeBracket)
    throws IOException
  {
    int context = peek();
    if ((context != nonempty) && (context != empty)) {
      throw new IllegalStateException("Nesting problem.");
    }
    if (deferredName != null) {
      throw new IllegalStateException("Dangling name: " + deferredName);
    }
    
    stackSize -= 1;
    if (context == nonempty) {
      newline();
    }
    out.write(closeBracket);
    return this;
  }
  
  private void push(int newTop) {
    if (stackSize == stack.length) {
      int[] newStack = new int[stackSize * 2];
      System.arraycopy(stack, 0, newStack, 0, stackSize);
      stack = newStack;
    }
    stack[(stackSize++)] = newTop;
  }
  


  private int peek()
  {
    if (stackSize == 0) {
      throw new IllegalStateException("JsonWriter is closed.");
    }
    return stack[(stackSize - 1)];
  }
  


  private void replaceTop(int topOfStack)
  {
    stack[(stackSize - 1)] = topOfStack;
  }
  




  public JsonWriter name(String name)
    throws IOException
  {
    if (name == null) {
      throw new NullPointerException("name == null");
    }
    if (deferredName != null) {
      throw new IllegalStateException();
    }
    if (stackSize == 0) {
      throw new IllegalStateException("JsonWriter is closed.");
    }
    deferredName = name;
    return this;
  }
  
  private void writeDeferredName() throws IOException {
    if (deferredName != null) {
      beforeName();
      string(deferredName);
      deferredName = null;
    }
  }
  




  public JsonWriter value(String value)
    throws IOException
  {
    if (value == null) {
      return nullValue();
    }
    writeDeferredName();
    beforeValue();
    string(value);
    return this;
  }
  





  public JsonWriter jsonValue(String value)
    throws IOException
  {
    if (value == null) {
      return nullValue();
    }
    writeDeferredName();
    beforeValue();
    out.append(value);
    return this;
  }
  



  public JsonWriter nullValue()
    throws IOException
  {
    if (deferredName != null) {
      if (serializeNulls) {
        writeDeferredName();
      } else {
        deferredName = null;
        return this;
      }
    }
    beforeValue();
    out.write("null");
    return this;
  }
  



  public JsonWriter value(boolean value)
    throws IOException
  {
    writeDeferredName();
    beforeValue();
    out.write(value ? "true" : "false");
    return this;
  }
  



  public JsonWriter value(Boolean value)
    throws IOException
  {
    if (value == null) {
      return nullValue();
    }
    writeDeferredName();
    beforeValue();
    out.write(value.booleanValue() ? "true" : "false");
    return this;
  }
  





  public JsonWriter value(double value)
    throws IOException
  {
    if ((Double.isNaN(value)) || (Double.isInfinite(value))) {
      throw new IllegalArgumentException("Numeric values must be finite, but was " + value);
    }
    writeDeferredName();
    beforeValue();
    out.append(Double.toString(value));
    return this;
  }
  



  public JsonWriter value(long value)
    throws IOException
  {
    writeDeferredName();
    beforeValue();
    out.write(Long.toString(value));
    return this;
  }
  





  public JsonWriter value(Number value)
    throws IOException
  {
    if (value == null) {
      return nullValue();
    }
    
    writeDeferredName();
    String string = value.toString();
    if ((!lenient) && (
      (string.equals("-Infinity")) || (string.equals("Infinity")) || (string.equals("NaN")))) {
      throw new IllegalArgumentException("Numeric values must be finite, but was " + value);
    }
    beforeValue();
    out.append(string);
    return this;
  }
  


  public void flush()
    throws IOException
  {
    if (stackSize == 0) {
      throw new IllegalStateException("JsonWriter is closed.");
    }
    out.flush();
  }
  



  public void close()
    throws IOException
  {
    out.close();
    
    int size = stackSize;
    if ((size > 1) || ((size == 1) && (stack[(size - 1)] != 7))) {
      throw new IOException("Incomplete document");
    }
    stackSize = 0;
  }
  
  private void string(String value) throws IOException {
    String[] replacements = htmlSafe ? HTML_SAFE_REPLACEMENT_CHARS : REPLACEMENT_CHARS;
    out.write("\"");
    int last = 0;
    int length = value.length();
    for (int i = 0; i < length; i++) {
      char c = value.charAt(i);
      String replacement;
      if (c < '') {
        String replacement = replacements[c];
        if (replacement == null)
          continue;
      } else { String replacement;
        if (c == ' ') {
          replacement = "\\u2028";
        } else { if (c != ' ') continue;
          replacement = "\\u2029";
        }
      }
      
      if (last < i) {
        out.write(value, last, i - last);
      }
      out.write(replacement);
      last = i + 1;
    }
    if (last < length) {
      out.write(value, last, length - last);
    }
    out.write("\"");
  }
  
  private void newline() throws IOException {
    if (indent == null) {
      return;
    }
    
    out.write("\n");
    int i = 1; for (int size = stackSize; i < size; i++) {
      out.write(indent);
    }
  }
  


  private void beforeName()
    throws IOException
  {
    int context = peek();
    if (context == 5) {
      out.write(44);
    } else if (context != 3) {
      throw new IllegalStateException("Nesting problem.");
    }
    newline();
    replaceTop(4);
  }
  




  private void beforeValue()
    throws IOException
  {
    switch (peek()) {
    case 7: 
      if (!lenient) {
        throw new IllegalStateException("JSON must have only one top-level value.");
      }
    

    case 6: 
      replaceTop(7);
      break;
    
    case 1: 
      replaceTop(2);
      newline();
      break;
    
    case 2: 
      out.append(',');
      newline();
      break;
    
    case 4: 
      out.append(separator);
      replaceTop(5);
      break;
    }
    
    throw new IllegalStateException("Nesting problem.");
  }
}
