package org.openqa.selenium.htmlunit;

import java.util.HashSet;
import java.util.Set;
import org.openqa.selenium.Keys;





















class KeyboardModifiersState
{
  private Set<Character> set = new HashSet();
  
  private boolean shiftPressed;
  
  KeyboardModifiersState() {}
  
  public boolean isShiftPressed() { return shiftPressed; }
  
  private boolean ctrlPressed;
  private boolean altPressed;
  public boolean isCtrlPressed() { return ctrlPressed; }
  

  public boolean isAltPressed() {
    return altPressed;
  }
  
  public void storeKeyDown(char key) {
    storeIfEqualsShift(key, true);
    storeIfEqualsCtrl(key, true);
    storeIfEqualsAlt(key, true);
    set.add(Character.valueOf(key));
  }
  
  public void storeKeyUp(char key) {
    storeIfEqualsShift(key, false);
    storeIfEqualsCtrl(key, false);
    storeIfEqualsAlt(key, false);
    set.remove(Character.valueOf(key));
  }
  
  private void storeIfEqualsShift(char key, boolean keyState) {
    if (key == Keys.SHIFT.charAt(0))
      shiftPressed = keyState;
  }
  
  private void storeIfEqualsCtrl(char key, boolean keyState) {
    if (key == Keys.CONTROL.charAt(0))
      ctrlPressed = keyState;
  }
  
  private void storeIfEqualsAlt(char key, boolean keyState) {
    if (key == Keys.ALT.charAt(0))
      altPressed = keyState;
  }
  
  boolean isPressed(Keys keys) {
    return isPressed(keys.charAt(0));
  }
  
  boolean isPressed(char ch) {
    return set.contains(Character.valueOf(ch));
  }
}
