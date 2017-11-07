package org.apache.xerces.xs;

public abstract interface XSValue
{
  public abstract String getNormalizedValue();
  
  public abstract Object getActualValue();
  
  public abstract XSSimpleTypeDefinition getTypeDefinition();
  
  public abstract XSSimpleTypeDefinition getMemberTypeDefinition();
  
  public abstract XSObjectList getMemberTypeDefinitions();
  
  public abstract short getActualValueType();
  
  public abstract ShortList getListValueTypes();
}
