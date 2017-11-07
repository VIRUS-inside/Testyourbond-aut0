package org.apache.xml.serializer;

import javax.xml.transform.Transformer;
import org.w3c.dom.Node;

public abstract interface TransformStateSetter
{
  public abstract void setCurrentNode(Node paramNode);
  
  public abstract void resetState(Transformer paramTransformer);
}
