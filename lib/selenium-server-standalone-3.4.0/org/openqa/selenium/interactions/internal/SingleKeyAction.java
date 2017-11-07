package org.openqa.selenium.interactions.internal;

import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Keyboard;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.internal.Locatable;




















public abstract class SingleKeyAction
  extends KeysRelatedAction
{
  protected final Keys key;
  private static final Keys[] MODIFIER_KEYS = { Keys.SHIFT, Keys.CONTROL, Keys.ALT, Keys.META, Keys.COMMAND, Keys.LEFT_ALT, Keys.LEFT_CONTROL, Keys.LEFT_SHIFT };
  

  protected SingleKeyAction(Keyboard keyboard, Mouse mouse, Keys key)
  {
    this(keyboard, mouse, null, key);
  }
  
  protected SingleKeyAction(Keyboard keyboard, Mouse mouse, Locatable locationProvider, Keys key) {
    super(keyboard, mouse, locationProvider);
    this.key = key;
    boolean isModifier = false;
    for (Keys modifier : MODIFIER_KEYS) {
      isModifier = (isModifier) || (modifier.equals(key));
    }
    
    if (!isModifier) {
      throw new IllegalArgumentException("Key Down / Up events only make sense for modifier keys.");
    }
  }
}
