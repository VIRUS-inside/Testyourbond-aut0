package org.yaml.snakeyaml.events;

import org.yaml.snakeyaml.error.Mark;






















public final class ScalarEvent
  extends NodeEvent
{
  private final String tag;
  private final Character style;
  private final String value;
  private final ImplicitTuple implicit;
  
  public ScalarEvent(String anchor, String tag, ImplicitTuple implicit, String value, Mark startMark, Mark endMark, Character style)
  {
    super(anchor, startMark, endMark);
    this.tag = tag;
    this.implicit = implicit;
    this.value = value;
    this.style = style;
  }
  





  public String getTag()
  {
    return tag;
  }
  


















  public Character getStyle()
  {
    return style;
  }
  







  public String getValue()
  {
    return value;
  }
  
  public ImplicitTuple getImplicit() {
    return implicit;
  }
  
  protected String getArguments()
  {
    return super.getArguments() + ", tag=" + tag + ", " + implicit + ", value=" + value;
  }
  
  public boolean is(Event.ID id)
  {
    return Event.ID.Scalar == id;
  }
}
