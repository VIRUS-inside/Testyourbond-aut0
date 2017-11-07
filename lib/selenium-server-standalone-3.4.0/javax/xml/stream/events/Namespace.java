package javax.xml.stream.events;

public abstract interface Namespace
  extends Attribute
{
  public abstract String getNamespaceURI();
  
  public abstract String getPrefix();
  
  public abstract boolean isDefaultNamespaceDeclaration();
}
