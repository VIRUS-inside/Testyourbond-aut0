package org.yaml.snakeyaml.tokens;

import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.error.YAMLException;











public abstract class Token
{
  private final Mark startMark;
  private final Mark endMark;
  
  public static enum ID
  {
    Alias,  Anchor,  BlockEnd,  BlockEntry,  BlockMappingStart,  BlockSequenceStart,  Directive,  DocumentEnd,  DocumentStart,  FlowEntry,  FlowMappingEnd,  FlowMappingStart,  FlowSequenceEnd,  FlowSequenceStart,  Key,  Scalar,  StreamEnd,  StreamStart,  Tag,  Value;
    
    private ID() {}
  }
  
  public Token(Mark startMark, Mark endMark)
  {
    if ((startMark == null) || (endMark == null)) {
      throw new YAMLException("Token requires marks.");
    }
    this.startMark = startMark;
    this.endMark = endMark;
  }
  
  public String toString() {
    return "<" + getClass().getName() + "(" + getArguments() + ")>";
  }
  
  public Mark getStartMark() {
    return startMark;
  }
  
  public Mark getEndMark() {
    return endMark;
  }
  


  protected String getArguments()
  {
    return "";
  }
  




  public abstract ID getTokenId();
  




  public boolean equals(Object obj)
  {
    if ((obj instanceof Token)) {
      return toString().equals(obj.toString());
    }
    return false;
  }
  




  public int hashCode()
  {
    return toString().hashCode();
  }
}
