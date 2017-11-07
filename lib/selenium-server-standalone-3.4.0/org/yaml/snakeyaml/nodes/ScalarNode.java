package org.yaml.snakeyaml.nodes;

import org.yaml.snakeyaml.error.Mark;



















public class ScalarNode
  extends Node
{
  private Character style;
  private String value;
  
  public ScalarNode(Tag tag, String value, Mark startMark, Mark endMark, Character style)
  {
    this(tag, true, value, startMark, endMark, style);
  }
  
  public ScalarNode(Tag tag, boolean resolved, String value, Mark startMark, Mark endMark, Character style)
  {
    super(tag, startMark, endMark);
    if (value == null) {
      throw new NullPointerException("value in a Node is required.");
    }
    this.value = value;
    this.style = style;
    this.resolved = resolved;
  }
  






  public Character getStyle()
  {
    return style;
  }
  
  public NodeId getNodeId()
  {
    return NodeId.scalar;
  }
  




  public String getValue()
  {
    return value;
  }
  
  public String toString() {
    return "<" + getClass().getName() + " (tag=" + getTag() + ", value=" + getValue() + ")>";
  }
}
