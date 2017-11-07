package org.yaml.snakeyaml.emitter;

import java.io.IOException;

abstract interface EmitterState
{
  public abstract void expect()
    throws IOException;
}
