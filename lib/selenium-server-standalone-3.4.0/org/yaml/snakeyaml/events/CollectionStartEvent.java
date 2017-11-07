package org.yaml.snakeyaml.events;

import org.yaml.snakeyaml.error.Mark;




















public abstract class CollectionStartEvent
  extends NodeEvent
{
  private final String tag;
  private final boolean implicit;
  private final Boolean flowStyle;
  
  public CollectionStartEvent(String anchor, String tag, boolean implicit, Mark startMark, Mark endMark, Boolean flowStyle)
  {
    super(anchor, startMark, endMark);
    this.tag = tag;
    this.implicit = implicit;
    this.flowStyle = flowStyle;
  }
  





  public String getTag()
  {
    return tag;
  }
  





  public boolean getImplicit()
  {
    return implicit;
  }
  





  public Boolean getFlowStyle()
  {
    return flowStyle;
  }
  
  protected String getArguments()
  {
    return super.getArguments() + ", tag=" + tag + ", implicit=" + implicit;
  }
}
