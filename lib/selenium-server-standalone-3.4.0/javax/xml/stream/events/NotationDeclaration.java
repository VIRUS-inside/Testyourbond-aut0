package javax.xml.stream.events;

public abstract interface NotationDeclaration
  extends XMLEvent
{
  public abstract String getName();
  
  public abstract String getPublicId();
  
  public abstract String getSystemId();
}
