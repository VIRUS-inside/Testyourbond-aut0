package org.yaml.snakeyaml.events;

import org.yaml.snakeyaml.error.Mark;















public abstract class Event
{
  private final Mark startMark;
  private final Mark endMark;
  
  public static enum ID
  {
    Alias,  DocumentEnd,  DocumentStart,  MappingEnd,  MappingStart,  Scalar,  SequenceEnd,  SequenceStart,  StreamEnd,  StreamStart;
    
    private ID() {}
  }
  
  public Event(Mark startMark, Mark endMark)
  {
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
  


  public abstract boolean is(ID paramID);
  

  public boolean equals(Object obj)
  {
    if ((obj instanceof Event)) {
      return toString().equals(obj.toString());
    }
    return false;
  }
  




  public int hashCode()
  {
    return toString().hashCode();
  }
}
