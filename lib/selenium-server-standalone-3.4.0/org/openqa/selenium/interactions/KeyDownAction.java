package org.openqa.selenium.interactions;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import java.util.List;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.internal.SingleKeyAction;
import org.openqa.selenium.internal.Locatable;





















@Deprecated
public class KeyDownAction
  extends SingleKeyAction
  implements Action
{
  public KeyDownAction(Keyboard keyboard, Mouse mouse, Locatable locationProvider, Keys key)
  {
    super(keyboard, mouse, locationProvider, key);
  }
  
  public KeyDownAction(Keyboard keyboard, Mouse mouse, Keys key) {
    super(keyboard, mouse, key);
  }
  
  public void perform() {
    focusOnElement();
    
    keyboard.pressKey(key);
  }
  
  public List<Interaction> asInteractions(PointerInput mouse, KeyInput keyboard)
  {
    ImmutableList.Builder<Interaction> interactions = ImmutableList.builder();
    
    optionallyClickElement(mouse, interactions);
    interactions.add(keyboard.createKeyDown(key.getCodePoint()));
    
    return interactions.build();
  }
}
