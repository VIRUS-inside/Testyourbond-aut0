package javax.xml.stream.events;

public abstract interface EntityReference
  extends XMLEvent
{
  public abstract EntityDeclaration getDeclaration();
  
  public abstract String getName();
}
