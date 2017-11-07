package org.openqa.selenium.interactions;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import java.util.List;
import org.openqa.selenium.interactions.internal.MouseAction;
import org.openqa.selenium.interactions.internal.MouseAction.Button;
import org.openqa.selenium.internal.Locatable;

















/**
 * @deprecated
 */
public class ContextClickAction
  extends MouseAction
  implements Action
{
  public ContextClickAction(Mouse mouse, Locatable where)
  {
    super(mouse, where);
  }
  



  public void perform()
  {
    moveToLocation();
    mouse.contextClick(getActionLocation());
  }
  
  public List<Interaction> asInteractions(PointerInput mouse, KeyInput keyboard)
  {
    ImmutableList.Builder<Interaction> interactions = ImmutableList.builder();
    
    moveToLocation(mouse, interactions);
    interactions.add(mouse.createPointerDown(MouseAction.Button.RIGHT.asArg()));
    interactions.add(mouse.createPointerUp(MouseAction.Button.RIGHT.asArg()));
    
    return interactions.build();
  }
}
