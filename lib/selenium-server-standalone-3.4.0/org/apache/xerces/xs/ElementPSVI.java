package org.apache.xerces.xs;

public abstract interface ElementPSVI
  extends ItemPSVI
{
  public abstract XSElementDeclaration getElementDeclaration();
  
  public abstract XSNotationDeclaration getNotation();
  
  public abstract boolean getNil();
  
  public abstract XSModel getSchemaInformation();
}
