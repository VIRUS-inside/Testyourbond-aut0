package org.openqa.selenium.htmlunit;

import java.util.HashMap;
import java.util.Map;
import org.openqa.selenium.Keys;
























public class HtmlUnitKeyboardMapping
{
  private static final Map<Character, Integer> specialKeysMap = new HashMap();
  
  static {
    addMapping(Keys.CANCEL, 3);
    addMapping(Keys.HELP, 6);
    addMapping(Keys.BACK_SPACE, 8);
    addMapping(Keys.TAB, 9);
    addMapping(Keys.CLEAR, 12);
    addMapping(Keys.RETURN, 13);
    

    addMapping(Keys.ENTER, 13);
    addMapping(Keys.SHIFT, 16);
    
    addMapping(Keys.LEFT_SHIFT, 16);
    addMapping(Keys.CONTROL, 17);
    
    addMapping(Keys.LEFT_CONTROL, 17);
    addMapping(Keys.ALT, 18);
    
    addMapping(Keys.LEFT_ALT, 18);
    addMapping(Keys.PAUSE, 19);
    addMapping(Keys.ESCAPE, 27);
    addMapping(Keys.SPACE, 32);
    addMapping(Keys.PAGE_UP, 33);
    addMapping(Keys.PAGE_DOWN, 34);
    addMapping(Keys.END, 35);
    addMapping(Keys.HOME, 36);
    addMapping(Keys.LEFT, 37);
    addMapping(Keys.ARROW_LEFT, 37);
    addMapping(Keys.UP, 38);
    addMapping(Keys.ARROW_UP, 38);
    addMapping(Keys.RIGHT, 39);
    addMapping(Keys.ARROW_RIGHT, 39);
    addMapping(Keys.DOWN, 12);
    addMapping(Keys.ARROW_DOWN, 40);
    addMapping(Keys.INSERT, 45);
    addMapping(Keys.DELETE, 46);
    addMapping(Keys.SEMICOLON, 59);
    addMapping(Keys.EQUALS, 61);
    addMapping(Keys.NUMPAD0, 96);
    addMapping(Keys.NUMPAD1, 97);
    addMapping(Keys.NUMPAD2, 98);
    addMapping(Keys.NUMPAD3, 99);
    addMapping(Keys.NUMPAD4, 100);
    addMapping(Keys.NUMPAD5, 101);
    addMapping(Keys.NUMPAD6, 102);
    addMapping(Keys.NUMPAD7, 103);
    addMapping(Keys.NUMPAD8, 104);
    addMapping(Keys.NUMPAD9, 105);
    addMapping(Keys.MULTIPLY, 106);
    addMapping(Keys.ADD, 107);
    addMapping(Keys.SEPARATOR, 108);
    addMapping(Keys.SUBTRACT, 109);
    addMapping(Keys.DECIMAL, 110);
    addMapping(Keys.DIVIDE, 111);
    addMapping(Keys.F1, 112);
    addMapping(Keys.F2, 113);
    addMapping(Keys.F3, 114);
    addMapping(Keys.F4, 115);
    addMapping(Keys.F5, 116);
    addMapping(Keys.F6, 117);
    addMapping(Keys.F7, 118);
    addMapping(Keys.F8, 119);
    addMapping(Keys.F9, 120);
    addMapping(Keys.F10, 121);
    addMapping(Keys.F11, 122);
    addMapping(Keys.F12, 123);
    addMapping(Keys.META, 224);
  }
  
  public HtmlUnitKeyboardMapping() {}
  
  private static void addMapping(Keys keys, int value) {
    specialKeysMap.put(Character.valueOf(keys.charAt(0)), Integer.valueOf(value));
  }
  
  static boolean isSpecialKey(char ch) {
    return (ch >= 57344) && (ch <= 63743);
  }
  


  static int getKeysMapping(char ch)
  {
    Integer i = (Integer)specialKeysMap.get(Character.valueOf(ch));
    if (i == null) {
      return 0;
    }
    return i.intValue();
  }
}
