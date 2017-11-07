package org.yaml.snakeyaml.representer;

import org.yaml.snakeyaml.nodes.Node;

public abstract interface Represent
{
  public abstract Node representData(Object paramObject);
}
