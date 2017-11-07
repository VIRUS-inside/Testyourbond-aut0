package org.seleniumhq.jetty9.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;





























public class StringUtil
{
  private static final Logger LOG = Log.getLogger(StringUtil.class);
  

  private static final Trie<String> CHARSETS = new ArrayTrie(256);
  
  public static final String ALL_INTERFACES = "0.0.0.0";
  
  public static final String CRLF = "\r\n";
  
  @Deprecated
  public static final String __LINE_SEPARATOR = System.lineSeparator();
  
  public static final String __ISO_8859_1 = "iso-8859-1";
  public static final String __UTF8 = "utf-8";
  public static final String __UTF16 = "utf-16";
  
  static
  {
    CHARSETS.put("utf-8", "utf-8");
    CHARSETS.put("utf8", "utf-8");
    CHARSETS.put("utf-16", "utf-16");
    CHARSETS.put("utf16", "utf-16");
    CHARSETS.put("iso-8859-1", "iso-8859-1");
    CHARSETS.put("iso_8859_1", "iso-8859-1");
  }
  






  public static String normalizeCharset(String s)
  {
    String n = (String)CHARSETS.get(s);
    return n == null ? s : n;
  }
  








  public static String normalizeCharset(String s, int offset, int length)
  {
    String n = (String)CHARSETS.get(s, offset, length);
    return n == null ? s.substring(offset, offset + length) : n;
  }
  


  public static final char[] lowercases = { '\000', '\001', '\002', '\003', '\004', '\005', '\006', '\007', '\b', '\t', '\n', '\013', '\f', '\r', '\016', '\017', '\020', '\021', '\022', '\023', '\024', '\025', '\026', '\027', '\030', '\031', '\032', '\033', '\034', '\035', '\036', '\037', ' ', '!', '"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ':', ';', '<', '=', '>', '?', '@', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '[', '\\', ']', '^', '_', '`', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '{', '|', '}', '~', '' };
  






















  public static String asciiToLowerCase(String s)
  {
    if (s == null) {
      return null;
    }
    char[] c = null;
    int i = s.length();
    

    while (i-- > 0)
    {
      char c1 = s.charAt(i);
      if (c1 <= '')
      {
        char c2 = lowercases[c1];
        if (c1 != c2)
        {
          c = s.toCharArray();
          c[i] = c2;
          break;
        }
      }
    }
    
    while (i-- > 0)
    {
      if (c[i] <= '') {
        c[i] = lowercases[c[i]];
      }
    }
    return c == null ? s : new String(c);
  }
  


  public static boolean startsWithIgnoreCase(String s, String w)
  {
    if (w == null) {
      return true;
    }
    if ((s == null) || (s.length() < w.length())) {
      return false;
    }
    for (int i = 0; i < w.length(); i++)
    {
      char c1 = s.charAt(i);
      char c2 = w.charAt(i);
      if (c1 != c2)
      {
        if (c1 <= '')
          c1 = lowercases[c1];
        if (c2 <= '')
          c2 = lowercases[c2];
        if (c1 != c2)
          return false;
      }
    }
    return true;
  }
  

  public static boolean endsWithIgnoreCase(String s, String w)
  {
    if (w == null) {
      return true;
    }
    if (s == null) {
      return false;
    }
    int sl = s.length();
    int wl = w.length();
    
    if (sl < wl) {
      return false;
    }
    for (int i = wl; i-- > 0;)
    {
      char c1 = s.charAt(--sl);
      char c2 = w.charAt(i);
      if (c1 != c2)
      {
        if (c1 <= '')
          c1 = lowercases[c1];
        if (c2 <= '')
          c2 = lowercases[c2];
        if (c1 != c2)
          return false;
      }
    }
    return true;
  }
  







  public static int indexFrom(String s, String chars)
  {
    for (int i = 0; i < s.length(); i++)
      if (chars.indexOf(s.charAt(i)) >= 0)
        return i;
    return -1;
  }
  








  public static String replace(String s, String sub, String with)
  {
    int c = 0;
    int i = s.indexOf(sub, c);
    if (i == -1) {
      return s;
    }
    StringBuilder buf = new StringBuilder(s.length() + with.length());
    
    do
    {
      buf.append(s.substring(c, i));
      buf.append(with);
      c = i + sub.length();
    } while ((i = s.indexOf(sub, c)) != -1);
    
    if (c < s.length()) {
      buf.append(s.substring(c, s.length()));
    }
    return buf.toString();
  }
  





  @Deprecated
  public static String unquote(String s)
  {
    return QuotedStringTokenizer.unquote(s);
  }
  











  public static void append(StringBuilder buf, String s, int offset, int length)
  {
    synchronized (buf)
    {
      int end = offset + length;
      for (int i = offset; i < end; i++)
      {
        if (i >= s.length())
          break;
        buf.append(s.charAt(i));
      }
    }
  }
  









  public static void append(StringBuilder buf, byte b, int base)
  {
    int bi = 0xFF & b;
    int c = 48 + bi / base % base;
    if (c > 57)
      c = 97 + (c - 48 - 10);
    buf.append((char)c);
    c = 48 + bi % base;
    if (c > 57)
      c = 97 + (c - 48 - 10);
    buf.append((char)c);
  }
  







  public static void append2digits(StringBuffer buf, int i)
  {
    if (i < 100)
    {
      buf.append((char)(i / 10 + 48));
      buf.append((char)(i % 10 + 48));
    }
  }
  







  public static void append2digits(StringBuilder buf, int i)
  {
    if (i < 100)
    {
      buf.append((char)(i / 10 + 48));
      buf.append((char)(i % 10 + 48));
    }
  }
  





  public static String nonNull(String s)
  {
    if (s == null)
      return "";
    return s;
  }
  

  public static boolean equals(String s, char[] buf, int offset, int length)
  {
    if (s.length() != length)
      return false;
    for (int i = 0; i < length; i++)
      if (buf[(offset + i)] != s.charAt(i))
        return false;
    return true;
  }
  

  public static String toUTF8String(byte[] b, int offset, int length)
  {
    return new String(b, offset, length, StandardCharsets.UTF_8);
  }
  

  public static String toString(byte[] b, int offset, int length, String charset)
  {
    try
    {
      return new String(b, offset, length, charset);
    }
    catch (UnsupportedEncodingException e)
    {
      throw new IllegalArgumentException(e);
    }
  }
  




























  public static int indexOfControlChars(String str)
  {
    if (str == null)
    {
      return -1;
    }
    int len = str.length();
    for (int i = 0; i < len; i++)
    {
      if (Character.isISOControl(str.codePointAt(i)))
      {

        return i;
      }
    }
    
    return -1;
  }
  





















  public static boolean isBlank(String str)
  {
    if (str == null)
    {
      return true;
    }
    int len = str.length();
    for (int i = 0; i < len; i++)
    {
      if (!Character.isWhitespace(str.codePointAt(i)))
      {

        return false;
      }
    }
    
    return true;
  }
  





















  public static boolean isNotBlank(String str)
  {
    if (str == null)
    {
      return false;
    }
    int len = str.length();
    for (int i = 0; i < len; i++)
    {
      if (!Character.isWhitespace(str.codePointAt(i)))
      {

        return true;
      }
    }
    
    return false;
  }
  

  public static boolean isUTF8(String charset)
  {
    return ("utf-8".equalsIgnoreCase(charset)) || ("utf-8".equalsIgnoreCase(normalizeCharset(charset)));
  }
  


  public static String printable(String name)
  {
    if (name == null)
      return null;
    StringBuilder buf = new StringBuilder(name.length());
    for (int i = 0; i < name.length(); i++)
    {
      char c = name.charAt(i);
      if (!Character.isISOControl(c))
        buf.append(c);
    }
    return buf.toString();
  }
  

  public static String printable(byte[] b)
  {
    StringBuilder buf = new StringBuilder();
    for (int i = 0; i < b.length; i++)
    {
      char c = (char)b[i];
      if ((Character.isWhitespace(c)) || ((c > ' ') && (c < ''))) {
        buf.append(c);
      }
      else {
        buf.append("0x");
        TypeUtil.toHex(b[i], buf);
      }
    }
    return buf.toString();
  }
  
  public static byte[] getBytes(String s)
  {
    return s.getBytes(StandardCharsets.ISO_8859_1);
  }
  
  public static byte[] getUtf8Bytes(String s)
  {
    return s.getBytes(StandardCharsets.UTF_8);
  }
  
  public static byte[] getBytes(String s, String charset)
  {
    try
    {
      return s.getBytes(charset);
    }
    catch (Exception e)
    {
      LOG.warn(e); }
    return s.getBytes();
  }
  












  public static String sidBytesToString(byte[] sidBytes)
  {
    StringBuilder sidString = new StringBuilder();
    

    sidString.append("S-");
    

    sidString.append(Byte.toString(sidBytes[0])).append('-');
    
    StringBuilder tmpBuilder = new StringBuilder();
    

    for (int i = 2; i <= 7; i++)
    {
      tmpBuilder.append(Integer.toHexString(sidBytes[i] & 0xFF));
    }
    
    sidString.append(Long.parseLong(tmpBuilder.toString(), 16));
    

    int subAuthorityCount = sidBytes[1];
    

    for (int i = 0; i < subAuthorityCount; i++)
    {
      int offset = i * 4;
      tmpBuilder.setLength(0);
      
      tmpBuilder.append(String.format("%02X%02X%02X%02X", new Object[] {
        Integer.valueOf(sidBytes[(11 + offset)] & 0xFF), 
        Integer.valueOf(sidBytes[(10 + offset)] & 0xFF), 
        Integer.valueOf(sidBytes[(9 + offset)] & 0xFF), 
        Integer.valueOf(sidBytes[(8 + offset)] & 0xFF) }));
      sidString.append('-').append(Long.parseLong(tmpBuilder.toString(), 16));
    }
    
    return sidString.toString();
  }
  









  public static byte[] sidStringToBytes(String sidString)
  {
    String[] sidTokens = sidString.split("-");
    
    int subAuthorityCount = sidTokens.length - 3;
    
    int byteCount = 0;
    byte[] sidBytes = new byte[8 + 4 * subAuthorityCount];
    

    sidBytes[(byteCount++)] = ((byte)Integer.parseInt(sidTokens[1]));
    

    sidBytes[(byteCount++)] = ((byte)subAuthorityCount);
    

    String hexStr = Long.toHexString(Long.parseLong(sidTokens[2]));
    
    while (hexStr.length() < 12)
    {
      hexStr = "0" + hexStr;
    }
    

    for (int i = 0; i < hexStr.length(); i += 2)
    {
      sidBytes[(byteCount++)] = ((byte)Integer.parseInt(hexStr.substring(i, i + 2), 16));
    }
    

    for (int i = 3; i < sidTokens.length; i++)
    {
      hexStr = Long.toHexString(Long.parseLong(sidTokens[i]));
      
      while (hexStr.length() < 8)
      {
        hexStr = "0" + hexStr;
      }
      

      for (int j = hexStr.length(); j > 0; j -= 2)
      {
        sidBytes[(byteCount++)] = ((byte)Integer.parseInt(hexStr.substring(j - 2, j), 16));
      }
    }
    
    return sidBytes;
  }
  







  public static int toInt(String string, int from)
  {
    int val = 0;
    boolean started = false;
    boolean minus = false;
    
    for (int i = from; i < string.length(); i++)
    {
      char b = string.charAt(i);
      if (b <= ' ')
      {
        if (started) {
          break;
        }
      } else if ((b >= '0') && (b <= '9'))
      {
        val = val * 10 + (b - '0');
        started = true;
      } else {
        if ((b != '-') || (started))
          break;
        minus = true;
      }
    }
    


    if (started)
      return minus ? -val : val;
    throw new NumberFormatException(string);
  }
  







  public static long toLong(String string)
  {
    long val = 0L;
    boolean started = false;
    boolean minus = false;
    
    for (int i = 0; i < string.length(); i++)
    {
      char b = string.charAt(i);
      if (b <= ' ')
      {
        if (started) {
          break;
        }
      } else if ((b >= '0') && (b <= '9'))
      {
        val = val * 10L + (b - '0');
        started = true;
      } else {
        if ((b != '-') || (started))
          break;
        minus = true;
      }
    }
    


    if (started)
      return minus ? -val : val;
    throw new NumberFormatException(string);
  }
  







  public static String truncate(String str, int maxSize)
  {
    if (str == null)
    {
      return null;
    }
    
    if (str.length() <= maxSize)
    {
      return str;
    }
    
    return str.substring(0, maxSize);
  }
  





  public static String[] arrayFromString(String s)
  {
    if (s == null) {
      return new String[0];
    }
    if ((!s.startsWith("[")) || (!s.endsWith("]")))
      throw new IllegalArgumentException();
    if (s.length() == 2) {
      return new String[0];
    }
    return csvSplit(s, 1, s.length() - 2);
  }
  





  public static String[] csvSplit(String s)
  {
    if (s == null)
      return null;
    return csvSplit(s, 0, s.length());
  }
  







  public static String[] csvSplit(String s, int off, int len)
  {
    if (s == null)
      return null;
    if ((off < 0) || (len < 0) || (off > s.length())) {
      throw new IllegalArgumentException();
    }
    List<String> list = new ArrayList();
    csvSplit(list, s, off, len);
    return (String[])list.toArray(new String[list.size()]);
  }
  
  static enum CsvSplitState { PRE_DATA,  QUOTE,  SLOSH,  DATA,  WHITE,  POST_DATA;
    






    private CsvSplitState() {}
  }
  





  public static List<String> csvSplit(List<String> list, String s, int off, int len)
  {
    if (list == null)
      list = new ArrayList();
    CsvSplitState state = CsvSplitState.PRE_DATA;
    StringBuilder out = new StringBuilder();
    int last = -1;
    while (len > 0)
    {
      char ch = s.charAt(off++);
      len--;
      
      switch (1.$SwitchMap$org$eclipse$jetty$util$StringUtil$CsvSplitState[state.ordinal()])
      {
      case 1: 
        if (!Character.isWhitespace(ch))
        {

          if ('"' == ch)
          {
            state = CsvSplitState.QUOTE;


          }
          else if (',' == ch)
          {
            list.add("");
          }
          else
          {
            state = CsvSplitState.DATA;
            out.append(ch); } }
        break;
      
      case 2: 
        if (Character.isWhitespace(ch))
        {
          last = out.length();
          out.append(ch);
          state = CsvSplitState.WHITE;


        }
        else if (',' == ch)
        {
          list.add(out.toString());
          out.setLength(0);
          state = CsvSplitState.PRE_DATA;
        }
        else
        {
          out.append(ch); }
        break;
      
      case 3: 
        if (Character.isWhitespace(ch))
        {
          out.append(ch);


        }
        else if (',' == ch)
        {
          out.setLength(last);
          list.add(out.toString());
          out.setLength(0);
          state = CsvSplitState.PRE_DATA;
        }
        else
        {
          state = CsvSplitState.DATA;
          out.append(ch);
          last = -1; }
        break;
      
      case 4: 
        if ('\\' == ch)
        {
          state = CsvSplitState.SLOSH;

        }
        else if ('"' == ch)
        {
          list.add(out.toString());
          out.setLength(0);
          state = CsvSplitState.POST_DATA;
        }
        else {
          out.append(ch); }
        break;
      
      case 5: 
        out.append(ch);
        state = CsvSplitState.QUOTE;
        break;
      
      case 6: 
        if (',' == ch)
        {
          state = CsvSplitState.PRE_DATA;
        }
        
        break;
      }
      
    }
    switch (1.$SwitchMap$org$eclipse$jetty$util$StringUtil$CsvSplitState[state.ordinal()])
    {
    case 1: 
    case 6: 
      break;
    
    case 2: 
    case 4: 
    case 5: 
      list.add(out.toString());
      break;
    
    case 3: 
      out.setLength(last);
      list.add(out.toString());
    }
    
    
    return list;
  }
  
  public static String sanitizeXmlString(String html)
  {
    if (html == null) {
      return null;
    }
    for (int i = 0; 
        

        i < html.length(); i++)
    {
      char c = html.charAt(i);
      
      switch (c)
      {
      case '"': 
      case '&': 
      case '\'': 
      case '<': 
      case '>': 
        break;
      
      default: 
        if ((Character.isISOControl(c)) && (!Character.isWhitespace(c))) {
          break;
        }
      }
      
    }
    if (i == html.length()) {
      return html;
    }
    
    StringBuilder out = new StringBuilder(html.length() * 4 / 3);
    out.append(html, 0, i);
    for (; 
        
        i < html.length(); i++)
    {
      char c = html.charAt(i);
      
      switch (c)
      {
      case '&': 
        out.append("&amp;");
        break;
      case '<': 
        out.append("&lt;");
        break;
      case '>': 
        out.append("&gt;");
        break;
      case '\'': 
        out.append("&apos;");
        break;
      case '"': 
        out.append("&quot;");
        break;
      
      default: 
        if ((Character.isISOControl(c)) && (!Character.isWhitespace(c))) {
          out.append('?');
        } else
          out.append(c);
        break; }
    }
    return out.toString();
  }
  







  public static String valueOf(Object object)
  {
    return object == null ? null : String.valueOf(object);
  }
  
  public StringUtil() {}
}
