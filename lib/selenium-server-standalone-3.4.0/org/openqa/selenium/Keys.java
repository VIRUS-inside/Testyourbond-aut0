package org.openqa.selenium;

import java.util.Arrays;
import java.util.OptionalInt;
import java.util.stream.IntStream;





















public enum Keys
  implements CharSequence
{
  NULL(57344), 
  CANCEL(57345), 
  HELP(57346), 
  BACK_SPACE(57347), 
  TAB(57348), 
  CLEAR(57349), 
  RETURN(57350), 
  ENTER(57351), 
  SHIFT(57352), 
  LEFT_SHIFT(SHIFT), 
  CONTROL(57353), 
  LEFT_CONTROL(CONTROL), 
  ALT(57354), 
  LEFT_ALT(ALT), 
  PAUSE(57355), 
  ESCAPE(57356), 
  SPACE(57357), 
  PAGE_UP(57358), 
  PAGE_DOWN(57359), 
  END(57360), 
  HOME(57361), 
  LEFT(57362), 
  ARROW_LEFT(LEFT), 
  UP(57363), 
  ARROW_UP(UP), 
  RIGHT(57364), 
  ARROW_RIGHT(RIGHT), 
  DOWN(57365), 
  ARROW_DOWN(DOWN), 
  INSERT(57366), 
  DELETE(57367), 
  SEMICOLON(57368), 
  EQUALS(57369), 
  

  NUMPAD0(57370), 
  NUMPAD1(57371), 
  NUMPAD2(57372), 
  NUMPAD3(57373), 
  NUMPAD4(57374), 
  NUMPAD5(57375), 
  NUMPAD6(57376), 
  NUMPAD7(57377), 
  NUMPAD8(57378), 
  NUMPAD9(57379), 
  MULTIPLY(57380), 
  ADD(57381), 
  SEPARATOR(57382), 
  SUBTRACT(57383), 
  DECIMAL(57384), 
  DIVIDE(57385), 
  

  F1(57393), 
  F2(57394), 
  F3(57395), 
  F4(57396), 
  F5(57397), 
  F6(57398), 
  F7(57399), 
  F8(57400), 
  F9(57401), 
  F10(57402), 
  F11(57403), 
  F12(57404), 
  
  META(57405), 
  COMMAND(META), 
  
  ZENKAKU_HANKAKU(57408);
  
  private final char keyCode;
  private final int codePoint;
  
  private Keys(Keys key) {
    this(key.charAt(0));
  }
  
  private Keys(char keyCode) {
    this.keyCode = keyCode;
    codePoint = String.valueOf(keyCode).codePoints().findFirst().getAsInt();
  }
  
  public int getCodePoint() {
    return codePoint;
  }
  
  public char charAt(int index) {
    if (index == 0) {
      return keyCode;
    }
    
    return '\000';
  }
  
  public int length() {
    return 1;
  }
  
  public CharSequence subSequence(int start, int end) {
    if ((start == 0) && (end == 1)) {
      return String.valueOf(keyCode);
    }
    
    throw new IndexOutOfBoundsException();
  }
  
  public String toString()
  {
    return String.valueOf(keyCode);
  }
  












  public static String chord(CharSequence... value)
  {
    return chord(Arrays.asList(value));
  }
  




  public static String chord(Iterable<CharSequence> value)
  {
    StringBuilder builder = new StringBuilder();
    
    for (CharSequence seq : value) {
      builder.append(seq);
    }
    
    builder.append(NULL);
    return builder.toString();
  }
  






  public static Keys getKeyFromUnicode(char key)
  {
    for (Keys unicodeKey : ) {
      if (unicodeKey.charAt(0) == key) {
        return unicodeKey;
      }
    }
    
    return null;
  }
}
