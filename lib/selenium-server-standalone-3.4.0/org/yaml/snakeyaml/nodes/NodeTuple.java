package org.yaml.snakeyaml.nodes;







public final class NodeTuple
{
  private Node keyNode;
  





  private Node valueNode;
  





  public NodeTuple(Node keyNode, Node valueNode)
  {
    if ((keyNode == null) || (valueNode == null)) {
      throw new NullPointerException("Nodes must be provided.");
    }
    this.keyNode = keyNode;
    this.valueNode = valueNode;
  }
  


  public Node getKeyNode()
  {
    return keyNode;
  }
  




  public Node getValueNode()
  {
    return valueNode;
  }
  
  public String toString()
  {
    return "<NodeTuple keyNode=" + keyNode.toString() + "; valueNode=" + valueNode.toString() + ">";
  }
}
