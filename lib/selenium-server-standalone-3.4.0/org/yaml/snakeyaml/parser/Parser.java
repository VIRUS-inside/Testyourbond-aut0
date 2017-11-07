package org.yaml.snakeyaml.parser;

import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.events.Event.ID;

public abstract interface Parser
{
  public abstract boolean checkEvent(Event.ID paramID);
  
  public abstract Event peekEvent();
  
  public abstract Event getEvent();
}
