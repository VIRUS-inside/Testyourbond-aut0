package com.google.gson.stream;

import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.bind.JsonTreeReader;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;




















































































































































































public class JsonReader
  implements Closeable
{
  private static final char[] NON_EXECUTE_PREFIX = ")]}'\n".toCharArray();
  
  private static final long MIN_INCOMPLETE_INTEGER = -922337203685477580L;
  
  private static final int PEEKED_NONE = 0;
  
  private static final int PEEKED_BEGIN_OBJECT = 1;
  
  private static final int PEEKED_END_OBJECT = 2;
  
  private static final int PEEKED_BEGIN_ARRAY = 3;
  
  private static final int PEEKED_END_ARRAY = 4;
  
  private static final int PEEKED_TRUE = 5;
  
  private static final int PEEKED_FALSE = 6;
  
  private static final int PEEKED_NULL = 7;
  private static final int PEEKED_SINGLE_QUOTED = 8;
  private static final int PEEKED_DOUBLE_QUOTED = 9;
  private static final int PEEKED_UNQUOTED = 10;
  private static final int PEEKED_BUFFERED = 11;
  private static final int PEEKED_SINGLE_QUOTED_NAME = 12;
  private static final int PEEKED_DOUBLE_QUOTED_NAME = 13;
  private static final int PEEKED_UNQUOTED_NAME = 14;
  private static final int PEEKED_LONG = 15;
  private static final int PEEKED_NUMBER = 16;
  private static final int PEEKED_EOF = 17;
  private static final int NUMBER_CHAR_NONE = 0;
  private static final int NUMBER_CHAR_SIGN = 1;
  private static final int NUMBER_CHAR_DIGIT = 2;
  private static final int NUMBER_CHAR_DECIMAL = 3;
  private static final int NUMBER_CHAR_FRACTION_DIGIT = 4;
  private static final int NUMBER_CHAR_EXP_E = 5;
  private static final int NUMBER_CHAR_EXP_SIGN = 6;
  private static final int NUMBER_CHAR_EXP_DIGIT = 7;
  private final Reader in;
  private boolean lenient = false;
  






  private final char[] buffer = new char['Ѐ'];
  private int pos = 0;
  private int limit = 0;
  
  private int lineNumber = 0;
  private int lineStart = 0;
  
  int peeked = 0;
  




  private long peekedLong;
  




  private int peekedNumberLength;
  




  private String peekedString;
  




  private int[] stack = new int[32];
  private int stackSize = 0;
  
  public JsonReader(Reader in) { stack[(stackSize++)] = 6;
    









    pathNames = new String[32];
    pathIndices = new int[32];
    




    if (in == null) {
      throw new NullPointerException("in == null");
    }
    this.in = in;
  }
  









  private String[] pathNames;
  








  private int[] pathIndices;
  







  public final void setLenient(boolean lenient)
  {
    this.lenient = lenient;
  }
  


  public final boolean isLenient()
  {
    return lenient;
  }
  


  public void beginArray()
    throws IOException
  {
    int p = peeked;
    if (p == 0) {
      p = doPeek();
    }
    if (p == 3) {
      push(1);
      pathIndices[(stackSize - 1)] = 0;
      peeked = 0;
    } else {
      throw new IllegalStateException("Expected BEGIN_ARRAY but was " + peek() + locationString());
    }
  }
  


  public void endArray()
    throws IOException
  {
    int p = peeked;
    if (p == 0) {
      p = doPeek();
    }
    if (p == 4) {
      stackSize -= 1;
      pathIndices[(stackSize - 1)] += 1;
      peeked = 0;
    } else {
      throw new IllegalStateException("Expected END_ARRAY but was " + peek() + locationString());
    }
  }
  


  public void beginObject()
    throws IOException
  {
    int p = peeked;
    if (p == 0) {
      p = doPeek();
    }
    if (p == 1) {
      push(3);
      peeked = 0;
    } else {
      throw new IllegalStateException("Expected BEGIN_OBJECT but was " + peek() + locationString());
    }
  }
  


  public void endObject()
    throws IOException
  {
    int p = peeked;
    if (p == 0) {
      p = doPeek();
    }
    if (p == 2) {
      stackSize -= 1;
      pathNames[stackSize] = null;
      pathIndices[(stackSize - 1)] += 1;
      peeked = 0;
    } else {
      throw new IllegalStateException("Expected END_OBJECT but was " + peek() + locationString());
    }
  }
  

  public boolean hasNext()
    throws IOException
  {
    int p = peeked;
    if (p == 0) {
      p = doPeek();
    }
    return (p != 2) && (p != 4);
  }
  

  public JsonToken peek()
    throws IOException
  {
    int p = peeked;
    if (p == 0) {
      p = doPeek();
    }
    
    switch (p) {
    case 1: 
      return JsonToken.BEGIN_OBJECT;
    case 2: 
      return JsonToken.END_OBJECT;
    case 3: 
      return JsonToken.BEGIN_ARRAY;
    case 4: 
      return JsonToken.END_ARRAY;
    case 12: 
    case 13: 
    case 14: 
      return JsonToken.NAME;
    case 5: 
    case 6: 
      return JsonToken.BOOLEAN;
    case 7: 
      return JsonToken.NULL;
    case 8: 
    case 9: 
    case 10: 
    case 11: 
      return JsonToken.STRING;
    case 15: 
    case 16: 
      return JsonToken.NUMBER;
    case 17: 
      return JsonToken.END_DOCUMENT;
    }
    throw new AssertionError();
  }
  
  int doPeek() throws IOException
  {
    int peekStack = stack[(stackSize - 1)];
    if (peekStack == 1) {
      stack[(stackSize - 1)] = 2;
    } else if (peekStack == 2)
    {
      int c = nextNonWhitespace(true);
      switch (c) {
      case 93: 
        return this.peeked = 4;
      case 59: 
        checkLenient();
      case 44: 
        break;
      default: 
        throw syntaxError("Unterminated array"); }
    } else {
      if ((peekStack == 3) || (peekStack == 5)) {
        stack[(stackSize - 1)] = 4;
        
        if (peekStack == 5) {
          int c = nextNonWhitespace(true);
          switch (c) {
          case 125: 
            return this.peeked = 2;
          case 59: 
            checkLenient();
          case 44: 
            break;
          default: 
            throw syntaxError("Unterminated object");
          }
        }
        int c = nextNonWhitespace(true);
        switch (c) {
        case 34: 
          return this.peeked = 13;
        case 39: 
          checkLenient();
          return this.peeked = 12;
        case 125: 
          if (peekStack != 5) {
            return this.peeked = 2;
          }
          throw syntaxError("Expected name");
        }
        
        checkLenient();
        pos -= 1;
        if (isLiteral((char)c)) {
          return this.peeked = 14;
        }
        throw syntaxError("Expected name");
      }
      
      if (peekStack == 4) {
        stack[(stackSize - 1)] = 5;
        
        int c = nextNonWhitespace(true);
        switch (c) {
        case 58: 
          break;
        case 61: 
          checkLenient();
          if (((pos < limit) || (fillBuffer(1))) && (buffer[pos] == '>')) {
            pos += 1;
          }
          break;
        default: 
          throw syntaxError("Expected ':'");
        }
      } else if (peekStack == 6) {
        if (lenient) {
          consumeNonExecutePrefix();
        }
        stack[(stackSize - 1)] = 7;
      } else if (peekStack == 7) {
        int c = nextNonWhitespace(false);
        if (c == -1) {
          return this.peeked = 17;
        }
        checkLenient();
        pos -= 1;
      }
      else if (peekStack == 8) {
        throw new IllegalStateException("JsonReader is closed");
      }
    }
    int c = nextNonWhitespace(true);
    switch (c) {
    case 93: 
      if (peekStack == 1) {
        return this.peeked = 4;
      }
    

    case 44: 
    case 59: 
      if ((peekStack == 1) || (peekStack == 2)) {
        checkLenient();
        pos -= 1;
        return this.peeked = 7;
      }
      throw syntaxError("Unexpected value");
    
    case 39: 
      checkLenient();
      return this.peeked = 8;
    case 34: 
      return this.peeked = 9;
    case 91: 
      return this.peeked = 3;
    case 123: 
      return this.peeked = 1;
    }
    pos -= 1;
    

    int result = peekKeyword();
    if (result != 0) {
      return result;
    }
    
    result = peekNumber();
    if (result != 0) {
      return result;
    }
    
    if (!isLiteral(buffer[pos])) {
      throw syntaxError("Expected value");
    }
    
    checkLenient();
    return this.peeked = 10;
  }
  
  private int peekKeyword() throws IOException
  {
    char c = buffer[pos];
    
    int peeking;
    
    if ((c == 't') || (c == 'T')) {
      String keyword = "true";
      String keywordUpper = "TRUE";
      peeking = 5; } else { int peeking;
      if ((c == 'f') || (c == 'F')) {
        String keyword = "false";
        String keywordUpper = "FALSE";
        peeking = 6; } else { int peeking;
        if ((c == 'n') || (c == 'N')) {
          String keyword = "null";
          String keywordUpper = "NULL";
          peeking = 7;
        } else {
          return 0; } } }
    int peeking;
    String keywordUpper;
    String keyword;
    int length = keyword.length();
    for (int i = 1; i < length; i++) {
      if ((pos + i >= limit) && (!fillBuffer(i + 1))) {
        return 0;
      }
      c = buffer[(pos + i)];
      if ((c != keyword.charAt(i)) && (c != keywordUpper.charAt(i))) {
        return 0;
      }
    }
    
    if (((pos + length < limit) || (fillBuffer(length + 1))) && 
      (isLiteral(buffer[(pos + length)]))) {
      return 0;
    }
    

    pos += length;
    return this.peeked = peeking;
  }
  
  private int peekNumber() throws IOException
  {
    char[] buffer = this.buffer;
    int p = pos;
    int l = limit;
    
    long value = 0L;
    boolean negative = false;
    boolean fitsInLong = true;
    int last = 0;
    
    int i = 0;
    for (;; 
        
        i++) {
      if (p + i == l) {
        if (i == buffer.length)
        {

          return 0;
        }
        if (!fillBuffer(i + 1)) {
          break;
        }
        p = pos;
        l = limit;
      }
      
      char c = buffer[(p + i)];
      switch (c) {
      case '-': 
        if (last == 0) {
          negative = true;
          last = 1;
        }
        else if (last == 5) {
          last = 6;
        }
        else {
          return 0;
        }
        break;
      case '+':  if (last == 5) {
          last = 6;
        }
        else {
          return 0;
        }
        break;
      case 'E': case 'e': 
        if ((last == 2) || (last == 4)) {
          last = 5;
        }
        else
          return 0;
        break;
      case '.': 
        if (last == 2) {
          last = 3;
        }
        else
          return 0;
        break;
      default: 
        if ((c < '0') || (c > '9')) {
          if (!isLiteral(c)) {
            break label372;
          }
          return 0;
        }
        if ((last == 1) || (last == 0)) {
          value = -(c - '0');
          last = 2;
        } else if (last == 2) {
          if (value == 0L) {
            return 0;
          }
          long newValue = value * 10L - (c - '0');
          fitsInLong &= ((value > -922337203685477580L) || ((value == -922337203685477580L) && (newValue < value)));
          
          value = newValue;
        } else if (last == 3) {
          last = 4;
        } else if ((last == 5) || (last == 6)) {
          last = 7;
        }
        break;
      }
    }
    label372:
    if ((last == 2) && (fitsInLong) && ((value != Long.MIN_VALUE) || (negative))) {
      peekedLong = (negative ? value : -value);
      pos += i;
      return this.peeked = 15; }
    if ((last == 2) || (last == 4) || (last == 7))
    {
      peekedNumberLength = i;
      return this.peeked = 16;
    }
    return 0;
  }
  
  private boolean isLiteral(char c) throws IOException
  {
    switch (c) {
    case '#': 
    case '/': 
    case ';': 
    case '=': 
    case '\\': 
      checkLenient();
    case '\t': 
    case '\n': 
    case '\f': 
    case '\r': 
    case ' ': 
    case ',': 
    case ':': 
    case '[': 
    case ']': 
    case '{': 
    case '}': 
      return false;
    }
    return true;
  }
  






  public String nextName()
    throws IOException
  {
    int p = peeked;
    if (p == 0) {
      p = doPeek();
    }
    String result;
    if (p == 14) {
      result = nextUnquotedValue(); } else { String result;
      if (p == 12) {
        result = nextQuotedValue('\''); } else { String result;
        if (p == 13) {
          result = nextQuotedValue('"');
        } else
          throw new IllegalStateException("Expected a name but was " + peek() + locationString()); } }
    String result;
    peeked = 0;
    pathNames[(stackSize - 1)] = result;
    return result;
  }
  






  public String nextString()
    throws IOException
  {
    int p = peeked;
    if (p == 0) {
      p = doPeek();
    }
    String result;
    if (p == 10) {
      result = nextUnquotedValue(); } else { String result;
      if (p == 8) {
        result = nextQuotedValue('\''); } else { String result;
        if (p == 9) {
          result = nextQuotedValue('"');
        } else if (p == 11) {
          String result = peekedString;
          peekedString = null; } else { String result;
          if (p == 15) {
            result = Long.toString(peekedLong);
          } else if (p == 16) {
            String result = new String(buffer, pos, peekedNumberLength);
            pos += peekedNumberLength;
          } else {
            throw new IllegalStateException("Expected a string but was " + peek() + locationString()); } } } }
    String result;
    peeked = 0;
    pathIndices[(stackSize - 1)] += 1;
    return result;
  }
  





  public boolean nextBoolean()
    throws IOException
  {
    int p = peeked;
    if (p == 0) {
      p = doPeek();
    }
    if (p == 5) {
      peeked = 0;
      pathIndices[(stackSize - 1)] += 1;
      return true; }
    if (p == 6) {
      peeked = 0;
      pathIndices[(stackSize - 1)] += 1;
      return false;
    }
    throw new IllegalStateException("Expected a boolean but was " + peek() + locationString());
  }
  





  public void nextNull()
    throws IOException
  {
    int p = peeked;
    if (p == 0) {
      p = doPeek();
    }
    if (p == 7) {
      peeked = 0;
      pathIndices[(stackSize - 1)] += 1;
    } else {
      throw new IllegalStateException("Expected null but was " + peek() + locationString());
    }
  }
  







  public double nextDouble()
    throws IOException
  {
    int p = peeked;
    if (p == 0) {
      p = doPeek();
    }
    
    if (p == 15) {
      peeked = 0;
      pathIndices[(stackSize - 1)] += 1;
      return peekedLong;
    }
    
    if (p == 16) {
      peekedString = new String(buffer, pos, peekedNumberLength);
      pos += peekedNumberLength;
    } else if ((p == 8) || (p == 9)) {
      peekedString = nextQuotedValue(p == 8 ? '\'' : '"');
    } else if (p == 10) {
      peekedString = nextUnquotedValue();
    } else if (p != 11) {
      throw new IllegalStateException("Expected a double but was " + peek() + locationString());
    }
    
    peeked = 11;
    double result = Double.parseDouble(peekedString);
    if ((!lenient) && ((Double.isNaN(result)) || (Double.isInfinite(result))))
    {
      throw new MalformedJsonException("JSON forbids NaN and infinities: " + result + locationString());
    }
    peekedString = null;
    peeked = 0;
    pathIndices[(stackSize - 1)] += 1;
    return result;
  }
  








  public long nextLong()
    throws IOException
  {
    int p = peeked;
    if (p == 0) {
      p = doPeek();
    }
    
    if (p == 15) {
      peeked = 0;
      pathIndices[(stackSize - 1)] += 1;
      return peekedLong;
    }
    
    if (p == 16) {
      peekedString = new String(buffer, pos, peekedNumberLength);
      pos += peekedNumberLength;
    } else if ((p == 8) || (p == 9) || (p == 10)) {
      if (p == 10) {
        peekedString = nextUnquotedValue();
      } else {
        peekedString = nextQuotedValue(p == 8 ? '\'' : '"');
      }
      try {
        long result = Long.parseLong(peekedString);
        peeked = 0;
        pathIndices[(stackSize - 1)] += 1;
        return result;
      }
      catch (NumberFormatException localNumberFormatException) {}
    }
    else {
      throw new IllegalStateException("Expected a long but was " + peek() + locationString());
    }
    
    peeked = 11;
    double asDouble = Double.parseDouble(peekedString);
    long result = asDouble;
    if (result != asDouble) {
      throw new NumberFormatException("Expected a long but was " + peekedString + locationString());
    }
    peekedString = null;
    peeked = 0;
    pathIndices[(stackSize - 1)] += 1;
    return result;
  }
  









  private String nextQuotedValue(char quote)
    throws IOException
  {
    char[] buffer = this.buffer;
    StringBuilder builder = new StringBuilder();
    for (;;) {
      int p = pos;
      int l = limit;
      
      int start = p;
      while (p < l) {
        int c = buffer[(p++)];
        
        if (c == quote) {
          pos = p;
          builder.append(buffer, start, p - start - 1);
          return builder.toString(); }
        if (c == 92) {
          pos = p;
          builder.append(buffer, start, p - start - 1);
          builder.append(readEscapeCharacter());
          p = pos;
          l = limit;
          start = p;
        } else if (c == 10) {
          lineNumber += 1;
          lineStart = p;
        }
      }
      
      builder.append(buffer, start, p - start);
      pos = p;
      if (!fillBuffer(1)) {
        throw syntaxError("Unterminated string");
      }
    }
  }
  


  private String nextUnquotedValue()
    throws IOException
  {
    StringBuilder builder = null;
    int i = 0;
    
    for (;;)
    {
      if (pos + i < limit) {
        switch (buffer[(pos + i)]) {
        case '#': 
        case '/': 
        case ';': 
        case '=': 
        case '\\': 
          checkLenient();
        case '\t': 
        case '\n': 
        case '\f': 
        case '\r': 
        case ' ': 
        case ',': 
        case ':': 
        case '[': 
        case ']': 
        case '{': 
        case '}': 
          break;
        default: 
          i++;break;
        



















        }
        
      }
      else if (i < buffer.length) {
        if (!fillBuffer(i + 1)) {
          break;
        }
        

      }
      else
      {
        if (builder == null) {
          builder = new StringBuilder();
        }
        builder.append(buffer, pos, i);
        pos += i;
        i = 0;
        if (!fillBuffer(1))
          break;
      }
    }
    String result;
    String result;
    if (builder == null) {
      result = new String(buffer, pos, i);
    } else {
      builder.append(buffer, pos, i);
      result = builder.toString();
    }
    pos += i;
    return result;
  }
  
  private void skipQuotedValue(char quote) throws IOException
  {
    char[] buffer = this.buffer;
    do {
      int p = pos;
      int l = limit;
      
      while (p < l) {
        int c = buffer[(p++)];
        if (c == quote) {
          pos = p;
          return; }
        if (c == 92) {
          pos = p;
          readEscapeCharacter();
          p = pos;
          l = limit;
        } else if (c == 10) {
          lineNumber += 1;
          lineStart = p;
        }
      }
      pos = p;
    } while (fillBuffer(1));
    throw syntaxError("Unterminated string");
  }
  
  private void skipUnquotedValue() throws IOException {
    do {
      for (int i = 0; 
          pos + i < limit; i++) {
        switch (buffer[(pos + i)]) {
        case '#': 
        case '/': 
        case ';': 
        case '=': 
        case '\\': 
          checkLenient();
        case '\t': 
        case '\n': 
        case '\f': 
        case '\r': 
        case ' ': 
        case ',': 
        case ':': 
        case '[': 
        case ']': 
        case '{': 
        case '}': 
          pos += i;
          return;
        }
      }
      pos += i;
    } while (fillBuffer(1));
  }
  








  public int nextInt()
    throws IOException
  {
    int p = peeked;
    if (p == 0) {
      p = doPeek();
    }
    

    if (p == 15) {
      int result = (int)peekedLong;
      if (peekedLong != result) {
        throw new NumberFormatException("Expected an int but was " + peekedLong + locationString());
      }
      peeked = 0;
      pathIndices[(stackSize - 1)] += 1;
      return result;
    }
    
    if (p == 16) {
      peekedString = new String(buffer, pos, peekedNumberLength);
      pos += peekedNumberLength;
    } else if ((p == 8) || (p == 9) || (p == 10)) {
      if (p == 10) {
        peekedString = nextUnquotedValue();
      } else {
        peekedString = nextQuotedValue(p == 8 ? '\'' : '"');
      }
      try {
        int result = Integer.parseInt(peekedString);
        peeked = 0;
        pathIndices[(stackSize - 1)] += 1;
        return result;
      }
      catch (NumberFormatException localNumberFormatException) {}
    }
    else {
      throw new IllegalStateException("Expected an int but was " + peek() + locationString());
    }
    
    peeked = 11;
    double asDouble = Double.parseDouble(peekedString);
    int result = (int)asDouble;
    if (result != asDouble) {
      throw new NumberFormatException("Expected an int but was " + peekedString + locationString());
    }
    peekedString = null;
    peeked = 0;
    pathIndices[(stackSize - 1)] += 1;
    return result;
  }
  

  public void close()
    throws IOException
  {
    peeked = 0;
    stack[0] = 8;
    stackSize = 1;
    in.close();
  }
  



  public void skipValue()
    throws IOException
  {
    int count = 0;
    do {
      int p = peeked;
      if (p == 0) {
        p = doPeek();
      }
      
      if (p == 3) {
        push(1);
        count++;
      } else if (p == 1) {
        push(3);
        count++;
      } else if (p == 4) {
        stackSize -= 1;
        count--;
      } else if (p == 2) {
        stackSize -= 1;
        count--;
      } else if ((p == 14) || (p == 10)) {
        skipUnquotedValue();
      } else if ((p == 8) || (p == 12)) {
        skipQuotedValue('\'');
      } else if ((p == 9) || (p == 13)) {
        skipQuotedValue('"');
      } else if (p == 16) {
        pos += peekedNumberLength;
      }
      peeked = 0;
    } while (count != 0);
    
    pathIndices[(stackSize - 1)] += 1;
    pathNames[(stackSize - 1)] = "null";
  }
  
  private void push(int newTop) {
    if (stackSize == stack.length) {
      int[] newStack = new int[stackSize * 2];
      int[] newPathIndices = new int[stackSize * 2];
      String[] newPathNames = new String[stackSize * 2];
      System.arraycopy(stack, 0, newStack, 0, stackSize);
      System.arraycopy(pathIndices, 0, newPathIndices, 0, stackSize);
      System.arraycopy(pathNames, 0, newPathNames, 0, stackSize);
      stack = newStack;
      pathIndices = newPathIndices;
      pathNames = newPathNames;
    }
    stack[(stackSize++)] = newTop;
  }
  



  private boolean fillBuffer(int minimum)
    throws IOException
  {
    char[] buffer = this.buffer;
    lineStart -= pos;
    if (limit != pos) {
      limit -= pos;
      System.arraycopy(buffer, pos, buffer, 0, limit);
    } else {
      limit = 0;
    }
    
    pos = 0;
    int total;
    while ((total = in.read(buffer, limit, buffer.length - limit)) != -1) {
      limit += total;
      

      if ((lineNumber == 0) && (lineStart == 0) && (limit > 0) && (buffer[0] == 65279)) {
        pos += 1;
        lineStart += 1;
        minimum++;
      }
      
      if (limit >= minimum) {
        return true;
      }
    }
    return false;
  }
  












  private int nextNonWhitespace(boolean throwOnEof)
    throws IOException
  {
    char[] buffer = this.buffer;
    int p = pos;
    int l = limit;
    for (;;) {
      if (p == l) {
        pos = p;
        if (!fillBuffer(1)) {
          break;
        }
        p = pos;
        l = limit;
      }
      
      int c = buffer[(p++)];
      if (c == 10) {
        lineNumber += 1;
        lineStart = p;
      }
      else if ((c != 32) && (c != 13) && (c != 9))
      {


        if (c == 47) {
          pos = p;
          if (p == l) {
            pos -= 1;
            boolean charsLoaded = fillBuffer(2);
            pos += 1;
            if (!charsLoaded) {
              return c;
            }
          }
          
          checkLenient();
          char peek = buffer[pos];
          switch (peek)
          {
          case '*': 
            pos += 1;
            if (!skipTo("*/")) {
              throw syntaxError("Unterminated comment");
            }
            p = pos + 2;
            l = limit;
            break;
          

          case '/': 
            pos += 1;
            skipToEndOfLine();
            p = pos;
            l = limit;
            break;
          
          default: 
            return c;
          }
        } else if (c == 35) {
          pos = p;
          




          checkLenient();
          skipToEndOfLine();
          p = pos;
          l = limit;
        } else {
          pos = p;
          return c;
        }
      } }
    if (throwOnEof) {
      throw new EOFException("End of input" + locationString());
    }
    return -1;
  }
  
  private void checkLenient() throws IOException
  {
    if (!lenient) {
      throw syntaxError("Use JsonReader.setLenient(true) to accept malformed JSON");
    }
  }
  



  private void skipToEndOfLine()
    throws IOException
  {
    while ((pos < limit) || (fillBuffer(1))) {
      char c = buffer[(pos++)];
      if (c == '\n') {
        lineNumber += 1;
        lineStart = pos;
      } else {
        if (c == '\r') {
          break;
        }
      }
    }
  }
  
  private boolean skipTo(String toFind)
    throws IOException
  {
    label104:
    for (; (pos + toFind.length() <= limit) || (fillBuffer(toFind.length())); pos += 1)
      if (buffer[pos] == '\n') {
        lineNumber += 1;
        lineStart = (pos + 1);
      }
      else {
        for (int c = 0; c < toFind.length(); c++) {
          if (buffer[(pos + c)] != toFind.charAt(c)) {
            break label104;
          }
        }
        return true;
      }
    return false;
  }
  
  public String toString() {
    return getClass().getSimpleName() + locationString();
  }
  
  private String locationString() {
    int line = lineNumber + 1;
    int column = pos - lineStart + 1;
    return " at line " + line + " column " + column + " path " + getPath();
  }
  



  public String getPath()
  {
    StringBuilder result = new StringBuilder().append('$');
    int i = 0; for (int size = stackSize; i < size; i++) {
      switch (stack[i]) {
      case 1: 
      case 2: 
        result.append('[').append(pathIndices[i]).append(']');
        break;
      
      case 3: 
      case 4: 
      case 5: 
        result.append('.');
        if (pathNames[i] != null) {
          result.append(pathNames[i]);
        }
        

        break;
      }
      
    }
    

    return result.toString();
  }
  







  private char readEscapeCharacter()
    throws IOException
  {
    if ((pos == limit) && (!fillBuffer(1))) {
      throw syntaxError("Unterminated escape sequence");
    }
    
    char escaped = buffer[(pos++)];
    switch (escaped) {
    case 'u': 
      if ((pos + 4 > limit) && (!fillBuffer(4))) {
        throw syntaxError("Unterminated escape sequence");
      }
      
      char result = '\000';
      int i = pos; for (int end = i + 4; i < end; i++) {
        char c = buffer[i];
        result = (char)(result << '\004');
        if ((c >= '0') && (c <= '9')) {
          result = (char)(result + (c - '0'));
        } else if ((c >= 'a') && (c <= 'f')) {
          result = (char)(result + (c - 'a' + 10));
        } else if ((c >= 'A') && (c <= 'F')) {
          result = (char)(result + (c - 'A' + 10));
        } else {
          throw new NumberFormatException("\\u" + new String(buffer, pos, 4));
        }
      }
      pos += 4;
      return result;
    
    case 't': 
      return '\t';
    
    case 'b': 
      return '\b';
    
    case 'n': 
      return '\n';
    
    case 'r': 
      return '\r';
    
    case 'f': 
      return '\f';
    
    case '\n': 
      lineNumber += 1;
      lineStart = pos;
    

    case '"': 
    case '\'': 
    case '/': 
    case '\\': 
      return escaped;
    }
    
    throw syntaxError("Invalid escape sequence");
  }
  



  private IOException syntaxError(String message)
    throws IOException
  {
    throw new MalformedJsonException(message + locationString());
  }
  


  private void consumeNonExecutePrefix()
    throws IOException
  {
    nextNonWhitespace(true);
    pos -= 1;
    
    if ((pos + NON_EXECUTE_PREFIX.length > limit) && (!fillBuffer(NON_EXECUTE_PREFIX.length))) {
      return;
    }
    
    for (int i = 0; i < NON_EXECUTE_PREFIX.length; i++) {
      if (buffer[(pos + i)] != NON_EXECUTE_PREFIX[i]) {
        return;
      }
    }
    

    pos += NON_EXECUTE_PREFIX.length;
  }
  
  static {
    JsonReaderInternalAccess.INSTANCE = new JsonReaderInternalAccess() {
      public void promoteNameToValue(JsonReader reader) throws IOException {
        if ((reader instanceof JsonTreeReader)) {
          ((JsonTreeReader)reader).promoteNameToValue();
          return;
        }
        int p = peeked;
        if (p == 0) {
          p = reader.doPeek();
        }
        if (p == 13) {
          peeked = 9;
        } else if (p == 12) {
          peeked = 8;
        } else if (p == 14) {
          peeked = 10;
        }
        else {
          throw new IllegalStateException("Expected a name but was " + reader.peek() + reader.locationString());
        }
      }
    };
  }
}
