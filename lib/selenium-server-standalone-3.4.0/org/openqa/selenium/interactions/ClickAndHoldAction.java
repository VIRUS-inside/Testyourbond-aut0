package org.openqa.selenium.interactions;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import java.util.List;
import org.openqa.selenium.interactions.internal.MouseAction;
import org.openqa.selenium.internal.Locatable;





















@Deprecated
public class ClickAndHoldAction
  extends MouseAction
  implements Action
{
  public ClickAndHoldAction(Mouse mouse, Locatable locationProvider)
  {
    super(mouse, locationProvider);
  }
  




  public void perform()
  {
    moveToLocation();
    mouse.mouseDown(getActionLocation());
  }
  
  public List<Interaction> asInteractions(PointerInput mouse, KeyInput keyboard)
  {
    ImmutableList.Builder<Interaction> interactions = ImmutableList.builder();
    
    moveToLocation(mouse, interactions);
    
    return interactions.build();
  }
}
