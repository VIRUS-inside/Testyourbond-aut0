package org.yaml.snakeyaml.nodes;

import java.util.List;
import org.yaml.snakeyaml.error.Mark;





















public class SequenceNode
  extends CollectionNode
{
  private final List<Node> value;
  
  public SequenceNode(Tag tag, boolean resolved, List<Node> value, Mark startMark, Mark endMark, Boolean flowStyle)
  {
    super(tag, startMark, endMark, flowStyle);
    if (value == null) {
      throw new NullPointerException("value in a Node is required.");
    }
    this.value = value;
    this.resolved = resolved;
  }
  
  public SequenceNode(Tag tag, List<Node> value, Boolean flowStyle) {
    this(tag, true, value, null, null, flowStyle);
  }
  
  public NodeId getNodeId()
  {
    return NodeId.sequence;
  }
  




  public List<Node> getValue()
  {
    return value;
  }
  
  public void setListType(Class<? extends Object> listType) {
    for (Node node : value) {
      node.setType(listType);
    }
  }
  
  public String toString() {
    return "<" + getClass().getName() + " (tag=" + getTag() + ", value=" + getValue() + ")>";
  }
}
