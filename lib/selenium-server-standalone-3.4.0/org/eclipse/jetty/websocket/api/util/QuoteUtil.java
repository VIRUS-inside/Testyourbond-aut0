package org.eclipse.jetty.websocket.api.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;























public class QuoteUtil
{
  public static final String ABNF_REQUIRED_QUOTING = "\"'\\\n\r\t\f\b%+ ;=";
  private static final char UNICODE_TAG = '￿';
  
  private static class DeQuotingStringIterator
    implements Iterator<String>
  {
    private final String input;
    private final String delims;
    private StringBuilder token;
    
    private static enum State
    {
      START, 
      TOKEN, 
      QUOTE_SINGLE, 
      QUOTE_DOUBLE;
      

      private State() {}
    }
    
    private boolean hasToken = false;
    private int i = 0;
    
    public DeQuotingStringIterator(String input, String delims)
    {
      this.input = input;
      this.delims = delims;
      int len = input.length();
      token = new StringBuilder(len > 1024 ? 512 : len / 2);
    }
    
    private void appendToken(char c)
    {
      if (hasToken)
      {
        token.append(c);
      }
      else
      {
        if (Character.isWhitespace(c))
        {
          return;
        }
        

        token.append(c);
        hasToken = true;
      }
    }
    



    public boolean hasNext()
    {
      if (hasToken)
      {
        return true;
      }
      
      State state = State.START;
      boolean escape = false;
      int inputLen = input.length();
      
      while (i < inputLen)
      {
        char c = input.charAt(i++);
        
        switch (QuoteUtil.1.$SwitchMap$org$eclipse$jetty$websocket$api$util$QuoteUtil$DeQuotingStringIterator$State[state.ordinal()])
        {

        case 1: 
          if (c == '\'')
          {
            state = State.QUOTE_SINGLE;
            appendToken(c);
          }
          else if (c == '"')
          {
            state = State.QUOTE_DOUBLE;
            appendToken(c);
          }
          else
          {
            appendToken(c);
            state = State.TOKEN;
          }
          break;
        

        case 2: 
          if (delims.indexOf(c) >= 0)
          {

            return hasToken;
          }
          if (c == '\'')
          {
            state = State.QUOTE_SINGLE;
          }
          else if (c == '"')
          {
            state = State.QUOTE_DOUBLE;
          }
          appendToken(c);
          break;
        

        case 3: 
          if (escape)
          {
            escape = false;
            appendToken(c);
          }
          else if (c == '\'')
          {
            appendToken(c);
            state = State.TOKEN;
          }
          else if (c == '\\')
          {
            escape = true;
          }
          else
          {
            appendToken(c);
          }
          break;
        

        case 4: 
          if (escape)
          {
            escape = false;
            appendToken(c);
          }
          else if (c == '"')
          {
            appendToken(c);
            state = State.TOKEN;
          }
          else if (c == '\\')
          {
            escape = true;
          }
          else
          {
            appendToken(c);
          }
          
          break;
        }
        
      }
      
      return hasToken;
    }
    

    public String next()
    {
      if (!hasNext())
      {
        throw new NoSuchElementException();
      }
      String ret = token.toString();
      token.setLength(0);
      hasToken = false;
      return QuoteUtil.dequote(ret.trim());
    }
    

    public void remove()
    {
      throw new UnsupportedOperationException("Remove not supported with this iterator");
    }
  }
  






  private static final char[] escapes = new char[32];
  
  static
  {
    Arrays.fill(escapes, 65535);
    
    escapes[8] = 'b';
    escapes[9] = 't';
    escapes[10] = 'n';
    escapes[12] = 'f';
    escapes[13] = 'r';
  }
  
  private static int dehex(byte b)
  {
    if ((b >= 48) && (b <= 57))
    {
      return (byte)(b - 48);
    }
    if ((b >= 97) && (b <= 102))
    {
      return (byte)(b - 97 + 10);
    }
    if ((b >= 65) && (b <= 70))
    {
      return (byte)(b - 65 + 10);
    }
    throw new IllegalArgumentException("!hex:" + Integer.toHexString(0xFF & b));
  }
  







  public static String dequote(String str)
  {
    char start = str.charAt(0);
    if ((start == '\'') || (start == '"'))
    {

      char end = str.charAt(str.length() - 1);
      if (start == end)
      {

        return str.substring(1, str.length() - 1);
      }
    }
    return str;
  }
  
  public static void escape(StringBuilder buf, String str)
  {
    for (char c : str.toCharArray())
    {
      if (c >= ' ')
      {

        if ((c == '"') || (c == '\\'))
        {
          buf.append('\\');
        }
        buf.append(c);

      }
      else
      {
        char escaped = escapes[c];
        

        if (escaped == 65535)
        {
          buf.append("\\u00");
          if (c < '\020')
          {
            buf.append('0');
          }
          buf.append(Integer.toString(c, 16));

        }
        else
        {
          buf.append('\\').append(escaped);
        }
      }
    }
  }
  








  public static void quote(StringBuilder buf, String str)
  {
    buf.append('"');
    escape(buf, str);
    buf.append('"');
  }
  












  public static void quoteIfNeeded(StringBuilder buf, String str, String delim)
  {
    if (str == null)
    {
      return;
    }
    
    int len = str.length();
    if (len == 0)
    {
      return;
    }
    
    for (int i = 0; i < len; i++)
    {
      int ch = str.codePointAt(i);
      if (delim.indexOf(ch) >= 0)
      {

        quote(buf, str);
        return;
      }
    }
    

    buf.append(str);
  }
  










  public static Iterator<String> splitAt(String str, String delims)
  {
    return new DeQuotingStringIterator(str.trim(), delims);
  }
  
  public static String unescape(String str)
  {
    if (str == null)
    {

      return null;
    }
    
    int len = str.length();
    if (len <= 1)
    {

      return str;
    }
    
    StringBuilder ret = new StringBuilder(len - 2);
    boolean escaped = false;
    
    for (int i = 0; i < len; i++)
    {
      char c = str.charAt(i);
      if (escaped)
      {
        escaped = false;
        switch (c)
        {
        case 'n': 
          ret.append('\n');
          break;
        case 'r': 
          ret.append('\r');
          break;
        case 't': 
          ret.append('\t');
          break;
        case 'f': 
          ret.append('\f');
          break;
        case 'b': 
          ret.append('\b');
          break;
        case '\\': 
          ret.append('\\');
          break;
        case '/': 
          ret.append('/');
          break;
        case '"': 
          ret.append('"');
          break;
        case 'u': 
          ret.append((char)((dehex((byte)str.charAt(i++)) << 24) + (dehex((byte)str.charAt(i++)) << 16) + (dehex((byte)str.charAt(i++)) << 8) + dehex(
            (byte)str.charAt(i++))));
          break;
        default: 
          ret.append(c);break;
        }
      }
      else if (c == '\\')
      {
        escaped = true;
      }
      else
      {
        ret.append(c);
      }
    }
    return ret.toString();
  }
  
  public static String join(Object[] objs, String delim)
  {
    if (objs == null)
    {
      return "";
    }
    StringBuilder ret = new StringBuilder();
    int len = objs.length;
    for (int i = 0; i < len; i++)
    {
      if (i > 0)
      {
        ret.append(delim);
      }
      if ((objs[i] instanceof String))
      {
        ret.append('"').append(objs[i]).append('"');
      }
      else
      {
        ret.append(objs[i]);
      }
    }
    return ret.toString();
  }
  
  public static String join(Collection<?> objs, String delim)
  {
    if (objs == null)
    {
      return "";
    }
    StringBuilder ret = new StringBuilder();
    boolean needDelim = false;
    for (Object obj : objs)
    {
      if (needDelim)
      {
        ret.append(delim);
      }
      if ((obj instanceof String))
      {
        ret.append('"').append(obj).append('"');
      }
      else
      {
        ret.append(obj);
      }
      needDelim = true;
    }
    return ret.toString();
  }
  
  public QuoteUtil() {}
}
