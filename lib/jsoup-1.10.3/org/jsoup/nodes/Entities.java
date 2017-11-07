package org.jsoup.nodes;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Arrays;
import java.util.HashMap;
import org.jsoup.SerializationException;
import org.jsoup.helper.DataUtil;
import org.jsoup.helper.StringUtil;
import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.Parser;









public class Entities
{
  private static final int empty = -1;
  private static final String emptyName = "";
  static final int codepointRadix = 36;
  private Entities() {}
  
  public static enum EscapeMode
  {
    xhtml("entities-xhtml.properties", 4), 
    


    base("entities-base.properties", 106), 
    


    extended("entities-full.properties", 2125);
    

    private String[] nameKeys;
    
    private int[] codeVals;
    private int[] codeKeys;
    private String[] nameVals;
    
    private EscapeMode(String file, int size)
    {
      Entities.load(this, file, size);
    }
    
    int codepointForName(String name) {
      int index = Arrays.binarySearch(nameKeys, name);
      return index >= 0 ? codeVals[index] : -1;
    }
    
    String nameForCodepoint(int codepoint) {
      int index = Arrays.binarySearch(codeKeys, codepoint);
      if (index >= 0)
      {

        return (index < nameVals.length - 1) && (codeKeys[(index + 1)] == codepoint) ? nameVals[(index + 1)] : nameVals[index];
      }
      
      return "";
    }
    
    private int size() {
      return nameKeys.length;
    }
  }
  
  private static final HashMap<String, String> multipoints = new HashMap();
  








  public static boolean isNamedEntity(String name)
  {
    return EscapeMode.extended.codepointForName(name) != -1;
  }
  






  public static boolean isBaseNamedEntity(String name)
  {
    return EscapeMode.base.codepointForName(name) != -1;
  }
  



  /**
   * @deprecated
   */
  public static Character getCharacterByName(String name)
  {
    return Character.valueOf((char)EscapeMode.extended.codepointForName(name));
  }
  





  public static String getByName(String name)
  {
    String val = (String)multipoints.get(name);
    if (val != null)
      return val;
    int codepoint = EscapeMode.extended.codepointForName(name);
    if (codepoint != -1)
      return new String(new int[] { codepoint }, 0, 1);
    return "";
  }
  
  public static int codepointsForName(String name, int[] codepoints) {
    String val = (String)multipoints.get(name);
    if (val != null) {
      codepoints[0] = val.codePointAt(0);
      codepoints[1] = val.codePointAt(1);
      return 2;
    }
    int codepoint = EscapeMode.extended.codepointForName(name);
    if (codepoint != -1) {
      codepoints[0] = codepoint;
      return 1;
    }
    return 0;
  }
  
  static String escape(String string, Document.OutputSettings out) {
    StringBuilder accum = new StringBuilder(string.length() * 2);
    try {
      escape(accum, string, out, false, false, false);
    } catch (IOException e) {
      throw new SerializationException(e);
    }
    return accum.toString();
  }
  

  static void escape(Appendable accum, String string, Document.OutputSettings out, boolean inAttribute, boolean normaliseWhite, boolean stripLeadingWhite)
    throws IOException
  {
    boolean lastWasWhite = false;
    boolean reachedNonWhite = false;
    EscapeMode escapeMode = out.escapeMode();
    CharsetEncoder encoder = out.encoder();
    CoreCharset coreCharset = CoreCharset.byName(encoder.charset().name());
    int length = string.length();
    
    int codePoint;
    for (int offset = 0; offset < length; offset += Character.charCount(codePoint)) {
      codePoint = string.codePointAt(offset);
      
      if (normaliseWhite) {
        if (StringUtil.isWhitespace(codePoint)) {
          if (((!stripLeadingWhite) || (reachedNonWhite)) && (!lastWasWhite))
          {
            accum.append(' ');
            lastWasWhite = true;
          }
        } else {
          lastWasWhite = false;
          reachedNonWhite = true;
        }
        
      }
      else if (codePoint < 65536) {
        char c = (char)codePoint;
        
        switch (c) {
        case '&': 
          accum.append("&amp;");
          break;
        case ' ': 
          if (escapeMode != EscapeMode.xhtml) {
            accum.append("&nbsp;");
          } else
            accum.append("&#xa0;");
          break;
        
        case '<': 
          if ((!inAttribute) || (escapeMode == EscapeMode.xhtml)) {
            accum.append("&lt;");
          } else
            accum.append(c);
          break;
        case '>': 
          if (!inAttribute) {
            accum.append("&gt;");
          } else
            accum.append(c);
          break;
        case '"': 
          if (inAttribute) {
            accum.append("&quot;");
          } else
            accum.append(c);
          break;
        default: 
          if (canEncode(coreCharset, c, encoder)) {
            accum.append(c);
          } else
            appendEncoded(accum, escapeMode, codePoint);
          break; }
      } else {
        String c = new String(Character.toChars(codePoint));
        if (encoder.canEncode(c)) {
          accum.append(c);
        } else
          appendEncoded(accum, escapeMode, codePoint);
      }
    }
  }
  
  private static void appendEncoded(Appendable accum, EscapeMode escapeMode, int codePoint) throws IOException {
    String name = escapeMode.nameForCodepoint(codePoint);
    if (name != "") {
      accum.append('&').append(name).append(';');
    } else
      accum.append("&#x").append(Integer.toHexString(codePoint)).append(';');
  }
  
  static String unescape(String string) {
    return unescape(string, false);
  }
  






  static String unescape(String string, boolean strict)
  {
    return Parser.unescapeEntities(string, strict);
  }
  













  private static boolean canEncode(CoreCharset charset, char c, CharsetEncoder fallback)
  {
    switch (1.$SwitchMap$org$jsoup$nodes$Entities$CoreCharset[charset.ordinal()]) {
    case 1: 
      return c < '';
    case 2: 
      return true;
    }
    return fallback.canEncode(c);
  }
  
  private static enum CoreCharset
  {
    ascii,  utf,  fallback;
    
    private CoreCharset() {}
    private static CoreCharset byName(String name) { if (name.equals("US-ASCII"))
        return ascii;
      if (name.startsWith("UTF-"))
        return utf;
      return fallback;
    }
  }
  
  private static final char[] codeDelims = { ',', ';' };
  
  private static void load(EscapeMode e, String file, int size) {
    nameKeys = new String[size];
    codeVals = new int[size];
    codeKeys = new int[size];
    nameVals = new String[size];
    
    InputStream stream = Entities.class.getResourceAsStream(file);
    if (stream == null) {
      throw new IllegalStateException("Could not read resource " + file + ". Make sure you copy resources for " + Entities.class.getCanonicalName());
    }
    int i = 0;
    try {
      ByteBuffer bytes = DataUtil.readToByteBuffer(stream, 0);
      String contents = Charset.forName("ascii").decode(bytes).toString();
      CharacterReader reader = new CharacterReader(contents);
      
      while (!reader.isEmpty())
      {

        String name = reader.consumeTo('=');
        reader.advance();
        int cp1 = Integer.parseInt(reader.consumeToAny(codeDelims), 36);
        char codeDelim = reader.current();
        reader.advance();
        int cp2;
        if (codeDelim == ',') {
          int cp2 = Integer.parseInt(reader.consumeTo(';'), 36);
          reader.advance();
        } else {
          cp2 = -1;
        }
        String indexS = reader.consumeTo('\n');
        
        if (indexS.charAt(indexS.length() - 1) == '\r') {
          indexS = indexS.substring(0, indexS.length() - 1);
        }
        int index = Integer.parseInt(indexS, 36);
        reader.advance();
        
        nameKeys[i] = name;
        codeVals[i] = cp1;
        codeKeys[index] = cp1;
        nameVals[index] = name;
        
        if (cp2 != -1) {
          multipoints.put(name, new String(new int[] { cp1, cp2 }, 0, 2));
        }
        i++;
      }
    }
    catch (IOException err)
    {
      throw new IllegalStateException("Error reading resource " + file);
    }
  }
}
