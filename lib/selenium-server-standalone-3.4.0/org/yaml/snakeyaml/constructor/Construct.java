package org.yaml.snakeyaml.constructor;

import org.yaml.snakeyaml.nodes.Node;

public abstract interface Construct
{
  public abstract Object construct(Node paramNode);
  
  public abstract void construct2ndStep(Node paramNode, Object paramObject);
}
