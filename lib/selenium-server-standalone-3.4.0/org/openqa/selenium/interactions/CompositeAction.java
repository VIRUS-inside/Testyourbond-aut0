package org.openqa.selenium.interactions;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import java.util.ArrayList;
import java.util.List;


















public class CompositeAction
  implements Action, IsInteraction
{
  public CompositeAction() {}
  
  private final List<Action> actionsList = new ArrayList();
  
  public void perform() {
    for (Action action : actionsList) {
      action.perform();
    }
  }
  
  public CompositeAction addAction(Action action) {
    Preconditions.checkNotNull(action, "Null actions are not supported.");
    actionsList.add(action);
    return this;
  }
  


  @Deprecated
  @VisibleForTesting
  int getNumberOfActions()
  {
    return actionsList.size();
  }
  
  public List<Interaction> asInteractions(PointerInput mouse, KeyInput keyboard)
  {
    ImmutableList.Builder<Interaction> interactions = ImmutableList.builder();
    
    for (Action action : actionsList) {
      if (!(action instanceof IsInteraction))
      {
        throw new IllegalArgumentException(String.format("Action must implement IsInteraction: %s", new Object[] { action }));
      }
      
      interactions.addAll(((IsInteraction)action).asInteractions(mouse, keyboard));
    }
    
    return interactions.build();
  }
}
