package javax.xml.stream.events;

public abstract interface EntityDeclaration
  extends XMLEvent
{
  public abstract String getBaseURI();
  
  public abstract String getName();
  
  public abstract String getNotationName();
  
  public abstract String getPublicId();
  
  public abstract String getReplacementText();
  
  public abstract String getSystemId();
}
