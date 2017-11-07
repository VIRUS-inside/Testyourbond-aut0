package org.apache.xerces.xs;

import org.apache.xerces.xs.datatypes.ObjectList;

public abstract interface XSMultiValueFacet
  extends XSObject
{
  public abstract short getFacetKind();
  
  public abstract StringList getLexicalFacetValues();
  
  public abstract ObjectList getEnumerationValues();
  
  public abstract XSObjectList getAnnotations();
}
