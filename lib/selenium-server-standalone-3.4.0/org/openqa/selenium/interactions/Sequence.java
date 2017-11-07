package org.openqa.selenium.interactions;

import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
























public class Sequence
  implements Encodable
{
  private final List<Encodable> actions = new LinkedList();
  private final InputSource device;
  
  public Sequence(InputSource device, int initialLength) {
    if (!(device instanceof Encodable)) {
      throw new IllegalArgumentException("Input device must implement Encodable: " + device);
    }
    
    this.device = device;
    
    for (int i = 0; i < initialLength; i++) {
      addAction(new Pause(device, Duration.ZERO));
    }
  }
  
  public Sequence addAction(Interaction action) {
    if (!action.isValidFor(device.getInputType())) {
      throw new IllegalArgumentException(String.format("Interaction (%s) is for wrong kind of input device: %s ", new Object[] {action
      
        .getClass(), device }));
    }
    
    if (!(action instanceof Encodable)) {
      throw new IllegalArgumentException("Interaction must implement Encodable: " + action);
    }
    

    actions.add((Encodable)action);
    
    return this;
  }
  
  public Map<String, Object> encode()
  {
    Map<String, Object> toReturn = new HashMap();
    toReturn.putAll(((Encodable)device).encode());
    
    List<Map<String, Object>> encodedActions = new LinkedList();
    for (Encodable action : actions) {
      Map<String, Object> encodedAction = new HashMap();
      encodedAction.putAll(action.encode());
      encodedActions.add(encodedAction);
    }
    toReturn.put("actions", encodedActions);
    
    return toReturn;
  }
  
  public Map<String, Object> toJson() {
    return encode();
  }
  
  int size() {
    return actions.size();
  }
}
