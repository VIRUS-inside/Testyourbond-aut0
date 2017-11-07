package org.openqa.selenium.interactions;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import java.util.List;
import org.openqa.selenium.interactions.internal.MouseAction;
import org.openqa.selenium.internal.Locatable;





















@Deprecated
public class MoveMouseAction
  extends MouseAction
  implements Action
{
  public MoveMouseAction(Mouse mouse, Locatable locationProvider)
  {
    super(mouse, locationProvider);
    if (locationProvider == null) {
      throw new IllegalArgumentException("Must provide a location for a move action.");
    }
  }
  
  public void perform() {
    mouse.mouseMove(getActionLocation());
  }
  
  public List<Interaction> asInteractions(PointerInput mouse, KeyInput keyboard)
  {
    ImmutableList.Builder<Interaction> interactions = ImmutableList.builder();
    
    moveToLocation(mouse, interactions);
    
    return interactions.build();
  }
}
