package javax.xml.stream.events;

public abstract interface ProcessingInstruction
  extends XMLEvent
{
  public abstract String getData();
  
  public abstract String getTarget();
}
