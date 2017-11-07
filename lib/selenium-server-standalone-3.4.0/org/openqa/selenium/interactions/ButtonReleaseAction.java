package org.openqa.selenium.interactions;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import java.util.List;
import org.openqa.selenium.interactions.internal.MouseAction;
import org.openqa.selenium.interactions.internal.MouseAction.Button;
import org.openqa.selenium.internal.Locatable;




















@Deprecated
public class ButtonReleaseAction
  extends MouseAction
  implements Action
{
  public ButtonReleaseAction(Mouse mouse, Locatable locationProvider)
  {
    super(mouse, locationProvider);
  }
  





  public void perform()
  {
    moveToLocation();
    mouse.mouseUp(getActionLocation());
  }
  
  public List<Interaction> asInteractions(PointerInput mouse, KeyInput keyboard)
  {
    ImmutableList.Builder<Interaction> interactions = ImmutableList.builder();
    
    moveToLocation(mouse, interactions);
    interactions.add(mouse.createPointerUp(MouseAction.Button.LEFT.asArg()));
    
    return interactions.build();
  }
}
