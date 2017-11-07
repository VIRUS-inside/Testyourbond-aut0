package org.apache.commons.codec.language;

import java.util.Locale;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
















































































































































































public class ColognePhonetic
  implements StringEncoder
{
  private static final char[] AEIJOUY = { 'A', 'E', 'I', 'J', 'O', 'U', 'Y' };
  private static final char[] SCZ = { 'S', 'C', 'Z' };
  private static final char[] WFPV = { 'W', 'F', 'P', 'V' };
  private static final char[] GKQ = { 'G', 'K', 'Q' };
  private static final char[] CKQ = { 'C', 'K', 'Q' };
  private static final char[] AHKLOQRUX = { 'A', 'H', 'K', 'L', 'O', 'Q', 'R', 'U', 'X' };
  private static final char[] SZ = { 'S', 'Z' };
  private static final char[] AHOUKQX = { 'A', 'H', 'O', 'U', 'K', 'Q', 'X' };
  private static final char[] TDX = { 'T', 'D', 'X' };
  

  public ColognePhonetic() {}
  


  private abstract class CologneBuffer
  {
    protected final char[] data;
    
    protected int length = 0;
    
    public CologneBuffer(char[] data) {
      this.data = data;
      length = data.length;
    }
    
    public CologneBuffer(int buffSize) {
      data = new char[buffSize];
      length = 0;
    }
    
    protected abstract char[] copyData(int paramInt1, int paramInt2);
    
    public int length() {
      return length;
    }
    
    public String toString()
    {
      return new String(copyData(0, length));
    }
  }
  
  private class CologneOutputBuffer extends ColognePhonetic.CologneBuffer
  {
    public CologneOutputBuffer(int buffSize) {
      super(buffSize);
    }
    
    public void addRight(char chr) {
      data[length] = chr;
      length += 1;
    }
    
    protected char[] copyData(int start, int length)
    {
      char[] newData = new char[length];
      System.arraycopy(data, start, newData, 0, length);
      return newData;
    }
  }
  
  private class CologneInputBuffer extends ColognePhonetic.CologneBuffer
  {
    public CologneInputBuffer(char[] data) {
      super(data);
    }
    
    public void addLeft(char ch) {
      length += 1;
      data[getNextPos()] = ch;
    }
    
    protected char[] copyData(int start, int length)
    {
      char[] newData = new char[length];
      System.arraycopy(data, data.length - this.length + start, newData, 0, length);
      return newData;
    }
    
    public char getNextChar() {
      return data[getNextPos()];
    }
    
    protected int getNextPos() {
      return data.length - length;
    }
    
    public char removeNext() {
      char ch = getNextChar();
      length -= 1;
      return ch;
    }
  }
  









  private static final char[][] PREPROCESS_MAP = { { 'Ä', 'A' }, { 'Ü', 'U' }, { 'Ö', 'O' }, { 'ß', 'S' } };
  







  private static boolean arrayContains(char[] arr, char key)
  {
    for (char element : arr) {
      if (element == key) {
        return true;
      }
    }
    return false;
  }
  










  public String colognePhonetic(String text)
  {
    if (text == null) {
      return null;
    }
    
    text = preprocess(text);
    
    CologneOutputBuffer output = new CologneOutputBuffer(text.length() * 2);
    CologneInputBuffer input = new CologneInputBuffer(text.toCharArray());
    


    char lastChar = '-';
    char lastCode = '/';
    


    int rightLength = input.length();
    
    while (rightLength > 0) {
      char chr = input.removeNext();
      char nextChar;
      char nextChar; if ((rightLength = input.length()) > 0) {
        nextChar = input.getNextChar();
      } else
        nextChar = '-';
      char code;
      char code;
      if (arrayContains(AEIJOUY, chr)) {
        code = '0';
      } else if ((chr == 'H') || (chr < 'A') || (chr > 'Z')) {
        if (lastCode == '/') {
          continue;
        }
        char code = '-'; } else { char code;
        if ((chr == 'B') || ((chr == 'P') && (nextChar != 'H'))) {
          code = '1'; } else { char code;
          if (((chr == 'D') || (chr == 'T')) && (!arrayContains(SCZ, nextChar))) {
            code = '2'; } else { char code;
            if (arrayContains(WFPV, chr)) {
              code = '3'; } else { char code;
              if (arrayContains(GKQ, chr)) {
                code = '4';
              } else if ((chr == 'X') && (!arrayContains(CKQ, lastChar))) {
                char code = '4';
                input.addLeft('S');
                rightLength++; } else { char code;
                if ((chr == 'S') || (chr == 'Z')) {
                  code = '8'; } else { char code;
                  if (chr == 'C') { char code;
                    if (lastCode == '/') { char code;
                      if (arrayContains(AHKLOQRUX, nextChar)) {
                        code = '4';
                      } else
                        code = '8';
                    } else {
                      char code;
                      if ((arrayContains(SZ, lastChar)) || (!arrayContains(AHOUKQX, nextChar))) {
                        code = '8';
                      } else
                        code = '4';
                    }
                  } else { char code;
                    if (arrayContains(TDX, chr)) {
                      code = '8'; } else { char code;
                      if (chr == 'R') {
                        code = '7'; } else { char code;
                        if (chr == 'L') {
                          code = '5'; } else { char code;
                          if ((chr == 'M') || (chr == 'N')) {
                            code = '6';
                          } else
                            code = chr;
                        }
                      } } } } } } } } }
      if ((code != '-') && (((lastCode != code) && ((code != '0') || (lastCode == '/'))) || (code < '0') || (code > '8'))) {
        output.addRight(code);
      }
      
      lastChar = chr;
      lastCode = code;
    }
    return output.toString();
  }
  
  public Object encode(Object object) throws EncoderException
  {
    if (!(object instanceof String)) {
      throw new EncoderException("This method's parameter was expected to be of the type " + String.class.getName() + ". But actually it was of the type " + object.getClass().getName() + ".");
    }
    



    return encode((String)object);
  }
  
  public String encode(String text)
  {
    return colognePhonetic(text);
  }
  
  public boolean isEncodeEqual(String text1, String text2) {
    return colognePhonetic(text1).equals(colognePhonetic(text2));
  }
  


  private String preprocess(String text)
  {
    text = text.toUpperCase(Locale.GERMAN);
    
    char[] chrs = text.toCharArray();
    
    for (int index = 0; index < chrs.length; index++) {
      if (chrs[index] > 'Z') {
        for (char[] element : PREPROCESS_MAP) {
          if (chrs[index] == element[0]) {
            chrs[index] = element[1];
            break;
          }
        }
      }
    }
    return new String(chrs);
  }
}
