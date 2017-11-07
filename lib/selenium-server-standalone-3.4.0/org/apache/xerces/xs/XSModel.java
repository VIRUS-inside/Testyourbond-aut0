package org.apache.xerces.xs;

public abstract interface XSModel
{
  public abstract StringList getNamespaces();
  
  public abstract XSNamespaceItemList getNamespaceItems();
  
  public abstract XSNamedMap getComponents(short paramShort);
  
  public abstract XSNamedMap getComponentsByNamespace(short paramShort, String paramString);
  
  public abstract XSObjectList getAnnotations();
  
  public abstract XSElementDeclaration getElementDeclaration(String paramString1, String paramString2);
  
  public abstract XSAttributeDeclaration getAttributeDeclaration(String paramString1, String paramString2);
  
  public abstract XSTypeDefinition getTypeDefinition(String paramString1, String paramString2);
  
  public abstract XSAttributeGroupDefinition getAttributeGroup(String paramString1, String paramString2);
  
  public abstract XSModelGroupDefinition getModelGroupDefinition(String paramString1, String paramString2);
  
  public abstract XSNotationDeclaration getNotationDeclaration(String paramString1, String paramString2);
  
  public abstract XSIDCDefinition getIDCDefinition(String paramString1, String paramString2);
  
  public abstract XSObjectList getSubstitutionGroup(XSElementDeclaration paramXSElementDeclaration);
}
