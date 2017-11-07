package org.openqa.selenium.interactions;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import java.util.List;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.internal.SingleKeyAction;
import org.openqa.selenium.internal.Locatable;




















@Deprecated
public class KeyUpAction
  extends SingleKeyAction
  implements Action
{
  public KeyUpAction(Keyboard keyboard, Mouse mouse, Locatable locationProvider, Keys key)
  {
    super(keyboard, mouse, locationProvider, key);
  }
  
  public KeyUpAction(Keyboard keyboard, Mouse mouse, Keys key) {
    super(keyboard, mouse, key);
  }
  
  public void perform() {
    focusOnElement();
    
    keyboard.releaseKey(key);
  }
  
  public List<Interaction> asInteractions(PointerInput mouse, KeyInput keyboard)
  {
    ImmutableList.Builder<Interaction> toReturn = ImmutableList.builder();
    
    optionallyClickElement(mouse, toReturn);
    toReturn.add(keyboard.createKeyUp(key.getCodePoint()));
    
    return toReturn.build();
  }
}
