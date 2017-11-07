package org.yaml.snakeyaml.parser;

import org.yaml.snakeyaml.events.Event;

abstract interface Production
{
  public abstract Event produce();
}
