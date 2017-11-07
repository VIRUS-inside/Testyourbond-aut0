package org.yaml.snakeyaml.nodes;

import java.util.List;
import org.yaml.snakeyaml.error.Mark;





















public class MappingNode
  extends CollectionNode
{
  private List<NodeTuple> value;
  private boolean merged = false;
  
  public MappingNode(Tag tag, boolean resolved, List<NodeTuple> value, Mark startMark, Mark endMark, Boolean flowStyle)
  {
    super(tag, startMark, endMark, flowStyle);
    if (value == null) {
      throw new NullPointerException("value in a Node is required.");
    }
    this.value = value;
    this.resolved = resolved;
  }
  
  public MappingNode(Tag tag, List<NodeTuple> value, Boolean flowStyle) {
    this(tag, true, value, null, null, flowStyle);
  }
  
  public NodeId getNodeId()
  {
    return NodeId.mapping;
  }
  




  public List<NodeTuple> getValue()
  {
    return value;
  }
  
  public void setValue(List<NodeTuple> merge) {
    value = merge;
  }
  
  public void setOnlyKeyType(Class<? extends Object> keyType) {
    for (NodeTuple nodes : value) {
      nodes.getKeyNode().setType(keyType);
    }
  }
  
  public void setTypes(Class<? extends Object> keyType, Class<? extends Object> valueType) {
    for (NodeTuple nodes : value) {
      nodes.getValueNode().setType(valueType);
      nodes.getKeyNode().setType(keyType);
    }
  }
  

  public String toString()
  {
    StringBuilder buf = new StringBuilder();
    for (NodeTuple node : getValue()) {
      buf.append("{ key=");
      buf.append(node.getKeyNode());
      buf.append("; value=");
      if ((node.getValueNode() instanceof CollectionNode))
      {
        buf.append(System.identityHashCode(node.getValueNode()));
      } else {
        buf.append(node.toString());
      }
      buf.append(" }");
    }
    String values = buf.toString();
    return "<" + getClass().getName() + " (tag=" + getTag() + ", values=" + values + ")>";
  }
  



  public void setMerged(boolean merged)
  {
    this.merged = merged;
  }
  


  public boolean isMerged()
  {
    return merged;
  }
}
