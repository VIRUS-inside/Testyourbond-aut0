package org.yaml.snakeyaml.emitter;

import java.io.IOException;
import org.yaml.snakeyaml.events.Event;

public abstract interface Emitable
{
  public abstract void emit(Event paramEvent)
    throws IOException;
}
