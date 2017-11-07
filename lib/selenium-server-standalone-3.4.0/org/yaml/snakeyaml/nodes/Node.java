package org.yaml.snakeyaml.nodes;

import org.yaml.snakeyaml.error.Mark;






























public abstract class Node
{
  private Tag tag;
  private Mark startMark;
  protected Mark endMark;
  private Class<? extends Object> type;
  private boolean twoStepsConstruction;
  protected boolean resolved;
  protected Boolean useClassConstructor;
  
  public Node(Tag tag, Mark startMark, Mark endMark)
  {
    setTag(tag);
    this.startMark = startMark;
    this.endMark = endMark;
    type = Object.class;
    twoStepsConstruction = false;
    resolved = true;
    useClassConstructor = null;
  }
  






  public Tag getTag()
  {
    return tag;
  }
  
  public Mark getEndMark() {
    return endMark;
  }
  



  public abstract NodeId getNodeId();
  


  public Mark getStartMark()
  {
    return startMark;
  }
  
  public void setTag(Tag tag) {
    if (tag == null) {
      throw new NullPointerException("tag in a Node is required.");
    }
    this.tag = tag;
  }
  



  public final boolean equals(Object obj)
  {
    return super.equals(obj);
  }
  
  public Class<? extends Object> getType() {
    return type;
  }
  
  public void setType(Class<? extends Object> type) {
    if (!type.isAssignableFrom(this.type)) {
      this.type = type;
    }
  }
  
  public void setTwoStepsConstruction(boolean twoStepsConstruction) {
    this.twoStepsConstruction = twoStepsConstruction;
  }
  
















  public boolean isTwoStepsConstruction()
  {
    return twoStepsConstruction;
  }
  
  public final int hashCode()
  {
    return super.hashCode();
  }
  
  public boolean useClassConstructor() {
    if (useClassConstructor == null) {
      if ((!tag.isSecondary()) && (isResolved()) && (!Object.class.equals(type)) && (!tag.equals(Tag.NULL)))
      {
        return true; }
      if (tag.isCompatible(getType()))
      {

        return true;
      }
      return false;
    }
    
    return useClassConstructor.booleanValue();
  }
  
  public void setUseClassConstructor(Boolean useClassConstructor) {
    this.useClassConstructor = useClassConstructor;
  }
  





  public boolean isResolved()
  {
    return resolved;
  }
}
